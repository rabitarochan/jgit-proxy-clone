package com.github.rabitarochan.jgit_proxy_clone;

import java.io.File;
import java.net.ProxySelector;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

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
            main.execute(args);
            System.exit(0);
        } catch (Exception ex) {
            OptionParser.printHelp(ex.getMessage());
            System.exit(1);
        }
    }
    
    private void execute(String[] args) throws InvalidRemoteException, TransportException, GitAPIException {
        setProxy();
        
        CloneCommand cmd = Git.cloneRepository()
                              .setURI(config.getRemoteUrl());
        
        if (config.getLocalPath() != "") {
            cmd.setDirectory(new File(config.getLocalPath()));
        }
        
        cmd.call();
    }
    
    private void setProxy() {
        ProxySelector.setDefault(new MyProxySelector(this.config.getProxy()));
    }
    
}
