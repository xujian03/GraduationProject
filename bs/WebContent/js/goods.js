/**
 * 商品展示页面js文件
 * @type {number}
 */
var cishu=0;
var times=($("#insideSpan").width()-$("#outDiv").width())/55;

console.log($("#insideSpan").width());
console.log($("#outDiv").width());
var user=null;
$.ajax({
    type:'get',
    url:'/bs/user/getuser',
    success:function (msg) {
        user=msg;
        if(user!=null){
            if(user.userId!=goods.userId){//该用户发布的商品将加入购物车按钮去除
                $("#addBtn").show();
            }
        }else{
            $("#addBtn").show();
        }
    }
});

//加入购物车按钮添加点击事件
$("#addBtn").click(function () {
    $.ajax({
        type:'post',
        url:'/bs/goods/shoppingcartadd',
        data:{
            goodsId:goods.goodsId
        },
        success:function (msg) {
            if(msg.status==1){
                getShoppingCart();
            }else if(msg.status==0){
                alert("当前状态未登录，请先登陆。");
                $("#loginModal").modal("show");
            }else if(msg.status==-1){
                alert(msg.info);
            }

        }
    })
})

getShoppingCart();
var scroll_offset;

$(function(){

    setTimeout(function () {
        times=($("#insideSpan").width()-$("#outDiv").width())/55;
        console.log($("#insideSpan").width());
        console.log($("#outDiv").width());
        if(times-parseInt(times)>0.7)times++;
    },5000);
    if(times-parseInt(times)>0.7)times++;
	$("#goright").click(function () {
	    cishu++;
	    if(cishu<=times){
	        var size=55*cishu;
	        $("#insideSpan").animate({right:size+'px'});
	    }else {
	        cishu--;
	    }
	});
	$("#goleft").click(function () {
	    cishu--;
	    if(cishu>=0){
	        var size=55*cishu;
	        $("#insideSpan").animate({right:size+'px'});
	    }else{
	        cishu++;
	    }
	});
    
	//点击小图片，显示表情
	$(".bq").click(function(e){
		$(".face").slideDown();//慢慢向下展开
		e.stopPropagation();   //阻止冒泡事件
	});

	//在桌面任意地方点击，他是关闭
	$(document).click(function(){
		$(".face").slideUp();//慢慢向上收
	});

	//点击小图标时，添加功能
	$(".face ul li").click(function(){
		var simg=$(this).find("img").clone();
		$(".message").append(simg);
	});

	//点击发表按扭，发表内容
	$("span.submit").click(function(){

		var txt=$(".message").html();
		if(txt==""){
			$('.message').focus();//自动获取焦点
			return;
		}
		$(".message").empty();
		if(txt.length>=800){
		    alert("评论字数过多！");
		    return;
        }
		txt=txt.replace(/"/g, "'");
		if(USERID==0){
            $.ajax({
                url:'postcomment',
                type:'post',
                data:{
                    goodsCommentMsg:txt,
                    goodsId:goods.goodsId
                },
                success:function (msg) {
                    if(msg.status==1){
                        alert(msg.info);
                        INDEX=1;
                        indexComment();
                        USERID=0;
                        return;
                    }else if(msg.status==0){
                        var isTiaoZhuan=confirm("当前状态未登录，是否跳转到登录页面？");
                        if(isTiaoZhuan==true){
                            window.location="/bs/login.html?returnUrl="+GetUrlRelativePath();
                        }
                    }else{
                        alert(msg.info);
                    }

                }
            })
        }else {
            $.ajax({
                url:'postcomment',
                type:'post',
                data:{
                    goodsCommentMsg:txt,
                    goodsId:goods.goodsId,
                    goodsCommentAimUser:USERNAME,
                    goodsCommentAimUserId:USERID
                },
                success:function (msg) {
                    if(msg.status==1){
                        alert(msg.info);
                        INDEX=1;
                        indexComment();
                        USERID=0;
                        $("#touser").hide(500);
                        $("#tousermsg").val("");

                        return;
                    }else if(msg.status==0){
                        var isTiaoZhuan=confirm("当前状态未登录，是否跳转到登录页面？");
                        if(isTiaoZhuan==true){
                            window.location="/bs/login.html?returnUrl="+GetUrlRelativePath();
                        }
                    }else{
                        alert(msg.info);
                    }

                }
            })
        }


	});
    //滚动屏幕
    
//    $(window).scroll(function() {
//        var scrolltop = $(this).scrollTop(); //获取当前页面顶部的坐标
//        console.log(scrolltop+"-"+scroll_offset.top);
//    })

})

/**
 * 改变购物车上的数字和加入购物车按钮的文字
 */
function getShoppingCart(){
    $.ajax({
        type:'get',
        url:'/bs/goods/shoppingcartget',
        success:function (msg) {
            if(msg!=""&&msg!=null){
                $("#gouwuche").text(msg.length);
                    for(var i=0;i<msg.length;i++){
                        if(goods.goodsId==msg[i].goodsId){
                            $("#addBtn").attr("disabled",true);
                            $("#addBtnMsg").text("已在购物车中");
                        }
                    }
            }else{
                $("#gouwuche").text(0);
            }
        }
    })
}
//获取当前页面url
function GetUrlRelativePath()
{
    var url = document.location.toString();
    var arrUrl = url.split("//");

    var start = arrUrl[1].indexOf("/");
    var relUrl = arrUrl[1].substring(start);//stop省略，截取从start开始到结尾的所有字符

    if(relUrl.indexOf("?") != -1){
        relUrl = relUrl.split("?")[0];
    }
    return relUrl;
}
var INDEX=1;
var TOTAL;
var USERNAME;
var USERID=0;
indexComment();//获取评论


function indexComment() {
    var string="";
    $.ajax({
        url:"/bs/goods/getcomment",
        data:{
            goodsId:goods.goodsId,
            page:INDEX
        },
        success:function (msg) {
            if(msg!=null){
                var option="";
                TOTAL=msg.total;
                for(var i=0;i<msg.status;i++){
                    var str1="";
                    if(msg.list[i].goodsCommentAimUserId!=0){str1="<a href=\"javascript:void(0);\" onclick=\"\">@"+msg.list[i].goodsCommentAimUser+"</a>"}
                    string+="<div class=\"row\" style=\"border-bottom:0.5px dashed Thistle;margin-top:6px\">" +
                        "<div class=\"col-xs-2 \">" +
                        "<div class=\"text-center pull-right\">" +
                        "<div>" +
                        "<img alt=\"头像\" class=\"img-circle\" style=\"width:40px;\"  src='"+msg.obj[i]+"'>" +
                        "<br><span>"+msg.list[i].goodsCommentUserName+"</span>" +
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "<div class=\"col-xs-10\">" +
                        "<div style=\"min-height:45px;padding-top:5px\">" +
                        "："+str1+msg.list[i].goodsCommentMsg+
                        "<br>" +
                        "</div>" +
                        "<small style=\"float:right\">评论时间："+msg.list[i].goodsCommentTime+"<a href=\"javascript:void(0);\" onclick=\"scrollPinglun('"+msg.list[i].goodsCommentUserName+"',"+msg.list[i].goodsCommentUserId+")\">&nbsp;回复</a></small>" +
                        "</div>" +
                        "</div>";

                }
                $("#goodsComment").empty();
                $("#goodsComment").append(string);
                for(var i=1;i<=msg.total;i++){
                    var str2="";
                    if(INDEX==i){
                        str2="selected = \"selected\"";
                    }
                    option+="<option value=\""+i+"\" "+str2+">"+i+"页</option>";
                }
                $("#indexpage").empty();
                $("#indexpage").append(option);
                $("#uppage").unbind();
                $("#uppage").click(function () {
                    if(INDEX>1){
                        INDEX--;
                        indexComment();
                    }
                });
                $("#downpage").unbind();
                $("#downpage").click(function () {
                    if(INDEX<TOTAL){
                        INDEX++;
                        indexComment();
                    }
                });

                $("#indexpage").unbind();
                $("#indexpage").change(function () {
                    var index=$("#indexpage").val();
                    INDEX=index;
                    indexComment();
                });


            }


        }
    })
}

function scrollPinglun(userName,userId) {
	scroll_offset=$("#fabiao").offset(); //site为目标位置的ID
    $("html,body").animate({scrollTop:scroll_offset.top-500},500);
    $("#touser").show();
    $("#tousermsg").val("@"+userName);
    USERNAME=userName;
    USERID=userId;
    $('.message').focus();
}

$("#tousermsg").keydown(function () {
    $("#tousermsg").val("");
    USERID=0;
    USERNAME="";
    $("#touser").hide(500);
})