/*
 *
 *  * Copyright (c) 2018. Inäbnit Pascal
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
package ch.hslu.swe.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URISyntaxException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
//Junit5 FTW!
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

/**
 * Vorsicht, Test funktioniert nur im HSLU-Netzwerk oder via VPN!
 *
 * @author Ini
 */
public class JSONGetter_Test {

    private static final Logger LOG = LogManager.getLogger(JSONGetter.class);

    String addr = "10.177.1.94";
    int openPort = 8090;
    int timeOutMillis = 500;
    Socket soc = new Socket();

    /**
     * Methode für Verbindung zum Testserver http://10.177.1.94:8090/rmhr-test
     *
     * @return false falls connection failt
     */
    private boolean connectiontoServer() {
        try {
            soc.connect(new InetSocketAddress(addr, openPort), timeOutMillis);
            if (soc.isConnected()) {
                LOG.debug("Connection successful");
            }
            return true;

        } catch (IOException ex) {
            LOG.debug("Connection could not be established");
            return false;
        }
    }

    @Test
    @DisplayName("Test: Verbindung zum REST Server OK?")
    public void testconnectiontoserver() {
        assertTrue(() -> connectiontoServer());
    }

    @Test
    @DisplayName("Test: falsche URL soll eine URISyntaxException auslösen")
    public void urlValidator_URISyntaxException() {
        URISyntaxException excpectedException = Assertions.assertThrows(URISyntaxException.class, () -> {
            JSONGetter.readJsonFromUrl("httpx://wrong.url/");
        });
    }

    @Test
    @DisplayName("Test: Kontrolliert ob ein valides JSONObject von einer URL bezogen werden kann")
    public void getJSONObject_Test() throws JSONException, IOException, URISyntaxException {
        String referenceJO = "{\"ablageTablar\":{\"bezeichnung\":\"R1-TC0\",\"id\":5},\"beschreibung\":\"Individuell zusammenstellbar, höhenverstellbar 70-117cm, Untergestell eckig, Metall anthrazit, B 160 T 60 H 75 cm\",\"id\":137,\"maximalerBestand\":50,\"minimalerBestand\":1,\"name\":\"Computer-Tisch\",\"preis\":1199.0,\"typCode\":\"PCTI-Steve-2011\"}";
        JSONObject exampleJO = JSONGetter.getJSONObject("http://10.177.1.94:8090/rmhr-test/ws/katalog/typ/code/PCTI-Steve-2011", false);
        JSONAssert.assertEquals(referenceJO, exampleJO, true);

        referenceJO = "{\"the_spanish_inquisition\":{\"bezeichnung\":\"R1-TC0\",\"id\":5},\"beschreibung\":\"Individuell zusammenstellbar, höhenverstellbar 70-117cm, Untergestell eckig, Metall anthrazit, B 160 T 60 H 75 cm\",\"id\":137,\"maximalerBestand\":50,\"minimalerBestand\":1,\"name\":\"Computer-Tisch\",\"preis\":1199.0,\"typCode\":\"PCTI-Steve-2011\"}";
        JSONAssert.assertNotEquals(referenceJO, exampleJO, JSONCompareMode.STRICT); //Nach einer kleinen Anpassung dürfte das nicht mehr stimmen
    }

