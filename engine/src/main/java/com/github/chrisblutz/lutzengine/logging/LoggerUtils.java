package com.github.chrisblutz.lutzengine.logging;

import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;


/**
 * @author Christopher Lutz
 */
public class LoggerUtils {
    
    public static final String LOG_FILE = "logs/engine.log";
    
    private static Handler sysOutHandler = null, fileHandler = null;
    
    public static Handler getSysOutHandler() {
        
        if (sysOutHandler == null) {
            
            sysOutHandler = new ConsoleHandler();
            sysOutHandler.setFormatter(new LogFormatter());
            sysOutHandler.setLevel(Level.ALL);
        }
        
        return sysOutHandler;
    }
    
    public static Handler getFileHandler(String file) throws IOException {
        
        if (fileHandler == null) {
            
            new File("logs").mkdir();
            
            fileHandler = new FileHandler(file);
            fileHandler.setFormatter(new LogFormatter());
            fileHandler.setLevel(Level.ALL);
        }
        
        return fileHandler;
    }
}
