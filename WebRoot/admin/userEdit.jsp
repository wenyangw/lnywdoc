<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<div align="center">
	<form id="admin_userEdit_form" method="post">
		<table>
		<tr>
			<th><label for="userName">登录名</label></th>
			<td><input name="userName" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请填写登录名'"
				style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="password">密码</label></th>
			<td><input name="password" type="password" class="easyui-validatebox"
				style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="realName">姓名</label></th>
			<td><input name="realName" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请填写姓名'"
				style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="sex">性别</label></th>
			<td><input name="sex" style="width: 160px;"></td>
		</tr>
		<tr>
			<th><label for="orderNum">显示顺序</label></th>
			<td><input name="orderNum" class="easyui-numberspinner" style="width:160px;"
        		required="required" data-options="value:1,min:1,max:20,editable:false"></td>
		</tr>
		<tr>
			<th><label for="did">部门</label></th>
			<td><input name="did" style="width: 160px;"></td>
		</tr>
		<tr>
			<th><label for="postId">职务</label></th>
			<td><input name="postId" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请选择职务'" style="width: 160px;"></td>
		</tr>
		<tr>
			<th><label for="isYwy">是否业务员</label></th>
			<td><input name="isYwy" type="checkbox" value="1"></td>
		</tr>
		<tr>
			<th><label for="isBgy">是否保管员</label></th>
			<td><input name="isBgy" type="checkbox" value="1"></td>
		</tr>
		<tr>
			<th><label for="roleIds">拥有角色</label></th>
			<td><input name="roleIds" style="width: 160px;"></td>
		</tr>
		</table>
		<input name="id" type="hidden">
		<input name="createTime" type="hidden">
		<input name="lastTime" type="hidden">
		<input name="menuId" type="hidden">
		<input name="operaDepId" type="hidden">
	</form>
</div>
