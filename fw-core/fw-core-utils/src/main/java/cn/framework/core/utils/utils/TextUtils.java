package cn.framework.core.utils.utils;


import com.google.gson.*;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文本工具类
 * @author LiuJingWei
 * @version 1.0
 */
public class TextUtils {
	private static final String LIST = "List";

	private static final String STR = ".";

	private static final int NUMBER_10 = 10;

	private static Logger logger = Logger.getLogger(TextUtils.class);

	private final static String[] HEX_DIGITS = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	private TextUtils() {
	}

	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b & 0xff;
		int d1 = n / 16;
		int d2 = n % 16;
		return HEX_DIGITS[d1] + HEX_DIGITS[d2];
	}

	public static String md5Encode(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
		} catch (Exception e) {
			logger.error("", e);
		}
		return resultString;
	}

	public static String md5CheckkSum(String fileName) {
		FileInputStream fin = null;
		String resultString = null;
		try {
			byte[] bs = new byte[1024];
			int len = 0;
			fin = new FileInputStream(fileName);
			MessageDigest md = MessageDigest.getInstance("MD5");

			while ((len = fin.read(bs)) > 0) {
				md.update(bs, 0, len);
				break;

			}
			resultString = byteArrayToHexString(md.digest());
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			if (fin != null) {
				try {
					fin.close();
				} catch (IOException e) {
				}
			}
		}

		return resultString;
	}

	/**
	 * 将str中的所有replaceChar全部顺序替换为param，如果为string，两边加单引号
	 */
	public static String replace4Sql(String str, char replaceChar, List<Object> param) {
		StringBuilder sb = new StringBuilder(str);
		StringBuilder rs = new StringBuilder();
		int offset = 0;
		int paramOffset = 0;
		for (int i = 0; i < sb.length(); i++) {
			if (replaceChar == sb.charAt(i) && paramOffset < param.size()) {
				rs.append(sb.substring(offset, i));
				if (param.get(paramOffset) instanceof String) {
					rs.append("'");
					rs.append(param.get(paramOffset));
					rs.append("'");
				} else {
					rs.append(param.get(paramOffset));
				}

				offset = i + 1;
				paramOffset++;
			}
		}
		rs.append(sb.substring(offset));
		return rs.toString();
	}

	public static String arrayToString(String[] strArray) {
		StringBuilder sb = new StringBuilder();
		for (Object s : strArray) {
			sb.append(s);
			sb.append(",");
			sb.append(" ");
		}
		sb.delete(sb.length() - 2, sb.length());
		return sb.toString();
	}

	public static String[] stringToArray(String str, String cc) {
		if (isEmpty(str) || isEmpty(cc)) {
			return null;
		}
		try {
			return str.split(cc);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * json字符串转map
	 */
	public static Map<String, String> jsonStringToSimpleMap(String jsonString) {
		if (isEmpty(jsonString)) {
			return new HashMap<String, String>(16);
		}

		JsonParser jsonParser = new JsonParser();
		Gson gson = new Gson();
		JsonElement jsonE = jsonParser.parse(jsonString);
		@SuppressWarnings("unchecked")
		Map<String, String> jsonMap = gson.fromJson(jsonE, Map.class);
		return jsonMap;
	}

	/**
	 * json字符串转map
	 */
	public static Map<String, Object> jsonStringToObjectMap(String jsonString) {
		if (isEmpty(jsonString)) {
			return new HashMap<String, Object>(16);
		}

		JsonParser jsonParser = new JsonParser();
		Gson gson = new Gson();
		JsonElement jsonE = jsonParser.parse(jsonString);
		@SuppressWarnings("unchecked")
		Map<String, Object> jsonMap = gson.fromJson(jsonE, Map.class);
		return jsonMap;
	}

	/**
	 * Object类型字符串还原成Object
	 */
	public static <T> T objectStringToObject(String objectString, Class<T> clazz) {
		if (isEmpty(objectString) || clazz == null) {
			logger.error("json转换类型数据失败，objectString：" + objectString + "，Class：" + clazz);
			return null;
		}
		try {
			Gson gson = new Gson();
			T t = gson.fromJson(objectString, clazz);
			return t;
		} catch (JsonSyntaxException e) {
			logger.error("json转换异常，" + e.getMessage());
		}
		return null;
	}

	/**
	 * json字符串转指定类型
	 */
	public static <T> T jsonStringToObject(String jsonString, Class<T> clazz) {
		if (isEmpty(jsonString) || clazz == null) {
			logger.error("json转换类型数据失败，jsonString：" + jsonString + "，Class：" + clazz);
			return null;
		}
		try {
			JsonParser jsonParser = new JsonParser();
			Gson gson = new Gson();
			JsonElement jsonE = jsonParser.parse(jsonString);
			T t = gson.fromJson(jsonE, clazz);
			return t;
		} catch (JsonSyntaxException e) {
			logger.error("json转换异常，" + e.getMessage());
		}
		return null;
	}

	public static String simpleMapToJsonString(Map<String, String> map, boolean serializeFlag) {
		if (serializeFlag) {
			Gson gson = new GsonBuilder().serializeNulls().create();
			return gson.toJson(map);
		} else {
			return simpleMapToJsonString(map);
		}
	}

	/**
	 * map转json字符串
	 */
	public static String simpleMapToJsonString(Map<String, String> map) {
		Gson gson = new Gson();
		return gson.toJson(map);
	}

	/**
	 * List转json字符串
	 */
	public static String objectListToJsonString(List<Object> list) {
		return new Gson().toJson(list);
	}

	/**
	 * map转json字符串
	 */
	public static String objectMapToJsonString(Map<String, Object> map) {
		Gson gson = new Gson();
		return gson.toJson(map);
	}

	/**
	 * ConcurrentMap转json字符串
	 */
	public static String objectConcurrentMapToJsonString(ConcurrentMap<String, Object> map) {
		Gson gson = new Gson();
		return gson.toJson(map);
	}

	/**
	 * map转json字符串
	 */
	public static <T> T objectMapToObject(Map<String, Object> map, Class<T> clazz) {
		Gson gson = new Gson();
		String json = gson.toJson(map);
		T t = gson.fromJson(json, clazz);
		return t;
	}

	/**
	 * <pre>
	 * Object按属性转换为json
	 * </pre>
	 */
	public static String objectToJsonString(Object object) {
		Gson gson = new Gson();
		return gson.toJson(object);
	}

	/**
	 * <pre>
	 * Object按属性转换为Map键值对
	 * </pre>
	 */
	public static Map<String, String> objectToSimpleMap(Object object) {
		if (object == null) {
			return null;
		}

		Gson gson = new Gson();
		Map<String, String> jsonMap = TextUtils.jsonStringToSimpleMap(gson.toJson(object));
		return jsonMap;
	}

	/**
	 * <pre>
	 * Object按属性转换为Map键值对
	 * </pre>
	 */
	public static String objectToJsonStringNull(Object object) {
		return new GsonBuilder().serializeNulls().create().toJson(object);
	}

	/**
	 * <pre>
	 * Object按属性转换为Map键值对
	 * </pre>
	 */
	public static Map<String, Object> objectToObjectMap(Object object) {
		if (object == null) {
			return null;
		}

		Gson gson = new Gson();
		Map<String, Object> jsonMap = TextUtils.jsonStringToObjectMap(gson.toJson(object));
		return jsonMap;
	}

	/**
	 * map转json字符串
	 */
	public static String mapToJsonString(Map<String, String> map) {
		if (map == null) {
			return null;
		}

		Gson gson = new Gson();
		return gson.toJson(map);
	}


	/**
	 * 取map值，默认为空
	 */
	public static String getMapKeyValue(Map<String, Object> map, String key) {
		Object value = map.get(key);
		return nvl(value, "");
	}


	/**
	 * 判断是否为整数
	 * 
	 * @param str 传入的字符串
	 * @return 是整数返回true,否则返回false
	 */
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断字符串是否在后续的数组中 return true:在数组中 exception: nullPointerException
	 */
	public static boolean inArray(String src, String... arr) {
		if (LIST.equals(src)) {
			TextUtils.class.getClass();
		}
		for (String s : arr) {
			if (src.equals(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否包含，多个String
	 */
	public static boolean contains(String line, String... strings) {

		for (String s : strings) {
			if (line.contains(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <pre>
	 * 格式化屏蔽手机号，如：123****789；屏蔽对象包括：卡号、证件号、手机号等
	 * </pre>
	 */
	public static String encryptMobileNoFormat(String srcStr) {
		if (srcStr == null) {
			return srcStr;
		}

		int length = srcStr.length();

		return srcStr.substring(0, 3) + "****" + srcStr.substring(length - 4);
	}

	/**
	 * <pre>
	 * 格式化屏蔽字符串，如：342622****789；屏蔽对象适用于：卡号、证件号等
	 * </pre>
	 */
	public static String encryptFieldFormat(String srcStr) {
		if (srcStr == null || srcStr.length() < NUMBER_10) {
			return srcStr;
		}

		int length = srcStr.length();

		return srcStr.substring(0, 6) + "****" + srcStr.substring(length - 4);
	}

	/**
	 * 根据编码格式获取字符串
	 */
	public static String obtainStr(byte[] bytes, String encode) {
		String str = null;
		try {
			str = new String((byte[]) bytes, encode);
		} catch (UnsupportedEncodingException e) {
			logger.error("", e);
		}
		return str;
	}

	public static String encode(Map<String, String> map, String charSet) throws UnsupportedEncodingException {
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String name = entry.getKey();
			String value = entry.getValue();
			builder.append(name).append("=").append(URLEncoder.encode(value, charSet)).append("&");
		}
		String form = builder.substring(0, builder.length());
		return form;
	}

	public static Map<String, String> convertValueType(Map<String, Object> objectMap) {
		Map<String, String> strMap = new HashMap<String, String>(16);
		Iterator<String> strIt = objectMap.keySet().iterator();
		while (strIt.hasNext()) {
			String key = strIt.next();
			Object value = objectMap.get(key);
			if (value instanceof Integer) {
				strMap.put(key, String.valueOf(objectMap.get(key)));
			} else {
				strMap.put(key, (String) objectMap.get(key));
			}
		}
		return strMap;
	}

	public static boolean isEmpty(Object str) {
		if (str == null || str.toString().length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 判断参数是否为空（多个参数）
	 * <li>1. 传入的参数，无论是谁为空都返回true</li>
	 * <li>1. 传入的参数，都不为空才返回false</li> <span><br>
	 * <b>eg: </b><br>
	 * TextUtils.isEmpty("a", null, "c"); == true<br>
	 * TextUtils.isEmpty("a", "b", "aa"); == false </span>
	 */
	public static boolean isEmpty(Object... objs) {
		boolean mark = false;
		for (Object obj : objs) {
			if (isEmpty(obj)) {
				mark = true;
			}
		}
		return mark;
	}

	/**
	 * * 输入参数为null时返回null，否则去除掉字符串两边的空格或者制表符（tab键，一个tab键代表两个空格）；
	 */
	public static String trim(String str) {
		return str == null ? null : str.trim();
	}

	/**
	 * 输入参数首先调用trim()方法处理去掉两边的空格或者制表符（tab键，一个tab键代表两个空格）<br>
	 * 如果输入参数为null则返回null，然后判断处理后的参数是否为空，如果为空就返回null，否则返回ts；
	 */
	public static String trimToNull(String str) {
		String ts = trim(str);
		return isEmpty(ts) ? null : ts;
	}

	public static String nvl(String str) {
		return nvl(str, "");
	}

	public static String nvl(Object str) {
		return nvl(str, "");
	}

	public static String nvl(String str, String defStr) {
		if (isEmpty(str)) {
			return defStr;
		}
		return str;
	}

	public static String nvl(Object str, String defStr) {
		if (str == null || str.equals("")) {
			return defStr;
		}
		return str.toString();
	}

	public static String nvlNull(Object str, String defStr) {
		if (str == null) {
			return defStr;
		}
		return str.toString();
	}

	/**
	 * 字符串编码格式转换
	 * 
	 * @param source 源字符串
	 * @param srcEncoding 源编码格式
	 * @param destEncoding 目标编码格式
	 * @return
	 */
	public static String convertEncoding(String source, String srcEncoding, String destEncoding) {
		if (isEmpty(srcEncoding) || isEmpty(destEncoding) || destEncoding.equals(srcEncoding)) {
			return source;
		}

		String result = null;
		try {
			result = new String(source.getBytes(srcEncoding), destEncoding);
		} catch (UnsupportedEncodingException e) {
			result = source;
		}

		return result;
	}

	/**
	 * 判断是否为简单数据类型
	 * 
	 * @param object
	 * @return
	 */
	public static boolean isSimpleData(Object object) {
		if (object instanceof String || object instanceof Integer || object instanceof Double || object instanceof Float || object instanceof Long || object instanceof Boolean) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 将str将多个分隔符进行切分，
	 * 
	 * 示例：StringTokenizerUtils.split("1,2;3 4"," ,;"); 返回: ["1","2","3","4"]
	 * 
	 * @param str
	 * @param seperators
	 * @return
	 */
	@SuppressWarnings("all")
	public static String[] split(String str, String seperators) {
		StringTokenizer tokenlizer = new StringTokenizer(str, seperators);
		List<Object> result = new ArrayList<Object>();
		while (tokenlizer.hasMoreElements()) {
			Object s = tokenlizer.nextElement();
			result.add(s);
		}
		return (String[]) result.toArray(new String[result.size()]);
	}

	public static Map<String, Object> objectMapCopy(Map<String, Object> srcMap) {
		Map<String, Object> tragetMap = new HashMap<String, Object>(16);
		for (String key : srcMap.keySet()) {
			tragetMap.put(key, srcMap.get(key));
		}
		return tragetMap;
	}

	public static Map<String, String> simpleMapCopy(Map<String, String> srcMap) {
		Map<String, String> tragetMap = new HashMap<String, String>(16);
		for (String key : srcMap.keySet()) {
			tragetMap.put(key, srcMap.get(key));
		}
		return tragetMap;
	}

	/**
	 * 功能：根据限制长度截取字符串（字符串中包括汉字，一个汉字等于两个字符）
	 * 
	 * @param strParameter 要截取的字符串
	 * @param limitLength 截取的长度
	 * @return 截取后的字符串
	 */
	public static String cutStringHanzi(String strParameter, int limitLength) {
		if (isEmpty(strParameter)) {
			return "";
		}

		if (strParameter.getBytes().length <= limitLength) {
			return strParameter;
		}

		// 返回的字符串
		String returnStr = strParameter;
		// 将汉字转换成两个字符后的字符串长度
		int tempInt = 0;
		// 对原始字符串截取的长度
		int cutInt = 0;

		byte[] b = strParameter.getBytes();
		for (int i = 0; i < b.length; i++) {
			if (b[i] >= 0) {
				tempInt = tempInt + 1;
			} else {
				// 一个汉字等于两个字符
				tempInt = tempInt + 2;
				i++;
			}
			cutInt++;

			if (tempInt >= limitLength) {
				if (tempInt % 2 != 0 && b[tempInt - 1] < 0) {
					cutInt--;
				}
				returnStr = returnStr.substring(0, cutInt);
				break;
			}
		}
		return returnStr;
	}

	/**
	 * <pre>
	 * 获取系统换行符
	 * </pre>
	 * 
	 * @return
	 */
	public static String systemLine() {
		return System.getProperty("line.separator");
	}

	/**
	 * 格式化数据，前补或后补特定字符
	 * 
	 * @param formatStr 待格式化字符
	 * @param addChar 增加字符
	 * @param flag 0：前补充，1：后补充
	 * @param num 字符长度
	 */
	public static String formatString(String formatStr, String addChar, int flag, int num) {
		String freshStr = formatStr;
		if (formatStr.length() >= num) {
			return freshStr;
		}
		// 原字符串长度小于要求处理的字符长度，将按前后加addChar处理
		while (freshStr.length() < num) {
			switch (flag) {
			case 0:
				freshStr = addChar + freshStr;
				break;
			case 1:
				freshStr = freshStr + addChar;
				break;
			default:
				break;
			}
		}

		return freshStr;
	}

	/**
	 * 格式化String类型数字 <b>小数点</b> 后位数
	 * 
	 * @param s 要格式化的数据
	 * @param indexCount 保留小数点后index<b>(int)</b>位
	 * @author luojun
	 * @date 2017-03-10 01:48:15
	 * @version 1.0.0
	 */
	public static String formatNumberString(String s, int indexCount) {
		if (isEmpty(s)) {
			return "";
		}
		// 没有小数点，直接返回
		if (s.indexOf(STR) == -1) {
			return s;
		}
		String[] arr = s.split("\\.");
		// 小数点后不够5位，直接返回
		if (arr[1].length() < indexCount) {
			return s;
		}
		return s.substring(0, s.indexOf(STR)) + s.substring(s.indexOf(STR), s.indexOf(STR) + (indexCount + 1));
	}

	/**
	 * 过滤SQL语句特殊字符
	 */
	public static String replaceSpecial4Sql(String srcSql) {
		if (isEmpty(srcSql)) {
			return srcSql;
		}
		srcSql = srcSql.replaceAll("<", "&lt;");
		srcSql = srcSql.replaceAll(">", "&gt;");
		return srcSql;
	}

	/**
	 * 过滤[<b>'</b>]，防sql注入
	 */
	public static String replace4SqlInject(String srcSql) {
		if (isEmpty(srcSql)) {
			return srcSql;
		}
		srcSql = srcSql.replaceAll("'", "''");
		return srcSql;
	}

	/**
	 * 正则表达式校验
	 */
	public static boolean regexValidate(String str, String regexPattern) {
		if (isEmpty(str)) {
			return false;
		}
		Pattern pattern = Pattern.compile(regexPattern);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/**
	 * APP版本判断是否已是最新版本，true:是最新版本，false:不是最新版本
	 * 
	 * @param clientVer
	 * @param serverVer
	 * @return
	 */
	public static boolean isLastestVer(String clientVer, String serverVer) {
		if (isEmpty(clientVer, clientVer)) {
			return false;
		}
		if (clientVer.equals(serverVer)) {
			return true;
		}
		String[] clients = clientVer.split("\\.");
		String[] servers = serverVer.split("\\.");
		for (int i = 0; i < clients.length && i < servers.length; i++) {
			String client = clients[i];
			String server = servers[i];
			if (Integer.parseInt(client) < Integer.parseInt(server)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取服务器IP地址
	 * 
	 * @return
	 */
	public static String getServerIp() {
		String ip = "127.0.0.1";
		try {
			// 获取服务器IP地址
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			logger.error("", e);
		}
		return ip;
	}

	public static boolean isNumeric(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isDigit(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 字符串转换大小写
	 * <li>caseFlag = 0：小写</li>
	 * <li>caseFlag = 1：大写</li>
	 */
	public static String coverCaseString(String source, int caseFlag) {
		if (TextUtils.isEmpty(source)) {
			return source;
		}
		try {
			switch (caseFlag) {
			case 0:
				return source.toLowerCase();
			case 1:
				return source.toUpperCase();
			default:
				return source;
			}
		} catch (Exception e) {
			// 防御性容错
			return source;
		}
	}

	/**
	 * 对象转换为Int类型数据
	 */
	public static int objectConvertInt(Object number) {
		if (number == null) {
			return 0;
		}
		if (number instanceof Double) {
			return ((Double) number).intValue();
		} else if (number instanceof BigDecimal) {
			return ((BigDecimal) number).intValue();
		}
		return Integer.parseInt(number.toString());
	}

	/**
	 * 字符串分割后匹配
	 * @param source 源字符串
	 * @param split 分隔符
	 * @param value 需匹配的字符串
	 * @return
	 */
	public static boolean characterStringSplitMatching(String source, String split, String value) {
		if(TextUtils.isEmpty(source) || TextUtils.isEmpty(split) || TextUtils.isEmpty(value)){
			return false;
		}
		String[] sourceArray;
		if (source.indexOf(",") <= 0) {
			sourceArray = new String[1];
			sourceArray[0] = source;
		} else {
			sourceArray = source.split(split);
		}
		for (String sa : sourceArray) {
			if (sa.equals(value)) {
				return true;
			}
		}
		return false;
	}
}