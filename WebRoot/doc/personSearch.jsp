<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<style type="text/css">
	.doc_person_search_table td{
		border-bottom: #cccccc solid 1px;
	}

	.doc_personSearch_input{
		width: 90px;
	}

	.doc_personSearch_date{
		width: 90px;
		margin-top: 3px;
	}

	.doc_personSearch_ope{
		width: 80px;
	}

	.select_person_title{
		text-align: right;
	}

</style>

<script type="text/javascript">
var personSearch_dg;
var doc_personSearch_sex;
var doc_personSearch_nation;
var doc_personSearch_bmbh;
var person_search_dgTitle;
var selectTitle=[];
$(function(){
	
	personSearch_dg = $('#doc_personSearch_dg');
	doc_personSearch_nation = $('#doc_personSearch_nation');
	doc_personSearch_sex = $('#doc_personSearch_sex');
	selectList(doc_personSearch_sex,'sex',false);
	selectList(doc_personSearch_nation,'nation',false);
	doc_personSearch_bmbh =lnyw.initCombo($("#doc_personSearch_bmbh"), 'id', 'depName', '${pageContext.request.contextPath}/admin/departmentAction!listDeps.action');

	$('input[id^="doc_personSearch_ope_"]').combobox({
		data: dictOpe,
		panelHeight: 'auto',
	});

    person_search_dgTitle=[
	    [
			{field:'bmbh',title:'部门',rowspan:'2'},
			{field:'orderNum',title:'排序',rowspan:'2'},
			{field:'name',title:'姓名',rowspan:'2'},
			{field:'postName',title:'岗位',rowspan:'2'},
			{field:'sex',title:'性别',rowspan:'2'},
			{field:'nation',title:'民族',rowspan:'2'},
			{field:'birthTime',title:'出生日期',rowspan:'2',type:'time', formatter: lnyw.dateFormatter},
			{field:'phone',title:'手机号',rowspan:'2',type:'number'},
			{field:'idCard',title:'身份证号码',rowspan:'2',type:'number'},
			{field:'joinPartyTime',title:'入党时间',rowspan:'2',type:'time',formatter: lnyw.dateFormatter},
			{field:'formalTime',title:'入党转正时间',rowspan:'2',type:'time',formatter: lnyw.dateFormatter},
			{field:'formalPartyCount',title:'党龄',rowspan:'2'},
			{title:'全日制教育',colspan:'6',align:'center', group: 1},
			{title:'在职教育',colspan:'4',align:'center', group: 2},
			{field:'bestEducation',title:'最高学历',rowspan:'2'},
			{field:'nowRankTime',title:'任现职级时间',rowspan:'2',type:'time',formatter: lnyw.dateFormatter},
			{field:'rankName',title:'职称',rowspan:'2'},
			{field:'getRankTime',title:'取得职称时间',rowspan:'2',type:'time',formatter: lnyw.dateFormatter},
			{field:'jobTime',title:'参加工作时间',rowspan:'2',type:'time', formatter: lnyw.dateFormatter},
			{field:'jtJobTime',title:'到集团工作时间',rowspan:'2',type:'time', formatter: lnyw.dateFormatter},
			{field:'companyTime',title:'到本单位工作时间',rowspan:'2',type:'time', formatter: lnyw.dateFormatter},
			{field:'outJobCount',title:'无就业年限',rowspan:'2'},
			{field:'workingYears',title:'工龄',rowspan:'2'},
			{field:'jtWorkingYears',title:'到集团工作工龄',rowspan:'2'},
			{field:'companyYears',title:'到本单位工作工龄',rowspan:'2'},
			{field:'socialCard',title:'个人社保编号',rowspan:'2',type:'number'},
			{field:'socialPayTime',title:'社保缴费时间',rowspan:'2',type:'time',formatter: lnyw.dateFormatter},
			{field:'medicalCard',title:'个人医保编号',rowspan:'2',type:'number'},
			{field:'companyHouseBankCard',title:'单位公积金号',rowspan:'2',type:'number'},
			{field:'houseBankCard',title:'个人公积金号',rowspan:'2',type:'number'},
			{field:'bz',title:'备注',rowspan:'2'},
			{field:'ename',title:'路径',rowspan:'2'},
		],
		[
			{field:'fullEntranceTime',title:'入学时间', group: 1,type:'time', formatter: lnyw.dateFormatter},
			{field:'fullGraduationTime',title:'毕业时间', group: 1,type:'time', formatter: lnyw.dateFormatter},
			{field:'fullSchool',title:'毕业院校',group: 1},
			{field:'fullMajor',title:'所学专业',group: 1},
			{field:'fullEducation',title:'学历',group: 1},
			{field:'fullDegree',title:'学位',group: 1},
			{field:'jobSchool',title:'毕业院校',group: 2},
			{field:'jobMajor',title:'所学专业',group: 2},
			{field:'jobEducation',title:'学历',group: 2},
			{field:'jobDegree',title:'学位',group: 2},
		]
	];

    selectTitle = getSelectField(person_search_dgTitle);

	personSearch_dg.datagrid({
        url : '${pageContext.request.contextPath}/doc/personAction!personSearchDatagrid.action',
		fit : true,
		singleSelect:true,
	    border : false,
	    columns: person_search_dgTitle,
	    toolbar: [{
   			iconCls: 'icon-edit',
   			text   : '导出报表',
   			handler: function(){
   				exportExcel();
   			},
   		
   		}],
	});
});

