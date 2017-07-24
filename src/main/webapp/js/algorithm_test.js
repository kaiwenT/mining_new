/**
 * Created by Jack on 2017/6/2.
 */

//细节展示
function kmeans_detail() {
    $("#algorithm_detail").html("");
    var content = "k值：<input type='text' class='form-control' id='k_value' name='k_value'><br>粒度选择：<input type='radio' name='kmeans_granularity' value='1' checked='checked'> 粗粒度 <input type='radio' name='kmeans_granularity' value='2'> 细粒度";
    $("#algorithm_detail").append(content);
}

function canopy_detail() {
    $("#algorithm_detail").html("");
    var content = "阀值：<input type='text' class='form-control' id='Threshold'> <br>粒度选择：<input type='radio' name='canopy_granularity' value='1' checked='checked'> 粗粒度 <input type='radio' name='canopy_granularity' value='2'> 细粒度 ";
    $("#algorithm_detail").append(content);
}

function dbscan_detail() {
    $("#algorithm_detail").html("");
    var content = "半径：<input type='text' class='form-control' id='radius'>最小数量：<input type='text' class='form-control' id='minNum'><br>粒度选择：<input type='radio' name='dbscan_size' value='1' checked='checked'> 粗粒度 <input type='radio' name='dbscan_size' value='2'> 细粒度 ";
    $("#algorithm_detail").append(content);
}

//选择算法时，显示对应的细节
function algorithmChange(){
    var alg = $("input[name='algorithm']:checked").val();
    if(alg == 1){
        kmeans_detail();
    }else if(alg == 2){
        canopy_detail();
    }else{
        dbscan_detail();
    }

}


//保存按钮点击事件
function conservationClick() {
    var alg = $("input[name='algorithm']:checked").val();
    if(alg == 1){
        kmeans_conservation();
    }else if(alg == 2){
        canopy_conservation();
    }else{
        dbscans_conservation();
    }
}


//聚类按钮点击事件
function clussterClick() {
    var alg = $("input[name='algorithm']:checked").val();
 //   console.log(alg+"**al");
    if(alg == 1){
        kmeans_cluster();
    }else if(alg == 2){
        canopy_cluster();
    }else{
        dbscan_cluster();
    }
}

//聚类结果处理
/**
 * 根据ajax获取的参数items生成hmtl
 * @param items 聚类结果集
 */
function showMining(){
	
	$.ajax({
		type : "post",
		url : "/AlgorithmContainer/getCountResult",
		data : {
		},
		dataType : "json",
		
		success : function(msg) {
			$('.summary_tab table tr:not(:first)').html('');
			if (msg.status == "OK") {
				// alert("删除成功");
				var items = msg.result;

				var indexOfTitle = parseInt(items[0][0]) + 1;
				var indexOfUrl = parseInt(items[0][1]) + 1;
				var indexOfTime = parseInt(items[0][2]) + 1;
				for (var i = 0; i < items.length - 1; i++) {
					// items第一行存储index，故从i+1读起
					item = items[i + 1];
					rows = '<tr</td><td height="32" align="center"><a href="'
							+ item[indexOfUrl]
							+ '" target="_blank">'
							+ item[indexOfTitle]
							+ '</a></td><td height="32" align="center">'
							+ item[indexOfTime]
							+ '</td><td height="32" align="center">'
							+ '<a href="javascript:;" onclick="toPaint('
							+ i
							+ ',\''
							+ item[indexOfTitle]
									.replace(/\"/g, " ").replace(/\'/g,
											" ")
							+ '\')">'
							+ item[0]
							+ '</a>' + '</td></tr>';
					$('.summary_tab table').append(rows);

				}
			} else {
				alert(msg.result);
			}
		},
		
		error : function() {
			alert("请求失败");
		}
	});
}

//ajax请求处理

