/**
 * 包含easyui的扩展和常用的方法
 * 
 * @author 孙宇
 * 
 * @version 20130818
 */

var pageSize = 18;
var pageList = [ pageSize, pageSize + 5, pageSize + 10, pageSize + 15, pageSize + 20, pageSize + 30, pageSize + 50, pageSize + 80, pageSize + 120, pageSize + 170, pageSize + 230, pageSize + 300];

var lnyw = $.extend({}, lnyw);/* 定义全局对象，类似于命名空间或包的作用 */
//图标
var iconData = [ {
	value : '',
	text : '默认'
}, {
	value : 'icon-add',
	text : 'icon-add'
}, {
	value : 'icon-edit',
	text : 'icon-edit'
}, {
	value : 'icon-remove',
	text : 'icon-remove'
}, {
	value : 'icon-save',
	text : 'icon-save'
}, {
	value : 'icon-cut',
	text : 'icon-cut'
}, {
	value : 'icon-ok',
	text : 'icon-ok'
}, {
	value : 'icon-no',
	text : 'icon-no'
}, {
	value : 'icon-cancel',
	text : 'icon-cancel'
}, {
	value : 'icon-reload',
	text : 'icon-reload'
}, {
	value : 'icon-search',
	text : 'icon-search'
}, {
	value : 'icon-print',
	text : 'icon-print'
}, {
	value : 'icon-help',
	text : 'icon-help'
}, {
	value : 'icon-undo',
	text : 'icon-undo'
}, {
	value : 'icon-redo',
	text : 'icon-redo'
}, {
	value : 'icon-back',
	text : 'icon-back'
}, {
	value : 'icon-sum',
	text : 'icon-sum'
}, {
	value : 'icon-tip',
	text : 'icon-tip'
} ];

var dictType = [ {
	value : '00',
	text : '变量'
},  {
	value : '01',
	text : '字段'
}, {
	value : '02',
	text : '表'
},{
	value : '03',
	text : '视图'
},{
	value : '04',
	text : '存储过程'
} ];

var dictDisplay = [ {
	value :'00',
	text :'无',
},{
	value:'01',
	text:'可查'
},{
	value:'02',
	text:'不可查'
}];
 
var dictOpe = [ 
{
	value :' ',
	text :'(无)',
},
{
	value :'=',
	text :'等于',
},{
	value:'>',
	text:'大于'
},{
	value:'<',
	text:'小于'
},{
	value:'>=',
	text:' 大于等于'
},{
	value:'<=',
	text:'小于等于'
},{
	value:'!=',
	text:'不等于'
},{
	value:'1',
	text:'类似'
},{
	value:'2',
	text:'左类似'
},{
	value:'3',
	text:'右类似'
}];

/**
 * @author 孙宇
 * 
 * 获得项目根路径
 * 
 * 使用方法：sy.bp();
 * 
 * @returns 项目根路径
 */
lnyw.bp = function() {
	var curWwwPath = window.document.location.href;
	var pathName = window.document.location.pathname;
	var pos = curWwwPath.indexOf(pathName);
	var localhostPaht = curWwwPath.substring(0, pos);
	var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
	return (localhostPaht + projectName);
};

/**
 * @author 孙宇
 * 
 * 使用方法:sy.pn();
 * 
 * @returns 项目名称(/sshe)
 */
lnyw.pn = function() {
	return window.document.location.pathname.substring(0, window.document.location.pathname.indexOf('\/', 1));
};

/**
 * 
 * 取消easyui默认开启的parser
 * 
 * 在页面加载之前，先开启一个进度条
 * 
 * 然后在页面所有easyui组件渲染完毕后，关闭进度条
 * 
 * @author 孙宇
 * 
 * @requires jQuery,EasyUI
 * 
 */
$.parser.auto = false;
$(function() {
	$.messager.progress({
		text : '页面加载中....',
		interval : 100
	});
	$.parser.parse(window.document);
	window.setTimeout(function() {
		$.messager.progress('close');
		if (self != parent) {
			window.setTimeout(function() {
				try {
					parent.$.messager.progress('close');
				} catch (e) {
				}
			}, 500);
		}
	}, 1);
	$.parser.auto = true;
});

/**
 * 使panel和datagrid在加载时提示
 * 
 * @author 孙宇
 * 
 * @requires jQuery,EasyUI
 * 
 */
