package com.mycompany.mysolitaire;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Card class
 * 
 * Represents cards.
 * Includes a card ID and references to images.
 * @author matth
 */
public class Card {
    
    private final DeckInfo.Suits suit;
    private final DeckInfo.Ranks rank;
    private final DeckInfo.Colors color;
    
    public Card(DeckInfo.Ranks rank, DeckInfo.Suits suit) {
        this.rank = rank;
        this.suit = suit;
        this.color = DeckInfo.get_suit_color(suit);
    }
    
    public DeckInfo.Ranks getRank() {
        return rank;
    }
    
    public DeckInfo.Suits getSuit() {
        return suit;
    }
    
    public DeckInfo.Colors getColor() {
        return color;
    }
    
    public String toString() {
        return rank.toString() + "_of_" + suit.toString();
    }
    
}
