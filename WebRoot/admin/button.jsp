<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var btns_dg;
var did;
$(function(){
	did = lnyw.tab_options().did;
	$('#admin_button_layout').layout({
		fit : true,
		border : false,
	});
	$('#admin_button_tree').tree({
		//url:'${pageContext.request.contextPath}/admin/menuAction!getTrees.action',
		url:'${pageContext.request.contextPath}/admin/menuAction!doNotNeedSession_tree.action',
		//parentField:'pid',
		onClick : function(node) {
			btns_dg.datagrid('load', {
				mid : node.id,
			});
		}
		
	});
	btns_dg = $('#admin_button_btns').datagrid({
		url : '${pageContext.request.contextPath}/admin/buttonAction!datagrid.action',
		fit : true,
	    border : false,
	    singleSelect : true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
	        {field:'id',title:'编号',width:100, checkbox : true},
	        {field:'text',title:'名称',width:100},
	        {field:'orderNum',title:'排序',width:30},
	        {
				field : 'iconCls',
				title : '图标',
				width : 30,
				formatter : function(value) {
					if (!value) {
						return '';
					} else {
						return lnyw.fs('<span class="{0}" style="display:inline-block;vertical-align:middle;width:16px;height:16px;"></span>', value);
						
					}
				},
			},
			{field:'handler',title:'链接',width:100},
	        {field:'mname',title:'所属菜单',width:100},
	        {field:'tabId',title:'所属标签',width:100},
	    ]],
		
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, btns_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);

});

function appendButt() {
	var p = $('#admin_button_addDialog');
	p.dialog({
		title : '增加功能按钮',
		href : '${pageContext.request.contextPath}/admin/buttonAdd.jsp',
		width : 340,
		height : 240,
		modal : true,
		buttons: [{
            text:'确定',
            iconCls:'icon-ok',
            handler:function(){
            	$('#admin_buttonAdd_form').form('submit', {
					url : '${pageContext.request.contextPath}/admin/buttonAction!add.action',
					success : function(d) {
						var json = $.parseJSON(jxc.toJson(d));
						if (json.success) {
							btns_dg.datagrid('appendRow', json.obj);
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
			var mid = f.find('input[name=mid]');
			var iconCls = f.find('input[name=iconCls]');
			var iconCombo = iconCls.combobox({
				data : iconData,
				formatter : function(v) {
					return lnyw.fs('<span class="{0}" style="display:inline-block;vertical-align:middle;width:16px;height:16px;"></span>{1}', v.value, v.value);
				}
			});
			var mtree = mid.combotree({
				lines : true,
				url : '${pageContext.request.contextPath}/admin/menuAction!doNotNeedSession_tree.action',
				onBeforeSelect: function(node) {
		       		if (!$(this).tree('isLeaf', node.target)) {
		       			$.messager.alert('提示', '请选择二级菜单！', 'error');
		                return false;
		            }
		        },
// 				onClick:function(node){
// 					var parent = mtree.combotree('tree').tree('getParent', node.target);
// 					if(parent == null){
// 						$.messager.alert('提示', '请选择二级菜单！', 'error');
// 					}
// 				}
			});
			$('input[name=text]').focus();
		}
	});
}

function editButt(){
	var rows = btns_dg.datagrid('getSelections');
	if (rows.length == 1) {
		var p = $('#admin_button_addDialog');
		p.dialog({
			title : '编辑功能按钮',
			href : '${pageContext.request.contextPath}/admin/buttonAdd.jsp',
			width : 350,
			height : 240,
			buttons : [ {
				text : '确定',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/admin/buttonAction!edit.action',
						success : function(d) {
							var json = $.parseJSON(jxc.toJson(d));
							if (json.success) {
								btns_dg.datagrid('reload');
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
				var mid = f.find('input[name=mid]');
				var iconCls = f.find('input[name=iconCls]');
				var iconCombo = iconCls.combobox({
					data : iconData,
					formatter : function(v) {
						return lnyw.fs('<span class="{0}" style="display:inline-block;vertical-align:middle;width:16px;height:16px;"></span>{1}', v.value, v.value);
					}
				});
				var mtree = mid.combotree({
					lines : true,
					url : '${pageContext.request.contextPath}/admin/menuAction!doNotNeedSession_treeRecursive.action',
					onBeforeSelect: function(node) {
			       		if (!$(this).tree('isLeaf', node.target)) {
			       			$.messager.alert('提示', '请选择二级菜单！', 'error');
			                return false;
			            }
			        },
// 					onClick:function(node){
// 						var parent = mtree.combotree('tree').tree('getParent', node.target);
// 						if(parent == null){
// 							$.messager.alert('提示', '请选择二级菜单！', 'error');
// 						}
// 					}
				});
				f.form('load', rows[0]);
				f.find('input[name=text]').focus();
			}
		});
	} else if (rows.length > 1) {
		$.messager.alert('提示', '同一时间只能编辑一条记录！', 'error');
	} else {
		$.messager.alert('提示', '请选择一条要编辑的记录！', 'error');
	}
}
function removeButt(){
	var rows = btns_dg.datagrid('getChecked');
	var ids = [];
	if (rows.length > 0) {
		$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
			if (r) {
				for ( var i = 0; i < rows.length; i++) {
					ids.push(rows[i].id);
				}
				$.ajax({
					url : '${pageContext.request.contextPath}/admin/buttonAction!delete.action',
					data : {
						ids : ids.join(',')
					},
					dataType : 'json',
					success : function(d) {
						btns_dg.datagrid('load');
						btns_dg.datagrid('unselectAll');
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
<div id="admin_button_layout" style="height:100%; width=100%">
	<div data-options="region:'west',title:'菜单',split:true" style="height:100px;width:180px">
		<ul id="admin_button_tree"></ul>
	</div>
    <div data-options="region:'center',title:'功能管理',split:true, fit:true" style="height:100px;">
    	<div id='admin_button_btns'></div>
    </div>
</div>
<div id='admin_button_addDialog'></div>


	
