package com.github.chrisblutz.leplugins.debug;

import com.github.chrisblutz.lutzengine.engine.Engine;
import com.github.chrisblutz.lutzengine.engine.input.Input;
import com.github.chrisblutz.lutzengine.engine.plugins.Plugin;
import com.github.chrisblutz.lutzengine.engine.plugins.PluginManager;
import com.github.chrisblutz.lutzengine.engine.plugins.events.PluginEvent;
import com.github.chrisblutz.lutzengine.engine.scenes.Scene;
import com.github.chrisblutz.lutzengine.engine.visuals.overlays.Overlay;
import com.github.chrisblutz.lutzengine.engine.visuals.ui.Screen;

import java.awt.*;


/**
 * @author Christopher Lutz
 */
public class Debug extends Plugin {
    
    private Scene currentScene = null;
    private Overlay debugOverlay;
    
    public Debug(PluginManager manager, String name, String id) {
        
        super(manager, name, id);
        
        debugOverlay = new Overlay() {
            
            private int mouseX = 0, mouseY = 0;
            private int numEntities = 0;
            private int numOverlays = 0;
            
            private int numPlugins = 0;
            
            @Override
            public void tick() {
                
                mouseX = Input.getMouseX();
                mouseY = Input.getMouseY();
                
                if (currentScene != null) {
                    
                    numEntities = currentScene.getEntityCount();
                    numOverlays = currentScene.getOverlayCount() - 1;
                }
                
                numPlugins = Engine.getPlugins().length;
            }
            
            @Override
            public void render(Graphics2D graphics) {
                
                String title = "Debug Information";
                
                String mouseStr = "Mouse Location: (" + mouseX + ", " + mouseY + ")";
                String numEntityStr = "Entity Count: " + numEntities;
                String numOverlayStr = "Overlay Count: " + numOverlays;
                String numPluginStr = "Plugin Count: " + numPlugins;
                
                graphics.setColor(Color.BLACK);
                
                graphics.setFont(graphics.getFont().deriveFont(14f));
                graphics.setFont(graphics.getFont().deriveFont(Font.BOLD));
                
                graphics.drawString(numOverlayStr, 2, Screen.getResolution().getHeight() - (graphics.getFontMetrics().getHeight() / 2));
                graphics.drawString(numEntityStr, 2, Screen.getResolution().getHeight() - ((graphics.getFontMetrics().getHeight() / 2) * 2) - 8);
                graphics.drawString(mouseStr, 2, Screen.getResolution().getHeight() - ((graphics.getFontMetrics().getHeight() / 2) * 3) - 16);
                graphics.drawString(numPluginStr, 2, Screen.getResolution().getHeight() - ((graphics.getFontMetrics().getHeight() / 2) * 5) - 32);
                
                graphics.setFont(graphics.getFont().deriveFont(18f));
                
                int y = Screen.getResolution().getHeight() - ((graphics.getFontMetrics().getHeight() / 2) * 7) - 32;
                int x2 = graphics.getFontMetrics().stringWidth(title);
                graphics.drawLine(2, y, x2, y);
                
                graphics.drawString(title, 2, Screen.getResolution().getHeight() - ((graphics.getFontMetrics().getHeight() / 2) * 7) - 38);
                
                y = Screen.getResolution().getHeight() - ((graphics.getFontMetrics().getHeight() / 2) * 7) - 58;
                graphics.drawLine(2, y, x2, y);
            }
        };
    }
    
    @Override
    public void registerHooks() {
        
        registerHook(PluginEvent.SCENE_CHANGE, event -> {
            
            currentScene = event.getCurrentScene();
            currentScene.addOverlay(debugOverlay, Integer.MAX_VALUE);
        });
    }
}
