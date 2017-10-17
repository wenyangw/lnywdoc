<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/common/page.jsp"%>

<script type="text/javascript">
$(function() {
	$("ul[id^='tree']").tree({
		//单击菜单项，node-子菜单
		onClick : function(node) {
			if (node.attributes.url != undefined) {
	            Open(node.text,
	            		'${pageContext.request.contextPath}' + node.attributes.url,
	            		node.id,
	            		node.attributes.did,
	            		node.attributes.userId,
	            		node.attributes.lx,
	            		node.attributes.query);
			}
		}
	});	
	
	//在右边center区域打开菜单，新增tab
	function Open(text, url, id, did, userId, lx, query) {
		var t = $("#layout_center_tabs");
		var exists = false;
		var ts = t.tabs('tabs');
		$.each(ts, function() {
			var o = $(this).panel('options');
			if(o.title == text){
				exists = true;
				return false;
			}else if(o.id == id) {
				t.tabs('close', o.title);
				return false;
			}
		});
		//if (t.tabs('exists', text)) {
		if (exists) {
			t.tabs('select', text);
		} else {
			t.tabs('add', {
				id : id,
				did : did,
				userId : userId,
				lx : lx,
				query: query,
				title : text,
				closable : true,
				href : url,
				//content : '<iframe name="'+text+'"id="'+text+'"src="'+url+'" width="100%" height="100%" frameborder="0" scrolling="auto" ></iframe>'
			});
		}
	}
    
});
</script>

<div class="easyui-accordion" data-options="border:false,fit:true" style="width:300px;height:200px;">
	<c:forEach var="topAcc" items="${menutop}">
		<div title="${topAcc.catName}" data-options="iconCls:'${topAcc.iconCls}'" style="overflow:auto;padding:10px;">
		<ul id="tree${topAcc.id }" class="easyui-tree" 
			data-options="
				url:'${pageContext.request.contextPath}/admin/menuAction!menuTree.action?cid=${topAcc.id }',
				parentField:'pid',
				">
		</ul>
		</div>
	</c:forEach>
</div>