package pithreads.examples.tut6.collectingVotes.broadcastAndCollect;


import pithreads.framework.PiAgent;
import pithreads.framework.PiChannel;
import pithreads.framework.RunException;
import pithreads.framework.Task;
import pithreads.framework.broadcast.PiChannelBroadcast;
import pithreads.framework.broadcast.PiThreadBroadcast;
import pithreads.framework.debug.DebugFactory;
import pithreads.framework.debug.PiFactory;
public class PollingBooth2 {

	private static final int NBVOTERS = 5;
	private static final int TURNS = 15;
	
	public static void main(String... args) {
		PiFactory myFactory = new DebugFactory(false);
	
		PiAgent agent = myFactory.newPiAgent();
	
		final PiChannelBroadcast<PiChannel<Boolean>> vote = myFactory.newPiChannelBroadcast(agent,"vote");
		
		for(int i = 0;i<NBVOTERS;i++){
			PiThreadBroadcast voter = myFactory.newPiThreadBroadcast(agent, "voter#"+i);
			Voter2 taskVoter =  new Voter2(vote,i);
			voter.assign(taskVoter);
			voter.start();
		}
		
		Task ifYes = new Task(){
			@Override
			public void body() throws RunException {
				log("global decision is YES");
			}
		};
		Task ifNo = new Task(){
			@Override
			public void body() throws RunException {
				log("global decision is NO");
			}
		};
		PiThreadBroadcast coordinator = myFactory.newPiThreadBroadcast(agent,"coordinator"); 
		Coordinator2 taskCoordinator = new Coordinator2(vote,TURNS,ifYes,ifNo);
		coordinator.assign(taskCoordinator);
		coordinator.start();
		agent.detach();
	}
}
