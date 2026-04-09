package br.com.leonardoz.patterns.thread_safe.thread_confinement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Pattern: Thread Local Confinement
 *
 * Example: Thread-safe date/time formatting using {@link DateTimeFormatter}.
 *
 * Java 8+ note: {@link DateTimeFormatter} is immutable and thread-safe, so it
 * can be stored in a plain {@code static final} field without any
 * {@link ThreadLocal} wrapper. This example shows the preferred modern approach
 * and intentionally omits {@code ThreadLocal} to highlight that the pattern is
 * unnecessary when the underlying object is already thread-safe.
 *
 * If you are still using the legacy {@link java.text.SimpleDateFormat} (which
 * is NOT thread-safe), use {@code ThreadLocal.withInitial()} instead:
 *
 * <pre>{@code
 * private static final ThreadLocal<SimpleDateFormat> FMT =
 *     ThreadLocal.withInitial(() -> new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));
 * }</pre>
 */
public class ThreadSafeDateFormat {

	// DateTimeFormatter is immutable and thread-safe — no ThreadLocal needed.
	private static final DateTimeFormatter FORMATTER =
			DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	public String format(LocalDateTime dateTime) {
		return FORMATTER.format(dateTime);
	}

}
