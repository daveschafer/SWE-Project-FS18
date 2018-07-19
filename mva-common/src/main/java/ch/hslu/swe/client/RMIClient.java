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
package ch.hslu.swe.client;

import static ch.hslu.swe.client.Client.isDateValidClient;
import ch.hslu.swe.helper.MoebelherstellerEnum;
import ch.hslu.swe.server.RMIInterface;
import org.apache.logging.log4j.LogManager;
import org.json.JSONArray;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import org.json.JSONException;

public class RMIClient {

    private String url;
    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(RMIClient.class);
    private static DateFormat entryDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    RMIClient(final String rmiServerIP, final int rmiPort) {
        // policy-Datei angeben und SecurityManager installieren
        /* System.setProperty("java.security.policy", "checker.policy");

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
         */

        // URL definieren und die Referenz auf das entfernte Objekt holen (Stub)
        LOG.debug("Verbindung zu " + rmiServerIP + " wird aufgebaut...");
        url = "rmi://" + rmiServerIP + ":" + rmiPort + "/" + RMIInterface.RO_NAME;
    }

    public void executeFunction(int functionNumber, MoebelherstellerEnum moebelherstellerEnum) throws RemoteException, MalformedURLException, NotBoundException, ParseException {
        boolean isValidNumber = _functions_Validator.between(functionNumber, 1, 9);
        if (!isValidNumber) {
            LOG.error("Keine gültige Funktion [1:9]");
            return;
        } else {
            switch (functionNumber) {
                case 1: {
                    this.a01(moebelherstellerEnum);
                    break;
                }
                case 2: {
                    this.a02(moebelherstellerEnum);
                    break;
                }
                case 3: {
                    this.a03(moebelherstellerEnum);
                    break;
                }
                case 4: {
                    LOG.debug("Bitte definieren Sie den Zeitraum");
                    Date startDatum = getStartDate();
                    Date endDatum = getStartDate();
                    if (startDatum == null || endDatum == null) {
                        this.a04(moebelherstellerEnum); //default werte (alles)
                    }
                    this.a04(moebelherstellerEnum, startDatum, endDatum);
                    break;
                }

                case 5: {
                    LOG.debug("Bitte definieren Sie den Zeitraum");
                    Date startDatum = getStartDate();
                    Date endDatum = getStartDate();
                    if (startDatum == null || endDatum == null) {
                        this.a05(moebelherstellerEnum); //default werte (alles)
                    }
                    this.a05(moebelherstellerEnum, startDatum, endDatum);
                    break;
                }
                case 6: {
                    this.a06(moebelherstellerEnum);
                    break;
                }
                case 7: {
                    LOG.debug("Bitte definieren Sie den Zeitraum");
                    Date startDatum = getStartDate();
                    Date endDatum = getStartDate();
                    if (startDatum == null || endDatum == null) {
                        this.a07(moebelherstellerEnum); //default werte (alles)
                    }
                    this.a07(moebelherstellerEnum, startDatum, endDatum);
                    break;
                }
                case 8: {
                    this.a08(moebelherstellerEnum);
                    break;
                }
                case 9: {
                    this.a09(moebelherstellerEnum);
                    break;
                }
                default: {
                    LOG.warn("Keine Funktion angegeben A01 wird ausgeführt");
                    this.a01(moebelherstellerEnum);
                    break;
                }

            }
        }
    }

    public int a01(final MoebelherstellerEnum moebelherstellerEnum) throws RemoteException, MalformedURLException, NotBoundException {
        RMIInterface stub = (RMIInterface) Naming.lookup(url);
        int moebelhaeuserCounter = stub.getMoebelhaeuser01(moebelherstellerEnum);
        LOG.info("Der Möbelhersteller " + moebelherstellerEnum.toString() + " beliefert " + moebelhaeuserCounter + " Möbelhäuser");
        return moebelhaeuserCounter;

    }

