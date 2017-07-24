// JavaScript Document
//id为iframe的id。
setIframeHeight("myiframe");
function setIframeHeight(myiframe){
	try{
		var iframe = document.getElementById(myiframe);
		//alert(iframe.attachEvent);
		if(iframe.attachEvent!=undefined){
			iframe.attachEvent("onload", function(){
				//console.log(iframe.contentWindow.document.documentElement.scrollHeight);
				iframe.height =  iframe.contentWindow.document.documentElement.scrollHeight;
				iframe.setAttribute("scrolling","no");
				//iframe.scrolling="no";
			});
			return;
		}else{
			iframe.onload = function(){
				//console.log(iframe.contentDocument.body.scrollHeight);
				iframe.height = iframe.contentDocument.body.scrollHeight;
				iframe.setAttribute("scrolling","no");
				//iframe.scrolling="no";
			};
			return;				 
		}	 
	}catch(e){
		alert(e);
		throw new Error('setIframeHeight Error');
	}
}