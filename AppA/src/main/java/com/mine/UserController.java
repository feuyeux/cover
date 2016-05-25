package com.mine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Component
@Path("/")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> views(@QueryParam("id") @DefaultValue("5") int id, @QueryParam("inputName") @RequestParam(required = false) String name) {
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
        User user = new User();
        user.setId(id);
        user.setName(name);
        return user;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String test(){
        return "Hi. Hi. Hi";
    }
}
