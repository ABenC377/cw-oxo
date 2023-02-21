package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ResetTests {
    private OXOModel model;
    private OXOController controller;
    String[] moves = {"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1", "i1",
            "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2", "i2",
            "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3", "i3",
            "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4", "i4",
            "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5", "i5",
            "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6", "i6",
            "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7", "i7",
            "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8", "i8",
            "a9", "b9", "c9", "d9", "e9", "f9", "g9", "h9", "i9"};
    @BeforeEach
    void setup() {
        model = new OXOModel(9, 9, 3);
        model.addPlayer(new OXOPlayer('X'));
        model.addPlayer(new OXOPlayer('O'));
        controller = new OXOController(model);
    }
    void sendCommandToController(String command) {
        // Try to send a command to the server - call will timeout if it takes too long (in case the server enters an infinite loop)
        // Note: this is ugly code and includes syntax that you haven't encountered yet
        String timeoutComment = "Controller took too long to respond (probably stuck in an infinite loop)";
        assertTimeoutPreemptively(Duration.ofMillis(1000), ()-> controller.handleIncomingCommand(command), timeoutComment);
    }

    // Testing the reset() method for the controller -
    // should reset all cells to null, and make the player to move next the first player
    @DisplayName("Testing that controller.reset() returns play to the first player")
    @ParameterizedTest(name = "{displayName} after an opening play of {arguments}")
    @ValueSource(strings = {"a1", "A1", "b1", "B1", "c1", "C1", "d1", "D1", "e1", "E1", "f1", "F1", "g1", "G1", "h1",
            "H1", "i1", "I1", "a2", "A2", "b2", "B2", "c2", "C2", "d2", "D2", "e2", "E2", "f2", "F2", "g2", "G2", "h2",
            "H2", "i2", "I2", "a3", "A3", "b3", "B3", "c3", "C3", "d3", "D3", "e3", "E3", "f3", "F3", "g3", "G3", "h3",
            "H3", "i3", "I3", "a4", "A4", "b4", "B4", "c4", "C4", "d4", "D4", "e4", "E4", "f4", "F4", "g4", "G4", "h4",
            "H4", "i4", "I4", "a5", "A5", "b5", "B5", "c5", "C5", "d5", "D5", "e5", "E5", "f5", "F5", "g5", "G5", "h5",
            "H5", "i5", "I5", "a6", "A6", "b6", "B6", "c6", "C6", "d6", "D6", "e6", "E6", "f6", "F6", "g6", "G6", "h6",
            "H6", "i6", "I6", "a7", "A7", "b7", "B7", "c7", "C7", "d7", "D7", "e7", "E7", "f7", "F7", "g7", "G7", "h7",
            "H7", "i7", "I7", "a8", "A8", "b8", "B8", "c8", "C8", "d8", "D8", "e8", "E8", "f8", "F8", "g8", "G8", "h8",
            "H8", "i8", "I8", "a9", "A9", "b9", "B9", "c9", "C9", "d9", "D9", "e9", "E9", "f9", "F9", "g9", "G9", "h9",
            "H9", "i9", "I9"})
    void resetReturnsPlayToFirstPlayerAfterSingleMove(String move) {
        sendCommandToController(move); // First player
        String invalidSetupComment = "Making a move did not change player turn and fill tile";
        assertEquals(model.getCurrentPlayerNumber(), 1, invalidSetupComment);
        controller.reset();
        String failedTestComment = "Reset failed to make it player 1's go, when only a single move was made and there are only two players";
        assertEquals(model.getCurrentPlayerNumber(), 0, failedTestComment);
    }

    @DisplayName("Testing that controller.reset() removes plays from the board")
    @ParameterizedTest(name = "{displayName} after an opening play of {arguments}")
    @ValueSource(strings = {"a1", "A1", "b1", "B1", "c1", "C1", "d1", "D1", "e1", "E1", "f1", "F1", "g1", "G1", "h1",
            "H1", "i1", "I1", "a2", "A2", "b2", "B2", "c2", "C2", "d2", "D2", "e2", "E2", "f2", "F2", "g2", "G2", "h2",
            "H2", "i2", "I2", "a3", "A3", "b3", "B3", "c3", "C3", "d3", "D3", "e3", "E3", "f3", "F3", "g3", "G3", "h3",
            "H3", "i3", "I3", "a4", "A4", "b4", "B4", "c4", "C4", "d4", "D4", "e4", "E4", "f4", "F4", "g4", "G4", "h4",
            "H4", "i4", "I4", "a5", "A5", "b5", "B5", "c5", "C5", "d5", "D5", "e5", "E5", "f5", "F5", "g5", "G5", "h5",
            "H5", "i5", "I5", "a6", "A6", "b6", "B6", "c6", "C6", "d6", "D6", "e6", "E6", "f6", "F6", "g6", "G6", "h6",
            "H6", "i6", "I6", "a7", "A7", "b7", "B7", "c7", "C7", "d7", "D7", "e7", "E7", "f7", "F7", "g7", "G7", "h7",
            "H7", "i7", "I7", "a8", "A8", "b8", "B8", "c8", "C8", "d8", "D8", "e8", "E8", "f8", "F8", "g8", "G8", "h8",
            "H8", "i8", "I8", "a9", "A9", "b9", "B9", "c9", "C9", "d9", "D9", "e9", "E9", "f9", "F9", "g9", "G9", "h9",
            "H9", "i9", "I9"})
    void resetRemovesSinglePlay(String move) {
        sendCommandToController(move);
        String setupFailureMessage = "making a move did not result in the board being non-empty";
        assertFalse(this.boardIsClear(), setupFailureMessage);
        controller.reset();
        String failureMessage = "controller.reset() did not result in the board being empty";
        assertTrue(this.boardIsClear(), failureMessage);
    }

    @DisplayName("Testing that controller.reset() removes plays from the board")
    @ParameterizedTest(name = "{displayName} #{index}")
    @MethodSource("intProvider")
    void resetRemovesSeriesOfPlays(int n) {
        for (int i = 0; i < (n % 4); i++) {
            model.addPlayer(new OXOPlayer('?'));
        }
        int plays = 1 + (n % 80);
        this.makeWinImpossible();
        ArrayList<String> remainingMoves = new ArrayList<>(Arrays.asList(moves));

        for (int i = 0; i < plays; i++) {
            int moveIndex = (int)(Math.random() * remainingMoves.size());
            String moveToPlay = remainingMoves.get(moveIndex);
            remainingMoves.remove(moveIndex);
            sendCommandToController(moveToPlay);
        }
        String setupFailureMessage = "Playing moves did not result in board being non-clear";
        assertFalse(this.boardIsClear(), setupFailureMessage);
        controller.reset();
        String failureMessage = "controller.reset() did not result in the board being cleared";
        assertTrue(this.boardIsClear(), failureMessage);
        String wrongPlayerMessage = "controller.reset() did not make the next turn the first players when there are " + (2 + (n % 4)) + " players";
        assertEquals(model.getCurrentPlayerNumber(), 0, wrongPlayerMessage);
    }

    @DisplayName("Testing that controller.reset() returns play to the first player")
    @ParameterizedTest(name = "{displayName} #{index}")
    @MethodSource("intProvider")
    void resetReturnsPlayToFirstPlayer(int n) {
        for (int i = 0; i < (n % 4); i++) {
            model.addPlayer(new OXOPlayer('?'));
        }
        int plays = 1 + (n % 80);
        this.makeWinImpossible();
        ArrayList<String> remainingMoves = new ArrayList<>(Arrays.asList(moves));

        for (int i = 0; i < plays; i++) {
            int moveIndex = (int)(Math.random() * remainingMoves.size());
            String moveToPlay = remainingMoves.get(moveIndex);
            remainingMoves.remove(moveIndex);
            sendCommandToController(moveToPlay);
        }
        String setupFailureMessage = "Playing moves did not result in board being non-clear";
        assertFalse(this.boardIsClear(), setupFailureMessage);
        controller.reset();
        String wrongPlayerMessage = "controller.reset() did not make the next turn the first players when there are " + (2 + (n % 4)) + " players";
        assertEquals(model.getCurrentPlayerNumber(), 0, wrongPlayerMessage);
    }

    @DisplayName("Testing that controller.reset() leaves the number of rows and columns unchanged")
    @ParameterizedTest(name = "{displayName} #{index}")
    @MethodSource("intProvider")
    void resetLeavesRowsAndColumnsUnchanged(int n) {
        controller.addRow();
        for (int i = 0; i < (n % 5); i++) {
            controller.addRow();
        }
        controller.addColumn();
        for (int j = 0; j < (n % 3); j++) {
            controller.addColumn();
        }
        int plays = 1 + (n % 80);
        this.makeWinImpossible();
        ArrayList<String> remainingMoves = new ArrayList<>(Arrays.asList(moves));

        for (int i = 0; i < plays; i++) {
            int moveIndex = (int)(Math.random() * remainingMoves.size());
            String moveToPlay = remainingMoves.get(moveIndex);
            remainingMoves.remove(moveIndex);
            sendCommandToController(moveToPlay);
        }
        int rowStart = model.getNumberOfRows();
        int colStart = model.getNumberOfColumns();
        controller.reset();
        int rowCurrent = model.getNumberOfRows();
        int colCurrent = model.getNumberOfColumns();
        String wrongRowMessage = "controller.reset() changes the number of rows";
        assertEquals(rowStart, rowCurrent, wrongRowMessage);
        String wrongColMessage = "controller.reset() changes the number of columns";
        assertEquals(colStart, colCurrent, wrongColMessage);
    }

    /*
        There are some test here which play random games and then make sure that the controller.reset() method does as
        it is intended to.  They also change the number of players to make sure it works well with up to six players.
        Due to the random nature of these tests, a relatively large number of iterations are required to achieve
        meaningful coverage.  I have plummed for 500 iterations, which seems like a large number, but when you consider
        that there are 81 * 4 different set-up environments (1-81 moves before reset, and 2-6 players) then it is only
        just over one iteration per setup
     */
    static Stream<Integer> intProvider() {
        return IntStream.rangeClosed(1, 500).boxed();
    }

    private boolean boardIsClear() {
        for (int row = 0; row < model.getNumberOfRows(); row++) {
            for (int col = 0; col < model.getNumberOfColumns(); col++) {
                if (model.getCellOwner(row, col) != null) {
                    return false;
                }
            }
        }
        return true;
    }

    private void makeWinImpossible() {
        while (model.getWinThreshold() < 10) {
            controller.increaseWinThreshold();
        }
    }
}
