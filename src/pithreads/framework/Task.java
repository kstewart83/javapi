package pithreads.framework;

import pithreads.framework.utils.Pair;



/**
 * 
 * This is the root (abstract) class of Tasks assigned to Pi-threads.
 * New tasks are created by inheriting from this class.
 * 
 * This class provides the interface for the main Pi-calculus constructs:
 * 
 * <ul>
 *   <li>send a value on a pi-channel</li>
 *   <li>receive a value on a pi-channel</li>
 *   <li>choose using a guarded choice</li>
 * </ul>
 * 
 * Other utility constructs are provided :
 * 
 * <ul>
 *   <li>logging</li>
 *   <li>printing</li>
 * </ul>
 * 
 * @author Frederic Peschanski
 */
public abstract class Task {
	private PiThread thread;
	
	protected Task() {
		this.thread = null;
	}
	
	/**
	 * Create a new named channel private to the thread executing the task.
	 * @param <T> The type of the data conveyed by the channel
	 * @param name The name of the channel (for debugging purpose)
	 * @return a new private channel.
	 */
	protected final <T> PiChannel<T> newChannel(String name) {
		return thread.getAgent().getFactory().createChannel(name);
	}
	
	/**
	 * Create a new named PiThread.
	 * @param name The name of the spawned PiThread
	 * @return a new PiThread
	 */
	protected final PiThread spawn(String name) {
		return thread.getAgent().getFactory().createThread(name);
	}
	
	/**
	 * Synchronize and receive a value on the specified channel
	 * @param <T> the type of the value to receive
	 * @param channel the channel used for communication
	 * @return the value received
	 * @throws RunException if a runtime exception occurs
	 */
	protected final <T> T receive(PiChannel<T> channel) throws RunException {
		return thread.receive(channel);
	}

	/**
	 * Synchronize and send a value on the specified channel
	 * @param <T> the type of the value to send
	 * @param channel the channel used for communication
	 * @param value the value to send
	 * @throws RunException if a runtime exception occurs
	 */
	protected final <T> void send(PiChannel<T> channel, T value) throws RunException {
		thread.send(channel, value);
	}
	
	/**
	 * Perform a choice using the Choice construct.
	 * @param choice the choice to perform
	 * @return the guard corresponding to the chosen branch of execution
	 * @throws RunException if a runtime exception occurs
	 */
	//	 public should be protected
	protected final Guard choose(Choice choice) throws RunException {
		return choice.enact(getPiThread());
	}
	
	/**
	 * Send a log event to the manager agent of the Pi-threads executing this task
	 * @param message the message to log
	 */
	//	 public should be protected
	protected final void log(String message) throws RunException {
		thread.log(message);
	}
	
	/**
	 * A shortcut for printing on the standard output
	 * @param str the string to print
	 */
	protected final void print(String str) {
		System.out.print("["+thread.getName()+"] ");
		System.out.print(str);
	}
	
	/**
	 * A shortcut for printing on the standard output
	 * Prints a newline at the end.
	 * @param str the string to print
	 */
	protected final void println(String str) {
		print(str);
		System.out.println();
	}
	
	/**
	 * A shortcut for printing a newline on the standard output
	 */
	protected final void println() {
		System.out.print("\n");
	}

	/**
	 * A shortcut for printing on the standard error output
	 * @param str the string to print
	 */
	protected final void err(String str) {
		System.err.print("["+thread.getName()+"] ");
		System.err.print(str);
	}
	
	/**
	 * A shortcut for printing on the standard error output
	 * Prints a newline at the end.
	 * @param str the string to print
	 */
	protected final void errln(String str) {
		err(str);
		System.err.println();
	}
	
	/**
	 * A shortcut for printing a newline on the standard error output
	 */
	protected final void errln() {
		System.err.print("\n");
	}

	/**
	 * The task body is the code to execute when the task is started.
	 * Each inherited task must provide an implementation for the task body.
	 * @throws RunException if a runtime exception occurs
	 */
	protected abstract void body() throws RunException;
	
	/**
	 * Get the Pi-thread actually running this task
	 * @return the refernce of the Pi-thread or null if the task is not currently executed
	 */
	protected final PiThread getPiThread() {
		return thread;
	}
	
	/** Execute the task from the specified Pi-thread */
	protected final void execute(PiThread thread) throws RunException {
		this.thread = thread;
		body();
		this.thread=null;
	}
	
	/**
	 * Synchronize and try once to receive a value on the specified channel
	 * @param <T> the type of the value to receive
	 * @param channel the channel used for communication
	 * @return the value received
	 * @throws RunException if a runtime exception occurs
	 */
	protected final <T> boolean tryReceive(PiChannel<T> channel) throws RunException {
		return thread.tryReceive(channel).getSecond();
	}
	
	protected final <T> Pair<T,Boolean> tryReceiveValued(PiChannel<T> channel) throws RunException {
		return thread.tryReceive(channel);
	}

	/**
	 * Synchronize and try once to send a value on the specified channel
	 * @param <T> the type of the value to send
	 * @param channel the channel used for communication
	 * @param value the value to send
	 * @throws RunException if a runtime exception occurs
	 */
	protected final <T> boolean trySend(PiChannel<T> channel, T value) throws RunException {
		return thread.trySend(channel, value);
	}
	
}
