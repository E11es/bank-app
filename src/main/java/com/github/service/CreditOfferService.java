package com.github.service;

import com.github.entity.Credit;
import com.github.entity.CreditOffer;
import com.github.entity.PaymentSchedule;

import java.util.List;

public interface CreditOfferService {

    List<CreditOffer> findAll();
    void delete(CreditOffer offer);
    void save(CreditOffer offer);
    List<PaymentSchedule> getPaymentsScheduleByOffer(CreditOffer offer);
}
