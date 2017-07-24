/**
 * Created by Jack on 2017/5/7.
 */
//停用词信息展示

function stopwordInforShow(page){
 //   console.log(page+"--------------")
    search_click=false;
    $.ajax({
        type:"post",
        url:"/stopword/selectAllStopword",
        data:{
            start:(parseInt(10*page-10)),
            limit:10
        },
        dataType:"json",
        success: function(msg){
            $('.infor_tab02 tr:not(:first)').html("");
            if( msg.status == "OK"){
                // alert("success");
                var items = msg.result ;
                var cookie_value1;
                var cookie_value2;
                var cookie_value3;
                var cookie_value4;
                $.each(items,function(idx,item) {
                    // alert(msg.tagName);
                    cookie_value1="'"+item.id+"'";
                    cookie_value2="'"+item.word+"'";
                    cookie_value3="'"+item.creator+"'";
                    cookie_value4="'"+new Date(item.createTime.time).format('yyyy-MM-dd hh:mm:ss')+"'";
                    row= '<tr><td colspan="1" height="40" align="center" bgcolor="#ffffff">'+((page-1)*10+idx+1)+'</td><td colspan="2" height="40" align="center" bgcolor="#ffffff">'+item.word+'</td><td colspan="2" height="40" align="center" bgcolor="#ffffff">'+new Date(item.createTime.time).format('yyyy-MM-dd hh:mm:ss')+'</td><td colspan="2" height="40" align="center" bgcolor="#ffffff">'+item.creator+'</td><td colspan="1" height="40" align="center" bgcolor="#ffffff"><button type="button" class="btn btn-danger delStopword" id="'+item.id+'">删除</button></td></tr>'
                    $('.infor_tab02').append(row);
                });
            }else{
                // alert("fail");
                $('.infor_tab02 tr:not(:first)').html("");
            }
        },
        error: function(){
            alert("数据请求失败");
        }
    })
}
/*function setCookie(value1,value2,value3,value4){
    // alert(name+value);
    var cookie_name1="id";
    var cookie_name2="word";
    var cookie_name3="creator";
    var cookie_name4="createTime";
    var Days = 1; // 此 cookie 将被保存 1 天
    var exp　= new Date();
    exp.setTime(exp.getTime() +Days*24*60*60*1000);
    document.cookie = cookie_name1 +"="+ escape (value1) + ";expires=" + exp.toGMTString();
    document.cookie = cookie_name2 +"="+ escape (value2) + ";expires=" + exp.toGMTString();
    document.cookie = cookie_name3 +"="+ escape (value3) + ";expires=" + exp.toGMTString();
    document.cookie = cookie_name4 +"="+ escape (value4) + ";expires=" + exp.toGMTString();
    baseAjax = "website_change.html";
}*/

function initShowPage(currenPage){
    var listCount = 0;
    if("undefined" == typeof(currenPage) || null == currenPage){
        currenPage = 1;
    }
    $.ajax({
        type: "post",
        url: "/stopword/selectStopwordCount",
        dataType: "json",
        success: function (msg) {
            if (msg.status == "OK") {
                // alert("success");
                listCount = msg.result;
                $("#page").initPage(listCount,currenPage,stopwordInforShow);
            } else {
                alert(msg.result);
            }
        },
        error: function () {
            alert("数据请求失败");
        }})
    }
initShowPage(1)

/*
/!**
 * 根据页码加载数据
 *
 * @param {整型}
 *            page 页码
 *!/
var search_click;
function setViewForPage(page){
    if(search_click){
        stopwordInforSearch(page);
    }else{
        stopwordInforShow(page);
    }
}
/!**
 * 省略号点击
 *!/
function setPageChangeView(){
    var bt_name=parseInt($("#other").attr('name'))+3;
    updatePageValue(bt_name);
    setViewForPage(bt_name);
    setFirstSelected();
    updateNowPage(bt_name);
}
/!**
 * 更新页码数据
 *
 * @param {Object}
 *            base_num
 *!/
function updatePageValue(base_num){
    var p1=parseInt(base_num);
    var p2=parseInt(base_num)+1;
    var p3=parseInt(base_num)+2;
    $("#p_1").val(p1);
    $("#p_2").val(p2);
    $("#p_3").val(p3);
    $("#other").attr('name',p1);
}
/!**
 * 页码点击
 *
 * @param {Object}
 *            p_id 页码
 *!/
function pageNumClick(p_id){
    // background: #0e63ab;
    // color: #fff;
    var button=document.getElementById(p_id);
    var page=button.value;
    if(page!=undefined&&page.length>0){
        setViewForPage(page);
        updateNowPage(page);
        // $(this).addClass("cur").siblings().removeClass("cur");
        cleanAllSelected();
        button.style.background='#0e63ab';
        button.style.color='#FFFFFF';
    }
}
/!**
 * 设置第一个页码按钮为选中状态
 *!/
function setFirstSelected(){
    cleanAllSelected();
    $("#p_1").css("background","#0e63ab");
    $("#p_1").css("color","#FFFFFF");
}
function setSecondSelected(){
    cleanAllSelected();
    $("#p_2").css("background","#0e63ab");
    $("#p_2").css("color","#FFFFFF");
}
function setThirdSelected(){
    cleanAllSelected();
    $("#p_3").css("background","#0e63ab");
    $("#p_3").css("color","#FFFFFF");
}
/!**
 * 清除所有的选中状态
 *!/
function cleanAllSelected(){
    $("#p_1").css("background","#CCCCCC");
    $("#p_1").css("color","buttontext");
    $("#p_2").css("background","#CCCCCC");
    $("#p_2").css("color","buttontext");
    $("#p_3").css("background","#CCCCCC");
    $("#p_3").css("color","buttontext");
}
/!**
 * 上一页，下一页点击
 *
 * @param {Object}
 *            action -1上一页，1下一页
 *!/
function changPageOne(action){
    var now_page=parseInt($("#down_page").attr('name'));
    var page=now_page+action;
    if(page>0){
        updateAllStyleAndData(page,action);
    }else{
        alert("这是第一页！！");
    }
}
/!**
 * 跳zhuan
 *!/
function changePage(){
    var page=$(".go_num").val();
    if(page!=undefined&&page.length>0){
        updateAllStyleAndData(page);
    }
}
function updateAllStyleAndData(page,action){
    updateNowPage(page);
    setViewForPage(page);
    if((page-1)%3==0){// 位置：第一个按钮 123 456 789
        setFirstSelected();
        if(action==1||action==undefined){// 点击下一页
            updatePageValue(page);
        }
    }else if(page%3==0){// 位置：第三个按钮
        setThirdSelected();
        if (action==-1||action==undefined) {// 点击上一页
            updatePageValue(page-2);
        }
    }else{// 位置：第二个按钮
        setSecondSelected();
        if(action==undefined){
            updatePageValue(page-1);
        }
    }
}
/!**
 * 更新当前页码
 *
 * @param {Object}
 *            page 当前页
 *!/
function updateNowPage(page){
    $("#down_page").attr('name',page);
}*/

