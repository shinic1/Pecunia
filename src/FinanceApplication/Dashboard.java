package FinanceApplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//Main dashboard view
public class Dashboard extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Use explicit resource loading
        FXMLLoader loader = new FXMLLoader(Dashboard.class.getResource("Dashboard.fxml"));
        Parent root = loader.load();

        // Get the controller instance and set up the budget
        DashboardController controller = loader.getController();
        controller.setBudget(new Budget()); // Initialize a new budget

        primaryStage.setTitle("Budget Dashboard");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
