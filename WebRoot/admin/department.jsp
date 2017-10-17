<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var depart_dg;
var did;
$(function(){
	did = lnyw.tab_options().did;
	depart_dg = $('#admin_department_dg').datagrid({
	    url:'${pageContext.request.contextPath}/admin/departmentAction!datagrid.action',
	    fit : true,
	    border : false,
	    singleSelect : true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		idField : 'id',
	    columns:[[
	        {field:'id',title:'编号',width:100},
	        {field:'depName',title:'部门名称',width:100},
	        {field:'orderNum',title:'显示顺序',width:100},
	    ]],
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, depart_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);

});

function appendDepa() {
	var p = $('#admin_department_addDialog');
	p.dialog({
		title : '增加部门',
		href : '${pageContext.request.contextPath}/admin/departmentAdd.jsp',
		width : 340,
		height : 200,
		modal : true,
		buttons: [{
            text:'确定',
            iconCls:'icon-ok',
            handler:function(){
            	$('#admin_departmentAdd_form').form('submit', {
					url : '${pageContext.request.contextPath}/admin/departmentAction!add.action',
					success : function(d) {
						var json = $.parseJSON(d);
						if (json.success) {
							depart_dg.datagrid('appendRow', json.obj);
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
        	$('input[name=id]').focus();
 		}
	});
}

function editDepa(){
	var rows = depart_dg.datagrid('getSelections');
	if (rows.length == 1) {
		var p = $('#admin_department_addDialog');
		p.dialog({
			title : '修改部门',
			href : '${pageContext.request.contextPath}/admin/departmentAdd.jsp',
			width : 350,
			height : 200,
			buttons : [ {
				text : '确定',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/admin/departmentAction!edit.action',
						success : function(d) {
							var json = $.parseJSON(d);
							if (json.success) {
								depart_dg.datagrid('reload');
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
					depName : rows[0].depName,
					orderNum : rows[0].orderNum,
				});
				f.find('input[name=id]').attr('readonly', true);
				f.find('input[name=depName]').focus();
			}
		});
	} else if (rows.length > 1) {
		$.messager.alert('提示', '同一时间只能编辑一条记录！', 'error');
	} else {
		$.messager.alert('提示', '请选择一条要编辑的记录！', 'error');
	}
}
function removeDepa(){
	var rows = depart_dg.datagrid('getChecked');
	var ids = [];
	if (rows.length > 0) {
		$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
			if (r) {
				for ( var i = 0; i < rows.length; i++) {
					ids.push(rows[i].id);
				}
				$.ajax({
					url : '${pageContext.request.contextPath}/admin/departmentAction!delete.action',
					data : {
						ids : ids.join(',')
					},
					dataType : 'json',
					success : function(d) {
						depart_dg.datagrid('load');
						depart_dg.datagrid('unselectAll');
						$.messager.show({
							title : '提示',
							msg : d.msg
						});
					}
				});
			}
		});
	} else {
		$.messager.alert('警告', '请选择最少一条记录进行删除！',  'warning');
	}
}
</script>
<div id='admin_department_dg'></div>
<div id='admin_department_addDialog'></div>

	
