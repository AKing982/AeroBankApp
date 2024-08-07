package com.example.aerobankapp.workbench.formatter;

import com.example.aerobankapp.dto.BillPayeeInfoDTO;
import com.example.aerobankapp.dto.PaymentHistoryDTO;
import com.example.aerobankapp.dto.ScheduledPaymentDTO;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class FormatterUtil
{

    public static List<ScheduledPaymentDTO> getFormattedBillPaymentSchedule(List<BillPayeeInfoDTO> billPayeeInfoDTOS){
      return billPayeeInfoDTOS.stream()
              .map(FormatterUtil::formatScheduledBillPayment)
              .collect(Collectors.toList());
    }

    public static ScheduledPaymentDTO formatScheduledBillPayment(BillPayeeInfoDTO billPayeeInfoDTO){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DecimalFormat currencyFormatter = new DecimalFormat("$#, ##0.00");

        String formattedLastPayment = billPayeeInfoDTO.lastPayment() != null ? billPayeeInfoDTO.lastPayment().format(dateFormatter) : null;
        String formattedNextPayment = billPayeeInfoDTO.nextPayment() != null ? billPayeeInfoDTO.nextPayment().format(dateFormatter) : null;
        String formattedPaymentAmount = billPayeeInfoDTO.paymentAmount() != null ? currencyFormatter.format(billPayeeInfoDTO.paymentAmount()) : null;

        return new ScheduledPaymentDTO(billPayeeInfoDTO.payeeName(),
                formattedLastPayment,
                formattedNextPayment,
                billPayeeInfoDTO.paymentDueDate(),
                formattedPaymentAmount);
    }


}
