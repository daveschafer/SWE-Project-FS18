/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.functions;

import ch.hslu.swe.dbaccess.MongoDBConnection;
import ch.hslu.swe.dbaccess.MongoDBManageCollection;
import ch.hslu.swe.dbaccess.MongoDBRead;
import ch.hslu.swe.helper.MoebelherstellerEnum;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import static java.util.Collections.reverseOrder;
import java.util.stream.Collectors;

/**
 * Alle Funktionalen Anforderungen A01 bis A09
 *
 * @author Dave
 */
public class FurnitureManufacturer implements FurnitureManufacturerInterface {

    private static final Logger LOG = LogManager.getLogger(FurnitureManufacturer.class);

    private final MoebelherstellerEnum moebelherstellerEnum;
    private String productsCollection;
    private String moebelhauserCollection;
    private MongoDBRead mongoreader;
    private final SimpleDateFormat mongoDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS\'Z\'");

    public FurnitureManufacturer(MoebelherstellerEnum Moebelhersteller) {
        this.moebelherstellerEnum = Moebelhersteller;
        DBSettings();
    }

    private void DBSettings() {        //DB Settings hier machen...
        productsCollection = "Produkte_" + moebelherstellerEnum.toString();
        moebelhauserCollection = "MH_" + moebelherstellerEnum.toString();
        mongoreader = new MongoDBRead();
    }

    @Override
    public int getMoebelhauser01() {
        return (int) this.mongoreader.ReadALL(this.moebelhauserCollection).count();
    }

    @Override
    public int getProductTypes02() {
        return (int) this.mongoreader.ReadALL(this.productsCollection).count();
    }

    //Bessere Lösung
    @Override
    public JSONArray getAverageOrderValuePerFurnitureShop03() {
        JSONArray MHAverageValue = new JSONArray();

        ArrayList<Document> alleMoebelHauser = mongoreader.ReadAll2Arraylist("MH_" + this.moebelherstellerEnum.toString());
        //1. Alle Möbelhäuser iterieren
        for (Document mh_doc : alleMoebelHauser) {
            String moebelhausCode = mh_doc.get("moebelhausCode").toString();

            //Mongoquery: db.Bestellungen_MH_BUCHELI_HORW_Test.find({} , {"bestellungPositionListe.anzahl" : 1, "bestellungPositionListe.produktTyp.preis" : 1})
            FindIterable<Document> matchings = findIterableBuilder(("Bestellungen_" + moebelhausCode + "_" + this.moebelherstellerEnum.toString()), new Document("bestellungPositionListe.anzahl", 1)
                    .append("bestellungPositionListe.produktTyp.preis", 1), new BasicDBObject());

            double BestellwertTotalMH = 0;
            double AnzBestellungenMH = 0;
            //2. Alle Bestellungen pro Möbelhaus iterieren
            for (Iterator<Document> bestellung_it = matchings.iterator(); bestellung_it.hasNext();) {
                AnzBestellungenMH++;
                Document maindoc = bestellung_it.next();
                //Der Cast ist eine Empfehlung vom MongoTeam übrigens: https://jira.mongodb.org/browse/JAVA-1720
                List<Document> bestellPosition = (List<Document>) maindoc.get("bestellungPositionListe");
                for (Document position_doc : bestellPosition) {
                    Integer anzahl = position_doc.getInteger("anzahl");
                    Document produktTyp = (Document) bestellPosition.get(0).get("produktTyp");
                    Double preis = getPreistypeSafe(produktTyp); //Prüfen ob der Typ nicht double ist und dann konvertieren!
                    //Preis kummulieren
                    BestellwertTotalMH += (preis * anzahl);
                }   //Durchgang einer Bestellung abgeschlossen
            }
            //Nachdem alle Bestellungen iteriert wurden Eintrag im JSON ergänzen
            JSONObject ordervalue = new JSONObject();
            ordervalue.put("moebelhausCode", moebelhausCode);
            Double durchschnittBestellwert = (BestellwertTotalMH / AnzBestellungenMH);
            //Fall Abfangen wenn Betellwert 0 ist (Möbelhaus wird nicht beliefert)
            if (durchschnittBestellwert.isNaN()) {
                durchschnittBestellwert = 0.0d;
            }
            ordervalue.put("durchschnittBestellwert", durchschnittBestellwert);
            MHAverageValue.put(ordervalue);
        }

        return MHAverageValue;
    }

