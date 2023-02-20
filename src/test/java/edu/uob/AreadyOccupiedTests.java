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
    private final String[] validInputs = {"a1", "A1", "b1", "B1", "c1", "C1", "d1", "D1", "e1", "E1", "f1", "F1", "h1",
            "H1", "i1", "I1", "a2", "A2", "b2", "B2", "c2", "C2", "d2", "D2", "e2", "E2", "f2", "F2", "g2", "G2", "h2",
            "H2", "i2", "I2", "a3", "A3", "b3", "B3", "c3", "C3", "d3", "D3", "e3", "E3", "f3", "F3", "g3", "G3", "h3",
            "H3", "i3", "I3", "a4", "A4", "b4", "B4", "c4", "C4", "d4", "D4", "e4", "E4", "f4", "F4", "g4", "G4", "h4",
            "H4", "i4", "I4", "a5", "A5", "b5", "B5", "c5", "C5", "d5", "D5", "e5", "E5", "f5", "F5", "g5", "G5", "h5",
            "H5", "i5", "I5", "a6", "A6", "b6", "B6", "c6", "C6", "d6", "D6", "e6", "E6", "f6", "F6", "g6", "G6", "h6",
            "H6", "i6", "I6", "a7", "A7", "b7", "B7", "c7", "C7", "d7", "D7", "e7", "E7", "f7", "F7", "g7", "G7", "h7",
            "H7", "i7", "I7", "a8", "A8", "b8", "B8", "c8", "C8", "d8", "D8", "e8", "E8", "f8", "F8", "g8", "G8", "h8",
            "H8", "i8", "I8", "a9", "A9", "b9", "B9", "c9", "C9", "d9", "D9", "e9", "E9", "f9", "F9", "g9", "G9", "h9",
            "H9", "i9", "I9"};
    private final int numberOfInputs = 81;

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
     * won, and there is no option for the test to play a move at cell g1 -> therefore no chance of a draw.  Therefore,
     * we can be certain that the game will continue until there is a repeated move.
     */
    @RepeatedTest(250)
    @DisplayName("Throws a CellAlreadyTakenException when a play that has already been made is made again in a 9X9 board")
    void testRandomMoves() {
        String moveBeingPlayed = "a1";
        while (!played.contains(moveBeingPlayed)) {
            moveBeingPlayed = validInputs[(int)(Math.random() * numberOfInputs)];
            played.add(moveBeingPlayed);
            sendCommandToController(moveBeingPlayed);
        }
        String failedTestString = "Input of " + moveBeingPlayed + " has already been played, but doesn't throw a CellAlreadyTakenException";
        String finalMoveBeingPlayed = moveBeingPlayed;
        assertThrows(CellAlreadyTakenException.class, ()-> sendCommandToController(finalMoveBeingPlayed), failedTestString);
    }

}
