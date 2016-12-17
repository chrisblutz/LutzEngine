package com.github.chrisblutz.lutzengine.engine.plugins.events;

/**
 * @author Christopher Lutz
 */
public interface PluginEventHook {
    
    void onEvent(PluginEvent event);
}
