package lnyswz.doc.util;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * 利用开源组件POI3.0.2动态导出EXCEL文档 转载时请保留以下信息，注明出处！
 * 
 * @author leno
 * @version v1.0
 * @param <T>
 *            应用泛型，代表任意一个符合javabean风格的类
 *            注意这里为了简单起见，boolean型的属性xxx的get器方式为getXxx(),而不是isXxx()
 *            byte[]表jpg格式的图片数据
 */
public class ExportExcel<T> {

	public void exportExcel(List<Object[]> dataset, OutputStream out,String hidNum) {
		exportExcel("Sheet1", null, dataset, out, "yyyy-MM-dd",hidNum);
	}

	public void exportExcel(String[] headers, List<Object[]> dataset,
			OutputStream out,String hidNum) {
		exportExcel("Sheet1", headers, dataset, out, "yyyy-MM-dd",hidNum);
	}

	public void exportExcel(String[] headers, List<Object[]> dataset,
			OutputStream out, String pattern,String hidNum) {
		exportExcel("Sheet1", headers, dataset, out, pattern,hidNum);
	}

	/**
	 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
	 * 
	 * @param title
	 *            表格标题名
	 * @param headers
	 *            表格属性列名数组
	 * @param dataset
	 *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
	 *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * @param out
	 *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * @param pattern
	 *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
	 */
	@SuppressWarnings("unchecked")
	public void exportExcel(String title, String[] headers,
			List<Object[]> dataset, OutputStream out, String pattern,String  hidNum) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 15);
		// 生成一个样式
		//HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
//		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
//		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
//		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//		// 生成一个字体
//		HSSFFont font = workbook.createFont();
//		font.setColor(HSSFColor.VIOLET.index);
//		font.setFontHeightInPoints((short) 12);
//		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
//		// 把字体应用到当前的样式
//		style.setFont(font);
//		// 生成并设置另一个样式
//		HSSFCellStyle style2 = workbook.createCellStyle();
//		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
//		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
//		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
//		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//		// 生成另一个字体
//		HSSFFont font2 = workbook.createFont();
//		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
//		// 把字体应用到当前的样式
//		style2.setFont(font2);

		// // 声明一个画图的顶级管理器
		// HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		// HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
		// 0, 0, 0, (short) 4, 2, (short) 6, 5));
		// 设置注释内容
		// comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
		// // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		// comment.setAuthor("leno");

		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			//cell.setCellStyle(style);
			//HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(headers[i]);
		}
		// 遍历集合数据，产生数据行
		
		int i = 0;
		for (Object[] d : dataset) {
			i++;	
			row = sheet.createRow(i);
			hidNum ="0,"+hidNum+",";
			int cellRow=0;
			for (int n = 0; n < d.length; n++) {
				String num=","+(n+1)+",";
				if(hidNum.indexOf(num)>0){
					continue;
				}
				HSSFCell cell = row.createCell(cellRow);
				cellRow++;
				String textValue = null;	//用于 时间 字符串 		
				if (d[n] instanceof Integer) {
					int intValue = (Integer) d[n];
					cell.setCellValue(intValue);//用于 数值类型
				}else if (d[n] instanceof BigDecimal) {
					BigDecimal BigDecimal = (BigDecimal) d[n];	
					Double dou = BigDecimal.doubleValue();					
					cell.setCellValue(dou);
				} else if (d[n] instanceof Date) {
					Date date = (Date) d[n];
					SimpleDateFormat sdf = new SimpleDateFormat(pattern);
					textValue = sdf.format(date);
				} else if (d[n] instanceof Character) {
					textValue = d[n].toString();
				} else if (d[n] instanceof Float) {
					float fValue = (Float) d[n];
//					textValue = new HSSFRichTextString(String.valueOf(fValue))
//							.toString();
					cell.setCellValue(fValue);
				} else if (d[n] instanceof Double) {
					double dValue = (Double) d[n];
//					textValue = new HSSFRichTextString(String.valueOf(dValue))
//							.toString();

					cell.setCellValue(dValue);
				} else if (d[n] instanceof Long) {
					long longValue = (Long) d[n];

					cell.setCellValue(longValue);
				}else if (textValue == null) {
					textValue = (String) d[n];
				}
				writeExcel(textValue, cell, workbook);
//				if (textValue != null) {
//					Pattern p = Pattern.compile("^//d+(//.//d+)?$");
//					Matcher matcher = p.matcher(textValue);
//					if (matcher.matches()) {
//						// 是数字当作double处理
//						cell.setCellValue(Double.parseDouble(textValue));
//					} else {
//						HSSFRichTextString richString = new HSSFRichTextString(
//								textValue);
//						HSSFFont font3 = workbook.createFont();
//						font3.setColor(HSSFColor.BLACK.index);						
//						richString.applyFont(font3);
//						cell.setCellValue(richString);
//					}
//				}
			}
		}

		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	private void writeExcel(String textValue,HSSFCell cell,HSSFWorkbook workbook){
		if (textValue != null) {
			Pattern p = Pattern.compile("^//d+(//.//d+)?$");
			Matcher matcher = p.matcher(textValue);
			if (matcher.matches()) {
				// 是数字当作double处理
				cell.setCellValue(Double.parseDouble(textValue));
			} else {
				if(textValue.indexOf("<font")>=0){

					textValue=textValue.substring(textValue.indexOf(">")+1,textValue.indexOf("<",(textValue.indexOf(">"))));				
				
				}
				//HSSFRichTextString richString = new HSSFRichTextString(textValue);
				//HSSFFont font3 = workbook.createFont();
				//font3.setColor(HSSFColor.BLACK.index);						
				//richString.applyFont(font3);
				cell.setCellValue(textValue);
			}
		}
		
	}

}