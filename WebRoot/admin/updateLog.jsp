<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<jsp:include page="/common/inc.jsp"></jsp:include>
<script type="text/javascript">
//页面数据加载
$(function(){
	//初始化页面信息
	$.ajax({
		url : '${pageContext.request.contextPath}/admin/updateLogAction!listUpdateLog.action',
		dataType : 'json',
		success : function(data) {
			$.each(data,function(){	
				var b = $("<li>"+this.updateDate+"&nbsp"+this.contents+"</li>").appendTo($('#text'));
			});	
		}
	});
});	

</script>
<div id='text'></div>

 



	
