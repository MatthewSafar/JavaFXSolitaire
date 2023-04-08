/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mysolitaire;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author matth
 */
public class CardColumnView extends Group implements MovementPlacement{
    private int cardCount;
    private double cardSpacing = 30.0;
    
    public CardColumnView(int init_size) {
        cardCount = init_size;
        
        Rectangle r = new Rectangle();
        r.setWidth(CardView.DEF_WIDTH);
        r.setHeight(CardView.DEF_WIDTH * CardView.H_W_RATIO);
        r.setFill(Color.TRANSPARENT);
        getChildren().add(r);
                
        if (init_size == 0) {
            getChildren().add(new CardView());
        }
        
        for (int iter=0; iter < cardCount; iter++) {
            var newCard = new CardView(App.settings.deckInfo.getBackImage());
            newCard.shiftBy(0,cardSpacing*iter);
            getChildren().add(newCard);
            
        }
    }
    
    public void removeTopCard() {
        getChildren().remove(getChildren().size() - 1);
        cardCount += -1;
    }
    
    public int getCardCount() {
        return cardCount;
    }
    
    public double getYLength() {
        if (cardCount == 0) {
            return 0;
        } else {
            return (cardCount - 1) * cardSpacing + CardView.DEF_WIDTH * CardView.H_W_RATIO;
        }
    }
    
    public double getEndPosX() {
        return getBoundsInParent().getMinX();
    }
    
    public double getEndPosY() {
        return getBoundsInParent().getMinY() + cardCount * cardSpacing;
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
