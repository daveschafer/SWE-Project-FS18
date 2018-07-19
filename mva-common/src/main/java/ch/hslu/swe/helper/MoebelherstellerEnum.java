/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.helper;


/**
 *
 * @author Dave
 */
public enum MoebelherstellerEnum {
    FISCHER,
    WALKER,
    ZWISSIG,
    TEST;

    public String returnBaseURL() {
        switch (this) {
            case FISCHER:
                return "http://10.177.1.94:8081/rmhr-fischer";
            case WALKER:
                return "http://10.177.1.94:8082/rmhr-walker";
            case ZWISSIG:
                return "http://10.177.1.94:8083/rmhr-zwissig";
            case TEST:
                return "http://10.177.1.94:8090/rmhr-test";
            default:
                throw new IllegalArgumentException("Möbelhersteller nicht gefunden");
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case FISCHER:
                return "Fischer";
            case WALKER:
                return "Walker";
            case ZWISSIG:
                return "Zwissig";
            case TEST:
                return "Test";
            default:
                throw new IllegalArgumentException("MH nicht gefunden!");
        }
    }
}
