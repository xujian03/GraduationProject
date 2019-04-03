/**
 * 待发货js
 */
$.ajax({
    url:'goods/daifahuo',
    success:function (msg) {
        if(msg!=null&&msg.total!=0){
            var string="";
            var btnbar="";
            var showKuaidi="";
            for(var i=0;i<msg.list.length;i++){
                if(msg.list[i].goodsStatus==3)
                {
                    btnbar="<button type=\"button\" class=\"btn btn-xs btn-primary pull-right\" data-toggle=\"modal\" data-target=\"#daiFaHuo\" onclick='bindFaHuoGoodsId("+msg.list[i].goodsId+")'>发货</button>" +
                        "<button type=\"button\" onclick='ftffahuo("+msg.list[i].goodsId+")' class=\"btn btn-xs btn-primary pull-right\" style=\"margin-right: 15px\">当面交易成功</button>" +
                        "<br>";
                }else if(msg.list[i].goodsStatus==4){
                    btnbar= "<button type=\"button\" class=\"btn btn-xs btn-primary pull-right\" style=\"margin-right: 15px\" onclick='kuaidilujin("+"\""+msg.list[i].goodsName+"\","+msg.list[i].goodsId+")'>查看商品物流信息</button>" +
                        "<br>";
                    showKuaidi="<strong>快递信息：</strong><span>"+msg.list[i].goodsDeliveryName+":"+msg.list[i].goodsDeliveryNo+"</span><br>"
                }

                string+="<div class=\"col-md-12 round bg-info\" style=\"padding: 15px;margin-bottom: 15px\">" +
                    "<div class=\"col-md-4\"><img src=\""+msg.obj[i].path+"\" class=\"img-responsive\" alt=\"\"></div>" +
                    "<div class=\"col-md-8\">" +
                    "<strong>商品名：</strong><span>"+msg.list[i].goodsName+"</span><br>" +
                    "<strong>商品描述：</strong><span>"+msg.list[i].goodsDescribe+"</span><br>" +
                    "<strong>商品类别：</strong><span>"+msg.list[i].bigClass+"-"+msg.list[i].smallClass+"</span><br>" +
                    "<strong>商品价格：</strong><span>"+msg.list[i].goodsPrice+"</span><br>" +
                    "<strong>所属订单：</strong><span>"+msg.list[i].orderOutTradeNo+"</span><br>" +
                    "<strong>收货地址：</strong><span>"+msg.obj[i].addressMsg+"</span><br>" +
                    "<strong>收货人：</strong><span>"+msg.obj[i].addressName+"-"+msg.obj[i].addressPhone+"</span><br>" +
                    showKuaidi+
                    "<a href=\"goods/"+msg.list[i].goodsId+"\" target=\"_blank\">查看商品</a><br>" +
                     btnbar+
                    "<span class=\"help-block\"><span class=\"label label-danger\">注意:</span>&nbsp;使用<strong>快递</strong>的方式发货的请点击<strong>发货按钮</strong>填写快递单号，使用<strong>当面交易</strong>方式的请点击<strong>当面交易成功</strong>按钮。当面交易成功后请让买家 <strong>当面点击确认收货！</strong></span>" +
                    "</div>" +
                    "</div>";
            }
            $("#daifahuomsg").empty();
            $("#daifahuomsg").append(string);


        }else{
            $("#daifahuomsg").empty();
            $("#daifahuomsg").append("<div class='row'><span class=\"label label-danger text-center\" style='text-align: center;display:block;margin: 10px 20px'>暂无此类商品！</span></div>");
        }
    }
})

function ftffahuo(goodsId){
	var ischekc=confirm("是否通过当面交易将商品交给对方？");
	if(ischekc){
		$.ajax({
			url:'goods/ftffahuo',
			type:'post',
			data:{goodsId:goodsId},
			success:function(msg){
				alert(msg.info);
				if(msg.status==1){
					window.location.reload();
				}
			}
		})
	}
}


function bindFaHuoGoodsId(id) {
    console.log(id);
    $("#kuaidiqueren").unbind();
    if(id!=null){
        var pid=id;
        $("#kuaidiqueren").click({pid: pid},function (event) {
            var goodsDeliveryName=$("#kuaidi").val();
            var goodsDeliveryNo=$("#kuaididanhao").val();
            loadShow();
            $.ajax({
                type:'post',
                data:{
                    goodsId:event.data.pid,
                    goodsDeliveryName:goodsDeliveryName,
                    goodsDeliveryNo:goodsDeliveryNo
                },
                url:'goods/fahuo',
                success:function (msg) {
                    loadHide();
                    if(msg.status==1){
                        alert(msg.info);
                        location.reload();
                        return;
                    }else{
                        alert(msg.info);
                    }
                }
            })
        })
    }
}


/*     -----------     退款      ------------*/

function tuikuanMethod(goodsId){
	$("#tuikuanmodel").modal("show");
	$("#tkkuaidiqueren").unbind();
	$("#tkkuaidiqueren").click(function(){
		var tkphone=$("#tkphone").val();
		var tkname=$("#tkname").val();
		var tkaddress=$("#tkaddress").val();
		var tkkuaidi=$("#tkphone").val();
		var tkkuaididanhao=$("#tkphone").val();
		var dindanhao=GetDateNow();
		$.ajax({
			type:'post',
			url:'goods/tuikuan',
			data:{
				tkphone:tkphone,
				tkname:tkname,
				tkaddress:tkaddress,
				tkkuaidi:tkkuaidi,
				tkkuaididanhao:tkkuaididanhao,
				goodsId:goodsId,
				orderNo:dindanhao
			},
			success:function(msg){
				alert(msg.info);
				if(msg.status==1)window.location.reload();
			}
		})
	});
	
}
tuikuantab();
//退款的货物的tab
function tuikuantab(){
	$.ajax({
		url:'goods/tuikuangoods',
		success:function(msg){
			var string='';
			for(var i=0;i<msg.length;i++){
				string+="<div style='margin-bottom:20px;border-bottom:1px solid #888;padding:15px 10px'>"+
	                    		"<span>商品页面:<a href='/bs/goods/"+msg[i].goodsId+"'>前往</a></span><br>"+
	                    		"<span>商品名称:"+msg[i].goodsName+"</span><br>"+
	                    		"<span>快递名称:"+msg[i].goodsDeliveryName+"</span><br>"+
	                    		"<span>快递单号:"+msg[i].goodsDeliveryNo+"</span><br>"+
	                    		"<button onclick=(checkgoods("+msg[i].goodsId+")) class=\"btn btn-primary btn-xs\" title='钱将交还给买家'>确认收货</button>"+
                    		"</div>";
			}
			$("#tuikuandiv").empty();
			$("#tuikuandiv").append(string);
			
		}
	})
}

//退货后卖家确认收货
function checkgoods(id){
	var ischeck=confirm("确认收货后钱将交还给卖家！");
	if(ischeck){
		$.ajax({
			url:'goods/tuihuocheck',
			type:'post',
			data:{
				goodsId:id
			},
			success:function(msg){
				alert(msg.info);
				if(msg.status==1){window.location.reload();}
			}
		})
	}
}


//订单编号获取
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

