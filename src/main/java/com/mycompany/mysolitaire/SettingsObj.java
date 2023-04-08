/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mysolitaire;

/**
 *
 * @author matth
 */
class SettingsObj {
    // layout and visuals
    // defaults
    private static final double DEFAULT_WIDTH = 1024;
    private static final double DEFAULT_HEIGHT = 640;
    
    private static final String DEFAULT_BACK_IMAGE_PATH = "file:./src/resources/back.png";
    private static final String DEFAULT_EMPTY_IMAGE_PATH = "file:./src/resources/spot.png";
    private static final String DEFAULT_DECK_PATH = "./src/resources/full_deck/";
    
    // possible settings
    private double windowWidth;
    private double windowHeight;
    
    public DeckInfo deckInfo;
    
    public SettingsObj() {
        windowWidth = DEFAULT_WIDTH;
        windowHeight = DEFAULT_HEIGHT;
        
        
        deckInfo = new DeckInfo(DEFAULT_DECK_PATH,
                DEFAULT_BACK_IMAGE_PATH,
                DEFAULT_EMPTY_IMAGE_PATH);
    }
    
    public double getWidth() {
        return windowWidth;
    }
    
    public double getHeight() {
        return windowHeight;
    }
    
    
}
