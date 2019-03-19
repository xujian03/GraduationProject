/**
 * 商品页面不存在是显示的js
 */

getShoppingCart();



/**
 * 改变购物车上的数字和加入购物车按钮的文字
 */
function getShoppingCart(){
    $.ajax({
        type:'get',
        url:'/bs/goods/shoppingcartget',
        success:function (msg) {
            if(msg!=""&&msg!=null){
                $("#gouwuche").text(msg.length);
                for(var i=0;i<msg.length;i++){
                    if(goods.goodsId==msg[i].goodsId){
                        $("#addBtn").attr("disabled",true);
                        $("#addBtnMsg").text("已在购物车中");
                    }
                }
            }else{
                $("#gouwuche").text(0);
            }
        }
    })
}