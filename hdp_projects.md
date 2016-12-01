
# Hortonworks supported Apache projects

## data management

### Hadoop and YARN
The core components of [Hadoop](http://hadoop.apache.org/) are [HDFS](http://hadoop.apache.org/docs/r1.2.1/hdfs_design.html) ("Hadoop Distributed File System"), and 
[YARN](http://hadoop.apache.org/docs/stable2/hadoop-yarn/hadoop-yarn-site/YARN.html) ("Yet Another Resource Negotiator").

HDFS is a fault-tolerant and scalable file system. YARN is the "cluster operating system" that enables diverse workloads to run concurrently on the same cluster hardware.

## data access

### Pig

[Pig](https://pig.apache.org/) is a platform for analyzing large datasets that's easy to program, e.g. a MapReduce job that's a few hundred lines of Java might be 10 or 20 lines of 'Pig Latin'. The language is extensible through user-defined functions.

### Hive

[Hive](https://hive.apache.org/index.html) is a very popular SQL-on-Hadoop solution. Traditionally, Hive was seen as technology that's suited to batch processing. Some recent optimizations, called [LLAP](https://cwiki.apache.org/confluence/display/Hive/LLAP), have improved performance to the point where we're able to get sub one-second response times from queries against large tables (billions of records).

### Tez

[Tez](https://tez.apache.org/) is a application framework that has enabled data to be processed more efficiently than MapReduce. Under the hood, it's used by Hive and Pig.

### Solr

[Solr](http://lucene.apache.org/solr/) is used for scalable text search. Documents are indexed and can be queried via REST.

### Spark



### Zeppelin

### Slider

### HBase

### Phoenix

### Accumulo

### Storm

## governance and integration

### Falcon

### Atlas

### Sqoop

### Flume

### Kafka

## operations

### Ambari

### Cloudbreak

### Zookeeper

### Oozie

## security

### Knox

### Ranger

