$( document ).ready(function() {
    $('#loading').hide();
});

function signIn()
{
	$('#validAppKey').hide();
	$('#validAppSecret').hide();

	var appKey = $('#appKey').val();
	var appSecret = $('#appSecret').val();
	var analyticsServer = $('#analyticsServer').val();


    if(appKey == "")
    {
        $('#validAppKey').show();
        return;
    }

    if(appSecret == "")
    {
        $('#validAppSecret').show();
        return;
    }

    if(analyticsServer == "")
    {
        $('#validAnalyticsServer').show();
    }


	var data ={
			appKey : appKey,
			appSecret : appSecret,
			analyticsServer : analyticsServer
	};


	$.ajax({
        url: 'register',
        type: 'POST',
        data: JSON.stringify(data),
        contentType: 'application/json',
	    xhrFields: {
	        withCredentials: true
	    },
    	success: function(response) {
            alert("success");
            $('#register').hide();
            $('#loading').show();
//            $.ajax({
//                    url: 'election',
//                    type: 'GET',
//                    contentType: 'application/json',
//            	    xhrFields: {
//            	        withCredentials: true
//            	    },
//                	success: function(response) {
//                        alert("success");
//                        getView("stock_election");
//                    },
//                    failure: function( response ) {
//                 	   alert('fail');
//                    }
//            	});
        },
        failure: function( response ) {
     	   alert('fail');
        }
	});
}

function clickEnter(){
	if(window.event.keyCode == 13){
		signIn();
	}
}