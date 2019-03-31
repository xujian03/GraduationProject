/**
 * 个人中心的消息处理js
 */
messageTab(false);


function messageTab(ishaveRead){
	$.ajax({
		url:'/bs/message/getmessage',
		data:{
			ishaveRead:ishaveRead
		},
		success:function(msg){
		    var string="";
			for(var i=0;i<msg.length;i++)
	        {
				var tongzhilevel="<span style=\"color:red;margin-left:20px\">系统通知：</span>";
				if(msg[i].messageLevel==2){
					tongzhilevel="<span style=\"color:green;margin-left:20px\">普通通知：</span>";
				}
				var btnmsg="";
				if(!msg[i].isRead){
					btnmsg="<button class=\"btn btn-success btn-xs\" style=\"float:right;margin-right:20px\" onclick='haveRead("+msg[i].messageId+");'>标记为已读</button>"
				}
	            string+="<a target='_blank'  style='text-decoration:none;' href=\""+msg[i].MessageUrl+"\">"+tongzhilevel+msg[i].messageString+"  <span style='color:#aaa;font-size:10px'>时间："+msg[i].messageTime+"</span></a><br>"+btnmsg+"</br></br>";
	        }
			$("messages").empty();
			$("messages").append(string);
		}
		
	})
}

$("input[type=checkbox][name='isHaveRead']").change(function () {
	//if($("input[type=radio][name='isHaveRead']").val())
	messageTab($("input[type=checkbox][name='isHaveRead']").prop('checked'));
})


//标记为已读
function haveRead(messageId){
	var isRead=confirm("是否将该条信息标记为已读？");
	if(isRead){
		$.ajax({
			url:'/bs/message/read',
			data:{
				messageId:messageId
			},
			type:'post',
			success:function(msg){
				messageTab($("input[type=checkbox][name='isHaveRead']").prop('checked'));
			} 
				
		})
	}
}
