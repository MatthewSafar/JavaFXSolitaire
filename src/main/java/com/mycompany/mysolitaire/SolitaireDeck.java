package com.mycompany.mysolitaire;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * SolitaireDeck
 * 
 * Controls the state of a solitaire game
 * 
 * @author matth
 */
public class SolitaireDeck {
    public static final int NUM_COLUMNS = 7;
    
    // Card array
    private final Card[] cardArray = new Card[DeckInfo.DECK_SIZE];
    // Array of stacks for the 4 final piles
    private final ArrayList<ArrayDeque<Card>> foundations;
    // Array of stacks for the facedown stacks in the middle
    private final ArrayList<ArrayDeque<Card>> columns;
    // Array of arraylists for faceup stacks in the middle
    private final ArrayList<ArrayList<Card>> playerColumns;
    // array for face down deck
    private final ArrayDeque<Card> deck;
    // array for face up decks?
    private final ArrayDeque<Card> waste;
    
    public static enum Pile {
        DECK, WASTE, FOUNDATION, COLUMN
    }
    
    SolitaireDeck() {
        // create cards and link file locations
        int cardCount = 0;
        for (DeckInfo.Suits suit : DeckInfo.Suits.values()) {
            for (DeckInfo.Ranks rank : DeckInfo.Ranks.values()) {
                cardArray[cardCount] = new Card(rank, suit);
                cardCount++;
            }
        }
        
        // initialize column arrays
        columns = new ArrayList<ArrayDeque<Card>> ();
        playerColumns = new ArrayList<ArrayList<Card>> ();
        for (int i = 0; i < NUM_COLUMNS; i++) {
            columns.add(new ArrayDeque<Card>());
            playerColumns.add(new ArrayList<Card>());
        }
        
        // initialize foundation arrays
        foundations = new ArrayList<ArrayDeque<Card>> ();
        for (DeckInfo.Suits value : DeckInfo.Suits.values()) {
            foundations.add(new ArrayDeque<Card>());
        }
        
        // initialize deck array
        deck = new ArrayDeque<Card>();
        waste = new ArrayDeque<Card>();
        
        // using the cards, create shuffled order, distribute cards to
        // piles accordingly
        reset();
    }
    
    // shuffle deck, start a game of solitaire
    public void reset() {
        // shuffle deck (what the fuck java)
        List<Integer> perm;
        perm = IntStream.range(0,DeckInfo.DECK_SIZE).boxed().collect(Collectors.toList());
        java.util.Collections.shuffle(perm);
        
        int cardPlace = 0;
        
        for (int i = 0; i < NUM_COLUMNS; i++) {
            // Add facedown cards to columns
            for (int j = 0; j < i; j++) {
                columns.get(i).addFirst(cardArray[perm.get(cardPlace)]);
                cardPlace++;
            }
            // put one faceup card on each column
            playerColumns.get(i).add(cardArray[perm.get(cardPlace)]);
            cardPlace++;
        }
        
        // put the rest in the deck
        for (int i = cardPlace; i < cardPlace + 4; i++) {
            deck.addFirst(cardArray[perm.get(i)]);
        }
        
        // put the rest in the deck
        for (int i = cardPlace+4; i < DeckInfo.DECK_SIZE; i++) {
            waste.addFirst(cardArray[perm.get(i)]);
        }
        
    }
    
    public boolean move(SolitaireDeck.Pile from, SolitaireDeck.Pile to, int fromwhich, int towhich) {
        return move(from, to, fromwhich,towhich, null);
    }
    
    public boolean move(SolitaireDeck.Pile from, SolitaireDeck.Pile to, int which) {
        if (from == SolitaireDeck.Pile.DECK || from == SolitaireDeck.Pile.WASTE) {
            return move(from, to, 0, which, null);
        } else {
            return move(from, to, which, 0, null);
        }
    }
    
    public boolean move(SolitaireDeck.Pile from, SolitaireDeck.Pile to) {
        return move(from, to, 0,0, null);
    }
    
    // todo
    public boolean move(SolitaireDeck.Pile from, SolitaireDeck.Pile to, int fromwhich, int towhich, Card whichCard) {
        return false;
    }
    
    public Card nextDeckCard() {
        if (deck.size() == 0) {
            // move waste back to deck
            deck.addAll(waste);
            waste.clear();
            return null;
        } else {
            Card nextCard = deck.removeFirst();
            waste.add(nextCard);
            return nextCard;
        }
    }
    
    // todo
    public boolean canAddFoundationCard(DeckInfo.Suits fsuit, Card toAdd) {
        return (fsuit == toAdd.getSuit());
    }
    
    public int getDeckSize() {
        return deck.size();
    }
    
    public ArrayList<Card> getPlayerColumn(int index) {
        return playerColumns.get(index);
    }
    
    public Card revealColumnCard(int index) {
        Card revealed = columns.get(index).removeLast();
        playerColumns.get(index).add(revealed);
        return revealed;
    }
}
