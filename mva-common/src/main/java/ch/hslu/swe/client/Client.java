/*
 *
 *  * Copyright (c) 2018. Inäbnit Pascal, David Schafer.
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

import ch.hslu.swe.helper.MoebelherstellerEnum;
import org.apache.logging.log4j.LogManager;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

@Deprecated
public class Client {

    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(Client.class);
    private static DateFormat entryDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static Date startDatum = null;
    private static Date endDatum = null;

    public static void main(final String[] args) throws ParseException {

        String ip;
        int port;
        int function;
        int moebelhersteller;

        MoebelherstellerEnum moebelherstellerEnum;

        LOG.debug("Bitte geben Sie die Server-IP ein");
        ip = new Scanner(System.in, "UTF-8").next();
        LOG.debug("Es wurde die IP-Adresse " + ip + " eingegeben");

        LOG.debug("Bitte geben Sie den Port ein");
        port = new Scanner(System.in, "UTF-8").nextInt();
        LOG.debug("Es wurde der Port " + port + " eingegeben");

        //make an endless client (unschön aber pragmatisch hehe)
        while (true) {
            LOG.info("Wählen Sie den Möbelhersteller [Fischer = 1, Walker = 2, Zwissig = 3, Test = 4]");
            moebelhersteller = new Scanner(System.in, "UTF-8").nextInt();

            switch (moebelhersteller) {
                case 1: {
                    moebelherstellerEnum = MoebelherstellerEnum.FISCHER;
                    break;
                }
                case 2: {
                    moebelherstellerEnum = MoebelherstellerEnum.WALKER;
                    break;
                }
                case 3: {
                    moebelherstellerEnum = MoebelherstellerEnum.ZWISSIG;
                    break;
                }
                case 4: {
                    moebelherstellerEnum = MoebelherstellerEnum.TEST;
                    break;
                }
                default:
                    moebelherstellerEnum = MoebelherstellerEnum.TEST;
                    break;
            }

            LOG.info("Bitte geben Sie die gewünschte Funktion ein (1-9)");
            function = new Scanner(System.in, "UTF-8").nextInt();
            LOG.debug("Es wurde die Funktion " + function + " gewählt");

            LOG.debug("Prüfe Erreichbarkeit des Servers...");
            boolean serverIsAvailable = RMIClient.hostAvailabilityCheck(ip, port);

            if (serverIsAvailable) {

                LOG.debug("Verbindungsaufbau zu Server..");

                try {
                    RMIClient client = new RMIClient(ip, port);
                    switch (function) {
                        case 1: {
                            client.a01(moebelherstellerEnum);
                            break;
                        }
                        case 2: {
                            client.a02(moebelherstellerEnum);
                            break;
                        }
                        case 3: {
                            client.a03(moebelherstellerEnum);
                            break;
                        }
                        case 4: {
                            LOG.debug("Bitte definieren Sie den Zeitraum");
                            Client.getDate();
                            client.a04(moebelherstellerEnum, startDatum, endDatum);
                            break;
                        }
                        default: {
                            client.a01(moebelherstellerEnum);
                            break;
                        }
                    }

                } catch (RemoteException | NotBoundException | MalformedURLException e) {
                    e.printStackTrace();
                }

            } else {
                LOG.error("Verbindung kann nicht aufgebaut werden,");
                LOG.error("Server " + ip + ":" + port + " nicht erreichbar");
            }
            LOG.debug("**************************************");
        }
    } //end of main

    static boolean isDateValidClient(final String date) {
        try {
            entryDateFormat.parse(date);
            return true;
        } catch (Exception e) {
            LOG.error("Falsches Date Format, bitte ein korrektes Format eingeben");
            return false;
        }
    }

    public static void getDate() throws ParseException {
        boolean dateCorrect;
        do {
            LOG.debug("Geben Sie ein Startdatum ein (yyyy-MM-dd)");
            String von = new Scanner(System.in, "UTF-8").next();
            dateCorrect = isDateValidClient(von);
            if (dateCorrect) {
                startDatum = entryDateFormat.parse(von);
            }
        } while (!dateCorrect);

        do {
            LOG.debug("Geben Sie ein EndDatum ein (yyyy-MM-dd)");
            String bis = new Scanner(System.in, "UTF-8").next();
            dateCorrect = isDateValidClient(bis);
            if (dateCorrect) {
                endDatum = entryDateFormat.parse(bis);
            }
        } while (!dateCorrect);

        LOG.debug("getDate beendet");
    }
}
