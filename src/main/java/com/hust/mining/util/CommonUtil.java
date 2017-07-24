package com.hust.mining.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hust.mining.constant.Constant;
import com.hust.mining.constant.Constant.Interval;

import org.apache.commons.lang.StringUtils;

public class CommonUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

	public static boolean hasEmptyArray(String[] array) {
		if (null == array || array.length == 0) {
			return true;
		}
		for (int i = 0; i < array.length; i++) {
			if (StringUtils.isBlank(array[i])) {
				return true;
			}
		}
		return false;
	}

	public static boolean isEmptyArray(String[] array) {
		if (null == array || array.length == 0) {
			return true;
		}
		for (int i = 0; i < array.length; i++) {
			if (!StringUtils.isBlank(array[i])) {
				return false;
			}
		}
		return true;
	}

	public static String getPrefixUrl(String url) {
		if (StringUtils.isEmpty(url)) {
			return StringUtils.EMPTY;
		}
		try {

			int pure = url.indexOf("/", url.indexOf("://") + 3);
			if (pure == -1) {
				return url;
			} else {
				String prefix = url.substring(0, pure);
				return prefix;
			}
		} catch (Exception e) {
			logger.error("get prefix of url failed, url :{}, exception:{}", url, e.toString());
			return StringUtils.EMPTY;
		}
	}
	
	public static String getTimeKey(String time, int interval) {
		if (StringUtils.isBlank(time) || !TimeUtil.isvalidate(time)) {
			return Constant.INVALID_TIME;
		}
		switch (interval) {
		case Interval.HOUR: {
			return time.substring(0, 13);
		}
		case Interval.DAY: {
			return time.substring(0, 10);
		}
		case Interval.MONTH: {
			return time.substring(0, 7);
		}
		default: {
			return Constant.INVALID_TIME;
		}
		}
	}
}