// 信息搜索
function stopwordInforSearch(page){
    search_click=true;
    // var obj1 = $("#web_url").val();
    var obj1 = $("#stopword_search").val();
    setFirstSelected();
    $.ajax({
        type:"post",
        url:"/stopword/selectByCondition",
        data:{
            word:obj1,
            start:(parseInt(10*page-10)),
            limit:10
        },
        dataType:"json",
        success: function(msg){
            if( msg.status == "OK"){
                // alert("success");
                $('.infor_tab02 tr:not(:first)').html("");
                var items = msg.result ;
                var cookie_value1;
                var cookie_value2;
                var cookie_value3;
                var cookie_value4;
                $.each(items,function(idx,item) {
                    // alert(msg.tagName);
                    cookie_value1="'"+item.id+"'";
                    cookie_value2="'"+item.word+"'";
                    cookie_value3="'"+item.creator+"'";
                    cookie_value4="'"+new Date(item.createTime.time).format('yyyy-MM-dd hh:mm:ss')+"'";
                    row= '<tr><td colspan="1" height="40" align="center" bgcolor="#ffffff">'+((page-1)*10+idx+1)+'</td><td colspan="2" height="40" align="center" bgcolor="#ffffff">'+item.word+'</td><td colspan="2" height="40" align="center" bgcolor="#ffffff">'+new Date(item.createTime.time).format('yyyy-MM-dd hh:mm:ss')+'</td><td colspan="2" height="40" align="center" bgcolor="#ffffff">'+item.creator+'</td><td colspan="1" height="40" align="center" bgcolor="#ffffff"><button type="button" class="btn btn-danger delStopword" id="'+item.id+'" >删除</button></td></tr>'
                    $('.infor_tab02').append(row);
                });
            }else{
                $('.infor_tab02 tr:not(:first)').html("");
                alert(msg.result);
            }
        },
        error: function(){
            alert("数据请求失败");
        }
    })
}

function initSearchPage(currenPage){
    var listCount = 0;
    if("undefined" == typeof(currenPage) || null == currenPage){
        currenPage = 1;
    }
    var obj1 = $("#stopword_search").val();
    $.ajax({
        type: "post",
        url: "/stopword/selectStopwordCount",
        data:{word:obj1},
        dataType: "json",
        success: function (msg) {
            if (msg.status == "OK") {
                // alert("success");
                listCount = msg.result;
                $("#page").initPage(listCount,currenPage,stopwordInforSearch);
            } else {
                alert(msg.result);
            }
        },
        error: function () {
            alert("数据请求失败");
        }})
    }

// 添加停用词页面跳转
function stopwordInforAdd(){
   baseAjax("stopword_add");
}

// 用户删除
$(function(){
    $(".infor_tab02").on("click",".delStopword",function(){
        var stopword_id = $(this).attr("id");
  //      console.log(stopword_id);
        stopwordInforDel(stopword_id);
        function stopwordInforDel(stopword_id){
            $.ajax({
                type:"post",
                url:"/stopword/deleteStopwordById",
                data:{
                    stopwordId:stopword_id
                } ,
                dataType:"json",
                success:function(msg){
                    // alert("lll");
                    if(msg.status=="OK"){
                        baseAjax("stopword_infor");
                    }else{
                        alert(msg.result);
                    }
                } ,
                error: function(){
                    alert("数据请求失败");
                }
            });
        }
    })
})
