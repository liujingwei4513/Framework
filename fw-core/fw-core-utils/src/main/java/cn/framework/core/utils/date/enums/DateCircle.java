package cn.framework.core.utils.date.enums;

import cn.framework.core.utils.utils.TextUtils;

import java.util.GregorianCalendar;

/**
 * 时间周期枚举
 * @author LiuJingWei
 * @version 1.0
 */
public enum DateCircle {
	/** 年 */
	YEAR("Y", "年", GregorianCalendar.YEAR),
	/** 月 */
	MONTH("M", "月", GregorianCalendar.MONTH),
	/** 周 */
	WEEK("W", "周", GregorianCalendar.WEEK_OF_YEAR),
	/** 日 */
	DAY("D", "日", GregorianCalendar.DAY_OF_MONTH),

	/** 时 */
	HOUR("H", "时", GregorianCalendar.HOUR_OF_DAY),
	/** 分 */
	MINUTE("M", "分", GregorianCalendar.MINUTE),
	/** 秒 */
	SECOND("S", "秒", GregorianCalendar.SECOND),;

	private final String code;
	private final String desc;
	private final int ajust;

	private DateCircle(String code, String desc, int ajust) {
		this.code = code;
		this.desc = desc;
		this.ajust = ajust;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public static DateCircle getEnumByCode(String resCode) {
		if (TextUtils.isEmpty(resCode)) {
			return null;
		}

		for (DateCircle each : values()) {
			if (each.getCode().equals(resCode)) {
				return each;
			}
		}

		return null;
	}

	@Override
	public String toString() {
		return "[" + code + ":" + desc + "]";
	}

	public int getAjust() {
		return ajust;
	}

}
