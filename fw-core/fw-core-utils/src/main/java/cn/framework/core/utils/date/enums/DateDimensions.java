package cn.framework.core.utils.date.enums;


import cn.framework.core.utils.utils.TextUtils;

/**
 * 时间维度枚举
 * @author LiuJingWei
 * @version 1.0
 */
public enum DateDimensions {

	/** 今天 */
	TIME_TODAY("00", "今天"),
	/** 昨天 */
	TIME_YESTERDAY("01", "昨天"),
	/** 本周 */
	TIME_THISWEEK("10", "本周"),
	/** 上周 */
	TIME_LASTWEEK("11", "上周"),
	/** 本月 */
	TIME_THISMONTH("20", "本月"),
	/** 上月 */
	TIME_LASTMONTH("21", "上月"),
	/** 本年 */
	TIME_YEAR("30", "本年"),
	/** 全部 */
	TIME_ALL("90", "全部"),
	/** 自定义 */
	TIME_DEFINED("99", "自定义"),;

	private final String code;
	private final String desc;

	private DateDimensions(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public static DateDimensions getEnumByCode(String resCode) {
		if (TextUtils.isEmpty(resCode)) {
			return null;
		}
		for (DateDimensions each : values()) {
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
}
