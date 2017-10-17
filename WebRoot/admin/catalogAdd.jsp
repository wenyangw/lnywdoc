<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<div align="center">
	<form id="admin_catalogAdd_form" method="post">
	<table>
		<tr>
			<th><label for="catName">模块名称</label></th>
			<td><input name="catName" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请填写模块名称'"
				style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="catName">显示顺序</label></th>
			<td><input name="orderNum" class="easyui-numberspinner" style="width:160px;"
        		required="required" data-options="value:1,min:1,max:20,editable:false"></td>
		</tr>
		<tr>
			<th><label for="iconCls">图标</label></th>
			<td><input name="iconCls" style="width: 160px;"></td>
		</tr>
	</table>
		<input name="id" type="hidden">
	</form>
</div>
