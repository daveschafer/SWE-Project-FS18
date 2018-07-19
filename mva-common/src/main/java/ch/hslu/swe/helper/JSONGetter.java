/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import org.apache.commons.validator.UrlValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Diese Klasse stellt sämtliche Hilfsmethoden für JSON zur Verfügung.
 *
 * @author Dave
 */
public class JSONGetter {

    private static final Logger LOG = LogManager.getLogger(JSONGetter.class);

    

    /**
     * Gibt ein JSONObject vom einer URL zurück.
     *
     * @param urlString URL Pfad des JSONs
     * @param addTimestamp <p style="color:green;">true</p>: Dem JSON Objekt
     * wird ein Timestamp hinzugefügt. <br>
     * <p style="color:red;">false</p>: Das JSON Object bleibt unverändert.
     * @return JSONObject
     * @throws IOException - Bei Lesefehlern von der URL
     * @throws JSONException - Falls die URL kein JSON beinhaltet wird eine
     * Exception geworfen
     * @throws java.net.URISyntaxException Bei einer inkorrekten URL wird eine
     * URISyntaxException geworfen
     *
     */
    public static JSONObject getJSONObject(String urlString, boolean addTimestamp) throws JSONException, IOException, URISyntaxException {
        String jsonText = readJsonFromUrl(urlString);
        JSONObject jsonObj = new JSONObject(jsonText);
        if (addTimestamp) {
            JSONGetter.addTimestamp(jsonObj);
            LOG.info("Timestamp hinzugefügt: " + jsonObj.get("timestamp"));
        }
        return jsonObj;
    }

    /**
     * Gibt ein JSONArray vom einer URL zurück.
     *
     * @param urlString URL Pfad des JSONs
     * @param addTimestamp <p style="color:green;">true</p>: Dem JSON Objekt
     * wird ein Timestamp hinzugefügt. <br>
     * <p style="color:red;">false</p>: Das JSON Object bleibt unverändert.
     * @return JSONArray
     * @throws IOException - Bei Lesefehlern von der URL
     * @throws JSONException - Falls die URL kein JSON beinhaltet wird eine
     * Exception geworfen
     * @throws java.net.URISyntaxException Bei einer inkorrekten URL wird eine
     * URISyntaxException geworfen
     *
     */
    public static JSONArray getJSONArray(String urlString, boolean addTimestamp) throws JSONException, IOException, URISyntaxException {
        String jsonText = readJsonFromUrl(urlString);
        try {
            JSONArray jsonArray = new JSONArray(jsonText);

            if (addTimestamp) {
                JSONGetter.addTimestamp2Array(jsonArray);
            }
            return jsonArray;
        } catch (JSONException ex) {
            LOG.error("Problem beim Import des JSONArrays (Array leer?!)");
            LOG.error("Leeres Array wird zurückgegeben");
            return new JSONArray();
        }
    }

    /**
     * Fügt einem JSONObject einen Timestamp hinzu
     *
     * @param JSONObject Quell JSONObject
     */
    private static void addTimestamp(JSONObject JSONObject) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        JSONObject.put("timestamp", timestamp.toString());
    }

    /**
     * Fügt jedem JSONOBject eines JSONArrays einen Timestamp hinzu.
     *
     * @param JSONArray Quell JSONArray
     */
    private static void addTimestamp2Array(JSONArray JSONArray) {
        for (int i = 0; i < JSONArray.length(); i++) {
            JSONGetter.addTimestamp(JSONArray.getJSONObject(i));
        }
    }
    
    ////
    ////***Helpers***
    ////
    

    //Helper URLValidator
    /**
     * Validiert ob eine URL im korrekten Format daher kommt.
     * 
     * @param url Quell URL
     * @return true oder false
     */
    private static boolean urlValidator(String url) {
        //nur http und https connections sind valid
        String[] schemes = {"http", "https"}; // DEFAULT schemes = "http", "https", "ftp"
        UrlValidator urlValidator = new UrlValidator(schemes);
        if (urlValidator.isValid(url)) {
            LOG.info("Valide URL gefunden!");
            return true;
        } else {
            LOG.error("Invalide URL gefunden!: " + url);
            return false;
        }
    }

    /**
     * Liest ein JSON Objekt aus einer URL und gibt das JSON Object/Array als
     * String zurück.
     *
     * @param urlString URL Pfad des JSONs
     * @return JSON Object/Array als String
     * @throws IOException - Bei Lesefehlern von der URL
     * @throws JSONException - Falls die URL kein JSON beinhaltet wird eine
     * Exception geworfen
     * @throws java.net.URISyntaxException Bei einer inkorrekten URL wird eine
     * URISyntaxException geworfen
     */
    public static String readJsonFromUrl(String urlString) throws IOException, JSONException, URISyntaxException {
        //Zuerst prüfen ob der String Valid ist, ansonsten abbruch
        if (!urlValidator(urlString)) {
            LOG.error("Invalid URL for JSON!");
            throw new URISyntaxException(urlString, "Ungültige URL");
        }
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1) {
                buffer.append(chars, 0, read);
            }

            return buffer.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
