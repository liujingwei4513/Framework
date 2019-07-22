package cn.framework.core.utils.utils;

import java.util.*;

/**
 * List工具类
 * @author LiuJingWei
 * @version 1.0
 */
public class ListUtils {

	/**
	 * List集合不为空
	 */
	public static boolean isNotEmpty(List<?> list) {
		return !isEmpty(list);
	}

	/**
	 * 判断list集合为空
	 */
	public static boolean isEmpty(List<?> list) {
		if (list == null || list.isEmpty()) {
			return true;
		}
		return false;
	}
}
