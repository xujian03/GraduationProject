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
console.log(isinclude);
if(isinclude==undefined||isinclude==null||isinclude=="false"||isinclude=="")
{
    $("input[name='isIncludeMyGoods']").get(1).checked=true;
}
if(isinclude==1){
    $("input[name='isIncludeMyGoods']").get(0).checked=true;
}else{
	$("input[name='isIncludeMyGoods']").get(1).checked=true;
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
        jumpwithparam(null,true);
    })
    $("#smallClass").change(function () {
        jumpwithparam(null,true);
    })
    $("input[type=radio][name='isIncludeMyGoods']").change(function () {
        jumpwithparam(null,true);
    })
    $("#sortby").change(function () {
        jumpwithparam(null,true);
    })
    $("#indexpage").change(function () {
        jumpwithparam(null,false);
    })
})

/**
 * 
 * @param upordown 翻页上一页下一页
 * @param isfirstpage 是否将page变成第一页
 */
function jumpwithparam(upordown,isfirstpage) {
    var bigClass=$("#bigClass").val();
    var smallClass=$("#smallClass").val();
    if(bigClass!=getParam("bigClass"))//如果选择框的大类别和url中的大类别不符合，那么说明大类别要更改，小类别就置空
    {
    	smallClass="";
    }
    var isinclude= $("input[name='isIncludeMyGoods']:checked").val();
    var sortby=$("#sortby").val();
    var keyword=$("#keyword").val();
    var page=getParam("page");
    var indexpage=$("#indexpage").val();
    if(indexpage!=page)
    {
    	page=indexpage;
    }
    if(page==null)page=1;
    if(upordown=="down"&&indexpage==page){
    	if(page>1)page--;
    	else return;
    }
    if(upordown=="up"&&indexpage==page){
    	if(page<countt)page++;
    	else return;
    }
    if(isfirstpage==true)page=1;
    if(bigClass==null)bigClass="";
    if(smallClass==null)smallClass="";
    if(isinclude==null)isinclude="";
    if(sortby==null)sortby="";

    window.location="/bs/search?keyword="+ keyword+"&page="+page+"&bigClass="+bigClass+"&smallClass="+smallClass+"&isincludemygoods="+isinclude+"&sortby="+sortby;
}

$("#searchBtn").click(function () {
    jumpwithparam(null,true);
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