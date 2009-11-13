/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pithreads.framework.debug;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import pithreads.framework.debug.event.AddInputCommitmentEvent;
import pithreads.framework.debug.event.AddOutputCommitmentEvent;
import pithreads.framework.debug.event.ChannelAquiredEvent;
import pithreads.framework.debug.event.ChannelReleasedEvent;
import pithreads.framework.debug.event.FailedChannelAquireEvent;
import pithreads.framework.debug.event.NextTurnEvent;
import pithreads.framework.debug.event.TakeInputCommitmentEvent;
import pithreads.framework.debug.event.TakeOutputCommitmentEvent;
import pithreads.framework.debug.event.ThreadCreateEvent;
import pithreads.framework.event.ControlEvent;
import pithreads.visualisation.visuEvent.CreateThreadEvent;

/**
 *
 * @author lucas
 */
public class DebugParser implements Receiver {

    private String fileName;
    BufferedReader br;
    private ArrayDeque<ControlEvent> list;

    public DebugParser(String fileName) {
        this.fileName = fileName;
        try {
            br = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException ex) {
            System.out.println("fichier '" + fileName + "' introuvable !");
        //Logger.getLogger(DebugParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        list = new ArrayDeque<ControlEvent>();
    }

    public ArrayDeque<ControlEvent> getList() {
        return list;
    }

    public void receiveEvent(ControlEvent e) {
        this.list.addFirst(e);
    }

    public void parseFile() throws IOException {
        String chaine = "";
        String line = br.readLine();

        while (line != null) {
            //System.out.println("LINE RECUP BOUCLE WHILE :\n" + line);
            if (line.equals("CHANNEL_AQUIRED")) {
                parseChannelAcquired(br);
            }
            if (line.equals("ADD_INPUT_COMMITMENT")) {
                parseAddInputCommitment(br);
            }
            if (line.equals("ADD_OUTPUT_COMMITMENT")) {
                parseAddOutputCommitment(br);
            }
            if (line.equals("CHANNEL_RELEASED")) {
                parseReleaseChannel(br);
            }
            if (line.equals("END_OF_AGENT")) {
                // ????????
            }
            //n'apparait jamais dans le fichier text
            if (line.equals("FAILED_AQUIRE_CHANNEL")) {
                parseFailedAquireChannel(br);
            }
            if (line.equals("NEXT_TURN")) {
                parseNextTurn(br);
            }
            if (line.equals("TAKE_INPUT_COMMITMENT")) {
                parseTakeInputCommitment(br);
            }
            if (line.equals("TAKE_OUTPUT_COMMITMENT")) {
                parseTakeOutputCommitment(br);
            }
            //n'apparait jamais dans le fichier text
            if (line.equals("CREATE_THREAD")) {
                parseCreateThread(br);
            }
            chaine = "";
            line = br.readLine();
        }
    }

    private void parseChannelAcquired(BufferedReader br) throws IOException {
        String line = br.readLine();
        PiThreadDebugInfo threadInfo = null;
        PiChannelDebugInfo channelInfo = null;
        while (!line.equals("end of CHANNEL_AQUIRED")) {
            if (line.equals("PiThread")) {
                threadInfo = parsePiThread(br);
            }
            if (line.equals("PiChannel")) {
                channelInfo = parsePiChannel(br);
            }
            line = br.readLine();
        }
        receiveEvent(new ChannelAquiredEvent(threadInfo, channelInfo));
    }

    private void parseAddInputCommitment(BufferedReader br) throws IOException {
        String line = br.readLine();
        PiThreadDebugInfo threadInfo = null;
        PiChannelDebugInfo channelInfo = null;
        while (!line.equals("end of ADD_INPUT_COMMITMENT")) {
            if (line.equals("PiThread")) {
                threadInfo = parsePiThread(br);
            }
            if (line.equals("PiChannel")) {
                channelInfo = parsePiChannel(br);
            }
            line = br.readLine();
        }
        receiveEvent(new AddInputCommitmentEvent(threadInfo, channelInfo));

    }

    private void parseAddOutputCommitment(BufferedReader br) throws IOException {
        String line = br.readLine();
        PiThreadDebugInfo threadInfo = null;
        PiChannelDebugInfo channelInfo = null;
        while (!line.equals("end of ADD_OUTPUT_COMMITMENT")) {
            if (line.equals("PiThread")) {
                threadInfo = parsePiThread(br);
            }
            if (line.equals("PiChannel")) {
                channelInfo = parsePiChannel(br);
            }
            line = br.readLine();
        }
        receiveEvent(new AddOutputCommitmentEvent(threadInfo, channelInfo));
    }

    private void parseReleaseChannel(BufferedReader br) throws IOException {
        String line = br.readLine();
        PiThreadDebugInfo threadInfo = null;
        PiChannelDebugInfo channelInfo = null;
        while (!line.equals("end of CHANNEL_RELEASED")) {
            if (line.equals("PiThread")) {
                threadInfo = parsePiThread(br);
            }
            if (line.equals("PiChannel")) {
                channelInfo = parsePiChannel(br);
            }
            line = br.readLine();
        }
        receiveEvent(new ChannelReleasedEvent(threadInfo, channelInfo));
    }

    private void parseFailedAquireChannel(BufferedReader br) throws IOException {
        String line = br.readLine();
        PiThreadDebugInfo threadInfo = null;
        PiChannelDebugInfo channelInfo = null;
        while (!line.equals("end of FAILED_AQUIRE_CHANNEL")) {
            if (line.equals("PiThread")) {
                threadInfo = parsePiThread(br);
            }
            if (line.equals("PiChannel")) {
                channelInfo = parsePiChannel(br);
            }
            line = br.readLine();
        }
        receiveEvent(new FailedChannelAquireEvent(threadInfo, channelInfo));
    }

    private void parseNextTurn(BufferedReader br) throws IOException {
        String line = br.readLine();
        PiThreadDebugInfo threadInfo = null;
        //PiChannelDebugInfo channelInfo = null;
        while (!line.equals("end of NEXT_TURN")) {
            if (line.equals("PiThread")) {
                threadInfo = parsePiThread(br);
            }
            /*
            if (line.equals("PiChannel")) {
            channelInfo = parsePiChannel(br);
            }*/
            line = br.readLine();
        }
        receiveEvent(new NextTurnEvent(threadInfo));
    }

    private void parseTakeInputCommitment(BufferedReader br) throws IOException {
        String line = br.readLine();
        boolean isReceiver = false;
        PiThreadDebugInfo sourceInfo = null;
        PiThreadDebugInfo receiverInfo = null;
        PiChannelDebugInfo channelInfo = null;
        while (!line.equals("end of TAKE_INPUT_COMMITMENT")) {
            if (line.equals("Receiver :")) {
                isReceiver = true;
            }
            if (line.equals("Source :")) {
                isReceiver = false; // probablement inutile
            }
            if (line.equals("PiThread")) {
                if (!isReceiver) {
                    sourceInfo = parsePiThread(br);
                } else {
                    receiverInfo = parsePiThread(br);
                }
            }
            if (line.equals("Channel :")) {
                channelInfo = parsePiChannel(br);
            }
            line = br.readLine();
        }
        receiveEvent(new TakeInputCommitmentEvent(sourceInfo, channelInfo, receiverInfo));
    }

    private void parseTakeOutputCommitment(BufferedReader br) throws IOException {
        String line = br.readLine();
        boolean isSender = false;
        PiThreadDebugInfo senderInfo = null;
        PiThreadDebugInfo sourceInfo = null;
        PiChannelDebugInfo channelInfo = null;
        while (!line.equals("end of TAKE_OUTPUT_COMMITMENT")) {

            if (line.equals("Sender :")) {
                isSender = true;
            }
            if (line.equals("Source :")) {
                isSender = false; // probablement inutile
            }
            if (line.equals("PiThread")) {
                if (!isSender) {
                    sourceInfo = parsePiThread(br);
                } else {
                    senderInfo = parsePiThread(br);
                }
            }
            if (line.equals("Channel :")) {
                channelInfo = parsePiChannel(br);
            }
            line = br.readLine();
        }
        receiveEvent(new TakeOutputCommitmentEvent(sourceInfo, channelInfo, senderInfo));
    }

    private void parseCreateThread(BufferedReader br) throws IOException {
        String line = br.readLine();
        PiThreadDebugInfo threadInfo = null;
        //PiChannelDebugInfo channelInfo = null;
        while (!line.equals("end of CREATE_THREAD")) {
            if (line.equals("PiThread")) {
                threadInfo = parsePiThread(br);
            }
            /*if (line.equals("PiChannel")) {
            channelInfo = parsePiChannel(br);
            }*/
            line = br.readLine();
        }
        receiveEvent(new ThreadCreateEvent(threadInfo));
    }

    /**********************/
    private PiChannelDebugInfo parsePiChannel(BufferedReader br) throws IOException {
        String line = br.readLine();
        int id = 0, refCount = 0, nbOwner = 0;
        String name = null, mot;
        AtomicBoolean acquired = new AtomicBoolean(false);
        while (!line.equals("EndOfPiChannel")) {
            if (line.indexOf(" ") != -1) {
                mot = line.substring(0, line.indexOf(" "));
            } else {
                mot = line;
            }
            if (mot.equals("refCount")) {
                mot = line.substring(line.indexOf(" ") + 1);
                refCount = Integer.parseInt(mot);
            }
            if (mot.equals("id")) {
                mot = line.substring(line.indexOf(" ") + 1);
                if (mot.equals("-")) {
                    System.out.println("- !!! attention id channel negatif !!! - (id enregistrer a 666)\n");
                    mot = "666";
                }
                id = Integer.parseInt(mot);
            //System.out.println("id channel recup=" + id);
            }
            if (mot.equals("name")) {
                mot = line.substring(line.indexOf(" ") + 1);
                name = mot;
            }
            if (mot.equals("acquired")) {
                mot = line.substring(line.indexOf(" ") + 1);
                if (mot.equals("true")) {
                    acquired.set(true);
                } else {
                    acquired.set(false);
                }
            }
            if (mot.equals("nbOwner")) {
                mot = line.substring(line.indexOf(" ") + 1);
                nbOwner = Integer.parseInt(mot);
            }
            line = br.readLine();
        }
        return new PiChannelDebugInfo(name, refCount, id, acquired, nbOwner);
    }

    private PiThreadDebugInfo parsePiThread(BufferedReader br) throws IOException {
        String line = br.readLine();
        int id = 0, enableGuardIndex = 0;
        long turn = 0;
        boolean terminateFlag = false;
        AtomicBoolean awakeLock = new AtomicBoolean(false);
        String mot, name = "";
        while (!line.equals("EndOfPiThread")) {
            if (line.indexOf(" ") != -1) {
                mot = line.substring(0, line.indexOf(" "));
            } else {
                mot = line;
            }
            if (mot.equals("id")) {
                mot = line.substring(line.indexOf(" ") + 1);
                //System.out.println("mot="+mot);
                if (mot.equals("-")) {
                    System.out.println("- !!! attention id thread negatif !!! - (id enregistrer a 666)\n");
                    mot = "666";
                }
                id = Integer.parseInt(mot);
            //System.out.println("id thread recup=" + id);
            }
            if (mot.equals("name")) {
                name = line.substring(line.indexOf(" ") + 1);
            }
            if (mot.equals("enableGuardIndex")) {
                mot = line.substring(line.indexOf(" ") + 1);
                if (mot.equals("-")) {
                    mot = "666";
                }
                id = Integer.parseInt(mot);
            }
            if (mot.equals("awakeLock")) {
                mot = line.substring(line.indexOf(" ") + 1);
                if (mot.equals("true")) {
                    awakeLock.set(true);
                } else {
                    //System.out.println("\n ------------ LE MOT ="+mot);
                    awakeLock.lazySet(false);
                //awakeLock.set(false);
                }
            }
            if (mot.equals("terminateFlag")) {
                mot = line.substring(line.indexOf(" ") + 1);
                terminateFlag = Boolean.valueOf(mot).booleanValue();
            }
            if (mot.endsWith("turn")) {
                mot = line.substring(line.indexOf(" ") + 1);
                turn = Integer.parseInt(mot);
            }
            line = br.readLine();
        }
        return new PiThreadDebugInfo(id, terminateFlag, enableGuardIndex, awakeLock, turn, name);
    }
}
