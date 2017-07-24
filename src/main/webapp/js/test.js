/**
 * 
 */
function test(){
    $.ajax({
        url : "/getCurrentUser",
        type : "post",
        data : "",
        success : function(msg) {
            if (msg.status == 'OK') {
                var user = msg.result;
                $(function() {
                    alert(user);
                });
            } else {
                alert("fail");
            }
            //document.write(user);
        },
        error : function() {
            alert("失败");
        },
    });
}