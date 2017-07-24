var fileArray = new Array();
border();
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
                    url : "/file/getColumnTitle",
                    method : "POST",
                    processData : false,
                    contentType : false,
                    mimeType : "multipart/form-data",
                    data : fd,
                    success : function(response) {
                        reSetView(response, filename, fileArray.length);
                    },
                    error : function() {
                        alert("预览失败");
                        stop();
                    }
                });
                fileArray.push(file);
            } else {
                alert(filename + " 不是Excel文件");
            }
        }
    }, false);
});
function reSetView(response, filename, index) {
    var msg = JSON.parse(response);
    if (msg.status !== "OK") {
        alert("预览失败，请重新选择文件！");
        return;
    }
    var array = msg.result;
    var date_ = new Date();
    var now_time = date_.toLocaleDateString();// 当前日期
    var li_context = '<li>文件：<input type="text" class="files_name form-control" style="width:110px;" name="'
            + index
            + '" value="'
            + filename
            + '" /> URL：<select class="select01  form-control">'
            + getSpinner(array, '链接|网址')
            + '</select>标题：<select class="select02  form-control">'
            + getSpinner(array, '标题|内容')
            + '</select> 时间：<select class="select03  form-control">'
            + getSpinner(array, '发布时间|发贴时间|时间')
            + '</select> 类型：<select class="select04  form-control"><option> 微博</option><option selected = true> 新闻</option></select><button type="button" class="btn btn-danger btn_up_del02">删除</button><button type="button" class="btn btn-primary btn_up_del01">上传</button></li>'
    $("#file_ul").append(li_context);
    $("#file_ul").css("border", "2px solid blue");
    $("#file_ul").css("border-radius", "5px");
    $("#file_ul").css("padding", "0 5px");
    up_del();
    all_up();
}
function getSpinner(array, regex) {
	var patt = new RegExp(regex); // 注意是非全局匹配
	var item = "";
	var alreay = false;
	for (var i = 0; i < array.length; i++) {
		if (patt.test(array[i]) && alreay == false) {
			item += '<option value= ' + i + ' selected = true>' + array[i] + '</option>';
			alreay = true;
		} else {
			item += '<option value= ' + i + '>' + array[i] + '</option>';
		}
	}
	item += "";
	return item;
}

var data12 = {
    "status" : "OK",
    "result" : [ "属性", "标题", "链接", "来源/发布人", "发布时间", "网站", "频道", "点击数", "回复数",
            "类型", "记者/作者", "内容长度", "分词", "摘要/内容", "发布日期" ]
}

function up_del() {
    $(".btn_up_del01").unbind('click').click(
            function() {
                var arrary = $(this).parent("li").children(".files_name").attr(
                        "name");
                var fileName = $(this).parent("li").children(".files_name")
                        .val();
                var urlIndex = $(this).parent("li").children("select.select01")
                        .val();
                var titleIndex = $(this).parent("li").children(
                        "select.select02").val();
                var time = $(this).parent("li").children("select.select03")
                        .val();
                var sourceType = $(this).parent("li").children(
                        "select.select04").val();
 //               console.log(arrary);
 //               console.log(fileName);
 //               console.log(time);
 //               console.log(urlIndex);
 //               console.log(titleIndex);
 //               console.log(sourceType);
                /* cookie_value1="'"+item.fileId+"'"; */
                upFile(fileArray[parseInt(arrary)], urlIndex, titleIndex, time,
                        sourceType);
                localRefresh();
                $(this).parent("li").remove();
                border();
            })

    $(".btn_up_del02").unbind('click').click(function() {
        var arrary = $(this).parent("li").children(".files_name").attr("name");
        $(this).parent("li").remove();
        border();
    });
}

Array.prototype.contains = function(needle) {
    for (i in this) {
        if (this[i] == needle)
            return true;
    }
    return false;
}
function all_up() {
    $(".btn_up_del03").unbind('click').click(function() {
        var liGroup = $("#file_ul li");
        for (var i = 0; i < liGroup.length; i++) {
            var num = liGroup.eq(i).children(".files_name").attr("name");
            var file = fileArray[num];
            var urlIndex = liGroup.eq(i).children("select.select01").val();
            var titleIndex = liGroup.eq(i).children("select.select02").val();
            var time = liGroup.eq(i).children("select.select03").val();
            var sourceType = liGroup.eq(i).children("select.select04").val();
 //           console.log(num);
 //           console.log(time);
 //           console.log(urlIndex);
 //           console.log(titleIndex);
 //           console.log(sourceType);
            upFile(file, urlIndex, titleIndex, time, sourceType);
        }
        localRefresh();
        allDel();
    });
}
function upFile(filex, urlIndex, titleIndex, time, sourceType) {
    var form = new FormData();
    form.append("file", filex);
    form.append("urlIndex", urlIndex);
    form.append("titleIndex", titleIndex);
    form.append("timeIndex", time);
    form.append("sourceType", sourceType);
 //   console.log("(((((");
 //   console.log(form);
    $.ajax({
        async : false,
        crossDomain : true,
        url : "/file/upload",
        method : "POST",
        processData : false,
        contentType : false,
        mimeType : "multipart/form-data",
        data : form,
        success : function(response) {
            var msg = JSON.parse(response);
            if (msg.status == "OK") {
                // alert(msg.tagName);
                // cookie_value1="'"+item.fileId+"'";

            } else {
                alert("上传失败");
            }
        },
        error : function() {
            alert("预览失败");
            stop();
        }
    });
}
function allDel() {
    $(".up_del li").remove();
    border();
}

function border() {
    if ($("#file_ul li").length == 0) {
        $("#file_ul").css("border", "none");
    } else {
        $("#file_ul").css("border", "2px solid blue");
        $("#file_ul").css("border-radius", "5px");
        $("#file_ul").css("padding", "0 5px");
    }
}