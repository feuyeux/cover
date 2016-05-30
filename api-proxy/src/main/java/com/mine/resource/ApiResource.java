package com.mine.resource;

import com.mine.domain.AppUser;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

@Component
@Path("/")
public class ApiResource {
    @Context
    private HttpServletRequest httpRequest;

    @Autowired
    private DiscoveryClient discovery;

    @GET
    @Path("users")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AppUser> appAUsers() {
        /*授权处理*/
        CasAuthenticationToken casAuthenticationToken = (CasAuthenticationToken) httpRequest.getUserPrincipal();
        Map<String, Object> map =  getAttributes();

        /*服务实例处理*/
        List<ServiceInstance> appAInstances = getAppInstaces("app-a");
        List<ServiceInstance> appBInstances = getAppInstaces("app-b");

        /*请求处理*/
        Client c = ClientBuilder.newClient();
        WebTarget target = c.target(appAInstances.get(0).getUri()).path("users");
        Invocation.Builder request = target.request(MediaType.APPLICATION_JSON_TYPE);
        Response response = request.get();
        //AppUser appUser = response.readEntity(AppUser.class);
        GenericType<List<AppUser>> genericType = new GenericType<List<AppUser>>() {};
        List<AppUser> users = request.get(genericType);

        /*数据处理*/

        return users;
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