    public int a02(final MoebelherstellerEnum moebelherstellerEnum) throws RemoteException, NotBoundException, MalformedURLException {
        RMIInterface stub = (RMIInterface) Naming.lookup(url);
        int productTypesCounter = stub.getProductTypes02(moebelherstellerEnum);
        LOG.info("Der Möbelhersteller " + moebelherstellerEnum.toString() + " bietet " + productTypesCounter + " verschiedene Produkte an.");
        return productTypesCounter;
    }

    public void a03(final MoebelherstellerEnum moebelherstellerEnum) throws RemoteException, NotBoundException, MalformedURLException {
        RMIInterface stub = (RMIInterface) Naming.lookup(url);

        JSONArray averageOrderValues = new JSONArray(stub.getAverageOrderValuePerFurnitureShop03(moebelherstellerEnum));

        for (int i = 0; i < averageOrderValues.length(); i++) {
            org.json.JSONObject jsonObject = averageOrderValues.getJSONObject(i);
            String moebelhausCode = jsonObject.getString("moebelhausCode");
            Double durchschnittBestellwert = jsonObject.getDouble("durchschnittBestellwert");
            LOG.debug("Moebelhaus: " + moebelhausCode + " | " + "Durchschnitt-BW: " + durchschnittBestellwert);
        }

    }

    /**
     * Ohne Zeitangaben = Alle Bestellungen
     *
     * @param moebelherstellerEnum
     * @throws RemoteException
     * @throws MalformedURLException
     * @throws NotBoundException
     */
    public void a04(final MoebelherstellerEnum moebelherstellerEnum) throws RemoteException, MalformedURLException, NotBoundException {
        RMIInterface stub = (RMIInterface) Naming.lookup(url);
        JSONArray orderValueoverTime = new JSONArray(stub.getOrderValueFromPeriod04(moebelherstellerEnum));

        LOG.info("***Ausgabe Bestellwert pro Möbelshop über Zeitraum (Total)***");

        for (int i = 0; i < orderValueoverTime.length(); i++) {
            org.json.JSONObject jsonObject = orderValueoverTime.getJSONObject(i);
            String moebelhausCode = jsonObject.getString("moebelhausCode");
            Double BestellwertTotal = jsonObject.getDouble("BestellwertTotal");
            LOG.debug("Moebelhaus: " + moebelhausCode + " | " + "Bestellwert Total: " + BestellwertTotal);
        }
    }

    /**
     * Mit Zeitangabe
     *
     * @param moebelherstellerEnum
     * @param Von
     * @param Bis
     * @throws RemoteException
     * @throws MalformedURLException
     * @throws NotBoundException
     */
    public void a04(final MoebelherstellerEnum moebelherstellerEnum, final Date Von, final Date Bis) throws RemoteException, MalformedURLException, NotBoundException {
        RMIInterface stub = (RMIInterface) Naming.lookup(url);
        JSONArray orderValueoverTime = new JSONArray(stub.getOrderValueFromPeriod04(moebelherstellerEnum, Von, Bis));

        LOG.info("***Ausgabe Bestellwert pro Möbelshop über Zeitraum ('" + Von + "' bis '" + Bis + "')***");

        for (int i = 0; i < orderValueoverTime.length(); i++) {
            org.json.JSONObject jsonObject = orderValueoverTime.getJSONObject(i);
            String moebelhausCode = jsonObject.getString("moebelhausCode");
            Double BestellwertTotal = jsonObject.getDouble("BestellwertTotal");
            LOG.info("Moebelhaus: " + moebelhausCode + " | " + "Bestellwert Total: " + BestellwertTotal);
        }

    }

