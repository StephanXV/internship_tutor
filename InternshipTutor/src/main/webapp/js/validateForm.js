$(document).ready( function () {


        $( "#regAzienda").validate( {
            rules: {
                ragione_sociale: {
                    minlength: 2,
                    maxlength: 90
                },
                piva: {
                    required: true,
                    minlength: 11,
                    maxlength: 11,
                    digits: true
                },
                indirizzo: {
                    required: true,
                    minlength: 2
                },
                citta: {
                    required: true,
                    minlength: 2
                },
                cap: {
                    required: true,
                    minlength: 5,
                    maxlength: 5,
                    digits: true
                },
                provincia: {
                    required: true,
                    minlength: 2,
                    maxlength: 2
                },
                tematiche: {
                    required: true,
                    minlength:2
                },
                corso_studio: {
                    required: true,
                    minlength:2
                },
                durata: {
                    required: true,
                    minlength:1,
                    maxlength:3,
                    digits: true
                },
                rappresentante_legale: {
                    required: true,
                    minlength:2
                },
                foro_competente: {
                    required: true,
                    minlength:2
                },
                username: {
                    required: true,
                    minlength:2
                },
                pw: {
                    required: true,
                    minlength:8
                },
                email: {
                    required: true,
                    minlength:5
                },
                telefono_rt: {
                    required: true,
                    minlength:5
                },
                email_rt: {
                    required: true,
                    minlength:5
                },
                nome_rt: {
                    required: true,
                    minlength:2
                },
                cognome_rt: {
                    required: true,
                    minlength:2
                }
            },
            messages: {
                ragione_sociale: {
                    required: "La ragione sociale &egrave; richiesta",
                    minlength: "Inserire almeno due caratteri",
                    maxlength: "Non inserire più di 90 caratteri"
                },
                indirizzo: {
                    required: "l'indirizzo &egrave; richiesto",
                    minlength: "Inserire almeno due caratteri"
                },
                citta: {
                    required: "La città &egrave; richiesta",
                    minlength: "Inserire almeno due caratteri"
                },
                piva: {
                    required: "Partita IVA non può essere vuoto",
                    minlength: "Inserire 11 cifre",
                    maxlength: "Inserire 11 cifre",
                    digit: "Sono ammesse solo cifre"
                },
                cap: {
                    required: "Il CAP &egrave; richiesto",
                    minlength: "Il cap &egrave; formato da 5 numeri",
                    maxlength: "Il cap &egrave; formato da 5 numeri"
                },
                provincia: {
                    required: "La provincia &egrave; richiesta",
                    minlength: "Inserisci 2 caratteri",
                    maxlength:"Inserisci 2 caratteri"
                },
                tematiche: {
                    required: "Le tematiche sono richieste",
                    minlength: "Inserisci almeno 2 caratteri"
                },
                foro_competente: {
                    required: "Il foro competente &egrave; richiesto",
                    minlength: "Inserisci almeno 2 caratteri"
                },
                durata: {
                    required: "La durata &egrave; richiesta",
                    minlength: "Inserisci almeno 1 cifra"
                },
                corso_studio: {
                    required: "Il corso &egrave; richiesto",
                    minlength: "Inserisci almeno 8 numeri"
                },
                rappresentante_legale: {
                    required: "Il rappresentante legale &egrave; richiesto",
                    minlength: "Inserisci almeno 2 caratteri"
                },
                email: {
                    required: "L'Email &egrave; richiesta",
                    minlength: "Inserire almeno 2 caratteri"
                },
                username: {
                    required: "L'username &egrave; richiesta",
                    minlength: "Inserire almeno 2 caratteri"
                },
                pw: {
                    required: "La password &egrave; richiesta",
                    minlength: "Inserire almeno 8 caratteri"
                },
                telefono_rt: {
                    required: "Il telefono del responsabile &egrave; richiesto",
                    minlength: "Deve essere lungo almeno 5 caratteri"
                },
                email_rt: {
                    required: "L'email del responsabile &egrave; richiesta",
                    minlength: "Deve essere lunga almeno 5 caratteri"
                },
                nome_rt: {
                    required: "Nome responsabile richiesto",
                    minlength: "Deve essere lungo almeno 2 caratteri"
                },
                cognome_rt: {
                    required: "Cognome responsabile richiesto",
                    minlength: "Deve essere lungo almeno 2 caratteri"
                }

            },

            errorElement: "div",
            errorPlacement: function ( error, element ) {
                // Add the `help-block` class to the error element
                error.addClass( "invalid-feedback" );
                if ( element.prop( "type" ) === "checkbox" ) {
                    error.insertAfter( element.parent( "label" ) );
                } else {
                    error.insertAfter( element );
                }
            },
            highlight: function ( element, errorClass, validClass ) {
                $( element ).addClass( "is-invalid" ).removeClass( "is-valid" );
            },
            unhighlight: function (element, errorClass, validClass) {
                $( element ).addClass( "is-valid" ).removeClass( "is-invalid" );
            },
            submitHandler: function(form) { // <- pass 'form' argument in
                $(".submit").attr("disabled", true);
                form.submit(); // <- use 'form' argument here.
            },
        } );
    });


