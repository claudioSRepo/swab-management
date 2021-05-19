package it.cs.contact.tracing.be.utils;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Util {

	private static final Logger logger = LoggerFactory.getLogger(Util.class);

	public static void logEnvironment(Object event, Context context, Gson gson) {

		// log execution details
		logger.info("ENVIRONMENT VARIABLES: {}", gson.toJson(System.getenv()));
		logger.info("CONTEXT: {}", gson.toJson(context));

		// log event details
		logger.info("EVENT: {}", gson.toJson(event));
		logger.info("EVENT TYPE: {}", event.getClass());
	}

	public static <K, V> Map.Entry<K, V> entry(final K key, final V value) {

		return new AbstractMap.SimpleEntry<>(key, value);
	}

	public static <K, U> Collector<Map.Entry<K, U>, ?, Map<K, U>> entriesToMap() {
		return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue);
	}

	public static int dateToNumber(final LocalDate date) {
		return Integer.parseInt(date.format(DateTimeFormatter.BASIC_ISO_DATE));
	}

	public static String dateToString(final LocalDate date) {
		return date.format(DateTimeFormatter.BASIC_ISO_DATE);
	}

	public static LocalDate numberToDate(final int num) {

		try {

			return stringToDate(String.valueOf(num));
		}
		catch (final Exception e) {

			logger.error("Error formatting number: {}", num);
			return null;
		}
	}

	public static LocalDate stringToDate(final String str) {

		try {

			return LocalDate.parse(str, DateTimeFormatter.BASIC_ISO_DATE);

		}
		catch (final Exception e) {

			logger.error("Error formatting string: {}", str);
			return null;
		}
	}

	public static boolean isValidDate(final Integer date) {

		return isValidDate(String.valueOf(date));
	}

	public static boolean isValidDate(final String date) {

		try {

			LocalDate.parse(date, DateTimeFormatter.BASIC_ISO_DATE);
			return true;
		}
		catch (final Exception e) {

			logger.error("Error formatting date: {}", date);
			return false;
		}

	}
}