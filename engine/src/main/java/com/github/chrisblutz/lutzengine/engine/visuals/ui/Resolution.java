package com.github.chrisblutz.lutzengine.engine.visuals.ui;

import com.github.chrisblutz.lutzengine.engine.Engine;
import com.github.chrisblutz.lutzengine.exceptions.ErrorCodes;
import com.github.chrisblutz.lutzengine.exceptions.LutzEngineException;

import java.awt.*;
import java.util.Set;
import java.util.TreeSet;


/**
 * @author Christopher Lutz
 */
public class Resolution implements Comparable<Resolution> {
    
    private int width;
    private int height;
    
    private Resolution(int width, int height) {
        
        this.width = width;
        this.height = height;
    }
    
    public int getWidth() {
        
        return width;
    }
    
    public int getHeight() {
        
        return height;
    }
    
    public String getDimensionsAsString() {
        
        return width + "x" + height;
    }
    
    @Override
    public String toString() {
        
        return this.getClass().getName() + "[width=" + width + ",height=" + height + "]";
    }
    
    public Dimension toDimension() {
        
        return new Dimension(width, height);
    }
    
    @Override
    public int compareTo(Resolution o) {
        
        return new Integer(this.getWidth()).compareTo(o.getWidth());
    }
    
    public static Resolution parseResolution(String toParse) {
        
        int width, height;
        
        if (toParse.contains("x")) {
            
            String[] parts = toParse.split("x", 2);
            
            try {
                
                width = Integer.parseInt(parts[0]);
                height = Integer.parseInt(parts[1]);
                
                if (width > 0 && height > 0) {
                    
                    return new Resolution(width, height);
                    
                } else {
                    
                    throw LutzEngineException.getResolutionFormatException(toParse);
                }
                
            } catch (NumberFormatException e) {
                
                throw LutzEngineException.getResolutionFormatException(toParse);
            }
            
        } else {
            
            throw LutzEngineException.getResolutionFormatException(toParse);
        }
    }
    
    public static Resolution[] getSupportedResolutions() {
        
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        
        Set<Resolution> res = new TreeSet<Resolution>();
        
        for (DisplayMode dm : gd.getDisplayModes()) {
            
            res.add(new Resolution(dm.getWidth(), dm.getHeight()));
        }
        
        return res.toArray(new Resolution[res.size()]);
    }
    
    public static Resolution[] getSupportedWindowedResolutions() {
        
        GraphicsDevice gd = Screen.getGraphicsEnvironment().getDefaultScreenDevice();
        
        Rectangle maxWindowSize = Screen.getMaximumWindowSize();
        int maxWidth = maxWindowSize.width;
        int maxHeight = maxWindowSize.height;
        
        Set<Resolution> res = new TreeSet<Resolution>();
        
        for (DisplayMode dm : gd.getDisplayModes()) {
            
            if (dm.getWidth() > maxWidth || dm.getHeight() > maxHeight) {
                
                res.add(new Resolution(maxWidth, maxHeight));
                
            } else {
                
                res.add(new Resolution(dm.getWidth(), dm.getHeight()));
            }
        }
        
        return res.toArray(new Resolution[res.size()]);
    }
    
    public static Resolution getDefaultResolution(boolean windowed) {
        
        Resolution[] res = windowed ? getSupportedWindowedResolutions() : getSupportedResolutions();
        
        if (res.length > 0) {
            
            return getClosestSupportedResolution(800, 600, windowed);
            
        } else {
            
            Engine.crash("No resolutions available!", null, ErrorCodes.CONFIGURATION_EXCEPTION);
            
            return null;
        }
    }
    
    public static Resolution getClosestSupportedResolution(int width, int height, boolean windowed) {
        
        Resolution[] res = windowed ? getSupportedWindowedResolutions() : getSupportedResolutions();
        
        int widthDif = 0, heightDif = 0;
        Resolution curr = res.length > 0 ? res[0] : null;
        
        for (Resolution r : res) {
            
            if (r.getWidth() - width <= widthDif && r.getHeight() - height <= heightDif) {
                
                curr = r;
            }
        }
        
        return curr;
    }
}
