/**
 * 注册时的js
 */

//获取select中的学院
$.ajax({
	url:'user/getcollege',
	success:function(msg){
		$("#xueyuan").append(msg);
	}
})
getGrade();
//学院改变后重新获取班级
$("#xueyuan").change(function(){
    getGrade();
})
//根据学院获取班级
function getGrade() {
    var xueyuan=$("#xueyuan").val();
    console.log(xueyuan)
    $("#banji").empty();
    if(xueyuan!="")
    {
        $.ajax({
            url:'user/getgrade',
            data:{
                college:xueyuan,
            },
            success:function(msg){
                $("#banji").attr("disabled",false);
                if(msg==""){
                    $("#banji").append("<option value=>没有选项</option>");
                }else{
                    $("#banji").append(msg);
                }
            }
        })
    }else{
        $("#banji").attr("disabled",true);
    }
}
/**
 * 去两边空格
 * @param s
 * @returns {void | string | *}
 */

function trim(s){
    return s.replace(/(^\s*)|(\s*$)/g, "");
}
// $("#emailLableFalse").removeClass("hidden");
$("#studentId").keyup(function () {    //学生学号
    var text=trim($("#studentId").val());
    if(text.length==0||text.length>10){
        $("#studentIdLableFalse").removeClass("hidden");
        $("#studentIdLableTrue").addClass("hidden");
    }else{
        $("#studentIdLableFalse").addClass("hidden");
        $("#studentIdLableTrue").removeClass("hidden");
    }
});

$("#email").keyup(function () {   //邮箱
    var text=trim($("#email").val());
    var testEmail=/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    if(text.length==0||text.length>=50||!testEmail.test(text)){//testEmail.test(text)在文本不是邮箱时返回false
        $("#emailLableTrue").addClass("hidden");
        $("#emailLableFalse").removeClass("hidden");
    }else {
        $("#emailLableTrue").removeClass("hidden");
        $("#emailLableFalse").addClass("hidden");
    }

});

$("#password").keyup(function () {    //密码
    var text=trim($("#password").val());
    if(text.length<6||text.length>20){
        $("#passwordLableFalse").removeClass("hidden");
        $("#passwordLableTrue").addClass("hidden");
    }else{
        $("#passwordLableFalse").addClass("hidden");
        $("#passwordLableTrue").removeClass("hidden");
    }
});

$("#name").keyup(function () {
    var name=$("#name").val();
    console.log(name);
    if(name.length>20||name.length==0)
    {
        $("#nameLableFalse").removeClass("hidden");
        $("#nameLableTrue").addClass("hidden");
    }else {
        $("#nameLableFalse").addClass("hidden");
        $("#nameLableTrue").removeClass("hidden");
    }
})

function timeInterval(){//获取验证码的按钮中文字的改变
    $("#btnYanZheng").html("(60)秒后获取邮箱验证码");
    var timeSec = 59;
    var timeStr = '';
    var codeTime = setInterval(function Internal(){
        if (timeSec == 0){
            $("#btnYanZheng").html("获取验证码");
            $("#btnYanZheng").removeAttr("disabled","disabled");
            clearInterval(codeTime);
            $("#btnYanZheng").attr('disabled',false);
            return;
        }
        timeStr = "("+timeSec+")秒后获取邮箱验证码";
        $("#btnYanZheng").html(timeStr);
        timeSec--;
    },1000);
}

$("#btnYanZheng").click(function () {
    var text=trim($("#email").val());
    var testEmail=/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    if(text.length==0||text.length>=50||!testEmail.test(text)){//testEmail.test(text)在文本不是邮箱时返回false
        alert("邮箱不能为空/邮箱格式不正确");
        return;
    }
    timeInterval();
    $("#btnYanZheng").attr("disabled","disabled");
    $.ajax({
        type:"post",
        url:"user/emailcode",
        data:{
            email:text,
        },
        success:function (msg) {
            alert(msg);
        }
    })
});

$("#submit1").click(function () {//提交按钮点击事件
    var name=$("#name").val();
    var usermsg=$("#xueyuan").val()+$("#banji").val();
    var studentId=trim($("#studentId").val());
    var code=$("#yanzhengma").val();
    var dangerMsg="";
    var isUsefulMsg=true;
    if($("#yanzhengma").val().length==0)
    {
        isUsefulMsg=false;
        dangerMsg=dangerMsg+"验证码不能为空  ";
    }
    if(studentId.length==0||studentId.length>10){
        isUsefulMsg=false;
        dangerMsg=dangerMsg+"学号不能为空或长度超过10个字符  ";
    }
    var email=trim($("#email").val());
    var testEmail=/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    if(email.length==0||email.length>=50||!testEmail.test(email)) {//testEmail.test(text)在文本不是邮箱时返回false
        isUsefulMsg=false;
        dangerMsg=dangerMsg+"邮箱格式错误  ";
    }
    var password=trim($("#password").val());
    if(password.length<6||password.length>20){
        isUsefulMsg=false;
        dangerMsg=dangerMsg+"密码长度需要在6到20字符内  ";
    }
    if(name.length==0||name.length>20)
    {
        isUsefulMsg=false;
        dangerMsg=dangerMsg+"姓名长度控制在20位字符以内";
    }
    if(isUsefulMsg==false){
        $("#dangermsg").text(" 警告："+dangerMsg).removeClass("hidden");
        return;
    }
    var file=$("#inputfile").prop("files");
    var data=new FormData();
    data.append('img',file[0]);
    data.append('email',email);
    data.append('password',password);
    data.append('name',name);
    data.append('studentid',studentId);
    data.append('msg',usermsg);
    data.append('code',code);
    $.ajax({
        url:'user/regist',
        type:'post',
        cache: false,
        data:data,
        processData: false,
        contentType: false,
        success : function(msg) {
            if(msg.status==-1){
                alert(msg.info);
                location.reload();
            }else if(msg.status==1){
                alert(msg.info);
                window.location.href="/bs"
            }else{
                alert(msg.info);
            }
            $("#dangermsg").addClass("hidden");
        }
    })
})

//回车点击事件
$(document).keydown(function(event){
	if(event.keyCode==13){
	$("#submit1").click();
} })