    @Test
    @DisplayName("Test: Kontrolliert ob ein valides JSONArray von einer URL bezogen werden kann")
    public void getJSONArray_Test() throws JSONException, IOException, URISyntaxException {
        String referenceJA = "[{\"adresse\":{\"ort\":\"Horw\",\"plz\":6048,\"strasse\":\"Bachstrasse 48-52\"},\"id\":666,\"kontakt\":{\"email\":\"info@moebelbucheli.ch\",\"telefon\":\"041 660 60 60\"},\"moebelhausCode\":\"MH_BUCHELI_HORW\",\"name\":\"Möbelhaus Bucheli GmbH\"},{\"adresse\":{\"ort\":\"Eschenbach LU\",\"plz\":6274,\"strasse\":\"Bundesstrasse 80\"},\"id\":667,\"kontakt\":{\"email\":\"info@mhegger.ch\",\"telefon\":\"041 855 66 77\"},\"moebelhausCode\":\"MH_EGGER_EBLU\",\"name\":\"Möbelhaus Egger AG\"},{\"adresse\":{\"ort\":\"Emmen\",\"plz\":6032,\"strasse\":\"Hasliring 60\"},\"id\":668,\"kontakt\":{\"email\":\"emmen@digamoebel.ch\",\"telefon\":\"041 268 84 84\"},\"moebelhausCode\":\"MH_DIGA_EMME\",\"name\":\"diga möbel ag\"},{\"adresse\":{\"ort\":\"Ibach\",\"plz\":6438,\"strasse\":\"Mythen-Centerstrasse 18\"},\"id\":669,\"kontakt\":{\"email\":\"ibach@micasa.ch\",\"telefon\":\"041 819 50 55\"},\"moebelhausCode\":\"MH_MICA_IBAS\",\"name\":\"micasa - Mythen-Center\"},{\"adresse\":{\"ort\":\"Zürich\",\"plz\":8051,\"strasse\":\"Ueberlandstrasse 423\"},\"id\":670,\"kontakt\":{\"email\":\"kontakt@schubigermoebel.ch\",\"telefon\":\"044 325 25 52\"},\"moebelhausCode\":\"MH_SCHU_ZHRI\",\"name\":\"schubiger möbel\"},{\"adresse\":{\"ort\":\"Spreitenbach\",\"plz\":8957,\"strasse\":\"Pfadackerstrasse 6\"},\"id\":671,\"kontakt\":{\"email\":\"kontakt@toptip.ch\",\"telefon\":\"056 417 90 20\"},\"moebelhausCode\":\"MH_TTIP_SPRE\",\"name\":\"Toptip\"},{\"adresse\":{\"ort\":\"Suhr\",\"plz\":5034,\"strasse\":\"Bernstrasse Ost 49\"},\"id\":672,\"kontakt\":{\"email\":\"moebel@pfister.ch\",\"telefon\":\"062 855 44 33\"},\"moebelhausCode\":\"MH_PFIS_SUHR\",\"name\":\"Möbel Pfister AG\"},{\"adresse\":{\"ort\":\"Schlieren\",\"plz\":8952,\"strasse\":\"Wagistrasse 20\"},\"id\":673,\"kontakt\":{\"email\":\"info@conforama.ch\",\"telefon\":\"044 738 28 88\"},\"moebelhausCode\":\"MH_CONF_SCHL\",\"name\":\"Conforama Wagi Shopping\"},{\"adresse\":{\"ort\":\"Pratteln\",\"plz\":4133,\"strasse\":\"Rütiweg 9\"},\"id\":674,\"kontakt\":{\"email\":\"kontakt@interio.ch\",\"telefon\":\"058 576 14 00\"},\"moebelhausCode\":\"MH_INTE_PRAT\",\"name\":\"Interio\"},{\"adresse\":{\"ort\":\"Pfäffikon SZ\",\"plz\":8808,\"strasse\":\"Schützenstrasse 2\"},\"id\":675,\"kontakt\":{\"email\":\"info@moebelmaerki.ch\",\"telefon\":\"055 410 22 22\"},\"moebelhausCode\":\"MH_MAER_PFSZ\",\"name\":\"möbel märki\"},{\"adresse\":{\"ort\":\"Egerkingen\",\"plz\":4622,\"strasse\":\"Lindenhagstrasse 3\"},\"id\":676,\"kontakt\":{\"email\":\"info@lipo.ch\",\"telefon\":\"062 288 60 10\"},\"moebelhausCode\":\"MH_LIPO_EGER\",\"name\":\"Lipo Einrichtungsmärkte\"},{\"adresse\":{\"ort\":\"Rothrist\",\"plz\":4852,\"strasse\":\"Rössliweg 43\"},\"id\":677,\"kontakt\":{\"email\":\"kontakt@moebelhubacher.ch\",\"telefon\":\"062 785 77 77\"},\"moebelhausCode\":\"MH_HUBA_RTHI\",\"name\":\"Möbel Hubacher AG\"},{\"adresse\":{\"ort\":\"Hinwil\",\"plz\":8340,\"strasse\":\"Wässeristrasse 28\"},\"id\":678,\"kontakt\":{\"email\":\"info@moebelferrari.ch\",\"telefon\":\"044 931 20 40\"},\"moebelhausCode\":\"MH_FERR_HINW\",\"name\":\"Möbel Ferrari AG\"},{\"adresse\":{\"ort\":\"Volketswil\",\"plz\":8604,\"strasse\":\"Brunnenstrasse 14\"},\"id\":679,\"kontakt\":{\"email\":\"info@moebelzentrumi.ch\",\"telefon\":\"044 908 42 42\"},\"moebelhausCode\":\"MH_MZMO_VOLK\",\"name\":\"MZ Möbelzentrum AG\"}]";
        JSONArray exampleJA = JSONGetter.getJSONArray("http://10.177.1.94:8090/rmhr-test/ws/moebelhaus", false);
        JSONAssert.assertEquals(referenceJA, exampleJA, true);

        referenceJA = "[{\"adresse\":{\"ort\":\"Emmen\",\"plz\":6020,\"strasse\":\"Riffigstrasse 18\"},\"id\":669,\"kontakt\":{\"email\":\"info@moebelbucheli.ch\",\"telefon\":\"041 660 60 60\"},\"moebelhausCode\":\"MH_BUCHELI_HORW\",\"name\":\"Möbelhaus Bucheli GmbH\"},{\"adresse\":{\"ort\":\"Eschenbach LU\",\"plz\":6274,\"strasse\":\"Bundesstrasse 80\"},\"id\":667,\"kontakt\":{\"email\":\"info@mhegger.ch\",\"telefon\":\"041 855 66 77\"},\"moebelhausCode\":\"MH_EGGER_EBLU\",\"name\":\"Möbelhaus Egger AG\"},{\"adresse\":{\"ort\":\"Emmen\",\"plz\":6032,\"strasse\":\"Hasliring 60\"},\"id\":668,\"kontakt\":{\"email\":\"emmen@digamoebel.ch\",\"telefon\":\"041 268 84 84\"},\"moebelhausCode\":\"MH_DIGA_EMME\",\"name\":\"diga möbel ag\"},{\"adresse\":{\"ort\":\"Ibach\",\"plz\":6438,\"strasse\":\"Mythen-Centerstrasse 18\"},\"id\":669,\"kontakt\":{\"email\":\"ibach@micasa.ch\",\"telefon\":\"041 819 50 55\"},\"moebelhausCode\":\"MH_MICA_IBAS\",\"name\":\"micasa - Mythen-Center\"},{\"adresse\":{\"ort\":\"Zürich\",\"plz\":8051,\"strasse\":\"Ueberlandstrasse 423\"},\"id\":670,\"kontakt\":{\"email\":\"kontakt@schubigermoebel.ch\",\"telefon\":\"044 325 25 52\"},\"moebelhausCode\":\"MH_SCHU_ZHRI\",\"name\":\"schubiger möbel\"},{\"adresse\":{\"ort\":\"Spreitenbach\",\"plz\":8957,\"strasse\":\"Pfadackerstrasse 6\"},\"id\":671,\"kontakt\":{\"email\":\"kontakt@toptip.ch\",\"telefon\":\"056 417 90 20\"},\"moebelhausCode\":\"MH_TTIP_SPRE\",\"name\":\"Toptip\"},{\"adresse\":{\"ort\":\"Suhr\",\"plz\":5034,\"strasse\":\"Bernstrasse Ost 49\"},\"id\":672,\"kontakt\":{\"email\":\"moebel@pfister.ch\",\"telefon\":\"062 855 44 33\"},\"moebelhausCode\":\"MH_PFIS_SUHR\",\"name\":\"Möbel Pfister AG\"},{\"adresse\":{\"ort\":\"Schlieren\",\"plz\":8952,\"strasse\":\"Wagistrasse 20\"},\"id\":673,\"kontakt\":{\"email\":\"info@conforama.ch\",\"telefon\":\"044 738 28 88\"},\"moebelhausCode\":\"MH_CONF_SCHL\",\"name\":\"Conforama Wagi Shopping\"},{\"adresse\":{\"ort\":\"Pratteln\",\"plz\":4133,\"strasse\":\"Rütiweg 9\"},\"id\":674,\"kontakt\":{\"email\":\"kontakt@interio.ch\",\"telefon\":\"058 576 14 00\"},\"moebelhausCode\":\"MH_INTE_PRAT\",\"name\":\"Interio\"},{\"adresse\":{\"ort\":\"Pfäffikon SZ\",\"plz\":8808,\"strasse\":\"Schützenstrasse 2\"},\"id\":675,\"kontakt\":{\"email\":\"info@moebelmaerki.ch\",\"telefon\":\"055 410 22 22\"},\"moebelhausCode\":\"MH_MAER_PFSZ\",\"name\":\"möbel märki\"},{\"adresse\":{\"ort\":\"Egerkingen\",\"plz\":4622,\"strasse\":\"Lindenhagstrasse 3\"},\"id\":676,\"kontakt\":{\"email\":\"info@lipo.ch\",\"telefon\":\"062 288 60 10\"},\"moebelhausCode\":\"MH_LIPO_EGER\",\"name\":\"Lipo Einrichtungsmärkte\"},{\"adresse\":{\"ort\":\"Rothrist\",\"plz\":4852,\"strasse\":\"Rössliweg 43\"},\"id\":677,\"kontakt\":{\"email\":\"kontakt@moebelhubacher.ch\",\"telefon\":\"062 785 77 77\"},\"moebelhausCode\":\"MH_HUBA_RTHI\",\"name\":\"Möbel Hubacher AG\"},{\"adresse\":{\"ort\":\"Hinwil\",\"plz\":8340,\"strasse\":\"Wässeristrasse 28\"},\"id\":678,\"kontakt\":{\"email\":\"info@moebelferrari.ch\",\"telefon\":\"044 931 20 40\"},\"moebelhausCode\":\"MH_FERR_HINW\",\"name\":\"Möbel Ferrari AG\"},{\"adresse\":{\"ort\":\"Volketswil\",\"plz\":8604,\"strasse\":\"Brunnenstrasse 14\"},\"id\":679,\"kontakt\":{\"email\":\"info@moebelzentrumi.ch\",\"telefon\":\"044 908 42 42\"},\"moebelhausCode\":\"MH_MZMO_VOLK\",\"name\":\"MZ Möbelzentrum AG\"}]";
        JSONAssert.assertNotEquals(referenceJA, exampleJA, JSONCompareMode.STRICT); //Nach einer kleinen Anpassung dürfte das nicht mehr stimmen
    }

