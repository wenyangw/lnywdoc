<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var did;
var updateLog_dg;
$(function(){
	did = lnyw.tab_options().did;
	updateLog_dg = $('#updateLog_gl_datagrid');
	updateLog_dg.datagrid({
		url:'${pageContext.request.contextPath}/admin/updateLogAction!datagrid.action',
		fit : true,
	    border : false,
	    pagination : true,
		pagePosition : 'bottom',	
		pageSize : pageSize,
		pageList : pageList,
	    columns:[[    	
	        {field:'id',title:'Id',width:100, checkbox : true},
	        {field:'updateTime',title:'更新时间',width:130},
	        {field:'contents',title:'更新内容',width:100},
	        {field:'updateDate',title:'前台显示时间',width:100},	       
	        ]],
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, updateLog_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
});
//增加更新数据
function appendUpdateLog() {
	var p = $('#admin_updateLog_addDate');
	p.dialog({
		title : '增加更新数据',
		href : '${pageContext.request.contextPath}/admin/updateLogAdd.jsp',
		width : 440,
		height : 330,
		modal : true,
		buttons: [{
            text:'增加',
            iconCls:'icon-ok',
            handler:function(){
            	$('#admin_updateLogAdd_form').form('submit', {
					url : '${pageContext.request.contextPath}/admin/updateLogAction!add.action',
					success : function(d) {
						var json = $.parseJSON(d);
						if (json.success) {
							updateLog_dg.datagrid('appendRow', json.obj);
							p.dialog('close');
						}
						$.messager.show({
							title : "提示",
							msg : json.msg
						});
					}
				});
            }
        }],
        onLoad : function() {
				var f = p.find('form');	
				f.form('load', {});
				f.find('input[name=id]').focus();
		}
	});
}
//修改功能
function editUpdateLog(){
	var rows = updateLog_dg.datagrid('getSelections');
	if (rows.length == 1) {
		var p = $('#admin_updateLog_addDate');
		p.dialog({
			title : '编辑更新数据',
			href : '${pageContext.request.contextPath}/admin/updateLogEdit.jsp',
			width : 440,
			height : 330,
			buttons : [ {
				text : '编辑',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/admin/updateLogAction!edit.action',
						success : function(d) {
							var json = $.parseJSON(d);
							if (json.success) {
								updateLog_dg.datagrid('reload');
								p.dialog('close');
							}
							$.messager.show({
								msg : json.msg,
								title : '提示'
							});
						}
					});
				}
			} ],
			onLoad : function() {
				var f = p.find('form');	
				f.form('load', {
					id : rows[0].id,
					contents: rows[0].contents,
					updateDate:rows[0].updateDate,
				});
				f.find('input[name=id]').focus();
			}
		});
	}else{
		$.messager.alert('警告', '请选择一条记录进行修改！',  'warning');
	}
}
//删除功能
function removeUpdateLog(){
	var rows=updateLog_dg.datagrid('getSelections');
	var ids = [];
	if(rows.length>0){
		 $.messager.confirm('请确认','您要删除当前选择项目？',function(r){
			if(r){
				for ( var i = 0; i < rows.length; i++) {
					ids.push(rows[i].id);
				}
				$.ajax({
					url :'${pageContext.request.contextPath}/admin/updateLogAction!delete.action',
					data: {
						ids : ids.join(','),
					},
					dataType:'json',
					success:function(d){
						updateLog_dg.datagrid('load');
						updateLog_dg.datagrid('unselectAll');
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
</script>			
<div id="updateLog_gl_datagrid"></div>
<div id='admin_updateLog_addDate'></div>