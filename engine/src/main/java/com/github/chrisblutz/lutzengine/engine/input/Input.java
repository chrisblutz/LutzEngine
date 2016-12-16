package com.github.chrisblutz.lutzengine.engine.input;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Christopher Lutz
 */
public class Input {
    
    private static Map<String, Integer> keyBindings = new HashMap<String, Integer>();
    private static List<Integer> keyPressed = new ArrayList<>();
    
    private static int mouseX = -1, mouseY = -1;
    private static boolean mouseOnScreen = false;
    private static Map<String, Integer> mouseBindings = new HashMap<String, Integer>();
    private static List<Integer> mousePressed = new ArrayList<>();
    private static Map<Integer, Point> mousePressPoints = new HashMap<>();
    
    private static KeyListener keyListener;
    private static MouseListener mouseListener;
    private static MouseMotionListener mouseMotionListener;
    private static MouseWheelListener mouseWheelListener;
    
    public static KeyListener getKeyListener() {
        
        if (keyListener == null) {
            
            keyListener = new KeyListener() {
                
                @Override
                public void keyTyped(KeyEvent e) {
                    // Do nothing
                }
                
                @Override
                public void keyPressed(KeyEvent e) {
                    
                    if (!keyPressed.contains(e.getKeyCode())) {
                        
                        keyPressed.add(e.getKeyCode());
                    }
                }
                
                @Override
                public void keyReleased(KeyEvent e) {
                    
                    if (keyPressed.contains(e.getKeyCode())) {
                        
                        keyPressed.remove(keyPressed.indexOf(e.getKeyCode()));
                    }
                }
            };
        }
        
        return keyListener;
    }
    
    public static MouseListener getMouseListener() {
        
        if (mouseListener == null) {
            
            mouseListener = new MouseListener() {
                
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Do nothing
                }
                
                @Override
                public void mousePressed(MouseEvent e) {
                    
                    if (!mousePressed.contains(e.getButton())) {
                        
                        mousePressed.add(e.getButton());
                    }
                    
                    mousePressPoints.put(e.getButton(), new Point(e.getX(), e.getY()));
                }
                
                @Override
                public void mouseReleased(MouseEvent e) {
                    
                    if (mousePressed.contains(e.getButton())) {
                        
                        mousePressed.remove(mousePressed.indexOf(e.getButton()));
                    }
                    
                    mousePressPoints.remove(e.getButton());
                }
                
                @Override
                public void mouseEntered(MouseEvent e) {
                    
                    mouseOnScreen = true;
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    
                    mouseOnScreen = false;
                }
            };
        }
        
        return mouseListener;
    }
    
    public static MouseMotionListener getMouseMotionListener() {
        
        if (mouseMotionListener == null) {
            
            mouseMotionListener = new MouseMotionListener() {
                
                @Override
                public void mouseDragged(MouseEvent e) {
                    
                    mouseX = e.getX();
                    mouseY = e.getY();
                }
                
                @Override
                public void mouseMoved(MouseEvent e) {
                    
                    mouseX = e.getX();
                    mouseY = e.getY();
                }
            };
        }
        
        return mouseMotionListener;
    }
    
    public static MouseWheelListener getMouseWheelListener() {
        
        if (mouseMotionListener != null) {
            
            
        }
        
        return mouseWheelListener;
    }
    
    public static void keyBind(String binding, int code) {
        
        keyBindings.put(binding, code);
    }
    
    public static boolean keyPressed(String binding) {
        
        if (keyBindings.containsKey(binding)) {
            
            return keyPressed(keyBindings.get(binding));
        }
        
        return false;
    }
    
    public static boolean keyPressed(int code) {
        
        if (keyPressed.contains(code)) {
            
            return true;
            
        } else {
            
            return false;
        }
    }
    
    public static int getMouseX() {
        
        return mouseX;
    }
    
    public static int getMouseY() {
        
        return mouseY;
    }
    
    public static Point getMousePressPoint(int button) {
        
        if (mousePressPoints.containsKey(button)) {
            
            return mousePressPoints.get(button);
            
        } else {
            
            return null;
        }
    }
    
    public static boolean isMouseOnScreen() {
        
        return mouseOnScreen;
    }
    
    public static void mouseBind(String binding, int button) {
        
        mouseBindings.put(binding, button);
    }
    
    public static boolean mousePressed(String binding) {
        
        if (mouseBindings.containsKey(binding)) {
            
            return mousePressed(mouseBindings.get(binding));
        }
        
        return false;
    }
    
    public static boolean mousePressed(int button) {
        
        if (mousePressed.contains(button)) {
            
            return true;
            
        } else {
            
            return false;
        }
    }
}
