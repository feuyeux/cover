package com.mine.resource;

import com.mine.domain.AppUser;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
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
import java.util.List;
import java.util.Map;

@Component
@Path("/")
public class ApiResource {
    @Context
    private HttpServletRequest httpRequest;

    @Autowired
    private DiscoveryClient discovery;

    @GET
    @Path("health")
    @Produces(MediaType.APPLICATION_JSON)
    public String getHealth() {
        return "Health: OK";
    }


    @GET
    @Path("users")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AppUser> appAUsers() {
        List<AppUser> result = new ArrayList<>();
        /*授权处理*/
        CasAuthenticationToken casAuthenticationToken = (CasAuthenticationToken) httpRequest.getUserPrincipal();
        Map<String, Object> map = getAttributes();

        /*服务实例处理*/
        List<ServiceInstance> appAInstances = getAppInstaces("app-a");
        List<ServiceInstance> appBInstances = getAppInstaces("app-b");

        /*请求处理*/
        URI appAUri = appAInstances.get(0).getUri();
        List<AppUser> appAUserList = getAppUsers(appAUri);
        URI appBUri = appBInstances.get(0).getUri();
        List<AppUser> appBUserList = getAppUsers(appBUri);

        /*数据处理*/
        result.addAll(appAUserList);
        result.addAll(appBUserList);
        return result;
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
            return null;
        }
    }
}