function signIn()
{
	$('#validAppKey').hide();
	$('#validAppSecret').hide();

	var appKey = $('#appKey').val();
	var appSecret = $('#appSecret').val();


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


	var data ={
			appKey : appKey,
			appSecret : appSecret,
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
    		console.log(response);

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