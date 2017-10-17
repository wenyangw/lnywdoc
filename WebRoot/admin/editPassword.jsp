<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<div align="center">
	<form id="admin_editPassword_form" method="post">
		<table>
		<tr>
			<th><label for="passwordOld">请输入原密码</label></th>
			<td><input name="passwordOld" type="password" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请填写密码'"
				style="width: 156px;"></td>
		</tr>		
		<tr>
			<th><label for="password">请输入新密码</label></th>
			<td><input id="password" name="password" type="password" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请填写密码'"
				style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="password_agin">确认密码</label></th>
			<td><input name="passwordAgain" type="password" class="easyui-validatebox"
				data-options="validType:'same[\'password\']'"
				style="width: 156px;" ></td>
		</tr>
		</table>
	</form>
</div>
