package com.github.chrisblutz.lutzengine.logging;

import com.github.chrisblutz.lutzengine.LutzEngine;

import java.util.logging.Logger;


/**
 * @author Christopher Lutz
 */
public class SystemLogUtils {
    
    public static void logEnvironment() {
        
        Logger l = LutzEngine.createLogger("System");
        
        l.info("========[ ENVIRONMENT INFO ]========");
        l.info("Java Version: " + System.getProperty("java.version"));
        l.info("Java Vendor: " + System.getProperty("java.vendor"));
        l.info("OS Name: " + System.getProperty("os.name"));
        l.info("OS Version: " + System.getProperty("os.version"));
        l.info("OS Architecture: " + System.getProperty("os.arch"));
        
        Runtime r = Runtime.getRuntime();
        
        l.info("# of Cores: " + r.availableProcessors());
        l.info("Total Memory Available: " + (r.maxMemory() / 1000000) + " MB (" + r.maxMemory() + " Bytes)");
        l.info("======[ END ENVIRONMENT INFO ]======");
        l.info("");
    }
}
