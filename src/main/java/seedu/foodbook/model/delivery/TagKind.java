package seedu.foodbook.model.delivery;

/**
 * Classification of delivery tags used for UI styling and simple logic.
 * <p>
 * Values:
 * <ul>
 *   <li>{@link #PERSONAL} — tag text equals (case-insensitive) "personal"</li>
 *   <li>{@link #CORPORATE} — tag text equals (case-insensitive) "corporate"</li>
 *   <li>{@link #OTHER} — any other tag or no tag</li>
 * </ul>
 */
public enum TagKind { PERSONAL, CORPORATE, OTHER }
