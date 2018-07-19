/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.server;

import ch.hslu.swe.dbaccess.MongoDBConnection;
import ch.hslu.swe.functions.FurnitureManufacturer;
import ch.hslu.swe.helper.MoebelherstellerEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

/**
 * Stellt die RMI Schnitstelle für den Client zur Verfügung. Diese Klasse
 * beinhaltet noch ein paar Hacks Um das Ganze unter Docker lauffähig zu machen.
 * Die Hacks sind in der Doku spezifiziert.
 *
 * @author Ini
 */
public class RMIServerDocker extends UnicastRemoteObject implements RMIInterface {

    private static final Logger LOG = LogManager.getLogger(RMIServerDocker.class);

    private static String rmiIP;
    private static final int rmiPort = 1099;

    private static final String mongoHostname = "your-mongo-server-hostname";
    private static final int mongoPort = 27017;

    //fix for rmi und docker ghetto
    static Registry reg;

    private RMIServerDocker() throws RemoteException, UnknownHostException {
        super(Registry.REGISTRY_PORT); //docker fix 3

        RMIServerDocker.rmiIP = "10.177.1.181"; // fixe docker Adresse hinterlegen
        LOG.debug("Server IP-Adresse: " + rmiIP);
    }

    public static void main(String[] args) throws UnknownHostException {
        System.setProperty("java.rmi.server.hostname", "10.177.1.181"); //docker fix 2
        try {
            reg = LocateRegistry.createRegistry(Registry.REGISTRY_PORT); //docker fix 1

            //Init DB Connection
            //Hostname, Port und Datenbankname einer MongoDB Instanz hier spezifizieren       
            //MongoDBConnection.setMongoDBDatabase("your-mongo-server-hostname", 27017, "yourmongoDBName");
            MongoDBConnection.setMongoDBDatabase(mongoHostname, mongoPort);

            //Init Interface Objekt
            RMIServerDocker rmiserverdocker = new RMIServerDocker();
            /*System.setProperty("java.security.policy", "checker.policy");
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
             */

            //Rebind
            reg.rebind(RMIInterface.RO_NAME, rmiserverdocker);

            // Ausführung anhalten (eine Variante)
            LOG.debug("Server gestartet, beenden mit ENTER-Taste!");
            new java.util.Scanner(System.in, "UTF-8").nextLine();

            // Unbind entferntes Objekt
            reg.unbind(RMIServerDocker.RO_NAME);
            System.exit(0);

        } catch (RemoteException | NotBoundException ex) {
            LOG.error(ex);
        }

    }

    @Override
    public int getMoebelhaeuser01(MoebelherstellerEnum moebelherstellerEnum) {
        FurnitureManufacturer manufacturer = new FurnitureManufacturer(moebelherstellerEnum);
        return manufacturer.getMoebelhauser01();
    }

    @Override
    public int getProductTypes02(MoebelherstellerEnum moebelherstellerEnum) {
        FurnitureManufacturer producttypes = new FurnitureManufacturer(moebelherstellerEnum);
        return producttypes.getProductTypes02();
    }

    @Override
    public String getAverageOrderValuePerFurnitureShop03(MoebelherstellerEnum moebelherstellerEnum) {
        FurnitureManufacturer producttypes = new FurnitureManufacturer(moebelherstellerEnum);
        return producttypes.getAverageOrderValuePerFurnitureShop03().toString();
    }

    @Override
    public String getOrderValueFromPeriod04(MoebelherstellerEnum moebelherstellerEnum) {
        FurnitureManufacturer producttypes = new FurnitureManufacturer(moebelherstellerEnum);
        return producttypes.getOrderValueFromPeriod04().toString();

    }

    @Override
    public String getOrderValueFromPeriod04(MoebelherstellerEnum moebelherstellerEnum, Date Von, Date Bis) {
        FurnitureManufacturer producttypes = new FurnitureManufacturer(moebelherstellerEnum);
        return producttypes.getOrderValueFromPeriod04(Von, Bis).toString();
    }

    @Override
    public String getTop3FurnitureShops05(MoebelherstellerEnum moebelherstellerEnum, Date Von, Date Bis) throws RemoteException {
        FurnitureManufacturer top3shops = new FurnitureManufacturer(moebelherstellerEnum);
        return top3shops.getTop3FurnitureShops05(Von, Bis).toString();
    }

    @Override
    public String getTop3FurnitureShops05(MoebelherstellerEnum moebelherstellerEnum) throws RemoteException {
        FurnitureManufacturer top3shops = new FurnitureManufacturer(moebelherstellerEnum);
        return top3shops.getTop3FurnitureShops05().toString();
    }

    @Override
    public double getAverageDeliveryTime06(MoebelherstellerEnum moebelherstellerEnum) throws RemoteException {
        FurnitureManufacturer averageDelivery = new FurnitureManufacturer(moebelherstellerEnum);
        return averageDelivery.getAverageDeliveryTime06();
    }

    @Override
    public String getTop5Products07(MoebelherstellerEnum moebelherstellerEnum, Date Von, Date Bis) throws RemoteException {
        FurnitureManufacturer top5prods = new FurnitureManufacturer(moebelherstellerEnum);
        return top5prods.getTop5Products07(Von, Bis).toString();
    }

    @Override
    public String getTop5Products07(MoebelherstellerEnum moebelherstellerEnum) throws RemoteException {
        FurnitureManufacturer top5prods = new FurnitureManufacturer(moebelherstellerEnum);
        return top5prods.getTop5Products07().toString();
    }

    @Override
    public String getOrdersForAllWeeks08(MoebelherstellerEnum moebelherstellerEnum) throws RemoteException {
        FurnitureManufacturer ordersallweeks = new FurnitureManufacturer(moebelherstellerEnum);
        return ordersallweeks.getOrdersForAllWeeks08().toString();
    }

    @Override
    public String getOrderVolumeForAllWeeks09(MoebelherstellerEnum moebelherstellerEnum) throws RemoteException {
        FurnitureManufacturer volumeallweeks = new FurnitureManufacturer(moebelherstellerEnum);
        return volumeallweeks.getOrderVolumeForAllWeeks09().toString();
    }

    private static boolean isPortAvailable(int port) {
        try (Socket ignored = new Socket("localhost", port)) {
            return false;
        } catch (IOException ignored) {
            return true;
        }
    }

}
