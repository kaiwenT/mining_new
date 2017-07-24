//生成x数据，x=“准数据or核心数据”

//生成准数据 旧版本，新的生成准数据在create_std_result.js
//function sczsj() {
//	var issueType = "standard";
//	scxsj(issueType);
//}

//生成核心数据
function schxsj(stdResId){
	var issueType = "core";
	scxsj(issueType,stdResId);
}

//生成x数据
function scxsj(issueType,stdResId){
	$.ajax({
		type : "post",
		url : "/issue/createIssueWithLink",
		data : {
			issueType : issueType,
			stdResId : stdResId
		},
		dataType : "json",
		beforeSend : function() {
			begin();
		},
		success : function(msg) {
			if (msg.status == "OK") {
				// var value = prompt("请输入准数据名：");
				// return value;
				baseAjax("topic_list");
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
}

function setCookie_issueType(value){
	var Days = 1; // 此 cookie 将被保存 1 天
	var exp　= new Date();
	exp.setTime(exp.getTime() +Days*24*60*60*1000);
	document.cookie = "issueType="+ escape (value) + ";expires=" + exp.toGMTString();
}

function setCookie_issueId(value){
	var Days = 1; // 此 cookie 将被保存 1 天
	var exp　= new Date();
	exp.setTime(exp.getTime() +Days*24*60*60*1000);
	document.cookie = "issueId="+ escape (value) + ";expires=" + exp.toGMTString();
}

function queryLinkedIssue(issueType){
	$.ajax({
		type : "post",
		url : "/issue/queryLinkedIssue",
		data : {
			issueType : issueType,
		},
		dataType : "json",
		beforeSend : function() {
			begin();
		},
		success : function(msg) {
			if (msg.status == "OK") {
			//	setCookie_issueType(issueType);
				setCookie_issueId(msg.result.issueId);
				baseAjax("topic_details_"+issueType);
			} else {
				alert(msg.result);
			}
		},
		complete : function() {
			stop();
		},
		error : function() {
		//	console.log("ERROR");
			alert("数据请求失败！");
		}
	});
}
