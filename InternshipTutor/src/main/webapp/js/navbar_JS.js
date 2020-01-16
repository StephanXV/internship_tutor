$("div").remove(".divider_menu");

$("li").remove(".profilo-nav");

$("li").remove(".logout-nav");

$("a").remove(".dropdown-nav-noJS");

var element = document.getElementById("navbarTogglerDemo03");
element.className = element.className.replace(/\bcollapseJS\b/g, "");

var element1 = document.getElementById("navbardropJS");
element1.className = element1.className.replace(/\b.dropdown-nav-JS\b/g, "");