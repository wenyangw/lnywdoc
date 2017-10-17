<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var level_did;
var level_treegrid;
$(function(){
	level_did = lnyw.tab_options().did;
	level_treegrid = $('#doc_level_tg').treegrid({
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
            {field:'catId',title:'分类id',hidden: true},
            {field:'catName',title:'分类',width:180},
			{field:'pid',title:'上级ID',width:50, hidden: true},
			{field:'pname',title:'上级名称',width:180, hidden: true},
	    ]],
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, level_treegrid, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', level_did);

});

function addLevel() {
	var levelAdd = $('#doc_level_addDialog');
	levelAdd.dialog({
		title : '增加类别',
		href : '${pageContext.request.contextPath}/doc/levelAdd.jsp',
		width : 350,
		height : 300,
		buttons : [ {
			text : '确定',
			handler : function() {
				var f = levelAdd.find('form');
				f.form('submit', {
					url : '${pageContext.request.contextPath}/doc/levelAction!add.action',
					onSubmit: function(){
						var pid = $('#doc_level_pid').combotree('getValue');
						var dir = $('input[name=dir]').val();
						if(checkLevelDir(pid, dir)){
							$.messager.alert('提示', '输入的路径已存在，请重新输入！', 'error');
							return false;
						}
					},
					success : function(d) {
						var json = $.parseJSON(jxc.toJson(d));
						if (json.success) {
							level_treegrid.treegrid('reload');
							levelAdd.dialog('close');
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
			var f = levelAdd.find('form');
			var pid = f.find('input[name=pid]');
			var ptree = pid.combotree({
				lines : true,
				url : '${pageContext.request.contextPath}/doc/levelAction!getLevelTree.action'
			});
            f.find('input[name=catId]').combobox({
				url: '${pageContext.request.contextPath}/doc/catAction!getCats.action',
                valueField: 'id',
                textField: 'catName'
			}).combobox("selectedIndex", 0);
			$('input[name=levelName]').focus();
		}
	});
}
function editLevel() {
	var node = level_treegrid.treegrid('getSelected');

	if (node) {
        var level_dir = node.dir;
        var p = $('#doc_level_addDialog');
		p.dialog({
			title : '修改类别',
			href : '${pageContext.request.contextPath}/doc/levelAdd.jsp',
			width : 350,
			height : 300,
			buttons : [ {
				text : '确定',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/doc/levelAction!edit.action',
						onSubmit: function(){
						    if(level_dir != $('input[name=dir]').val()) {
                                var pid = $('#doc_level_pid').combotree('getValue');
                                var dir = $('input[name=dir]').val();
                                if (checkLevelDir(pid, dir)) {
                                    $.messager.alert('提示', '输入的路径已存在，请重新输入！', 'error');
                                    return false;
                                }
                            }
                        },
						success : function(d) {
							var json = $.parseJSON(jxc.toJson(d));
							if (json.success) {
                                level_treegrid.treegrid('reload');
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
				pid.combotree({
					lines : true,
					url : '${pageContext.request.contextPath}/doc/levelAction!getLevelTree.action',
					onLoadSuccess : function() {
						$.messager.progress('close');
					}
				});
                f.find('input[name=catId]').combobox({
                    url: '${pageContext.request.contextPath}/doc/catAction!getCats.action',
                    valueField: 'id',
                    textField: 'catName'
                });
				f.form('load', node);
			}
		});
	} else {
		$.messager.alert('提示', '请选中一条要编辑的记录！', 'error');
	}
}

function removeLevel() {
	var node = level_treegrid.treegrid('getSelected');
 	if(node.children){
 		$.messager.alert('警告', '此类别包含子类别，无法删除！', 'warning');
 	}else{
 	    if(checkEntryByLevel(node.id)){
            $.messager.alert('警告', '此类别包含有条目，无法删除！', 'warning');
		}else{
			if (node) {
				$.messager.confirm('询问', '您确定要删除【' + node.levelName + '】？', function(b) {
					if (b) {
						$.ajax({
							url : '${pageContext.request.contextPath}/doc/levelAction!delete.action',
							data : {
								id : node.id
							},
							cache : false,
							dataType : 'JSON',
							success : function(r) {
								if (r.success) {
									level_treegrid.treegrid('reload');
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
}

function checkLevelDir(pid, dir){
    var flag = false;
    $.ajax({
        url : '${pageContext.request.contextPath}/doc/levelAction!checkDir.action',
        data : {
            pid : pid,
			dir: dir
        },
        cache : false,
        dataType : 'JSON',
		async: false,
        success : function(r) {
            flag = r.success;
        }
    });
    return flag;
}

function checkEntryByLevel(levelId){
    var flag = false;
    $.ajax({
        url : '${pageContext.request.contextPath}/doc/entryAction!checkEntryByLevel.action',
        data : {
            levelId : levelId
        },
        cache : false,
        dataType : 'JSON',
        async: false,
        success : function(r) {
            flag = r.success;
        }
    });
    return flag;
}

</script>
<table id='doc_level_tg'></table>
<div id='doc_level_addDialog'></div>

	
