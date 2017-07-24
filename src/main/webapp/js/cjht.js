/**
 * Created by Administrator on 2016/12/18.
 */
$(document).ready(function() {
	$(':radio').click(function() {
		if (this.checked) {
			$(this).parent().css("color", "red");
			$(this).parent().siblings('label').css("color", "black");
		}
	});
})

function creatInt() {
//	var issueType = $("input[name='issueType']:checked").val();
	//默认创建原始数据任务
	var issueType = "extensive";
	setCookie(issueType);
	var title = $("#chuangjian").val().replace(" ", "");
	if (title === undefined || title == '') {
		alert("请输入任务名称");
		return;
	}

	$.ajax({
		type : "post",
		url : "/issue/create",
		data : {
			issueName : $("#chuangjian").val(),
			issueType : issueType,
		},
		dataType : "json",
		success : function(msg) {
			if (msg.status == "OK") {
				baseAjax("topic_list");
				$('.left ul li').eq(1).addClass('current').siblings().removeClass("current")
			} else {
				alert("fail");
			}
		},
		error : function() {
			alert("数据请求失败");
		}
	});
}

function setCookie(value) {
    var Days = 1; // 此 cookie 将被保存 1 天
    var exp　= new Date();
    exp.setTime(exp.getTime() +Days*24*60*60*1000);
    document.cookie = "issueType="+ escape (value) + ";expires=" + exp.toGMTString();
}
