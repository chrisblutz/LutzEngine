package com.github.chrisblutz.lutzengine.engine.plugins.events;

import com.github.chrisblutz.lutzengine.engine.scenes.Scene;


/**
 * @author Christopher Lutz
 */
public class PluginEvent {
    
    public static final int LOAD = 0;
    public static final int UNLOAD = 1;
    public static final int PRE_UI = 5;
    public static final int RENDER_PASS = 6;
    public static final int SCENE_CHANGE = 10;
    
    private Scene currentScene;
    
    public PluginEvent(Scene currentScene) {
        
        this.currentScene = currentScene;
    }
    
    public Scene getCurrentScene() {
        
        return currentScene;
    }
}
