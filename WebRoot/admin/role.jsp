<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var role_dg;
var depCombo;
var btns;
var did;

// var admin_role_depCombo;

$(function(){
	did = lnyw.tab_options().did;
	//初始化表格
	role_dg = $('#admin_role_dg').datagrid({
	    url:'${pageContext.request.contextPath}/admin/roleAction!datagrid.action',
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
	        {field:'roleName',title:'名称',width:100},
	        {field:'description',title:'描述',width:100},
	        {field:'did',title:'关联部门代码',width:100,hidden: true},
	        {field:'dname',title:'关联部门',width:100},
	        {field:'menuIds',title:'菜单Id',width:100, hidden: true},
	        {field:'menuNames',title:'拥有菜单'},
	    ]],
	});
	
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, role_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
	
});

//增加角色
function appendRole() {
	var p = $('#admin_role_addDialog');
	p.dialog({
		title : '增加角色',
		href : '${pageContext.request.contextPath}/admin/roleAdd.jsp',
		width : 340,
		height : 200,
		modal : true,
		buttons: [{
            text:'确定',
            iconCls:'icon-ok',
            handler:function(){
            	$('#admin_roleAdd_form').form('submit', {
					url : '${pageContext.request.contextPath}/admin/roleAction!add.action',
					success : function(d) {
						var json = $.parseJSON(jxc.toJson(d));
						if (json.success) {
							role_dg.datagrid('appendRow', json.obj);
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
        	var admin_role_depCombo = lnyw.initCombo($("#admin_role_depId"), 'id', 'depName', '${pageContext.request.contextPath}/admin/departmentAction!listDeps.action');
         	$('#clearBtn').linkbutton({
         	    iconCls: 'icon-cancel'
         	});
//         	var did = f.find('input[name=did]');
//         	depCombo = did.combobox({
// 			    url:'${pageContext.request.contextPath}/admin/departmentAction!listDeps.action',
// 			    valueField:'id',
// 			    textField:'depName'
// 			});
        	$('input[name=roleName]').focus();
 		}
	});
}

//修改角色
function editRole(){
	var rows = role_dg.datagrid('getSelections');
	if (rows.length == 1) {
		var p = $('#admin_role_addDialog');
		p.dialog({
			title : '修改角色',
			href : '${pageContext.request.contextPath}/admin/roleAdd.jsp',
			width : 350,
			height : 200,
			buttons : [ {
				text : '确定',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/admin/roleAction!edit.action',
						success : function(d) {
							var json = $.parseJSON(jxc.toJson(d));
							if (json.success) {
								role_dg.datagrid('reload');
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
				var did = f.find('input[name=did]');
	        	depCombo = did.combobox({
				    url:'${pageContext.request.contextPath}/admin/departmentAction!listDeps.action',
				    valueField:'id',
				    textField:'depName'
				});
				f.form('load', rows[0]);
				f.find('input[name=roleName]').focus();
			}
		});
	} else if (rows.length > 1) {
		$.messager.alert('提示', '同一时间只能编辑一条记录！', 'error');
	} else {
		$.messager.alert('提示', '请选择一条要编辑的记录！', 'error');
	}
}

//删除角色
function removeRole(){
	var rows = role_dg.datagrid('getChecked');
	var ids = [];
	if (rows.length > 0) {
		$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
			if (r) {
				for ( var i = 0; i < rows.length; i++) {
					ids.push(rows[i].id);
				}
				$.ajax({
					url : '${pageContext.request.contextPath}/admin/roleAction!delete.action',
					data : {
						ids : ids.join(',')
					},
					dataType : 'json',
					success : function(d) {
						role_dg.datagrid('reload');
						role_dg.datagrid('unselectAll');
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

//角色授权
function auth(){
	var rows = role_dg.datagrid('getSelections');
	if (rows.length == 1) {
		var p = $('#admin_role_authDialog');
		p.dialog({
			title : '设置权限',
			href : '${pageContext.request.contextPath}/admin/roleAuth.jsp',
			width : 350,
			height : 450,
			modal: true,
			buttons : [ {
				text : '确定',
				handler : function() {
					//var f = p.find('form');
					$('#admin_roleAuth_form').form('submit', {
						url : '${pageContext.request.contextPath}/admin/roleAction!auth.action',
						onSubmit : function() {
//	 						parent.$.messager.progress({
//	 							title : '提示',
//	 							text : '数据处理中，请稍后....'
//	 						});
							var isValid = $(this).form('validate');
							if (!isValid) {
								//parent.$.messager.progress('close');
							}
							var checknodes = $('#admin_roleAuth_tree').tree('getCheckedExt');
							var mids = [];
							var bids = [];
							if (checknodes && checknodes.length > 0) {
								for ( var i = 0; i < checknodes.length; i++) {
									if(checknodes[i].attributes.type == 'Menu'){
										mids.push(checknodes[i].id);
									}else{
										bids.push(checknodes[i].id);
									}
								}
							}
							$('#roleId').val(rows[0].id);
							$('#menuIds').val(mids);
							$('#btnIds').val(bids);
							return isValid;
						},
						success : function(d) {
							var j = jQuery.parseJSON(jxc.toJson(d));
							if (j.success) {
								role_dg.datagrid('reload');
								p.dialog('close');
							}
							$.messager.show({
								msg : j.msg,
								title : '提示'
							});
						}
					});
				}
			} ],
			onLoad : function() {
				$('#admin_roleAuth_tree').tree({
					url:'${pageContext.request.contextPath}/admin/roleAction!tree.action',
					checkbox:true,
					onLoadSuccess : function(node, data) {
						//var ids = lnyw.stringToList(rows[0].menuIds.concat(rows[0].btnIds));
						var ids = lnyw.stringToList(rows[0].btnIds);
						if (ids.length > 0) {
							for ( var i = 0; i < ids.length; i++) {
								if ($(this).tree('find', ids[i])) {
									$(this).tree('check', $(this).tree('find', ids[i]).target);
								}
							}
						}
					},
				});
				
			}
		});
	} else if (rows.length > 1) {
		$.messager.alert('提示', '同一时间只能授权一个角色！', 'error');
	} else {
		$.messager.alert('提示', '请选择一个要授权的角色！', 'error');
	}	
}

</script>
<div id='admin_role_dg'></div>
<div id='admin_role_addDialog'></div>
<div id='admin_role_authDialog'></div>

	
