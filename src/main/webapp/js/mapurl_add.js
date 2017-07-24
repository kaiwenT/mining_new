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
	box = document.getElementById('drop_area'); // 拖拽区域
	box.addEventListener("drop", function(e) {
		e.preventDefault(); // 取消默认浏览器拖拽效果
//		console.log("before");
//		console.log(e.dataTransfer.files);
		var file = e.dataTransfer.files[0]; // 获取文件对象
		if (file.name.lastIndexOf("xls") !== -1
				|| file.name.lastIndexOf("xlsx") !== -1) {
			box.innerHTML=file.name;
			var fd = new FormData();
			fd.append("file", file);
			$("#submitUpload").one("click",function() {
				submit(fd);
			});
		} else {
			alert(file.name + " 不是Excel文件");
		}
	}, false);
});

function submit(fd) {
	$.ajax({
		crossDomain : true,
		url : "/file/uploadDomainExcel",
		method : "POST",
		processData : false,
		contentType : false,
		dataType : "json",
		mimeType : "multipart/form-data",
		data : fd,
		beforeSend : function() {
			begin();
			},
		success : function(msg) {
			alert(msg.result);
			baseAjax("website_infor");
		},
		complete : function() {
			stop();
			box.innerHTML="将文件拖拽到此处";
		},
		error : function() {
			alert("数据请求失败");
		}
	});
}