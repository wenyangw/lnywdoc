<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var catalog_dg;
var did;
$(function(){
	did = lnyw.tab_options().did;
	
	catalog_dg = $('#admin_catalog_dg').datagrid({
	    url:'${pageContext.request.contextPath}/admin/catalogAction!datagrid.action',
	    fit : true,
	    border : false,
	    singleSelect : true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		idField : 'id',
	    columns:[[
	        {field:'id',title:'编号',width:100, checkbox : true},
	        {field:'catName',title:'名称',width:100},
	        {field:'orderNum',title:'排序',width:100, sortable : true},
	        {
				field : 'iconCls',
				title : '图标',
				//width : 70,
				formatter : function(value) {
					if (!value) {
						return '';
					} else {
						return lnyw.fs('<span class="{0}" style="display:inline-block;vertical-align:middle;width:16px;height:16px;"></span>', value);
						
					}
				},
			}
	    ]],
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, catalog_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);

});


function appendCata() {
	var cataAdd = $('#admin_catalog_addDialog');
	cataAdd.dialog({
		title : '增加模块',
		href : '${pageContext.request.contextPath}/admin/catalogAdd.jsp',
		width : 340,
		height : 200,
		modal : true,
		buttons: [{
            text:'确定',
            iconCls:'icon-ok',
            handler:function(){
            	$('#admin_catalogAdd_form').form('submit', {
					url : '${pageContext.request.contextPath}/admin/catalogAction!add.action',
					success : function(d) {
						var json = $.parseJSON(d);
						if (json.success) {
							catalog_dg.datagrid('appendRow', json.obj);
							cataAdd.dialog('close');
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
			var f = cataAdd.find('form');
			var iconCls = f.find('input[name=iconCls]');
			var iconCombo = iconCls.combobox({
				data : iconData,
				formatter : function(v) {
					return lnyw.fs('<span class="{0}" style="display:inline-block;vertical-align:middle;width:16px;height:16px;"></span>{1}', v.value, v.value);
				}
			});
			$('input[name=catName]').focus();
		}
	});
}

function editCata(){
	var rows = catalog_dg.datagrid('getSelections');
	if (rows.length == 1) {
		var p = $('#admin_catalog_addDialog');
		p.dialog({
			title : '修改模块',
			href : '${pageContext.request.contextPath}/admin/catalogAdd.jsp',
			width : 350,
			height : 200,
			buttons : [ {
				text : '确定',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/admin/catalogAction!edit.action',
						success : function(d) {
							var json = $.parseJSON(d);
							if (json.success) {
								catalog_dg.datagrid('reload');
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
				var iconCls = f.find('input[name=iconCls]');
				var iconCombo = iconCls.combobox({
					data : iconData,
					formatter : function(v) {
						return lnyw.fs('<span class="{0}" style="display:inline-block;vertical-align:middle;width:16px;height:16px;"></span>{1}', v.value, v.value);
					}
				});
				f.form('load', {
					id : rows[0].id,
					catName : rows[0].catName,
					orderNum : rows[0].orderNum,
					iconCls : rows[0].iconCls,
				});
				f.find('input[name=catName]').focus();
			}
		});
	} else if (rows.length > 1) {
		$.messager.alert('提示', '同一时间只能编辑一条记录！', 'error');
	} else {
		$.messager.alert('提示', '请选择一条要编辑的记录！', 'error');
	}
}
function removeCata(){
	var rows = catalog_dg.datagrid('getChecked');
	var ids = [];
	if (rows.length > 0) {
		$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
			if (r) {
				for ( var i = 0; i < rows.length; i++) {
					ids.push(rows[i].id);
				}
				$.ajax({
					url : '${pageContext.request.contextPath}/admin/catalogAction!delete.action',
					data : {
						ids : ids.join(',')
					},
					dataType : 'json',
					success : function(d) {
						catalog_dg.datagrid('load');
						catalog_dg.datagrid('unselectAll');
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
<div id='admin_catalog_dg'></div>
<div id='admin_catalog_addDialog'></div>

	
