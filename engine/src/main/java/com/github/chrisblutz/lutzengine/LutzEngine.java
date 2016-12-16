package com.github.chrisblutz.lutzengine;

import com.github.chrisblutz.lutzengine.engine.Engine;
import com.github.chrisblutz.lutzengine.engine.game.Game;
import com.github.chrisblutz.lutzengine.exceptions.ErrorCodes;
import com.github.chrisblutz.lutzengine.logging.LoggerUtils;
import com.github.chrisblutz.lutzengine.logging.SystemLogUtils;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Copyright 2016 Christopher Lutz
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Christopher Lutz
 */
public class LutzEngine {
    
    private static Game game;
    
    private static Thread mainThread;
    
    private static Logger logger;
    
    private static Properties properties;
    
    public static Game getGame() {
        
        return game;
    }
    
    public static Logger getLogger() {
        
        return logger;
    }
    
    public static Logger createLogger(String name) {
        
        Logger l = Logger.getLogger(name);
        l.setParent(logger);
        
        return l;
    }
    
    public static void startEngine(Game game) {
        
        LutzEngine.game = game;
        
        mainThread = new Thread(LutzEngine::initialize);
        mainThread.setUncaughtExceptionHandler((t, e) -> Engine.crash("A fatal error occurred in thread '" + t.getName() + "'.", e, ErrorCodes.UNCAUGHT_EXCEPTION));
        
        mainThread.setName("LutzEngine-Main");
        mainThread.start();
    }
    
    public static void initialize() {
        
        logger = Logger.getLogger("Game");
        logger.setUseParentHandlers(false);
        logger.addHandler(LoggerUtils.getSysOutHandler());
        logger.setLevel(Level.ALL);
        
        try {
            
            logger.addHandler(LoggerUtils.getFileHandler(LoggerUtils.LOG_FILE));
            
        } catch (Exception e) {
            
            logger.log(Level.WARNING, "An error occurred while setting up the file logger.", e);
        }
        
        SystemLogUtils.logEnvironment();
        
        logger.info("Loading engine information...");
        
        properties = new Properties();
        
        try {
            
            InputStream propIn = LutzEngine.class.getResourceAsStream("/engine.info");
            
            if (propIn != null) {
                
                properties.load(propIn);
                
            } else {
                
                logger.severe("No engine information detected.");
            }
            
        } catch (Exception e) {
            
            logger.severe("No engine information detected.");
        }
        
        Engine.setup();
    }
    
    public static String getEngineProperty(String property) {
        
        return properties.getProperty(property);
    }
    
    public static String getEngineVersion() {
        
        String version = getEngineProperty("engine.version");
        
        if (version != null) {
            
            return version;
            
        } else {
            
            return "UNSPECIFIED";
        }
    }
    
    public static void terminate(int errorCode) {
        
        Engine.shutdown(errorCode);
        
        System.exit(errorCode);
    }
}
