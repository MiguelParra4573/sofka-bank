package com.bank.ms_clientes.controller;

import com.bank.ms_clientes.exception.ClienteConflictException;
import com.bank.ms_clientes.exception.ClienteNotFoundException;
import com.bank.ms_clientes.model.Cliente;
import com.bank.ms_clientes.service.ClienteServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteServiceImpl service;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping(value = "/test")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> helloWorld() {
        return new ResponseEntity<String>("Correcto!!!", HttpStatus.OK);
    }

    @KafkaListener(topics = "cliente-request-topic", groupId = "grupo-ms-clientes")
    public void recibirClienteId(Message<String> message) {
        String clienteId = message.getPayload();
        MessageHeaders headers = message.getHeaders();

        System.out.println("📥 Recibida solicitud de clienteId: " + clienteId);

        try {
            UUID id = UUID.fromString(clienteId);
            Cliente cliente = service.obtenerClientePorId(id);

            if (cliente != null) {
                String mensaje = "{\"id\":\"" + cliente.getClienteId() + "\",\"nombre\":\"" + cliente.getNombre() + "\"}";
                System.out.println("✅ Respuesta a enviar: " + mensaje);

                // Enviar respuesta con correlationId correcto
                Message<String> responseMessage = MessageBuilder
                        .withPayload(mensaje)
                        .setHeader(KafkaHeaders.TOPIC, "cliente-response-topic")
                        .setHeader(KafkaHeaders.CORRELATION_ID, headers.get(KafkaHeaders.CORRELATION_ID))
                        .build();

                kafkaTemplate.send(responseMessage);
                System.out.println("✅ Respuesta enviada a cliente-response-topic: " + mensaje);
            } else {
                System.out.println("❌ Cliente no encontrado para ID: " + clienteId);
            }
        } catch (Exception e) {
            System.err.println("❌ Error procesando solicitud para clienteId: " + clienteId);
            e.printStackTrace();
        }
    }

    @PostMapping
    public ResponseEntity<Object> crearCliente(@Valid @RequestBody Cliente cliente, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            Cliente clienteCreado = service.crearCliente(cliente);
            return ResponseEntity.ok("¡Bienvenido! Nos alegra que seas parte de Sofka-Bank");
        } catch (ClienteConflictException e) {
            return new ResponseEntity<>("El cliente ya se encuentra registrado!", HttpStatus.CONFLICT);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerCliente(@PathVariable UUID id) {
        try {
            Cliente cliente = service.obtenerClientePorId(id);
            return new ResponseEntity<>(cliente, HttpStatus.OK);
        } catch (ClienteNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarParcialmenteCliente(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        try {
            Cliente clienteActualizado = service.actualizarClienteParcial(id, updates);
            return ResponseEntity.ok(clienteActualizado);
        } catch (ClienteNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar el cliente");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable UUID id) {
        try {
            boolean eliminado = service.eliminarCliente(id);
            if (eliminado) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ClienteNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<?> obtenerClientes(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<Cliente> clientes = service.obtenerTodosLosClientes(page, size);
        Map<String, Object> response = new HashMap<>();
        response.put("clientes", clientes.getContent());
        response.put("currentPage", clientes.getNumber());
        response.put("totalItems", clientes.getTotalElements());
        response.put("totalPages", clientes.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
