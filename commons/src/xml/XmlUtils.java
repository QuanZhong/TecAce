package xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * DOM4J读写xml工具类(支持全部常规属性以及属性为java.util.List的集合,日期类型支持Calendar和Date两种类型，暂未支持TimeStamp)
 * 
 * @author quanzhong<br>
 */
public class XmlUtils {

	/**
	 * 
	 * 创建xml文档
	 * 
	 * @param fileName
	 * @throws Exception
	 */
	public static Document createDocument() {
		Document document = DocumentHelper.createDocument();
		return document;
	}

	/**
	 * 载入xml文档
	 * 
	 * @param filename
	 * @return
	 */
	public static Document load(String filename) {
		Document document = null;
		try {
			SAXReader saxReader = new SAXReader();
			document = saxReader.read(new File(filename));
		} catch (Exception e) {
		}
		return document;
	}

	/**
	 * 载入xml文档
	 * 
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public static Document load(URL url) throws Exception {
		Document document = null;
		SAXReader saxReader = new SAXReader();
		document = saxReader.read(url);
		return document;
	}

	/**
	 * xml字符串转为xml文档对象
	 * 
	 * @param text
	 * @return
	 * @throws DocumentException
	 */

	public static Document parseStringToXml(String text)
			throws DocumentException {
		Document document = DocumentHelper.parseText(text);
		return document;
	}

	/**
	 * xml文档对象转为String字符串
	 * 
	 * @param document
	 * @return
	 */
	public static String documentToString(Document document) {
		String docXmlText = document.asXML();
		return docXmlText;
	}

	/**
	 * 增加一个节点
	 * 
	 * @param element
	 * @param tagName
	 * @param text
	 */
	public static void addElement(Element element, String tagName, String text) {
		Element engNameElement = element.addElement(tagName);
		if (text != null) {
			engNameElement.setText(text);
		}
	}
	/**
	 * 把Document对象转换为美化格式的String
	 * @param document
	 * @return
	 * @throws IOException
	 */
	public static String getPrettyPrintXml(Document document) throws IOException{
		StringWriter strWriter = new StringWriter();
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter xmlWriter = new XMLWriter(strWriter, format);
		xmlWriter.write(document);
        return strWriter.toString();
	}
	/**
	 * xml文档写入xml文件
	 * 
	 * @param document
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public static boolean doc2XmlFile(Document document, String filename)
			throws Exception {
		boolean flag = true;
		XMLWriter writer = null;
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			writer = new XMLWriter(new OutputStreamWriter(
					new FileOutputStream(filename), "UTF-8"), format);
			writer.write(document);
			writer.flush();
		} catch (Exception ex) {
			flag = false;
			throw new Exception(ex);
		} finally {
		    writer.close();
		}
		return flag;
	}

	/**
	 * 
	 * @param tagName
	 * @param document
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean isExist(String tagName, Document document) {
		boolean flag = false;
		Element root = document.getRootElement();
		for (Iterator i = root.elementIterator(); i.hasNext();) {
			Element element = (Element) i.next();
			if (tagName.equals(element.getName())) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	public static Document objToDoc(Object obj) throws IllegalArgumentException, IllegalAccessException{
		Document document = createDocument();
		String className = obj.getClass().getSimpleName();
		Element element = document.addElement(className);
		builDobjToDoc(obj, element);
		return document;
		
	}
	@SuppressWarnings("unchecked")
	private static void builDobjToDoc(Object obj, Element element) throws IllegalArgumentException, IllegalAccessException{
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			String fieldName = field.getName();
			if (!"serialVersionUID".equals(fieldName)) {
				Object objField = field.get(obj);
				Class<?> fieldType = field.getType();
				String fieldTypeName = fieldType.getSimpleName();
				if ("List".equals(fieldTypeName) ||  "ArrayList".equals(fieldTypeName)) {
					List<Object> list = (List<Object>) objField;
					if (list != null) {
						for (Object c : list) {
							String attributeName = c.getClass().getSimpleName();
							Element attributeElement = element.addElement(attributeName);
							builDobjToDoc(c, attributeElement);
						}
					}
					
				} else {
					Element fieldElement = element.addElement(fieldName);
					if (objField != null) {
							fieldElement.setText(objField.toString());
					}

				}

			}

		}
	}
	/**
	 * 
	 * 将java对象映射为xml文档,当document为null时自动创建新文档，否则追加。
	 * 
	 * @param clazz
	 * @param obj
	 * @param document
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Document obj2xml(Object obj, Document document)
			throws Exception {
		Element root = null;
		if (document == null) {
			document = createDocument();
			String rootName = "oxm-app";
			root = document.addElement(rootName);
			root.addAttribute("version", "1.0");
		} else {
			root = document.getRootElement();
		}
		String className = obj.getClass().getSimpleName();
		Element objElement = root.addElement(className);
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			String fieldName = field.getName();
			if (!"serialVersionUID".equals(fieldName)) {
				Element fieldElement = objElement.addElement(fieldName);
				Object objField = field.get(obj);
				Class<?> fieldType = field.getType();
				String fieldTypeName = fieldType.getSimpleName();
				if ("List".equals(fieldTypeName)
						|| "ArrayList".equals(fieldTypeName)) {
					List<Object> list = (List<Object>) objField;
					if (list != null) {
						for (Object c : list) {
							String attributeName = c.getClass().getSimpleName();
							Element attributeElement = fieldElement
									.addElement(attributeName);
							Field[] attributeFields = c.getClass()
									.getDeclaredFields();
							for (Field f : attributeFields) {
								f.setAccessible(true);
								String attFieldName = f.getName();
								if (!"serialVersionUID".equals(attFieldName)) {
									Element attFieldElement = attributeElement
											.addElement(attFieldName);
									if (null != f.get(c)) {
										attFieldElement.setText(f.get(c)
												.toString());
									}
								}
							}
						}
					}
					
				} else {
					if (objField != null) {
						if("Calendar".equals(fieldTypeName)){
							Calendar calendar = (Calendar) objField;
							if(null != calendar){
								String dateString = DateUtils.convertToString(calendar, DateUtils.YEAR_MONTH_DATE_TIME_FORMAT);
								fieldElement.setText(dateString);
							}
						}else if("Date".equals(fieldTypeName)){
							Date date = (Date) objField;
							if(null != date){
								String dateString = DateUtils.convertToString(date, DateUtils.YEAR_MONTH_DATE_TIME_FORMAT);
								fieldElement.setText(dateString);
							}
						}else{
							fieldElement.setText(objField.toString());
						}
						
					}

				}

			}

		}
		return document;
	}

	/**
	 * 将集合对象映射成为xml文档,当document为null时自动创建新文档，否则追加。
	 * 
	 * @param list
	 * @param document
	 * @return
	 * @throws Exception
	 */
	public static Document writeList(List<? extends Object> list,
			Document document) throws Exception {
		if (list == null)
			return null;
		for (int i = 0; i < list.size(); i++) {
			if (i == 0 && document == null) {
				document = obj2xml(list.get(i), document);
			} else {
				obj2xml(list.get(i), document);
			}
		}
		return document;
	}

