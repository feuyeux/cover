package com.mine.resource;

import com.mine.domain.AppUser;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Path("/")
public class UserResource {
    @Context
    private HttpServletRequest httpRequest;

    @GET
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    public AppUser view(@QueryParam("id") int id, @QueryParam("name") String name) {
        return new AppUser(id, name == null ? "who-pa-who" : name);
    }

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AppUser> views(@QueryParam("id") @DefaultValue("5") int id, @QueryParam("inputName") @RequestParam(required = false) String name) {
//        System.out.println(buildHead());

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
//        System.out.println(buildHead());
        return "Hi. Hi. Hi";
    }

//    public Map<String, Object> buildHead() {
//        CasAuthenticationToken casAuthenticationToken = (CasAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
//        AttributePrincipal principal = casAuthenticationToken.getAssertion().getPrincipal();
//        return principal.getAttributes();
//    }
}
