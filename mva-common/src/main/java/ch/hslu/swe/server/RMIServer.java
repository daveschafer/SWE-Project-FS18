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
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

/**
 * Stellt die RMI Schnitstelle für den Client zur Verfügung.
 *
 * @author Ini
 */
public class RMIServer extends UnicastRemoteObject implements RMIInterface {

    private static final Logger LOG = LogManager.getLogger(RMIServer.class);

    private static String rmiIP;
    private static int rmiPort = 1099;

    private static String mongoHostname = "swef18-tbschafe.el.eee.intern";
    private static int mongoPort = 27017;

    private RMIServer() throws RemoteException, UnknownHostException {
        RMIServer.rmiIP = InetAddress.getLocalHost().getHostAddress();
        LOG.debug("Server IP-Adresse: " + rmiIP);
    }

    private RMIServer(int rmiPort) throws RemoteException, UnknownHostException {
        RMIServer.rmiIP = InetAddress.getLocalHost().getHostAddress();
        LOG.debug("Server IP-Adresse: " + rmiIP);
        if (isPortAvailable(rmiPort)) {
            RMIServer.rmiPort = rmiPort;
        } else {
            LOG.error("Server kann nicht gestartet werden, Port ist bereits in Verwendung!");
        }
    }

    private RMIServer(String rmiServerIP, int rmiPort) throws RemoteException, UnknownHostException {
        RMIServer.rmiIP = InetAddress.getLocalHost().getHostAddress();
        LOG.debug("Server IP-Adresse: " + rmiIP);
        if (isPortAvailable(rmiPort)) {
            RMIServer.rmiPort = rmiPort;
        } else {
            LOG.error("Server kann nicht gestartet werden, Port ist bereits in Verwendung!");
        }
    }

    public static void main(String[] args) throws UnknownHostException {

        try {
            //Init DB Connection
            
            //Hostname, Port und Datenbankname einer MongoDB Instanz hier spezifizieren       
            // MongoDBConnection.setMongoDBDatabase("your-mongo-server-hostname", 27017, "yourmongoDBName"); 
            MongoDBConnection.setMongoDBDatabase(mongoHostname, mongoPort);

            //Create Remote Object
            RMIInterface rmiServerInterfaceRO = new RMIServer();

            // Create and Start Name Registry
            Registry reg = LocateRegistry.createRegistry(rmiPort);

            // Install Security Manager with Security Policy
            /*System.setProperty("java.security.policy", "checker.policy");

            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
             */
            // Entferntes Objekt beim Namensdienst registrieren (binding)
            reg.rebind(RMIInterface.RO_NAME, rmiServerInterfaceRO);

            // Ausführung anhalten (eine Variante)
            LOG.debug("Server gestartet, beenden mit ENTER-Taste!");
            new java.util.Scanner(System.in, "UTF-8").nextLine();

            // Unbind entferntes Objekt
            reg.unbind(RMIServer.RO_NAME);
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
        FurnitureManufacturer averageorder = new FurnitureManufacturer(moebelherstellerEnum);
        return averageorder.getAverageOrderValuePerFurnitureShop03().toString();
    }

    @Override
    public String getOrderValueFromPeriod04(MoebelherstellerEnum moebelherstellerEnum) {
        FurnitureManufacturer ordervalue = new FurnitureManufacturer(moebelherstellerEnum);
        return ordervalue.getOrderValueFromPeriod04().toString();

    }

    @Override
    public String getOrderValueFromPeriod04(MoebelherstellerEnum moebelherstellerEnum, Date Von, Date Bis) {
        FurnitureManufacturer ordervalue = new FurnitureManufacturer(moebelherstellerEnum);
        return ordervalue.getOrderValueFromPeriod04(Von, Bis).toString();
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

    //Helpers
    private static boolean isPortAvailable(int port) {
        try (Socket ignored = new Socket("localhost", port)) {
            return false;
        } catch (IOException ignored) {
            return true;
        }
    }

}
