/**
 * 类簇具体信息js
 */

/**
 * 点击类显示类簇具体信息
 * @param index
 * @param rid
 * @param count
 * @returns
 */
function showClusterDetails(index,rid,count){
	var url = '';
	$("#clusterItemAll").prop("checked",false);
	$.ajax({
		type : "post",
		url : "/result/getClusterResult",
		data : {
			clusterIndex : index,
			resultId : rid
		},
		dataType : "json",
		async: false,//同步
		success : function(msg) {
			if (msg.status == "OK") {
				var items = msg.result;
				var indexOfUrl = parseInt(items[0][1]);// + 1
				if(items == null || items.length == 0){
					alert('没有记录');
					return;
				}else if(items.length == 1){
					url = items[i + 1][indexOfUrl];
					return;
				}
				var indexOfTitle = parseInt(items[0][0]) ;//+ 1				
				var indexOfTime = parseInt(items[0][2]) ;//+ 1
				
				$('.details_tab table tr:not(:first)').remove();
				for (var i = 0; i < items.length - 1; i++) {
					// items第一行存储index，故从i+1读起
					item = items[i + 1];
					url = item[indexOfUrl];
					rows = '<tr><td height="32" align="center"><input type="checkbox" id="itemCheckbox" style="width:20px;height:20px" class="'
						+ i
						+ '"/></td><td height="32" align="center"><a href="'
						+ item[indexOfUrl]
						+ '" target="blank">'
						+ item[indexOfTitle]
						+ '</a></td><td height="32" align="center">'
						+ item[indexOfTime]
						+ '</td><td height="32" align="center">'
						+ '<button class="btn btn-danger delItem" type="button" id="'
						+ i
						+'" >删除</button></td></tr>';
				$('.details_tab table').append(rows);
				//将类的id作为table的id
				$('.details_tab table').attr('id',index);
				}
			}else{
				alert(msg.result);				
			}
		},
		error : function(msg) {
			 $('#code').hide();
		     $('#goodcover').hide();
		     freshData();
//			alert(msg.result);
		}
	});
	//类中只有一个元素直接打开url
	if(url != '' && count == 1){
		window.open(url);
	}else if(url == ''){
		 $('#code').hide();
	     $('#goodcover').hide();
	     freshData();
	}else{
		 $('#code').center();
	     $('#goodcover').show();
	     $('#code').fadeIn();
	}
}
//全选类中所有元素
$(function() {
	$("#clusterItemAll").click(function() {
//		$("#itemCheckbox").each(function(){
//			 $(this).prop("checked",!!$(".checkAll").prop("checked"));
//		});
		if (this.checked) {
			$(".details_tab tr :checkbox").prop("checked", true);
		} else {
			$(".details_tab tr :checkbox").prop("checked", false);
		}
	});
});

/* 重置某个类簇元素*/
function clusterItemsReset() {
	var index = $('.details_tab table').attr('id');
	$.ajax({
		type : "post",
		url : "/result/resetClusterItems",
		data : JSON.stringify(index),
		dataType : "json",
		contentType : "application/json",
		success : function(msg) {
			if (msg.status == "OK") {
				showClusterDetails(index,null, 2);
			} else {
				alert(msg.result);
			}
		},
		error : function(msg) {
			alert(msg.result);
		}
	});
}


//删除类中单个元素
$(function(){
	$(".details_tab table").on("click",".delItem",function(){
		var index = $(this).attr("id");

		if(index == null || index < 0){
			alert('删除出现错误');
		}else{
			deleteClusterItem(index);
		}
		
	})
})

/* 删除 类中某些元素 
 * index --点击一个标题后边的删除按钮时的下标
 * 若index为空则取多选框选中的元素
 * */
function deleteClusterItem(index) {
	var sets = new Array();	
	var clusterIndex = $('.details_tab table').attr('id');
	var sum = 0 ;
	$(".details_tab input").each(function(i) {
		sum++;
	});
	if(index == null || index == -1){
		$(".details_tab input:checked").each(function(i) {
			sets.push($(this).attr('class'));
		});
	}else{
		sets.push(index);
	}
	
	$.ajax({
		type : "post",
		traditional : true,
		url : "/result/deleteClusterItems",
		data : {
			clusterIndex: clusterIndex,
			ItemIdSets: sets,
		},
		dataType : "json",
//		async: false,//同步
		success : function(msg) {
			if (msg.status == "OK") {
				var rid = $('.summary_up table tr button').attr("id");
				if(sets.length == sum){
					$('#code').hide();
				    $('#goodcover').hide();
				    freshData();
				}else{
					showClusterDetails(clusterIndex,rid,2);
				}				
			} else {
				//alert(msg.result);
				 $('#code').hide();
			     $('#goodcover').hide();
			     freshData();
			}
		},
		error : function(msg) {
//			alert(msg.result);
			 $('#code').hide();
		     $('#goodcover').hide();
		     freshData();
		}
	});
}

//弹出框的样式
$(function() {
    // alert($(window).height());
    $('#closebt').click(function() {
        $('#code').hide();
        $('#goodcover').hide();
        freshData();
    });
	$('#goodcover').click(function() {
        $('#code').hide();
        $('#goodcover').hide();
        freshData();
    });
    /*
	 * var val=$(window).height(); var codeheight=$("#code").height(); var
	 * topheight=(val-codeheight)/2; $('#code').css('top',topheight);
	 */
    jQuery.fn.center = function(loaded) {
        var obj = this;
        body_width = parseInt($(window).width());
        body_height = parseInt($(window).height());
        block_width = parseInt(obj.width());
        block_height = parseInt(obj.height());

        left_position = parseInt((body_width / 2) - (block_width / 2) + $(window).scrollLeft());
        if (body_width < block_width) {
            left_position = 0 + $(window).scrollLeft();
        };

        top_position = parseInt((body_height / 2) - (block_height / 2));// +
																		// $(window).scrollTop());
        if (body_height < block_height) {
            top_position = 0 + $(window).scrollTop();
        };

        if (!loaded) {

            obj.css({
                'position': 'fixed'
            });
            obj.css({
                'top': ($(window).height() - $('#code').height()) * 0.5,
                'left': left_position
            });
            $(window).bind('resize', function() {
                obj.center(!loaded);
            });
            $(window).bind('scroll', function() {
                obj.center(!loaded);
            });

        } else {
            obj.stop();
            obj.css({
                'position': 'fixed'
            });
            obj.animate({
                'top': top_position
            }, 200, 'linear');
        }
    }

})