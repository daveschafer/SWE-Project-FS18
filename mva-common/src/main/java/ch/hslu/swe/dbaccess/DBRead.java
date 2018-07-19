/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.dbaccess;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;

/**
 * Lesen von einer Collection.
 * @author Dave
 */
public interface DBRead {
    
    /**
     * Gibt alle EInträge der Collection zurück.
     * @param Collection_Name Name der Collection
     * @return Gibt eine Mongocollection zurück
     */
     MongoCollection ReadALL(String Collection_Name);
    
    /**
     * Gibt ein zu einer ID korrespondierendes Document zurück.
     * @param Collection_Name Name der Collection
     * @param entry_id UID des gesuchten Elements
     * @return Document.
     */
     ArrayList<Document> ReadEntry(String Collection_Name, int entry_id);
    
    /**
     * Liefert eine ArrayList von Documenten welche das Suchkriterium erfüllen.
     * @param Collection_Name Name der Collectiom
     * @param searchQuery Query des gesuchten Typs (z.B. query.puty("fieldname", "value");
     * @return ArrayList mit den zutreffenden Documents
     */
     ArrayList<Document> SearchEntries(String Collection_Name,BasicDBObject searchQuery); //---> Gibt einen MongoDB Query Tipp für solche ANfragen
}
