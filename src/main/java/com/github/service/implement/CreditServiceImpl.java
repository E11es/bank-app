package com.github.service.implement;

import com.github.entity.Bank;
import com.github.entity.Credit;
import com.github.repository.BankRepository;
import com.github.repository.CreditRepository;
import com.github.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CreditServiceImpl implements CreditService {

    @Autowired
    private CreditRepository creditRepository;
    @Autowired
    private BankRepository bankRepository;

    @Override
    public List<Credit> findAll(){
        return creditRepository.findAll();
    }

    @Override
    public void delete(Credit credit){
        creditRepository.delete(credit);
    }

    @Override
    public void save(Credit credit){
        creditRepository.saveAndFlush(credit);
    }

    @Override
    public List<Credit> findByBank(Bank bank) {
        if(bank==null|| bank.getId()==null|| !bankRepository.existsById(bank.getId())){
            return new ArrayList<>();
        }
        return creditRepository.findByBank(bank);
    }
}
