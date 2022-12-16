drop table `vouchers`;
drop table `requests`;
drop table `users`;

create table users
(
    id int auto_increment,
    firstname varchar(100) null,
    lastname varchar(100) null,
    email varchar(100) null,
    password varchar(100) null,
    county varchar(50) null,
    city varchar(50) null,
    role varchar(10) null,
    constraint users_pk
        primary key (id)
);

create unique index users_email_uindex
    on users (email);


INSERT INTO users(firstname, lastname, email, `password`, county, city, role) VALUES ('Giani', 'Fantasticul', 'giani@gmail.com', '$2a$13$V32Ve0haQLLszdzZCby7AeiQjKLFzYYv4POAr740ilbecbVEhohIa', 'Cluj', 'Cluj-Napoca', 'CLIENT');
INSERT INTO users(firstname, lastname, email, `password`, county, city, role) VALUES ('Firma Reciclare', NULL, 'firma@gmail.com', '$2a$13$V32Ve0haQLLszdzZCby7AeiQjKLFzYYv4POAr740ilbecbVEhohIa', 'Cluj', 'Cluj-Napoca', 'COMPANY');
INSERT INTO users(firstname, lastname, email, `password`, county, city, role) VALUES ('Lidl', null, 'lidl@gmail.com', '$2a$13$V32Ve0haQLLszdzZCby7AeiQjKLFzYYv4POAr740ilbecbVEhohIa', 'Cluj', 'Cluj-Napoca', 'RETAILER');


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