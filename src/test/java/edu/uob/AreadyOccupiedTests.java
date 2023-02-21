package edu.uob;
import edu.uob.OXOMoveException.*;

import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class AreadyOccupiedTests {
    private OXOModel model;
    private OXOController controller;
    private final String[] validInputs = {"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1",
            "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2", "i2",
            "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3", "i3",
            "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4", "i4",
            "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5", "i5",
            "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6", "i6",
            "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7", "i7",
            "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8", "i8",
            "a9", "b9", "c9", "d9", "e9", "f9", "g9", "h9", "i9"};

    private Set<String> played;
    @BeforeEach
    void Setup() {
        model = new OXOModel(9, 9, 10);
        played = new HashSet<>();
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

    /*
     * This test randomly makes moves until it makes one that has already been made, and then checks
     * that the correct exception is thrown.  Because it's random it makes sense to run it a relatively large number
     * of times to improve the chances of interesting edge cases being run into.  It should never fail, so there
     * is no downside to runnning it a large number of times.
     *
     * We know that this test should never fail because the win threshold is 10, so it is impossible for the game to be
     * won, and there is no option for the test to play a move at cell i1 -> therefore no chance of a draw.  Hence,
     * we can be certain that the game will continue until there is a repeated move.
     */
    @RepeatedTest(500)
    @DisplayName("Throws a CellAlreadyTakenException when a play that has already been made is made again in a 9X9 board")
    void testRandomMoves() {
        String moveBeingPlayed = "a1";
        while (!played.contains(moveBeingPlayed)) {
            moveBeingPlayed = validInputs[(int)(Math.random() * validInputs.length)];
            played.add(moveBeingPlayed);
            sendCommandToController(moveBeingPlayed);
        }
        String failedTestString = "Input of " + moveBeingPlayed + " has already been played, but doesn't throw a CellAlreadyTakenException";
        String finalMoveBeingPlayed = moveBeingPlayed;
        assertThrows(CellAlreadyTakenException.class, ()-> sendCommandToController(finalMoveBeingPlayed), failedTestString);
    }

}
