package com.github.rabitarochan.jgit_proxy_clone;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

/**
 * Config class.
 */
public class Config {

    private String remoteUrl;
    private String localPath;
    
    private String proxyHost;
    private int proxyPort;
    private boolean isNoProxy;
    
    /**
     * Initialize config instance.
     */
    public Config() {
        this.remoteUrl = "";
        this.localPath = "";
        this.proxyHost = "";
        this.proxyPort = -1;
        this.isNoProxy = true;
    }
    
    /**
     * Get remote url value.
     * @return remoteUrl remote url value.
     */
    public String getRemoteUrl() {
        return this.remoteUrl;
    }
    
    /**
     * Get local path value.
     * @return localPath local path value.
     */
    public String getLocalPath() {
        return this.localPath;
    }
    
    /**
     * Get proxy instance.
     * @return Proxy instance.
     */
    public Proxy getProxy() {
        if (this.isNoProxy) {
            return Proxy.NO_PROXY;
        } else {
            InetSocketAddress socket =
                    InetSocketAddress.createUnresolved(this.proxyHost, this.proxyPort);
            return new Proxy(Proxy.Type.HTTP, socket);
        }
    }
    
    /**
     * Get proxy server address.
     * @return proxy server address value. if not use proxy, then return empty value.
     */
    public String getProxyAddress() {
    	SocketAddress address = this.getProxy().address();
    	if (address == null) {
    		return "";
    	} else {
    		return address.toString();
    	}
    }
    
    
    /**
     * Set remote url value.
     * @param remoteUrl remote url value.
     */
    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
        
        String[] splittedUrl = this.remoteUrl.split("/");
        String directory = splittedUrl[splittedUrl.length - 1];
        
        if (directory.endsWith(".git")) {
            this.localPath = directory.substring(0, directory.length() - 4);
        } else {
            this.localPath = directory;
        }
    }
    
    /**
     * Set local path value.
     * @param localPath local path value.
     */
    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
    
    /**
     * Set proxy host and proxy port value.
     * @param proxyHost proxy host value.
     * @param proxyPort proxy port value.
     */
    public void setProxy(String proxyHost, int proxyPort) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.isNoProxy = false;
    }
    
}
