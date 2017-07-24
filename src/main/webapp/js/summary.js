/**
 * Created by Administrator on 2016/12/18.
 */
/* 搜索 */
function fileSearch() {
	var value = $(".summary_time input[name = 'timeradio']:checked").val();
	var end = new Date;
	var strEnd = "" + end.getFullYear() + "-";
	strEnd += (end.getMonth() + 1) + "-";
	strEnd += end.getDate() + 1;

	if (value == "1") {
		$(".summary_cont").css('display', 'block');
		var start = new Date(end.setDate(end.getDate() - 7));
		var strStart = "" + start.getFullYear() + "-";
		strStart += (start.getMonth() + 1) + "-";
		strStart += start.getDate();
	//	console.log(strStart);
	} else if (value == "2") {
		$(".summary_cont").css('display', 'block');
		var start = new Date(end.setMonth(end.getMonth() - 1));
		var strStart = "" + start.getFullYear() + "-";
		strStart += (start.getMonth() + 1) + "-";
		strStart += start.getDate();
		// console.log(strStart);
	} else if (value == "3") {
		$(".summary_cont").css('display', 'block');
		var start = new Date(end.setMonth(end.getMonth() - 3));
		var strStart = "" + start.getFullYear() + "-";
		strStart += (start.getMonth() + 1) + "-";
		strStart += start.getDate();
		// console.log(strStart);
	} else if (value == "4") {
		$(".summary_cont").css('display', 'block');
		var strEnd = $(".lol_end").val();
		var strStart = $(".lol_begin").val();
//		console.log(strEnd);
//		console.log(strStart);
	} else {

	}
	// console.log(value);
	$.ajax({
		type : "post",
		url : "/file/searchFileByCon",
		data : {
			startTime : strStart,
			endTime : strEnd
		},
		dataType : "json",
		success : function(msg) {
			// console.log(msg);
			if (msg.status == "OK") {
				// alert("success") ;
				var items = msg.result;
				// console.log(msg)
				$('.summary_up table tr:not(:first)').html('');
				$.each(
					items,
					function(i, item) {
						rows = '<tr><td height="32" align="center"><input type="checkbox" style="width: 20px; height: 20px;" name="groupIds"  class="'
								+ item.fileId
								+ '" /></td><td height="32" align="center">'
								+ item.fileName
								+ '</td><td height="32" align="center">'
								+ item.creator
								+ '</td><td height="32" align="center">'
								+ new Date(
										item.uploadTime.time)
										.format('yyyy-MM-dd hh:mm:ss')
								+ '</td></tr>'
						$('.summary_up table').append(rows);
					})
			} else {
				alert(msg.result);
			}
		},
		error : function(msg) {
			alert(msg.result);
		}
	});
}

// 数据汇总
function fileSummary() {
	$('.summary_tab table tr:not(:first)').html('');
	var fileIds = [];
	$(".summary_up input:checked").each(function(i) {
		fileIds.push($(this).attr("class"));
	});
	if (fileIds.length != 0) {
		$.ajax({
					type : "post",
					url : "/issue/miningByFile",
					data : JSON.stringify(fileIds),
					dataType : "json",
					contentType : "application/json",
					beforeSend : function() {
						begin();
					},
					success : function(msg) {
						// console.log(msg);
						if (msg.status == "OK") {
							var items = msg.result;

							var indexOfTitle = parseInt(items[0][0]) + 1;
							var indexOfUrl = parseInt(items[0][1]) + 1;
							var indexOfTime = parseInt(items[0][2]) + 1;
//							console.log(indexOfTitle);
//							console.log(indexOfUrl);
//							console.log(indexOfTime);
							for (var i = 0; i < items.length - 1; i++) {
								// items第一行存储index，故从i+1读起
								item = items[i + 1];
								
								rows = '<tr><td height="32" align="center"><input type="checkbox" style="width:20px;height:20px" class="' 
								+ i
								+ '"/></td><td height="32" align="center"><a href="javascript:;" onclick="showClusterDetails('
								+ i + ',\''
								+ '' + '\',' 
								+ item[0] + ')">' 
								+ item[indexOfTitle] + 
								'</a></td><td height="32" align="center">' 
								+ item[indexOfTime] + 
								'</td><td height="32" align="center">'
								+ '<a href="javascript:;" onclick="toPaint(' + 
								i + ',\'' + item[indexOfTitle].replace(/\"/g, " ").replace(/\'/g, " ") + '\')">' 
								+ item[0] + '</a>' + '</td></tr>';
								$('.summary_tab table').append(rows);
							}
						} else {
							alert(msg.result);
						}

					},
					complete : function() {
						stop();
					},
					error : function(msg) {
						alert(msg.result);
					}
				});
	}else{
		alert("请先按搜索按钮！");
	}
}

function toPaint(currentSet, title) {
	setCookie('currentSet', currentSet);
	setCookie('title', title);
	baseAjax("data_results");
}
/* 合并 */
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

//刷新数据
function freshData() {
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
					
					rows = '<tr><td height="32" align="center"><input type="checkbox" style="width:20px;height:20px" class="' + i
						+ '"/></td><td height="32" align="center"><a href="javascript:;" onclick="showClusterDetails(' + i + ',\'' + '' + '\',' + item[0] + ')">'
						+ item[indexOfTitle] + '</a></td><td height="32" align="center">' + item[indexOfTime] + '</td><td height="32" align="center">' + '<a href="javascript:;" onclick="toPaint(' + i
						+ ',\'' + item[indexOfTitle].replace(/\"/g, " ").replace(/\'/g, " ") + '\')">' + item[0] + '</a>' + '</td></tr>';
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

/* 删除 */
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

$(function() {
	$("#chooseAll").click(function() {
		if (this.checked) {
			$(".summary_tab tr :checkbox").prop("checked", true);
		} else {
			$(".summary_tab tr :checkbox").prop("checked", false);
		}
	});
})


//选中指定数目的类簇
function selectClusters(){	
	var num = $("#clusternum_input").val();
	$(".summary_tab tr").each(function(){
		var clusterNum = $(this).find('a:not(:first)').text();
//		console.log(num+'--');
//		console.log(clusterNum);
		if(num == clusterNum){
//			console.log('yes');
			$(this).find('input:checkbox').prop("checked", true);
		}
	})
}