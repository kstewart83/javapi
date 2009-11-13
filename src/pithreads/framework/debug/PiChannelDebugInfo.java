package pithreads.framework.debug;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import pithreads.framework.PiAgent;
import pithreads.framework.commit.InputCommitment;
import pithreads.framework.commit.OutputCommitment;

public class PiChannelDebugInfo {

    private String data="";
    private Set<InputCommitment> inCommits;
    private Set<OutputCommitment> outCommits;
    private int refCount;
    private final String name;
    private volatile int id;
    private PiAgent agent;
    private AtomicBoolean acquired;
    private Thread owner;
    private int nbOwners;

    public PiChannelDebugInfo(PiChannelDebug pcd) {
        this.inCommits = pcd.inCommits;
        this.outCommits = pcd.outCommits;
        this.refCount = pcd.refCount;
        this.name = pcd.name;
        this.id = pcd.id;
        this.agent = pcd.agent;

        this.acquired = pcd.acquired;
        this.owner = pcd.owner;

        this.nbOwners = pcd.nbOwners;

        data = toString();
    }
    
    public PiChannelDebugInfo(String name, int refCount, int id, AtomicBoolean acq, int nbOwners){
        this.name=name;
        this.refCount=refCount;
        this.id=id;
        this.acquired=acq;
        this.nbOwners=nbOwners;
    }

    public int getId() {
        return this.id;
    }

    public String getName(){
    return name;
    }
    public int refCount(){
    return refCount;
    }
    public Set<InputCommitment> getInCommits(){
        return inCommits;
        }
    public Set<OutputCommitment> getOutCommits(){
    return outCommits;
    }
    public Thread getOwner(){
    return owner;
    }
    public int nbOwners(){
    return nbOwners;
    }

    public void upadteInfo(PiChannelDebug pcd) {
        this.inCommits = pcd.inCommits;
        this.outCommits = pcd.outCommits;
        this.refCount = pcd.refCount;
        //this.name=pcd.name;
        this.id = pcd.id;
        this.agent = pcd.agent;

        this.acquired = pcd.acquired;
        this.owner = pcd.owner;

        this.nbOwners = pcd.nbOwners;
    }

    public String toString() {
        String result="";
        result += "\nPiChannel\n";

        result += "inCommits \n";
        if (inCommits != null) {
            for (InputCommitment i : inCommits) {
                result += "type=" + i.getType() + "\n";
            }
        }
        result += "EndOfinCommits\n";

        result += "outCommits\n";
        if (outCommits != null) {
            for (OutputCommitment i : outCommits) {
                result += "type=" + i.getType() + "\n";
            }
        }
        result += "EndOfoutCommits\n";

        result += "refCount " + refCount + "\n";
        result += "name " + name + "\n";
        result += "id " + id + "\n";
        //data += "agent " + agent.toString() + "\n";
        result += "acquired " + acquired + "\n";
        //data += "owner " + owner.toString() + "\n";
        result += "nbOwner " + nbOwners + "\n";
        result+="EndOfPiChannel";
        return result;
    }
}
