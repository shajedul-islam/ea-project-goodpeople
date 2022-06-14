package com.miu.ea.goodpeople;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTest {

	public static void main(String[] a) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

    	ZonedDateTime zdtWithZoneOffset = ZonedDateTime.parse("2022-06-13T05:00 Z[UTC]", formatter);
    	System.out.println("********************** time: " + zdtWithZoneOffset);
	}
}
