package com.mine.config;

import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyCasAuthenticationUserDetailsService implements AuthenticationUserDetailsService {
    @Override
    public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Map<String, Object> attributes = ((CasAssertionAuthenticationToken) token).getAssertion().getPrincipal().getAttributes();
        String au = (String) attributes.get("authorities");
        if (au != null) {
            authorities.add(new SimpleGrantedAuthority(au));
        }

        System.out.println("authorites:\t" + attributes.get("authorities"));
        System.out.println("allow:\t" + attributes.get("ruleallow"));
        System.out.println("deny:\t" + attributes.get("ruledeny"));

        User user = new User(token.getName(), "", authorities);
        System.out.println(user.toString() + "+" + token.getCredentials() + "+" + token.getDetails() + "+" + token.getPrincipal());
        return user;
    }

}