    /**
     * Nurnoch als Nachschlagewerk (JSON Variante).
     *
     * @return Gibt ein Array zurück.
     * @deprecated use {@link #getAverageOrderValuePerFurnitureShop03()}
     * instead.
     */
    public JSONArray getAverageOrderValuePerFurnitureShop03_json() {
        JSONArray MHAverageValue = new JSONArray();
        ArrayList<Document> alleMoebelHauser = mongoreader.ReadAll2Arraylist("MH_" + this.moebelherstellerEnum.toString());

        //1. Alle Möbelhäuser iterieren
        for (Document doc : alleMoebelHauser) {
            double BestellwertTotal = 0;
            String moebelhausCode = doc.get("moebelhausCode").toString();
            ArrayList<Document> moebelhausBestellungen = mongoreader.ReadAll2Arraylist("Bestellungen_" + moebelhausCode + "_" + this.moebelherstellerEnum.toString());

            double betellungenCount = 0;
            //2. Alle Bestellungen iterieren
            for (Document specificMoebelhaus : moebelhausBestellungen) {
                //Methode ist zwar "deprecated" aber siehe hier: https://goo.gl/t5oiQf
                String bestellPositionString = com.mongodb.util.JSON.serialize(specificMoebelhaus.get("bestellungPositionListe"));

                JSONArray bestellPosition = new JSONArray(bestellPositionString);
                //Anzahl mal Preis in jeder Bestellung multiplizieren
                for (int i = 0; i < bestellPosition.length(); i++) {
                    JSONObject bpBestellung = bestellPosition.getJSONObject(i);
                    int bp1_Anzahl = bpBestellung.getInt("anzahl");
                    JSONObject bp1_produkttyp = bpBestellung.getJSONObject("produktTyp");
                    int produktpreis = bp1_produkttyp.getInt("preis");

                    //Azahl mal Preis
                    int totalpreis = bp1_Anzahl * produktpreis;
                    BestellwertTotal += totalpreis;
                    betellungenCount++;
                }
            }
            //Nachdem alle Bestellungen iteriert wurden EIntrag im JSON ergänzen
            JSONObject ordervalue = new JSONObject();
            //bestellwertTotal geteilt durch bestellungen = Durchschnittspreis pro Bestellung
            ordervalue.put("moebelhausCode", moebelhausCode);
            Double durchschnittBestellwert = BestellwertTotal / betellungenCount;
            //Fall abfangen wenn ein Möbelhaus garnicht beliefert wird (somit NaN) --> ToDo bessere Lösung finden
            if (durchschnittBestellwert.isNaN()) {
                durchschnittBestellwert = 0.0d;
            }
            ordervalue.put("durchschnittBestellwert", durchschnittBestellwert);
            MHAverageValue.put(ordervalue);
        }

        return MHAverageValue;
    }

    /**
     * Aufruf ohne Zeitangabe gibt den Totalwert der Bestellungen pro Möbelhaus
     * zurück.
     *
     * @return Totalwert aller Bestellungen aller Möbelhäuser
     */
    public JSONArray getOrderValueFromPeriod04() {
        Date Von = new Date(0);
        Date Bis = new Date();
        return this.getOrderValueFromPeriod04(Von, Bis);
    }

