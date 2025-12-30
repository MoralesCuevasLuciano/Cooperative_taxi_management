-- Script para crear la tabla payroll_settlements manualmente
-- Ejecutar este script en MySQL antes de reiniciar la aplicación

DROP TABLE IF EXISTS `payroll_settlements`;

CREATE TABLE `payroll_settlements` (
    `id_payroll_settlement` BIGINT NOT NULL AUTO_INCREMENT,
    `id_member_account` BIGINT NOT NULL,
    `gross_salary` DOUBLE NOT NULL,
    `net_salary` DOUBLE NOT NULL,
    `year_month` VARCHAR(7) NOT NULL,
    `payment_date` DATE NULL,
    `active` BIT NOT NULL DEFAULT 1,
    PRIMARY KEY (`id_payroll_settlement`),
    UNIQUE KEY `uk_member_account_year_month` (`id_member_account`, `year_month`),
    CONSTRAINT `fk_payroll_settlement_member_account` 
        FOREIGN KEY (`id_member_account`) 
        REFERENCES `member_accounts` (`id_account`)
        ON DELETE RESTRICT 
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


