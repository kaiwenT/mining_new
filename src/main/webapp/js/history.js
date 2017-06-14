/**
 * Created by Administrator on 2016/12/18.
 */
function historyRecord() {
	$.ajax({
		type : "post",
		url : "/result/queryResultList",
		success : function(msg) {
			$('.summary_up table tr:not(:first)').html('');
			if (msg.status === 'OK') {
				var items = msg.result;
				$.each(items, function(i, item) {
					rows = '<tr><td height="32" align="center"><a href="javascript:;" onclick="historyData(\'' + item.rid + '\')">' + item.comment + '</a></td><td height="32" align="center">'
						+ item.creator + '</td><td height="32" align="center">' + new Date(item.createTime.time).format('yyyy-MM-dd hh:mm:ss')
						+ '</td><td height="32" align="center"><img src="images/reset.png" id="' + item.rid + '" onclick="historyReset()" /> <img src="images/delete.png" id="' + item.rid
						+ '" onclick="historyDel()" /></td></tr>'
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
					rows = '<tr><td height="32" align="center"><input type="checkbox" class="' + i + '"/></td><td height="32" align="center"><a href="' + item[indexOfUrl] + '" target="_blank">'
						+ item[indexOfTitle] + '</a></td><td height="32" align="center">' + item[indexOfTime] + '</td><td height="32" align="center">' + '<a href="javascript:;" onclick="toPaint(' + i
						+ ',\'' + item[indexOfTitle].replace(/\"/g, " ").replace(/\'/g, " ") + '\')">' + item[0] + '</a>' + '</td></tr>';
					$('.summary_tab table').append(rows);

				}
			} else {
				alert(msg.result);
			}
		},
		error : function() {
			alert("请求失败");
		}
	})
}

function buildStandardData() {
	$.ajax({
		type : "post",
		url : "/issue/create"
	});
}

/* 删除 */
function historyDel() {
	$(".summary_up table tr").unbind('click').on("click", "img", function() {
		var result_id = $(this).attr("id");
		console.log(result_id);
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
					console.log(msg);
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

function toPaint(currentSet, title) {
	setCookie('currentSet', currentSet);
	setCookie('title', title);
	baseAjax("data_results");
}

function historyReset() {
	$(".summary_up table tr").unbind('click').on("click", "img", function() {
		var result_id = $(this).attr("id");
		console.log(result_id);
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
					alert("合并失败");
				}
			},
			error : function() {
				alert("请求失败");
			}
		})
	})
}

/* 合并 */
function addLayData() {
	var sets = [];
	$(".summary_tab input:checked").each(function(i) {
		sets.push($(this).attr('class'));
	});
	console.log(sets);
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
		error : function() {
			alert("数据请求失败");
		}
	});
}

function freshData() {
	$.ajax({
		type : "post",
		url : "/result/getCountResult",
		success : function(msg) {
			$('.summary_tab table tr:not(:first)').html('');
			if (msg.status == "OK") {
				// alert("删除成功");
				var items = msg.result;
				console.log(items);

				var indexOfTitle = parseInt(items[0][0]) + 1;
				var indexOfUrl = parseInt(items[0][1]) + 1;
				var indexOfTime = parseInt(items[0][2]) + 1;
				for (var i = 0; i < items.length - 1; i++) {
					// items第一行存储index，故从i+1读起
					item = items[i + 1];
					console.log(item);
					rows = '<tr><td height="32" align="center"><input type="checkbox" class="' + i + '"/></td><td height="32" align="center"><a href="' + item[indexOfUrl] + '" target="_blank">'
						+ item[indexOfTitle] + '</a></td><td height="32" align="center">' + item[indexOfTime] + '</td><td height="32" align="center">' + '<a href="javascript:;" onclick="toPaint(' + i
						+ ',\'' + item[indexOfTitle].replace(/\"/g, " ").replace(/\'/g, " ") + '\')">' + item[0] + '</a>' + '</td></tr>';
					$('.summary_tab table').append(rows);

				}
			} else {
				alert(msg.result);
			}
		},
		error : function() {
			alert("数据请求失败");
		}
	});
}

/* 删除 */
function deleteLayData() {
	var sets = [];
	$(".summary_tab input:checked").each(function(i) {
		sets.push($(this).attr('class'));
	});
	console.log(sets);
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
		error : function() {
			alert("数据请求失败");
		}
	});
}

$(function() {
	$("#historyAll").click(function() {
		if (this.checked) {
			$(".summary_tab tr :checkbox").prop("checked", true);
		} else {
			$(".summary_tab tr :checkbox").prop("checked", false);
		}
	})
})
