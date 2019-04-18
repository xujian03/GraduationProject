<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="icon" href="/favicon.ico" type="image/x-icon"/>
<title>发布书籍</title>
    <link rel="stylesheet" href="/bs/css/bootstrap.min.css">
    <link rel="stylesheet" href="/bs/css/bootstrap-select.min.css">
    <script src="/bs/js/jquery-1.11.3.js"></script>
    <link rel="stylesheet" href="../css/jigsaw.css">
    <script src="../js/jigsaw.js"></script>
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
<div class="modal fade" id="loadingModal">
    <div style="width: 200px;height:20px; z-index: 20000; position: absolute; text-align: center; left: 50%; top: 50%;margin-left:-100px;margin-top:-10px">
        <div class="progress progress-striped active" style="margin-bottom: 0;">
            <div class="progress-bar" style="width: 100%;"></div>
        </div>
        <h4>正在加载...</h4>
    </div>
</div>


  <div class="row">
    <div class="col-md-8 col-md-offset-2" style="border:2px solid #b9def0;padding: 20px">
        <form >
            <legend>书籍信息上传</legend>
            <div class="form-group"><!--书籍名 -->
                <label for="goodsName">书籍名：</label>
                <input type="text" class="form-control" placeholder="输入书籍名称( 必填项/字数应在 1 到30字符之间 )" id="goodsName">
            </div>
            <div class="form-group"><!--书籍描述 -->
                <label for="goodsdescribe">书籍描述：</label>
                <textarea style=" height:100px;resize: vertical;" class="form-control" placeholder="输入书籍描述( 必填项字数应在 1 到120字符之间 )" id="goodsdescribe"></textarea>
            </div>

            <div class="form-group">
                <label >书籍分类：</label>
                <div class="row">
                    <div class="col-md-6 text-center">
                        <select class="form-control" id="bigClass" style="margin-bottom: 15px">
                            <option  value=>== 请选择 ==</option>
                            
                        </select>
                    </div>
                    <div class="col-md-6 text-center">
                        <select class="form-control" id="smallClass" style="margin-bottom: 15px">
                            
                           
                        </select>
                    </div>

                </div>

                <div class="form-group" ><!--书籍价格 -->
                    <div class="row">
                        <div class="col-md-4">
                            <label for="goodsPrice">书籍价格：</label>
                            <div class="input-group">
                                <span class="glyphicon glyphicon-yen input-group-addon" style="position:static"></span>
                                <input type="text" class="form-control" placeholder="书籍价格( 必填项/不得大于8000元 )" id="goodsPrice">
                            </div>
                        </div>

                    </div>

                </div>

                <div class="form-group">
                    <label for="gphoto">书籍的图片：</label>
                    <input type="file" id="gphoto" multiple="multiple">
                    <p class="help-block">按住CTRL键可选择多张图片最多支持10张、支持图像格式：jpg,gif,png。至少选择一张图片且图片小于10MB</p>
                </div>

                <span id="dangermsg" class="text-left glyphicon glyphicon-remove-sign hidden label label-danger" style=""></span></br>
                <button type="button" class="btn btn-primary pull-right" id="fabu">发布</button>
            </div>

        </form>
    </div>
  </div>




    <script src="/bs/js/bootstrap.js"></script>
    <script src="/bs/js/goodupload.js"></script>
</body>
</html>