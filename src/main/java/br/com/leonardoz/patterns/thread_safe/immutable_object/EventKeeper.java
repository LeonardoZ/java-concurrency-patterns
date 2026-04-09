package br.com.leonardoz.patterns.thread_safe.immutable_object;

import java.util.Arrays;
import java.util.UUID;

/**
 * Pattern: Immutable Object with Volatile Reference
 *
 * Example: Event keeper
 *
 * Java 16+ update: {@code Event} is now a {@code record}. The compact
 * constructor defensively copies the {@code payload} byte array so that the
 * record remains truly immutable — arrays are mutable in Java even inside
 * records.
 */
public class EventKeeper {

	private volatile Event lastEvent = new Event(null, null, null, null, new byte[0]);

	public void acceptEvent(String name, String type, String username, byte[] payload) {
		// Java 14+ switch expression: expression form, no fall-through, no break.
		lastEvent = switch (type) {
			case "STORAGE" -> new Event(UUID.randomUUID().toString(), name, type, username, payload);
			default        -> lastEvent;
		};
	}

	public Event getLastEvent() {
		return lastEvent;
	}

	/**
	 * Java 16+ Record.
	 *
	 * The compact constructor copies {@code payload} so external callers cannot
	 * mutate the stored array after construction, preserving true immutability.
	 */
	record Event(String id, String name, String type, String username, byte[] payload) {
		Event {
			payload = Arrays.copyOf(payload, payload.length);
		}
	}

}
