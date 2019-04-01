<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" href="css/bootstrap.css">
    <script src="js/jquery-1.11.3.js"></script>
    <script src="js/bootstrap.js"></script>
	<link rel="stylesheet" type="text/css" href="css/jquery.dataTables.min.css">
    <script type="text/javascript" src="js/jquery.dataTables.min.js"></script>

    <style>
    </style>
</head>
<body>
	<div class="row">
		<nav class="navbar navbar-default navbar-static-top navbar-inverse">
    		<div class="navbar-header">
        		<a href="" style="margin-left:30px" class="navbar-brand">易书网后台</a>
    		</div>

			 <ul style="margin-right:40px" class="nav navbar-nav  navbar-right">
                <li class="active"><a href="">${sessionScope.admin.userId}</a></li>
                <li><a href="user/loginout" class="">登出</a></li>
            </ul> 
		</nav>
	</div>

    <div class="row">
        <div class="col-sm-3 col-xs-3 col-md-2" id="tab1" style="min-height:600px;">
            <ul class="nav nav-pills nav-stacked">
                <li  class="active"><a href="#pan1" data-toggle="pill" ><span class="glyphicon glyphicon-user"></span> 用户管理</a></li>
                <li><a href="#pan5" data-toggle="pill" id="A_dizhi"><span class="glyphicon glyphicon-cog"></span> 商品管理</a></li>
                <li><a href="#pan2" data-toggle="pill" id="A_dizhi"><span class="glyphicon glyphicon-cog"></span> 班级管理</a></li>
                <li><a href="#pan3" data-toggle="pill" id="A_dingdan"><span class="glyphicon glyphicon-th-list"></span> 商品类别管理</a></li>
                <li><a href="#pan4" data-toggle="pill" id="A_shangping"><span class="glyphicon glyphicon-book"></span> 发送通知</a></li>          
            </ul>
        </div>
        <div class="col-sm-9 col-xs-9 col-md-10" id="tab" style="min-height:600px;border-bottom-left-radius: 15px;border-bottom-right-radius: 15px">
            <br>
            <div class="tab-content">
        <div class="tab-pane active  " id="pan1" style="padding:10px 10px;">
        <div class="row">
		<table id="userTable" class="display" cellspacing="0" width="100%">
            <thead>
            <tr>
                <th>用户编号</th>
                <th>姓名</th>
                <th>邮箱</th>
				<th>班级</th>
                <th>学号</th>
				<th>状态</th>
				<th>操作</th>
            </tr>
            </thead>
            <tbody id="userlist">
           
            
            </tbody>
        </table>
		</div>
                </div>
				<div class="tab-pane fade" id="pan2" style="padding:10px 10px;">
                    班级管理
                </div>
                <div class="tab-pane fade" id="pan3" style="padding:10px 10px;">
                   商品类别管理
                </div>
                <div class="tab-pane fade" id="pan4">
                    	<!-- Button trigger modal -->
						<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#tongzhiModel">
						    向所有用户发送通知
						</button>
						<div class="row">
							<div class="col-md-4">
								<div class="input-group" style="margin-top: 40px">
					                <div class="input-icon-group">
					                    <div class="input-group" style="width:100%">
					                        <input type="text" class="form-control" placeholder="搜索" data-clearbtn="true" id="messageSearch"/>
					                    </div>
					                </div>
					                <span class="input-group-btn">
					                        <button class="btn btn-danger" type="button" title="搜索" id="messageSearchBtn"><span class="glyphicon glyphicon-search" ></span></button>
					                </span>
			             		</div>
							</div>
						</div>
						
						<!-- Modal -->
						<div class="modal fade" id="tongzhiModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
						    <div class="modal-dialog" role="document">
						        <div class="modal-content">
						            <div class="modal-header">
						                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						                <h4 class="modal-title" id="myModalLabel">Modal title</h4>
						            </div>
						            <div class="modal-body">
						           		<div class='row'>
						           		<div class="col-md-12">
						                	<label for="studentId">通知内容：</label>
                    						<input type="text" class="form-control" placeholder="输入通知内容" id="message"><br>
						                </div>
						           		</div>

						            </div>
						            <div class="modal-footer">
						                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						                <button type="button" id="messagebtn" class="btn btn-primary" data-dismiss="modal">发送</button>
						            </div>
						        </div>
						    </div>
						</div>
						
						<!-- 通知的列表 -->
						<table id="messageTable" class="display" cellspacing="0" width="100%">
				            <thead>
				            <tr>
				                <th>通知id</th>
				                <th>用户id</th>
				                <th>通知的内容</th>
								<th>是否为已读</th>
				                <th>发送时间</th>
				                <th>操作</th>
				            </tr>
				            </thead>
				            <tbody id="messagelist">
				           
				            
				            </tbody>
				        </table>
						
                </div>
                <div class="tab-pane fade" id="pan5">
                    	商品管理
                </div>
			</div>
           </div>   
</div>	
						


<script>

</script>	   
<script src="js/admincontrol.js"></script>
</body>
</html>