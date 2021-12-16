package com.github.service;

import com.github.entity.Bank;
import com.github.entity.Client;

import java.util.List;

public interface ClientService {

    List<Client> findAll();
    void delete(Client client);
    void save(Client client);
    List<Client> findByBank(Bank bank);
}