    /*
    * Gesucht ist der Totalwert (Volumen) aller Bestellungen
    * Ausserdem soll nach Datum gefiltert werden können
    * Also z.B. mongoDB gib mir alle Bestellungen die grösser als "Von" und kleiner als "Bis" sind
    * Falls Von / Bis nicht gesetzt sind --> auf ältestes und jetziges Datum setzen
    * 
    * Neue Erkenntniss, ich muss trotzdem zuerst 03_new machen bevor ich das hier effizient lösen kann
     */
    @Override
    public JSONArray getOrderValueFromPeriod04(Date Von, Date Bis) {
        //Dateformat im MongoDB Stil
        JSONArray MHTotValue = new JSONArray();
        //Validate Date
        if (!(isDateValid(mongoDateFormat.format(Von))) || !(isDateValid(mongoDateFormat.format(Bis)))) {
            LOG.error("Falsches Date Format! Standarddatum wird gewählt");
            return getOrderValueFromPeriod04();
        }
        LOG.info("Von Date: " + mongoDateFormat.format(Von));
        LOG.info("Bis Date: " + mongoDateFormat.format(Bis));

        ArrayList<Document> alleMoebelHauser = mongoreader.ReadAll2Arraylist("MH_" + this.moebelherstellerEnum.toString());

        //1. Alle Möbelhäuser iterieren
        for (Document mh_doc : alleMoebelHauser) {
            String moebelhausCode = mh_doc.get("moebelhausCode").toString();

            BasicDBObject searchQuery = new BasicDBObject();
            //Lessons learned: einfach ein Date übergeben und nicht noch shapen vorher...
            searchQuery.put("datum", new BasicDBObject("$gte", Von).append("$lt", Bis));
            FindIterable<Document> matchings = findIterableBuilder(("Bestellungen_" + moebelhausCode + "_" + this.moebelherstellerEnum.toString()), new Document("bestellungPositionListe.anzahl", 1)
                    .append("bestellungPositionListe.produktTyp.preis", 1), searchQuery);

            double BestellwertTotalMH = 0;
            //2. Alle Bestellungen pro Möbelhaus iterieren
            for (Iterator<Document> bestellung_it = matchings.iterator(); bestellung_it.hasNext();) {
                Document maindoc = bestellung_it.next();
                //Der Cast ist eine Empfehlung vom MongoTeam übrigens: https://jira.mongodb.org/browse/JAVA-1720
                List<Document> bestellPosition = (List<Document>) maindoc.get("bestellungPositionListe");
                for (Document position_doc : bestellPosition) {
                    Integer anzahl = position_doc.getInteger("anzahl");
                    Document produktTyp = (Document) bestellPosition.get(0).get("produktTyp");
                    Double preis = getPreistypeSafe(produktTyp); //Prüfen ob der Typ nicht double ist und dann konvertieren!
                    BestellwertTotalMH += (preis * anzahl);
                }
            }
            JSONObject ordervalue = new JSONObject();
            ordervalue.put("moebelhausCode", moebelhausCode);
            ordervalue.put("BestellwertTotal", BestellwertTotalMH);
            MHTotValue.put(ordervalue);
        }
        return MHTotValue;
    }

    @Override
    public JSONArray getTop3FurnitureShops05(Date Von, Date Bis) {
        //Ansatz: Nimm Die Ergebnise von 04, sortiere sie nach grösse und gib 3 zurück --> profit
        JSONArray valueAllMH = getOrderValueFromPeriod04(Von, Bis);
        List<JSONObject> valueAllMH_List = new ArrayList<>();
        for (int i = 0; i < valueAllMH.length(); i++) {
            valueAllMH_List.add(valueAllMH.getJSONObject(i));
        }
        Collections.sort(valueAllMH_List, (jsonObjectA, jsonObjectB) -> {
            int compare = 0;
            try {
                double keyA = jsonObjectA.getDouble("BestellwertTotal");
                double keyB = jsonObjectB.getDouble("BestellwertTotal");
                compare = Double.compare(keyB, keyA);
            } catch (JSONException e) {
                LOG.error("Unerwartete JSON Exception, Stacktracke wird generiert: ");
                e.printStackTrace();
            }
            return compare;
        });
        JSONArray top3 = new JSONArray();
        for (int i = 0; i < 3; i++) {
            top3.put(valueAllMH_List.get(i));
        }
        return top3;
    }

    /**
     * Aufruf ohne Zeitangaben gibt die Top3 über die ganze Zeit zurück.
     *
     * @return Top3 Möbelhäuser über die gesamteZeit (JSONArray).
     */
    public JSONArray getTop3FurnitureShops05() {
        Date Von = new Date(0);
        Date Bis = new Date();
        return this.getTop3FurnitureShops05(Von, Bis);
    }

