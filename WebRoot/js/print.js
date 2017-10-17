function loadApplet(applet_URL) {
//    var URL = applet_URL + "/applets/jre-1_5_0_18-windows-i586-p.exe";
//    document.write('<OBJECT id="myApplet" name="myApplet"');
//    document.write('classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93" WIDTH="300" HEIGHT="300" MAYSCRIPT name="myApplet"');
//    document.write('codebase='+ URL +'>');
//    document.write('<PARAM NAME="CODE" VALUE="PrinterApplet.class" />');
//    document.write('<PARAM NAME="CODEBASE" VALUE="applets" />');
//    document.write('<PARAM NAME="ARCHIVE" VALUE="jasperreports-applet-5.5.0.jar" />');
//    document.write('<PARAM NAME="type" VALUE="application/x-java-applet;version=1.5.0" />');
//    document.write('<PARAM NAME="scriptable" VALUE="false" />');
//    document.write('<PARAM NAME="REPORT_URL" VALUE="../jxc/jrPrint.action">');
//    document.write('no support java');
//    document.write('<comment>');
//    document.write('<embed type="application/x-java-applet;version=1.5.0"');
//    document.write('CODE="PrinterApplet.class"');
//    document.write('JAVA_CODEBASE="applets" ARCHIVE="jasperreports-applet-5.5.0.jar"');
//    document.write('scriptable=false');
//    document.write('pluginspage='+ URL +'>');
//    document.write('<noembed></noembed>');
//    document.write('</embed>');
//    document.write('</comment>');
//    document.write('</OBJECT>');
	var url = applet_URL + "/jxc/jrPrint.action";
    document.write('<APPLET ID="JrPrt" CODE = "PrinterApplet.class" CODEBASE = "applets" ARCHIVE = "jasperreports-applet-5.5.0.jar,commons-logging-1.1.1.jar,commons-collections-3.2.1.jar" WIDTH = "300" HEIGHT = "100">');   
	document.write('<PARAM NAME = "type" VALUE="application/x-java-applet;version=1.2.2">');   
	document.write('<PARAM NAME = "scriptable" VALUE="false">');   
	document.write('<PARAM NAME = "REPORT_URL" VALUE ="'+url+'">');   
	document.write('</APPLET>'); 
}