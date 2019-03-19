/**
 * 为user.html中的售卖商品栏
 */

var myGoodsTabNumber=1;

getMyGoods();

function getMyGoods() {
    $.ajax({
        url:'/bs/goods/getmygoods',
        data:{
          page:myGoodsTabNumber,
        },
        success:function (msg) {
            if(msg!=null&&msg.list.length!=0){
                var mygoodstring="";
                var buttonbar="";
                $("#goodsManage").empty();
                for(var i=0;i<msg.list.length;i++){
                    var descirpt="";
                    var buttonbar="";
                    if(msg.list[i].goodsStatus==1){
                        descirpt="<span style='color: green'>正在出售</span>";
                        buttonbar="<span class=\"label label-primary\">商品管理：</span><br><br>"+
                        "<button class=\"btn btn-default btn-xs pull-right\" onclick='jumpEidtGoods("+msg.list[i].goodsId+")'>修改商品信息</button>"+
                        "<span class=\"help-block\">点击按钮将会跳转到修改商品信息的页面</span>"+
                        "<div class=\"hrstyle\"></div>"+
                        "<button class=\"btn btn-danger btn-xs pull-right\" onclick='obtained("+msg.list[i].goodsId+")'>下架该商品</button>"+
                            "<button class=\"btn btn-danger btn-xs pull-right\" style='margin-right: 10px' onclick='deleteGoods("+msg.list[i].goodsId+")'>删除该商品</button>";
                    }else  if(msg.list[i].goodsStatus==0){
                        descirpt="<span style='color: orange'>已下架</span>";
                        buttonbar="<span class=\"label label-primary\">商品管理：</span><br><br>"+
                            "<button class=\"btn btn-default btn-xs pull-right\" onclick='jumpEidtGoods("+msg.list[i].goodsId+")'>修改商品信息</button>"+
                            "<span class=\"help-block\">点击按钮将会跳转到修改商品信息的页面</span>"+
                            "<div class=\"hrstyle\"></div>"+
                            "<button class=\"btn btn-success btn-xs pull-right\" onclick='shelf("+msg.list[i].goodsId+")'>上架该商品</button>"+
                            "<button class=\"btn btn-danger btn-xs pull-right\" style='margin-right: 10px' onclick='deleteGoods("+msg.list[i].goodsId+")'>删除该商品</button>";
                    }else  if(msg.list[i].goodsStatus==-1){
                        descirpt="<span style='color: red'>商品违规!</span>";
                        buttonbar="<span class=\"label label-primary\">商品管理：</span><br><br>"+
                            "<div class=\"hrstyle\"></div>"+
                         "<button class=\"btn btn-danger btn-xs pull-right\" style='margin-right: 10px' onclick='deleteGoods("+msg.list[i].goodsId+")'>删除该商品</button>";
                    }

                    mygoodstring+="<div class=\"col-md-12 round bg-info\" style=\"margin-bottom: 10px;padding: 15px;\"  >" +
                        "<div class=\"row\">" +
                        "<div class=\"col-md-8\">" +
                        "<span class=\"label label-primary\">商品信息：</span><br>" +
                        "<span>商品名称："+msg.list[i].goodsName+"</span><br>" +
                        "<span>商品类别："+msg.list[i].bigClass+"-"+msg.list[i].smallClass+"</span><br>" +
                        "<span>商品描述："+msg.list[i].goodsDescribe+"</span><br>" +
                        "<span>上架时间："+msg.list[i].goodsTime+"</span><br>" +
                        "<span>商品状态："+descirpt+"</span><br>" +
                        "<span>商品被浏览次数："+msg.list[i].goodsHeat+"次</span><br>" +
                        "<span>商品的价格："+msg.list[i].goodsPrice+"元</span><br>" +
                        "<a href=\"goods/"+msg.list[i].goodsId+"\" target=\"_blank\">查看商品</a><br>" +
                        "</div>" +
                        "<div class=\"col-md-4\">" +
                        buttonbar+
                        "</div>" +
                        "</div>" +
                        "</div>";
                }
                $("#goodsManage").append(mygoodstring);
                if(msg.total==myGoodsTabNumber){
                    $("#downpagegoods").addClass("pagedisable");
                }
                if(myGoodsTabNumber==1){
                    $("#uppagegoods").addClass("pagedisable");
                }
                $("#uppagegoods").unbind();
                $("#downpagegoods").unbind();
                $("#uppagegoods").click(function () {
                    if(myGoodsTabNumber>1){
                        myGoodsTabNumber--;
                        getMyGoods();
                        $("#downpagegoods").removeClass("pagedisable");
                    }
                })
                $("#downpagegoods").click(function () {
                    if(msg.total>myGoodsTabNumber){
                        myGoodsTabNumber++ ;
                        getMyGoods();
                        $("#uppagegoods").removeClass("pagedisable");

                    }
                })
            }else{
                $("#uppagegoods").addClass("pagedisable");
                $("#downpagegoods").addClass("pagedisable");
                $("#goodsManage").empty();
                $("#goodsManage").append("<div class='row'><span class=\"label label-danger text-center\" style='text-align: center;display:block;margin: 10px 20px'>暂无此类订单！</span></div>");
            }
        }
    })
}

function jumpEidtGoods(id) {
    window.open("/bs/goods/edit/"+id);
}

//上架
function shelf(id) {
    var isxia=confirm("是否确定上架？");
    if(isxia==false){
        return;
    }
    loadShow();
    $.ajax({
        type:'post',
        url:'/bs/goods/shelf',
        data:{
            id:id
        },
        success:function (msg) {
            loadHide();
            if(msg.status==1){
                alert(msg.info);
                location.reload();
            }else {
                alert(msg.info);
            }
        }
    })
}
//下架
function obtained(id) {
    var isxia=confirm("一旦下架其他用户将无法浏览您的商品，是否确定下架？");
    if(isxia==false){
        return;
    }
    loadShow();
    $.ajax({
        type:'post',
        url:'/bs/goods/obtained',
        data:{
            id:id
        },
        success:function (msg) {
            loadHide();
            if(msg.status==1){
                alert(msg.info);
                location.reload();
            }else {
                alert(msg.info);
            }

        }
    })
}
//删除商品
function deleteGoods(id) {
    var isdelete=confirm("是否删除该商品？（为不可逆操作）");
    if(isdelete){
        $.post("goods/deletegoods",
            {
                goodsId:id,
            },function (data) {
                if(data.status==1){
                    alert(data.info);
                    window.location.reload();
                    return;
                }else {alert(data.info);}
            })
    }
}
