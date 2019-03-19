/**
 * 求购主页js文件
 */
$.ajax({
    url:'/bs/user/getuser',
    success:function (msg) {
        if(msg!=null){
            $("#qiugoubtn").show();
        }
    }
});
var MAXINDEX;
var page=1;
$("#uploadbtn").click(function () {
    var buyingName=$("#buyingName").val();
    var buyingMsg=$("#buyingMsg").val();
    if(buyingName.length==0||buyingName.length>=100||buyingMsg.length==0||buyingMsg.length>=50){
        alert("标题或内容长度不符合规则！");
        return;
    }
    var data=new FormData();
    data.append("buyingName",buyingName);
    data.append("buyingMsg",buyingMsg);
    var files=$("#buyingImg").prop('files');
    for(var i=0;i<files.length;i++){
        data.append("imgs",files[i]);
        if(i==5){break;}
    }
    $(this).attr("disabled","disabled");
    $.ajax({
        type:'post',
        url:'buying/upload',
        data:data,
        processData: false,
        contentType: false,
        success:function (msg) {
            $("#buyingName").val("");
            $("#buyingMsg").val("");
            $("#buyingImg").val("");
            if(msg.status==1){
                var istiao=confirm("发布成功，是否跳转到该页面?");
                if(istiao){
                    window.location="/bs/buying/"+msg.obj;
                    return;
                }
                $("#uploadModal").modal("hide");
                window.location.reload();
            }else {
                alert(msg.info);
                $("#uploadbtn").attr("disabled",false);
            }

        }
    })
})
getComment();
function getComment() {
   var keyword= $("#keyword").val();
   $.ajax({
       url:'buying/getbuying',
       data:{
           page:page,
           keyword:keyword
       },
       success:function (msg) {
           if(msg.list.length==0){
               var al=          "<div style='width: 700px;margin: 0 auto'>"+
                                    "<div class=\"alert alert-warning \">" +
                                        "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                                            "<span aria-hidden=\"true\">&times;</span>" +
                                        "</button>" +
                                        "<strong>暂无该条件的求购帖</strong>" +
                                    "</div>";
                                "<div>"
               $(".bg").empty();
               $(".bg").append(al);
               return;
           }
           var strings="";
           for(var i=0;i<msg.list.length;i++)
           {
               strings+="<a href=\"buying/"+msg.list[i].buyingId+"\">" +
                        "<div class=\"col-md-10 col-md-offset-1\">" +
                        "求购书名："+msg.list[i].buyingName+"<span class=\"pull-right\" style=\"color: black\">发布者："+msg.list[i].userName+" <small>时间 "+msg.list[i].buyingTime+"</small> </span><span style=\"margin:0px 20px;color:black\">"+msg.list[i].buyingMsg+"</span>" +
                        "</div>" +
                        "</a>";
           }
           MAXINDEX=msg.total;
           $(".bg").empty();
           $(".bg").append(strings);
           $("#uppage").unbind();
           $("#uppage").click(function () {
               if(page>1){
                   page--;
                   getComment();
               }
           });
           $("#downpage").unbind();
           $("#downpage").click(function () {
               if(page<MAXINDEX){
                   page++;
                   getComment();
               }
           });
           var option="";
           for(var i=1;i<=MAXINDEX;i++){
               var str2="";
               if(page==i){
                   str2="selected = \"selected\"";
               }
               option+="<option value=\""+i+"\" "+str2+">"+i+"页</option>";
           }
           $("#indexpage").empty();
           $("#indexpage").append(option);
           $("#indexpage").unbind();
           $("#indexpage").change(function () {
               var index=$("#indexpage").val();
               page=index;
               getComment();
           });

       }
   })
}
$("#searchBtn").click(function () {
        page=1;
        getComment();

})
// $("#keyword").keypress(function () {
//         page=1;
//         getComment();
// })

var cpLock=false;
$('#keyword').bind('compositionstart', function(){
    cpLock=true;
});
$('#keyword').bind('compositionend', function(){
    cpLock=false;
    if(!cpLock)
    {
        //业务逻辑
        page=1;
        getComment();
    };
});
$('#keyword').bind("keyup",function(){
    if(cpLock)
        return;
    //此处业务逻辑
    page=1;
    getComment();
    ;});

//回车点击事件
$(document).keydown(function(event){
    if(event.keyCode==13){
        $("#searchBtn").click();
    } })