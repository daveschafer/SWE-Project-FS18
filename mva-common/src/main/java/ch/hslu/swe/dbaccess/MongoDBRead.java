/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.dbaccess;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Klasse zum auslesen von Datensätzen aus einer MongoDB.
 *
 * @author Dave
 */
public class MongoDBRead implements DBRead {

    //Global Vars
    private MongoDatabase database;
    private static final Logger LOG = LogManager.getLogger(MongoDBRead.class);

    /**
     * Initialisiert eine Verbindung zur Standard MongoDB.
     */
    public MongoDBRead() {
        //Use Default Database.
        this.database = MongoDBConnection.getDatabase();
    }

    @Override
    public MongoCollection<Document> ReadALL(String Collection_Name) {
        return this.database.getCollection(Collection_Name);
    }

    
    /**
     * Gibt eine Arraylist mit allen Einträgen der Collection zurück.
     *
     * @param Collection_Name Name der Collection
     * @return Arraylist
     */
    public ArrayList<Document> ReadAll2Arraylist(String Collection_Name) {
        MongoCollection collection = ReadALL(Collection_Name);
        ArrayList<Document> documentList = new ArrayList<>();

        try (MongoCursor cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                // Bei Match --> Add to Array
                documentList.add((Document) cursor.next());
            }
        }
        return documentList;
    }

    /**
     * Gibt ein JSONArray mit allen Einträgen der Collection zurück.
     *
     * @param Collection_Name Name der Collection
     * @return JSONArray
     */
    public JSONArray ReadAll2JSONArray(String Collection_Name) {
        JSONArray jsonArr = new JSONArray(ReadAll2Arraylist(Collection_Name));
        return jsonArr;
    }

    @Override
    public ArrayList<Document> ReadEntry(String Collection_Name, int entry_id) {
        MongoCollection collection = ReadALL(Collection_Name);
        ArrayList<Document> documentList = new ArrayList<>();

        //Konstruiert "WHERE" - Query, um anschliessend auf das Element mit der "entry_id" zu filtern
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("id", entry_id);

        //try with ressource :)
        try (MongoCursor cursor = collection.find(whereQuery).iterator()) {
            while (cursor.hasNext()) {
                // Bei Match --> Add to Array
                documentList.add((Document) cursor.next());
            }
        }
        return documentList;
    }

    @Override
    public ArrayList<Document> SearchEntries(String Collection_Name, BasicDBObject searchQuery) {
        MongoCollection collection = ReadALL(Collection_Name);
        ArrayList<Document> documentList = new ArrayList<>();

        if (searchQuery.isEmpty()) {
            LOG.error("Leere searchquery nicht erlaubt!");
        }

        try (MongoCursor cursor = collection.find(searchQuery).iterator()) {
            while (cursor.hasNext()) {
                // Bei Match --> Add to Array
                documentList.add((Document) cursor.next());
            }
        }
        return documentList;
    }
}
