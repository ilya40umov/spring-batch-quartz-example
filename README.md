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

1) org.sbq.batch.mains.ActivityEmulator does exactly what it's supposed to, it emulates some activity from users. This part is only needed to
keep adding some new data to DB. You should run only one instance of this class(meaning that this part does not deal with clustering).

2) org.sbq.batch.mains.SchedulerRunner is meant to be run in multiple instances concurrently in order to simulate a bunch of nodes in a cluster.

### Simulated environment ###

The example is meant to test the following environment: several servers(at least 2 nodes) running in a cluster against RDBMS(hopefully clustered)
which have to perform certain batch tasks periodically and have fail-over, work distribution etc.

### Implemented Jobs ###

<b>CalculateEventMetricsScheduledJob:</b> calculates a number of occurrences for each type of event since last job run and updates the site
statistic entry(which hold metrics of site since its start); Triggered each 5 minutes; saves where it finished(time for which it processed);
 misfire policy 'FireOnceAndProceed', meaning that only one call to the job is needed for any number of misfires;

<b>CalculateOnlineMetricsScheduledJob:</b> calculates total number of users online, number of users jogging, chatting,
dancing and idle for a certain point of time; Triggered each 15 seconds;
uses ScheduledFireTime from Quartz to identify for which point it should calculate the metrics; misfire policy 'IgnoreMisfire',
meaning that all missed executions will be fired as soon as Quartz identifies them(so that the job can catch up);
this job randomly (in ~ 1/3 of cases) throws an exception(TransientException) in order to emulate network issues;

## Addressed Scenarios ##

### Scheduler: No single point of failure ###

<b>Use case:</b> Make sure that if one node goes down, the scheduled tasks are still being executed by the rest of the nodes.

<b>How supported/implemented:</b> Quartz should be running on each machine in a cluster.
Each Quartz should be configured to work with DB-backed JobStore and clustering should be enabled in Quartz properties.
When at least 1 node with Quartz is up, the scheduled tasks will keep being executed(guaranteed by Quartz architecture).

<b>Steps to verify:</b> Run init.sql. Start one instance of ActivityEmulator(optional). Start several instances of SchedulerRunner.
Watch them executing jobs. Kill some of them. See how load is spread between the nodes which are left running.

### Scheduler: Work distribution ###

<b>Use case:</b> Make sure that the tasks are getting distributed among nodes in the cluster.
(This is important because after a certain point one node won't be able to handle all tasks).

<b>How supported/implemented:</b> Quartz with DB JobStore performs work distribution automatically.

<b>Steps to verify:</b> Run init.sql. Start one instance of ActivityEmulator(optional). Start several instances of SchedulerRunner.
Looking at the log file on each instance of SchedulerRunner verify that the tasks are executed on each node(The distribution is not guaranteed to
be even).

### Scheduler: Misfire Support ###

<b>Use case:</b> Make sure that if all nodes go down and then after while at least one is back online,
all of missed job executions(for particular jobs which are sensitive to misfires) are invoked.

<b>How supported/implemented:</b> Quartz with DB JobStore performs detection of misfired jobs automatically upon startup of the first node from
cluster.

<b>Steps to verify:</b> Run init.sql. Start one instance of ActivityEmulator(optional).
Start several instances of SchedulerRunner. Stop all instances of SchedulerRunner. Wait for some time.
Start at least one instance of SchedulerRunner. See how misfired executions are detected and executed.

### Scheduler: Task Recovery ###

<b>Use case:</b> Make sure that if a node executing a certain job goes down, the job is automatically repeated/re-started.

<b>How supported/implemented:</b> This use case is tricky because a server crash is likely to leave the job in unknown state(especially if it
writes data into non-transactional storage like Mongo). For now I assume the simplest use-case where the job just have to be restarted and we can
ignore the fact of possible data collisions. Using requestRecovery feature from Quartz and SYNCHRONOUS executor(which uses Quartz thread for
performing batch processing) we can rely on Quartz in terms of identifying crashed jobs and re-invoking them on a different node(or on the same one
 if it's up and the first one to identify the problem).

<b>NOTE:</b> I think that a more smooth transition for job recovery can be made by storing job state in ExecutionContext which will be picked up by
 Spring Batch when you create a new execution for the same job instance.

<b>Steps to verify:</b> Run init.sql. Start one instance of ActivityEmulator(optional). Start several instances of SchedulerRunner.
Look at the logs and find out which SchedulerRunner is running LongRunningBatchScheduledJob, kill it. See how after a while another job logs the
message that it's picked up the job(it can also be verified in DB by looking at executions table).

### Spring Batch: Retries Support ###

<b>Use case:</b> Retry a job if it fails due to a transient problem(such as a network connectivity issue, or DB being down for a couple of minutes).

<b>How supported/implemented:</b> Spring Batch provides RetryTemplate and RetryOperationsInterceptor for this purpose,
which allow to specify number of retries, back-off policy and types of exceptions which considered retry-able.

<b>Steps to verify:</b> Run init.sql. Start one instance of ActivityEmulator(optional). Start several instances of SchedulerRunner.
In logs you should see "calculateOnlineMetrics() - TRANSIENT EXCEPTION..." which indicates that exception has been thrown but a method of Service
class was retried by RetryOperationsInterceptor.

### General: Monitoring ###

<b>Use case:</b> There should be an easy way to get the following info at any point in time:
list of all jobs which are being executed at the moment, history of all job executions(with parameters and execution results success/failure),
list of all scheduled jobs(e.g. next time a particular job runs etc.).

### General: Execution Management ###

How do I manually re-execute a particular job(with given parameters) if it fails completely(i.e. no luck after N retries)?

### General: Graceful Halt ###

How can I signal to all nodes to stop, so that I can deploy a new version of software, do maintenance etc.?