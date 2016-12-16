package com.github.chrisblutz.lutzengine.engine;

import com.github.chrisblutz.lutzengine.LutzEngine;
import com.github.chrisblutz.lutzengine.engine.plugins.Plugin;
import com.github.chrisblutz.lutzengine.engine.plugins.PluginLoader;
import com.github.chrisblutz.lutzengine.engine.plugins.PluginManager;
import com.github.chrisblutz.lutzengine.engine.plugins.events.PluginEvent;
import com.github.chrisblutz.lutzengine.engine.resources.ResourceManager;
import com.github.chrisblutz.lutzengine.engine.visuals.ui.Screen;
import com.github.chrisblutz.lutzengine.exceptions.CrashReporter;
import com.github.chrisblutz.lutzengine.exceptions.ErrorCodes;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Christopher Lutz
 */
public class Engine {
    
    private static Logger logger;
    
    private static Thread resourceThread = null;
    
    private static Plugin[] plugins = new Plugin[0];
    
    private static boolean crashed = false;
    private static Throwable crashThrowable = null;
    
    public static void setup() {
        
        logger = LutzEngine.createLogger("Engine");
        
        logger.info("Commencing engine setup...");
        
        setupOpenGL();
        
        Screen.setupGraphicsEnvironment();
        
        loadResources();
        
        plugins = PluginLoader.loadPlugins();
        
        for (Plugin p : plugins) {
            
            p.registerHooks();
            p.fireHook(PluginEvent.LOAD, PluginManager.generatePluginEvent());
        }
        
        LutzEngine.getGame().load();
        
        // TODO Handle other startup events
        
        waitForResourceLoad();
        
        for (Plugin p : plugins) {
            
            p.fireHook(PluginEvent.PRE_UI, PluginManager.generatePluginEvent());
        }
        
        LutzEngine.getGame().preUI();
        
        Screen.setupUI();
        
        logger.info("Starting main game loop...");
        
        GameLoop.start();
    }
    
    public static void setupOpenGL() {
        
        logger.info("Setting up Java2D to use OpenGL pipeline...");
        
        System.setProperty("sun.java2d.opengl", "true");
    }
    
    public static void loadResources() {
        
        resourceThread = new Thread(new Runnable() {
            
            @Override
            public void run() {
                
                ResourceManager.loadAll();
            }
        });
        resourceThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                
                Engine.crash("A fatal error occurred in thread '" + t.getName() + "'.", e, ErrorCodes.RESOURCE_LOAD_EXCEPTION);
            }
        });
        
        resourceThread.setName("LutzEngine-Resources");
        resourceThread.start();
    }
    
    public static void waitForResourceLoad() {
        
        while (resourceThread.isAlive()) ;
        return;
    }
    
    public static Logger getEngineLogger() {
        
        return logger;
    }
    
    public static Plugin[] getPlugins() {
        
        return plugins;
    }
    
    public static void crash(String message, Throwable t, int errorCode) {
        
        logger.log(Level.SEVERE, message, t);
        
        logger.severe("The engine can no longer continue normal operation...");
        
        crashed = true;
        crashThrowable = t;
        
        LutzEngine.terminate(errorCode);
    }
    
    public static void shutdown() {
        
        shutdown(0);
    }
    
    public static void shutdown(int exitCode) {
        
        logger.info("Shutting down engine...");
        
        shutdownLoop();
        
        Screen.shutdownUI();
        
        LutzEngine.getGame().unload();
        
        for (Plugin p : plugins) {
            
            p.fireHook(PluginEvent.UNLOAD, PluginManager.generatePluginEvent());
        }
        
        logger.info("Finalizing shutdown (exit code " + exitCode + ")...");
        
        if (crashed) {
            
            CrashReporter.dump(crashThrowable);
        }
    }
    
    public static void shutdownLoop() {
        
        logger.info("Stopping main game loop...");
        
        GameLoop.stop();
    }
}
