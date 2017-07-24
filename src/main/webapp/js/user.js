// JavaScript Document
//用户信息展示
function userInforShow(page){
	search_click=false;
	$.ajax({
		type:"get",
		url:"/user/selectAllUser",
		data:{
			start:(parseInt(10*page-10)),
			limit:10
		},
		dataType:"json",
		success: function(msg){
			$('.infor_tab02 tr:not(:first)').html("");
			if( msg.status == "OK"){
				// alert("success");
				var items = msg.result.user ;
				var array_userRole=msg.result.userRole;
				var array_role=msg.result.role;
				var cookie_value1;
				var cookie_value2;
				var cookie_value3;
				var cookie_value4;
				var cookie_value5;
				var cookie_value6;
				var cookie_value7;
				$.each(items,function(idx,item) {
					// alert(msg.tagName);
					cookie_value1="'"+item.userName+"'";
					cookie_value2="'"+item.trueName+"'";
					cookie_value3="'"+item.telphone+"'";
					cookie_value4="'"+item.email+"'";
					cookie_value5="'"+getRoleName(item.userId,array_userRole,array_role)+"'";
					cookie_value6="'"+item.userId+"'";
					cookie_value7="'"+item.password+"'";
					row= '<tr><td width="66" height="40" align="center" bgcolor="#ffffff">'+((page-1)*10+idx+1)+'</td><td width="64" height="40" align="center" bgcolor="#ffffff">'+item.userName+'</td><td width="66" height="40" align="center" bgcolor="#ffffff">'+item.trueName+'</td><td width="67" height="40" align="center" bgcolor="#ffffff">'+getRoleName(item.userId,array_userRole,array_role)+'</td><td width="104" height="40" align="center" bgcolor="#ffffff">'+item.telphone+'</td><td width="157" height="40" align="center" bgcolor="#ffffff">'+item.email+'</td><td width="130" height="40" align="center" bgcolor="#ffffff"><button type="button" class="btn btn-primary" onClick="setCookie('+cookie_value1+','+cookie_value2+','+cookie_value3+','+cookie_value4+','+cookie_value5+','+cookie_value6+','+cookie_value7+')">编辑</button>&nbsp;&nbsp;&nbsp;<button type="button" class="btn btn-danger delUser"  id="'+item.userId+'" >删除</button></td></tr>'
					$('.infor_tab02').append(row);
				});
			}else{
				 alert(msg.result);
			}
		},
		error: function(msg){
			alert(eval('(' + msg.responseText + ')').result);
        },
	})	
}
function initShowPage(currenPage){
    var listCount = 0;
    if("undefined" == typeof(currenPage) || null == currenPage){
        currenPage = 1;
    }
    $.ajax({
        type: "post",
        url: "/user/selectUserInfoCount",
		dataType:"json",
        success: function (msg) {
            if (msg.status == "OK") {
                // alert("success");
                listCount = msg.result;
                $("#page").initPage(listCount,currenPage,userInforShow);
            } else {
                alert(msg.result);
            }
        },
        error: function(msg){
			alert(eval('(' + msg.responseText + ')').result);
        },})
}

initShowPage(1)

function initSearchPage(currenPage){
    var listCount = 0;
    if("undefined" == typeof(currenPage) || null == currenPage){
        currenPage = 1;
    }
    var obj1 = $('#user_uname').val();
	var obj2 = $('#user_tname').val();
	var obj3 = $('#user_role').val();
	var obj4 = $('#user_tel').val();
	var obj5 = $('#user_email').val();
    $.ajax({
        type: "post",
        url: "/user/selectUserInfoCount",
        data:{
			userName:obj1,
			trueName:obj2,
			roleName:obj3,
			telphone:obj4,
			email:obj5
		},
        dataType: "json",
        success: function (msg) {
            if (msg.status == "OK") {
                // alert("success");
                listCount = msg.result;
                $("#page").initPage(listCount,currenPage,userInforSearch);
            } else {
                alert(msg.result);
            }
        },
        error: function(msg){
			alert(eval('(' + msg.responseText + ')').result);
        },})
}
function getRoleName(userId,array_userRole,array_role){
	var name=null;
	try{
		$.each(array_userRole, function(i,item) {
			if(item.userId==userId){
				var roleId=item.roleId;
				// console.log(roleId);
				if(roleId>=0){
					name = getRoleNames(roleId,array_role);
				}else{
					throw new Error('getRoleName Error null');
				}
			}
		});
	}catch(e){
		// TODO handle the exception
	}
	return name;
}
function getRoleNames(id,array){
	var name;
	$.each(array, function(i,item) {
		// console.log(id+", "+item.roleId);
		if(item.roleId==id){
			// var name=item.roleName;
			name=item.roleName;
			// return item.roleName;
		}
	});
	// console.log(name);
	return name;
}
function setCookie(value1,value2,value3,value4,value5,value6,value7){
	// alert(name+value);
	var cookie_name1="userName";
	var cookie_name2="trueName";
	var cookie_name3="telphone";
	var cookie_name4="email";
	var cookie_name5="roleName";
	var cookie_name6="userId";
	var cookie_name7="passWord";
	var Days = 1; // 此 cookie 将被保存 1 天
	var exp　= new Date();
	exp.setTime(exp.getTime() +Days*24*60*60*1000);
	document.cookie = cookie_name1 +"="+ escape (value1) + ";expires=" + exp.toGMTString();
	document.cookie = cookie_name2 +"="+ escape (value2) + ";expires=" + exp.toGMTString();
	document.cookie = cookie_name3 +"="+ escape (value3) + ";expires=" + exp.toGMTString();
	document.cookie = cookie_name4 +"="+ escape (value4) + ";expires=" + exp.toGMTString();
	document.cookie = cookie_name5 +"="+ escape (value5) + ";expires=" + exp.toGMTString();
	document.cookie = cookie_name6 +"="+ escape (value6) + ";expires=" + exp.toGMTString();
	document.cookie = cookie_name7 +"="+ escape (value7) + ";expires=" + exp.toGMTString();
	if(value5 == '超级管理员'){
		alert("对不起，不能修改超级管理员信息");
		return;
	}else if(value5 == '管理员'){
		alert("对不起，不能修改管理员信息");
		return;
	}
	baseAjax("user_change");
}


