/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.DataCollector;

/**
 *
 * @author Dave
 */
public enum CollectionTyp {
    LIEFERUNG,
    BESTELLUNG,
    PRODUKT,
    MOEBELHAUS;
    
    
    public String returnSubURL() {
        switch (this) {
            case LIEFERUNG:
                return "/ws/lieferung/";
            case BESTELLUNG:
                return "/ws/bestellung/";
            case PRODUKT:
                return "/ws/katalog/";
            case MOEBELHAUS:
                return "/ws/moebelhaus/";
            default:
                throw new IllegalArgumentException("Möbelhersteller nicht gefunden");
        }
    }
    
     @Override
    public String toString() {
        switch (this) {
            case LIEFERUNG:
                return "Lieferungen";
            case BESTELLUNG:
                return "Bestellungen";
            case PRODUKT:
                return "Produkte";
            case MOEBELHAUS:
                return "MH";
            default:
                throw new IllegalArgumentException("CollectionType nicht gefunden!");
        }
    }
    
}
