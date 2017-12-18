<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var document_did;
var document_dg;
var document_levelTg;

$(function(){
    document_did = lnyw.tab_options().did;

	$('#doc_document_layout').layout({
		fit : true,
		border : false,
	});

    //wwy
    //初始化档案类别列表
    document_levelTg = $('#doc_document_levelTg').treegrid({
        url:'${pageContext.request.contextPath}/doc/levelAction!treegrid.action',
        idField: 'id',
        treeField: 'levelName',
        queryParams: {
            catId: '02'
        },
        fit: true,
        columns:[[
            {field:'id',title:'编号',width:180, hidden: true},
            {field:'levelName',title:'名称',width:300},
            {field:'dir',title:'路径',hidden: true}
        ]],
        onSelect: function(row){
            clearPics();
            document_dg.datagrid('load', {
                levelId: row.id,
				catId: '02'
			});
        }
    });

    document_dg = $('#doc_document_dg').datagrid({
		url: '${pageContext.request.contextPath}/doc/entryAction!docsDatagrid.action',
		fit : true,
	    border : false,
	    singleSelect : true,
		idField: 'id',
        pagination : true,
        pagePosition : 'bottom',
        pageSize : pageSize,
        pageList : pageList,
		columns:[[
	        {field:'id',title:'编号', hidden:true},
	        {field:'orderNum',title:'件号',width:30},
	        {field:'owner',title:'责任者', width: 100},
	        {field:'fileno',title:'文号', width: 100},
	        {field:'entryName',title:'题名', width: 400},
	        {field:'recordTime',title:'形成日期', width: 80, formatter: lnyw.dateFormatter},
	        {field:'createTime',title:'录入日期', width: 80, formatter: lnyw.dateFormatter},
	        {field:'dir',title:'路径', width: 80},
//	        {field:'sort',title:'类型',width:150},
	        {field:'bz',title:'备注',width:150},
	        {field:'volume',title:'卷号',width:50},
	        {field:'pageCount',title:'页数',width:50},
	        {field:'levelId',title:'类别id',hidden: true}
	    ]],
	    toolbar:'#doc_document_tb',
        onClickRow: function(index, row){
	    	$('#doc_document_upload').css('display', 'block');
            reloadImg(row.id);
	    }
	});
    //根据权限，动态加载功能按钮
    lnyw.toolbar(0, document_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', document_did);

    //wwy
	//上传文档图片
	$(':file[name="upload"]').change(function(){
		$('#img_form').form('submit', {
		    url: '${pageContext.request.contextPath}/doc/uploadAction!upload.action',
			onSubmit: function(param){
		        param.dirName = getFilePath();
            },
            success: function(data){
                var json = $.parseJSON(jxc.toJson(data));
                if(json.success){
                    $.ajax({
                        url : '${pageContext.request.contextPath}/doc/imgAction!add.action',
                        data : {
                            entryId : document_dg.datagrid('getSelected').id,
                            filePath: json.obj.filePath,
							catId: '02'
                        },
                        cache : false,
                        dataType : 'JSON',
                        success : function(d) {
                            addImg(d.obj);
                            refreshCount(document_dg.datagrid('getSelected'), 1);
                        }
                    });
                }
                $.messager.show({
                    msg : json.msg,
                    title : '提示'
                });
            }
		});
    });

	//初始化信息
	init();

});

//以下为初始化
function init(){
	//清空全部字段
	$('input').val('');
}

function changeDgSelect(){
    var opts = document_dg.datagrid('options');
    opts.singleSelect = !opts.singleSelect;
}

function getEntryId(){
    return document_dg.datagrid("getSelected") ? document_dg.datagrid("getSelected").id : 0;
}

//wwy
//获取上传图片保存路径
function getFilePath(){
    return '/documents/' + getLevelPath(document_levelTg.treegrid('getSelected')) + '/' + document_dg.datagrid('getSelected').dir + '/';
}

//wwy
//获取每层level路径
function getLevelPath(node){
    var dir = node.dir;
    var parent = document_levelTg.treegrid('getParent', node.id);
    if(parent){
        dir = getLevelPath(parent) + '/' + dir;
    }
    return dir;
}

//wwy
//刷新页面数量
function refreshCount(row, count){
    document_dg.datagrid("updateRow", {
        index: document_dg.datagrid("getRowIndex", row),
        row:{
            pageCount: row.pageCount + count
        }
    });
//    if(document_levelTg.treegrid('getParent', row.id)){
//		refreshCount(document_levelTg.treegrid('getParent', row.id), count);
//	}
}

//wwy
//在图片显示div显示图片
function addImg(img){
    if($('#doc_document_pics img').length % parseInt($('#doc_document_pics').width() / 240 == 0)){
        $('#doc_document_pics').append("<br/>");
	}
    $('#doc_document_pics').append("<img src='" + img.filePath + "?v=" + moment().format('YYYYMMDDHHmmssSSS') + "' onclick=showImgInDoc('" + img.id + "') height='300' width='240'/>");
}

//wwy
//重新装载图片
function reloadImg(entryId){
    clearPics();
    $.ajax({
        url : '${pageContext.request.contextPath}/doc/imgAction!getImgsByEntry.action',
        data : {
            entryId : entryId,
			catId: '02'
        },
        cache : false,
        dataType : 'JSON',
        success : function(data) {
            if(data) {
                $.each(data, function (index, item) {
                    addImg(item);
                });
            }
        }
    });
}

function clearPics(){
    $('#doc_document_pics').html('');
}

//wwy
//单击图片后，弹出窗口显示原图
function showImgInDoc(imgId){
    $("#doc_document_img").dialog({
        title: '查看信息',
        width: $(window).width() * 0.6,
        height: $(window).height(),
        closed: false,
        cache: false,
        href: '${pageContext.request.contextPath}/doc/imgShowInDoc.jsp',
        modal: true,
        onLoad: function(){
            $.ajax({
                url : '${pageContext.request.contextPath}/doc/imgAction!getImg.action',
                data : {
                    id : imgId,
                },
                cache : false,
				async: false,
                dataType : 'JSON',
                success : function(data) {
                    $('#doc_img').html('');
                    $('#doc_img_id').val(data.obj.id);
                    $('#doc_img_bz').val(data.obj.bz);
                    $('#doc_img_crux').val(data.obj.crux);
                    $('#doc_img_orderNum').val(data.obj.orderNum);
                    var img_html = '<img src="' + data.obj.filePath + '?v=' + moment().format('YYYYMMDDHHmmssSSS') + '" style="width:100%;"/>';
                    $('#doc_img').html(img_html);
                }
            });
		}
    });
}

//wwy
//保存图片属性
function saveImg(){
    $('#doc_document_img form').form('submit', {
        url: '${pageContext.request.contextPath}/doc/imgAction!edit.action',
        success : function(d) {
            var json = $.parseJSON(jxc.toJson(d));
            if (json.success) {
                //刷新图片位置
				reloadImg(getEntryId());
            }
            $.messager.show({
                msg : json.msg,
                title : '提示'
            });
        }
	});
}

//wwy
//删除图片
function deleteImg(){
    $.messager.confirm('询问', '您确定要删除本页面？', function(b) {
        if (b) {
            $.ajax({
                url : '${pageContext.request.contextPath}/doc/imgAction!delete.action',
                data : {
                    id : $('#doc_img_id').val()
                },
                cache : false,
                dataType : 'JSON',
                success : function(r) {
                    if (r.success) {
                        reloadImg(getEntryId());
                        refreshCount(document_dg.datagrid('getSelected'), -1);
                        $("#doc_document_img").dialog('close');
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

//wwy
//增加条目
function addDocment(){
	var row = document_levelTg.treegrid('getSelected');
	if(row){
		document_levelTg.treegrid('expand', row.id);
		setTimeout(function(){
			if (document_levelTg.treegrid('getChildren', row.id).length == 0){
				//|| document_levelTg.treegrid('getChildren', row.id)[0].type == 'entry'){
				var entryAdd = $('#doc_document_addEntryDialog');
				entryAdd.dialog({
					title : '增加条目',
					href : '${pageContext.request.contextPath}/doc/entryAdd.jsp',
					width : 350,
					height : 300,
					buttons : [ {
						text : '确定',
						handler : function() {
							var f = entryAdd.find('form');
							f.form('submit', {
								url : '${pageContext.request.contextPath}/doc/entryAction!add.action',
								onSubmit: function(param){
									var dir = $('input#doc_entry_dir').val();
									if(checkEntryDir(0, row.id, dir)){
										$.messager.alert('提示', '输入的路径已存在，请重新输入！', 'error');
										return false;
									}
									param.personId = 0;
									param.levelId = row.id;
								},
								success : function(d) {
									var json = $.parseJSON(jxc.toJson(d));
									if (json.success) {
										document_dg.datagrid('appendRow', json.obj);
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
						$('.entry_levelId').css("display", 'none');
                        $('.entry_document').css("display", 'table-row');
                        <%--$('input[name=sort]').combobox({--%>
							<%--url: '${pageContext.request.contextPath}/doc/sort.json',--%>
							<%--valueField: 'text',--%>
							<%--textField: 'text',--%>
                            <%--editable: false--%>
						<%--}).combobox('selectedIndex', 0);--%>
						$('input[name=entryName]').focus();

					}
				});
			}else{
				$.messager.alert('警告', '必须选择最低类别后才能增加条目，请选择！', 'warning');
			}
		},500);
	}else{
		$.messager.alert('警告', '必须选择某个类别后才能增加条目，请选择！', 'warning');
	}
}

//wwy
//修改条目
function editDocument(){
    if(document_dg.datagrid('getSelections').length == 1){
		var row = document_dg.datagrid('getSelected');
		if(row){
			var entryDir = row.dir;
			var entryAdd = $('#doc_document_addEntryDialog');
			entryAdd.dialog({
				title : '编辑条目',
				href : '${pageContext.request.contextPath}/doc/entryAdd.jsp',
				width : 550,
				height : 340,
				buttons : [ {
					text : '确定',
					handler : function() {
						var f = entryAdd.find('form');
						f.form('submit', {
							url : '${pageContext.request.contextPath}/doc/entryAction!edit.action',
							onSubmit: function(param){
								if(entryDir != $('input#doc_entry_dir').val() || row.levelId != $('#doc_entry_levelId').combotree('tree').tree('getSelected').id) {
									var dir = $('input#doc_entry_dir').val();

									if (checkEntryDir(0, $('#doc_entry_levelId').combotree('tree').tree('getSelected').id, dir)) {
										$.messager.alert('提示', '输入的路径已存在，请重新输入！', 'error');
										return false;
									}
								}
								param.personId = 0;
								param.catId = '02';
							},
							success : function(d) {
								var json = $.parseJSON(jxc.toJson(d));
								if (json.success) {
									var levelId = row.levelId;

									//改变所属类别后，移除
									if(levelId != json.obj.levelId){
										document_dg.datagrid('deleteRow', document_dg.datagrid('getRowIndex', row));
									}else{
										json.obj.pageCount = row.pageCount;
										document_dg.datagrid('updateRow',
											{
												index: document_dg.datagrid('getRowIndex', row),
												row: json.obj
											}
										);

									}

									clearPics();
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
					$('.entry_document').css("display", 'table-row');
					var f = entryAdd.find('form');
                    <%--$('input[name=sort]').combobox({--%>
                        <%--url: '${pageContext.request.contextPath}/doc/sort.json',--%>
                        <%--valueField: 'text',--%>
                        <%--textField: 'text',--%>
                        <%--editable: false--%>
                    <%--});--%>
					var table = f.find('table');
					$(table).append(
						'<tr class="entry_levelId">' +
						'<th><label for="levelId">类别</label></th>' +
						'<td><input name="levelId" id="doc_entry_levelId" style="width: 160px;"></td>' +
						'</tr>'
					);
					var levelId = f.find('input[name=levelId]');
					levelId.combotree({
						lines : true,
						url : '${pageContext.request.contextPath}/doc/levelAction!getLevelTree.action?catId=02',
						onLoadSuccess : function() {
							$.messager.progress('close');
						},
						onBeforeSelect: function(node) {
							if (!$(this).tree('isLeaf', node.target)) {
								$.messager.alert('提示', '请选择最低类别！', 'error');
								return false;
							}
						}
					});
					f.form('load', row);

					$('input[name=entryName]').focus();
				}
			});
		}else{
			$.messager.alert('警告', '必须选择某个条目后才能进行编辑，请选择！', 'warning');
		}
    }else{
        $.messager.alert('警告', '只能选择一个条目进行编辑，请选择！', 'warning');
    }
}

//wwy
//删除条目
function removeDocument(){
    if(document_dg.datagrid('getSelections').length == 1){
		var row = document_dg.datagrid('getSelected');
		if(row){
			if(row.pageCount == 0){
				$.messager.confirm('询问', '您确定要删除本条目？', function(b) {
					if (b) {
						$.ajax({
							url : '${pageContext.request.contextPath}/doc/entryAction!delete.action',
							data : {
								id : row.id,
								catId: '02'
							},
							cache : false,
							dataType : 'JSON',
							success : function(r) {
								if (r.success) {
									clearPics();
									document_dg.treegrid('deleteRow', document_dg.datagrid('getRowIndex', row));
								}
								$.messager.show({
									msg : r.msg,
									title : '提示'
								});
							}
						});
					}
				});
			}else{
				$.messager.alert('警告', '选择的条目有图片内容，不能删除，请选择！', 'warning');
			}
		}else{
			$.messager.alert('警告', '必须选择某个条目后才能进行编辑，请选择！', 'warning');
		}
    }else{
        $.messager.alert('警告', '只能选择一个条目进行删除，请选择！', 'warning');
    }
}

function printEntrys(){
    var rows = document_dg.datagrid('getSelections');
    if(rows){
		$.messager.confirm('询问', '您确定要打印目录？', function(b) {
			if (b) {
			    var ids = []
			    $.each(rows, function(){
			        ids.push(this.id);
				});
				var url = lnyw.bp() + '/doc/entryAction!printEntrys.action?ids=' + ids.join(',');
				jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW);
			}
		});
    }else{
        $.messager.alert('警告', '必须选择至少一个条目后才能打印，请选择！', 'warning');
    }
}

function printImg(){
    $('#doc_img img').jqprint();
}

function checkEntryDir(documentId, levelId, dir){
    var flag = false;
    $.ajax({
        url : '${pageContext.request.contextPath}/doc/entryAction!checkDir.action',
        data : {
            documentId : documentId,
            levelId: levelId,
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

//////////////////////////////////////////////以上为处理代码

</script>

<div id='doc_document_layout' style="height:100%;width:100%">
	<div data-options="region:'west',title:'类别',split:true" style="height:100%;width:200px">
		<div id="doc_document_levelTg"></div>
	</div>
	<div data-options="region:'center',title:'',split:true, fit:true" style="height:100%;width:100%">
		<div id='doc_document_detail_layout' style="height:100%;width:100%">
			<div data-options="region:'north',title:'',split:true" style="height:400px;width:100%">
				<div id="doc_document_dg"></div>
			</div>
			<div data-options="region:'center',title:'上传文档',split:true" style="height:60%;width:100%">
				<div id="doc_document_upload" style="display: none;">
					<form id="img_form" method="post" enctype="multipart/form-data">
						<input name="upload" type="file" />
					</form>
					<br/>
					<br/>
					<div id="doc_document_pics"></div>
				</div>
			</div>
		</div>
	</div>
</div>

//wwy
//档案分类工具栏
<div id="doc_document_tb">
	<input type="checkbox" id="doc_document_multi" onclick="changeDgSelect()">多选</input>
</div>


//wwy
//增加条目对话框
<div id='doc_document_addEntryDialog'></div>

//wwy
//显示图片
<div id="doc_document_img"></div>