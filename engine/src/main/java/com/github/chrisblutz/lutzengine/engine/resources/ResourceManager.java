package com.github.chrisblutz.lutzengine.engine.resources;

import com.github.chrisblutz.lutzengine.LutzEngine;
import com.github.chrisblutz.lutzengine.engine.visuals.ui.Screen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Christopher Lutz
 */
public class ResourceManager {
    
    private static Logger logger;
    private static FilenameFilter filter = null;
    
    public static class Images {
        
        private static HashMap<String, BufferedImage> images = new HashMap<>();
        
        public static int quantity() {
            
            File imageDir = new File("resources/images");
            
            if (imageDir.exists()) {
                
                return quantity(imageDir);
                
            } else {
                
                return 0;
            }
        }
        
        private static int quantity(File dir) {
            
            int quantity = 0;
            
            File[] files = dir.listFiles();
            for (File file : files) {
                
                if (file.isDirectory()) {
                    
                    quantity += quantity(file);
                    
                } else if (file.getName().endsWith(".png")) {
                    
                    quantity++;
                }
            }
            
            return quantity;
        }
        
        public static void load() {
            
            logger.info("Loading image resources...");
            
            File imageDir = new File("resources/images");
            
            if (imageDir.exists()) {
                
                loadImages(imageDir, "");
            }
            
            logger.info("Found " + images.size() + " image resource(s).");
        }
        
        public static void loadImages(File dir, String rootName) {
            
            File[] files = dir.listFiles();
            for (File file : files) {
                
                if (file.isDirectory()) {
                    
                    loadImages(file, rootName + file.getName() + ":");
                    
                } else if (file.getName().endsWith(".png")) {
                    
                    try {
                        
                        // Remove file extension for saving
                        String name = file.getName().substring(0, file.getName().lastIndexOf("."));
                        
                        BufferedImage image = ImageIO.read(file);
                        
                        // Convert image to the correct model for rendering
                        BufferedImage converted = Screen.getGraphicsConfiguration().createCompatibleImage(image.getWidth(), image.getHeight(), image.getTransparency());
                        Graphics2D g = converted.createGraphics();
                        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
                        g.dispose();
                        
                        images.put(rootName + name, converted);
                        
                    } catch (Exception e) {
                        
                        logger.log(Level.WARNING, "Failed to load resource '" + file.getPath() + "'.", e);
                    }
                }
            }
        }
        
        public static BufferedImage retrieve(String name) {
            
            return images.get(name);
        }
    }
    
    public static void loadAll() {
        
        logger = LutzEngine.createLogger("Resource-Thread");
        
        Images.load();
    }
    
    public static int checkQuantity() {
        
        int quantity = 0;
        quantity += Images.quantity();
        
        return quantity;
    }
}
