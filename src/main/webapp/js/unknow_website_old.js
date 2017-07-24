// JavaScript Document
//用户信息展示
function websiteInforShow(page){
	search_click=false;
	$.ajax({
		type:"post",
		url:"/website/selectAllWebsiteUnknow",
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
				var cookie_value4;
				var cookie_value5;
				$.each(items,function(idx,item) {
					// alert(msg.tagName);
					cookie_value1="'"+item.id+"'";
					cookie_value2="'"+item.name+"'";
					cookie_value3="'"+item.type+"'";
					cookie_value4="'"+item.url+"'";
					cookie_value5="'"+item.level+"'";
					row= '<tr><td width="42" height="40" align="center" bgcolor="#ffffff">'+((page-1)*10+idx+1)+'</td><td width="238" height="40" align="center" bgcolor="#ffffff"><div class="tab_url"><a href ="'+item.url+'" target = "_blank">'+item.url+'</a></div></td><td width="83" height="40" align="center" bgcolor="#ffffff">'+item.name+'</td><td width="76" height="40" align="center" bgcolor="#ffffff">'+item.level+'</td><td width="77" height="40" align="center" bgcolor="#ffffff">'+item.type+'</td><td width="150" height="40" align="center" bgcolor="#ffffff"><button type="button" class="btn btn-primary" onClick="setCookie('+cookie_value1+','+cookie_value2+','+cookie_value3+','+cookie_value4+','+cookie_value5+')">编辑</button>&nbsp;&nbsp;&nbsp;<button type="button" class="btn btn-danger delWebsite" id="'+item.id+'" >删除</button></td></tr>'
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
        url: "/website/selectUnknowWebsiteCount",
        dataType: "json",
        success: function (msg) {
            if (msg.status == "OK") {
                // alert("success");
                listCount = msg.result;
                $("#page").initPage(listCount,currenPage,websiteInforShow);
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
    var obj2 = $("#web_name").val();
    var obj3 = $("#web_level").val();
    var obj4 = $("#web_type").val();
    $.ajax({
        type: "post",
        url: "/website/selectUnknowWebsiteCount",
        data:{
            url:$("#web_url").val(),
            name:obj2,
            // level:obj3,
            levle:obj3,
            type:obj4},
        dataType: "json",
        success: function (msg) {
            if (msg.status == "OK") {
                // alert("success");
                listCount = msg.result;
                $("#page").initPage(listCount,currenPage,websiteInforSearch);
            } else {
                alert(msg.result);
            }
        },
        error: function () {
            alert("数据请求失败");
        }})
}

function setCookie(value1,value2,value3,value4,value5){
	// alert(name+value);
	var cookie_name1="id";
	var cookie_name2="websiteName";
	var cookie_name3="type";
	var cookie_name4="url";
	var cookie_name5="level";
	var Days = 1; // 此 cookie 将被保存 1 天
	var exp　= new Date();
	exp.setTime(exp.getTime() +Days*24*60*60*1000);
	document.cookie = cookie_name1 +"="+ escape (value1) + ";expires=" + exp.toGMTString();
	document.cookie = cookie_name2 +"="+ escape (value2) + ";expires=" + exp.toGMTString();
	document.cookie = cookie_name3 +"="+ escape (value3) + ";expires=" + exp.toGMTString();
	document.cookie = cookie_name4 +"="+ escape (value4) + ";expires=" + exp.toGMTString();
	document.cookie = cookie_name5 +"="+ escape (value5) + ";expires=" + exp.toGMTString();
	baseAjax("website_change");
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
		websiteInforSearch(page);
	}else{
		websiteInforShow(page);
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
}
*/


// 信息搜索
function websiteInforSearch(page){
	search_click=true;
	// var obj1 = $("#web_url").val();
	var obj2 = $("#web_name").val();
	var obj3 = $("#web_level").val();
	var obj4 = $("#web_type").val();
	setFirstSelected();
	$.ajax({
		type:"post",
		url:"/website/selectByCondition",
		data:{
			url:$("#web_url").val(),
			name:obj2,
			// level:obj3,
			levle:obj3,
			type:obj4,
			start:(parseInt(10*page-10)),
			limit: 10
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
				var cookie_value4;
				var cookie_value5;
				$.each(items,function(idx,item) {
					// alert(msg.tagName);
					cookie_value1="'"+item.id+"'";
					cookie_value2="'"+item.name+"'";
					cookie_value3="'"+item.type+"'";
					cookie_value4="'"+item.url+"'";
					cookie_value5="'"+item.level+"'";
					row= '<tr><td width="42" height="40" align="center" bgcolor="#ffffff">'+((page-1)*10+idx+1)+'</td><td width="238" height="40" align="center" bgcolor="#ffffff"><div class="tab_url">'+item.url+'</div></td><td width="83" height="40" align="center" bgcolor="#ffffff">'+item.name+'</td><td width="76" height="40" align="center" bgcolor="#ffffff">'+item.level+'</td><td width="77" height="40" align="center" bgcolor="#ffffff">'+item.type+'</td><td width="150" height="40" align="center" bgcolor="#ffffff"><button type="button" class="btn btn-primary" onClick="setCookie('+cookie_value1+','+cookie_value2+','+cookie_value3+','+cookie_value4+','+cookie_value5+')" >编辑</button>&nbsp;&nbsp;&nbsp;<button type="button" class="btn btn-danger delWebsite" id="'+item.id+'" >删除</button></td></tr>'
					
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
function websiteInforAdd(){
	baseAjax("website_add");
}
function addWebsite(){
	// console.log($("#urlWebsite").val())
	$.ajax({
		type:"post",
		url:"/website/insertWebsite",
		data:{
			url:$("#urlWebsite").val(),
			name:$("#nameWibsite").val(),
			// level:$("#levelWebsite").val(),
			levle:$("#levelWebsite").val(),
			type:$("#typeWebsite").val()
		},
		dataType:"json",
		success: function(msg){
//			console.log(msg);
			if( msg.status == "OK"){
				baseAjax("unknow_website_infor");
			}else{
				alert(msg.result);
			}
		},
		error: function(){
            alert("数据请求失败");
        }
	})	
}

function clearWebsite(){
	url:$("#urlWebsite").val('');
	name:$("#nameWibsite").val('');
	level:$("#levelWebsite").val('');
	type:$("#typeWebsite").val('');
}



// 用户编辑
function getCookie(name) {
	
//	console.log(document.cookie);
	var arr =document.cookie.match(new RegExp("(^|)"+name+"=([^;]*)(;|$)"));
	if(arr !=null) 
		return unescape(arr[2]); 
	return null;
}

function websiteInforChange(){
	var newId=getCookie("id");
	$.ajax({
		type:"post",
		url:"/website/updateWebsite",
		data:{
			id:newId,
			url:$("#new_url_website").val(),
			name:$("#new_name_website").val(),
			level:$("#new_level_website").val(),
			type:$("#new_type_website").val()
		},
		dataType:"json",
		success: function(msg){
//			console.log(msg);
			if( msg.status == "OK"){
				// alert("更新成功");	
			}else{
				alert(msg.result);
			}
		},
		error: function(){
            alert("数据请求失败");
        },
	})	
}
function clearNewWebsite(){
	$("#new_url_website").val('');
	$("#new_name_website").val('');
	$("#new_level_website").val('');
	$("#new_type_website").val('');
}


// 用户删除
$(function(){
	$(".infor_tab02").on("click",".delWebsite",function(){
		var website_id = $(this).attr("id");
		websiteInforDel(website_id);
		function websiteInforDel(website_id){
			$.ajax({
				type:"post",
				url:"/website/deleteWebsite",
				data:{
					websiteId:website_id,
				} ,
				dataType:"json",
				success:function(msg){
					if(msg.status=="OK"){
					    baseAjax("unknow_website_infor");
					}else{
						alert(msg.result);
					}
				} ,
				error: function(){
		            alert("数据请求失败");
		        }
			});
		}
	})
})