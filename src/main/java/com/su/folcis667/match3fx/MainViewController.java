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
package com.su.folcis667.match3fx;

import com.su.folcis667.Match3Game;
import com.su.folcis667.jfxmain;
import com.su.folcis667.match3.Cell;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;
import jfxtras.scene.control.ToggleGroupValue;

/**
 * FXML Controller class
 *
 * @author me
 */
public class MainViewController implements Initializable {

    @FXML
    private BorderPane MainPane;

    @FXML
    private GridPane StatePane;

    @FXML
    private GridPane InputPane;

    @FXML
    private TextField mGoalView;
    final SimpleIntegerProperty mGoalProperty = new SimpleIntegerProperty(10);

    @FXML
    private TextField mMoveLimitView;
    final SimpleIntegerProperty mMoveLimitProperty = new SimpleIntegerProperty(3);

    @FXML
    private TextField mCostView;
    SimpleDoubleProperty mCostProperty = new SimpleDoubleProperty(1.0);

    @FXML
    private Label mTotalMovesView;
    ReadOnlyStringWrapper mTotalMovesProperty = new ReadOnlyStringWrapper("0");

    @FXML
    private Label mTotalMatchedView;
    ReadOnlyStringWrapper mTotalMatchedProperty = new ReadOnlyStringWrapper("0");
    ArrayList<Integer> mMatchList = new ArrayList<>();

    @FXML
    private Label mTotalCostView;
    ReadOnlyStringWrapper mTotalCostProperty = new ReadOnlyStringWrapper("0");

    @FXML
    private ToggleGroup tglgrp;
    ReadOnlyObjectProperty<Toggle> mSearchType = null;

    @FXML
    private Label mResultDisplay;

    private static ExecutorService mThreatPool
            = Executors.newFixedThreadPool(3);

