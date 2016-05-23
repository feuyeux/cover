package com.mine.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * No customizations of {@link AbstractSecurityWebApplicationInitializer} are necessary.
 *
 * @author Rob Winch
 */
public class SecurityWebApplicationInitializer extends
        AbstractSecurityWebApplicationInitializer {

    public SecurityWebApplicationInitializer() {
        super(SecurityConfig.class);
    }
}