$.fn.panel.defaults.loadingMessage = '加载中....';
$.fn.datagrid.defaults.loadMsg = '加载中....';

/**
 * @author 孙宇
 * 
 * @requires jQuery,EasyUI
 * 
 * panel关闭时回收内存
 */
$.fn.panel.defaults.onBeforeDestroy = function() {
	var frame = $('iframe', this);
	try {
		if (frame.length > 0) {
			for ( var i = 0; i < frame.length; i++) {
				frame[i].contentWindow.document.write('');
				frame[i].contentWindow.close();
			}
			frame.remove();
			if ($.browser.msie) {
				CollectGarbage();
			}
		}
	} catch (e) {
	}
};

lnyw.dateFormatter = function (value){
    return (value ? moment(value).format('YYYY-MM-DD') : '');
}

lnyw.formatNumberRgx = function(num) {  
	  var parts = num.toString().split(".");  
	  parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");  
	  return parts.join(".");  
};  

lnyw.delcommafy = function(num){  
	  if((num + "").trim() == ""){  
	       return "";  
	  }  
	  num = num.replace(/,/gi,'');  
	  
	  return num;  
};


/**
 * @author 孙宇
 * 
 * 增加formatString功能
 * 
 * 使用方法：sy.fs('字符串{0}字符串{1}字符串','第一个变量','第二个变量');
 * 
 * @returns 格式化后的字符串
 */
lnyw.fs = function(str) {
	for ( var i = 0; i < arguments.length - 1; i++) {
		str = str.replace("{" + i + "}", arguments[i + 1]);
	}
	return str;
};

lnyw.tab_options = function(){
	var to = $('#layout_center_tabs').tabs('getSelected').panel('options');
	return to;
};

$.fn.tree.defaults.loadFilter = function(data, parent) {
	var opt = $(this).data().tree.options;
	var idFiled, textFiled, parentField;
	if (opt.parentField) {
		idFiled = opt.idFiled || 'id';
		textFiled = opt.textFiled || 'text';
		parentField = opt.parentField;
		var i, l, treeData = [], tmpMap = [];
		for (i = 0, l = data.length; i < l; i++) {
			tmpMap[data[i][idFiled]] = data[i];
		}
		for (i = 0, l = data.length; i < l; i++) {
			if (tmpMap[data[i][parentField]]
					&& data[i][idFiled] != data[i][parentField]) {
				if (!tmpMap[data[i][parentField]]['children'])
					tmpMap[data[i][parentField]]['children'] = [];
				data[i]['text'] = data[i][textFiled];
				tmpMap[data[i][parentField]]['children'].push(data[i]);
			} else {
				data[i]['text'] = data[i][textFiled];
				treeData.push(data[i]);
			}
		}
		return treeData;
	}
	return data;
};

/**
 * @author 孙宇
 * 
 * 接收一个以逗号分割的字符串，返回List，list里每一项都是一个字符串
 * 
 * @returns list
 */
lnyw.getList = function(value) {
	if (value != undefined && value != '') {
		var values = [];
		var t = value.split(',');
		for ( var i = 0; i < t.length; i++) {
			values.push('' + t[i]);/* 避免他将ID当成数字 */
		}
		return values;
	} else {
		return [];
	}
};

//datagrid中较长的字段显示15位，后加省略号
lnyw.memo = function (value, limitLength){  
	if(value != undefined){
	    if(value.length > limitLength){  
	        return "<span title='"+value+"'>"+value.substring(0,limitLength)+"..."+"</span>";  
	    }else{  
	        return "<span title='"+value+"'>"+value+"</span>";  
	    }
	}
};

//不再使用
//设置combobox初始项
lnyw.comboDefault = function(target, key){
	var data = target.combobox('getData');
	if(data.length > 0){
		target.combobox('setValue', data[0][key]);
//		target.combobox('select', data[0][key]);
	}
},

//初始化combobox
lnyw.initCombo = function(target, key, value, url){
	return target.combobox({
	    url: url,
	    width: 90,
	    valueField: key,
	    textField: value,
	    //panelHeight: 'auto',
//	    onLoadSuccess:function(){
//	    	target.combobox('selectedIndex', 0);
//	    }
	});
},


