// JavaScript Document
//结果汇总
/*$(function() {
    $('.data_xq_tit li').mouseover(
            function(event) {
                $(this).addClass('stop').siblings().removeClass('stop');
                // 和事件源索引号相同的div显示 -- 事件源的索引号 == $(this).index()
                $('.data_xq_cont').eq($(this).index()).addClass('stop_zs')
                        .siblings().removeClass('stop_zs');
            });
});*/
var title = "";
function paint() {
    var currentSet = getCookie('currentSet');
    title = getCookie('title');
    var json = {
        interval : 2,
        currentSet : currentSet
    };
    $.ajax({
        type : "post",
        url : "/standardResult/statisticSingleSet",
        data : JSON.stringify(json),
        dataType : "json",
        contentType : "application/json",
        success : function(msg) {
            parseTime(msg.result.time);
            parseAmount(msg.result.count);
        },
        error : function() {
            alert("请求失败");
        },

    });
}
paint();

function mouseover(th){	
    $(th).addClass('stop').siblings().removeClass('stop');
    // 和事件源索引号相同的div显示 -- 事件源的索引号 == $(this).index()
    $('.data_xq_cont').eq($(th).index()).addClass('stop_zs')
            .siblings().removeClass('stop_zs');
}
function parseTime(json) {
    var netAttenHtml = '';
    var typeHtml = '';
    var mediaAttenHtml = '';
    var mediaHtml = '';
    for ( var time in json) {
        var netAtten = json[time].netizenAttention;
        netAttenHtml += parse_type(time, netAtten);
        var infoType = json[time].infoType;
        typeHtml += parse_type(time, infoType);
        var mediaAtten = json[time].mediaAttention;
        mediaAttenHtml += parse_media(time, mediaAtten);
        var media = json[time].media;
        mediaHtml += parse_media(time, media);
    }
    $('.media_count').append(mediaHtml);
    $('.media_atten').append(mediaAttenHtml);
    $('.info_count').append(typeHtml);
    $('.info_atten').append(netAttenHtml);
    var icvalue = getTableData(document.getElementById('info_count'));
    paintzx('line_chart01', icvalue.xAxis, icvalue.legend, icvalue.series);
    var iavalue = getTableData(document.getElementById('info_atten'));
    paintzx('line_chart02', iavalue.xAxis, iavalue.legend, iavalue.series);
    var mcvalue = getTableData(document.getElementById('media_count'));
    paintzx('line_chart03', mcvalue.xAxis, mcvalue.legend, mcvalue.series);
    var mavalue = getTableData(document.getElementById('media_atten'));
    paintzx('line_chart04', mavalue.xAxis, mavalue.legend, mavalue.series);
}

function parse_type(time, json) {
    var row = '<tr><td>' + time + '</td><td>' + convertData(json['论坛'])
            + '</td><td>' + convertData(json['新闻']) + '</td><td>'
            + convertData(json['博客']) + '</td><td>' + convertData(json['报纸'])
            + '</td><td>' + convertData(json['微信']) + '</td><td>'
            + convertData(json['贴吧']) + '</td><td>' + convertData(json['问答'])
            + '</td><td>' + convertData(json['手机']) + '</td><td>'
            + convertData(json['视频']) + '</td><td>' + convertData(json['微博'])
            + '</td><td>' + convertData(json['其他']) + '</td></tr>';
    return row;
}

function parse_media(time, json) {
    var row = '<tr><td>' + time + '</td><td>' + convertData(json['中央'])
            + '</td><td>' + convertData(json['省级']) + '</td><td>'
            + convertData(json['其他']) + '</td><td>' + convertData(json['未知'])
            + '</td></tr>';
    return row;
}