    //Ansatz: Jede Lieferung iterieren | CounterLieferungen (int)
    //Lieferdatum - Bestelldatum rechnen | Lieferzeit in Tagen (int)
    //Alle Lieferzeiten summieren (Lieferzeit Total)
    //Lieferzeit Total geteilt durch CounterLieferungen = Durchschnitt
    @Override
    public double getAverageDeliveryTime06() {
        double lieferzeitenTotal = 0;
        double counterLieferungen = 0;
        ArrayList<Document> alleMoebelHauser = mongoreader.ReadAll2Arraylist("MH_" + this.moebelherstellerEnum.toString());
        //1. Alle Möbelhäuser iterieren und Mongoquery absetzen
        for (Document mh_doc : alleMoebelHauser) {
            String moebelhausCode = mh_doc.get("moebelhausCode").toString();
            FindIterable<Document> matchings = findIterableBuilder(("Lieferungen_" + moebelhausCode + "_" + this.moebelherstellerEnum.toString()), new Document("datum", 1)
                    .append("bestellung.datum", 1), new BasicDBObject());

            //2. Alle Bestellungen pro Möbelhaus iterieren (get Lieferdatum)
            for (Iterator<Document> bestellung_it = matchings.iterator(); bestellung_it.hasNext();) {
                Document maindoc = bestellung_it.next();
                LocalDate lieferdatum = maindoc.getDate("datum").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                Document bestellung = (Document) maindoc.get("bestellung");
                LocalDate bestelldatum = bestellung.getDate("datum").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                counterLieferungen++;
                lieferzeitenTotal += Period.between(bestelldatum, lieferdatum).getDays();
            }
        }
        LOG.debug("Total Lieferungen: " + counterLieferungen + " | Total Lieferzeit: " + lieferzeitenTotal);
        Double averageDeliveryTime = (lieferzeitenTotal / counterLieferungen);
        DecimalFormat df = new DecimalFormat("#.###");   //Runden auf 3 stellen nach dem Komma
        averageDeliveryTime = Double.valueOf(df.format(averageDeliveryTime));
        LOG.debug("Durchschnitt Lieferzeit: " + averageDeliveryTime);
        return averageDeliveryTime;
    }

