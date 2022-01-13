package com.github.service.implement;

import com.github.entity.Bank;
import com.github.entity.Client;
import com.github.repository.BankRepository;
import com.github.repository.ClientRepository;
import com.github.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private BankRepository bankRepository;

    @Override
    public List<Client> findAll(){
        return clientRepository.findAll();
    }

    @Override
    public void delete(Client client){
        clientRepository.delete(client);
    }

    @Override
    public void save(Client client){
        clientRepository.saveAndFlush(client);
    }

    @Override
    public List<Client> findByBank(Bank bank) {
        if(bank==null|| bank.getId()==null|| !bankRepository.existsById(bank.getId())){
            return new ArrayList<>();
        }
        return clientRepository.findByBank(bank);
    }
}
