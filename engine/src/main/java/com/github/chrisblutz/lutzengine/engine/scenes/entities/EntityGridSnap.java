package com.github.chrisblutz.lutzengine.engine.scenes.entities;

/**
 * @author Christopher Lutz
 */
public abstract class EntityGridSnap extends Entity {
    
    
    private boolean gridEnabled = true;
    private int gridWidth, gridHeight;
    
    public EntityGridSnap(double x, double y) {
        
        super(x, y);
        
        gridEnabled = false;
        this.gridWidth = 1;
        this.gridHeight = 1;
    }
    
    public EntityGridSnap(double x, double y, int gridWidth, int gridHeight) {
        
        super(x, y);
        
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
    }
    
    public boolean isGridEnabled() {
        
        return gridEnabled;
    }
    
    public void setGridEnabled(boolean gridEnabled) {
        
        this.gridEnabled = gridEnabled;
    }
    
    public int getGridWidth() {
        
        return gridWidth;
    }
    
    public void setGridWidth(int gridWidth) {
        
        this.gridWidth = gridWidth;
    }
    
    public int getGridHeight() {
        
        return gridHeight;
    }
    
    public void setGridHeight(int gridHeight) {
        
        this.gridHeight = gridHeight;
    }
    
    public int snapX(double x) {
        
        return snapX(x, 0);
    }
    
    public int snapX(double x, int offset) {
        
        return ((int) (x / gridWidth) + offset) * gridWidth;
    }
    
    public int snapY(double y) {
        
        return snapY(y, 0);
    }
    
    public int snapY(double y, int offset) {
        
        return ((int) (y / gridHeight) + offset) * gridHeight;
    }
}
