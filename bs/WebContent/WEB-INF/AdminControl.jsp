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
    <script type="text/javascript" src="js/echarts.js"></script>

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
        <div class="col-sm-3 col-xs-3 col-md-2" id="tab1" style="min-height:600px;border-right:1px solid #555">
            <ul class="nav nav-pills nav-stacked">
                <li  class="active"><a href="#pan1" data-toggle="pill" ><span class="glyphicon glyphicon-user"></span> 用户管理</a></li>
                <li><a href="#pan5" data-toggle="pill" id=""><span class="glyphicon glyphicon-glass"></span> 商品管理</a></li>
                <li><a href="#pan2" data-toggle="pill" id=""><span class="glyphicon glyphicon-blackboard"></span> 班级管理</a></li>
                <li><a href="#pan3" data-toggle="pill" id=""><span class="glyphicon glyphicon-tasks"></span> 商品类别管理</a></li>
                <li><a href="#pan4" data-toggle="pill" id=""><span class="glyphicon glyphicon-envelope"></span> 发送通知</a></li>          
                <li><a href="#pan6" data-toggle="pill" id=""><span class="glyphicon glyphicon-signal"></span> 用户活跃度</a></li>          
                <li><a href="#pan7" data-toggle="pill" id=""><span class="glyphicon glyphicon-yen"></span> 提现申请</a></li>          
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
                    	<div class='row'>
                    		<span style='font-size:25px'>管理按钮：</span>
                    		<button  data-toggle="modal" data-target="#addschool"  class='btn btn-default'>添加学院</button>
                    		<button class='btn btn-success'  data-toggle="modal" data-target="#addclassgrade"   >添加班级</button>
                    	</div>
                    	<div class="row">
                    		<div class="col-md-6">
                    			<table id="collegeTable" class="display" cellspacing="0" width="100%">
						            <thead>
						            <tr>
						                <th>学院编号</th>
						                <th>学院名称</th>
						                <th>操作</th>
						            </tr>
						            </thead>
						            <tbody id="collegelist">
						           
						            
						            </tbody>
					        	</table>
                    		</div>
                    		<div class="col-md-6">
                    			<table id="gradeTable" class="display" cellspacing="0" width="100%">
						            <thead>
						            <tr>
						                <th>班级编号</th>
						                <th>班级名称</th>
						                <td>所属学院编号</td>
						                <th>操作</th>
						            </tr>
						            </thead>
						            <tbody id="gradelist">
						           
						            
						            </tbody>
					        	</table>
                    		</div>
                    	</div>
                    	
                    	<!-- 添加学院 -->
						<div class="modal fade" id="addschool" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
						    <div class="modal-dialog" role="document">
						        <div class="modal-content">
						            <div class="modal-header">
						                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						                <h4 class="modal-title" id="myModalLabel">添加学院</h4>
						            </div>
						            <div class="modal-body">
						           		<div class='row'>
						           		<div class="col-md-12">
						                	<label for="school">添加的学院名称：</label>
                    						<input type="text" class="form-control" placeholder="输入学院名称" id="school"><br>
						                </div>
						           		</div>

						            </div>
						            <div class="modal-footer">
						                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						                <button type="button" id="addschoolbtn" class="btn btn-primary" data-dismiss="modal">添加</button>
						            </div>
						        </div>
						    </div>
						</div>
                    	
                    	<!-- 添加班级-->
						<div class="modal fade" id="addclassgrade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
						    <div class="modal-dialog" role="document">
						        <div class="modal-content">
						            <div class="modal-header">
						                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						                <h4 class="modal-title" id="myModalLabel">添加班级</h4>
						            </div>
						            <div class="modal-body">
						           		<div class='row'>
						           		<div class="col-md-6">
						           			<label for="xueyuan">属于哪个学院：</label>
						                	<select class="form-control" id="xueyuan" style="margin-bottom: 15px">
						                	</select>
										</div>
						           		<div class="col-md-6">
						                	<label for="gclass">添加的班级名称：</label>
                    						<input type="text" class="form-control" placeholder="输入班级名称" id="gclass"><br>
						                </div>
						           		</div>

						            </div>
						            <div class="modal-footer">
						                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						                <button type="button" id="addgradebtn" class="btn btn-primary" data-dismiss="modal">添加</button>
						            </div>
						        </div>
						    </div>
						</div>
                    	
                    	
                    	
                    	
                    	
                    	
                </div>
                <div class="tab-pane fade" id="pan3" style="padding:10px 10px;">
                   		<div class='row'>
                    		<span style='font-size:25px'>管理按钮：</span>
                    		<button  data-toggle="modal" data-target="#addbigclass"  class='btn btn-default'>添加商品大类别</button>
                    		<button class='btn btn-success'  data-toggle="modal" data-target="#addsmallclass"   >添加商品小类别</button>
                    	</div>
                    	
                    	
                    	<div class="row">
                    		<div class="col-md-6">
                    			<table id="bigClassTable" class="display" cellspacing="0" width="100%">
						            <thead>
						            <tr>
						                <th>类别编号</th>
						                <th>类别名称</th>
						                <th>操作</th>
						            </tr>
						            </thead>
						            <tbody id="bigClasslist">
						           
						            
						            </tbody>
					        	</table>
                    		</div>
                    		<div class="col-md-6">
                    			<table id="smallClassTable" class="display" cellspacing="0" width="100%">
						            <thead>
						            <tr>
						                <th>小类别编号</th>
						                <th>小类别名称</th>
						                <td>所属类别编号</td>
						                <th>操作</th>
						            </tr>
						            </thead>
						            <tbody id="smallClasslist">
						           
						            
						            </tbody>
					        	</table>
                    		</div>
                    	</div>
                    	
						<!-- 添加大类别 -->
						<div class="modal fade" id="addbigclass" tabindex="-1" role="dialog" aria-labelledby="addbigclassLabel">
						    <div class="modal-dialog" role="document">
						        <div class="modal-content">
						            <div class="modal-header">
						                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						                <h4 class="modal-title" id="addbigclassLabel">添加</h4>
						            </div>
						            <div class="modal-body">
						           		<div class='row'>
						           		<div class="col-md-12">
						                	<label for="message">大类别：</label>
                    						<input type="text" class="form-control" placeholder="输入类别" id="bigclassinput"><br>
						                </div>
						           		</div>

						            </div>
						            <div class="modal-footer">
						                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						                <button type="button" id="bigclassbtn" class="btn btn-primary" data-dismiss="modal">发送</button>
						            </div>
						        </div>
						    </div>
						</div>
                    	
                    	<!-- 添加小类别-->
						<div class="modal fade" id="addsmallclass" tabindex="-1" role="dialog" aria-labelledby="myModalLabel1">
						    <div class="modal-dialog" role="document">
						        <div class="modal-content">
						            <div class="modal-header">
						                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						                <h4 class="modal-title" id="myModalLabel1">添加小类别</h4>
						            </div>
						            <div class="modal-body">
						           		<div class='row'>
						           		<div class="col-md-6">
						           			<label for="xueyuan">属于哪个大的类别：</label>
						                	<select class="form-control" id="bigClass" style="margin-bottom: 15px">

                        					</select>
										</div>
						           		<div class="col-md-6">
						                	<label for="gclass">小类别的名称：</label>
                    						<input type="text" class="form-control" placeholder="输入小类别" id="smallclassinput"><br>
						                </div>
						           		</div>

						            </div>
						            <div class="modal-footer">
						                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						                <button type="button" id="addsmallclassbtn" class="btn btn-primary" data-dismiss="modal">添加</button>
						            </div>
						        </div>
						    </div>
						</div>
                    	
                    	
                    	
                    	
                    	
                    	
                    	
                    	
                    	
                    	
                    	
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
						<div class="modal fade" id="tongzhiModel" tabindex="-1" role="dialog" aria-labelledby="tongzhiModelLabel">
						    <div class="modal-dialog" role="document">
						        <div class="modal-content">
						            <div class="modal-header">
						                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						                <h4 class="modal-title" id="tongzhiModelLabel">通知</h4>
						            </div>
						            <div class="modal-body">
						           		<div class='row'>
						           		<div class="col-md-12">
						                	<label for="message">通知内容：</label>
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
                    	<table id="goodsTable" class="display" cellspacing="0" width="100%">
				            <thead>
				            <tr>
				                <th>商品id</th>
				                <th>所属用户id</th>
				                <th>类别</th>
								<th>商品名</th>
				                <th>商品描述</th>
				                <th>发布时间</th>
				                <th>状态</th>
				                <th>操作</th>
				            </tr>
				            </thead>
				            <tbody id="goodslist">
				           
				            
				            </tbody>
				        </table>
				        <ul class="pager">
                        <li><a  href="javascript:void(0);" id="uppagegoods">上一页</a> </li>
                        <li><a href="javascript:void(0);" id="downpagegoods">下一页</a> </li>
                    </ul>
                </div>
                <div class="tab-pane fade" id="pan6">
                    	<div id="userloginchart" style="width:1000px;height:450px">
                    	</div>
                </div>
                 <div class="tab-pane fade" id="pan7">
                    	<div class='row'>
                    		<div class='col-md-10 col-offset-md-1'>
                    			<table id="applicationTable" class="display" cellspacing="0" width="100%">
						            <thead>
						            <tr>
						                <th>申请id</th>
						                <th>支付宝账号</th>
						                <th>申请金额</th>
						                <th>用户id</th>
						                <th>申请时间</th>
						                <th>操作</th>
						            </tr>
						            </thead>
						            <tbody id="applicationlist">
						           
						            
						            </tbody>
				        		</table>
                    		</div>
                    	</div>
                </div>
			</div>
           </div>   
</div>	
						

<script>

</script>	   
<script src="js/admincontrol.js"></script>
</body>
</html>