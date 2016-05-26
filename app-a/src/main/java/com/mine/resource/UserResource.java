package com.mine.resource;

import com.mine.domain.AppUser;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Path("/")
public class UserResource {
    @Context
    private HttpServletRequest httpRequest;
    @Value("${appB.server}")
    private String appB;

    @GET
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    public AppUser view(@QueryParam("id") int id, @QueryParam("name") String name) {
        CasAuthenticationToken casAuthenticationToken = (CasAuthenticationToken) httpRequest.getUserPrincipal();
        Client c = ClientBuilder.newClient();
        WebTarget target = c.target(appB).path("user").queryParam("id", id);
        Invocation.Builder request = target.request(MediaType.APPLICATION_JSON_TYPE);
        Response response = request.get();
        AppUser appUser = response.readEntity(AppUser.class);
        return appUser;
    }

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AppUser> views(@QueryParam("id") @DefaultValue("5") int id, @QueryParam("inputName") @RequestParam(required = false) String name) {
        System.out.println(buildHead());

        ArrayList<AppUser> list = new ArrayList();
        AppUser appUser = new AppUser();
        appUser.setId(id);
        appUser.setName("zhang");

        AppUser u2 = new AppUser(id + 1, "wang");

        list.add(appUser);
        list.add(u2);

        if (name != null) {
            AppUser u3 = new AppUser(id + 2, name);
            ArrayList<String> sList = new ArrayList<>();
            sList.add("one");
            sList.add("two");
            u3.setList(sList);
            list.add(u3);
        }
        return list;
    }

    @GET
    @Path("/hi")
    public String hi() {
        System.out.println(buildHead());
        return "Hi. Hi. Hi";
    }

    public Map<String, Object> buildHead() {
        CasAuthenticationToken casAuthenticationToken = (CasAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        AttributePrincipal principal = casAuthenticationToken.getAssertion().getPrincipal();
        return principal.getAttributes();
    }
}
