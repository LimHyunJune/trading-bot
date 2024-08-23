var REST_POST_URL = setRestUrl();

$( document ).ready(function() {

});

function setRestUrl(){
	var wholeUrl = window.location.href;
	var url = "http://"+wholeUrl.split("/")[2];
	return url;
}

function getView(func){
	location.href = REST_POST_URL+"/"+func;

}