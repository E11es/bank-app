
create table banks_table
(
    id   uuid ,
    name varchar(255) not null,
    primary key (id)
);
create table clients_table
(
    id         uuid,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    phone      bigint       not null,
    email      varchar(255) not null,
    passport   bigint       not null,
    bank_id    uuid,
    primary key (id)
);
create table credits_table
(
    id            uuid ,
    limit         integer not null,
    interest_rate double  not null,
    bank_id       uuid,
    primary key (id)
);
create table offers_table
(
    id          uuid ,
    credit_sum  double not null,
    credit_term double not null,
    client_id   uuid,
    credit_id   uuid,
    primary key (id)
);
create table payments_table
(
    id                 uuid ,
    credit_body_sum    double not null,
    credit_percent_sum double not null,
    payment_date       date,
    payment_sum        double not null,
    offer_id           uuid,
    primary key (id)
);
alter table clients_table
    add constraint FK75m9t0v6p4eq1kiwuvxedyuf2 foreign key (bank_id) references banks_table on delete cascade;
alter table credits_table
    add constraint FKk43nmjww7rhqf3eu7h2mudwem foreign key (bank_id) references banks_table on delete cascade;
alter table offers_table
    add constraint FKlbvggcc9b6wnm1qe7xby9mta9 foreign key (client_id) references clients_table on delete cascade;
alter table offers_table
    add constraint FKmbrijrf2384ml3rp6u4clxw88 foreign key (credit_id) references credits_table on delete cascade;
alter table payments_table
    add constraint FKoouhe4hvh6jmrp5ru34t8ct8y foreign key (offer_id) references offers_table on delete cascade;