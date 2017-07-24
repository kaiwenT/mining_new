//切换到任务—标签选项卡时，刷新页面
$('#myTab li:eq(0) a').click(function (e){
	e.preventDefault()
	  $(this).tab('show')
	  showlabelofStandard(); //显示已有的标签
	  showlabelNotInStandard();//显示没有的标签
//	  console.log("---------")
	  
})

/** 以下js代码全是关于为准数据任务贴标签、查看准数据任务有哪些标签及没有哪些标签。涉及到标签和准数据任务*/
//查看当前准数据的网站统计信息
function showcountwebsite()
{
	var issueId = getCookie("issueId");
//	console.log("当前话题ID是："+issueId);
	$.ajax({
		type:"post",
		url:"/issue/countURL",
		data:{issueId:issueId},
		datatype:"json",
		success: function(msg){
//			console.log("URL后台是：");
//			console.log(msg);
			if( msg.status == "OK"){
				var items = msg.result ; //从后台获取的为label实体
				var cookie_value1;
				$('#websiteCountInfo').html("");
				$.each(items,function(idx,item) {
					row= '<P>'+item[0]+'&nbsp;&nbsp;&nbsp;'+item[1]+'&nbsp;&nbsp;&nbsp;'+item[2]+'</P>'
                    $('#websiteCountInfo').append(row);
				});
			}else{
				$('#websiteCountInfo').html(msg.result);
			}
		},
		error: function(){
			alert("请求失败！");
		}
		
	})
}

//查看当前准数据已有的标签
function showlabelofStandard()
{
	var issueId = getCookie("issueId");
//	console.log("当前话题ID是："+issueId);
	$.ajax({
		type:"post",
		url:"/issue/selectLabelsForStandResult",
		data:{issueId:issueId},
		datatype:"json",
		success: function(msg){
//			console.log("已有后台是：");
//			console.log(msg);
			if( msg.status == "OK"){
				var items = msg.result ; //从后台获取的为label实体
				var cookie_value1;
				$('#labelofStandard').html("");
				$.each(items,function(idx,item) {
					cookie_value1="'"+item.labelid+"'";
					cookie_value2="'"+item.labelname+"'";
					row= '<button type="button" class="btn btn-success" style="margin: 5px;" onclick="deleteLabelOfStandard('+"'"+item.labelid+"'"+','+"'"+issueId+"'"+')">'+item.labelname+'</button>'
                    $('#labelofStandard').append(row);
				});
			}else{
				$('#labelofStandard').html("");
			}
		},
		error: function(){
			alert("请求失败！");
		}
		
	})
}

//查看当前准数据没有的标签
function showlabelNotInStandard()
{
	var issueId = getCookie("issueId");
//	console.log("当前话题ID是："+issueId);
	$.ajax({
		type:"post",
		url:"/issue/findLabelNotInStandardResult",
		data:{issueId:issueId},
		datatype:"json",
		success: function(msg){
//			console.log("没有后台是：");
//			console.log(msg);
			if( msg.status == "OK"){
				var items = msg.result ; //从后台获取的为label实体
				var cookie_value1;
				$('#labelNotinStandard').html("");
				$.each(items,function(idx,item) {
					cookie_value1="'"+item.labelid+"'";
					cookie_value2="'"+item.labelname+"'";
					row= '<button type="button" class="btn btn-success" style="margin: 5px;" onclick="setLabelForStandardResult('+"'"+item.labelid+"'"+','+"'"+issueId+"'"+')">'+item.labelname+'</button>'
                    $('#labelNotinStandard').append(row);
				});
			}else{
				$('#labelNotinStandard').html("");
			}
		},
		error: function(){
			alert("请求失败！");
		}
		
	})
}

function deleteLabelOfStandard(labelid,issueId) 
{
//	console.log("需要删除标签的话题ID是："+issueId);
//	console.log("需要删除标签ID是："+labelid);
	$.ajax({
		type:"post",
		url:"/issue/deleteLabelOfStandard",
		data:{issueId:issueId,labelid:labelid},
		datatype:"json",
		success: function(msg){
			if( msg.status == "OK"){
				showlabelofStandard(); //显示已有的标签
		        showlabelNotInStandard();//显示没有的标签
			}else{
				alert(msg.result);
			}
		},
		error: function(){
			alert("请求失败！");
		}
	})
}

function setLabelForStandardResult(labelid,issueId) 
{
//	console.log("需要添加标签的话题ID是："+issueId);
//	console.log("需要添加标签ID是："+labelid);
	$.ajax({
		type:"post",
		url:"/issue/SetLabelForStandardResult",
		data:{issueId:issueId,labelid:labelid},
		datatype:"json",
		success: function(msg){
			if( msg.status == "OK"){
				showlabelofStandard(); //显示已有的标签
		        showlabelNotInStandard();//显示没有的标签
			}else{
				alert(msg.result);
			}
		},
		error: function(){
			alert("请求失败！");
		}
	})
}

