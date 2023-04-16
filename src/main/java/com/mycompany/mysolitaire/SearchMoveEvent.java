/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mysolitaire;

import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author matth
 */
public class SearchMoveEvent extends Event {

    public static final EventType<SearchMoveEvent> SEARCHMOVE = new EventType(ANY,"SEARCHMOVE");
    public SearchMoveEvent() {
        super(SearchMoveEvent.SEARCHMOVE);
    }
}
