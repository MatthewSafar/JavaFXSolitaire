package com.mycompany.mysolitaire;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author matth
 */
public class SolitairePane extends Pane {
    public SolitaireDeck game;
    
    public CardView deck;
    public WasteView waste;
    public FoundationPileView[] foundations;
    public CardColumnView[] columns;
    public CardViewGroup[] playerColumns;
    
    public Image resetImage = new Image("file:./src/resources/uno_reverse.png");
    
    SolitairePane() {
        
        game = new SolitaireDeck();
        
        deck = new CardView(App.settings.deckInfo.getBackImage());
        deck.moveTo(10,20);
        
        waste = new WasteView();
        waste.moveTo(CardView.DEF_WIDTH + 10 + 10,20);
        
        foundations = new FoundationPileView[DeckInfo.Suits.values().length];
        for (DeckInfo.Suits suit : DeckInfo.Suits.values()) {
            var newfound = new FoundationPileView(suit);
            newfound.moveTo((CardView.DEF_WIDTH + 10)*(3+suit.ordinal()) + 10,20);
            foundations[suit.ordinal()] = newfound;
        }
        
        columns = new CardColumnView[SolitaireDeck.NUM_COLUMNS];
        for (int iter = 0; iter < SolitaireDeck.NUM_COLUMNS; iter++) {
            var newcol = new CardColumnView(iter);
            newcol.moveTo((CardView.DEF_WIDTH + 10)*iter + 10, 220);
            columns[iter] = newcol;
        }
        
        playerColumns = new CardViewGroup[SolitaireDeck.NUM_COLUMNS];
        for (int iter = 0; iter < SolitaireDeck.NUM_COLUMNS; iter++) {
            var newGroup = new CardViewGroup();
            newGroup.moveTo(columns[iter].getEndPosX(),columns[iter].getEndPosY());
            var firstCard = new CardViewGroup(game.getPlayerColumn(iter).get(0));
            newGroup.appendCardViewGroup(firstCard);
            playerColumns[iter] = newGroup;
        }

        deck.setOnMouseClicked((MouseEvent mouseEvent) -> {
            Card nextCard = game.nextDeckCard();
            
            if (game.getDeckSize() == 0) {
                deck.setImage(resetImage);
            } else if (deck.getImage() == resetImage) {
                deck.setImage(App.settings.deckInfo.getBackImage());
            }
            if (nextCard != null) {
                waste.add_card(nextCard);
            }
        });
        
        setOnDropEvent(event -> onDropEvent(event.getTarget(), event.getPosX(),event.getPosY()));
        
        
        getChildren().addAll(columns);
        getChildren().add(deck);
        getChildren().addAll(foundations);
        getChildren().addAll(playerColumns);
        getChildren().add(waste);
        
        
        
        setStyle("-fx-background-color: darkgreen; -fx-text-fill: white;");
    }
    
    public final void setOnDropEvent(EventHandler<? super DropEvent> value) {
        this.addEventHandler(DropEvent.DROP, value);
    }
    
    private void onDropEvent(Object source, double x, double y) {
        System.out.println("Drop Event fired successfully at (" + Double.toString(x) + ", " + Double.toString(y) + ")");
        System.out.println("Source: " + ((CardViewGroup)source).getCard().toString());
        System.out.println("is_leaf: " + Boolean.toString( ((CardViewGroup)source).is_leaf()));
        // do stuff
        var cvg = (CardViewGroup) source;
        Card toTest = ((CardViewGroup) source).getCard();
        var parent = ((Node) source).getParent();
        
        if (cvg.is_leaf()) {
            // check if within bounds of foundation piles
            for (FoundationPileView foundation : foundations) {
                if (game.canAddFoundationCard(foundation.getSuit(),toTest) && foundation.getBoundsInParent().contains(x,y)){
                    if (parent instanceof WasteView) { // coming from the waste
                        waste.remove_top_card();
                        game.move(SolitaireDeck.Pile.WASTE,SolitaireDeck.Pile.FOUNDATION, 0);
                    } else if (parent instanceof FoundationPileView) {
                        ((FoundationPileView) parent).remove_top_card(null);
                    } else if (parent instanceof CardViewGroup) {
                        ((CardViewGroup) parent).removeLowerGroup();
                    }
                    foundation.put_card_on_top(toTest);
                    
                }
            }
        }
        // check player columns
        for (CardViewGroup group : playerColumns) {
            if (group.bottomBoundsContains(x,y)) {
                if (parent instanceof WasteView) { // coming from the waste
                    waste.remove_top_card();
                    game.move(SolitaireDeck.Pile.WASTE,SolitaireDeck.Pile.COLUMN, 0);
                } else if (parent instanceof FoundationPileView) {
                    ((FoundationPileView) parent).remove_top_card(null);
                } else if (parent instanceof CardViewGroup) {
                    ((CardViewGroup) parent).removeLowerGroup();
                }
                group.getBottomGroup().appendCardViewGroup(cvg);
                //game.move(SolitaireDeck.Pile.WASTE,SolitaireDeck.Pile.FOUNDATION, 0);
            }
        }
        
        // check for card reveal
        if ((parent instanceof CardViewGroup) && (((CardViewGroup)parent).getTopGroup().is_leaf())) {
            // get index of playerColumn
            int whichCol = -1;
            for (int iter = 0; iter < playerColumns.length; iter++) {
                if (playerColumns[iter] == ((CardViewGroup)parent).getTopGroup()) {
                    whichCol = iter;
                    break;
                }
            }
            if (columns[whichCol].getCardCount() > 0) {
                columns[whichCol].removeTopCard();
                playerColumns[whichCol].moveTo(columns[whichCol].getEndPosX(),columns[whichCol].getEndPosY());
                Card revealedCard = game.revealColumnCard(whichCol);
                playerColumns[whichCol].appendCardViewGroup(new CardViewGroup(revealedCard)); 
            }
        }
    }
    
}
