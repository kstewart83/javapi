package pithreads.framework.debug;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.logging.Level;
import java.util.logging.Logger;


import pithreads.framework.debug.event.AddInputCommitmentEvent;
import pithreads.framework.debug.event.AddOutputCommitmentEvent;
import pithreads.framework.debug.event.ChannelAquiredEvent;
import pithreads.framework.debug.event.ChannelReleasedEvent;
import pithreads.framework.debug.event.NextTurnEvent;
import pithreads.framework.debug.event.TakeInputCommitmentEvent;
import pithreads.framework.debug.event.TakeOutputCommitmentEvent;
import pithreads.framework.debug.event.ThreadCreateEvent;
import pithreads.framework.event.AwakeEvent;
import pithreads.framework.event.ControlEvent;
import pithreads.framework.debug.event.EndOfAgentEvent;
import pithreads.framework.debug.event.FailedChannelAquireEvent;
import pithreads.framework.event.WaitEvent;

public class PiDataDebug extends Thread implements Receiver{

    private ArrayDeque<ControlEvent> list;
    private String cible;
	private boolean finish;

    public PiDataDebug(String cible) {
        list = new ArrayDeque<ControlEvent>();
        this.cible=cible;
        finish=false;
    }

    public void receiveEvent(ControlEvent event) {
        this.list.addFirst(event);
    }
    
    public ArrayDeque<ControlEvent> getList(){
        return list;
    }
    
	public void writeInFile(String name, String text) {
		  try {
	          BufferedWriter sortie = new BufferedWriter(new FileWriter(name));
	          sortie.write(text);	
	          sortie.flush();
	          sortie.close();
	      } catch (IOException ex) {

	      }
	}
	
	@Override
    public void run() {
		BufferedWriter sortie;
		try {
			sortie = new BufferedWriter(new FileWriter(cible));

        while (true) {

        	yield();
            if (!list.isEmpty()) {
                ControlEvent ce = list.removeLast();
                switch (ce.getType()) {
                    case ADD_INPUT_COMMITMENT: {
                        AddInputCommitmentEvent ac = ce.asAddInputCommitment();
                        sortie.write(ac.toString());
                        //System.out.print(ac.toString());
                        break;
                    }

                    case ADD_OUTPUT_COMMITMENT: {
                        AddOutputCommitmentEvent ac = ce.asAddOutputCommitment();
                        sortie.write(ac.toString());
                        //System.out.print(ac.toString());
                        break;
                    }
                    case CHANNEL_AQUIRED: {
                        ChannelAquiredEvent ac = ce.asChannelAquiredEvent();
                        sortie.write(ac.toString());
                        //System.out.print(ac.toString());
                        break;
                    }
                    case RELEASE_CHANNEL: {
                        ChannelReleasedEvent ac = ce.asChannelReleasedEvent();
                        sortie.write(ac.toString());
                        //System.out.print(ac.toString());
                        break;
                    }
                    case NEXT_TURN: {
                        NextTurnEvent ac = ce.asNextTurnEvent();
                        sortie.write(ac.toString());
                        //System.out.print(ac.toString());
                        break;
                    }
                    /*case FAILED_AQUIRE_CHANNEL: {
                        FailedChannelAquireEvent ac = ce.asFailedChannelAquireEvent();
                      //sortie.write(ac.toString());
                        System.out.print(ac.toString());
                        break;
                    }*/
                    case TAKE_INPUT_COMMITMENT: {
                        TakeInputCommitmentEvent ac = ce.asTakeInputCommitmentEvent();
                        sortie.write(ac.toString());
                        break;
                    }
                    case TAKE_OUTPUT_COMMITMENT: {
                        TakeOutputCommitmentEvent ac = ce.asTakeOutputCommitment();
                        sortie.write(ac.toString());
                        //System.out.print(ac.toString());
                        break;
                    }
                    case CREATE_THREAD: {
                        ThreadCreateEvent ac = ce.asThreadCreateEvent();
                      	sortie.write(ac.toString());
                        //System.out.print(ac.toString());
                        break;
                    }
                    case AWAKE_THREAD: {
                        AwakeEvent ac = ce.asAwakeEvent();
                      	sortie.write(ac.toString());
                        //System.out.print(ac.toString());
                        break;
                    }
                    case WAIT_THREAD: {
                        WaitEvent ac = ce.asWaitEvent();
                      	sortie.write(ac.toString());
                        //System.out.print(ac.toString());
                        break;
                    }
                    case END_OF_AGENT : {
                    	pithreads.framework.debug.event.EndOfAgentEvent ac = ce.asEndOfAgentEvent();
                    	sortie.write(ac.toString());
                        //System.out.print(ac.toString());
                    	sortie.flush();
                    	sortie.close();
                    	return;
                    }
                    case LOG: {
                        
                        break;
                    }
                    default : {
                    	System.out.println("unknowned event");
                    	//throw new IllegalArgumentException("Does not understand this event: "+ce);
                    }


            }
            try {
                this.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(PiDataDebug.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
		}} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.finish=true;
		}
    }
    
	public static void main(String ... args){
		PiDataDebug pdd = new PiDataDebug("cible");
		pdd.start();
	}
}
