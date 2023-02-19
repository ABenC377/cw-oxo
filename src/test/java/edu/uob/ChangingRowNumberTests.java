package edu.uob;

import edu.uob.OXOMoveException.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    void addRowTo4Works() throws OXOMoveException {
        controller.addRow();
        String failedTestComment = "Adding row to three-row board does not result in a four-row board";
        assertEquals(model.getNumberOfRows(), 4, failedTestComment);
    }
    @Test
    void addRowTo5Works() throws OXOMoveException {
        controller.addRow();
        controller.addRow();
        String failedTestComment = "Adding row to four-row board does not result in a five-row board";
        assertEquals(model.getNumberOfRows(), 5, failedTestComment);
    }
    @Test
    void addRowTo6Works() throws OXOMoveException {
        controller.addRow();
        controller.addRow();
        controller.addRow();
        String failedTestComment = "Adding row to five-row board does not result in a six-row board";
        assertEquals(model.getNumberOfRows(), 6, failedTestComment);
    }
    @Test
    void addRowTo7Works() throws OXOMoveException {
        controller.addRow();
        controller.addRow();
        controller.addRow();
        controller.addRow();
        String failedTestComment = "Adding row to six-row board does not result in a seven-row board";
        assertEquals(model.getNumberOfRows(), 7, failedTestComment);
    }

    @Test
    void addRowTo8Works() throws OXOMoveException {
        controller.addRow();
        controller.addRow();
        controller.addRow();
        controller.addRow();
        controller.addRow();
        String failedTestComment = "Adding row to seven-row board does not result in a eight-row board";
        assertEquals(model.getNumberOfRows(), 8, failedTestComment);
    }

    @Test
    void addRowTo9Works() throws OXOMoveException {
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
    void addRowTo10Fails() throws OXOMoveException {
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
    void removeRowTo8Works() throws OXOMoveException {
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
    void removeRowTo7Works() throws OXOMoveException {
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
    void removeRowTo6Works() throws OXOMoveException {
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
    void removeRowTo5Works() throws OXOMoveException {
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
    void removeRowTo4Works() throws OXOMoveException {
        controller.addRow();
        controller.addRow();
        String failedTestComment = "Failed to get to five rows";
        assertEquals(model.getNumberOfRows(), 5, failedTestComment);
        controller.removeRow();
        String failedToRemoveString = "Removing row from five-row board does not result in a four-row board";
        assertEquals(model.getNumberOfRows(), 4, failedToRemoveString);
    }
    @Test
    void removeRowTo3Works() throws OXOMoveException {
        controller.addRow();
        String failedTestComment = "Failed to get to four rows";
        assertEquals(model.getNumberOfRows(), 4, failedTestComment);
        controller.removeRow();
        String failedToRemoveString = "Removing row from four-row board does not result in a three-row board";
        assertEquals(model.getNumberOfRows(), 3, failedToRemoveString);
    }
    @Test
    void removeRowTo2Works() throws OXOMoveException {
        controller.removeRow();
        String failedToRemoveString = "Removing row from three-row board does not result in a two-row board";
        assertEquals(model.getNumberOfRows(), 2, failedToRemoveString);
    }
    @Test
    void removeRowTo1Works() throws OXOMoveException {
        controller.removeRow();
        controller.removeRow();
        String failedToRemoveString = "Removing row from two-row board does not result in a one-row board";
        assertEquals(model.getNumberOfRows(), 1, failedToRemoveString);
    }
    @Test
    void removeRowTo0Fails() throws OXOMoveException {
        controller.removeRow();
        controller.removeRow();
        controller.removeRow();
        String failedToRemoveString = "Removing row from one-row board does not keep the board at one row";
        assertEquals(model.getNumberOfRows(), 1, failedToRemoveString);
    }
}
