/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mysolitaire;

import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 *
 * @author matth
 */
public class CardViewGroup extends Group implements MovementPlacement {
    public static final double FAST_ANIM_DUR = 100.0;
    public static final double START_ANIM_DUR = 500.0;
    
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
        
        card.toFront();
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
        r.setFill(Color.TRANSPARENT);
        
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
            y += p.getLayoutY();
            p = p.getParent();
        }
        return y;
    }
    
    public boolean bottomBoundsContains(double x, double y) {
        CardViewGroup bottom = getBottomGroup();
        double cornerX = bottom.getXInPane();
        double cornerY = bottom.getYInPane();
        
        boolean inX = (cornerX < x && x < cornerX + CardView.DEF_WIDTH);
        boolean inY = (cornerY < y && y < cornerY + CardView.DEF_WIDTH * CardView.H_W_RATIO);
        
        return (inX && inY);
    }
    
    public boolean appendCardViewGroup(CardViewGroup cvg) {
        if (lowerGroup == null) {
            lowerGroup = cvg;
            lowerGroup.relocate(0, 0);
            if (!is_base) {
                lowerGroup.shiftBy(0, cardOffset);
            }
            lowerGroup.setInteraction(Interaction.DRAGGABLE);
            lowerGroup.toFront();
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
                setOnMouseClicked((MouseEvent mouseEvent) -> {
                    // do nothing
                });
                break;
            case DRAGGABLE:
                setInteraction(CardViewGroup.Interaction.NONE);
                // allow the label to be dragged around.
                final Delta dragDelta = new Delta();
                setOnMousePressed((MouseEvent mouseEvent) -> {
                    if (!(mouseEvent.getButton() == MouseButton.PRIMARY)) {
                        return;
                    }
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
                    if (!(mouseEvent.getButton() == MouseButton.PRIMARY)) {
                        return;
                    }
                    setCursor(Cursor.HAND);
                    double absx = getXInPane() + CardView.DEF_WIDTH / 2;
                    double absy = getYInPane() + CardView.DEF_WIDTH * CardView.H_W_RATIO / 2;
                    fireEvent(new DropEvent(absx,absy));
                    mouseEvent.consume();
                }); 
                setOnMouseDragged((MouseEvent mouseEvent) -> {
                    if (!(mouseEvent.getButton() == MouseButton.PRIMARY)) {
                        return;
                    }
                    setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
                    setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
                    mouseEvent.consume();
                }); 
                setOnMouseEntered((MouseEvent mouseEvent) -> {
                    setCursor(Cursor.HAND);
                });
                setOnMouseClicked((MouseEvent mouseEvent) -> {
                    if ((mouseEvent.getButton() == MouseButton.SECONDARY) ||
                            ((mouseEvent.getButton()== MouseButton.PRIMARY) && mouseEvent.isShiftDown())) {
                        // create a "search to place" event
                        fireEvent(new SearchMoveEvent());
                        mouseEvent.consume();
                    }
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
    
    public void moveTo(double x, double y, double animDur) {
        TranslateTransition translate = new TranslateTransition();
        translate.setDuration(Duration.millis(animDur));
        translate.setNode(this);
        translate.setFromX(this.getLayoutX()-x);
        translate.setFromY(this.getLayoutY()-y);
        relocate(x,y);
        translate.setToX(0);
        translate.setToY(0);
        translate.play();

        persistentX = x;
        persistentY = y;
    }
    
    public void moveTo(double x, double y, double animDur, Runnable func) {
        TranslateTransition translate = new TranslateTransition();
        translate.setDuration(Duration.millis(animDur));
        translate.setNode(this);
        translate.setFromX(this.getLayoutX()-x);
        translate.setFromY(this.getLayoutY()-y);
        relocate(x,y);
        translate.setToX(0);
        translate.setToY(0);
        translate.setOnFinished(e -> func.run());
        translate.play();

        persistentX = x;
        persistentY = y;
    }
    
    public void moveBack() {
        moveTo(persistentX, persistentY, FAST_ANIM_DUR);
    }
    
    public void moveFrom(double x, double y, double animDur) {
        TranslateTransition translate = new TranslateTransition();
        translate.setDuration(Duration.millis(animDur));
        translate.setNode(this);
        translate.setFromX(0-x);
        translate.setFromY(0-y);
        translate.setToX(0);
        translate.setToY(0);
        translate.play();
    }
    
    @Override
    public void shiftBy(double x, double y) {
        relocate(getLayoutX() + x,getLayoutY() + y);
        persistentX = persistentX + x;
        persistentY = persistentY + y;
    }
    
    public void shiftBy(double x, double y, double animDur) {
        moveTo(getLayoutX() + x,getLayoutY() + y,animDur);
    }
    
}

class Delta {
    double x,y;
}
