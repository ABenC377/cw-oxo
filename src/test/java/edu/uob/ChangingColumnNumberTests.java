package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class ChangingColumnNumberTests {
    private OXOModel model;
    private OXOController controller;

    // Make a new "standard" (3x3) board before running each test case (i.e. this method runs before every `@Test` method)
    // In order to test boards of different sizes, winning thresholds or number of players, create a separate test file (without this method in it !)
    @BeforeEach
    void setup() {
        model = new OXOModel(3, 3, 10);
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


    // Testing the addColumn() method in the controller.  Should add a column until you reach 9.
    // Then it should do nothing.
    @Test
    @DisplayName("Testing that calling controller.addColumn() results in a column being added to a 3x3 board")
    void addColumnTo4Works() {
        controller.addColumn();
        String failedTestComment = "Adding column to three-column board does not result in a four-column board";
        assertEquals(model.getNumberOfColumns(), 4, failedTestComment);
    }

    @Test
    @DisplayName("Testing that calling controller.addColumn() results in a column being added to a 3x4 board")
    void addColumnTo5Works() {
        this.addNColumns(2);
        String failedTestComment = "Adding column to four-column board does not result in a five-column board";
        assertEquals(model.getNumberOfColumns(), 5, failedTestComment);
    }

    @Test
    @DisplayName("Testing that calling controller.addColumn() results in a column being added to a 3x5 board")
    void addColumnTo6Works() {
        this.addNColumns(3);
        String failedTestComment = "Adding column to five-column board does not result in a six-column board";
        assertEquals(model.getNumberOfColumns(), 6, failedTestComment);
    }

    @Test
    @DisplayName("Testing that calling controller.addColumn() results in a column being added to a 3x6 board")
    void addColumnTo7Works() {
        this.addNColumns(4);
        String failedTestComment = "Adding column to six-column board does not result in a seven-column board";
        assertEquals(model.getNumberOfColumns(), 7, failedTestComment);
    }

    @Test
    @DisplayName("Testing that calling controller.addColumn() results in a column being added to a 3x7 board")
    void addColumnTo8Works() {
        this.addNColumns(5);
        String failedTestComment = "Adding column to seven-column board does not result in an eight-column board";
        assertEquals(model.getNumberOfColumns(), 8, failedTestComment);
    }

    @Test
    @DisplayName("Testing that calling controller.addColumn() results in a column being added to a 3x8 board")
    void addColumnTo9Works() {
        this.addNColumns(6);
        String failedTestComment = "Adding column to eight-column board does not result in a nine-column board";
        assertEquals(model.getNumberOfColumns(), 9, failedTestComment);
    }

    @Test
    @DisplayName("Testing that calling controller.addColumn() does not add another column being to a 3x9 board")
    void addColumnTo10Fails() {
        this.addNColumns(7);
        String failedTestComment = "Adding column to nine-column board does results in a ten-column board (which it shouldn't)";
        assertEquals(model.getNumberOfColumns(), 9, failedTestComment);
    }

    @Test
    @DisplayName("Testing that calling controller.removeColumn() results in a column being removed from a 3x9 board")
    void removeColumnTo8Works() {
        this.addNColumns(6);
        String failedSetupComment = "Adding columns to get to the starting state for this test failed";
        assertEquals(model.getNumberOfColumns(), 9, failedSetupComment);
        controller.removeColumn();
        String failedTestComment = "Removing column from nine-column board does not result in an eight-column board";
        assertEquals(model.getNumberOfColumns(), 8, failedTestComment);
    }

    @Test
    @DisplayName("Testing that calling controller.removeColumn() results in a column being removed from a 3x8 board")
    void removeColumnTo7Works() {
        this.addNColumns(5);
        String failedSetupComment = "Adding columns to get to the starting state for this test failed";
        assertEquals(model.getNumberOfColumns(), 8, failedSetupComment);
        controller.removeColumn();
        String failedTestComment = "Removing column from eight-column board does not result in a seven-column board";
        assertEquals(model.getNumberOfColumns(), 7, failedTestComment);
    }

    @Test
    @DisplayName("Testing that calling controller.removeColumn() results in a column being removed from a 3x7 board")
    void removeColumnTo6Works() {
        this.addNColumns(4);
        String failedSetupComment = "Adding columns to get to the starting state for this test failed";
        assertEquals(model.getNumberOfColumns(), 7, failedSetupComment);
        controller.removeColumn();
        String failedTestComment = "Removing column from seven-column board does not result in a six-column board";
        assertEquals(model.getNumberOfColumns(), 6, failedTestComment);
    }

    @Test
    @DisplayName("Testing that calling controller.removeColumn() results in a column being removed from a 3x6 board")
    void removeColumnTo5Works() {
        this.addNColumns(3);
        String failedSetupComment = "Adding columns to get to the starting state for this test failed";
        assertEquals(model.getNumberOfColumns(), 6, failedSetupComment);
        controller.removeColumn();
        String failedTestComment = "Removing column from six-column board does not result in a five-column board";
        assertEquals(model.getNumberOfColumns(), 5, failedTestComment);
    }

    @Test
    @DisplayName("Testing that calling controller.removeColumn() results in a column being removed from a 3x5 board")
    void removeColumnTo4Works() {
        this.addNColumns(2);
        String failedSetupComment = "Adding columns to get to the starting state for this test failed";
        assertEquals(model.getNumberOfColumns(), 5, failedSetupComment);
        controller.removeColumn();
        String failedTestComment = "Removing column from five-column board does not result in a four-column board";
        assertEquals(model.getNumberOfColumns(), 4, failedTestComment);
    }

    @Test
    @DisplayName("Testing that calling controller.removeColumn() results in a column being removed from a 3x4 board")
    void removeColumnTo3Works() {
        controller.addColumn();
        String failedSetupComment = "Adding columns to get to the starting state for this test failed";
        assertEquals(model.getNumberOfColumns(), 4, failedSetupComment);
        controller.removeColumn();
        String failedTestComment = "Removing column from four-column board does not result in a three-column board";
        assertEquals(model.getNumberOfColumns(), 3, failedTestComment);
    }

    @Test
    @DisplayName("Testing that calling controller.removeColumn() results in a column being removed from a 3x3 board")
    void removeColumnTo2Works() {
        this.removeNColumns(1);
        String failedTestComment = "Removing column from three-column board does not result in a two-column board";
        assertEquals(model.getNumberOfColumns(), 2, failedTestComment);
    }

    @Test
    @DisplayName("Testing that calling controller.removeColumn() results in a column being removed from a 3x2 board")
    void removeColumnTo1Works() {
        this.removeNColumns(2);
        String failedTestComment = "Removing column from two-column board does not result in a one-column board";
        assertEquals(model.getNumberOfColumns(), 1, failedTestComment);
    }

    @Test
    @DisplayName("Testing that calling controller.removeColumn() does not remove another column from a 3x1 board")
    void removeColumnTo0Fails() {
        this.removeNColumns(3);
        String failedTestComment = "Removing column from one-column board does not keep the board with one column";
        assertEquals(model.getNumberOfColumns(), 1, failedTestComment);
    }

    @DisplayName("Testing that controllet.removeColumn() does not remove a column with a non-empty cell from a 9x1 board")
    @ParameterizedTest(name = "{displayName}.  {arguments} is the occupied cell")
    @ValueSource(strings = {"a1", "A1", "b1", "B1", "c1", "C1", "d1", "D1", "e1", "E1", "f1", "F1", "g1", "G1", "h1",
            "H1", "i1", "I1"})
    void removeOccupiedColumn9x1(String input) {
        this.addNRows(6);
        this.removeNColumns(2);
        int start = model.getNumberOfColumns();
        sendCommandToController(input);
        controller.removeColumn();
        int current = model.getNumberOfColumns();
        String failureMessage = "controller.removeColumn() removed a column despite it being occupied";
        assertEquals(start, current, failureMessage);
    }

    @DisplayName("Testing that controllet.removeColumn() does not remove a column with a non-empty cell from a 9x2 board")
    @ParameterizedTest(name = "{displayName}.  {arguments} is the occupied cell")
    @ValueSource(strings = {"a2", "A2", "b2", "B2", "c2", "C2", "d2", "D2", "e2", "E2", "f2", "F2", "g2", "G2", "h2",
            "H2", "i2", "I2"})
    void removeOccupiedColumn9x2(String input) {
        this.addNRows(6);
        this.removeNColumns(1);
        int start = model.getNumberOfColumns();
        sendCommandToController(input);
        controller.removeColumn();
        int current = model.getNumberOfColumns();
        String failureMessage = "controller.removeColumn() removed a column despite it being occupied";
        assertEquals(start, current, failureMessage);
    }

    @DisplayName("Testing that controllet.removeColumn() does not remove a column with a non-empty cell from a 9x3 board")
    @ParameterizedTest(name = "{displayName}.  {arguments} is the occupied cell")
    @ValueSource(strings = {"a3", "A3", "b3", "B3", "c3", "C3", "d3",
            "D3", "e3", "E3", "f3", "F3", "g3", "G3", "h3", "H3", "i3", "I3"})
    void removeOccupiedColumn9x3(String input) {
        this.addNRows(6);
        int start = model.getNumberOfColumns();
        sendCommandToController(input);
        controller.removeColumn();
        int current = model.getNumberOfColumns();
        String failureMessage = "controller.removeColumn() removed a column despite it being occupied";
        assertEquals(start, current, failureMessage);
    }

    @DisplayName("Testing that controllet.removeColumn() does not remove a column with a non-empty cell from a 9x4 board")
    @ParameterizedTest(name = "{displayName}.  {arguments} is the occupied cell")
    @ValueSource(strings = {"a4", "A4", "b4", "B4", "c4", "C4", "d4", "D4", "e4", "E4", "f4", "F4", "g4", "G4", "h4",
            "H4", "i4", "I4"})
    void removeOccupiedColumn9x4(String input) {
        this.addNRows(6);
        this.addNColumns(1);
        int start = model.getNumberOfColumns();
        sendCommandToController(input);
        controller.removeColumn();
        int current = model.getNumberOfColumns();
        String failureMessage = "controller.removeColumn() removed a column despite it being occupied";
        assertEquals(start, current, failureMessage);
    }

    @DisplayName("Testing that controllet.removeColumn() does not remove a column with a non-empty cell from a 9x5 board")
    @ParameterizedTest(name = "{displayName}.  {arguments} is the occupied cell")
    @ValueSource(strings = {"a5", "A5", "b5", "B5", "c5", "C5", "d5", "D5", "e5", "E5", "f5", "F5", "g5", "G5", "h5",
            "H5", "i5", "I5"})
    void removeOccupiedColumn9x5(String input) {
        this.addNRows(6);
        this.addNColumns(2);
        int start = model.getNumberOfColumns();
        sendCommandToController(input);
        controller.removeColumn();
        int current = model.getNumberOfColumns();
        String failureMessage = "controller.removeColumn() removed a column despite it being occupied";
        assertEquals(start, current, failureMessage);
    }

    @DisplayName("Testing that controllet.removeColumn() does not remove a column with a non-empty cell from a 9x6 board")
    @ParameterizedTest(name = "{displayName}.  {arguments} is the occupied cell")
    @ValueSource(strings = {"a6", "A6", "b6", "B6", "c6", "C6", "d6", "D6", "e6", "E6", "f6", "F6", "g6", "G6", "h6",
            "H6", "i6", "I6"})
    void removeOccupiedColumn9x6(String input) {
        this.addNRows(6);
        this.addNColumns(3);
        int start = model.getNumberOfColumns();
        sendCommandToController(input);
        controller.removeColumn();
        int current = model.getNumberOfColumns();
        String failureMessage = "controller.removeColumn() removed a column despite it being occupied";
        assertEquals(start, current, failureMessage);
    }

    @DisplayName("Testing that controllet.removeColumn() does not remove a column with a non-empty cell from a 9x7 board")
    @ParameterizedTest(name = "{displayName}.  {arguments} is the occupied cell")
    @ValueSource(strings = {"a7", "A7", "b7", "B7", "c7", "C7", "d7", "D7", "e7", "E7", "f7", "F7", "g7", "G7", "h7",
            "H7", "i7", "I7"})
    void removeOccupiedColumn9x7(String input) {
        this.addNRows(6);
        this.addNColumns(4);
        int start = model.getNumberOfColumns();
        sendCommandToController(input);
        controller.removeColumn();
        int current = model.getNumberOfColumns();
        String failureMessage = "controller.removeColumn() removed a column despite it being occupied";
        assertEquals(start, current, failureMessage);
    }

    @DisplayName("Testing that controllet.removeColumn() does not remove a column with a non-empty cell from a 9x8 board")
    @ParameterizedTest(name = "{displayName}.  {arguments} is the occupied cell")
    @ValueSource(strings = {"a8", "A8", "b8", "B8", "c8", "C8", "d8", "D8", "e8", "E8", "f8", "F8", "g8", "G8", "h8",
            "H8", "i8", "I8"})
    void removeOccupiedColumn9x8(String input) {
        this.addNRows(6);
        this.addNColumns(5);
        int start = model.getNumberOfColumns();
        sendCommandToController(input);
        controller.removeColumn();
        int current = model.getNumberOfColumns();
        String failureMessage = "controller.removeColumn() removed a column despite it being occupied";
        assertEquals(start, current, failureMessage);
    }

    @DisplayName("Testing that controllet.removeColumn() does not remove a column with a non-empty cell from a 9x9 board")
    @ParameterizedTest(name = "{displayName}.  {arguments} is the occupied cell")
    @ValueSource(strings = {"a9", "A9", "b9", "B9", "c9", "C9", "d9", "D9", "e9", "E9", "f9", "F9", "g9", "G9", "h9",
            "H9", "i9", "I9"})
    void removeOccupiedColumn9x9(String input) {
        this.addNRows(6);
        this.addNColumns(6);
        int start = model.getNumberOfColumns();
        sendCommandToController(input);
        controller.removeColumn();
        int current = model.getNumberOfColumns();
        String failureMessage = "controller.removeColumn() removed a column despite it being occupied";
        assertEquals(start, current, failureMessage);
    }

    @DisplayName("Testing that controller.removeColumn() does not remove a column if it results in a draw")
    @ParameterizedTest(name = "{displayName} on a {arguments}x9 board")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9})
    void removeToMakeDraw9Cols(int rows) {
        addNColumns(6);
        removeNRows(2);
        addNRows(rows - 1);
        this.fillCells(rows, (model.getNumberOfColumns() - 1));
        int start = model.getNumberOfColumns();
        controller.removeRow();
        int current = model.getNumberOfColumns();
        String failureMessage = "controller.removeColumn() removed a column despite it resulting in a draw state";
        assertEquals(start, current, failureMessage);
    }

    @DisplayName("Testing that controller.removeColumn() does not remove a column if it results in a draw")
    @ParameterizedTest(name = "{displayName} on a {arguments}x8 board")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9})
    void removeToMakeDraw8Cols(int rows) {
        addNColumns(5);
        removeNRows(2);
        addNRows(rows - 1);
        this.fillCells(rows, (model.getNumberOfColumns() - 1));
        int start = model.getNumberOfColumns();
        controller.removeRow();
        int current = model.getNumberOfColumns();
        String failureMessage = "controller.removeColumn() removed a column despite it resulting in a draw state";
        assertEquals(start, current, failureMessage);
    }

    @DisplayName("Testing that controller.removeColumn() does not remove a column if it results in a draw")
    @ParameterizedTest(name = "{displayName} on a {arguments}x7 board")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9})
    void removeToMakeDraw7Cols(int rows) {
        addNColumns(4);
        removeNRows(2);
        addNRows(rows - 1);
        this.fillCells(rows, (model.getNumberOfColumns() - 1));
        int start = model.getNumberOfColumns();
        controller.removeRow();
        int current = model.getNumberOfColumns();
        String failureMessage = "controller.removeColumn() removed a column despite it resulting in a draw state";
        assertEquals(start, current, failureMessage);
    }

    @DisplayName("Testing that controller.removeColumn() does not remove a column if it results in a draw")
    @ParameterizedTest(name = "{displayName} on a {arguments}x6 board")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9})
    void removeToMakeDraw6Cols(int rows) {
        addNColumns(3);
        removeNRows(2);
        addNRows(rows - 1);
        this.fillCells(rows, (model.getNumberOfColumns() - 1));
        int start = model.getNumberOfColumns();
        controller.removeRow();
        int current = model.getNumberOfColumns();
        String failureMessage = "controller.removeColumn() removed a column despite it resulting in a draw state";
        assertEquals(start, current, failureMessage);
    }

    @DisplayName("Testing that controller.removeColumn() does not remove a column if it results in a draw")
    @ParameterizedTest(name = "{displayName} on a {arguments}x5 board")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9})
    void removeToMakeDraw5Cols(int rows) {
        addNColumns(2);
        removeNRows(2);
        addNRows(rows - 1);
        this.fillCells(rows, (model.getNumberOfColumns() - 1));
        int start = model.getNumberOfColumns();
        controller.removeRow();
        int current = model.getNumberOfColumns();
        String failureMessage = "controller.removeColumn() removed a column despite it resulting in a draw state";
        assertEquals(start, current, failureMessage);
    }

    @DisplayName("Testing that controller.removeColumn() does not remove a column if it results in a draw")
    @ParameterizedTest(name = "{displayName} on a {arguments}x4 board")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9})
    void removeToMakeDraw4Cols(int rows) {
        addNColumns(1);
        removeNRows(2);
        addNRows(rows - 1);
        this.fillCells(rows, (model.getNumberOfColumns() - 1));
        int start = model.getNumberOfColumns();
        controller.removeRow();
        int current = model.getNumberOfColumns();
        String failureMessage = "controller.removeColumn() removed a column despite it resulting in a draw state";
        assertEquals(start, current, failureMessage);
    }

    @DisplayName("Testing that controller.removeColumn() does not remove a column if it results in a draw")
    @ParameterizedTest(name = "{displayName} on a {arguments}x3 board")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9})
    void removeToMakeDraw3Cols(int rows) {
        removeNRows(2);
        addNRows(rows - 1);
        this.fillCells(rows, (model.getNumberOfColumns() - 1));
        int start = model.getNumberOfColumns();
        controller.removeRow();
        int current = model.getNumberOfColumns();
        String failureMessage = "controller.removeColumn() removed a column despite it resulting in a draw state";
        assertEquals(start, current, failureMessage);
    }

    @DisplayName("Testing that controller.removeColumn() does not remove a column if it results in a draw")
    @ParameterizedTest(name = "{displayName} on a {arguments}x2 board")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9})
    void removeToMakeDraw2Cols(int rows) {
        removeNColumns(1);
        removeNRows(2);
        addNRows(rows - 1);
        this.fillCells(rows, (model.getNumberOfColumns() - 1));
        int start = model.getNumberOfColumns();
        controller.removeRow();
        int current = model.getNumberOfColumns();
        String failureMessage = "controller.removeColumn() removed a column despite it resulting in a draw state";
        assertEquals(start, current, failureMessage);
    }

    @DisplayName("Testing that controller.addColumn() when in a draw removes the draw state from the model")
    @ParameterizedTest(name = "{displayName} in a {arguments}x1 board")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9})
    void addColumnToEscapeDrawTest1Col(int rows) {
        this.removeNColumns(2);
        this.removeNRows(2);
        this.addNRows(rows - 1);
        this.fillCells(model.getNumberOfRows(), model.getNumberOfColumns());
        String setupFailureMessage = "Game is not in a drawn state despite all cells being occupied";
        assertTrue(model.isGameDrawn(), setupFailureMessage);
        controller.addColumn();
        String failureMessage = "Adding a column to a draw state does not make the game undrawn";
        assertFalse(model.isGameDrawn(), failureMessage);
    }

    @DisplayName("Testing that controller.addColumn() when in a draw removes the draw state from the model")
    @ParameterizedTest(name = "{displayName} in a {arguments}x2 board")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9})
    void addColumnToEscapeDrawTest2Col(int rows) {
        this.removeNColumns(1);
        this.removeNRows(2);
        this.addNRows(rows - 1);
        this.fillCells(model.getNumberOfRows(), model.getNumberOfColumns());
        String setupFailureMessage = "Game is not in a drawn state despite all cells being occupied";
        assertTrue(model.isGameDrawn(), setupFailureMessage);
        controller.addColumn();
        String failureMessage = "Adding a column to a draw state does not make the game undrawn";
        assertFalse(model.isGameDrawn(), failureMessage);
    }

    @DisplayName("Testing that controller.addColumn() when in a draw removes the draw state from the model")
    @ParameterizedTest(name = "{displayName} in a {arguments}x3 board")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9})
    void addColumnToEscapeDrawTest3Col(int rows) {
        this.removeNRows(2);
        this.addNRows(rows - 1);
        this.fillCells(model.getNumberOfRows(), model.getNumberOfColumns());
        String setupFailureMessage = "Game is not in a drawn state despite all cells being occupied";
        assertTrue(model.isGameDrawn(), setupFailureMessage);
        controller.addColumn();
        String failureMessage = "Adding a column to a draw state does not make the game undrawn";
        assertFalse(model.isGameDrawn(), failureMessage);
    }

    @DisplayName("Testing that controller.addColumn() when in a draw removes the draw state from the model")
    @ParameterizedTest(name = "{displayName} in a {arguments}x4 board")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9})
    void addColumnToEscapeDrawTest4Col(int rows) {
        this.addNColumns(1);
        this.removeNRows(2);
        this.addNRows(rows - 1);
        this.fillCells(model.getNumberOfRows(), model.getNumberOfColumns());
        String setupFailureMessage = "Game is not in a drawn state despite all cells being occupied";
        assertTrue(model.isGameDrawn(), setupFailureMessage);
        controller.addColumn();
        String failureMessage = "Adding a column to a draw state does not make the game undrawn";
        assertFalse(model.isGameDrawn(), failureMessage);
    }

    @DisplayName("Testing that controller.addColumn() when in a draw removes the draw state from the model")
    @ParameterizedTest(name = "{displayName} in a {arguments}x5 board")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9})
    void addColumnToEscapeDrawTest5Col(int rows) {
        this.addNColumns(2);
        this.removeNRows(2);
        this.addNRows(rows - 1);
        this.fillCells(model.getNumberOfRows(), model.getNumberOfColumns());
        String setupFailureMessage = "Game is not in a drawn state despite all cells being occupied";
        assertTrue(model.isGameDrawn(), setupFailureMessage);
        controller.addColumn();
        String failureMessage = "Adding a column to a draw state does not make the game undrawn";
        assertFalse(model.isGameDrawn(), failureMessage);
    }

    @DisplayName("Testing that controller.addColumn() when in a draw removes the draw state from the model")
    @ParameterizedTest(name = "{displayName} in a {arguments}x6 board")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9})
    void addColumnToEscapeDrawTest6Col(int rows) {
        this.addNColumns(3);
        this.removeNRows(2);
        this.addNRows(rows - 1);
        this.fillCells(model.getNumberOfRows(), model.getNumberOfColumns());
        String setupFailureMessage = "Game is not in a drawn state despite all cells being occupied";
        assertTrue(model.isGameDrawn(), setupFailureMessage);
        controller.addColumn();
        String failureMessage = "Adding a column to a draw state does not make the game undrawn";
        assertFalse(model.isGameDrawn(), failureMessage);
    }

    @DisplayName("Testing that controller.addColumn() when in a draw removes the draw state from the model")
    @ParameterizedTest(name = "{displayName} in a {arguments}x7 board")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9})
    void addColumnToEscapeDrawTest7Col(int rows) {
        this.addNColumns(4);
        this.removeNRows(2);
        this.addNRows(rows - 1);
        this.fillCells(model.getNumberOfRows(), model.getNumberOfColumns());
        String setupFailureMessage = "Game is not in a drawn state despite all cells being occupied";
        assertTrue(model.isGameDrawn(), setupFailureMessage);
        controller.addColumn();
        String failureMessage = "Adding a column to a draw state does not make the game undrawn";
        assertFalse(model.isGameDrawn(), failureMessage);
    }

    @DisplayName("Testing that controller.addColumn() when in a draw removes the draw state from the model")
    @ParameterizedTest(name = "{displayName} in a {arguments}x8 board")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9})
    void addColumnToEscapeDrawTest8Col(int rows) {
        this.addNColumns(5);
        this.removeNRows(2);
        this.addNRows(rows - 1);
        this.fillCells(model.getNumberOfRows(), model.getNumberOfColumns());
        String setupFailureMessage = "Game is not in a drawn state despite all cells being occupied";
        assertTrue(model.isGameDrawn(), setupFailureMessage);
        controller.addColumn();
        String failureMessage = "Adding a column to a draw state does not make the game undrawn";
        assertFalse(model.isGameDrawn(), failureMessage);
    }


    private void addNColumns(int n) {
        for (int i = 0; i < n; i++) {
            controller.addColumn();
        }
    }
    private void removeNColumns(int n) {
        for (int i = 0; i < n; i++) {
            controller.removeColumn();
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
    private void fillCells(int row, int col) {
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                String command = String.valueOf((char)('a' + r)) + (c + 1);
                sendCommandToController(command);
            }
        }
    }
}
