<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<div align="center">
	<form id="admin_roleAdd_form" method="post">
	<table>
		<tr>
			<th><label for="roleName">角色名称</label></th>
			<td><input name="roleName" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请填写角色名称'"
				style="width: 156px;"></td>
			<td></td>
		</tr>
		<tr>
			<th><label for="description">描述</label></th>
			<td><input name="description" style="width: 156px;"></td>
			<td></td>
		</tr>
		<tr>
			<th><label for="did">关联部门</label></th>
			<td>
				<input id="admin_role_depId" name="did" style="width: 160px;">
				<a id="clearBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true"></a>
			</td>
		</tr>
	</table>
		<input name="id" type="hidden">
	</form>
</div>