///** 以下js代码全是关于为准数据文件贴标签、查看准数据文件有哪些标签及没有哪些标签。涉及到标签和准数据文件*/
////查看当前准数据的网站统计信息
//function showcountwebsite()
//{
//	var stdResId = getCookie("stdResId");
//	console.log("当前话题ID是："+stdResId);
//	$.ajax({
//		type:"post",
//		url:"/standardResult/countURL",
//		data:{stdResId:stdResId},
//		datatype:"json",
//		success: function(msg){
//			console.log("URL后台是：");
//			console.log(msg);
//			if( msg.status == "OK"){
//				var items = msg.result ; //从后台获取的为label实体
//				var cookie_value1;
//				$('#websiteCountInfo').html("");
//				$.each(items,function(idx,item) {
//					row= '<P>'+item+'</P>'
//                    $('#websiteCountInfo').append(row);
//				});
//			}else{
//				$('#websiteCountInfo').html("");
//			}
//		},
//		error: function(){
//			alert("请求失败！");
//		}
//		
//	})
//}
//
////查看当前准数据已有的标签
//function showlabelofStandard()
//{
//	var issueId = getCookie("stdResId");
//	console.log("当前话题ID是："+issueId);
//	$.ajax({
//		type:"post",
//		url:"/standardResult/selectLabelsForStandResult",
//		data:{stdResId:issueId},
//		datatype:"json",
//		success: function(msg){
//			console.log("已有后台是：");
//			console.log(msg);
//			if( msg.status == "OK"){
//				var items = msg.result ; //从后台获取的为label实体
//				var cookie_value1;
//				$('#labelofStandard').html("");
//				$.each(items,function(idx,item) {
//					cookie_value1="'"+item.labelid+"'";
//					cookie_value2="'"+item.labelname+"'";
//					row= '<button type="button" class="btn btn-success" style="margin: 5px;" onclick="deleteLabelOfStandard('+"'"+item.labelid+"'"+','+"'"+issueId+"'"+')">'+item.labelname+'</button>'
//                    $('#labelofStandard').append(row);
//				});
//			}else{
//				$('#labelofStandard').html("");
//			}
//		},
//		error: function(){
//			alert("请求失败！");
//		}
//		
//	})
//}
//
////查看当前准数据没有的标签
//function showlabelNotInStandard()
//{
//	var issueId = getCookie("stdResId");
//	console.log("当前话题ID是："+issueId);
//	$.ajax({
//		type:"post",
//		url:"/standardResult/findLabelNotInStandardResult",
//		data:{stdResId:issueId},
//		datatype:"json",
//		success: function(msg){
//			console.log("没有后台是：");
//			console.log(msg);
//			if( msg.status == "OK"){
//				var items = msg.result ; //从后台获取的为label实体
//				var cookie_value1;
//				$('#labelNotinStandard').html("");
//				$.each(items,function(idx,item) {
//					cookie_value1="'"+item.labelid+"'";
//					cookie_value2="'"+item.labelname+"'";
//					row= '<button type="button" class="btn btn-success" style="margin: 5px;" onclick="setLabelForStandardResult('+"'"+item.labelid+"'"+','+"'"+issueId+"'"+')">'+item.labelname+'</button>'
//                    $('#labelNotinStandard').append(row);
//				});
//			}else{
//				$('#labelNotinStandard').html("");
//			}
//		},
//		error: function(){
//			alert("请求失败！");
//		}
//		
//	})
//}
//
//function deleteLabelOfStandard(labelid,issueId) 
//{
//	console.log("需要删除标签的话题ID是："+issueId);
//	console.log("需要删除标签ID是："+labelid);
//	$.ajax({
//		type:"post",
//		url:"/standardResult/deleteLabelOfStandard",
//		data:{stdResId:issueId,labelid:labelid},
//		datatype:"json",
//		success: function(msg){
//			if( msg.status == "OK"){
//				showlabelofStandard(); //显示已有的标签
//		        showlabelNotInStandard();//显示没有的标签
//			}else{
//				alert(msg.result);
//			}
//		},
//		error: function(){
//			alert("请求失败！");
//		}
//	})
//}
//
//function setLabelForStandardResult(labelid,issueId) 
//{
//	console.log("需要添加标签的话题ID是："+issueId);
//	console.log("需要添加标签ID是："+labelid);
//	$.ajax({
//		type:"post",
//		url:"/standardResult/SetLabelForStandardResult",
//		data:{stdResId:issueId,labelid:labelid},
//		datatype:"json",
//		success: function(msg){
//			if( msg.status == "OK"){
//				showlabelofStandard(); //显示已有的标签
//		        showlabelNotInStandard();//显示没有的标签
//			}else{
//				alert(msg.result);
//			}
//		},
//		error: function(){
//			alert("请求失败！");
//		}
//	})
//}

