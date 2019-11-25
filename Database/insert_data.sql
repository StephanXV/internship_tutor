 
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
	values ('admin@mail.it', 'admin', 'Wzt1qwYki8QB5MxWbRmhAAdQkQVx6J1/', 'ad'),
		   ('google@mail.it', 'google', '2jq4XG03cR2KDk53vgRMCkx0ydVgO0r5', 'az'),
		   ('microsoft@mail.it', 'microsoft', 'UCbLgSj1jbP50jW0hJUeqx7wgGL8r76f', 'az'),
           ('stefano.florio@mail.it', 'steflo', 'rVAcsixrOLRdlqXV6oK7fBd+pNm2tl4V', 'st'),
           ('giuseppe.gasbarro@mail.it', 'giusgas', 'P2C27LozxOTfclPSjSLgTw1u19rXsG8Q', 'st'),
           ('enrico.monte@mail.it', 'enrimon', 'ItCMzC/jMvxzXXqSuHlC7dE1yPaqmi62', 'st');

insert into azienda (id_utente, ragione_sociale, indirizzo, citta, cap, provincia, rappresentante_legale, piva,
					foro_competente, src_documento_convenzione, tematiche, stato_convenzione, corso_studio,
                    inizio_convenzione, durata_convenzione, id_responsabile)
	values (2, 'Google', 'Via Federico Confalonieri 4', 'Milano', '20124', 'MI', 'Carlo Rossi', '28361583764',
		   'Tribunale della Repubblica di Milano', null, 'Inserimento nello sviluppo di un\' applicazione per dispositivi mobili, 
           utilizzando React Native e MongoDB', 0, 'Informatica', null, 6, 1),
           (3, 'Microsoft', 'Via Avignone 10', 'Roma', '00144', 'RM', 'Claudio Rossi', '39274837218',
           'Tribunale Ordinario di Roma', 'convenzione_41598925698264908571573730186680.pdf', 'Utilizzo dell\'intelligenza artificiale per sviluppare sistemi di riconoscimento 
		   facciale; sviluppo di siti web con Angular 6', 1, 'Informatica', '2019-08-27', 12, 2);
           
insert into studente (id_utente, nome, cognome, codice_fiscale, data_nascita, citta_nascita, provincia_nascita,
					 citta_residenza, provincia_residenza, cap_residenza, telefono, corso_laurea, handicap)
	values (4, 'Stefano', 'Florio', 'FLRSFN97H08E372H', '1997-06-08', 'Vasto', 'CH', 'Vasto', 'CH', '66054', '3937726352', 'Informatica', false),
		   (5, 'Giuseppe', 'Gasbarro', 'GSPGBR97H08E372H', '1997-05-23', 'Lanciano', 'CH', 'Lanciano', 'CH', '66034', '3937726352', 'Informatica', false),
           (6, 'Enrico', 'Monte', 'ERCMNT97H08E372H', '1997-05-05', 'Vasto', 'CH', 'Torrebruna', 'CH', '66050', '3937726352', 'Informatica', false);

insert into offerta_tirocinio (luogo, settore, orari, durata, titolo, obiettivi, modalita, facilitazioni, id_azienda, id_tutore_tirocinio)
	values ('Roma', 'Intelligenza artificiale', null, 2, 'Riconoscimento facciale con AI', 'Imparare a lavorare in un team di sviluppo; acquisire una 
		   conoscenza avanzata sulle AI e sulla loro applicazione', 'Il tirocinante dapprima seguirà un corso di intelligenza artificiale, poi sarà affiancato da un tutor 
           esperto per applicare ciò che ha appreso nel corso', 'Buoni pasto', 3, 1),
           
           ('Roma', 'Divisione sviluppo web', null, 1, 'Sviluppo in Angular 6', 'Imparare a lavorare in un team di sviluppo; acquisire una 
		   conoscenza avanzata di Angular 6', 'Il tirocinante sarà affiancato da un tutor esperto che lo seguirà nello sviluppo dei compiti 
           assegnati', null, 3, 2),
           
           ('Milano', 'Ricerca operativa', null, 3, 'Ricerca operativa in assembly', 'Imparare a lavorare in un team di sviluppo; acquisire
           conoscenze di base sulla ricerca operativa.', 'Il tirocinante sarà affiancato da un tutor esperto e realizzerà algoritmi', 
           'Rimborso viaggio', 3, 2);
           
           
insert into candidatura (id_studente, id_offerta_tirocinio, id_tutore_uni, cfu, stato_candidatura,
						src_documento_candidatura, data_inizio, data_fine, tms)
	values (4, 1, 1, 6, 0, null, null, null, current_timestamp()),
		   (5, 1, 2, 3, 1, null, '2019-11-21', '2020-1-27', current_timestamp()),
           (6, 1, 1, 4, 0, null, null, null, current_timestamp()),
           (4, 2, 2, 6, 1, null, '2019-12-21', '2020-1-27', current_timestamp()),
           (5, 2, 2, 3, 2, null, '2019-11-21', '2020-1-27', current_timestamp()),
           (6, 2, 1, 3, 0, null, null, null, current_timestamp());
           
insert into resoconto (id_studente, id_offerta_tirocinio, ore_effettive, descrizione_attivita, giudizio, src_documento_resoconto)
	values (5, 2, 75, 'Il tirocinante ha contribuito correttamente allo sviluppo lato server del sito web, lavorando in un team di sviluppo', 
		   'Promosso', null);
           
insert into valutazione (id_studente, id_azienda, stelle)
	values (5, 3, 4);
           
			