// JavaScript Document
//用户信息展示
function powerInforShow(page){
	search_click=false;
	$.ajax({
		type:"post",
		url:"/power/selectAllPower",
		data:{
			start:(parseInt(10*page-10)),
			limit:10
		},
		dataType:"json",
		success: function(msg){
			//console.log(msg);
			if( msg.status == "OK"){
				// alert("success");
				var items = msg.result ;
				var cookie_value1;
				var cookie_value2;
				var cookie_value3;
				$('.infor_tab02 tr:not(:first)').html("");
				$.each(items,function(idx,item) {
					// alert(msg.tagName);
					cookie_value1="'"+item.powerId+"'";
					cookie_value2="'"+item.powerName+"'";
					cookie_value3="'"+item.powerUrl+"'";
					row= '<tr><td width="169" height="40" align="center" bgcolor="#ffffff">'+((page-1)*10+idx+1)+'</td><td width="231" height="40" align="center" bgcolor="#ffffff">'+item.powerName+'</td><td colspan="2" width="140" height="40" align="center" bgcolor="#ffffff"><button type="button" class="btn btn-primary" onClick="setCookie('+cookie_value1+','+cookie_value2+','+cookie_value3+')">编辑</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button type="button" class="btn btn-danger delPower" id="'+item.powerId+'" >删除</button></td></tr>'
					$('.infor_tab02').append(row);
					
				});
			}else{
				// /alert("fail");
				$('.infor_tab02 tr:not(:first)').html("");
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
        url: "/power/selectPowerCount",
		dataType:"json",
        success: function (msg) {
            if (msg.status == "OK") {
                // alert("success");
                listCount = msg.result;
                $("#page").initPage(listCount,currenPage,powerInforShow);
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
    var powerInfor=$("#power_Search").val();
    $.ajax({
        type: "post",
        url: "/power/selectPowerCount",
        data:{
        	powerName:powerInfor
		},
        dataType: "json",
        success: function (msg) {
            if (msg.status == "OK") {
                // alert("success");
                listCount = msg.result;
                $("#page").initPage(listCount,currenPage,powerInforSearch);
            } else {
                alert(msg.result);
            }
        },
        error: function(msg){
			alert(eval('(' + msg.responseText + ')').result);
        },})
}

function setCookie(value1,value2,value3){
	// alert(name+value);
	var cookie_name1="id";
	var cookie_name2="powerName";
	var cookie_name3="url";
	var Days = 1; // 此 cookie 将被保存 1 天
	var exp　= new Date();
	exp.setTime(exp.getTime() +Days*24*60*60*1000);
	document.cookie = cookie_name1 +"="+ escape (value1) + ";expires=" + exp.toGMTString();
	document.cookie = cookie_name2 +"="+ escape (value2) + ";expires=" + exp.toGMTString();
	document.cookie = cookie_name3 +"="+ escape (value3) + ";expires=" + exp.toGMTString();
	baseAjax("power_change");
}


// 信息搜索
function powerInforSearch(page){
	search_click=true
	var powerInfor=$("#power_Search").val();
//	setFirstSelected();
	$.ajax({
		type:"post",
		url:"/power/selectOnePowerInfo",
		data:{
			powerName:powerInfor,
			start:(parseInt(10*page-10)),
			limit:10
		},
		dataType:"json",
		success: function(msg){
			$(".infor_tab02 tr:not(:first)").html("");
			if( msg.status == "OK"){
				// alert("success");
				var items = msg.result ;
				var cookie_value1;
				var cookie_value2;
				var cookie_value3;
				$.each(items,function(idx,item) {
					// alert(msg.tagName);
					cookie_value1="'"+item.powerId+"'";
					cookie_value2="'"+item.powerName+"'";
					cookie_value3="'"+item.powerUrl+"'";
					row= '<tr><td width="169" height="40" align="center" bgcolor="#ffffff">'+((page-1)*10+idx+1)+'</td><td width="231" height="40" align="center" bgcolor="#ffffff">'+item.powerName+'</td><td colspan="2" width="140" height="40" align="center" bgcolor="#ffffff"><button type="button" class="btn btn-primary" onClick="setCookie('+cookie_value1+','+cookie_value2+','+cookie_value3+')">编辑</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button type="button" class="btn btn-danger delPower" id="'+item.powerId+'" >删除</button></td></tr>'
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
function powerInforAdd() {
	baseAjax("power_add");
}
function addPower(){
	$.ajax({
		type:"post",
		url:"/power/insertPowerInfo",
		data:{
			powerName:$("#namePower").val(),
			powerUrl:$("#urlPower").val()
		},
		dataType:"json",
		success: function(msg){
			//console.log(msg);
			if( msg.status == "OK"){
				baseAjax("power_infor");
			}else{
				alert(msg.result);
			}
		},
		error: function(msg){
			alert(eval('(' + msg.responseText + ')').result);
		}
	})	
}

function clearPower(){
	$("#namePower").val('');
	$("#urlPower").val('');
}


// 用户编辑
function getCookie(name) {
	
	//console.log(document.cookie);
	var arr =document.cookie.match(new RegExp("(^|)"+name+"=([^;]*)(;|$)"));
	if(arr !=null) 
		return unescape(arr[2]); 
	return null;
}

function powerInforChange(){
	var newId=getCookie("id");
	$.ajax({
		type:"post",
		url:"/power/updatePowerInfo",
		data:{
			powerId:newId,
			powerName:$("#new_name_power").val(),
			powerUrl:$("#new_url_power").val()	
		},
		dataType:"json",
		success: function(msg){
			//console.log(msg);
			if( msg.status == "OK"){
				alert("修改成功");
				baseAjax("power_infor");
			}else{
				alert(msg.result);
			}
		},
		error: function(msg){
			alert(eval('(' + msg.responseText + ')').result);
		}
	})	
}
function clearNewPower(){
	$("#new_name_power").val('');
	$("#new_url_power").val('');
}

// 用户删除
$(function(){
	$(".infor_tab02").on("click",".delPower",function(){
		var power_id = $(this).attr("id");
		//console.log(power_id);
		powerInforDel(power_id);
		function powerInforDel(power_id){
			$.ajax({
				type:"post",
				url:"/power/deletePowerById",
				data:{
					powerId:power_id,
				} ,
				dataType:"json",
				success:function(msg){
					if(msg.status=="OK"){
						baseAjax("power_infor");
					}else{
						alert(msg.result);
					}
				} ,
				error:function(msg){
					alert(eval('(' + msg.responseText + ')').result);
				}
			});
		}
	})
})