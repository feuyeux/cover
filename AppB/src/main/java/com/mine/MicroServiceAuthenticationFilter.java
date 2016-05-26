package com.mine;

/**
 * Created by zhangchangjun on 2016/5/26.
 */
import org.jasig.cas.client.proxy.ProxyGrantingTicketStorage;
import org.jasig.cas.client.util.CommonUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class MicroServiceAuthenticationFilter extends CasAuthenticationFilter {
    private RequestMatcher proxyReceptorMatcher;
    private ProxyGrantingTicketStorage proxyGrantingTicketStorage;
    private String artifactParameter = "ticket";
    private boolean authenticateAllArtifacts;
    private AuthenticationFailureHandler proxyFailureHandler = new SimpleUrlAuthenticationFailureHandler();

    public MicroServiceAuthenticationFilter() {

        this.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler());
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        System.out.println(request.getHeader("msPrincipal"));

        if(this.proxyReceptorRequest(request)) {
            this.logger.debug("Responding to proxy receptor request");
            CommonUtils.readAndRespondToProxyReceptorRequest(request, response, this.proxyGrantingTicketStorage);
            return null;
        } else {
            boolean serviceTicketRequest = this.serviceTicketRequest(request, response);
            String username = serviceTicketRequest?"_cas_stateful_":"_cas_stateless_";
            String password = this.obtainArtifact(request);
            if(password == null) {
                this.logger.debug("Failed to obtain an artifact (cas ticket)");
                password = "";
            }

            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

    protected String obtainArtifact(HttpServletRequest request) {
        return request.getParameter(this.artifactParameter);
    }

    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {

        boolean serviceTicketRequest = this.serviceTicketRequest(request, response);
        boolean result = serviceTicketRequest || this.proxyReceptorRequest(request) || this.proxyTicketRequest(serviceTicketRequest, request);
        if(this.logger.isDebugEnabled()) {
            this.logger.debug("requiresAuthentication = " + result);
        }

        return result;
    }

    private boolean serviceTicketRequest(HttpServletRequest request, HttpServletResponse response) {
        boolean result = super.requiresAuthentication(request, response);
        if(this.logger.isDebugEnabled()) {
            this.logger.debug("serviceTicketRequest = " + result);
        }

        return result;
    }

    private boolean proxyTicketRequest(boolean serviceTicketRequest, HttpServletRequest request) {
        if(serviceTicketRequest) {
            return false;
        } else {
            boolean result = this.authenticateAllArtifacts && this.obtainArtifact(request) != null && !this.authenticated();
            if(this.logger.isDebugEnabled()) {
                this.logger.debug("proxyTicketRequest = " + result);
            }

            return result;
        }
    }

    private boolean authenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }

    private boolean proxyReceptorRequest(HttpServletRequest request) {
        boolean result = this.proxyReceptorConfigured() && this.proxyReceptorMatcher.matches(request);
        if(this.logger.isDebugEnabled()) {
            this.logger.debug("proxyReceptorRequest = " + result);
        }

        return result;
    }

    private boolean proxyReceptorConfigured() {
        boolean result = this.proxyGrantingTicketStorage != null && this.proxyReceptorMatcher != null;
        if(this.logger.isDebugEnabled()) {
            this.logger.debug("proxyReceptorConfigured = " + result);
        }

        return result;
    }
}
