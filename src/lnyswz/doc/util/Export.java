package lnyswz.doc.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import lnyswz.common.bean.DataGrid;

public class Export {
	private static final String CONTENTTYPE = "application/octet-stream";

	public static void print(DataGrid dg, String fileName) {
		File fileReport = new java.io.File(getRootPath() + "/report/"
				+ fileName + ".jasper");

		JasperReport jasperReport = null;
		JasperPrint jasperPrint = null;
		try {
			jasperReport = (JasperReport) JRLoader.loadObject(fileReport);
			JRDataSource datasource = new JRBeanCollectionDataSource(
					dg.getRows());
			// 填充报表
			jasperPrint = JasperFillManager.fillReport(jasperReport,
					(Map<String, Object>) dg.getObj(), datasource);
			if (null != jasperPrint) {
				HttpServletResponse response = ServletActionContext
						.getResponse();
				response.setContentType(CONTENTTYPE);
				ServletOutputStream ouputStream = response.getOutputStream();

				ObjectOutputStream oos = new ObjectOutputStream(ouputStream);
				oos.writeObject(jasperPrint);
				oos.flush();
				oos.close();

				ouputStream.flush();
				ouputStream.close();
			}

		} catch (Exception e) {
		}

	}
	
	public static void export(DataGrid dg, String fileName, String location, String type) {
		File fileReport = new File(getRootPath() + "/report/" + fileName + ".jasper");

		JasperReport jasperReport = null;
		JasperPrint jasperPrint = null;
		OutputStream out;
		try {
			jasperReport = (JasperReport) JRLoader.loadObject(fileReport);
			JRDataSource datasource = new JRBeanCollectionDataSource(
					dg.getRows());
			// 填充报表
			jasperPrint = JasperFillManager.fillReport(jasperReport, (Map<String, Object>) dg.getObj(), datasource);
			out = new FileOutputStream(Export.getRootPath() + location);
			if (null != jasperPrint) {
				JRExporter exporter = getExporter(type);
				
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);  
				exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");  
				//注意此处用的不是JRPdfExporterParameter.OUTPUT_FILE，要用这个，还需新建File  
				//exporter.setParameter(JRPdfExporterParameter.OUTPUT_FILE_NAME, getRootPath() + "/pdf/" + pdfName);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
		        
				exporter.exportReport();
		        
		        out.close();
			}
		} catch (Exception e) {
		}

	}

	private static JRExporter getExporter(String type) {
		JRExporter exporter;
		if("xls".equals(type)){
			exporter = new JRXlsExporter();
		}else if("docx".equals(type)){
			exporter = new JRDocxExporter();
		}else if("rtf".equals(type)){
			exporter = new JRRtfExporter();
		}else{
			exporter = new JRPdfExporter();
		}
		
		return exporter;
	}
	
	public static String getRootPath() {
		// 因为类名为"Export"，因此" Export.class"一定能找到
		String result = Export.class.getResource("Export.class").toString();
		int index = result.indexOf("WEB-INF");
		if (index == -1) {
			index = result.indexOf("bin");
		}
		result = result.substring(0, index);
		if (result.startsWith("jar")) {
			// 当class文件在jar文件中时，返回"jar:file:/F:/ ..."样的路径
			result = result.substring(10);
		} else if (result.startsWith("file")) {
			// 当class文件在class文件中时，返回"file:/F:/ ..."样的路径
			result = result.substring(6);
		}
		if (result.endsWith("/"))
			result = result.substring(0, result.length() - 1);// 不包含最后的"/"
		return result;
	}

	public static void toJs(List<String> lists) {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType(CONTENTTYPE);
			ServletOutputStream ouputStream = response.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(ouputStream);
			oos.writeObject(lists);
			oos.flush();
			oos.close();
		} catch (Exception e) {

		}
	}
	
	public static String getExportType(String etype) {
		if(etype != null){ 
			return etype;
		}else{
			return "pdf";
		}
	}
	
	/**
	 * 访问远程(WebService)xml数据后返回的xml格式字符串并生成为本地文件
	 * 
	 */
	public static void saveFile(Document document, String savaFileURL){
		TransformerFactory transF = TransformerFactory.newInstance();
		try{
			DOMSource source = new DOMSource(document);
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			Transformer transformer = transF.newTransformer();
			StreamResult result = new StreamResult(bytes);
			
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "YES");
			transformer.transform(source, result);
		 
			FileOutputStream fos = new FileOutputStream(savaFileURL);
			fos.write(bytes.toByteArray());
			fos.close();
			
			
			System.out.println("生成xml文件成功!");
		}catch(TransformerConfigurationException e){
			System.out.println(e.getMessage());
		}catch(IllegalArgumentException e){
			System.out.println(e.getMessage());
		}catch(FileNotFoundException e){
			System.out.println(e.getMessage());
		}catch(TransformerException e){
			System.out.println(e.getMessage());
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	
	public static Document createDoc(String str){
		try {
			StringReader read = new StringReader(str);
			// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
			InputSource source = new InputSource(read);
			// 创建一个新的SAXBuilder
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(source);
			
			return document;
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
			System.out.println(e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println("ParserConfigurationException");
			System.out.println(e.getMessage());
		} catch (SAXParseException e) {
			System.out.println("SAXParseException");
			System.out.println(e.getMessage());
		} catch (SAXException e) {
			System.out.println("SAXException");
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException");
			System.out.println(e.getMessage());
		}
		return null;
	}
}
