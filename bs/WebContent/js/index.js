getShoppingCart()

/**
 * indexhtml js文件
 */
function bindmenu(){
    $("#menu").mouseenter(function () {
        $("#menuone").show();
    })
    $(".target").mouseover(function () {
        $("#menuone").show();
    })
    $("#menu").mouseleave(function () {
        $("#menuone").hide();
    })
    $(".target a").mousemove(function (e) {
        $(this).tab("show");
    })
}


$.ajax({
    url:'goods/getallclass',
    success:function (msg) {
        var string="";
        var smallclass="";
        for(let i=0;i<msg.length;i++){

            string+="<li class=\"target\"><a href=\"#pan"+(i+1)+"\" data-toggle=\"tab\" class='jumptosearch' >"+msg[i].obj.bigClass+"</a></li>";
            var str="";
            for(let j=0;j<msg[i].list.length;j++){
                str+="<div class=\"col-md-4 text-center menu1\" style=\"margin:5px 0px;\">" +
                    " <a href=\"search?keyword=&bigClass="+msg[i].obj.bigClass+"&smallClass="+msg[i].list[j].smallClass+"&isincludemygoods=0&sortby=1\">"+msg[i].list[j].smallClass+"</a>" +
                    "</div>";
            }

            smallclass+=" <div class=\"tab-pane \" id=\"pan"+(i+1)+"\">" +
                             " <div class=\"row\" style=\"padding: 20px 0px\">" +
                                     str+
                              "</div>" +
                       " </div>";
        }
        $("#classmenu").empty();
        $("#classmenu").append(string);
        $("#smallclass").empty();
        $("#smallclass").append(smallclass);
        $(".jumptosearch").click(function(){
        	var bigClass=$(this).text();
        	window.location.href="/bs/search?keyword=&bigClass="+bigClass+"&smallClass=&isincludemygoods=0&sortby=1"
        })
        bindmenu();
    }
});

$.ajax({
    url:'goods/suggest',
    success:function (msg) {
        var string="";
        var ctrl="";
        for(var i=0;i<msg.list.length;i++)
        {
            if(i==3)break;
            var activi="active";
            if(i!=0)activi="";
            string+="<div class=\"item "+activi+"\">" +
                "                        <a href=\"goods/"+msg.list[i].goodsId+"\">" +
                "                            <img src=\""+msg.obj[i]+"\" alt=\"图片无法显示\">" +
                "                        </a>" +
                "                    </div>";
            ctrl+="<li data-target=\"#carousel-example-generic\" data-slide-to=\""+i+"\" class=\" "+activi+"\"></li>";
        }
        $(".carousel-inner").append(string);
        $(".carousel-indicators").append(ctrl);
    }
})

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
            }else{
                $("#gouwuche").text(0);
            }
        }
    })
}

$(function () {
    setTimeout(function () {
    	$.ajax({
    		url:'/bs/message/countmessage',
    		type:'get',
    		success:function(count){
    			userMag(count);
    		}
    	})
        
    },200);
    $("#searchBtn").click(function () {
        jumpSearch();
    })
})




function userMag(count) {
    if(TUSER!=null){
        $("#usermsg").append("<div style=\"\">\n" +
                                "<a href=\"user.html\">\n" +
                                    "<img src=\""+TUSER.userAvatar+"\" class=\"img-circle\" alt=\"\">\n" +
                                "</a>\n" +
                            "</div>\n" +
                            "<span>"+TUSER.userName+"</span>\n" +
                            "<span>余额:"+TUSER.userMoney+"￥</span>\n" +
                            "<div class=\"menu1\">\n" +
                                "<a href=\"user.html?tabid=A_tongzhi\" style=\"color: #000066\">我的消息  <span class=\"badge\" style=\"background-color:#000066;size: 10px\" id=\"xiaoxi\">"+count+"</span></a>\n" +
                            "</div>");
    }else {
        $("#usermsg").append("<div style=\"\">\n" +
            "                    <a href=\"javascript:void (0);\" data-toggle=\"modal\" data-target=\"#loginModal\">\n" +
            "                        <img src=\"img/moren.jpg\" class=\"img-circle\" alt=\"\">\n" +
            "                    </a>\n" +
            "                </div>\n" +
            "                <div class=\"menu1\">\n" +
            "                    <a href=\"javascript:void (0);\" data-toggle=\"modal\" data-target=\"#loginModal\" style=\"color: #ac2925; \">登录</a>\n" +
            "                </div>");
    }
}


function jumpSearch() {
    var searchMsg=$("#searchMsg").val();
    // if(isEmpty(searchMsg)){return;}
    window.location="/bs/search?keyword="+searchMsg;
}



//判断字符是否为空的方法
function isEmpty(obj){
    obj=$.trim(obj);
    if(typeof obj == "undefined" || obj == null || obj == ""){
        return true;
    }else{
        return false;
    }
}
