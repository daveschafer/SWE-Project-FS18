/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.functions;

import java.util.Date;
import org.json.JSONArray;

/**
 * Dieses Interface stellt die Funktionalen Anforderungen gemäss
 * Aufgabenbeschrieb dar.
 *
 * @author Dave
 */
public interface FurnitureManufacturerInterface{

    /**
     * A01: Wieviel Möbelhäuser sind bei einem Möbelhersteller als Kunde
     * registriert? Liefert die Anzahl Möbelhäuser eines Möbelherstellers
     * zurück.
     *
     * @return JSONArray der Kunden
     */
    public int getMoebelhauser01();

    /**
     * A02: Wieviel verschiedene Produkt-Typen hat ein Möbelhersteller in seinem
     * Angebot?
     *
     * @return Anzahl Produkttypen
     */
    public int getProductTypes02();

    /**
     * A03: Wie gross ist der durchschnittliche Wert einer Bestellung pro
     * Möbelhaus bei einem Möbelhersteller?
     *
     * @return Durchschnittswert einer Bestellung pro Möbelhaus in einem
     * JSONArray in der Form [ {"Möblhaus : Name", "Bestellwert-Durchscnitt :
     * value"}, {...}]
     */
    public JSONArray getAverageOrderValuePerFurnitureShop03();

    /**
     * A04: Wie gross ist der Bestellungswert pro Möbelhersteller in einer
     * bestimmten Zeitperiode? Info: REST importAllOrders | Bestellungen
     *
     * @param Von Start der Messperiode
     * @param Bis Ende der Messperiode
     * @return Liefert ein JSONArray in der Form [ {"Möblhaus : Name",
     * "BestellwertP : value"}, {...}]
     */
    public JSONArray getOrderValueFromPeriod04(Date Von, Date Bis);

    /**
     * A05: Welche drei Möbelhäuser haben das grösste Bestellungsvolumen (Wert)
     * bei einem Möbelhersteller? (Optional: in einer vorgegebenen Zeitperiode)?
     * Info: Top 3. Bestellwert=Bestellvolumen | Bestellungen
     *
     * @param Von (optional) Start der Messperiode
     * @param Bis (optional) Ende der Messperiode
     * @return Gibt ein JSONArray zurück in der Form: { { "Moebelhaus" :
     * "moebelhauscode" , "Bestellvolumen" : "value" }, ...}
     */
    public JSONArray getTop3FurnitureShops05(Date Von, Date Bis);

    /**
     * A06: Wie lange ist die durchschnittliche Lieferzeit eines
     * Möbelherstellers. Info: importLieferungen / Lieferdatum - Bestelldatum =
     * Tage | Lieferungen
     *
     * @return Gibt die Zeitdauer in Tagen zurück, (Format #.### --- bis auf 3
     * Kommastellen).
     */
    public double getAverageDeliveryTime06();

    /**
     * A07: Welche fünf Produkte wurden bei einem Möbelhersteller am häufigsten
     * verkauft? (Optional: in einer vorgegebenen Zeitperiode)
     *
     * @param Von (optional) Start der Messperiode
     * @param Bis (optional) Ende der Messperiode
     * @return Gibt die Top 5 Produkte zurück in einem JSONArray der Form: {
     * "Produkt : prodcode", "Sales : Anz
     *
     * //Info: Aus importAllOrders --- Liste mit Productyp und Anzahl |
     * Bestellungen
     */
    public JSONArray getTop5Products07(Date Von, Date Bis);

    /**
     * A08: Anzahl Bestellungen pro Möbelhaus und Kalenderwoche des aktuellen
     * Kalenderjahres?1 Diese Anfrage sei Ressourceintensiv? Trotzem könnte man
     * einfach eine neue MongoCollection dafür machen
     *
     * @return Liefert die Anzahl Bestellungen pro Möbelhaus pro Kalenderwoche
     * (des aktuellen Jahres) in einem JSONArray der Form: [ {"Möbelhaus: name",
     * "Bestellungen KW01: Anzahl", "Bestellungen KW02: Anzahl"...}, {"Möbelhaus
     * :..."}]
     */
    public JSONArray getOrdersForAllWeeks08();

    /**
     * A09: Bestellungsvolumen pro Möbelhaus und Kalenderwoche des aktuellen
     * Kalenderjahres?
     *
     * @return Liefert das Bestellungsvolumen pro Möbelhaus pro Kalenderwoche
     * (des aktuellen Jahres in einem JSONArray der Form: [ {"Möbelhaus: name",
     * "Bestellungen KW01: Anzahl", "Bestellungen KW02: Anzahl"...}, {"Möbelhaus
     * :..."}]
     */
    public JSONArray getOrderVolumeForAllWeeks09();
}
