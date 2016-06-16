package com.mine.resource;

import com.mine.domain.AppUser;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Path("/")
public class ApiResource {
    private static final Logger logger = LogManager.getLogger(ApiResource.class);
    private static final GrantedAuthority ROLE_A = new SimpleGrantedAuthority("ROLE_A");
    private static final GrantedAuthority ROLE_B = new SimpleGrantedAuthority("ROLE_B");
    @Context
    private HttpServletRequest httpRequest;
    @Context
    private Environment env;
    @Autowired
    private DiscoveryClient discovery;

    private AtomicInteger counter = new AtomicInteger();

    @GET
    @Path("health")
    @Produces(MediaType.APPLICATION_JSON)
    public String getHealth() {
        return "Health: OK";
    }

    /**
     * https://localhost:8011/rest/users
     *
     * @return
     */
    @GET
    @Path("rest/users")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AppUser> appAUsers() {
        List<AppUser> result = new ArrayList<>();
        /*授权处理*/
        CasAuthenticationToken casAuthenticationToken = (CasAuthenticationToken) httpRequest.getUserPrincipal();
        Map<String, Object> map = getAttributes();
        Collection<? extends GrantedAuthority> authorities = casAuthenticationToken.getUserDetails().getAuthorities();
        String allow = (String) map.get("ruleallow");
        String deny = (String) map.get("ruledeny");

        /*服务实例处理*/
        List<ServiceInstance> appAInstances = getAppInstaces("app-a");
        List<ServiceInstance> appBInstances = getAppInstaces("app-b");

        /*轮询访问app */
        int i = Math.abs(counter.incrementAndGet());
        int indexA = i % appAInstances.size();
        int indexB = i % appBInstances.size();

        /*请求处理*/
        List<AppUser> appAUserList = null;
        List<AppUser> appBUserList = null;
        if (!appAInstances.isEmpty() && authorities.contains(ROLE_A)) {
            URI appAUri = appAInstances.get(indexA).getUri();
            appAUserList = getAppUsers(appAUri);
        }
        if (!appBInstances.isEmpty() && authorities.contains(ROLE_B)) {
            URI appBUri = appBInstances.get(indexB).getUri();
            appBUserList = getAppUsers(appBUri);
        }

        /*跨业务域请求处理*/
        try {
            URI remoteAppAInstanceUri = URI.create(env.getProperty("app-a"));
            appAUserList.addAll(getAppUsers(remoteAppAInstanceUri));
        } catch (Exception ignored) {
        }

        /*数据处理*/
        List<AppUser> retList = getListByRule(allow, deny, appAUserList);
        if ((retList != null) && (!retList.isEmpty())) {
            result.addAll(retList);
        }

        retList = getListByRule(allow, deny, appBUserList);
        if ((retList != null) && (!retList.isEmpty())) {
            result.addAll(retList);
        }

        return result;
    }

    List<AppUser> getListByRule(String allow, String deny, List<AppUser> appList) {
        List<AppUser> retList = new ArrayList<>();
        if (appList == null) {
            return null;
        }

        if (allow != null) {
            if (allow.contains("ALL")) {
                retList.addAll(appList);
            } else {
                String[] allowList = {allow};
                if (allow.contains(",")) {
                    allowList = allow.split(",");
                }
                for (int i = 0; i < appList.size(); i++) {
                    AppUser appUser = appList.get(i);
                    for (int j = 0; j < allowList.length; j++) {
                        if (appUser.toString().contains(allowList[j])) {
                            retList.add(appUser);
                        }
                    }
                }
            }
        }

        if (deny != null) {
            String[] denyList = {deny};
            if (deny.contains(",")) {
                denyList = deny.split(",");
            }

            for (int i = 0; i < appList.size(); i++) {
                AppUser appUser = appList.get(i);
                for (int j = 0; j < denyList.length; j++) {
                    if (appUser.toString().contains(denyList[j])) {
                        retList.remove(appUser);
                    }
                }
            }
        }

        return retList;
    }

    private List<AppUser> getAppUsers(URI appAUri) {
        Client c = ClientBuilder.newClient();
        WebTarget target = c.target(appAUri).path("users");
        Invocation.Builder request = target.request(MediaType.APPLICATION_JSON_TYPE);
        Response response = request.get();
        //AppUser appUser = response.readEntity(AppUser.class);
        GenericType<List<AppUser>> genericType = new GenericType<List<AppUser>>() {
        };
        return request.get(genericType);
    }

    private List<ServiceInstance> getAppInstaces(String appName) {
        return discovery.getInstances(appName);
    }

    public Map<String, Object> getAttributes() {
        try {
            CasAuthenticationToken casAuthenticationToken = (CasAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            AttributePrincipal principal = casAuthenticationToken.getAssertion().getPrincipal();
            return principal.getAttributes();
        } catch (Exception e) {
            logger.error("Fail to get authentication info", e);
            return null;
        }
    }
}