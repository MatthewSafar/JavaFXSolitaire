package com.mycompany.mysolitaire;

import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import static javafx.scene.transform.Transform.translate;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * CardView
 * An extension of an ImageView that is used for card-shaped objects.
 * Stores a "represented" card, but has wider functionality than that. 
 * When it does not represent a real card, will return null to getCard().
 * 
 * @author matth
 */
public class CardView extends ImageView implements MovementPlacement{
    private Card represented;
    
    public static final double DEF_WIDTH = 120.0;
    public static final double H_W_RATIO = 1065.0 / 769.0;
    
    
    public CardView() {
        super();
        represented = null;
        setPreserveRatio(true);
        setSmooth(true);
        setFitWidth(DEF_WIDTH);
    }
    
    public CardView(Image thisImage) {
        super(thisImage);
        represented = null;
        setPreserveRatio(true);
        setSmooth(true);
        setFitWidth(DEF_WIDTH);
    }
    
    public CardView(Card newCard) {
        super(App.settings.deckInfo.getCardImage(newCard));
        represented = newCard;
        setPreserveRatio(true);
        setSmooth(true);
        setFitWidth(DEF_WIDTH);
    }
    
    public void setCard(Card newCard) {
        represented = newCard;
        setImage(App.settings.deckInfo.getCardImage(newCard));
    }
    
    public Card getCard() {
        return represented;
    }
    
    @Override
    public void moveTo(double x, double y) {
        relocate(x,y);
    }
    
    
    @Override
    public void shiftBy(double x, double y) {
        relocate(getLayoutX() + x,getLayoutY() + y);
    }
    
}
