/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pithreads.visualisation.visuEvent;

/**
 *
 * @author mathurin
 */
public class OverligneTheChannel extends VisuEvent {

    private Object idChannel;

    public OverligneTheChannel(Object source,Object idChannel){
    super(source,Type.OVERLIGNE_THE_CHANNEL);
    this.idChannel = idChannel;
    }

    public int getChannelId() {
        return (Integer) idChannel;
    }
}
