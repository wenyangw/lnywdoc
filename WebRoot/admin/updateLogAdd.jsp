<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<script type="text/javascript">
</script>
<div align="center">
	<form id="admin_updateLogAdd_form" method="post">
	<table>
		<tr>
			<th><label for="updateDate">前台显示时间</label></th>
			<td><input type="text" name="updateDate" class="easyui-datebox" 
				data-options="value: moment().format('YYYY-MM-DD')" style="width:106px">
			</td>
		</tr>
		<tr>
			<th><label for="contents">更新内容</label></th>
			<td><textarea name="contents" style="height:200px;width:200px"></textarea></td>
		</tr>
	</table>
	</form>
</div>