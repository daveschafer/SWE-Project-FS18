/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.datastructures;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ort",
    "plz",
    "strasse"
})
public class Adresse {

    @JsonProperty("ort")
    private String ort;
    @JsonProperty("plz")
    private Integer plz;
    @JsonProperty("strasse")
    private String strasse;

    /**
     * No args constructor for use in serialization
     *
     */
    public Adresse() {
    }

    /**
     *
     * @param strasse Strasse
     * @param plz Postleitzahl
     * @param ort Ort
     */
    public Adresse(String ort, Integer plz, String strasse) {
        super();
        this.ort = ort;
        this.plz = plz;
        this.strasse = strasse;
    }

    @JsonProperty("ort")
    public String getOrt() {
        return ort;
    }

    @JsonProperty("ort")
    public void setOrt(String ort) {
        this.ort = ort;
    }

    @JsonProperty("plz")
    public Integer getPlz() {
        return plz;
    }

    @JsonProperty("plz")
    public void setPlz(Integer plz) {
        this.plz = plz;
    }

    @JsonProperty("strasse")
    public String getStrasse() {
        return strasse;
    }

    @JsonProperty("strasse")
    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }
}
