// JavaScript Document
//3.1任务详情展示
function dataShow(){
	var newId=getCookie("id");
	console.log(newId);
    $.ajax({
        type:"post",
        url:"/file/queryIssueFiles",
        data:{
            issueId:newId
        },
        dataType:"json",
        beforeSend : function(){
		    begin();
		},
        success:function(msg){
            console.log(msg);
            if(msg.status=="OK"){
				var items = msg.result.issue;
				$(function() {
					name = '<span>'+items.issueName+'</span>',
					admin = '<span>'+items.creator+'</span>',
					ct = '<span>'+new Date(items.createTime.time).format('yyyy-MM-dd hh:mm:ss')+'</span>',
					lo = '<span>'+items.lastOperator+'</span>',
					lut ='<span>'+new Date(items.lastUpdateTime.time).format('yyyy-MM-dd hh:mm:ss')+'</span>'

					$('.ckht_list li').eq(0).append( name ),
					$('.ckht_list li').eq(1).append( admin ),
					$('.ckht_list li').eq(2).append( ct ),
					$('.ckht_list li').eq(3).append( lo ),
					$('.ckht_list li').eq(4).append( lut );
				});
				var tabs = msg.result.list; 
				$('.up_list tr:not(:first)').html("");
				$.each(tabs,function(i,item){
					cookie_value1="'"+item.fileId+"'";
					row ='<tr><td width="257" align="center" valign="middle">'+item.fileName+
					'</td><td width="95" align="center" valign="middle">'+item.creator+
					'</td><td width="173" align="center" valign="middle">'+new Date(item.uploadTime.time).format('yyyy-MM-dd hh:mm:ss')+
					'</td><td align="center" valign="middle"><img src="images/julei.png" class="btn_sc" onClick="choiceType('+cookie_value1+')" /><img class="btn_jl" src="images/delete.png" id="'+item.fileId+'" onclick="bind()" /></td></tr>'
					$('.up_list').append(row);
				});				
            }else{
                alert("查询失败");
            }

        } ,
        complete:function(){
		    stop();
		},
        error:function(){
            // ���������
        }
    });
}
dataShow()

function localRefresh(){
	var newId=getCookie("id");
	console.log(newId);
    $.ajax({
        type:"post",
        url:"/file/queryIssueFiles",
        data:{
            issueId:newId
        },
        dataType:"json",
        beforeSend : function(){
		    begin();
		},
        success:function(msg){
            console.log(msg);
            if(msg.status=="OK"){
				var tabs = msg.result.list; 
				$('.up_list tr:not(:first)').html("");
				$.each(tabs,function(i,item){
					cookie_value1="'"+item.fileId+"'";
					row ='<tr><td width="257" align="center" valign="middle">'+item.fileName+
					'</td><td width="95" align="center" valign="middle">'+item.creator+
					'</td><td width="173" align="center" valign="middle">'+new Date(item.uploadTime.time).format('yyyy-MM-dd hh:mm:ss')+
					'</td><td align="center" valign="middle"><img src="images/julei.png" class="btn_sc" onClick="choiceType('+cookie_value1+')" /><img class="btn_jl" src="images/delete.png" id="'+item.fileId+'" onclick="bind()" /></td></tr>'
					$('.files_list table').append(row);
				});				
            }else{
                alert("查询失败");
            }

        } ,
        complete:function(){
		    stop();
		},
        error:function(){
            // ���������
        }
    });
}

function choiceType(id){
	console.log("----------"+id);
	$("#shade").css("display","block");
    $("#choose_mining_type").css("display","block");
    $("#choice_confirm").attr("data-id",id);
}

function buttonConfirm(){
	console.log($("#choice_confirm").attr("data-id"))
    $("#shade").css("display","none");
    $("#choose_mining_type").css("display","none");
    //点击确定按钮的业务逻辑处理
    var id = $("#choice_confirm").attr("data-id");
    var type =$("input[name='choice']:checked").val();
    console.log("==="+type)
    clusterSingleFile(id,type);
}

function setCookie(value1){
	// alert(name+value);
	var cookie_name1="id";
	var Days = 1; // 此 cookie 将被保存 1 天
	var exp　= new Date();
	exp.setTime(exp.getTime() +Days*24*60*60*1000);
	document.cookie = cookie_name1 +"="+ escape (value1) + ";expires=" + exp.toGMTString();
	// window.location.href = "summary.html";
}

function getCookie(name) {
	
	console.log(document.cookie);
	var arr =document.cookie.match(new RegExp("(^|)"+name+"=([^;]*)(;|$)"));
	if(arr !=null) 
		return unescape(arr[2]); 
	return null;
}



function clusterSingleFile(id,type){
	
	console.log(id);
	$.ajax({
		type:"post",
		url:"/issue/miningSingleFile",
		data:{
			fileId:id,
			granularityId:type
		},
		dataType:"json",
		beforeSend : function(){
		    begin();
		},
		success: function(msg){
			console.log(msg);
			if( msg.status == "OK"){
				window.location.href = "history.html";
			}else{
				alert(msg.result);
			}
		},
		error: function(){
			alert("请求失败");
		},
		complete : function(){
		  stop();  
		}
	})	
}

function bind(){
	$(".up_list tr").unbind('click').on("click",".btn_jl",function(){
		var file_id = $(this).attr("id");
		console.log(file_id);
		$.ajax({
		        type:"post",
		        url:"/file/deleteFileById",
		        data:{
		            fileid:file_id
		        },
		        dataType:"json",
		        success: function(msg){
		            if( msg.status == "OK"){
		                localRefresh();
		            }else{
		                alert("fail");
		            }
		        },
		        error: function(){
		            alert("数据请求失败");
		        }
		    })  
	})
}