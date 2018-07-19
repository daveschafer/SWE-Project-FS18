/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.DataCollector;

import ch.hslu.swe.dbaccess.MongoDBConnection;
import ch.hslu.swe.dbaccess.MongoDBManageCollection;
import ch.hslu.swe.dbaccess.MongoDBRead;
import ch.hslu.swe.helper.JSONGetter;
import ch.hslu.swe.helper.MoebelherstellerEnum;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimerTask;
import org.apache.logging.log4j.LogManager;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 * @author Dave
 */
public class RESTCollectorMongo extends TimerTask implements RESTCollector {

    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(RESTCollectorMongo.class);
    private final MoebelherstellerEnum moebelhersteller;

    public RESTCollectorMongo(MoebelherstellerEnum moebelhersteller) {
        this.moebelhersteller = moebelhersteller;
    }

    //Helper
    /**
     * Generischer Import. Achtung: Die Möbelhäuser Collection (MH_xxx) muss
     * bereits bestehen.
     *
     * @param CollectionTyp : Name der Collection, z.B. Bestellungen_... ,
     * Lieferungen_..., MH_... bestehen muss "true", ansonsten false
     * @return
     */
    private boolean genericImporter(CollectionTyp CollectionTyp) {
        MongoDBRead reader = new MongoDBRead();
        String mhCollection = ("MH_" + moebelhersteller.toString());
        //Möbelhäuser Collection muss zwingend schon bestehen
        if (!(MongoDBConnection.checkIfCollectionExists(mhCollection))) {
            LOG.error("Möbelhäuser noch nicht importiert!");
            return false;
        }
        ArrayList<Document> alleMoebelHauser = reader.ReadAll2Arraylist(mhCollection);

        LOG.debug("Möbelhauser gefunden, Anzahl: " + alleMoebelHauser.size());
        MongoDBManageCollection collectionAdder = new MongoDBManageCollection();

        //Iteration über alle Moebelhäuser, für jedes Haus eine Collection erstellen
        for (Document doc : alleMoebelHauser) {
            String moebelhausCode = doc.get("moebelhausCode").toString();
            LOG.debug("moebelhausCode: " + moebelhausCode);
            try {
                JSONArray jsonREST = JSONGetter.getJSONArray(moebelhersteller.returnBaseURL() + CollectionTyp.returnSubURL() + "moebelhaus?code=" + moebelhausCode, true);
                LOG.debug("Erstelle neue Collection: '" + CollectionTyp.toString() + "_" + moebelhausCode + "_" + moebelhersteller.toString() + "'");
                //prüfen ob leeres Array zurückkommt
                if (jsonREST.length() > 0) {
                    collectionAdder.UpdateCollectionALL(CollectionTyp.toString() + "_" + moebelhausCode + "_" + moebelhersteller.toString(), jsonREST);
                } else {
                    LOG.warn("Leeres JSONArray (keine Einträge für " + moebelhausCode + "?) | Collection wird nicht erstellt");
                }
            } catch (IOException | URISyntaxException | JSONException ex) {
                LOG.error("Fehler beim importieren der Collection '" + moebelhausCode + "' : " + ex);
                return false;
            }
        }
        try {
            //datefixer muss nach jeder Bestellung oder Lieferungs Import laufen
            this.dateTimeFix();
        } catch (ParseException ex) {
            LOG.error("Problem beim Date-Parsen (falsches Datumsformat in der DB) : " + ex);
        }
        return true;
    }

    //Importiert alle Möbelhaus Bestellungen (als einzelne Collections).
    @Override
    public boolean importAllOrders() {
        return genericImporter(CollectionTyp.BESTELLUNG);
    }

