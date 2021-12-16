package com.github.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "BANKS_TABLE")
public class Bank {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private String name;

 //   @OneToMany(mappedBy = "bank", fetch = FetchType.LAZY, cascade = CascadeType.MERGE, targetEntity = Client.class)
//    //В случае FetchType.LAZY объекты загружаются в память не сразу, а только при обращении к ним
//
 //   @OneToMany(mappedBy = "bank", fetch = FetchType.LAZY, cascade = CascadeType.MERGE, targetEntity = Credit.class)

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
//
//    public List<Client> getClientList() {
//        return clientList;
//    }
//
//    public void setClientList(List<Client> clientList) {
//        this.clientList = clientList;
//    }
//
//    public List<Credit> getCreditList() {
//        return creditList;
//    }
//
//    public void setCreditList(List<Credit> creditList) {
//        this.creditList = creditList;
//    }
}
