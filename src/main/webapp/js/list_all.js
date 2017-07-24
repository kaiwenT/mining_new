// JavaScript Document
//2.1任务列表显示

issueType="extensive";
// 选中全局任务时的数据类型
$(document).ready(function(){
	var r = getCookie("issueType");
    if(r != null){
    	issueType = r;
    }
    setCookie_issueType(issueType);
    var choosenLabel = $("input[name='issueType'][value="+issueType+"]");
    choosenLabel.parent().css("color","red");
    choosenLabel.parent().siblings('label').css("color","black");
    choosenLabel.attr("checked",true);
	initShowPage(1);
});

// radio选中事件
$(function(){
	$(":radio").click(function(){
		// 清空数据显示
		$('.ht_cont tr:not(:first)').html("");
		issueType=$(this).val();
		setCookie_issueType(issueType);
		var choosenLabel = $("input[name='issueType'][value="+issueType+"]");
    	choosenLabel.parent().css("color","red");
    	choosenLabel.parent().siblings('label').css("color","black");
    	choosenLabel.attr("checked",true);
		// 显示数据
    	initShowPage(1);
	});
});

function setCookie_issueType(value){
	var Days = 1; // 此 cookie 将被保存 1 天
	var exp　= new Date();
	exp.setTime(exp.getTime() +Days*24*60*60*1000);
	document.cookie = "issueType="+ escape (value) + ";expires=" + exp.toGMTString();
}

function allData (page){
	search_click=false;
    $.ajax({
        type:"post",
        url:"/issue/queryAllIssue",
		data:JSON.stringify(GetJsonData(page)),
		dataType:"json",
		contentType:"application/json",
        success:function(msg){
            if(msg.status=="OK"){
                // alert("success") ;
				var items = msg.result.list ;
				
				$('.ht_cont tr:not(:first)').html("");
				var count=0;
				$.each(items,function(idx,item) {
						var item_issueId="'"+item.issueId+"'";
						count++;
						row= '<tr><td height="40" align="center">'+((page-1)*10+count)+
						'</td><td height="40" align="center"><a href="javascript:;" onclick="setCookie('+item_issueId+')">'+item.issueName+
						'</a></td><td height="40" align="center">'+item.creator+
						'</td><td height="40" align="center">'+ new Date(item.createTime.time).format('yyyy-MM-dd hh:mm:ss')+
						'</td><td height="40" align="center">'+item.lastOperator+
						'</td><td height="40" align="center">'+ new Date(item.lastUpdateTime.time).format('yyyy-MM-dd hh:mm:ss')+
						'</td><td height="40" align="center"><button type="button" class="btn btn-danger" onclick="deleteData('+"'"+item.issueId+"'"+')">删除</button></td></tr>'
						$('.ht_cont').append(row);
				});
				
            }else{
            }

        },
        error:function(){
        }
    });
}
function initShowPage(currenPage){
    var listCount = 0;
    if("undefined" == typeof(currenPage) || null == currenPage){
        currenPage = 1;
    }
    $.ajax({
        type: "post",
        url: "/issue/queryAllIssueCount",
        data:JSON.stringify(GetJsonData(currenPage)),
		dataType:"json",
		contentType:"application/json",
        success: function (msg) {
            if (msg.status == "OK") {
                // alert("success");
                listCount = msg.result;
                $("#page").initPage(listCount,currenPage,allData);
            } else {
                alert(msg.result);
            }
        },
        error: function () {
            alert("非常抱歉，您没有权限访问该资源，请联系管理员");
        }})
}

function initSearchPage(currenPage){
    var listCount = 0;
    if("undefined" == typeof(currenPage) || null == currenPage){
        currenPage = 1;
    }
    $.ajax({
        type: "post",
        url: "/issue/queryAllIssueCount",
        data:JSON.stringify(SearchJsonData(currenPage)),
        dataType: "json",
		contentType:"application/json",
        success: function (msg) {
            if (msg.status == "OK") {
                // alert("success");
                listCount = msg.result;
                $("#page").initPage(listCount,currenPage,searchData);
            } else {
            	$('.ht_cont tr:not(:first)').html("");
                alert(msg.result);
            }
        },
        error: function () {
            alert("非常抱歉，您没有权限访问该资源，请联系管理员");
        }})
}


