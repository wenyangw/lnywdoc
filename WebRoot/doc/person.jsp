<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<style type="text/css">
	.doc_person_warn_right {
		color:red;
		float:right;
	}

	.doc_person_warn_left {
		float:left;
	}

</style>

<script type="text/javascript">
    var person_did;
    var person_dg;
    var person_levelTg;
    var doc_person_tabs;
    var doc_person_depCombo;
    var doc_person_bmbh;
    var doc_objData;
    var doc_obj;
    var doc_objNOAudit;
    var doc_type;//用于保存时候判断是  增加 还是  修改
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
        monitorInputChange();
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
            onSelect: function(rowIndex, rowData){
                if(doc_person_tabs.is(':hidden')){
                    doc_person_tabs.show();
                }

                if(getTabIndex() == 0){
                	if($('#doc_person_name').val() != rowData.name ){
                		 getPerson(rowData.id);
                	}
//                     getPerson(rowData.id);
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
					if(getPersonId()) {
                        getPerson(getPersonId());
                    }


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

        var person_tool_dg = $('#doc_person_button_dg').datagrid({
            width: 1000,
			border: false
        });
        //根据权限，动态加载功能按钮
        lnyw.toolbar(0, person_tool_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', person_did);

        //初始化信息
        init();
     	intiObject(); 
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





    /******************
  	**********获取人员信息 sungj
  	*******************/
    function getPerson(personId){
    	//初始化
    	inputClear();
    	intiObject();
		$.ajax({
			url : '${pageContext.request.contextPath}/doc/personAction!getPerson.action',
			async: false,
			cache: false,
			dataType : 'json',
			data:{
				id: personId
			},
			success: function(data) {
				doc_objData=data;
				
				$('#doc_person_form').form('load', dateFormatter(data));//加载form表单数据
				
				if(!(doc_objData["status"] == "0" )){	
					if(doc_objData["isAudit"] == "9" ){
						var bz = getAuditBz(personId);
						var message=[];
						message.push(warnMessage()+"没有通过审批!");
						if(bz != null && bz != ""){
							message.push("("+bz+")");
						}						
		 				$("#doc_person_warn").html(message.join(""));
		 				$('#doc_person_button_cancel').css("display","block");//显示撤销按钮			 				
			 		}else{
			 			$("#doc_person_warn").html(warnMessage()+"已提交，等待审批中");
			 		}
					getPersonSps(data.timeStamp,personId);//显示提交申请的修改信息
				}
				
				freezeInput();			
			}
		});
	}



    /******************
  	**********初始化新建页面  sungj
  	*******************/
    function addPerson(){
    	inputClear();
        doc_person_tabs.show();
        //判断是否停留在人员页面
        if(getTabIndex() != 0){
            doc_person_tabs.tabs('select',0);
        }      
        person_dg.datagrid('clearSelections');

        //对部门进行处理
        var bmbh=doc_person_depCombo.combobox('getValue');
        //部门不可编辑
        doc_person_bmbh.combobox({
            editable:false,
        });
        doc_person_bmbh.combobox('select', bmbh);

        doc_objData="";

        $('#doc_person_outJobCount').val(0);
        $('#doc_person_orderNum').val(0);

        outFreezeInput("add");
    }



  	/******************
  	**********修改人员
  	*******************/
    function editPerson(){
    	if(doc_objData["status"] != "0" ){
    		if(doc_objData["isAudit"] == "9" ){
    			$.messager.alert('提示','选中人员信息审批被拒绝,请重新选择!','error');
    		}else{
    			$.messager.alert('提示','选中人员信息正在审批中,请重新选择!','error');
    		}  		
    	}else {
            outFreezeInput("edit");
        }
    }


    /******************
  	**********人员信息删除 sung
  	*******************/
    function removePerson(){
    	if(doc_objData["status"] != "0" ){
    		if(doc_objData["isAudit"] == "9" ){
    			$.messager.alert('提示','选中人员信息审批被拒绝,请重新选择!','error');
    		}else{
    			$.messager.alert('提示','选中人员信息正在审批中,请重新选择!','error');
    		}
    	}else {
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
    								id: getPersonId(),
    								status:"3",
    								needAudit:getPersonAudit()
    							},
    							success: function(d) {
    								var msg;
    								if(getPersonAudit() == "0"){
    									doc_person_depCombo.combobox('select', bmbh);
										msg="删除信息成功！";
    								}else{
    									$("#doc_person_warn").html("删除信息已提交，等待审批中");
    									doc_objData["status"]="3"; 
										msg=d.msg;
    								}	
    								 $.messager.show({
    									 msg : msg,
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
    }


    //提交前检查
    function checkPerson(){
        if($('#doc_person_ename').val() != doc_objData["ename"]){
            if(checkPath($('#doc_person_ename').val())){
                $.messager.alert('提示','该路径已经存在!请重新输入!','error');
                return false;
            }
        }
        if(doc_type == "edit"){
            saveEditPerson();
        }else if(doc_type == "add"){
            savePerson();
        }
    }

    //判断路径是否存在
    function checkPath(ename){
        var flag=false;
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

    //新增加人员，保存数据到后台  sungj
	function savePerson(){
		$.messager.confirm('提示', '您是否要新增人员信息', function(r){
			if (r){
				var needAudit=getPersonAudit();
				$('#doc_person_form').form('submit', {
					url: '${pageContext.request.contextPath}/doc/personAction!add.action',
					onSubmit: function(param){
						param.needAudit=needAudit;							
						return $(this).form('enableValidation').form('validate');
					},
					success: function(d){			
						var json = $.parseJSON(jxc.toJson(d));
						var msg;
						getSelectRow(doc_person_depCombo.combobox('getValue'),json.obj.bmbh,json.obj.id);
						
						if(needAudit == "0"){
							doc_objData=Object.create(Object.prototype);
							doc_objData["status"]='0';
							doc_objData["ename"]=$('#doc_person_ename').val();
							$("#doc_person_warn").html("");
							msg=noAudit("增加人员信息进成功！");
						}else{
							doc_objData["status"]='1';
							$("#doc_person_warn").html("增加人员已提交，等待审批中");
							msg=json.msg;
						}	
						 $.messager.show({
							 msg : msg,
							 title : '提示'
						 });
					}
				});
			}
		});
	}
	//对修改后提交的信息进行处理
    function saveEditPerson(){
        $.messager.confirm('提示', '您是否要保存人员信息', function(r){
            if (r){
                var doc_cond = [];
                for(var key in doc_obj){
                    doc_cond.push(
                        {
                            field : key,
                            newValue : doc_obj[key],
                            oldValue : doc_objData[key],
                        });
                }
                var doc_field_noAudit = [];
                for(var key in doc_objNOAudit){
                    doc_field_noAudit.push(key);
                }
				//将数据提交后台
				savePersonSp(getPersonId(),doc_objNOAudit,doc_cond,doc_field_noAudit.join(","));  			
 			}
	    });
	}
    //修改人员信息，保存数据到后台  sungj
    function savePersonSp(personId,doc_objNOAudit,doc_cond,noAuditField){
    	
    	var bmbh=$('#doc_person_bmbh').combobox('getValue');
    	var needAudit=getPersonAudit();
    	 $.ajax({
    	 		url : '${pageContext.request.contextPath}/doc/personAction!edit.action',
    	 		async: false,
    	 		cache: false,
    	 		dataType : 'json',
    	 		type: 'POST',
    	 		data:{
    	 			status:"2", 
    	 			id:personId,
    				personCond:JSON.stringify(doc_objNOAudit),
    				personSpCond:JSON.stringify(doc_cond),
    				bmbh:bmbh,
    				noAuditField:noAuditField,
    				needAudit:needAudit
    	 		},
    	 		success: function(d) {
    	 			
    	 			if(d.success){
    	 				getSelectRow(doc_person_depCombo.combobox('getValue'),bmbh,personId);
        	 			
        	 			var msg;
        	 			if(needAudit == "0"){
        	 				msg=noAudit("修改人员信息进成功！");
        	 			}else{
        	 				if(doc_cond && doc_cond.length > 0){ 
            	 				$("#doc_person_warn").html("修改信息已提交，等待审批中");
            	 				doc_objData["status"]='2';
            	 				msg="修改人员信息进入审批，请等待！";
            	 				showEditText(doc_cond);
            	 			}else{     
            	 				msg=noAudit("修改人员信息进成功！");
            	 
            	 			}
        	 			}
        	 			
        	 			
        	 			$.messager.show({
       					 msg : msg,
       					 title : '提示'
       					 
       			 		});
    	 			}else{
    	 				$.messager.show({
       					 msg : d.msg,
       					 title : '提示'
       			 		});
    	 			}	
    	 			
    	 		}
    	 	});
    }
	function showEditText(doc_cond){
		$('.doc_person_span').css('color','black');
		$.each(doc_cond,function(){
			$('#doc_person_'+this.field+'_span').html($('#doc_person_'+this.field+'_span').text()
														+'<span class="newValueShow"  >'
														+this.newValue+'</span>');
		});
		$('.newValueShow').css('color','red'); 
	}

	function noAudit(msg){
			doc_objData["status"]='0';
			$('.doc_person_span').css('color','black');
			$("#doc_person_warn").html("");
			var m=msg;
			return m;
	} 
	function getSelectRow(oldBmbh,newBmbh,id){
		if(oldBmbh != newBmbh){
			doc_person_depCombo.combobox('select', newBmbh);
		}
		person_dg.datagrid({"onLoadSuccess":function(data){
		    $(this).datagrid('selectRecord', id);
		}});
		freezeInput();
		hideButton();
	}
	//获取审批拒绝备注内容
	function getAuditBz(personId){
		 var bz="";
		 $.ajax({
			 	url: '${pageContext.request.contextPath}/doc/czshAction!getAuditBz.action',
				async: false,
				cache: false,
				dataType : 'json',
				data:{
					personId:personId
				},
				success: function(data) {
					if(data){
						bz=data;
					}
				}
			});
		 return bz;
	}
	//显示提交申请的修改信息
	function getPersonSps(timeStamp,personId){
		$.ajax({
			url : '${pageContext.request.contextPath}/doc/personSpAction!getPersonSps.action',
			async: false,
			cache: false,
			dataType : 'json',
			data:{
				timeStamp:timeStamp,
				personId:personId	
			},
			success: function(data) {
				$.each(data.rows,function(){
					$('#doc_person_'+this.field+'_span').html($('#doc_person_'+this.field+'_span').text()
																+'<span class="newValueShow"  >'
																+this.newValue+'</span>');
				});
				$('.newValueShow').css('color','red');
			}
		});

	}
	//取消审批
	function cancelSp(){		
		  $.messager.confirm('提示', '您是否要撤销'+warnMessage(), function(r){
	          if (r){
	        	  	  var bmbh=$('#doc_person_bmbh').combobox('getValue');
		        	  $.ajax({
		        			url : '${pageContext.request.contextPath}/doc/personAction!cancelSp.action',
		        			async: false,
		        			cache: false,
		        			dataType : 'json',
		        			data:{
		        				id:getPersonId()
		        			},
		        			success: function(data) {
		        				if(doc_objData["status"] == "1"){
		        					doc_person_depCombo.combobox('select', bmbh);
			        				doc_person_tabs.hide();
		        				}else{
		        					clearHint();
		        					doc_objData["status"] = "0";
		        					doc_objData["isAudit"] = "0";
		        					
		        				}
		        				$.messager.show({
		          					 msg : "撤销人员审批信息成功！",
		          					 title : '提示'
		          			 	});
		        				
		        			}
		        		});

	          		}
		    });
	}

	 //取得审批等级
    function getPersonAudit(){
        var need=0;
        if(NEED_AUDIT == '1'){
			$.ajax({
				url : '${pageContext.request.contextPath}/doc/personSpAction!getAuditLevel.action',
				async: false,
				cache: false,
				dataType : 'json',
				data:{
					bmbh: person_did,
					ywlxId: '01'
				},
				success: function(d) {
					need = d;
				}
			});
		}
        return need;
    }
	//清空 input 和 提交修改的内容
	function inputClear(){
		$("#doc_person").find("input").val('');
		clearHint();
		loadSelectList();
		
	}
	function clearHint(){
		$('.newValueShow').html("");//清空提示修改信息
		$('.doc_person_span').css('color','black');
		hideButton();
		$("#doc_person_warn").html("");
		doc_type="";

	}

	function hideButton(){
		$('.doc_person_button_hide').css("display","none");	//隐藏 保存，撤销按键
	}
	//初始化obj类
	function intiObject(){
		doc_obj=Object.create(Object.prototype);//初始化obj类（用于存放修改后内容）
		doc_objNOAudit=Object.create(Object.prototype);//初始化obj类（用于存放不需要审批的内容）

	}
	//将form表单内的input都变为灰色不可编辑
	function freezeInput(){
		 $("#doc_person_form :input").attr("disabled", true);
		 $("#doc_person_form .select ").combobox("disable");
		 $("#doc_person_form .audit_time").datebox({ disabled: true });
	}
	//1.隐藏撤销按钮显示保存按钮 2.将form表单的input变为可编辑，
	function outFreezeInput(type){
		
// 		$('#doc_person_button_cancel').css("display","none");	
	 	$('#doc_person_save').css("display","block");

	 	doc_type=type;

	 	$("#doc_person_form :input").removeAttr("disabled");
		$("#doc_person_form .select").combobox("enable");
		$("#doc_person_form .audit_time").datebox({ disabled: false });

	}
	//格式化input内容 时间类型显示为 YYYY-MM-DD
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
	//对下拉列表进行处理     tuer为可添加，false为不可添加
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

	//加载下拉列表
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


	//监听事件，对人员form表单内的input失去焦点触发事件，获取修改input的name和value
	function monitorInputChange(){
	    $(".audit").blur(
	        function(){
	            var field=this.name;
	            var fieldVal=this.value;
	            changeAuditInputPushObje(field,fieldVal);
	        }
	    );
	
	    $(".audit_number").blur(
	        function(){
	            var field=$(this).attr("numberboxname");
	            var fieldVal=this.value;
	            changeAuditInputPushObje(field,fieldVal);
	        }
	    );
	
	
	    $('.audit_select').combobox({
	        onChange:function(newValue,oldValue){
	            var field=$(this).attr("comboname");
	            changeAuditInputPushObje(field,newValue);
	        }
	    });
	
	    $('.audit_time').datebox({
	        onSelect: function(data){
	            var field=$(this).attr("comboname");
	            var fieldVal=moment(data).format('YYYY-MM-DD');
	            changeAuditInputPushObje(field,fieldVal);
	
	        }
	    });
	
	
	
	
	
	
	    $(".no_audit").blur(
	        function(){
	            var field=this.name;
	            var fieldVal=this.value;
	            changeNOAuditInputPushObje(field,fieldVal);
	        }
	    );
	
	    $(".no_audit_number").blur(
	        function(){
	            var field=$(this).attr("numberboxname");
	            var fieldVal=this.value;
	            changeNOAuditInputPushObje(field,fieldVal);
	        }
	    );
	
	
	    $('.no_audit_select').combobox({
	        onChange:function(newValue,oldValue){
	            var field=$(this).attr("comboname");
	            changeNOAuditInputPushObje(field,newValue);
	        }
	    });
	
	    $('.no_audit_time').datebox({
	        onSelect: function(data){
	            var field=$(this).attr("comboname");
	            var fieldVal=moment(data).format('YYYY-MM-DD');
	            changeNOAuditInputPushObje(field,fieldVal);
	
	        }
	    });
	}

	function changeAuditInputPushObje(field,fieldVal){
	    if(doc_objData[field] != fieldVal){
	    	 $('#doc_person_'+field+'_span').css('color','red');
	        doc_obj[field]=fieldVal;
	    }else {
	     	 $('#doc_person_'+field+'_span').css('color','black');
	        delete doc_obj[field];
	    }

	}

	function changeNOAuditInputPushObje(field,fieldVal){
	    if(doc_objData[field] != fieldVal){
	    	$('#doc_person_'+field+'_span').css('color','red');
	        doc_objNOAudit[field]=fieldVal;
	    }else {
	    	$('#doc_person_'+field+'_span').css('color','black');
	        delete doc_objNOAudit[field];
	    }

	}

	//提示信息
	function warnMessage(){
		var warnMessage=[];
		 warnMessage["1"]="增加人员";
		 warnMessage["2"]="修改信息";
		 warnMessage["3"]="删除信息";
		 return warnMessage[doc_objData["status"]];
	}


    //////////////////////////////////////////////以上为处理代码
</script>

<div id='doc_person_layout' style="height:100%;width:100%;">
	<div data-options="region:'west',title:'部门-人员',split:true" style="height:100%;width:200px;">
		<div id='doc_person_depLayout' style="height:100%;width:100%">
			<div data-options="region:'north',title:'部门',split:true" style="height:80px;width:100%;">
				请选择部门：<input id="doc_person_depId" name="depId" size="16">
			</div>
			<div data-options="region:'center',title:'人员',split:true" style="height:100%;width:100%;">
				<div id="doc_person_dg"></div>
			</div>
		</div>
	</div>
	<div data-options="region:'center',title:'',split:true, fit:true" style="height:100%;width:100%;">
		<div id="doc_person_tabs" class="easyui-tabs" data-options="fit:true, border:false," style="width:100%;height:100%;">
			<div title="基本信息" data-options="closable:false">
				<div id="doc_person" style="overflow: scroll;">
					<div id="doc_person_button_dg"></div>
					<form id='doc_person_form' method="post">
						<span class="doc_person_span doc_person_button_hide" id="doc_person_save">  <a href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-save'"  onclick="checkPerson();">保存</a>	</span>
						<span class="doc_person_span doc_person_button_hide" id="doc_person_button_cancel">  <a href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'"  onclick="cancelSp();">撤销</a>	</span>
						<table id="doc_person_table" class="doc_person_table_class">
							<tr>
								<td class="doc_person_th">&nbsp;</td>
							</tr>
							<tr>
								<td class="doc_person_th " colspan="6"><span class="doc_person_span doc_person_warn_left">**基本信息**</span>

									<span class="doc_person_warn_right" id="doc_person_warn"></span></td>
							</tr>
							<tr>
								<td colspan="6"><hr style="height:1px;border:none;border-top:1px dashed red;" /></td>
							</tr>
							<tr>
								<td class="doc_person_th"><span   id="doc_person_bmbh_span" class="doc_person_span">部门:</span><br/><input id="doc_person_bmbh"    name="bmbh"  style="width:138px;"></td>
								<td class="doc_person_th"><span  id="doc_person_postName_span"  class="doc_person_span">岗位:</span><br/><input id="doc_person_post" class="audit_select select"  name="postName" style="width:138px;"></td>
								<td class="doc_person_th"><span  id="doc_person_orderNum_span"  class="doc_person_span">序号:</span><br/><input id="doc_person_orderNum" name="orderNum" class="easyui-numberbox no_audit_number" style="width:138px;"></td>
								<td class="doc_person_th"><span  id="doc_person_ename_span" class="doc_person_span">路径:</span><br/><input class="easyui-validatebox no_audit" type="text" name="ename"  id="doc_person_ename" data-options="required:true,missingMessage:'请填写路径'" style="width:138px;"/></td>
							</tr>
							<tr>
								<td  class="doc_person_th"><span id="doc_person_name_span"  class="doc_person_span">姓名:</span><br/><input class="easyui-validatebox audit"  type="text" name="name"id="doc_person_name" data-options="required:true,missingMessage:'请填写姓名'" style="width: 138px;"/></td>
								<td  class="doc_person_th">
									<span id="doc_person_sex_span" class="doc_person_span">性别:</span><br/>
									<input id="doc_person_sex" class=" no_audit_select select" name="sex" style="width:138px;">
								</td>
								<td class="doc_person_th"><span id="doc_person_birthTime_span" class="doc_person_span">出生日期:</span><br/><input name="birthTime" class="audit_time" style="width:138px;" ></td>
								<td class="doc_person_th"><span id="doc_person_nation_span" class="doc_person_span ">民族:</span><br/>
									<input id="doc_person_nation"class=" audit_select"  name="nation" style="width:138px;">
								</td>
							</tr>
							<tr>
								<td class="doc_person_th"><span id="doc_person_phone_span" class="doc_person_span">手机号:</span><br/><input class="easyui-numberbox audit_number" type="text" name="phone" id="phone"  style="width:133px;"   /></td>
								<td class="doc_person_th"><span id="doc_person_idCard_span" class="doc_person_span">身份证号码:</span><br/><input type="text" class="audit" name="idCard" style="width:133px;"   /></td>
								<td class="doc_person_th"><span id="doc_person_joinPartyTime_span" class="doc_person_span">入党时间:</span><br/><input type="text" id='doc_person_joinPartyTime' name="joinPartyTime" class="audit_time"  style="width:138px;"></td>
								<td class="doc_person_th"><span id="doc_person_formalTime_span" class="doc_person_span">转正时间:</span><br/><input type="text" name="formalTime" class="audit_time" style="width:138px;"></td>
							</tr>
							<tr>
								<td class="doc_person_th"><span id="doc_person_bz_span" class="doc_person_span">备注:</span><br/><input  type="text" name="bz" class="no_audit" style="width:133px;" /></td>
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
								<td class="doc_person_th"><span  id="doc_person_fullEntranceTime_span" class="doc_person_span">入学时间:</span><br/><input type="text" name="fullEntranceTime" class="audit_time" style="width:138px;"></td>
								<td class="doc_person_th"><span  id="doc_person_fullGraduationTime_span" class="doc_person_span">毕业时间:</span><br/><input type="text" name="fullGraduationTime" class="audit_time" style="width:138px;"></td>
								
								<td class="doc_person_th"><span  id="doc_person_fullSchool_span" class="doc_person_span">毕业院校:</span><br/>
									<input id="doc_person_fullSchool" class="audit_select select" name="fullSchool"  style="width:130px;">
								</td>
								<td class="doc_person_th"><span  id="doc_person_fullMajor_span" class="doc_person_span">所学专业:</span><br/>
									<input id="doc_person_fullMajor" name="fullMajor" class="audit_select select"  style="width:130px;">
								</td>
								<td class="doc_person_th"><span  id="doc_person_fullEducation_span" class="doc_person_span">学历:</span><br/>
									<input id="doc_person_fullEducation" name="fullEducation" class="audit_select select"  style="width:130px;">
								</td>
								<td class="doc_person_th"><span  id="doc_person_fullDegree_span" class="doc_person_span">学位:</span><br/>
									<input id="doc_person_fullDegree" name="fullDegree" class="audit_select select"  style="width:130px;">
									
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
								<td class="doc_person_th"><span  id="doc_person_jobSchool_span" class="doc_person_span">毕业院校:</span><br/>
									<input id="doc_person_jobSchool" name="jobSchool"   class="audit_select select" style="width:138px;">
								</td>
								<td class="doc_person_th"><span  id="doc_person_jobMajor_span" class="doc_person_span">所学专业:</span><br/>
									<input id="doc_person_jobMajor" name="jobMajor"  class="audit_select select"  style="width:138px;">
								</td>
								<td class="doc_person_th"><span  id="doc_person_jobEducation_span" class="doc_person_span">学历:</span><br/>
									<input id="doc_person_jobEducation" name="jobEducation"  class="audit_select select"  style="width:138px;">
								</td>
								<td class="doc_person_th"><span  id="doc_person_jobDegree_span" class="doc_person_span">学位:</span><br/>
									<input id="doc_person_jobDegree" name="jobDegree"  class="audit_select select"  style="width:138px;">
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
								<td class="doc_person_th"><span id="doc_person_bestEducation_span"  class="doc_person_span">最高学历:</span><br/>
									<input id="doc_person_bestEducation" name="bestEducation"  class="audit_select select"  style="width:138px;">
								</td>
							</tr>
							<tr>
								<td class="doc_person_th"><span id="doc_person_nowRankTime_span" class="doc_person_span">任现职级时间:</span><br/><input type="text"   name="nowRankTime" class="audit_time" style="width:138px;"></td>
								<td class="doc_person_th"><span id="doc_person_rankName_span" class="doc_person_span">职称:</span><br/>
									<input id="doc_person_rankName" name="rankName"  class="audit_select select"  style="width:138px;">
								</td>
								<td class="doc_person_th"><span id="doc_person_getRankTime_span" class="doc_person_span">取得职称时间:</span><br/><input type="text" name="getRankTime" class="audit_time" style="width:138px;"></td>
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
								<td class="doc_person_th"><span id="doc_person_jobTime_span" class="doc_person_span">参加工作时间:</span><br/><input type="text" name="jobTime" class="audit_time" style="width:138px;"></td>
								<td class="doc_person_th"><span id="doc_person_jtJobTime_span" class="doc_person_span">到集团工作时间:</span><br/><input type="text" name="jtJobTime" class="audit_time" style="width:138px;"></td>
								<td class="doc_person_th"><span id="doc_person_companyTime_span" class="doc_person_span">到本单位工作时间:</span><br/><input type="text" name="companyTime" class="audit_time"    style="width:138px;"></td>
								<td class="doc_person_th"><span id="doc_person_outJobCount_span" class="doc_person_span">无就业年限:</span><br/><input class="easyui-numberbox audit_number"  type="text" id="doc_person_outJobCount" name="outJobCount"  style="width:133px;"/></td>
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
								<td class="doc_person_th"><span id="doc_person_socialCard_span"  class="doc_person_span">个人社保编号:</span><br/><input class="easyui-numberbox audit_number"  type="text" name="socialCard" style="width:133px;" /></td>
								<td class="doc_person_th"><span id="doc_person_socialPayTime_span"  class="doc_person_span">社保缴费时间:</span><br/><input type="text" name="socialPayTime" class="audit_time" style="width:138px;"></td>
								<td class="doc_person_th"><span id="doc_person_medicalCard_span"  class="doc_person_span">个人医保编号:</span><br/><input class="easyui-numberbox audit_number"  type="text" name="medicalCard" style="width:133px;"/></td>
								<td class="doc_person_th"><span id="doc_person_companyHouseBankCard_span"  class="doc_person_span">单位公积金号:</span><br/><input class="easyui-numberbox audit_number"  type="text" name="companyHouseBankCard" style="width:133px;"/></td>
								<td class="doc_person_th"><span id="doc_person_houseBankCard_span"  class="doc_person_span">个人公积金号:</span><br/><input class="easyui-numberbox audit_number"  type="text" name="houseBankCard" style="width:133px;"/></td>
							</tr>
						</table>
					</form>
				</div>
			</div>
			<div title="档案材料" data-options="closable:false" >
				<div id='doc_person_levelLayout' style="height:100%;width:100%;">
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