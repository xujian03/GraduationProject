package net.xj.nutz.module;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.jasper.tagplugins.jstl.core.Out;
import org.json.JSONArray;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.view.JspView;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;
import org.w3c.dom.ls.LSInput;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.ndktools.javamd5.Mademd5;
import com.sun.org.apache.regexp.internal.REUtil;

import net.xj.nutz.bean.AlipayConfig;
import net.xj.nutz.bean.Result;
import net.xj.nutz.bean.Tb_address;
import net.xj.nutz.bean.Tb_goods;
import net.xj.nutz.bean.Tb_gphoto;
import net.xj.nutz.bean.Tb_order;
import net.xj.nutz.bean.Tb_user;

@At("/order")
@IocBean
public class OrderModule {
	@Inject
	private Dao dao;
	
	private static final Log log=Logs.get();
	
	/**
	 * 生成订单 status=-1时最好刷新页面
	 * @param orderOutTradeNo
	 * @param addressId
	 * @param session
	 * @return
	 */
	@At("/creatorder")
	@POST
	public Object creatOrder(@Param("orderOutTradeNo")String orderOutTradeNo,
							 @Param("addressId")long addressId,
							 HttpSession session 
							 ){
		Result result =new Result();
		if(isEmpty(orderOutTradeNo)){//订单编号判空
			result.setInfo("订单不能为空");
			result.setStatus(0);
			return result;
		}
		Tb_order order=dao.fetch(Tb_order.class,Cnd.where("orderOutTradeNo","=",orderOutTradeNo));//判断是否已经有这个订单号了
		if(order!=null){
			result.setInfo("该订单号已存在");
			result.setStatus(-1);
			return result;
		}
		if(addressId<0){//地址id判断
			result.setInfo("地址信息错误");
			result.setStatus(0);
			return result;
		}
		Tb_user user=(Tb_user)session.getAttribute("user");
		if(user==null){//用户是否登陆判断
			result.setInfo("请先登录");
			result.setStatus(0);
			return result;
		}
		List<Tb_goods> list=(List<Tb_goods>) session.getAttribute("shoppingcart");
		if(list==null||list.size()==0){
			result.setInfo("无商品在购物车中请先去购物");
			result.setStatus(0);
			return result;
		}
		final List<Tb_goods> listReal=new ArrayList<Tb_goods>();//商品列表
		for(int i=0;i<list.size();i++){
			Tb_goods goods=dao.fetch(Tb_goods.class,list.get(i).getGoodsId());
			if(user.getUserId()==goods.getUserId()){
				list.remove(i);
				result.setInfo("不能购买自己发布的商品！");
				result.setStatus(0);
				return result;
			}
			if(goods.getGoodsStatus()!=1){
				result.setInfo("有商品已经被购买，请从购物车中去除！");
				result.setStatus(-1);
				return result;
			}
			if(goods.getUserId()==-1){
				result.setInfo("有商品被货主删除了！");
				list.remove(i);
				result.setStatus(-1);
				return result;
			}
			listReal.add(goods);
		}
		float money=0;//订单价格
		for(int i=0;i<listReal.size();i++){
			money=money+listReal.get(i).getGoodsPrice();
			listReal.get(i).setGoodsStatus(2);
			listReal.get(i).setOrderOutTradeNo(orderOutTradeNo);
			listReal.get(i).setOrderStatus(0);
		}
		if(money<=0){
			result.setInfo("订单价格不能小于等于0！");
			result.setStatus(-1);
			return result;
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=df.format(new Date());//当前时间
		Tb_address address=dao.fetch(Tb_address.class,Cnd.where("addressId","=",addressId).and("userId","=",user.getUserId()));
		if(address==null){
			result.setInfo("找不到该地址！");
			result.setStatus(0);
			return result;
		}
		Trans.exec(new Atom(){//锁
		    public void run(){
		    	dao.update(listReal);//更新商品状态
		    }
		  });
		
		order=new Tb_order();
		order.setOrderOutTradeNo(orderOutTradeNo);
		order.setUserId(user.getUserId());
		order.setOrderStatus(0);
		order.setOrderCountMoney(money);
		order.setOrderTime(time);
		order.setAddressPhone(address.getAddressPhone());
		order.setAddressMsg(address.getAddressMsg());
		order.setAddressName(address.getAddressName());
		dao.fastInsert(order);
		result.setInfo("订单成功生成！");
		result.setStatus(1);
		session.setAttribute("shoppingcart",null);
		return result;
	}
	
	/**
	 * 所有商品未发货的情况下取消订单
	 * @param outTradeNo 订单号
	 * @param session
	 * @return  status=1 取消成功  status=0 用户未登录  status=-1订单不存在 status=-2 有商品已发货无法取消订单
	 */
	@At("/cancelorder")
	@Ok("json")
	public Object cancelOrder(@Param("orderOutTradeNo")String outTradeNo,HttpSession session) {
		Result result=new Result();
		Tb_user user=(Tb_user)session.getAttribute("user");
		if(user==null){result.setInfo("用户未登录！");result.setStatus(0);return result;}//用户登录检查
		if(outTradeNo==null){result.setInfo("订单不存在！");result.setStatus(-1);return result;}//订单号是否存入
		final Tb_order order=dao.fetch(Tb_order.class,Cnd.where("orderOutTradeNo","=",outTradeNo).and("userId","=",user.getUserId()).and("orderStatus","=",1));
		if(order==null){result.setInfo("订单不存在！");result.setStatus(-1);return result;}//订单号是否存在
		final List<Tb_goods> listGoods=dao.query(Tb_goods.class, Cnd.where("orderOutTradeNo","=",order.getOrderOutTradeNo()));//获取订单的商品
		if(listGoods==null){result.setInfo("商品不存在！");result.setStatus(-1);return result;}
		Boolean isCouldChange=true;//是否可以取消订单
		for(int i=0;i<listGoods.size();i++){
			if(listGoods.get(i).getGoodsStatus()!=3){
				isCouldChange=false;
			}
			listGoods.get(i).setOrderOutTradeNo("");
			listGoods.get(i).setOrderStatus(0);
			listGoods.get(i).setGoodsStatus(1);
		}
		if(isCouldChange){//订单可以取消
			if(order.getOrderTradeNo()!=null&&!order.getOrderTradeNo().equals("")){//支付宝退款
				if(order.getOrderCountMoney()==order.getOrderTotalAmount()){
					String code=refundOrder(order.getOrderOutTradeNo(),Float.toString(order.getOrderTotalAmount()));
				result.setObj("退款到支付宝");
				}else{
					result.setInfo("对不起，");result.setObj("无法退款到支付宝(有商品退货了)");result.setStatus(-2);return result;
				}
				
			}else{//退款到余额
				final float tuiKuan=order.getOrderCountMoney();
				final long userId=user.getUserId();
				Trans.exec(new Atom(){//锁
				    public void run(){
				    	dao.update(Tb_user.class,Chain.makeSpecial("userMoney", "+"+tuiKuan),Cnd.where("userId","=",userId));
				    }
				  });
				
				result.setObj("退款到余额");
			}
			
			Trans.exec(new Atom(){//锁
			    public void run(){
			    	dao.update(listGoods);
			    	order.setOrderStatus(-1);
			    	dao.update(order);
			    }
			  });
			result.setInfo("订单取消成功！");result.setStatus(1);return result;
		}else{
			result.setInfo("商品已经发货，无法取消订单！");result.setObj("");result.setStatus(-2);return result;
		}

	}
	
	/**
	 * refund.query.jsp重写
	 * 退款
	 */
	public String refundOrder(String outTradeNo,String money) {
		//获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
		
		//设置请求参数
		AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
		
		//商户订单号，商户网站订单系统中唯一订单号
		String out_trade_no = outTradeNo;
		//支付宝交易号
		//String trade_no = new String(request.getParameter("WIDTRtrade_no").getBytes("ISO-8859-1"),"UTF-8");
		//请二选一设置
		//需要退款的金额，该金额不能大于订单金额，必填
		String refund_amount = money;
		//退款的原因说明
		String refund_reason = "退款";
		//标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
		//String out_request_no = new String(request.getParameter("WIDTRout_request_no").getBytes("ISO-8859-1"),"UTF-8");
		
		alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
				+ "\"trade_no\":\""+ "" +"\"," 
				+ "\"refund_amount\":\""+ refund_amount +"\"," 
				+ "\"refund_reason\":\""+ refund_reason +"\"," 
				+ "\"out_request_no\":\""+ "" +"\"}");
		
		//请求
		String result = null;
		try {
			result = alipayClient.execute(alipayRequest).getBody();
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//输出
		System.out.println("["+result+"]");
		
		JSONArray jsonArray = null;
        jsonArray = new JSONArray("["+result+"]");
        System.out.println(jsonArray.getJSONObject(0).get("alipay_trade_refund_response"));
        String str1=jsonArray.getJSONObject(0).get("alipay_trade_refund_response").toString();
        jsonArray=new JSONArray("["+str1+"]");
        String code= jsonArray.getJSONObject(0).get("code").toString();
        return code;
	}
	
	/**
	 * notify_url.jsp用框架重写
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@At("/notify")
	public String notifyUrl(HttpServletRequest  request) throws Exception{
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		
		boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

		//——请在这里编写您的程序（以下代码仅作参考）——
		
		/* 实际验证过程建议商户务必添加以下校验：
		1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
		2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
		3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
		4、验证app_id是否为该商户本身。
		*/
		if(signVerified) {//验证成功
			//商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		
			//支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
		
			//交易状态
			String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
			
			final Tb_order order=dao.fetch(Tb_order.class,Cnd.where("orderOutTradeNo","=",out_trade_no));
			if(order==null){log.debug("订单不存在："+out_trade_no+"-fail");canCelOrder(out_trade_no);return "fail";}
			if(order.getOrderStatus()!=0){log.debug("订单状态错误："+out_trade_no+"-fail");canCelOrder(out_trade_no);return "fail";}
			if(trade_status.equals("TRADE_FINISHED")){
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				//如果有做过处理，不执行商户的业务程序
				log.debug("-------+支付失败");
				//注意：
				//退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
			}else if (trade_status.equals("TRADE_SUCCESS")){
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				//如果有做过处理，不执行商户的业务程序
				
				log.debug("-------+支付成功");
				//注意：
				//付款完成后，支付宝系统发送该交易状态通知
				if(order.getOrderStatus()==0){
					String total_amount=tradeQuery(out_trade_no);
					log.debug("订单"+out_trade_no+"付款"+total_amount+"元");
					if(!total_amount.equals("0")){
						order.setOrderTradeNo(trade_no);
						order.setOrderTotalAmount(Float.parseFloat(total_amount));
						order.setOrderStatus(1);
						
						Trans.exec(new Atom(){//锁
						    public void run(){
						    	dao.update(order);
						    }
						  });
						final List<Tb_goods> list=dao.query(Tb_goods.class, Cnd.where("orderOutTradeNo","=",out_trade_no));
						for(int i=0;i<list.size();i++){
							list.get(i).setGoodsStatus(3);
							list.get(i).setOrderStatus(1);
						}
						Trans.exec(new Atom(){//锁
						    public void run(){
						    	dao.update(list);
						    }
						  });
						
					}
				}
			}
			log.debug("订单支付成功："+out_trade_no+"-success");
			return "success";
			
		}else {//验证失败
			log.debug("订单验证失败："+"-fail");
			return "fail";
		
			//调试用，写文本函数记录程序运行情况是否正常
			//String sWord = AlipaySignature.getSignCheckContentV1(params);
			//AlipayConfig.logResult(sWord);
		}
	} 
	
