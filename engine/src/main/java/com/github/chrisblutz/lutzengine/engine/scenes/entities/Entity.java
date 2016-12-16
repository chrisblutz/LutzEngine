package com.github.chrisblutz.lutzengine.engine.scenes.entities;

import java.awt.*;


/**
 * @author Christopher Lutz
 */
public abstract class Entity {
    
    private double x, y;
    private boolean render = true;
    
    public Entity(double x, double y) {
        
        this.x = x;
        this.y = y;
    }
    
    public double getX() {
        
        return x;
    }
    
    public void setX(double x) {
        
        this.x = x;
    }
    
    public double getY() {
        
        return y;
    }
    
    public void setY(double y) {
        
        this.y = y;
    }
    
    public boolean shouldRender() {
        
        return render;
    }
    
    public void setRender(boolean render) {
        
        this.render = render;
    }
    
    public abstract void tick();
    
    public abstract void render(Graphics2D graphics);
}
