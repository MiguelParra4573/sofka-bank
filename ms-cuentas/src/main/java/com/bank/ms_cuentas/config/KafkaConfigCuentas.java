package com.bank.ms_cuentas.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.core.*;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfigCuentas {

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                org.apache.kafka.common.serialization.StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                org.apache.kafka.common.serialization.StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                org.apache.kafka.common.serialization.StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                org.apache.kafka.common.serialization.StringDeserializer.class);
        // Para evitar conflictos, usa un group id distinto si quieres aislarlo:
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "grupo-ms-cuentas-reply");
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, String> replyContainer(
            ConcurrentKafkaListenerContainerFactory<String, String> factory) {

        // Este container “escucha” en el topic de RESPUESTA
        ConcurrentMessageListenerContainer<String, String> container =
                factory.createContainer("cliente-response-topic");

        container.getContainerProperties().setGroupId("grupo-ms-cuentas-reply");
        // No iniciar automáticamente, lo controlará el template
        container.setAutoStartup(true);
        return container;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate(
            ProducerFactory<String, String> pf,
            ConcurrentMessageListenerContainer<String, String> repliesContainer) {
        return new ReplyingKafkaTemplate<>(pf, repliesContainer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