//kmeans保存设置请求
function kmeans_conservation(){
	 var algorithm = $("input[name='algorithm']:checked").val();
     var granularity = $("#kmeans_granularity:checked").val();
 //    console.log(algorithm+"**k");
//	 console.log(granularity+"**gr");
    //ajax 发送请求
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
	        		alert("设置成功！");
					
	        	}else{
	        //		console.log(msg.result);
	        		alert(msg.result);
	        	}
	        },
			error: function(){
				alert("请求失败");
			}
	    	
	    })
}

//canopy保存设置请求
function canopy_conservation() {
	var algorithm = $("input[name='algorithm']:checked").val();
    var granularity = $("#canopy_granularity:checked").val();
 //   console.log(algorithm+"**k");
//	 console.log(granularity+"**gr");
   //ajax 发送请求
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
	        		alert("设置成功！");
					
	        	}else{
	        	//	console.log(msg.result);
	        		alert(msg.result);
	        	}
	        },
			error: function(){
				alert("请求失败");
			}
	    	
	    })
}
//dbscan保存设置请求
function dbscans_conservation() {
	var algorithm = $("input[name='algorithm']:checked").val();
    var granularity = $("#dbscan_size:checked").val();
//    console.log(algorithm+"**k");
//	 console.log(granularity+"**gr");
   //ajax 发送请求
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
	        		alert("设置成功！");
					
	        	}else{
	        	//	console.log(msg.result);
	        		alert(msg.result);
	        	}
	        },
			error: function(){
				alert("请求失败");
			}
	    	
	    })
}
//kmeans聚类请求
function kmeans_cluster(){
    var k_value = $("#k_value").val();
    var granularity = $("#kmeans_granularity:checked").val();
 //   console.log(k_value+"**k");
//	console.log(granularity+"**gr");
    //ajax 发送请求
    $.ajax({
        type:"POST",
        url:"/AlgorithmContainer/ClusterByKmeans",
        data:{
        	k_value:k_value,
        	granularity:granularity
        },
        dataType:"json",
        beforeSend : function(){
            begin();
        },
        success: function(msg){
            if(msg.status=="OK"){
                var items = msg.result;
                showMining(items);
            }else{
                alert(msg.result);
            }
        },
        complete : function(){
      //      console.log("all")
            stop();
        },
        error: function(){
            alert("请求失败");
        }
    })
}
//canopy聚类请求
function canopy_cluster(){
    var Threshold = $("#Threshold").val();
    var granularity = $("#canopy_granularity:checked").val();
 //   console.log("Threshold:"+Threshold);
 //   console.log("granularity"+granularity);
    //ajax 发送请求
    $.ajax({
        type:"post",
        url:"/AlgorithmContainer/ClusterByCanopy",
        data:{
        	Threshold:Threshold,
        	granularity:granularity

        },
        dataType:"json",
        beforeSend : function(){
            begin();
        },
        success: function(msg){
            if(msg.status=="OK"){
                showMining();
            }else{
                alert(msg.result);
               
            }
        },
        complete : function(){
    //        console.log("all")
            stop();
        },
        error: function(){
            alert("请求失败");
        }
    })
}
//dbscan聚类请求
function dbscan_cluster(){
    var radius = $("#radius").val();
    var minNum = $("#minNum").val();
    var granularity = $("#dbscan_size:checked").val();
 //   console.log("radius:"+radius);
 //   console.log("minNum:"+minNum);
 //   console.log("granularity:"+granularity);
    //ajax 发送请求
    $.ajax({
        type:"post",
        url:"/AlgorithmContainer/ClusterByDBScan",
        data:{
        	Eps:radius,
    		MinPts:minNum,
			granularity:granularity
        },
        dataType:"json",
        beforeSend : function(){
            begin();
        },
        success: function(msg){
            if(msg.status=="OK"){
                showMining();
            }else{
                alert(msg.result);
            }
        },
        complete : function(){
    //        console.log("all")
            stop();
        },
        error: function(){
            alert("请求失败");
        }
    })
}