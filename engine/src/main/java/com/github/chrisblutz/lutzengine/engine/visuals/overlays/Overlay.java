package com.github.chrisblutz.lutzengine.engine.visuals.overlays;

import java.awt.*;


/**
 * @author Christopher Lutz
 */
public abstract class Overlay {
    
    private boolean render = true;
    
    public boolean shouldRender() {
        
        return render;
    }
    
    public void setRender(boolean render) {
        
        this.render = render;
    }
    
    public abstract void tick();
    
    public abstract void render(Graphics2D graphics);
}
