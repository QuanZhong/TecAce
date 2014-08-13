package xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import test.BroadcastList;

/**
 * 通过XML生成java对象（目前仅支持普通xml文档的数据转化）
 * TODO:1\需要处理类名首字母小写问题2；处理中间有特殊字符问题3；不处理带命名空间的xml。
 * @author quanzhong
 *
 */
public class GenerageJavaCode {
	
	public static int generage(String xmlPath, String codePath) throws DocumentException, IOException{
		SAXReader reader = new SAXReader();
		Document document = reader.read(xmlPath);
		Map<String,StringBuffer> map = new HashMap<String,StringBuffer>();
		Element element = document.getRootElement();
		build(map, element, codePath);
		return map.size();
	}
	
	@SuppressWarnings("unchecked")
	public static void build(Map<String,StringBuffer> map, Element element, String codePath) throws IOException{
		String className = element.getName();
		StringBuffer buffer = new StringBuffer();
		StringBuffer bufferMethod = new StringBuffer();
		buffer.append("package test;\n\n");
		buffer.append("import java.util.List;\n\n");
		buffer.append("public class "+className+" {\n\n");
		List<Element> elements = element.elements();
		int flag = 0;
		Element maxEl = null;
		int preSubNum = 0;
		for(Element el : elements){
			String fieldName = el.getName();
			int num = element.elements(fieldName).size();
			int subNum = el.elements().size();
//			System.out.println(fieldName+":"+num+":"+subNum);
			if(num == 1 && subNum==0){//简单属性
				buffer.append("\tprivate String "+fieldName+";\n\n");
				bufferMethod.append("\tpublic String get"+fieldName+"(){\n\t\treturn "+fieldName+";\n\t}\n\n");
				bufferMethod.append("\tpublic void set"+fieldName+"(String "+fieldName+"){\n\t\tthis."+fieldName+" = "+fieldName+";\n\t}\n\n");
			}else if(num != 1 && subNum>0){//列表
				if(maxEl == null || preSubNum < subNum){
					preSubNum = subNum;
					maxEl = el;
				}
				++flag;
				if(flag == num){
					buffer.append("\tprivate List<"+fieldName+"> "+fieldName+"s;\n\n");
					bufferMethod.append("\tpublic List<"+fieldName+"> get"+fieldName+"s(){\n\t\treturn "+fieldName+"s;\n\t}\n\n");
					bufferMethod.append("\tpublic void set"+fieldName+"s(List<"+fieldName+"> "+fieldName+"s){\n\t\tthis."+fieldName+"s = "+fieldName+"s;\n\t}\n\n");
					build(map, maxEl, codePath);
				}
			}else if(num == 1 && subNum>0){//特殊列表（只有一个）
					buffer.append("\tprivate List<"+fieldName+"> "+fieldName+"s;\n\n");
					bufferMethod.append("\tpublic List<"+fieldName+"> get"+fieldName+"s(){\n\t\treturn "+fieldName+"s;\n\t}\n\n");
					bufferMethod.append("\tpublic void set"+fieldName+"s(List<"+fieldName+"> "+fieldName+"s){\n\t\tthis."+fieldName+"s = "+fieldName+"s;\n\t}\n\n");
					build(map, el, codePath);
			}
			
		}
		bufferMethod.append("\n}");
		buffer.append(bufferMethod);
		map.put(className, buffer);
		FileWriter out = new FileWriter(codePath+File.separator+className+".java");
		BufferedWriter writer = new BufferedWriter(out);
		writer.write(buffer.toString());
		if(out != null && writer != null){
			writer.close();
			out.close();
		}
		System.out.println(buffer.toString()+"\n\n------生成源代码："+className+".java-------\n\n");
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String xmlFile = "e:/playlist.xml";
		/**
		 * 1、通过XML文件生成java源代码。
		 */
		System.out.println(generage(xmlFile,  "E:\\Workspace_20140211\\commons\\src\\test"));
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
		Document doc = XmlUtils.objToDoc(obj);
		System.out.println(XmlUtils.getPrettyPrintXml(doc));
		
		
	}

}
