/**
 * Created by Administrator on 2016/12/18.
 */
// document.write('<script type="text/javascript"
// src="js/cluster_details.js"></script>');

function setCookie_resultId(value){
	var Days = 1; // 此 cookie 将被保存 1 天
	var exp　= new Date();
	exp.setTime(exp.getTime() +Days*24*60*60*1000);
	document.cookie = "resultId="+ escape (value) + ";expires=" + exp.toGMTString();
}

function getCookie(name) {
	var arr =document.cookie.match(new RegExp("(^|)"+name+"=([^;]*)(;|$)"));
	if(arr !=null) 
		return unescape(arr[2]); 
	return null;
}
//显示聚类的结果表, 目前没有用到
function historyRecord() {
	$.ajax({
		type : "post",
		url : "/result/queryResultList",
		success : function(msg) {
			$('.summary_up table tr:not(:first)').html('');
			if (msg.status === 'OK') {
				var items = msg.result;
				$.each(items, function(i, item) {
					if(i==0)
					{
						historyData(item.rid);
						setCookie_resultId(item.rid);
						var issueId = getCookie("issueId");
						showExtensiveIssueName(issueId);
					}
					rows = '<tr><td height="32" align="center"><a href="javascript:;" onclick="historyData(\'' + item.rid + '\')">' + item.comment + '</a></td><td height="32" align="center">'
						+ item.creator + '</td><td height="32" align="center">' + new Date(item.createTime.time).format('yyyy-MM-dd hh:mm:ss')
						+ '</td><td height="32" align="center"><button type="button" class="btn btn-primary" id="' + item.rid + '" onclick="historyReset()">重置</button> <button type="button" class="btn btn-danger" id="' + item.rid
						+ '" onclick="historyDel()">删除</button></td></tr>'
					$('.summary_up table').append(rows);
				});
			}
		},
		error : function(msg) {
			alert("数据请求失败");
		}
	});
}
historyRecord();

//显示当前任务名字
function showExtensiveIssueName(issueId) {
	$.ajax({
		type : "post",
		url : "/file/queryIssueFiles",
		data : {
			issueId : issueId
		},
		dataType : "json",
		success : function(msg) {
			if (msg.status == "OK") {
				var items = msg.result.issue;
				$('.issueName').text("任务名称：" + items.issueName);
			} else {
				alert(msg.result);
			}

		},
		error : function() {
			alert("error:datashow.js-->showExtensiveIssueDetails(issueId)")
		}
	});
}

//显示聚类结果
function historyData(rid) {
	$.ajax({
		type : "post",
		url : "/result/getCountResult",
		data : {
			resultId : rid
		},
		dataType : "json",
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
					rows = '<tr><td height="32" align="center"><input type="checkbox" style="width:20px;height:20px" class="' + i
						+ '"/>&nbsp;'
						+(i+1)+'</td><td height="32" align="center"><a href="javascript:;" onclick="showClusterDetails('
						// + item[indexOfUrl]
						// + '
						+ i + ',\''
						// + item[indexOfUrl]
						+ rid + '\',' + item[0] + ')">' + item[indexOfTitle] + '</a></td><td height="32" align="center">' + item[indexOfTime] + '</td><td height="32" align="center">'
						//添加画图的代码为：'<a href="javascript:;" onclick="toPaint(' + i + ',\'' + item[indexOfTitle].replace(/\"/g, " ").replace(/\'/g, " ") + '\')">' + item[0] + '</a>'
						+  item[0]  + '</td></tr>';
					$('.summary_tab table').append(rows);

				}
			} else {
				alert(msg.result);
			}
		},
		error : function(msg) {
			alert(msg.result);
		}
	})
}

function freshData() {
	var rid = getCookie("resultId");
	$.ajax({
		type : "post",
		url : "/result/getCountResult",
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
					rows = '<tr><td height="32" align="center"><input type="checkbox" style="width:20px;height:20px" class="' + i
						+ '"/>&nbsp;'
						+(i+1)+'</td><td height="32" align="center"><a href="javascript:;" onclick="showClusterDetails(' + i + ',\'' + rid + '\',' + item[0] + ')">'
						+ item[indexOfTitle] + '</a></td><td height="32" align="center">' + item[indexOfTime] + '</td><td height="32" align="center">' + item[0] + '</td></tr>';
					$('.summary_tab table').append(rows);
				}
			} else {
				alert(msg.result);
			}
		},
		error : function(msg) {
			alert(msg.result);
		}
	});
}

function buildStandardData() {
	$.ajax({
		type : "post",
		url : "/issue/create"
	});
}

/* 删除历史记录 */
function historyDel() {
	$(".summary_up table tr").unbind('click').on("click", "button", function() {
		var result_id = $(this).attr("id");
		
		fileDel(result_id);
		function fileDel(result_id) {

			$.ajax({
				type : "post",
				url : "/result/delResultById",
				data : {
					resultId : result_id
				},
				dataType : "json",
				success : function(msg) {
					
					if (msg.status == "OK") {
						historyRecord();
					} else {
						alert("fail");
					}
				},
				error : function() {
					alert("数据请求失败")
				}
			})
		}
	})
}

//下载准数据
function download(){
	var form = $('<form method="POST" action="/file/download">');
	$('body').append(form);
	form.submit(); // 自动提交
}

/*准数据页面中不需要出图
function toPaint(currentSet, title) {
	setCookie('currentSet', currentSet);
	setCookie('title', title);
	baseAjax("data_results");
}
*/

//聚类结果重置
function Reset() {
	var result_id  = getCookie("resultId");
	$.ajax({
		type : "post",
		url : "/result/resetResultById",
		data : {
			resultId : result_id
		},
		dataType : "json",
		success : function(msg) {
			if (msg.status == "OK") {
				freshData();
			} else {
				alert("重置失败");
			}
		},
		error : function(msg) {
			alert(msg.result);
		}
	})
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
			alert(msg.result);
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
			alert(msg.result);
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
		var clusterNum = $(this).find('td:last').text();
	//	console.log(num+'--');
	//	console.log(clusterNum);
		if(num == clusterNum){
	//		console.log('yes');
			$(this).find('input:checkbox').prop("checked", true);
		}
	})
}