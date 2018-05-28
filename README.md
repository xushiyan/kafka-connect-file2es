# ETL file data to ElasticSearch

## Overview

![overview](./docs/overview.png)

## Run the Pipeline

### Start Services

```bash
docker-compose up -d
```

Then visit

- [Kafka topics UI](http://localhost:8000)
- [Kafka Connect UI](http://localhost:8001)
- [ElasticSearch UI](http://localhost:1358)

### Create Connectors

- File source connectors for `topic1` and `topic2`

```bash
curl -X POST http://localhost:8083/connectors \
    -H 'Content-Type:application/json' \
    -H 'Accept:application/json' \
    -d @connectors/filesource-topic1.json
```

```bash
curl -X POST http://localhost:8083/connectors \
    -H 'Content-Type:application/json' \
    -H 'Accept:application/json' \
    -d @connectors/filesource-topic2.json
```

- ElasticSearch sink connector

```bash
curl -X POST http://localhost:8083/connectors \
    -H 'Content-Type:application/json' \
    -H 'Accept:application/json' \
    -d @connectors/elasticsearchsink.json
```

## References

- [Confluent Docker Configuration](https://docs.confluent.io/current/installation/docker/docs/configuration.html)
- [Confluent Kafka Connect REST API](https://docs.confluent.io/current/connect/references/restapi.html)
- [Confluent ElasticSearch Connector](https://docs.confluent.io/current/connect/connect-elasticsearch/docs/elasticsearch_connector.html)
