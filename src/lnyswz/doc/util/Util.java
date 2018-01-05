package lnyswz.doc.util;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import lnyswz.common.util.Common;

public class Util {
	public static String getReportName(String bmbh, String fileName){
		JSONArray jsonArray = Common.readJson(fileName);
		for(int i = 0; i < jsonArray.size(); i++){
        	JSONObject jsonO = JSON.parseObject(jsonArray.get(i).toString());
        	if(bmbh.equals(jsonO.getString("bmbh"))){
        		return jsonO.getString("report");
        	}
        }
		return null;
	}
	
	public static String getQueryWhere(String input, String[] fields, Map<String, Object> params){
		String str = "";
		if(input.indexOf(" ") > 0 ){
			String[] in = input.split("\\s+");
			for(int i = 0; i < in.length; i++){
				String str1 = ""; 
				for(String s : fields){
					str1 = Common.joinString(s + " like :search" + i, str1, " OR ");
				}
				params.put("search" + i, "%" + in[i] + "%");
				str = Common.joinString("(" + str1 + ")", str, " AND ");
			}
		}else{
			for(String s : fields){
				str = Common.joinString(s + " like :search", str, " OR ");
			}
			params.put("search", "%" + input + "%");
		}
		return str;
	}
	
	
	public static String[] getNullPropertyNames (Object source,String field) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
       
        	String[] fieldArray = field.split(",");//分割出来的字符数组
        	for(java.beans.PropertyDescriptor pd : pds) {
        		if(!(Arrays.asList(fieldArray).contains(pd.getName()))){
        			emptyNames.add(pd.getName());
        		}
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static void copyPropertiesIgnoreNull(Object src, Object target,String field){
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src,field));
    }
	
	
}
