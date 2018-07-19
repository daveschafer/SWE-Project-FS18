/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.helper;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Dave
 */
public class MoebelherstellerEnum_Test {

    public MoebelherstellerEnum_Test() {
    }

    @Test
    public void moebeherstellerEnum_Test() {
        MoebelherstellerEnum mh = MoebelherstellerEnum.FISCHER;
        assertTrue((mh.toString().equals("Fischer"))); //Fischer
        assertTrue((mh.returnBaseURL().equals("http://10.177.1.94:8081/rmhr-fischer"))); //Fischer
        mh = MoebelherstellerEnum.WALKER;
        assertTrue((mh.toString().equals("Walker"))); //Walker
        assertTrue((mh.returnBaseURL().equals("http://10.177.1.94:8082/rmhr-walker"))); //Walker
        mh = MoebelherstellerEnum.ZWISSIG;
        assertTrue((mh.toString().equals("Zwissig"))); //Zwyssig
        assertTrue((mh.returnBaseURL().equals("http://10.177.1.94:8083/rmhr-zwissig"))); //Zwyssig
        mh = MoebelherstellerEnum.TEST;
        assertTrue((mh.toString().equals("Test"))); // Test
        assertTrue((mh.returnBaseURL().equals("http://10.177.1.94:8090/rmhr-test"))); // Test
    }
}
