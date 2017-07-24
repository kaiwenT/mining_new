/**
 * 准数据结果js
 */
// document.write('<script type="text/javascript"
// src="js/cluster_details.js"></script>');

function setCookie(key,value){
	var Days = 1; // 此 cookie 将被保存 1 天
	var exp　= new Date();
	exp.setTime(exp.getTime() +Days*24*60*60*1000);
	document.cookie = key+"="+ escape (value) + ";expires=" + exp.toGMTString();
}

//显示当前任务名字
function showStdIssueName(issueId) {
	$.ajax({
		type : "post",
		url : "/standardResult/queryIssueName",
		data : {
			issueId : issueId
		},
		dataType : "json",
		success : function(msg) {
			if (msg.status == "OK") {
				var items = msg.result;
				$('.issueName').text("任务名称：" + items.issue.issueName);
				setCookie("stdResId",items.stdRes.stdRid);
			
				stdResData(getCookie("stdResId"));
			} else {
				alert(msg.result);
			}

		},
		error : function(msg) {
			alert(eval('(' + msg.responseText + ')').result);
		}
	});
}

showStdIssueName(getCookie("issueId"));

//显示准数据聚类结果
function stdResData(rid) {
	$.ajax({
		type : "post",
		url : "/standardResult/getCountResult",
		data : {
			resultId : rid
		},
		dataType : "json",
		success : function(msg) {
			$('.summary_tab table tr:not(:first)').html('');
			if (msg.status == "OK") {
				
				var items = msg.result;
				
				var indexOfTitle = parseInt(items[0][0]) + 1;
				var indexOfUrl = parseInt(items[0][1]) + 1;
				var indexOfTime = parseInt(items[0][2]) + 1;
				for (var i = 0; i < items.length - 1; i++) {
					// items第一行存储index，故从i+1读起
					item = items[i + 1];
					
					rows = '<tr><td height="32" align="center">'
						+(i+1)+'</td><td height="32" align="center"><a href="javascript:;">' 
						+ item[indexOfTitle] + '</a></td><td height="32" align="center">' 
						+ item[indexOfTime] + '</td><td height="32" align="center">'
						//添加画图的代码为：
						+ '<a href="javascript:;" onclick="toPaint(' + i + ',\'' + item[indexOfTitle].replace(/\"/g, " ").replace(/\'/g, " ") + '\')">' + item[0] + '</a>'
						+ '</td></tr>';
					$('.summary_tab table').append(rows);

				}
			} else {
				alert(msg.result);
			}
		},
		error : function(msg) {
			alert(eval('(' + msg.responseText + ')').result);
		}
	})
}

//生成核心任务、结果数据
$("#create_core_result").click(function() {
	$.ajax({
		type : "post",
		url : "/issue/createCore",
		data : {
			linkedIssueId: getCookie("stdIssueId"),
			stdResId : getCookie("stdResId")
		},
		dataType : "json",
		beforeSend : function() {
			begin();
		},
		success : function(msg) {
			if (msg.status == "OK") {
				download(msg.result.coreResId);
			} else {
				alert(msg.result);
			}
		},
		complete : function() {
			stop();
		},
		error : function() {
			alert("数据请求失败！");
		//	console.log("ERROR");
		}
	});
});


function download(coreResId){
	$(function() {
		var form = $('<form method="POST" action="/coreResult/download">');
		form.append($('<input type="hidden" name="coreResId" value="' + coreResId + '"/>'));
		$('body').append(form);
		form.submit(); // 自动提交
	});
}

function getCookie(name) {
	var arr =document.cookie.match(new RegExp("(^|)"+name+"=([^;]*)(;|$)"));
	if(arr !=null) 
		return unescape(arr[2]); 
	return null;
}

function freshData() {
	var rid = getCookie("stdResId");
	$.ajax({
		type : "post",
		url : "/result/getCountResult",
		success : function(msg) {
			$('.summary_tab table tr:not(:first)').html('');
			if (msg.status == "OK") {
				// alert("删除成功");
				var items = msg.result;

				var indexOfTitle = parseInt(items[0][0]) + 1;
				var indexOfUrl = parseInt(items[0][1]) + 1;
				var indexOfTime = parseInt(items[0][2]) + 1;
				for (var i = 0; i < items.length - 1; i++) {
					// items第一行存储index，故从i+1读起
					item = items[i + 1];
					//console.log(item);
					rows = '<tr><td height="32" align="center"><input type="checkbox" style="width:20px;height:20px" class="' + i
						+ '"/></td><td height="32" align="center"><a href="javascript:;" onclick="showClusterDetails(' + i + ',\'' + rid + '\',' + item[0] + ')">'
						+ item[indexOfTitle] + '</a></td><td height="32" align="center">' + item[indexOfTime] + '</td><td height="32" align="center">' + item[0] + '</td></tr>';
					$('.summary_tab table').append(rows);

				}
			} else {
				alert(msg.result);
			}
		},
		error : function(msg) {
			alert(eval('(' + msg.responseText + ')').result);
		}
	});
}

/*准数据页面中 出图*/
function toPaint(currentSet, title) {
	setCookie('currentSet', currentSet);
	setCookie('title', title);
	baseAjax("data_results");
}



/* 合并类簇 */
function addLayData() {
	var sets = [];
	$(".summary_tab input:checked").each(function(i) {
		sets.push($(this).attr('class'));
	});
	
	$.ajax({
		type : "post",
		url : "/result/combineSets",
		data : JSON.stringify(sets),
		dataType : "json",
		contentType : "application/json",
		success : function(msg) {
			if (msg.status == "OK") {
				freshData();
			} else {
				alert(msg.result);
			}
		},
		error : function(msg) {
			alert(eval('(' + msg.responseText + ')').result);
		}
	});
}



/* 删除选中类簇 */
function deleteLayData() {
	var sets = [];
	$(".summary_tab input:checked").each(function(i) {
		sets.push($(this).attr('class'));
	});
	
	$.ajax({
		type : "post",
		url : "/result/deleteSets",
		data : JSON.stringify(sets),
		dataType : "json",
		contentType : "application/json",
		success : function(msg) {
			if (msg.status == "OK") {
				freshData();
			} else {
				alert(msg.result);
			}
		},
		error : function(msg) {
			alert(eval('(' + msg.responseText + ')').result);
		}
	});
}

// 全选所有聚类历史结果
$(function() {
	$("#historyAll").click(function() {
		if (this.checked) {
			$(".summary_tab tr :checkbox").prop("checked", true);
		} else {
			$(".summary_tab tr :checkbox").prop("checked", false);
		}
	})
})

//选中指定数目的类簇
function selectClusters(){	
	var num = $("#clusternum_input").val();
	$(".summary_tab tr").each(function(){
		var clusterNum = $(this).find('a:not(:first)').text();
	//	console.log(num+'--');
	//	console.log(clusterNum);
		if(num == clusterNum){
	//		console.log('yes');
			$(this).find('input:checkbox').prop("checked", true);
		}
	})
}