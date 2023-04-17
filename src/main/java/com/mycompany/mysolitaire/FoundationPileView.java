package com.mycompany.mysolitaire;

import javafx.scene.Group;
import javafx.scene.Node;

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
        
        var emptySpot = new CardView(App.settings.deckInfo.getFoundationImage(suit));
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
    public CardViewGroup put_card_on_top(Card newCard) {
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
        return topCard;
    }
    
    public Card remove_top_card(Card newCard) {
        Card out = topCard.getCard();
        getChildren().remove(topCard);
        getChildren().remove(underCard);
        if (newCard != null) {
            topCard = new CardViewGroup(underCard);
            underCard = new CardView(newCard);
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
    
    public double getXInPane() {
        double x = this.getLayoutX();
        Node p = this.getParent();
        while (!(p instanceof SolitairePane)) {
            x += p.getLayoutX();
            p = p.getParent();
        }
        return x;
    }
    
    public double getYInPane() {
        double y = this.getLayoutY();
        Node p = this.getParent();
        while (!(p instanceof SolitairePane)) {
            y += p.getLayoutX();
            p = p.getParent();
        }
        return y;
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
