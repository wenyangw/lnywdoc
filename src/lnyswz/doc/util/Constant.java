package lnyswz.doc.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Wangwy
 * @edited
 * 	2017.08.25
 */
public interface Constant {
	//分类
	public static String CATID_PERSON = "01";
	public static String CATID_DOC = "02";


	//
	//当level,entry出现在同一个treegrid时，将id增加，用来区分
	public static int ID_PLUS = 50000;
	public static int Level2_ID_PLUS = 3000000;
	public static int ENTRY_ID_PLUS = 5000000;

	//当person中level,entry出现在同一个treegrid时，将id增加，用来区分
	public static int PERSON_ID_PLUS = 100000000;  // Id
	public static int LEVEL_ID_PLUS = 100000; // 第一、二levelId


	//当doc中level,entry出现在同一个treegrid时，将id增加，用来区分
	public static int DOC_LEVEL0_PLUS = 1000000;
	public static int DOC_LEVEL1_PLUS = 10000;
	public static int DOC_LEVEL2_PLUS = 1000;

	public static String SEARCH_VALUE_TYPE_STRING = "string";
	public static String SEARCH_VALUE_TYPE_DATE = "date";
	public static String SEARCH_VALUE_TYPE_COUNT = "count";

	public static String TYPE_LEVEL1 = "level1";
	public static String TYPE_LEVEL2 = "level2";
	public static String TYPE_ENTRY = "entry";

	public static int PAGE_SIZE = 18;
	public static int PAGE_DOCUMENT_SIZE = 14;

	//文件上传后保存的路径
	public static String UPLOADFILE_PATH = "/attached";


}
