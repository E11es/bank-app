package com.github.service;

import com.github.entity.Bank;
import com.github.entity.Credit;

import java.util.List;

public interface CreditService {

    List<Credit> findAll();

    void delete(Credit credit);

    void save(Credit credit);

    List<Credit> findByBank(Bank bank);
}
