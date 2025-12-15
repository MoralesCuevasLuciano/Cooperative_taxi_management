package com.pepotec.cooperative_taxi_managment.controllers;

import com.pepotec.cooperative_taxi_managment.models.dto.person.member.account.MemberAccountCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.person.member.account.MemberAccountDTO;
import com.pepotec.cooperative_taxi_managment.services.MemberAccountService;
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
    name = "Member Accounts",
    description = "API para la gestión de cuentas de socios"
)
@RestController
@RequestMapping("/member-accounts")
public class MemberAccountController {

    @Autowired
    private MemberAccountService memberAccountService;

    @Operation(
        summary = "Crear una nueva cuenta de socio",
        description = "Crea una nueva cuenta para un socio existente.",
        tags = {"Member Accounts"},
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Cuenta de socio creada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MemberAccountDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Socio no encontrado"),
            @ApiResponse(responseCode = "409", description = "El socio ya tiene una cuenta asociada")
        }
    )
    @PostMapping("/members/{memberId}")
    public ResponseEntity<MemberAccountDTO> createMemberAccount(
        @PathVariable Long memberId,
        @Valid @RequestBody MemberAccountCreateDTO account
    ) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(memberAccountService.createMemberAccount(memberId, account));
    }

    @Operation(
        summary = "Actualizar una cuenta de socio existente",
        description = "Actualiza los datos de una cuenta de socio por su ID.",
        tags = {"Member Accounts"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Cuenta de socio actualizada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MemberAccountDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Cuenta de socio no encontrada")
        }
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<MemberAccountDTO> updateMemberAccount(
        @PathVariable Long id,
        @Valid @RequestBody MemberAccountDTO account
    ) {
        account.setId(id);
        return ResponseEntity.ok(memberAccountService.updateMemberAccount(account));
    }

    @Operation(
        summary = "Eliminar una cuenta de socio",
        description = "Elimina una cuenta de socio por su ID.",
        tags = {"Member Accounts"},
        responses = {
            @ApiResponse(responseCode = "204", description = "Cuenta de socio eliminada exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID inválido"),
            @ApiResponse(responseCode = "404", description = "Cuenta de socio no encontrada")
        }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMemberAccount(@PathVariable Long id) {
        memberAccountService.deleteMemberAccount(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Obtener una cuenta de socio por ID",
        description = "Busca y retorna una cuenta de socio por su ID.",
        tags = {"Member Accounts"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Cuenta de socio encontrada",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MemberAccountDTO.class)
                )
            ),
            @ApiResponse(responseCode = "404", description = "Cuenta de socio no encontrada")
        }
    )
    @GetMapping("/get/{id}")
    public ResponseEntity<MemberAccountDTO> getMemberAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(memberAccountService.getMemberAccountById(id));
    }

    @Operation(
        summary = "Obtener una cuenta de socio por ID de socio",
        description = "Busca y retorna la cuenta de un socio por el ID del socio.",
        tags = {"Member Accounts"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Cuenta de socio encontrada",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MemberAccountDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "ID de socio inválido"),
            @ApiResponse(responseCode = "404", description = "Cuenta de socio no encontrada")
        }
    )
    @GetMapping("/get/by-member/{memberId}")
    public ResponseEntity<MemberAccountDTO> getMemberAccountByMemberId(@PathVariable Long memberId) {
        return ResponseEntity.ok(memberAccountService.getMemberAccountByMemberId(memberId));
    }

    @Operation(
        summary = "Obtener todas las cuentas de socios",
        description = "Retorna una lista de todas las cuentas de socios.",
        tags = {"Member Accounts"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de cuentas de socios retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MemberAccountDTO.class)
                )
            )
        }
    )
    @GetMapping("/list")
    public ResponseEntity<List<MemberAccountDTO>> getAllMemberAccounts() {
        return ResponseEntity.ok(memberAccountService.getAllMemberAccounts());
    }

    @Operation(
        summary = "Obtener todas las cuentas de socios activas",
        description = "Retorna una lista de todas las cuentas de socios activas.",
        tags = {"Member Accounts"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de cuentas de socios activas retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MemberAccountDTO.class)
                )
            )
        }
    )
    @GetMapping("/list/active")
    public ResponseEntity<List<MemberAccountDTO>> getActiveMemberAccounts() {
        return ResponseEntity.ok(memberAccountService.getActiveMemberAccounts());
    }
}


