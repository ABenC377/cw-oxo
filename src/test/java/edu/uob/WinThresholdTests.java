package edu.uob;
import edu.uob.OXOMoveException.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

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
    @ValueSource(ints = {3, 4, 5, 6, 7, 8})
    void validIncreaseempty(int start) {
        this.increaseThresholdByN(start - 3);
        int startingThreshold = model.getWinThreshold();
        String invalidSetupMessage = "Starting win threshold is not as provided";
        assertEquals(startingThreshold, start, invalidSetupMessage);
        controller.increaseWinThreshold();
        int updatedThreshold = model.getWinThreshold();
        String failureMessage = "Win threshold was not increased by one";
        assertEquals(updatedThreshold, startingThreshold + 1, failureMessage);
    }

    @Test
    void invalidIncrease9to10empty() {
        this.increaseThresholdByN(6);
        int startingThreshold = model.getWinThreshold();
        String invalidSetupMessage = "Starting win threshold is not nine";
        assertEquals(startingThreshold, 9, invalidSetupMessage);
        controller.increaseWinThreshold();
        int updatedThreshold = model.getWinThreshold();
        String failureMessage = "Win threshold was increased beyond the upper limit of nine";
        assertEquals(updatedThreshold, startingThreshold, failureMessage);
    }

    @ParameterizedTest
    @ValueSource(ints = {9, 8, 7, 6, 5, 4})
    void validReductionEmpty(int start) {
        this.increaseThresholdByN(start - 3);
        int startingThreshold = model.getWinThreshold();
        String invalidSetupMessage = "Starting win threshold is not as provided";
        assertEquals(startingThreshold, start, invalidSetupMessage);
        controller.decreaseWinThreshold();
        int updatedThreshold = model.getWinThreshold();
        String failureMessage = "Win threshold was not reduced by one";
        assertEquals(updatedThreshold, startingThreshold - 1, failureMessage);
    }

    @Test
    void invalidReduction3to2empty() {
        this.decreaseThresholdByN(2);
        int startingThreshold = model.getWinThreshold();
        String invalidSetupMessage = "Starting win threshold is not three";
        assertEquals(startingThreshold, 3, invalidSetupMessage);
        controller.decreaseWinThreshold();
        int updatedThreshold = model.getWinThreshold();
        String failureMessage = "Win threshold was reduced below three";
        assertEquals(updatedThreshold, startingThreshold, failureMessage);
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 7, 6, 5, 4, 3})
    void validDecreaseOfThresholdBecauseMultipleWinners9x9(int start) {
        increaseThresholdByN(start - 2);
        int startingThreshold = model.getWinThreshold();
        String invalidSetupMessage = "Starting win threshold is not as provided";
        assertEquals(startingThreshold, start + 1, invalidSetupMessage);
        this.setupBoardWithOneNxNWinners(start);
        controller.decreaseWinThreshold();
        String failureMessage = "Win threshold was not reduced when doing so does not result in a draw";
        int updatedThreshold = model.getWinThreshold();
        assertEquals(updatedThreshold, startingThreshold - 1, failureMessage);
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 6, 5, 4, 3})
    void validDecreaseOfThresholdBecauseMultipleWinners8x8(int start) {
        increaseThresholdByN(start - 2);
        int startingThreshold = model.getWinThreshold();
        String invalidSetupMessage = "Starting win threshold is not as provided";
        assertEquals(startingThreshold, start + 1, invalidSetupMessage);
        controller.removeRow();
        controller.removeColumn();
        this.setupBoardWithOneNxNWinners(start);
        controller.decreaseWinThreshold();
        String failureMessage = "Win threshold was not reduced when doing so does not result in a draw";
        int updatedThreshold = model.getWinThreshold();
        assertEquals(updatedThreshold, startingThreshold - 1, failureMessage);
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 5, 4, 3})
    void validDecreaseOfThresholdBecauseMultipleWinners7x7(int start) {
        increaseThresholdByN(start - 2);
        int startingThreshold = model.getWinThreshold();
        String invalidSetupMessage = "Starting win threshold is not as provided";
        assertEquals(startingThreshold, start + 1, invalidSetupMessage);
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        this.setupBoardWithOneNxNWinners(start);
        controller.decreaseWinThreshold();
        String failureMessage = "Win threshold was not reduced when doing so does not result in a draw";
        int updatedThreshold = model.getWinThreshold();
        assertEquals(updatedThreshold, startingThreshold - 1, failureMessage);
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 4, 3})
    void validDecreaseOfThresholdBecauseMultipleWinners6x6(int start) {
        increaseThresholdByN(start - 2);
        int startingThreshold = model.getWinThreshold();
        String invalidSetupMessage = "Starting win threshold is not as provided";
        assertEquals(startingThreshold, start + 1, invalidSetupMessage);
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        this.setupBoardWithOneNxNWinners(start);
        controller.decreaseWinThreshold();
        String failureMessage = "Win threshold was not reduced when doing so does not result in a draw";
        int updatedThreshold = model.getWinThreshold();
        assertEquals(updatedThreshold, startingThreshold - 1, failureMessage);
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 3})
    void validDecreaseOfThresholdBecauseMultipleWinners5x5(int start) {
        increaseThresholdByN(start - 2);
        int startingThreshold = model.getWinThreshold();
        String invalidSetupMessage = "Starting win threshold is not as provided";
        assertEquals(startingThreshold, start + 1, invalidSetupMessage);
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        this.setupBoardWithOneNxNWinners(start);
        controller.decreaseWinThreshold();
        String failureMessage = "Win threshold was not reduced when doing so does not result in a draw";
        int updatedThreshold = model.getWinThreshold();
        assertEquals(updatedThreshold, startingThreshold - 1, failureMessage);
    }

    @ParameterizedTest
    @ValueSource(ints = {3})
    void validDecreaseOfThresholdBecauseMultipleWinners4x4(int start) {
        increaseThresholdByN(start - 2);
        int startingThreshold = model.getWinThreshold();
        String invalidSetupMessage = "Starting win threshold is not as provided";
        assertEquals(startingThreshold, start + 1, invalidSetupMessage);
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        this.setupBoardWithOneNxNWinners(start);
        controller.decreaseWinThreshold();
        String failureMessage = "Win threshold was not reduced when doing so does not result in a draw";
        int updatedThreshold = model.getWinThreshold();
        assertEquals(updatedThreshold, startingThreshold - 1, failureMessage);
    }


    @ParameterizedTest
    @ValueSource(ints = {8, 7, 6, 5, 4, 3})
    void invalidDecreaseOfThresholdBecauseMultipleWinners9x9(int start) {
        increaseThresholdByN(start - 2);
        int startingThreshold = model.getWinThreshold();
        String invalidSetupMessage = "Starting win threshold is not as provided";
        assertEquals(startingThreshold, start + 1, invalidSetupMessage);
        this.setupBoardWithTwoNxNWinners(start);
        controller.decreaseWinThreshold();
        String failureMessage = "Win threshold was reduced when doing so results in a draw";
        int updatedThreshold = model.getWinThreshold();
        assertEquals(updatedThreshold, startingThreshold, failureMessage);
    }


    @ParameterizedTest
    @ValueSource(ints = {7, 6, 5, 4, 3})
    void invalidDecreaseOfThresholdBecauseMultipleWinners8x8(int start) {
        increaseThresholdByN(start - 2);
        int startingThreshold = model.getWinThreshold();
        String invalidSetupMessage = "Starting win threshold is not as provided";
        assertEquals(startingThreshold, start + 1, invalidSetupMessage);
        controller.removeRow();
        controller.removeColumn();
        this.setupBoardWithTwoNxNWinners(start);
        controller.decreaseWinThreshold();
        String failureMessage = "Win threshold was reduced when doing so results in a draw";
        int updatedThreshold = model.getWinThreshold();
        assertEquals(updatedThreshold, startingThreshold, failureMessage);
    }


    @ParameterizedTest
    @ValueSource(ints = {6, 5, 4, 3})
    void invalidDecreaseOfThresholdBecauseMultipleWinners7x7(int start) {
        increaseThresholdByN(start - 2);
        int startingThreshold = model.getWinThreshold();
        String invalidSetupMessage = "Starting win threshold is not as provided";
        assertEquals(startingThreshold, start + 1, invalidSetupMessage);
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        this.setupBoardWithTwoNxNWinners(start);
        controller.decreaseWinThreshold();
        String failureMessage = "Win threshold was reduced when doing so results in a draw";
        int updatedThreshold = model.getWinThreshold();
        assertEquals(updatedThreshold, startingThreshold, failureMessage);
    }


    @ParameterizedTest
    @ValueSource(ints = {5, 4, 3})
    void invalidDecreaseOfThresholdBecauseMultipleWinners6x6(int start) {
        increaseThresholdByN(start - 2);
        int startingThreshold = model.getWinThreshold();
        String invalidSetupMessage = "Starting win threshold is not as provided";
        assertEquals(startingThreshold, start + 1, invalidSetupMessage);
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        this.setupBoardWithTwoNxNWinners(start);
        controller.decreaseWinThreshold();
        String failureMessage = "Win threshold was reduced when doing so results in a draw";
        int updatedThreshold = model.getWinThreshold();
        assertEquals(updatedThreshold, startingThreshold, failureMessage);
    }


    @ParameterizedTest
    @ValueSource(ints = {4, 3})
    void invalidDecreaseOfThresholdBecauseMultipleWinners5x5(int start) {
        increaseThresholdByN(start - 2);
        int startingThreshold = model.getWinThreshold();
        String invalidSetupMessage = "Starting win threshold is not as provided";
        assertEquals(startingThreshold, start + 1, invalidSetupMessage);
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        this.setupBoardWithTwoNxNWinners(start);
        controller.decreaseWinThreshold();
        String failureMessage = "Win threshold was reduced when doing so results in a draw";
        int updatedThreshold = model.getWinThreshold();
        assertEquals(updatedThreshold, startingThreshold, failureMessage);
    }


    @ParameterizedTest
    @ValueSource(ints = {3})
    void invalidDecreaseOfThresholdBecauseMultipleWinners4x4(int start) {
        increaseThresholdByN(start - 2);
        int startingThreshold = model.getWinThreshold();
        String invalidSetupMessage = "Starting win threshold is not as provided";
        assertEquals(startingThreshold, start + 1, invalidSetupMessage);
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        this.setupBoardWithTwoNxNWinners(start);
        controller.decreaseWinThreshold();
        String failureMessage = "Win threshold was reduced when doing so results in a draw";
        int updatedThreshold = model.getWinThreshold();
        assertEquals(updatedThreshold, startingThreshold, failureMessage);
    }


    private void increaseThresholdByN(int n) {
        for (int i = 0; i < n; i++) {
            controller.increaseWinThreshold();;
        }
    }

    private void decreaseThresholdByN(int n) {
        for (int i = 0; i < n; i++) {
            controller.decreaseWinThreshold();
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
}
