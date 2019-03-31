<%@page import="net.xj.nutz.bean.Tb_goods"%>
<%@page import="org.nutz.lang.util.NutType"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="net.xj.nutz.bean.Tb_user"%>
<%@page import="org.nutz.json.Json"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>搜索</title>
    <link rel="stylesheet" href="/bs/css/bootstrap.css">
    <script src="/bs/js/jquery-1.11.3.js"></script>
    <script src="/bs/js/bootstrap.js"></script>
    <link rel="stylesheet" href="/bs/css/jigsaw.css">
    <script src="/bs/js/jigsaw.js"></script>
    <style>
        body{
            padding-top:70px;
            background-color: rgb(230,233,229);
        }
        .choose{
            color: grey;
        }
        a .col-md-3{
            margin:0px 0px 20px 0px;
        }
        .changewidth{
				transition: all 0.5s;
        }
        .changewidth:hover{
        	transform: scale(0.95);
        }
        small{
        	color:red,
        }
    </style>
</head>
<body>

<nav class="navbar navbar-default navbar-fixed-top">
    <div class="col-md-2">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-responsive-collapse">
                <span class="sr-only"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a href="/bs/" class="navbar-brand">导航</a>
        </div>
    </div>
    <div class="col-md-9">
        <div class="collapse navbar-collapse navbar-responsive-collapse" style="margin-right: 150px">
            <ul class="nav navbar-nav">
                <li><a href="buy.html">求购专区</a></li>
                <li class="nav-divider"> </li>
            </ul>
            <ul class="nav navbar-nav navbar-right" style="" id="loginoruser">

            </ul>

        </div>
    </div>
</nav>

<!--登录模态框-->
<div class="modal fade" id="loginModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content" style="width: 350px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="exampleModalLabel">登录</h4>
            </div>
            <div class="modal-body">
                <form method="post">
                    <div class="form-group">
                        <label for="login_name" class="control-label">账号:</label>
                        <input type="text" class="form-control" id="login_name" name="admin" placeholder="输入学号/邮箱" >
                    </div>
                    <div class="form-group">
                        <label for="login_password" class="control-label">密码:</label>
                        <input type="password" class="form-control" id="login_password" name="pwd"  placeholder="输入密码">
                    </div>
                    <div class="jigsaw1 form-group"><!--验证码 -->
                        <div id="captcha" style="position: relative"></div>
                        <div id="msg"></div>
                    </div>
                    <div class="btn-group btn-group-justified">
                        <div class="btn-group" role="group">
                            <button type="button" class="btn btn-primary " disabled="disabled" value="" id="loginbtn">登录</button>
                        </div>
                    </div>
                    <a href="/bs/login.html">手机端点击这里</a>
                </form>
            </div>
            <div class="modal-footer" style="border-top: none;">

            </div>
        </div>
    </div>
