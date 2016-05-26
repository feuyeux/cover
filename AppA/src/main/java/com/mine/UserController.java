package com.mine;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
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
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Context
    HttpServletRequest httpRequest;

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> views(@QueryParam("id") @DefaultValue("5") int id, @QueryParam("inputName") @RequestParam(required = false) String name) {
        System.out.println(buildHead());

        ArrayList<User> list = new ArrayList();
        User user = new User();
        user.setId(id);
        user.setName("zhang");

        User u2 = new User(id + 1, "wang");

        list.add(user);
        list.add(u2);

        if (name != null) {
            User u3 = new User(id + 2, name);
            ArrayList<String> sList = new ArrayList<>();
            sList.add("one");
            sList.add("two");
            u3.setList(sList);
            list.add(u3);
        }

        return list;
    }



    @GET
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    public User view(@QueryParam("id") int id, @QueryParam("name") String name) {
        Client c = ClientBuilder.newClient();
        WebTarget target = c.target("https://localhost:8013/user").queryParam("id", id);

        Invocation.Builder request = target.request(MediaType.APPLICATION_JSON_TYPE);
        request.header("msPrincipal",httpRequest.getUserPrincipal());
        Response response = request.get();
        User user = response.readEntity(User.class);
        return user;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String test(){

        System.out.println(buildHead());
        return "Hi. Hi. Hi";
    }

    public Map<String ,Object> buildHead() {
        CasAuthenticationToken casAuthenticationToken = (CasAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        AttributePrincipal principal = casAuthenticationToken.getAssertion().getPrincipal();
        return principal.getAttributes();
    }

}
