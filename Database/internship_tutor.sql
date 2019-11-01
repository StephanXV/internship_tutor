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
    provincia_nascita varchar(50) not null,
    citta_residenza varchar(50) not null,
    provincia_residenza varchar(50) not null,
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
    email varchar(100) not null,
    telefono varchar(20) not null
);

create table azienda (
	id_utente integer unsigned not null primary key,
    ragione_sociale varchar(150) not null,
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
    email varchar(100) not null,
    telefono varchar(20) not null
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
    id_azienda integer unsigned not null,
    constraint offerte_azienda foreign key(id_azienda)
		references azienda(id_utente) on delete cascade on update cascade
);

create table tutore_uni (
	id integer unsigned not null primary key auto_increment,
    nome varchar(50) not null,
    cognome varchar(50) not null,
    email varchar(100) not null,
    telefono varchar(20) not null
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
    constraint studente_resoconto foreign key(id_studente) 
		references studente(id_utente) on update cascade on delete cascade,
	constraint offerta_tirocinio_resoconto foreign key(id_offerta_tirocinio) 
		references offerta_tirocinio(id) on update cascade on delete cascade
 ); 
	
create table valutazione (
	id_studente integer unsigned not null,
    id_azienda integer unsigned not null, 
    stelle integer(1) unsigned not null,
    constraint studente_valutazione foreign key(id_studente) 
		references studente(id_utente) on update cascade on delete cascade,
	constraint azienda_valutazione foreign key(id_azienda) 
		references azienda(id_utente) on update cascade on delete cascade
 );
 
 insert into responsabile_tirocini (nome, cognome, email, telefono)
	values ('Paolo', 'Rossi', 'paolo.rossi@mail.it', '3832283647'),
		   ('Marco', 'Rossi', 'marco.rossi@mail.it', '3832221317');
    
insert into tutore_tirocinio (nome, cognome, email, telefono)
	values ('Antonio', 'Rossi', 'antonio.rossi@mail.it', '3232412647'),
           ('Sofia', 'Rossi', 'sofia.rossi@mail.it', '323232317');
           
insert into tutore_uni (nome, cognome, email, telefono)
	values ('Lucia', 'Rossi', 'lucia.rossi@mail.it', '383121332'),
           ('Giorgio', 'Rossi', 'giorgio.rossi@mail.it', '321483647');
           
insert into utente (email, username, pw, tipologia)
	values ('admin@mail.it', 'admin', 'admin', 'ad'),
		   ('google@mail.it', 'google', 'google', 'az'),
		   ('microsoft@mail.it', 'microsoft', 'microsoft', 'az'),
           ('stefano.florio@mail.it', 'steflo', 'steflo', 'st'),
           ('giuseppe.gasbarro@mail.it', 'giusgas', 'giusgas', 'st'),
           ('enrico.monte@mail.it', 'enrimon', 'enrimon', 'st');

insert into azienda (id_utente, ragione_sociale, indirizzo, citta, cap, provincia, rappresentante_legale, piva,
					foro_competente, src_documento_convenzione, tematiche, stato_convenzione, corso_studio,
                    inizio_convenzione, durata_convenzione, id_responsabile)
	values (2, 'Google', 'Via Federico Confalonieri 4', 'Milano', '20124', 'MI', 'Carlo Rossi', '28361583764',
		   'Tribunale della Repubblica di Milano', null, 'Inserimento nello sviluppo di un\' applicazione per dispositivi mobili, 
           utilizzando React Native e MongoDB', 0, 'Informatica', null, 6, 1),
           (3, 'Microsoft', 'Via Avignone 10', 'Roma', '00144', 'RM', 'Claudio Rossi', '39274837218',
           'Tribunale Ordinario di Roma', null, 'Utilizzo dell\'intelligenza artificiale per sviluppare sistemi di riconoscimento 
		   facciale; sviluppo di siti web con Angular 6', 1, 'Informatica', '2019-08-27', 12, 2);
           
insert into studente (id_utente, nome, cognome, codice_fiscale, data_nascita, citta_nascita, provincia_nascita,
					 citta_residenza, provincia_residenza, cap_residenza, telefono, corso_laurea, handicap)
	values (4, 'Stefano', 'Florio', 'FLRSFN97H08E372H', '1997-06-08', 'Vasto', 'CH', 'Vasto', 'CH', '66054', '3937726352', 'Informatica', false),
		   (5, 'Giuseppe', 'Gasbarro', 'GSPGBR97H08E372H', '1997-05-23', 'Lanciano', 'CH', 'Lanciano', 'CH', '66034', '3937726352', 'Informatica', false),
           (6, 'Enrico', 'Monte', 'ERCMNT97H08E372H', '1997-05-05', 'Vasto', 'CH', 'Torrebruna', 'CH', '66050', '3937726352', 'Informatica', false);

insert into offerta_tirocinio (luogo, settore, orari, durata, titolo, obiettivi, modalita, facilitazioni, id_azienda)
	values ('Roma', 'Intelligenza artificiale', null, 2, 'Riconoscimento facciale con AI', 'Imparare a lavorare in un team di sviluppo; acquisire una 
		   conoscenza avanzata sulle AI e sulla loro applicazione', 'Il tirocinante dapprima seguirà un corso di intelligenza artificiale, poi sarà affiancato da un tutor 
           esperto per applicare ciò che ha appreso nel corso', 'Buoni pasto', 3),
           ('Roma', 'Divisione sviluppo web', null, 1, 'Sviluppo in Angular 6', 'Imparare a lavorare in un team di sviluppo; acquisire una 
		   conoscenza avanzata di Angular 6', 'Il tirocinante sarà affiancato da un tutor esperto che lo seguirà nello sviluppo dei compiti 
           assegnati', null, 3);
           
insert into candidatura (id_studente, id_offerta_tirocinio, id_tutore_uni, cfu, ore_tirocinio, stato_candidatura,
						src_documento_candidatura, data_inizio, data_fine, tms)
	values (4, 1, 1, 6, 25, 0, null, null, null, current_timestamp()),
		   (5, 1, 2, 3, 25, 1, null, '2019-11-21', '2020-1-27', current_timestamp()),
           (6, 1, 1, 4, 25, 0, null, null, null, current_timestamp()),
           (4, 2, 2, 6, 25, 1, null, '2019-12-21', '2020-1-27', current_timestamp()),
           (5, 2, 2, 3, 25, 3, null, '2019-11-21', '2020-1-27', current_timestamp()),
           (6, 1, 1, 3, 25, 0, null, null, null, current_timestamp());
           
insert into resoconto (id_studente, id_offerta_tirocinio, ore_effettive, descrizione_attivita, giudizio, src_documento_resoconto)
	values (5, 2, 75, 'Il tirocinante ha contribuito correttamente allo sviluppo lato server del sito web, lavorando in un team di sviluppo', 
		   'Promosso', null);
           
insert into valutazione (id_studente, id_azienda, stelle)
	values (5, 3, 4);
           
			