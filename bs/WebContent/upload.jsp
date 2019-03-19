<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>上传文件</title>
<script type="text/javascript" src="${base}/js/jquery-1.7.2.js"></script>
<script type="text/javascript">
$(function(){
	$("#html4button").click(function(){
		$("html4").submit();
	});
});
</script>
</head>
<body>

<div>
	<form action="${base}/upload/html4" id="html4" method="post" enctype="multipart/form-data">
		<input type="file" name="f" multiple="multiple" ></input>
		<button id="html4button">提交</button>
	</form>
	
</div>

</body>
</html>