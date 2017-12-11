<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var person_did;
var person_dg;
var person_levelTg;
var doc_person_tabs;
var doc_person_depCombo;
var doc_person_bmbh;
var doc_pathEname;
var doc_add_flag = false;

$(function(){
	person_did = lnyw.tab_options().did;

	$('#doc_person_layout').layout({
		fit : true,
		border : false,
	});
	
	$('#doc_person_depLayout').layout({
		fit : true,
		border : false,
	});

    //初始化部门列表
    doc_person_depCombo = lnyw.initCombo($("#doc_person_depId"), 'id', 'depName', '${pageContext.request.contextPath}/admin/departmentAction!listDeps.action');
    doc_person_bmbh = lnyw.initCombo($("#doc_person_bmbh"), 'id', 'depName', '${pageContext.request.contextPath}/admin/departmentAction!listDeps.action');

    person_dg = $('#doc_person_dg').datagrid({
		url: null,
		fit : true,
	    border : false,
	    singleSelect : true,
		idField: 'id',
		columns:[[
	        {field:'id',title:'编号',width:60,hidden:true},
	        {field:'orderNum',title:'序号',width:60},
	        {field:'name',title:'姓名',width:80},
            {field:'ename',title:'路径',hidden:true},
	    ]],
	    toolbar:'#doc_person_tb',
	    onSelect: function(rowIndex, rowData){
	    	if(doc_person_tabs.is(':hidden')){
				doc_person_tabs.show();
	    	}
	   
			if(getTabIndex() == 0){
				getPerson(rowData.id);
			}
		    if(getTabIndex() == 1){
                $('#doc_person_upload').css('display', 'none');
			    person_levelTg.treegrid('load', {
			        personId: rowData.id,
					catId: '01'
				});
			}
	    }
	});
	
	doc_person_depCombo.combobox({
		onSelect: function(){
		    person_dg.datagrid('load', {
		 		bmbh : doc_person_depCombo.combobox('getValue'),
                onLoadSuccess: function(data){
                    if(!data){
                        var len = person_dg.datagrid('getRows').length;
 		    			for(var i = 0; i < len ; i++){
               				person_dg.datagrid('deleteRow', 0);
            			}
            			return false;
                    }
                },
		 	});
			$("#doc_person").find("input").val('');
            doc_person_tabs.hide();
		}

	});
	

	//wwy
	//初始化档案类别列表
    person_levelTg = $('#doc_person_levelTg').treegrid({
        url:'${pageContext.request.contextPath}/doc/entryAction!getLevelEntries.action',
        idField: 'id',
        treeField: 'entryName',
        queryParams: {
            personId: getPersonId(),
			catId: '01'
		},
        fit: true,
        columns:[[
            {field:'id',title:'编号',width:180, hidden: true},
            {field:'entryName',title:'名称',width:300},
            {field:'orderNum',title:'排序',width:40},
            {field:'dir',title:'路径',width:80},
            {field:'bz',title:'备注',width:150},
            {field:'createTime',title:'创建时间',
                formatter: function(value){
					if(value) {
						return moment(value).format('YYYY-MM-DD');
					}
                }},
            {field:'recordTime',title:'建档时间',
			formatter: function(value){
                if(value) {
                    return moment(value).format('YYYY-MM-DD');
                }
			}},
            {field:'pageCount',title:'页数',width:40},
            {field:'type',title:'类型', hidden:true},
            {field:'levelId',title:'类别', hidden:true}
        ]],
		onSelect: function(row){
            if(row.type === 'entry'){
                reloadImg(row.id);
                $('#doc_person_upload').css('display', 'block');
            }else{
                $('#doc_person_upload').css('display', 'none');
            }
        },
		onExpand: function(){
            clearPics();
        },
        onCollapse: function(){
            clearPics();
        }
    });
    //根据权限，动态加载功能按钮
    lnyw.toolbar(1, person_levelTg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', person_did);




    //wwy
    //选中标签后，装载数据
    doc_person_tabs = $('#doc_person_tabs').tabs({
        onSelect: function(title, index){
            if(index == 0){
				//基本信息部分
            	getPerson(getPersonId());

				
            }
            if(index == 1){
                //档案材料部分
                $('#doc_person_upload').css('display', 'none');
                person_levelTg.treegrid('load', {
                    personId: getPersonId(),
					catId: '01'
				});
            }
        }
    });

    //初始化隐藏tabs信息 sungj
    doc_person_tabs.hide();

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
                            entryId : getEntryId(),
                            filePath: json.obj.filePath
                        },
                        cache : false,
                        dataType : 'JSON',
                        success : function(d) {
                            addImg(d.obj);
                            refreshCount(person_levelTg.treegrid('getSelected'), 1);
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
    doc_person_depCombo.combobox('selectedIndex', 0);

    //wwy
    //按默认部门刷新人员列表
    var intervalId = setInterval(function() {
        if(doc_person_depCombo.combobox('getValue') != ''){
            var opts = person_dg.datagrid('options');
            opts.url = '${pageContext.request.contextPath}/doc/personAction!listByDep.action';
            opts.queryParams = {bmbh : doc_person_depCombo.combobox('getValue')};
            person_dg.datagrid(opts);
            clearInterval(intervalId);
		}
    }, 1000);
}

/***
 * 加载下拉列表
 */
function selectList(target, pam, bol){
    target.combobox({
        url : '${pageContext.request.contextPath}/doc/personAction!getSelect.action',
        valueField:'param',
        textField:'param',
        editable:bol,
        onBeforeLoad: function (param) {
            param.param = pam;
        },
    });
}

function getTabIndex(){
    return doc_person_tabs.tabs('getTabIndex',doc_person_tabs.tabs('getSelected'));
}

function getPersonId(){
    return person_dg.datagrid("getSelected") ? person_dg.datagrid("getSelected").id : 0;
}

function getEntryId(){
    return person_levelTg.treegrid("getSelected") ? person_levelTg.treegrid("getSelected").id : 0;
}

//wwy
//获取上传图片保存路径
function getFilePath(){
    return '/' + person_dg.datagrid('getSelected').ename + '/' + getLevelPath(person_levelTg.treegrid('getSelected')) + '/';
}

//wwy
//获取每层level路径
function getLevelPath(node){
    var dir = node.dir;
    var parent = person_levelTg.treegrid('getParent', node.id);
    if(parent){
        dir = getLevelPath(parent) + '/' + dir;
    }
    return dir;
}

//wwy
//刷新页面数量
function refreshCount(row, count){
    person_levelTg.treegrid("update", {
        id: row.id,
        row:{
            pageCount: row.pageCount + count
        }
    });
    if(person_levelTg.treegrid('getParent', row.id)){
		refreshCount(person_levelTg.treegrid('getParent', row.id), count);
	}
}

//wwy
//在图片显示div显示图片
function addImg(img){
    if($('#doc_person_pics img').length % parseInt($('#doc_person_pics').width() / 240 == 0)){
        $('#doc_person_pics').append("<br/>");
	}
    $('#doc_person_pics').append("<img src='" + img.filePath + "' onclick=showImg('" + img.id + "') height='300' width='240'/>");
}

//wwy
//重新装载图片
function reloadImg(entryId){
    clearPics();
    $.ajax({
        url : '${pageContext.request.contextPath}/doc/imgAction!getImgsByEntry.action',
        data : {
            entryId : entryId
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
    $('#doc_person_pics').html('');
}

//wwy
//单击图片后，弹出窗口显示原图
function showImg(imgId){
    $("#doc_person_img").dialog({
        title: '查看信息',
        width: $(window).width() * 0.6,
        height: $(window).height(),
        closed: false,
        cache: false,
        href: '${pageContext.request.contextPath}/doc/imgShow.jsp',
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
                    $('#person_img').html("");
                    $('#doc_img_id').val(data.obj.id);
                    $('#doc_img_bz').val(data.obj.bz);
                    $('#doc_img_crux').val(data.obj.crux);
                    $('#doc_img_orderNum').val(data.obj.orderNum);
                    var img_html = '<img src="' + data.obj.filePath + '" style="width:100%;"/>';
    				$('#person_img').html(img_html);
                }
            });

		}
    });
}
//wwy
//保存图片属性
function saveImg(){
    $('#doc_person_img form').form('submit', {
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
                        refreshCount(person_levelTg.treegrid('getSelected'), -1);
                        $("#doc_person_img").dialog('close');
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

//初始化新建页面  sungj
function addPerson(){
    doc_add_flag = true;
	doc_person_tabs.show();
	//判断是否停留在人员页面 
	if(getTabIndex() != 0){
		 doc_person_tabs.tabs('select',0);
	}

	$("#doc_person").find("input").val('');
	person_dg.datagrid('clearSelections');
	$("#doc_person_sex").combobox('selectedIndex', 1);

    var bmbh=doc_person_depCombo.combobox('getValue');
    doc_person_bmbh.combobox({
	    editable:false,
	});
    doc_person_bmbh.combobox('select', bmbh);

    loadSelectList();
	doc_pathEname="";
	$('#doc_person_outJobCount').val(0);
	$('#doc_person_orderNum').val(0);
}


//获取人员信息 sungj
function getPerson(personId){
	loadSelectList();
	$("#doc_person").find("input").val('');
	$.ajax({
		url : '${pageContext.request.contextPath}/doc/personAction!getPerson.action',
		async: false,
		cache: false,
		dataType : 'json',
		data:{
			id: personId
		},
		success: function(data) {
			$('#doc_person_form').form('load', dateFormatter(data));
			doc_pathEname=data.ename;
		}
	});
}

function dateFormatter(data){
    data.birthTime = data.birthTime ? moment(data.birthTime).format('YYYY-MM-DD') : '';
    data.joinPartyTime = data.joinPartyTime ? moment(data.joinPartyTime).format('YYYY-MM-DD') : '';
    data.formalTime = data.formalTime ? moment(data.formalTime).format('YYYY-MM-DD') : '';
    data.fullEntranceTime = data.fullEntranceTime ? moment(data.fullEntranceTime).format('YYYY-MM-DD') : '';
    data.fullGraduationTime = data.fullGraduationTime ? moment(data.fullGraduationTime).format('YYYY-MM-DD') : '';
    data.nowRankTime =  data.nowRankTime ? moment(data.nowRankTime).format('YYYY-MM-DD') : '';
    data.getRankTime = data.getRankTime ? moment(data.getRankTime).format('YYYY-MM-DD') : '';
    data.jobTime = data.jobTime ? moment(data.jobTime).format('YYYY-MM-DD') : '';
    data.jtJobTime = data.jtJobTime ? moment(data.jtJobTime).format('YYYY-MM-DD') : '';
    data.companyTime = data.companyTime ? moment(data.companyTime).format('YYYY-MM-DD') : '';
    data.socialPayTime = data.socialPayTime ? moment(data.socialPayTime).format('YYYY-MM-DD') : '';

    return data;
}

function loadSelectList(){
	selectList($('#doc_person_sex'),'sex',true);
	selectList($('#doc_person_post'),'postName',true);
	selectList($('#doc_person_nation'),'nation',true);
	selectList($('#doc_person_fullSchool'),'fullSchool',true);
	selectList($('#doc_person_fullMajor'),'fullMajor',true);
	selectList($('#doc_person_fullEducation'),'fullEducation',true);
	selectList($('#doc_person_fullDegree'),'fullDegree',true);
	selectList($('#doc_person_jobSchool'),'jobSchool',true);
	selectList($('#doc_person_jobMajor'),'jobMajor',true);
	selectList($('#doc_person_jobEducation'),'jobEducation',true);
	selectList($('#doc_person_jobDegree'),'jobDegree',true);
	selectList($('#doc_person_rankName'),'rankName',true);
	selectList($('#doc_person_bestEducation'),'bestEducation',true);


}
//提交前检查
function checkPerson(){
	if(!($('#doc_person_ename').val() == doc_pathEname)){
		if(checkPath($('#doc_person_ename').val())){
			$.messager.alert('提示','该路径已经存在!请重新输入!','error');
			return false;
		}
	}
	savePerson();
}

//判断路径是否存在
function checkPath(ename){
	var flag;
	$.ajax({
		url : '${pageContext.request.contextPath}/doc/personAction!checkEname.action',
		async: false,
		cache: false,
		dataType : 'json',
		data:{
			ename: ename
		},
		success: function(d) {
			flag = d;
		}
	});
	return flag;
}

//保存数据到后台  sungj
function savePerson(){
	if(getPersonId() > 0){
	    $.messager.confirm('提示', '您是否要保存人员信息', function(r){
    	    if (r){
				var selectedPerson = person_dg.datagrid('getSelected');
				var bmbh = '';
				$('#doc_person_form').form('submit', {
					url: '${pageContext.request.contextPath}/doc/personAction!edit.action',
					onSubmit: function(param){
						bmbh = doc_person_bmbh.combobox('getValue');
						param.id = getPersonId();
						return $(this).form('enableValidation').form('validate');
					},
					success: function(d){
						var json = $.parseJSON(jxc.toJson(d));

						doc_person_depCombo.combobox('select', bmbh);

						var index = person_dg.datagrid('getRowIndex', selectedPerson);
						person_dg.datagrid('selectRow',index);

                        doc_add_flag = false;
                        $.messager.show({
							msg : json.msg,
							title : '提示'
						});
					}
				});
			}
	    });
	}else{
	    if(doc_add_flag){
			$.messager.confirm('提示', '您是否要新增人员信息', function(r){
				if (r){
					$('#doc_person_form').form('submit', {
						url: '${pageContext.request.contextPath}/doc/personAction!add.action',
						onSubmit: function(){
							return $(this).form('enableValidation').form('validate');
						},
						success: function(d){
							var json = $.parseJSON(jxc.toJson(d));

							doc_person_depCombo.combobox('select', json.obj.bmbh);

							var selected = person_dg.datagrid('getRows')[0];
							selected.id = json.obj.id;
							var index = person_dg.datagrid('getRowIndex', selected);
							person_dg.datagrid('selectRow', index);
                            doc_add_flag = false;
							 $.messager.show({
								 msg : json.msg,
								 title : '提示'
							 });
						}
					});
				}
			});
		}else{
            $.messager.alert('提示','新增人员请点击【增加人员】按钮后操作','error');
        }
	}
}

function showMessage(f){
	var flag=false;
	if(f){
		$.messager.confirm('提示', '您是否要保存人员信息', function(r){
			if(r){
				flag=true;
			}
		});
	}
	return flag;
}

//人员信息删除 sung
function removePerson(){
	if(!checkEntry()){
		$.messager.confirm('提示', '您是否要删除人员信息', function(r){
			if (r){
				var bmbh=$('#doc_person_bmbh').combobox('getValue');
				if(getPersonId() > 0){
					$.ajax({
						url : '${pageContext.request.contextPath}/doc/personAction!delete.action',
						async: false,
						cache: false,
						dataType : 'json',
						data:{
							id: getPersonId()
						},
						success: function(d) {
							 addPerson();
							 doc_person_depCombo.combobox('select', bmbh);
							 doc_person_tabs.hide();
							 $.messager.show({
								 msg : d.msg,
								 title : '提示'
							 });
						}
					});
				}
			}
		});
	}else{
		$.messager.alert('提示','选中人员含有档案材料信息不能删除，请重新选择!','error');
		return false;
	}
}
//判断人员下是否包含条目
function checkEntry(){
	var flag;
		$.ajax({
			url : '${pageContext.request.contextPath}/doc/personAction!checkEntry.action',
			async: false,
			cache: false,
			dataType : 'json',
			data:{
				id: getPersonId()
			},
			success: function(d) {
				flag = d;
			}
		});
		return flag;
}

//wwy
//增加条目
function addEntry(){
    if(person_dg.datagrid("getSelected")){
        var row = person_levelTg.treegrid('getSelected');
        if(row && row.type == 'level_print'){
            person_levelTg.treegrid('expand', row.id);
            setTimeout(function(){
				if (person_levelTg.treegrid('getChildren', row.id).length == 0 || person_levelTg.treegrid('getChildren', row.id)[0].type == 'entry'){
					var entryAdd = $('#doc_person_addEntryDialog');
					entryAdd.dialog({
						title : '增加条目',
						href : '${pageContext.request.contextPath}/doc/entryAdd.jsp',
						width : 550,
						height : 300,
						buttons : [ {
							text : '确定',
							handler : function() {
								var f = entryAdd.find('form');
								f.form('submit', {
									url : '${pageContext.request.contextPath}/doc/entryAction!add.action',
									onSubmit: function(param){
                                        var dir = $('input#doc_entry_dir').val();
                                        if(checkEntryDir(getPersonId(), row.levelId, dir)){
                                            $.messager.alert('提示', '输入的路径已存在，请重新输入！', 'error');
                                            return false;
                                        }
										param.personId = getPersonId();
										param.levelId = row.levelId;
									},
									success : function(d) {
										var json = $.parseJSON(jxc.toJson(d));
										if (json.success) {
                                            person_levelTg.treegrid('append', {
                                                parent: row.id,
												data: [json.obj]
											});
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
							entryAdd.find('form');
							$('input[name=entryName]').focus();
							$('.entry_levelId').css("display", 'none');
						}
					});
				}else{
					$.messager.alert('警告', '必须选择最低类别后才能增加条目，请选择！', 'warning');
				}
            },500);
        }else{
            $.messager.alert('警告', '必须选择某个类别后才能增加条目，请选择！', 'warning');
		}
	}else{
        $.messager.alert('警告', '必须先选择某个人员后才能增加条目，请选择！', 'warning');
	}
}

//wwy
//修改条目
function editEntry(){
    if(person_dg.datagrid("getSelected")){
        if(person_levelTg.treegrid('getSelected') && person_levelTg.treegrid('getSelected').type == 'entry'){
            var row = person_levelTg.treegrid('getSelected');
            var entryDir = row.dir;
            var entryAdd = $('#doc_person_addEntryDialog');
            entryAdd.dialog({
                title : '编辑条目',
                href : '${pageContext.request.contextPath}/doc/entryAdd.jsp',
                width : 550,
                height : 300,
                buttons : [ {
                    text : '确定',
                    handler : function() {
                        var f = entryAdd.find('form');
                        f.form('submit', {
                            url : '${pageContext.request.contextPath}/doc/entryAction!edit.action',
                            onSubmit: function(param){
                                if(entryDir != $('input#doc_entry_dir').val()) {
                                    var dir = $('input#doc_entry_dir').val();

                                    if (checkEntryDir(getPersonId(), row.levelId, dir)) {
                                        $.messager.alert('提示', '输入的路径已存在，请重新输入！', 'error');
                                        return false;
                                    }
                                }
                                param.personId = getPersonId();
                                param.catId = '01';
                                //param.levelId = row.levelId;
                            },
                            success : function(d) {
                                var json = $.parseJSON(jxc.toJson(d));
                                if (json.success) {
                                    var levelId = row.levelId;
                                    person_levelTg.treegrid('update', {
                                        id: row.id,
										row: json.obj
									});

                                    //改变所属类别后，移除
                                    if(levelId != json.obj.levelId){
                                        person_levelTg.treegrid('remove', row.id);
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
                    var f = entryAdd.find('form');
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
                        url : '${pageContext.request.contextPath}/doc/levelAction!getLevelTree.action?catId=01',
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
        $.messager.alert('警告', '必须先选择某个人员后才能增加条目，请选择！', 'warning');
    }
}

//wwy
//删除条目
function removeEntry(){
    if(person_dg.datagrid("getSelected")){
        var row = person_levelTg.treegrid('getSelected');
        if(row && row.type == 'entry'){
            if(row.pageCount == 0){
                $.messager.confirm('询问', '您确定要删除本条目？', function(b) {
                    if (b) {
                        $.ajax({
                            url : '${pageContext.request.contextPath}/doc/entryAction!delete.action',
                            data : {
                                id : row.id
                            },
                            cache : false,
                            dataType : 'JSON',
                            success : function(r) {
                                if (r.success) {
                                    clearPics();
                                    person_levelTg.treegrid('remove', row.id);
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
        $.messager.alert('警告', '必须先选择某个人员后才能增加条目，请选择！', 'warning');
    }
}

function printLevel(){
    var selected = person_levelTg.treegrid('getSelected');
    if(selected){
	    if(selected.type == 'level_print'){
			$.messager.confirm('询问', '您确定要打印类别目录？', function(b) {
				if (b) {
                	var url = lnyw.bp() + '/doc/levelAction!printLevel.action?id=' + selected.id + "&personId=" + getPersonId();
                    jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW);
				}
			});
		}else{
            $.messager.alert('警告', '必须选择底层类别后才能打印，请选择！', 'warning');
        }
    }else{
        $.messager.alert('警告', '必须选择某个类别后才能打印，请选择！', 'warning');
    }
}

function printImg(){
    $('#person_img img').jqprint();
}

function checkEntryDir(personId, levelId, dir){
    var flag = false;
    $.ajax({
        url : '${pageContext.request.contextPath}/doc/entryAction!checkDir.action',
        data : {
            personId : personId,
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

<div id='doc_person_layout' style="height:100%;width:100%">
	<div data-options="region:'west',title:'部门-人员',split:true" style="height:100%;width:200px">
		<div id='doc_person_depLayout' style="height:100%;width:100%">
			<div data-options="region:'north',title:'部门',split:true" style="height:80px;width:100%">
				请选择部门：<input id="doc_person_depId" name="depId" size="16">
			</div>
			<div data-options="region:'center',title:'人员',split:true" style="height:100%;width:100%">
				<div id="doc_person_dg"></div>
			</div>
		</div>
	</div>
	<div data-options="region:'center',title:'', split:true, fit:true">
		<div id="doc_person_tabs" class="easyui-tabs" data-options="border:false, fit:true">
			<div id="doc_person" title="基本信息" data-options="closable:false">
				<%--<div id="doc_person">--%>
					<a href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'" onclick="checkPerson();">保存</a>
					<a href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'" onclick="removePerson();">删除</a>
					<form id='doc_person_form' method="post">
						<table id="doc_person_table" class="doc_person_table_class">
							<tr>
								<td class="doc_person_th">&nbsp;</td>
							</tr>
							<tr>
								<td class="doc_person_th"><span class="doc_person_span">**基本信息**</span></td>
							</tr>
							<tr>
								<td colspan="6"><hr style="height:1px;border:none;border-top:1px dashed red;" /></td>
							</tr>
							<tr>
								<td class="doc_person_th"><span class="doc_person_span">部门:</span><br/><input id="doc_person_bmbh" name="bmbh"  style="width:138px"></td>
								<td class="doc_person_th"><span class="doc_person_span">岗位:</span><br/><input id="doc_person_post" name="postName"  style="width:138px"></td>
								<td class="doc_person_th"><span class="doc_person_span">序号:</span><br/><input id="doc_person_orderNum" name="orderNum"  class="easyui-numberbox" style="width:138px"></td>
								<td class="doc_person_th"><span class="doc_person_span">路径:</span><br/><input class="easyui-validatebox" type="text" name="ename" id="doc_person_ename" data-options="required:true,missingMessage:'请填写路径'" style="width:138px"/></td>
							</tr>
							<tr>
								<td  class="doc_person_th"><span class="doc_person_span">姓名:</span><br/><input class="easyui-validatebox" type="text" name="name"id="doc_person_name" data-options="required:true,missingMessage:'请填写姓名'" style="width:138px"/></td>
								<td  class="doc_person_th">
									<span class="doc_person_span">性别:</span><br/>
									<input id="doc_person_sex" name="sex" style="width:138px">
								</td>
								<td class="doc_person_th"><span class="doc_person_span">出生日期:</span><br/><input name="birthTime" class="easyui-datebox" style="width:138px" ></td>
								<td class="doc_person_th"><span class="doc_person_span">民族:</span><br/>
									<input id="doc_person_nation" name="nation" style="width:138px">
								</td>
							</tr>
							<tr>
								<td class="doc_person_th"><span class="doc_person_span">手机号:</span><br/><input class="easyui-numberbox" type="text" name="phone" style="width:133px" /></td>
								<td class="doc_person_th"><span class="doc_person_span">身份证号码:</span><br/><input type="text" name="idCard" style="width:133px"/></td>
								<td class="doc_person_th"><span class="doc_person_span">入党时间:</span><br/><input type="text" id='doc_person_joinPartyTime' name="joinPartyTime" class="easyui-datebox"  style="width:138px"></td>
								<td class="doc_person_th"><span class="doc_person_span">转正时间:</span><br/><input type="text" name="formalTime" class="easyui-datebox" style="width:138px"></td>
							</tr>
							<tr>
								<td class="doc_person_th"><span class="doc_person_span">备注:</span><br/><input  type="text" name="bz" style="width:133px" /></td>
							</tr>
							<tr>
								<td class="doc_person_th">&nbsp;</td>
							</tr>
							<tr>
								<td class="doc_person_th"><span class="doc_person_span">**全日制教育**</span></td>
							</tr>
							<tr>
								<td colspan="6"><hr style="height:1px;border:none;border-top:1px dashed red;" /></td>
							</tr>
							<tr>
								<td class="doc_person_th"><span class="doc_person_span">入学时间:</span><br/><input type="text" name="fullEntranceTime" class="easyui-datebox" style="width:138px"></td>			
								<td class="doc_person_th"><span class="doc_person_span">毕业时间:</span><br/><input type="text" name="fullGraduationTime" class="easyui-datebox" style="width:138px"></td>			
								
								<td class="doc_person_th"><span class="doc_person_span">毕业院校:</span><br/>
									<input id="doc_person_fullSchool" name="fullSchool" style="width:130px">
								</td>
								<td class="doc_person_th"><span class="doc_person_span">所学专业:</span><br/>
									<input id="doc_person_fullMajor" name="fullMajor" style="width:130px">
								</td>
								<td class="doc_person_th"><span class="doc_person_span">学历:</span><br/>
									<input id="doc_person_fullEducation" name="fullEducation" style="width:130px">
								</td>
								<td class="doc_person_th"><span class="doc_person_span">学位:</span><br/>
									<input id="doc_person_fullDegree" name="fullDegree" style="width:130px">
									
								</td>
							</tr>
							<tr>
								<td class="doc_person_th">&nbsp;</td>
							</tr>
							<tr>
								<td class="doc_person_th"><span class="doc_person_span">**在职教育**</span></td>
							</tr>
							<tr>
								<td colspan="6"><hr style="height:1px;border:none;border-top:1px dashed red;" /></td>
							</tr>
							<tr>
							<tr>
								<td class="doc_person_th"><span class="doc_person_span">毕业院校:</span><br/>
									<input id="doc_person_jobSchool" name="jobSchool" style="width:138px">
								</td>
								<td class="doc_person_th"><span class="doc_person_span">所学专业:</span><br/>
									<input id="doc_person_jobMajor" name="jobMajor" style="width:138px">
								</td>
								<td class="doc_person_th"><span class="doc_person_span">学历:</span><br/>
									<input id="doc_person_jobEducation" name="jobEducation" style="width:138px">
								</td>
								<td class="doc_person_th"><span class="doc_person_span">学位:</span><br/>
									<input id="doc_person_jobDegree" name="jobDegree" style="width:138px">
								</td>								
							</tr>
							<tr>
								<td class="doc_person_th">&nbsp;</td>
							</tr>
							<tr>
								<td class="doc_person_th"><span class="doc_person_span">**专业等级**</span></td>
							</tr>
							<tr>
								<td colspan="6"><hr style="height:1px;border:none;border-top:1px dashed red;" /></td>
							</tr>
							<tr>		
								<td class="doc_person_th"><span class="doc_person_span">最高学历:</span><br/>
									<input id="doc_person_bestEducation" name="bestEducation" style="width:138px">	
								</td>
							</tr>
							<tr>
								<td class="doc_person_th"><span class="doc_person_span">任现职级时间:</span><br/><input type="text" name="nowRankTime" class="easyui-datebox" style="width:138px"></td>
								<td class="doc_person_th"><span class="doc_person_span">职称:</span><br/>
									<input id="doc_person_rankName" name="rankName" style="width:138px">									
								</td>
								<td class="doc_person_th"><span class="doc_person_span">取得职称时间:</span><br/><input type="text" name="getRankTime" class="easyui-datebox" style="width:138px"></td>				
							</tr>
						
							<tr>
								<td class="doc_person_th">&nbsp;</td>
							</tr>
							<tr>
								<td class="doc_person_th"><span class="doc_person_span">**工作年限**</span></td>
							</tr>
							<tr>
								<td colspan="6"><hr style="height:1px;border:none;border-top:1px dashed red;" /></td>
							</tr>
							<tr>
								<td class="doc_person_th"><span class="doc_person_span">参加工作时间:</span><br/><input type="text" name="jobTime" class="easyui-datebox" style="width:138px"></td>			
								<td class="doc_person_th"><span class="doc_person_span">到集团工作时间:</span><br/><input type="text" name="jtJobTime" class="easyui-datebox" style="width:138px"></td>			
								<td class="doc_person_th"><span class="doc_person_span">到本单位工作时间:</span><br/><input type="text" name="companyTime" class="easyui-datebox"    style="width:138px"></td>
								<td class="doc_person_th"><span class="doc_person_span">无就业年限:</span><br/><input class="easyui-numberbox" type="text" id="doc_person_outJobCount" name="outJobCount"  style="width:133px"/></td>
							</tr>						
							<tr>
								<td class="doc_person_th">&nbsp;</td>
							</tr>
							<tr>
								<td class="doc_person_th"><span class="doc_person_span">**社会保障**</span></td>
							</tr>
							<tr>
								<td colspan="6"><hr style="height:1px;border:none;border-top:1px dashed red;" /></td>
							</tr>
							<tr>	
								<td class="doc_person_th"><span class="doc_person_span">个人社保编号:</span><br/><input class="easyui-numberbox" type="text" name="socialCard" style="width:133px" /></td>
								<td class="doc_person_th"><span class="doc_person_span">社保缴费时间:</span><br/><input type="text" name="socialPayTime" class="easyui-datebox" style="width:138px"></td>
								<td class="doc_person_th"><span class="doc_person_span">个人医保编号:</span><br/><input class="easyui-numberbox" type="text" name="medicalCard" style="width:133px"/></td>
								<td class="doc_person_th"><span class="doc_person_span">单位公积金号:</span><br/><input class="easyui-numberbox" type="text" name="companyHouseBankCard" style="width:133px"/></td>
								<td class="doc_person_th"><span class="doc_person_span">个人公积金号:</span><br/><input class="easyui-numberbox" type="text" name="houseBankCard" style="width:133px"/></td>
							</tr>
						</table>
					</form>
				<%--</div>--%>
			</div>
			<div title="档案材料" data-options="closable:false" >

				<div id='doc_person_levelLayout' style="height:100%;width:100%">
					<div data-options="region:'north',title:'目录',split:true" style="height:400px;width:100%">
						<div id="doc_person_levelTg"></div>
					</div>
					<div data-options="region:'center',title:'上传文档',split:true" style="height:60%;width:100%">

						<div id="doc_person_upload">
                            <form id="img_form" method="post" enctype="multipart/form-data">
                                <input name="upload" type="file" />
                            </form>
							<br/>
							<br/>
							<div id="doc_person_pics"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div id="doc_person_tb">
	<a href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'" onclick="addPerson();">增加人员</a>
</div>
//wwy
//档案分类工具栏
<div id="doc_person_entryTb">
</div>
//wwy
//增加条目对话框
<div id='doc_person_addEntryDialog'></div>

//wwy
//显示图片
<div id="doc_person_img"></div>