package com.github.rabitarochan.jgit_proxy_clone;

import java.io.File;
import java.io.IOException;
import java.net.ProxySelector;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.StoredConfig;

public class Main {

    private final Config config;
    
    public Main(Config config) {
        this.config = config;
    }
    
    public static void main(String[] args) {
        OptionParser parser = new OptionParser();
        Config config = parser.parseArgs(args);
        
        Main main = new Main(config);
        try {
            main.execute();
            System.exit(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
    
    private void execute() throws InvalidRemoteException, TransportException, GitAPIException, IOException {
        setProxy();
        
        CloneCommand cmd = Git.cloneRepository()
                              .setURI(config.getRemoteUrl());
        
        if (config.getLocalPath() != "") {
            cmd.setDirectory(new File(config.getLocalPath()));
        }
        
        Git git = cmd.call();
        
        // Set proxy setting to repository config.
        StoredConfig gitConfig = git.getRepository().getConfig();
        gitConfig.setString("remote", "origin", "proxy", config.getProxyAddress());
        gitConfig.save();
        
        git.getRepository().close();
    }
    
    private void setProxy() {
    	ProxySelector.setDefault(new MyProxySelector(this.config.getProxy()));
    }
    
}
