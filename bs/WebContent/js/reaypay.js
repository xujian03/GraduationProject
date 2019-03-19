/**
 *选择支付方式的js文件
 *
 */
$.ajax({
    url:'/bs/order/getorderlist',
    data:{
        outTradeNo:order.orderOutTradeNo,
    },
    success:function (msg) {
            $("#goodsMsg").append(msg);
            $("#goodsMsg").append("<span class='pull-right' style='border-top: 1px solid dimgrey'>共计："+"<span style=\";color:red;\">"+order.orderCountMoney+"</span></span>");
    }
})

$.ajax({
    url:'/bs/user/getuser',
    success:function (msg) {
        if(msg!=null){
            $("#myyue").append(msg.userMoney+'￥');
            if(msg.userMoney<order.orderCountMoney){
                $("#paywayyue").attr("disabled",true).attr("title","余额不足");
            }
        }
    }
})



$("#chooseZhiFuBao").click(function () {
    $("#paywayzhifubao").click();
});


$("input[name='payway']").change(function () {
    var check=$("input[name='payway']:checked").val();
    if(check!=null&&check!=""){
        $("#pay").attr("disabled",false);
        if(check==="paywayyue"){
            $("#pay").unbind();
            $("#pay").click(function () {
                $("#model").click();
                $("#orderSubmit").click(function () {
                    $.ajax({
                        type:'post',
                        url:'/bs/order/paywithyue',
                        data:{
                            orderNo:order.orderOutTradeNo,
                            password:$("#password").val()
                        },
                        success:function (msg) {
                            console.log(msg);
                            if(msg.status==1){
                                alert(msg.info);
                                location.href="/bs/user.html";
                            }else if(msg.status==0){
                                alert(msg.info);
                                location.href="/bs/login.html";

                            }else{
                                alert(msg.info);
                            }
                        }
                    })
                })
            })
        }else if(check==="paywayzhifubao"){
            $("#pay").unbind();
           $("#pay").click(function () {
               window.open('/bs/order/pay?orderNo='+order.orderOutTradeNo);
           })
        }
    }
})

$('#myModal').on('shown.bs.modal', function () {//模态框激活
    $('#myInput').focus();
});