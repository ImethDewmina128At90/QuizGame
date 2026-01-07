import db.JDBC;
import screens.TitleScreenGui;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // Initialize database tables
        JDBC.initializeDatabase();

        //ensures Swing GUI tasks are executed on the event dispatch thread (EDT)
        SwingUtilities.invokeLater(() -> {
            //create and display the title screen GUI window
            new TitleScreenGui().setVisible(true);
        });
    }
}