// 信息搜索
function userInforSearch(page){
	search_click=true;
	var obj1 = $('#user_uname').val();
	var obj2 = $('#user_tname').val();
	var obj3 = $('#user_role').val();
	var obj4 = $('#user_tel').val();
	var obj5 = $('#user_email').val();
//	console.log(obj1)
	$.ajax({
		type:"post",
		url:"/user/getUserInfoByPageLimit",
		data:{
			userName:obj1,
			trueName:obj2,
			roleName:obj3,
			telphone:obj4,
			email:obj5,
			page:10,
			row:(parseInt(10*page-10))
		},
		dataType:"json",
		success: function(msg){
		    $('.infor_tab02 tr:not(:first)').html("");
			if( msg.status == "OK"){
				var items = msg.result.user ;
				var array_userRole=msg.result.userRole;
				var array_role=msg.result.role;
				var cookie_value1;
				var cookie_value2;
				var cookie_value3;
				var cookie_value4;
				var cookie_value5;
				var cookie_value6;
				var cookie_value7;
				$.each(items,function(idx,item) {
					// alert(msg.tagName);
					cookie_value1="'"+item.userName+"'";
					cookie_value2="'"+item.trueName+"'";
					cookie_value3="'"+item.telphone+"'";
					cookie_value4="'"+item.email+"'";
					cookie_value5="'"+getRoleName(item.userId,array_userRole,array_role)+"'";
					cookie_value6="'"+item.userId+"'";
					cookie_value7="'"+item.password+"'";
					row= '<tr><td width="66" height="40" align="center" bgcolor="#ffffff">'+((page-1)*10+idx+1)+'</td><td width="64" height="40" align="center" bgcolor="#ffffff">'+item.userName+'</td><td width="66" height="40" align="center" bgcolor="#ffffff">'+item.trueName+'</td><td width="67" height="40" align="center" bgcolor="#ffffff">'+getRoleName(item.userId,array_userRole,array_role)+'</td><td width="104" height="40" align="center" bgcolor="#ffffff">'+item.telphone+'</td><td width="157" height="40" align="center" bgcolor="#ffffff">'+item.email+'</td><td width="130" height="40" align="center" bgcolor="#ffffff"><button type="button" class="btn btn-primary" onClick="setCookie('+cookie_value1+','+cookie_value2+','+cookie_value3+','+cookie_value4+','+cookie_value5+','+cookie_value6+','+cookie_value7+')" >编辑</button>&nbsp;&nbsp;&nbsp;<button type="button" class="btn btn-danger delUser"  id="'+item.userId+'" >删除</button></td></tr>'
					$('.infor_tab02').append(row);
				});
			}else{
				 alert(msg.result);
			}
		},
		error: function(msg){
			alert(eval('(' + msg.responseText + ')').result);
        },
	})	
}


