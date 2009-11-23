package pithreads.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Implementation of the guarded choice
 * 
 * This is the most versatile and expressive construct of the Pi-calculus.
 * 
 * A choice is simply a set of guarded execution branches (each one represented
 * by a task).
 * 
 * @author Frederic Peschanski
 *
 */
public class Choice {
	private List<Task> choiceTasks;
	private List<Guard> choiceGuards;
	private TreeSet<PiChannel<?>> choiceChannels;
	
	/**
	 * Create a choice
	 */
	public Choice() {
		choiceTasks = new ArrayList<Task>();
		choiceGuards = new ArrayList<Guard>();
		choiceChannels = new TreeSet<PiChannel<?>>(); // the use of a tree set is need because we exploit a global ordering on channels
	}

	/**
	 * Add an input guard and corresponding task to this choice.
	 * @param <T> the type of the value to receive
	 * @param chan The channel used for synchronization/input
	 * @param task The task to execute upon reception
	 */
	public <T> void addInput(PiChannel<T> chan, InputTask<T> task) {
		choiceGuards.add(new InputGuard<T>(chan,choiceGuards.size()));
		choiceChannels.add(chan);
		choiceTasks.add(task);
	}
	
	/**
	 * Add an output guard and corresponding task to this choice.
	 * @param <T> the type of the value to send
	 * @param chan the channel used for synchronization/output
	 * @param maker The value maker for this output (on-demand value creation)
	 * @param task the task to execute upon emission
	 */
	public <T> void addOutput(PiChannel<T> chan, ValueMaker<T> maker, Task task) {
		choiceGuards.add(new OutputGuard<T>(chan,maker,choiceGuards.size()));
		choiceChannels.add(chan);
		choiceTasks.add(task);
	}

	/* package */ Guard enact(PiThread thread) throws RunException {
		Guard enabledGuard = thread.choose(choiceGuards, choiceChannels);
		
		switch(enabledGuard.getType()) {
		case INPUT:
			InputTask inTask = (InputTask) choiceTasks.get(enabledGuard.getIndex());
			inTask.setReceivedValue(thread.getReceivedValue());
			thread.runTask(inTask);
			break;
		default:
			thread.runTask(choiceTasks.get(enabledGuard.getIndex()));			
		}
		
		return enabledGuard;
	}
}


