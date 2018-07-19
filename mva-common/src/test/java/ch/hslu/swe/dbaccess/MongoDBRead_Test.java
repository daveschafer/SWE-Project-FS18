/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.dbaccess;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.jupiter.api.DisplayName;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

/**
 *
 * @author Dave
 */
public class MongoDBRead_Test {

    public MongoDBRead_Test() {
    }

    private static final Logger LOG = LogManager.getLogger(MongoDBRead_Test.class);
    private static MongoDBRead reader;
    private static JSONObject referenceJO;
    private static JSONArray referenceJA;
    private static final String testCollection = "testCollection0001";

    @BeforeClass
    public static void setUp() throws JSONException {
        MongoDBConnection.setMongoDBDatabase("your-mongo-server-hostname", 27017);
        reader = new MongoDBRead();
        //Creating some Reference JSON Data
        referenceJO = createJSONObject();
        referenceJA = createJSONArray();
        //creating some reference Collections
        MongoDBManageCollection manager = new MongoDBManageCollection();
        manager.AddArray2Collection(testCollection, referenceJA);
    }

    @AfterClass
    public static void tearDown() {
        //Deleting reference Collections...
        MongoDBConnection.getDatabase().getCollection(testCollection).drop();
    }

    @Test
    @DisplayName("Test: Kontrolliert ob eine korrekte MongoCollection zurückgegeben wird.")
    public void ReadALL_Test() {
        assertEquals((long) referenceJA.length(), reader.ReadALL(testCollection).count()); //Anzahl Elemente müssen gleich sein
        FindIterable<Document> findings = reader.ReadALL(testCollection).find();
        assertNotNull(findings.first()); //Wurde überhaupt etwas gefunden?
        for (Document doc : findings) {
            assertTrue(doc.getString("information").equals("test"));
            assertTrue(doc.getInteger("id") == 3);
            assertTrue(doc.getString("name").equals("course1"));
            return; //return nach einem Durchlauf reicht
        }
    }

    @Test
    @DisplayName("Test: Kontrolliert ob eine korrekte Arraylist zurückgegeben wird.")
    public void ReadAll2Arraylist_Test() {
        ArrayList<Document> exampleArLi = reader.ReadAll2Arraylist(testCollection);
        assertFalse(exampleArLi.isEmpty()); //schauen ob AL nicht leer
        assertEquals(exampleArLi.size(), referenceJA.length());
        for (Document doc : exampleArLi) {
            assertTrue(doc.getString("information").equals("test"));
            assertTrue(doc.getInteger("id") == 3);
            assertTrue(doc.getString("name").equals("course1"));
            return; //return nach einem Durchlauf reicht
        }
    }

    @Test
    public void ReadAll2JSONArray_Test() throws JSONException {
        JSONArray exJAfromDB = reader.ReadAll2JSONArray(testCollection);
        JSONAssert.assertEquals(referenceJA, exJAfromDB, JSONCompareMode.LENIENT);  //Die beiden JSONArrays müssen gleich sein | Lenient Mode weill exJAfromDB noch zusätzliche ID Felder hat
    }

    @Test
    public void ReadEntry_Test() {
        ArrayList<Document> exampleArLi = reader.ReadEntry(testCollection, 3); //muss min. ein Eintrag finden mit dieser ID
        assertFalse(exampleArLi.isEmpty()); //schauen ob AL nicht leer

        for (Document doc : exampleArLi) {
            assertTrue(doc.getString("information").equals("test"));
            assertTrue(doc.getInteger("id") == 3);
            assertTrue(doc.getString("name").equals("course1"));
            return; //return nach einem Durchlauf reicht
        }
    }

    @Test
    public void SearchEntries_Test() {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("information", "test");
        ArrayList<Document> exampleArLi = reader.SearchEntries(testCollection, searchQuery);
        assertFalse(exampleArLi.isEmpty()); //schauen ob AL nicht leer

        for (Document doc : exampleArLi) {
            assertTrue(doc.getString("information").equals("test"));
            assertTrue(doc.getInteger("id") == 3);
            assertTrue(doc.getString("name").equals("course1"));
            return; //return nach einem Durchlauf reicht
        }
    }

    //Helpers für Referenzdaten
    private static JSONObject createJSONObject() throws JSONException {
        JSONObject item = new JSONObject();
        item.put("information", "test");
        item.put("id", 3);
        item.put("name", "course1");
        return item;
    }

    private static JSONArray createJSONArray() throws JSONException {
        JSONArray array = new JSONArray();
        array.put(createJSONObject());
        array.put(createJSONObject());
        array.put(createJSONObject());
        return array;
    }
}
