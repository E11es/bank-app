package com.github.service;

import com.github.entity.Bank;
import com.github.entity.Credit;

import java.util.List;
import java.util.UUID;

public interface BankService {

    List<Bank> findAll(); //Возвращает все сущности данного класса
    void delete(Bank bank); //Удаляет сущность с заданным id, но почему нельзя указать в параметрах просто сущность Банка
    void save(Bank bank);
    //Нужны ли еще какие-либо методы?

}
