var TUSER=null;
$.ajax({
    url:'/bs/user/getuser',
    success:function (msg) {
        if(msg==null){
            haveNoUser();
        }else{
            TUSER=msg;
            haveUser(msg);
        }
    }
});

$("body").css("padding-top",$("nav").height());
//有用户时的导航栏
function haveUser(user) {
    let string="<li>" +
        "<a href=\"\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">"+user.userName+" <span class=\"caret\"></span></a>" +
        "<ul class=\"dropdown-menu\">" +
        "<li class='text-center'>余额："+user.userMoney+" 元</li>" +
        "<li class='text-center'><a href=\"/bs/user.html\">用户中心</a></li>" +
        "<li class='text-center'><a href=\"/bs/user.html?tabid=A_dingdan\">我的订单</a></li>" +
        "<li class='text-center'><a href=\"/bs/user.html?tabid=A_shangping\">我的商品</a></li>" +
        "<li class='text-center'><a href=\"/bs/cart.html\">购物车</a></li>" +
        "<li class='text-center'><a href=\"/bs/user.html\">我的消息&nbsp;<span class=\"badge\" style='background-color:red;'>5</span></a></li>" +
        "</ul>" +
        "</li>" +
        "<li><a href=\"/bs/user/loginout\">登出</a></li>";
    $("#loginoruser").append(string);
}
//没有用户时的导航栏
function haveNoUser() {
    let string="<li><a href=\"\" data-toggle=\"modal\" data-target=\"#loginModal\" >登录</a></li>" +
        "<li><a href=\"/bs/regist.html\">注册</a></li>";
    $("#loginoruser").append(string);
}


var isCouldLogin=false;//验证码是否填写正确
jigsaw.init(document.getElementById('captcha'), function () {
    isCouldLogin=true;
    $("#loginbtn").attr("disabled",false);
});




$("#loginbtn").click(function () {
    if(isCouldLogin==false){
        alert("图片验证码验证错误！");
        return;
    }
    var email=$("#login_name").val();
    var password=$("#login_password").val();
    $.ajax({
        url:"/bs/user/modalogin",
        data:{
            email:email,
            password:password
        },
        type:'post',
        success:function (msg) {
            if(msg.status==1){
               // alert(msg.info);
                location.reload();
                return;
            }else{
                alert(msg.info);
                $(".refreshIcon").click();
                isCouldLogin=false;
                $("#loginbtn").attr("disabled",true);
            }
        }
    })

});

$(".refreshIcon").click(function(){
	isCouldLogin=false;
	$("#loginbtn").attr("disabled",true);
});
