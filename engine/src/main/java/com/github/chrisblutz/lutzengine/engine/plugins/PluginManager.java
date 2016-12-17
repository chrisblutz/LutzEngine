package com.github.chrisblutz.lutzengine.engine.plugins;

import com.github.chrisblutz.lutzengine.LutzEngine;
import com.github.chrisblutz.lutzengine.engine.plugins.events.PluginEvent;
import com.github.chrisblutz.lutzengine.engine.visuals.ui.Screen;

import java.util.logging.Logger;


/**
 * @author Christopher Lutz
 */
public class PluginManager {
    
    private String id;
    private Logger logger;
    
    public PluginManager(String id) {
        
        this.id = id;
        logger = LutzEngine.createLogger("Plugin " + id);
    }
    
    public String getPluginId() {
        
        return id;
    }
    
    public Logger getLogger() {
        
        return logger;
    }
    
    public static PluginEvent generatePluginEvent() {
        
        return new PluginEvent(Screen.getCurrentScene());
    }
}
