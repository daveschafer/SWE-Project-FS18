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
    "bezeichnung",
    "id"
})
public class AblageTablar {

    @JsonProperty("bezeichnung")
    private String bezeichnung;
    @JsonProperty("id")
    private Integer id;

    public AblageTablar(String bezeichnung, int id) {
        this.bezeichnung = bezeichnung;
        this.id = id;
    }

    //Dummy Constructor needed for JSONMapping
    public AblageTablar() {
    }

    @JsonProperty("bezeichnung")
    public String getBezeichnung() {
        return bezeichnung;
    }

    @JsonProperty("bezeichnung")
    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }
}