function getSelectField(source){
    var results = [];
    $.each(source[0], function(){
        if(this.group){
            var obj0 = this;
            $.each(source[1],function(){
                if(this.group == obj0.group){
                	var obj1 = Object.create(this);
                	obj1.title = obj0.title + "-" + this.title;
                	results.push(obj1);
                }
            });
        }else {
            results.push(this);
        }
    });
    return results;
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
        width: 90,
		onBeforeLoad: function (param) {
			param.param = pam;
		},
	});
}

/**
 * 查询内容
 */
function selectClick(){
	var cond = [];
 	var flag = true;
 	cueMessage("name",$('#doc_personSearch_name').val().trim(),"like","","string",0);
 	cueMessage("sex",doc_personSearch_sex.combobox('getValue').trim(),"like","","string",0);
 	cueMessage("nation",doc_personSearch_nation.combobox('getValue'),"like","","string",0);
 	cueMessage("bmbh",doc_personSearch_bmbh.combobox('getValue'),"like","","string","");
 	cueMessage("formalTime",$('#doc_personSearch_formalTimeBegin').datebox('getValue'),">=","_a","date",0);
 	cueMessage("formalTime",$('#doc_personSearch_formalTimeEnd').datebox('getValue'),"<=","_b","date",0);
 	cueMessage("jobTime",$('#doc_personSearch_jobTimeBegin').datebox('getValue'),">=","_a","date",0);
 	cueMessage("jobTime",$('#doc_personSearch_jobTimeEnd').datebox('getValue'),"<=","_b","date",0);
 	cueMessage("jtJobTime",$('#doc_personSearch_jtJobTimeBegin').datebox('getValue'),">=","_a","date",0);
 	cueMessage("jtJobTime",$('#doc_personSearch_jtJobTimeEnd').datebox('getValue'),"<=","_b","date",0);
 	cueMessage("companyTime",$('#doc_personSearch_companyTimeBegin').datebox('getValue'),">=","_a","date",0);
 	cueMessage("companyTime",$('#doc_personSearch_companyTimeEnd').datebox('getValue'),"<=","_b","date",0);
 	cueMessage("formalTime",$('#doc_personSearch_formalTime').datebox('getValue'),$('#doc_personSearch_ope_formalTime').combobox('getValue'),$('#doc_personSearch_formalNum').val(),"count",0);
 	cueMessage("jobTime",$('#doc_personSearch_jobTime').datebox('getValue'),$('#doc_personSearch_ope_jobTime').combobox('getValue'),$('#doc_personSearch_jobNum').val(),"count",0);
 	cueMessage("jtJobTime",$('#doc_personSearch_jtJobTime').datebox('getValue'),$('#doc_personSearch_ope_jtJobTime').combobox('getValue'),$('#doc_personSearch_jtJobNum').val(),"count",0);
 	cueMessage("companyTime",$('#doc_personSearch_companyTime').datebox('getValue'),$('#doc_personSearch_ope_companyTime').combobox('getValue'),$('#doc_personSearch_companyNum').val(),"count",0);

 
	function cueMessage(field,value,operator,count,type,round){

		if(type == "count"){
			if(!((count == "" && operator.trim() == "" && value.trim() == "" ) || (count != "" && operator.trim() != "" && value.trim() != "" ))){
			    $.messager.alert('警告', '请将已填写的条件填写完整！', 'warning');
				flag=false;
			}
		}
		
		if(value.trim()){
		    var obj={};
			obj.field=field;
			obj.value=value;
			obj.operator=operator;
			obj.count=count;
			obj.type=type;
			obj.round=round;
			cond.push(obj); 
		}
	}

	if(flag){
		personSearch_dg.datagrid('load', {
			search: JSON.stringify(cond),
		});		
	}

}

function cleanClick(){
	$('#doc_select_person_layout input').val('');
}

