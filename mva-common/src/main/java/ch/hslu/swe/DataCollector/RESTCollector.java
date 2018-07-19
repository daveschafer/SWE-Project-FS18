/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.DataCollector;

/**
 *
 * @author Dave
 */
public interface RESTCollector {

    /**
     * Importiert alle Orders (Bestellungen) vom REST Server in die Datenbank.
     *
     * @return Gibt true zurück falls der import erfolgreich war.
     */
    public boolean importAllOrders();

    /**
     * Importiert alle Deliveries (Lieferungen) vom REST Server in die
     * Datenbank.
     *
     * @return Gibt true zurück falls der import erfolgreich war.
     */
    public boolean importAllDeliveries();

    /**
     * Importiert alle Möbelhäuser vom REST Server in die Datenbank.
     *
     * @return Gibt true zurück falls der import erfolgreich war.
     */
    public boolean importAllMoebelhauser();

    /**
     * Importiert alle Produkte welche der Möbelhersteller anbietet vom REST
     * Server in die Datenbank.
     *
     * @return Gibt true zurück falls der import erfolgreich war.
     *
     */
    public boolean importAllProducts();
    
    
   
}