function GetJsonData(page) {
	var myDate=new Date();
	var timeStamp = Date.parse(myDate)/1000;
	myDate.setTime((timeStamp+24*60*60)*1000);
	end=myDate.getFullYear() + "-" + (myDate.getMonth()+1) + "-" + (myDate.getDate());
	myDate.setTime((timeStamp-90*24*60*60)+1000);
	start=myDate.getFullYear() + "-" + (myDate.getMonth()+1) + "-" + myDate.getDate();
    var json = {
        "issueId":"",
        "issueName":"" ,
        "issueType":issueType,
        "createStartTime":start,
        "createEndTime":end,
        "user":"",
        "lastUpdateStartTime":start,
        "lastUpdateEndTime":end,
        "pageNo":parseInt(page),
        "pageSize":10
    };
    return json;
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
		searchData(page);
	}else{
		allData(page);
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


function setCookie(value1){
	var cookie_issueId="issueId";
	var Days = 1; // 此 cookie 将被保存 1 天
	var exp　= new Date();
	exp.setTime(exp.getTime() +Days*24*60*60*1000);
	document.cookie = cookie_issueId +"="+ escape (value1) + ";expires=" + exp.toGMTString();
	baseAjax("original_data");
}

function getCookie(name) {
	
	var arr =document.cookie.match(new RegExp("(^|)"+name+"=([^;]*)(;|$)"));
	if(arr !=null) 
		return unescape(arr[2]); 
	return null;
}

// 2.2任务搜索
function searchData(page){
	search_click=true;
	setFirstSelected();
    $.ajax({
        type:"post",
        url:"/issue/queryAllIssue",
        data:JSON.stringify(SearchJsonData(page)),
        dataType:"json",
        contentType:"application/json",
        success:function(msg){
            if(msg.status=="OK"){
                $('.ht_cont tr:not(:first)').html("");
				var items = msg.result.list ;
				$.each(items,function(idx,item) {
					row= '<tr><td height="40" align="center">'+((page-1)*10+idx+1)+'</td><td height="40" align="center"><a href="#">'+item.issueName+'</a></td><td height="40" align="center">'+item.creator+'</td><td height="40" align="center">'+new Date(item.lastUpdateTime.time).format('yyyy-MM-dd hh:mm:ss')+'</td><td height="40" align="center">'+item.lastOperator+'</td><td height="40" align="center">'+new Date(item.lastUpdateTime.time).format('yyyy-MM-dd hh:mm:ss')+'</td><td height="40" align="center"><button type="button" class="btn btn-danger" onclick="deleteData('+"'"+item.issueId+"'"+')">删除</button></td>'
					$('.ht_cont').append(row);
					
				});
				
            }else{
            	 $('.ht_cont tr:not(:first)').html("");
            	 alert(issueType+" data have been erased!");
            }

        } ,
        error:function(){
            
        }
    });
}

function SearchJsonData(page) {
	// var obj = $('#ht_name').val();
	var obj1 = $('#b_time').val();
	var obj2 = $('#o_time').val();
	if(!(obj2==""|| obj2=="null" || obj2=="undefined")){
		var timestamp = Date.parse(new Date(obj2))/1000;
		var endDate = new Date();
		endDate.setTime((timestamp+24*60*60)*1000);
		obj2 = endDate.getFullYear() + "-" + (endDate.getMonth()+1) + "-" + endDate.getDate();
	}
	var obj3 = $('#cj_name').val();
	var obj4 = $('#lb_time').val();
	var obj5 = $('#lo_time').val();
	if(!(obj5==""|| obj5=="null" || obj5=="undefined")){
		var timestamp2 = Date.parse(new Date(obj5))/1000;
		var endDate2 = new Date();
		endDate2.setTime((timestamp2+24*60*60)*1000);
		obj5 = endDate2.getFullYear() + "-" + (endDate2.getMonth()+1) + "-" + endDate2.getDate();	
	}
	var json = {
		"issueId":"",
		"issueName": $('#ht_name').val(),
		"issueType": issueType,
		"createStartTime":obj1,
		"createEndTime":obj2,
		"user":obj3,
		"lastUpdateStartTime":obj4,
		"lastUpdateEndTime":obj5,
		"pageNo":parseInt(page),
		"pageSize":10
    };
    return json;
}

// 2.3管理任务
/*
 * $(function(){ $(".ht_cont").on("click","img",function(){ var issueId =
 * $(this).attr("class"); deleteData(issueId); }) })
 */

function deleteData(issueId){
	$.ajax({
		type:"post",
		url:"/issue/deleteAll",
		data:{
			issueId:issueId,
			issueType:issueType,
		} ,
		dataType:"json",
		success:function(msg){
			if(msg.status=="OK"){
				initShowPage(1)
			}else{
				alert("fail");
			}
		} ,
		error:function(){		
		}
	});
}

