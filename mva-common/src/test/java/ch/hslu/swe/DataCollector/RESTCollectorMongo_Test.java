/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.DataCollector;

import ch.hslu.swe.dbaccess.MongoDBConnection;
import ch.hslu.swe.dbaccess.MongoDBRead;
import ch.hslu.swe.helper.MoebelherstellerEnum;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.jupiter.api.DisplayName;

/**
 *  * Die meisten Tests sind Integrationstests.
 *
 * @author Dave
 */
public class RESTCollectorMongo_Test {

    private static final Logger LOG = LogManager.getLogger(RESTCollectorMongo_Test.class);
    private RESTCollectorMongo restcollector = new RESTCollectorMongo(MoebelherstellerEnum.TEST);

    public RESTCollectorMongo_Test() {
    }

    @BeforeClass
    public static void setUpClass() {
        //Verbinde zu junit Test DB
        //Hostname, Port und Datenbankname einer MongoDB Instanz hier spezifizieren       
        MongoDBConnection.setMongoDBDatabase("your-mongo-server-hostname", 27017, "yourmongoDBName_forTesting");

        //Drop alle bestehenden Collections
        dropAllCollections();

        //Möbelhäuser importieren
        RESTCollectorMongo rpre = new RESTCollectorMongo(MoebelherstellerEnum.TEST);
        rpre.importAllMoebelhauser();
    }

    @AfterClass
    public static void tearDownClass() {
        //Drop alle bestehenden Collections
        dropAllCollections();

        //Hostname, Port und Datenbankname einer MongoDB Instanz hier spezifizieren       
        MongoDBConnection.setMongoDBDatabase("your-mongo-server-hostname", 27017, "yourmongoDBName");

    }

    private static void dropAllCollections() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        if (!database.getName().equals("MVA_04_junit")) {
            LOG.fatal("Nicht JUNIT Test DB! Kritisch!");
            return;
        }

        MongoIterable<String> matchings = database.listCollectionNames();
        for (Iterator<String> allCollections = matchings.iterator(); allCollections.hasNext();) {
            String collname = allCollections.next();
            if (collname.contains("_Test")) {
                database.getCollection(collname).drop();
            }
        }
        LOG.debug("Alle Collections der Test DB gedroppt (Ausser System)");
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    @DisplayName("Test 01.1 : Kontrolliert ob alle Bestellungen importiert werden.")
    public void importAllOrders_Test() {
        LOG.debug("Test 01 : importAllOrders_Test");
        // boolean importAllOrders_ret = restcollector.importAllOrders();
        //assertTrue(importAllOrders_ret);
        restcollector.importAllOrders();
        //Prüfe ob die Collections existieren.
        assertTrue(MongoDBConnection.checkIfCollectionExists("Bestellungen_MH_BUCHELI_HORW_Test"));
        assertTrue(MongoDBConnection.checkIfCollectionExists("Bestellungen_MH_CONF_SCHL_Test"));
        assertTrue(MongoDBConnection.checkIfCollectionExists("Bestellungen_MH_DIGA_EMME_Test"));
        assertTrue(MongoDBConnection.checkIfCollectionExists("Bestellungen_MH_EGGER_EBLU_Test"));
        assertTrue(MongoDBConnection.checkIfCollectionExists("Bestellungen_MH_FERR_HINW_Test"));
        assertTrue(MongoDBConnection.checkIfCollectionExists("Bestellungen_MH_HUBA_RTHI_Test"));
        assertTrue(MongoDBConnection.checkIfCollectionExists("Bestellungen_MH_INTE_PRAT_Test"));
        assertTrue(MongoDBConnection.checkIfCollectionExists("Bestellungen_MH_LIPO_EGER_Test"));
        assertTrue(MongoDBConnection.checkIfCollectionExists("Bestellungen_MH_MAER_PFSZ_Test"));
        assertTrue(MongoDBConnection.checkIfCollectionExists("Bestellungen_MH_MICA_IBAS_Test"));
        assertTrue(MongoDBConnection.checkIfCollectionExists("Bestellungen_MH_PFIS_SUHR_Test"));
        assertTrue(MongoDBConnection.checkIfCollectionExists("Bestellungen_MH_SCHU_ZHRI_Test"));
        assertTrue(MongoDBConnection.checkIfCollectionExists("Bestellungen_MH_TTIP_SPRE_Test"));
        //Prüfe auf False
        assertFalse(MongoDBConnection.checkIfCollectionExists("Bestellungen_irgendwas_Test"));
        //Suche noch einen bestimmten werd aus einer Collection
        MongoDBRead reader = new MongoDBRead();
        BasicDBObject searchquery = new BasicDBObject();
        searchquery.put("id", 687);
        ArrayList<Document> SearchEntries = reader.SearchEntries("Bestellungen_MH_BUCHELI_HORW_Test", searchquery);
        assertFalse(SearchEntries.isEmpty()); //darf nicht leer sein
        int int_id = SearchEntries.get(0).getInteger("id");
        assertEquals(687, int_id); // prüfe ob die richtige ID gefunden wurde
        LOG.debug("ID " + int_id + " wurde gefunden");
    }

    @DisplayName("Test01.2 : Kontrolliert ob alle Bestellungen in eine Collection zusammengefasst werden könne.")
    public void importAllOrdersAsOne_Test() {
//Maybe implement this
    }

