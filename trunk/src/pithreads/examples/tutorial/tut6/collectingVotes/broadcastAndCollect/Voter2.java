package pithreads.examples.tut6.collectingVotes.broadcastAndCollect;

import pithreads.framework.PiChannel;
import pithreads.framework.RunException;
import pithreads.framework.broadcast.PiChannelBroadcast;
import pithreads.framework.broadcast.TaskBroadCast;

public class Voter2 extends TaskBroadCast {

	private PiChannelBroadcast<PiChannel<Boolean>> vote;
	private int id;
	
	public Voter2(PiChannelBroadcast<PiChannel<Boolean>> vote,int id){
		this.vote=vote;
		this.id = id;
	}
	
	
	@Override
	public void body() throws RunException {
		log("voter#"+id+" started");
		while(true){
			PiChannel<Boolean> res = listen(vote,id);
			if (Math.random()<0.5){
				log("Voter #"+id+" : voted 'YES'");
				send(res,true);
			}else{
				log("Voter #"+id+" : voted 'NO'");
				send(res,false);
			}
			
		}
	}
}
