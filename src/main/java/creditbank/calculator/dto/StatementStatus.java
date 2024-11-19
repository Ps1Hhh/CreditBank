package creditbank.calculator.dto;

/**
 * Enum для описания статусов заявки.
 */
public enum StatementStatus {
    CREATED,          // Заявка создана
    PREAPPROVED,      // Заявка предварительно одобрена
    APPROVED,         // Заявка окончательно одобрена
    CALCULATED,       // Рассчитаны параметры кредита
    CREDIT_ISSUED,    // Кредит выдан
    REJECTED          // Заявка отклонена
}