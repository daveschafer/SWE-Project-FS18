/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.main;

import ch.hslu.swe.DataCollector.RESTCollectorMongo;
import ch.hslu.swe.dbaccess.MongoDBConnection;
import ch.hslu.swe.helper.MoebelherstellerEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;

/**
 * Main Entry of our Application. Deprecated.
 *
 * @author Dave
 */
public class Main {

    private static Logger LOG = LogManager.getLogger(Main.class);

    //Startpunkt der Applikation
    public static void main(final String[] args) throws JSONException {

        //Die MAIN hier braucht es eigentlich nicht mehr, haben wir nur teilweise für Kurztests gebraucht.
        
//Hostname, Port und Datenbankname einer MongoDB Instanz hier spezifizieren       
        MongoDBConnection.setMongoDBDatabase("your-mongo-server-hostname", 27017, "yourmongoDBName"); //ProduktivInstanz

        //Import Data
        RESTCollectorMongo collector_Test = new RESTCollectorMongo(MoebelherstellerEnum.TEST);
        collector_Test.importAllMoebelhauser();
        collector_Test.importAllDeliveries();
        collector_Test.importAllOrders();
        collector_Test.importAllProducts();

        /*
        collector_Test = new RESTCollectorMongo(MoebelherstellerEnum.FISCHER);
        collector_Test.importAllMoebelhauser();
        collector_Test.importAllDeliveries();
        collector_Test.importAllOrders();
        collector_Test.importAllProducts();

        collector_Test = new RESTCollectorMongo(MoebelherstellerEnum.WALKER);
        collector_Test.importAllMoebelhauser();
        collector_Test.importAllDeliveries();
        collector_Test.importAllOrders();
        collector_Test.importAllProducts();

        collector_Test = new RESTCollectorMongo(MoebelherstellerEnum.ZWISSIG);
        collector_Test.importAllMoebelhauser();
        collector_Test.importAllDeliveries();
        collector_Test.importAllOrders();
        collector_Test.importAllProducts();
         */
        //collector_Test.dateTimeFix();
        //collector_Test.importAllMoebelhauser();
        // FurnitureManufacturer furman = new FurnitureManufacturer(MoebelherstellerEnum.TEST);
        //furman.getOrderVolumeForAllWeeks09();
        //JSONArray arr08 = furman.getOrdersForAllWeeks08();
        //furman.checkIfCollectionAgeUp2date(arr08);
        //boolean importAllOrders = collector_Test.importAllOrders();
        // boolean importAllDeliveries = collector_Test.importAllDeliveries();
        //LOG.debug("Ergebnisse: "+importAllDeliveries +" : "+importAllOrders);
        //collector_Test.importAllMoebelhauser();
        //collector_Test.importAllProducts();
        //Try import Lieferungen
        //RESTCollectorMongo rCollector = new RESTCollectorMongo(MoebelherstellerEnum.TEST);
        //rCollector.importAllOrdersAsOne();
        /*LOG.info("Prüfe ob Collection Produkte_Test existiert: " + MongoDBConnection.checkIfCollectionExists("Produkte_Test"));
        LOG.info("Prüfe ob Collection Bestellungen_MH_LIPO_EGER_Test existiert: " + MongoDBConnection.checkIfCollectionExists("Bestellungen_MH_LIPO_EGER_Test"));
        LOG.info("Prüfe ob Collection Bestellungen_MH_LIPO_EGER_YOLO existiert: " + MongoDBConnection.checkIfCollectionExists("Bestellungen_MH_LIPO_EGER_YOLO"));
        //RESTCollectorMongo collector = new RESTCollectorMongo();
        //collector.importAllOrders(MoebelherstellerEnum.TEST);
        LOG.info("Import beendet");
         */
        //JSON Testing Class referenced
        /* try {
        // JSONTesting.startTesting();
        //MONGOTesting Mongotest1 = new MONGOTesting();
        //Mongotest1.startTesting();
        FunctionsA0xTesting.startTesting();
        } catch (IOException ex1) {
        LOG.error("Error while getting JSON from URL (IOEXCEPTION)");
        } catch (URISyntaxException urie) {
        LOG.error("Ungültige URL Exception wurde ausgelöst: " + urie);
        }
         */
    }

}