    @Test
    @DisplayName("Test02 : Kontrolliert ob alle Lieferungen importiert werden")
    public void importAllDeliveries_Test() {
        LOG.debug("Test 02 : importAllDeliveries_Test");

        boolean importAllDeliveries_ret = restcollector.importAllDeliveries();
        assertTrue(importAllDeliveries_ret);
        assertTrue(MongoDBConnection.checkIfCollectionExists("Lieferungen_MH_BUCHELI_HORW_Test"));
        assertTrue(MongoDBConnection.checkIfCollectionExists("Lieferungen_MH_CONF_SCHL_Test"));
        assertTrue(MongoDBConnection.checkIfCollectionExists("Lieferungen_MH_DIGA_EMME_Test"));
        assertTrue(MongoDBConnection.checkIfCollectionExists("Lieferungen_MH_EGGER_EBLU_Test"));
        assertTrue(MongoDBConnection.checkIfCollectionExists("Lieferungen_MH_FERR_HINW_Test"));
        // assertTrue(MongoDBConnection.checkIfCollectionExists("Lieferungen_MH_HUBA_RTHI_Test"));
        assertTrue(MongoDBConnection.checkIfCollectionExists("Lieferungen_MH_INTE_PRAT_Test"));
        assertTrue(MongoDBConnection.checkIfCollectionExists("Lieferungen_MH_LIPO_EGER_Test"));
        assertTrue(MongoDBConnection.checkIfCollectionExists("Lieferungen_MH_MAER_PFSZ_Test"));
        //assertTrue(MongoDBConnection.checkIfCollectionExists("Lieferungen_MH_MICA_IBAS_Test"));
        assertTrue(MongoDBConnection.checkIfCollectionExists("Lieferungen_MH_PFIS_SUHR_Test"));
        //assertTrue(MongoDBConnection.checkIfCollectionExists("Lieferungen_MH_SCHU_ZHRI_Test"));
        assertTrue(MongoDBConnection.checkIfCollectionExists("Lieferungen_MH_TTIP_SPRE_Test"));
        //Prüfe auf False
        assertFalse(MongoDBConnection.checkIfCollectionExists("Lieferungen_irgendwas_Test"));

        MongoDBRead reader = new MongoDBRead();
        BasicDBObject searchquery = new BasicDBObject();
        searchquery.put("id", 835);
        ArrayList<Document> SearchEntries = reader.SearchEntries("Lieferungen_MH_BUCHELI_HORW_Test", searchquery);
        assertFalse(SearchEntries.isEmpty()); //darf nicht leer sein
        int int_id = SearchEntries.get(0).getInteger("id");
        assertEquals(835, int_id); // prüfe ob die richtige ID gefunden wurde
        LOG.debug("ID " + int_id + " wurde gefunden");

    }

    @Test
    @DisplayName("Test03: Kontrolliert ob alle Möbelhäuser importiert werden")
    public void importAllMoebelhauser_Test() {
        LOG.debug("Test 03 : importAllMoebelhauser_Test");
        dropAllCollections(); // Damit die Möbelhäuser wieder gelöscht werden
        boolean importAllMoebelhauser_ret = restcollector.importAllMoebelhauser();
        assertTrue(importAllMoebelhauser_ret);

        assertTrue(MongoDBConnection.checkIfCollectionExists("MH_Test"));

        MongoDBRead reader = new MongoDBRead();
        BasicDBObject searchquery = new BasicDBObject();
        searchquery.put("id", 666);
        ArrayList<Document> SearchEntries = reader.SearchEntries("MH_Test", searchquery);
        assertFalse(SearchEntries.isEmpty()); //darf nicht leer sein
        int int_id = SearchEntries.get(0).getInteger("id");
        assertEquals(666, int_id); // prüfe ob die richtige ID gefunden wurde
        assertEquals("MH_BUCHELI_HORW", SearchEntries.get(0).getString("moebelhausCode"));
        LOG.debug("ID " + int_id + " | moebelhauscode " + SearchEntries.get(0).getString("moebelhauscode") + "wurde gefunden");

    }

    @Test
    @DisplayName("Test04: Kontrolliert ob alle Produkte importiert werden")
    public void importAllProducts_Test() {
        LOG.debug("Test 04 : importAllProducts_Test");

        boolean importAllProducts_ret = restcollector.importAllProducts();
        assertTrue(importAllProducts_ret);

        assertTrue(MongoDBConnection.checkIfCollectionExists("Produkte_Test"));

        MongoDBRead reader = new MongoDBRead();
        BasicDBObject searchquery = new BasicDBObject();
        searchquery.put("id", 133);
        ArrayList<Document> SearchEntries = reader.SearchEntries("Produkte_Test", searchquery);
        assertFalse(SearchEntries.isEmpty()); //darf nicht leer sein
        int int_id = SearchEntries.get(0).getInteger("id");
        assertEquals(133, int_id); // prüfe ob die richtige ID gefunden wurde
        assertEquals("Computer-Tisch", SearchEntries.get(0).getString("name"));
        LOG.debug("ID " + int_id + " | produkt " + SearchEntries.get(0).getString("name") + "wurde gefunden");
    }

    @Test
    public void dateTimeFix_Test() throws ParseException {
        LOG.debug("Test 05 : dateTimeFix_Test");

        restcollector.dateTimeFix();

    }
}
