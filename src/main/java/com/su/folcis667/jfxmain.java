/*
 * Copyright (c) 2016, Samuel Savage
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.su.folcis667;

import com.su.folcis667.match3fx.StateViewController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author me
 */
public class jfxmain extends Application {

    @Override
    public void start(Stage primaryStage) {
//        content.getChildren().setAll();
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });

        FXMLLoader fxml_loader = new FXMLLoader(getClass().getResource("/match3fx/StateView.fxml"));
        GridPane state_view = null;
        try {
            state_view = (GridPane) fxml_loader.load();
//       Scene myScene = new Scene(myPane);
//       primaryStage.setScene(myScene);
//       primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(jfxmain.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        StateViewController state_view_controller = (StateViewController) fxml_loader.getController();

//        GridPane state_view = new GridPane();
//        state_view.getChildren().setAll(FXMLLoader.load("StateView.fxml"));
        StackPane root = new StackPane();
        root.getChildren().add(btn);

        Scene scene = new Scene(state_view, 800, 650);

        primaryStage.setTitle("Match 3 Puzzle Solver");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        Match3Game asdf = new Match3Game(5, 5, 4);
        state_view_controller.refresh(asdf.mCells);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
