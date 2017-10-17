package lnyswz.common.applet;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JRViewer;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

public class JRPrinterApplet extends javax.swing.JApplet {
	/**    
     *    
     */
	private URL url = null;
	private String isPrint = "1";
	private String showPrint = "0";
	private JasperPrint jasperPrint = null;

	/** Creates new form AppletViewer */
	public JRPrinterApplet() {

	}

	/**    
    *    
    */
	public void init() {
		String strUrl = getParameter("REPORT_URL");
		isPrint = getParameter("REPORT_PRINT");
		showPrint = getParameter("SHOW_PRINT");
		if(strUrl != null) {
			try {
				url = new URL(strUrl);
			} catch (Exception e) {
				StringWriter swriter = new StringWriter();
				PrintWriter pwriter = new PrintWriter(swriter);
				e.printStackTrace(pwriter);
				JOptionPane.showMessageDialog(this, swriter.toString());
			}
		}else{
			JOptionPane.showMessageDialog(this, "init():Source URL not specified");
		}
	}

	public void start() {
		if (url != null) {
			if (jasperPrint == null) {
				try {
					jasperPrint = (JasperPrint) JRLoader.loadObject(url);
				} catch (Exception e) {
					StringWriter swriter = new StringWriter();
					PrintWriter pwriter = new PrintWriter(swriter);
					e.printStackTrace(pwriter);
					JOptionPane.showMessageDialog(this, swriter.toString());
				}
			}

			if (jasperPrint != null) {
				final JasperPrint print = jasperPrint;
				if("1".equals(isPrint)){
					Thread thread = new Thread(
						new Runnable() {
							public void run() {
								try {
									if("1".equals(showPrint)){
//										/** 设置纸张 */  
//	                                    PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();  
//	                                    System.out.println("开始设置打印纸张大小");  
//	                                    MediaSize ms = new MediaSize(241, 279, Size2DSyntax.MM, MediaSizeName.ISO_A4);
//	                                    
//	                                    printRequestAttributeSet.add(ms.getMediaSizeForName(MediaSizeName.ISO_A4));  
//	                                    System.out.println("完成设置打印纸张大小");  
//	                                    JRPrintServiceExporter pse = new JRPrintServiceExporter();  
//	                                    System.out.println("开始装载报表数据");  
//	                                    pse.setParameter(JRExporterParameter.JASPER_PRINT, print);  
//	                                    System.out.println("完成装载报表数据");  
//	                                    System.out.println("开始加入纸张设置");  
//	                                    pse.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET,  
//	                                                    printRequestAttributeSet);  
//	                                    System.out.println("完成加入纸张设置");  
//	                                    pse.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.TRUE);   
//	                                    pse.exportReport();  
										JasperPrintManager.printReport(print, true);
									}else{
										JasperPrintManager.printReport(print, false);
									}
								} catch (Exception e) {
									StringWriter swriter = new StringWriter();
									PrintWriter pwriter = new PrintWriter(swriter);
									e.printStackTrace(pwriter);
									JOptionPane.showMessageDialog(null,
											swriter.toString());
								}
							}
						});
					thread.start();
				}else{
					try {
	                    JRViewer view =new JRViewer(print);
						JFrame frame = new JFrame("打印预览");
						frame.setSize(900, 550); // 设置窗口大小
						Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize(); // 获得显示器大小对象
						Dimension frameSize = frame.getSize(); // 获得窗口大小对象
						if (frameSize.width > displaySize.width)
							frameSize.width = displaySize.width; // 窗口的宽度不能大于显示器的宽度
						if (frameSize.height > displaySize.height)
							frameSize.height = displaySize.height; // 窗口的高度不能大于显示器的高度
						frame.setLocation((displaySize.width - frameSize.width) / 2,
								(displaySize.height - frameSize.height) / 2);
						frame.add(view);
						frame.setVisible(true);
	                } catch (Exception e) {  
	                    e.printStackTrace();  
	                }
				}
			} else {
				JOptionPane.showMessageDialog(this, "Empty report.");
			}
		} else {
			JOptionPane.showMessageDialog(this,
					"start():Source URL not specified");
		}

	}

}