	/**
	 * xml文件转化为java对象，目前支持String类型java属性以及List\ArrayList类型java属性，暂未支持其余java类型，后续可以依据需要扩展。
	 * 要求，所设计的java类必须放置于同一package下。包名不做限制。
	 * @param document
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public static <T> Object docToObj(Document document, Class<T> clazz) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		String pacage = clazz.getPackage().getName();//包名
		Object obj = Class.forName(clazz.getName()).newInstance();
		Element root = document.getRootElement();
		build(obj,pacage,root);
		return obj;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void build(Object obj,String pacage,Element root) throws IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException{
		Field[] fields = obj.getClass().getDeclaredFields();
		System.out.println("构建对象："+obj.getClass().getName());
		for (Iterator i = root.elementIterator(); i.hasNext();) {
			
			Element element = (Element) i.next();
			String fieldName = element.getName();
			String value = element.getTextTrim();
			for (Field f : fields) {
				f.setAccessible(true);
				if (fieldName.equals(f.getName()) || (fieldName+"s").equals(f.getName())) {//s考虑列表等复数类型
					if ((f.getType() == List.class) || (f.getType() == ArrayList.class)) {
						List objList = (List) f.get(obj);
						if(objList == null){
							objList = new ArrayList();
							f.set(obj, objList);
						}
						Object subObj = Class.forName(pacage+"."+fieldName).newInstance();
						objList.add(subObj);
						build(subObj,pacage,element);
						
					}else{//咱全部默认已String方式，不考虑其余java类型
						f.set(obj, value);
					}
					
				}
				
			}
			System.out.println("\t\t"+fieldName+"="+value);
		}
	}
	/**
	 * 将xml文档转化为对象返回(支持List类型的集合属性) (待处理的问题,需要对static
	 * final类型做判断,需要对枚举类型做处理,对对象属性的处理)
	 * 
	 * @param document
	 * @param clazz
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> List<T> xml2obj(Document document, Class<T> clazz)
			throws Exception {
		List<T> resultList = new ArrayList<T>();
		Element root = document.getRootElement();
		for (Iterator i = root.elementIterator(); i.hasNext();) {
			String className = clazz.getSimpleName();
			Element element = (Element) i.next();
			if (className.equals(element.getName())) {
				Object obj = Class.forName(clazz.getName()).newInstance();
				Field[] fields = obj.getClass().getDeclaredFields();
				for (Iterator j = element.elementIterator(); j.hasNext();) {
					Element elementField = (Element) j.next();
					String fieldName = elementField.getName();
					String value = elementField.getTextTrim();
					if (value != null) {
						for (Field f : fields) {
							f.setAccessible(true);
							if (fieldName.equals(f.getName())) {
								if (f.getType() == String.class) {
									if (!"".equals(value.trim()))
										f.set(obj, value);
								} else if (f.getType() == int.class) {
									if (!"".equals(value.trim()))
										f.setInt(obj, Integer.parseInt(value));
								} else if (f.getType() == Integer.class) {
									if (!"".equals(value.trim()))
										f.set(obj, Integer.valueOf(value));
								} else if (f.getType() == double.class) {
									if (!"".equals(value.trim()))
										f.setDouble(obj,
												Double.parseDouble(value));
								} else if (f.getType() == Double.class) {
									if (!"".equals(value.trim()))
										f.set(obj, Double.valueOf(value));
								} else if (f.getType() == long.class) {
									if (!"".equals(value.trim()))
										f.setLong(obj, Long.parseLong(value));
								} else if (f.getType() == Long.class) {
									if (!"".equals(value.trim()))
										f.set(obj, Long.valueOf(value));
								} else if (f.getType() == short.class) {
									if (!"".equals(value.trim()))
										f.setShort(obj, Short.parseShort(value));
								} else if (f.getType() == Short.class) {
									if (!"".equals(value.trim()))
										f.set(obj, Short.valueOf(value));
								} else if (f.getType() == char.class) {
									if (!"".equals(value.trim()))
										f.setChar(obj, value.charAt(0));
								} else if (f.getType() == Character.class) {
									if (!"".equals(value.trim()))
										f.set(obj, Character.valueOf(value
												.charAt(0)));
								} else if (f.getType() == float.class) {
									if (!"".equals(value.trim()))
										f.setFloat(obj, Float.parseFloat(value));
								} else if (f.getType() == Float.class) {
									if (!"".equals(value.trim()))
										f.set(obj, Float.valueOf(value));
								} else if (f.getType() == boolean.class) {
									if (!"".equals(value.trim()))
										f.setBoolean(obj,
												Boolean.parseBoolean(value));
								} else if (f.getType() == Boolean.class) {
									if (!"".equals(value.trim()))
										f.set(obj, Boolean.valueOf(value));
								} else if (f.getType() == byte.class) {
									if (!"".equals(value.trim()))
										f.setByte(obj, Byte.parseByte(value));
								} else if (f.getType() == Byte.class) {
									if (!"".equals(value.trim()))
										f.set(obj, Byte.valueOf(value));
								} else if ((f.getType() == List.class)
										|| (f.getType() == ArrayList.class)) {
									List<Element> eList = elementField
											.elements();
									if (eList != null && eList.size() > 0) {
										List list = new ArrayList<T>();
										for (Element elementChildFielld : eList) {
											List<Element> ecf = elementChildFielld
													.elements();
											Type fieldType = f.getGenericType();
											Type[] types = ((ParameterizedType) fieldType)
													.getActualTypeArguments();
											Object obj2 = Class.forName(
													types[0].toString().split(
															" ")[1])
													.newInstance();
											for (Element e : ecf) {
												String cvalue = e.getTextTrim();
												if (cvalue != null) {
													Field[] finalFields = obj2
															.getClass()
															.getDeclaredFields();
													for (Field ff : finalFields) {
														ff.setAccessible(true);
														if (e.getName().equals(
																ff.getName())) {
															if (ff.getType() == String.class) {
																ff.set(obj2,
																		cvalue);
															} else if (ff
																	.getType() == int.class) {
																ff.setInt(
																		obj2,
																		Integer.parseInt(cvalue));
															} else if (ff
																	.getType() == Integer.class) {
																ff.set(obj2,
																		Integer.valueOf(cvalue));
															} else if (ff
																	.getType() == double.class) {
																ff.setDouble(
																		obj2,
																		Double.parseDouble(cvalue));
															} else if (ff
																	.getType() == Double.class) {
																ff.set(obj2,
																		Double.valueOf(cvalue));
															} else if (ff
																	.getType() == long.class) {
																ff.setLong(
																		obj2,
																		Long.parseLong(cvalue));
															} else if (ff
																	.getType() == Long.class) {
																ff.set(obj2,
																		Long.valueOf(cvalue));
															} else if (ff
																	.getType() == short.class) {
																ff.setShort(
																		obj2,
																		Short.parseShort(cvalue));
															} else if (ff
																	.getType() == Short.class) {
																ff.set(obj2,
																		Short.valueOf(cvalue));
															} else if (ff
																	.getType() == char.class) {
																ff.setChar(
																		obj2,
																		cvalue.charAt(0));
															} else if (ff
																	.getType() == Character.class) {
																ff.set(obj2,
																		Character
																				.valueOf(cvalue
																						.charAt(0)));
															} else if (ff
																	.getType() == float.class) {
																ff.setFloat(
																		obj2,
																		Float.parseFloat(cvalue));
															} else if (ff
																	.getType() == Float.class) {
																ff.set(obj2,
																		Float.valueOf(cvalue));
															} else if (ff
																	.getType() == Boolean.class) {
																ff.setBoolean(
																		obj2,
																		Boolean.parseBoolean(cvalue));
															} else if (ff
																	.getType() == Boolean.class) {
																ff.set(obj2,
																		Boolean.valueOf(cvalue));
															} else if (ff
																	.getType() == byte.class) {
																ff.setByte(
																		obj2,
																		Byte.parseByte(cvalue));
															} else if (ff
																	.getType() == Byte.class) {
																ff.set(obj2,
																		Byte.valueOf(cvalue));
															} else if (f
																	.getType() == Calendar.class) {
																if (!"".equals(value
																		.trim())) {
																	Calendar c = null;
																	try {
																		if(value != null){
																			c = DateUtils.convertToCalendar(value, DateUtils.YEAR_MONTH_DATE_TIME_FORMAT);
																		}
																	} catch (Exception ee) {
																		throw new Exception(
																				"xml文件中日期格式不正确："
																						+ value);
																	}
																	f.set(obj,
																			c);
																}
															} else if (f
																	.getType() == Date.class) {
																if (!"".equals(value
																		.trim())) {
																	try {
																		Date date = null;
																		if(value != null){
																			date = DateUtils.convertToDate(value, DateUtils.YEAR_MONTH_DATE_TIME_FORMAT);
																		}
																		f.set(obj,date);
																	} catch (Exception ee) {
																		throw new Exception(
																				"xml文件中日期格式不正确："
																						+ value);
																	}
																}
															}else {
																throw new Exception(
																		"暂不支持此类型属性映射："
																				+ f.getType());
															}

														}
													}

												}

											}
											list.add(obj2);

										}
										f.set(obj, list);
									}

								} else if (f.getType() == Calendar.class) {
									if (!"".equals(value.trim())) {
										Calendar c = null;
										try {
											if(value != null){
												c = DateUtils.convertToCalendar(value, DateUtils.YEAR_MONTH_DATE_TIME_FORMAT);
											}
										} catch (Exception e) {
											throw new Exception(
													"xml文件中日期格式不正确：" + value);
										}
										f.set(obj, c);
									}
								} else if (f.getType() == Date.class) {
									if (!"".equals(value.trim())) {
										try {
											Date date = null;
											if(value != null){
												date = DateUtils.convertToDate(value, DateUtils.YEAR_MONTH_DATE_TIME_FORMAT);
											}
											f.set(obj,date);
										} catch (Exception e) {
											throw new Exception(
													"xml文件中日期格式不正确：" + value);
										}
									}
								}else {
									throw new Exception("暂不支持此类型属性映射："
											+ f.getType());
								}
							}
						}
					}
				}
				resultList.add((T) obj);
			}

		}
		return resultList;
	}

}
