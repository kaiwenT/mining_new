/**
 * @author 秦念念
 */
//设置算法和粒度选择
function algorithm_submit(){
    //获取算法选择的值
	var object = document.getElementsByName("algorithm_choose");
	for(var i = 0; i < object.length;i++)
	{
		if(object[i].checked)
		{
			var algorithm = object[i].value;
		}
	}
    //获取粒度选择的值
    var object1 = document.getElementsByName("grain_size_choose");
    for(var i = 0; i < object1.length;i++)
	{
		if(object1[i].checked)
		{
			var granularity = object1[i].value;
		}
	}
    //ajax异步提交请求
//	console.log(algorithm+"**al");
//	console.log(granularity+"**gr");
    $.ajax({
    	type:"POST",
    	url:"/user/setAlgorithmAndGranularity",
    	data:{
    		algorithm:algorithm,
    		granularity:granularity	
    	},
    	dataType:"json",
		
        success : function(msg){
        	if(msg.status=="OK"){
        		alert("修改成功！");
				
        	}else{
   //     		console.log(msg.result);
        		alert(msg.result);
        	}
        },
		error: function(){
			alert("请求失败");
		}
    	
    })
}

//加载完成时执行algorithm_Show()
$(function(){
   algorithm_Show();
})


function algorithm_Show(){
    $.ajax({
        type:"post",
        url:"/user/getAlgorithmAndGranularity",
        dataType:"json",
        success:function(msg){
   //         console.log(msg);
            if(msg.status=="OK"){
				var items = msg.result.user;
				var al = msg.result.algorithm;
				var gr = msg.result.granularity;
				$("input[name='algorithm_choose'][value="+al+"]").attr("checked",true);
				$("input[name='grain_size_choose'][value="+gr+"]").attr("checked",true);
				
            }else{
                alert("显示失败");
            }

        } ,
        error:function(){
            // ���������
        }
    });
}
