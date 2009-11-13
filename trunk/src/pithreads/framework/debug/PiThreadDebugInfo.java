package pithreads.framework.debug;

import java.util.Deque;
import java.util.concurrent.atomic.AtomicBoolean;
import pithreads.framework.PiAgent;
import pithreads.framework.Task;
import pithreads.framework.utils.ImmediateLock;

public class PiThreadDebugInfo {

    private String data;
    private final PiAgent agent;
    private volatile int id;
    private Deque<Task> plan;
    private ImmediateLock immediateLock;
    private Object receivedValue;
    private int enabledGuardIndex;
    private AtomicBoolean awakeLock;
    private volatile boolean terminateFlag;
    private volatile long turn;
    private String name;

    public PiThreadDebugInfo(PiThreadDebug ptd) {
        this.agent = ptd.agent;
        this.id = (int) ptd.getId();
        this.plan = ptd.plan;
        this.immediateLock = ptd.immediateLock;
        this.receivedValue = ptd.receivedValue;
        this.enabledGuardIndex = ptd.enabledGuardIndex;
        this.awakeLock = ptd.awakeLock;
        this.terminateFlag = ptd.terminateFlag;
        this.turn = ptd.turn;
        this.name = ptd.getName();
        data="";
    }

    public PiThreadDebugInfo(int id, boolean termFlag, int egi, AtomicBoolean awkLock, long turn, String name) {
        this.agent = null;
        this.id = id;
        this.terminateFlag = termFlag;
        this.enabledGuardIndex = egi;
        this.awakeLock = awkLock;
        this.turn = turn;
        this.name = name;
    }

    public Deque<Task> plan() {
        return plan;
    }

    public ImmediateLock immediateLock() {
        return immediateLock;
    }

    public Object receivedValue() {
        return receivedValue;
    }

    public int enabledGuardIndex() {
        return enabledGuardIndex;
    }

    public AtomicBoolean awakeLock() {
        return awakeLock;
    }

    public boolean getTerminateFlag() {
        return terminateFlag;
    }

    public long getTurn() {
        return turn;
    }

    public void fromString(String s) {
        int ind = 0;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        data = "";
        data += "\nPiThread\n";
        // data+="agent "+agent.toString()+"\n";
        data += "id " + id + "\n";
        data += "name " + name + "\n";
        data += "plan\n";
        if (plan != null) {
            for (int i = 0; i < plan.size(); i++) {
                data += "task " + plan.getFirst().toString() + "\n";
            }
        }
        data += "EndOfplan\n";

        // data+="immediateLock "+immediateLock.toString()+"\n"; // NullPointerException
        // data+="receivedValue "+receivedValue.toString()+"\n"; // NullPointerException
        data += "enabledGuardIndex " + enabledGuardIndex + "\n";
        data += "awakeLock " + awakeLock.toString() + "\n";
        data += "terminateFlag " + terminateFlag + "\n";
        data += "turn " + turn + "\n";
        data += "EndOfPiThread";

        return data;
    }

    public int getId() {
        return this.id;
    }
}
