/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mysolitaire;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author matth
 */
class SettingsObj {
    // layout and visuals
    // defaults
    private static final double DEFAULT_WIDTH = 1024;
    private static final double DEFAULT_HEIGHT = 640;
    
    public static final String RESOURCE_PATH = "resources/";
    public static final String DEFAULT_MUSIC_PATH = RESOURCE_PATH + "music/";
    public static final String[] SUPPORTED_AUDIO_TYPES = new String[] {"wav", "mp3"};
    
    public static final String STATIC_PATH = RESOURCE_PATH + "static/";
    private static final String EMPTY_IMAGE_PATH = STATIC_PATH + "empty.png";
    private static final String HEART_FOUNDATION_IMAGE_PATH = STATIC_PATH + "heart_foundation.png";
    private static final String DIAMOND_FOUNDATION_IMAGE_PATH = STATIC_PATH + "diamond_foundation.png";
    private static final String CLUB_FOUNDATION_IMAGE_PATH = STATIC_PATH + "club_foundation.png";
    private static final String SPADE_FOUNDATION_IMAGE_PATH = STATIC_PATH + "spade_foundation.png";
    private static final String DECK_REFRESH_IMAGE_PATH = STATIC_PATH + "deck_refresh.png";
    
    public static final String DECKS_PATH = RESOURCE_PATH + "decks/";
    private static final String DEFAULT_BACK_IMAGE_PATH = DECKS_PATH + "default_blue.png";
    private static final String DEFAULT_DECK_PATH = DECKS_PATH + "/default/";
    
    private static final boolean DEFAULT_HARDMODE = false;
    
    private static final boolean DEFAULT_ISMUTE = false;
    
    // possible settings
    private double windowWidth;
    private double windowHeight;
    
    public DeckInfo deckInfo;
    
    private boolean hardmode;
    
    private boolean isMute;
    private int currentTrackNum = -1;
    private int maxTrackNum;
    
    private ArrayList<MediaPlayer> musicList;
    
    public SettingsObj() {
        windowWidth = DEFAULT_WIDTH;
        windowHeight = DEFAULT_HEIGHT;
        
        
        deckInfo = new DeckInfo(DEFAULT_DECK_PATH,
                DEFAULT_BACK_IMAGE_PATH,
                EMPTY_IMAGE_PATH,
                HEART_FOUNDATION_IMAGE_PATH,
                DIAMOND_FOUNDATION_IMAGE_PATH,
                CLUB_FOUNDATION_IMAGE_PATH,
                SPADE_FOUNDATION_IMAGE_PATH,
                DECK_REFRESH_IMAGE_PATH
            );
        
        hardmode = DEFAULT_HARDMODE;
        
        isMute = DEFAULT_ISMUTE;
        
        musicList = new ArrayList<MediaPlayer>();
        updateMusicList(DEFAULT_MUSIC_PATH);
    }
    
    public void updateMusicList(String newMusicPath) {
        File musicFolder = new File(newMusicPath);
        if (musicFolder.isDirectory()) {
            File[] fileList = musicFolder.listFiles();
            for (File file : fileList) {
                String extension = FilenameUtils.getExtension(file.getAbsolutePath());
                if (Arrays.asList(SUPPORTED_AUDIO_TYPES).contains(extension)) {
                    MediaPlayer newMedia = new MediaPlayer(new Media(file.toURI().toString()));
                    // add general settings here
                    musicList.add(newMedia);
                    maxTrackNum += 1;
                }
            }
        }
    }
    
    public MediaPlayer getNextMusicTrack() {
        if (musicList.size() > 0) {
            currentTrackNum += 1;
            if (currentTrackNum >= maxTrackNum) {
                currentTrackNum = 0;
            }
            return musicList.get(currentTrackNum);
        } else {
            return null;
        }
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
    
    public boolean isMute() {
        return isMute;
    }
    
    public void setMute(boolean newMute) {
        isMute = newMute;
    }
    
    
}
