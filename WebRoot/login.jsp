<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/common/inc.jsp"></jsp:include>

<script type="text/javascript">
$(function() {
	var name = "";
	var password = "";

	$('input[name=userName]').focus();
	$('#login_loginBtn').bind('click', function() {
		//将获取的账户和密码分别赋值name，password ；便于比较用户密码是否为空；
			name = $('input[name=userName]').val();
			password = $('input[name=password]').val();
			if (name != undefined && password != undefined) {
				$('#login_loginForm').submit();
				return true;
			} else {
				return false;
			}
		}
	);

	$('input').bind('keyup', function(event) {/* 增加回车提交功能 */
		if (event.keyCode == '13') {
			name = $('input[name=userName]').val();
			password = $('input[name=password]').val();
			if (name != "" && password != "") {
				$('#login_loginForm').submit();
				return true;
	 		}else{
	 			return false;
	 		}
		}
	});
	
});
</script>
<style type="text/css">
body{background-image:url('${pageContext.request.contextPath}/images/login.jpg');}
</style>

</head>
<body>
	<div id="login_loginWindow" class="easyui-window"
		data-options="
			title : '欢迎使用辽宁印刷物资有限责任公司信息管理平台', 
			modal : true,
			closable : false,
			minimizable : false,
			maximizable : false,
			resizable : false,
			collapsible : false"
			style="width: 350px; height: 230px; overflow: hidden;">
			<div class="easyui-tabs" data-options="fit:true,border:false">
				<div title="用户登录" style="overflow: hidden;">
					<div class="info">
						<div class="tip icon-tip"></div>
						<div>
							请输入用户帐号/密码
						</div>
					</div>
					<div align="center">
						<form id="login_loginForm" method="post"
							action="${pageContext.request.contextPath}/login.action">
							<div style="padding: 5px 0;">
								<label for="login">
									帐号:
								</label>
								<input type="text" name="userName" class="easyui-validatebox"
									data-options="required:true,missingMessage:'请填写登录账号'"
									style="width: 160px;"></input>
							</div>
							<div style="padding: 5px 0;">
								<label for="password">
									密码:
								</label>
								<input type="password" name="password"
									class="easyui-validatebox"
									data-options="required:true,missingMessage:'请填写登录密码'"
									style="width: 160px;"></input>
							</div>
							<div style="padding: 5px 0; text-align: center; color: red;"
								id="showMsg">
								${msg}
							</div>
							<div style="padding: 5px 0;">
								<a id="login_loginBtn" href="#" class="easyui-linkbutton"
									data-options="iconCls:'icon-ok'">登录</a>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
