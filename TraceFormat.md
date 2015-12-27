# Introduction #

Pithreads may be unpredictable and to understand a program using them we need a tool. It should be able to trace all actions performed by every Pithread running. Those traces must respect the format we established here so that the tool can produce an easy to understand logfile.

By the way that tool permits you to track down a bug and to eliminate it.


# Format basics #

  * option 1 : use the JSon format to encode the traces

  * option 2 : use s-expressions

  * option 3 : use XML

# High-level traces #

## Spawn a new thread ##

```
{  "agentId" : <id> , 
   "clock" : <val> ,
   "trace" = { "type" : "Spawn",
                      "threadName" : <name>,
                      "threadId" : <tid>,
                    }
} 
```

Using s-expressions :

```
(trace 
   (agentId <id>)
   (clock <val>)
   (msg (type spawn)
             (threadName <name>)
             (threadId <tid>)))))        
```

Using infix s-expressions :

```
trace {
  agentId = <id>,
  clock = <val>,
  msg {
     type = spawn,
     threadName = <name>,
     threadId = <tid>
  }
}
```


## Create a new channel ##

```
{  "agentId" : <id> , 
   "clock" : <val> ,
   "trace" = { "type" : "CreateChannel",
                      "Chanid" : <cid>,
                      "Datatype" : <Type>,
                    }
} 
```

## Send a message (blocking) ##


```
{  "agentId" : <id> , 
   "clock" : <val> ,
   "trace" = { "type" : "BlockingSend",
                      "sendid" : <sid>,
                      "Chanid"   : <cid>                 
                    }
} 
```


## Receive a message (blocking) ##

```
{  "agentId" : <id> , 
   "clock" : <val> ,
   "trace" = { "type" : "BlockingReceive",
                      "rdcvid" : <rid>,
                      "Chanid"   : <cid>                 
                    }
} 
```

## Synchronization (simple send/recv case) ##


```
{  "agentId" : <id> , 
   "clock" : <val> ,
   "trace" = { "type" : "Sync",
                      "sendId" : <sid>,
                      "recvId" : <rid>,
                      "chanId" : <cid>,
                      "data" : <repr>
                    }
} 
```

## Choice (blocking) ##
### Trace ###
```
{  "agentId" : <id> , 
   "clock" : <val> ,
   "trace" = { "type" : "Choice",
               "ThreadId" : <tid>
               "strategy" : "localPriority",   // also "random", etc...
               "branches" : [
                          {
                            // see "branch format" below 
                          }, 
                          
                      ]
                    }
} 
```
### Branch format ###

**Input**
```
{
    "guard" : <guard>,
    "action" : {
              "type" : "input",
              "channel" : <cid>,
              "Data" : <repr>
    } 
}
```
**Output**
```
{
    "guard" : <guard>,
    "action" : {
              "type" : "Output",
              "channel" : <cid>
    } 
}
```
**User**
```
{
    "guard" : <guard>,
    "action" : {
              "type" : "User"
    } 
}
```




## Enact choice ##

```
{  "agentId" : <id> , 
   "clock" : <val> ,
   "trace" = { "type" : "EnactChoiceSync",
                      "sender" : { id:<sid>, branch:<int>},
                      "receiver" : { id:<rid>, branch:<int>},
                      "chanId" : <cid>,
                      "data" : <repr> ,
                    }
} 
```

  * Internal choice

```
{  "agentId" : <id> , 
   "clock" : <val> ,
   "trace" = { "type" : "EnactChoiceInternal",
                      "thread" : { id:<sid>, branch:<int>},
                    }
} 
```

## try Receive ##

```
{  "agentId" : <id> , 
   "clock" : <val> ,
   "trace" = { "type" : "TryReceive",
                      "Rcvid" : <rid>,
                      "Chanid"   : <cid>                 
                    }
} 
```

## try Send ##

```
{  "agentId" : <id> , 
   "clock" : <val> ,
   "trace" = { "type" : "TrySend",
                      "Sendid" : <sid>,
                      "Chanid"   : <cid>                 
                    }
} 
```

## Log ##

```
{  "agentId" : <id> , 
   "clock" : <val> ,
   "trace" = { "type" : "Log",
                      "Threadid" : <tid>,
                      "Message"   : <message>                 
                    }
} 
```


# Low-level traces #

```
{  "trace" = { "type" : "CreateAgent",
               "agentid" : <aid>
               "agentname" : <agentname>
            }
}
```



# Wiki help #

Add your content here.  Format your content with:
  * Text in **bold** or _italic_
  * Headings, paragraphs, and lists
  * Automatic links to other wiki pages