<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<div align="center">
	<form id="admin_menuAdd_form" method="post">
		<table>
		<tr>
			<th><label for="text">菜单名称</label></th>
			<td><input name="text" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请填写菜单名称'"
				style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="orderNum">显示顺序</label></th>
			<td><input name="orderNum" class="easyui-numberspinner" style="width:160px;"
        		required="required" data-options="value:1,min:1,max:200,editable:true"></td>
		</tr>
		<tr>
			<th><label for="iconCls">图标</label></th>
			<td><input name="iconCls" style="width: 160px;"></td>
		</tr>
		<tr>
			<th><label for="url">地址</label></th>
			<td><input name="url" style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="lx">单据类型</label></th>
			<td><input name="lx" style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="query">查询类型</label></th>
			<td><input name="query" style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="pid">上级菜单</label></th>
			<td><input name="pid" style="width: 160px;"></td>
		</tr>
		<tr>
			<th><label for="cid">所属模块</label></th>
			<td><input name="cid" style="width: 160px;"></td>
		</tr>
		</table>
		<input name="id" type="hidden">
	</form>
</div>
