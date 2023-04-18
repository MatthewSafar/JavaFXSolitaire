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
    private int moveCounter = 0;
    
    // Card array
    private final Card[] cardArray = new Card[DeckInfo.DECK_SIZE];
    // Array of stacks for the 4 final piles
    private final ArrayList<ArrayDeque<Card>> foundations;
    // Array of stacks for the facedown stacks in the middle
    private final ArrayList<ArrayDeque<Card>> columns;
    // Array of arraylists for faceup stacks in the middle
    private final ArrayList<ArrayDeque<Card>> playerColumns;
    // array for face down deck
    private final ArrayDeque<Card> deck;
    // array for face up decks?
    private final ArrayDeque<Card> waste;
    
    public static enum Pile {
        DECK, WASTE, FOUNDATION, COLUMN, UNREVEALED
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
        playerColumns = new ArrayList<ArrayDeque<Card>> ();
        for (int i = 0; i < NUM_COLUMNS; i++) {
            columns.add(new ArrayDeque<Card>());
            playerColumns.add(new ArrayDeque<Card>());
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
                columns.get(i).addLast(cardArray[perm.get(cardPlace)]);
                cardPlace++;
            }
            // put one faceup card on each column
            playerColumns.get(i).addLast(cardArray[perm.get(cardPlace)]);
            cardPlace++;
        }
        
        // put the rest in the deck
        for (int i = cardPlace; i < DeckInfo.DECK_SIZE; i++) {
            deck.addLast(cardArray[perm.get(i)]);
        }
        
    }
    
    public void move(SolitaireDeck.Pile from, SolitaireDeck.Pile to, int fromwhich, int towhich) {
        move(from, to, fromwhich,towhich, 1);
    }
    
    public void move(SolitaireDeck.Pile from, SolitaireDeck.Pile to, int which) {
        if (from == SolitaireDeck.Pile.DECK || from == SolitaireDeck.Pile.WASTE) {
            move(from, to, 0, which, 1);
        } else {
            move(from, to, which, 0, 1);
        }
    }
    
    public void move(SolitaireDeck.Pile from, SolitaireDeck.Pile to) {
        move(from, to, 0,0, 1);
    }
    
    // There are lots of error checks to do here. Do we care?
    public void move(SolitaireDeck.Pile from, SolitaireDeck.Pile to, int fromwhich, int towhich, int howmany) {
        System.out.println("From: " + from.toString() + 
                ", To: " + to.toString() + 
                ", From Num: " + Integer.toString(fromwhich) + 
                ", To Num: " + Integer.toString(towhich) + 
                ", Howmany: " + Integer.toString(howmany));
        // remove from pile
        ArrayDeque<Card> toMove = new ArrayDeque<Card>();
        switch (from) {
            case WASTE:
                toMove.addLast(waste.removeLast());
                break;
            case FOUNDATION:
                toMove.addLast(foundations.get(fromwhich).removeLast());
                break;
            case COLUMN:
                for (int iter = 0; iter < howmany; iter++) {
                    toMove.addLast(playerColumns.get(fromwhich).removeLast());
                }
                break;
            case UNREVEALED: 
                toMove.addLast(columns.get(fromwhich).removeLast());
            default:
                System.out.println("Something went horribly wrong!");
        }
        
        switch (to) {
            case FOUNDATION:
                foundations.get(towhich).addLast(toMove.removeLast());
                break;
            case COLUMN:
                for (int iter = 0; iter < howmany; iter++) {
                    playerColumns.get(towhich).addLast(toMove.removeLast());
                }
                break;
            default:
                System.out.println("Something went horribly wrong!");
        }
        
        moveCounter += 1;
    }
    
    public Card nextDeckCard() {
        if (deck.size() == 0) {
            
            return null;
        } else {
            Card nextCard = deck.removeFirst();
            waste.add(nextCard);
            return nextCard;
        }
    }
    public boolean deckIsEmpty() {
        return (deck.size() == 0);
    }
    
    public Card nextWasteCard() {
        if (waste.size() < 4) {
            return null;
        } else {
            ArrayDeque<Card> pile = new ArrayDeque<Card> ();
            for (int iter = 0; iter < 3; iter++) {
                pile.addLast(waste.removeLast());
            }
            Card nextCard = waste.peekLast();
            for (int iter = 0; iter < 3; iter++) {
                waste.addLast(pile.removeLast());
            }
            return nextCard;
        }
    }
    public boolean wasteIsEmpty() {
        return (waste.size() == 0);
    }
    
    public Card getNextFoundationCard( DeckInfo.Suits suit) {
        var foundation = foundations.get(suit.ordinal());
        if (foundation.size() >= 2) {
            Card temp = foundation.removeLast();
            Card nextCard = foundation.getLast();
            foundation.addLast(temp);
            return nextCard;
        } else {
            return null;
        }
    }
    
    // move waste back to deck
    public void resetDeck() {
        deck.addAll(waste);
        waste.clear();
    }
    
    // todo
    public boolean canAddFoundationCard(int whichCol, Card toAdd) {
        boolean sameSuit = DeckInfo.Suits.values()[whichCol] == toAdd.getSuit();
        if (foundations.get(whichCol).isEmpty()) {
            return sameSuit && (toAdd.getRank() == DeckInfo.Ranks.A);
        } else {
            Card topCard = foundations.get(whichCol).getLast();
            boolean oneRankHigher = topCard.getRank().ordinal() == toAdd.getRank().ordinal() - 1;
            return sameSuit && oneRankHigher;
        }
    }
    
    public boolean canAddColumnCard(int whichCol, Card newCard) {
        if (playerColumns.get(whichCol).isEmpty()) {
            return newCard.getRank() == DeckInfo.Ranks.KING;
        } else {
            Card bottomCard = playerColumns.get(whichCol).getLast();
            boolean alternatesColor = bottomCard.getColor() != newCard.getColor();
            boolean oneRankLower = bottomCard.getRank().ordinal() == newCard.getRank().ordinal() + 1;
            return alternatesColor && oneRankLower;
        }
        
    }
    
    public int getDeckSize() {
        return deck.size();
    }
    
    public ArrayDeque<Card> getPlayerColumn(int index) {
        return playerColumns.get(index);
    }
    
    public Card revealColumnCard(int index) {
        Card revealed = columns.get(index).removeLast();
        playerColumns.get(index).add(revealed);
        return revealed;
    }
}
