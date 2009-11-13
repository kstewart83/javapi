/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pithreads.visualisation.visuEvent;

/**
 *
 * @author mathurin
 */
public class AddLogInListEvent extends VisuEvent {
    private String message;

    public AddLogInListEvent(Object source,Object msg){
    super(source,Type.ADD_LOG_IN_LIST);
    this.message = (String) msg;
    //System.out.println("ICI on crée log");
    }
    public String getMessage(){
    return this.message;
    }

}
