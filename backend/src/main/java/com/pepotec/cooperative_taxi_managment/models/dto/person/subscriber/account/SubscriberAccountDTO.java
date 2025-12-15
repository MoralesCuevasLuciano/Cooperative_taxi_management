package com.pepotec.cooperative_taxi_managment.models.dto.person.subscriber.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pepotec.cooperative_taxi_managment.models.dto.person.subscriber.SubscriberDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriberAccountDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "The subscriber id cannot be null")
    private Long subscriberId;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SubscriberDTO subscriber;

    @NotNull(message = "The balance cannot be null")
    private Double balance;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate lastModified;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean active;
}


