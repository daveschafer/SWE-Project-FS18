/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.dbaccess;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Hinzufügen von neuen Datensätze in eine bestehende Collection.
 * @author Dave
 */
public interface DBAdd2Collection {
    
    /**
     * Fügt einen neuen Datensatz der Collection zu (ohne auf Konflikte oder duplizierte Objekte zu achten).
     * 
     * @param Collection_Name Name der Collection 
     * @param jsonArray Neuer Datensatz, welcher der Collection hinzugefügt wird
     */
    void AddArray2Collection(String Collection_Name, JSONArray jsonArray);
    
    /**
     * Fügt einen einzelnen Datensatz (JSONObject) der Collection hinzu (ohne auf Konflikte oder duplizierte Objekte zu achten).
     * @param Collection_Name Name der Collection
     * @param json Neuer Datensatz, JSONObject
     */
    void AddEntry(String Collection_Name, JSONObject json);

}
