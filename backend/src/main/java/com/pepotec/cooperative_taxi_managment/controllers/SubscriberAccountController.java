package com.pepotec.cooperative_taxi_managment.controllers;

import com.pepotec.cooperative_taxi_managment.models.dto.person.subscriber.account.SubscriberAccountCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.person.subscriber.account.SubscriberAccountDTO;
import com.pepotec.cooperative_taxi_managment.services.SubscriberAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    name = "Subscriber Accounts",
    description = "API para la gestión de cuentas de abonados"
)
@RestController
@RequestMapping("/subscriber-accounts")
public class SubscriberAccountController {

    @Autowired
    private SubscriberAccountService subscriberAccountService;

    @Operation(
        summary = "Crear una nueva cuenta de abonado",
        description = "Crea una nueva cuenta para un abonado existente.",
        tags = {"Subscriber Accounts"},
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Cuenta de abonado creada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SubscriberAccountDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Abonado no encontrado"),
            @ApiResponse(responseCode = "409", description = "El abonado ya tiene una cuenta asociada")
        }
    )
    @PostMapping("/subscribers/{subscriberId}")
    public ResponseEntity<SubscriberAccountDTO> createSubscriberAccount(
        @PathVariable Long subscriberId,
        @Valid @RequestBody SubscriberAccountCreateDTO account
    ) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(subscriberAccountService.createSubscriberAccount(subscriberId, account));
    }

    @Operation(
        summary = "Actualizar una cuenta de abonado existente",
        description = "Actualiza los datos de una cuenta de abonado por su ID.",
        tags = {"Subscriber Accounts"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Cuenta de abonado actualizada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SubscriberAccountDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Cuenta de abonado no encontrada")
        }
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<SubscriberAccountDTO> updateSubscriberAccount(
        @PathVariable Long id,
        @Valid @RequestBody SubscriberAccountDTO account
    ) {
        account.setId(id);
        return ResponseEntity.ok(subscriberAccountService.updateSubscriberAccount(account));
    }

    @Operation(
        summary = "Eliminar una cuenta de abonado",
        description = "Elimina una cuenta de abonado por su ID.",
        tags = {"Subscriber Accounts"},
        responses = {
            @ApiResponse(responseCode = "204", description = "Cuenta de abonado eliminada exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID inválido"),
            @ApiResponse(responseCode = "404", description = "Cuenta de abonado no encontrada")
        }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSubscriberAccount(@PathVariable Long id) {
        subscriberAccountService.deleteSubscriberAccount(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Obtener una cuenta de abonado por ID",
        description = "Busca y retorna una cuenta de abonado por su ID.",
        tags = {"Subscriber Accounts"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Cuenta de abonado encontrada",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SubscriberAccountDTO.class)
                )
            ),
            @ApiResponse(responseCode = "404", description = "Cuenta de abonado no encontrada")
        }
    )
    @GetMapping("/get/{id}")
    public ResponseEntity<SubscriberAccountDTO> getSubscriberAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(subscriberAccountService.getSubscriberAccountById(id));
    }

    @Operation(
        summary = "Obtener una cuenta de abonado por ID de abonado",
        description = "Busca y retorna la cuenta de un abonado por el ID del abonado.",
        tags = {"Subscriber Accounts"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Cuenta de abonado encontrada",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SubscriberAccountDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "ID de abonado inválido"),
            @ApiResponse(responseCode = "404", description = "Cuenta de abonado no encontrada")
        }
    )
    @GetMapping("/get/by-subscriber/{subscriberId}")
    public ResponseEntity<SubscriberAccountDTO> getSubscriberAccountBySubscriberId(@PathVariable Long subscriberId) {
        return ResponseEntity.ok(subscriberAccountService.getSubscriberAccountBySubscriberId(subscriberId));
    }

    @Operation(
        summary = "Obtener todas las cuentas de abonados",
        description = "Retorna una lista de todas las cuentas de abonados.",
        tags = {"Subscriber Accounts"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de cuentas de abonados retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SubscriberAccountDTO.class)
                )
            )
        }
    )
    @GetMapping("/list")
    public ResponseEntity<List<SubscriberAccountDTO>> getAllSubscriberAccounts() {
        return ResponseEntity.ok(subscriberAccountService.getAllSubscriberAccounts());
    }

    @Operation(
        summary = "Obtener todas las cuentas de abonados activas",
        description = "Retorna una lista de todas las cuentas de abonados activas.",
        tags = {"Subscriber Accounts"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de cuentas de abonados activas retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SubscriberAccountDTO.class)
                )
            )
        }
    )
    @GetMapping("/list/active")
    public ResponseEntity<List<SubscriberAccountDTO>> getActiveSubscriberAccounts() {
        return ResponseEntity.ok(subscriberAccountService.getActiveSubscriberAccounts());
    }
}