//动态加载功能按钮
lnyw.toolbar = function(tabId, panel, url, did) {
	var mid = $('#layout_center_tabs').tabs('getSelected').panel('options').id;
	$.ajax({
		url : url,
		data : {
			mid : mid,
			tabId: tabId,
			did : did,
		},
		dataType : 'json',
		success : function(d) {
			if(d != null){
				console.info(d);
				panel.datagrid("addToolbarItem",d);
			}
		}
	});
	return;
};


/**
 * @author 孙宇
 * 
 * 接收一个以逗号分割的字符串，返回List，list里每一项都是一个字符串
 * 
 * @returns list
 */
lnyw.stringToList = function(value) {
	if (value != undefined && value != '') {
		var values = [];
		var t = value.split(',');
		for ( var i = 0; i < t.length; i++) {
			values.push('' + t[i]);/* 避免他将ID当成数字 */
		}
		return values;
	} else {
		return [];
	}
};

//选择combobox第几项
$.extend($.fn.combobox.methods, {
    selectedIndex: function (jq, index) {
        if (!index) {
            index = 0;
        }
        $(jq).combobox({
            onLoadSuccess: function () {
                var opt = $(jq).combobox('options');
                var data = $(jq).combobox('getData');

                for (var i = 0; i < data.length; i++) {
                    if (i == index) {
                        $(jq).combobox('setValue', eval('data[index].' + opt.valueField));
                        break;
                    }
                }
            }
        });
    }
});


/**
 * @author 王文阳
 * 
 * 扩展tree组件，获得实心结点
 * 
 * @returns list
 */
$.extend($.fn.tree.methods, {
	getCheckedExt : function(jq) {// 扩展getChecked方法,使其能实心节点也一起返回
		var checked = $(jq).tree("getChecked");
		var checkbox2 = $(jq).find("span.tree-checkbox2").parent();
		$.each(checkbox2, function() {
			var node = $.extend({}, $.data(this, "tree-node"), {
				target : this
			});
			checked.push(node);
		});
		return checked;
	},
	getSolidExt : function(jq) {// 扩展一个能返回实心节点的方法
		var checked = [];
		var checkbox2 = $(jq).find("span.tree-checkbox2").parent();
		$.each(checkbox2, function() {
			var node = $.extend({}, $.data(this, "tree-node"), {
				target : this
			});
			checked.push(node);
		});
		return checked;
	}
});

/**
 * @author 王文阳
 * 
 * 扩展datagrid组件，动态增加、删除toolbar按钮
 * 
 */
$.extend($.fn.datagrid.methods, {
	addToolbarItem : function (jq, items) {
		return jq.each(function () {
			var dpanel = $(this).datagrid('getPanel');
			var toolbar = dpanel.children("div.datagrid-toolbar");
			if(toolbar.length == 0){
				toolbar = $("<div class=\"datagrid-toolbar\"></div>").prependTo(dpanel);
				$(this).datagrid('resize');
			}
			for (var i = 0; i < items.length; i++) {
				var btn = items[i];
				if (btn == "-") {
					$("<div class=\"datagrid-btn-separator\"></div>").appendTo(toolbar);
				} else {
					//var td = $("<td></td>").appendTo(tr);
					//修改代码，加入onclick
					var b = $("<a href=\"javascript:void(0)\" onclick=\""+ btn.handler +"\"></a>").appendTo(toolbar);
					//原代码，直接执行
					//b[0].onclick = eval(btn.handler || function () {});
					b.linkbutton($.extend({}, btn, {
							plain : true
						}));
				}
			}
		});
	},
	removeToolbarItem : function (jq, param) {
		return jq.each(function () {
			var dpanel = $(this).datagrid('getPanel');
			var toolbar = dpanel.children("div.datagrid-toolbar");
			var cbtn = null;
			if (typeof param == "number") {
				cbtn = toolbar.find("td").eq(param).find('span.l-btn-text');
			} else if (typeof param == "string") {
				cbtn = toolbar.find("span.l-btn-text:contains('" + param + "')");
			}
			if (cbtn && cbtn.length > 0) {
				cbtn.closest('td').remove();
				cbtn = null;
			}
		});
	},
});

$.extend($.fn.propertygrid.defaults.columns[0][1], {
    formatter : function(value, rowData, rowIndex) {
		return lnyw.memo(value, 15);
    }
});


