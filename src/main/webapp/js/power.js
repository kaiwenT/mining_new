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
			console.log(msg);
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
					row= '<tr><td width="169" height="30" align="center" bgcolor="#ffffff">'+((page-1)*10+idx+1)+'</td><td width="231" height="30" align="center" bgcolor="#ffffff">'+item.powerName+'</td><td colspan="2" width="140" height="30" align="center" bgcolor="#ffffff"><a href="javascript:;"><img src="images/user_bj.png"  onClick="setCookie('+cookie_value1+','+cookie_value2+','+cookie_value3+')"></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;"><img src="images/user_del.png" class="delPower" id="'+item.powerId+'" /></a></td></tr>'
					$('.infor_tab02').append(row);
					
				});
			}else{
				// /alert("fail");
				$('.infor_tab02 tr:not(:first)').html("");
			}
		},
		error: function(){
			
		}
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
        error: function () {
            alert("数据请求失败");
        }})
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

/**
 * 根据页码加载数据
 * 
 * @param {整型}
 *            page 页码
 */
var search_click;
function setViewForPage(page){
	if(search_click){
		powerInforSearch(page);
	}else{
		powerInforShow(page);
	}
}
/**
 * 省略号点击
 */
function setPageChangeView(){
	var bt_name=parseInt($("#other").attr('name'))+3;
	updatePageValue(bt_name);
	setViewForPage(bt_name);
	setFirstSelected();
	updateNowPage(bt_name);
}
/**
 * 更新页码数据
 * 
 * @param {Object}
 *            base_num
 */
function updatePageValue(base_num){
	var p1=parseInt(base_num);
	var p2=parseInt(base_num)+1;
	var p3=parseInt(base_num)+2;
	$("#p_1").val(p1);
	$("#p_2").val(p2);
	$("#p_3").val(p3);
	$("#other").attr('name',p1);
}
/**
 * 页码点击
 * 
 * @param {Object}
 *            p_id 页码
 */
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
/**
 * 设置第一个页码按钮为选中状态
 */
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
/**
 * 清除所有的选中状态
 */
function cleanAllSelected(){
	$("#p_1").css("background","#CCCCCC");
	$("#p_1").css("color","buttontext");
	$("#p_2").css("background","#CCCCCC");
	$("#p_2").css("color","buttontext");
	$("#p_3").css("background","#CCCCCC");
	$("#p_3").css("color","buttontext");
}
/**
 * 上一页，下一页点击
 * 
 * @param {Object}
 *            action -1上一页，1下一页
 */
function changPageOne(action){
	var now_page=parseInt($("#down_page").attr('name'));
	var page=now_page+action;
	if(page>0){
		updateAllStyleAndData(page,action);
	}
}
/**
 * 跳zhuan
 */
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
/**
 * 更新当前页码
 * 
 * @param {Object}
 *            page 当前页
 */
function updateNowPage(page){
	$("#down_page").attr('name',page);
}


// 信息搜索
function powerInforSearch(page){
	search_click=true
	var powerInfor=$("#power_Search").val();
	setFirstSelected();
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
					row= '<tr><td width="169" height="30" align="center" bgcolor="#ffffff">'+((page-1)*10+idx+1)+'</td><td width="231" height="30" align="center" bgcolor="#ffffff">'+item.powerName+'</td><td colspan="2" width="140" height="30" align="center" bgcolor="#ffffff"><a href="javascript:;"><img src="images/user_bj.png"  onClick="setCookie('+cookie_value1+','+cookie_value2+','+cookie_value3+')"></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;"><img src="images/user_del.png" class="delPower" id="'+item.powerId+'" /></a></td></tr>'
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
			console.log(msg);
			if( msg.status == "OK"){
				baseAjax("power_infor");
			}else{
				alert(msg.result);
			}
		},
		error: function(){
			
		}
	})	
}

function clearPower(){
	$("#namePower").val('');
	$("#urlPower").val('');
}


// 用户编辑
function getCookie(name) {
	
	console.log(document.cookie);
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
			console.log(msg);
			if( msg.status == "OK"){
				alert("修改成功");	
			}else{
				alert(msg.result);
			}
		},
		error: function(){
			alert("数据请求失败");
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
		console.log(power_id);
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
				error:function(){
					alert("数据请求失败");
				}
			});
		}
	})
})