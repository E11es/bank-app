package com.github.service;

import com.github.entity.CreditOffer;
import com.github.entity.PaymentSchedule;

import java.util.List;

public interface PaymentScheduleService {

    List<PaymentSchedule> findAll();
    void delete(PaymentSchedule paymentSchedule);
    void save(PaymentSchedule paymentSchedule);
    List<PaymentSchedule> generatePaymentSchedule(CreditOffer offer);
}
