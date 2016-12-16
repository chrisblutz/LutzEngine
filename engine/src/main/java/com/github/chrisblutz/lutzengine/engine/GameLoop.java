package com.github.chrisblutz.lutzengine.engine;

import com.github.chrisblutz.lutzengine.engine.plugins.Plugin;
import com.github.chrisblutz.lutzengine.engine.plugins.PluginManager;
import com.github.chrisblutz.lutzengine.engine.plugins.events.PluginEvent;
import com.github.chrisblutz.lutzengine.engine.scenes.entities.Entity;
import com.github.chrisblutz.lutzengine.engine.visuals.overlays.Overlay;
import com.github.chrisblutz.lutzengine.engine.visuals.ui.Screen;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.logging.Level;


/**
 * @author Christopher Lutz
 */
public class GameLoop {
    
    private static class Loop implements Runnable {
        
        private boolean isRunning;
        private Canvas c;
        private long cycleTime;
        
        public Loop(Canvas c) {
            
            this.c = c;
            isRunning = true;
        }
        
        public void stop() {
            
            isRunning = false;
        }
        
        @Override
        public void run() {
            
            cycleTime = System.currentTimeMillis();
            c.createBufferStrategy(2);
            BufferStrategy strategy = c.getBufferStrategy();
            
            while (isRunning) {
                
                updateStates();
                updateUI(strategy);
                synchronize();
            }
        }
        
        private void updateStates() {
            
            if (Screen.getCurrentScene() != null) {
                
                for (Entity e : Screen.getCurrentScene().getAllEntities()) {
                    
                    e.tick();
                }
                
                for (Overlay o : Screen.getCurrentScene().getAllOverlays()) {
                    
                    o.tick();
                }
            }
            
            for (Plugin p : Engine.getPlugins()) {
                
                p.fireHook(PluginEvent.RENDER_PASS, PluginManager.generatePluginEvent());
            }
        }
        
        private void updateUI(BufferStrategy str) {
            
            Graphics2D g = (Graphics2D) str.getDrawGraphics();
            Screen.performRenderPass(g);
            g.dispose();
            str.show();
        }
        
        private void synchronize() {
            
            cycleTime = cycleTime + frameDelay;
            long dif = cycleTime - System.currentTimeMillis();
            
            try {
                
                Thread.sleep(Math.max(0, dif));
                
            } catch (Exception e) {
                
                Engine.getEngineLogger().log(Level.WARNING, "Loop sync interrupted.", e);
            }
        }
    }
    
    private static int frameDelay = 0;
    private static Loop mainLoop;
    private static Thread gameThread;
    
    public static void setup(Canvas c) {
        
        mainLoop = new Loop(c);
        gameThread = new Thread(mainLoop);
    }
    
    public static void start() {
        
        gameThread.start();
    }
    
    public static void stop() {
        
        if (mainLoop != null) {
            
            mainLoop.stop();
        }
    }
    
    public static int getFrameDelay() {
        
        return frameDelay;
    }
    
    public static void setFrameDelay(int frameDelay) {
        
        GameLoop.frameDelay = frameDelay;
    }
}
