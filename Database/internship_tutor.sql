drop database if exists internship_tutor;
create database internship_tutor;
use internship_tutor;

create table studente (
	id integer unsigned not null primary key auto_increment,
    nome varchar(50) not null,
    cognome varchar(50) not null,
    codice_fiscale varchar(16) not null,
    data_nascita date not null,
    citta_nascita varchar(50) not null,
    provincia_nascita varchar(50) not null,
    citta_residenza varchar(50) not null,
    provincia_residenza varchar(50) not null,
    telefono varchar(20),
    