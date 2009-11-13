/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pithreads.visualisation.visuEvent;

/**
 *
 * @author mathurin
 */
public class ChannelInListSelectedEvent extends VisuEvent {

    private Object idChannel;

    public ChannelInListSelectedEvent(Object source, Object idChannel) {
        super(source,Type.CHANNEL_IN_LIST_SELECTED);
        this.source=source;
        this.idChannel=idChannel;
    }

    public int getPiChannelId() {
        return (Integer) idChannel;
    }

}