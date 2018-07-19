/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.ParseException;
import org.apache.logging.log4j.LogManager;

/**
 * CLI Commands Klasse. Einstiegspunkt, startet den Parser.
 * @author Dave
 */
public class CLICommands {

    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(CLICommands.class);
    private String[] args;

    //Hier sind alle JCommander Parameter definiert, wird fürs parsen benötigt
    final RMICLIParameters mainArgs = new RMICLIParameters();

    public CLICommands(String[] args) {
        this.args = args;
    }

    /**
     * Parst die Commands welche übergeben werden und überprüft ob sie Valid
     * sind.
     */
    public void processCommands() {
        //Process the args here
        JCommander jcommander = new JCommander(mainArgs);

        jcommander.setProgramName("G04Client");

        try {
            jcommander.parse(args);
        } catch (ParameterException exception) {
            LOG.error(exception.getMessage());
            showUsage(jcommander);
        }

        if (mainArgs.isHelp()) {
            showUsage(jcommander);
        }
    }

    void showUsage(JCommander jcommander) {
        jcommander.usage();
        System.exit(0);
    }

    public void run() {
        LOG.debug("*************************************");
        LOG.debug("Starte Client mit folgenden Optionen:");
        LOG.debug(mainArgs.toString());
        //do a bit more here
        //Jetzt muss der Wahre Client gestartet werden
        RMIClient rmiclient = new RMIClient(mainArgs.getRMIServerIP(), mainArgs.getRMIServerPort());

        try {
            rmiclient.executeFunction(mainArgs.getFunctionNumber(), mainArgs.getMoebelhersteller());
        } catch (MalformedURLException | NotBoundException | RemoteException | ParseException e) {
            LOG.error("Fehler bei der Command Execution: " + e.getMessage());
        }
    }
}
