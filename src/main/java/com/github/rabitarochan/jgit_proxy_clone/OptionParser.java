package com.github.rabitarochan.jgit_proxy_clone;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

/**
 * OptionParser class.
 */
public class OptionParser {

    static Options OPTS;
    static Option OPT_PROXY_HOST;
    static Option OPT_PROXY_PORT;
        
    /**
     * Initialize commons-cli Option, Options instances.
     */
    static {
        OPT_PROXY_HOST = new Option("h", "host", true, "");
        OPT_PROXY_HOST.setRequired(false);
        
        OPT_PROXY_PORT = new Option("p", "port", true, "");
        OPT_PROXY_PORT.setRequired(false);
        
        OPTS = new Options();
        OPTS.addOption(OPT_PROXY_HOST);
        OPTS.addOption(OPT_PROXY_PORT);
    }
    
    
    private CommandLineParser parser;

    /**
     * Initialize OptionParser instance.
     */
    public OptionParser() {
        this.parser = new PosixParser();
    }
    
    /**
     * Show help.
     */
    public static void printHelp() {
        // TODO 
    }
    
    /**
     * Show help with message.
     * @param message
     */
    public static void printHelp(String message) {
        println("message: " + message);
        println("========================================");
        printHelp();
    }
    
    private static void println(String v) {
        System.out.println(v);
    }
    
    
    /**
     * Parse commandline arguments to Config.
     * @param args Commandline arguments.
     * @return Config instance.
     */
    public Config parseArgs(String[] args) {
        try {
            CommandLine cmdLine = this.parser.parse(OPTS, args);
            
            if (!validCommandline(cmdLine)) {
                printHelp();
            }
            
            return createConfig(cmdLine);
        } catch (Exception e) {
            printHelp(e.getMessage());
            return null;
        }
    }
    
    /**
     * Checks is valid commandline arguments.
     * @param cmdLine parsed commandline arguments.
     * @return if valid arguments then 'true', otherwise 'false'.
     */
    private boolean validCommandline(CommandLine cmdLine) {
        String[] args = cmdLine.getArgs();
        if (args.length != 1 && args.length != 2) {
            return false;
        }
        
        if (!isUseProxyOptions(cmdLine) && !isNotUseProxyOptions(cmdLine)) {
            return false;
        }
        
        String portString = cmdLine.getOptionValue(OPT_PROXY_PORT.getOpt());
        if (!isNumber(portString)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Create Config instalce from commandline arguments.
     * @param cmdLine Commandline arguments.
     * @return Config instance.
     */
    private Config createConfig(CommandLine cmdLine) {
        Config config = new Config();
        
        String[] args = cmdLine.getArgs();
        config.setRemoteUrl(args[0]);
        if (args.length == 2) {
            config.setLocalPath(args[1]);
        }
        
        if (isUseProxyOptions(cmdLine)) {
            String proxyHost = cmdLine.getOptionValue(OPT_PROXY_HOST.getOpt());
            String proxyPortString = cmdLine.getOptionValue(OPT_PROXY_PORT.getOpt());
            int proxyPort = Integer.parseInt(proxyPortString);
            
            config.setProxy(proxyHost, proxyPort);
        }
        
        return config;
    }
    
    private boolean isUseProxyOptions(CommandLine cmdLine) {
        boolean hasHost = cmdLine.hasOption(OPT_PROXY_HOST.getOpt());
        boolean hasPort = cmdLine.hasOption(OPT_PROXY_PORT.getOpt());
        
        return (hasHost && hasPort);
    }

    private boolean isNotUseProxyOptions(CommandLine cmdLine) {
        boolean hasHost = cmdLine.hasOption(OPT_PROXY_HOST.getOpt());
        boolean hasPort = cmdLine.hasOption(OPT_PROXY_PORT.getOpt());
        
        return (!hasHost && !hasPort);
    }
    
    private boolean isNumber(String v) {
        try {
            Integer.parseInt(v);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