	/**
	 * 订单取消，不在网站中映射位置
	 * @param out_trade_no
	 * @throws AlipayApiException
	 */
	public void canCelOrder(String out_trade_no) throws AlipayApiException {
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl,AlipayConfig.app_id,AlipayConfig.merchant_private_key,"json",AlipayConfig.charset,AlipayConfig.alipay_public_key,AlipayConfig.sign_type);
		AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
		request.setBizContent("{" +
		"\"out_trade_no\":\""+out_trade_no+"\"," +
		"\"trade_no\":\"\"" +
		"  }");
		AlipayTradeCancelResponse response = alipayClient.execute(request);
		if(response.isSuccess()){
		System.out.println("调用成功");
		} else {
		System.out.println("调用失败");
		}
	}
	/**
	 * 订单查询
	 * @throws AlipayApiException 
	 */
	public String tradeQuery(String outTradeNo) throws AlipayApiException{
		//获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
		
		//设置请求参数
		AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();
		
		//商户订单号，商户网站订单系统中唯一订单号
		String out_trade_no = outTradeNo;
		//支付宝交易号
		//String trade_no = new String(request.getParameter("WIDTQtrade_no").getBytes("ISO-8859-1"),"UTF-8");
		//请二选一设置
		
		alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","+"\"trade_no\":\""+ "" +"\"}");
		
		//请求
		String result = alipayClient.execute(alipayRequest).getBody();
		JSONArray jsonArray = null;
		jsonArray = new JSONArray("["+result+"]");
        String str1=jsonArray.getJSONObject(0).get("alipay_trade_query_response").toString();
        jsonArray=new JSONArray("["+str1+"]");
		String code=jsonArray.getJSONObject(0).get("code").toString();
		if(code.equals("10000")){
			String count=jsonArray.getJSONObject(0).get("total_amount").toString();
			return count;
		}
		return "0";
	}
	
	/**
	 * return_url.jsp重写
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@At("/return")
	@Ok("raw:html")
	public Object returnUrl(HttpServletRequest request) throws Exception{
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		
		boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

		//——请在这里编写您的程序（以下代码仅作参考）——
		if(signVerified) {
			//商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		
			//支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
		
			//付款金额
			String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");
			log.debug("外部单号："+out_trade_no+"支付宝单号："+trade_no+"金额"+total_amount);
			final Tb_order order=dao.fetch(Tb_order.class,Cnd.where("orderOutTradeNo","=",out_trade_no));
			if(order.getOrderTotalAmount()!=0){
				return "已经支付成功<a href='/bs/user.html?tabid=A_dingdan'>查看订单</a>";
			}
			
			order.setOrderTotalAmount(Float.valueOf(total_amount));//订单设置返回金额
			order.setOrderStatus(1);
			order.setOrderTradeNo(trade_no);
			if(order.getOrderCountMoney()!=order.getOrderTotalAmount()){//应付金额和实付金额不等
				order.setOrderStatus(-1);
				
				Trans.exec(new Atom(){//锁
				    public void run(){
				    	dao.update(order);
				    }
				  });
				return "支付失败  交易号："+trade_no;
			}
			if(order.getOrderStatus()==1){//支付成功
				final List<Tb_goods> list=dao.query(Tb_goods.class, Cnd.where("orderOutTradeNo","=",order.getOrderOutTradeNo()));
				for(int i=0;i<list.size();i++){
					list.get(i).setOrderOutTradeNo(order.getOrderOutTradeNo());
					list.get(i).setOrderStatus(1);
					list.get(i).setGoodsStatus(3);//付款成功
				}
				
				Trans.exec(new Atom(){//锁
				    public void run(){
				    	dao.update(list);
				    }
				  });
			}
			
			Trans.exec(new Atom(){//锁
			    public void run(){
			    	dao.update(order);
			    }
			  });
			return("支付成功！<a href='/bs/user.html?tabid=A_dingdan'>查看订单</a>");
		}else {
			return("验签失败<a href='/bs'>返回首页</a>");
		}
	}
	
	/**
	 * 支付接口  提供session即可
	 * @param session
	 * @param orderNo
	 * @return
	 * @throws AlipayApiException
	 */
	@At("/pay")
	@Ok("raw:html")
	public String pay(HttpSession session,
					   @Param("orderNo")String orderNo) throws AlipayApiException{
		Tb_user user=(Tb_user)session.getAttribute("user");
		if(user==null){
			return "<h3>请先登录！</h3><script>setTimeout(window.location=\"/bs/login.html\",5000);</script>";
		}
		Tb_order order= dao.fetch(Tb_order.class, Cnd.where("orderOutTradeNo","=",orderNo).and("userId","=",user.getUserId()));
		if(order==null){
			return "<h3>未查询到订单！</h3><script>setTimeout(window.location=\"/bs/\",5000);</script>";
		}
		if(order.getOrderStatus()!=0){
			return "<h3>订单状态异常！</h3><script>setTimeout(window.location=\"/bs/\",5000);</script>";
		}
		//获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
		
		//设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(AlipayConfig.return_url);
		alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
		
		//商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no =orderNo;
		//付款金额，必填
		String total_amount =Float.toString(order.getOrderCountMoney());
		//订单名称，必填
		String subject = "易书网-支付";
		//商品描述，可空
		//String body = new String(request.getParameter("WIDbody").getBytes("ISO-8859-1"),"UTF-8");
		
		alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
				+ "\"total_amount\":\""+ total_amount +"\"," 
				+ "\"subject\":\""+ subject +"\"," 
				+ "\"body\":\""+ "" +"\"," 
				+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		
		//若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
		//alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
		//		+ "\"total_amount\":\""+ total_amount +"\"," 
		//		+ "\"subject\":\""+ subject +"\"," 
		//		+ "\"body\":\""+ body +"\"," 
		//		+ "\"timeout_express\":\"10m\"," 
		//		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		//请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节
		
		//请求
		String result = alipayClient.pageExecute(alipayRequest).getBody();
		
		//输出
		return result;
	}
	/**
	 * 使用余额支付
	 * @param orderNo
	 * @param password
	 * @param session
	 * @return
	 */
	@At("/paywithyue")
	@Ok("json")
	public Object payWithYuE(@Param("orderNo")String orderNo,
							 @Param("password")String password,
							 HttpSession session)
	{
		Result result=new Result();
		Tb_user user=(Tb_user)session.getAttribute("user");
		if(user==null){result.setInfo("未登录，支付失败!");result.setStatus(0);return result;}
		if(isEmpty(orderNo)||isEmpty(password)){result.setInfo("有数据为空，支付失败!");result.setStatus(-1);return result;}
		Mademd5 md = new Mademd5();
		password=md.toMd5(password); 
		final Tb_user userReal=dao.fetch(Tb_user.class,Cnd.where("userId","=",user.getUserId()).and("userPassWord","=",password));
		if(userReal==null){result.setInfo("密码有误，支付失败!");result.setStatus(-3);return result;}
		final Tb_order order=dao.fetch(Tb_order.class,Cnd.where("orderOutTradeNo","=",orderNo));
		if(order==null){result.setInfo("订单不存在，支付失败!");result.setStatus(-4);return result;}
		if(userReal.getUserMoney()<order.getOrderCountMoney()){result.setInfo("余额不足，支付失败!");result.setStatus(-5);return result;}
		userReal.setUserMoney(userReal.getUserMoney()-order.getOrderCountMoney());
		order.setOrderStatus(1);
		final List<Tb_goods> list=dao.query(Tb_goods.class, Cnd.where("orderOutTradeNo","=",order.getOrderOutTradeNo())) ;
		if(list!=null){
			for(Tb_goods goods : list) {
				  goods.setGoodsStatus(3);//货物已出售状态
				  goods.setOrderStatus(1);//订单已支付状态
				}
		}
		
		Trans.exec(new Atom(){//锁
		    public void run(){
		    	dao.update(userReal);
		    	dao.update(order);
		    	dao.update(list);
		    }
		  });
		user.setUserPassWord("");
		session.setAttribute("user",userReal);
		result.setInfo("支付成功!");
		result.setStatus(1);
		return result;
	}
	
	/**
	 * //获取某个订单no中包含的商品
	 * @param session
	 * @param outTradeNo
	 * @return
	 */
	@At("/getorderlist")
	public String getOrderList(HttpSession session,
								@Param("outTradeNo")String outTradeNo){
		String string=null;
		Tb_user user=(Tb_user)session.getAttribute("user");
		if(user==null){
			return string;
		}
		Tb_order order=dao.fetch(Tb_order.class,Cnd.where("orderOutTradeNo","=",outTradeNo).and("userId","=",user.getUserId()));
		if(order==null){
			return string;
		}
		List<Tb_goods> list=dao.query(Tb_goods.class, Cnd.where("orderOutTradeNo","=",order.getOrderOutTradeNo()));
		if(list!=null)
		{
			string="";
			for(int i=0;i<list.size();i++){
				string +=list.get(i).getGoodsName()+"</br>";
			}
		}
		return string;
	}
	
	@At("/readypay/*")//定向到选择支付方式界面
	@Ok("re")
	public Object showGoods(String Id,HttpSession session){
		if(Id==null){
			return ">>:/user.html";
		}
		Tb_user user=(Tb_user)session.getAttribute("user");
		if(user==null){
			return ">>:/login.html?returnUrl=/bs/order/readypay/"+Id;
		}
		log.debug("访问订单支付id="+Id);
		Tb_order order=dao.fetch(Tb_order.class, Cnd.where("orderOutTradeNo","=",Id).and("userId","=",user.getUserId()).and("orderStatus","=",0));
		log.debug(Json.toJson(order, JsonFormat.compact()));
		if(order==null){
			return ">>:/user.html";
		}else{
			return new JspView("../../readypay?order="+Json.toJson(order, JsonFormat.compact()));
		}
	}
	
	/**
	 * 是否为空
	 * @param a
	 * @return
	 */
	public Boolean isEmpty(String a) {
		if(a==null){
			return true;
		}
		a=a.trim();
		if(a.equals("")){
			return true;
		}
		return false;
	}
}