/************* 以下js代码全是标签增、删、查、改。只涉及到标签。************************/
//显示每一页标签信息
function labelInforShow(page){
	search_click=false;
	$.ajax({
		type:"post",
		url:"/label/selectAllLabel",
		data:{
			start:(parseInt(7*page-7)),
			limit:7
		},
		dataType:"json",
		success: function(msg){
		
			if( msg.status == "OK"){
				// alert("success");
				var items = msg.result ;
				var cookie_value1;
				var cookie_value2;
				$('.infor_tab02 tr:not(:first)').html("");
				$.each(items,function(idx,item) {
					// alert(msg.tagName);
					cookie_value1="'"+item.labelid+"'";
					cookie_value2="'"+item.labelname+"'";
                    row= '<tr><td width="169" height="40" align="center" bgcolor="#ffffff">'+((page-1)*7+idx+1)+'</td><td width="231" height="40" align="center" bgcolor="#ffffff">'+item.labelname+'</td><td colspan="2" width="140" height="40" align="center" bgcolor="#ffffff"><button type="button" class="btn btn-primary" onclick="labelInforEdit('+"'"+item.labelid+"'"+','+"'"+item.labelname+"'"+')">编辑</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button type="button" class="btn btn-danger dellabel" id="'+item.labelid+'" >删除</button></td></tr>'
                    $('.infor_tab02').append(row);
					
				});
			}else{
				// /alert("fail");
				$('.infor_tab02 tr:not(:first)').html("");
			}
		},
		error: function(){
			alert("请求失败！");
		}
	})	
}

//初始化显示页面
function initShowPage(currenPage){
    var listCount = 0;
    if("undefined" == typeof(currenPage) || null == currenPage){
        currenPage = 1;
    }
  
    $.ajax({
        type: "post",
        url: "/label/selectlabelcount",
		dataType:"json",
        success: function (msg) {
            if (msg.status == "OK") {
               
                listCount = msg.result;
                $("#page").initPage(listCount,currenPage,labelInforShow);
            } else {
                alert(msg.result);
            }
        },
        error: function () {
            alert("数据请求失败！！");
        }});
}

initShowPage(1)

//初始化查询标签页面
function initSearchPage(currenPage){
    var listCount = 0;
    if("undefined" == typeof(currenPage) || null == currenPage){
        currenPage = 1;
    }
    labelInfor=$("#label_Search").val();

    $.ajax({
        type: "post",
        url: "/label/selectlabelcount",
        data:{
        	labelname:labelInfor
		},
        dataType: "json",
        success: function (msg) {
            if (msg.status == "OK") {
                listCount = msg.result;
 //               console.log("查找到的条数为："+listCount);
                $("#page").initPage(listCount,currenPage,labelInforSearch);
            } else {
                alert(msg.result);
            }
        },
        error: function () {
            alert("数据请求失败");
        }})
}

function setCookie(value1,value2){
	// alert(name+value);
	var cookie_name1="labelid";
	var cookie_name2="labelname";
	var Days = 1; // 此 cookie 将被保存 1 天
	var exp　= new Date();
	exp.setTime(exp.getTime() +Days*24*60*60*1000);
	document.cookie = cookie_name1 +"="+ escape (value1) + ";expires=" + exp.toGMTString();
	document.cookie = cookie_name2 +"="+ escape (value2) + ";expires=" + exp.toGMTString();
}

//搜索标签
function labelInforSearch(page){
	search_click=true
	var labelInfor=$("#label_Search").val();
	setFirstSelected();
	$.ajax({
		type:"post",
		url:"/label/selectLabelByName",
		data:{
			labelname:labelInfor,
			start:(parseInt(7*page-7)),
			limit:7
		},
		dataType:"json",
		beforeSend : function(){
		    begin();
		},
		success: function(msg){
			$(".infor_tab02 tr:not(:first)").html("");
			if( msg.status == "OK"){
				// alert("success");
				var items = msg.result ;
				var cookie_value1;
				var cookie_value2;
				$.each(items,function(idx,item) {
					// alert(msg.tagName);
					cookie_value1="'"+item.labelid+"'";
					cookie_value2="'"+item.labelname+"'";
					row= '<tr><td width="169" height="40" align="center" bgcolor="#ffffff">'+((page-1)*7+idx+1)+'</td><td width="231" height="30" align="center" bgcolor="#ffffff">'+item.labelname+'</td><td colspan="2" width="140" height="30" align="center" bgcolor="#ffffff"><button type="button" class="btn btn-primary" onclick="labelInforEdit('+"'"+item.labelid+"'"+','+"'"+item.labelname+"'"+')">编辑</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button type="button" class="btn btn-danger dellabel" id="'+item.labelid+'" >删除</button></td></tr>'
					$('.infor_tab02').append(row);
				});
			}else{
				alert(msg.result);
			}
		},
		complete:function(){
		    stop();
		},
		error: function(){
			alert("数据请求失败");
		}
	})	
}


