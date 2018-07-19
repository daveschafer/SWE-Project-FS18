/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.helper;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 *
 * @author Dave
 */
public class MONGODBConverter_Test {

    private static final Logger LOG = LogManager.getLogger(MONGODBConverter_Test.class);

    public MONGODBConverter_Test() {
    }

    private static JSONObject createJSONObject() throws JSONException {
        JSONObject item = new JSONObject();
        item.put("information", "test");
        item.put("id", 3);
        item.put("name", "course1");
        return item;
    }

    private static JSONArray createJSONArray() throws JSONException {
        JSONArray array = new JSONArray();
        array.put(createJSONObject());
        array.put(createJSONObject());
        array.put(createJSONObject());
        return array;
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    @Test
    @DisplayName("Test: Validiert ob JSONObjecte erfolgreich in korrekte Documente umgewandelt werden")
    public void getDocument_test() throws JSONException {
        JSONObject exampleJO = createJSONObject();
        Document result = MONGODBConverter.getDocument(exampleJO);
        JSONAssert.assertEquals(result.toJson(), exampleJO, false);         //Ein JSONobject zu Dokument zu JSONObject Dokument soll gleich wie das Original sein.
        result.put("zusatzwert", "notneeded");
        JSONAssert.assertNotEquals(result.toJson(), exampleJO, false); //Das result wurde verändert, somit soll ein Fehler geworfen werden
    }

    @Test
    @DisplayName("Test: Validiert ob JSONArrays erfolgreich in korrekte List<Document> Collections umgewandelt werden")
    public void getDocumentsList_test() throws JSONException {
        JSONArray exampleJA = createJSONArray();
        MONGODBConverter.getDocumentsList(exampleJA);
        List<Document> results = MONGODBConverter.getDocumentsList(exampleJA);

        for (int i = 0; i < exampleJA.length(); i++) {
            JSONAssert.assertEquals(results.get(i).toJson(), exampleJA.getJSONObject(i), false);             //Jedes JSON in der List sollte noch dasselbe sein wie im JSONArray
            results.get(i).put("zusatzwert", "notneeded");
            JSONAssert.assertNotEquals(results.get(i).toJson(), exampleJA.getJSONObject(i), false);             //List geändert, sollte nun false sein
        }
    }
}
