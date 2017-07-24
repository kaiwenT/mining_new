// JavaScript Document
//用户信息展示
function weightInforShow(page){
	search_click=false;
	$.ajax({
		type:"post",
		url:"/weight/selectAllWeight",
		data:{
			start:(parseInt(10*page-10)),
			limit:10
		},
		dataType:"json",
		success: function(msg){
			$('.infor_tab02 tr:not(:first)').html("");
			if( msg.status == "OK"){
				// alert("success");
				var items = msg.result ;
				var cookie_value1;
				var cookie_value2;
				var cookie_value3;
				$.each(items,function(idx,item) {
					// alert(msg.tagName);
					cookie_value1="'"+item.id+"'";
					cookie_value2="'"+item.name+"'";
					cookie_value3="'"+item.weight+"'";
					row= '<tr><td width="88" height="40" align="center" bgcolor="#ffffff">'+((page-1)*10+idx+1)+'</td><td width="162" height="40" align="center" bgcolor="#ffffff">'+item.name+'</td><td width="181" height="40" align="center" bgcolor="#ffffff">'+item.weight+'</td><td colspan="2" width="243" height="40" align="center" bgcolor="#ffffff"><button type="button" class="btn b btn-primary" onClick="setCookie('+cookie_value1+','+cookie_value2+','+cookie_value3+')">编辑</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button type="button" class="btn btn-danger delWeight" id="'+item.id+'">删除</button></td></tr>'
					$('.infor_tab02').append(row);
				});
			}else{
				alert(msg.result);
			}
		},
		error: function(){
            alert("数据请求失败");
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
        url: "/weight/selectWeightCount",
        dataType: "json",
        success: function (msg) {
            if (msg.status == "OK") {
                // alert("success");
                listCount = msg.result;
                $("#page").initPage(listCount,currenPage,weightInforShow);
            } else {
                alert(msg.result);
            }
        },
        error: function () {
            alert("数据请求失败");
        }})
}
initShowPage(1)

function initSearchPage(currenPage){
    var listCount = 0;
    if("undefined" == typeof(currenPage) || null == currenPage){
        currenPage = 1;
    }
    var obj1 = $("#stopword_search").val();
    $.ajax({
        type: "post",
        url: "/weight/selectWeightCount",
        data:{
        	name:$("#name").val(),
            weight:$("#weight").val()},
        dataType: "json",
        success: function (msg) {
            if (msg.status == "OK") {
                // alert("success");
                listCount = msg.result;
                $("#page").initPage(listCount,currenPage,weightInforSearch);
            } else {
                alert(msg.result);
            }
        },
        error: function () {
            alert("数据请求失败");
        }})
}
function setCookie(value1,value2,value3){
	// alert(name+value);
	var cookie_name1="id";
	var cookie_name2="weightName";
	var cookie_name3="weight";
	var Days = 1; // 此 cookie 将被保存 1 天
	var exp　= new Date();
	exp.setTime(exp.getTime() +Days*24*60*60*1000);
	document.cookie = cookie_name1 +"="+ escape (value1) + ";expires=" + exp.toGMTString();
	document.cookie = cookie_name2 +"="+ escape (value2) + ";expires=" + exp.toGMTString();
	document.cookie = cookie_name3 +"="+ escape (value3) + ";expires=" + exp.toGMTString();
	baseAjax("weight_change");
}
/*
/!**
 * 根据页码加载数据
 * 
 * @param {整型}
 *            page 页码
 *!/
var search_click;
function setViewForPage(page){
	if(search_click){
		weightInforSearch(page);
	}else{
		weightInforShow(page);
	}
}
/!**
 * 省略号点击
 *!/
function setPageChangeView(){
	var bt_name=parseInt($("#other").attr('name'))+3;
	updatePageValue(bt_name);
	setViewForPage(bt_name);
	setFirstSelected();
	updateNowPage(bt_name);
}
/!**
 * 更新页码数据
 * 
 * @param {Object}
 *            base_num
 *!/
function updatePageValue(base_num){
	var p1=parseInt(base_num);
	var p2=parseInt(base_num)+1;
	var p3=parseInt(base_num)+2;
	$("#p_1").val(p1);
	$("#p_2").val(p2);
	$("#p_3").val(p3);
	$("#other").attr('name',p1);
}
/!**
 * 页码点击
 * 
 * @param {Object}
 *            p_id 页码
 *!/
function pageNumClick(p_id){
	// background: #0e63ab;
    // color: #fff;
	var button=document.getElementById(p_id);
	var page=button.value;
	if(page!=undefined&&page.length>0){
		setViewForPage(page);
		updateNowPage(page);
		// $(this).addClass("cur").siblings().removeClass("cur");
		cleanAllSelected();
		button.style.background='#0e63ab';
		button.style.color='#FFFFFF';
	}
}
/!**
 * 设置第一个页码按钮为选中状态
 *!/
function setFirstSelected(){
	cleanAllSelected();
	$("#p_1").css("background","#0e63ab");
	$("#p_1").css("color","#FFFFFF");
}
function setSecondSelected(){
	cleanAllSelected();
	$("#p_2").css("background","#0e63ab");
	$("#p_2").css("color","#FFFFFF");
}
function setThirdSelected(){
	cleanAllSelected();
	$("#p_3").css("background","#0e63ab");
	$("#p_3").css("color","#FFFFFF");
}
/!**
 * 清除所有的选中状态
 *!/
function cleanAllSelected(){
	$("#p_1").css("background","#CCCCCC");
	$("#p_1").css("color","buttontext");
	$("#p_2").css("background","#CCCCCC");
	$("#p_2").css("color","buttontext");
	$("#p_3").css("background","#CCCCCC");
	$("#p_3").css("color","buttontext");
}
/!**
 * 上一页，下一页点击
 * 
 * @param {Object}
 *            action -1上一页，1下一页
 *!/
function changPageOne(action){
	var now_page=parseInt($("#down_page").attr('name'));
	var page=now_page+action;
	if(page>0){
		updateAllStyleAndData(page,action);
	}
}
/!**
 * 跳zhuan
 *!/
function changePage(){
	var page=$(".go_num").val();
	if(page!=undefined&&page.length>0){
		updateAllStyleAndData(page);
	}
}
function updateAllStyleAndData(page,action){
	updateNowPage(page);
	setViewForPage(page);
	if((page-1)%3==0){// 位置：第一个按钮 123 456 789
		setFirstSelected();
		if(action==1||action==undefined){// 点击下一页
			updatePageValue(page);
		}
	}else if(page%3==0){// 位置：第三个按钮
		setThirdSelected();
		if (action==-1||action==undefined) {// 点击上一页
			updatePageValue(page-2);
		}
	}else{// 位置：第二个按钮
		setSecondSelected();
		if(action==undefined){
			updatePageValue(page-1);
		}
	}
}
/!**
 * 更新当前页码
 * 
 * @param {Object}
 *            page 当前页
 *!/
function updateNowPage(page){
	$("#down_page").attr('name',page);
}*/


// 信息搜索
function weightInforSearch(page){
//	console.log($("#name").val());
//	console.log($("#weight").val());
	search_click=true;
	$.ajax({
		type:"post",
		url:"/weight/selectByCondition",
		data:{
			name:$("#name").val(),
			weight:$("#weight").val(),
			start:(parseInt(10*page-10)),
			limit:10
		},
		dataType:"json",
		success: function(msg){
			$('.infor_tab02 tr:not(:first)').html("");
			if( msg.status == "OK"){
				// alert("success");
				var items = msg.result ;
				var cookie_value1;
				var cookie_value2;
				var cookie_value3;
				$.each(items,function(idx,item) {
					// alert(msg.tagName);
					cookie_value1="'"+item.id+"'";
					cookie_value2="'"+item.name+"'";
					cookie_value3="'"+item.weight+"'";
					row= '<tr><td width="88" height="40" align="center" bgcolor="#ffffff">'+((page-1)*10+idx+1)+'</td><td width="162" height="40" align="center" bgcolor="#ffffff">'+item.name+'</td><td width="181" height="40" align="center" bgcolor="#ffffff">'+item.weight+'</td><td colspan="2" width="243" height="40" align="center" bgcolor="#ffffff"><button type="button" class="btn btn-primary" onClick="setCookie('+cookie_value1+','+cookie_value2+','+cookie_value3+')">编辑</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button type="button" class="btn btn-danger delWeight" id="'+item.id+'" >删除</button></td></tr>'
					$('.infor_tab02').append(row);
				});
			}else{
				alert(msg.result);
			}
		},
		error: function(){
            alert("数据请求失败");
        }
	})	
}


// 用户添加
function weightInforAdd(){
	baseAjax("weight_add");
}
function addWeight(){
	$.ajax({
		type:"post",
		url:"/weight/insertWeight",
		data:{
			name:$("#add_type").val(),
			weight:$("#add_weight").val()
		},
		dataType:"json",
		success: function(msg){
//			console.log(msg);
			if( msg.status == "OK"){
				baseAjax("weight_infor");
			}else{
				alert(msg.result);
			}
		},
		error: function(){
            alert("数据请求失败");
        },
	})	
}

function clearWeight(){
	$("#add_type").val('');
	$("#add_weight").val('');
}



// 用户编辑
function getCookie(name) {
	
	console.log(document.cookie);
	var arr =document.cookie.match(new RegExp("(^|)"+name+"=([^;]*)(;|$)"));
	if(arr !=null) 
		return unescape(arr[2]); 
	return null;
}


function weightInforChange(){
	var newId=getCookie("id");
	$.ajax({
		type:"post",
		url:"/weight/updateWeight",
		data:{
			id:newId,
			name:$("#new_name_weight").val(),
			weight:$("#new_weight_weight").val()
		},
		dataType:"json",
		success: function(msg){
			console.log(msg);
			if( msg.status == "OK"){
				baseAjax("weight_infor");	
			}else{
				alert(msg.result);
			}
		},
		error: function(){
            alert("数据请求失败");
        },
	})	
}
function clearNewWeight(){
	$("#new_name_weight").val('');
	$("#new_weight_weight").val('');
}


// 用户删除
$(function(){
	$(".infor_tab02").on("click",".delWeight",function(){
		var weight_id = $(this).attr("id");
		console.log(weight_id);
		weightInforDel(weight_id);
		
		function weightInforDel(weight_id){
	
			$.ajax({
				type:"post",
				url:"/weight/deleteWeight",
				data:{
					weightId:weight_id,
				} ,
				dataType:"json",
				success:function(msg){
					// alert("lll");
					console.log(msg);
					if(msg.status=="OK"){
						baseAjax("weight_infor");
					}else{
						alert(msg.result);
					}
				},
				complete : function() {
                    stop();
                },
				error: function(){
		            alert("数据请求失败");
		        },
			});
		}
	})
})