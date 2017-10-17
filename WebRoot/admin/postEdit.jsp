<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<script type="text/javascript">
</script>
<div align="center">
	<form id="admin_postEdit_form" method="post">
	<table>
		<tr>
			<th><label for="id">编号</label></th>
			<td><input name="id"readonly="readonly" style="width: 220px;"></td>
		</tr>
		<tr>
			<th><label for="postName">职务名称</label></th>
			<td><input name="postName" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请填写职务名称'"
				style="width: 220px;"></td>
		</tr>
		<tr>
			<th><label for="orderNum">排序</label></th>
			<td><input name="orderNum" class="easyui-numberspinner"
				data-options="required:true, value:1,min:1,max:50,editable:false"></td>
		</tr>
		
	</table>
		
	</form>
</div>
