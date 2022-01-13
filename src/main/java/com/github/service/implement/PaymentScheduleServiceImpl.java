package com.github.service.implement;

import com.github.entity.CreditOffer;
import com.github.entity.PaymentSchedule;
import com.github.repository.PaymentScheduleRepository;
import com.github.service.PaymentScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedList;
import java.util.List;

@Service
public class PaymentScheduleServiceImpl implements PaymentScheduleService {

    @Autowired
    private PaymentScheduleRepository paymentScheduleRepository;

    @Override
    public List<PaymentSchedule> findAll() {
        return paymentScheduleRepository.findAll();
    }

    @Override
    public void delete(PaymentSchedule paymentSchedule) {
        paymentScheduleRepository.delete(paymentSchedule);
    }

    @Override
    public void save(PaymentSchedule paymentSchedule) {
        paymentScheduleRepository.saveAndFlush(paymentSchedule);
    }

    @Override
    public List<PaymentSchedule> generatePaymentSchedule(CreditOffer offer) {
        double creditSum = offer.getCreditSum();

        double monthlyInterest = offer.getCredit().getInterestRate() / (12 * 100);
        double termInMonths = offer.getCreditTerm();
        double monthlyPayment = offer.getCreditSum() * (monthlyInterest + monthlyInterest / (Math.pow((1 + monthlyInterest), termInMonths) - 1));

        double balance = creditSum;
        double interest;
        double principal;
        LocalDate startDate = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());
        List<PaymentSchedule> paymentList = new LinkedList<>();

        for (int i = 1, j = 0; i <= termInMonths; i++, j++) {
            PaymentSchedule payment = new PaymentSchedule();
            interest = balance * monthlyInterest;
            LocalDate paymentDate = startDate.plusMonths(j).with((TemporalAdjusters.firstDayOfNextMonth()));
            payment.setPaymentDate(paymentDate);
            payment.setPaymentSum(round(monthlyPayment, 4));
            principal = monthlyPayment - interest;
            balance = balance - principal;
            payment.setCreditPercentSum(round(principal, 4));
            payment.setCreditBodySum(round(balance, 4));
            payment.setOfferMapped(offer);
            paymentList.add(payment);
        }
        return paymentScheduleRepository.saveAll(paymentList);
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