function JSONToCSVConvertor(JSONData, ReportTitle, ShowLabel) {
    var arrData = typeof JSONData != 'object' ? JSON.parse(JSONData): JSONData;  
    var CSV = '';  
//     CSV += ReportTitle + '\r\n\n';   
    if (ShowLabel) {  
        var row = "";  
        for ( var index in arrData[0]) {  
            row += index + ',';  
        }  
        row = row.slice(0, -1);  
        CSV += row + '\r\n';  
    }  
    for (var i = 0; i < arrData.length; i++) {  
        var row = "";  
        for ( var index in arrData[i]) {  
            row += '"' + arrData[i][index] + '",';  
        }  
        row.slice(0, row.length - 1);  
        CSV += row + '\r\n';  
    }  
    if (CSV == '') {  
        alert("Invalid data");  
        return;  
    }  
    var fileName =  moment(new Date).format('YYYYMMDDHHmmss');

    var link = document.createElement("a");

    //var uri = 'data:text/csv;charset=utf-8,\uFEFF' + CSV;
    //link.href = encodeURI(uri);

	var blob = new Blob(['\uFEFF' + CSV], {type: 'text/csv'}); //解决大文件下载
    link.href = URL.createObjectURL(blob);

    //set the visibility hidden so it will not effect on your web-layout
    link.style = "visibility:hidden";  
    link.download = fileName + ".csv";  
    //this part will append the anchor tag and remove it after automatic click  
    document.body.appendChild(link);  
    link.click();  
    document.body.removeChild(link);  
}  

function exportExcel(){
	$('#doc_personSearch_Dialog').dialog({
		title : '选择导出内容',
		width : 590,
		height : 350,
		modal : true,
		buttons: [
			{
				text:'全选',
				iconCls:'icon-edit',
				handler:function(){
					$("input:checkbox").prop("checked","checked");
				},
			},
			{
				text:'反选',
				iconCls:'icon-edit',
				handler:function(){
					$("input:checkbox").each(function(){
						this.checked = !this.checked;
					});
				},
			},
			{
				text:'导出',
				iconCls:'icon-ok',
				handler:function(){
					var selectField=[];
					$.each(selectTitle, function(){
						var s = $('input:checkbox[name=' + this.field + ']');
						if(s.is(':checked')){
							selectField.push(this);
						}
					});
					var exportData = [];
					$.each(personSearch_dg.datagrid('getData').rows, function(){
						var onlyPerson = this;
						var onlyData = {};
						$.each(selectField, function(){
							if(onlyPerson[this.field] == null){
								onlyData[this.title] = " ";
							}else{
								if(this.type == 'time'){
									onlyData[this.title] = moment(onlyPerson[this.field]).format('YYYY-MM-DD');
								}else if(this.type == 'number'){
									onlyData[this.title] = onlyPerson[this.field] + "\t";
								}else{
									onlyData[this.title] = onlyPerson[this.field];
								}

							}
						});
						exportData.push(onlyData);
					});
					JSONToCSVConvertor(exportData, "", true);
					$('#doc_personSearch_Dialog').dialog('close');	
				},
			}
		],
		onBeforeOpen : function() {
			var stars = "<table width='100%'><tr>";
			$.each(selectTitle, function(index){
				stars += '<td align="left">';
				stars += '<input type="checkbox"  name="' + this.field + '"';
				stars += 'value="' + this.field + '" checked=checked ><b>' + this.title;
				//每四条数据进行换行
				if((index + 1) % 4 == 0){
					stars += '</tr><tr>';
				}
			});
			stars += '</tr></table>';
			$('#select_personField').html(stars);
		},
	});
}


