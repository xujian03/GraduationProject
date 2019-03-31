/**
 * 用户中心的js文件
 */
var user=getUser();

var HEIGHT=$(window).height();
$("#name").text("姓名："+user.userName);
$("#studentid").text("学号："+user.userStudentId);
$("#studentMsg").text("班级："+user.userMsg);
$("#youxiang").text("邮箱："+user.userEmail);
$("#yue").text("账户余额："+user.userMoney+" 元");
$("#touxiang").attr("src",user.userAvatar);
// $("#tab").height(HEIGHT*0.8+"px");
// $("#tab1").height(HEIGHT*0.8+"px");

//loading页面设置
//使点击空白处遮罩层不会消失
$("#loadingModal").modal({backdrop:'static'});
//按Tab键遮罩层不会消失 ，默认值为true
$("#loadingModal").modal({keyboard:false});
//也可以一起运用
//backdrop 为 static 时，点击模态对话框的外部区域不会将其关闭。
//keyboard 为 false 时，按下 Esc 键不会关闭 Modal。
$('#loadingModal').modal({backdrop: 'static', keyboard: false});

//初始化函数
$(function () {
    loadHide();//loading页面隐藏
})

getAddressList();
diZhi();
jumpTab();
//获取二级学院
$.ajax({
    url:'user/getcollege',
    success:function(msg){
        $("#xueyuan").append(msg);
        getGrade();
    }
})
//学院改变后重新获取班级
$("#xueyuan").change(function(){
    getGrade();
})
//获取班级
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

//更换头像按钮的点击事件
$("#btnimg").click(function () {
    var img=$("#img").prop("files");
    if(img.length==0){
        alert("请先选择文件！");
        return;
    }
    loadShow();
    var data=new FormData();
    data.append("img",img[0]);
    data.append("email",user.userEmail);
    $.ajax({
        url:'user/reavatar',
        type:'post',
        cache: false,
        data:data,
        processData: false,
        contentType: false,
        success:function (msg) {
            loadHide();
            if(msg.status==-1){
                alert(msg.info);
                window.location.href="/bs/login.html";
            }else if(msg.status==1||msg.status==-2){
                alert(msg.info);
                location.reload();
            }
        }
    })
});
//更换学生班级
$("#btnstudentmsg").click(function () {
    var xueyuan=$("#xueyuan").val();
    var banji=$("#banji").val();
    if(xueyuan==null||xueyuan==""||banji==null||banji==""){
        alert("请选选择");
        return;
    }
    $.ajax({
        type:"post",
        url:"user/restudentmsg",
        data:{
            studentmsg:xueyuan+banji,
        },
        success:function (msg) {
            if(msg.status==0){
                alert(msg.info);
            }else if(msg.status==1){
                alert(msg.info);
                location.reload();
            }
        }
    })
});
//模态框点击事件
$("#addressSubmit").click(function () {
    var msg1=$("#input_province").val();
    var msg2=$("#input_city").val();
    var msg3=$("#input_area").val();
    var msg4=$("#addressMsg").val();
    var phone=$("#addressPhone").val();
    var name=$("#addressName").val();
    if(msg1==""||msg2==""||msg3==""||phone==""||name==""||msg4==""){
        alert("信息不完整");
        return;
    }
    if(name.length>20||phone.length>13){
        alert("手机号和姓名不应该太长");
        return;
    }
    $.ajax({
        type:"post",
        url:"user/addaddress",
        data:{
            phone:phone,
            name:name,
            msg:msg1+msg2+msg3+msg4,
        },
        success:function (msg) {
            if(msg.status==1){
                alert(msg.info);
                $('#myModal').modal('hide');
                getAddressList();
            }else if(msg.status==-2){
                alert(msg.info);
                $('#myModal').modal('hide');
            }
        }
    })
    //$('#myModal').modal('hide');
});
//手机号只能输入数字
$('#addressPhone').on('input propertychange', function(e) {

    var text = $(this).val().replace(/[^\d]/g, "");

    $(this).val(text)

})


/**
 * 获取收货地址
 */
