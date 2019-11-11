$('.click_to_scroll').click(function(){
    var clickedId = $(this).attr('href');
    $('html, body').animate({ scrollTop: ($(clickedId).offset().top)} , 1000);
    return false;
});