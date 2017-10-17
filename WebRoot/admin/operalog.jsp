<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var operalog_dg;
var did;
//页面加载
$(function(){
	did = lnyw.tab_options().did;
	operalog_dg = $('#operalog_gl_datagrid');
	operalog_dg.datagrid({
		url:'${pageContext.request.contextPath}/admin/operalogAction!datagrid.action',
		fit : true,
	    border : false,
	    pagination : true,
		pagePosition : 'bottom',		
		pageSize : pageSize,
		pageList : pageList,
	    columns:[[    	
	        {field:'id',title:'编号',width:100, checkbox : true},
	        {field:'realName',title:'姓名',width:100},
	        {field:'bmmc',title:'所属部门',width:100},
	        {field:'menuName',title:'操作菜单',width:100},
	        {field:'keyId',title:'关键字',width:100},
	        {field:'operation',title:'具体操作',width:250},
	        {field:'logTime',title:'操作时间',width:130},
	    ]],
	     //将查询条件固定在datagrid上
	    toolbar:'#admin_operalog_tb',
	});	
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, operalog_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
});
//删除信息
function removeOperalog(){
	var rows=operalog_dg.datagrid('getSelections');
	var ids = [];
	if(rows.length>0){
		 $.messager.confirm('请确认','您要删除当前选择项目？',function(r){
			if(r){
				for ( var i = 0; i < rows.length; i++) {
					ids.push(rows[i].id);
				}
				$.ajax({
					url :'${pageContext.request.contextPath}/admin/operalogAction!delete.action',
					data: {
						ids : ids.join(','),
					},
					dataType:'json',
					success:function(d){
						operalog_dg.datagrid('load');
						operalog_dg.datagrid('unselectAll');
						$.messager.show({
							title:'提示',
							msg: d.msg,
						});					
					}
				});
			} 
		 });
	 }else{
		$.messager.alert('警告', '请选择一条记录进行删除！',  'warning');	 
	 }
}
//筛选信息
function searchOperalog(){
	operalog_dg.datagrid('load',{
		//筛选时间
		logTime: $('input[name=logTime]').val(),
		logTimeEnd: $('input[name=logTimeEnd]').val(),
	});
}
</script>
<div id="admin_operalog_selet" class="easyui-layout" data-options="fit:true">			
	<div data-options="region:'center'">
		<div id="admin_operalog_tb" style="padding:3px;height:auto">
			请输入查询日期范围:<input type="text" name="logTime" class="easyui-datebox" 
							data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
			——<input type="text" name="logTimeEnd" 
				class="easyui-datebox" data-options="value: moment().format('YYYY-MM-DD')" style="width:100px">
			<a href="#" class="easyui-linkbutton" 
				data-options="iconCls:'icon-search',plain:true" onclick="searchOperalog();">查询</a>
		</div>
		<div id="operalog_gl_datagrid"></div>
	</div>
</div>

