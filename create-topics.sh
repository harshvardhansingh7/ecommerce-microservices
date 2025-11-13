#!/bin/bash
echo "Waiting for Kafka to start..."
sleep 10

TOPICS=("order-created" "order-confirmed" "order-cancelled" "payment-processed" "order-shipped")

for TOPIC in "${TOPICS[@]}"
do
  kafka-topics --bootstrap-server kafka:9092 --create --topic $TOPIC --if-not-exists --partitions 1 --replication-factor 1
  echo "Topic '$TOPIC' created (if it did not exist)"
done
