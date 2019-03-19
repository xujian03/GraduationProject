/**
 * user.html页面中处理订单的js
 *
 * <div class="col-md-12 bg-info round orderstyle" >
 <div class="col-md-4">
 <span class="label label-primary">订单信息：</span><br>
 <span>订&nbsp;&nbsp;单&nbsp;&nbsp;号：123123</span><br>
 <span>金&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额：10￥</span><br>
 <span>创建时间：12313</span><br>
 <span class="label label-primary">收货人信息：</span><br>
 <span>收&nbsp;&nbsp;货&nbsp;&nbsp;人：123123</span><br>
 <span>手&nbsp;&nbsp;机&nbsp;&nbsp;号：123123</span><br>
 <span>收货地址：123123</span><br>
 </div>
 <div class="col-md-8">
 <span class="label label-warning">购买商品详情：</span><br>
 <span>商品名：123</span><div class="fenge"></div>
 <span>价格：12￥</span><div class="fenge"></div>
 <span>快递名：圆通</span><div class="fenge"></div>
 <span>快递号：234242424242424</span><div class="fenge"></div><button class="btn btn-default btn-xs">售后</button>
 </div>
 </div>
 */
var PageOrderNumber=1;
var page1=0;
getOrderTab();
function getOrderTab(pageNumber){
    if(pageNumber==null||pageNumber==""){
        pageNumber=1;
        PageOrderNumber=1;
    }
   var type= $("#classOrder").val();
    $.ajax({
        url:"/bs/user/getorder",
        data:{
            page:pageNumber,
            type:type
        },
        success:function (msg) {
            $("#order").empty();
            var orderString="";
            var goodsString="";
            if(msg!=null){
                for(var i=0;i<msg.length;i++)
                {
                    goodsString="";
                    if(msg[i].hasOwnProperty("list"))
                        for(var j=0;j<msg[i].list.length;j++)
                        {
                            if(j==0)goodsString+="<span class='label label-warning'>购买商品详情：</span><br>";
                            goodsString+= "<span>商品名："+msg[i].list[j].goodsName+"</span><div class='fenge'></div>"+
                                "<span>价格："+msg[i].list[j].goodsPrice+"￥</span><div class='fenge'></div><br>";
                            if(msg[i].list[j].hasOwnProperty("goodsDeliveryName")) {
                                goodsString += "<span>快递名：" + msg[i].list[j].goodsDeliveryName + "</span><div class='fenge'></div>" +
                                    "<span>快递号：" + msg[i].list[j].goodsDeliveryNo + "</span><div class='fenge'></div><button type='button' class='btn btn-default btn-xs'>售后</button><br>";
                            }
                        }
                    var orderClass="";
                    if(msg[i].obj.orderStatus==0){orderClass="<span style='color:green;'>正在付款</span>";}
                    else if(msg[i].obj.orderStatus==1){orderClass="<span style='color:green;'>订单付款成功</span>";}
                    else if(msg[i].obj.orderStatus==-1){orderClass="<span style='color:red;'>订单付款失败</span>";}
                    else if(msg[i].obj.orderStatus==2){orderClass="<span style='color:green;'>订单完成</span>";}
                    else if(msg[i].obj.orderStatus==2){orderClass="<span style='color:orange;'>售后订单</span>";}
                    var orderCancelBtn="";
                    if(msg[i].obj.orderStatus==1){
                        orderCancelBtn="<button type='button' class='btn btn-default btn-xs' onclick='cancelOrder("+msg[i].obj.orderOutTradeNo+")'>取消订单</button><br>";
                    }
                    if(msg[i].obj.orderStatus==0) {
                        orderCancelBtn="<button type='button' class='btn btn-default btn-xs' onclick='jumpToPay("+msg[i].obj.orderOutTradeNo+")'>前往支付</button><br>"
                    }
                        orderString+="<div class='col-md-12 bg-info round orderstyle' >"+
                        "<div class='col-md-6'>"+
                        "<span class='label label-primary'>订单信息：</span><br>"+
                        "<span>订&nbsp;&nbsp;单&nbsp;&nbsp;号："+msg[i].obj.orderOutTradeNo+"</span><br>"+
                        "<span>金&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额："+msg[i].obj.orderCountMoney+"￥</span><br>"+
                        "<span>订单状态："+orderClass+"</span><br>"+
                        "<span>创建时间："+msg[i].obj.orderTime+"</span><br>"+
                        "<span class='label label-primary'>收货人信息：</span><br>"+
                        "<span>收&nbsp;&nbsp;货&nbsp;&nbsp;人："+msg[i].obj.addressName+"</span><br>"+
                        "<span>手&nbsp;&nbsp;机&nbsp;&nbsp;号："+msg[i].obj.addressPhone+"</span><br>"+
                        "<span>收货地址："+msg[i].obj.addressMsg+"</span><br>"+
                        orderCancelBtn+
                        "</div>"+
                        "<div class='col-md-6'>"+
                        goodsString+
                        "</div>"+
                        "</div>";
                }
                $("#order").append(orderString);
                page1= msg[0].total;
                console.log(msg);
                console.log(page1);
                if(PageOrderNumber==page1)
                {
                    $("#downpage").addClass("pagedisable");
                }else {
                    $("#downpage").removeClass("pagedisable");
                }
                if(PageOrderNumber==1){
                    $("#uppage").addClass("pagedisable");
                }else {
                    $("#uppage").removeClass("pagedisable");
                }
                $("#uppage").unbind();
                $("#downpage").unbind();
                $("#uppage").click(function () {
                    if(PageOrderNumber>1){
                        PageOrderNumber--;
                        $("#downpage").removeClass("pagedisable");
                        getOrderTab(PageOrderNumber);
                    }else {
                        $("#uppage").addClass("pagedisable");
                    }
                })
                $("#downpage").click(function () {
                    if(PageOrderNumber<page1){
                        PageOrderNumber++;
                        $("#uppage").removeClass("pagedisable");
                        getOrderTab(PageOrderNumber);
                    }else {
                        $("#downpage").addClass("pagedisable");
                    }
                })
            }else{
                $("#uppage").addClass("pagedisable");
                $("#downpage").addClass("pagedisable");
                $("#order").empty();
                $("#order").append("<div class='row'><span class=\"label label-danger text-center\" style='text-align: center;display:block;margin: 10px 20px'>暂无此类订单！</span></div>")
            }

        }
    })
}

//取消订单
function cancelOrder(orderNo){
    var isCancel=confirm("是否取消订单?");
    if(isCancel){
        loadShow();
        $.ajax({
            type:"post",
            data:{
                orderOutTradeNo:orderNo,
            },
            url:'/bs/order/cancelorder',
            success:function (msg) {
                loadHide();
                if(msg.status==1){
                    alert(msg.info+msg.obj);
                    location.reload();
                }else {
                    alert(msg.info);
                }
            }

        })
    }

}

$("#classOrder").change(function () {
    loadShow();
    getOrderTab();
    loadHide();
})

//跳转到支付界面
function jumpToPay(outTradeNo) {
    var isPay=confirm("是否前往支付界面？");
    if(isPay)
    location.href="/bs/order/readypay/"+outTradeNo;
}