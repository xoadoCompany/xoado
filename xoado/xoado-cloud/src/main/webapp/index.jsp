<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
<form action="/files/base64" method="post" enctype="multipart/form-data">
		<input type="text" name="base64text">   base64以及name<br>
		<input type="submit" value="增加图片">
	</form>
	<form action="/files/stream" method="post" enctype="multipart/form-data">
		图片名称<input type="file" name="attachmentpicture"><br>
		图片名称<input type="file" name="attachmentpicture"><br>
		<input type="submit" value="增加">
	</form>
	-----------------------------------------------------------------------------------------------
	<form action="files/base64" method="post" enctype="multipart/form-data">
		<input type="text" name="base64text">   base64以及name<br>
		<input type="submit" value="增加图片">
	</form>
	<form action="files/stream" method="post" enctype="multipart/form-data">
		图片名称<input type="file" name="attachmentpicture"><br>
		图片名称<input type="file" name="attachmentpicture"><br>
		<input type="submit" value="增加">
	</form>
	扫描图片
		<form action="/tools/scanner" method="post" enctype="multipart/form-data">
		图片名称<input type="text" name="base64Body"><br>
		<input type="submit" value="增加">
	</form>
	
</body>
</html>