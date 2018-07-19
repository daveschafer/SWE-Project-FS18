/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package divers;

import ch.hslu.swe.datastructures.AblageTablar;
import ch.hslu.swe.datastructures.Adresse;
import ch.hslu.swe.datastructures.Kontakt;
import ch.hslu.swe.datastructures.Moebelhaus;
import ch.hslu.swe.datastructures.Produkt;
import ch.hslu.swe.helper.JSONGetter;
import ch.hslu.swe.helper.MONGODBConverter;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

/**
 *
 * @author Dave
 */
public class TestPackageEverythingElse {

    public TestPackageEverythingElse() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    @DisplayName("Testet alle unnoetigen Klassen")
    public void TestEveryThingElse() throws IOException, URISyntaxException {

        //FunctionsA0xTesting ft = new FunctionsA0xTesting();
        AblageTablar at = new AblageTablar("test", 0);
        at.getBezeichnung();
        at.getId();
        at.setBezeichnung("neeew");
        at.setId(1);
        AblageTablar at2 = new AblageTablar();

        Adresse ad = new Adresse("a", 2, "b");
        ad.getOrt();
        ad.getPlz();
        ad.getStrasse();
        ad.setOrt("c");
        ad.setPlz(3);
        ad.setStrasse("d");
        Adresse ad2 = new Adresse();

        Kontakt kt = new Kontakt("abcd@mail.com", "09999");
        kt.getEmail();
        kt.getTelefon();
        kt.setEmail("new@n.com");
        kt.setTelefon("827820");
        Kontakt kt2 = new Kontakt();

        Moebelhaus mht = new Moebelhaus(ad, 53, kt, "test", "test");
        mht.getAdresse();
        mht.getId();
        mht.getKontakt();
        mht.getMoebelhausCode();
        mht.getName();
        mht.setAdresse(ad);
        mht.setId(334);
        mht.setKontakt(kt);
        mht.setMoebelhausCode("ffefe");
        mht.setName("yolso");
        Moebelhaus mht2 = new Moebelhaus();

        Produkt pt = new Produkt(at, "superb", 0, 0, 0, "superb2", 0, "supber3");
        pt.getAblageTablar();
        pt.getBeschreibung();
        pt.getId();
        pt.getMaximalerBestand();
        pt.getMinimalerBestand();
        pt.getName();
        pt.getPreis();
        pt.getTypCode();
        pt.setAblageTablar(at);
        pt.setBeschreibung("dsf");
        pt.setId(2424);
        pt.setMaximalerBestand(Integer.MAX_VALUE);
        pt.setMinimalerBestand(Integer.MIN_VALUE);
        pt.setName("dasf");
        pt.setPreis(999);
        pt.setTypCode("fsfdgkn");
        Produkt pt2 = new Produkt();
        
        MONGODBConverter monc = new MONGODBConverter();
        JSONGetter jg1 = new JSONGetter();

    }
}
