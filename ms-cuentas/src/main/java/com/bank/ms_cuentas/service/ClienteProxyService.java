package com.bank.ms_cuentas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.RequestReplyFuture;

import org.springframework.stereotype.Service;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class ClienteProxyService {

    @Autowired
    private ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;

    public String obtenerInfoCliente(String clienteId) {
        try {
            // Preparamos el registro con topic y el mensaje (el clienteId)
            ProducerRecord<String, String> record = new ProducerRecord<>("cliente-request-topic", clienteId);

            // Enviamos y esperamos
            RequestReplyFuture<String, String, String> replyFuture = replyingKafkaTemplate.sendAndReceive(record);

            // Bloquea hasta que llegue el ConsumerRecord (o falle)
            ConsumerRecord<String, String> consumerRecord = replyFuture.get(10, TimeUnit.SECONDS);

            String respuestaJson = consumerRecord.value();
            if (respuestaJson == null) {
                throw new RuntimeException("No se recibió respuesta del microservicio ms-clientes");
            }
            return respuestaJson;

        } catch (TimeoutException e) {
            throw new RuntimeException("Timeout al esperar respuesta de Kafka", e);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener información del cliente", e);
        }
    }
}
