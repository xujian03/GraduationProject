/**
 * 注册模块
 */
var user=getUser();
if(user==null){
    alert("请先登录！");
    $("#loginModal").modal("show");
}

//loading页面设置
//使点击空白处遮罩层不会消失
$("#loadingModal").modal({backdrop:'static'});
//按Tab键遮罩层不会消失 ，默认值为true
$("#loadingModal").modal({keyboard:false});
//也可以一起运用
//backdrop 为 static 时，点击模态对话框的外部区域不会将其关闭。
//keyboard 为 false 时，按下 Esc 键不会关闭 Modal。
$('#loadingModal').modal({backdrop: 'static', keyboard: false});


$.ajax({
	url:'/bs/goods/getbigclass',
	success:function(msg){
		//msg=msg.substr(0,msg.length);
        loadHide();
		console.log(msg);
		$("#bigClass").append(msg);
		$('#smallClass').attr("disabled","disabled");
	}
})



//大类别选框值得改变获取小类别
$("#bigClass").change(function(){
	var bigClass=$("#bigClass").val()
	if(bigClass!=""&&bigClass!=null){
		$.ajax({
			url:'/bs/goods/getsmallclass',
			data:{
				bigclass:bigClass
			},
			success:function(msg){
				$("#smallClass").empty();
				if(msg==""){
					$("#smallClass").append("<option value=>没有选项</option>");
				}else{
					$("#smallClass").append(msg);
				}
				$("#smallClass").attr("disabled",false);
				//$('#areaSelect').attr("disabled","disabled");
			}
		})
	}else{
		$("#smallClass").empty();
		$('#smallClass').attr("disabled","disabled");
	}
})

//提交按钮点击事件
$("#fabu").click(function () {
    var goodsName=$("#goodsName").val();
    var goodsDescribe=$("#goodsdescribe").val();
    var bigClass=$("#bigClass").val();
    var smallClass=$("#smallClass").val();
    var goodsPrice=$("#goodsPrice").val();
    var files=$("#gphoto").prop('files');
    var dangerMsg="";
    var isUserful=true;
    if(goodsName==""||goodsName==null){
        isUserful=false;
        dangerMsg=dangerMsg+" 请输入商品名";
    }else if(goodsName.length>=30){
        isUserful=false;
        dangerMsg=dangerMsg+" 商品名不得超过30个字";
    }

    if(goodsDescribe==""||goodsDescribe==null){
        isUserful=false;
        dangerMsg=dangerMsg+" 请输入商品描述";
    }else if(goodsDescribe.length>=120){
        isUserful=false;
        dangerMsg=dangerMsg+" 商品描述不得超过120个字";
    }

    if(goodsPrice==""||goodsPrice==null){
        isUserful=false;
        dangerMsg=dangerMsg+" 请输入商品价格";
    }else if(!isNumber(goodsPrice)){
        isUserful=false;
        dangerMsg=dangerMsg+" 商品价格应为数字";
    }else if(goodsPrice<=0||goodsPrice>8000){
        isUserful=false;
        dangerMsg=dangerMsg+" 商品价格应在0到8000之间";
    }

    if(files.length==0){
        isUserful=false;
        dangerMsg=dangerMsg+" 请选择图片";
    }else if(files.length>10){
        isUserful=false;
        dangerMsg=dangerMsg+" 图片不得超过10张";
    }
    if(isUserful==false)
    {
        $("#dangermsg").text(" 警告："+dangerMsg).removeClass("hidden");
        return;
    }
    var data=new FormData();
    data.append("goodsName",goodsName);
    data.append("goodsDescribe",goodsDescribe);
    data.append("bigClass",bigClass);
    data.append("smallClass",smallClass);
    data.append("goodsPrice",goodsPrice);
    for(var i=0;i<files.length;i++){
        data.append("gphotos",files[i]);
    }
    $("#fabu").text("正在发布,请稍等").attr("disabled",true);
    loadShow();
    $.ajax({
        url:'/bs/goods/uploadgoods',
        type:'post',
        cache: false,
        data:data,
        processData: false,
        contentType: false,
        success : function(msg) {
            loadHide();
            console.log(msg);
            if(msg.status==-1){//重要参数为空
                alert(msg.info);
            }else if(msg.status==0){//未登录
                alert(msg.info);
            }else if(msg.status==1){//发布成功
                alert(msg.info);
                var isTiaozhuan=confirm("是否查看该商品?");
                if(isTiaozhuan==true){
                    window.location="/bs/goods/"+msg.obj;
                    return;
                }
                location.reload();
            }
            $("#dangermsg").addClass("hidden");
            $("#fabu").text("发布").attr("disabled",false);
        }
    })
})

//是否为数字
function isNumber(val){

    var regPos = /^\d+(\.\d+)?$/; //非负浮点数
    var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
    if(regPos.test(val) || regNeg.test(val)){
        return true;
    }else{
        return false;
    }

}

function getUser() {
    var retUser=null;
    $.ajax({
        type:'get',
        url:'/bs/user/getuser',
        async:false,
        success:function (msg) {
            retUser=msg;
        }
    });
    return retUser;
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