package com.github.chrisblutz.lutzengine.exceptions;

/**
 * @author Christopher Lutz
 */
public class LutzEngineException extends RuntimeException {
    
    public LutzEngineException(String message) {
        
        super(message);
    }
    
    public static LutzEngineException getResolutionFormatException(String resStr) {
        
        return new LutzEngineException("Error reading resolution: " + resStr + ".");
    }
}
