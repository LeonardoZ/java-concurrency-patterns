package br.com.leonardoz.patterns.thread_safe.thread_confinement;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Pattern: Thread Local Confinement
 * 
 * Example: Using a thread safe SimpleDateFormat object
 * 
 */
public class ThreadSafeDateFormat {

	private static final ThreadLocal<SimpleDateFormat> threadLocalDateFormat = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("DD/MM/YYYY HH:mm:ss");
		}
	};

	public String format(Date date) {
		return threadLocalDateFormat.get().format(date);
	}

}
