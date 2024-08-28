function start_selling()
{
    $('#current-status').text("매도 분석 중...");
    $.ajax({
            url: 'sell',
            type: 'GET',
            contentType: 'application/json',
            xhrFields: {
                withCredentials: true
            },
            success: function(response) {
                alert('팔아!!!!!!!!!!!!!!!!!!!!!!!!!!!!!');
                alert("NEXT ROUND START");
                start_selection();
            },
            failure: function( response ) {
               alert('fail');
            }
        });
}

function start_buying()
{
    $('#current-status').text("매수 분석 중...");
    $.ajax({
            url: 'buy',
            type: 'GET',
            contentType: 'application/json',
            xhrFields: {
                withCredentials: true
            },
            success: function(response) {
                alert('사!!!!!!!!!!!!!!!!!!!!!!!!!!!!!');
                start_selling();
            },
            failure: function( response ) {
               alert('fail');
            }
        });
}

function start_selection()
{
    $('#current-status').text("종목 선정 중...");
    $.ajax({
            url: 'select',
            type: 'GET',
            contentType: 'application/json',
            xhrFields: {
                withCredentials: true
            },
            success: function(response) {
                alert(response);
                start_buying();
            },
            failure: function( response ) {
               alert('fail');
            }
        });
}


$( document ).ready(function() {
    start_selection();
});