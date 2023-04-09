/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mysolitaire;

import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author matth
 */
public class CardViewGroup extends Group implements MovementPlacement {
    private CardViewGroup lowerGroup;
    private CardView card;
    private double cardOffset = 20.0;
    private boolean is_base = true;
    private double persistentX;
    private double persistentY;
    
    public static enum Interaction {
        NONE, DRAGGABLE
    }
    
    public CardViewGroup(CardView thisCard) {
        card = thisCard;
        is_base = false;
        
        lowerGroup = null;
        
        getChildren().add(card);
        persistentX = 0;
        persistentY = 0;
    }
    
    public CardViewGroup(Card thisCard) {
        this(new CardView(thisCard));
    }
    
    public CardViewGroup() {
        card = new CardView();
        
        lowerGroup = null;
        
        Rectangle r = new Rectangle();
        r.setWidth(CardView.DEF_WIDTH);
        r.setHeight(CardView.DEF_WIDTH * CardView.H_W_RATIO);
        //r.setFill(Color.TRANSPARENT);
        
        getChildren().add(r);
                
        persistentX = 0;
        persistentY = 0;
    }
    
    public String toString() {
        if (is_base) {
            return "Base";
        } else {
            return getCard().toString();
        }
    }
    
    public Card getCard() {
        return card.getCard();
    }
    
    public CardView getCardView() {
        return card;
    }
    
    public Card getBottomCard() {
        if (is_leaf()) {
            return getCard();
        } else {
            return lowerGroup.getBottomCard();
        }
    }
    
    public CardViewGroup getTopGroup() {
        CardViewGroup c = this;
        Node p = c.getParent();
        while (p instanceof CardViewGroup) {
            c = (CardViewGroup) p;
            p = c.getParent();
        }
        
        return c;
    }
    
    public CardViewGroup getBottomGroup() {
        if (is_leaf()) {
            return this;
        } else {
            return lowerGroup.getBottomGroup();
        }
    }
    
    public boolean bottomBoundsContains(double x, double y) {
        CardViewGroup c = getTopGroup();
        Bounds topBounds = c.getBoundsInParent();
        
        return c.getBoundsInParent().contains(x,y);
    }
    
    public boolean appendCardViewGroup(CardViewGroup cvg) {
        if (lowerGroup == null) {
            lowerGroup = cvg;
            lowerGroup.moveTo(0, 0);
            if (!is_base) {
                lowerGroup.shiftBy(0, cardOffset);
            }
            lowerGroup.setInteraction(Interaction.DRAGGABLE);
            getChildren().add(lowerGroup);
            return true;
        } else {
            return false;
        }
    }
    
    public void removeLowerGroup() {
        getChildren().remove(lowerGroup);
        lowerGroup = null;
    }
    
    public boolean is_leaf() {
        return (lowerGroup == null);
    }
    
    public int getSize() {
        if (is_leaf()) {
            return 1;
        } else {
            return 1 + lowerGroup.getSize();
        }
    }
    
    public boolean is_base() {
        return is_base;
    }
    
    public boolean containsGroup(CardViewGroup cvg) {
        if (cvg == this) {
            return true;
        } else {
            if (is_leaf()) {
                return cvg == this;
            } else {
                return lowerGroup.containsGroup(cvg);
            }
        }
    }
    
    public void setInteraction(CardViewGroup.Interaction inter) {
        if (null != inter) switch (inter) {
        // clear mouse interactions
            case NONE:
                setOnMousePressed((MouseEvent mouseEvent) -> {
                    // do nothing    
                }); 
                setOnMouseReleased((MouseEvent mouseEvent) -> {
                    // do nothing    
                }); 
                setOnMouseDragged((MouseEvent mouseEvent) -> {
                    // do nothing    
                }); 
                setOnMouseEntered((MouseEvent mouseEvent) -> {
                    setCursor(Cursor.DEFAULT);  
                }); 
                break;
            case DRAGGABLE:
                setInteraction(CardViewGroup.Interaction.NONE);
                // allow the label to be dragged around.
                final Delta dragDelta = new Delta();
                setOnMousePressed((MouseEvent mouseEvent) -> {
                    // record a delta distance for the drag and drop operation.
                    dragDelta.x = getLayoutX() - mouseEvent.getSceneX();
                    dragDelta.y = getLayoutY() - mouseEvent.getSceneY();
                    setCursor(Cursor.MOVE);
                    Node c = (Node) getTopGroup();
                    Node p = c.getParent();
                    while (!(p instanceof SolitairePane)) {
                        c = p;
                        p = p.getParent();
                    }
                    c.toFront();
                    toFront();
                    mouseEvent.consume();
                }); 
                setOnMouseReleased((MouseEvent mouseEvent) -> {
                    setCursor(Cursor.HAND);
                    // this is a bit yikes
                    var p = getParent();
                    while (!(p instanceof SolitairePane)) {
                        p = p.getParent();
                    }
                    double absx = mouseEvent.getSceneX() - p.getBoundsInParent().getMinX();
                    double absy = mouseEvent.getSceneY() - p.getBoundsInParent().getMinY();
                    fireEvent(new DropEvent(absx,absy));
                    moveBack();
                    mouseEvent.consume();
                }); 
                setOnMouseDragged((MouseEvent mouseEvent) -> {
                    setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
                    setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
                    mouseEvent.consume();
                }); 
                setOnMouseEntered((MouseEvent mouseEvent) -> {
                    setCursor(Cursor.HAND);
                    mouseEvent.consume();
                }); 
                break;
            default:
                break;
        }
    }

    @Override
    public void moveTo(double x, double y) {
        relocate(x,y);
        persistentX = x;
        persistentY = y;
    }
    
    public void moveBack() {
        relocate(persistentX,persistentY);
    }
    
    @Override
    public void shiftBy(double x, double y) {
        relocate(getLayoutX() + x,getLayoutY() + y);
        persistentX = persistentX + x;
        persistentY = persistentY + y;
    }
    
}

class Delta {
    double x,y;
}
