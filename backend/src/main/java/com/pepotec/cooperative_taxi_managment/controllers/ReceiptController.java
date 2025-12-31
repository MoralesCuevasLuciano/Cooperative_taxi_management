package com.pepotec.cooperative_taxi_managment.controllers;

import com.pepotec.cooperative_taxi_managment.models.dto.receipt.ReceiptCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.receipt.ReceiptDTO;
import com.pepotec.cooperative_taxi_managment.models.enums.ReceiptType;
import com.pepotec.cooperative_taxi_managment.services.ReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
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
    name = "Receipts",
    description = "API para la gestión de recibos físicos de cuotas mensuales"
)
@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @Operation(
        summary = "Crear un nuevo recibo",
        description = "Crea un nuevo recibo físico para un socio o abonado por el pago de una cuota mensual.",
        tags = {"Receipts"},
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Recibo creado exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ReceiptDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o recibo duplicado"),
            @ApiResponse(responseCode = "404", description = "Cuenta de socio o abonado no encontrada"),
            @ApiResponse(responseCode = "409", description = "Ya existe un recibo para esta cuenta y período")
        }
    )
    @PostMapping("/create")
    public ResponseEntity<ReceiptDTO> create(@Valid @RequestBody ReceiptCreateDTO dto) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(receiptService.create(dto));
    }

    @Operation(
        summary = "Obtener un recibo por ID",
        description = "Obtiene los detalles de un recibo específico por su ID.",
        tags = {"Receipts"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Recibo encontrado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ReceiptDTO.class)
                )
            ),
            @ApiResponse(responseCode = "404", description = "Recibo no encontrado")
        }
    )
    @GetMapping("/get/{id}")
    public ResponseEntity<ReceiptDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(receiptService.getById(id));
    }

    @Operation(
        summary = "Listar todos los recibos",
        description = "Obtiene una lista de todos los recibos registrados.",
        tags = {"Receipts"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de recibos",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ReceiptDTO.class)
                )
            )
        }
    )
    @GetMapping("/list")
    public ResponseEntity<List<ReceiptDTO>> listAll() {
        return ResponseEntity.ok(receiptService.listAll());
    }

    @Operation(
        summary = "Listar recibos por cuenta de socio",
        description = "Obtiene una lista de todos los recibos asociados a una cuenta de socio específica.",
        tags = {"Receipts"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de recibos del socio",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ReceiptDTO.class)
                )
            )
        }
    )
    @GetMapping("/list/by-member-account/{memberAccountId}")
    public ResponseEntity<List<ReceiptDTO>> listByMemberAccount(@PathVariable Long memberAccountId) {
        return ResponseEntity.ok(receiptService.listByMemberAccount(memberAccountId));
    }

    @Operation(
        summary = "Listar recibos por cuenta de abonado",
        description = "Obtiene una lista de todos los recibos asociados a una cuenta de abonado específica.",
        tags = {"Receipts"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de recibos del abonado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ReceiptDTO.class)
                )
            )
        }
    )
    @GetMapping("/list/by-subscriber-account/{subscriberAccountId}")
    public ResponseEntity<List<ReceiptDTO>> listBySubscriberAccount(@PathVariable Long subscriberAccountId) {
        return ResponseEntity.ok(receiptService.listBySubscriberAccount(subscriberAccountId));
    }

    @Operation(
        summary = "Listar recibos por período",
        description = "Obtiene una lista de todos los recibos emitidos en un período específico (año-mes).",
        tags = {"Receipts"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de recibos del período",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ReceiptDTO.class)
                )
            )
        }
    )
    @GetMapping("/list/by-period")
    public ResponseEntity<List<ReceiptDTO>> listByPeriod(
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth
    ) {
        return ResponseEntity.ok(receiptService.listByPeriod(yearMonth));
    }

    @Operation(
        summary = "Listar recibos por tipo",
        description = "Obtiene una lista de todos los recibos de un tipo específico (MEMBER o SUBSCRIBER).",
        tags = {"Receipts"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de recibos del tipo especificado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ReceiptDTO.class)
                )
            )
        }
    )
    @GetMapping("/list/by-type/{receiptType}")
    public ResponseEntity<List<ReceiptDTO>> listByReceiptType(@PathVariable ReceiptType receiptType) {
        return ResponseEntity.ok(receiptService.listByReceiptType(receiptType));
    }

    @Operation(
        summary = "Listar recibos por rango de fechas de emisión",
        description = "Obtiene una lista de todos los recibos emitidos entre dos fechas específicas.",
        tags = {"Receipts"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de recibos en el rango de fechas",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ReceiptDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Fechas inválidas o rango incorrecto")
        }
    )
    @GetMapping("/list/by-issue-date-range")
    public ResponseEntity<List<ReceiptDTO>> listByIssueDateRange(
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {
        return ResponseEntity.ok(receiptService.listByIssueDateRange(startDate, endDate));
    }

    @Operation(
        summary = "Actualizar un recibo existente",
        description = "Actualiza los datos de un recibo existente por su ID.",
        tags = {"Receipts"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Recibo actualizado exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ReceiptDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o recibo duplicado"),
            @ApiResponse(responseCode = "404", description = "Recibo no encontrado"),
            @ApiResponse(responseCode = "409", description = "Ya existe un recibo para esta cuenta y período")
        }
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<ReceiptDTO> update(
        @PathVariable Long id,
        @Valid @RequestBody ReceiptCreateDTO dto
    ) {
        return ResponseEntity.ok(receiptService.update(id, dto));
    }

    @Operation(
        summary = "Eliminar un recibo",
        description = "Elimina (soft delete) un recibo por su ID.",
        tags = {"Receipts"},
        responses = {
            @ApiResponse(responseCode = "204", description = "Recibo eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Recibo no encontrado")
        }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        receiptService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

