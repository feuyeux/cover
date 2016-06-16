package com.mine.resource;

import com.mine.domain.AppUser;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Component
@Path("/")
public class UserResource {
    @Context
    private HttpServletRequest httpRequest;

    @GET
    @Path("users")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AppUser> appUsers() {
        AppUser appUser = new AppUser(1, "张三a-1");
        AppUser appUser2 = new AppUser(2, "李四a-1");
        List<AppUser> list =new ArrayList<>();
        list.add(appUser);
        list.add(appUser2);
        return list;
    }
}
