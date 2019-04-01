/**
 * 
 */
addUser();
addMessage();
//向用户表中添加用户信息
function addUser(){
	$.ajax({
		url:'admin/getuser',
		success:function(msg){
			$('#userTable').dataTable().fnDestroy();
			$("#userlist").empty();
			var string="";
			for(var i=0;i<msg.length;i++)
			{
				var status="<span style='color:red'>被封禁</span>";
				var btn="<button onclick='unBannedUser("+msg[i].userId+")' class='btn btn-success btn-xs'>解封</button>";
				if(msg[i].isLocked==false){
					btn="<button onclick='bannedUser("+msg[i].userId+")' class='btn btn-danger btn-xs'>封禁</button>";
					status="<span style='color:green'>正常</span>";
				}
				string+="<tr>"+
						"<td>"+msg[i].userId+"</td>"+
						"<td>"+msg[i].userName+"</td>"+
						"<td>"+msg[i].userEmail+"</td>"+
						"<td>"+msg[i].userMsg+"</td>"+
						"<td>"+msg[i].userStudentId+"</td>"+
						"<td>"+status+"</td>"+             
						"<td>"+btn+"</td>"+
						"</tr>";
			}
			$("#userlist").append(string);
			$('#userTable').dataTable({
	            "bFilter": false,
	            "paging": false,
	            "info": false
	        });
		}
	})
}
//封禁
function bannedUser(uid){
	$.ajax({
		url:'admin/banned',
		type:'post',
		data:{
			userId:uid
		},
		success:function(msg){
			if(msg.status!=1){alert(msg.info);return;}
			addUser();
		}
	})
}
//解封
function unBannedUser(uid){
	$.ajax({
		url:'admin/unbanned',
		type:'post',
		data:{
			userId:uid
		},
		success:function(msg){
			if(msg.status!=1){alert(msg.info);return;}
			addUser();
		}
	})
}

//删除通知
function deleteMessage(messageId){
	var isdelete=confirm("是否删除该通知?");
	if(isdelete){
		$.ajax({
			url:'admin/delete',
			type:'post',
			data:{
				messageId:messageId
			},
			success:function(){
				addMessage();
			}
			
		})
	}
}




//发送系统通知
function addMessage(searchString){
	
	$.ajax({
		url:'admin/getmessage',
		data:{
			keyword:searchString
		}, 
		success:function(msg){
			$('#messageTable').dataTable().fnDestroy();
			$("#messagelist").empty();
			var string="";
			for(var i=0;i<msg.length;i++)
			{
				var status="<span style='color:orange'>已读</span>";
				var btn="<button onclick='deleteMessage("+msg[i].messageId+");' class='btn btn-success btn-xs'>删除</button>";
				if(msg[i].isRead==0){
					status="<span style='color:green'>未读</span>";
				}
				string+="<tr>"+
						"<td>"+msg[i].messageId+"</td>"+
						"<td>"+msg[i].userId+"</td>"+
						"<td>"+msg[i].messageString+"</td>"+
						"<td>"+status+"</td>"+
						"<td>"+msg[i].messageTime+"</td>"+           
						"<td>"+btn+"</td>"+
						"</tr>";
			}
			$("#messagelist").append(string);
			
			$('#messageTable').dataTable({
	            "bFilter": false,
	            "paging": false,
	            "info": false
	        });
		}
	})
}


$("#messagebtn").click(function(){
	var message=$("#message").val();
	$.ajax({
		url:'admin/sendmessage',
		type:'post',
		data:{
			message:message
		},
		success:function(msg){
			alert(msg.info);
			if(msg.status==1){
				addMessage();
			}
		}
	})
})

//通知信息搜索按钮
$("#messageSearchBtn").click(function(){
	var message=$("#messageSearch").val();
	addMessage(message);
})