/**
 * auther by 王文阳
 * 
 * date : 2013 - 09 - 15
 */
$.extend($.fn.validatebox.defaults.rules, {
	mustLength: { //判断固定长度
		validator: function(value, param) { 
			return value.length == param[0];
		},
		message: '输入内容长度{0}字符.'
	},
	minLength : { // 判断最小长度
		validator : function(value, param) {
			return value.length >= param[0];
		},
		message : '最少输入 {0} 个字符。'
	},
	length:{validator:function(value,param){
		var len=$.trim(value).length;
			return len>=param[0]&&len<=param[1];
		},
			message:"内容长度介于{0}和{1}之间."
		},
	phone : {// 验证电话号码
		validator : function(value) {
			return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
		},
		message : '格式不正确,请使用下面格式:020-88888888'
	},
	mobile : {// 验证手机号码
		validator : function(value) {
			return /^(13|15|18)\d{9}$/i.test(value);
		},
		message : '手机号码格式不正确(正确格式如：13450774432)'
	},
	phoneOrMobile:{//验证手机或电话
		validator : function(value) {
			return /^(13|15|18)\d{9}$/i.test(value) || /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
		},
		message:'请填入手机或电话号码,如13688888888或020-8888888'
	},
	idcard : {// 验证身份证
		validator : function(value) {
			return /^\d{15}(\d{2}[A-Za-z0-9])?$/i.test(value);
		},
		message : '身份证号码格式不正确'
	},
	floatOrInt : {// 验证是否为小数或整数
		validator : function(value) {
			return /^(\d{1,3}(,\d\d\d)*(\.\d{1,3}(,\d\d\d)*)?|\d+(\.?\d+))?$/i.test(value);
		},
		message : '请输入数字，并保证格式正确'
	},
	currency : {// 验证货币
		validator : function(value) {
			return /^d{0,}(\.\d+)?$/i.test(value);
		},
		message : '货币格式不正确'
	},
	qq : {// 验证QQ,从10000开始
		validator : function(value) {
			return /^[1-9]\d{4,9}$/i.test(value);
		},
		message : 'QQ号码格式不正确(正确如：453384319)'
	},
	integer : {// 验证整数
		validator : function(value) {
			return /^[+]?[0-9]+\d*$/i.test(value);
		},
		message : '请输入整数'
	},
	chinese : {// 验证中文
		validator : function(value) {
			return /^[\u0391-\uFFE5]+$/i.test(value);
		},
		message : '请输入中文'
	},
	english : {// 验证英语
		validator : function(value) {
			return /^[A-Za-z]+$/i.test(value);
		},
		message : '请输入英文'
	},
	unnormal : {// 验证是否包含空格和非法字符
		validator : function(value) {
			return /.+/i.test(value);
		},
		message : '输入值不能为空和包含其他非法字符'
	},
	username : {// 验证用户名
		validator : function(value) {
			return /^[a-zA-Z][a-zA-Z0-9_]{5,15}$/i.test(value);
		},
		message : '用户名不合法（字母开头，允许6-16字节，允许字母数字下划线）'
	},
	faxno : {// 验证传真
		validator : function(value) {
//			return /^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/i.test(value);
			return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
		},
		message : '传真号码不正确'
	},
	zip : {// 验证邮政编码
		validator : function(value) {
			return /^[1-9]\d{6}$/i.test(value);
		},
		message : '邮政编码格式不正确'
	},
	ip : {// 验证IP地址
		validator : function(value) {
			return /d+.d+.d+.d+/i.test(value);
		},
		message : 'IP地址格式不正确'
	},
	name : {// 验证姓名，可以是中文或英文
			validator : function(value) {
				return /^[\u0391-\uFFE5]+$/i.test(value)|/^\w+[\w\s]+\w+$/i.test(value);
			},
			message : '请输入姓名'
	},
	email:{
		validator : function(value){
			return /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(value); 
		},
		message : '请输入有效的电子邮件账号(例：abc@126.com)'	
	},
	same:{
		validator : function(value, param){
			if($("#"+param[0]).val() != "" && value != ""){
				return $("#"+param[0]).val() == value; 
			}else{
				return true;
			}
		},
		message : '两次输入的密码不一致！'
	},
    startWith : { //开始前param[1]位与param[0]一致，否则提示param[2]
    	validator: function(value,param){
    		var v = $('#'+param[0]).val();
    		var len = param[1];
    		if(value.substring(0, len) == v.substring(0, len)){
    			return true;
    		}
    		return false;
    	},
    	message: '{2}'
    }
});

