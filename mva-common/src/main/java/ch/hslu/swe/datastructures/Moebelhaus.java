/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.datastructures;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "adresse",
    "id",
    "kontakt",
    "moebelhausCode",
    "name"
})
public class Moebelhaus {

    @JsonProperty("adresse")
    private Adresse adresse;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("kontakt")
    private Kontakt kontakt;
    @JsonProperty("moebelhausCode")
    private String moebelhausCode;
    @JsonProperty("name")
    private String name;
    
    
    /**
     * No args constructor for use in serialization
     *
     */
    public Moebelhaus() {
    }

    /**
     *
     * @param id ID
     * @param adresse Adresse
     * @param moebelhausCode Möbelhauscode
     * @param name Name
     * @param kontakt Kontakt
     */
    public Moebelhaus(Adresse adresse, Integer id, Kontakt kontakt, String moebelhausCode, String name) {
        super();
        this.adresse = adresse;
        this.id = id;
        this.kontakt = kontakt;
        this.moebelhausCode = moebelhausCode;
        this.name = name;
    }
    

    @JsonProperty("adresse")
    public Adresse getAdresse() {
        return adresse;
    }

    @JsonProperty("adresse")
    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("kontakt")
    public Kontakt getKontakt() {
        return kontakt;
    }

    @JsonProperty("kontakt")
    public void setKontakt(Kontakt kontakt) {
        this.kontakt = kontakt;
    }

    @JsonProperty("moebelhausCode")
    public String getMoebelhausCode() {
        return moebelhausCode;
    }

    @JsonProperty("moebelhausCode")
    public void setMoebelhausCode(String moebelhausCode) {
        this.moebelhausCode = moebelhausCode;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

}