    public static void Shutdown() {
        mThreatPool.shutdown();
        try {
            mThreatPool.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        mThreatPool.shutdownNow();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mGoalView.textProperty().bindBidirectional(mGoalProperty, new NumberStringConverter());
        mMoveLimitView.textProperty().bindBidirectional(mMoveLimitProperty, new NumberStringConverter());
        mCostView.textProperty().bindBidirectional(mCostProperty, new NumberStringConverter());
        mTotalMovesView.textProperty().bind(mTotalMovesProperty);
        mTotalMatchedView.textProperty().bind(mTotalMatchedProperty);
        mTotalCostView.textProperty().bind(mTotalCostProperty);
        mSearchType = tglgrp.selectedToggleProperty();
        NewStateView(0, new Match3Game(10, 10, 3),
                new ArrayList<Integer>(), new ArrayList<>());
    }

    void Restart(ArrayList<ArrayList<Integer>> badPaths) {
        Shutdown();
        mThreatPool = Executors.newFixedThreadPool(3);
        NewStateView(0, new Match3Game(10, 10, 3),
                new ArrayList<Integer>(), badPaths);
    }

    @FXML
    protected void HandleRestart(ActionEvent event) {
        Restart(new ArrayList<>());
    }

    ListView<String> NewStateView(int row, Match3Game game,
            ArrayList<Integer> path, ArrayList<ArrayList<Integer>> badPaths) {
        mTotalMovesProperty.set(Integer.toString(row));
        mTotalCostProperty.set(Double.toString(row * mCostProperty.get()));

        int num_matches = game.RemoveMatches();
        if (mMatchList.size() > row) {
            mMatchList.subList(row, mMatchList.size()).clear();
        }
        mMatchList.add(num_matches);
        num_matches = 0;
        for (Integer m : mMatchList) {
            num_matches += m;
        }
        mTotalMatchedProperty.set(Integer.toString(num_matches));
        if ((!"Manual".equals(((RadioButton) mSearchType.get()).getText()))
                && (row * mCostProperty.get() >= mMoveLimitProperty.get())) {
            String msg = "Limit Reached: \n";
            for (Integer x : path) {
                msg += x.toString() + " -> ";
            }
            msg += "FAILED\n";
            final String java_is_dumb = msg;
            Platform.runLater(() -> {
                mResultDisplay.setText(mResultDisplay.getText() + java_is_dumb);
            });
            badPaths.add(path);
            Restart(badPaths);
            return null;
        }

        if (Integer.parseInt(mTotalMatchedProperty.get()) >= mGoalProperty.get()) {
            String msg = "!! GOAL ACHIEVED:\n";
            for (Integer x : path) {
                msg += x.toString() + " -> ";
            }
            msg += "GOAL!!!";
            final String java_is_dumb = msg;
            Platform.runLater(() -> {
                mResultDisplay.setText(mResultDisplay.getText()+java_is_dumb);
            });
            return null;
        }

        GridPane state_view = new GridPane();
        MainViewController.RefreshCells(game.mCells, state_view);

        ListView<String> swaps = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();
        swaps.setItems(items);

//        this.StatePane.getColumnConstraints().add(new ColumnConstraints(250));
        this.StatePane.getRowConstraints().add(new RowConstraints(250));

        for (Iterator<Node> i = this.StatePane.getChildren().iterator(); i.hasNext();) {
            Node node = i.next();
            if (GridPane.getRowIndex(node) >= row
                    && (node instanceof GridPane
                    || node instanceof ListView)) {
                i.remove();
            }
        }
        this.StatePane.add(state_view, 0, row);
        this.StatePane.add(swaps, 1, row);

        ArrayList<Match3Game.MatchingPair> pairs = game.GetMatchableCells();
        int count = 0;
        int best_match_index = -1;
        int best_match = -1;
        for (Match3Game.MatchingPair pair : pairs) {
            Match3Game next = new Match3Game(game.GetNextState(pair));
            int num_next_matches = next.RemoveMatches();
            String match = count++ + " match: " + pair.mLeft.get()
                    + " and " + pair.mRight.get() + " | " + num_next_matches;
            items.add(match);
            if (num_next_matches > best_match && !IsBadChoice(count - 1, path, badPaths)) {
                best_match = num_next_matches;
                best_match_index = count - 1;
            }

            if ("Manual".equals(((RadioButton) mSearchType.get()).getText())) {
                mThreatPool.submit(new FutureTask<>(() -> {
                    int num = next.GetMaxNumSuccessorMoves(0, 0);
                    int index = items.indexOf(match);
                    String s = items.get(index) + " + " + num;
                    Platform.runLater(() -> {
                        if ("Manual".equals(((RadioButton) mSearchType.get()).getText())) {
                            items.set(index, s);
                        }
                    });
                    return s;
                }));
            }
        }
        RadioButton button = (RadioButton) mSearchType.get();
        if (pairs.isEmpty() || best_match == -1) {
            items.add("NO MATCHES POSSIBLE FOR THIS STATE!");
        } else if ("Depth One".equals(button.getText())) {
            final int x = best_match_index;
            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    swaps.scrollTo(x);
                    swaps.getSelectionModel().select(x);
                    path.add(x);
                    Match3Game.MatchingPair pair = pairs.get(x);
                    Cell[][] cells = game.GetNextState(pair);
                    Match3Game next_game = new Match3Game(cells);
                    NewStateView(row + 1, next_game, path, badPaths);
                }
            });
        } else if ("Depth Two".equals(button.getText())) {
            // do the other search
        } else {
            swaps.getSelectionModel().selectedItemProperty().addListener(
                    new ChangeListener<String>() {

                @Override
                public void changed(ObservableValue<? extends String> observable,
                        String oldValue, String newValue) {
                    try {
                        int index = Integer.parseInt(newValue.split(" ")[0]);
                        Match3Game.MatchingPair pair = pairs.get(index);
                        Cell[][] cells = game.GetNextState(pair);
                        Match3Game next_game = new Match3Game(cells);
                        NewStateView(row + 1, next_game, path, badPaths);
                    } catch (NumberFormatException ex) {
                        // do nothing.
                    }
                }
            });
        }
        return swaps;
    }

    boolean IsBadChoice(int next, ArrayList<Integer> path, ArrayList<ArrayList<Integer>> badPaths) {
        for (ArrayList<Integer> p : badPaths) {
            if (PathMatch(p, path) && p.size() > path.size()
                    && p.get(path.size()) == next) {
                return true;
            }
        }
        return false;
    }

    boolean PathMatch(ArrayList<Integer> p1, ArrayList<Integer> p2) {
        for (int i = 0; i < p1.size() && i < p2.size(); ++i) {
            if (!Objects.equals(p1.get(i), p2.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static void RefreshCells(Cell[][] cells, GridPane view) {
        view.getChildren().clear();
        int size = 250 / cells.length;
        for (int i = 0; i < cells.length; ++i) {
            for (int j = 0; j < cells[i].length; ++j) {
                Cell c = cells[i][j];
                Rectangle c_rec = new Rectangle(size, size);
                c_rec.setFill(c.color());
                view.add(c_rec, c.x(), c.y());
            }
        }
    }

}
