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
public class DropEvent extends Event {
    private final double posX;
    private final double posY;
    
    public static final EventType<DropEvent> DROP = new EventType(ANY,"DROP");
    public DropEvent(double posX, double posY) {
        super(DropEvent.DROP);
        
        this.posX = posX;
        this.posY = posY;
    }
    
    public double getPosX() {
        return posX;
    }
    
    public double getPosY() {
        return posY;
    }
    
}
