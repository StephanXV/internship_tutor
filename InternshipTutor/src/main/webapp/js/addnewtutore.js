/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

 function change($i){
     remove("#new_tutore");
     if ($i === "new") {
             add_tutore();
     } else {
         select_tutore($i);
     }
   }
   
   function select_tutore($j){
       var input = $("<input>")
               .attr("type", "hidden")
               .attr("name", "id_tutore").val($j);
       $('#richiestaForm').append($(input));
   }


function add_tutore() {
           var input = $("<input>")
               .attr("type", "hidden")
               .attr("name", "id_tutore").val("add");
       $('#richiestaForm').append($(input));
    $("#new_tutore").append("<input name=\"nome_tutore\" placeholder=\"Nome\" type=\"text\" required/>")
            .append("<input name=\"cognome_tutore\" placeholder=\"Cognome\" type=\"text\" required/>")
            .append("<input name=\"email_tutore\" placeholder=\"Email\" type=\"email\" required/>")
            .append("<input name=\"telefono_tutore\" placeholder=\"Telefono\" type=\"tel\" required/>");

}
