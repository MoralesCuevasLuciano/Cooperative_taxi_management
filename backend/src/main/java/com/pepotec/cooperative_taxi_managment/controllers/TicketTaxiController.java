package com.pepotec.cooperative_taxi_managment.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.pepotec.cooperative_taxi_managment.services.TicketTaxiService;
import com.pepotec.cooperative_taxi_managment.models.dto.TicketTaxiDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@ApiResponses(
    value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    }
)
@Tag(
    name = "Ticket Taxi",
    description = "API para la gestión de tickets de taxi de la cooperativa"
)
@RestController
@RequestMapping("/ticket-taxi")
public class TicketTaxiController {

    @Autowired
    private TicketTaxiService ticketTaxiService;

    @Operation(
        summary = "Crear un nuevo ticket de taxi",
        description = "Crea un nuevo ticket de taxi con la información proporcionada.",
        tags = {"Ticket Taxi"},
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Ticket de taxi creado exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TicketTaxiDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
        }
    )
    @PostMapping("/create")
    public ResponseEntity<TicketTaxiDTO> createTicketTaxi(@Valid @RequestBody TicketTaxiDTO ticketTaxi) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ticketTaxiService.createTicketTaxi(ticketTaxi));
    }

    @Operation(
        summary = "Actualizar un ticket de taxi existente",
        description = "Actualiza los datos de un ticket de taxi existente por su ID.",
        tags = {"Ticket Taxi"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Ticket de taxi actualizado exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TicketTaxiDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Ticket de taxi no encontrado")
        }
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<TicketTaxiDTO> updateTicketTaxi(
        @PathVariable Long id,
        @Valid @RequestBody TicketTaxiDTO ticketTaxi
    ) {
        ticketTaxi.setId(id);
        return ResponseEntity.ok(ticketTaxiService.updateTicketTaxi(ticketTaxi));
    }

    @Operation(
        summary = "Eliminar un ticket de taxi",
        description = "Elimina un ticket de taxi por su ID.",
        tags = {"Ticket Taxi"},
        responses = {
            @ApiResponse(responseCode = "204", description = "Ticket de taxi eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID inválido"),
            @ApiResponse(responseCode = "404", description = "Ticket de taxi no encontrado")
        }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTicketTaxi(@PathVariable Long id) {
        ticketTaxiService.deleteTicketTaxi(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Obtener un ticket de taxi por ID",
        description = "Busca y retorna un ticket de taxi por su ID.",
        tags = {"Ticket Taxi"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Ticket de taxi encontrado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TicketTaxiDTO.class)
                )
            ),
            @ApiResponse(responseCode = "404", description = "Ticket de taxi no encontrado")
        }
    )
    @GetMapping("/get/{id}")
    public ResponseEntity<TicketTaxiDTO> getTicketTaxiById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketTaxiService.getTicketTaxiById(id));
    }

    @Operation(
        summary = "Obtener un ticket de taxi por número de ticket",
        description = "Busca y retorna un ticket de taxi por su número de ticket.",
        tags = {"Ticket Taxi"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Ticket de taxi encontrado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TicketTaxiDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Número de ticket inválido"),
            @ApiResponse(responseCode = "404", description = "Ticket de taxi no encontrado")
        }
    )
    @GetMapping("/get/ticket-number/{ticketNumber}")
    public ResponseEntity<TicketTaxiDTO> getTicketTaxiByTicketNumber(@PathVariable String ticketNumber) {
        return ResponseEntity.ok(ticketTaxiService.getTicketTaxiByTicketNumber(ticketNumber));
    }

    @Operation(
        summary = "Obtener todos los tickets de taxi",
        description = "Retorna una lista de todos los tickets de taxi.",
        tags = {"Ticket Taxi"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de tickets de taxi retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TicketTaxiDTO.class)
                )
            )
        }
    )
    @GetMapping("/list")
    public ResponseEntity<List<TicketTaxiDTO>> getAllTicketTaxis() {
        return ResponseEntity.ok(ticketTaxiService.getAllTicketTaxis());
    }

    @Operation(
        summary = "Obtener tickets de taxi por vehículo",
        description = "Retorna una lista de tickets de taxi filtrados por vehículo.",
        tags = {"Ticket Taxi"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de tickets de taxi retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TicketTaxiDTO.class)
                )
            )
        }
    )
    @GetMapping("/get/by-vehicle/{vehicleId}")
    public ResponseEntity<List<TicketTaxiDTO>> getTicketTaxisByVehicle(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(ticketTaxiService.getTicketTaxisByVehicle(vehicleId));
    }

    @Operation(
        summary = "Obtener tickets de taxi por rendición",
        description = "Retorna una lista de tickets de taxi filtrados por rendición.",
        tags = {"Ticket Taxi"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de tickets de taxi retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TicketTaxiDTO.class)
                )
            )
        }
    )
    @GetMapping("/get/by-rendicion/{rendicionId}")
    public ResponseEntity<List<TicketTaxiDTO>> getTicketTaxisByRendicion(@PathVariable Long rendicionId) {
        return ResponseEntity.ok(ticketTaxiService.getTicketTaxisByRendicion(rendicionId));
    }

    @Operation(
        summary = "Obtener tickets de taxi por rango de fechas de inicio",
        description = "Retorna una lista de tickets de taxi filtrados por rango de fechas de inicio.",
        tags = {"Ticket Taxi"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de tickets de taxi retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TicketTaxiDTO.class)
                )
            )
        }
    )
    @GetMapping("/get/by-start-date-range")
    public ResponseEntity<List<TicketTaxiDTO>> getTicketTaxisByStartDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(ticketTaxiService.getTicketTaxisByStartDateRange(startDate, endDate));
    }

    @Operation(
        summary = "Obtener tickets de taxi por rango de fechas de corte",
        description = "Retorna una lista de tickets de taxi filtrados por rango de fechas de corte.",
        tags = {"Ticket Taxi"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de tickets de taxi retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TicketTaxiDTO.class)
                )
            )
        }
    )
    @GetMapping("/get/by-cut-date-range")
    public ResponseEntity<List<TicketTaxiDTO>> getTicketTaxisByCutDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(ticketTaxiService.getTicketTaxisByCutDateRange(startDate, endDate));
    }

    @Operation(
        summary = "Obtener tickets de taxi por vehículo y rango de fechas de inicio",
        description = "Retorna una lista de tickets de taxi filtrados por vehículo y rango de fechas de inicio.",
        tags = {"Ticket Taxi"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de tickets de taxi retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TicketTaxiDTO.class)
                )
            )
        }
    )
    @GetMapping("/get/by-vehicle/{vehicleId}/start-date-range")
    public ResponseEntity<List<TicketTaxiDTO>> getTicketTaxisByVehicleAndStartDateRange(
        @PathVariable Long vehicleId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(ticketTaxiService.getTicketTaxisByVehicleAndStartDateRange(vehicleId, startDate, endDate));
    }

    @Operation(
        summary = "Obtener tickets de taxi por vehículo y rango de fechas de corte",
        description = "Retorna una lista de tickets de taxi filtrados por vehículo y rango de fechas de corte.",
        tags = {"Ticket Taxi"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de tickets de taxi retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TicketTaxiDTO.class)
                )
            )
        }
    )
    @GetMapping("/get/by-vehicle/{vehicleId}/cut-date-range")
    public ResponseEntity<List<TicketTaxiDTO>> getTicketTaxisByVehicleAndCutDateRange(
        @PathVariable Long vehicleId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(ticketTaxiService.getTicketTaxisByVehicleAndCutDateRange(vehicleId, startDate, endDate));
    }

    @Operation(
        summary = "Obtener tickets de taxi por rendición y rango de fechas de inicio",
        description = "Retorna una lista de tickets de taxi filtrados por rendición y rango de fechas de inicio.",
        tags = {"Ticket Taxi"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de tickets de taxi retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TicketTaxiDTO.class)
                )
            )
        }
    )
    @GetMapping("/get/by-rendicion/{rendicionId}/start-date-range")
    public ResponseEntity<List<TicketTaxiDTO>> getTicketTaxisByRendicionAndStartDateRange(
        @PathVariable Long rendicionId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(ticketTaxiService.getTicketTaxisByRendicionAndStartDateRange(rendicionId, startDate, endDate));
    }

    @Operation(
        summary = "Obtener tickets de taxi por rendición y rango de fechas de corte",
        description = "Retorna una lista de tickets de taxi filtrados por rendición y rango de fechas de corte.",
        tags = {"Ticket Taxi"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de tickets de taxi retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TicketTaxiDTO.class)
                )
            )
        }
    )
    @GetMapping("/get/by-rendicion/{rendicionId}/cut-date-range")
    public ResponseEntity<List<TicketTaxiDTO>> getTicketTaxisByRendicionAndCutDateRange(
        @PathVariable Long rendicionId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(ticketTaxiService.getTicketTaxisByRendicionAndCutDateRange(rendicionId, startDate, endDate));
    }
}

