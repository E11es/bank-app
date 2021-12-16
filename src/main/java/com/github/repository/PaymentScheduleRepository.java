package com.github.repository;

import com.github.entity.CreditOffer;
import com.github.entity.PaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, UUID> {
    List<PaymentSchedule> findByOfferMapped(CreditOffer offer);
}
