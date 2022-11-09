create table requests
(
    id int auto_increment,
    client_id int null,
    company_id int null,
    quantity int null,
    type varchar(15) null,
    status varchar(10) null,
    constraint requests_pk
        primary key (id),
    constraint requests_users_id_fk
        foreign key (client_id) references users (id),
    constraint requests_users_id_fk_2
        foreign key (company_id) references users (id)
);