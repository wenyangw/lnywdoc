<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var did;
var treegrid;
$(function(){
	did = lnyw.tab_options().did;
	treegrid = $('#admin_menu_tg').treegrid({
		url:'${pageContext.request.contextPath}/admin/menuAction!treegrid.action',
	    idField:'id',
	    treeField:'text',
	    fit: true,
	    columns:[[
	        {field:'id',title:'编号',width:180, hidden: true},
	        {field:'text',title:'名称',width:180},
	        {field:'url',title:'地址',width:180},
	        {field:'orderNum',title:'排序',width:50},
	        {field:'iconCls',title:'图标',width:50,
	        	formatter : function(value) {
				if (!value) {
					return '';
				} else {
					return lnyw.fs('<span class="{0}" style="display:inline-block;vertical-align:middle;width:16px;height:16px;"></span>', value);
					
				}
			},},
			{field:'lx',title:'单据类型',width:50},
			{field:'query',title:'查询类型',width:50,},
			{field:'pid',title:'上级ID',width:50, hidden: true},
			{field:'pname',title:'上级菜单',width:180, hidden: true},
	        {field:'cid',title:'模块ID',width:50, hidden: true},
	        {field:'cname',title:'模块名称',width:180},
	    ]],
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, treegrid, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);

});

function appendMenu() {
	var menuAdd = $('#admin_menu_addDialog');
	menuAdd.dialog({
		title : '增加菜单',
		href : '${pageContext.request.contextPath}/admin/menuAdd.jsp',
		width : 350,
		height : 300,
		buttons : [ {
			text : '确定',
			handler : function() {
				var f = menuAdd.find('form');
				f.form('submit', {
					url : '${pageContext.request.contextPath}/admin/menuAction!add.action',
					success : function(d) {
						var json = $.parseJSON(jxc.toJson(d));
						if (json.success) {
							//if(d.obj.genre == '03'){
								treegrid.treegrid('reload');
							//}
							menuAdd.dialog('close');
							//parent.ctrlTree.tree('reload');/*刷新左侧菜单树*/
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
			var f = menuAdd.find('form');
			var pid = f.find('input[name=pid]');
			var cid = f.find('input[name=cid]');
			var iconCls = f.find('input[name=iconCls]');
			var iconCombo = iconCls.combobox({
				data : iconData,
				formatter : function(v) {
					return lnyw.fs('<span class="{0}" style="display:inline-block;vertical-align:middle;width:16px;height:16px;"></span>{1}', v.value, v.value);
				}
			});
			var ptree = pid.combotree({
				lines : true,
				url : '${pageContext.request.contextPath}/admin/menuAction!allTopTree.action'
			});
			var ctree= cid.combobox({
			    url:'${pageContext.request.contextPath}/admin/catalogAction!listCatas.action',
			    valueField:'id',
			    textField:'catName'
			});
			$('input[name=menuName]').focus();
		}
	});
}
function editMenu() {
	var node = treegrid.treegrid('getSelected');
	if (node) {
		var p = $('#admin_menu_addDialog');
		p.dialog({
			title : '修改菜单',
			href : '${pageContext.request.contextPath}/admin/menuAdd.jsp',
			width : 350,
			height : 300,
			buttons : [ {
				text : '确定',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/admin/menuAction!edit.action',
						success : function(d) {
							var json = $.parseJSON(jxc.toJson(d));
							if (json.success) {
								//if(d.obj.genre == '03'){
									treegrid.treegrid('reload');
								//}
								p.dialog('close');
								//parent.ctrlTree.tree('reload');/*刷新左侧菜单树*/
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
 				var pid = f.find('input[name=pid]');
 				var cid = f.find('input[name=cid]');
 				var iconCls = f.find('input[name=iconCls]');
				var iconCombo = iconCls.combobox({
					data : iconData,
					formatter : function(v) {
						return lnyw.fs('<span class="{0}" style="display:inline-block;vertical-align:middle;width:16px;height:16px;"></span>{1}', v.value, v.value);
					}
				});
				var ptree = pid.combotree({
					lines : true,
					url : '${pageContext.request.contextPath}/admin/menuAction!allTopTree.action',
					onLoadSuccess : function() {
						$.messager.progress('close');
					}
				});
				var ctree= cid.combobox({
				    url:'${pageContext.request.contextPath}/admin/catalogAction!listCatas.action',
				    valueField:'id',
				    textField:'catName'
				});
				f.form('load', node);
			}
		});
	} else {
		$.messager.alert('提示', '请选中一条要编辑的记录！', 'error');
	}
}

function removeMenu() {
	var node = treegrid.treegrid('getSelected');
 	if(node.children){
 		$.messager.alert('警告', '此菜单包含子菜单，无法删除！', 'warning');
 	}else{
		if (node) {
			$.messager.confirm('询问', '您确定要删除【' + node.menuName + '】？', function(b) {
				if (b) {
					$.ajax({
						url : '${pageContext.request.contextPath}/admin/menuAction!delete.action',
						data : {
							id : node.id
						},
						cache : false,
						dataType : 'JSON',
						success : function(r) {
							if (r.success) {
								treegrid.treegrid('reload');
							}
							$.messager.show({
								msg : r.msg,
								title : '提示'
							});
						}
					});
				}
			});
		}
	}
}
</script>
<table id='admin_menu_tg'></table>
<div id='admin_menu_addDialog'></div>

	
