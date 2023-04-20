package com.mycompany.mysolitaire;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
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
    
    SolitairePane() {
        
        game = new SolitaireDeck();
        
        deck = new CardView(App.settings.deckInfo.getBackImage());
        deck.moveTo(10,20);
        
        waste = new WasteView();
        waste.moveTo(CardView.DEF_WIDTH + 10 + 10,20);
        waste.setCardSource(-(CardView.DEF_WIDTH + 10), 0);
        
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
            newGroup.moveTo(columns[iter].getEndPosX(),columns[iter].getEndPosY(),CardViewGroup.START_ANIM_DUR);
            var firstCard = new CardViewGroup(game.getPlayerColumn(iter).getLast());
            newGroup.appendCardViewGroup(firstCard);
            playerColumns[iter] = newGroup;
        }

        deck.setOnMouseClicked((MouseEvent mouseEvent) -> {
            if (!(mouseEvent.getButton() == MouseButton.PRIMARY)) {
                return;
            }
            if (App.settings.isHardmode()) {
                Card[] nextCards = new Card[3];
                for (int iter = 0; iter < 3; iter++) {
                    nextCards[iter] = game.nextDeckCard();
                }

                System.out.println("dcards: " + Integer.toString(game.getDeckSize()));
                if (deck.getImage() == App.settings.deckInfo.deckRefreshImage) {
                    waste.animatedRemoveAll();
                } else {
                    waste.removeAll();
                }

                if (game.deckIsEmpty() && deck.getImage() != App.settings.deckInfo.deckRefreshImage) {
                    deck.setImage(App.settings.deckInfo.deckRefreshImage);
                } else if (deck.getImage() == App.settings.deckInfo.deckRefreshImage) {
                    game.resetDeck();
                    deck.setImage(App.settings.deckInfo.getBackImage());
                }


                for (int iter = 0; iter < 3; iter++) {
                    if (nextCards[iter] != null) {
                        waste.add_card(nextCards[iter]);
                    }
                }

            } else {
                Card nextCard = game.nextDeckCard();

                if (game.deckIsEmpty() && deck.getImage() != App.settings.deckInfo.deckRefreshImage) {
                    deck.setImage(App.settings.deckInfo.deckRefreshImage);
                } else if (deck.getImage() == App.settings.deckInfo.deckRefreshImage && !game.wasteIsEmpty()) {
                    game.resetDeck();
                    deck.setImage(App.settings.deckInfo.getBackImage());
                    waste.animatedRemoveAll();
                }
                if (nextCard != null) {
                    waste.add_card(nextCard);
                }
            }
        });
        
        setOnDropEvent(event -> onDropEvent(event.getTarget(), event.getPosX(),event.getPosY()));
        setOnSearchMoveEvent(event -> onSearchMoveEvent(event.getTarget()));
        
        
        getChildren().addAll(columns);
        getChildren().add(deck);
        getChildren().addAll(foundations);
        getChildren().add(waste);
        getChildren().addAll(playerColumns);
        
        
        
        
        setStyle("-fx-background-color: rgb(9, 70, 9); -fx-text-fill: white;");
    }
    
    public final void setOnDropEvent(EventHandler<? super DropEvent> value) {
        this.addEventHandler(DropEvent.DROP, value);
    }
    
    public final void setOnSearchMoveEvent(EventHandler<? super SearchMoveEvent> value) {
        this.addEventHandler(SearchMoveEvent.SEARCHMOVE, value);
    }
    
    private void onDropEvent(Object source, double x, double y) {
        //System.out.println("Drop Event fired successfully at (" + Double.toString(x) + ", " + Double.toString(y) + ")");
        //System.out.println("Source: " + ((CardViewGroup)source).getCard().toString());
        //System.out.println("size: " + Integer.toString( ((CardViewGroup)source).getSize()));
        boolean successfulDrop = false;
        var cvg = (CardViewGroup) source;
        Card toTest = ((CardViewGroup) source).getCard();
        var parent = ((Node) source).getParent();
        
        if (cvg.is_leaf()) {
            // check if within bounds of foundation piles
            for (FoundationPileView foundation : foundations) {
                int toWhich = foundation.getSuit().ordinal();
                if (game.canAddFoundationCard(toWhich,toTest) && 
                        foundation.getBoundsInParent().contains(x,y) &&
                        foundation != parent){
                    if (parent instanceof WasteView) { // coming from the waste
                        waste.remove_top_card(game.nextWasteCard());
                        game.move(SolitaireDeck.Pile.WASTE,SolitaireDeck.Pile.FOUNDATION, toWhich);
                    } else if (parent instanceof CardViewGroup) {
                        var pcvg = ((CardViewGroup) parent);
                        int fromWhich = getColumnIndex(pcvg);
                        pcvg.removeLowerGroup();
                        game.move(SolitaireDeck.Pile.COLUMN, SolitaireDeck.Pile.FOUNDATION, fromWhich, toWhich);
                    }
                    foundation.put_card_on_top(toTest);
                    successfulDrop = true;
                }
            }
        }
        // check player columns
        for (CardViewGroup group : playerColumns) {
            int toWhich = getColumnIndex(group);
            if (game.canAddColumnCard(toWhich, toTest) && 
                    group.bottomBoundsContains(x,y) &&
                    group != cvg.getTopGroup()) {
                
                if (parent instanceof WasteView) { // coming from the waste
                    game.move(SolitaireDeck.Pile.WASTE,SolitaireDeck.Pile.COLUMN, toWhich);
                    waste.remove_top_card(game.nextWasteCard());
                } else if (parent instanceof FoundationPileView) {
                    var pfpv = ((FoundationPileView) parent);
                    game.move(SolitaireDeck.Pile.FOUNDATION, SolitaireDeck.Pile.COLUMN, pfpv.getSuit().ordinal(), toWhich);
                    pfpv.remove_top_card(game.getNextFoundationCard(pfpv.getSuit()));
                } else if (parent instanceof CardViewGroup) {
                    var pcvg = ((CardViewGroup) parent);
                    pcvg.removeLowerGroup();
                    int fromWhich = getColumnIndex(pcvg);
                    game.move(SolitaireDeck.Pile.COLUMN, SolitaireDeck.Pile.COLUMN, fromWhich, toWhich, cvg.getSize());
                }
                group.getBottomGroup().appendCardViewGroup(cvg);
                successfulDrop = true;
            }
        }
        
        // check for card reveal
        if ((parent instanceof CardViewGroup) && (((CardViewGroup)parent).getTopGroup().is_leaf())) {
            // get index of playerColumn
            int whichCol = getColumnIndex((CardViewGroup) parent);
            if (columns[whichCol].getCardCount() > 0) {
                columns[whichCol].removeTopCard();
                playerColumns[whichCol].moveTo(columns[whichCol].getEndPosX(),columns[whichCol].getEndPosY());
                Card revealedCard = game.revealColumnCard(whichCol);
                playerColumns[whichCol].appendCardViewGroup(new CardViewGroup(revealedCard));
            }
        }
        
        if (!successfulDrop) {
            cvg.moveBack();
        }
    }
    
    // look for a pile to put card on. put it there if possible
    // TODO: add animation (if possible);
    private void onSearchMoveEvent(Object source) {
        var cvg = (CardViewGroup) source;
        Card toTest = ((CardViewGroup) source).getCard();
        var parent = ((Node) source).getParent();
        
        boolean successfulmove = false;
        double originX = cvg.getXInPane();
        double originY = cvg.getYInPane();
        
        if (cvg.is_leaf()) {
            // check if within bounds of foundation piles
            for (FoundationPileView foundation : foundations) {
                int toWhich = foundation.getSuit().ordinal();
                if (foundation != parent && game.canAddFoundationCard(toWhich,toTest)){
                    if (parent instanceof WasteView) { // coming from the waste
                        waste.remove_top_card(game.nextWasteCard());
                        game.move(SolitaireDeck.Pile.WASTE,SolitaireDeck.Pile.FOUNDATION, toWhich);
                    } else if (parent instanceof CardViewGroup) {
                        var pcvg = ((CardViewGroup) parent);
                        int fromWhich = getColumnIndex(pcvg);
                        pcvg.removeLowerGroup();
                        game.move(SolitaireDeck.Pile.COLUMN, SolitaireDeck.Pile.FOUNDATION, fromWhich, toWhich);
                    }
                    CardViewGroup newCVG = foundation.put_card_on_top(toTest);
                    foundation.toFront();
                    newCVG.moveFrom(foundation.getXInPane() - originX, foundation.getYInPane() - originY,
                           CardViewGroup.FAST_ANIM_DUR);
                    successfulmove = true;
                    break;
                }
            }
        }
        
        // check player columns
        if (!successfulmove) {
            for (CardViewGroup group : playerColumns) {
                int toWhich = getColumnIndex(group);
                if (group != cvg.getTopGroup() && game.canAddColumnCard(toWhich, toTest)) {

                    if (parent instanceof WasteView) { // coming from the waste
                        game.move(SolitaireDeck.Pile.WASTE,SolitaireDeck.Pile.COLUMN, toWhich);
                        waste.remove_top_card(game.nextWasteCard());
                    } else if (parent instanceof FoundationPileView) {
                        var pfpv = ((FoundationPileView) parent);
                        game.move(SolitaireDeck.Pile.FOUNDATION, SolitaireDeck.Pile.COLUMN, pfpv.getSuit().ordinal(), toWhich);
                        pfpv.remove_top_card(game.getNextFoundationCard(pfpv.getSuit()) );
                    } else if (parent instanceof CardViewGroup) {
                        var pcvg = ((CardViewGroup) parent);
                        pcvg.removeLowerGroup();
                        int fromWhich = getColumnIndex(pcvg);
                        game.move(SolitaireDeck.Pile.COLUMN, SolitaireDeck.Pile.COLUMN, fromWhich, toWhich, cvg.getSize());
                    }
                    group.getBottomGroup().appendCardViewGroup(cvg);
                    group.toFront();
                    cvg.moveFrom(cvg.getXInPane() - originX, cvg.getYInPane() - originY,
                           CardViewGroup.FAST_ANIM_DUR);
                    successfulmove = true;
                    break;
                }
            }
        }

        // check for card reveal
        if ((parent instanceof CardViewGroup) && (((CardViewGroup)parent).getTopGroup().is_leaf())) {
            // get index of playerColumn
            int whichCol = getColumnIndex((CardViewGroup) parent);
            if (columns[whichCol].getCardCount() > 0) {
                columns[whichCol].removeTopCard();
                playerColumns[whichCol].moveTo(columns[whichCol].getEndPosX(),columns[whichCol].getEndPosY());
                Card revealedCard = game.revealColumnCard(whichCol);
                playerColumns[whichCol].appendCardViewGroup(new CardViewGroup(revealedCard));
            }
        }
    }
    
    private int getColumnIndex(CardViewGroup cvg) {
        int whichCol = -1;
        for (int iter = 0; iter < playerColumns.length; iter++) {
            if (playerColumns[iter] == cvg.getTopGroup()) {
                whichCol = iter;
                break;
            }
        }
        return whichCol;
    }
    
}
