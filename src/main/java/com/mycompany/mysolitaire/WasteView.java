/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mysolitaire;

import java.util.ArrayDeque;
import javafx.scene.Group;
import javafx.scene.Node;

/**
 *
 * @author matth
 */
public class WasteView extends Group implements MovementPlacement{
    private final ArrayDeque<CardViewGroup> cardStack;
    private final CardView emptySpot;
    private static final double CARD_OFFSET = 20.0;
    private static final int STACK_MAX_SIZE = 3;
    
    private double cardSourceX = 0;
    private double cardSourceY = 0;
    
    public WasteView() {
        cardStack = new ArrayDeque<CardViewGroup>();
        
        emptySpot = new CardView(App.settings.deckInfo.emptyImage);
        getChildren().add(emptySpot);
    }
    
    public void add_card(Card newCard) {
        if (cardStack.size() == STACK_MAX_SIZE) {
            CardViewGroup toremove = cardStack.removeFirst();
            getChildren().remove(toremove);
            for (CardViewGroup c : cardStack) {
                c.shiftBy(-CARD_OFFSET,0,CardViewGroup.FAST_ANIM_DUR);
            }
        }
        
        if (cardStack.size() > 0) {
            CardViewGroup cvRef = cardStack.getLast();
            cvRef.setInteraction(CardViewGroup.Interaction.NONE);
        }
        
        var newCardView = new CardViewGroup(newCard);
        newCardView.moveTo(cardSourceX, cardSourceY);
        newCardView.shiftBy(CARD_OFFSET*cardStack.size()-cardSourceX,0.0-cardSourceY,
                CardViewGroup.FAST_ANIM_DUR);
        newCardView.setInteraction(CardViewGroup.Interaction.DRAGGABLE);
        newCardView.toFront();
        cardStack.add(newCardView);
        getChildren().add(newCardView);
    }
    
    public Card remove_top_card(Card newCard) {
        CardViewGroup toremove = cardStack.removeLast();
        getChildren().remove(toremove);
        if (cardStack.size() > 0) {
            var nextTopCard = cardStack.getLast();
            nextTopCard.setInteraction(CardViewGroup.Interaction.DRAGGABLE);
        }
        // add a card to the back
        if (newCard != null) {
            var newCardView = new CardViewGroup(newCard);
            newCardView.moveTo(0, 0);
            for (CardViewGroup cvg : cardStack) {
                cvg.shiftBy(CARD_OFFSET, 0.0);
            }
            cardStack.addFirst(newCardView);
            getChildren().add(newCardView);
            newCardView.toBack();
            emptySpot.toBack();
            
        }
        
        return toremove.getCard();
    }
    
    // position needs to be given in relative terms to the waste
    // i.e. top left corner of waste is (0,0)
    public void setCardSource(double x, double y) {
        cardSourceX = x;
        cardSourceY = y;
    }
    
    public void animatedRemoveAll() {
        for (CardViewGroup toRemove : cardStack) {
            toRemove.moveTo(cardSourceX, cardSourceY,
                    CardViewGroup.FAST_ANIM_DUR, this::removeAll);
        }
    }
    
    public void removeAll() {
        for (CardViewGroup toRemove : cardStack) {
            getChildren().remove(toRemove);
        }
        cardStack.clear();
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
