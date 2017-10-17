<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<div align="center">
	<form id="doc_entryAdd_form" method="post">
		<table>
		<tr>
			<th><label for="entryName">名称</label></th>
			<td><input name="entryName" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请填写名称'"
				style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="recordTime">建档时间</label></th>
			<td><input name="recordTime" class="easyui-datebox" data-options="value: moment().format('YYYY-MM-DD')" style="width:156px"></td>
		</tr>
		<tr>
			<th><label for="orderNum">显示顺序</label></th>
			<td><input name="orderNum" class="easyui-numberspinner" style="width:160px;"
        		required="required" data-options="value:1,min:1,max:200,editable:true"></td>
		</tr>
		<tr>
			<th><label for="dir">路径</label></th>
			<td><input id="doc_entry_dir" name="dir" style="width: 156px;"></td>
		</tr>
		<tr class="entry_document" style="display: none">
			<th><label for="owner">责任者</label></th>
			<td><input name="owner" style="width: 156px;"></td>
		</tr>
		<tr class="entry_document"  style="display: none">
			<th><label for="fileno">文号</label></th>
			<td><input name="fileno" style="width: 156px;"></td>
		</tr>
		<tr class="entry_document"  style="display: none">
			<th><label for="sort">类型</label></th>
			<td><input name="sort" style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="bz">备注</label></th>
			<td><input name="bz" style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="crux">关键字</label></th>
			<td><input name="crux" style="width: 156px;"></td>
		</tr>

		</table>
		<input type="hidden" name="id"  >

	</form>
</div>
