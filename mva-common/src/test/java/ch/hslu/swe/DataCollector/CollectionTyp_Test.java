/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.DataCollector;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.jupiter.api.DisplayName;

/**
 * Die meisten Tests sind Integrationstests.
 * 
 * @author Dave
 */
public class CollectionTyp_Test {

    public CollectionTyp_Test() {
    }

    @Test
    @DisplayName("Test01 : Kontrolliert die Integrität des Enumerators")
    public void CollectionTyp_Test() {
        CollectionTyp ct = CollectionTyp.BESTELLUNG;
        assertTrue((ct.toString().equals("Bestellungen"))); //Bestellungen
        assertTrue((ct.returnSubURL().equals("/ws/bestellung/"))); //Bestellungen
        ct = CollectionTyp.LIEFERUNG;
        assertTrue((ct.toString().equals("Lieferungen"))); //Lieferung
        assertTrue((ct.returnSubURL().equals("/ws/lieferung/"))); //Lieferung
        ct = CollectionTyp.PRODUKT;
        assertTrue((ct.toString().equals("Produkte"))); //Lieferung
        assertTrue((ct.returnSubURL().equals("/ws/katalog/"))); //Lieferung
        ct = CollectionTyp.MOEBELHAUS;
        assertTrue((ct.toString().equals("MH"))); //Lieferung
        assertTrue((ct.returnSubURL().equals("/ws/moebelhaus/"))); //Lieferung
    }
}