/**
 * 扩展datagrid的editor，实现只读列
 */
$.extend($.fn.datagrid.defaults.editors, {
    textRead: {
        init: function(container, options){
            var input = $('<input type="text" class="datagrid-editable-input" readonly="readonly" style="color:black;background-color: yellow;">').appendTo(container);
            return input;
        },
        destroy: function(target){
            $(target).remove();
        },
        getValue: function(target){
            return $(target).val();
        },
        setValue: function(target, value){
        	//$(target).removeAttr('readonly');
            $(target).val(value);
            //$(target).attr('readonly', 'readonly');
        },
        resize: function(target, width){
//            $(target)._outerWidth(width);
        	var input = $(target);
            if (jQuery.support.boxModel == true){
                 input.width(width - (input.outerWidth() - input.width()));
            } else {
                 input.width(width);
             }
        }
    }
});

/**
 * Datagrid扩展方法tooltip 基于Easyui 1.3.3，可用于Easyui1.3.3+
 * 简单实现，如需高级功能，可以自由修改
 * 使用说明:
 *   在easyui.min.js之后导入本js
 *   代码案例:
 *      $("#dg").datagrid({....}).datagrid('tooltip'); 所有列
 *      $("#dg").datagrid({....}).datagrid('tooltip',['productid','listprice']); 指定列
 * @author ____′↘夏悸
 */
$.extend($.fn.datagrid.methods, {
    tooltip : function (jq, fields) {
        return jq.each(function () {
            var panel = $(this).datagrid('getPanel');
            if (fields && typeof fields == 'object' && fields.sort) {
                $.each(fields, function () {
                    var field = this;
                    bindEvent($('.datagrid-body td[field=' + field + '] .datagrid-cell', panel));
                });
            } else {
                bindEvent($(".datagrid-body .datagrid-cell", panel));
            }
        });
        function bindEvent(jqs) {
            jqs.mouseover(function () {
                var content = $(this).text();
                $(this).tooltip({
                    content : content,
                    trackMouse : true,
                    onHide : function () {
                        $(this).tooltip('destroy');
                    }
                }).tooltip('show');
            });
        }
    }
});

/** 
 * 扩展表格列对齐属性： 
 *      自定义一个列字段属性： 
 *      headalign ：原始align属性针对数据有效, headalign针对列名有效
 *      
 */  
 $.extend($.fn.datagrid.defaults,{  
     onLoadSuccess : function() {  
         var target = $(this);  
         var opts = $.data(this, "datagrid").options;  
         var panel = $(this).datagrid("getPanel");  
         //获取列
         var fields=$(this).datagrid('getColumnFields',false);
         //datagrid头部 table 的第一个tr 的td们，即columns的集合
         var headerTds =panel.find(".datagrid-view2 .datagrid-header .datagrid-header-inner table tr:first-child").children();
         //重新设置列表头的对齐方式
         headerTds.each(function (i, obj) {
             var col = target.datagrid('getColumnOption',fields[i]);
             if (!col.hidden && !col.checkbox)
             {
                 var headalign=col.headalign||col.align||'left';
                 $("div:first-child", obj).css("text-align", headalign);
             }
         });
     }  
});  



