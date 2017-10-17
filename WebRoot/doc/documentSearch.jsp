<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<style type="text/css">
	.search_label {
		width: 70px;
		text-align: right;
		display: inline-block;
	}

	.search_field {
		margin-left:10px;
	}

</style>

<script type="text/javascript">
var document_search_did;
var document_search_tg;
var document_search_sortCombo;

$(function(){
    document_search_did = lnyw.tab_options().did;

	$('#document_search_layout').layout({
		fit : true,
		border : false,
	});
	
	//wwy
	//初始化查询结果列表
    document_search_tg = $('#document_search_tg').treegrid({
        url: '${pageContext.request.contextPath}/doc/entryAction!searchEntry.action',
        idField: 'id',
        treeField: 'entryName',
        fit: true,
        columns:[[
            {field:'id',title:'id',hidden:true},
            {field:'entryName',title:'题名',width:400},
            {field:'owner',title:'责任人',width:100},
            {field:'fileno',title:'文号',width:100},
            {field:'recordTime',title:'形成时间'},
            {field:'createTime',title:'录入时间'},
            {field:'pageCount',title:'页数'},
            {field:'bz',title:'备注'},
            {field:'type',title:'类别', hidden:true},
            {field:'personId',title:'人员', hidden:true},
            {field:'entryId',title:'entryId', hidden:true}
        ]],
        onBeforeExpand: function(row){
            var opts = document_search_tg.treegrid('options');
            opts.queryParams.type = row.type;
            opts.queryParams.personId = row.personId;
        },
        onExpand: function(){
        	clearPicsInSearch();
    	},
    	onCollapse: function(){
        	clearPicsInSearch();
    	},
		onSelect: function(row){
            if(row.type === 'entry'){
                $('#document_search_pics').html('');
                $.ajax({
	                url : '${pageContext.request.contextPath}/doc/imgAction!getImgsByEntry.action',
                    data : {
                        entryId : row.entryId,
						//catId: '02'
                    },
                    cache : false,
                    dataType : 'JSON',
                    success : function(data) {
	                    $.each(data, function(index, item){
                            addImgInSearch(item.filePath);
						});

                    }
                });
                $('#document_search_upload').css('display', 'block');
            }else{
                $('#document_search_upload').css('display', 'none');
            }
        }
    });
    //根据权限，动态加载功能按钮
    lnyw.toolbar(1, document_search_tg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', document_search_did);

    document_search_sortCombo = $("input[name=entrySort]").combobox({
        url: '${pageContext.request.contextPath}/doc/sort.json',
        valueField: 'text',
        textField: 'text',
        editable: false
	});

	//初始化信息
	init();



});

//以下为初始化
function init(){
	//清空全部字段
	$('input').val('');
}

function searchDocument(){
    clearPicsInSearch();
    var cond = [];
    if($('input[name=entryName]').val().trim()){
        cond.push(
            {
                field : "entryName",
                value : $('input[name=entryName]').val()
            });
    }
    if($('input[name=entryCrux]').val().trim()){
        cond.push(
            {
                field : "crux",
                value : $('input[name=entryCrux]').val()
            });
    }
    if(document_search_sortCombo.combobox('getText').trim()){
        cond.push(
            {
                field : "sort",
                value : document_search_sortCombo.combobox('getText').trim()
            });
    }
    //var opts = document_search_tg.treegrid('options');
    //opts.url = '${pageContext.request.contextPath}/doc/entryAction!searchEntry.action';
    document_search_tg.treegrid('load', {
        catId: '02',
        search: JSON.stringify(cond),
		type: 'person',
		logicOr: $('input#document_search_logic').is(':checked') ? '1' : '0'
    });
}

//wwy
//在图片显示div显示图片
function addImgInSearch(filePath){
    $('#document_search_pics').append("<img src='" + filePath + "' onclick=showImgInSearch('" + filePath + "') height='300' width='240'/>");
}

//wwy
//单击图片后，弹出窗口显示原图
function showImgInSearch(filePath){
    $("#document_search_img").dialog({
        title: '查看信息',
        width: $(window).width() * 0.6,
        height: $(window).height() * 0.9,
        closed: false,
        cache: false,
        href: '${pageContext.request.contextPath}/doc/imgShowInSearch.jsp',
        modal: true,
        onLoad: function(){
            $('#doc_searchImg').html('<img src="' + filePath + '" style="width:100%" />');
		}
    });
}

function clearPicsInSearch(){
    $('#document_search_pics').html('');
}

function printImgInSearch(){
    $('#doc_searchImg').jqprint();
}

//////////////////////////////////////////////以上为处理代码

</script>

<div id='document_search_layout' style="height:100%;width:100%">
	<div data-options="region:'west',title:'条件',split:true" style="height:100%;width:300px">
		<div id="document_search_condition">
			<a href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'" onclick="searchDocument();">查询</a><br/>
			<span class="search_label">关系或</span><input type="checkbox" id='document_search_logic' name="logicOr" class="search_field"/><br/>
			<span class="search_label">条目名称</span><input type="text" name="entryName" class="search_field"/><br/>
			<span class="search_label">条目关键字</span><input type="text" name="entryCrux" class="search_field"/><br/>
			<span class="search_label">类型</span><input type="text" name="entrySort" class="search_field"/><br/>
		</div>
	</div>
	<div data-options="region:'center',title:'结果',split:true, fit:true" style="height:100%;width:100%">
		<div style="height:100%;width:100%">
			<div data-options="region:'north',title:'目录',split:true" style="height:400px;width:100%">
				<div id="document_search_tg"></div>
			</div>
			<div data-options="region:'center',title:'内容',split:true" style="height:60%;width:100%">
				<div id="document_search_pics"></div>
			</div>
		</div>
	</div>
</div>

//wwy
//显示图片
<div id="document_search_img"></div>
