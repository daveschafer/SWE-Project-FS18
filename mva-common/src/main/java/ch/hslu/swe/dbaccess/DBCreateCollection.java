/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.dbaccess;

import org.json.JSONArray;

/**
 * Neue Collection erstellen.
 * @author Dave
 */
public interface DBCreateCollection {
 
    /**
     * Erstellen einer MongoDB Collection.
     * 
     * @param Collection_Name Name der Collection
     * @param Array Quelldaten in JSONArray
     */
     void CreateColletion(String Collection_Name, JSONArray Array);
}
