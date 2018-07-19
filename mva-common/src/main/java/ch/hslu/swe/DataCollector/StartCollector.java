/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.DataCollector;

import ch.hslu.swe.dbaccess.MongoDBConnection;
import ch.hslu.swe.helper.MoebelherstellerEnum;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;

/**
 * Entry Point of the RESTCollector to Mongo Service. How to Start: sudo mvn
 * clean compile exec:java -Dexec.mainClass=bot.main.MainMethode Importiert alle
 * 4 Möbelhäuser in Zeitabständen von 5 Minuten jeweils alle 20min.
 *
 * @author Dave
 */
public class StartCollector {

    private static Logger LOG = LogManager.getLogger(StartCollector.class);

    //Entry Point RESTCollector
    public static void main(final String[] args) throws JSONException {
        //DB Session muss initialisiert werden (manuell)
        //Hostname, Port und Datenbankname einer MongoDB Instanz hier spezifizieren
        MongoDBConnection.setMongoDBDatabase("your-mongo-server-hostname", 27017, "yourmongoDBName"); //ProduktivInstanz

        Calendar startTime_Calendar = Calendar.getInstance();//jetzige Zeit bekommen mittels Calendar hack
        long startTimeInMillis = startTime_Calendar.getTimeInMillis();
        Date startTime = new Date(startTimeInMillis);

        LOG.info("RESTCollector Service started at " + startTime);

        //Java TimerThread erstellen welcher den Collector startet und danach alle 15min pullen lässt
        TimerTask timerTaskFISCHER = new RESTCollectorMongo(MoebelherstellerEnum.FISCHER);
        TimerTask timerTaskTEST = new RESTCollectorMongo(MoebelherstellerEnum.TEST);
        TimerTask timerTaskWALKER = new RESTCollectorMongo(MoebelherstellerEnum.WALKER);
        TimerTask timerTaskZWISSIG = new RESTCollectorMongo(MoebelherstellerEnum.ZWISSIG);

        //running timer task as daemon thread
        Timer timerTEST = new Timer(true);
        timerTEST.schedule(timerTaskTEST, startTime, 1200000); //starttime = jetzt | delay 20min = 1200000ms
        LOG.info("Timertask MH:TEST gestartet");

        //300000ms = 5min
        Timer timerFISCHER = new Timer(true);
        timerFISCHER.schedule(timerTaskFISCHER, new Date(startTimeInMillis + (1 * 300000)), 1200000);
        LOG.info("Timertask MH:FISCHER gestartet");

        Timer timerWALKER = new Timer(true);
        timerWALKER.schedule(timerTaskWALKER, new Date(startTimeInMillis + (2 * 300000)), 1200000); // 2 * = 10 min
        LOG.info("Timertask MH:WALKER gestartet");

        Timer timerZWISSIG = new Timer(true);
        timerZWISSIG.schedule(timerTaskZWISSIG, new Date(startTimeInMillis + (3 * 300000)), 1200000); // 3 * = 15 min
        LOG.info("Timertask MH:ZWISSIG gestartet");
        
        //while true loop um den prozess für immer laufen zu lassen (nicht elegant aber pragmatisch)
        
        while(true){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(StartCollector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
