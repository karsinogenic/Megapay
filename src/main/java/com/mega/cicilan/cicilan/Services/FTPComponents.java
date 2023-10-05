package com.mega.cicilan.cicilan.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FTPComponents {

    @Value("${ftp.url}")
    private String ftp_url;

    @Value("${ftp.port}")
    private Integer ftp_port;

    @Value("${ftp.username}")
    private String ftp_username;

    @Value("${ftp.password}")
    private String ftp_password;

    @Value("${ftp.remote.directory}")
    private String ftp_dir;

    @Value("${loc.url}")
    private String loc_url;

    @Value("${shopee.url}")
    private String shopee_url;

    @Value("${proxy.host}")
    private String proxy_host;

    @Value("${proxy.port}")
    private Integer proxy_port;

    public String getProxy_host() {
        return proxy_host;
    }

    public void setProxy_host(String proxy_host) {
        this.proxy_host = proxy_host;
    }

    public Integer getProxy_port() {
        return proxy_port;
    }

    public void setProxy_port(Integer proxy_port) {
        this.proxy_port = proxy_port;
    }

    public String getLoc_url() {
        return loc_url;
    }

    public void setLoc_url(String loc_url) {
        this.loc_url = loc_url;
    }

    public String getFtp_url() {
        return ftp_url;
    }

    public void setFtp_url(String ftp_url) {
        this.ftp_url = ftp_url;
    }

    public Integer getFtp_port() {
        return ftp_port;
    }

    public void setFtp_port(Integer ftp_port) {
        this.ftp_port = ftp_port;
    }

    public String getFtp_username() {
        return ftp_username;
    }

    public void setFtp_username(String ftp_username) {
        this.ftp_username = ftp_username;
    }

    public String getFtp_password() {
        return ftp_password;
    }

    public void setFtp_password(String ftp_password) {
        this.ftp_password = ftp_password;
    }

    public String getFtp_dir() {
        return ftp_dir;
    }

    public void setFtp_dir(String ftp_dir) {
        this.ftp_dir = ftp_dir;
    }

}
