import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class strip extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("cmptool.fxml"));
        primaryStage.setTitle("Tool");
        primaryStage.setScene(new Scene(root, 800, 450));
        primaryStage.setFullScreen(false);
        primaryStage.show();
    }

    public static void main(String args[]) throws IOException
    {
        launch(args);
    }

}