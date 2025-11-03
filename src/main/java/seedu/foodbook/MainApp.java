package seedu.foodbook;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import seedu.foodbook.commons.core.Config;
import seedu.foodbook.commons.core.LogsCenter;
import seedu.foodbook.commons.core.Version;
import seedu.foodbook.commons.exceptions.DataLoadingException;
import seedu.foodbook.commons.util.ConfigUtil;
import seedu.foodbook.commons.util.StringUtil;
import seedu.foodbook.logic.Logic;
import seedu.foodbook.logic.LogicManager;
import seedu.foodbook.model.FoodBook;
import seedu.foodbook.model.Model;
import seedu.foodbook.model.ModelManager;
import seedu.foodbook.model.ReadOnlyFoodBook;
import seedu.foodbook.model.ReadOnlyUserPrefs;
import seedu.foodbook.model.UserPrefs;
import seedu.foodbook.model.util.SampleDataUtil;
import seedu.foodbook.storage.FoodBookStorage;
import seedu.foodbook.storage.JsonFoodBookStorage;
import seedu.foodbook.storage.JsonUserPrefsStorage;
import seedu.foodbook.storage.Storage;
import seedu.foodbook.storage.StorageManager;
import seedu.foodbook.storage.UserPrefsStorage;
import seedu.foodbook.ui.Ui;
import seedu.foodbook.ui.UiManager;

/**
 * Runs the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(1, 5, 0, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing FoodBook ]===========================");
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());
        initLogging(config);

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        FoodBookStorage foodBookStorage = new JsonFoodBookStorage(userPrefs.getFoodBookFilePath());
        storage = new StorageManager(foodBookStorage, userPrefsStorage);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model, storage);

        ui = new UiManager(logic);
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s food book and {@code userPrefs}. <br>
     * The data from the sample food book will be used instead if {@code storage}'s food book is not found,
     * or an empty food book will be used instead if errors occur when reading {@code storage}'s food book.
     */
    private Model initModelManager(Storage storage, ReadOnlyUserPrefs userPrefs) {
        logger.info("Using data file : " + storage.getFoodBookFilePath());

        Optional<ReadOnlyFoodBook> foodBookOptional;
        ReadOnlyFoodBook initialData;
        try {
            foodBookOptional = storage.readFoodBook();
            if (!foodBookOptional.isPresent()) {
                logger.info("Creating a new data file " + storage.getFoodBookFilePath()
                        + " populated with a sample FoodBook.");
            }
            initialData = foodBookOptional.orElseGet(SampleDataUtil::getSampleFoodBook);
        } catch (DataLoadingException e) {
            logger.warning("Data file at " + storage.getFoodBookFilePath() + " could not be loaded."
                    + " Will be starting with an empty FoodBook.");
            initialData = new FoodBook();
        }

        return new ModelManager(initialData, userPrefs);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            if (!configOptional.isPresent()) {
                logger.info("Creating new config file " + configFilePathUsed);
            }
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataLoadingException e) {
            logger.warning("Config file at " + configFilePathUsed + " could not be loaded."
                    + " Using default config properties.");
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using preference file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            if (!prefsOptional.isPresent()) {
                logger.info("Creating new preference file " + prefsFilePath);
            }
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataLoadingException e) {
            logger.warning("Preference file at " + prefsFilePath + " could not be loaded."
                    + " Using default preferences.");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting FoodBook " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping FoodBook ] =============================");
        try {
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
    }
}
