package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class GameFinishedTests {
    private OXOModel model;
    private OXOController controller;
    @BeforeEach
    void Setup() {
        // This game is set up with a win threshold of 1 so that any move results in the game being won.
        // therefore, we can quickly get it into a winning state and test that it doesn't allow further input
        model = new OXOModel(8, 8, 3);
        model.addPlayer(new OXOPlayer('X'));
        model.addPlayer(new OXOPlayer('O'));
        controller = new OXOController(model);
    }

    // This next method is a utility function that can be used by any of the test methods to _safely_ send a command to the controller
    void sendCommandToController(String command) {
        // Try to send a command to the server - call will timeout if it takes too long (in case the server enters an infinite loop)
        // Note: this is ugly code and includes syntax that you haven't encountered yet
        String timeoutComment = "Controller took too long to respond (probably stuck in an infinite loop)";
        assertTimeoutPreemptively(Duration.ofMillis(1000), ()-> controller.handleIncomingCommand(command), timeoutComment);
    }

    // Should be able to change board size after a game is won (so long as there are no moves in the row/column
    // to be removed)
    @DisplayName("Testing that controller.removeRow() does reduce the number of rows after the game is won")
    @ParameterizedTest(name = "{displayName}.  winning line is {0}->{2}->{4}")
    @CsvSource({
            "a1, b1, a2, b2, a3", "a2, b1, a3, b2, a4", "a3, b1, a4, b2, a5", "a4, b1, a5, b2, a6", "a5, b1, a6, b2, a7",
            "a1, b1, b2, c2, c3", "a2, b1, b3, b2, c4", "a3, b1, b4, b2, c5", "a4, b1, b5, b2, c6", "a5, b1, b6, b2, c7",
            "a1, b2, b1, c2, c1", "a2, b1, b2, b3, c2", "a3, b1, b3, b2, c3", "a4, b1, b4, b2, c4", "a5, b1, b5, b2, c5", "a6, b1, b6, b2, c6", "a7, b1, b7, b2, c7",
            "a3, b1, b2, c2, c1", "a4, b1, b3, b2, c2", "a5, b1, b4, b2, c3", "a6, b1, b5, b2, c4", "a7, b1, b6, b2, c5",
            "a3, b1, a2, c2, a1", "a4, b1, a3, b2, a2", "a5, b1, a4, b2, a3", "a6, b1, a5, b2, a4", "a7, b1, a6, b2, a5",

            "b1, a1, b2, a2, b3", "b2, a1, b3, a2, b4", "b3, a1, b4, a2, b5", "b4, a1, b5, a2, b6", "b5, a1, b6, a2, b7",
            "b1, a1, c2, a2, d3", "b2, a1, c3, a2, d4", "b3, a1, c4, a2, d5", "b4, a1, c5, a2, d6", "b5, a1, c6, a2, d7",
            "b1, a1, c1, a2, d1", "b2, a1, c2, a3, d2", "b3, a1, c3, a2, d3", "b4, a1, c4, a2, d4", "b5, a1, c5, a2, d5", "b6, a1, c6, a2, d6", "b7, a1, c7, a2, d7",
            "b3, a1, c2, a2, d1", "b4, a1, c3, a2, d2", "b5, a1, c4, a2, d3", "b6, a1, c5, a2, d4", "b7, a1, c6, a2, d5",
            "b3, a1, b2, a2, b1", "b4, a1, b3, a2, b2", "b5, a1, b4, a2, b3", "b6, a1, b5, a2, b4", "b7, a1, b6, a2, b5",

            "c1, a1, c2, a2, c3", "c2, a1, c3, a2, c4", "c3, a1, c4, a2, c5", "c4, a1, c5, a2, c6", "c5, a1, c6, a2, c7",
            "c1, a1, d2, a2, e3", "c2, a1, d3, a2, e4", "c3, a1, d4, a2, e5", "c4, a1, d5, a2, e6", "c5, a1, d6, a2, e7",
            "c1, a1, d1, a2, e1", "c2, a1, d2, a3, e2", "c3, a1, d3, a2, e3", "c4, a1, d4, a2, e4", "c5, a1, d5, a2, e5", "c6, a1, d6, a2, e6", "c7, a1, d7, a2, e7",
            "c3, a1, d2, a2, e1", "c4, a1, d3, a2, e2", "c5, a1, d4, a2, e3", "c6, a1, d5, a2, e4", "c7, a1, d6, a2, e5",
            "c3, a1, c2, a2, c1", "c4, a1, c3, a2, c2", "c5, a1, c4, a2, c3", "c6, a1, c5, a2, c4", "c7, a1, c6, a2, c5",
            "c1, a3, b1, a2, a1", "c2, a1, b2, a3, a2", "c3, a1, b3, a2, a3", "c4, a1, b4, a2, a4", "c5, a1, b5, a2, a5", "c6, a1, b6, a2, a6", "c7, a1, b7, a2, a7",
            "c3, a3, b2, a2, a1", "c4, a1, b3, a3, a2", "c5, a1, b4, a2, a3", "c6, a1, b5, a2, a4", "c7, a1, b6, a2, a5",
            "c3, a3, b2, a2, a1", "c4, a1, b3, a3, a2", "c5, a1, b4, a2, a3", "c6, a1, b5, a2, a4", "c7, a1, b6, a2, a5",

            "d1, a1, d2, a2, d3", "d2, a1, d3, a2, d4", "d3, a1, d4, a2, d5", "d4, a1, d5, a2, d6", "d5, a1, d6, a2, d7",
            "d1, a1, e2, a2, f3", "d2, a1, e3, a2, f4", "d3, a1, e4, a2, f5", "d4, a1, e5, a2, f6", "d5, a1, e6, a2, f7",
            "d1, a1, e1, a2, f1", "d2, a1, e2, a3, f2", "d3, a1, e3, a2, f3", "d4, a1, e4, a2, f4", "d5, a1, e5, a2, f5", "d6, a1, e6, a2, f6", "d7, a1, e7, a2, f7",
            "d3, a1, e2, a2, f1", "d4, a1, e3, a2, f2", "d5, a1, e4, a2, f3", "d6, a1, e5, a2, f4", "d7, a1, e6, a2, f5",
            "d3, a1, d2, a2, d1", "d4, a1, d3, a2, d2", "d5, a1, d4, a2, d3", "d6, a1, d5, a2, d4", "d7, a1, d6, a2, d5",
            "d1, a3, c1, a2, b1", "d2, a1, c2, a3, b2", "d3, a1, c3, a2, b3", "d4, a1, c4, a2, b4", "d5, a1, c5, a2, b5", "d6, a1, c6, a2, b6", "d7, a1, c7, a2, b7",
            "d3, a3, c2, a2, b1", "d4, a1, c3, a3, b2", "d5, a1, c4, a2, b3", "d6, a1, c5, a2, b4", "d7, a1, c6, a2, b5",
            "d3, a3, c2, a2, b1", "d4, a1, c3, a3, b2", "d5, a1, c4, a2, b3", "d6, a1, c5, a2, b4", "d7, a1, c6, a2, b5",

            "e1, a1, e2, a2, e3", "e2, a1, e3, a2, e4", "e3, a1, e4, a2, e5", "e4, a1, e5, a2, e6", "e5, a1, e6, a2, e7",
            "e1, a1, f2, a2, g3", "e2, a1, f3, a2, g4", "e3, a1, f4, a2, g5", "e4, a1, f5, a2, g6", "e5, a1, f6, a2, g7",
            "e1, a1, f1, a2, g1", "e2, a1, f2, a3, g2", "e3, a1, f3, a2, g3", "e4, a1, f4, a2, g4", "e5, a1, f5, a2, g5", "e6, a1, f6, a2, g6", "e7, a1, f7, a2, g7",
            "e3, a1, f2, a2, g1", "e4, a1, f3, a2, g2", "e5, a1, f4, a2, g3", "e6, a1, f5, a2, g4", "e7, a1, f6, a2, g5",
            "e3, a1, e2, a2, e1", "e4, a1, e3, a2, e2", "e5, a1, e4, a2, e3", "e6, a1, e5, a2, e4", "e7, a1, e6, a2, e5",
            "e1, a3, d1, a2, c1", "e2, a1, d2, a3, c2", "e3, a1, d3, a2, c3", "e4, a1, d4, a2, c4", "e5, a1, d5, a2, c5", "e6, a1, d6, a2, c6", "e7, a1, d7, a2, c7",
            "e3, a3, d2, a2, c1", "e4, a1, d3, a3, c2", "e5, a1, d4, a2, c3", "e6, a1, d5, a2, c4", "e7, a1, d6, a2, c5",
            "e3, a3, d2, a2, c1", "e4, a1, d3, a3, c2", "e5, a1, d4, a2, c3", "e6, a1, d5, a2, c4", "e7, a1, d6, a2, c5",

            "f1, a1, f2, a2, f3", "f2, a1, f3, a2, f4", "f3, a1, f4, a2, f5", "f4, a1, f5, a2, f6", "f5, a1, f6, a2, f7",
            "f3, a1, f2, a2, f1", "f4, a1, f3, a2, f2", "f5, a1, f4, a2, f3", "f6, a1, f5, a2, f4", "f7, a1, f6, a2, f5",
            "f1, a3, e1, a2, d1", "f2, a1, e2, a3, d2", "f3, a1, e3, a2, d3", "f4, a1, e4, a2, d4", "f5, a1, e5, a2, d5", "f6, a1, e6, a2, d6", "f7, a1, e7, a2, d7",
            "f3, a3, e2, a2, d1", "f4, a1, e3, a3, d2", "f5, a1, e4, a2, d3", "f6, a1, e5, a2, d4", "f7, a1, e6, a2, d5",
            "f3, a3, e2, a2, d1", "f4, a1, e3, a3, d2", "f5, a1, e4, a2, d3", "f6, a1, e5, a2, d4", "f7, a1, e6, a2, d5",

            "g1, a1, g2, a2, g3", "g2, a1, g3, a2, g4", "g3, a1, g4, a2, g5", "g4, a1, g5, a2, g6", "g5, a1, g6, a2, g7",
            "g3, a1, g2, a2, g1", "g4, a1, g3, a2, g2", "g5, a1, g4, a2, g3", "g6, a1, g5, a2, g4", "g7, a1, g6, a2, g5",
            "g1, a3, f1, a2, e1", "g2, a1, f2, a3, e2", "g3, a1, f3, a2, e3", "g4, a1, f4, a2, e4", "g5, a1, f5, a2, e5", "g6, a1, f6, a2, e6", "g7, a1, f7, a2, e7",
            "g3, a3, f2, a2, e1", "g4, a1, f3, a3, e2", "g5, a1, f4, a2, e3", "g6, a1, f5, a2, e4", "g7, a1, f6, a2, e5",
            "g3, a3, f2, a2, e1", "g4, a1, f3, a3, e2", "g5, a1, f4, a2, e3", "g6, a1, f5, a2, e4", "g7, a1, f6, a2, e5",
    })
    void attemptRowDecrease(ArgumentsAccessor moves) {
        this.sendCommandToController(moves.getString(0));
        this.sendCommandToController(moves.getString(1));
        this.sendCommandToController(moves.getString(2));
        this.sendCommandToController(moves.getString(3));
        this.sendCommandToController(moves.getString(4));
        String invalidSetupMessage = "playing a move in a winthreshold=1 game does not result in the game being won";
        assertNotNull(model.getWinner(), invalidSetupMessage);
        int start = model.getNumberOfRows();
        controller.removeRow();
        int current = model.getNumberOfRows();
        String failureMessage = "controller.removeRow() did not remove a row after the game was won";
        assertEquals(start - 1, current, failureMessage);
    }

    @DisplayName("Testing that controller.removeColumn() does reduce the number of columns after the game is won")
    @ParameterizedTest(name = "{displayName}.  winning line is {0}->{2}->{4}")
    @CsvSource({
            "a1, b1, a2, b2, a3", "a2, b1, a3, b2, a4", "a3, b1, a4, b2, a5", "a4, b1, a5, b2, a6", "a5, b1, a6, b2, a7",
            "a1, b1, b2, c2, c3", "a2, b1, b3, b2, c4", "a3, b1, b4, b2, c5", "a4, b1, b5, b2, c6", "a5, b1, b6, b2, c7",
            "a1, b2, b1, c2, c1", "a2, b1, b2, b3, c2", "a3, b1, b3, b2, c3", "a4, b1, b4, b2, c4", "a5, b1, b5, b2, c5", "a6, b1, b6, b2, c6", "a7, b1, b7, b2, c7",
            "a3, b1, b2, c2, c1", "a4, b1, b3, b2, c2", "a5, b1, b4, b2, c3", "a6, b1, b5, b2, c4", "a7, b1, b6, b2, c5",
            "a3, b1, a2, c2, a1", "a4, b1, a3, b2, a2", "a5, b1, a4, b2, a3", "a6, b1, a5, b2, a4", "a7, b1, a6, b2, a5",

            "b1, a1, b2, a2, b3", "b2, a1, b3, a2, b4", "b3, a1, b4, a2, b5", "b4, a1, b5, a2, b6", "b5, a1, b6, a2, b7",
            "b1, a1, c2, a2, d3", "b2, a1, c3, a2, d4", "b3, a1, c4, a2, d5", "b4, a1, c5, a2, d6", "b5, a1, c6, a2, d7",
            "b1, a1, c1, a2, d1", "b2, a1, c2, a3, d2", "b3, a1, c3, a2, d3", "b4, a1, c4, a2, d4", "b5, a1, c5, a2, d5", "b6, a1, c6, a2, d6", "b7, a1, c7, a2, d7",
            "b3, a1, c2, a2, d1", "b4, a1, c3, a2, d2", "b5, a1, c4, a2, d3", "b6, a1, c5, a2, d4", "b7, a1, c6, a2, d5",
            "b3, a1, b2, a2, b1", "b4, a1, b3, a2, b2", "b5, a1, b4, a2, b3", "b6, a1, b5, a2, b4", "b7, a1, b6, a2, b5",

            "c1, a1, c2, a2, c3", "c2, a1, c3, a2, c4", "c3, a1, c4, a2, c5", "c4, a1, c5, a2, c6", "c5, a1, c6, a2, c7",
            "c1, a1, d2, a2, e3", "c2, a1, d3, a2, e4", "c3, a1, d4, a2, e5", "c4, a1, d5, a2, e6", "c5, a1, d6, a2, e7",
            "c1, a1, d1, a2, e1", "c2, a1, d2, a3, e2", "c3, a1, d3, a2, e3", "c4, a1, d4, a2, e4", "c5, a1, d5, a2, e5", "c6, a1, d6, a2, e6", "c7, a1, d7, a2, e7",
            "c3, a1, d2, a2, e1", "c4, a1, d3, a2, e2", "c5, a1, d4, a2, e3", "c6, a1, d5, a2, e4", "c7, a1, d6, a2, e5",
            "c3, a1, c2, a2, c1", "c4, a1, c3, a2, c2", "c5, a1, c4, a2, c3", "c6, a1, c5, a2, c4", "c7, a1, c6, a2, c5",
            "c1, a3, b1, a2, a1", "c2, a1, b2, a3, a2", "c3, a1, b3, a2, a3", "c4, a1, b4, a2, a4", "c5, a1, b5, a2, a5", "c6, a1, b6, a2, a6", "c7, a1, b7, a2, a7",
            "c3, a3, b2, a2, a1", "c4, a1, b3, a3, a2", "c5, a1, b4, a2, a3", "c6, a1, b5, a2, a4", "c7, a1, b6, a2, a5",
            "c3, a3, b2, a2, a1", "c4, a1, b3, a3, a2", "c5, a1, b4, a2, a3", "c6, a1, b5, a2, a4", "c7, a1, b6, a2, a5",

            "d1, a1, d2, a2, d3", "d2, a1, d3, a2, d4", "d3, a1, d4, a2, d5", "d4, a1, d5, a2, d6", "d5, a1, d6, a2, d7",
            "d1, a1, e2, a2, f3", "d2, a1, e3, a2, f4", "d3, a1, e4, a2, f5", "d4, a1, e5, a2, f6", "d5, a1, e6, a2, f7",
            "d1, a1, e1, a2, f1", "d2, a1, e2, a3, f2", "d3, a1, e3, a2, f3", "d4, a1, e4, a2, f4", "d5, a1, e5, a2, f5", "d6, a1, e6, a2, f6", "d7, a1, e7, a2, f7",
            "d3, a1, e2, a2, f1", "d4, a1, e3, a2, f2", "d5, a1, e4, a2, f3", "d6, a1, e5, a2, f4", "d7, a1, e6, a2, f5",
            "d3, a1, d2, a2, d1", "d4, a1, d3, a2, d2", "d5, a1, d4, a2, d3", "d6, a1, d5, a2, d4", "d7, a1, d6, a2, d5",
            "d1, a3, c1, a2, b1", "d2, a1, c2, a3, b2", "d3, a1, c3, a2, b3", "d4, a1, c4, a2, b4", "d5, a1, c5, a2, b5", "d6, a1, c6, a2, b6", "d7, a1, c7, a2, b7",
            "d3, a3, c2, a2, b1", "d4, a1, c3, a3, b2", "d5, a1, c4, a2, b3", "d6, a1, c5, a2, b4", "d7, a1, c6, a2, b5",
            "d3, a3, c2, a2, b1", "d4, a1, c3, a3, b2", "d5, a1, c4, a2, b3", "d6, a1, c5, a2, b4", "d7, a1, c6, a2, b5",

            "e1, a1, e2, a2, e3", "e2, a1, e3, a2, e4", "e3, a1, e4, a2, e5", "e4, a1, e5, a2, e6", "e5, a1, e6, a2, e7",
            "e1, a1, f2, a2, g3", "e2, a1, f3, a2, g4", "e3, a1, f4, a2, g5", "e4, a1, f5, a2, g6", "e5, a1, f6, a2, g7",
            "e1, a1, f1, a2, g1", "e2, a1, f2, a3, g2", "e3, a1, f3, a2, g3", "e4, a1, f4, a2, g4", "e5, a1, f5, a2, g5", "e6, a1, f6, a2, g6", "e7, a1, f7, a2, g7",
            "e3, a1, f2, a2, g1", "e4, a1, f3, a2, g2", "e5, a1, f4, a2, g3", "e6, a1, f5, a2, g4", "e7, a1, f6, a2, g5",
            "e3, a1, e2, a2, e1", "e4, a1, e3, a2, e2", "e5, a1, e4, a2, e3", "e6, a1, e5, a2, e4", "e7, a1, e6, a2, e5",
            "e1, a3, d1, a2, c1", "e2, a1, d2, a3, c2", "e3, a1, d3, a2, c3", "e4, a1, d4, a2, c4", "e5, a1, d5, a2, c5", "e6, a1, d6, a2, c6", "e7, a1, d7, a2, c7",
            "e3, a3, d2, a2, c1", "e4, a1, d3, a3, c2", "e5, a1, d4, a2, c3", "e6, a1, d5, a2, c4", "e7, a1, d6, a2, c5",
            "e3, a3, d2, a2, c1", "e4, a1, d3, a3, c2", "e5, a1, d4, a2, c3", "e6, a1, d5, a2, c4", "e7, a1, d6, a2, c5",

            "f1, a1, f2, a2, f3", "f2, a1, f3, a2, f4", "f3, a1, f4, a2, f5", "f4, a1, f5, a2, f6", "f5, a1, f6, a2, f7",
            "f3, a1, f2, a2, f1", "f4, a1, f3, a2, f2", "f5, a1, f4, a2, f3", "f6, a1, f5, a2, f4", "f7, a1, f6, a2, f5",
            "f1, a3, e1, a2, d1", "f2, a1, e2, a3, d2", "f3, a1, e3, a2, d3", "f4, a1, e4, a2, d4", "f5, a1, e5, a2, d5", "f6, a1, e6, a2, d6", "f7, a1, e7, a2, d7",
            "f3, a3, e2, a2, d1", "f4, a1, e3, a3, d2", "f5, a1, e4, a2, d3", "f6, a1, e5, a2, d4", "f7, a1, e6, a2, d5",
            "f3, a3, e2, a2, d1", "f4, a1, e3, a3, d2", "f5, a1, e4, a2, d3", "f6, a1, e5, a2, d4", "f7, a1, e6, a2, d5",

            "g1, a1, g2, a2, g3", "g2, a1, g3, a2, g4", "g3, a1, g4, a2, g5", "g4, a1, g5, a2, g6", "g5, a1, g6, a2, g7",
            "g3, a1, g2, a2, g1", "g4, a1, g3, a2, g2", "g5, a1, g4, a2, g3", "g6, a1, g5, a2, g4", "g7, a1, g6, a2, g5",
            "g1, a3, f1, a2, e1", "g2, a1, f2, a3, e2", "g3, a1, f3, a2, e3", "g4, a1, f4, a2, e4", "g5, a1, f5, a2, e5", "g6, a1, f6, a2, e6", "g7, a1, f7, a2, e7",
            "g3, a3, f2, a2, e1", "g4, a1, f3, a3, e2", "g5, a1, f4, a2, e3", "g6, a1, f5, a2, e4", "g7, a1, f6, a2, e5",
            "g3, a3, f2, a2, e1", "g4, a1, f3, a3, e2", "g5, a1, f4, a2, e3", "g6, a1, f5, a2, e4", "g7, a1, f6, a2, e5",
    })
    void attemptColDecrease(ArgumentsAccessor moves) {
        this.sendCommandToController(moves.getString(0));
        this.sendCommandToController(moves.getString(1));
        this.sendCommandToController(moves.getString(2));
        this.sendCommandToController(moves.getString(3));
        this.sendCommandToController(moves.getString(4));
        String invalidSetupMessage = "playing a move in a winthreshold=1 game does not result in the game being won";
        assertNotNull(model.getWinner(), invalidSetupMessage);
        int start = model.getNumberOfColumns();
        controller.removeColumn();
        int current = model.getNumberOfColumns();
        String failureMessage = "controller.removeColumn() did not remove a column after the game was won";
        assertEquals(start - 1, current, failureMessage);
    }

    @DisplayName("Testing that controller.addRow() does increase the number of rows after the game is won")
    @ParameterizedTest(name = "{displayName}.  winning line is {0}->{2}->{4}")
    @CsvSource({
            "a1, b1, a2, b2, a3", "a2, b1, a3, b2, a4", "a3, b1, a4, b2, a5", "a4, b1, a5, b2, a6", "a5, b1, a6, b2, a7", "a6, b1, a7, b2, a8",
            "a1, b1, b2, c2, c3", "a2, b1, b3, b2, c4", "a3, b1, b4, b2, c5", "a4, b1, b5, b2, c6", "a5, b1, b6, b2, c7", "a6, b1, b7, b2, c8",
            "a1, b2, b1, c2, c1", "a2, b1, b2, b3, c2", "a3, b1, b3, b2, c3", "a4, b1, b4, b2, c4", "a5, b1, b5, b2, c5", "a6, b1, b6, b2, c6", "a7, b1, b7, b2, c7", "a8, b1, b8, b2, c8",
            "a3, b1, b2, c2, c1", "a4, b1, b3, b2, c2", "a5, b1, b4, b2, c3", "a6, b1, b5, b2, c4", "a7, b1, b6, b2, c5", "a8, b1, b7, b2, c6",
            "a3, b1, a2, c2, a1", "a4, b1, a3, b2, a2", "a5, b1, a4, b2, a3", "a6, b1, a5, b2, a4", "a7, b1, a6, b2, a5", "a8, b1, a7, b2, a6",

            "b1, a1, b2, a2, b3", "b2, a1, b3, a2, b4", "b3, a1, b4, a2, b5", "b4, a1, b5, a2, b6", "b5, a1, b6, a2, b7", "b6, a1, b7, a2, b8",
            "b1, a1, c2, a2, d3", "b2, a1, c3, a2, d4", "b3, a1, c4, a2, d5", "b4, a1, c5, a2, d6", "b5, a1, c6, a2, d7", "b6, a1, c7, a2, d8",
            "b1, a1, c1, a2, d1", "b2, a1, c2, a3, d2", "b3, a1, c3, a2, d3", "b4, a1, c4, a2, d4", "b5, a1, c5, a2, d5", "b6, a1, c6, a2, d6", "b7, a1, c7, a2, d7", "b8, a1, c8, a2, d8",
            "b3, a1, c2, a2, d1", "b4, a1, c3, a2, d2", "b5, a1, c4, a2, d3", "b6, a1, c5, a2, d4", "b7, a1, c6, a2, d5", "b8, a1, c7, a2, d6",
            "b3, a1, b2, a2, b1", "b4, a1, b3, a2, b2", "b5, a1, b4, a2, b3", "b6, a1, b5, a2, b4", "b7, a1, b6, a2, b5", "b8, a1, b7, a2, b6",

            "c1, a1, c2, a2, c3", "c2, a1, c3, a2, c4", "c3, a1, c4, a2, c5", "c4, a1, c5, a2, c6", "c5, a1, c6, a2, c7", "c6, a1, c7, a2, c8",
            "c1, a1, d2, a2, e3", "c2, a1, d3, a2, e4", "c3, a1, d4, a2, e5", "c4, a1, d5, a2, e6", "c5, a1, d6, a2, e7", "c6, a1, d7, a2, e8",
            "c1, a1, d1, a2, e1", "c2, a1, d2, a3, e2", "c3, a1, d3, a2, e3", "c4, a1, d4, a2, e4", "c5, a1, d5, a2, e5", "c6, a1, d6, a2, e6", "c7, a1, d7, a2, e7", "c8, a1, d8, a2, e8",
            "c3, a1, d2, a2, e1", "c4, a1, d3, a2, e2", "c5, a1, d4, a2, e3", "c6, a1, d5, a2, e4", "c7, a1, d6, a2, e5", "c8, a1, d7, a2, e6",
            "c3, a1, c2, a2, c1", "c4, a1, c3, a2, c2", "c5, a1, c4, a2, c3", "c6, a1, c5, a2, c4", "c7, a1, c6, a2, c5", "c8, a1, c7, a2, c6",
            "c1, a3, b1, a2, a1", "c2, a1, b2, a3, a2", "c3, a1, b3, a2, a3", "c4, a1, b4, a2, a4", "c5, a1, b5, a2, a5", "c6, a1, b6, a2, a6", "c7, a1, b7, a2, a7", "c8, a1, b8, a2, a8",
            "c3, a3, b2, a2, a1", "c4, a1, b3, a3, a2", "c5, a1, b4, a2, a3", "c6, a1, b5, a2, a4", "c7, a1, b6, a2, a5", "c8, a1, b7, a2, a6",
            "c3, a3, b2, a2, a1", "c4, a1, b3, a3, a2", "c5, a1, b4, a2, a3", "c6, a1, b5, a2, a4", "c7, a1, b6, a2, a5", "c8, a1, b7, a2, a6",

            "d1, a1, d2, a2, d3", "d2, a1, d3, a2, d4", "d3, a1, d4, a2, d5", "d4, a1, d5, a2, d6", "d5, a1, d6, a2, d7", "d6, a1, d7, a2, d8",
            "d1, a1, e2, a2, f3", "d2, a1, e3, a2, f4", "d3, a1, e4, a2, f5", "d4, a1, e5, a2, f6", "d5, a1, e6, a2, f7", "d6, a1, e7, a2, f8",
            "d1, a1, e1, a2, f1", "d2, a1, e2, a3, f2", "d3, a1, e3, a2, f3", "d4, a1, e4, a2, f4", "d5, a1, e5, a2, f5", "d6, a1, e6, a2, f6", "d7, a1, e7, a2, f7", "d8, a1, e8, a2, f8",
            "d3, a1, e2, a2, f1", "d4, a1, e3, a2, f2", "d5, a1, e4, a2, f3", "d6, a1, e5, a2, f4", "d7, a1, e6, a2, f5", "d8, a1, e7, a2, f6",
            "d3, a1, d2, a2, d1", "d4, a1, d3, a2, d2", "d5, a1, d4, a2, d3", "d6, a1, d5, a2, d4", "d7, a1, d6, a2, d5", "d8, a1, d7, a2, d6",
            "d1, a3, c1, a2, b1", "d2, a1, c2, a3, b2", "d3, a1, c3, a2, b3", "d4, a1, c4, a2, b4", "d5, a1, c5, a2, b5", "d6, a1, c6, a2, b6", "d7, a1, c7, a2, b7", "d8, a1, c8, a2, b8",
            "d3, a3, c2, a2, b1", "d4, a1, c3, a3, b2", "d5, a1, c4, a2, b3", "d6, a1, c5, a2, b4", "d7, a1, c6, a2, b5", "d8, a1, c7, a2, b6",
            "d3, a3, c2, a2, b1", "d4, a1, c3, a3, b2", "d5, a1, c4, a2, b3", "d6, a1, c5, a2, b4", "d7, a1, c6, a2, b5", "d8, a1, c7, a2, b6",

            "e1, a1, e2, a2, e3", "e2, a1, e3, a2, e4", "e3, a1, e4, a2, e5", "e4, a1, e5, a2, e6", "e5, a1, e6, a2, e7", "e6, a1, e7, a2, e8",
            "e1, a1, f2, a2, g3", "e2, a1, f3, a2, g4", "e3, a1, f4, a2, g5", "e4, a1, f5, a2, g6", "e5, a1, f6, a2, g7", "e6, a1, f7, a2, g8",
            "e1, a1, f1, a2, g1", "e2, a1, f2, a3, g2", "e3, a1, f3, a2, g3", "e4, a1, f4, a2, g4", "e5, a1, f5, a2, g5", "e6, a1, f6, a2, g6", "e7, a1, f7, a2, g7", "e8, a1, f8, a2, g8",
            "e3, a1, f2, a2, g1", "e4, a1, f3, a2, g2", "e5, a1, f4, a2, g3", "e6, a1, f5, a2, g4", "e7, a1, f6, a2, g5", "e8, a1, f7, a2, g6",
            "e3, a1, e2, a2, e1", "e4, a1, e3, a2, e2", "e5, a1, e4, a2, e3", "e6, a1, e5, a2, e4", "e7, a1, e6, a2, e5", "e8, a1, e7, a2, e6",
            "e1, a3, d1, a2, c1", "e2, a1, d2, a3, c2", "e3, a1, d3, a2, c3", "e4, a1, d4, a2, c4", "e5, a1, d5, a2, c5", "e6, a1, d6, a2, c6", "e7, a1, d7, a2, c7", "e8, a1, d8, a2, c8",
            "e3, a3, d2, a2, c1", "e4, a1, d3, a3, c2", "e5, a1, d4, a2, c3", "e6, a1, d5, a2, c4", "e7, a1, d6, a2, c5", "e8, a1, d7, a2, c6",
            "e3, a3, d2, a2, c1", "e4, a1, d3, a3, c2", "e5, a1, d4, a2, c3", "e6, a1, d5, a2, c4", "e7, a1, d6, a2, c5", "e8, a1, d7, a2, c6",

            "f1, a1, f2, a2, f3", "f2, a1, f3, a2, f4", "f3, a1, f4, a2, f5", "f4, a1, f5, a2, f6", "f5, a1, f6, a2, f7", "f6, a1, f7, a2, f8",
            "f1, a1, g2, a2, h3", "f2, a1, g3, a2, h4", "f3, a1, g4, a2, h5", "f4, a1, g5, a2, h6", "f5, a1, g6, a2, h7", "f6, a1, g7, a2, h8",
            "f1, a1, g1, a2, h1", "f2, a1, g2, a3, h2", "f3, a1, g3, a2, h3", "f4, a1, g4, a2, h4", "f5, a1, g5, a2, h5", "f6, a1, g6, a2, h6", "f7, a1, g7, a2, h7", "f8, a1, g8, a2, h8",
            "f3, a1, g2, a2, h1", "f4, a1, g3, a2, h2", "f5, a1, g4, a2, h3", "f6, a1, g5, a2, h4", "f7, a1, g6, a2, h5", "f8, a1, g7, a2, h6",
            "f3, a1, f2, a2, f1", "f4, a1, f3, a2, f2", "f5, a1, f4, a2, f3", "f6, a1, f5, a2, f4", "f7, a1, f6, a2, f5", "f8, a1, f7, a2, f6",
            "f1, a3, e1, a2, d1", "f2, a1, e2, a3, d2", "f3, a1, e3, a2, d3", "f4, a1, e4, a2, d4", "f5, a1, e5, a2, d5", "f6, a1, e6, a2, d6", "f7, a1, e7, a2, d7", "f8, a1, e8, a2, d8",
            "f3, a3, e2, a2, d1", "f4, a1, e3, a3, d2", "f5, a1, e4, a2, d3", "f6, a1, e5, a2, d4", "f7, a1, e6, a2, d5", "f8, a1, e7, a2, d6",
            "f3, a3, e2, a2, d1", "f4, a1, e3, a3, d2", "f5, a1, e4, a2, d3", "f6, a1, e5, a2, d4", "f7, a1, e6, a2, d5", "f8, a1, e7, a2, d6",

            "g1, a1, g2, a2, g3", "g2, a1, g3, a2, g4", "g3, a1, g4, a2, g5", "g4, a1, g5, a2, g6", "g5, a1, g6, a2, g7", "g6, a1, g7, a2, g8",
            "g3, a1, g2, a2, g1", "g4, a1, g3, a2, g2", "g5, a1, g4, a2, g3", "g6, a1, g5, a2, g4", "g7, a1, g6, a2, g5", "g8, a1, g7, a2, g6",
            "g1, a3, f1, a2, e1", "g2, a1, f2, a3, e2", "g3, a1, f3, a2, e3", "g4, a1, f4, a2, e4", "g5, a1, f5, a2, e5", "g6, a1, f6, a2, e6", "g7, a1, f7, a2, e7", "g8, a1, f8, a2, e8",
            "g3, a3, f2, a2, e1", "g4, a1, f3, a3, e2", "g5, a1, f4, a2, e3", "g6, a1, f5, a2, e4", "g7, a1, f6, a2, e5", "g8, a1, f7, a2, e6",
            "g3, a3, f2, a2, e1", "g4, a1, f3, a3, e2", "g5, a1, f4, a2, e3", "g6, a1, f5, a2, e4", "g7, a1, f6, a2, e5", "g8, a1, f7, a2, e6",

            "h1, a1, h2, a2, h3", "h2, a1, h3, a2, h4", "h3, a1, h4, a2, h5", "h4, a1, h5, a2, h6", "h5, a1, h6, a2, h7", "h6, a1, h7, a2, h8",
            "h3, a1, h2, a2, h1", "h4, a1, h3, a2, h2", "h5, a1, h4, a2, h3", "h6, a1, h5, a2, h4", "h7, a1, h6, a2, h5", "h8, a1, h7, a2, h6",
            "h1, a3, g1, a2, f1", "h2, a1, g2, a3, f2", "h3, a1, g3, a2, f3", "h4, a1, g4, a2, f4", "h5, a1, g5, a2, f5", "h6, a1, g6, a2, f6", "h7, a1, g7, a2, f7", "h8, a1, g8, a2, f8",
            "h3, a3, g2, a2, f1", "h4, a1, g3, a3, f2", "h5, a1, g4, a2, f3", "h6, a1, g5, a2, f4", "h7, a1, g6, a2, f5", "h8, a1, g7, a2, f6",
            "h3, a3, g2, a2, f1", "h4, a1, g3, a3, f2", "h5, a1, g4, a2, f3", "h6, a1, g5, a2, f4", "h7, a1, g6, a2, f5", "h8, a1, g7, a2, f6"
    })
    void attemptRowIncrease(ArgumentsAccessor moves) {
        this.sendCommandToController(moves.getString(0));
        this.sendCommandToController(moves.getString(1));
        this.sendCommandToController(moves.getString(2));
        this.sendCommandToController(moves.getString(3));
        this.sendCommandToController(moves.getString(4));
        String invalidSetupMessage = "playing a move in a winthreshold=1 game does not result in the game being won";
        assertNotNull(model.getWinner(), invalidSetupMessage);
        int start = model.getNumberOfRows();
        controller.addRow();
        int current = model.getNumberOfRows();
        String failureMessage = "controller.addRow() did not add a row after the game was won";
        assertEquals(start + 1, current, failureMessage);
    }

    @DisplayName("Testing that controller.addColumn() does increase the number of columns after the game is won")
    @ParameterizedTest(name = "{displayName}.  winning line is {0}->{2}->{4}")
    @CsvSource({
            "a1, b1, a2, b2, a3", "a2, b1, a3, b2, a4", "a3, b1, a4, b2, a5", "a4, b1, a5, b2, a6", "a5, b1, a6, b2, a7", "a6, b1, a7, b2, a8",
            "a1, b1, b2, c2, c3", "a2, b1, b3, b2, c4", "a3, b1, b4, b2, c5", "a4, b1, b5, b2, c6", "a5, b1, b6, b2, c7", "a6, b1, b7, b2, c8",
            "a1, b2, b1, c2, c1", "a2, b1, b2, b3, c2", "a3, b1, b3, b2, c3", "a4, b1, b4, b2, c4", "a5, b1, b5, b2, c5", "a6, b1, b6, b2, c6", "a7, b1, b7, b2, c7", "a8, b1, b8, b2, c8",
            "a3, b1, b2, c2, c1", "a4, b1, b3, b2, c2", "a5, b1, b4, b2, c3", "a6, b1, b5, b2, c4", "a7, b1, b6, b2, c5", "a8, b1, b7, b2, c6",
            "a3, b1, a2, c2, a1", "a4, b1, a3, b2, a2", "a5, b1, a4, b2, a3", "a6, b1, a5, b2, a4", "a7, b1, a6, b2, a5", "a8, b1, a7, b2, a6",

            "b1, a1, b2, a2, b3", "b2, a1, b3, a2, b4", "b3, a1, b4, a2, b5", "b4, a1, b5, a2, b6", "b5, a1, b6, a2, b7", "b6, a1, b7, a2, b8",
            "b1, a1, c2, a2, d3", "b2, a1, c3, a2, d4", "b3, a1, c4, a2, d5", "b4, a1, c5, a2, d6", "b5, a1, c6, a2, d7", "b6, a1, c7, a2, d8",
            "b1, a1, c1, a2, d1", "b2, a1, c2, a3, d2", "b3, a1, c3, a2, d3", "b4, a1, c4, a2, d4", "b5, a1, c5, a2, d5", "b6, a1, c6, a2, d6", "b7, a1, c7, a2, d7", "b8, a1, c8, a2, d8",
            "b3, a1, c2, a2, d1", "b4, a1, c3, a2, d2", "b5, a1, c4, a2, d3", "b6, a1, c5, a2, d4", "b7, a1, c6, a2, d5", "b8, a1, c7, a2, d6",
            "b3, a1, b2, a2, b1", "b4, a1, b3, a2, b2", "b5, a1, b4, a2, b3", "b6, a1, b5, a2, b4", "b7, a1, b6, a2, b5", "b8, a1, b7, a2, b6",

            "c1, a1, c2, a2, c3", "c2, a1, c3, a2, c4", "c3, a1, c4, a2, c5", "c4, a1, c5, a2, c6", "c5, a1, c6, a2, c7", "c6, a1, c7, a2, c8",
            "c1, a1, d2, a2, e3", "c2, a1, d3, a2, e4", "c3, a1, d4, a2, e5", "c4, a1, d5, a2, e6", "c5, a1, d6, a2, e7", "c6, a1, d7, a2, e8",
            "c1, a1, d1, a2, e1", "c2, a1, d2, a3, e2", "c3, a1, d3, a2, e3", "c4, a1, d4, a2, e4", "c5, a1, d5, a2, e5", "c6, a1, d6, a2, e6", "c7, a1, d7, a2, e7", "c8, a1, d8, a2, e8",
            "c3, a1, d2, a2, e1", "c4, a1, d3, a2, e2", "c5, a1, d4, a2, e3", "c6, a1, d5, a2, e4", "c7, a1, d6, a2, e5", "c8, a1, d7, a2, e6",
            "c3, a1, c2, a2, c1", "c4, a1, c3, a2, c2", "c5, a1, c4, a2, c3", "c6, a1, c5, a2, c4", "c7, a1, c6, a2, c5", "c8, a1, c7, a2, c6",
            "c1, a3, b1, a2, a1", "c2, a1, b2, a3, a2", "c3, a1, b3, a2, a3", "c4, a1, b4, a2, a4", "c5, a1, b5, a2, a5", "c6, a1, b6, a2, a6", "c7, a1, b7, a2, a7", "c8, a1, b8, a2, a8",
            "c3, a3, b2, a2, a1", "c4, a1, b3, a3, a2", "c5, a1, b4, a2, a3", "c6, a1, b5, a2, a4", "c7, a1, b6, a2, a5", "c8, a1, b7, a2, a6",
            "c3, a3, b2, a2, a1", "c4, a1, b3, a3, a2", "c5, a1, b4, a2, a3", "c6, a1, b5, a2, a4", "c7, a1, b6, a2, a5", "c8, a1, b7, a2, a6",

            "d1, a1, d2, a2, d3", "d2, a1, d3, a2, d4", "d3, a1, d4, a2, d5", "d4, a1, d5, a2, d6", "d5, a1, d6, a2, d7", "d6, a1, d7, a2, d8",
            "d1, a1, e2, a2, f3", "d2, a1, e3, a2, f4", "d3, a1, e4, a2, f5", "d4, a1, e5, a2, f6", "d5, a1, e6, a2, f7", "d6, a1, e7, a2, f8",
            "d1, a1, e1, a2, f1", "d2, a1, e2, a3, f2", "d3, a1, e3, a2, f3", "d4, a1, e4, a2, f4", "d5, a1, e5, a2, f5", "d6, a1, e6, a2, f6", "d7, a1, e7, a2, f7", "d8, a1, e8, a2, f8",
            "d3, a1, e2, a2, f1", "d4, a1, e3, a2, f2", "d5, a1, e4, a2, f3", "d6, a1, e5, a2, f4", "d7, a1, e6, a2, f5", "d8, a1, e7, a2, f6",
            "d3, a1, d2, a2, d1", "d4, a1, d3, a2, d2", "d5, a1, d4, a2, d3", "d6, a1, d5, a2, d4", "d7, a1, d6, a2, d5", "d8, a1, d7, a2, d6",
            "d1, a3, c1, a2, b1", "d2, a1, c2, a3, b2", "d3, a1, c3, a2, b3", "d4, a1, c4, a2, b4", "d5, a1, c5, a2, b5", "d6, a1, c6, a2, b6", "d7, a1, c7, a2, b7", "d8, a1, c8, a2, b8",
            "d3, a3, c2, a2, b1", "d4, a1, c3, a3, b2", "d5, a1, c4, a2, b3", "d6, a1, c5, a2, b4", "d7, a1, c6, a2, b5", "d8, a1, c7, a2, b6",
            "d3, a3, c2, a2, b1", "d4, a1, c3, a3, b2", "d5, a1, c4, a2, b3", "d6, a1, c5, a2, b4", "d7, a1, c6, a2, b5", "d8, a1, c7, a2, b6",

            "e1, a1, e2, a2, e3", "e2, a1, e3, a2, e4", "e3, a1, e4, a2, e5", "e4, a1, e5, a2, e6", "e5, a1, e6, a2, e7", "e6, a1, e7, a2, e8",
            "e1, a1, f2, a2, g3", "e2, a1, f3, a2, g4", "e3, a1, f4, a2, g5", "e4, a1, f5, a2, g6", "e5, a1, f6, a2, g7", "e6, a1, f7, a2, g8",
            "e1, a1, f1, a2, g1", "e2, a1, f2, a3, g2", "e3, a1, f3, a2, g3", "e4, a1, f4, a2, g4", "e5, a1, f5, a2, g5", "e6, a1, f6, a2, g6", "e7, a1, f7, a2, g7", "e8, a1, f8, a2, g8",
            "e3, a1, f2, a2, g1", "e4, a1, f3, a2, g2", "e5, a1, f4, a2, g3", "e6, a1, f5, a2, g4", "e7, a1, f6, a2, g5", "e8, a1, f7, a2, g6",
            "e3, a1, e2, a2, e1", "e4, a1, e3, a2, e2", "e5, a1, e4, a2, e3", "e6, a1, e5, a2, e4", "e7, a1, e6, a2, e5", "e8, a1, e7, a2, e6",
            "e1, a3, d1, a2, c1", "e2, a1, d2, a3, c2", "e3, a1, d3, a2, c3", "e4, a1, d4, a2, c4", "e5, a1, d5, a2, c5", "e6, a1, d6, a2, c6", "e7, a1, d7, a2, c7", "e8, a1, d8, a2, c8",
            "e3, a3, d2, a2, c1", "e4, a1, d3, a3, c2", "e5, a1, d4, a2, c3", "e6, a1, d5, a2, c4", "e7, a1, d6, a2, c5", "e8, a1, d7, a2, c6",
            "e3, a3, d2, a2, c1", "e4, a1, d3, a3, c2", "e5, a1, d4, a2, c3", "e6, a1, d5, a2, c4", "e7, a1, d6, a2, c5", "e8, a1, d7, a2, c6",

            "f1, a1, f2, a2, f3", "f2, a1, f3, a2, f4", "f3, a1, f4, a2, f5", "f4, a1, f5, a2, f6", "f5, a1, f6, a2, f7", "f6, a1, f7, a2, f8",
            "f1, a1, g2, a2, h3", "f2, a1, g3, a2, h4", "f3, a1, g4, a2, h5", "f4, a1, g5, a2, h6", "f5, a1, g6, a2, h7", "f6, a1, g7, a2, h8",
            "f1, a1, g1, a2, h1", "f2, a1, g2, a3, h2", "f3, a1, g3, a2, h3", "f4, a1, g4, a2, h4", "f5, a1, g5, a2, h5", "f6, a1, g6, a2, h6", "f7, a1, g7, a2, h7", "f8, a1, g8, a2, h8",
            "f3, a1, g2, a2, h1", "f4, a1, g3, a2, h2", "f5, a1, g4, a2, h3", "f6, a1, g5, a2, h4", "f7, a1, g6, a2, h5", "f8, a1, g7, a2, h6",
            "f3, a1, f2, a2, f1", "f4, a1, f3, a2, f2", "f5, a1, f4, a2, f3", "f6, a1, f5, a2, f4", "f7, a1, f6, a2, f5", "f8, a1, f7, a2, f6",
            "f1, a3, e1, a2, d1", "f2, a1, e2, a3, d2", "f3, a1, e3, a2, d3", "f4, a1, e4, a2, d4", "f5, a1, e5, a2, d5", "f6, a1, e6, a2, d6", "f7, a1, e7, a2, d7", "f8, a1, e8, a2, d8",
            "f3, a3, e2, a2, d1", "f4, a1, e3, a3, d2", "f5, a1, e4, a2, d3", "f6, a1, e5, a2, d4", "f7, a1, e6, a2, d5", "f8, a1, e7, a2, d6",
            "f3, a3, e2, a2, d1", "f4, a1, e3, a3, d2", "f5, a1, e4, a2, d3", "f6, a1, e5, a2, d4", "f7, a1, e6, a2, d5", "f8, a1, e7, a2, d6",

            "g1, a1, g2, a2, g3", "g2, a1, g3, a2, g4", "g3, a1, g4, a2, g5", "g4, a1, g5, a2, g6", "g5, a1, g6, a2, g7", "g6, a1, g7, a2, g8",
            "g3, a1, g2, a2, g1", "g4, a1, g3, a2, g2", "g5, a1, g4, a2, g3", "g6, a1, g5, a2, g4", "g7, a1, g6, a2, g5", "g8, a1, g7, a2, g6",
            "g1, a3, f1, a2, e1", "g2, a1, f2, a3, e2", "g3, a1, f3, a2, e3", "g4, a1, f4, a2, e4", "g5, a1, f5, a2, e5", "g6, a1, f6, a2, e6", "g7, a1, f7, a2, e7", "g8, a1, f8, a2, e8",
            "g3, a3, f2, a2, e1", "g4, a1, f3, a3, e2", "g5, a1, f4, a2, e3", "g6, a1, f5, a2, e4", "g7, a1, f6, a2, e5", "g8, a1, f7, a2, e6",
            "g3, a3, f2, a2, e1", "g4, a1, f3, a3, e2", "g5, a1, f4, a2, e3", "g6, a1, f5, a2, e4", "g7, a1, f6, a2, e5", "g8, a1, f7, a2, e6",

            "h1, a1, h2, a2, h3", "h2, a1, h3, a2, h4", "h3, a1, h4, a2, h5", "h4, a1, h5, a2, h6", "h5, a1, h6, a2, h7", "h6, a1, h7, a2, h8",
            "h3, a1, h2, a2, h1", "h4, a1, h3, a2, h2", "h5, a1, h4, a2, h3", "h6, a1, h5, a2, h4", "h7, a1, h6, a2, h5", "h8, a1, h7, a2, h6",
            "h1, a3, g1, a2, f1", "h2, a1, g2, a3, f2", "h3, a1, g3, a2, f3", "h4, a1, g4, a2, f4", "h5, a1, g5, a2, f5", "h6, a1, g6, a2, f6", "h7, a1, g7, a2, f7", "h8, a1, g8, a2, f8",
            "h3, a3, g2, a2, f1", "h4, a1, g3, a3, f2", "h5, a1, g4, a2, f3", "h6, a1, g5, a2, f4", "h7, a1, g6, a2, f5", "h8, a1, g7, a2, f6",
            "h3, a3, g2, a2, f1", "h4, a1, g3, a3, f2", "h5, a1, g4, a2, f3", "h6, a1, g5, a2, f4", "h7, a1, g6, a2, f5", "h8, a1, g7, a2, f6"
    })
    void attemptColIncrease(ArgumentsAccessor moves) {
        this.sendCommandToController(moves.getString(0));
        this.sendCommandToController(moves.getString(1));
        this.sendCommandToController(moves.getString(2));
        this.sendCommandToController(moves.getString(3));
        this.sendCommandToController(moves.getString(4));
        String invalidSetupMessage = "playing a move in a winthreshold=1 game does not result in the game being won";
        assertNotNull(model.getWinner(), invalidSetupMessage);
        int start = model.getNumberOfColumns();
        controller.addColumn();
        int current = model.getNumberOfColumns();
        String failureMessage = "controller.addColumn() did not add a column after the game was won";
        assertEquals(start + 1, current, failureMessage);
    }
}
