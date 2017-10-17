<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
	
    
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <jsp:include page="/common/inc.jsp"></jsp:include>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false, href:'${pageContext.request.contextPath}/layout/top.jsp'" style="height:75px;background:#95CCD9;padding:10px">辽宁印刷物资有限责任公司进销存平台</div>
	<div data-options="region:'west',split:false,title:'功能导航', href:'${pageContext.request.contextPath}/layout/west.jsp'" style="width:200px;padding:0px;"></div>
	<div data-options="region:'east',split:true,collapsed:true,title:'提示信息', href:'${pageContext.request.contextPath}/layout/east.jsp'" style="width:250px;padding:10px;">信息</div>
	<div data-options="region:'south',border:false" style="height:50px;background:#95CCD9;padding:10px;" align="center">辽宁印刷物资有限责任公司<br>版权所有 &copy; 2014  All Rights Reserved</div>
	<div data-options="region:'center',title:'Center',href:'${pageContext.request.contextPath}/layout/center.jsp'"></div>
</body>
</html>