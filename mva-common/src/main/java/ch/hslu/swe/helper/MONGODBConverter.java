/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.helper;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse stellt Hilfsfunktionen zur Verwaltung einer MongoDB zur
 * Verfügung.
 *
 * @author Dave
 */
public class MONGODBConverter {

    /**
     * Liefert eine List mit Document Objekten zurück (benötigt für MongoDB).
     *
     * @param sourcejsonArray Quell JSON Array
     * @return Ein List Objekt mit Documents (MongoDB DBObject)
     */
    public static List<Document> getDocumentsList(JSONArray sourcejsonArray) {
        List<Document> documentsList = new ArrayList<>();

        //Jedes Objekt aus dem JSONArray in ein Document Objekt parsen und in die neue Liste Übergeben
        for (int i = 0; i < sourcejsonArray.length(); i++) {
            documentsList.add(Document.parse(sourcejsonArray.getJSONObject(i).toString()));
        }
        return documentsList;
    }

    /**
     * Liefert ein Document Object (MongoDB DBObject) aus einem JSON Objekt
     * zurück.
     *
     * @param sourcejsonObject Quell JSON Objekt
     * @return Document
     */
    public static Document getDocument(JSONObject sourcejsonObject) {
        return Document.parse(sourcejsonObject.toString());
    }

}
