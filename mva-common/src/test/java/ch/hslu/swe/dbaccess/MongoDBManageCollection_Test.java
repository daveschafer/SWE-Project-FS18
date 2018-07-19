/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.dbaccess;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.jupiter.api.DisplayName;

/**
 *
 * @author Dave
 */
public class MongoDBManageCollection_Test {

    public MongoDBManageCollection_Test() {
    }

    private static final Logger LOG = LogManager.getLogger(MongoDBManageCollection_Test.class);

    private static MongoDBManageCollection manager;
    private static MongoDBRead reader;
    private static JSONObject referenceJO;
    private static JSONArray referenceJA;
    private static final String testCollection = "testCollection0001";

    @BeforeClass
    public static void setUpClass() throws JSONException {
        MongoDBConnection.setMongoDBDatabase("your-mongo-server-hostname", 27017);
        manager = new MongoDBManageCollection();
        reader = new MongoDBRead();
        //Creating some Reference JSON Data
        referenceJO = createJSONObject(3);
        referenceJA = createJSONArray();
        //creating some reference Collections
        manager = new MongoDBManageCollection();
        MongoDBConnection.getDatabase().getCollection(testCollection).drop();
    }

    @AfterClass
    public static void tearDownClass() {
        MongoDBConnection.getDatabase().getCollection(testCollection).drop();
    }

    @Before
    public void setupDBCollection() {
        manager.AddArray2Collection(testCollection, referenceJA); //Test collection erstellen
    }

    @After
    public void dropDBCollection() {
        MongoDBConnection.getDatabase().getCollection(testCollection).drop();
    }

    @Test
    @DisplayName("Test: Kontrolliert ob einzelne Einträge in eine Collection hinzugefügt werden können")
    public void AddEntry_Test() throws JSONException {
        JSONObject newJO = new JSONObject();
        newJO.put("id", 99);
        newJO.put("name", "test99");
        newJO.put("information", "thespanishinquisition");

        manager.AddEntry(testCollection, newJO);
        ArrayList<Document> entryDB = reader.ReadEntry(testCollection, 99);
        assertFalse(entryDB.isEmpty()); //Test ob überhaupt etwas gefunden wurde
        for (Document doc : entryDB) {
            if (doc.get("id").equals(99)) {
                assertTrue(doc.get("name").equals("test99")); // Test ob beide Elemente im Array sind
                assertTrue(doc.get("information").equals("thespanishinquisition")); // Test ob beide Elemente im Array sind
            }
        }
    }

    @Test
    @DisplayName("Test: Kontrolliert ob ein Array von Eintägen in eine Collection eingefügt werden können")
    public void AddArray2Collection_Test() throws JSONException {
        JSONArray newJA = new JSONArray();
        JSONObject newJO = new JSONObject();
        newJO.put("id", 99);
        newJO.put("name", "test99");
        newJO.put("information", "thespanishinquisition");

        newJA.put(newJO);
        newJA.put(newJO);
        newJA.put(newJO);

        manager.AddArray2Collection(testCollection, newJA);
        ArrayList<Document> entryDB = reader.ReadEntry(testCollection, 99);
        assertFalse(entryDB.isEmpty()); //Test ob überhaupt etwas gefunden wurde
        int countNewJo = 0;
        for (Document doc : entryDB) {
            if (doc.get("id").equals(99)) {
                countNewJo++;
                assertTrue(doc.get("name").equals("test99")); // Test ob beide Elemente im Array sind
                assertTrue(doc.get("information").equals("thespanishinquisition")); // Test ob beide Elemente im Array sind
            }
        }
        assertTrue(countNewJo == 3); //Überprüfe ob 3 Einträge gefunden wurden
    }

    @Test
    @DisplayName("Test: Kontrolliert ob ein einzelner *bestehender* Eintrag in einer Collection aktualisiert werden kann")
    public void UpdateCollectionEntry_updateExisting_Test() throws JSONException {
        JSONObject newJO = new JSONObject();
        newJO.put("id", 3);
        newJO.put("name", "test88update");
        newJO.put("information", "thespanishinquisitionupdate");

        //Test 1 bestehenden Eintrag aktualisieren
        manager.UpdateCollectionEntry(testCollection, newJO);
        ArrayList<Document> entryDB = reader.ReadEntry(testCollection, 3); //Diese anfrage soltle dafür sorgen dass alle IDs mit 3 zurückkommen
        assertFalse(entryDB.isEmpty()); //Test ob überhaupt etwas gefunden wurde

        assertTrue(reader.ReadALL(testCollection).count() == 3); ///es dürfen nicht mehr als 1 objekte vorhanden sein

        for (Document doc : entryDB) {
            if (doc.get("id").equals(3)) {
                if (doc.get("name").equals("test88update")) {
                    assertTrue(doc.get("information").equals("thespanishinquisitionupdate")); // Test ob beide Elemente im Array sind
                }
            }
        }
    }

    @Test
    @DisplayName("Test: Kontrolliert ob ein einzelner *nicht bestehender* Eintrag in einer Collection eingefügt werden kann")
    public void UpdateCollectionEntry_updateNotExisting_Test() throws JSONException {
        JSONObject newJO = new JSONObject();
        newJO.put("id", 77);
        newJO.put("name", "test77_update");
        newJO.put("information", "thespanishinquisition_new");

        //Test 2 - Eintrag aktualisieren den es nicht gibt, ergo wird ein neuer erstell
        manager.UpdateCollectionEntry(testCollection, newJO);
        ArrayList<Document> entryDB = reader.ReadEntry(testCollection, 77); //Diese anfrage soltle dafür sorgen dass alle IDs mit 3 zurückkommen
        assertFalse(entryDB.isEmpty()); //Test ob überhaupt etwas gefunden wurde

        assertTrue(reader.ReadALL(testCollection).count() == 4); //neu sollten 4 Elemente vorhanden sein in der DB

        for (Document doc : entryDB) {
            if (doc.get("id").equals(77)) {
                if (doc.get("name").equals("test77_update")) {
                    assertTrue(doc.get("information").equals("thespanishinquisition_new")); // Test ob beide Elemente im Array sind
                }
            }
        }

    }

