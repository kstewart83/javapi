package pithreads.framework;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the configuration options for pi-thread agents.
 * 
 * The options are set according to the following rules (in priority order):
 * - explicit setting the option at the factory level
 * - through system environment variables, or -D command line options
 * 
 * @author Frederic Peschanski
 *
 */
public class PiAgentConfig {
	private Map<String,Object> userConfig;
	
	private final boolean PIAGENT_TERMINATION_DETECTION__DEFAULT = true;
	private final int PIAGENT_QUEUE_SIZE__DEFAULT = 256;
	private final long PIAGENT_QUEUE_POLL_TIMEOUT__DEFAULT = 250;
	private final boolean PIAGENT_LOG_USE_STDOUT__DEFAULT = true;
	private final boolean PIAGENT_LOG_USE_STDERR__DEFAULT = false;
	private final String PIAGENT_LOG_USE_FILE__DEFAULT = null;
	
	public PiAgentConfig() {
		userConfig = new HashMap<String, Object>();
	}
		
	/** This enables/disables the termination detection algorithm. 
	 * The environment variable is : piagent.termination.detection
	 * Command line use:
	 *   -Dpiagent.termination.detection=true    enables the option
	 *   -Dpiagent.termination.detection=false   disables the option
	 */
	public boolean terminationDetection() {
		// first check user configuration
		Object obj = userConfig.get("PIAGENT_TERMINATION_DETECTION");
		if(obj!=null) {
			return (Boolean) obj;
		}
		// second check system environment
		String opt = System.getProperty("piagent.termination.detection");
		if(opt.toUpperCase().equals("TRUE")) {
			return true;
		} else if(opt.toUpperCase().equals("FALSE")) {
			return false;
		}
		// third use default
		return PIAGENT_TERMINATION_DETECTION__DEFAULT;
	}
	
	/** Force the termination detection option. */
	public void setTerminationDetection(boolean flag) {
		userConfig.put("PIAGENT_TERMINATION_DETECTION",flag);
	}
	
	/** This options sets the logging information to output on standard output
	 * The environment variable is : piagent.log.use.stdout
	 * Command line use:
	 *   -Dpiagent.log.use.stdout=true    enables the option
	 *   -Dpiagent.log.use.stdout=false   disables the option
	 */
	public boolean logUseStdout() {
		// first check user configuration
		Object obj = userConfig.get("PIAGENT_LOG_USE_STDOUT");
		if(obj!=null) {
			return (Boolean) obj;
		}
		// second check system environment
		String opt = System.getProperty("piagent.log.use.stdout");
		if(opt.toUpperCase().equals("TRUE")) {
			return true;
		} else if(opt.toUpperCase().equals("FALSE")) {
			return false;
		}
		// third use default
		return PIAGENT_LOG_USE_STDOUT__DEFAULT;
	}
	
	/** Force the termination detection option. */
	public void setLogUseStdout(boolean flag) {
		userConfig.put("PIAGENT_LOG_USE_STDOUT",flag);
	}

	/** This options sets the logging information to output on standard error output.
	 * This option is only enabled if the standard output is not selected.
	 * The environment variable is : piagent.log.use.stderr
	 * Command line use:
	 *   -Dpiagent.log.use.stderr=true    enables the option
	 *   -Dpiagent.log.use.stderr=false   disables the option
	 */
	public boolean logUseStderr() {
		// first check user configuration
		Object obj = userConfig.get("PIAGENT_LOG_USE_STDERR");
		if(obj!=null) {
			return (Boolean) obj;
		}
		// second check system environment
		String opt = System.getProperty("piagent.log.use.stderr");
		if(opt.toUpperCase().equals("TRUE")) {
			return true;
		} else if(opt.toUpperCase().equals("FALSE")) {
			return false;
		}
		// third use default
		return PIAGENT_LOG_USE_STDERR__DEFAULT;
	}
	
	/** Force the termination detection option. */
	public void setLogUseStderr(boolean flag) {
		userConfig.put("PIAGENT_LOG_USE_STDERR",flag);
	}

