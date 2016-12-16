package com.github.chrisblutz.lutzengine.engine.visuals.overlays;

import com.github.chrisblutz.lutzengine.engine.visuals.ui.Screen;

import java.awt.*;


/**
 * @author Christopher Lutz
 */
public abstract class OverlayGridSnap extends Overlay {
    
    private boolean gridEnabled = true;
    private int gridWidth, gridHeight;
    
    private boolean drawGrid = false;
    
    public OverlayGridSnap() {
        
        gridEnabled = false;
        this.gridWidth = 1;
        this.gridHeight = 1;
    }
    
    public OverlayGridSnap(int gridWidth, int gridHeight) {
        
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
    
    public boolean shouldDrawGrid() {
        
        return drawGrid;
    }
    
    public void setDrawGrid(boolean drawGrid) {
        
        this.drawGrid = drawGrid;
    }
    
    @Override
    public void render(Graphics2D graphics) {
        
        if (isGridEnabled() && shouldDrawGrid()) {
            
            graphics.setColor(Color.BLACK);
            
            for (int x = gridWidth; x < Screen.getDisplayMode().getWidth(); x += gridWidth) {
                
                graphics.drawLine(x, 0, x, Screen.getDisplayMode().getHeight());
            }
            
            for (int y = gridHeight; y < Screen.getDisplayMode().getHeight(); y += gridHeight) {
                
                graphics.drawLine(0, y, Screen.getDisplayMode().getWidth(), y);
            }
        }
    }
}
