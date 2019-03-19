<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" href="/bs/css/bootstrap.min.css">
    <script src="/bs/js/jquery-1.11.3.js"></script>
    <script src="/bs/js/bootstrap.js"></script>
    <script>
    var goods=<%= request.getParameter("goods")%>;
    </script>

</head>
<body>
<div class="row">
    <div class="col-md-8 col-md-offset-2" style="border:2px solid #b9def0;padding: 20px">
        <form >
            <legend>修改书籍信息</legend>
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

                <span id="dangermsg" class="text-left glyphicon glyphicon-remove-sign hidden label label-danger" style=""></span></br>
                <button type="button" class="btn btn-primary pull-right" id="fabu">修改</button>
            </div>

        </form>
    </div>
</div>
<script src="/bs/js/editgoods.js"></script>
</body>
</html>

