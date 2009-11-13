/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pithreads.examples.tut1.pingpong;

import java.io.IOException;
import pithreads.framework.debug.DebugParser;
import pithreads.visualisation.ApplicationController;
import pithreads.visualisation.PopUpStart;
import pithreads.visualisation.model.*;

/**
 *
 * @author lucas
 */
public class ParserMain {

    public static void main(String... args) throws IOException {
        System.out.println("la");
        DebugParser parser = new DebugParser("test.txt");

        parser.parseFile();
        PopUpStart popUp = new PopUpStart(parser);
        popUp.setVisible(true);
    }
}






