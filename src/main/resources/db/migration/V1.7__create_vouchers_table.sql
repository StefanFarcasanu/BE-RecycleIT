create table vouchers
(
    id int auto_increment,
    client_id int null,
    retailer_id int not null,
    value double null,
    details varchar(100) null,
    code varchar(100) null,
    status varchar(20) null,
    valid_until timestamp null,
    constraint vouchers_pk
        primary key (id),
    constraint vouchers_users_id_fk
        foreign key (retailer_id) references users (id)
);

create unique index vouchers_code_uindex
    on vouchers (code);