package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class GameFinishedTests {
    private OXOModel model;
    private OXOController controller;
    @BeforeEach
    void Setup() {
        // This game is set up with a win threshold of 1 so that any move results in the game being won.
        // therefore, we can quickly get it into a winning state and test that it doesn't allow further input
        model = new OXOModel(8, 8, 1);
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
    @ParameterizedTest
    @ValueSource(strings = {"a1", "A1", "b1", "B1", "c1", "C1", "d1", "D1", "e1", "E1", "f1", "F1", "g1", "G1",
            "a2", "A2", "b2", "B2", "c2", "C2", "d2", "D2", "e2", "E2", "f2", "F2", "g2", "G2",
            "a3", "A3", "b3", "B3", "c3", "C3", "d3", "D3", "e3", "E3", "f3", "F3", "g3", "G3",
            "a4", "A4", "b4", "B4", "c4", "C4", "d4", "D4", "e4", "E4", "f4", "F4", "g4", "G4",
            "a5", "A5", "b5", "B5", "c5", "C5", "d5", "D5", "e5", "E5", "f5", "F5", "g5", "G5",
            "a6", "A6", "b6", "B6", "c6", "C6", "d6", "D6", "e6", "E6", "f6", "F6", "g6", "G6",
            "a7", "A7", "b7", "B7", "c7", "C7", "d7", "D7", "e7", "E7", "f7", "F7", "g7", "G7"})
    void attemptRowDecrease(String move) {
        this.sendCommandToController(move);
        String invalidSetupMessage = "playing a move in a winthreshold=1 game does not result in the game being won";
        assertTrue(model.getWinner() != null, invalidSetupMessage);
        int start = model.getNumberOfRows();
        controller.removeRow();
        int current = model.getNumberOfRows();
        String failureMessage = "controller.removeRow() did not remove a row after the game was won";
        assertEquals(start - 1, current, failureMessage);
    }
    @ParameterizedTest
    @ValueSource(strings = {"a1", "A1", "b1", "B1", "c1", "C1", "d1", "D1", "e1", "E1", "f1", "F1", "g1", "G1",
            "a2", "A2", "b2", "B2", "c2", "C2", "d2", "D2", "e2", "E2", "f2", "F2", "g2", "G2",
            "a3", "A3", "b3", "B3", "c3", "C3", "d3", "D3", "e3", "E3", "f3", "F3", "g3", "G3",
            "a4", "A4", "b4", "B4", "c4", "C4", "d4", "D4", "e4", "E4", "f4", "F4", "g4", "G4",
            "a5", "A5", "b5", "B5", "c5", "C5", "d5", "D5", "e5", "E5", "f5", "F5", "g5", "G5",
            "a6", "A6", "b6", "B6", "c6", "C6", "d6", "D6", "e6", "E6", "f6", "F6", "g6", "G6",
            "a7", "A7", "b7", "B7", "c7", "C7", "d7", "D7", "e7", "E7", "f7", "F7", "g7", "G7"})
    void attemptColDecrease(String move) {
        this.sendCommandToController(move);
        String invalidSetupMessage = "playing a move in a winthreshold=1 game does not result in the game being won";
        assertTrue(model.getWinner() != null, invalidSetupMessage);
        int start = model.getNumberOfColumns();
        controller.removeColumn();
        int current = model.getNumberOfColumns();
        String failureMessage = "controller.removeColumn() did not remove a column after the game was won";
        assertEquals(start - 1, current, failureMessage);
    }
    @ParameterizedTest
    @ValueSource(strings = {"a1", "A1", "b1", "B1", "c1", "C1", "d1", "D1", "e1", "E1", "f1", "F1", "g1", "G1", "h1", "H1",
            "a2", "A2", "b2", "B2", "c2", "C2", "d2", "D2", "e2", "E2", "f2", "F2", "g2", "G2", "h2", "H2",
            "a3", "A3", "b3", "B3", "c3", "C3", "d3", "D3", "e3", "E3", "f3", "F3", "g3", "G3", "h3", "H3",
            "a4", "A4", "b4", "B4", "c4", "C4", "d4", "D4", "e4", "E4", "f4", "F4", "g4", "G4", "h4", "H4",
            "a5", "A5", "b5", "B5", "c5", "C5", "d5", "D5", "e5", "E5", "f5", "F5", "g5", "G5", "h5", "H5",
            "a6", "A6", "b6", "B6", "c6", "C6", "d6", "D6", "e6", "E6", "f6", "F6", "g6", "G6", "h6", "H6",
            "a7", "A7", "b7", "B7", "c7", "C7", "d7", "D7", "e7", "E7", "f7", "F7", "g7", "G7", "h7", "H7",
            "a8", "A8", "b8", "B8", "c8", "C8", "d8", "D8", "e8", "E8", "f8", "F8", "g8", "G8", "h8", "H8"})
    void attemptRowIncrease(String move) {
        this.sendCommandToController(move);
        String invalidSetupMessage = "playing a move in a winthreshold=1 game does not result in the game being won";
        assertTrue(model.getWinner() != null, invalidSetupMessage);
        int start = model.getNumberOfRows();
        controller.addRow();
        int current = model.getNumberOfRows();
        String failureMessage = "controller.addRow() did not add a row after the game was won";
        assertEquals(start + 1, current, failureMessage);
    }
    @ParameterizedTest
    @ValueSource(strings = {"a1", "A1", "b1", "B1", "c1", "C1", "d1", "D1", "e1", "E1", "f1", "F1", "g1", "G1", "h1", "H1",
            "a2", "A2", "b2", "B2", "c2", "C2", "d2", "D2", "e2", "E2", "f2", "F2", "g2", "G2", "h2", "H2",
            "a3", "A3", "b3", "B3", "c3", "C3", "d3", "D3", "e3", "E3", "f3", "F3", "g3", "G3", "h3", "H3",
            "a4", "A4", "b4", "B4", "c4", "C4", "d4", "D4", "e4", "E4", "f4", "F4", "g4", "G4", "h4", "H4",
            "a5", "A5", "b5", "B5", "c5", "C5", "d5", "D5", "e5", "E5", "f5", "F5", "g5", "G5", "h5", "H5",
            "a6", "A6", "b6", "B6", "c6", "C6", "d6", "D6", "e6", "E6", "f6", "F6", "g6", "G6", "h6", "H6",
            "a7", "A7", "b7", "B7", "c7", "C7", "d7", "D7", "e7", "E7", "f7", "F7", "g7", "G7", "h7", "H7",
            "a8", "A8", "b8", "B8", "c8", "C8", "d8", "D8", "e8", "E8", "f8", "F8", "g8", "G8", "h8", "H8"})
    void attemptColIncrease(String move) {
        this.sendCommandToController(move);
        String invalidSetupMessage = "playing a move in a winthreshold=1 game does not result in the game being won";
        assertTrue(model.getWinner() != null, invalidSetupMessage);
        int start = model.getNumberOfColumns();
        controller.addColumn();
        int current = model.getNumberOfColumns();
        String failureMessage = "controller.addColumn() did not add a column after the game was won";
        assertEquals(start + 1, current, failureMessage);
    }
}
