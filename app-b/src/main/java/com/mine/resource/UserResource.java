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
    @Path("users")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AppUser> appUsers() {
        AppUser appUser = new AppUser(1, "张三b-1");
        AppUser appUser2 = new AppUser(2, "李四b-1");
        List<AppUser> list = new ArrayList<>();
        list.add(appUser);
        list.add(appUser2);
        return list;
    }
}
