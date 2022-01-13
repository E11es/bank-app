package com.github;

import com.github.repository.CreditOfferRepository;
import com.github.service.PaymentScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentScheduleGenerator {
    @Autowired
    private PaymentScheduleService paymentScheduleService;
    @Autowired
    private CreditOfferRepository creditOfferRepository;

    @EventListener
    public void generate(ContextRefreshedEvent refreshedEvent) {
        creditOfferRepository.findAll()
                .forEach(offer -> paymentScheduleService.generatePaymentSchedule(offer));
    }
}
