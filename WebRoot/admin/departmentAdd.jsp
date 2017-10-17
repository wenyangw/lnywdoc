<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<div align="center">
	<form id="admin_departmentAdd_form" method="post">
	<table>
		<tr>
			<th><label for="id">编号</label></th>
			<td><input name="id" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请填写部门编号',
					validType:['mustLength[2]',]"
				style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="depName">部门名称</label></th>
			<td><input name="depName" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请填写部门名称'"
				style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="orderNum">显示顺序</label></th>
			<td><input name="orderNum" class="easyui-numberspinner" style="width:160px;"
        		required="required" data-options="value:1,min:1,max:20,editable:false"></td>
		</tr>
	</table>
	</form>
</div>
