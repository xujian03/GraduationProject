<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="icon" href="favicon.ico" type="image/x-icon"/>
    <title>支付</title>
    <link rel="stylesheet" href="/bs/css/bootstrap.min.css">
    <link rel="stylesheet" href="/bs/css/bootstrap-select.min.css">
    <link rel="stylesheet" href="/bs/css/iconfont.css">
    <script src="/bs/js/jquery-1.11.3.js"></script>
    <script src="/bs/js/bootstrap.min.js"></script>
    <style>
        .titleimg a:hover{ text-decoration:none;}
        .titleimg a{ text-decoration:none;}
        .col-center-block {
             float: none;
             display: block;
             margin-left: auto;
             margin-right: auto;
         }

    </style>
    <script>
        var order=<%=request.getParameter("order")%>;
    </script>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-sm-4 titleimg col-md-offset-1">
            <a href="/bs"><img src="/bs/img/website02.png" alt=""><span class="" style="color:black;font-size: 25px;vertical-align:text-top"> 收银台</span></a>
        </div>
        <div class="col-md-4 pull-right ">
            <div class="row" style="padding-top: 20px">
                <span style="color: #777777;">订单号：</span><span style="color:red;"><script>document.write(order.orderOutTradeNo)</script></span>
            </div>
            <div class="row">
                <span class="help-block">请您在15分钟内完成支付，否则订单会被自动取消</span>
                <a class="btn btn-primary" role="button" data-toggle="collapse" href="#collapseExample" aria-expanded="false" aria-controls="collapseExample">
                订单更多信息
                </a>
                <div class="collapse" id="collapseExample">
                    <div class="well" id="orderMsg">
                           <span class="help-block">收货地址：<script>document.write(order.addressMsg+" 收货人："+order.addressName+" "+order.addressPhone)</script></span>
                           <span class="help-block" style="float: left">商品名称：</span><div  style="float: left"><span class="help-block" id="goodsMsg"></span></div>
                            <div class="row"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
            <div class="col-md-8 col-md-offset-2" style="margin-top:20px;border:1px solid lightsteelblue;padding: 30px">
                <h3 style="color: crimson">选择支付方式：</h3>
                <div class="col-md-8 col-center-block" style="margin-top: 20px">
                    <input type="radio" style="margin-right: 20px;position: relative;bottom: 5px" name="payway" id="paywayyue" value="paywayyue"><label for="paywayyue"><i class="iconfont icon-icon" style="font-size: 50px"></i><span id="myyue" style="font-size: 30px">&nbsp;&nbsp;我的余额：</span></label><br>
                    <hr style="border: 1px solid #c0a16b">
                    <input type="radio" style="margin-right: 20px;position: relative;bottom: 7px"  name="payway" id="paywayzhifubao" value="paywayzhifubao"><span id="chooseZhiFuBao"><i class="iconfont icon-rectangle390" style="color:rgb(0,160,234);font-size: 50px" ></i><label for="paywayzhifubao"><span style="font-size: 30px;color:rgb(0,160,234);">&nbsp;支付宝</span></label></span><br>
                    <button type="button" id="pay" class="btn btn-danger btn-lg" disabled="disabled" style="margin-top: 30px">立即支付</button>
                    <button style="display: none" id="model" data-toggle="modal" data-target="#myModal"></button>
                </div>

            </div>
    </div>
</div>
<!--支付输入密码 -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">输入登录密码完成支付</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-4 text-right"><label for="password">输入密码：</label></div>
                    <div class="col-md-6"><input type="password"class="form-control" id="password"></div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="orderSubmit">支付</button>
            </div>
        </div>
    </div>
</div>
</body>
<script src="/bs/js/reaypay.js"></script>
