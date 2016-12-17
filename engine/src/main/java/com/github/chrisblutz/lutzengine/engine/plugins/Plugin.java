package com.github.chrisblutz.lutzengine.engine.plugins;

import com.github.chrisblutz.lutzengine.engine.plugins.events.PluginEvent;
import com.github.chrisblutz.lutzengine.engine.plugins.events.PluginEventHook;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Christopher Lutz
 */
public abstract class Plugin {
    
    private PluginManager manager;
    private String name, id;
    
    private Map<Integer, PluginEventHook> hooks = new HashMap<>();
    
    public Plugin(PluginManager manager, String name, String id) {
        
        this.manager = manager;
        this.name = name;
        this.id = id;
    }
    
    public PluginManager getManager() {
        
        return manager;
    }
    
    public String getName() {
        
        return name;
    }
    
    public String getId() {
        
        return id;
    }
    
    public void registerHook(int eventCode, PluginEventHook eventHook) {
        
        hooks.put(eventCode, eventHook);
    }
    
    public void fireHook(int eventCode, PluginEvent event) {
        
        if (hooks.containsKey(eventCode)) {
            
            PluginEventHook hook = hooks.get(eventCode);
            
            if (hook != null) {
                
                hook.onEvent(event);
            }
        }
    }
    
    public abstract void registerHooks();
}
