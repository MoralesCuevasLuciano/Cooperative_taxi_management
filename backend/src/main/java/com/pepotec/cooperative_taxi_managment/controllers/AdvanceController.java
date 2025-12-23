package com.pepotec.cooperative_taxi_managment.controllers;

import com.pepotec.cooperative_taxi_managment.models.dto.advance.AdvanceCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.advance.AdvanceDTO;
import com.pepotec.cooperative_taxi_managment.services.AdvanceService;
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
    name = "Advances",
    description = "API para la gestión de adelantos de sueldo (vales)"
)
@RestController
@RequestMapping("/advances")
public class AdvanceController {

    @Autowired
    private AdvanceService advanceService;

    @Operation(
        summary = "Crear un nuevo adelanto",
        description = "Crea un nuevo adelanto de sueldo para un socio (no chofer).",
        tags = {"Advances"},
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Adelanto creado exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AdvanceDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o el socio es chofer"),
            @ApiResponse(responseCode = "404", description = "Cuenta de socio no encontrada")
        }
    )
    @PostMapping("/create")
    public ResponseEntity<AdvanceDTO> create(@Valid @RequestBody AdvanceCreateDTO dto) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(advanceService.create(dto));
    }

    @Operation(
        summary = "Obtener un adelanto por ID",
        description = "Obtiene los detalles de un adelanto específico por su ID.",
        tags = {"Advances"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Adelanto encontrado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AdvanceDTO.class)
                )
            ),
            @ApiResponse(responseCode = "404", description = "Adelanto no encontrado")
        }
    )
    @GetMapping("/get/{id}")
    public ResponseEntity<AdvanceDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(advanceService.getById(id));
    }

    @Operation(
        summary = "Listar todos los adelantos",
        description = "Obtiene una lista de todos los adelantos registrados.",
        tags = {"Advances"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de adelantos",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AdvanceDTO.class)
                )
            )
        }
    )
    @GetMapping("/list")
    public ResponseEntity<List<AdvanceDTO>> listAll() {
        return ResponseEntity.ok(advanceService.listAll());
    }

    @Operation(
        summary = "Listar adelantos por cuenta de socio",
        description = "Obtiene una lista de adelantos asociados a una cuenta de socio específica.",
        tags = {"Advances"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de adelantos del socio",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AdvanceDTO.class)
                )
            )
        }
    )
    @GetMapping("/by-account/{memberAccountId}")
    public ResponseEntity<List<AdvanceDTO>> listByAccount(@PathVariable Long memberAccountId) {
        return ResponseEntity.ok(advanceService.listByAccount(memberAccountId));
    }

    @Operation(
        summary = "Listar adelantos por rango de fechas",
        description = "Obtiene una lista de adelantos dentro de un rango de fechas específico.",
        tags = {"Advances"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de adelantos en el rango de fechas",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AdvanceDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Fechas inválidas")
        }
    )
    @GetMapping("/by-date-range")
    public ResponseEntity<List<AdvanceDTO>> listByDateRange(
        @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate startDate,
        @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate endDate
    ) {
        return ResponseEntity.ok(advanceService.listByDateRange(startDate, endDate));
    }
}