    @Test
    @DisplayName("Test: Kontrolliert ob ein Array *bestehender* Einträge in einer Collection aktualisiert werden können")
    public void UpdateCollectionALL_updateExisting_Test() throws JSONException {
        JSONArray newJA = new JSONArray();
        JSONObject newJO = new JSONObject();
        newJO.put("id", 1);
        newJO.put("name", "test111_1");
        newJO.put("information", "thespanishinquisitionupdate");

        newJA.put(newJO);
        JSONObject newJo2 = new JSONObject();
        newJo2.put("id", 2);
        newJo2.put("name", "test111_2");
        newJo2.put("information", "thespanishinquisitionupdate");
        newJA.put(newJo2);

        JSONObject newJo3 = new JSONObject();
        newJo3.put("id", 3);
        newJo3.put("name", "test111_3");
        newJo3.put("information", "thespanishinquisitionupdate");
        newJA.put(newJo3);

        manager.UpdateCollectionALL(testCollection, newJA);
        ArrayList<Document> entryDB = reader.ReadAll2Arraylist(testCollection);
        assertFalse(entryDB.isEmpty()); //Test ob überhaupt etwas gefunden wurde
        for (Document doc : entryDB) {
            if (doc.get("id").equals(1)) {
                assertTrue(doc.get("name").equals("test111_1")); // Test ob beide Elemente im Array sind
                assertTrue(doc.get("information").equals("thespanishinquisitionupdate")); // Test ob beide Elemente im Array sind
            } else if (doc.get("id").equals(2)) {
                assertTrue(doc.get("name").equals("test111_2")); // Test ob beide Elemente im Array sind
                assertTrue(doc.get("information").equals("thespanishinquisitionupdate")); // Test ob beide Elemente im Array sind
            } else if (doc.get("id").equals(3)) {
                assertTrue(doc.get("name").equals("test111_3")); // Test ob beide Elemente im Array sind
                assertTrue(doc.get("information").equals("thespanishinquisitionupdate")); // Test ob beide Elemente im Array sind
            }
        }
        assertTrue(reader.ReadALL(testCollection).count() == 3); //es sollten nicht mehr als 3 Elemente vorhanden sein in der DB
    }

    public void UpdateCollectionALL_updateNotExisting_Test() throws JSONException {
        JSONArray newJA = new JSONArray();
        JSONObject newJO = new JSONObject();
        newJO.put("id", 31);
        newJO.put("name", "test222_1");
        newJO.put("information", "thespanishinquisitionupdate");

        newJA.put(newJO);
        JSONObject newJo2 = new JSONObject();
        newJo2.put("id", 32);
        newJo2.put("name", "test222_2");
        newJo2.put("information", "thespanishinquisitionupdate");
        newJA.put(newJo2);

        JSONObject newJo3 = new JSONObject();
        newJo3.put("id", 33);
        newJo3.put("name", "test222_3");
        newJo3.put("information", "thespanishinquisitionupdate");
        newJA.put(newJo3);

        manager.UpdateCollectionALL(testCollection, newJA);
        ArrayList<Document> entryDB = reader.ReadAll2Arraylist(testCollection);
        assertFalse(entryDB.isEmpty()); //Test ob überhaupt etwas gefunden wurde
        for (Document doc : entryDB) {
            if (doc.get("id").equals(31)) {
                assertTrue(doc.get("name").equals("test111_1")); // Test ob beide Elemente im Array sind
                assertTrue(doc.get("information").equals("thespanishinquisitionupdate")); // Test ob beide Elemente im Array sind
            } else if (doc.get("id").equals(32)) {
                assertTrue(doc.get("name").equals("test111_2")); // Test ob beide Elemente im Array sind
                assertTrue(doc.get("information").equals("thespanishinquisitionupdate")); // Test ob beide Elemente im Array sind
            } else if (doc.get("id").equals(33)) {
                assertTrue(doc.get("name").equals("test111_3")); // Test ob beide Elemente im Array sind
                assertTrue(doc.get("information").equals("thespanishinquisitionupdate")); // Test ob beide Elemente im Array sind
            } else if (doc.get("id").equals(1) | doc.get("id").equals(2) | doc.get("id").equals(3)) {
                assertTrue(doc.get("name").equals("course1")); // Test ob beide Elemente im Array sind
                assertTrue(doc.get("information").equals("test")); // Test ob beide Elemente im Array sind
            }
        }
        assertTrue(reader.ReadALL(testCollection).count() == 6); //es sollten neu 6 Einträge sein Elemente vorhanden sein in der DB
    }

    ///
    //Helpers für Referenzdaten
    ///
    private static JSONObject createJSONObject(int id) throws JSONException {
        JSONObject item = new JSONObject();
        item.put("information", "test");
        item.put("id", id);
        item.put("name", "course1");
        return item;
    }

    private static JSONArray createJSONArray() throws JSONException {
        JSONArray array = new JSONArray();
        array.put(createJSONObject(1));
        array.put(createJSONObject(2));
        array.put(createJSONObject(3));
        return array;
    }
}
