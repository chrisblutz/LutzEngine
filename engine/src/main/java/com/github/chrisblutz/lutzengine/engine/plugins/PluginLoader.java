package com.github.chrisblutz.lutzengine.engine.plugins;

import com.github.chrisblutz.lutzengine.LutzEngine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * @author Christopher Lutz
 */
public class PluginLoader {
    
    private static Logger logger;
    
    public static Plugin[] loadPlugins() {
        
        logger = LutzEngine.createLogger("Plugin-Loader");
        
        logger.info("Loading plugins...");
        
        File pluginDir = new File("plugins/");
        
        if (pluginDir.exists()) {
            
            List<Plugin> plugins = new ArrayList<>();
            
            String[] files = pluginDir.list((dir, name) -> name.endsWith(".jar"));
            
            for (String name : files) {
                
                try {
                    
                    File fullJar = new File(pluginDir, name);
                    
                    List<String> classes = new ArrayList<>();
                    
                    ZipInputStream jarIn = new ZipInputStream(new FileInputStream(fullJar));
                    
                    for (ZipEntry entry = jarIn.getNextEntry(); entry != null; entry = jarIn.getNextEntry()) {
                        
                        if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                            
                            classes.add(entry.getName().replace("\\", "/").replace("/", ".").substring(0, entry.getName().length() - ".class".length()));
                        }
                    }
                    
                    jarIn.close();
                    
                    ClassLoader loader = URLClassLoader.newInstance(new URL[]{fullJar.toURI().toURL()}, ClassLoader.getSystemClassLoader());
                    
                    for (String className : classes) {
                        
                        loader.loadClass(className);
                    }
                    
                    InputStream info = loader.getResourceAsStream("plugin.info");
                    
                    if (info != null) {
                        
                        Properties p = new Properties();
                        p.load(info);
                        info.close();
                        
                        String pluginName = p.getProperty("plugin.name");
                        String pluginId = p.getProperty("plugin.id");
                        String pluginMainClass = p.getProperty("plugin.main");
                        
                        if (pluginName != null && pluginId != null && pluginMainClass != null) {
                            
                            try {
                                
                                Class<?> main = Class.forName(pluginMainClass, true, loader);
                                Class<? extends Plugin> mainPlugin = main.asSubclass(Plugin.class);
                                Constructor<? extends Plugin> ctorPlugin = mainPlugin.getConstructor(PluginManager.class, String.class, String.class);
                                
                                PluginManager manager = new PluginManager(pluginId);
                                Plugin plugin = ctorPlugin.newInstance(manager, pluginName, pluginId);
                                plugins.add(plugin);
                                
                            } catch (ClassCastException e) {
                                
                                logger.warning("Main plugin class file '" + pluginName + "' does not extend Plugin!");
                                
                            } catch (ClassNotFoundException e) {
                                
                                logger.warning("Could not find main plugin class file '" + pluginName + "'!");
                                
                            } catch (NoSuchMethodException e) {
                                
                                logger.warning("Main plugin file '" + pluginName
                                        + "' does not have a constructor that takes a PluginManager and two strings as parameters!");
                                
                            } catch (SecurityException e) {
                                
                                logger.log(Level.WARNING, "The plugin loader encountered security errors while obtaining the constructor for main plugin class '"
                                        + pluginName + "'!", e);
                                
                            } catch (InstantiationException | InvocationTargetException | IllegalArgumentException | IllegalAccessException e) {
                                
                                logger.log(Level.WARNING, "An error occurred while calling the constructor in '" + pluginName + "'!", e);
                                
                            }
                        }
                    }
                    
                } catch (IOException e) {
                    
                    logger.log(Level.WARNING, "An error occurred while loading '/plugins/" + name + "'!", e);
                    
                } catch (ClassNotFoundException e) {
                    
                    logger.log(Level.WARNING, "An error occurred while loading classes within '/plugins/" + name + "'!", e);
                }
            }
            
            logger.info("Found " + plugins.size() + " plugin(s).");
            
            return plugins.toArray(new Plugin[plugins.size()]);
        }
        
        logger.info("Found 0 plugins.");
        
        return new Plugin[0];
    }
}
