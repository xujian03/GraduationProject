<%@page import="com.alipay.api.internal.util.json.JSONReader"%>
<%@ page import="net.xj.nutz.bean.*"%>
<%@page import="com.alipay.api.DefaultAlipayClient"%>
<%@page import="com.alipay.api.request.AlipayTradeCancelRequest"%>
<%@page import="com.alipay.api.AlipayClient"%>
<%@page import="com.alipay.api.response.AlipayTradeCancelResponse"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

</head>
<body>
<%
String out_trade_no = new String(request.getParameter("WIDRQout_trade_no").getBytes("ISO-8859-1"),"UTF-8");
out.print(out_trade_no);
AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl,AlipayConfig.app_id,AlipayConfig.merchant_private_key,"json",AlipayConfig.charset,AlipayConfig.alipay_public_key,AlipayConfig.sign_type);
AlipayTradeCancelRequest request1 = new AlipayTradeCancelRequest();
request1.setBizContent("{" +
"\"out_trade_no\":\""+out_trade_no+"\"," +
"\"trade_no\":\"\"" +
"  }");
AlipayTradeCancelResponse response1 = alipayClient.execute(request1);
if(response1.isSuccess()){
System.out.println("调用成功");
} else {
System.out.println("调用失败");
}

%>
<script type="text/javascript">
	
</script>
</body>
</html>