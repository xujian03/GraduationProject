/**
 * 收货js
 */
$.ajax({
    url:'goods/daishouhuo',
    success:function (msg) {
        if(msg!=null&&msg.total!=0){
            var string="";
            var btnbar="";
            for(var i=0;i<msg.list.length;i++){
                string+="<div class=\"col-md-12 round bg-info\" style=\"padding: 15px 30px;margin-bottom: 15px\">" +
                    "<span class=\"label label-primary\">商品信息</span><br>" +
                    "<strong>商品名：</strong><span>"+msg.list[i].goodsName+"</span><br>" +
                    "<strong>商品描述：</strong><span>"+msg.list[i].goodsDescribe+"</span><br>" +
                    "<strong>商品类别：</strong><span>"+msg.list[i].bigClass+"-"+msg.list[i].smallClass+"</span><br>" +
                    "<strong>商品价格：</strong><span>"+msg.list[i].goodsPrice+" 元</span><br>" +
                    "<strong>所属订单：</strong><span>"+msg.list[i].orderOutTradeNo+"</span><br>" +
                    "<strong>快递信息：</strong><span>"+msg.list[i].goodsDeliveryName+":"+msg.list[i].goodsDeliveryNo+"</span><br>" +
                    "<span class=\"label label-warning\">操作</span><br>" +
                    "<a href=\"goods/"+msg.list[i].goodsId+"\" class=\"btn btn-default btn-xs\" target=\"_blank\" style=\"margin:5px 5px 5px 0px\">查看商品展示页面</a>" +
                    "<button  class=\"btn btn-default btn-xs\" target=\"_blank\" style=\"margin:5px 5px 5px 0px\" onclick='kuaidilujin("+"\""+msg.list[i].goodsName+"\","+msg.list[i].goodsId+")'>查看商品物流信息</button>"+
                    "<button class=\"btn btn-success btn-xs\" style=\"margin-right:5px\" onclick='queRenShouHuo("+msg.list[i].goodsId+")'>确认收货</button>" +
                    "<button class=\"btn btn-warning btn-xs\">退货</button>" +
                    "</div>";
            }
            $("#daishouhuomsg").append(string);
        }else{
            $("#daishouhuomsg").append("<div class='row'><span class=\"label label-danger text-center\" style='text-align: center;display:block;margin: 20px 20px'>暂无需要收货的商品！</span></div>");
        }
    }
});

function queRenShouHuo(id) {
    var isShouHuo=confirm("是否确认收货？");
    if(isShouHuo){
        $.ajax({
            type:'post',
            url:'goods/shouhuo',
            data:{
                goodsId:id
            },
            success:function (msg) {
                if(msg.status==1){
                    alert(msg.info);
                    location.reload();
                    return;
                }else{
                    alert(msg.info);
                }
            }
        })
    }
}

//快递的路径
function kuaidilujin(title,goodsId) {

    $("#kuaidiModalTitle").text(title);
    $.ajax({
        url:'goods/querykd',
        data:{
            goodsId:goodsId,
        },
        success:function (msg) {

            $("#kuaidiModalMsg").empty();
            if(msg!=null){
                msg=JSON.parse(msg);

                if(msg.hasOwnProperty("Reason")){alert("暂无快递信息!");return;}
                console.log(msg);
                var string="";
                for(var i=0;i<msg.Traces.length;i++){
                    string+="<img src=\"img/deliver.png\" style=\"width: 18px;height: 18px;.\" alt=\"\"><strong>时间："+msg.Traces[i].AcceptTime +"</strong>-<span>"+msg.Traces[i].AcceptStation+"</span><br>";
                    if(i!=msg.Traces.length-1){
                        string+="<div style='margin-left:10px;height: 20px;border-left: 2px solid gray;'></div>";
                    }
                }
                $("#kuaidiModalMsg").append(string);
                $("#kuaidiModal").modal("show");
            }

        }
    })
}