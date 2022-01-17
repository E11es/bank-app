package com.github.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "payments_table")
public class PaymentSchedule {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    private LocalDate paymentDate;
    private Double paymentSum;
    private Double creditBodySum;
    private Double creditPercentSum;
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = CreditOffer.class)
    @JoinColumn(name = "offer_id")
    private CreditOffer offerMapped;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Double getPaymentSum() {
        return paymentSum;
    }

    public void setPaymentSum(Double paymentSum) {
        this.paymentSum = paymentSum;
    }

    public Double getCreditBodySum() {
        return creditBodySum;
    }

    public void setCreditBodySum(Double creditBodySum) {
        this.creditBodySum = creditBodySum;
    }

    public Double getCreditPercentSum() {
        return creditPercentSum;
    }

    public void setCreditPercentSum(Double creditPercentSum) {
        this.creditPercentSum = creditPercentSum;
    }

    public CreditOffer getOfferMapped() {
        return offerMapped;
    }

    public void setOfferMapped(CreditOffer offerMapped) {
        this.offerMapped = offerMapped;
    }
}
