package com.github.service;

import com.github.entity.Bank;

import java.util.List;

public interface BankService {

    List<Bank> findAll();

    void delete(Bank bank);

    void save(Bank bank);

}
