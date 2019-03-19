<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发布书籍</title>
    <link rel="stylesheet" href="/bs/css/bootstrap.min.css">
    <link rel="stylesheet" href="/bs/css/bootstrap-select.min.css">
    <script src="/bs/js/jquery-1.11.3.js"></script>
</head>
<body>


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