package com.pepotec.cooperative_taxi_managment.controllers;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.pepotec.cooperative_taxi_managment.services.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.pepotec.cooperative_taxi_managment.models.dto.DriverDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.time.LocalDate;

@ApiResponses(
    value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    }
)
@Tag(
    name = "Drivers",
    description = "API para la gestión de conductores de la cooperativa"
)
@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;

    /**
     * Crear un nuevo conductor
     */
    @Operation(
        summary = "Crear un nuevo conductor",
        description = "Crea un nuevo conductor con la información proporcionada.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Conductor creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflicto - DNI, CUIT o Email ya existen")
        }
    )
    @PostMapping
    public ResponseEntity<DriverDTO> createDriver(@Valid @RequestBody DriverDTO driver) {
        return ResponseEntity.status(HttpStatus.CREATED).body(driverService.createDriver(driver));
    }

    /**
     * Actualizar un conductor existente
     */
    @Operation(
        summary = "Actualizar un conductor existente",
        description = "Actualiza los datos de un conductor existente por su ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Conductor actualizado exitosamente"),
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<DriverDTO> updateDriver(@PathVariable Long id, @Valid @RequestBody DriverDTO driver) {
        return ResponseEntity.ok(driverService.updateDriver(driver));
    }

    /**
     * Dar de baja un conductor
     */
    @Operation(
        summary = "Dar de baja un conductor",
        description = "Da de baja un conductor por su ID.",
        responses = {
            @ApiResponse(responseCode = "204", description = "Conductor dado de baja exitosamente"),
        }
    )

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Dar de baja un conductor con una fecha de baja específica
     */
    @Operation(
        summary = "Dar de baja un conductor con una fecha de baja específica",
        description = "Da de baja un conductor por su ID con una fecha de baja específica.",
        responses = {
            @ApiResponse(responseCode = "204", description = "Conductor dado de baja exitosamente"),
        }
    )
    @DeleteMapping("/{id}/leave-date/{leaveDate}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id, @PathVariable LocalDate leaveDate) {
        driverService.deleteDriver(id, leaveDate);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtener un conductor por ID
     */
    @Operation(
        summary = "Obtener un conductor por ID",
        description = "Obtiene un conductor por su ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Conductor encontrado"),
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getDriverById(@PathVariable Long id) {
        return ResponseEntity.ok(driverService.getDriverById(id));
    }

    /**
     * Obtener un conductor por DNI
     */
    @Operation(
        summary = "Obtener un conductor por DNI",
        description = "Obtiene un conductor por su DNI.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Conductor encontrado"),
        }
    )
    @GetMapping("/dni/{dni}")
    public ResponseEntity<DriverDTO> getDriverByDni(@PathVariable String dni) {
        return ResponseEntity.ok(driverService.getDriverByDni(dni));
    }

    /**
     * Obtener todos los conductores
     */
    @Operation(
        summary = "Obtener todos los conductores",
        description = "Obtiene todos los conductores.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de conductores encontrados"),
        }
    )
    @GetMapping
    public ResponseEntity<List<DriverDTO>> getAllDrivers() {
        return ResponseEntity.ok(driverService.getAllDrivers());
    }

    /**
     * Obtener todos los conductores activos
     */
    @Operation(
        summary = "Obtener todos los conductores activos",
        description = "Obtiene todos los conductores activos.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de conductores activos encontrados"),
        }
    )
    @GetMapping("/active")
    public ResponseEntity<List<DriverDTO>> getAllDriversActive() {
        return ResponseEntity.ok(driverService.getAllDriversActive());
    }

}
