<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var czsh_did;
var czsh_lx;
var czsh_menuId;

var czsh_toDg;
var czsh_dg;
var czsh_tabs;

var czsh_personId;
var czsh_timeStamp;
var czsh_status;

$(function(){
	czsh_did = lnyw.tab_options().did;
	czsh_lx = lnyw.tab_options().lx;
	czsh_menuId = lnyw.tab_options().id;
	
	$('#doc_czsh_layout').layout({
		fit : true,
		border : false,
	});

	var cardView = $.extend({}, $.fn.datagrid.defaults.view, {
	    renderRow: function(target, fields, frozen, rowIndex, rowData){
	        var cc = [];
	        cc.push('<td colspan=' + fields.length + ' style="width:1000px; padding:10px 5px; border:0;">');
	        if (!frozen){
	            cc.push('<table border= "0" width = 95%><tr>');
	            for(var i = 0; i < fields.length; i++){

	                var copts = $(target).datagrid('getColumnOption', fields[i]);
					if(fields[i] == 'br'){
                        cc.push('</tr><tr>');
                    }else if(fields[i].indexOf('lbr_') >= 0){
                         cc.push('</tr><tr><th class="read">' + copts.title + '</th></tr><tr>');
                     }else if(fields[i] == 'hbr') {
                        cc.push('</tr><tr><td>&nbsp;</td></tr><tr>');
                    }else{
						//赋值，供显示单据明细调用
						if(fields[i] == 'personId'){
							czsh_personId = rowData[fields[i]];
						}
						if(fields[i] == 'timeStamp'){
							czsh_timeStamp = rowData[fields[i]];
						}
						if(fields[i] == 'status'){
							czsh_status = rowData[fields[i]];
						}

                        if(!copts.hidden) {
                            cc.push('<th>' + copts.title + '</th><td>' + copts.formatter(rowData[fields[i]]) + '</td>');
                        }

                    }
	            }
	            //cc.push('</tr></table>');
	            cc.push('</tr>');
	        }
	        //cc.push('<br><div id="cardDg" style="padding-left: 500px;"></div>');
	        cc.push('<tr ><td></td><td colspan="5" height="200px"><div id="cardDg"></div></td></tr>');

	        cc.push('</table>');

	        cc.push('</td>');
	        return cc.join('');
	    }
	});
	
	
	czsh_toDg = $('#doc_czsh_toDg').datagrid({
		url: '${pageContext.request.contextPath}/doc/personSpAction!listAudits.action',
		fit: true,
		showHeader: false,
        queryParams: {
            bmbh: czsh_did,
        },
		pagination : true,
		pagePosition : 'bottom',
		pageSize : 1,
		pageList : [1],
		columns:[[
            {field:'lbr_1', title:'信息'},
			{field:'status',title:'操作类型：',
				formatter:function(value){
			    	if(value == '1') {
                        return '新增人员';
                    }else if(value == '2'){
                        return '修改人员信息';
					}else if(value == '3'){
                        return '删除人员';
					}
			}},
            {field:'br'},
			{field:'createName',title:'操作人：',formatter:function(value){
                return value;
            }},
			{field:'createTime',title:'操作时间：',formatter:function(value){
                return value;
            }},
			{field:'hbr'},
            {field:'lbr_2', title:'修改内容'},
			{field:'personName',title:'姓名：',formatter:function(value, row){
                return value;
            }},
            {field:'personId',hidden:true},
            {field:'timeStamp',hidden:true},
            {field:'needAudit',hidden:true},
            {field:'isAudit',hidden:true},
	    ]],
	    view: cardView,
	    onLoadSuccess:function(data){
	    	$('#cardDg').datagrid({
	    		url:'${pageContext.request.contextPath}/doc/personSpAction!listFields.action',
	    		queryParams: {
	        		personId: czsh_personId,
	        		timeStamp: czsh_timeStamp,
					status: czsh_status
	        	},
	    		fit : true,
	    	    border : false,
	    	    singleSelect : true,
	    	    remoteSort: false,
//	     	    fitColumns: true,
	    		columns:[[
					{field:'field',title:'项目',width:200,align:'center',
						formatter:function(value){
					    	return jxc.person_fields[value];
					}},
					{field:'oldValue',title:'原内容',width:200,align:'center'},
					{field:'newValue',title:'新内容',width:200,align:'center'},
	    	    ]],
	    	});
	    }
	});
	lnyw.toolbar(0, czsh_toDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', czsh_did);
	
	czsh_dg = $('#doc_czsh_dg').datagrid({
		fit : true,
	    border : false,
	    singleSelect : true,
	    remoteSort: false,
// 	    fitColumns: true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
			{field:'personName',title:'姓名',align:'center'},
			{field:'timeStamp',title:'批次',align:'center'},
			{field:'createName',title:'操作人',align:'center'},
	        {field:'createTime',title:'操作时间',align:'center',width:100},
	        {field:'auditName',title:'审批人',align:'center',width:100},
	        {field:'auditTime',title:'审批时间',align:'center',width:100},
            {field:'status',title:'类别',align:'center',width:100,
                formatter: function(value){
                    if(value == '1'){
                        return '新增';
                    }else if(value == '2'){
                        return '修改';
                    }else if(value == '3'){
                        return '删除';
                    }
                },
                styler: function(value){
                    if(value == '9'){
                        return 'color:red;';
                    }
                }},
	        {field:'needAudit',title:'等级',align:'center',width:100},
	        {field:'isAudit',title:'结果',align:'center',width:100,
	        	formatter: function(value){
        			if(value == '9'){
        				return '拒绝';
        			}else{
        				return '通过';
        			}
        		},
	        	styler: function(value){
					if(value == '9'){
						return 'color:red;';
					}
				}},
	        {field:'bz',title:'备注',align:'center',width:100,
        		formatter: function(value){
        			return lnyw.memo(value, 15);
        		}},
	    ]],
	    toolbar:'#doc_czsh_tb'
	});
	lnyw.toolbar(1, czsh_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', czsh_did);

	czsh_dg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="czsh-ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
            $('#czsh-ddv-'+index).datagrid({
                url:'${pageContext.request.contextPath}/doc/personSpAction!listFields.action',
                fitColumns:true,
                singleSelect:true,
                rownumbers:true,
                loadMsg:'',
                height:'auto',
                queryParams: {
                    personId: row.personId,
                    timeStamp: row.timeStamp,
                    status: row.status
        		},
                columns:[[
                    {field:'field',title:'项目',width:200,align:'center',
                        formatter:function(value){
                            return jxc.person_fields[value];
                        }},
                    {field:'oldValue',title:'原内容',width:200,align:'center'},
                    {field:'newValue',title:'新内容',width:200,align:'center'}
                ]],
                onResize:function(){
                	czsh_dg.datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                    	czsh_dg.datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            czsh_dg.datagrid('fixDetailRowHeight',index);
        }
    });

	//选中列表标签后，装载数据
	czsh_tabs = $('#doc_czsh_tabs').tabs({
		onSelect: function(title, index){
			if(index == 0){
 				czsh_toDg.datagrid('reload');
			}
			if(index == 1){
				czsh_dg.datagrid({
					url: '${pageContext.request.contextPath}/doc/czshAction!datagrid.action',
					queryParams:{
						bmbh: czsh_did,
					}
				});
			}
		},
	});
	
	//初始化创建时间
	$('#createDate').html(moment().format('YYYY年MM月DD日'));
	
	//初始化信息
	//init();
});

