<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="js/jquery-1.7.2.js"></script>
</head>
<body>
<form id="infoLogoForm" enctype='multipart/form-data'> 
        <input type="file" id="ctn-input-file" name="file" multiple="multiple" style="height:40px">
        <div>
            <button id="button" type="button">保存</button>
        </div>
</form>
<script>

$("#button").click(function(){
	var files=$("#ctn-input-file").prop('files');
		console.log(files);
		var data=new FormData();
		for(var i=0;i<files.length;i++){
			data.append("f",files[i]);
		}
		upload(data);
		console.log(data)
	
	
	function upload(data){
		$.ajax({
	        url: "upload/html4",
	        type: 'POST',
	        cache: false,
	        data:data,
	        processData: false,
	        contentType: false,
	        success : function(data) {
					alert(data)
	        }
	    });		
	}
    
});
</script>
</body>
</html>