</script>
<div id='doc_select_person_layout' class='easyui-layout' style="height: 100%;">
	<div data-options="region:'west',title:'查询条件',split:true,"
		style="width: 300px;">
		
		<div id='doc_select_person' class='easyui-layout'
			data-options="split:false,border:false,fit:true"
			style="height: 100%;">
			<div align="center" data-options="region:'north',border:false"
				style="height: 48px;">
				<a href="#" class="easyui-linkbutton"
					data-options="iconCls:'icon-search',plain:true"
					onclick="selectClick();">查询</a> <a href="#"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-reload',plain:true"
					onclick="cleanClick();">清除</a>

			</div>
			<div id='select_person_condition' data-options="region:'center',border:false">
				<table class="doc_person_search_table">
					<tr>
						<td class="select_person_title">部门:</td>
						<td></td>
						<td><input  name="bmbh" id="doc_personSearch_bmbh" /></td>
					</tr>
					<tr>
						<td class="select_person_title">姓名:</td>
						<td></td>
						<td><input  name="name" id="doc_personSearch_name" class="doc_personSearch_input" /></td>
					</tr>
					<tr>
						<td class="select_person_title">性别:</td>
						<td></td>
						<td><input name="sex" id="doc_personSearch_sex" /></td>
					</tr>
					<tr>
						<td class="select_person_title">民族:</td>
						<td></td>
						<td><input name="nation" id="doc_personSearch_nation" /></td>
					</tr>

					<tr>
						<td class="select_person_title">入党转正时间:</td>
						<td></td>
						<td>
							<input  name="formalTimeBegin" id="doc_personSearch_formalTimeBegin" class="easyui-datebox doc_personSearch_date" />
							<br/>
							<input  name="formalTimeEnd" id="doc_personSearch_formalTimeEnd" class="easyui-datebox doc_personSearch_date" />
						</td>
					</tr>
					<tr>
						<td class="select_person_title">参加工作时间:</td>
						<td></td>
						<td>
							<input name="jobTimeBegin" id="doc_personSearch_jobTimeBegin" class="easyui-datebox doc_personSearch_date" />
							<br/>
							<input name="jobTimeEnd" id="doc_personSearch_jobTimeEnd" class="easyui-datebox doc_personSearch_date" />
						</td>
					</tr>
					<tr>
						<td class="select_person_title">集团工作时间:</td>
						<td></td>
						<td>
							<input  name="jtJobTimeBegin" id="doc_personSearch_jtJobTimeBegin" class="easyui-datebox doc_personSearch_date" />
							<br/>
							<input  name="jtJobTimeEnd" id="doc_personSearch_jtJobTimeEnd" class="easyui-datebox doc_personSearch_date" />
						</td>
					</tr>
					<tr>
						<td class="select_person_title">本单位工作时间:</td>
						<td></td>
						<td>
							<input  name="companyTimeBegin" id="doc_personSearch_companyTimeBegin" class="easyui-datebox doc_personSearch_date" />
							<br/>
							<input  name="companyTimeEnd" id="doc_personSearch_companyTimeEnd" class="easyui-datebox doc_personSearch_date" />
						</td>
					</tr>
					<tr>
						<td class="select_person_title">党龄查询:</td>
						<td class="select_person_title">
							<input name="opeFormalTime" id="doc_personSearch_ope_formalTime" class="doc_personSearch_ope" />
							<br/>
							<span class="doc_personSearch_label">截止日期</span>
						</td>
						<td>
							<input name="formalNum" id="doc_personSearch_formalNum" class="easyui-numberbox doc_personSearch_date" />
							<br/>
							<input name="formalTime" id="doc_personSearch_formalTime" class="easyui-datebox doc_personSearch_date" />
						</td>	
					</tr>
					<tr>
						<td class="select_person_title">工龄查询:</td>
						<td class="select_person_title">
							<input name="opeJobTime" id="doc_personSearch_ope_jobTime" class="doc_personSearch_ope" />
							<br/>
							<span class="doc_personSearch_label">截止日期</span>
						</td>
						<td>
							<input name="jobNum" id="doc_personSearch_jobNum" class="easyui-numberbox doc_personSearch_date" />
							<br/>
							<input name="jobTime" id="doc_personSearch_jobTime" class="easyui-datebox doc_personSearch_date" />
						</td>
					</tr>
					<tr>
						<td class="select_person_title">集团工龄查询:</td>
						<td class="select_person_title">
							<input name="opejtJobTime" id="doc_personSearch_ope_jtJobTime" class="doc_personSearch_ope" />
							<br/>
							<span class="doc_personSearch_label">截止日期</span>
						</td>
						<td>
							<input name="jobTime" id="doc_personSearch_jtJobNum" class="easyui-numberbox doc_personSearch_date" />
							<br/>
							<input name="jtJobTime" id="doc_personSearch_jtJobTime" class="easyui-datebox doc_personSearch_date" />
						</td>
					</tr>
					<tr>
						<td class="select_person_title">本单位工龄查询:</td>
						<td class="select_person_title">
							<input name="opeCompanyTime" id="doc_personSearch_ope_companyTime" class="doc_personSearch_ope" />
							<br/>
							<span class="doc_personSearch_label">截止日期</span>
						</td>
						<td>
							<input name="companyNum" id="doc_personSearch_companyNum" class="easyui-numberbox doc_personSearch_date" />
							<br/>
							<input name="companyTime" id="doc_personSearch_companyTime" class="easyui-datebox doc_personSearch_date" />
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
	<div data-options="region:'center',title:'详细内容',split:true"
		style="width: 100%; height: 100%">
		<div id='doc_personSearch_dg'></div>
	</div>
</div>
<div id='doc_personSearch_Dialog'>
	<div id='select_personField'></div>
</div>