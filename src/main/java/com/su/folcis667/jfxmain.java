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

import com.su.folcis667.match3.Cell;
import com.su.folcis667.match3fx.StateViewController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author me
 */
public class jfxmain extends Application {

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader fxml_loader = new FXMLLoader(getClass().getResource("/match3fx/StateView.fxml"));
        GridPane state_view = null;
        try {
            state_view = (GridPane) fxml_loader.load();
        } catch (IOException ex) {
            Logger.getLogger(jfxmain.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        StateViewController state_view_controller = (StateViewController) fxml_loader.getController();

        ListView<String> swaps = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();
        swaps.setItems(items);

        GridPane main_view = new GridPane();
        main_view.getColumnConstraints().add(new ColumnConstraints(250));
        main_view.getRowConstraints().add(new RowConstraints(250));
        main_view.setHgap(5);
        main_view.setVgap(5);
//        main_view.setPadding(new Insets(10, 10, 10, 10));
        main_view.add(state_view, 0, 0);
        main_view.add(swaps, 1, 0);

        ScrollPane s1 = new ScrollPane();
        s1.setPrefSize(800, 650);
        s1.setContent(main_view);

        Scene scene = new Scene(s1, 800, 650);
        primaryStage.setTitle("Match 3 Puzzle Solver");
        primaryStage.setScene(scene);
        primaryStage.show();

        Match3Game asdf = new Match3Game(5, 5, 3);
        state_view_controller.refresh(asdf.mCells);

        ArrayList<Match3Game.MatchingPair> pairs = asdf.GetMatchableCells();
        int count = 0;
        for (Match3Game.MatchingPair pair : pairs) {
            String match = count++ + " match: " + pair.mLeft.get() + " and " + pair.mRight.get();
//            match += pair.mLeft.c() + " match: ";
//            match += "(" + pair.mLeft.x() + ", " + pair.mLeft.y() + ")";
//            match += " and (" + pair.mRight.x() + ", " + pair.mRight.y() + ")";
            items.add(match);
        }
        if (pairs.isEmpty()) {
            items.add("NO MATCHES POSSIBLE FOR THIS STATE!");
        }

        swaps.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Your action here
                System.out.println("Selected item: " + newValue);
                try {
                    int index = Integer.parseInt(newValue.split(" ")[0]);
                    Match3Game.MatchingPair pair = pairs.get(index);
                    Cell[][] cells = asdf.GetNextState(pair);
                    NewStateView(0, 1, cells, main_view);
                } catch (NumberFormatException ex) {
                    // do nothing.
                }
            }
        });
    }

    ListView<String> NewStateView(int row, int col, Cell[][] cells, GridPane mainView) {
        GridPane state_view = new GridPane();
        StateViewController.refresh(cells, state_view);

        ListView<String> swaps = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();
        swaps.setItems(items);

        mainView.getColumnConstraints().add(new ColumnConstraints(250));
        mainView.getRowConstraints().add(new RowConstraints(250));

        mainView.add(state_view, row, col);
        mainView.add(swaps, row + 1, col);

        Match3Game game = new Match3Game(cells);
        ArrayList<Match3Game.MatchingPair> pairs = game.GetMatchableCells();
        int count = 0;
        for (Match3Game.MatchingPair pair : pairs) {
            String match = count++ + " match: " + pair.mLeft.get() + " and " + pair.mRight.get();
            items.add(match);
        }
        if (pairs.isEmpty()) {
            items.add("NO MATCHES POSSIBLE FOR THIS STATE!");
        }

        swaps.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Your action here
                System.out.println("Selected item: " + newValue);
                try {
                    int index = Integer.parseInt(newValue.split(" ")[0]);
                    Match3Game.MatchingPair pair = pairs.get(index);
                    Cell[][] cells = Match3Game.RemoveMatches(game.GetNextState(pair));
                    NewStateView(row, col + 1, cells, mainView);
                } catch (NumberFormatException ex) {
                    // do nothing.
                }
            }
        });

        return swaps;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
