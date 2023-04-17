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
    
    private static final String RESOURCE_PATH = "./src/resources/";
    
    private static final String DEFAULT_BACK_IMAGE_PATH = "file:" + RESOURCE_PATH + "back.png";
    private static final String DEFAULT_EMPTY_IMAGE_PATH = "file:" + RESOURCE_PATH + "empty.png";
    private static final String DEFAULT_DECK_PATH = RESOURCE_PATH + "/full_deck/";
    private static final String DEFAULT_HEART_FOUNDATION_IMAGE_PATH = "file:" + RESOURCE_PATH + "heart_foundation.png";
    private static final String DEFAULT_DIAMOND_FOUNDATION_IMAGE_PATH = "file:" + RESOURCE_PATH + "diamond_foundation.png";
    private static final String DEFAULT_CLUB_FOUNDATION_IMAGE_PATH = "file:" + RESOURCE_PATH + "club_foundation.png";
    private static final String DEFAULT_SPADE_FOUNDATION_IMAGE_PATH = "file:" + RESOURCE_PATH + "spade_foundation.png";
    private static final String DEFAULT_DECK_REFRESH_IMAGE_PATH = "file:" + RESOURCE_PATH + "deck_refresh.png";
    
    private static final boolean DEFAULT_HARDMODE = false;
    
    // possible settings
    private double windowWidth;
    private double windowHeight;
    
    public DeckInfo deckInfo;
    
    private boolean hardmode;
    
    public SettingsObj() {
        windowWidth = DEFAULT_WIDTH;
        windowHeight = DEFAULT_HEIGHT;
        
        
        deckInfo = new DeckInfo(DEFAULT_DECK_PATH,
                DEFAULT_BACK_IMAGE_PATH,
                DEFAULT_EMPTY_IMAGE_PATH,
                DEFAULT_HEART_FOUNDATION_IMAGE_PATH,
                DEFAULT_DIAMOND_FOUNDATION_IMAGE_PATH,
                DEFAULT_CLUB_FOUNDATION_IMAGE_PATH,
                DEFAULT_SPADE_FOUNDATION_IMAGE_PATH,
                DEFAULT_DECK_REFRESH_IMAGE_PATH
            );
        
        hardmode = DEFAULT_HARDMODE;
    }
    
    public double getWidth() {
        return windowWidth;
    }
    
    public double getHeight() {
        return windowHeight;
    }
    
    public boolean isHardmode() {
        return hardmode;
    }
    
    
}
