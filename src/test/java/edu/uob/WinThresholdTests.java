package edu.uob;
import edu.uob.OXOMoveException.*;

import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class WinThresholdTests {
    private OXOModel model;
    private OXOController controller;
    @BeforeEach
    void Setup() {
        model = new OXOModel(9, 9, 5);
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

    @Test
    void validReduction5to4empty() {
        int startingThreshold = model.getWinThreshold();
        String invalidSetupMessage = "Starting win threshold is not five";
        assertEquals(startingThreshold, 5, invalidSetupMessage);
        controller.decreaseWinThreshold();
        int updatedThreshold = model.getWinThreshold();
        String failureMessage = "Win threshold was not reduced from five to four";
        assertEquals(updatedThreshold, startingThreshold - 1, failureMessage);
    }

    @Test
    void validReduction4to3empty() {
        controller.decreaseWinThreshold();
        int startingThreshold = model.getWinThreshold();
        String invalidSetupMessage = "Starting win threshold is not four";
        assertEquals(startingThreshold, 4, invalidSetupMessage);
        controller.decreaseWinThreshold();
        int updatedThreshold = model.getWinThreshold();
        String failureMessage = "Win threshold was not reduced from four to three";
        assertEquals(updatedThreshold, startingThreshold - 1, failureMessage);
    }

    @Test
    void invalidReduction3to2empty() {
        controller.decreaseWinThreshold();
        controller.decreaseWinThreshold();
        int startingThreshold = model.getWinThreshold();
        String invalidSetupMessage = "Starting win threshold is not three";
        assertEquals(startingThreshold, 3, invalidSetupMessage);
        controller.decreaseWinThreshold();
        int updatedThreshold = model.getWinThreshold();
        String failureMessage = "Win threshold was reduced below three";
        assertEquals(updatedThreshold, startingThreshold, failureMessage);
    }
}
