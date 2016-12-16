package com.github.chrisblutz.lutzengine.engine.visuals.ui;

import com.github.chrisblutz.lutzengine.engine.visuals.overlays.Overlay;


/**
 * @author Christopher Lutz
 */
public abstract class UIOverlay extends Overlay {
    
    public int scaleX(int x) {
        
        return x;
    }
    
    public int scaleY(int y) {
        
        return y;
    }
    
    public int scaleWidth(int width) {
        
        return width;
    }
    
    public int scaleHeight(int height) {
        
        return height;
    }
}
