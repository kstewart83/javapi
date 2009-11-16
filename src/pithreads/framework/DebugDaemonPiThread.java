package pithreads.framework;


public class DebugDaemonPiThread extends DebugPiThread {

	/* package */ DebugDaemonPiThread(PiAgent agent, String name) {
		super(agent, name);
	}

	
	/* package */ DebugDaemonPiThread(PiAgent agent) {
		super(agent);
	}
	
	protected void waitForCommitment() throws RunException {
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
