package com.github.chrisblutz.lutzengine.tools.leveleditor.ui;

import javax.swing.*;


/**
 * @author Christopher Lutz
 */
public class EditorUI extends JFrame {
    
    public EditorUI(){
        
        super("Lutz Engine | Level Editor");
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setResizable(true);
        this.setVisible(true);
    }
}
