package com.pepotec.cooperative_taxi_managment.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.pepotec.cooperative_taxi_managment.services.ModelService;
import com.pepotec.cooperative_taxi_managment.models.dto.model.ModelDTO;
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
import java.util.List;
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
    name = "Models",
    description = "API para la gestión de modelos de vehículos"
)
@RestController
@RequestMapping("/models")
public class ModelController {

    @Autowired
    private ModelService modelService;

    @Operation(
        summary = "Crear un nuevo modelo",
        description = "Crea un nuevo modelo con la información proporcionada.",
        tags = {"Models"},
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Modelo creado exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ModelDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflicto - Ya existe un modelo con el mismo nombre para la marca")
        }
    )
    @PostMapping("/create")
    public ResponseEntity<ModelDTO> createModel(@Valid @RequestBody ModelDTO model) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(modelService.createModel(model));
    }

    @Operation(
        summary = "Actualizar un modelo existente",
        description = "Actualiza los datos de un modelo existente por su ID.",
        tags = {"Models"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Modelo actualizado exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ModelDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Modelo no encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflicto - Ya existe un modelo con el mismo nombre para la marca")
        }
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<ModelDTO> updateModel(
        @PathVariable Long id,
        @Valid @RequestBody ModelDTO model
    ) {
        model.setId(id);
        return ResponseEntity.ok(modelService.updateModel(model));
    }

    @Operation(
        summary = "Eliminar un modelo",
        description = "Elimina un modelo por su ID.",
        tags = {"Models"},
        responses = {
            @ApiResponse(responseCode = "204", description = "Modelo eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID inválido"),
            @ApiResponse(responseCode = "404", description = "Modelo no encontrado")
        }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteModel(@PathVariable Long id) {
        modelService.deleteModel(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Obtener un modelo por ID",
        description = "Busca y retorna un modelo por su ID.",
        tags = {"Models"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Modelo encontrado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ModelDTO.class)
                )
            ),
            @ApiResponse(responseCode = "404", description = "Modelo no encontrado")
        }
    )
    @GetMapping("/get/{id}")
    public ResponseEntity<ModelDTO> getModelById(@PathVariable Long id) {
        return ResponseEntity.ok(modelService.getModelById(id));
    }

    @Operation(
        summary = "Obtener todos los modelos",
        description = "Retorna una lista de todos los modelos.",
        tags = {"Models"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de modelos retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ModelDTO.class)
                )
            )
        }
    )
    @GetMapping("/list")
    public ResponseEntity<List<ModelDTO>> getAllModels() {
        return ResponseEntity.ok(modelService.getAllModels());
    }

    @Operation(
        summary = "Obtener modelos por marca",
        description = "Retorna una lista de modelos filtrados por marca.",
        tags = {"Models"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de modelos retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ModelDTO.class)
                )
            )
        }
    )
    @GetMapping("/get/by-brand/{brandId}")
    public ResponseEntity<List<ModelDTO>> getModelsByBrand(@PathVariable Long brandId) {
        return ResponseEntity.ok(modelService.getModelsByBrand(brandId));
    }
}