//以下为商品列表处理代码
function init(){
	//清空全部字段
	$('input').val('');

	//根据权限，动态加载功能按钮
	//lnyw.toolbar(0, czsh_spdg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
}


//////////////////////////////////////////////以下为业务审核处理代码

function czsh_audit(){
	var selected = czsh_toDg.datagrid('getRows');
	if(selected.length > 0){
		$.ajax({
			type: "POST",
			async: false,
			url: '${pageContext.request.contextPath}/doc/personSpAction!getPersonSp.action',
			data: {
				personId: czsh_personId,
				timeStamp: czsh_timeStamp
			},
			dataType: 'json',
			success: function(d){
			    if(d.isAudit == '9' || d.isAudit - 1 == selected[0].isAudit) {
                    $.messager.alert('警告', '该单据已审批结束！',  'warning');
                    czsh_toDg.datagrid('reload');
                }else{
					$.messager.prompt('请确认', '是否将该笔操作审核通过？', function(bz){
						if (bz != undefined){
							$.ajax({
								type: "POST",
								url: '${pageContext.request.contextPath}/doc/personSpAction!audit.action',
								data: {
								    bmbh: czsh_did,
                                    personId: czsh_personId,
                                    timeStamp: czsh_timeStamp,
                                    status: czsh_status,
                                    needAudit: selected[0].needAudit,
                                    menuId: czsh_menuId,
                                    bz: bz
								},
								dataType: 'json',
								success: function(d){
									if(d.success){
										czsh_toDg.datagrid('reload');
										$.messager.show({
											title : '提示',
											msg : d.msg
										});
									}  
								},
							});
						}
					});
				}
			}
		});
	}else{
		$.messager.alert('警告', '没有需要进行审批的操作！',  'warning');
	}
}

