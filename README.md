spring-batch-quartz-example
===========================

## Summary ##

An example which is meant to show how Spring Batch and Quartz can address the key issues of batch processing and job scheduling in a clustered
environment.

## Used Technologies ##

Spring Framework(IoC, JDBC, Transactions, etc.), Quartz Scheduler, Spring Batch, MySQL.

## Project details ##

### How to run ###

The application contains two "main" classes:

1) org.ilya40umov.batch.mains.ActivityEmulator does exactly what it's supposed to, it emulates some activity from users. This part is only needed to
keep adding some new data to DB. You should run only one instance of this class(meaning that this part does not deal with clustering).

2) org.ilya40umov.batch.mains.SchedulerRunner is meant to be run in multiple instances concurrently in order to simulate a bunch of nodes in a cluster.

### Simulated environment ###

The example is meant to test the following environment: several servers(at least 2 nodes) running in a cluster against RDBMS(hopefully clustered)
which have to perform certain batch tasks periodically and have fail-over, work distribution etc.

## Addressed Scenarios ##

### Scheduler: No single point of failure ###

<b>Use case:</b> Make sure that if one node goes down, the scheduled tasks are still being executed by the rest of the nodes.

<b>How supported/implemented:</b> Quartz should be running on each machine in a cluster.
Each Quartz should be configured to work with DB-backed JobStore and clustering should be enabled in Quartz properties.
When at least 1 node with Quartz is up, the scheduled tasks will keep being executed(guaranteed by Quartz architecture).

<b>Steps to verify:</b> Start one instance of ActivityEmulator(optional). Start several instances of SchedulerRunner.
Watch them executing jobs. Kill some of them. See how load is spread between the nodes which are left running.

### Scheduler: Work distribution ###

<b>Use case:</b> Make sure that the tasks are getting distributed among nodes in the cluster.
(This is important because after a certain point one node won't be able to handle all tasks).

TODO: verify and document

XXX built into Quartz architecture

### Scheduler: Misfire Support ###

<b>Use case:</b> Make sure that if all nodes go down and then after while at least one is back online,
all of missed job executions(for particular jobs which are sensitive to misfires) are invoked.

TODO: verify and document

XXX built into Quartz architecture

### ???: Task Recovery ###

<b>Use case:</b> Make sure that if a node executing a certain job goes down, the job is automatically repeated/re-started.

TODO: verify and document

XXX (?Quartz requestRecovery feature or Spring Batch ?)

### ???: Retries Support ###

Retry a job if it fails due to "retry-able" reason(such as a network connectivity issue).

### General: Monitoring ###

<b>Use case:</b> There should be an easy way to get the following info at any point in time:
list of all jobs which are being executed at the moment, history of all job executions(with parameters and execution results success/failure),
list of all scheduled jobs(e.g. next time a particular job runs etc.).

### General: Execution Management ###

How do I manually re-execute a particular job(with given parameters) if it fails completely(i.e. no luck after N retries)?

### General: Graceful Halt ###

How can I signal to all nodes to stop, so that I can deploy a new version of software, do maintenance etc.?