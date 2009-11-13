/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pithreads.visualisation.visuEvent;

import java.util.EventObject;
import pithreads.framework.event.LogEvent;
/**
 *
 * @author mathurin
 */
public abstract class VisuEvent extends EventObject {
    //private static final long serialVersionUID ;

    public static enum Type { CREATE_ARROW_IN,CREATE_ARROW_OUT,
    SEND_MESSAGE_IN,SEND_MESSAGE_OUT,
    CREATE_OR_CHECK_CHANNEL ,CHANGE_COLOR_RELEASED,CHANGE_COLOR_FAILED,CREATE_THREAD,
    CHANGE_COLOR_THREAD_AWAKE,CHANGE_COLOR_THREAD_WAIT,NOTHING,UNREGISTER_THREAD,CHANNEL_IN_LIST_SELECTED
            , THREAD_IN_LIST_SELECTED,OVERLIGNE_THE_THREAD,OVERLIGNE_THE_CHANNEL, NEW_FILE_TO_PARSE_ENTERED, CLEAR_ALL,ADD_LOG_IN_LIST } ;

    private final Type type;
        
    public VisuEvent(Object source, Type type){
        super(source);
        this.type=type;
    }

    public AddLogInListEvent asAddLogInList() {
        return (AddLogInListEvent) this;
    }

    public AwakeThreadEvent asAwakeThreadEvent() {
        return (AwakeThreadEvent) this;
    }

    public ChangeColorReleased asChangeColorReleased() {
        return (ChangeColorReleased) this;
    }

    public ChannelInListSelectedEvent asChannelInListSelectedEvent() {
        return (ChannelInListSelectedEvent) this;
    }

    public CreateArrowInEvent asCreateArrowInEvent() {
        return (CreateArrowInEvent) this;
    }

    public CreateArrowOutEvent asCreateArrowOutEvent() {
        return (CreateArrowOutEvent) this;
    }

    public CreateOrCheckChannelInGreenEvent asCreateOrCheckChannelInGreenEvent() {
        return (CreateOrCheckChannelInGreenEvent) this;
    }

    public CreateThreadEvent asCreateThreadEvent() {
        return (CreateThreadEvent) this;
    }

    public NewFileToParseEnteredEvent asNewFileToParseEntered() {
        return (NewFileToParseEnteredEvent) this;
    }

    public OverligneTheChannel asOverligneTheChannel() {
        return (OverligneTheChannel) this;
    }

    public OverligneTheThread asOverligneTheThread() {
        return (OverligneTheThread) this;
    }

    public SendMessageInEvent asSendMessageInEvent() {
        return (SendMessageInEvent) this;
    }

    public SendMessageOutEvent asSendMessageOutEvent() {
        return (SendMessageOutEvent) this;
    }

    public WaitThreadEvent asWaitThreadEvent() {
        return (WaitThreadEvent) this;
    }

    public UnregisterThreadEvent asUnregisterThreadEvent() {
        return (UnregisterThreadEvent) this;
    }

    public ThreadInListSelectedEvent asThreadInListSelectedEvent() {
        return (ThreadInListSelectedEvent) this;
    }

    public Type getType() {
		return type;
	}

}
