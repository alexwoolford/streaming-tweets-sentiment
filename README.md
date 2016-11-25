
# Streaming Tweets, enriched with sentiment, made searchable in Solr

Create the Solr collection and disable multivalue for the fields:

    solr create_collection -c tweets_temp -d data_driven_schema_configs -shards 2 -replicationFactor 2
    
    curl -X POST -H 'Content-type:application/json' --data-binary '{
      "add-field":{
         "name":"createdAt",
         "type":"tdates",
         "multiValued":false,
         "indexed":true,
         "required":true,
         "stored":true }
    }' http://hadoop02.woolford.io:8983/solr/tweets_temp/schema
    
    curl -X POST -H 'Content-type:application/json' --data-binary '{
      "add-field":{
         "name":"userId",
         "type":"long",
         "multiValued":false,
         "indexed":true,
         "required":true,
         "stored":true }
    }' http://hadoop02.woolford.io:8983/solr/tweets_temp/schema
    
    curl -X POST -H 'Content-type:application/json' --data-binary '{
      "add-field":{
         "name":"userScreenName",
         "type":"string",
         "multiValued":false,
         "indexed":true,
         "required":true,
         "stored":true }
    }' http://hadoop02.woolford.io:8983/solr/tweets_temp/schema
    
    curl -X POST -H 'Content-type:application/json' --data-binary '{
      "add-field":{
         "name":"latitude",
         "type":"double",
         "multiValued":false,
         "indexed":true,
         "required":true,
         "stored":true }
    }' http://hadoop02.woolford.io:8983/solr/tweets_temp/schema
    
    curl -X POST -H 'Content-type:application/json' --data-binary '{
      "add-field":{
         "name":"longitude",
         "type":"double",
         "multiValued":false,
         "indexed":true,
         "required":true,
         "stored":true }
    }' http://hadoop02.woolford.io:8983/solr/tweets_temp/schema
    
    curl -X POST -H 'Content-type:application/json' --data-binary '{
      "add-field":{
         "name":"place",
         "type":"string",
         "multiValued":false,
         "indexed":true,
         "required":false,
         "stored":true }
    }' http://hadoop02.woolford.io:8983/solr/tweets_temp/schema
    
    curl -X POST -H 'Content-type:application/json' --data-binary '{
      "add-field":{
         "name":"text",
         "type":"string",
         "multiValued":false,
         "indexed":true,
         "required":true,
         "stored":true }
    }' http://hadoop02.woolford.io:8983/solr/tweets_temp/schema
    
    curl -X POST -H 'Content-type:application/json' --data-binary '{
      "add-field":{
         "name":"lang",
         "type":"string",
         "multiValued":false,
         "indexed":true,
         "required":true,
         "stored":true }
    }' http://hadoop02.woolford.io:8983/solr/tweets_temp/schema
    
    curl -X POST -H 'Content-type:application/json' --data-binary '{
      "add-field":{
         "name":"sentiment",
         "type":"int",
         "multiValued":false,
         "indexed":true,
         "required":true,
         "stored":true }
    }' http://hadoop02.woolford.io:8983/solr/tweets_temp/schema

To make the records show up in Solr, the autoSoftCommit was set to run every second:

    vi /opt/lucidworks-hdpsearch/solr/server/solr/configsets/data_driven_schema_configs/conf/solrconfig.xml

    <autoSoftCommit>
      <maxTime>10000</maxTime>
    </autoSoftCommit>