// 标签添加
function labelInforAdd() {
	$("#addLabel").css("display",'block');
	$("#labelInfo").css("display",'none');
	$("#editLabel").css("display",'none');
}
function addlabel(){
	$.ajax({
		type:"post",
		url:"/label/insertLabel",
		data:{
			labelname:$("#namelabel").val(),
		},
		dataType:"json",
		success: function(msg){
//			console.log(msg);
			if( msg.status == "OK"){
				backLabelInfor();
			}else{
				alert(msg.result);
			}
		},
		error: function(){
			alert("添加失败！");
		}
	})	
}

function clearlabel(){
	$("#namelabel").val('');
}

function getCookie(name) {
	var arr =document.cookie.match(new RegExp("(^|)"+name+"=([^;]*)(;|$)"));
	if(arr !=null) 
		return unescape(arr[2]); 
	return null;
}


//标签编辑
function labelInforEdit(labelId,labelName){
    $("#addLabel").css("display",'none');
    $("#labelInfo").css("display",'none');
    $("#editLabel").css("display",'block');
    setCookie(labelId,labelName);
    $("#new_name_label").val(labelName);
}

function labelInforChange(){
	var newId=getCookie("labelid");

//	console.log($("#new_name_label").val());
	$.ajax({
		type:"post",
		url:"/label/updateLabel",
		data:{
			labelid:newId,
			labelname:$("#new_name_label").val(),
		},
		dataType:"json",
		success: function(msg){
//			console.log(msg);
			if( msg.status == "OK"){
				alert("修改成功");
				backLabelInfor();
			}else{
				alert(msg.result);
			}
		},
		error: function(){
			alert("数据请求失败!");
		}
	})	
}
function clearNewlabel(){
	$("#new_name_label").val('');
}

// 标签删除
$(function(){
	$(".infor_tab02").on("click",".dellabel",function(){
		var label_id = $(this).attr("id");
		labelInforDel(label_id);
		function labelInforDel(label_id){
			$.ajax({
				type:"post",
				url:"/label/deleteLabelById",
				data:{
					labelId:label_id,
				} ,
				dataType:"json",
				success:function(msg){
					if(msg.status=="OK"){
						initShowPage(1);
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
//返回标签信息显示页面
function showLabelInfor()
{
	initShowPage(1)
}

//返回标签信息显示界面
function backLabelInfor() {
	initShowPage(1);
    $("#addLabel").css("display",'none');
    $("#labelInfo").css("display",'block');
    $("#editLabel").css("display",'none');
}

//弹出框的样式
$(function() {
    // alert($(window).height());
	 $('#attachlabel').click(function() {
	        $('#code').center();
	        $('#goodcover').show();
	        $('#code').fadeIn();
	        showcountwebsite();//显示统计网站的信息
	        showlabelofStandard(); //显示已有的标签
	        showlabelNotInStandard();//显示没有的标签
	    });
    $('#closebt').click(function() {
        $('#code').hide();
        $('#goodcover').hide();
       
    });
	$('#goodcover').click(function() {
        $('#code').hide();
        $('#goodcover').hide();
       
    });
    
    jQuery.fn.center = function(loaded) {
        var obj = this;
        body_width = parseInt($(window).width());
        body_height = parseInt($(window).height());
        block_width = parseInt(obj.width());
        block_height = parseInt(obj.height());

        left_position = parseInt((body_width / 2) - (block_width / 2) + $(window).scrollLeft());
        if (body_width < block_width) {
            left_position = 0 + $(window).scrollLeft();
        };

        top_position = parseInt((body_height / 2) - (block_height / 2));// +
																		// $(window).scrollTop());
        if (body_height < block_height) {
            top_position = 0 + $(window).scrollTop();
        };

        if (!loaded) {

            obj.css({
                'position': 'fixed'
            });
            obj.css({
                'top': ($(window).height() - $('#code').height()) * 0.5,
                'left': left_position
            });

        } else {
            obj.stop();
            obj.css({
                'position': 'fixed'
            });
            obj.animate({
                'top': top_position
            }, 200, 'linear');
        }
    }

})