package com.ds.transfer.http.util;

import java.util.Calendar;

public class DateUtil {

	public static void main(String[] args) {
		String from = "2016-01-09 00:00:00";
		String to = "2016-03-09 23:59:59";
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2016);
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 9);
		System.out.println(c.getTime());
	}

}
