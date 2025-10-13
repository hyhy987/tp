package seedu.foodbook.model;

import java.nio.file.Path;

import seedu.foodbook.commons.core.GuiSettings;

/**
 * Unmodifiable view of user prefs.
 */
public interface ReadOnlyUserPrefs {

    GuiSettings getGuiSettings();

    Path getFoodBookFilePath();

}
