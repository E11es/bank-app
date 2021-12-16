package com.github.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "CLIENTS_TABLE")
public class Client {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String passport;

    //Создали связь. Разобраться что из себя представляют аргументы в ManyToOne, а также, что такое joinColumn
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, targetEntity = Bank.class)
    @JoinColumn(name = "bank_id")
    private Bank bank;
    //targetEntity - указывает, где "лежит" владеемая сущность
    //@JoinColumn задаёт имя столбца, в котором будет храниться ссылка на владеемый объект (эта аннотация только у владельца).
    //Параметр fetch = FetchType.EAGER говорит, что при загрузке владеемого объекта необходимо сразу загрузить и коллекцию владельцев

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public String getDisplayName(){
        return firstName+" "+lastName;
    }
}
