/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pithreads.visualisation.visuEvent;

import java.io.File;

/**
 *
 * @author mathurin
 */
public class NewFileToParseEnteredEvent extends VisuEvent {
    private File fichier;

    public NewFileToParseEnteredEvent(Object source, Object fichier){
        super(source,Type.NEW_FILE_TO_PARSE_ENTERED);
        this.fichier = (File) fichier;
    }

    public File getFichier(){
    return fichier;
    }

}
