/**
 *  账号管理js文件
**/

function setCookie(value1){
	// alert(name+value);
	var cookie_name1="USERID";
	
	var Days = 1; // 此 cookie 将被保存 1 天
	var exp　= new Date();
	exp.setTime(exp.getTime() +Days*24*60*60*1000);
	document.cookie = cookie_name1 +"="+ escape (value1) + ";expires=" + exp.toGMTString();
}
function getCookie(name) {
//	console.log(document.cookie);
	var arr =document.cookie.match(new RegExp("(^|)"+name+"=([^;]*)(;|$)"));
	if(arr !=null) 
		return unescape(arr[2]); 
	return null;
}

//个人信息显示
function personalInforShow(){
	$.ajax({
		type:"GET",
		url:"/personal/getPersonalInfo",
		dataType:"json",
		success : function(msg){
			if( msg.status == "OK"){
				var user = msg.result;
				setCookie(user.userId);
				//$("#new_userName").val(user.userName);
				$("#new_trueName").val(user.trueName);
				$("#new_telphone").val(user.telphone);
				$("#new_email").val(user.email);
			}else{
				alert(msg.result);
			}
		},
		error : function(msg){
			alert(msg.result);
		},
	})
		
}

personalInforShow();
//置空个人信息
function clearChangeInfor(){
	baseAjax('personal_infor');
}
//个人信息修改
function personalInforChange(){
	if(!$("#new_telphone").val().match(/^(1[3-8][0-9]{9})$/)){
		$("#tel_warn").html("电话号码必须为11位！"); 
		$("#new_telphone").focus(); 
		return false; 
	} 
	// /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/
	if (!$("#new_email").val().match(/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/)) { 
		// alert("邮箱格式不正确");
		$("#email_warn").html("邮箱必须符合邮箱规范！"); 
		$("#new_email").focus(); 
		return false; 
	}
	
	$.ajax({
		type:"post",
		url:"/personal/updatePersonalInfo",
		data:{
			userId:getCookie("USERID"),
//			userName:$("#new_userName").val(),
			trueName:$("#new_trueName").val(),
			telphone:$("#new_telphone").val(),
			email:$("#new_email").val(),
		},
		dataType:"json",
		success: function(msg){
//			console.log(msg);
			if( msg.status == "OK"){
				alert(msg.result);
				baseAjax("topic_list");
			}else{
				alert(msg.result);
			}
		},
		error: function(msg){
			alert(msg.result);
        },
	})	
}

//置空密码
function clearPassword(){
	$("#old_pass").val('');
	$("#new_pass").val('');
	$("#confirm_pass").val('');
}

//密码修改
function passwordChange(){
	var newPass = $("#new_pass").val();
	var confirmpass = $("#confirm_pass").val();
	if(!newPass.match(/^[0-9a-zA-Z_#]{6,16}$/)){
		$("#newpass_warn").html("密码必须为6位以上字母、数字或符号"); 
		$("#new_pass").focus(); 
		return false; 
	} 
	if(confirmpass != newPass){
		$("#confirmpass_warn").html("两次密码不一致"); 
		$("#confirm_pass").focus(); 
		return false; 
	} 	
	$.ajax({
		type:"POST",
		url:"/personal/updatePassword",
		data:{
			userId:getCookie("USERID"),
			oldpass:$("#old_pass").val(),
			newpass:newPass
		},
		success: function(msg){
//			console.log(msg);
			if( msg.status == "OK"){
				alert("密码更改成功！");
				baseAjax("topic_list");
			}else{
				alert(msg.result);
			}
		},
		error: function(msg){
			alert(msg.result);
        },
	})
}
