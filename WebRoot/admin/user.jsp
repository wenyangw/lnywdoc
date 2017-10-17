<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var sexData = [ {
	value : '1',
	text : '男'
}, {
	value : '0',
	text : '女'
}];

var did;
var menuId;
var user_dg;
$(function(){
	did = lnyw.tab_options().did;
	menuId = lnyw.tab_options().id;
	
	user_dg = $('#admin_user_dg').datagrid({
	    url:'${pageContext.request.contextPath}/admin/userAction!datagrid.action',
	    fit : true,
	    border : false,
	    singleSelect : true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		idField : 'id',
	    columns:[[
	        {field:'id',title:'编号',width:30},
	        {field:'userName',title:'登录名',width:100},
// 	        {field:'password',title:'密码',width:100,hidden:true,
// 	        	formatter : function(value, rowData, rowIndex) {
// 					return '******';
// 				}
// 	        },
	        {field:'realName',title:'姓名',width:100},
	        {field:'sex',title:'性别标识', hidden:true},
	        {field:'csex',title:'性别',},
	        {field:'orderNum',title:'排序',width:30},
	        {field:'did',title:'部门id',width:100, hidden:true},
	        {field:'dname',title:'部门',width:100},
	        {field:'postId',title:'职务id',width:100, hidden:true},
	        {field:'postName',title:'职务',width:100},
	        {field: 'isYwy',title:'是否业务员',
	        	formatter : function(value) {
					if (value == '1') {
						return '是';
					} else {
						return '否';
					}
				}},
	        {field: 'isBgy',title:'是否保管员',
	        	formatter : function(value) {
					if (value == '1') {
						return '是';
					} else {
						return '否';
					}
				}},
	        {field:'roleIds',title:'角色Id',width:100, hidden:true},
	        {field:'createTime',title:'创建时间',width:100},
	        {field:'modifyTime',title:'修改时间',width:100},
	        {field:'lastTime',title:'最后登录时间',width:100},
	        {field:'roleNames',title:'拥有角色'},
	    ]],
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, user_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);

});

function appendUser() {
	var p = $('#admin_user_addDialog');
	p.dialog({
		title : '增加用户',
		href : '${pageContext.request.contextPath}/admin/userAdd.jsp',
		width : 350,
		height : 360,
		modal : true,
		buttons: [{
            text:'确定',
            iconCls:'icon-ok',
            handler:function(){
            	$('#admin_userAdd_form').form('submit', {
					url : '${pageContext.request.contextPath}/admin/userAction!add.action',
					onSubmit:function(){
					},
					success : function(d) {
						var json = $.parseJSON(jxc.toJson(d));
						if (json.success) {
							user_dg.datagrid('appendRow', json.obj);
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
			var sex = f.find('input[name=sex]');
			var did = f.find('input[name=did]');
			var postId = f.find('input[name=postId]');
			var sexCombo = sex.combobox({
				data : sexData,
			});
			var depCombo = did.combobox({
			    url:'${pageContext.request.contextPath}/admin/departmentAction!listDeps.action',
			    valueField:'id',
			    textField:'depName'
			});
			var postCombo = postId.combobox({
			    url:'${pageContext.request.contextPath}/admin/postAction!listPosts.action',
			    valueField:'id',
			    textField:'postName'
			});
			f.find('input[name=operaDepId]').val(did);
			f.find('input[name=menuId]').val(menuId);
			
			$('input[name=userName]').focus();
		}
	});
}

function editUser(){
	var rows = user_dg.datagrid('getSelections');
	if (rows.length == 1) {
		var p = $('#admin_user_addDialog');
		p.dialog({
			title : '修改用户',
			href : '${pageContext.request.contextPath}/admin/userEdit.jsp',
			width : 350,
			height : 360,
			buttons : [ {
				text : '确定',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/admin/userAction!edit.action',
						onSubmit:function(){},
						success : function(d) {
							var json = $.parseJSON(jxc.toJson(d));
							if (json.success) {
								user_dg.datagrid('unselectAll');
								user_dg.datagrid('reload');
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
				var sex = f.find('input[name=sex]');
				var did = f.find('input[name=did]');
				var postId = f.find('input[name=postId]');
				var roleIds = f.find('input[name=roleIds]');
				var sexCombo = sex.combobox({
					data : sexData,
				});
				var depCombo = did.combobox({
				    url:'${pageContext.request.contextPath}/admin/departmentAction!listDeps.action',
				    valueField:'id',
				    textField:'depName',
				});
				var postCombo = postId.combobox({
				    url:'${pageContext.request.contextPath}/admin/postAction!listPosts.action',
				    valueField:'id',
				    textField:'postName'
				});
				var roleCombo = roleIds.combobox({
				    url:'${pageContext.request.contextPath}/admin/roleAction!listRoles.action',
				    valueField : 'id',
					textField : 'roleName',
				    multiple: true,				
				});
				f.form('load', {
					id: rows[0].id,
					userName : rows[0].userName,
					password : '',
					realName : rows[0].realName,
					sex : rows[0].sex,
					orderNum : rows[0].orderNum,
					did : rows[0].did,
					postId: rows[0].postId,
					isYwy: rows[0].isYwy,
					isBgy: rows[0].isBgy,
					createTime : rows[0].createTime,
					lastTime : rows[0].lastTime,
					roleIds : lnyw.getList(rows[0].roleIds),
					operaDepId : did,
					menuId : menuId,
				});
				
				f.find('input[name=userName]').focus();
			}
		});
	} else if (rows.length > 1) {
		$.messager.alert('提示', '同一时间只能编辑一条记录！', 'error');
	} else {
		$.messager.alert('提示', '请选择一条要编辑的记录！', 'error');
	}
}

function removeUser(){
	var rows = user_dg.datagrid('getChecked');
	//var ids = [];
	if (rows.length == 1) {
		$.messager.confirm('请确认', '您要删除当前所选用户？', function(r) {
			if (r) {
// 				for ( var i = 0; i < rows.length; i++) {
// 					ids.push(rows[i].id);
// 				}
				$.ajax({
					url : '${pageContext.request.contextPath}/admin/userAction!delete.action',
					data : {
						//ids : ids.join(',')
						id:rows[0].id,
						operaDepId: did,
						menuId : menuId,
					},
					dataType : 'json',
					success : function(d) {
						user_dg.datagrid('load');
						user_dg.datagrid('unselectAll');
						$.messager.show({
							title : '提示',
							msg : d.msg
						});
					}
				});
			}
		});
	} else {
		$.messager.alert('警告', '请选择一条记录进行删除！',  'warning');
	}
}
</script>
<div id='admin_user_dg'></div>
<div id='admin_user_addDialog'></div>

	
