function showListDetail(index) {
	
    var indexNum = parseInt(index) - 1;
    if (isNaN(indexNum)) {
    	return;
    }
    var itemId = "#detail-list" + indexNum;
    while ($(itemId).offset() === undefined && indexNum > 0) {
    	indexNum = indexNum - 1;
    	itemId = "#detail-list" + indexNum;
    }
    var itemTop = $(itemId).offset().top - $("#detail-list1").offset().top;
    $('html, body').animate({scrollTop: 105 + itemTop});
}

function preMonth() {
	document.changeMonth.mode.value = "p";
	document.changeMonth.submit();
}

function nextMonth() {
	document.changeMonth.mode.value = "l";
	document.changeMonth.submit();
}
function hideCalendar() {
	$('.calendar-div').addClass('calendar-hide');
	$('#tohide-btn').addClass('hide');
	$('#toshow-btn').removeClass('hide');
}
function showCalendar() {
	$('.calendar-div').removeClass('calendar-hide');
	$('#tohide-btn').removeClass('hide');
	$('#toshow-btn').addClass('hide');
}