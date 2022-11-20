DROP TABLE requests;

CREATE TABLE requests
(
    id int auto_increment,
    client_id int null,
    company_id int null,
    quantity double null,
    type varchar(15) null,
    status varchar(10) null,
    date timestamp null,
    constraint requests_pk
        primary key (id),
    constraint requests_users_id_fk
        foreign key (client_id) references users (id),
    constraint requests_users_id_fk_2
        foreign key (company_id) references users (id)
);

INSERT INTO requests(client_id, company_id, quantity, type, `status`, `date`) VALUES (1, 2, 200, 'METAL', 'PENDING', CURRENT_TIMESTAMP());
INSERT INTO requests(client_id, company_id, quantity, type, `status`, `date`) VALUES (1, 2, 5, 'PLASTIC', 'PENDING', CURRENT_TIMESTAMP());
INSERT INTO requests(client_id, company_id, quantity, type, `status`, `date`) VALUES (1, 2, 10, 'PLASTIC', 'PENDING', CURRENT_TIMESTAMP());