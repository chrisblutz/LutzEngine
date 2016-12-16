package com.github.chrisblutz.lutzengine.engine.visuals.ui;

import com.github.chrisblutz.lutzengine.LutzEngine;
import com.github.chrisblutz.lutzengine.engine.Engine;
import com.github.chrisblutz.lutzengine.engine.GameLoop;
import com.github.chrisblutz.lutzengine.engine.input.Input;
import com.github.chrisblutz.lutzengine.engine.plugins.Plugin;
import com.github.chrisblutz.lutzengine.engine.plugins.PluginManager;
import com.github.chrisblutz.lutzengine.engine.plugins.events.PluginEvent;
import com.github.chrisblutz.lutzengine.engine.scenes.Scene;
import com.github.chrisblutz.lutzengine.exceptions.ErrorCodes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;


/**
 * @author Christopher Lutz
 */
public class Screen {
    
    private static Logger logger;
    
    private static GraphicsEnvironment env;
    private static GraphicsDevice dev;
    private static GraphicsConfiguration config;
    private static DisplayMode mode;
    
    private static Rectangle maxWindowSize;
    
    private static int screenMode = 0;
    private static boolean fullscreen = false;
    private static Resolution resolution;
    
    private static JFrame frame;
    private static Canvas canvas;
    
    private static long avgFps = 0;
    private static long totalFps = 0;
    private static int fps = 0;
    private static long totalSeconds = 0;
    private static int frames = 0;
    private static long lastTime = -1;
    
    private static Scene current = null;
    
    public static void setupGraphicsEnvironment() {
        
        logger = LutzEngine.createLogger("Interface");
        
        logger.info("Retrieving graphics environment...");
        
        env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        dev = env.getDefaultScreenDevice();
        config = dev.getDefaultConfiguration();
        
        mode = dev.getDisplayMode();
        logger.fine("Found screen with bounds " + mode.getWidth() + "x" + mode.getHeight() + ".");
        
        calculateMaxWindowSize();
    }
    
    public static void calculateMaxWindowSize() {
        
        JFrame utils = new JFrame();
        JPanel utilPanel = new JPanel();
        utilPanel.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        utils.add(utilPanel);
        utils.pack();
        
        maxWindowSize = utilPanel.getBounds();
        
        utils.dispose();
    }
    
    public static GraphicsEnvironment getGraphicsEnvironment() {
        
        return env;
    }
    
    public static GraphicsDevice getGraphicsDevice() {
        
        return dev;
    }
    
    public static GraphicsConfiguration getGraphicsConfiguration() {
        
        return config;
    }
    
    public static DisplayMode getDisplayMode() {
        
        return mode;
    }
    
    public static Rectangle getMaximumWindowSize() {
        
        return maxWindowSize;
    }
    
    public static Logger getLogger() {
        
        return logger;
    }
    
    public static void setupUI() {
        
        logger.info("Setting up main user interface...");
        
        frame = new JFrame("test");
        
        canvas = new Canvas();
        
        frame.add(canvas);
        
        logger.info("Setting up input listeners...");
        
        frame.addKeyListener(Input.getKeyListener());
        frame.addMouseListener(Input.getMouseListener());
        frame.addMouseMotionListener(Input.getMouseMotionListener());
        
        canvas.addKeyListener(Input.getKeyListener());
        canvas.addMouseListener(Input.getMouseListener());
        canvas.addMouseMotionListener(Input.getMouseMotionListener());
        
        frame.setResizable(false);
        frame.addWindowListener(new WindowAdapter() {
            
            @Override
            public void windowClosing(WindowEvent e) {
                
                LutzEngine.terminate(0);
            }
        });
        
        refreshScreen();
        
        GameLoop.setup(canvas);
    }
    
