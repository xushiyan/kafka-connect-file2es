# ETL file data to ElasticSearch

## Overview

![overview](./docs/overview.png)

## Run the Pipeline

### Start Services

```bash
docker-compose up -d
```

Then visit

- [Kafka topics ui](http://localhost:8000)
- [Kafka Connect ui](http://localhost:8001)
- [ElasticSearch ui](http://localhost:1358)

### Create Connectors

- File connectors for `topic1` and `topic2`

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

- Check out [documentation](https://docs.confluent.io/current/connect/connect-elasticsearch/docs/configuration_options.html) for more configurations
