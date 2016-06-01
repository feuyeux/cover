package com.mine.config;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
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

public class CasUserDetailsService implements AuthenticationUserDetailsService {
    private static final Logger logger = LogManager.getLogger(CasUserDetailsService.class);

    @Override
    public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Map<String, Object> attributes = ((CasAssertionAuthenticationToken) token).getAssertion().getPrincipal().getAttributes();
        String au = (String) attributes.get("authorities");
        if (au != null) {
            String[] aus =au.split(",");
            for(String au0:aus){
                authorities.add(new SimpleGrantedAuthority(au0));
            }
        }

        logger.info("authorites:\t" + attributes.get("authorities"));
        logger.info("allow:\t" + attributes.get("ruleallow"));
        logger.info("deny:\t" + attributes.get("ruledeny"));
        return new User(token.getName(), "", authorities);
    }
}