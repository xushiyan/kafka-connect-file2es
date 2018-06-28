# ETL file data to ElasticSearch

## Overview

![overview](./docs/overview.png)

## Run the Pipeline

### Start Services

```bash
docker-compose up -d
```

After services fully start, visit

- [Kafka topics UI](http://localhost:8000)
- [Kafka Connect UI](http://localhost:8001)

### Create Connectors

```bash
# file source connector for topic: analytics.server.logs
curl -X POST http://localhost:8083/connectors \
    -H 'Content-Type:application/json' \
    -H 'Accept:application/json' \
    -d @connectors/connect.source.file.analytics.server.logs.json

# file source connector for topic: analytics.user.events
curl -X POST http://localhost:8083/connectors \
    -H 'Content-Type:application/json' \
    -H 'Accept:application/json' \
    -d @connectors/connect.source.file.analytics.user.events.json

# elasticsearch sink connector for all topics
curl -X POST http://localhost:8083/connectors \
    -H 'Content-Type:application/json' \
    -H 'Accept:application/json' \
    -d @connectors/connect.sink.elasticsearch.json
```

## Generate Data

### Write data to files

```bash
# messages for topic: analytics.server.logs
echo '{"ts":1528697281001,"id":"ac1393","log":"Nam congue pretium ligula, ac susc ."}' >> data/analytics.server.logs.txt
echo '{"ts":1528697281002,"id":"ac1393","log":"Quisque pretium justo massa, ac laet"}' >> data/analytics.server.logs.txt

# messages for topic: analytics.user.events
echo '{"ts":1528591539001,"id":"12bfc4","event":"login"}'  >> data/analytics.user.events.txt
echo '{"ts":1528592539001,"id":"12bfc4","event":"logout"}' >> data/analytics.user.events.txt
echo '{"ts":1528593539001,"id":"12bfc4","event":"login"}'  >> data/analytics.user.events.txt
echo '{"ts":1528594539001,"id":"12bfc4","event":"logout"}' >> data/analytics.user.events.txt
```

### View Data in ElasticSearch

- Open [ElasticSearch UI](http://localhost:1358)
- Configure [ElasticSearch host](http://localhost:9200) and index (e.g., `server.logs-2018-06`) to view data
- Sample data should have been populated in the UI

### Create [kafka-connect-datagen](https://github.com/xushiyan/kafka-connect-datagen) task

```bash
# custom source connector that generates random data
curl -X POST http://localhost:8083/connectors \
    -H 'Content-Type:application/json' \
    -H 'Accept:application/json' \
    -d @connectors/connect.source.datagen.json
```

Based on the [configuration](./connectors/connect.source.datagen.json), the connector task generates 10 messages every 5 seconds and send those to topic `generated.events`.

- To view the data, go to [Kafka topics UI](http://localhost:8000/#/cluster/default/topic/n/generated.events/).
- To pause the data generation, go to [Kafka Connect UI](http://localhost:8001/#/cluster/kafka-connect-1/connector/connect.source.datagen) and click the pause button on top right corner.

## References

- [Apache Kafka Documentation - Connect](https://kafka.apache.org/documentation/#connect)
- [Confluent Docker Configuration](https://docs.confluent.io/current/installation/docker/docs/configuration.html)
- [Confluent Kafka Connect REST API](https://docs.confluent.io/current/connect/references/restapi.html)
- [Confluent ElasticSearch Connector](https://docs.confluent.io/current/connect/connect-elasticsearch/docs/elasticsearch_connector.html)
