/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.dbaccess;

import com.mongodb.MongoClientException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

/**
 *
 * @author Dave
 */
public class MongoDBConnection_Test {

    public MongoDBConnection_Test() {
    }

    @Before
    public void setUp() {
        //Hostname, Port und Datenbankname einer MongoDB Instanz hier spezifizieren       
        MongoDBConnection.setMongoDBDatabase("your-mongo-server-hostname", 27017); //Without DB

    }

    @Test
    @DisplayName("Test: Kontrolliert ob die Datenbank korrekt angebunden ist")
    public void getDatabase_Test() {
        assertTrue(MongoDBConnection.getDatabase().getName().equals("MVA_04")); //Check ob die richtige DB zurückgegeben wird
        assertFalse(MongoDBConnection.getDatabase().getName().equals("MVA_99"));
    }

    @Test
    @DisplayName("Test: Kontrolliert ob Collections korrekt abgefragt werden")
    public void checkIfCollectionExists_Test() {
        assertTrue(MongoDBConnection.checkIfCollectionExists("MH_Test"));
        assertFalse(MongoDBConnection.checkIfCollectionExists("MH_jksahkbfhsdfzdsfhvsd4rztfvjh"));
    }

    @Test
    public void setMongoDBDatabase_Test() {
        //illegal Settings
        Assertions.assertThrows(MongoClientException.class, () -> {
            MongoDBConnection.setMongoDBDatabase("your-mongo-server", 9999); //with wrong port for exception
        });
    }

}
