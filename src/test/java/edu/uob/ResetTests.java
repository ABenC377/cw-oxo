package edu.uob;

import edu.uob.OXOMoveException.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class ResetTests {
    private OXOModel model;
    private OXOController controller;
    @BeforeEach
    void setup() {
        model = new OXOModel(3, 3, 3);
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
    @Test
    void resetMakesNextMoveXs() throws OXOMoveException {
        sendCommandToController("a1"); // First player
        String invalidSetupComment = "Making a move did not change player turn and fill tile";
        assertEquals(model.getCurrentPlayerNumber(), 1, invalidSetupComment);
        assertEquals(model.getCellOwner(0, 0), model.getPlayerByNumber(0), invalidSetupComment);
        controller.reset();
        String failedTestComment = "Reset failed to make it player 1's go";
        assertEquals(model.getCurrentPlayerNumber(), 0, failedTestComment);
    }
    @Test
    void resetClears3X300() throws OXOMoveException {
        sendCommandToController("a1"); // First player
        String invalidSetupComment = "Making a move did not change player turn and fill tile";
        assertEquals(model.getCurrentPlayerNumber(), 1, invalidSetupComment);
        assertEquals(model.getCellOwner(0, 0), model.getPlayerByNumber(0), invalidSetupComment);
        controller.reset();
        String failedTestComment = "Reset failed to clear tile a1";
        assertEquals(model.getCellOwner(0, 0), null, failedTestComment);
    }
    @Test
    void resetClears3X301() throws OXOMoveException {
        sendCommandToController("a2"); // First player
        String invalidSetupComment = "Making a move did not change player turn and fill tile";
        assertEquals(model.getCurrentPlayerNumber(), 1, invalidSetupComment);
        assertEquals(model.getCellOwner(0, 1), model.getPlayerByNumber(0), invalidSetupComment);
        controller.reset();
        String failedTestComment = "Reset failed to clear tile a2";
        assertEquals(model.getCellOwner(0, 1), null, failedTestComment);
    }
    @Test
    void resetClears3X302() throws OXOMoveException {
        sendCommandToController("a3"); // First player
        String invalidSetupComment = "Making a move did not change player turn and fill tile";
        assertEquals(model.getCurrentPlayerNumber(), 1, invalidSetupComment);
        assertEquals(model.getCellOwner(0, 2), model.getPlayerByNumber(0), invalidSetupComment);
        controller.reset();
        String failedTestComment = "Reset failed to clear tile a3";
        assertEquals(model.getCellOwner(0, 2), null, failedTestComment);
    }
    @Test
    void resetClears3X310() throws OXOMoveException {
        sendCommandToController("b1"); // First player
        String invalidSetupComment = "Making a move did not change player turn and fill tile";
        assertEquals(model.getCurrentPlayerNumber(), 1, invalidSetupComment);
        assertEquals(model.getCellOwner(1, 0), model.getPlayerByNumber(0), invalidSetupComment);
        controller.reset();
        String failedTestComment = "Reset failed to clear tile b1";
        assertEquals(model.getCellOwner(1, 0), null, failedTestComment);
    }
    @Test
    void resetClears3X311() throws OXOMoveException {
        sendCommandToController("b2"); // First player
        String invalidSetupComment = "Making a move did not change player turn and fill tile";
        assertEquals(model.getCurrentPlayerNumber(), 1, invalidSetupComment);
        assertEquals(model.getCellOwner(1, 1), model.getPlayerByNumber(0), invalidSetupComment);
        controller.reset();
        String failedTestComment = "Reset failed to clear tile b2";
        assertEquals(model.getCellOwner(1, 1), null, failedTestComment);
    }
    @Test
    void resetClears3X312() throws OXOMoveException {
        sendCommandToController("b3"); // First player
        String invalidSetupComment = "Making a move did not change player turn and fill tile";
        assertEquals(model.getCurrentPlayerNumber(), 1, invalidSetupComment);
        assertEquals(model.getCellOwner(1, 2), model.getPlayerByNumber(0), invalidSetupComment);
        controller.reset();
        String failedTestComment = "Reset failed to clear tile b3";
        assertEquals(model.getCellOwner(1, 2), null, failedTestComment);
    }
    @Test
    void resetClears3X320() throws OXOMoveException {
        sendCommandToController("c1"); // First player
        String invalidSetupComment = "Making a move did not change player turn and fill tile";
        assertEquals(model.getCurrentPlayerNumber(), 1, invalidSetupComment);
        assertEquals(model.getCellOwner(2, 0), model.getPlayerByNumber(0), invalidSetupComment);
        controller.reset();
        String failedTestComment = "Reset failed to clear tile c1";
        assertEquals(model.getCellOwner(2, 0), null, failedTestComment);
    }
    @Test
    void resetClears3X321() throws OXOMoveException {
        sendCommandToController("c2"); // First player
        String invalidSetupComment = "Making a move did not change player turn and fill tile";
        assertEquals(model.getCurrentPlayerNumber(), 1, invalidSetupComment);
        assertEquals(model.getCellOwner(2, 1), model.getPlayerByNumber(0), invalidSetupComment);
        controller.reset();
        String failedTestComment = "Reset failed to clear tile c2";
        assertEquals(model.getCellOwner(2, 1), null, failedTestComment);
    }
    @Test
    void resetClears3X322() {
        sendCommandToController("c3"); // First player
        String invalidSetupComment = "Making a move did not change player turn and fill tile";
        assertEquals(model.getCurrentPlayerNumber(), 1, invalidSetupComment);
        assertEquals(model.getCellOwner(2, 2), model.getPlayerByNumber(0), invalidSetupComment);
        controller.reset();
        String failedTestComment = "Reset failed to clear tile c3";
        assertEquals(model.getCellOwner(2, 2), null, failedTestComment);
    }
}
