package com.github.chrisblutz.lutzengine.engine.game;

/**
 * @author Christopher Lutz
 */
public interface Game {
    
    String getName();
    
    void load();
    
    void preUI();
    
    void unload();
}
