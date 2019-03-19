/**
 * 搜索页面js文件
 */
$.ajax({
    url:'/bs/goods/getbigclass',
    success:function(msg){
        $("#bigClass").append(msg);
        $('#smallClass').attr("disabled","disabled");
        var bigclass=decodeURI(getParam("bigClass"));
        if(bigclass==undefined||bigclass==null||bigclass==""){
            return;
        }
        $("#bigClass").val(bigclass);
        var smallclass=getParam("smallClass");
        // if(smallclass==undefined||smallclass==null||smallclass==""){
        //     return;
        // }
        changeSmallClass(smallclass);
    }
})

var isinclude=getParam("isincludemygoods");
if(isinclude==undefined||isinclude==null||isinclude=="false"||isinclude=="")
{
    $("input[name='isIncludeMyGoods']").get(1).checked=true;
}
if(isinclude=="true"){
    $("input[name='isIncludeMyGoods']").get(0).checked=true;
}

var sortby=getParam("sortby");
if(sortby==2)
{
    $("#sortby").val("2");
}else if(sortby==3)
{
    $("#sortby").val("3");
}else if(sortby==4)
{
    $("#sortby").val("4");
}else
{
    $("#sortby").val("1");
}


var k=getParam("keyword");
if(k!="null") $("#keyword").val(k);

//大类别选框值得改变获取小类别
$("#bigClass").change(function(){
    changeSmallClass();
})

console.log( $("input[name='isIncludeMyGoods']:checked").val())

function changeSmallClass(smallclass) {
    var bigClass=$("#bigClass").val()
    if(bigClass!=""&&bigClass!=null){
        $.ajax({
            url:'/bs/goods/getsmallclass',
            data:{
                bigclass:bigClass
            },
            success:function(msg){
                $("#smallClass").empty();
                if(msg!=""){
                    $("#smallClass").append("<option  value=>== 请选择 ==</option>");
                    $("#smallClass").append(msg);
                }
                $("#smallClass").attr("disabled",false);
                if(smallclass==undefined||smallclass==null||smallclass==""){
                    return;
                }
                $("#smallClass").val(smallclass);
            }
        })
    }else{
        $("#smallClass").empty();
        $('#smallClass').attr("disabled","disabled");
    }
}

$(function () {
    $("#bigClass").change(function () {
        jumpwithparam();
    })
    $("#smallClass").change(function () {
        jumpwithparam();
    })
    $("#input[name='isIncludeMyGoods']").change(function () {
        jumpwithparam();
    })
    $("#sortby").change(function () {
        jumpwithparam();
    })
})


function jumpwithparam() {
    var bigClass=$("#bigClass").val();
    var smallClass=$("#smallClass").val();
    var isinclude= $("input[name='isIncludeMyGoods']:checked").val();
    var sortby=$("#sortby").val();
    var keyword=$("#keyword").val();
    if(bigClass==null)bigClass="";
    if(smallClass==null)smallClass="";
    if(isinclude==null)isinclude="";
    if(sortby==null)sortby="";

    window.location="/bs/search?keyword="+ keyword+"&bigClass="+bigClass+"&smallClass="+smallClass+"&isincludemygoods="+isinclude+"&sortby="+sortby;
}

$("#searchBtn").click(function () {
    jumpwithparam();
})
/**
 * 获取指定的URL参数值
 * URL:http://www.quwan.com/index?name=tyler
 * 参数：paramName URL参数
 * 调用方法:getParam("name")
 * 返回值:tyler
 */
function getParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if ( r != null ){
        return decodeURI(r[2]);
    }else{
        return null;
    }
}