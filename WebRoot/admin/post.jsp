<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var post_dg;
var did;
$(function(){
	post_dg = $('#admin_post_datagrid');
	did = lnyw.tab_options().did;
	post_dg.datagrid({
		url:'${pageContext.request.contextPath}/admin/postAction!datagrid.action',
		fit : true,
		singleSelect:true,
	    border : false,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
	    columns:[[
	        {field:'id',title:'编号',width:100},
	        {field: 'postName',title:'职务名称',width:100},
	        {field: 'orderNum',title:'排序',width:100},
	        ]],
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, post_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
});
function appendPost() {
	var p = $('#admin_post_addDialog');
	p.dialog({
		title : '增加职务',
		href : '${pageContext.request.contextPath}/admin/postAdd.jsp',
		width : 340,
		height : 210,
		modal : true,
		buttons: [{
            text:'增加',
            iconCls:'icon-ok',
            handler:function(){
            	$('#jxc_postAdd_form').form('submit', {
					url : '${pageContext.request.contextPath}/admin/postAction!add.action',
					onSubmit: function(){						
							if($(this).form('validate')){
								return true;
							}else{
								return false;
							}
						
						},
					success : function(d) {
						var json = $.parseJSON(d);
						if (json.success) {
							post_dg.datagrid('appendRow', json.obj);
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
							
				f.find('input[name=id]').focus();
			}
	});
}

function editPost(){
	var rows = post_dg.datagrid('getSelections');
	if(rows.length > 0){	
		var p = $('#admin_post_addDialog');
		p.dialog({
			title : '编辑职务',
			href : '${pageContext.request.contextPath}/admin/postAdd.jsp',
			width : 350,
			height : 210,
			buttons : [ {
				text : '编辑',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/admin/postAction!edit.action',
						onSubmit: function(){
							if($(this).form('validate')){
								return true;
							}else{
								return false;
							}
						},
						success : function(d) {
							var json = $.parseJSON(d);
							if (json.success) {
								post_dg.datagrid('reload');
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
				
				f.form('load', rows[0]);
				f.find('input[name=id]').focus();
			}
		});	
		}else{
			$.messager.alert('警告', '请选择一条记录进行修改！',  'warning');
		}
}
function removePost(){
	var rows = post_dg.datagrid('getSelections');

	if (rows.length > 0) {
		$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
			if (r) {
				
				$.ajax({
					url : '${pageContext.request.contextPath}/admin/postAction!delete.action',
					data : {
						id : rows[0].id,
					},
					dataType : 'json',
					success : function(d) {
						post_dg.datagrid('load');
						post_dg.datagrid('unselectAll');
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
<div id="admin_post_datagrid"></div>
<div id='admin_post_addDialog'></div>

