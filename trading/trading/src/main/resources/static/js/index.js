$( document ).ready(function() {
});

function signIn()
{
	$('#validAppKey').hide();
	$('#validAppSecret').hide();
	$('#validAnalyticsServer').hide();

	var appKey = $('#appKey').val();
	var appSecret = $('#appSecret').val();
	var approvalKey = $('#approvalKey').val();
	var accessToken = $('#accessToken').val();
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
        return;
    }


	var data ={
			appKey : appKey,
			appSecret : appSecret,
			approvalKey : approvalKey,
			accessToken : accessToken,
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
            getView('run');
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