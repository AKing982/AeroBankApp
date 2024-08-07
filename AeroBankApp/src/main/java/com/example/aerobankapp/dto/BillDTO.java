package com.example.aerobankapp.dto;

import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.AccountNumber;
import com.example.aerobankapp.model.ReferenceNumber;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record BillDTO(Long billId,

                      AccountCode payeeAccountCode,
                      String payeeName,
                      AccountNumber payeeAccountNumber,
                      BigDecimal billAmount,
                      LocalDate dueDate,
                      LocalDate paymentDate,
                      String status,
                      String currency,
                      ReferenceNumber referenceNumber,
                      String description,
                      String createdAt,
                      String createdBy,
                      LocalDate createAt,
                      String modifiedBy,
                      LocalDate modifiedAt) {
}
