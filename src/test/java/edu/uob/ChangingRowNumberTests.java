package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class ChangingRowNumberTests {
    private OXOModel model;
    private OXOController controller;

    // Make a new "standard" (3x3) board before running each test case (i.e. this method runs before every `@Test` method)
    // In order to test boards of different sizes, winning thresholds or number of players, create a separate test file (without this method in it !)
    @BeforeEach
    void setup() {
        model = new OXOModel(3, 3, 3);
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
    // Testing the addRow() method in the controller.  Should add a row until you reach 9.
    // Then it should do nothing.
    @Test
    void addRowTo4Works() {
        controller.addRow();
        String failedTestComment = "Adding row to three-row board does not result in a four-row board";
        assertEquals(model.getNumberOfRows(), 4, failedTestComment);
    }
    @Test
    void addRowTo5Works() {
        controller.addRow();
        controller.addRow();
        String failedTestComment = "Adding row to four-row board does not result in a five-row board";
        assertEquals(model.getNumberOfRows(), 5, failedTestComment);
    }
    @Test
    void addRowTo6Works() {
        controller.addRow();
        controller.addRow();
        controller.addRow();
        String failedTestComment = "Adding row to five-row board does not result in a six-row board";
        assertEquals(model.getNumberOfRows(), 6, failedTestComment);
    }
    @Test
    void addRowTo7Works() {
        controller.addRow();
        controller.addRow();
        controller.addRow();
        controller.addRow();
        String failedTestComment = "Adding row to six-row board does not result in a seven-row board";
        assertEquals(model.getNumberOfRows(), 7, failedTestComment);
    }

    @Test
    void addRowTo8Works() {
        controller.addRow();
        controller.addRow();
        controller.addRow();
        controller.addRow();
        controller.addRow();
        String failedTestComment = "Adding row to seven-row board does not result in a eight-row board";
        assertEquals(model.getNumberOfRows(), 8, failedTestComment);
    }

    @Test
    void addRowTo9Works() {
        controller.addRow();
        controller.addRow();
        controller.addRow();
        controller.addRow();
        controller.addRow();
        controller.addRow();
        String failedTestComment = "Adding row to eight-row board does not result in a nine-row board";
        assertEquals(model.getNumberOfRows(), 9, failedTestComment);
    }

    @Test
    void addRowTo10Fails() {
        controller.addRow();
        controller.addRow();
        controller.addRow();
        controller.addRow();
        controller.addRow();
        controller.addRow();
        controller.addRow();
        String failedTestComment = "Adding row to nine-row board results in a ten-row board (which it shouldn't)";
        assertEquals(model.getNumberOfRows(), 9, failedTestComment);
    }
    @Test
    void removeRowTo8Works() {
        controller.addRow();
        controller.addRow();
        controller.addRow();
        controller.addRow();
        controller.addRow();
        controller.addRow();
        String failedTestComment = "Failed to get to nine rows";
        assertEquals(model.getNumberOfRows(), 9, failedTestComment);
        controller.removeRow();
        String failedToRemoveString = "Removing row from nine-row board does not result in an eight-row board";
        assertEquals(model.getNumberOfRows(), 8, failedToRemoveString);
    }
    @Test
    void removeRowTo7Works() {
        controller.addRow();
        controller.addRow();
        controller.addRow();
        controller.addRow();
        controller.addRow();
        String failedTestComment = "Failed to get to eight rows";
        assertEquals(model.getNumberOfRows(), 8, failedTestComment);
        controller.removeRow();
        String failedToRemoveString = "Removing row from eight-row board does not result in a seven-row board";
        assertEquals(model.getNumberOfRows(), 7, failedToRemoveString);
    }

    @Test
    void removeRowTo6Works() {
        controller.addRow();
        controller.addRow();
        controller.addRow();
        controller.addRow();
        String failedTestComment = "Failed to get to seven rows";
        assertEquals(model.getNumberOfRows(), 7, failedTestComment);
        controller.removeRow();
        String failedToRemoveString = "Removing row from seven-row board does not result in a six-row board";
        assertEquals(model.getNumberOfRows(), 6, failedToRemoveString);
    }
    @Test
    void removeRowTo5Works() {
        controller.addRow();
        controller.addRow();
        controller.addRow();
        String failedTestComment = "Failed to get to six rows";
        assertEquals(model.getNumberOfRows(), 6, failedTestComment);
        controller.removeRow();
        String failedToRemoveString = "Removing row from six-row board does not result in a five-row board";
        assertEquals(model.getNumberOfRows(), 5, failedToRemoveString);
    }
    @Test
    void removeRowTo4Works() {
        controller.addRow();
        controller.addRow();
        String failedTestComment = "Failed to get to five rows";
        assertEquals(model.getNumberOfRows(), 5, failedTestComment);
        controller.removeRow();
        String failedToRemoveString = "Removing row from five-row board does not result in a four-row board";
        assertEquals(model.getNumberOfRows(), 4, failedToRemoveString);
    }
    @Test
    void removeRowTo3Works() {
        controller.addRow();
        String failedTestComment = "Failed to get to four rows";
        assertEquals(model.getNumberOfRows(), 4, failedTestComment);
        controller.removeRow();
        String failedToRemoveString = "Removing row from four-row board does not result in a three-row board";
        assertEquals(model.getNumberOfRows(), 3, failedToRemoveString);
    }
    @Test
    void removeRowTo2Works() {
        controller.removeRow();
        String failedToRemoveString = "Removing row from three-row board does not result in a two-row board";
        assertEquals(model.getNumberOfRows(), 2, failedToRemoveString);
    }
    @Test
    void removeRowTo1Works() {
        controller.removeRow();
        controller.removeRow();
        String failedToRemoveString = "Removing row from two-row board does not result in a one-row board";
        assertEquals(model.getNumberOfRows(), 1, failedToRemoveString);
    }
    @Test
    void removeRowTo0Fails() {
        controller.removeRow();
        controller.removeRow();
        controller.removeRow();
        String failedToRemoveString = "Removing row from one-row board does not keep the board at one row";
        assertEquals(model.getNumberOfRows(), 1, failedToRemoveString);
    }


    @ParameterizedTest
    @ValueSource(strings = {"a1", "A1", "a2", "A2", "a3", "A3", "a4", "A4", "a5", "A5", "a6", "A6", "a7", "A7", "a8", "A8", "a9", "A9"})
    void removeOccupiedRow1x9(String input) {
        this.addNColumns(6);
        this.removeNRows(2);
        int start = model.getNumberOfRows();
        sendCommandToController(input);
        controller.removeRow();
        int current = model.getNumberOfRows();
        String failureMessage = "controller.removeRow() removed a row despite it being occupied";
        assertEquals(start, current, failureMessage);
    }
    @ParameterizedTest
    @ValueSource(strings = {"b1", "B1", "b2", "B2", "b3", "B3", "b4", "B4", "b5", "B5", "b6", "B6", "b7", "B7", "b8", "B8", "b9", "B9"})
    void removeOccupiedRow2x9(String input) {
        this.addNColumns(6);
        this.removeNRows(1);
        int start = model.getNumberOfRows();
        sendCommandToController(input);
        controller.removeRow();
        int current = model.getNumberOfRows();
        String failureMessage = "controller.removeRow() removed a row despite it being occupied";
        assertEquals(start, current, failureMessage);
    }
    @ParameterizedTest
    @ValueSource(strings = {"c1", "C1", "c2", "C2", "c3", "C3", "c4", "C4", "c5", "C5", "c6", "C6", "c7", "C7", "c8", "C8", "c9", "C9"})
    void removeOccupiedRow3x9(String input) {
        this.addNColumns(6);
        int start = model.getNumberOfRows();
        sendCommandToController(input);
        controller.removeRow();
        int current = model.getNumberOfRows();
        String failureMessage = "controller.removeRow() removed a row despite it being occupied";
        assertEquals(start, current, failureMessage);
    }
    @ParameterizedTest
    @ValueSource(strings = {"d1", "D1", "d2", "D2", "d3", "D3", "d4", "D4", "d5", "D5", "d6", "D6", "d7", "D7", "d8", "D8", "d9", "D9"})
    void removeOccupiedRow4x9(String input) {
        this.addNColumns(6);
        this.addNRows(1);
        int start = model.getNumberOfRows();
        sendCommandToController(input);
        controller.removeRow();
        int current = model.getNumberOfRows();
        String failureMessage = "controller.removeRow() removed a row despite it being occupied";
        assertEquals(start, current, failureMessage);
    }
    @ParameterizedTest
    @ValueSource(strings = {"e1", "E1", "e2", "E2", "e3", "E3", "e4", "E4", "e5", "E5", "e6", "E6", "e7", "E7", "e8", "E8", "e9", "E9"})
    void removeOccupiedRow5x9(String input) {
        this.addNColumns(6);
        this.addNRows(2);
        int start = model.getNumberOfRows();
        sendCommandToController(input);
        controller.removeRow();
        int current = model.getNumberOfRows();
        String failureMessage = "controller.removeRow() removed a row despite it being occupied";
        assertEquals(start, current, failureMessage);
    }
    @ParameterizedTest
    @ValueSource(strings = {"f1", "F1", "f2", "F2", "f3", "F3", "f4", "F4", "f5", "F5", "f6", "F6", "f7", "F7", "f8", "F8", "f9", "F9"})
    void removeOccupiedRow6x9(String input) {
        this.addNColumns(6);
        this.addNRows(3);
        int start = model.getNumberOfRows();
        sendCommandToController(input);
        controller.removeRow();
        int current = model.getNumberOfRows();
        String failureMessage = "controller.removeRow() removed a row despite it being occupied";
        assertEquals(start, current, failureMessage);
    }
    @ParameterizedTest
    @ValueSource(strings = {"g1", "G1", "g2", "G2", "g3", "G3", "g4", "G4", "g5", "G5", "g6", "G6", "g7", "G7", "g8", "G8", "g9", "G9"})
    void removeOccupiedRow7x9(String input) {
        this.addNColumns(6);
        this.addNRows(4);
        int start = model.getNumberOfRows();
        sendCommandToController(input);
        controller.removeRow();
        int current = model.getNumberOfRows();
        String failureMessage = "controller.removeRow() removed a row despite it being occupied";
        assertEquals(start, current, failureMessage);
    }
    @ParameterizedTest
    @ValueSource(strings = {"h1", "H1", "h2", "H2", "h3", "H3", "h4", "H4", "h5", "H5", "h6", "H6", "h7", "H7", "h8", "H8", "h9", "H9"})
    void removeOccupiedRow8x9(String input) {
        this.addNColumns(6);
        this.addNRows(5);
        int start = model.getNumberOfRows();
        sendCommandToController(input);
        controller.removeRow();
        int current = model.getNumberOfRows();
        String failureMessage = "controller.removeRow() removed a row despite it being occupied";
        assertEquals(start, current, failureMessage);
    }
    @ParameterizedTest
    @ValueSource(strings = {"i1", "I1", "i2", "I2", "i3", "I3", "i4", "I4", "i5", "I5", "i6", "I6", "i7", "I7", "i8", "I8", "i9", "I9"})
    void removeOccupiedRow9x9(String input) {
        this.addNColumns(6);
        this.addNRows(9);
        int start = model.getNumberOfRows();
        sendCommandToController(input);
        controller.removeRow();
        int current = model.getNumberOfRows();
        String failureMessage = "controller.removeRow() removed a row despite it being occupied";
        assertEquals(start, current, failureMessage);
    }

    private void addNColumns(int n) {
        for (int i = 0; i < n; i++) {
            controller.addColumn();
        }
    }
    private void addNRows(int n) {
        for (int i = 0; i < n; i++) {
            controller.addRow();
        }
    }
    private void removeNRows(int n) {
        for (int i = 0; i < n; i++) {
            controller.removeRow();
        }
    }

}
