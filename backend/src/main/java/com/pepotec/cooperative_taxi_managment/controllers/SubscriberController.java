package com.pepotec.cooperative_taxi_managment.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.pepotec.cooperative_taxi_managment.services.SubscriberService;
import com.pepotec.cooperative_taxi_managment.models.dto.SubscriberDTO;
import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.List;

@ApiResponses(
    value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    }
)
@Tag(
    name = "Subscribers",
    description = "API para la gestión de suscriptores de la cooperativa"
)
@RestController
@RequestMapping("/subscribers")
public class SubscriberController {

    @Autowired
    private SubscriberService subscriberService;

    /**
     * Crear un nuevo suscriptor
     */
    @Operation(
        summary = "Crear un nuevo suscriptor",
        description = "Crea un nuevo suscriptor con la información proporcionada.",
        tags = {"Subscribers"},
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Suscriptor creado exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SubscriberDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflicto - DNI, CUIT o Email ya existen")
        }
    )
    @PostMapping("/create")
    public ResponseEntity<SubscriberDTO> createSubscriber(@Valid @RequestBody SubscriberDTO subscriber) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(subscriberService.createSubscriber(subscriber));
    }

    /**
     * Actualizar un suscriptor existente
     */
    @Operation(
        summary = "Actualizar un suscriptor existente",
        description = "Actualiza los datos de un suscriptor existente por su ID.",
        tags = {"Subscribers"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Suscriptor actualizado exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SubscriberDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Suscriptor no encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflicto - DNI, CUIT o Email ya existen")
        }
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<SubscriberDTO> updateSubscriber(
        @PathVariable Long id,
        @Valid @RequestBody SubscriberDTO subscriber
    ) {
        if (subscriber.getId() != null && !subscriber.getId().equals(id)) {
            throw new InvalidDataException(
                String.format("El ID del body (%d) no coincide con el ID de la URL (%d)",
                    subscriber.getId(), id)
            );
        }
        subscriber.setId(id);
        return ResponseEntity.ok(subscriberService.updateSubscriber(subscriber));
    }

    /**
     * Eliminar un suscriptor
     */
    @Operation(
        summary = "Eliminar un suscriptor",
        description = "Elimina un suscriptor por su ID.",
        tags = {"Subscribers"},
        responses = {
            @ApiResponse(responseCode = "204", description = "Suscriptor eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID inválido"),
            @ApiResponse(responseCode = "404", description = "Suscriptor no encontrado")
        }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSubscriber(@PathVariable Long id) {
        subscriberService.deleteSubscriber(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtener un suscriptor por ID
     */
    @Operation(
        summary = "Obtener un suscriptor por ID",
        description = "Busca y retorna un suscriptor por su ID.",
        tags = {"Subscribers"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Suscriptor encontrado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SubscriberDTO.class)
                )
            ),
            @ApiResponse(responseCode = "404", description = "Suscriptor no encontrado")
        }
    )
    @GetMapping("/get/{id}")
    public ResponseEntity<SubscriberDTO> getSubscriberById(@PathVariable Long id) {
        return ResponseEntity.ok(subscriberService.getSubscriberById(id));
    }

    /**
     * Obtener un suscriptor por DNI
     */
    @Operation(
        summary = "Obtener un suscriptor por DNI",
        description = "Busca y retorna un suscriptor por su DNI.",
        tags = {"Subscribers"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Suscriptor encontrado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SubscriberDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "DNI inválido"),
            @ApiResponse(responseCode = "404", description = "Suscriptor no encontrado")
        }
    )
    @GetMapping("/get/dni/{dni}")
    public ResponseEntity<SubscriberDTO> getSubscriberByDni(@PathVariable String dni) {
        return ResponseEntity.ok(subscriberService.getSubscriberByDni(dni));
    }

    /**
     * Obtener todos los suscriptores
     */
    @Operation(
        summary = "Obtener todos los suscriptores",
        description = "Retorna una lista de todos los suscriptores.",
        tags = {"Subscribers"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de suscriptores retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SubscriberDTO.class)
                )
            )
        }
    )
    @GetMapping("/list")
    public ResponseEntity<List<SubscriberDTO>> getAllSubscribers() {
        return ResponseEntity.ok(subscriberService.getAllSubscribers());
    }
    /**
     * Obtener todos los suscriptores activos
     */
    @Operation(
        summary = "Obtener todos los suscriptores activos",
        description = "Retorna una lista de todos los suscriptores activos.",
        tags = {"Subscribers"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de suscriptores activos retornada exitosamente"),
        }
    )
    @GetMapping("/get/active")
    public ResponseEntity<List<SubscriberDTO>> getAllSubscribersActive() {
        return ResponseEntity.ok(subscriberService.getAllSubscribersActive());
    }
}
