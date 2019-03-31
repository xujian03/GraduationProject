/**
 * 求购帖子js文件
 */
var toolbar = [ 'title', 'bold', 'italic', 'underline', 'strikethrough','color', '|', 'ol', 'ul', 'blockquote', '|','link', 'hr', '|', 'indent', 'outdent' ];
var editor = new Simditor( {
	textarea : $('#editor'),
	placeholder : '这里输入回复内容...',
	toolbarFloat: true,
	toolbarFloatOffset:0,
	imageButton:['upload'],
	pasteImage : false,
	toolbar : toolbar,  //工具栏
});
var page=1;
var MAXINDEX;
$(function(){


		
		$("#btn1").click(function(){
			//editor.setValue("<u>hello</u>");
			alert(editor.getValue());
		});

})


/**
 * 点击回复按钮弹出回复模态框
 * @param buyingId 帖子id
 * @param isflowcomment 是否为回复别人的回复
 * @param flowCommentId 如果是，那么帖子id
 * @param isHaveReplyUser 如果是，那么是否有回复用户
 * @param flowUserId  如果是 回复的用户id
 */
function commentModel(buyingId,isflowcomment,flowCommentId,isHaveReplyUser,flowUserId) {
	if(TUSER==null){alert("请先登录！");$("#loginModal").modal("show");return;}
    $("#commentModal").modal("show");
    $("#replyBtn").unbind('click');
    $("#replyBtn").click({buyingId:buyingId,isflowcomment:isflowcomment,flowCommentId:flowCommentId,isHaveReplyUser:isHaveReplyUser,flowUserId:flowUserId},function (event) {
		var msg=editor.getValue();
		if (msg==""||msg.length>=1000){
			alert("回复长度不得超过1000");
			return;
		}
		$.ajax({
			type:'post',
			url:'/bs/buying/postcomment',
			data:{
				buyingId:event.data.buyingId,
				buyingMsg:msg,
                isDoubleComment:event.data.isflowcomment,
				isHaveReplyUser:event.data.isHaveReplyUser,
				flowCommentId:event.data.flowCommentId,
				flowUserId:event.data.flowUserId
			},
			success:function (msg) {
				if(msg.status==1){
					editor.setValue("");
					$("#commentModal").modal("hide");
					getComment();
					return;
				}else {
					alert(msg.info);
				}
			}
		})
	})

}
getComment();
function getComment() {
	$.ajax({
		url:"getcomment",
		data:{
			buyingId:buyingId,
			page:page
		},
		success:function (msg) {
			if(msg!=null){
				//console.log(msg);
				var strings="";
				for(let i=0;i<msg.length;i++){
					var c="";
					for(let j=0;j<msg[i].list.length;j++)
					{
						if(j==0){c+="<div class=\"well\" style=\"overflow:auto;min-height:50px;max-height: 150px\">";}
						var a="";
						//console.log(msg[i].list[j]);
						if(msg[i].list[j].isHaveReplyUser==true)a="<a href='javascript:void(0);'>@"+msg[i].list[j].flowUserName+"</a>";
						c+="<div class='row' style='border-bottom: 1px solid #C8C8C8'><div class='' style='float: left;max-width: 200px;margin-left: 14px'><a href='javascript:void(0);'>"+ msg[i].list[j].userName+"</a> 回复："+a+"</div>"+"<div class=''  style='float: left;width: 300px;'>"+ msg[i].list[j].buyingCommentMsg+"</div><div class=''  style='float: right;margin-right: 30px'>"+"<a href='javascript:void(0);' onclick='commentModel(buyingId,true,"+msg[i].obj.buyingCommentId+",true,"+msg[i].list[j].userId+")'>回复</a></div></div><br>"
						if(j+1==msg[i].list.length){c+="</div>";}
					}
					strings+="<div style=\"width: 100%;background-color:#fff;border-bottom: 1px solid DarkGray;\">" +
						"<div class=\"row\">" +
						"<div class=\"col-md-2 text-center\" style=\"min-height: 150px;\">" +
						"<div style=\"position:absolute;left:30%;top:20%;\">"+
						"<img src=\""+msg[i].info+"\" style=\"width:80px;height:80px;\" class=\"img-circle\" alt=\"\"><br>" +
						"<span><a href='javascript:void(0);'>"+msg[i].obj.userName+"</a></span>" +
						"</div>"+
						"</div>" +
						"<div class=\"col-md-10\"  style=\"min-height: 150px;\">" +
						"<div style=\"height: 150px;width: 100%;padding: 10px 20px\">" +
						"<span>" +
						msg[i].obj.buyingCommentMsg+
						"</span>" +
                        "</div>" +
                        "<div class='row' style='margin-bottom: 15px'>"+
						"<div class='col-md-4 col-md-offset-8'>" +
						"<span style=\"margin-right: 20px\">" +
						""+msg[i].obj.buyingCommentTime+
						"</span>" +
						"<a href='javascript:void(0);' onclick='commentModel(buyingId,true,"+msg[i].obj.buyingCommentId+",null,0)'>回复</a>" +
						"</div>" +
                        "</div>" +

                        c+
						"</div>" +
						"</div>" +
						"</div>";
					MAXINDEX=msg[i].total;
				}
				$("comment").empty();
				$("comment").append(strings);
				$("#uppage").unbind();
				$("#uppage").click(function () {
					if(page>1){
						page--;
						getComment();
					}
				});
				$("#downpage").unbind();
				$("#downpage").click(function () {
					if(page<MAXINDEX){
						page++;
						getComment();
					}
				});
				var option="";
				for(var i=1;i<=MAXINDEX;i++){
					var str2="";
					if(page==i){
						str2="selected = \"selected\"";
					}
					option+="<option value=\""+i+"\" "+str2+">"+i+"页</option>";
				}
				$("#indexpage").empty();
				$("#indexpage").append(option);
				$("#indexpage").unbind();
				$("#indexpage").change(function () {
					var index=$("#indexpage").val();
					page=index;
					getComment();
				});
			}
		}
	})
}
//求购已完成按钮
function buyingOver(id){
	var isOver=confirm("是否关闭该求购？");
	if(isOver==true){
		$.ajax({
			type:"post",
			url:'/bs/buying/over',
			data:{
				bid:id
			},
			success:function(msg){
				alert(msg.info);
				window.location.reload();
			}
		});
	}
	
}

