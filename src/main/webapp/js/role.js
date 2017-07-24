// JavaScript Document
//用户信息展示
function roleInforShow(page){
	search_click=false;
	$.ajax({
		type:"post",
		url:"/role/selectAllRole",
		data:{
			start:(parseInt(10*page-10)),
			limit:10
		},
		dataType:"json",
		success: function(msg){
			if(msg.status=="OK"){
				var items=msg.result;
				var cookie_value1;
				var cookie_value2;
				$('#role_infor_tab tr:not(:first)').html("");
				
				$.each(items,function(idx,item){
					cookie_value1="'"+item.roleId+"'";
					cookie_value2="'"+item.roleName+"'";
					row = '<tr><td width="169" height="40" align="center" bgcolor="#ffffff">'+((page-1)*10+idx+1)+'</td><td width="231" height="40" align="center" bgcolor="#ffffff">'+item.roleName+'</td><td colspan="2" width="140" height="40" align="center" bgcolor="#ffffff"><button type="button" class="btn btn-primary" onClick="setCookie('+cookie_value1+','+cookie_value2+')">编辑</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button type="button" class="btn btn-danger delRole" id="'+item.roleId+'">删除</button></td></tr>'
					$('#role_infor_tab').append(row);
				});
			}else{
				$('#role_infor_tab tr:not(:first)').html("");
			}
		},
		complete : function(){
		//	console.log("all")
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
        url: "/role/selectRoleInfoCount",
		dataType:"json",
        success: function (msg) {
            if (msg.status == "OK") {
                // alert("success");
                listCount = msg.result;
                $("#page").initPage(listCount,currenPage,roleInforShow);
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
    var roleInfo = $("#searchRole").val();
    $.ajax({
        type: "post",
        url: "/role/selectRoleInfoCount",
        data:{
        	roleName:roleInfo
		},
        dataType: "json",
        success: function (msg) {
            if (msg.status == "OK") {
                // alert("success");
                listCount = msg.result;
                $("#page").initPage(listCount,currenPage,roleInforSearch);
            } else {
                alert(msg.result);
            }
        },
        error: function(msg){
			alert(eval('(' + msg.responseText + ')').result);
        },})
}
function setCookie(value1,value2){
	// alert(name+value);
	var cookie_name1="id";
	var cookie_name2="role_name";
	var Days = 1; // 此 cookie 将被保存 1 天
	var exp　= new Date();
	exp.setTime(exp.getTime() +Days*24*60*60*1000);
	document.cookie = cookie_name1 +"="+ escape (value1) + ";expires=" + exp.toGMTString();
	document.cookie = cookie_name2 +"="+ escape (value2) + ";expires=" + exp.toGMTString();
//	if(value2 == '超级管理员'){
//		alert("对不起，不能修改超级管理员信息");
//		return;
//	}else 
//		if(value2 == '管理员'){
//		alert("对不起，不能修改管理员信息");
//		return;
//	}
	baseAjax("role_change");
}



// 信息搜索
function roleInforSearch(page){
	var roleInfo = $("#searchRole").val();
//	search_click=true;
	$.ajax({
		type:"post",
		url:"/role/selectOneRoleInfo",
		data:{
			roleName:roleInfo,
			start:(parseInt(10*page)-10),
			limit:10
		},
		dataType:"json",
		success: function(msg){
			if( msg.status == "OK"){
				$('#role_infor_tab tr:not(:first)').html("");
				var items=msg.result;
				var cookie_value1;
				var cookie_value2;
				$.each(items,function(idx,item){
					cookie_value1="'"+item.roleId+"'";
					cookie_value2="'"+item.roleName+"'";
					row = '<tr><td width="169" height="40" align="center" bgcolor="#ffffff">'+((page-1)*10+idx+1)+'</td><td width="231" height="40" align="center" bgcolor="#ffffff">'+item.roleName+'</td><td colspan="2" width="140" height="40" align="center" bgcolor="#ffffff"><button type="button" class="btn btn-primary" onClick="setCookie('+cookie_value1+','+cookie_value2+')">编辑</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button type="button" class="btn btn-danger delRole" id="'+item.roleId+'">删除</button></td></tr>'
					$('#role_infor_tab').append(row);
				});
			}else{
				$('#role_infor_tab tr:not(:first)').html("");
				alert("输入角色名有误，请重新输入");
				//roleInforShow(1)
			}
		},
		complete : function(){
		//	console.log("one")
           // stop();
        },
        error: function(msg){
			alert(eval('(' + msg.responseText + ')').result);
        },
	})	
}


// 角色添加
function roleInforAdd() {
	baseAjax("role_add");
}

function addRoleInfo(){
//	console.log($(".addRole").val())
	$.ajax({
		type:"post",
		url:"/role/insertRoleInfo",
		data:{
			roleName:$(".addRole").val()
		},
		dataType:"json",
		beforeSend : function(){
            begin();
        },
		success: function(msg){
			if( msg.status == "OK"){
				baseAjax("role_infor");
			}else{
				alert(msg.result);
			}
		},
		complete:function(){
            stop();
        },
        error: function(msg){
			alert(eval('(' + msg.responseText + ')').result);
        },
	})
}

function clearRole(){
	$(".addRole").val('');
}

// 角色编辑
function getCookie(name) {
	
//	console.log(document.cookie);
	var arr =document.cookie.match(new RegExp("(^|)"+name+"=([^;]*)(;|$)"));
	if(arr !=null) 
		return unescape(arr[2]); 
	return null;
}



//角色删除
$(function(){
	$("#role_infor_tab").on("click",".delRole",function(){
		var role_id = $(this).attr("id");
		var role_name = $(this).parents('tr').find("td").eq('1').text();
		
		if(role_name == "超级管理员"){
			alert('对不起，超级管理员角色不能被删除！');
			return;
		}
		if(role_name == "管理员"){
			alert('对不起，管理员角色不能被删除！');
			return;
		}
		roleInforDel(role_id);
		function roleInforDel(role_id){
			$.ajax({
				type:"post",
				url:"/role/deleteRoleInfoById",
				data:{
					roleId:role_id
				} ,
				dataType:"json",
				success:function(msg){
					if(msg.status=="OK"){
					    baseAjax("role_infor");
					}else{
						alert(msg.result);
					}
				},
				error: function(msg){
					alert(eval('(' + msg.responseText + ')').result);
		        }
			});
		}
	})
})