    @Test
    @DisplayName("Test: Kontrolliert ob ein valides JSON-String Objekt von eienr URL bezogen werden kann")
    public void readJsonFromUrl_Test() throws JSONException, IOException, URISyntaxException {
        String referenceJO = "{\"ablageTablar\":{\"bezeichnung\":\"R1-TC0\",\"id\":5},\"beschreibung\":\"Individuell zusammenstellbar, höhenverstellbar 70-117cm, Untergestell eckig, Metall anthrazit, B 160 T 60 H 75 cm\",\"id\":137,\"maximalerBestand\":50,\"minimalerBestand\":1,\"name\":\"Computer-Tisch\",\"preis\":1199.0,\"typCode\":\"PCTI-Steve-2011\"}";
        String exampleJOString = JSONGetter.readJsonFromUrl("http://10.177.1.94:8090/rmhr-test/ws/katalog/typ/code/PCTI-Steve-2011");
        JSONAssert.assertEquals(referenceJO, new JSONObject(exampleJOString), true);

        referenceJO = "{\"THESPANISHINQUISITION\":{\"bezeichnung\":\"R1-TC0\",\"id\":5},\"beschreibung\":\"Individuell zusammenstellbar, höhenverstellbar 70-117cm, Untergestell eckig, Metall anthrazit, B 160 T 60 H 75 cm\",\"id\":137,\"maximalerBestand\":50,\"minimalerBestand\":1,\"name\":\"Computer-Tisch\",\"preis\":1199.0,\"typCode\":\"PCTI-Steve-2011\"}";
        JSONAssert.assertNotEquals(referenceJO, exampleJOString, JSONCompareMode.STRICT); //Nach einer kleinen Anpassung dürfte das nicht mehr stimmen
    }
}