function parseAmount(json) {
    var typeHtml = '';
    var levelHtml = '';
    var typeJson = json.type;
    var levelJson = json.level;
    typeHtml = '<tr><td>' + convertData(typeJson['论坛']) + '</td><td>'
            + convertData(typeJson['新闻']) + '</td><td>'
            + convertData(typeJson['博客']) + '</td><td>'
            + convertData(typeJson['报纸']) + '</td><td>'
            + convertData(typeJson['微信']) + '</td><td>'
            + convertData(typeJson['贴吧']) + '</td><td>'
            + convertData(typeJson['问答']) + '</td><td>'
            + convertData(typeJson['手机']) + '</td><td>'
            + convertData(typeJson['视频']) + '</td><td>'
            + convertData(typeJson['微博']) + '</td><td>'
            + convertData(typeJson['其他']) + '</td></tr>';
    levelHtml = '<tr><td>' + convertData(levelJson['中央']) + '</td><td>'
            + convertData(levelJson['省级']) + '</td><td>'
            + convertData(levelJson['其他']) + '</td><td>'
            + convertData(levelJson['未知']) + '</td></tr>';
    $('.info_amount').append(typeHtml);
    $('.media_amount').append(levelHtml);

    var iavalue = getPieData(typeJson);
    var lavalue = getPieData(levelJson);
    paintbt('pie_chart01', iavalue.legend, iavalue.series);
    paintbt('pie_chart02', lavalue.legend, lavalue.series);
}
function convertData(json) {
    if (json === undefined) {
        return 0;
    } else {
        return json;
    }
}

function getTableData(table) {
    var xAxis = new Array();
    var legend = new Array();
    var series = new Array();
    for (var j = 0; j < table.rows[0].cells.length; j++) {
        var json = {
            name : "",
            type : "bar",
            data : [],
            label : {
                normal : {
                    show : true,
                    position : 'top',
                    textStyle : {
                        color : '#000000',
                        fontSize : 14,
                    }
                }
            },
        };
        for (var i = 0; i < table.rows.length; i++) {
            if (j == 0 && i == 0) {
                continue;
            } else if (j == 0 && i > 0) {
                xAxis.push(table.rows[i].cells[j].innerText);
            } else {
                if (i == 0) {
                    json.name = table.rows[i].cells[j].innerText;
                    legend.push(json.name);
                } else {
                    json.data.push(table.rows[i].cells[j].innerText);
                }
            }
        }
        if (j == 0) {
            continue;
        }
        series.push(json);
    }
    var value = {
        xAxis : xAxis,
        legend : legend,
        series : series
    };
    return value;
}

function getPieData(data) {
    var legend = new Array();
    var series = new Array();
    for ( var ele in data) {
        var json = {
            value : data[ele],
            name : ele,
            label : {
                normal : {
                    show : true,
                    position : 'top',
                    
                }
            }
        }
        series.push(json);
        legend.push(ele);
    }
    var value = {
        legend : legend,
        series : series
    }
    return value;
}

function paintzx(id, xAxis, legend, series) {
    var myChart = echarts.init(document.getElementById(id));
    // 指定图表的配置项和数据
    var option = {
        toolbox : { // 可视化的工具箱
            show : true,
            orient : 'vertical',
            top : 'middle',
            feature : {
                dataView : { // 数据视图
                    show : true
                },
                restore : { // 重置
                    show : true
                },
                dataZoom : { // 数据缩放视图
                    show : true
                },
                saveAsImage : {// 保存图片
                    show : true
                },
                magicType : {// 动态类型切换
                    type : [ 'bar', 'line' ]
                }
            }
        },
        tooltip : { // 弹窗组件
            show : true
        },
        legend : {
            data : legend,
        },
        xAxis : {
            data : xAxis
        },
        yAxis : {},
        series : series
    };
    myChart.setOption(option);
}

function paintbt(id, legend, series) {
    var myChart = echarts.init(document.getElementById(id));
    var option = {
        title : {
            text : title,
            x : 'center'
        },
        tooltip : {
            trigger : 'item',
            formatter : "{a} <br/>{b} : {c} ({d}%)"
        },
        legend : {
            orient : 'vertical',
            left : 'left',
            data : legend
        },
        series : [ {
            type : 'pie',
            radius : '55%',
            center : [ '50%', '60%' ],
            data : series,
            itemStyle : {
                emphasis : {
                    shadowBlur : 10,
                    shadowOffsetX : 0,
                    shadowColor : 'rgba(0, 0, 0, 0.5)'
                }
            }
        } ]
    };
    myChart.setOption(option);
}
