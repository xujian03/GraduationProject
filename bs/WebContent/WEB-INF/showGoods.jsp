<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
   	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0, user-scalable=yes" />
    <title>Title</title>
    <link rel="stylesheet" href="/bs/css/bootstrap.min.css">
    <link rel="stylesheet" href="/bs/css/reset.css">
    <script src="/bs/js/jquery-1.11.3.js"></script>
       <link rel="stylesheet" href="/bs/css/jigsaw.css">
    <script src="/bs/js/jigsaw.js"></script>
    <script>
        var goods=<%= request.getParameter("goods")%>;
        var gphoto=<%= request.getParameter("gphoto")%>;
    </script>
    <style>
    body{
            padding-top:70px;
            background-color: rgb(230,233,229);
                }
        *{margin:0px; padding:0px;}
#qq{width:600px;/*宽*/height:170px;/*高*/background:#fff;/*背景颜色*/
	margin:50px auto 30px; border-radius:5px;/*Html5 圆角*/}
#qq p{font-size:12px; color:#666; font-family:"微软雅黑";
		line-height:45px; text-indent:20px;}
#qq .message{width:560px; height:70px;margin:0px auto; outline:none;
			border:1px solid #ddd; /*粗细 风格 颜色*/}
#qq .But{width:560px;height:35px;margin:15px auto 0px; position:relative;/*相对，参考对象*/}
#qq .But img.bq{float:left;/*左浮动*/}
#qq .But span.submit{width:80px;height:30px; background:#ff8140;					display:block; float:right;/*右浮动*/								line-height:30px;border-radius:5px;								cursor:pointer;/*手指*/color:#fff;font-size:12px;					text-align:center;font-family:"微软雅黑";}

/*face begin*/
#qq .But .face{width:390px; height:170px; background:#fff; padding:10px;
				border:1px solid #ddd; box-shadow:2px 2px 3px #666;
				position:absolute;/*绝对定位*/ top:21px; left:15px;
				display:none;/*隐藏*/}
#qq .But .face ul li{width:22px;height:22px; 
				list-style-type:none;/*去掉圆点*/ float:left;
				margin:2px; cursor:pointer;}

/*msgCon begin*/
.msgCon{width:600px;height:500px; margin:0px auto;}
.msgCon .msgBox{ background:#fff;
				padding:10px; margin-top:20px;}
