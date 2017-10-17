<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<div align="center">
	<form id="doc_levelAdd_form" method="post">
		<table>
		<tr>
			<th><label for="levelName">名称</label></th>
			<td><input name="levelName" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请填写名称'"
				style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="orderNum">显示顺序</label></th>
			<td><input name="orderNum" class="easyui-numberspinner" style="width:160px;"
        		required="required" data-options="value:1,min:1,max:200,editable:true"></td>
		</tr>
		<tr>
			<th><label for="dir">路径</label></th>
			<td><input name="dir" style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="bz">备注</label></th>
			<td><input name="bz" style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="doc_level_cat">分类</label></th>
			<td><input id="doc_level_cat" name="catId" style="width: 160px;"></td>
		</tr>
		<tr>
			<th><label for="doc_level_pid">上级类别</label></th>
			<td><input id="doc_level_pid" name="pid" style="width: 160px;"></td>
		</tr>
		</table>
		<input name="id" type="hidden">
	</form>
</div>
