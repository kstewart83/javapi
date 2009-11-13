package pithreads.framework.debug;

import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.BlockingDeque;

import pithreads.framework.PiChannel;
import pithreads.framework.PiThread;
import pithreads.framework.event.ControlEvent;

public class PiAgentDebugInfo {

    private String data;
    private BlockingDeque<ControlEvent> eventQueue;
    private int QUEUE_CAPACITY;
    private TreeMap<Integer, PiThread> piThreads;
    private TreeMap<Integer, PiChannel<?>> piChannels;
    private Set<Integer> waitThreads;
    private boolean initialSequence;
    private long startTime;
    private int genThreadId;
    private int genChanId;
    private int ID_NOT_ASSIGNED;
    private int ID_ALREADY_REGISTERED;
    private int ID_TOO_MANY;
    private int genId;

    public PiAgentDebugInfo() {
    }

    public PiAgentDebugInfo(PiAgentDebug pad) {
        this.eventQueue = pad.eventQueue;
        this.QUEUE_CAPACITY = pad.QUEUE_CAPACITY;
        this.piThreads = pad.piThreads;
        this.piChannels = pad.piChannels;
        this.waitThreads = pad.waitThreads;
        this.initialSequence = pad.initialSequence;
        this.startTime = pad.startTime;
        this.genThreadId = pad.genThreadId;
        this.genChanId = pad.genChanId;

        this.ID_NOT_ASSIGNED = pad.ID_NOT_ASSIGNED;
        this.ID_ALREADY_REGISTERED = pad.ID_ALREADY_REGISTERED;
        this.ID_TOO_MANY = pad.ID_TOO_MANY;
        this.genId = pad.genId;

        data = toString();
    }

    public PiAgentDebugInfo(String text) {
    }

    public void updateInfo(PiAgentDebug pad) {
        this.eventQueue = pad.eventQueue;
        this.QUEUE_CAPACITY = pad.QUEUE_CAPACITY;
        this.piThreads = pad.piThreads;
        this.piChannels = pad.piChannels;
        this.waitThreads = pad.waitThreads;
        this.initialSequence = pad.initialSequence;
        this.startTime = pad.startTime;
        this.genThreadId = pad.genThreadId;
        this.genChanId = pad.genChanId;

        this.ID_NOT_ASSIGNED = pad.ID_NOT_ASSIGNED;
        this.ID_ALREADY_REGISTERED = pad.ID_ALREADY_REGISTERED;
        this.ID_TOO_MANY = pad.ID_TOO_MANY;
        this.genId = pad.genId;

        data = toString();
    }

    public String toString() {

        data += "";

        data += "eventQueue \n";
        if (eventQueue != null) {
            for (int i = 0; i < eventQueue.size(); i++) {
                data += eventQueue.getFirst().toString();
                data += "\n";
            }
        }
        data += "endOfEventQueue\n";

        data += "QUEUE_CAPACITY " + QUEUE_CAPACITY + "\n";

        data += "piThreads\n";
        if (piThreads != null) {
            for (int i : piThreads.keySet()) {
                data += piThreads.ceilingEntry(i).getKey() + " " + piThreads.ceilingEntry(i).getValue().toString();
            }
        }
        data += "\nendOfpiThreads\n";

        data += "piChannels\n";
        if (piChannels != null) {
            for (int i : piChannels.keySet()) {
                data += piChannels.ceilingEntry(i).getKey() + " " + piChannels.ceilingEntry(i).getValue().toString();
            }
        }
        data += "\nendOfpiChannels\n";

        data += "waitThreads\n";
        if (waitThreads != null) {
            for (int i : waitThreads) {
                data += waitThreads + "\n";
            }
        }
        data += "\nendOfwaitThreads\n";

        data += "initialSequence " + initialSequence + "\n";
        data += "startTime " + startTime + "\n";
        data += "genThreadId " + genThreadId + "\n";
        data += "genChanId " + genChanId + "\n";
        data += "ID_NOT_ASSIGNED " + ID_NOT_ASSIGNED + "\n";
        data += "ID_ALREADY_REGISTERED " + ID_ALREADY_REGISTERED + "\n";
        data += "ID_TOO_MANY " + ID_TOO_MANY + "\n";
        data += "genId " + genId + "\n";
        return data;
    }
}
