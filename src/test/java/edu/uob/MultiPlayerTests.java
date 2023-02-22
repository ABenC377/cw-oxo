package edu.uob;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class MultiPlayerTests {
    private OXOModel model;
    private OXOController controller;
    private final String[][] winners = {{"e1", "f1", "g1"}, {"h1", "h2", "h3"}, {"i1", "i2", "i3"},
            {"e2", "f3", "g4"}, {"f2", "g3", "h4"}, {"e4", "f4", "g4"}};
    private final String[] losers1 = {"a1", "b1", "c1", "d1", "a2", "b2", "c2", "d2",
            "a3", "b3", "c3", "d3", "a4", "b4", "c4", "d4", "a5", "b5", "c5", "d5"};
    private final String[] losers2 = {"a6", "b6", "c6", "d6", "e6", "a7", "b7", "c7",
            "d7", "e7", "a8", "b8", "c8", "d8", "e8", "a9", "b9", "c9", "d9", "e9"};
    private final String[] losers3 = {"f5", "g5", "h5", "i5", "f6", "g6", "h6", "i6",
            "f7", "g7", "h7", "i7", "f8", "g8", "h8", "i8", "f9", "g9", "h9", "i9"};
    @BeforeEach
    void Setup() {
        model = new OXOModel(9, 9, 3);
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

    @DisplayName("Testing that players can be added")
    @ParameterizedTest(name = "{displayName} {arguments} times")
    @MethodSource("intProvider")
    void canAddNPlayers(int n) {
        int start = model.getNumberOfPlayers();
        this.addNPlayers(n);
        int current = model.getNumberOfPlayers();
        String failureMessage = "Adding " + n + " players to the model did not update the number of players to " + (n + 2);
        assertEquals(start + n, current, failureMessage);
    }

    private void addNPlayers(int n) {
        for (int i = 0; i < n; i++) {
            char c = 'M';
            model.addPlayer(new OXOPlayer(c));
        }
    }

    @DisplayName("Testing that games can reach a win state correctly with more than two players")
    @ParameterizedTest(name = "{displayName} ({arguments} players)")
    @MethodSource("playerProvider")
    void testGamePlay4Players(int additionalPlayers) {
        int newPlayers = (additionalPlayers % 18);
        addNPlayers(newPlayers);
        int total = newPlayers + 2;
        int winner = (int)(Math.random() * total);
        String[] winningMoves = winners[(int)(Math.random() * 6)];

        for (int i = 0; i < total; i++) {
            if (i == winner) {
                sendCommandToController(winningMoves[0]);
            } else {
                sendCommandToController(losers1[i]);
            }
        }
        for (int i = 0; i < total; i++) {
            if (i == winner) {
                sendCommandToController(winningMoves[1]);
            } else {
                sendCommandToController(losers2[i]);
            }
        }
        boolean cont = true;
        for (int i = 0; i < total; i++) {
            if (i == winner) {
                sendCommandToController(winningMoves[2]);
                cont = false;
            } else {
                if (cont) {
                    sendCommandToController(losers3[i]);
                }
            }
        }
        String failureMessage = "making three in a row does not result in a win state with more than two players.  Winning line was "
                 + winningMoves[0] + "->" + winningMoves[1] + "->" + winningMoves[2];
        assertNotNull(model.getWinner(), failureMessage);
    }

    static Stream<Integer> intProvider() {
        return IntStream.rangeClosed(1, 150).boxed();
    }
    static Stream<Integer> playerProvider() {
        return IntStream.rangeClosed(1, 720).boxed();
    }
}
