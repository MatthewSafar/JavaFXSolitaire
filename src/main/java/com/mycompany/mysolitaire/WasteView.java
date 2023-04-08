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
    private static final double CARD_OFFSET = 20.0;
    private static final int STACK_MAX_SIZE = 3;
    
    public WasteView() {
        cardStack = new ArrayDeque<CardViewGroup>();
        
        var emptySpot = new CardView(App.settings.deckInfo.getEmptyImage());
        getChildren().add(emptySpot);
    }
    
    public void add_card(Card newCard) {
        if (cardStack.size() == STACK_MAX_SIZE) {
            CardViewGroup toremove = cardStack.removeFirst();
            getChildren().remove(toremove);
            for (CardViewGroup c : cardStack) {
                c.shiftBy(-CARD_OFFSET,0);
            }
        }
        
        if (cardStack.size() > 0) {
            CardViewGroup cvRef = cardStack.getLast();
            cvRef.setInteraction(CardViewGroup.Interaction.NONE);
        }
        
        var newCardView = new CardViewGroup(newCard);
        newCardView.shiftBy(CARD_OFFSET*cardStack.size(),0.0);
        newCardView.setInteraction(CardViewGroup.Interaction.DRAGGABLE);
        newCardView.toFront();
        cardStack.add(newCardView);
        getChildren().add(newCardView);
    }
    
    public Card remove_top_card() {
        CardViewGroup toremove = cardStack.removeLast();
        getChildren().remove(toremove);
        if (cardStack.size() > 0) {
            var nextTopCard = cardStack.getLast();
            nextTopCard.setInteraction(CardViewGroup.Interaction.DRAGGABLE);
        }
        return toremove.getCard();
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