    // Info: Aus importAllOrders (Collections_Bstellungen) --- Liste mit Productyp und Anzahl | Bestellungen
    // Ansatz: Top Produkte ähnlich wie getTop3FurnitureShops05 evaluieren aber nur die ID zurückgeben
    // Danach auf die Collection Produkte_Test zugreifen und mit einer mongoQuery das gesamte JSONObject des Produkts zurückgeben.
    // Am häufigsten Verkauft = ANzahl am grössten
    // Ansatz mit Sub Documents: https://stackoverflow.com/questions/22751210/mongodb-find-subdocument-and-sort-the-results
    @Override
    public JSONArray getTop5Products07(Date Von, Date Bis) {
        JSONArray Top5Products = new JSONArray();
        if (!(isDateValid(mongoDateFormat.format(Von))) || !(isDateValid(mongoDateFormat.format(Bis)))) {
            LOG.error("Falsches Date Format! Standarddatum wird gewählt");
            return getTop5Products07();
        }

        ArrayList<Document> alleMoebelHauser = mongoreader.ReadAll2Arraylist("MH_" + this.moebelherstellerEnum.toString());
        HashMap<Integer, Integer> produktID_Anzahl_Map = new HashMap<>();

        //1. Alle Möbelhäuser iterieren
        for (Document mh_doc : alleMoebelHauser) {
            String moebelhausCode = mh_doc.get("moebelhausCode").toString();

            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.put("datum", new BasicDBObject("$gte", Von).append("$lt", Bis));
            FindIterable<Document> matchings = findIterableBuilder(("Bestellungen_" + moebelhausCode + "_" + this.moebelherstellerEnum.toString()), new Document("bestellungPositionListe.anzahl", 1)
                    .append("bestellungPositionListe.produktTyp.id", 1), searchQuery);

            for (Iterator<Document> bestellung_it = matchings.iterator(); bestellung_it.hasNext();) {
                Document maindoc = bestellung_it.next();
                List<Document> bestellPosition = (List<Document>) maindoc.get("bestellungPositionListe");
                for (Document position_doc : bestellPosition) {
                    Integer anzahl = position_doc.getInteger("anzahl");
                    Document produktTyp = (Document) bestellPosition.get(0).get("produktTyp");
                    Integer produktTyp_id = produktTyp.get("id", Integer.class);
                    if (!produktID_Anzahl_Map.containsKey(produktTyp_id)) {
                        produktID_Anzahl_Map.put(produktTyp_id, anzahl);
                    } else {
                        produktID_Anzahl_Map.put(produktTyp_id, produktID_Anzahl_Map.get(produktTyp_id) + anzahl); //Increment if key exists
                    }
                }
            }
        }

        //Die Hashmap ist ganz einfach sortiert dank Java8 Lambdas :)
        HashMap<Integer, Integer> produktID_Anzahl_sortedMap = MapSorter(produktID_Anzahl_Map, true);

        //Top 5 Elemente zusammenstellen
        if (!produktID_Anzahl_Map.isEmpty()) {
            for (int i = 0; i < 5; i++) {
                JSONObject jOB = new JSONObject();
                Integer key = (Integer) produktID_Anzahl_sortedMap.keySet().toArray()[i];
                Integer value = (Integer) produktID_Anzahl_sortedMap.values().toArray()[i];
                jOB.put("produktID", key);
                jOB.put("anzahl", value);
                ArrayList<Document> produktInfos = mongoreader.ReadEntry(("Produkte_" + moebelherstellerEnum.toString()), key);
                jOB.put("produktInfos", produktInfos);
                Top5Products.put(jOB);
            }
        } else {
            LOG.error("Keine Ergebnisse gefunden (prodIDMap Empty)");
            throw new ExceptionInInitializerError("Keine Ergebnisse gefunden");
        }
        return Top5Products;
    }

    /**
     * getTop5Products07 ohne Zeitangabe.
     *
     * @return Gibt die Allzeit besten Top5 Produkte zurück.
     */
    public JSONArray getTop5Products07() {
        Date Von = new Date(0);
        Date Bis = new Date();
        return this.getTop5Products07(Von, Bis);
    }

