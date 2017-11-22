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
var search_did;
var search_tg;

$(function(){
	search_did = lnyw.tab_options().did;

	$('#doc_search_layout').layout({
		fit : true,
		border : false,
	});
	
	//wwy
	//初始化档案类别列表
    search_tg = $('#doc_search_tg').treegrid({
        //url: '${pageContext.request.contextPath}/doc/entryAction!searchEntry.action',
        idField: 'id',
        treeField: 'entryName',
        fit: true,
        columns:[[
            {field:'id',title:'id',hidden:true},
            {field:'entryName',title:'名称',width:400},
            {field:'createTime',title:'创建时间'},
            {field:'recordTime',title:'建档时间'},
            {field:'pageCount',title:'页数'},
            {field:'type',title:'类别', hidden:true},
            {field:'personId',title:'人员', hidden:true},
            {field:'entryId',title:'entryId', hidden:true}
        ]],
        onBeforeExpand: function(row){
            var opts = search_tg.treegrid('options');
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
                $('#doc_search_pics').html('');
                $.ajax({
	                url : '${pageContext.request.contextPath}/doc/imgAction!getImgsByEntry.action',
                    data : {
                        entryId : row.id
                    },
                    cache : false,
                    dataType : 'JSON',
                    success : function(data) {
	                    $.each(data, function(index, item){
                            addImgInSearch(item.filePath);
						});

                    }
                });
                $('#doc_search_upload').css('display', 'block');
            }else{
                $('#doc_search_upload').css('display', 'none');
            }
        }
    });
    //根据权限，动态加载功能按钮
    lnyw.toolbar(1, search_tg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', search_did);

	//初始化信息
	init();

});

//以下为初始化
function init(){
	//清空全部字段
	$('input').val('');
}

function getEntryId(){
    return search_levelTg.datagrid("getSelected") ? search_levelTg.datagrid("getSelected").id : 0;
}

function searchLevel(){
    clearPicsInSearch();
    var cond = [];
    if($('input[name=levelName]').val().trim()){
        cond.push(
            {
                field : "levelName",
                value : $('input[name=levelName]').val()
            });
    }
    var opts = search_tg.treegrid('options');
    opts.url = '${pageContext.request.contextPath}/doc/entryAction!searchLevel.action';
    search_tg.treegrid('load', {
        catId: '01',
        search: JSON.stringify(cond),
    });

}

function searchDoc(){
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
    var opts = search_tg.treegrid('options');
    opts.url = '${pageContext.request.contextPath}/doc/entryAction!searchEntry.action';
    search_tg.treegrid('load', {
        catId: '01',
        search: JSON.stringify(cond),
		logicOr: $('input#doc_search_logic').is(':checked') ? '1' : '0'
    });
}

function searchImg(){
    $.ajax({
        url : '${pageContext.request.contextPath}/doc/entryAction!searchImg.action',
        cache : false,
        dataType : 'JSON',
        success : function(data) {
            console.info(data);
        }
    });
}

//wwy
//在图片显示div显示图片
function addImgInSearch(filePath){
    $('#doc_search_pics').append("<img src='" + filePath + "' onclick=showImgInSearch('" + filePath + "') height='300' width='240'/>");
}

//wwy
//单击图片后，弹出窗口显示原图
function showImgInSearch(filePath){
    console.info(filePath);
    $("#doc_search_img").dialog({
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
    $('#doc_search_pics').html('');
}

function printImgInSearch(){
    $('#doc_searchImg').jqprint();
}

//////////////////////////////////////////////以上为处理代码

</script>

<div id='doc_search_layout' style="height:100%;width:100%">
	<div data-options="region:'west',title:'条件',split:true" style="height:100%;width:300px">
		<div id="doc_search_condition">

			<hr>
			<a href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'" onclick="searchLevel();">查询</a><br/>
			<span class="search_label">类别名称</span><input type="text" name="levelName" class="search_field"/><br/>
			<hr>
			<a href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'" onclick="searchDoc();">查询</a><br/>
			<span class="search_label">关系或</span><input type="checkbox" id='doc_search_logic' name="logicOr" class="search_field"/><br/>
			<span class="search_label">条目名称</span><input type="text" name="entryName" class="search_field"/><br/>
			<span class="search_label">条目关键字</span><input type="text" name="entryCrux" class="search_field"/><br/>
			<%--<hr>--%>
			<%--<a href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'" onclick="searchImg();">查询</a><br/>--%>
			<%--<span class="search_label">图片关键字</span><input type="text" name="imgCrux" class="search_field"/><br/>--%>
		</div>
	</div>
	<div data-options="region:'center',title:'结果',split:true, fit:true" style="height:100%;width:100%">
		<div id='doc_search_levelLayout' style="height:100%;width:100%">
			<div data-options="region:'north',title:'目录',split:true" style="height:400px;width:100%">
				<div id="doc_search_tg"></div>
			</div>
			<div data-options="region:'center',title:'内容',split:true" style="height:60%;width:100%">
				<div id="doc_search_pics"></div>
			</div>
		</div>
	</div>
</div>

//wwy
//显示图片
<div id="doc_search_img"></div>
