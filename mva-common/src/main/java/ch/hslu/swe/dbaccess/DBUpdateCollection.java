/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.dbaccess;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Eine bestehende Collection aktualiseren (Duplicate Aware). Falls Einträge noch nicht existieren werden Sie erstellt.
 * @author Dave
 */
public interface DBUpdateCollection {
    
    /**
     * Eine Collection Aktualisieren (Neue Werte werden überschrieben, unveränderte bleiben erhalten, neue werdne hinzugefügt).
     * @param Collection_Name Name der Collection
     * @param updatejsonArray  Update Daten in JSONArray Format
     */
    void UpdateCollectionALL(String Collection_Name, JSONArray updatejsonArray);
    
    /**
     * Einen spezifischen DB Eintrag aktualisieren. Falls der EIntrag nicht existiert, wird er erstellt.
     * @param Collection_Name Name der Collection
     * @param updateJSON Die neue JSONObject Sourcedatei
     */
    void UpdateCollectionEntry(String Collection_Name, JSONObject updateJSON);
}
