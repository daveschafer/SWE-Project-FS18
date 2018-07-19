/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.functions;

import ch.hslu.swe.dbaccess.MongoDBConnection;
import ch.hslu.swe.helper.MoebelherstellerEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

/**
 *
 * @author Dave
 */
public class FurnitureManufacteurer_Test {

    private static final Logger LOG = LogManager.getLogger(FurnitureManufacteurer_Test.class);

    public FurnitureManufacteurer_Test() {
    }

    @BeforeClass
    public static void setUpClass() {
        //Init DB Connection
        String hostname = "yourMongoServer";
        int hostport = 27017;
        MongoDBConnection.setMongoDBDatabase(hostname, hostport);
    }

    private MoebelherstellerEnum mh_Test;
    private FurnitureManufacturer furMan;

    @Before
    public void setUp() {
        mh_Test = MoebelherstellerEnum.TEST;
        furMan = new FurnitureManufacturer(mh_Test);
    }

    @After
    public void tearDown() {
        LOG.debug("******** Testcase beendet **********");
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    @Test
    @DisplayName("Test 01: Kontrolliert ob alle Möbelhäuser korrekt abgeholt werden.")
    public void getMoebelhauser01_Test() {
        LOG.debug("**** Test 01: getMoebelhauser01_Test ****");
        int mhAnz_Referenz = 14; //14 Möbelhäuser beim Furman "Test";
        int mhAnz_Test = furMan.getMoebelhauser01();

        assertEquals(mhAnz_Referenz, mhAnz_Test);
        LOG.debug("getMoebelhauser01 - Count Furnitureshops: " + mhAnz_Test);
    }

    @Test
    @DisplayName("Test 02: Kontrolliert die Anzahl Produkttypen.")
    public void getProductTypes02_Test() {
        LOG.debug("**** Test 02: getProductTypes02_Test ****");
        int AnzProdukte_Test = furMan.getProductTypes02();
        int AnzProdukte_Referez = 67; //Momentan 67 Produkte bei Test

        assertEquals(AnzProdukte_Referez, AnzProdukte_Test);
        LOG.debug("getProductTypes02 - Count Producttypes: " + AnzProdukte_Test);
    }

    @Test
    @DisplayName("Test 03: Kontrolliert ob die Durchscnittsbestellwerte stimmen (mongoDB)")
    public void getAverageOrderValuePerFurnitureShop03_Test() throws JSONException {
        LOG.debug("**** Test 03: getAverageOrderValuePerFurnitureShop03_Test ****");
        JSONArray furMan_Average = furMan.getAverageOrderValuePerFurnitureShop03();
        for (int i = 0; i < furMan_Average.length(); i++) {
            JSONObject job = furMan_Average.getJSONObject(i);
            String moebelhausCode = job.getString("moebelhausCode");
            Double durchschnittBestellwert = job.getDouble("durchschnittBestellwert");
            LOG.debug("Moeebelhaus: " + moebelhausCode + " | " + "Durchschnitt-BW: " + durchschnittBestellwert);
            //Value CHecker
            switch (moebelhausCode) {
                case "MH_BUCHELI_HORW":
                    assertEquals(24431.5d, durchschnittBestellwert, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                case "MH_EGGER_EBLU":
                    assertEquals(63441d, durchschnittBestellwert, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                case "MH_DIGA_EMME":
                    assertEquals(17545.4d, durchschnittBestellwert, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                case "MH_MICA_IBAS":
                    assertEquals(9890d, durchschnittBestellwert, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                case "MH_SCHU_ZHRI":
                    assertEquals(11872.5d, durchschnittBestellwert, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                case "MH_TTIP_SPRE":
                    assertEquals(33600d, durchschnittBestellwert, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                case "MH_PFIS_SUHR":
                    assertEquals(12987d, durchschnittBestellwert, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                case "MH_CONF_SCHL":
                    assertEquals(17910.666666666668d, durchschnittBestellwert, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                case "MH_INTE_PRAT":
                    assertEquals(4866.5d, durchschnittBestellwert, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                case "MH_MAER_PFSZ":
                    assertEquals(15035d, durchschnittBestellwert, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                case "MH_LIPO_EGER":
                    assertEquals(12578.5d, durchschnittBestellwert, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                case "MH_HUBA_RTHI":
                    assertEquals(5289d, durchschnittBestellwert, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                case "MH_FERR_HINW":
                    assertEquals(21783d, durchschnittBestellwert, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                case "MH_MZMO_VOLK":
                    assertEquals(0.0d, durchschnittBestellwert, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                default:
                    LOG.error("Unbekanntes Moebelhaus");
                    assertTrue(false); //klar oder?
                    break;
            }
        } //schleife endet
    }

    @Test
    @DisplayName("Test 03: Kontrolliert ob die Durchscnittsbestellwerte stimmen (json)")
    public void getAverageOrderValuePerFurnitureShop03_json_Test() throws JSONException {
        LOG.debug("**** Test 03: getAverageOrderValuePerFurnitureShop03_json_Test ****");
        JSONArray furMan_Average = furMan.getAverageOrderValuePerFurnitureShop03_json();
        for (int i = 0; i < furMan_Average.length(); i++) {
            JSONObject job = furMan_Average.getJSONObject(i);
            String moebelhausCode = job.getString("moebelhausCode");
            Double durchschnittBestellwert = job.getDouble("durchschnittBestellwert");
            LOG.debug("Moebelhaus: " + moebelhausCode + " | " + "Durchschnitt-BW: " + durchschnittBestellwert);
            //Value CHecker

        } //schleife endet
    }

//Ohne Zeitangaben
    @Test
    @DisplayName("Test 04: Kontrolliert ob die Bestellwerte (Total) stimmen. (Ohne Zeitangaben = Alle Bestellungen)")
    public void getOrderValueFromPeriod04_Test() throws JSONException {
        LOG.debug("**** Test 04: getOrderValueFromPeriod04_Test ****");
        JSONArray ordervalues = furMan.getOrderValueFromPeriod04();

        for (int i = 0; i < ordervalues.length(); i++) {
            JSONObject job = ordervalues.getJSONObject(i);
            String moebelhausCode = job.getString("moebelhausCode");
            Double BestellwertTotal = job.getDouble("BestellwertTotal");

            LOG.debug("Moebelhaus: " + moebelhausCode + " | " + "BestellwertTotal: " + BestellwertTotal);
            //Value CHecker
            switch (moebelhausCode) {
                case "MH_BUCHELI_HORW":
                    assertEquals(97726d, BestellwertTotal, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                case "MH_EGGER_EBLU":
                    assertEquals(63441.0d, BestellwertTotal, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                case "MH_DIGA_EMME":
                    assertEquals(87727.0d, BestellwertTotal, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                case "MH_MICA_IBAS":
                    assertEquals(19780.0d, BestellwertTotal, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                case "MH_SCHU_ZHRI":
                    assertEquals(11872.5d, BestellwertTotal, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                case "MH_TTIP_SPRE":
                    assertEquals(33600.0d, BestellwertTotal, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                case "MH_PFIS_SUHR":
                    assertEquals(12987.0d, BestellwertTotal, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                case "MH_CONF_SCHL":
                    assertEquals(53732.0d, BestellwertTotal, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                case "MH_INTE_PRAT":
                    assertEquals(9733.0, BestellwertTotal, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                case "MH_MAER_PFSZ":
                    assertEquals(60140.0d, BestellwertTotal, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                case "MH_LIPO_EGER":
                    assertEquals(25157.0d, BestellwertTotal, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                case "MH_HUBA_RTHI":
                    assertEquals(5289.0d, BestellwertTotal, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                case "MH_FERR_HINW":
                    assertEquals(65349.0d, BestellwertTotal, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                case "MH_MZMO_VOLK":
                    assertEquals(0.0d, BestellwertTotal, 0.0001d); //delta = 0.0001 rundungsfehler
                    break;
                default:
                    LOG.error("Unbekanntes Moebelhaus");
                    assertTrue(false); //klar oder?
                    break;
            }
        } //schleife endet
    }

//Mit Zeitangaben
    /* public void getOrderValueFromPeriod04_Test(Von, Bis) {
    }*/
//Ohne Zeitangaben
    @Test
    @DisplayName("Test 05: Kontrolliert ob die richtigen 3 TopShops ausgegeben werden.")
    public void getTop3FurnitureShops05_Test() throws JSONException {
        LOG.debug("**** Test 05: getTop3FurnitureShops05_Test ****");
        JSONArray top3Shops = furMan.getTop3FurnitureShops05();
        for (int i = 0; i < top3Shops.length(); i++) {
            JSONObject job = top3Shops.getJSONObject(i);
            String moebelhausCode = job.getString("moebelhausCode");
            switch (i) {
                case 0:
                    //Platz 1
                    assertEquals("MH_BUCHELI_HORW", moebelhausCode);
                    LOG.debug("Platz " + (i + 1) + " : " + moebelhausCode);
                    break;
                case 1:
                    //Platz 2
                    assertEquals("MH_DIGA_EMME", moebelhausCode); //MH_DIGA_EMME
                    LOG.debug("Platz " + (i + 1) + " : " + moebelhausCode);
                    break;
                case 2:
                    //Platz 3
                    assertEquals("MH_FERR_HINW", moebelhausCode);
                    LOG.debug("Platz " + (i + 1) + " : " + moebelhausCode);
                    break;
                default:
                    LOG.error("Array zu gross (mehr als 3 Plätze?!)");
                    assertTrue(false); //klar oder?
                    break;
            }
        } //ende schleife
    }

    //Mit Zeitangaben
    /* public void getTop3FurnitureShops05_Test(Von, Bis) {

    }*/
    @Test
    @DisplayName("Test 06: Kontrolliert ob die Durchschnittliche Lieferzeit stimmt.")
    public void getAverageDeliveryTime06_Test() {
        LOG.debug("**** Test 06: getAverageDeliveryTime06_Test ****");
        Double lieferzeit_Test = furMan.getAverageDeliveryTime06();
        LOG.info("Lieferzeit MH 'Test': " + lieferzeit_Test + " Tage");
        assertEquals(3.214d, lieferzeit_Test, 0.0001d);
    }

    //Ohne Zeitangeben
    @Test
    @DisplayName("Test 07: Kontrolliert ob die Top5 Produkte (über die gesamte Zeit) richtig sind.")
    public void getTop5Products07_Test() throws JSONException {
        LOG.debug("**** Test 07: getTop5Products07_Test ****");
        JSONArray top5products = furMan.getTop5Products07();
        int vorgaenger_Anzahl = Integer.MAX_VALUE;
        for (int i = 0; i < top5products.length(); i++) {
            LOG.debug("Platz " + i + " : " + top5products.getJSONObject(i));
            JSONObject job = top5products.getJSONObject(i);
            int produktID = job.getInt("produktID");

            switch (produktID) {
                case 223:
                case 243:
                case 173:
                case 207:
                case 195:
                    assertTrue(true); // klar oder (sind die TOp 5 IDs)
                    break;
                default:
                    assertTrue(false); //klar oder
            }

            int aktuell_Anzahl = job.getInt("anzahl");
            assertTrue(vorgaenger_Anzahl >= aktuell_Anzahl); // Prüfe ob die vorgänger Anzahl grösser ist als die jetztige (somit wissen wir dass die Rangfolge stimmt
            vorgaenger_Anzahl = aktuell_Anzahl;
        } // ende schleife
    }

//mit zeitangaben
    /*
    public void getTop5Products07_Test(Von,Bis) {
    }*/
    @Test
    @DisplayName("Test 08 + Test09: Kontrolliert ob die KW Orders korrespondieren.")
    public void getOrdersForAllWeeks08_getOrderVolumeForAllWeeks09_Test() throws JSONException {
        LOG.debug("**** Test 08 + 09: getOrdersForAllWeeks08_Test | getOrderVolumeForAllWeeks09_Test ****");
        JSONArray ordersForAllWeeks = furMan.getOrdersForAllWeeks08();
        JSONArray orderVolumeForAllWeeks = furMan.getOrderVolumeForAllWeeks09();

        for (int i = 0; i < ordersForAllWeeks.length(); i++) {
            LOG.debug("MH-NR" + i + " : " + ordersForAllWeeks.getJSONObject(i));
            JSONObject job08 = ordersForAllWeeks.getJSONObject(i);
            JSONObject job09 = orderVolumeForAllWeeks.getJSONObject(i);
            String moebelhausCode = job08.getString("moebelhausCode");
            int ordersAllKW08 = 0;
            int ordersAllKW09 = 0;
            int anzahlBestellungKW = 0;

            JSONArray jar_Bestellungen08 = job08.getJSONArray("Bestellungen");
            JSONArray jar_Bestellungen09 = job09.getJSONArray("Bestellungen");

            ordersAllKW08 = jar_Bestellungen08.length();
            ordersAllKW09 = jar_Bestellungen09.length();
            JSONObject job_kw_08 = new JSONObject();
            JSONObject job_kw_09 = new JSONObject();

            switch (moebelhausCode) {
                case "MH_BUCHELI_HORW":
                    //Test 08
                    assertEquals(2, ordersAllKW08); //Bestellungen für Alle KWs summiert
                    job_kw_08 = jar_Bestellungen08.getJSONObject(0); // get KW1
                    anzahlBestellungKW = job_kw_08.getInt("anzahlBestellungen");
                    assertEquals(2, anzahlBestellungKW); //Bestellungen in der KW_1
                    //Test 09
                    assertEquals(2, ordersAllKW09); //Bestellungen für Alle KWs summiert
                    job_kw_09 = jar_Bestellungen09.getJSONObject(0); // get KW1
                    anzahlBestellungKW = job_kw_09.getInt("Bestellvolumen");
                    assertEquals(46663, anzahlBestellungKW); //Bestellungen in der KW_1

                    break;
                case "MH_EGGER_EBLU":
                    //Test08
                    assertEquals(0, ordersAllKW08); //Bestellungen für Alle KWs summiert

                    JSONException excpectedException08 = Assertions.assertThrows(JSONException.class, () -> {
                        jar_Bestellungen08.getJSONObject(0).getInt("anzahlBestellungen"); // Das sollte eine Exception geben da es den Wert nicht gibt
                    });
                    //Test09
                    assertEquals(0, ordersAllKW09); //Bestellungen für Alle KWs summiert
                    JSONException excpectedException09 = Assertions.assertThrows(JSONException.class, () -> {
                        jar_Bestellungen09.getJSONObject(0).getInt("Bestellvolumen"); // Das sollte eine Exception geben da es den Wert nicht gibt
                    });
                    break;
                case "MH_DIGA_EMME":
                    //Test08
                    assertEquals(4, ordersAllKW08); //Bestellungen für Alle KWs summiert
                    job_kw_08 = jar_Bestellungen08.getJSONObject(1); // get KW6
                    anzahlBestellungKW = job_kw_08.getInt("anzahlBestellungen");
                    assertEquals(1, anzahlBestellungKW); //Bestellungen in der KW_6

                    //Test09
                    assertEquals(4, ordersAllKW09); //Bestellungen für Alle KWs summiert
                    job_kw_09 = jar_Bestellungen09.getJSONObject(1); // get KW6
                    anzahlBestellungKW = job_kw_09.getInt("Bestellvolumen");
                    assertEquals(25485, anzahlBestellungKW); //Bestellungen in der KW_6
                    break;
                case "MH_MICA_IBAS":
                    //Test08
                    assertEquals(2, ordersAllKW08); //Bestellungen für Alle KWs summiert
                    job_kw_08 = jar_Bestellungen08.getJSONObject(0); // get KW7
                    anzahlBestellungKW = job_kw_08.getInt("anzahlBestellungen");
                    assertEquals(1, anzahlBestellungKW); //Bestellungen in der KW_7
                    //Test09
                    assertEquals(2, ordersAllKW09); //Bestellungen für Alle KWs summiert
                    job_kw_09 = jar_Bestellungen09.getJSONObject(0); // get KW7
                    anzahlBestellungKW = job_kw_09.getInt("Bestellvolumen");
                    assertEquals(3052, anzahlBestellungKW); //Bestellungen in der KW_7
                    break;
                case "MH_SCHU_ZHRI":
                    //Test08
                    assertEquals(1, ordersAllKW08); //Bestellungen für Alle KWs summiert
                    job_kw_08 = jar_Bestellungen08.getJSONObject(0); // get KW10
                    anzahlBestellungKW = job_kw_08.getInt("anzahlBestellungen");
                    assertEquals(1, anzahlBestellungKW); //Bestellungen in der KW_10
                    //Test09
                    assertEquals(1, ordersAllKW09); //Bestellungen für Alle KWs summiert
                    job_kw_09 = jar_Bestellungen09.getJSONObject(0); // get KW10
                    Double anzahlBestellungKW_dbl = job_kw_09.getDouble("Bestellvolumen"); //Special Case wo ein double zurückkomt
                    assertEquals(11872.5d, anzahlBestellungKW_dbl, 0.0001d); //Bestellungen in der KW_10
                    break;
                case "MH_TTIP_SPRE":
                    //Test08+09
                    assertEquals(0, ordersAllKW08); //Bestellungen für Alle KWs summiert
                    assertEquals(0, ordersAllKW09); //Bestellungen für Alle KWs summiert

                    break;
                case "MH_PFIS_SUHR":
                    //Test08+09
                    assertEquals(0, ordersAllKW08); //Bestellungen für Alle KWs summiert
                    assertEquals(0, ordersAllKW09); //Bestellungen für Alle KWs summiert

                    break;
                case "MH_CONF_SCHL":
                    //Test08
                    assertEquals(1, ordersAllKW08); //Bestellungen für Alle KWs summiert
                    job_kw_08 = jar_Bestellungen08.getJSONObject(0); // get KW3
                    anzahlBestellungKW = job_kw_08.getInt("anzahlBestellungen");
                    assertEquals(2, anzahlBestellungKW); //Bestellungen in der KW_3
                    //Test09
                    assertEquals(1, ordersAllKW09); //Bestellungen für Alle KWs summiert
                    job_kw_09 = jar_Bestellungen09.getJSONObject(0); // get KW3
                    anzahlBestellungKW = job_kw_09.getInt("Bestellvolumen");
                    assertEquals(38442, anzahlBestellungKW); //Bestellungen in der KW_3
                    break;
                case "MH_INTE_PRAT":
                    //Test08
                    assertEquals(1, ordersAllKW08); //Bestellungen für Alle KWs summiert
                    job_kw_08 = jar_Bestellungen08.getJSONObject(0); // get KW4
                    anzahlBestellungKW = job_kw_08.getInt("anzahlBestellungen");
                    assertEquals(1, anzahlBestellungKW); //Bestellungen in der KW_4
                    //Test09
                    assertEquals(1, ordersAllKW09); //Bestellungen für Alle KWs summiert
                    job_kw_09 = jar_Bestellungen09.getJSONObject(0); // get KW4
                    anzahlBestellungKW = job_kw_09.getInt("Bestellvolumen");
                    assertEquals(4840, anzahlBestellungKW); //Bestellungen in der KW_4

                    break;
                case "MH_MAER_PFSZ":
                    //Test08
                    assertEquals(2, ordersAllKW08); //Bestellungen für Alle KWs summiert
                    job_kw_08 = jar_Bestellungen08.getJSONObject(0); // get KW2
                    anzahlBestellungKW = job_kw_08.getInt("anzahlBestellungen");
                    assertEquals(1, anzahlBestellungKW); //Bestellungen in der KW_2
                    //Test09
                    assertEquals(2, ordersAllKW09); //Bestellungen für Alle KWs summiert
                    job_kw_09 = jar_Bestellungen09.getJSONObject(0); // get KW2
                    anzahlBestellungKW = job_kw_09.getInt("Bestellvolumen");
                    assertEquals(20912, anzahlBestellungKW); //Bestellungen in der KW_2
                    break;
                case "MH_LIPO_EGER":
                    //Test08
                    assertEquals(1, ordersAllKW08); //Bestellungen für Alle KWs summiert
                    job_kw_08 = jar_Bestellungen08.getJSONObject(0); // get KW2
                    anzahlBestellungKW = job_kw_08.getInt("anzahlBestellungen");
                    assertEquals(1, anzahlBestellungKW); //Bestellungen in der KW_2
                    //Test09
                    assertEquals(1, ordersAllKW09); //Bestellungen für Alle KWs summiert
                    job_kw_09 = jar_Bestellungen09.getJSONObject(0); // get KW2
                    anzahlBestellungKW = job_kw_09.getInt("Bestellvolumen");
                    assertEquals(12990, anzahlBestellungKW); //Bestellungen in der KW_2
                    break;
                case "MH_HUBA_RTHI":
                    //Test08
                    assertEquals(1, ordersAllKW08); //Bestellungen für Alle KWs summiert
                    job_kw_08 = jar_Bestellungen08.getJSONObject(0); // get KW4
                    anzahlBestellungKW = job_kw_08.getInt("anzahlBestellungen");
                    assertEquals(1, anzahlBestellungKW); //Bestellungen in der KW_4
                    //Test09
                    assertEquals(1, ordersAllKW09); //Bestellungen für Alle KWs summiert
                    job_kw_09 = jar_Bestellungen09.getJSONObject(0); // get KW4
                    anzahlBestellungKW = job_kw_09.getInt("Bestellvolumen");
                    assertEquals(5289, anzahlBestellungKW); //Bestellungen in der KW_4
                    break;
                case "MH_FERR_HINW":
                    //Test08
                    assertEquals(2, ordersAllKW08); //Bestellungen für Alle KWs summiert
                    job_kw_08 = jar_Bestellungen08.getJSONObject(1); // get KW9
                    anzahlBestellungKW = job_kw_08.getInt("anzahlBestellungen");
                    assertEquals(1, anzahlBestellungKW); //Bestellungen in der KW_9
                    //Test09
                    assertEquals(2, ordersAllKW09); //Bestellungen für Alle KWs summiert
                    job_kw_09 = jar_Bestellungen09.getJSONObject(1); // get KW9
                    anzahlBestellungKW = job_kw_09.getInt("Bestellvolumen");
                    assertEquals(45384, anzahlBestellungKW); //Bestellungen in der KW_9
                    break;
                case "MH_MZMO_VOLK":
                    //Test08+09
                    assertEquals(0, ordersAllKW08); //Bestellungen für Alle KWs summiert
                    assertEquals(0, ordersAllKW09); //Bestellungen für Alle KWs summiert

                    break;
                default:
                    LOG.error("Unbekanntes Moebelhaus");
                    assertTrue(false); //klar oder?
                    break;
            }
        }
    }

}
