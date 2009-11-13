package pithreads.framework.event;

import java.util.EventObject;

import pithreads.framework.debug.event.AddInputCommitmentEvent;
import pithreads.framework.debug.event.AddOutputCommitmentEvent;
import pithreads.framework.debug.event.ChannelAquiredEvent;
import pithreads.framework.debug.event.ChannelReleasedEvent;
import pithreads.framework.debug.event.FailedChannelAquireEvent;
import pithreads.framework.debug.event.TakeInputCommitmentEvent;
import pithreads.framework.debug.event.TakeOutputCommitmentEvent;
import pithreads.framework.debug.event.ThreadCreateEvent;
import pithreads.framework.debug.event.EndOfAgentEvent;


public abstract class ControlEvent extends EventObject {
	private static final long serialVersionUID = -8104975442770201118L;

	public static enum Type { REGISTER_THREAD, UNREGISTER_THREAD, NEW_CHANNEL, RECLAIM_CHANNEL, LOG, 
		WAIT_THREAD, AWAKE_THREAD, USER, CHANNEL_AQUIRED, FAILED_AQUIRE_CHANNEL, RELEASE_CHANNEL,
		ADD_INPUT_COMMITMENT, ADD_OUTPUT_COMMITMENT, TAKE_INPUT_COMMITMENT, TAKE_OUTPUT_COMMITMENT, NEXT_TURN,
        END_OF_AGENT, CREATE_THREAD} ;
	
	private final long time;
	protected final Type type;
	
	protected ControlEvent(Object source,Type type) {
		super(source);
		this.type = type;
		time = System.currentTimeMillis();
	}

    public AddInputCommitmentEvent asAddInputCommitment() {
        return (AddInputCommitmentEvent) this;
    }

    public AddOutputCommitmentEvent asAddOutputCommitment() {
        return (AddOutputCommitmentEvent) this;
    }

    public ChannelAquiredEvent asChannelAquiredEvent() {
        return (ChannelAquiredEvent) this;
    }

    public ChannelReleasedEvent asChannelReleasedEvent() {
        return (ChannelReleasedEvent) this;
    }

    public FailedChannelAquireEvent asFailedChannelAquireEvent() {
        return (FailedChannelAquireEvent) this;
    }

    public TakeInputCommitmentEvent asTakeInputCommitmentEvent() {
        return (TakeInputCommitmentEvent) this;
    }

    public TakeOutputCommitmentEvent asTakeOutputCommitment() {
        return (TakeOutputCommitmentEvent) this;
    }

    public ThreadCreateEvent asThreadCreateEvent() {
        return (ThreadCreateEvent) this;
    }
	
	public Type getType() {
		return type;
	}
	
	public long getTime() {
		return time;
	}
	
	public RegisterEvent asRegisterEvent() {
		return (RegisterEvent) this;
	}

	public UnregisterEvent asUnregisterEvent() {
		return (UnregisterEvent) this;
	}
	
	public LogEvent asLogEvent() {
		return (LogEvent) this;
	}
	
	public NewEvent asNewEvent() {
		return (NewEvent) this;
	}
	
	public ReclaimEvent asReclaimEvent() {
		return (ReclaimEvent) this;
	}

	public WaitEvent asWaitEvent() {
		return (WaitEvent) this;
	}
	
	public AwakeEvent asAwakeEvent() {
		return (AwakeEvent) this;
	}

	public EndOfAgentEvent asEndOfAgentEvent() {
		return (EndOfAgentEvent) this;
	}
	
	public  String toString(){
		return this.type.toString() + " " + source.toString() + " " + "\n";
	}

	public pithreads.framework.debug.event.NextTurnEvent asNextTurnEvent() {
		return (pithreads.framework.debug.event.NextTurnEvent) this;
	}

}
