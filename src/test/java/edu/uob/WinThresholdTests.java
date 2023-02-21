package edu.uob;
import edu.uob.OXOMoveException.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class WinThresholdTests {
    private OXOModel model;
    private OXOController controller;
    @BeforeEach
    void Setup() {
        model = new OXOModel(9, 9, 3);
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

    @ParameterizedTest
    @MethodSource("intProvider")
    void validIncrease9x9Empty(int start) {
        this.increaseThresholdByN(start - 1);
        int startingThreshold = model.getWinThreshold();
        controller.increaseWinThreshold();
        int updatedThreshold = model.getWinThreshold();
        String failureMessage = "Win threshold was not increased by one";
        assertEquals(updatedThreshold, startingThreshold + 1, failureMessage);
    }

    @ParameterizedTest
    @MethodSource("intProvider")
    void validIncrease8x8Empty(int start) {
        controller.removeRow();
        controller.removeColumn();
        this.increaseThresholdByN(start - 1);
        int startingThreshold = model.getWinThreshold();
        controller.increaseWinThreshold();
        int updatedThreshold = model.getWinThreshold();
        String failureMessage = "Win threshold was not increased by one";
        assertEquals(updatedThreshold, startingThreshold + 1, failureMessage);
    }

    @ParameterizedTest
    @MethodSource("intProvider")
    void validIncrease7x7Empty(int start) {
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        this.increaseThresholdByN(start - 1);
        int startingThreshold = model.getWinThreshold();
        controller.increaseWinThreshold();
        int updatedThreshold = model.getWinThreshold();
        String failureMessage = "Win threshold was not increased by one";
        assertEquals(updatedThreshold, startingThreshold + 1, failureMessage);
    }

    @ParameterizedTest
    @MethodSource("intProvider")
    void validIncrease6x6Empty(int start) {
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        this.increaseThresholdByN(start - 1);
        int startingThreshold = model.getWinThreshold();
        controller.increaseWinThreshold();
        int updatedThreshold = model.getWinThreshold();
        String failureMessage = "Win threshold was not increased by one";
        assertEquals(updatedThreshold, startingThreshold + 1, failureMessage);
    }

    static Stream<Integer> intProvider() {
        return IntStream.rangeClosed(1, 100).boxed();
    }

    @ParameterizedTest
    @MethodSource("intProvider")
    void validDecrease9x9Empty(int start) {
        model.setWinThreshold(9);
        this.decreaseThresholdByN(start - 1);
        int startingThreshold = model.getWinThreshold();
        controller.decreaseWinThreshold();
        int updatedThreshold = model.getWinThreshold();
        String failureMessage = "Win threshold was not increased by one";
        assertEquals(updatedThreshold, startingThreshold - 1, failureMessage);
    }

    @ParameterizedTest
    @MethodSource("intProvider")
    void validDecrease8x8Empty(int start) {
        this.reduceBoardByN(1);
        model.setWinThreshold(9);
        this.decreaseThresholdByN(start - 1);
        int startingThreshold = model.getWinThreshold();
        controller.decreaseWinThreshold();
        int updatedThreshold = model.getWinThreshold();
        String failureMessage = "Win threshold was not increased by one";
        assertEquals(updatedThreshold, startingThreshold - 1, failureMessage);
    }

    @ParameterizedTest
    @MethodSource("intProvider")
    void validDecrease7x7Empty(int start) {
        this.reduceBoardByN(2);
        model.setWinThreshold(9);
        this.decreaseThresholdByN(start - 1);
        int startingThreshold = model.getWinThreshold();
        controller.decreaseWinThreshold();
        int updatedThreshold = model.getWinThreshold();
        String failureMessage = "Win threshold was not increased by one";
        assertEquals(updatedThreshold, startingThreshold - 1, failureMessage);
    }

    @ParameterizedTest
    @MethodSource("intProvider")
    void validDecrease6x6Empty(int start) {
        this.reduceBoardByN(3);
        model.setWinThreshold(9);
        this.decreaseThresholdByN(start - 1);
        int startingThreshold = model.getWinThreshold();
        controller.decreaseWinThreshold();
        int updatedThreshold = model.getWinThreshold();
        String failureMessage = "Win threshold was not increased by one";
        assertEquals(updatedThreshold, startingThreshold - 1, failureMessage);
    }

    @ParameterizedTest
    @MethodSource("intProvider")
    void validDecrease5x5Empty(int start) {
        this.reduceBoardByN(4);
        model.setWinThreshold(9);
        this.decreaseThresholdByN(start - 1);
        int startingThreshold = model.getWinThreshold();
        controller.decreaseWinThreshold();
        int updatedThreshold = model.getWinThreshold();
        String failureMessage = "Win threshold was not increased by one";
        assertEquals(updatedThreshold, startingThreshold - 1, failureMessage);
    }

    @ParameterizedTest
    @MethodSource("intProvider")
    void validDecrease4x4Empty(int start) {
        this.reduceBoardByN(5);
        model.setWinThreshold(9);
        this.decreaseThresholdByN(start - 1);
        int startingThreshold = model.getWinThreshold();
        controller.decreaseWinThreshold();
        int updatedThreshold = model.getWinThreshold();
        String failureMessage = "Win threshold was not increased by one";
        assertEquals(updatedThreshold, startingThreshold - 1, failureMessage);
    }

    @ParameterizedTest
    @MethodSource("intProvider")
    void validDecrease3x3Empty(int start) {
        this.reduceBoardByN(6);
        model.setWinThreshold(9);
        this.decreaseThresholdByN(start - 1);
        int startingThreshold = model.getWinThreshold();
        controller.decreaseWinThreshold();
        int updatedThreshold = model.getWinThreshold();
        String failureMessage = "Win threshold was not increased by one";
        assertEquals(updatedThreshold, startingThreshold - 1, failureMessage);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8})
    void invalidDecreaseGameDrawn(int size) {
        this.reduceBoardByN(size);
        this.increaseThresholdByN(10);
        this.makeGameADraw();
        int current = model.getWinThreshold();
        controller.decreaseWinThreshold();
        int updated = model.getWinThreshold();
        String failureMessage = "Win threshold was able to be decreased despite the game being a draw";
        assertEquals(current, updated, failureMessage);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8})
    void invalidIncreaseGameDrawn(int size) {
        this.reduceBoardByN(size);
        this.increaseThresholdByN(10);
        this.makeGameADraw();
        int current = model.getWinThreshold();
        controller.increaseWinThreshold();
        int updated = model.getWinThreshold();
        String failureMessage = "Win threshold was able to be increased despite the game being a draw";
        assertEquals(current, updated, failureMessage);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void invalidDecreaseGameWon(int size) {
        this.reduceBoardByN(size);
        this.setupBoardWithOneNxNWinners(model.getWinThreshold());
        int current = model.getWinThreshold();
        controller.decreaseWinThreshold();
        int updated = model.getWinThreshold();
        String failureMessage = "Win threshold was able to be decreased despite the game being won";
        assertEquals(current, updated, failureMessage);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void invalidIncreaseGameWon(int size) {
        this.reduceBoardByN(size);
        this.setupBoardWithOneNxNWinners(model.getWinThreshold());
        int current = model.getWinThreshold();
        controller.increaseWinThreshold();
        int updated = model.getWinThreshold();
        String failureMessage = "Win threshold was able to be increased despite the game being won";
        assertEquals(current, updated, failureMessage);
    }

    @ParameterizedTest
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
    void invalidDecreaseGameStarted(String input) {
        this.sendCommandToController(input);
        int start = model.getWinThreshold();
        controller.decreaseWinThreshold();
        int current = model.getWinThreshold();
        String failureMessage = "Win threshold was reduced despite game having been started";
        assertEquals(start, current, failureMessage);
    }

    @ParameterizedTest
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
    void invalidIncreaseGameStarted(String input) {
        this.sendCommandToController(input);
        int start = model.getWinThreshold();
        controller.increaseWinThreshold();
        int current = model.getWinThreshold();
        String failureMessage = "Win threshold was increased despite game having been started";
        assertEquals(start, current, failureMessage);
    }


    private void increaseThresholdByN(int n) {
        for (int i = 0; i < n; i++) {
            controller.increaseWinThreshold();
        }
    }

    private void decreaseThresholdByN(int n) {
        for (int i = 0; i < n; i++) {
            controller.decreaseWinThreshold();
        }
    }

    private void reduceBoardByN(int n) {
        for (int i = 0; i < n; i++) {
            model.removeRow();
            model.removeColumn();
        }
    }

    private void setupBoardWithTwoNxNWinners(int n) {
        for (int i = 0; i < n; i++) {
            String aString = "a" + (i + 1);
            sendCommandToController(aString);
            String bString = "b" + (i + 1);
            sendCommandToController(bString);
        }
    }

    private void setupBoardWithOneNxNWinners(int n) {
        for (int i = 0; i < n; i++) {
            String aString = "a" + (i + 1);
            sendCommandToController(aString);
        }
    }

    private void makeGameADraw() {
        for (int row = 0; row < model.getNumberOfRows(); row++) {
            for (int col = 0; col < model.getNumberOfColumns(); col++) {
                char rowChar = (char) ('a' + row);
                String input = String.valueOf(rowChar) + String.valueOf(col + 1);
                this.sendCommandToController(input);
            }
        }
    }

}
