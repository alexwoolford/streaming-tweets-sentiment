
# Streaming Tweets, enriched with sentiment, made searchable in Solr

This is a Spark streaming example that filters English tweets, from the Garden Hose, with a known latitude/longititude. The tweets have been enriched with sentiment using Stanford's NLP package, and persisted to Solr so they can be searched.

### Solr

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

### Example document

    {
      "responseHeader":{
        "status":0,
        "QTime":7,
        "params":{
          "q":"(sentiment:3) AND (userScreenName:TAR_1414)",
          "indent":"true",
          "wt":"json"}},
      "response":{"numFound":1,"start":0,"maxScore":4.947212,"docs":[
          {
            "id":"802212536150093824",
            "createdAt":"2016-11-25T18:09:07Z",
            "userId":77349489,
            "userScreenName":"TAR_1414",
            "latitude":30.07883536,
            "longitude":-95.41952513,
            "text":"Going shopping in Old Town Spring with my family! @ Old Town Spring https://t.co/TRV4dAxQzu",
            "lang":"en",
            "sentiment":3,
            "_indexed_at_tdt":"2016-11-25T18:09:09.01Z",
            "_version_":1551994557863821312}]
      }}