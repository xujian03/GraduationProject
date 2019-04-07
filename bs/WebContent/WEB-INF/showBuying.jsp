<%@page import="com.sun.xml.internal.txw2.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <link rel="stylesheet" href="/bs/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="../css/simditor.css" />
    <script src="/bs/js/jquery-1.11.3.js"></script>
    <link rel="stylesheet" href="/bs/css/jigsaw.css">
    <script src="/bs/js/jigsaw.js"></script>
    <script src="/bs/js/bootstrap.js"></script>
    <style>
        body{
            padding-top:70px;
            background-color: rgb(220,239,254);
        }
        .container a{text-decoration:none;color:DoderBlue;font-size: 10px}
        .container a:hover{text-decoration:none;color:DoderBlue}
        .container a:visited{text-decoration:none;color:DoderBlue}
    </style>
    <script>
    	var buyingId=<%=request.getParameter("buyingId")%>;
    </script>
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
            <li><a href="../search">商品搜索</a></li>
            <li><a href="../buy.html">求购专区</a></li>
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
        <div class="col-md-8 col-md-offset-2">
            <div style="width: 100%;background-color:#fff;border-bottom: 1px solid DarkGray">
                <div style="width: 100%;border-bottom: 1px solid Navy;padding: 10px 20px;">
                    <span>书籍名:</span><strong><%=request.getParameter("buyingName") %></strong>
                    <%
                    	String status=request.getParameter("buyingStatus");
                    	if(status.equals("0"))
                    	out.print("<span style='color: green' class='pull-right'>正在求购</span>");
                    	else if(status.equals("1"))
                        	out.print("<span style='color: red' class='pull-right'>已完成求购</span>");
                    %>
                </div>
                <div class="row">
                    <div class="col-md-2 text-center" style="min-height: 150px;">
                    	<div style="position:absolute;left:30%;top:20%;">
                    		<img src="<%=request.getParameter("userAvage") %>" style="width:80px;height:80px;" class="img-circle" alt=""><br>
                        	<a href="javascript:void(0)" style="">求购人<%=request.getParameter("userName") %></a>
                    	</div>
                        
                    </div>
                    <div class="col-md-10"  style="min-height: 150px;">
                        <div style="min-height: 100px;width: 100%;padding: 10px 20px">
                            	<%=request.getParameter("buyingMsg") %>
                        </div>
                        <div class="row">
                        <div class="col-md-4 col-md-offset-8">
                                <span style="margin-right: 20px">
                                    	<%=request.getParameter("buyingTime") %>
                                </span>
                            <a href="javascript:void(0)" onclick="commentModel(<% out.print(request.getParameter("buyingId")+",false,0,false,0"); %>);">回复</a>
                        </div>
                        </div>
                        <button  class="btn btn-primary" type="button" data-toggle="collapse" data-target="#collapseExample" style="margin-bottom: 4px;<% boolean p=request.getParameter("buyingImg").equals(""); if(p) out.println("display:none;"); %>" aria-expanded="false" aria-controls="collapseExample">
                            	查看求购书籍图片
                        </button>
                        
                        
                        <div class="collapse" id="collapseExample">
                            <div class="well">
                            	<%
                            		String strings=request.getParameter("buyingImg");
                            		String [] paths=strings.split(",");
                            		for(int i=0;i<paths.length;i++){
                            			out.print("<img src=\" "+paths[i]+" \" class=\"img-responsive\" alt=\"图片找不到了\">");
                            		}
                            	%>
                                
                            </div>
                        </div>
                    </div>
                </div>
               </div>
                
                <comment>
                
                </comment>
                <ul class="pager">
                    <li><a  href="javascript:void(0);" id="uppage">上一页</a> </li>
                    <li><a href="javascript:void(0);" id="downpage">下一页</a> </li>
                    <lable for="indexpage">第</lable>
                    <select id="indexpage">

                    </select>
                </ul>
                
            </div>
			<div class="col-md-2">
			<%
			//是否为作者自己的发布的求购如果时则在页面上添加求购管理的按钮
				String isMyBuying=request.getParameter("isMyBuying");
                    		if(isMyBuying.equals("true")&&request.getParameter("buyingStatus").equals("0")){
                    			out.print("<h4>求购管理</h4> <button class='btn btn-success' onclick='buyingOver("+request.getParameter("buyingId")+");'>求购已完成</button>");
                    		}
             %>
			
			</div>
        </div>
    </div>


<!-- 回复model -->
<div class="modal fade " id="commentModal" tabindex="-1" role="dialog" aria-labelledby="commentModalLabel">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="commentModalLabel">回复</h4>
            </div>
            <div class="modal-body">
                <textarea id="editor" placeholder="这里输入回复内容..." autofocus></textarea>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" data-dismiss="" id="replyBtn">回复</button>
            </div>
        </div>
    </div>
</div>


<script type="text/javascript" src="../js/module.js"></script>
<script type="text/javascript" src="../js/hotkeys.js"></script>
<script type="text/javascript" src="../js/uploader.js"></script>
<script type="text/javascript" src="../js/simditor.js"></script>
<script type="text/javascript" src="../js/buying.js"></script>
</body>
</html>