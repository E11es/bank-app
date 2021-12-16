package com.github.service.implement;

import com.github.entity.Bank;
import com.github.entity.Credit;
import com.github.repository.BankRepository;
import com.github.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service //Инициализируем наш сервис
public class BankServiceImpl implements BankService {

    @Autowired
    private BankRepository bankRepository;

    //@Override говорит о том, что я либо пререопределяю существующий метод, либо же реализую метод интерфейса
    @Override
    public List<Bank> findAll(){
        return bankRepository.findAll();
    }

    @Override
    public void delete(Bank bank){
        bankRepository.delete(bank);
    }

    @Override
    public void save(Bank bank){
        bankRepository.saveAndFlush(bank);
    }
}