	/** This options sets the logging information to output on a specified file.
	 * This option is only enabled if the standard output or error output are not selected.
	 * The environment variable is : piagent.log.use.file
	 * Command line use:
	 *   -Dpiagent.log.use.file=filename  uses filename for logging
	 *   -Dpiagent.log.use.stderr=false   disables the option
	 */
	public String logUseFile() {
		// first check user configuration
		Object obj = userConfig.get("PIAGENT_LOG_USE_FILE");
		if(obj!=null) {
			return (String) obj;
		}
		// second check system environment
		String opt = System.getProperty("piagent.log.use.file");
		if(opt!=null) {
			return opt;
		}
		// third use default
		return PIAGENT_LOG_USE_FILE__DEFAULT;
	}
	
	/** Force the termination detection option. */
	public void setLogUseFile(String filename) {
		userConfig.put("PIAGENT_LOG_USE_FILE",filename);
	}

	/** 
	 * This option is to use with care. It allows to change
	 * the queue size for event processing.
	 * Low values reduces the asynchronism between pi-threads and the pi-agent
	 * High values uses more memory and increases the time when events are processed
	 * (wrt. the time when they are sent).
	 * The environment variable is : piagent.queue.capacioty
	 * Command line use:
	 *   -Dpiagent.queue.capacity=XXX   set the option (if XXX is a strictly positive integer)
	 */
	public int queueCapacity()  {
		// first check user configuration
		Object obj = userConfig.get("PIAGENT_QUEUE_CAPACITY");
		if(obj!=null) {
			return (Integer) obj;
		}
		// second check system environment
		String opt = System.getProperty("piagent.queue.capacity");
		try {
			int val = Integer.parseInt(opt);
			if(val>0) {
				return val;
			}
		} catch(NumberFormatException e) {}
		// third use default
		return PIAGENT_QUEUE_SIZE__DEFAULT;
	}
	
	/** Force the queue size option. */
	public void setQueueCapacity(int capacity) {
		if(capacity<=0) {
			throw new IllegalArgumentException("Queue capacity must be strictly positive");
		}
		userConfig.put("PIAGENT_QUEUE_CAPACITY",capacity);
	}
	
	/** 
	 * This option is to use with care. It allows to change
	 * the timeout value (in millisecond) when polling the event queue.
	 * It is used by the termination detection algorithm has an heuristic to
	 * start the detection process.
	 * Low values starts termination early.
	 * High values starts termination late.
	 * The environment variable is : piagent.queue.poll.timeout
	 * Command line use:
	 *   -Dpiagent.queue.poll.timeout=XXX   set the option (if XXX is a strictly positive long integer)
	 */
	public long queuePollTimeout() {
		// first check user configuration
		Object obj = userConfig.get("PIAGENT_QUEUE_POLL_TIMEOUT");
		if(obj!=null) {
			return (Long) obj;
		}
		// second check system environment
		String opt = System.getProperty("piagent.queue.poll.timeout");
		try {
			long val = Long.parseLong(opt);
			if(val>0) {
				return val;
			}
		} catch(NumberFormatException e) {}
		// third use default
		return PIAGENT_QUEUE_POLL_TIMEOUT__DEFAULT;
	}

	/** Force the queue size option. */
	public void setQueuePollTimeout(long timeout) {
		if(timeout<=0) {
			throw new IllegalArgumentException("Queue poll timeout must be strictly positive");
		}
		userConfig.put("PIAGENT_QUEUE_POLL_TIMEOUT",timeout);
	}
	
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("PiAgentConfig {\n");
		buf.append("  Termination detection = ");
		buf.append(terminationDetection());
		buf.append("\n");
		buf.append("  Log use stdout = ");
		buf.append(logUseStdout());
		buf.append("\n");
		buf.append("  Log use stderr = ");
		buf.append(logUseStderr());
		buf.append("\n");
		buf.append("  Log use file = ");
		buf.append(logUseFile());
		buf.append("\n");
		buf.append("  Queue capacity = ");
		buf.append(queueCapacity());
		buf.append("\n");
		buf.append("  Queue poll timeout = ");
		buf.append(queuePollTimeout());
		buf.append("\n");
		
		return buf.toString();
	}

}