    // Ansatz: bei 06 und 07 abschauen
    // Aufbau: ,{moebelhauscode: Digga}, Bestellungen [{ "KW": 01 },{"Bestellungen" : 999}]
    // Anzahl Bestellungen pro Möbelhaus = collection.itemscount (voll easy
    // Am Schluss die Anfrage in eine Collection speichern und am anfang checken ob von dieser gelesen werden kann
    @Override
    public JSONArray getOrdersForAllWeeks08() {
        //Reserved --> check if collection exists
        String persistentCollection = "MH_" + this.moebelherstellerEnum.toString() + "_getOrdersForAllWeeks08";
        if (MongoDBConnection.checkIfCollectionExists(persistentCollection)) {
            LOG.debug("Collection existiert bereits");
            //Prüfen ob Collection aktuell ist, sprich weniger oder gleich alt wie die aktuelle KW
            JSONArray jar_DB = mongoreader.ReadAll2JSONArray(persistentCollection);
            boolean collUp2Date = checkIfCollectionAgeUp2date(jar_DB);
            if (collUp2Date) {
                LOG.debug("Collection ist aktuell in der DB vorhanden und wird direkt aus der DB zurückgegeben");
                return jar_DB;
            } else { //else --> Collection löschen und FUnktion normal ausführen
                MongoDBConnection.getDatabase().getCollection(persistentCollection).drop();
            }
        }

        //Core (wenn Collection noch nicht existiert oder veraltet ist
        ArrayList<Document> alleMoebelHauser = mongoreader.ReadAll2Arraylist("MH_" + this.moebelherstellerEnum.toString());
        JSONArray OrdersForAlWeeks = new JSONArray();

        //1. Alle Möbelhäuser iterieren
        for (Document mh_doc : alleMoebelHauser) {
            String moebelhausCode = mh_doc.get("moebelhausCode").toString();
            int aktuellesJahr = Calendar.getInstance().get(Calendar.YEAR);

            FindIterable<Document> matchings = findIterableBuilder(("Bestellungen_" + moebelhausCode + "_" + this.moebelherstellerEnum.toString()), new Document("datum", 1), new BasicDBObject());

            //3. Jede Bestellung iterieren und Kalenderwoche extrahieren
            HashMap<Integer, Integer> KW_Bestellungen = new HashMap<>();
            for (Iterator<Document> bestellung_it = matchings.iterator(); bestellung_it.hasNext();) {
                Document maindoc = bestellung_it.next();
                Date datumBestellung = maindoc.getDate("datum");
                //get Calendarweek aktuelleKW
                Calendar cal = Calendar.getInstance();
                cal.setTime(datumBestellung);
                if (cal.get(Calendar.YEAR) == aktuellesJahr) {
                    int week = cal.get(Calendar.WEEK_OF_YEAR);
                    if (!KW_Bestellungen.containsKey(week)) {
                        KW_Bestellungen.put(week, 1);
                    } else {
                        KW_Bestellungen.put(week, KW_Bestellungen.get(week) + 1); //Increment by 1 if key exists
                    }
                }
            }

            HashMap<Integer, Integer> KW_Bestellungen_sorted = MapSorter(KW_Bestellungen, false);
            //Add to JSON
            JSONArray bestellungenProKW = new JSONArray();
            for (int i = 0; i < KW_Bestellungen_sorted.size(); i++) {
                JSONObject jOB = new JSONObject();
                Integer key = (Integer) KW_Bestellungen_sorted.keySet().toArray()[i]; //week
                Integer value = (Integer) KW_Bestellungen_sorted.values().toArray()[i]; //anz Bestellungen
                jOB.put("anzahlBestellungen", value);
                jOB.put("KW", key);
                bestellungenProKW.put(jOB);
            }
            //JSONArray ergänzen
            JSONObject jo_moebelhaus = new JSONObject();
            jo_moebelhaus.put("moebelhausCode", moebelhausCode);
            jo_moebelhaus.put("Bestellungen", bestellungenProKW);
            OrdersForAlWeeks.put(jo_moebelhaus);
        }
        //Reserved --> Save collection2DB
        MongoDBManageCollection manager = new MongoDBManageCollection();
        manager.AddArray2Collection(persistentCollection, OrdersForAlWeeks);

        return OrdersForAlWeeks;
    }

