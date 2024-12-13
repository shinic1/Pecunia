package FinanceApplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//The main class of the Finance Application
 
public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Loads the Login.fxml file
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        
        // Sets the title for the primary stage 
        primaryStage.setTitle("Login - Finance Application");
        
        // Sets the scene with the loaded FXML content
        primaryStage.setScene(new Scene(root));
        
        // Displays the primary stage
        primaryStage.show();
    }
    public static void main(String[] args) {
        // Launches the JavaFX application
        launch(args);
    }
}
