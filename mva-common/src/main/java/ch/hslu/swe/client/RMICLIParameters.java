/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.client;

import ch.hslu.swe.helper.MoebelherstellerEnum;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.apache.logging.log4j.LogManager;

/**
 * Diese Klasse stellt alle Parameter für den JCommander bereit (werden fürs
 * Parsing benötigt).
 *
 * @author Dave
 */
@Parameters(separators = "=") //leertaste funktioniert defaultmässig
public class RMICLIParameters {

    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(RMICLIParameters.class);

    @Parameter(names = {"-h", "--help"},
            help = true, //Der Wert ist wichtig das jcommander den synthax als Hilfe erkennt
            description = "Zeigt alle Commands,Paremeter des CLI Clients an")
    private boolean help;

    @Parameter(names = {"-r", "-rmiserver"},
            validateWith = _ServerIP_Validator.class,
            description = "IP des RMIServers")
    private String rmiServerIP = "10.177.1.181";

    //Mongodb Server -> not yet used
    @Parameter(names = {"-d", "-databaseIP"},
            validateWith = _ServerIP_Validator.class,
            description = "IP des Datenbankservers (MongoDB)"
    )
    private String dbServerIP = "10.177.1.181";

    @Parameter(names = {"-p", "-port"},
            description = "Port des RMIServers")
    private int rmiPort = 1099;

    @Parameter(names = {"-m", "-moebelhersteller"},
            required = true,
            converter = _moebelhersteller_Converter.class,
            description = "Angabe des zu bearbeitenden Möbelherstellers."
    )
    private MoebelherstellerEnum moebelhersteller;

    @Parameter(names = {"-f", "function"},
            required = true,
            description = "Angabe der auszuführenden Funktion.\n\tPossible Values: [1,2,3,4,5,6,7,8,9]",
            validateWith = _functions_Validator.class
    ) // Todo implement arities http://jcommander.org/#_arities_multiple_values_for_parameters
    private int functionNumber;

    public boolean isHelp() {
        return help;
    }
    
    //Getter
    
    public String getRMIServerIP(){
        return rmiServerIP;
    }
    
    public int getRMIServerPort(){
        return rmiPort; 
    }
    
    public int getFunctionNumber(){
        return functionNumber;
    }
    public MoebelherstellerEnum getMoebelhersteller(){
        return moebelhersteller;
    }

    @Override
    public String toString() {
        return "\nhelp=" + help
                + "\nRMIServer="+ rmiServerIP
                + "\nRMIPort="+rmiPort
                + "\nDBServer="+dbServerIP
                + "\nMoebelhersteller="+moebelhersteller
                + "\nFunktion="+functionNumber;
    }
}
