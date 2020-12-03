package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    /*
    * Объекты космоса (удалённость от земли)
        Планеты (радиус, наличие атмосферы, сила притяжения)
        Звезды (плотность, цвет, температура)
        Кометы (период прохождения через солнечную систему, название)
    * */

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("gui/MainForm.fxml"));
        primaryStage.setTitle("Объекты космоса");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
