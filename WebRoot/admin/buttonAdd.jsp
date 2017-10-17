<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<div align="center">
	<form id="admin_buttonAdd_form" method="post">
	<table>
		<tr>
			<th><label for="text">功能名称</label></th>
			<td><input name="text" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请填写功能名称'"
				style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="handler">地址</label></th>
			<td><input name="handler" style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="orderNum">显示顺序</label></th>
			<td><input name="orderNum" class="easyui-numberspinner" style="width:160px;"
        		required="required" data-options="value:1,min:1,max:20,editable:false"></td>
		</tr>
		<tr>
			<th><label for="iconCls">图标</label></th>
			<td><input name="iconCls" style="width: 160px;"></td>
		</tr>
		<tr>
			<th><label for="mid">所属菜单</label></th>
			<td><input name="mid" style="width: 160px;"></td>
		</tr>
		<tr>
			<th><label for="tabId">所属标签</label></th>
			<td><input name="tabId" class="easyui-numberspinner" style="width:160px;"
        		required="required" data-options="value:0,min:0,max:10,editable:false"></td>
		</tr>
	</table>
		<input name="id" type="hidden">
	</form>
</div>
