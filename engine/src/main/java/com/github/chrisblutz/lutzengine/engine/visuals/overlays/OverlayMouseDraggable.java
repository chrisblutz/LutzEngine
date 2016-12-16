package com.github.chrisblutz.lutzengine.engine.visuals.overlays;

import com.github.chrisblutz.lutzengine.engine.input.Input;

import java.awt.*;


/**
 * @author Christopher Lutz
 */
public abstract class OverlayMouseDraggable extends OverlayGridSnap {
    
    private int button;
    private int x1 = -1, x2 = -1, y1 = -1, y2 = -1;
    
    public OverlayMouseDraggable(int button) {
        
        this.button = button;
    }
    
    public int getX1() {
        
        return x1;
    }
    
    public int getY1() {
        
        return y1;
    }
    
    public int getX2() {
        
        return x2;
    }
    
    public int getY2() {
        
        return y2;
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
        
        if (Input.mousePressed(button)) {
            
            setRender(true);
            
            Point p = Input.getMousePressPoint(button);
            
            if (p != null) {
                
                x1 = (int) p.getX();
                y1 = (int) p.getY();
                x2 = Input.getMouseX();
                y2 = Input.getMouseY();
                
                if (x1 > x2) {
                    
                    int temp = x2;
                    x2 = x1;
                    x1 = temp;
                }
                
                if (y1 > y2) {
                    
                    int temp = y2;
                    y2 = y1;
                    y1 = temp;
                }
            }
            
            if (isGridEnabled()) {
                
                x1 = snapX(x1);
                x2 = snapX(x2, 1);
                y1 = snapY(y1);
                y2 = snapY(y2, 1);
            }
            
        } else {
            
            setRender(false);
        }
    }
}