function czsh_refuse(){
	var selected = czsh_toDg.datagrid('getRows');
	if(selected.length > 0){
		$.ajax({
			type: "POST",
			async: false,
			url: '${pageContext.request.contextPath}/doc/personSpAction!getPersonSp.action',
			data: {
                personId: czsh_personId,
                timeStamp: czsh_timeStamp
			},
			dataType: 'json',
			success: function(d){
                if(d.isAudit == '9' || d.isAudit - 1 == selected[0].isAudit) {
                    $.messager.alert('警告', '该单据已审批结束！',  'warning');
                    czsh_toDg.datagrid('reload');
                }else{
					$.messager.prompt('请确认', '<font color="red">是否拒绝将该操作审核通过？</font>', function(bz){
						if (bz != undefined){
							$.ajax({
								type: "POST",
								url: '${pageContext.request.contextPath}/doc/personSpAction!refuse.action',
								data: {
                                    bmbh: czsh_did,
                                    personId: czsh_personId,
                                    timeStamp: czsh_timeStamp,
                                    status: czsh_status,
                                    needAudit: selected[0].needAudit,
                                    menuId: czsh_menuId,
                                    bz: bz
								},
								dataType: 'json',
								success: function(d){
									if(d.success){
										czsh_toDg.datagrid('reload');
										$.messager.show({
											title : '提示',
											msg : d.msg
										});
									}  
								},
							});
							
						}
					});
				}
			}
		});
	}else{
		$.messager.alert('警告', '没有需要进行审批的操作！',  'warning');
	}
	
}
//////////////////////////////////////////////以上为已审核列表处理代码
function searchCzsh(){
	czsh_dg.datagrid('load',{
		bmbh: czsh_did,
		createTime: $('input[name=createTimeCzsh]').val(),
	});
}

//////////////////////////////////////////////以上为已审核列表处理代码


</script>

<div id="doc_czsh_tabs" class="easyui-tabs" data-options="fit:true, border:false," style="width:100%;height:100%;">
    <div title="待审核" data-options="closable:false">
				<table id='doc_czsh_toDg'></table>
    </div>
    <div title="已审核列表" data-options="closable:false" >
    	<table id='doc_czsh_dg'></table>
    </div>
</div>
<div id="doc_czsh_tb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeYwsh" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchCzsh();">查询</a>
</div>