$( "#regStudente").validate( {
    rules: {
        codice_fiscale: {
            required: true,
            minlength: 16,
            maxlength: 16
        },
        handicap: {
            required: true,
        },
        cap_residenza: {
            required: true,
            minlength: 5,
            maxlength: 5,
        },
        provincia_residenza: {
            required: true,
            minlength: 2,
            maxlength: 2
        },
        provincia_nascita: {
            required: true,
            minlength: 2,
            maxlength: 2
        },
        citta_residenza: {
            required: true,
            minlength: 2
        },
        citta_nascita: {
            required: true,
            minlength:2
        },
        email: {
            required: true,
            minlength:5
        },
        username: {
            required: true,
            minlength:2,
        },
        telefono: {
            required: true,
            minlength:5
        },
        pw: {
            required: true,
            minlength:8
        },
        data_nascita: {
            required: true,
            minlength:2
        },
        nome: {
            required: true,
            minlength:2
        },
        cognome: {
            required: true,
            minlength:2
        },
        corso_laurea: {
            required: true,
            minlength: 2
        }
    },
    messages: {
        codice_fiscale: {
            required: "Il codice fiscale &egrave; richiesto",
            minlength: "Il codice fiscale &egrave; formato da 16 caratteri",
            maxlength: "Il codice fiscale &egrave; formato da 16 caratteri"
        },
        handicap: {
            required: "Specificare l'handicap &egrave; richiesto",
        },
        cap_residenza: {
            required: "Il cap &egrave; richiesto",
            minlength: "Il cap è formato da 5 cifre",
            maxlength: "Il cap è formato da 5 cifre"
        },
        provincia_residenza: {
            required: "La provincia di residenza &egrave; richiesta",
            minlength: "La provincia è formata da 2 carattere",
            maxlength: "La provincia è formata da 2 carattere"
        },
        provincia_nascita: {
            required: "La provincia di nascita &egrave; richiesta",
            minlength: "La provincia è formata da 2 carattere",
            maxlength: "La provincia è formata da 2 carattere"
        },
        citta_residenza: {
            required: "La citt&agrave; &egrave; richiesto",
            minlength: "Inserisci almeno 2 caratteri"
        },
        citta_nascita: {
            required: "La citt&agrave; &egrave; richiesto",
            minlength: "Inserisci almeno 2 caratteri"
        },
        email: {
            required: "L'Email &egrave; richiesta",
            minlength: "Inserire almeno 5 caratteri"
        },
        username: {
            required: "L'username &egrave; richiesta",
            minlength: "Inserire almeno 2 caratteri"
        },
        pw: {
            required: "La password &egrave; richiesta",
            minlength: "Inserire almeno 8 caratteri"
        },
        telefono: {
            required: "Il telefono &egrave; richiesto",
            minlength: "Deve essere lungo almeno 5 caratteri"
        },
        data_nascita: {
            required: "La data di nascita &egrave; richiesta",
            minlength: "Deve essere lunga almeno 2 caratteri"
        },
        nome: {
            required: "Nome richiesto",
            minlength: "Deve essere lungo almeno 2 caratteri"
        },
        cognome: {
            required: "Cognome richiesto",
            minlength: "Deve essere lungo almeno 2 caratteri"
        },
        corso_laurea: {
            required: "Corso di laurea richiesto",
            minlength: "Deve essere lungo almeno 2 caratteri"
        }

    },

    errorElement: "div",
    errorPlacement: function ( error, element ) {
        // Add the `help-block` class to the error element
        error.addClass( "invalid-feedback" );
        if ( element.prop( "type" ) === "checkbox" ) {
            error.insertAfter( element.parent( "label" ) );
        } else {
            error.insertAfter( element );
        }
    },
    highlight: function ( element, errorClass, validClass ) {
        $( element ).addClass( "is-invalid" ).removeClass( "is-valid" );
    },
    unhighlight: function (element, errorClass, validClass) {
        $( element ).addClass( "is-valid" ).removeClass( "is-invalid" );
    },
    submitHandler: function(form) { // <- pass 'form' argument in
        $(".submit").attr("disabled", true);
        form.submit(); // <- use 'form' argument here.
    },

});
