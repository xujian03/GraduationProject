/**
 * 登录时的js文件
 */

//获取登录成功后要跳转的页面
var returnUrl=getParam("returnUrl");
//console.log(returnUrl);

//更换验证码的函数
function changeImg() {
    $("#img_yanzhengma").attr("src","user/imglogin?rnd=" + Math.random());
}

//提交按钮的点击事件
$("#submit1").click(function () {
    var name=$("#emailOrStudentId").val();//登录名
    var password=$("#password").val();//密码
    var yanzhengma=$("#yanzhengma").val();//验证码
    var dangerMsg=" 警告：";
    var isUserful=true;
    if(name==""||name==null){
        dangerMsg=dangerMsg+"学号邮箱栏不能为空  ";
        isUserful=false;
    }
    if(password==""||password==null){
        dangerMsg=dangerMsg+"密码栏不能为空  ";
        isUserful=false;
    }
    if(yanzhengma==""||yanzhengma==null){
        dangerMsg=dangerMsg+"验证码不能为空";
        isUserful=false;
    }
    if(isUserful==false){
        $("#dangermsg").removeClass("hidden").text(dangerMsg);
        return;
    }
    $.ajax({
        type:"post",
        data:{
            password:password,
            email: name,
            code:yanzhengma
        },
        url:'user/login',
        success:function (msg) {
           if(msg.status==1){
               alert("登录成功！");
               if(returnUrl!=""&&returnUrl!=null){
                   window.location.href=returnUrl;
               }else{
                   window.location.href="/bs"
               }

           }else{
               alert(msg.info);
               changeImg();
               $("#dangermsg").addClass("hidden");
                $("#yanzhengma").val("");
           }
        }
    })
});

//回车点击事件
$(document).keydown(function(event){
	if(event.keyCode==13){
	$("#submit1").click();
} })
getsuggest();
function getsuggest() {
    $.ajax({
        url:'goods/suggest',
        success:function (msg) {
            var string="";
            $("#goodsMsg").empty();
            for(var i=0;i<msg.total;i++){
                string+="<div class=\"col-md-4\" style=\"margin-bottom:10px \">" +
                    "<div  style=\"border: 1px solid grey;background-color:white\" class=\"text-center\">" +
                    "<img src=\""+msg.obj[i]+" \" class=\"img-responsive\" alt=\"商品推荐\">" +
                    "<div style=\"padding: 5px\">" +
                    "<span style=\"color:rgb(255,0,0);\">"+msg.list[i].goodsPrice+"￥</span>" +
                    "<span>"+msg.list[i].goodsName+"</span><br>" +
                    "<a href=\"/bs/goods/"+msg.list[i].goodsId+"\" target=\"_blank\">前往购买</a>" +
                    "</div>" +
                    "</div>" +
                    "</div>";
            }
            $("#goodsMsg").append(string);
        }
    })
}



/**
 * 获取指定的URL参数值
 * URL:http://www.quwan.com/index?name=tyler
 * 参数：paramName URL参数
 * 调用方法:getParam("name")
 * 返回值:tyler
 */
function getParam(paramName) {
    paramValue = "", isFound = !1;
    if (this.location.search.indexOf("?") == 0 && this.location.search.indexOf("=") > 1) {
        arrSource = unescape(this.location.search).substring(1, this.location.search.length).split("&"), i = 0;
        while (i < arrSource.length && !isFound) arrSource[i].indexOf("=") > 0 && arrSource[i].split("=")[0].toLowerCase() == paramName.toLowerCase() && (paramValue = arrSource[i].split("=")[1], isFound = !0), i++
    }
    return paramValue == "" && (paramValue = null), paramValue
}