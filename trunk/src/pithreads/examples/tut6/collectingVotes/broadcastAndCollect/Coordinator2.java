package pithreads.examples.tut6.collectingVotes.broadcastAndCollect;

import java.util.ArrayList;

import pithreads.framework.PiChannel;
import pithreads.framework.RunException;
import pithreads.framework.Task;
import pithreads.framework.broadcast.PiChannelBroadcast;
import pithreads.framework.broadcast.TaskBroadCast;

public class Coordinator2 extends TaskBroadCast {

	private PiChannelBroadcast<PiChannel<Boolean>> vote;
	private int nb;
	private Task ifYes;
	private Task ifNo;
	
	public Coordinator2(PiChannelBroadcast<PiChannel<Boolean>> vote,
					int nb,	Task ifYes, Task ifNo){
		this.vote=vote;
		this.nb = nb;
		this.ifYes=ifYes;
		this.ifNo=ifNo;
	}
	
	@Override
	public void body() throws RunException {
		for(int i = 0;i<nb-1;i++){
			// create a secret channel for this turn's vote
			PiChannel<Boolean> secret = new PiChannel<Boolean>(vote.agent,"secret#"+i);
			// broadcast the vote activation message (with the secret channel)
			//and collect all votes
			ArrayList<Boolean> votes = broadcastAndCollect(vote, 5, secret);  
			// analyze the votes an take the final decision
			int nbYes = 0;
			int nbNo = 0;
			
			for (int j = 0;j<votes.size();j++){
				if (votes.get(j)){
					nbYes++;
				}else{
					nbNo++;
				}
			}
			
			if (nbYes > nbNo){
				ifYes.execute(this.getPiThread());
			}else if (nbYes == nbNo){
				log("Perfect equalty, no winner this turn.");
			}else{
				ifNo.execute(this.getPiThread());
			}
		}

	}

}
