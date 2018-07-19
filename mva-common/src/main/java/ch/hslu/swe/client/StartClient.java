/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.client;

/**
 * Starte CLICommands (Einstiegspunkt). Übergibt angeheftete Strings, bet. Commands und prüft diese danach.
 * 
 * @author Dave
 */
public class StartClient {

    public static void main(final String[] args) {
        CLICommands clicommands = new CLICommands(args);
        //maybe seperate thread for this?
        //überprüft die Argumente
        clicommands.processCommands();
        //führt die argumente aus
        clicommands.run();

    }

}