(function ($) {
	$.fn.my97 = function (options, params) {
	    if (typeof options == "string") {
	        return $.fn.my97.methods[options](this, params);
	    }
	    options = options || {};
	    if (!WdatePicker) {
	        alert("未引入My97js包！");
	        return;
	    }
	    return this.each(function () {
	        var data = $.data(this, "my97");
	        var newOptions;
	        if (data) {
	            newOptions = $.extend(data.options, options);
	            data.opts = newOptions;
	        } else {
	            newOptions = $.extend({}, $.fn.my97.defaults, $.fn.my97.parseOptions(this), options);
	            $.data(this, "my97", {
	                options : newOptions
	            });
	        }
	        $(this).addClass('Wdate').click(function () {
	            WdatePicker(newOptions);
	        });
	    });
	};
    $.fn.my97.methods = {
        setValue : function (target, params) {
            target.val(params);
        },
        getValue : function (target) {
            return target.val();
        },
        clearValue : function (target) {
            target.val('');
        }
    };
    $.fn.my97.parseOptions = function (target) {
        return $.extend({}, $.parser.parseOptions(target, ["el", "vel", "weekMethod", "lang", "skin", "dateFmt", "realDateFmt", "realTimeFmt", "realFullFmt", "minDate", "maxDate", "startDate", {
                        doubleCalendar : "boolean",
                        enableKeyboard : "boolean",
                        enableInputMask : "boolean",
                        autoUpdateOnChanged : "boolean",
                        firstDayOfWeek : "number",
                        isShowWeek : "boolean",
                        highLineWeekDay : "boolean",
                        isShowClear : "boolean",
                        isShowToday : "boolean",
                        isShowOthers : "boolean",
                        readOnly : "boolean",
                        errDealMode : "boolean",
                        autoPickDate : "boolean",
                        qsEnabled : "boolean",
                        autoShowQS : "boolean",
                        opposite : "boolean"
                    }
                ]));
    };
    $.fn.my97.defaults = {
        //dateFmt : 'yyyy-MM-dd HH:mm:ss'
    	dateFmt : 'yyyy-MM-dd'
    };
 
    $.parser.plugins.push('my97');
})(jQuery);

/**  
 * 使用方法: <br> 
 * 开启: MaskUtil.mask(); <br> 
 * 关闭: MaskUtil.unmask(); <br> 
 *   
 * 自定义提示文字：MaskUtil.mask('其它提示文字...');  
 */  
lnyw.MaskUtil = (function() {  
  
    var $mask = undefined, $maskMsg = undefined;  
  
    var defMsg = '正在处理，请稍待。。。';  
  
    function init() {
        if (!$mask) {  
            //$mask = $("<div class=\"datagrid-mask mymask\"></div>").appendTo("body");
            $mask = $(".datagrid-mask");
        }  
        if (!$maskMsg) {  
            //$maskMsg = $("<div class=\"datagrid-mask-msg mymask\">" + defMsg + "</div>").appendTo("body").css({  
            //    'font-size' : '12px'  
            //});
            $maskMsg = $(".datagrid-mask-msg").css({  
                'font-size' : '12px'  
            });
        }  
  
        $mask.css({  
            width : "100%",  
            height : $(document).height()  
        });  
  
        var scrollTop = $(document.body).scrollTop();  
  
        $maskMsg.css({  
            left : ($(document.body).outerWidth(true) - 190) / 2,  
            top : (($(window).height() - 45) / 2) + scrollTop  
        });  
  
    }  
  
    return {  
        mask : function(msg) {  
            init();
            $mask.show();
            $maskMsg.html(msg || defMsg).show();
        	
        	/**
        	var $mask = $('.datagrid-mask');  
            var $mask_msg = $('.datagrid-mask-msg');  
              
            $mask.css({  
                display: 'block',  
                width : "100%",  
                height : $(document).height()  
            }).appendTo(document.body);  
              
            $mask_msg.css({  
                display: 'block', //显示出来  
                'z-index': 9999, //最顶层，用户才能点到链接  
                //width: 560,  
                //height: 90,  
                //padding: '10px 10px 10px 60px', //覆盖原来的样式  
                //background: '#ffc url("${ctx}/jquery-easyui/themes/default/images/messager_warning.gif") no-repeat scroll 10px 10px', //覆盖原来的样式  
                left: ($(window).width()  - $mask_msg.outerWidth())/2,  
                top:  ($(window).height() - $mask_msg.outerHeight())/2  
            }).html(msg || defMsg);  
      
            $(window).resize(function() {  
                $mask.css({  
                    width:  $(window).width(),   
                    height: $(window).height()  
                });  
                $mask_msg.css({  
                    left: ($(window).width()  - $mask_msg.outerWidth())/2,  
                    top:  ($(window).height() - $mask_msg.outerHeight())/2  
                });  
            }).resize(); 
        	**/
        },  
        unmask : function() {  
            $mask.hide();  
            $maskMsg.hide();
        	
        	/**
        	var $mask = $('.datagrid-mask');  
            var $mask_msg = $('.datagrid-mask-msg');
            $mask.css({  
                display: 'none',
            });
            $mask_msg.css({  
                display: 'none',
            });
            **/
        }  
    };  
  
}());  


