package com.nsc.AmoskiUser.client;


import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class UsernamePasswordTokenSerializable implements HostAuthenticationToken, RememberMeAuthenticationToken, Serializable {
    private static Logger logger = LoggerFactory.getLogger(UsernamePasswordTokenSerializable.class);
    private String username;
    private char[] password;
    private boolean rememberMe;
    private String host;

    public UsernamePasswordTokenSerializable() {
        this.rememberMe = false;
    }

    public UsernamePasswordTokenSerializable(String username, char[] password) {
        this(username, (char[])password, false, (String)null);
    }

    public UsernamePasswordTokenSerializable(String username, String password) {
        this(username, (char[])(password != null ? password.toCharArray() : null), false, (String)null);
    }

    public UsernamePasswordTokenSerializable(String username, char[] password, String host) {
        this(username, password, false, host);
    }

    public UsernamePasswordTokenSerializable(String username, String password, String host) {
        this(username, password != null ? password.toCharArray() : null, false, host);
    }

    public UsernamePasswordTokenSerializable(String username, char[] password, boolean rememberMe) {
        this(username, (char[])password, rememberMe, (String)null);
    }

    public UsernamePasswordTokenSerializable(String username, String password, boolean rememberMe) {
        this(username, (char[])(password != null ? password.toCharArray() : null), rememberMe, (String)null);
    }

    public UsernamePasswordTokenSerializable(String username, char[] password, boolean rememberMe, String host) {
        this.rememberMe = false;
        this.username = username;
        this.password = password;
        this.rememberMe = rememberMe;
        this.host = host;
    }

    public UsernamePasswordTokenSerializable(String username, String password, boolean rememberMe, String host) {
        this(username, password != null ? password.toCharArray() : null, rememberMe, host);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char[] getPassword() {
        return this.password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public Object getPrincipal() {
        return this.getUsername();
    }

    public Object getCredentials() {
        return this.getPassword();
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isRememberMe() {
        return this.rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public void clear() {
        this.username = null;
        this.host = null;
        this.rememberMe = false;
        if (this.password != null) {
            for(int i = 0; i < this.password.length; ++i) {
                this.password[i] = 0;
            }

            this.password = null;
        }

    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getName());
        sb.append(" - ");
        sb.append(this.username);
        sb.append(", rememberMe=").append(this.rememberMe);
        if (this.host != null) {
            sb.append(" (").append(this.host).append(")");
        }

        return sb.toString();
    }
}
