package pithreads.framework;



public class DaemonPiThread extends DefaultPiThread {

	/* package */ DaemonPiThread(PiAgent agent, String name) {
		super(agent, name);
	}

	
	/* package */ DaemonPiThread(PiAgent agent) {
		super(agent);
	}
	
	@Override
	protected final void waitForCommitment() throws RunException {
		// block until awaken
		try {
			blockNow();
		} catch(InterruptedException e) {
			RunException re = new RunException("wait for commitments failed: thread interrupted");
			re.initCause(e);
			throw re;
		}
	}

}
