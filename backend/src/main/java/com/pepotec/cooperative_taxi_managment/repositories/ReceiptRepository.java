package com.pepotec.cooperative_taxi_managment.repositories;

import com.pepotec.cooperative_taxi_managment.models.entities.ReceiptEntity;
import com.pepotec.cooperative_taxi_managment.models.enums.ReceiptType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository para recibos físicos otorgados a socios o abonados.
 */
@Repository
public interface ReceiptRepository extends JpaRepository<ReceiptEntity, Long> {
    
    /**
     * Busca recibos por cuenta de socio.
     * @param memberAccountId ID de la cuenta de socio
     * @return Lista de recibos asociados a la cuenta
     */
    List<ReceiptEntity> findByMemberAccountId(Long memberAccountId);
    
    /**
     * Busca recibos activos por cuenta de socio.
     * @param memberAccountId ID de la cuenta de socio
     * @return Lista de recibos activos asociados a la cuenta
     */
    List<ReceiptEntity> findByMemberAccountIdAndActiveTrue(Long memberAccountId);
    
    /**
     * Busca recibos por cuenta de abonado.
     * @param subscriberAccountId ID de la cuenta de abonado
     * @return Lista de recibos asociados a la cuenta
     */
    List<ReceiptEntity> findBySubscriberAccountId(Long subscriberAccountId);
    
    /**
     * Busca recibos activos por cuenta de abonado.
     * @param subscriberAccountId ID de la cuenta de abonado
     * @return Lista de recibos activos asociados a la cuenta
     */
    List<ReceiptEntity> findBySubscriberAccountIdAndActiveTrue(Long subscriberAccountId);
    
    /**
     * Busca un recibo por cuenta de socio y período.
     * @param memberAccountId ID de la cuenta de socio
     * @param period Período en formato "YYYY-MM"
     * @return Recibo encontrado (si existe)
     */
    @Query("SELECT r FROM ReceiptEntity r WHERE r.memberAccount.id = :memberAccountId AND r.yearMonth = :period")
    Optional<ReceiptEntity> findByMemberAccountIdAndPeriod(
        @Param("memberAccountId") Long memberAccountId,
        @Param("period") String period
    );
    
    /**
     * Busca un recibo por cuenta de abonado y período.
     * @param subscriberAccountId ID de la cuenta de abonado
     * @param period Período en formato "YYYY-MM"
     * @return Recibo encontrado (si existe)
     */
    @Query("SELECT r FROM ReceiptEntity r WHERE r.subscriberAccount.id = :subscriberAccountId AND r.yearMonth = :period")
    Optional<ReceiptEntity> findBySubscriberAccountIdAndPeriod(
        @Param("subscriberAccountId") Long subscriberAccountId,
        @Param("period") String period
    );
    
    /**
     * Verifica si existe un recibo por cuenta de socio y período.
     * @param memberAccountId ID de la cuenta de socio
     * @param period Período en formato "YYYY-MM"
     * @return true si existe, false en caso contrario
     */
    @Query("SELECT COUNT(r) > 0 FROM ReceiptEntity r WHERE r.memberAccount.id = :memberAccountId AND r.yearMonth = :period")
    boolean existsByMemberAccountIdAndPeriod(
        @Param("memberAccountId") Long memberAccountId,
        @Param("period") String period
    );
    
    /**
     * Verifica si existe un recibo por cuenta de abonado y período.
     * @param subscriberAccountId ID de la cuenta de abonado
     * @param period Período en formato "YYYY-MM"
     * @return true si existe, false en caso contrario
     */
    @Query("SELECT COUNT(r) > 0 FROM ReceiptEntity r WHERE r.subscriberAccount.id = :subscriberAccountId AND r.yearMonth = :period")
    boolean existsBySubscriberAccountIdAndPeriod(
        @Param("subscriberAccountId") Long subscriberAccountId,
        @Param("period") String period
    );
    
    /**
     * Busca recibos por período.
     * @param period Período en formato "YYYY-MM"
     * @return Lista de recibos del período
     */
    @Query("SELECT r FROM ReceiptEntity r WHERE r.yearMonth = :period")
    List<ReceiptEntity> findByPeriod(@Param("period") String period);
    
    /**
     * Busca recibos por tipo.
     * @param receiptType Tipo de recibo (MEMBER o SUBSCRIBER)
     * @return Lista de recibos del tipo especificado
     */
    List<ReceiptEntity> findByReceiptType(ReceiptType receiptType);
    
    /**
     * Busca recibos por rango de fechas de emisión.
     * @param startDate Fecha inicial
     * @param endDate Fecha final
     * @return Lista de recibos emitidos en el rango
     */
    List<ReceiptEntity> findByIssueDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Busca recibos activos por rango de fechas de emisión.
     * @param startDate Fecha inicial
     * @param endDate Fecha final
     * @return Lista de recibos activos emitidos en el rango
     */
    List<ReceiptEntity> findByIssueDateBetweenAndActiveTrue(LocalDate startDate, LocalDate endDate);
    
    /**
     * Busca un recibo por número de recibo, número de talonario y tipo de recibo.
     * @param receiptNumber Número del recibo
     * @param bookletNumber Número del talonario
     * @param receiptType Tipo de recibo (MEMBER o SUBSCRIBER)
     * @return Recibo encontrado (si existe)
     */
    @Query("SELECT r FROM ReceiptEntity r WHERE r.receiptNumber = :receiptNumber AND r.bookletNumber = :bookletNumber AND r.receiptType = :receiptType")
    Optional<ReceiptEntity> findByReceiptNumberAndBookletNumberAndReceiptType(
        @Param("receiptNumber") Integer receiptNumber,
        @Param("bookletNumber") Integer bookletNumber,
        @Param("receiptType") ReceiptType receiptType
    );
    
    /**
     * Verifica si existe un recibo por número de recibo, número de talonario y tipo de recibo.
     * @param receiptNumber Número del recibo
     * @param bookletNumber Número del talonario
     * @param receiptType Tipo de recibo (MEMBER o SUBSCRIBER)
     * @return true si existe, false en caso contrario
     */
    @Query("SELECT COUNT(r) > 0 FROM ReceiptEntity r WHERE r.receiptNumber = :receiptNumber AND r.bookletNumber = :bookletNumber AND r.receiptType = :receiptType")
    boolean existsByReceiptNumberAndBookletNumberAndReceiptType(
        @Param("receiptNumber") Integer receiptNumber,
        @Param("bookletNumber") Integer bookletNumber,
        @Param("receiptType") ReceiptType receiptType
    );
}

