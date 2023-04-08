package com.mycompany.mysolitaire;

import javafx.scene.Group;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * SolitaireFoundationPile
 * Represents the piles of cards at the top that you slowly fill up with cards
 * sort of acts like a stack memory structure where you add and then remove the
 * last one added.
 * 
 * @author matth
 */
public class FoundationPileView extends Group implements MovementPlacement {
    private CardView underCard;
    private CardViewGroup topCard;
    private int cardCount = 0;
    private final DeckInfo.Suits suit;
    
    public FoundationPileView(DeckInfo.Suits suit) {
        this.suit = suit;
        
        var emptySpot = new CardView(App.settings.deckInfo.getEmptyImage());
        getChildren().add(emptySpot);
        
        underCard = new CardView();
        underCard.toFront();
        topCard = new CardViewGroup();
        getChildren().add(underCard);
        getChildren().add(topCard);
    }
    
    public DeckInfo.Suits getSuit() {
        return suit;
    }
    
    // TODO add visual indicator of number of cards
    public void put_card_on_top(Card newCard) {
        if (topCard != null) {
            getChildren().remove(topCard);
            getChildren().remove(underCard);
            underCard = topCard.getCardView();
            underCard.toFront();
            getChildren().add(underCard);
        }
        topCard = new CardViewGroup(newCard);
        topCard.setInteraction(CardViewGroup.Interaction.DRAGGABLE);
        getChildren().add(topCard);
        topCard.toFront();
        cardCount = cardCount + 1;
    }
    
    public Card remove_top_card(Card newCard) {
        Card out = topCard.getCard();
        getChildren().remove(topCard);
        getChildren().remove(underCard);
        if (newCard != null) {
            System.out.println("Undercard: " + underCard.getCard().toString());
            System.out.println("topCard: " + topCard.toString());
            topCard = new CardViewGroup(underCard);
            underCard = new CardView(newCard);
            System.out.println("after:");
            System.out.println("Undercard: " + underCard.getCard().toString());
            System.out.println("topCard: " + topCard.toString());
            topCard.setInteraction(CardViewGroup.Interaction.DRAGGABLE);
        } else if (underCard.getCard() != null) {
            topCard = new CardViewGroup(underCard);
            underCard = new CardView();
            topCard.setInteraction(CardViewGroup.Interaction.DRAGGABLE);
        } else { //undercard is null and newcard is null (topcard is last card)
            topCard = new CardViewGroup();            
        }
        
        getChildren().add(underCard);
        underCard.toFront();
        getChildren().add(topCard);
        topCard.toFront();
        cardCount = cardCount - 1;
        return out;
    }

    @Override
    public void moveTo(double x, double y) {
        relocate(x,y);
    }
    
    
    @Override
    public void shiftBy(double x, double y) {
        var nowX = getLayoutX();
        var nowY = getLayoutY();
        relocate(nowX + x,nowY + y);
    }
}
