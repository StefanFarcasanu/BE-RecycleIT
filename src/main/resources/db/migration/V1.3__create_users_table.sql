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