insert into responsabile_tirocini(nome, cognome, email, telefono)
	values ('Marco', 'Rossi', 'marco.rossi@gmail.com', '1231233246');
    
insert into tutore_tirocinio(nome, cognome, email, telefono)
	values ('Paolo', 'Rossi', 'paolo.rossi@gmail.com', '1231233246');

insert into tutore_uni(nome, cognome, email, telefono)
	values ('Giuseppe', 'Della Penna', 'giuseppe.dellapenna@gmail.com', '1231233246');
        
insert into utente (email, username, pw, tipologia)
	values ('azienda1@gmail.com', 'aziendaGoogle', '123', 'az');
    
insert into azienda (id_utente, ragione_sociale, indirizzo, citta, cap, provincia, rappresentante_legale, piva, foro_competente, tematiche, corso_studio, durata_convenzione, id_responsabile)
	values (1, 'Google', 'Via Cardinale, 15', 'L\'Aquila', '67100', 'AQ', 'Giulio Rossi', '12312312311', 'Tribunale di L\'Aquila', 'Sviluppo con framework React, Processo di produzione software AGILE', 'Informatica', 6, 1)