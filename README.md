spring-batch-quartz-example
===========================

## Summary ##

An example which is meant to show how Spring Batch and Quartz can address the key issues of batch processing and job scheduling in a clustered
environment.

## Used Technologies ##

Spring Framework(IoC, JDBC, Transactions, etc.), Quartz Scheduler, Spring Batch, MySQL.

## How to run ##

The application contains two "main" classes:

1) ActivityEmulator does exactly what it's supposed to. It emulates some user activity. This part is only needed to keep adding some new data to DB.
You should run only one instance of this class(meaning that this part does not deal with clustering).

2) SchedulerRunner is meant to be run in multiple instances concurrently in order to simulate a bunch of nodes in a cluster.

<b>Expected environment:</b> several servers(e.g. 3 nodes) running in a cluster against RDBMS(hopefully clustered).

## Solved Problems ##

Q: How do I make sure that if one node goes down, my scheduled tasks will keep being executed? <br/>
A: Quartz will be running on each machine with DB-backed JobStore. Thus even is one node is down all other nodes will keep doing the work. <br/>
Test: Start several instances of SchedulerRunner. Watch them executing jobs. Kill some of them. See how load is spread between the nodes which left
 running.<br/>

Q: How do I make sure that if a node executing a certain job goes down, the job is repeated/re-started if needed.<br/>
A: ... (?Quartz requestRecovery feature or Spring Batch ?)<br/>
Test: ...<br/>

Q: If all nodes go down and then at least one is back online, how do I make sure that all of missed job executions(for particular jobs which are
sensitive on this matter) are invoked?<br/>

Q: How do I know which jobs are being executed at the moment and how can I review the history of all executions?<br/>

Q: How can I signal to all nodes to stop, so that I can deploy a new version of software, do maintenance etc.?<br/>

