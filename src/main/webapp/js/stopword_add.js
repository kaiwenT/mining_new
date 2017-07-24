/**
 * Created by Jack on 2017/5/8.
 */
// 添加停用词
function addStopwordInfor(word) {
	if (null == word)
		word = "";
	var num = $('#addStopword').attr("data-num");
	var row = '<tr class="infor_tab02_tit"> <td colspan="2" height="40" align="center" valign="middle" bgcolor="#ffffff">'
			+ (++num)
			+ '</td> <td colspan="3" height="40" align="center" valign="middle" bgcolor="#ffffff" > <input name="stopword" type="text" class="form-control" style="width: 100%; height: 100%; text-align: center" placeholder="请输入停用词" value="'
			+ word + '"> </td> </tr>';
	$('#addStopword').before(row);
	$('#addStopword').attr("data-num", num);
}

function delStopwordInfor(){
	var prevTr = $('#addStopword').prev();
	var num = $('#addStopword').attr("data-num");
	$('#addStopword').attr("data-num", --num);
	prevTr.remove();
}

// 提交添加停用词请求
function addStopword() {
	// console.log($("#urlWebsite").val())
	// 获取Json数据
	/**
	 * 
	 */
//	console.log("111122"+getWords());
	$.ajax({
		type : "post",
		url : "/stopword/insertStopwords",
		data : {
			words : getWords()
		},
		dataType : "json",
        traditional:true,
		success : function(msg) {
//			console.log(msg);
			if (msg.status == "OK") {
				baseAjax("stopword_infor");
			} else {
				alert(msg.result);
			}
			stop();
		},
		error : function() {
			alert("数据请求失败");
		}
	})
}

function getWords(){
	var words = new Array();
	$("input[name='stopword']").each(function(index,element){
		words.push($(this).val());
	});
//	console.log(words);
	return words;
}

$(function() {
	// 阻止浏览器默认行。
	$(document).on({
		dragleave : function(e) { // 拖离
			e.preventDefault();
		},
		drop : function(e) { // 拖后放
			e.preventDefault();
		},
		dragenter : function(e) { // 拖进
			e.preventDefault();
		},
		dragover : function(e) { // 拖来拖去
			e.preventDefault();
		}
	});
	// 用javascript来侦听drop事件，首先要判断拖入的文件是否符合要求，包括图片类型、大小等，
	// 然后获取本地图片信息，实现预览，最后上传
	var box = document.getElementById('drop_area'); // 拖拽区域
	box.addEventListener("drop", function(e) {
		e.preventDefault(); // 取消默认浏览器拖拽效果
		var fileList = e.dataTransfer.files; // 获取文件对象
		// 检测是否是拖拽文件到页面的操作
		if (fileList.length == 0) {
			return false;
		}
		// 检测文件是不是excel文件
		for (var index = 0; index < fileList.length; index++) {
			// file_array[file_array.length] = fileList[index];
			var filename = fileList[index].name;
			if (filename.lastIndexOf("xls") !== -1
					|| filename.lastIndexOf("xlsx") !== -1) {
				var file = fileList[index];
				var fd = new FormData();
				fd.append("file", file);
				$.ajax({
					async : false,
					crossDomain : true,
					url : "/file/getStopword",
					method : "POST",
					dataType:"json",
					processData : false,
					contentType : false,
					mimeType : "multipart/form-data",
					data : fd,
					success : function(msg) {
//						console.log(":"+msg.status)
						if (msg.status == "OK") {
							//判断上一个停用词表格是否为空若为空则删除
							while(1){
//							console.log($('#addStopword').prev().find("input").val());
							if($('#addStopword').prev().find("input").val()==""){
								delStopwordInfor();
							}else{
								break;
							}
							}
							// 批量添加停用词（预览）
							var items = msg.result;
							$.each(items, function(idx, item) {
								addStopwordInfor(item);
							});
						} else {
							alert(msg.result);
						}
					},
					error : function() {
						alert("预览失败");
						stop();
					}
				});
			} else {
				alert(filename + " 不是Excel文件");
			}
		}
	}, false);
});
