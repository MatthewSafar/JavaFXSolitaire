/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mysolitaire;

import static java.lang.Math.floor;
import javafx.scene.image.Image;

/**
 * This would be fun to make more complicated, but for solitaire, it's
 * just a whole bunch of static stuff.
 * 
 * In the future, it would be cool to make this a general class for storing
 * different types of decks, for making different games easily
 * @author matth
 */
public class DeckInfo {
    private Image backImage;
    private Image emptyImage;
    private Image cardImages[] = new Image[52];
    
    // These are all static. In a better world, there is more functionality here 
    public static enum Colors {
        RED, BLACK
    }
    public static enum Suits {
        HEART, SPADE, CLUB, DIAMOND
    }
    public static enum Ranks {
        A, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING
    }
    public static final int DECK_SIZE = 52;
    
    public DeckInfo(String deck_location, String backImageLocation, String emptyImageLocation) {
        setCardImages(deck_location);
        setBackImage(backImageLocation);
        setEmptyImage(emptyImageLocation);
    }
    
    // returns the color of a suit
    public static Colors get_suit_color(Suits suit) {
        if (suit == Suits.CLUB || suit == Suits.SPADE) {
            return Colors.BLACK;
        } else {
            return Colors.RED;
        }
    }
    
    public void setBackImage(String filePath) {
        backImage = new Image(filePath);
    }
    
    public Image getBackImage() {
        return backImage;
    }
    
    public void setEmptyImage(String filePath) {
        emptyImage = new Image(filePath);
    }
    
    public Image getEmptyImage() {
        return emptyImage;
    }
    
    public void setCardImages(String folderPath) {
        for (int i = 0; i < DECK_SIZE; i++) {
            cardImages[i] = new Image("file:" + folderPath + "/" + indexToName(i));
        }
    }
    
    public Image getCardImage(Suits suit, Ranks rank) {
        return cardImages[cardToIndex(suit,rank)];
    }
    
    public Image getCardImage(Card card) {
        return cardImages[cardToIndex(card.getSuit(), card.getRank())];
    }
    
    private static int cardToIndex(Suits suit, Ranks rank) {
        return rank.ordinal() + suit.ordinal() * 13;
    }
    
    private static Suits indexToSuit(int index) {
        return Suits.values()[index / 13];
    }
    
    private static Ranks indexToRank(int index) {
        return Ranks.values()[index % 13];
    }
    
    private static String indexToName(int index) {
        return indexToRank(index).toString() + "_" + indexToSuit(index).toString()+".png";
    }
}
