package lnyswz.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import lnyswz.doc.util.Export;

public class Common {
	public static String joinString(String source, String dest, String split){
		if(source.indexOf(dest) < 0){
			if(source.length() > 0){
				source += split;
			}
			source += dest;
		}
		return source;
	}
	
	public static JSONArray readJson(String fileName){
		File file = new File(Export.getRootPath() + "/json/" + fileName);
        Scanner scanner = null;
        StringBuilder buffer = new StringBuilder();
        try {
            scanner = new Scanner(file, "utf-8");
            while (scanner.hasNextLine()) {
                buffer.append(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        
        JSONArray jsonArray = JSON.parseArray(buffer.toString());
        return  jsonArray;
	}

}
