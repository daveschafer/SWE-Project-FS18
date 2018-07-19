/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.datastructures;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ablageTablar",
    "beschreibung",
    "id",
    "maximalerBestand",
    "minimalerBestand",
    "name",
    "preis",
    "typCode"
})
public class Produkt {

    @JsonProperty("ablageTablar")
    private AblageTablar ablageTablar;
    @JsonProperty("beschreibung")
    private String beschreibung;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("maximalerBestand")
    private Integer maximalerBestand;
    @JsonProperty("minimalerBestand")
    private Integer minimalerBestand;
    @JsonProperty("name")
    private String name;
    @JsonProperty("preis")
    private Integer preis;
    @JsonProperty("typCode")
    private String typCode;

    /**
     *
     * @param id ID
     * @param preis Preis
     * @param maximalerBestand Maximaler Bestand
     * @param ablageTablar Ablage Tablar (nr)
     * @param name Name
     * @param beschreibung Beschreibung
     * @param minimalerBestand minimaler Bestand
     * @param typCode Typcode (eindeutig)
     */
    public Produkt(AblageTablar ablageTablar, String beschreibung, int id, int maximalerBestand, int minimalerBestand, String name, int preis, String typCode) {
        this.ablageTablar = ablageTablar;
        this.beschreibung = beschreibung;
        this.id = id;
        this.maximalerBestand = maximalerBestand;
        this.minimalerBestand = minimalerBestand;
        this.name = name;
        this.preis = preis;
        this.typCode = typCode;
    }
    
    //Dummy Constructor needed for JSONMapping
    public Produkt(){        
    }

    //Getter und Setter Methode
    @JsonProperty("ablageTablar")
    public AblageTablar getAblageTablar() {
        return ablageTablar;
    }

    @JsonProperty("ablageTablar")
    public void setAblageTablar(AblageTablar ablageTablar) {
        this.ablageTablar = ablageTablar;
    }

    @JsonProperty("beschreibung")
    public String getBeschreibung() {
        return beschreibung;
    }

    @JsonProperty("beschreibung")
    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("maximalerBestand")
    public Integer getMaximalerBestand() {
        return maximalerBestand;
    }

    @JsonProperty("maximalerBestand")
    public void setMaximalerBestand(Integer maximalerBestand) {
        this.maximalerBestand = maximalerBestand;
    }

    @JsonProperty("minimalerBestand")
    public Integer getMinimalerBestand() {
        return minimalerBestand;
    }

    @JsonProperty("minimalerBestand")
    public void setMinimalerBestand(Integer minimalerBestand) {
        this.minimalerBestand = minimalerBestand;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("preis")
    public int getPreis() {
        return preis;
    }

    @JsonProperty("preis")
    public void setPreis(int preis) {
        this.preis = preis;
    }

    @JsonProperty("typCode")
    public String getTypCode() {
        return typCode;
    }

    @JsonProperty("typCode")
    public void setTypCode(String typCode) {
        this.typCode = typCode;
    }
}
