package com.github.chrisblutz.lutzengine.engine.scenes.entities;

import com.github.chrisblutz.lutzengine.engine.input.Input;


/**
 * @author Christopher Lutz
 */
public abstract class EntityFollowMouse extends EntityGridSnap {
    
    private int width, height;
    private boolean vanishWithMouse = false;
    
    public EntityFollowMouse(double x, double y, int width, int height) {
        
        super(x, y);
        
        this.width = width;
        this.height = height;
    }
    
    public boolean shouldVanishWithMouse() {
        
        return vanishWithMouse;
    }
    
    public void setVanishWithMouse(boolean vanish) {
        
        this.vanishWithMouse = vanish;
    }
    
    public void enableGrid(int gridWidth, int gridHeight) {
        
        setGridWidth(gridWidth);
        setGridHeight(gridHeight);
        setGridEnabled(true);
    }
    
    public void disableGrid() {
        
        setGridEnabled(false);
    }
    
    @Override
    public void tick() {
        
        setX(Input.getMouseX() - (width / 2));
        setY(Input.getMouseY() - (height / 2));
        
        if (isGridEnabled()) {
            
            setX(snapX(getX()));
            setY(snapY(getY()));
        }
        
        if (shouldVanishWithMouse()) {
            
            setRender(Input.isMouseOnScreen());
        }
    }
}
