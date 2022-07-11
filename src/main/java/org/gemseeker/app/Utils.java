package org.gemseeker.app;

import java.time.format.DateTimeFormatter;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 * Utils holds global variables and global methods/functions.
 * 
 * @author Gem
 */
public final class Utils {

    /**
     * If DEBUG is true, all logs will be displayed in the console and saved as
     * text file in the logs folder.
     */
    public static final boolean DEBUG = true;
    
    public static final String APP_NAME = "Office Services";
    public static final String APP_VERSION = "1.0-SNAPSHOT";
    public static final String DB_NAME = "officeservicesdb";
    
    public static final String APP_FOLDER = userHome() + fileSeparator() + "Office_Services";
    public static final String LOG_FOLDER = APP_FOLDER + fileSeparator() + "logs";
    public static final String DATA_FOLDER = APP_FOLDER + fileSeparator() + "data";
    public static final String IMAGES_FOLDER = APP_FOLDER + fileSeparator() + "images";
    public static final String SIGNATURES_FOLDER = APP_FOLDER + fileSeparator() + "signatures";
    public static final String TEMP_FOLDER = APP_FOLDER + fileSeparator() + "temp";
    
    public static final String DATABASE_PATH = DATA_FOLDER + fileSeparator() + DB_NAME;
    public static final String SETTINGS_FILE = APP_FOLDER + fileSeparator() + "settings.xml";
    
    /**
     * Date format MMMM dd, yyyy (ex: June 01, 2022)
     */
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
    
    /**
     * Date format MMM dd, yyyy (ex: Jun 01, 2022)
     */
    public static final DateTimeFormatter DATE_FORMAT2 = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    
    /**
     * Date format MM-dd-yyyy (ex: 06-01-2022)
     */
    public static final DateTimeFormatter DATE_FORMAT3 = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    
    public static void setAsNumericalTextField(TextField...textFields) {
        for (TextField tf : textFields) {
            tf.addEventFilter(KeyEvent.KEY_TYPED, evt -> {
                if (!"-0123456789.".contains(evt.getCharacter())) evt.consume();
                String text = tf.getText();
                String chr = evt.getCharacter();
                if ((chr.equals("-") || chr.equals(".")) && (text.contains("-") || text.contains("."))) {
                    evt.consume();
                }
            });
        }
    }

    public static void setAsIntegerTextField(TextField textField) {
        if (textField != null) {
            textField.addEventFilter(KeyEvent.KEY_TYPED, evt -> {
                if (!"0123456789".contains(evt.getCharacter())) evt.consume();
            });
        }
    }
    
    public static String fileSeparator() {
        return System.getProperty("file.separator");
    }
    
    public static String userHome() {
        return System.getProperty("user.home");
    }
}
