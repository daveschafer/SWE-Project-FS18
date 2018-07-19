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
    "email",
    "telefon"
})
public class Kontakt {

    @JsonProperty("email")
    private String email;
    @JsonProperty("telefon")
    private String telefon;

    /**
     * No args constructor for use in serialization
     *
     */
    public Kontakt() {
    }

    /**
     *
     * @param email Emailadresse
     * @param telefon Telefonnummer
     */
    public Kontakt(String email, String telefon) {
        super();
        this.email = email;
        this.telefon = telefon;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("telefon")
    public String getTelefon() {
        return telefon;
    }

    @JsonProperty("telefon")
    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
}
