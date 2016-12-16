package com.github.chrisblutz.lutzengine.exceptions;

import com.github.chrisblutz.lutzengine.LutzEngine;
import com.github.chrisblutz.lutzengine.engine.Engine;
import com.github.chrisblutz.lutzengine.engine.plugins.Plugin;

import java.io.File;
import java.io.PrintStream;
import java.util.Calendar;


/**
 * @author Christopher Lutz
 */
public class CrashReporter {
    
    public static void dump(Throwable t) {
        
        try {
            
            File cDDir = new File("crash-dumps");
            cDDir.mkdirs();
            
            File cDump = new File("crash-dumps/dump-" + getDumpTimestampFormatted() + ".log");
            cDump.createNewFile();
            
            PrintStream ps = new PrintStream(cDump);
            
            ps.println("PLEASE SUBMIT THE 'engine.log' FILE FOUND IN '../logs/' WHEN SUBMITTING THIS CRASH DUMP.");
            ps.println();
            
            ps.println("#########################");
            ps.println("## GENERAL INFORMATION ##");
            ps.println("#########################");
            ps.println();
            
            ps.println("Error Type: " + (t != null ? t.getClass().getName() : "-"));
            ps.println();
            
            ps.println("########################");
            ps.println("## ENGINE INFORMATION ##");
            ps.println("########################");
            ps.println();
            
            ps.println("Engine Version: " + LutzEngine.getEngineVersion());
            ps.println();
            
            ps.println("#############################");
            ps.println("## ENVIRONMENT INFORMATION ##");
            ps.println("#############################");
            ps.println();
            
            ps.println("Java Version: " + System.getProperty("java.version"));
            ps.println("Java Vendor: " + System.getProperty("java.vendor"));
            ps.println("OS Name: " + System.getProperty("os.name"));
            ps.println("OS Version: " + System.getProperty("os.version"));
            ps.println("OS Architecture: " + System.getProperty("os.arch"));
            
            Runtime r = Runtime.getRuntime();
            
            ps.println("# of Cores: " + r.availableProcessors());
            ps.println("Total Memory Available: " + (r.maxMemory() / 1000000) + " MB (" + r.maxMemory() + " Bytes)");
            ps.println();
            
            ps.println("#######################");
            ps.println("## INSTALLED PLUGINS ##");
            ps.println("#######################");
            ps.println();
            
            Plugin[] plugins = Engine.getPlugins();
            
            if (plugins.length > 0) {
                
                for (Plugin p : plugins) {
                    
                    ps.println(p.getName() + " (ID: " + p.getId() + ")");
                }
                
            } else {
                
                ps.println("No plugins installed.");
            }
            
            ps.println();
            
            if (t != null) {
                
                ps.println("############################");
                ps.println("## FULL ERROR STACK TRACE ##");
                ps.println("############################");
                ps.println();
                
                t.printStackTrace(ps);
                
            } else {
                
                ps.println();
                ps.println("NO ERROR DETECTED.");
            }
            
            ps.close();
            
        } catch (Exception e) {
            
            System.err.println("An error occurred while creating the crash dump.");
            e.printStackTrace();
        }
    }
    
    private static String getDumpTimestampFormatted() {
        
        Calendar c = Calendar.getInstance();
        
        String str = (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE) + "-" + c.get(Calendar.YEAR) + "_"
                + c.get(Calendar.HOUR_OF_DAY) + "-" + c.get(Calendar.MINUTE) + "-" + c.get(Calendar.SECOND);
        
        return str;
    }
}