// 用户添加
function userInforAdd(){
	baseAjax("user_add");
}
function addUser(){
	if(!$("#passWord").val().match(/^[\w]{6,30}$/)){
		$("#warnPassword").html("密码必须介于6-30位之间"); 
		$("#passWord").focus(); 
		return false; 
	} 
	if(!$("#userTel").val().match(/^(1[3-8][0-9]{9})$/)){
		$("#warnTel").html("电话号码必须为11位！"); 
		$("#userTel").focus(); 
		return false; 
	} 
	// /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/
	if (!$("#userEmail").val().match(/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/)) { 
		// alert("邮箱格式不正确");
		$("#warnEmail").html("邮箱必须符合邮箱规范！"); 
		$("#userEmail").focus(); 
		return false; 
	}
	
	var name = $("#userName").val().replace(' ','');
	if(name===undefined||name==''){
        alert('请输入正确信息');
        return;
    }
	var trueName = $("#trueName").val().replace(' ','');
	if(trueName===undefined||trueName==''){
        alert('请输入正确信息');
        return;
    }
	var rolename = $("#select_roleName option:selected").val();
	if(rolename===undefined||rolename == null ||rolename=='null'||rolename==''||rolename=='请选择角色'){
        alert('角色为空，请重新选择');
        return;
    }
	$.ajax({
		type:"post",
		url:"/user/insertUserInfo",
		data:{
			userName:$("#userName").val(),
			trueName:$("#trueName").val(),
			password:$("#passWord").val(),
			telphone:$("#userTel").val(),
			email:$("#userEmail").val(),
			roleName:$("#select_roleName option:selected").val()
		},
		dataType:"json",
		success: function(msg){
//			console.log(msg);
			if( msg.status == "OK"){
			    baseAjax("user_infor");
			}else{
				alert(msg.result);
			}
		},
		error: function(msg){
			alert(eval('(' + msg.responseText + ')').result);
        },
	})	
}
function clearUserInfor(){
	$("#userName").val('');
	$("#trueName").val('');
	$("#passWord").val('');
	$("#userTel").val('');
	$("#userEmail").val('');
	$("#select_roleName option:selected").val('请选择角色');
}

// 用户编辑
function getCookie(name) {
	
//	console.log(document.cookie);
	var arr =document.cookie.match(new RegExp("(^|)"+name+"=([^;]*)(;|$)"));
	if(arr !=null) 
		return unescape(arr[2]); 
	return null;
}

function userInforChange(){
	if(!$("#new_telphone_type").val().match(/^(1[3-8][0-9]{9})$/)){
		$("#tel_warn").html("电话号码必须为11位！"); 
		$("#new_telphone_type").focus(); 
		return false; 
	} 
	// /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/
	if (!$("#new_email_type").val().match(/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/)) { 
		// alert("邮箱格式不正确");
		$("#email_warn").html("邮箱必须符合邮箱规范！"); 
		$("#new_email_type").focus(); 
		return false; 
	}
	var rolename = $("#select_roleName option:selected").val();
	
	if(rolename===undefined||rolename == null ||rolename=='null'||rolename==''||rolename=='请选择角色'){
        alert('角色为空，请重新选择');
        return;
    }
	var newRole=getCookie("roleName");
	var newId=getCookie("userId");
	var newPassword=getCookie("passWord");
	$.ajax({
		type:"post",
		url:"/user/updateUserInfo",
		data:{
			userId:newId,
			userName:$("#new_user_type").val(),
			trueName:$("#new_true_type").val(),
			password:$("#new_user_type").val(),
			telphone:$("#new_telphone_type").val(),
			email:$("#new_email_type").val(),
			roleName:$("#select_roleName option:selected").val(),
		},
		dataType:"json",
		success: function(msg){
//			console.log(msg);
			if( msg.status == "OK"){
				alert("用户信息更新成功");
				// alert(msg.result);
				baseAjax("user_infor");
			}else{
				alert(msg.result);
			}
		},
		error: function(msg){
			alert(eval('(' + msg.responseText + ')').result);
        },
	})	
}
function clearChangeInfor(){
	$("#new_user_type").val('');
	$("#new_true_type").val('');
	$("#new_telphone_type").val('');
	$("#new_email_type").val('');
	$("#select_roleName option:selected").val('请选择角色');
}

// 获取当前用户ID
function CurrentUserId() {
	var userId = null;
   $.ajax({
       url : "/getCurrentUserId",
       type : "post",
       data : "",
       async: false,// 同步
       success : function(msg) {
           if (msg.status == 'OK') {
               userId = msg.result;
           }
       },
       error : function(msg) {
    	   alert(eval('(' + msg.responseText + ')').result);
        }
    });
    return userId;
    // window.onload = user();
 }

// 删除用户判断是否删除的是当前登录用户,用户是否为管理员
$(function(){
	$(".infor_tab02").on("click",".delUser",function(){
		var user_id = $(this).attr("id");
		var rolename = $(this).parents('tr').find("td").eq('3').text();
		
		var currentUserId = CurrentUserId();
		
		if(user_id == currentUserId){
			alert('对不起，不能删除当前登录用户');
		}else if(rolename == "超级管理员"){
			alert('对不起，不能删除管理员');			
		}else{
			userInforDel(user_id);
		}
	})
})
// 用户删除
function userInforDel(user_id){	
	$.ajax({
		type:"post",
		url:"/user/deleteUserInfoById",
		data:{
			userId:user_id,
		} ,
		dataType:"json",
		success:function(msg){
			
			if(msg.status=="OK"){
				baseAjax("user_infor");
			}else{
				alert(msg.result);
			}

		} ,
		error: function(msg){
			alert(eval('(' + msg.responseText + ')').result);
        },
	});
}