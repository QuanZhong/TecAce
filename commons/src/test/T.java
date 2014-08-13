package test;

import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import xml.GenerageJavaCode;
import xml.XmlUtils;

public class T {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) throws DocumentException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		String xmlFile = "e:/playlist.xml";
		/**
		 * 1、通过XML文件生成java源代码。
		 */
		System.out.println(GenerageJavaCode.generage(xmlFile,  "E:\\Workspace_20140211\\commons\\src\\test"));
		/**
		 * 2、把XML文件转化为java内存对象。
		 */
		BroadcastList obj = (BroadcastList) XmlUtils.docToObj(XmlUtils.load(xmlFile), BroadcastList.class);
		/**
		 * 3、输出内存中转化后的java对象。
		 */
		System.out.println(obj);
		/**
		 * 4、对象输出为xml
		 */
//		Document doc = XmlUtils.objToDoc(obj);
//		System.out.println(XmlUtils.getPrettyPrintXml(doc));
		
		XStream xstream = new XStream(new DomDriver());
		System.out.println("ESB状态通知输入参数如下：\n"+xstream.toXML(obj));


	}

}