    public static void performRenderPass(Graphics2D g) {
        
        try {
            
            frames++;
            
            if (lastTime == -1) {
                
                lastTime = System.currentTimeMillis();
                
            } else {
                
                long t = System.currentTimeMillis();
                
                if (t - lastTime >= 1000) {
                    
                    totalSeconds++;
                    lastTime = t;
                    fps = frames;
                    totalFps += fps;
                    avgFps = totalFps / totalSeconds;
                    frames = 0;
                }
            }
            
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getDisplayMode().getWidth(), getDisplayMode().getHeight());
            
            if (getCurrentScene() != null) {
                
                Graphics2D newG = (Graphics2D) g.create();
                
                getCurrentScene().render(newG);
                
                newG.dispose();
            }
            
            if (System.getProperty("lutzengine.debug").equals("true")) {
                
                g.setColor(Color.BLACK);
                g.setFont(g.getFont().deriveFont(Font.BOLD));
                g.setFont(g.getFont().deriveFont(18f));
                
                String fps = "FPS: " + Long.toString(getFPS());
                g.drawString(fps, 2, 4 + (g.getFontMetrics().getHeight() / 2));
            }
            
        } catch (Exception e) {
            
            Engine.crash("An error occurred during the render loop.", e, ErrorCodes.RENDER_LOOP_EXCEPTION);
        }
    }
    
    public static long getFPS() {
        
        return fps;
    }
    
    public static long getAverageFPS() {
        
        return avgFps;
    }
    
    public static void setCurrentScene(Scene s) {
        
        logger.info("Current scene set to '" + s.getId() + "'.");
        
        current = s;
        
        for (Plugin p : Engine.getPlugins()) {
            
            p.fireHook(PluginEvent.SCENE_CHANGE, PluginManager.generatePluginEvent());
        }
    }
    
    public static Scene getCurrentScene() {
        
        return current;
    }
    
    public static Scene clearCurrentScene() {
        
        Scene temp = getCurrentScene();
        current = null;
        
        for (Plugin p : Engine.getPlugins()) {
            
            p.fireHook(PluginEvent.SCENE_CHANGE, PluginManager.generatePluginEvent());
        }
        
        return temp;
    }
    
    public static Resolution getResolution() {
        
        return resolution;
    }
    
    public static void shutdownUI() {
        
        logger.info("Shutting down interface...");
        
        logger.info("Average FPS: " + getAverageFPS());
    }
    
    public static void refreshTitle() {
        
        if (frame != null) {
            
            frame.setTitle(LutzEngine.getGame().getName());
        }
    }
    
    public static void changeScreenMode(int screenMode) {
        
        Screen.screenMode = screenMode;
        
        switch (screenMode) {
            
            case 0:
                
                // Window
                
                disposeFullscreen();
                frame.dispose();
                frame.setUndecorated(false);
                frame.pack();
                frame.setVisible(true);
                
                break;
            
            case 1:
                
                // Borderless window
                
                disposeFullscreen();
                frame.dispose();
                frame.setUndecorated(true);
                frame.pack();
                frame.setVisible(true);
                
                break;
            
            case 2:
                
                // Fullscreen
                
                frame.dispose();
                makeFullscreen();
                
                break;
        }
    }
    
    private static void disposeFullscreen() {
        
        if (fullscreen) {
            
            fullscreen = false;
            
            dev.setFullScreenWindow(null);
        }
    }
    
    private static void makeFullscreen() {
        
        if (!fullscreen) {
            
            fullscreen = true;
            
            frame.setUndecorated(true);
            
            try {
                
                dev.setFullScreenWindow(frame);
                
            } catch (Exception e) {
                
                dev.setFullScreenWindow(null);
            }
        }
    }
    
    public static void refreshScreen() {
        
        disposeFullscreen();
        frame.dispose();
        
        resolution = Resolution.getDefaultResolution(screenMode == 0);
        
        canvas.setPreferredSize(resolution.toDimension());
        frame.setTitle(LutzEngine.getGame().getName());
        frame.pack();
        
        int mode = 0;
        
        changeScreenMode(mode);
    }
}