function getAddressList() {
    $("#addressList").text("");
    $("#addressList").append("<tr class=\"primary\"><th>收货人</th><th>手机号</th><th>地址</th><th>操作</th></tr>");
    $("#LableAddress").text("");
    var address=getAddress();
    if(address.status==0){
        //alert(address.info);
        $("#LableAddress").append("<span class=\"label center-block label-danger pull-center\"><span class='glyphicon glyphicon-exclamation-sign'></span> 获取地址失败</span>");
        return;
    }
    if(address.total==0){
        $("#LableAddress").append("<span class=\"label center-block label-danger pull-center\"><span class='glyphicon glyphicon-exclamation-sign'></span> 暂无地址信息</span>");
        return;
    }
    var list=address.list;
    for(let i=0;i<address.total;i++){
        $("#addressList").append("<tr class=\"active\"><td>"+list[i].addressName+"</td><td>"+list[0].addressPhone+"</td><td>"+list[i].addressMsg+"</td><td><button class='btn btn-default form-control' id='delete_"+list[i].addressId+"'>删除</button></td></tr>");
        $("#delete_"+list[i].addressId).click(function () {
            var isdelet=confirm("是否删除改地址？");
            if(isdelet==false){
                return;
            }
            deleteAddress(list[i].addressId);
            getAddressList();
        })
    }
}

/**
 * 获取地址
 */
function getAddress() {
    var retUser=null;
    $.ajax({
        type:'get',
        url:'user/getaddress',
        async:false,
        success:function (msg) {
            retUser=msg;
        }
    });
    return retUser;
}

/**
 * 删除地址
 */
function deleteAddress(id) {
    var Msg=null;
    $.ajax({
        type:'post',
        async:false,
        url:'user/deleteaddress',
        data:{
            addressId:id,
        },
        success:function (msg) {
            Msg=msg;
        }
    })
    return Msg;
}
/**
 *获取登录用户信息
 */
function getUser() {
    var retUser=null;
    $.ajax({
        type:'get',
        url:'user/getuser',
        async:false,
        success:function (msg) {
            retUser=msg;
        }
    });
    return retUser;
}
//地址选择框
function diZhi() {
    var html = "<option value=''>== 请选择 ==</option>";
    $("#input_city").append(html);
    $("#input_area").append(html);
    $.each(sunwanglin,function(idx, item) {
                if (parseInt(item.level) == 0) {
                    html += "<option value='" + item.names + "' exid='" + item.code + "'>"
                        + item.names + "</option>";
                }
            });
    $("#input_province").append(html);

    $("#input_province").change(
        function() {
            if ($(this).val() == "")
                return;
            $("#input_city option").remove();
            $("#input_area option").remove();
            var code = $(this).find("option:selected")
                .attr("exid");
            code = code.substring(0, 2);
            var html = "<option value=''>== 请选择 ==</option>";
            $("#input_area").append(html);
            $
                .each(
                    sunwanglin,
                    function(idx, item) {
                        if (parseInt(item.level) == 1
                            && code == item.code
                                .substring(
                                    0,
                                    2)) {
                            html += "<option value='" + item.names + "' exid='" + item.code + "'>"
                                + item.names
                                + "</option>";
                        }
                    });
            $("#input_city").append(html);
        });

    $("#input_city").change(function() {
                if ($(this).val() == "")
                    return;
                $("#input_area option").remove();
                var code = $(this).find("option:selected")
                    .attr("exid");
                code = code.substring(0, 4);
                var html = "<option value=''>== 请选择 ==</option>";
                $.each(sunwanglin,function(idx, item) {
                            if (parseInt(item.level) == 2&& code == item.code.substring(0,4)) {
                                html += "<option value='" + item.names + "' exid='" + item.code + "'>"
                                    + item.names
                                    + "</option>";
                            }
                        });
                $("#input_area").append(html);
            });
    //绑定
    $("#input_province").val("北京市");
    $("#input_province").change();
    $("#input_city").val("市辖区");
    $("#input_city").change();
    $("#input_area").val("朝阳区");

};


$(window).resize(function () {
    // let height=$("nav").height();
    // console.log(height);
})

/**
 * 跳转到制定tab
 * 参数url+?+tabid=tabid
 */
function jumpTab() {
   var tabid=getParam("tabid");
   $("#"+tabid).click();
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

//loading页面隐藏
function loadHide(time) {
    var time1=time;
    if(time==null)
    {
        var time1=500;
    }
    setTimeout(function () {
        $("#loadingModal").modal('hide');
    },time1);
}
//loading页面显示
function loadShow() {
    $("#loadingModal").modal('show');
}


//更改密码
$("#passwordbtn").click(function(){
	var passwd=$("#oldpassword").val();//旧密码
	var passwd1=$("#newpassword").val();//新密码
	var passwd2=$("#renewpassword").val();//重复新密码
	if(passwd==''||passwd1==''||passwd2==''){
		alert("有参数为空");
		return;
	}
	$.ajax({
		url:'user/newpasswd',
		data:{
			oldpasswd:passwd,
			newpasswd:passwd1,
			renewpasswd:passwd2,
		},
		type:'post',
		success:function(msg){
			alert(msg.info);
			if(msg.status==1){
				window.location.reload();
			}
		}
	})
})


