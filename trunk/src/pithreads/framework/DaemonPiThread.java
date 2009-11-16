package pithreads.framework;


public class DaemonPiThread extends PiThread {

	/* package */ DaemonPiThread(PiAgent agent, String name) {
		super(agent, name);
	}

	
	/* package */ DaemonPiThread(PiAgent agent) {
		super(agent);
	}
	
	/* package */ void waitForCommitment() throws RunException {
		// block until awaken
		// this version does not send event
		try {
			immediateLock.acquire();
		} catch(InterruptedException e) {
			RunException re = new RunException("Thread interrupted");
			re.initCause(e);
			throw re;
		}
	}

}
