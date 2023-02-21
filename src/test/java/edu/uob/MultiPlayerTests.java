package edu.uob;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class MultiPlayerTests {
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
    void canAddNPlayers(int n) {
        int start = model.getNumberOfPlayers();
        this.addNPlayers(n);
        int current = model.getNumberOfPlayers();
        String failureMessage = "Adding " + n + " players to the model did not update the number of players to 2 + " + n;
        assertEquals(start + n, current, failureMessage);
    }
    @ParameterizedTest
    @MethodSource("intProvider")
    void canRemoveNPlayers(int n) {
        this.addNPlayers(81);
        int start = model.getNumberOfPlayers();
        this.removeNPlayers(n);
        int current = model.getNumberOfPlayers();
        String failureMessage = "removing " + n + " players to the model did not update the number of players to 200 - " + n;
        assertEquals(start - n, current, failureMessage);
    }

    @Test
    void cannotGetZeroPlayers() {
        this.removeNPlayers(2);
        int start = model.getNumberOfPlayers();
        model.removePlayer();
        int current = model.getNumberOfPlayers();
        String failureMessage = "Number of players was reduced below zero, which is clearly bonkers";
        assertEquals(start, current, failureMessage);
    }

    private void addNPlayers(int n) {
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            char c = (char)(r.nextInt(26) + 'A');
            model.addPlayer(new OXOPlayer(c));
        }
    }

    private void removeNPlayers(int n) {
        for (int i = 0; i < n; i++) {
            model.removePlayer();
        }
    }

    static Stream<Integer> intProvider() {
        return IntStream.rangeClosed(1, 81).boxed();
    }
}
