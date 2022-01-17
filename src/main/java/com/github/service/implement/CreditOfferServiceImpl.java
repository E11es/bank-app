package com.github.service.implement;

import com.github.entity.CreditOffer;
import com.github.entity.PaymentSchedule;
import com.github.repository.CreditOfferRepository;
import com.github.repository.PaymentScheduleRepository;
import com.github.service.CreditOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CreditOfferServiceImpl implements CreditOfferService {

    @Autowired
    private CreditOfferRepository creditOfferRepository;
    @Autowired
    private PaymentScheduleRepository paymentScheduleRepository;

    @Override
    public List<CreditOffer> findAll() {
        return creditOfferRepository.findAll();
    }

    @Override
    public void delete(CreditOffer creditOffer) {
        creditOfferRepository.delete(creditOffer);
    }

    @Override
    public void save(CreditOffer creditOffer) {
        creditOfferRepository.save(creditOffer);
    }

    @Override
    public List<PaymentSchedule> getPaymentsScheduleByOffer(CreditOffer offer) {
        if (offer == null || offer.getId() == null || !creditOfferRepository.existsById(offer.getId())) {
            return new ArrayList<>();
        }
        return paymentScheduleRepository.findByOfferMapped(offer);
    }
}