    /**
     * Importiert alle Bestellungen in eine grosse Collection
     * "Bestellungen_Alle_FurMan".
     *
     * @return true bei erfolgreichem import.
     */
    @Deprecated
    private boolean importAllOrdersAsOne() {
        //Vor jedem aufruf muss die bestehene COllection gedroppt werden
        String allCollection = ("Bestellungen_Alle_" + moebelhersteller.toString());
        MongoDBConnection.getDatabase().getCollection(allCollection).drop();

        MongoDBRead reader = new MongoDBRead();
        MongoDBManageCollection manager = new MongoDBManageCollection();
        String mhCollection = ("MH_" + moebelhersteller.toString());
        ArrayList<Document> alleMoebelHauser = reader.ReadAll2Arraylist(mhCollection);
        try {
            for (Document doc : alleMoebelHauser) {
                String moebelhausCode = doc.get("moebelhausCode").toString();
                LOG.debug("Add MH to Alle Collection: " + moebelhausCode);
                //Alle Collections auslesen und in die "Alle" Collection integrieren
                manager.AddArray2Collection(allCollection, reader.ReadAll2JSONArray("Bestellungen_" + moebelhausCode + "_" + moebelhersteller.toString()));
            }
        } catch (Exception e) {
            LOG.error("Something went wrong: " + e);
            return false;
        }
        return true;
    }

    //Importiert alle Möbelhaus Lieferungen (als einzelne Collections).
    @Override
    public boolean importAllDeliveries() {
        return genericImporter(CollectionTyp.LIEFERUNG);
    }

    @Override
    public boolean importAllMoebelhauser() {
        try {
            JSONArray alleMH = JSONGetter.getJSONArray(moebelhersteller.returnBaseURL() + CollectionTyp.MOEBELHAUS.returnSubURL(), false);
            MongoDBManageCollection collectionAdder = new MongoDBManageCollection();
            collectionAdder.UpdateCollectionALL("MH_" + moebelhersteller.toString(), alleMH);
            return true;
        } catch (JSONException | IOException | URISyntaxException ex) {
            LOG.error("Fehler beim importieren der Möbelhäuser für " + moebelhersteller.toString() + " : " + ex);
            return false;
        }
    }

    @Override
    public boolean importAllProducts() {
        try {
            JSONArray alleProdukte = JSONGetter.getJSONArray(moebelhersteller.returnBaseURL() + CollectionTyp.PRODUKT.returnSubURL(), false);
            MongoDBManageCollection collectionAdder = new MongoDBManageCollection();
            collectionAdder.UpdateCollectionALL("Produkte_" + moebelhersteller.toString(), alleProdukte);
            return true;
        } catch (JSONException | IOException | URISyntaxException ex) {
            LOG.error("Fehler beim importieren der Produkte für " + moebelhersteller.toString() + " : " + ex);
            return false;
        }
    }

