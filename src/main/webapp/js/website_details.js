/**
 * Created by Jack on 2017/7/17.
 */

function websiteInforEdit() {
    $("#new_name").removeAttr("disabled");
    $("#new_column").removeAttr("disabled");
    $("#new_type").removeAttr("disabled");
    $("#new_rank").removeAttr("disabled");
    $("#new_incidence").removeAttr("disabled");
    $("#new_weight").removeAttr("disabled");
    $("#btn_edit").css("display","none");
    $("#btn_submit").css("display","block");
}

function domainOneInfoChange() {
    var uuid = getCookie("domain_id");
    $.ajax({
        type:"post",
        url:"/domain/updateDomainOne",
        data:{
            uuid:uuid,
            url:$("#url").val(),
            name:$("#new_name").val(),
            column:$("#new_column").val(),
            type:$("#new_type").val(),
            rank:$("#new_rank").val(),
            incidence:$("#new_incidence").val(),
            weight:$("#new_weight").val()
        },
        datatype:"json",
        success:function (msg) {
            if(msg.status == "OK"){
                alert(msg.result);
                baseAjax("website_one_details");
            }
        },
        error: function () {
            alert("数据请求失败");
        }
    })
}

function domainTwoInfoChange() {
    var uuid = getCookie("uuid");
    $.ajax({
        type:"post",
        url:"/domain/updateDomainTwo",
        data:{
            uuid:uuid,
            url:$("#url").val(),
            name:$("#new_name").val(),
            column:$("#new_column").val(),
            type:$("#new_type").val(),
            rank:$("#new_rank").val(),
            incidence:$("#new_incidence").val(),
            weight:$("#new_weight").val()
        },
        datatype:"json",
        success:function (msg) {
            if(msg.status == "OK"){
                alert(msg.result);
                baseAjax("website_two_details");
            }
        },
        error: function () {
            alert("数据请求失败");
        }
    })
}

function back(){
    baseAjax("website_infor");
}