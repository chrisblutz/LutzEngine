package com.github.chrisblutz.lutzengine.engine.scenes;


import com.github.chrisblutz.lutzengine.engine.scenes.entities.Entity;
import com.github.chrisblutz.lutzengine.engine.visuals.overlays.Overlay;

import java.awt.*;
import java.util.*;
import java.util.List;


/**
 * @author Christopher Lutz
 */
public class Scene {
    
    private String id;
    private Map<Integer, List<Entity>> entities = new HashMap<>();
    private Map<Integer, List<Overlay>> overlays = new HashMap<>();
    
    public Scene(String id) {
        
        this.id = id;
    }
    
    public String getId() {
        
        return id;
    }
    
    public void addEntity(Entity entity) {
        
        addEntity(entity, 0);
    }
    
    public void addEntity(Entity entity, int layer) {
        
        if (!entities.containsKey(layer)) {
            
            entities.put(layer, new ArrayList<Entity>());
        }
        
        entities.get(layer).add(entity);
    }
    
    public boolean hasEntities(int layer) {
        
        return entities.containsKey(layer) && entities.get(layer).size() > 0;
    }
    
    public Entity[] getAllEntities() {
        
        List<Entity> eL = new ArrayList<>();
        
        for (int layer : entities.keySet()) {
            
            eL.addAll(Arrays.asList(getAllEntities(layer)));
        }
        
        return eL.toArray(new Entity[eL.size()]);
    }
    
    public Entity[] getAllEntities(int layer) {
        
        if (entities.containsKey(layer)) {
            
            List<Entity> list = entities.get(layer);
            return list.toArray(new Entity[list.size()]);
            
        } else {
            
            return new Entity[0];
        }
    }
    
    public int getEntityCount() {
        
        int count = 0;
        
        for (int layer : entities.keySet()) {
            
            count += entities.get(layer).size();
        }
        
        return count;
    }
    
    public void addOverlay(Overlay overlay, int layer) {
        
        if (!overlays.containsKey(layer)) {
            
            overlays.put(layer, new ArrayList<>());
        }
        
        overlays.get(layer).add(overlay);
    }
    
    public boolean hasOverlays(int layer) {
        
        return overlays.containsKey(layer) && overlays.get(layer).size() > 0;
    }
    
    public Overlay[] getAllOverlays() {
        
        List<Overlay> oL = new ArrayList<>();
        
        for (int layer : overlays.keySet()) {
            
            oL.addAll(Arrays.asList(getAllOverlays(layer)));
        }
        
        return oL.toArray(new Overlay[oL.size()]);
    }
    
    public Overlay[] getAllOverlays(int layer) {
        
        if (overlays.containsKey(layer)) {
            
            List<Overlay> list = overlays.get(layer);
            return list.toArray(new Overlay[list.size()]);
            
        } else {
            
            return new Overlay[0];
        }
    }
    
    public int getOverlayCount() {
        
        int count = 0;
        
        for (int layer : overlays.keySet()) {
            
            count += overlays.get(layer).size();
        }
        
        return count;
    }
    
    public void render(Graphics2D graphics) {
        
        List<Integer> layers = new ArrayList<>();
        layers.addAll(entities.keySet());
        layers.addAll(overlays.keySet());
        Collections.sort(layers);
        
        for (int layer : layers) {
            
            if (hasEntities(layer)) {
                
                Entity[] e = getAllEntities(layer);
                
                for (Entity entity : e) {
                    
                    if (entity.shouldRender()) {
                        
                        Graphics2D g = (Graphics2D) graphics.create();
                        entity.render(g);
                        g.dispose();
                    }
                }
            }
            
            if (hasOverlays(layer)) {
                
                Overlay[] e = getAllOverlays(layer);
                
                for (Overlay overlay : e) {
                    
                    if (overlay.shouldRender()) {
                        
                        Graphics2D g = (Graphics2D) graphics.create();
                        overlay.render(g);
                        g.dispose();
                    }
                }
            }
        }
    }
}