    //Aufbau: { "KW": 01 }, {"Bestellvolumen" : 999}
    //Volumen = Anzahl mal Preis pro Typ kummuliert
    @Override
    public JSONArray getOrderVolumeForAllWeeks09() {
        //Reserved --> check if collection exists
        String persistentCollection = "MH_" + this.moebelherstellerEnum.toString() + "_getOrderVolumeForAllWeeks09";
        if (MongoDBConnection.checkIfCollectionExists(persistentCollection)) {
            LOG.debug("Collection existiert bereits");
            //Prüfen ob Collection aktuell ist, sprich weniger oder gleich alt wie die aktuelleKW
            JSONArray jar_DB = mongoreader.ReadAll2JSONArray(persistentCollection);
            boolean collUp2Date = checkIfCollectionAgeUp2date(jar_DB);
            if (collUp2Date) {
                LOG.debug("Collection ist aktuell in der DB vorhanden und wird direkt aus der DB zurückgegeben");
                return jar_DB;
            } else { //else --> Collection löschen und FUnktion normal ausführen
                MongoDBConnection.getDatabase().getCollection(persistentCollection).drop();
            }
        }

        //Core (wenn Collection nicht existiert
        ArrayList<Document> alleMoebelHauser = mongoreader.ReadAll2Arraylist("MH_" + this.moebelherstellerEnum.toString());
        JSONArray OrdersForAlWeeks = new JSONArray();

        //1. Alle Möbelhäuser iterieren
        for (Document mh_doc : alleMoebelHauser) {
            String moebelhausCode = mh_doc.get("moebelhausCode").toString();
            int aktuellesJahr = Calendar.getInstance().get(Calendar.YEAR);

            FindIterable<Document> matchings = findIterableBuilder(("Bestellungen_" + moebelhausCode + "_" + this.moebelherstellerEnum.toString()), new Document("datum", 1)
                    .append("bestellungPositionListe.anzahl", 1)
                    .append("bestellungPositionListe.produktTyp.preis", 1), new BasicDBObject());

            //3. Jede Bestellung iterieren und Kalenderwoche extrahieren
            HashMap<Integer, Double> KW_Bestellungen = new HashMap<>();
            for (Iterator<Document> bestellung_it = matchings.iterator(); bestellung_it.hasNext();) {
                Document maindoc = bestellung_it.next();
                Date datumBestellung = maindoc.getDate("datum");
                //get Calendarweek
                Calendar cal = Calendar.getInstance();
                cal.setTime(datumBestellung);
                if (cal.get(Calendar.YEAR) == aktuellesJahr) {
                    int week = cal.get(Calendar.WEEK_OF_YEAR);
                    Double bestellvolumen = 0d;
                    ///***
                    List<Document> bestellPosition = (List<Document>) maindoc.get("bestellungPositionListe");
                    for (Document position_doc : bestellPosition) {
                        Integer anzahl = position_doc.getInteger("anzahl");
                        Document produktTyp = (Document) bestellPosition.get(0).get("produktTyp");
                        Double preis = getPreistypeSafe(produktTyp);
                        bestellvolumen += (preis * anzahl);                         //Preis kummulieren
                    }
                    ///***
                    if (!KW_Bestellungen.containsKey(week)) {
                        KW_Bestellungen.put(week, bestellvolumen);
                    } else {
                        KW_Bestellungen.put(week, KW_Bestellungen.get(week) + bestellvolumen); //Increment by BEstellvolumen if key exists
                    }
                }
            }

            HashMap<Integer, Double> KW_Bestellungen_sorted = MapSorterDouble(KW_Bestellungen, false);
            //Add to JSON
            JSONArray bestellungenProKW = new JSONArray();
            for (int i = 0; i < KW_Bestellungen_sorted.size(); i++) {
                JSONObject jOB = new JSONObject();
                Integer key = (Integer) KW_Bestellungen_sorted.keySet().toArray()[i]; //week
                Double value = (Double) KW_Bestellungen_sorted.values().toArray()[i]; //Bestellvolumen
                jOB.put("Bestellvolumen", value);
                jOB.put("KW", key);
                bestellungenProKW.put(jOB);
            }
            //JSONArray ergänzen
            JSONObject jo_moebelhaus = new JSONObject();
            jo_moebelhaus.put("moebelhausCode", moebelhausCode);
            jo_moebelhaus.put("Bestellungen", bestellungenProKW);
            OrdersForAlWeeks.put(jo_moebelhaus);
        }
        //Reserved --> Save collection
        MongoDBManageCollection manager = new MongoDBManageCollection();
        manager.AddArray2Collection(persistentCollection, OrdersForAlWeeks);

        return OrdersForAlWeeks;
    }

