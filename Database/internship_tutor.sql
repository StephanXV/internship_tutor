drop database if exists internship_tutor;
create database internship_tutor;
use internship_tutor;

create table utente (
	id integer unsigned not null primary key auto_increment,
    email varchar(100) not null unique,
    username varchar(100) not null unique,
    pw varchar(100) not null,
    tipologia enum('st' ,'az', 'ad')
);

create table studente (
	id_utente integer unsigned not null primary key,
    nome varchar(50) not null,
    cognome varchar(50) not null,
    codice_fiscale varchar(16) not null unique,
    data_nascita date not null,
    citta_nascita varchar(50) not null,
    provincia_nascita varchar(2) not null,
    citta_residenza varchar(50) not null,
    provincia_residenza varchar(2) not null,
    cap_residenza varchar(10) not null,
    telefono varchar(20) not null,
    corso_laurea varchar(100) not null,
    handicap bool not null,
    constraint studente_utente foreign key(id_utente)
		references utente(id) on delete cascade on update cascade
);

create table responsabile_tirocini (
	id integer unsigned not null primary key auto_increment,
	nome varchar(50) not null,
	cognome varchar(50) not null,
    email varchar(100) not null unique,
    telefono varchar(20) not null unique
);

create table azienda (
	id_utente integer unsigned not null primary key,
    ragione_sociale varchar(150) not null unique,
    indirizzo varchar(200) not null,
    citta varchar(100) not null,
    cap varchar(10) not null,
    provincia varchar(2) not null,
    rappresentante_legale varchar(200) not null,
    piva varchar(16) not null unique,
    foro_competente varchar(100) not null,
    src_documento_convenzione varchar(250),
    tematiche text not null,
    stato_convenzione integer(1) not null default 0,
    corso_studio varchar(200) not null,
    inizio_convenzione date default null,
    durata_convenzione integer(3) not null,
    id_responsabile integer unsigned not null,
    constraint responsabile_azienda foreign key(id_responsabile)
		references responsabile_tirocini(id) on delete cascade
        on update cascade,
	constraint azienda_utente foreign key(id_utente)
		references utente(id) on delete cascade on update cascade
);

create table tutore_tirocinio (
	id integer unsigned not null primary key auto_increment,
    nome varchar(50) not null,
    cognome varchar(50) not null,
    email varchar(100) not null unique,
    telefono varchar(20) not null unique
);

create table offerta_tirocinio (
	id integer unsigned not null primary key auto_increment,
    luogo varchar(100) not null,
    settore varchar(100) not null,
    orari varchar(200),
    durata integer(3),
    titolo varchar(200),
    obiettivi text not null,
    modalita text not null,
    facilitazioni text,
    attiva bool not null default true,
    id_azienda integer unsigned not null,
    constraint offerte_azienda foreign key(id_azienda)
		references azienda(id_utente) on delete cascade on update cascade
);

create table tutore_uni (
	id integer unsigned not null primary key auto_increment,
    nome varchar(50) not null,
    cognome varchar(50) not null,
    email varchar(100) not null unique,
    telefono varchar(20) not null unique
 ); 
 
create table candidatura (
	id_studente integer unsigned not null,
    id_offerta_tirocinio integer unsigned not null, 
    id_tutore_uni integer unsigned not null,
    cfu integer(2) not null,
    ore_tirocinio integer(3) not null,
    stato_candidatura integer(1) not null default 0,
    src_documento_candidatura varchar(250),
    data_inizio date,
    data_fine date,
    tms timestamp default current_timestamp,
    constraint candidatura_unica unique (id_studente, id_offerta_tirocinio),
    constraint studente_candidatura foreign key(id_studente) 
		references studente(id_utente) on update cascade on delete cascade,
	constraint offerta_tirocinio_candidatura foreign key(id_offerta_tirocinio) 
		references offerta_tirocinio(id) on update cascade on delete cascade,
    constraint tutore_candidatura foreign key(id_tutore_uni) 
		references tutore_uni(id) on update cascade on delete cascade
 );   
 	
create table resoconto (
	id_studente integer unsigned not null,
    id_offerta_tirocinio integer unsigned not null, 
    ore_effettive integer(3) not null,
    descrizione_attivita text not null,
    giudizio text not null,
    src_documento_resoconto varchar(250),
    constraint resoconto_unico unique (id_studente, id_offerta_tirocinio),
    constraint studente_resoconto foreign key(id_studente) 
		references studente(id_utente) on update cascade on delete cascade,
	constraint offerta_tirocinio_resoconto foreign key(id_offerta_tirocinio) 
		references offerta_tirocinio(id) on update cascade on delete cascade
 ); 
	
create table valutazione (
	id_studente integer unsigned not null,
    id_azienda integer unsigned not null, 
    stelle integer(1) unsigned not null,
    constraint valutazione_unica unique (id_studente, id_azienda),
    constraint studente_valutazione foreign key(id_studente) 
		references studente(id_utente) on update cascade on delete cascade,
	constraint azienda_valutazione foreign key(id_azienda) 
		references azienda(id_utente) on update cascade on delete cascade
 );
