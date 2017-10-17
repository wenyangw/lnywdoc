<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
$(function(){
	$('#dateShow').html(moment().format('YYYY年MM月DD日'));
});
function editPassword(){
		var p = $('#top_editPasswordDialog');
		p.dialog({
			title : '修改密码',
			href : '${pageContext.request.contextPath}/admin/editPassword.jsp',
			width : 350,
			height : 200,
			buttons : [ {
				text : '确定',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/admin/userAction!editPassword.action',
						onSubmit:function(){		
							if($(this).form('validate')){
								var flag=true;
								$.ajax({
									url : '${pageContext.request.contextPath}/admin/userAction!checkPassword.action',
									async: false,
									data : {
										password: $('input[name=passwordOld]').val()
									},
									dataType : 'json',
									success : function(d) {
										
										if(!d.success){								
											flag=false;
										}								
									}								
								});	
								if(!flag){
			            			$.messager.alert('提示', '原密码不正确！', 'error');
			            		}
		            			return flag;
							}else{
            					return false;
            				}
						},
						success : function(d) {
							var json = $.parseJSON(d);
							if (json.success) {
								p.dialog('close');
							}
							$.messager.show({
								msg : json.msg,
								title : '提示'
							});
						}
					});
				}
			} ],
			
		});

}
</script>
<style type="text/css">
#dateShow {padding: 0px;margin: 0px;text-align:right;}
 .logo {	padding: 0px;margin: 0px; 
			background-image:url('${pageContext.request.contextPath}/images/top.jpg');
			background-repeat:no-repeat;
		} 
</style>
 <div class="logo">
	<div id="dateShow"></div>			 
	<div id="menubutton" align="right">
	 	欢迎您：${user.realName },  </font><br>
		<a href="#" class="easyui-menubutton" 
			data-options="menu:'#control_panel'">功能设置</a>
	</div>
	<div id="control_panel" style="width:150px;">
	<div><a  href="#" class="easyui-linkbutton"
			data-options="plain:true"
			onClick="editPassword()">修改密码</a>
	</div>
	<div ><a href="${pageContext.request.contextPath}/logout.action"  class="easyui-linkbutton"
			data-options="plain:true">退出系统</a></div>       
    </div>
</div>
 <div id='top_editPasswordDialog'></div>
 
