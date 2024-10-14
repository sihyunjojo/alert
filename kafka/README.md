# 카프카 토픽 생성
```
docker exec -it kafka-1 /bin/bash

cd /opt/bitnami/kafka/bin

kafka-topics.sh --create --topic hot-area-topic --bootstrap-server 127.0.0.1:29092 --partitions 3 --replication-factor 1  
kafka-topics.sh --create --topic hot-post-topic --bootstrap-server 127.0.0.1:29092 --partitions 3 --replication-factor 1  
kafka-topics.sh --create --topic pending-notification-topic --bootstrap-server 127.0.0.1:29092 --partitions 3 --replication-factor 1  
```