    /**
     * Diese Methode konvertiert das "String" Datum in der MongoDB in einen
     * korrekten "Date" Typ. Es werden alle COllections nach falschen
     * "String"-Datums durchsucht.
     *
     * @throws java.text.ParseException
     */
    public void dateTimeFix() throws ParseException {
        MongoDatabase database = MongoDBConnection.getDatabase();
        MongoIterable<String> matchings = database.listCollectionNames();
        //Alle Collections iterieren
        for (Iterator<String> allCollections = matchings.iterator(); allCollections.hasNext();) {
            String collname = allCollections.next();
            //ISODate Formatter
            DateTimeFormatter mongoDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS\'Z\'");
            DateTimeFormatter gislerschesDateFormat = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            DateTimeFormatter gislerschesDateFormatv2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.ENGLISH);

            //Search Query definieren, MongoDB ist toll
            BasicDBObject datum = new BasicDBObject("datum", new BasicDBObject("$type", 2));
            BasicDBObject bestellung_datum = new BasicDBObject("bestellung.datum", new BasicDBObject("$type", 2));
            BasicDBList or = new BasicDBList();
            or.add(datum); //datum
            or.add(bestellung_datum); //or bestellung.datum
            BasicDBObject searchquery = new BasicDBObject("$or", or);
            MongoCollection<Document> currentCollection = database.getCollection(collname); //brauchen wir später noch für den Update
            FindIterable<Document> dateMatches = currentCollection.find(searchquery); //search!

            //Alle matches iterieren
            for (Iterator<Document> date_find = dateMatches.iterator(); date_find.hasNext();) {
                Document maindoc = date_find.next();

                LOG.debug("Col(" + collname + ") |Datum exists? : " + maindoc.containsKey("datum"));
                LOG.debug("bestellung exists? : " + maindoc.containsKey("bestellung"));

                if (maindoc.containsKey("datum")) {
                    LOG.debug("Altes Datum: " + maindoc.get("datum").toString());
                    LocalDateTime updatedDate;
                    try { //ja das ist nicht schön gelöst, ich würde es ja auch gerne anders machen!
                        updatedDate = LocalDateTime.parse(maindoc.get("datum").toString(), gislerschesDateFormat);
                    } catch (DateTimeParseException parsex) {
                        LOG.warn("Dateiformat v2 gefunden");
                        updatedDate = LocalDateTime.parse(maindoc.get("datum").toString(), gislerschesDateFormatv2);
                    }
                    Date updatedDateMongo = Date.from(updatedDate.toInstant(ZoneOffset.UTC));
                    updatedDate.format(mongoDateFormat);
                    LOG.debug("Neues Datum: " + updatedDateMongo);
                    maindoc.replace("datum", updatedDateMongo);
                    //Replace in Collection, MongoDB ist toll
                    currentCollection.findOneAndReplace(Filters.eq("_id", maindoc.get("_id")),
                            maindoc,
                            new FindOneAndReplaceOptions().upsert(true));
                }
                if (maindoc.containsKey("bestellung")) {
                    Document bestellungen_bson = (Document) maindoc.get("bestellung");
                    LOG.debug("bestellungen.datum vorhanden? :" + bestellungen_bson.containsKey("datum"));
                    if (bestellungen_bson.containsKey("datum")) {
                        LOG.debug("Altes Datum: " + bestellungen_bson.get("datum").toString());
                        LocalDateTime updatedDate; //= LocalDateTime.parse(position_doc.get("datum").toString(), gislerschesDateFormat);
                        try { //ja das ist nicht schön gelöst, ich würde es ja auch gerne anders machen!
                            updatedDate = LocalDateTime.parse(bestellungen_bson.get("datum").toString(), gislerschesDateFormat);
                        } catch (DateTimeParseException parsex) {
                            LOG.warn("Dateformat v2 gefunden");
                            updatedDate = LocalDateTime.parse(bestellungen_bson.get("datum").toString(), gislerschesDateFormatv2);
                        }
                        Date updatedDateMongo = Date.from(updatedDate.toInstant(ZoneOffset.UTC));
                        updatedDate.format(mongoDateFormat);
                        LOG.debug("Neues Datum: " + updatedDateMongo);
                        bestellungen_bson.replace("datum", updatedDateMongo);
                        maindoc.replace("bestellungen", bestellungen_bson);
                        //Replace in Collection
                        currentCollection.findOneAndReplace(Filters.eq("id", maindoc.get("id")),
                                maindoc,
                                new FindOneAndReplaceOptions().upsert(true));
                    }//ende if bestellung.datum
                } //ende if bestellung
            } //ende date_matches schleife
        } //ende "alle collections iterieren" schleife
    } //ende Methode

    @Override
    public void run() {
        //Einstiegspunkt für den TimerTask
        LOG.info("Timer task started at:" + new Date() + " for " + moebelhersteller.name());
        boolean importsOK = completeImport(); //imports ausführen

        if (importsOK) {
            LOG.debug("Alle Imports (" + moebelhersteller.name() + ") ohne Probleme abgeschlossen");
        } else {
            LOG.warn("Probleme während Imports(" + moebelhersteller.name() + " aufgetreten");
        }

        LOG.info("Timer task finished at:" + new Date() + " for " + moebelhersteller.name());
    }

    /**
     * Erledigt die Imports importAllMoebelhauser, importAllOrders,
     * importAllDeliveries und importAllProducts sowie den dateTimeFix in der
     * korrekten Reihenfolge.
     *
     * @return
     */
    public boolean completeImport() {
        //4 imports machen und am schluss bool verwerten
        boolean i1 = importAllMoebelhauser();
        boolean i2 = importAllOrders();
        boolean i3 = importAllDeliveries();
        boolean i4 = importAllProducts();

        try {
            dateTimeFix();
        } catch (ParseException ex) {
            LOG.error("DateTimeFix fehlgeschlagen, inkonsistente Datumstypen in der Datenbank!");
        }

        return (i1 && i2 && i3 && i4);
    }

}
