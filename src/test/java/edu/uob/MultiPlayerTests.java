package edu.uob;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class MultiPlayerTests {
    private OXOModel model;
    @BeforeEach
    void Setup() {
        model = new OXOModel(9, 9, 3);
        model.addPlayer(new OXOPlayer('X'));
        model.addPlayer(new OXOPlayer('O'));
    }

    @DisplayName("Testing that players can be added")
    @ParameterizedTest(name = "{displayName} {arguments} times")
    @MethodSource("intProvider")
    void canAddNPlayers(int n) {
        int start = model.getNumberOfPlayers();
        this.addNPlayers(n);
        int current = model.getNumberOfPlayers();
        String failureMessage = "Adding " + n + " players to the model did not update the number of players to 2 + " + n;
        assertEquals(start + n, current, failureMessage);
    }

    @DisplayName("Testing that players can be removed")
    @ParameterizedTest(name = "{displayName} {arguments} times")
    @MethodSource("intProvider")
    void canRemoveNPlayers(int n) {
        this.addNPlayers(81);
        int start = model.getNumberOfPlayers();
        this.removeNPlayers(n);
        int current = model.getNumberOfPlayers();
        String failureMessage = "removing " + n + " players to the model did not update the number of players to 200 - " + n;
        assertEquals(start - n, current, failureMessage);
    }

    @DisplayName("Testing that you cannot get to zero players")
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
