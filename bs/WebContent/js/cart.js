/**
 * 购物车js文件
 */
var ORDER=GetDateNow();

// 页面加载完成执行
$(function () {
    $("#orderSubmit").click(function () {
        var orderNo=$("#orderNo").text();
        var adderNo=$("#address").val();
        
        $.ajax({
            url:'/bs/order/creatorder',
            type:'post',
            data:{
                orderOutTradeNo:orderNo,
                addressId:adderNo
            },
            success:function (msg) {
                console.log(msg);
                if(msg.status==1){
                    var isPay=confirm("开始支付");
                    if(isPay){
                        location.href="/bs/order/readypay/"+orderNo;
                    }

                }
            }
        })
    })
})

// 获取购物车列表
$.ajax({
    url:'/bs/goods/imgshoppingcartget',
    success:function (msg) {
        if(msg!=""&&msg!=null){
            getAddress();
            var string="";
            var totalmoney=0;
            for(var i=0;i<msg.length;i++){
                var goodsStatus=""
                if(msg[i].status==1){
                    goodsStatus="<span style=\"color:SeaGreen;\">正常出售中</span>";
                }else {
                    goodsStatus="<span style=\";color:Crimson;\" >已售出！</span>";;
                }
                string+="<tr class='table-bordered'><td><img src='"+msg[i].info+"' class='pull-right' alt='图片加载失败'></td><td class='pull-left' style='line-height: 50px'>&nbsp;&nbsp;&nbsp;"+msg[i].obj.goodsName+"</td><td>"+"￥"+msg[i].obj.goodsPrice+"</td><td>"+goodsStatus+"</td><td>"+"<a href=\"javascript:void(0);\"id='"+msg[i].obj.goodsId+"'>移除该商品></a>"+"</td></tr>";
                totalmoney+=msg[i].obj.goodsPrice;
            }
            $("#total").text("￥"+totalmoney);
            $("#havegoods").append(string);
            for(let i=0;i<msg.length;i++){// 移除商品
                $("#"+msg[i].obj.goodsId).click(function () {
                    $.ajax({
                        type:'post',
                        url:'/bs/goods/shoppingcartremove',
                        data:{
                            goodsId:msg[i].obj.goodsId,
                            isRemoveAll:false
                        },
                        success:function (msg) {
                            if(msg.status==1){
                                alert(msg.info);
                                window.location.reload();
                            }else {
                                alert(msg.info);
                            }
                        }
                    })
                })
            }
            $("#removeAll").click(function () {
                $.ajax({
                    type:'post',
                    url:'/bs/goods/shoppingcartremove',
                    data:{
                        isRemoveAll:true,
                    },
                    success:function (msg) {
                        if(msg.status==1){
                            location.reload();
                        }
                    }
                })
            })
            $("#nogoods").hide();
            $("#jiesuantab").show();
        }else {
            $("#jiesuantab").hide();
            $("#nogoods").show();
        }

    }
});


// 模态框信息添加
var isAdd=false;
$("#jiesuan").click(function () {
    if(isAdd==false){
    	var add=$("#address").val();
    	if(add==""||add==null){var isAddAddress=confirm("是否添加地址？");
    		if(isAddAddress){
    			window.location.href="/bs/user.html?tabid=A_dizhi";
    		}
    		return;
    	}
    	
        modelMsg(true);
        isAdd=true;
    }

})


// 获取地址
function getAddress(){
    $.ajax({
        url:'/bs/user/getaddress',
        success:function (msg) {
            if(msg.status==1){
                if(msg.total==0){
                    $("#address").append("<option value=>== 请先去添加地址 ==</option>");
                }else{
                    var string="";
                    for(var i=0;i<msg.total;i++){
                        string+="<option value='"+msg.list[i].addressId+"'>收货人："+msg.list[i].addressName+"-收货人手机号："+msg.list[i].addressPhone+"-收货地址："+msg.list[i].addressMsg+"</option>";
                    }
                    $("#address").append(string);
                }
            }

        }
    })
}

$('#myModal').on('shown.bs.modal', function () {// 模态框激活
    $('#myInput').focus();
});

$("#address").change(function () {
    modelMsg(false);
})

// 模态框信息
function modelMsg(isTrue){

    $("#addressMsg").empty();
    $("#orderNo").empty();
    $("#orderNo").append(ORDER);

    $("#addressMsg").append($("#address").find("option:selected").text());

    if(isTrue)
    $.ajax({
        url:'/bs/goods/shoppingcartget',
        success:function (msg) {
            // $("#orderMsg").empty();

            for(var i=0;i<msg.length;i++){
                $("#orderMsg").append("<strong>商品"+(i+1)+":</strong><span>"+msg[i].goodsName+"</span><br>");
            }
            $("#orderMsg").append("<div class=\"pull-right\">"+"<span class=\"help-block\" style=\"margin-right: 20px;float: left\">共计：</span>"+"<span style=\"padding:20px;color:red;font-size:20px;margin-right: 30px\">"+$("#total").text()+"</span></div>");
        }
    })
}


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
            string+="<button class=\"btn btn-default pull-right\"onclick=\"getsuggest();\" style='margin-right: 10px'>" +
                "<span class=\"glyphicon glyphicon-repeat\" style=\"line-height: 2px\"></span><span>&nbsp;换一组</span>" +
                "</button>";
            $("#goodsMsg").append(string);
        }
    })
}

// 订单编号获取
function GetDateNow() {
    var vNow = new Date();
    var sNow = "";
    sNow += String(vNow.getFullYear());
    sNow += String(vNow.getMonth() + 1);
    sNow += String(vNow.getDate());
    sNow += String(vNow.getHours());
    sNow += String(vNow.getMinutes());
    sNow += String(vNow.getSeconds());
    sNow += String(vNow.getMilliseconds());
    return sNow;
}
