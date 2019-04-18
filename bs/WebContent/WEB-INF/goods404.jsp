<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>商品不存在</title>
    <link rel="icon" href="/favicon.ico" type="image/x-icon"/>
    <meta http-equiv="refresh" content="5;url=/bs">
    <link rel="stylesheet" href="/bs/css/bootstrap.min.css">
    <link rel="stylesheet" href="/bs/css/bootstrap-select.min.css">
    <script src="/bs/js/jquery-1.11.3.js"></script>
    <script src="/bs/js/bootstrap.js"></script>
    <script src="/bs/js/bootstrap-select.min.js"></script>
    <link rel="stylesheet" href="/bs/css/jigsaw.css">
    <script src="/bs/js/jigsaw.js"></script>
</head>
<body>
<div class="container">
    <div class="row" style="margin-top:70px">
        <div class="col-md-offset-2 col-md-7 text-center">
            <img src="/bs/img/goods404.png" style="margin-right: 10px" alt=""><strong style="color:red">很抱歉，您查看的宝贝不存在，可能已下架或者被转移。</strong>
            <br>
            <small>页面将于5秒后跳转到首页</small>
        </div>
        <div class="col-md-2">
            <div class="pull-right" style="border:1px solid rgb(241,2,21);padding:10px 20px">
                <a href="/bs/cart.html" style="text-decoration:none;"><span class="glyphicon glyphicon-shopping-cart" style="color:rgb(241,2,21);"></span> <span style="color:rgb(241,2,21);">我的购物车  </span><span class="badge" style="background-color:rgb(241,2,21)" id="gouwuche">0</span></a>
            </div>
        </div>
    </div>
    <div class="row" style="margin-top: 50px;display:none">
        <span>推荐书籍：</span>
        <div style="width: 100%;border:0.5px solid rosybrown"></div>
    </div>
</div>
<script src="/bs/js/goods404.js"></script>
</body>
</html>