    public void a05(final MoebelherstellerEnum moebelherstellerEnum, final Date Von, final Date Bis) throws NotBoundException, MalformedURLException, RemoteException {
        RMIInterface stub = (RMIInterface) Naming.lookup(url);
        JSONArray top3shops = new JSONArray(stub.getTop3FurnitureShops05(moebelherstellerEnum, Von, Bis));

        LOG.info("***Ausgabe der Top 3 Möbelshops***");
        for (int i = 0; i < top3shops.length(); i++) {
            org.json.JSONObject jsonObject = top3shops.getJSONObject(i);
            String moebelhausCode = jsonObject.getString("moebelhausCode");
            LOG.info("Platz " + (i + 1) + ": Moebelhaus: " + moebelhausCode);
        }
    }

    public void a05(final MoebelherstellerEnum moebelherstellerEnum) throws NotBoundException, MalformedURLException, RemoteException {
        RMIInterface stub = (RMIInterface) Naming.lookup(url);
        JSONArray top3shops = new JSONArray(stub.getTop3FurnitureShops05(moebelherstellerEnum));
        LOG.info("***Ausgabe der Top 3 Möbelshops***");

        for (int i = 0; i < top3shops.length(); i++) {
            org.json.JSONObject jsonObject = top3shops.getJSONObject(i);
            String moebelhausCode = jsonObject.getString("moebelhausCode");
            LOG.info("Platz " + (i + 1) + ": Moebelhaus: " + moebelhausCode);
        }
    }

    public void a06(final MoebelherstellerEnum moebelherstellerEnum) throws RemoteException, NotBoundException, MalformedURLException {
        RMIInterface stub = (RMIInterface) Naming.lookup(url);
        double averageDeliveryTime = (stub.getAverageDeliveryTime06(moebelherstellerEnum));

        LOG.info("Durchschnittliche Lieferzeit des Möbelherstellers '" + moebelherstellerEnum.name() + "' beträgt: " + averageDeliveryTime);
    }

    public void a07(final MoebelherstellerEnum moebelherstellerEnum, final Date Von, final Date Bis) throws RemoteException, NotBoundException, MalformedURLException {
        RMIInterface stub = (RMIInterface) Naming.lookup(url);
        JSONArray top5products = new JSONArray(stub.getTop5Products07(moebelherstellerEnum, Von, Bis));

        LOG.info("***Ausgabe der Top 5 Produkte***");
        for (int i = 0; i < top5products.length(); i++) {
            org.json.JSONObject jsonObject = top5products.getJSONObject(i);
            int produktID = jsonObject.getInt("produktID");
            int anzahl = jsonObject.getInt("anzahl");
            //String produktInfos = jsonObject.getString("produktInfos");
            LOG.info("Platz: '" + (i + 1) + "' | produktID: '" + produktID + "' | Anzahl: '" + anzahl + "'");
        }
    }

    public void a07(final MoebelherstellerEnum moebelherstellerEnum) throws RemoteException, NotBoundException, MalformedURLException {
        RMIInterface stub = (RMIInterface) Naming.lookup(url);
        JSONArray top5products = new JSONArray(stub.getTop5Products07(moebelherstellerEnum));

        LOG.info("***Ausgabe der Top 5 Produkte***");
        for (int i = 0; i < top5products.length(); i++) {
            org.json.JSONObject jsonObject = top5products.getJSONObject(i);
            int produktID = jsonObject.getInt("produktID");
            int anzahl = jsonObject.getInt("anzahl");
            //   String produktInfos = jsonObject.getString("produktInfos");
            LOG.info("Platz: '" + (i + 1) + "' | produktID: '" + produktID + "' | Anzahl: '" + anzahl + "'");
        }
    }