    ///
    //********Helpers********
    ///
    public static boolean isDateValid(String date) {
        try {
            //MongoDBFormat
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS\'Z\'");

            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Funktion prüft ob die neusten Einträge im JSONArray Älter als die
     * aktuelle Kalenderwoche (KW) sind. Sobald Einträge gefunden werden, die
     * grösser oder Gleich der aktuellen KW sind wird true zurückgegeben und die
     * Collection wird als "aktuell" angesehen.
     *
     * @param persistedArray Das zu prüfende Array in der Form [ {
     * moebelhauscode: "abc"}, { Bestellungen : [ { anzahlBestellungen : x},{KW
     * : y}]]
     * @return true wennn aktuell | false wenn nicht aktuell
     *
     */
    public boolean checkIfCollectionAgeUp2date(JSONArray persistedArray) {
        //Aktuelle KW berechnen
        Calendar now = Calendar.getInstance();
        int aktuelleKW = now.get(Calendar.WEEK_OF_YEAR);
        int dbKW = 0;

        for (int i = 0; i < persistedArray.length(); i++) {
            JSONObject job_ArrayItem = persistedArray.getJSONObject(i); //Bestellungen Array ist immer an 1 Stelle
            JSONArray jar_Bestellungen = job_ArrayItem.getJSONArray("Bestellungen");
            for (int j = 0; j < jar_Bestellungen.length(); j++) {
                JSONObject job_b = jar_Bestellungen.getJSONObject(j);
                dbKW = job_b.getInt("KW");
                LOG.debug("KW: " + dbKW);
                if (dbKW >= aktuelleKW) {
                    LOG.debug("KW ist up2date, Collection ist aktuell");
                    LOG.debug("AktuelleKW: " + aktuelleKW + " | dbKW: " + dbKW);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Liefert den Preis vom Preisfeld eines BSON Documents Typsicher als Double
     * zurück.
     *
     * @param sourceDoc source BSON
     * @return Preis als Double
     */
    private Double getPreistypeSafe(Document sourceDoc) {
        Double preis;
        //Prüfen ob der Typ nicht double ist und dann konvertieren!
        if ((sourceDoc.get("preis")) instanceof Integer) {
            //Int gefunden
            preis = sourceDoc.get("preis", Integer.class).doubleValue();
        } else {
            //Double gefunden
            preis = sourceDoc.get("preis", Double.class);
        }
        return preis;
    }

    /**
     * Sortiert eine Hashmap nach Value oder Key.
     *
     * @param sourceMap Quell map zum sortieren
     * @param sortByValue falls true wird nach value sortiert, sonst nach key
     * @return sortierte Map
     */
    private HashMap<Integer, Integer> MapSorter(HashMap<Integer, Integer> sourceMap, boolean sortByValue) {
        if (sortByValue) {
            HashMap<Integer, Integer> sortedMap
                    = sourceMap.entrySet().stream()
                            .sorted(reverseOrder(Map.Entry.comparingByValue())) //Reverse ORder damit die grössten Werte zuerst kommen
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                    (e1, e2) -> e1, LinkedHashMap::new));
            return sortedMap;
        } else {
            HashMap<Integer, Integer> sortedMap
                    = sourceMap.entrySet().stream()
                            .sorted(Map.Entry.comparingByKey()) //Ohne Reverse Order damit die kleinste ID zuoberst ist
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                    (e1, e2) -> e1, LinkedHashMap::new));
            return sortedMap;
        }
    }

    //ToDO, elegantere Variante finden, keine Zeit dafür im Moment
    private HashMap<Integer, Double> MapSorterDouble(HashMap<Integer, Double> sourceMap, boolean sortByValue) {
        if (sortByValue) {
            HashMap<Integer, Double> sortedMap
                    = sourceMap.entrySet().stream()
                            .sorted(reverseOrder(Map.Entry.comparingByValue())) //Reverse ORder damit die grössten Werte zuerst kommen
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                    (e1, e2) -> e1, LinkedHashMap::new));
            return sortedMap;
        } else {
            HashMap<Integer, Double> sortedMap
                    = sourceMap.entrySet().stream()
                            .sorted(Map.Entry.comparingByKey()) //Ohne Reverse Order damit die kleinste ID zuoberst ist
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                    (e1, e2) -> e1, LinkedHashMap::new));
            return sortedMap;
        }
    }

    /**
     * Liefert einen FindIterable Iterator aus der Datenbank zurück welcher den
     * Kriterien entspricht.
     *
     * @param collectionName Name der zu durchsuchenden Collection
     * @param projectionDocument Das Projektionsdocument (kann auch Mehrstufig
     * sein .append)
     * @param searchQuery Ein Searchquery mit speziellen Kriterien | Falls keine
     * speziellen Kriterien vorhanden sind ein neues BasicDBObject leer
     * übergeben.
     * @return FindIterable matchings
     */
    private FindIterable<Document> findIterableBuilder(final String collectionName, final Document projectionDocument, final BasicDBObject searchQuery) {
        FindIterable<Document> matchings = MongoDBConnection.getDatabase().getCollection((collectionName)).find(searchQuery)
                .projection(projectionDocument);

        return matchings;
    }

}
