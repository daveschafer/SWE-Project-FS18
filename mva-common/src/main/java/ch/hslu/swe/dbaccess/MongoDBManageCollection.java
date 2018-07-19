/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.dbaccess;

import ch.hslu.swe.helper.MONGODBConverter;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Erstellen von neuen Collection, bearbeiten von bestehenden. Es können ganze
 * Collections oder einzelne Einträge bearbeitet werden.
 *
 * @author Dave
 */
public class MongoDBManageCollection implements DBUpdateCollection, DBAdd2Collection {
//Global Vars

    private MongoDatabase database;
    private static final Logger LOG = LogManager.getLogger(MongoDBManageCollection.class);

    /**
     * Initialisiert eine Verbindung zur Standard MongoDB.
     */
    public MongoDBManageCollection() {
        //Use Default Database.
        this.database = MongoDBConnection.getDatabase();
    }

    @Override
    public void UpdateCollectionALL(String Collection_Name, JSONArray updatejsonArray) {
        //Prüfe ob Collection noch nicht existiert
        if (!MongoDBConnection.checkIfCollectionExists(Collection_Name)) {
            AddArray2Collection(Collection_Name, updatejsonArray);
        }
        LOG.debug("entered UpdateALL");
        List<Document> documentList = MONGODBConverter.getDocumentsList(updatejsonArray);
        MongoCollection<Document> collection = this.database.getCollection(Collection_Name);
        int replaceCounter = 0;
        //List<Document> in die collection einfügen
        for (Document updatedDoc : documentList) {
            collection.findOneAndReplace(
                    Filters.eq("id", updatedDoc.get("id")),
                    updatedDoc, new FindOneAndReplaceOptions().upsert(true)
            );
            replaceCounter++;
        }
        LOG.debug("" + replaceCounter + " Dokumente Aktualisiert!");
    }

    @Override
    public void UpdateCollectionEntry(String Collection_Name, JSONObject updateJSON) {
        Document documentObject = MONGODBConverter.getDocument(updateJSON);
        MongoCollection<Document> collection = this.database.getCollection(Collection_Name);

        //List<Document> in die collection einfügen
        Document oldDoc = (Document) collection.findOneAndReplace(
                Filters.eq("id", updateJSON.get("id")),
                documentObject, new FindOneAndReplaceOptions().upsert(true)
        );

        if (oldDoc == null) {
            oldDoc = new Document("value", "overwritten");
        }

        LOG.debug("Alter Wert: " + oldDoc.toString());
        LOG.debug("Neuer Wert: " + documentObject.toString());
    }

    @Override
    public void AddArray2Collection(String Collection_Name, JSONArray jsonArray) {
        List<Document> documentList = MONGODBConverter.getDocumentsList(jsonArray);
        MongoCollection<Document> collection = this.database.getCollection(Collection_Name);

        //List<Document> in die collection einfügen
        collection.insertMany(documentList);

        LOG.debug("Einfuegen von '" + documentList.size() + "' Objekten abgeschlossen.");
        LOG.debug("Array beinhaltet neu: '" + collection.count() + "' Objekte");

    }

    @Override
    public void AddEntry(String Collection_Name, JSONObject json) {
        Document documentObject = MONGODBConverter.getDocument(json);
        MongoCollection<Document> collection = this.database.getCollection(Collection_Name);

        //List<Document> in die collection einfügen
        collection.insertOne(documentObject);

        LOG.debug("Einfuegen des Objekts abgeschlossen.");
        LOG.debug("Array beinhaltet neu: '" + collection.count() + "' Objekte");
    }
}