    public void a08(final MoebelherstellerEnum moebelherstellerEnum) throws RemoteException, NotBoundException, MalformedURLException {
        RMIInterface stub = (RMIInterface) Naming.lookup(url);
        JSONArray ordersAllWeeks = new JSONArray(stub.getOrdersForAllWeeks08(moebelherstellerEnum));

        LOG.info("***Ausgabe Bestellungen über alle KWs des aktuellen jahres***");
        for (int i = 0; i < ordersAllWeeks.length(); i++) {
            org.json.JSONObject jsonObject = ordersAllWeeks.getJSONObject(i);
            String moebelhausCode = jsonObject.getString("moebelhausCode");
            JSONArray jar_Bestellungen08 = jsonObject.getJSONArray("Bestellungen");
            for (int j = 0; j < jar_Bestellungen08.length(); j++) {
                org.json.JSONObject job_kw_08 = jar_Bestellungen08.getJSONObject(j); // get KW1
                int KW = job_kw_08.getInt("KW");
                int anzahlBestellungKW = job_kw_08.getInt("anzahlBestellungen");

                LOG.info("Möbelhaus: '" + moebelhausCode + "' | KW: '" + KW + "' | AnzahlBestellungen: '" + anzahlBestellungKW + "'");
            }
        }
    }

    public void a09(final MoebelherstellerEnum moebelherstellerEnum) throws RemoteException, NotBoundException, MalformedURLException {
        RMIInterface stub = (RMIInterface) Naming.lookup(url);
        JSONArray ordersVolumeAllweeks = new JSONArray(stub.getOrderVolumeForAllWeeks09(moebelherstellerEnum));

        //LOG.info("***Ausgabe BestellVOLUMEN über alle KWs des aktuellen jahres***");

        for (int i = 0; i < ordersVolumeAllweeks.length(); i++) {
            org.json.JSONObject jsonObject = ordersVolumeAllweeks.getJSONObject(i);
            LOG.debug("JSONOB: "+jsonObject.toString());
            String moebelhausCode = jsonObject.getString("moebelhausCode");
            JSONArray jar_Bestellungen09 = jsonObject.getJSONArray("Bestellungen");
            double Bestellvolumen = 0;
            int KW =0;
            for (int j = 0; j < jar_Bestellungen09.length(); j++) {
                org.json.JSONObject job_kw_09 = jar_Bestellungen09.getJSONObject(j); // get KW1
                try {
                    KW = job_kw_09.getInt("KW");
                    Bestellvolumen = job_kw_09.getDouble("Bestellvolumen");
                } catch (JSONException e) {
                    LOG.warn("JSON Wert nicht gefunden..." + e.getMessage());
                }
                LOG.info("Möbelhaus: '" + moebelhausCode + "' | KW: '" + KW + "' | BestellVOLUMEN: '" + Bestellvolumen + "'");
            }
        }
    }

    //***Helpers//
    public Date getStartDate() throws ParseException {
        boolean dateCorrect;
        do {
            LOG.debug("Geben Sie ein Startdatum ein (yyyy-MM-dd)");
            String von = new Scanner(System.in, "UTF-8").next();
            dateCorrect = isDateValidClient(von);
            if (dateCorrect) {
                return entryDateFormat.parse(von);
            }
        } while (!dateCorrect);
        LOG.debug("getStartDate beendet");
        return null; //nicht schön aber wird weiter oben abgefangen
    }

    public Date getEndDate() throws ParseException {
        boolean dateCorrect;
        do {
            LOG.debug("Geben Sie ein EndDatum ein (yyyy-MM-dd)");
            String bis = new Scanner(System.in, "UTF-8").next();
            dateCorrect = isDateValidClient(bis);
            if (dateCorrect) {
                return entryDateFormat.parse(bis);
            }
        } while (!dateCorrect);
        LOG.debug("getEndDate beendet");
        return null; //nicht schön aber wird weiter oben abgefangen
    }

    static boolean hostAvailabilityCheck(final String ip, final int port) {
        try (Socket ignored = new Socket(ip, port)) {
            return true;
        } catch (IOException ex) {
            LOG.debug("Server-Socket unter den vorgegebenen Parametern " + ip + ":" + port + " nicht ereichbar");
        }
        return false;
    }

    public String getUrl() {
        return url;
    }

}
