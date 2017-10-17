<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<div align="center">
	<div class="doc_img_top">
		<form method="post">
			<table>
				<tr>
					<th><span>备注</span></th><td><input type="text" id="doc_img_bz" name="bz" size="40"/></td>
				</tr>
				<tr>
					<th><span>关键字</span></th><td><input type="text" id="doc_img_crux" name="crux" size="40"/></td>
				</tr>
				<tr>
					<th><span>排序</span></th><td><input type="text" id="doc_img_orderNum" name="orderNum" size="10"/></td>
				</tr>
			</table>
			<input type="hidden" id="doc_img_id" name="id"/>
		</form>
		<input type="button" onclick="saveImg()" value="保存属性" />&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" onclick="deleteImg()" value="删除图片"/>
		<input type="button" onclick="printImg()" value="打印图片"/>
		<hr/>
	</div>
	<div id='doc_img' style="margin-top: 20px;"></div>
</div>