</div>
<!--登录模态框-->
<script src="/bs/js/title.js"> </script>
<div class="container">
    <div class="row">
        <div class="col-md-3 col-md-offset-1">
            <a href="/bs"><img src="img/website02.png" class="" alt=""></a>
        </div>
        <div class="col-md-6">
            <div class="input-group" style="margin-top: 20px">
                <div class="input-icon-group">
                    <div class="input-group" style="width:100%">
                        <input id="keyword" type="text" class="form-control" placeholder="搜索(多个关键词请用空格分开)" data-clearbtn="true"/>
                    </div>
                </div>
                <span class="input-group-btn">
                <button class="btn btn-danger" type="button" title="搜索" id="searchBtn"><span class="glyphicon glyphicon-search" ></span></button>
            </span>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-10 col-md-offset-1" style="background-color:#fff;">
            <div class="col-md-12">
                <div style="width:100%;padding: 20px;">

                    <div class="row">
                        <div class="col-md-3 text-center">
                            <strong class="choose">商品类别选择</strong>
                            <select class="form-control" id="bigClass" style="margin-bottom: 15px" value="计算机">
                                <option  value=>== 请选择 ==</option>

                            </select>
                        </div>
                        <div class="col-md-3 text-center" style="padding: 20px">
                            <select class="form-control" id="smallClass" style="margin-bottom: 15px">


                            </select>
                        </div>
                        <div class="col-md-3">
                            <strong class="choose">是否包括我自己发布的商品</strong>
                            <label style="margin-top:8px"><input name="isIncludeMyGoods" type="radio" value="1" />是</label>
                            <label><input name="isIncludeMyGoods" type="radio" value="0" />否</label>
                        </div>
                        <div class="col-md-3">
                        <strong class="choose">排序方式</strong>
							<select class="form-control" id="sortby" style="margin-bottom: 15px">
        						<option value="1">发布时间正序</option>
        						<option value="2">发布时间倒序</option>
        						<option value="3">热度正序</option>
        						<option value="4">热度倒序</option>
                            </select>
                        </div>
                    </div>


                </div>
            </div>
        </div>

        <div class="col-md-10 col-md-offset-1" style="background-color:#fff; ">
        <%
        		String[] paths=request.getParameter("path").split(",");
        		List<Tb_goods> list = (List<Tb_goods>)Json.fromJson(NutType.list(Tb_goods.class), request.getParameter("goods"));
        		for(int i=0;i<list.size();i++)
        		{
        			String h="<a class='urlstyle' href=\"/bs/goods/"+list.get(i).getGoodsId()+"\">" +
                        "<div class=\"col-md-3\" style='background-color:#F3F3F3;padding:5px 3px;'>" +
                            "<img src=\""+paths[i]+"\" class=\"text-center changewidth\" style=\"width: 100%\">" +
                            "<div class=\"text-center\" style=\"width: 100%;\"><span  style='color:#666'>" +
                                list.get(i).getGoodsName() +
                            "</span></div>" +
                            "<div class=\"text-center\" style=\"width: 100%;\">" +
                                "<small style='color:#666'>" +
                                    "商品热度：" +list.get(i).getGoodsHeat()+
                                "</small>" +
                            "</div>" +
                            "<div class=\"text-center\" style=\"width: 100%;\">" +
                                "<small style='color:#666'>" +
                                   " 商品发布日期：" +list.get(i).getGoodsTime()+
                                "</small>" +
                            "</div>" +
                        "</div>" +
                   " </a>";
                   out.print(h);
        		}
        		if(list==null||list.size()==0){//当没有符合条件的商品出现的话，
        			out.print("<div class='alert alert-success' role='alert'>sorry,暂无符合条件的商品。</div>");
        		}
        %>
        
        </div>
    </div>
<div class="row">
        <ul class="pager">
          <% 
          	String page1=request.getParameter("page");
          	String count=request.getParameter("count");
          	int pp=Integer.valueOf(page1);
          	int cc=Integer.valueOf(count);
          	out.print("<li><a  href=\"javascript:void(0);\" onclick=\"jumpwithparam('down',false);\" id=\"uppage\">上一页</a> </li><li><a href=\"javascript:void(0);\" onclick=\"jumpwithparam('up',false);\" id=\"downpage\">下一页</a> </li>");
          	String pString="";
          	for(int i=0;i<cc;i++)
          	{
          		if(i!=pp-1)
          		pString+="<option value='"+(i+1)+"'>"+(i+1)+"页</option>";
          		else pString+="<option value='"+(i+1)+"' selected='selected'>"+(i+1)+"页</option>";
          	}
          	out.print("<select id=\"indexpage\">"+pString+"</select>");
          %>  
		</ul>
</div>
</div>
<script>
  var countt=<%=cc %>;
</script>
<script src="js/search.js"></script>
</body>
</html>
    
    
    
    
<!-- 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body class="List<Tb_user> list = (List<Tb_user>)Json.fromJson(NutType.list(Tb_user.class), userString);">
搜索
<%=request.getParameter("goods")
	
%>
</body>
</html>

 -->
