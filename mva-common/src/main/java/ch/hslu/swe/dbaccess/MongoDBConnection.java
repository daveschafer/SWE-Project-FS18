/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.dbaccess;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * Klasse zum definieren welche MongoDB Datenbank verwendet werden soll.
 *
 * @author Dave
 */
public class MongoDBConnection {

    private static final Logger LOG = LogManager.getLogger(MongoDBConnection.class);

    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static ServerAddress mongoSRVaddr;
    private static String DBName = "YourDefaultDB";

    /**
     * Stellt eine ungesicherte Verbindung zur MongoDB her.
     *
     * @param hostname Serveradresse des MongoDB Servers
     * @param hostport Port des MongoDB Servers
     */
    public static void setMongoDBDatabase(String hostname, int hostport) {
        LOG.debug("MongoDB Unsecure Connection");
        mongoSRVaddr = new ServerAddress(hostname, hostport);
        mongoClient = new MongoClient(mongoSRVaddr, MongoClientOptions.builder().serverSelectionTimeout(10000).build()); //10sek timeout
        database = mongoClient.getDatabase(DBName);

        if (testConnection()) {
            LOG.debug("DB Verbindung hergestellt zu: 'Server: " + hostname + "' DB: '" + DBName + "'");
        }
    }

    /**
     * Stellt eine ungesicherte Verbindung zur definierten MongoDB her.
     *
     * @param hostname Serveradresse des MongoDB Servers
     * @param hostport Port des MongoDB Servers
     * @param DBName Name der Datenbank (Default: MVA_04)
     */
    public static void setMongoDBDatabase(String hostname, int hostport, String DBName) {
        if (DBName.isEmpty()) {
            DBName = "MVA_04";
        }
        MongoDBConnection.DBName = DBName;
        setMongoDBDatabase(hostname, hostport);
    }

    /**
     * Stellt eine gesicherte Verbindung zur MongoDB her.
     *
     * @param addr Serveradresse des MongoDB Servers
     * @param credential Zugangsdaten
     * @param options Defaultoptionen
     */
    public static void setMongoDBDatabase(ServerAddress addr, MongoCredential credential, MongoClientOptions options) {
        LOG.debug("MongoDB Secure Connection");
        mongoClient = new MongoClient(addr, credential, options);
        database = mongoClient.getDatabase(DBName);
        if (testConnection()) {
            LOG.debug("DB Verbindung hergestellt zu: 'Server: " + addr.toString() + "' DB: '" + DBName + "'");
        }
    }

    /**
     * Liefert die aktuelle Default mongoDB zurück.
     *
     * @return MongoDatabase (defualt)
     */
    public static MongoDatabase getDatabase() {
        if (database != null) {
            return database;
        } else {
            throw new MongoClientException("Datenbank nicht initialisiert");
        }
    }

    //
//Helpers
    //
    private static boolean testConnection() {
        try {
            mongoClient.getAddress();
        } catch (Exception exp) {
            mongoClient.close();
            LOG.error("Keine Verbindung zum MongoDBServer");
            throw new MongoClientException("Keine Verbindung zum MongoDBServer!");
        }
        return true;
    }

    public static boolean checkIfCollectionExists(final String collectionName) {
        return database.listCollectionNames()
                .into(new ArrayList<>()).contains(collectionName);
    }

}
