package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

public class ChangingColumnNumberTests {
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


    // Testing the addColumn() method in the controller.  Should add a column until you reach 9.
    // Then it should do nothing.
    @Test
    void addColumnTo4Works() throws OXOMoveException {
        controller.addColumn();
        String failedTestComment = "Adding column to three-column board does not result in a four-column board";
        assertEquals(model.getNumberOfColumns(), 4, failedTestComment);
    }

    @Test
    void addColumnTo5Works() throws OXOMoveException {
        controller.addColumn();
        controller.addColumn();
        String failedTestComment = "Adding column to four-column board does not result in a five-column board";
        assertEquals(model.getNumberOfColumns(), 5, failedTestComment);
    }

    @Test
    void addColumnTo6Works() throws OXOMoveException {
        controller.addColumn();
        controller.addColumn();
        controller.addColumn();
        String failedTestComment = "Adding column to five-column board does not result in a six-column board";
        assertEquals(model.getNumberOfColumns(), 6, failedTestComment);
    }

    @Test
    void addColumnTo7Works() throws OXOMoveException {
        controller.addColumn();
        controller.addColumn();
        controller.addColumn();
        controller.addColumn();
        String failedTestComment = "Adding column to six-column board does not result in a seven-column board";
        assertEquals(model.getNumberOfColumns(), 7, failedTestComment);
    }

    @Test
    void addColumnTo8Works() throws OXOMoveException {
        controller.addColumn();
        controller.addColumn();
        controller.addColumn();
        controller.addColumn();
        controller.addColumn();
        String failedTestComment = "Adding column to seven-column board does not result in an eight-column board";
        assertEquals(model.getNumberOfColumns(), 8, failedTestComment);
    }

    @Test
    void addColumnTo9Works() throws OXOMoveException {
        controller.addColumn();
        controller.addColumn();
        controller.addColumn();
        controller.addColumn();
        controller.addColumn();
        controller.addColumn();
        String failedTestComment = "Adding column to eight-column board does not result in a nine-column board";
        assertEquals(model.getNumberOfColumns(), 9, failedTestComment);
    }

    @Test
    void addColumnTo10Fails() throws OXOMoveException {
        controller.addColumn();
        controller.addColumn();
        controller.addColumn();
        controller.addColumn();
        controller.addColumn();
        controller.addColumn();
        controller.addColumn();
        String failedTestComment = "Adding column to nine-column board does results in a ten-column board (which it shouldn't)";
        assertEquals(model.getNumberOfColumns(), 9, failedTestComment);
    }
}