.msgCon .msgBox dl{height:60px; border-bottom:1px dotted #ddd;}
.msgCon .msgBox dl dt{width:50px; height:50px;float:left;}
.msgCon .msgBox dl dt img{border-radius:25px;}
.msgCon .msgBox dl dd{width:500px; height:50px;  line-height:50px;float:right; font-size:16px;font-family:"微软雅黑";}
.msgCon .msgBox .msgTxt{font-size:12px; color:#666; line-height:25px;}
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
    <div class="row" style="border:0.5px solid LightGrey;background-color:#fff">
        <div class="col-md-9 col-md-offset-1" >
            <div class="row">
                <div class="col-sm-7"><!--图片部分-->
                    <div id="carousel-example-generic" class="carousel slide" data-ride="carousel">
                        <!-- 图片下面的三个圆形按钮 
                        <ol class="carousel-indicators">
                            <li data-target="#carousel-example-generic" data-slide-to="0" class="active"></li>
                            <li data-target="#carousel-example-generic" data-slide-to="1"></li>
                            <li data-target="#carousel-example-generic" data-slide-to="2"></li>
                        </ol>-->

                        <!-- 图片 -->
                        <div class="carousel-inner" role="listbox">
                            <div class="item active">
                                <script>
                                    document.write('<img src="'+gphoto[0].gphotoPath+'"alt="图片无法显示">')
                                </script>
                            </div>
                            <script>
                                for(var i=1;i<gphoto.length;i++){
                                    document.write('<div class="item">');
                                    document.write('<img src="'+gphoto[i].gphotoPath+'"alt="图片无法显示"></div>')
                                }
                            </script>
                        </div>
							
                      
                    </div>
                            <div class="row" style="margin-top:5px">
                            <div class="col-sm-1">
                            <a href="javascript:void(0);" style="position:relative;left: 10px;top: 5px" id="goleft"><span class="glyphicon glyphicon-chevron-left" style="font-size: 40px;margin-top: 40%"></span></a>
                            </div>
                            <div class="col-sm-10">
                            
                            <div style="display: inline-block;width: 100%;height: 60px;white-space:nowrap;overflow: hidden" id="outDiv">
                            <span style="position: relative;" id="insideSpan" >
                           
							<script>
							 for(var i=0;i<gphoto.length;i++){
								 if(i==0)document.write('<img src="'+gphoto[i].gphotoPath+'" id="imgofload" style="height: 50px;margin:5px 0px 5px 5px;" alt="" data-target="#carousel-example-generic" data-slide-to="0" class="active">');
								 else document.write('<img src="'+gphoto[i].gphotoPath+'"  style="height: 50px;margin:5px 0px 5px 5px;" alt="" data-target="#carousel-example-generic" data-slide-to="'+i+'" class="">');
                             }
							</script>
                            </span>

                            </div>
                            </div>

                            <div class="col-sm-1">
                            <a href="javascript:void(0);"  style="position:relative;left: -32px;top: 10px" id="goright"> <span class="glyphicon glyphicon-chevron-right" style="font-size: 40px"></span></a>
                            </div>
                            </div>
                </div>
                <div class="col-sm-5">
                		<div class="row">
                			<div class="col-sm-offset-5 col-sm-offset-4 ">
                				<br><br>
                				<div style="margin:15px 0px">
                				    <span class="label label-danger" style="font-size:20px ">商品</span><strong  style="font-size:20px "><script>document.write(" "+goods.goodsName);</script></strong>
                				    <div style="height:20px"></div>
                				    <span class="label label-info" style="font-size:20px ">详情</span><span style="font-size:20px "><script>document.write(" "+goods.goodsDescribe);</script></span>
                				</div>
                				<div class="bg-info" style="padding:3px">
                					<span style="padding:20px;color:#999;font-size:20px">售价</span><span style="padding:20px;color:red;font-size:20px;">￥<script>document.write(" "+goods.goodsPrice);</script></span>
                				</div>
                				<div  style="padding:3px">
                					<span style="padding:20px;font-size:20px">商品状态</span>
                                    <script>
                					if(goods.goodsStatus==1)document.write("<span style=\"padding-bottom:20px;padding-top:20px;color:green;font-size:20px;\">正常</span>");
                					else if(goods.goodsStatus==2)document.write("<span style=\"padding-bottom:20px;padding-top:20px;color:orange;font-size:20px;\">正在付款</span>");
                					else if(goods.goodsStatus==3)document.write("<span style=\"padding-bottom:20px;padding-top:20px;color:red;font-size:20px;\">已售出</span>");
                					else if(goods.goodsStatus==0)document.write("<span style=\"padding-bottom:20px;padding-top:20px;color:red;font-size:20px;\">已下架，其他用户无法访问</span>");
                					else if(goods.goodsStatus==4)document.write("<span style=\"padding-bottom:20px;padding-top:20px;color:green;font-size:20px;\">已发货</span>");
                					else if(goods.goodsStatus==5)document.write("<span style=\"padding-bottom:20px;padding-top:20px;color:green;font-size:20px;\">已确认收货</span>");
                					else if(goods.goodsStatus==6)document.write("<span style=\"padding-bottom:20px;padding-top:20px;color:red;font-size:20px;\">退货中</span>");
                					else if(goods.goodsStatus==8)document.write("<span style=\"padding-bottom:20px;padding-top:20px;color:red;font-size:20px;\">退货完成</span>");
                                    </script>
                				</div>
                				<button class="btn btn-danger btn-lg" style="margin-top:20px;display:none" id="addBtn"><span style="margin-top:20px;font-family:STHupo;font-size:23px;margin:4px" id="addBtnMsg">加入购物车</span> </button>
                			</div>
                		</div>
                </div>
            </div>
        </div>
        <div class="col-md-2"><!-- 购物车 -->
           <div class="pull-right" style="border:1px solid rgb(241,2,21);padding:10px 20px">
             <a href="/bs/cart.html" style="text-decoration:none;"><span class="glyphicon glyphicon-shopping-cart" style="color:rgb(241,2,21);"></span> <span style="color:rgb(241,2,21);">我的购物车  </span><span class="badge" style="background-color:rgb(241,2,21)" id="gouwuche">0</span></a>
           </div>			
        </div>
    </div>
    <div class="row" style="margin-top:50px;">
        <div class="col-md-12 "  >
            <ul class="nav nav-tabs text-center" >
                <li style="width:50%" class="active"><a href="#pan1" data-toggle="tab">商品详情</a></li>
                <li style="width:50%"><a href="#pan2" data-toggle="tab">用户讨论</a></li>
            </ul>
            <div class="tab-content" style="margin-top:-1px;border:0.5px solid LightGrey;background-color:#fff;padding-left:0px;padding-right:0px">
                <div class="tab-pane active" id="pan1" style="padding:10px 20px">
                		<script>
                			for(var i=0;i<gphoto.length;i++){
                            	document.write('<div style="margin-top:6px">');
                            	document.write('<img src="'+gphoto[i].gphotoPath+'" class="img-responsive" alt="图片无法显示"></div>')
                        	}
                		</script>
                    	
                </div>
                <div class="tab-pane fade" id="pan2" style="min-height:200px;padding:10px">
                   <div class="row">
                   		<div class="col-md-8 col-md-offset-2" id='goodsComment'>
                   			<!-- 评论内容 -->
                   			
                   		</div>
                   		<div class="col-md-8 col-md-offset-2" id='goodsComment'>
                   		<ul class="pager">
                            <li><a  href="javascript:void(0);" id="uppage">上一页</a> </li>
                            <li><a href="javascript:void(0);" id="downpage">下一页</a> </li>
                            <lable for="indexpage">第</lable>
                            <select id="indexpage">
                            	
                            </select>
                        </ul>
                        </div>
                   </div>
                    <div id="qq">
						<p>发表我的评论</p>
						<div class="row">
							<div class="col-md-3" id="touser" style="display:none;">
							<input type="text" class="form-control" id="tousermsg">
							</div>
						</div>
						
						
						<div class="message" contentEditable='true'></div>

						<div class="But">
						<img src="/bs/images/bba_thumb.gif" class='bq'/>
						<span class='submit' id="fabiao">发表</span>
											<!--face begin-->
											<div class="face">
										<ul>
											<li><img src="/bs/images/zz2_thumb.gif" title="[织]" ></li>
											<li><img src="/bs/images/horse2_thumb.gif" title="[神马]"></li>
											<li><img src="/bs/images/fuyun_thumb.gif" title="[浮云]"></li>
											<li><img src="/bs/images/geili_thumb.gif" title="[给力]"></li>
											<li><img src="/bs/images/wg_thumb.gif" title="[围观]"></li>
											<li><img src="/bs/images/vw_thumb.gif" title="[威武]"></li>
											<li><img src="/bs/images/panda_thumb.gif" title="[熊猫]"></li>
											<li><img src="/bs/images/rabbit_thumb.gif" title="兔子]"></li>
											<li><img src="/bs/images/otm_thumb.gif" title="[奥特曼]"></li>
											<li><img src="/bs/images/j_thumb.gif" title="[囧]"></li>
											<li><img src="/bs/images/hufen_thumb.gif" title="[互粉]"></li>
											<li><img src="/bs/images/liwu_thumb.gif" title="[礼物]"></li>
											<li><img src="/bs/images/smilea_thumb.gif" title="呵呵]"></li>
											<li><img src="/bs/images/tootha_thumb.gif" title="嘻嘻]"></li>
											<li><img src="/bs/images/laugh.gif" title="[哈哈]"></li>
											<li><img src="/bs/images/tza_thumb.gif" title="[可爱]"></li>
											<li><img src="/bs/images/kl_thumb.gif" title="[可怜]"></li>
											<li><img src="/bs/images/kbsa_thumb.gif" title="[挖鼻屎]"></li>
											<li><img src="/bs/images/cj_thumb.gif" title="[吃惊]"></li>
											<li><img src="/bs/images/shamea_thumb.gif" title="[害羞]"></li>
											<li><img src="/bs/images/zy_thumb.gif" title="[挤眼]"></li>
											<li><img src="/bs/images/bz_thumb.gif" title="[闭嘴]"></li>
											<li><img src="/bs/images/bs2_thumb.gif" title="[鄙视]"></li>
											<li><img src="/bs/images/lovea_thumb.gif" title="[爱你]"></li>
											<li><img src="/bs/images/sada_thumb.gif" title="[泪]"></li>
											<li><img src="/bs/images/heia_thumb.gif" title="[偷笑]"></li>
											<li><img src="/bs/images/qq_thumb.gif" title="[亲亲]"></li>
											<li><img src="/bs/images/sb_thumb.gif" title="[生病]"></li>
											<li><img src="/bs/images/mb_thumb.gif" title="[太开心]"></li>
											<li><img src="/bs/images/ldln_thumb.gif" title="[懒得理你]"></li>
											<li><img src="/bs/images/yhh_thumb.gif" title="[右哼哼]"></li>
											<li><img src="/bs/images/zhh_thumb.gif" title="[左哼哼]"></li>
											<li><img src="/bs/images/x_thumb.gif" title="[嘘]"></li>
											<li><img src="/bs/images/cry.gif" title="[衰]"></li>
											<li><img src="/bs/images/wq_thumb.gif" title="[委屈]"></li>
											<li><img src="/bs/images/t_thumb.gif" title="[吐]"></li>
											<li><img src="/bs/images/k_thumb.gif" title="[打哈气]"></li>
											<li><img src="/bs/images/bba_thumb.gif" title="[抱抱]"></li>
											<li><img src="/bs/images/angrya_thumb.gif" title="[怒]"></li>
											<li><img src="/bs/images/yw_thumb.gif" title="[疑问]"></li>
											<li><img src="/bs/images/cza_thumb.gif" title="[馋嘴]"></li>
											<li><img src="/bs/images/88_thumb.gif" title="[拜拜]"></li>
											<li><img src="/bs/images/sk_thumb.gif" title="[思考]"></li>
											<li><img src="/bs/images/sweata_thumb.gif" title="[汗]"></li>
											<li><img src="/bs/images/sleepya_thumb.gif" title="[困]"></li>
											<li><img src="/bs/images/sleepa_thumb.gif" title="[睡觉]"></li>
											<li><img src="/bs/images/money_thumb.gif" title="[钱]"></li>
											<li><img src="/bs/images/sw_thumb.gif" title="[失望]"></li>
											<li><img src="/bs/images/cool_thumb.gif" title="[酷]"></li>
											<li><img src="/bs/images/hsa_thumb.gif" title="[花心]"></li>
											<li><img src="/bs/images/hatea_thumb.gif" title="[哼]"></li>
											<li><img src="/bs/images/gza_thumb.gif" title="[鼓掌]"></li>
											<li><img src="/bs/images/dizzya_thumb.gif" title="[晕]"></li>
											<li><img src="/bs/images/bs_thumb.gif" title="[悲伤]"></li>
											<li><img src="/bs/images/crazya_thumb.gif" title="[抓狂]"></li>
											<li><img src="/bs/images/h_thumb.gif" title="[黑线]"></li>
											<li><img src="/bs/images/yx_thumb.gif" title="[阴险]"></li>
											<li><img src="/bs/images/nm_thumb.gif" title="[怒骂]"></li>
											<li><img src="/bs/images/hearta_thumb.gif" title="[心]"></li>
											<li><img src="/bs/images/unheart.gif" title="[伤心]"></li>
											<li><img src="/bs/images/pig.gif" title="[猪头]"></li>
											<li><img src="/bs/images/ok_thumb.gif" title="[ok]"></li>
											<li><img src="/bs/images/ye_thumb.gif" title="[耶]"></li>
											<li><img src="/bs/images/good_thumb.gif" title="[good]"></li>
											<li><img src="/bs/images/no_thumb.gif" title="[不要]"></li>
											<li><img src="/bs/images/z2_thumb.gif" title="[赞]"></li>
											<li><img src="/bs/images/come_thumb.gif" title="[来]"></li>
											<li><img src="/bs/images/sad_thumb.gif" title="[弱]"></li>
											<li><img src="/bs/images/lazu_thumb.gif" title="[蜡烛]"></li>
											<li><img src="/bs/images/clock_thumb.gif" title="[钟]"></li>
											<li><img src="/bs/images/cake.gif" title="[蛋糕]"></li>
											<li><img src="/bs/images/m_thumb.gif" title="[话筒]"></li>
											<li><img src="/bs/images/weijin_thumb.gif" title="[围脖]"></li>
											<li><img src="/bs/images/lxhzhuanfa_thumb.gif" title="[转发]"></li>
											<li><img src="/bs/images/lxhluguo_thumb.gif" title="[路过这儿]"></li>
											<li><img src="/bs/images/bofubianlian_thumb.gif" title="[bofu变脸]"></li>
											<li><img src="/bs/images/gbzkun_thumb.gif" title="[gbz困]"></li>
											<li><img src="/bs/images/boboshengmenqi_thumb.gif" title="[生闷气]"></li>
											<li><img src="/bs/images/chn_buyaoya_thumb.gif" title="[不要啊]"></li>
											<li><img src="/bs/images/daxiongleibenxiong_thumb.gif" title="[dx泪奔]"></li>
											<li><img src="/bs/images/cat_yunqizhong_thumb.gif" title="[运气中]"></li>
											<li><img src="/bs/images/youqian_thumb.gif" title="[有钱]"></li>
											<li><img src="/bs/images/cf_thumb.gif" title="[冲锋]"></li>
											<li><img src="/bs/images/camera_thumb.gif" title="[照相机]"></li>
										</ul>
											</div>
											<!--face end-->
										</div>
											</div>

							<div id="time1"></div>
							<!--msgCon begin-->
							<div class="msgCon">




							</div>

                </div>
            </div>
        </div>
    </div>
</div>

<!-- Modal -->
<div class="modal fade" id="commentModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">发布评论</h4>
            </div>
            <div class="modal-body">
            	<div id="show"></div>
				<div class="comment">
  					<div class="com_form">
    				<textarea class="input" id="saytext" name="saytext"></textarea>
    				<p>
      					<input type="button" class="sub_btn" value="提交">
      					<span class="emotion">表情</span>
      				</p>
  					</div>
				</div>
            </div>
            <div class="modal-footer" style="display:none;">
                
            </div>
        </div>
    </div>
</div>

</body>
<script src="/bs/js/bootstrap.js"></script>
<script src="/bs/js/goods.js"></script>
</html>




