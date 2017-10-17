<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var entry_did;
var entry_treegrid;
$(function(){
	entry_did = lnyw.tab_options().did;
    level_treegrid = $('#doc_entry_levelTg').treegrid({
        url:'${pageContext.request.contextPath}/doc/levelAction!treegrid.action',
        idField:'id',
        treeField:'levelName',
        fit: true,
        columns:[[
            {field:'id',title:'编号',width:180, hidden: true},
            {field:'levelName',title:'名称',width:180},
            {field:'orderNum',title:'排序',width:50},
            {field:'dir',title:'路径',width:180},
            {field:'bz',title:'备注',width:180},
            {field:'pid',title:'上级ID',width:50, hidden: true},
            {field:'pname',title:'上级名称',width:180, hidden: true},
            {field:'children',title:'子',width:20, hidden: true},
        ]],
    });
    //根据权限，动态加载功能按钮
    lnyw.toolbar(0, level_treegrid, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', level_did);

	entry_treegrid = $('#doc_entry_tg').treegrid({
		url:'${pageContext.request.contextPath}/doc/entryAction!treegrid.action',
	    idField:'id',
	    treeField:'entryName',
	    fit: true,
	    columns:[[
	        {field:'id',title:'编号',width:180, hidden: true},
	        {field:'entryName',title:'名称',width:180},
	        {field:'orderNum',title:'排序',width:50},
            {field:'dir',title:'路径',width:180},
            {field:'bz',title:'备注',width:180},
			{field:'pid',title:'上级ID',width:50, hidden: true},
			{field:'pname',title:'上级名称',width:180, hidden: true},
            {field:'children',title:'子',width:20, hidden: true},
	    ]],
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, entry_treegrid, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', entry_did);

});

function addEntry() {
	var entryAdd = $('#doc_entry_addDialog');
	entryAdd.dialog({
		title : '增加类别',
		href : '${pageContext.request.contextPath}/doc/entryAdd.jsp',
		width : 350,
		height : 300,
		buttons : [ {
			text : '确定',
			handler : function() {
				var f = entryAdd.find('form');
				f.form('submit', {
					url : '${pageContext.request.contextPath}/doc/entryAction!add.action',
					success : function(d) {
						var json = $.parseJSON(jxc.toJson(d));
						if (json.success) {
                            entry_treegrid.treegrid('reload');
							entryAdd.dialog('close');
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
			var f = entryAdd.find('form');
			var pid = f.find('input[name=pid]');
			var ptree = pid.combotree({
				lines : true,
				url : '${pageContext.request.contextPath}/doc/entryAction!getEntryTree.action'
			});
			$('input[name=entryName]').focus();
		}
	});
}
function editEntry() {
	var node = entry_treegrid.treegrid('getSelected');
	if (node) {
		var p = $('#doc_entry_addDialog');
		p.dialog({
			title : '修改类别',
			href : '${pageContext.request.contextPath}/doc/entryAdd.jsp',
			width : 350,
			height : 300,
			buttons : [ {
				text : '确定',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/doc/entryAction!edit.action',
						success : function(d) {
							var json = $.parseJSON(jxc.toJson(d));
							if (json.success) {
                                entry_treegrid.treegrid('reload');
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
 				var pid = f.find('input[name=pid]');
				var ptree = pid.combotree({
					lines : true,
					url : '${pageContext.request.contextPath}/doc/entryAction!getEntryTree.action',
					onLoadSuccess : function() {
						$.messager.progress('close');
					}
				});
				f.form('load', node);
			}
		});
	} else {
		$.messager.alert('提示', '请选中一条要编辑的记录！', 'error');
	}
}

function removeEntry() {
	var node = entry_treegrid.treegrid('getSelected');
 	if(node.children){
 		$.messager.alert('警告', '此类别包含子类别，无法删除！', 'warning');
 	}else{
		if (node) {
			$.messager.confirm('询问', '您确定要删除【' + node.entryName + '】？', function(b) {
				if (b) {
					$.ajax({
						url : '${pageContext.request.contextPath}/doc/entryAction!delete.action',
						data : {
							id : node.id
						},
						cache : false,
						dataType : 'JSON',
						success : function(r) {
							if (r.success) {
                                entry_treegrid.treegrid('reload');
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
<table id='doc_entry_tg'></table>
<div id='doc_entry_addDialog'></div>

	
