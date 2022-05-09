package it.polimi.ingsw.client.gui;


import it.polimi.ingsw.client.View;
import it.polimi.ingsw.server.ConnectionMessage.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Gui extends Application implements View {


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/prova.fxml"));
            Scene scene= new Scene(root);
            primaryStage.setTitle("Eriantys");
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void askAction(AskActionMessage message) {

    }

    @Override
    public void displayInfo(InfoMessage message) {

    }

    @Override
    public void setupMultipleChoice(MultipleChoiceMessage message) {

    }

    @Override
    public void setupNickname(NicknameMessage message) {

    }

    @Override
    public void displayBoard(UpdateBoard message) {

    }

    @Override
    public void setupConnection() {

    }
}
