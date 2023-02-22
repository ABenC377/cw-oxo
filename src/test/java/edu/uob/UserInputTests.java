package edu.uob;
import edu.uob.OXOMoveException.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class UserInputTests {
    private OXOModel model;
    private OXOController controller;

    // Make a new "standard" (3x3) board before running each test case (i.e. this method runs before every `@Test` method)
    // In order to test boards of different sizes, winning thresholds or number of players, create a separate test file (without this method in it !)
    @BeforeEach
    void setup() {
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

    @DisplayName("Testing that a valid user input does not throw an exception")
    @ParameterizedTest(name = "{displayName} when the user inputs {arguments}")
    @ValueSource(strings = {"a1", "A1", "b1", "B1", "c1", "C1", "d1", "D1", "e1", "E1", "f1", "F1", "g1", "G1", "h1",
            "H1", "i1", "I1", "a2", "A2", "b2", "B2", "c2", "C2", "d2", "D2", "e2", "E2", "f2", "F2", "g2", "G2", "h2",
            "H2", "i2", "I2", "a3", "A3", "b3", "B3", "c3", "C3", "d3", "D3", "e3", "E3", "f3", "F3", "g3", "G3", "h3",
            "H3", "i3", "I3", "a4", "A4", "b4", "B4", "c4", "C4", "d4", "D4", "e4", "E4", "f4", "F4", "g4", "G4", "h4",
            "H4", "i4", "I4", "a5", "A5", "b5", "B5", "c5", "C5", "d5", "D5", "e5", "E5", "f5", "F5", "g5", "G5", "h5",
            "H5", "i5", "I5", "a6", "A6", "b6", "B6", "c6", "C6", "d6", "D6", "e6", "E6", "f6", "F6", "g6", "G6", "h6",
            "H6", "i6", "I6", "a7", "A7", "b7", "B7", "c7", "C7", "d7", "D7", "e7", "E7", "f7", "F7", "g7", "G7", "h7",
            "H7", "i7", "I7", "a8", "A8", "b8", "B8", "c8", "C8", "d8", "D8", "e8", "E8", "f8", "F8", "g8", "G8", "h8",
            "H8", "i8", "I8", "a9", "A9", "b9", "B9", "c9", "C9", "d9", "D9", "e9", "E9", "f9", "F9", "g9", "G9", "h9",
            "H9", "i9", "I9"})
    void testValidUserInputs(String inputString) {
        String failedTestString = "Valid input resulted in an OXOMoveExceoption being thrown";
        assertDoesNotThrow(()-> sendCommandToController(inputString), failedTestString);
    }

    @DisplayName("Testing that InvalidIdentifierLengthExpectation is thrown when an input of the wrong size is provided by user")
    @ParameterizedTest(name = "{displayName}.  input is {arguments}")
    @ValueSource(strings = {"a10", "", " a1", "a1 ", " A1", "A1 ", " a2", "a2 ", " A2", "A2 ", " b1", "b1 ", " b2",
            "b2 ", " B2", "B2 ", " b1", "b1 ", "    ", "a10", "aergaergaegr", "b", "1", " a1 ", " "})
    void testWrongSizeUserInputs(String inputString) {
        String failedTestString = "Input of non-two-size doesn't throw an InvalidIdentifierLengthException";
        assertThrows(InvalidIdentifierLengthException.class, ()-> sendCommandToController(inputString), failedTestString);
    }

    @DisplayName("Testing that InvalidIdentifierCharacterException is thrown when a non-letter row identifier is provided")
    @ParameterizedTest(name = "{displayName}.  input is 'unicode({arguments})1'")
    @MethodSource("letterGenerator")
    void testWrongRowInputs(int unicode) {
        String command = (char)unicode + "1";
        String failedTestString = "Input of non-letter row identifier doesn't throw an InvalidIdentifierCharacterException";
        assertThrows(InvalidIdentifierCharacterException.class, ()-> sendCommandToController(command), failedTestString);
    }

    static Stream<Integer> letterGenerator() {
        return IntStream.rangeClosed(123, 2000).boxed();
    }

    @DisplayName("Testing that InvalidIdentifierCharacterException is thrown when a non-digit column identifier is provided")
    @ParameterizedTest(name = "{displayName}.  input is {arguments}")
    @ValueSource(strings = {"a!", "a\"", "a#", "a$", "a%", "a&", "a'", "a(", "a)", "a*", "a+", "a,", "a-", "a.",
            "a/", "a:", "a;", "a<", "a=", "a>", "a?", "a@", "aA", "aB", "aC", "aD", "aE", "aF", "aG", "aH", "aI",
            "aJ", "aK", "aL", "aM", "aN", "aO", "aP", "aQ", "aR", "aS", "aT", "aU", "aV", "aW", "aX", "aY", "aZ",
            "a'", "aƒ", "a∑", "a^", "aπ", "aß", "aa", "ab", "ac", "ad", "ae", "af", "ag", "ah", "ai", "aj", "ak",
            "al", "am", "an", "ao", "ap", "aq", "ar", "as", "at", "au", "av", "aw", "ax", "ay", "az", "a¡", "a€",
            "a#", "a¢", "a∞", "a§", "a¶", "a•", "aª", "aº"})
    void testWrongColumnInputs(String inputString) {
        String failedTestString = "Input of non-digit column identifier doesn't throw an InvalidIdentifierCharacterException";
        assertThrows(InvalidIdentifierCharacterException.class, ()-> sendCommandToController(inputString), failedTestString);
    }

    @DisplayName("Testing that OutsideCellRangeException is thrown when a cell outside of a 9x9 grid is requested")
    @ParameterizedTest(name = "{displayName}.  input is {arguments}")
    @ValueSource(strings = {
            "a0", "A0", "b0", "B0", "c0", "C0", "d0", "D0", "e0", "E0", "f0", "F0", "g0", "G0", "h0", "H0", "i0", "I0",
            "j0", "J0", "k0", "K0", "l0", "L0", "m0", "M0", "n0", "N0", "o0", "O0", "p0", "P0", "q0", "Q0", "r0", "R0",
            "s0", "S0", "t0", "T0", "u0", "U0", "v0", "V0", "w0", "W0", "x0", "X0", "y0", "Y0", "z0", "Z0",
            "j1", "J1", "k1", "K1", "l1", "L1", "m1", "M1", "n1", "N1", "o1", "O1", "p1", "P1", "q1", "Q1", "r1", "R1",
            "s1", "S1", "t1", "T1", "u1", "U1", "v1", "V1", "w1", "W1", "x1", "X1", "y1", "Y1", "z1", "Z1",
            "j2", "J2", "k2", "K2", "l2", "L2", "m2", "M2", "n2", "N2", "o2", "O2", "p2", "P2", "q2", "Q2", "r2", "R2",
            "s2", "S2", "t2", "T2", "u2", "U2", "v2", "V2", "w2", "W2", "x2", "X2", "y2", "Y2", "z2", "Z2",
            "j3", "J3", "k3", "K3", "l3", "L3", "m3", "M3", "n3", "N3", "o3", "O3", "p3", "P3", "q3", "Q3", "r3", "R3",
            "s3", "S3", "t3", "T3", "u3", "U3", "v3", "V3", "w3", "W3", "x3", "X3", "y3", "Y3", "z3", "Z3",
            "j4", "J4", "k4", "K4", "l4", "L4", "m4", "M4", "n4", "N4", "o4", "O4", "p4", "P4", "q4", "Q4", "r4", "R4",
            "s4", "S4", "t4", "T4", "u4", "U4", "v4", "V4", "w4", "W4", "x4", "X4", "y4", "Y4", "z4", "Z4",
            "j5", "J5", "k5", "K5", "l5", "L5", "m5", "M5", "n5", "N5", "o5", "O5", "p5", "P5", "q5", "Q5", "r5", "R5",
            "s5", "S5", "t5", "T5", "u5", "U5", "v5", "V5", "w5", "W5", "x5", "X5", "y5", "Y5", "z5", "Z5",
            "j6", "J6", "k6", "K6", "l6", "L6", "m6", "M6", "n6", "N6", "o6", "O6", "p6", "P6", "q6", "Q6", "r6", "R6",
            "s6", "S6", "t6", "T6", "u6", "U6", "v6", "V6", "w6", "W6", "x6", "X6", "y6", "Y6", "z6", "Z6",
            "j7", "J7", "k7", "K7", "l7", "L7", "m7", "M7", "n7", "N7", "o7", "O7", "p7", "P7", "q7", "Q7", "r7", "R7",
            "s7", "S7", "t7", "T7", "u7", "U7", "v7", "V7", "w7", "W7", "x7", "X7", "y7", "Y7", "z7", "Z7",
            "j8", "J8", "k8", "K8", "l8", "L8", "m8", "M8", "n8", "N8", "o8", "O8", "p8", "P8", "q8", "Q8", "r8", "R8",
            "s8", "S8", "t8", "T8", "u8", "U8", "v8", "V8", "w8", "W8", "x8", "X8", "y8", "Y8", "z8", "Z8",
            "j9", "J9", "k9", "K9", "l9", "L9", "m9", "M9", "n9", "N9", "o9", "O9", "p9", "P9", "q9", "Q9", "r9", "R9",
            "s9", "S9", "t9", "T9", "u9", "U9", "v9", "V9", "w9", "W9", "x9", "X9", "y9", "Y9", "z9", "Z9",
            })
    void testOutsideOfBoardReference9X9(String inputString) {
        String failedTestString = "Input of a cell outside of the board doesn't throw an OutsideCellRangeException";
        assertThrows(OutsideCellRangeException.class, ()-> sendCommandToController(inputString), failedTestString);
    }

    @DisplayName("Testing that OutsideCellRangeException is thrown when a cell outside of an 8x8 grid is requested")
    @ParameterizedTest(name = "{displayName}.  input is {arguments}")
    @ValueSource(strings = {
            "i1", "I1", "i2", "I2", "i3", "I3", "i4", "I4", "i5", "I5", "i6", "I6", "i7", "I7", "i8", "I8",
            "a9", "A9", "b9", "B9", "c9", "C9", "d9", "D9", "e9", "E9", "f9", "F9", "g9", "G9", "h9", "H9", "i9", "I9",
    })
    void testOutsideOfBoardReference8X8(String inputString) {
        controller.removeColumn();
        controller.removeRow();
        String failedTestString = "Input of a cell outside of the board doesn't throw an OutsideCellRangeException";
        assertThrows(OutsideCellRangeException.class, ()-> sendCommandToController(inputString), failedTestString);
    }

    @DisplayName("Testing that OutsideCellRangeException is thrown when a cell outside of a 7x7 grid is requested")
    @ParameterizedTest(name = "{displayName}.  input is {arguments}")
    @ValueSource(strings = {
            "h1", "H1", "h2", "H2", "h3", "H3", "h4", "H4", "h5", "H5", "h6", "H6", "h7", "H7",
            "a8", "A8", "b8", "B8", "c8", "C8", "d8", "D8", "e8", "E8", "f8", "F8", "g8", "G8", "h8", "H8",
    })
    void testOutsideOfBoardReference7X7(String inputString) {
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        String failedTestString = "Input of a cell outside of the board doesn't throw an OutsideCellRangeException";
        assertThrows(OutsideCellRangeException.class, ()-> sendCommandToController(inputString), failedTestString);
    }

    @DisplayName("Testing that OutsideCellRangeException is thrown when a cell outside of a 6x6 grid is requested")
    @ParameterizedTest(name = "{displayName}.  input is {arguments}")
    @ValueSource(strings = {
            "g1", "G1", "g2", "G2", "g3", "G3", "g4", "G4", "g5", "G5", "g6", "G6",
            "a7", "A7", "b7", "B7", "c7", "C7", "d7", "D7", "e7", "E7", "f7", "F7", "g7", "G7",
    })
    void testOutsideOfBoardReference6X6(String inputString) {
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        String failedTestString = "Input of a cell outside of the board doesn't throw an OutsideCellRangeException";
        assertThrows(OutsideCellRangeException.class, ()-> sendCommandToController(inputString), failedTestString);
    }

    @DisplayName("Testing that OutsideCellRangeException is thrown when a cell outside of a 5x5 grid is requested")
    @ParameterizedTest(name = "{displayName}.  input is {arguments}")
    @ValueSource(strings = {
            "f1", "F1", "f2", "F2", "f3", "F3", "f4", "F4", "f5", "F5",
            "a6", "A6", "b6", "B6", "c6", "C6", "d6", "D6", "e6", "E6", "f6", "F6"
    })
    void testOutsideOfBoardReference5X5(String inputString) {
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        String failedTestString = "Input of a cell outside of the board doesn't throw an OutsideCellRangeException";
        assertThrows(OutsideCellRangeException.class, ()-> sendCommandToController(inputString), failedTestString);
    }

    @DisplayName("Testing that OutsideCellRangeException is thrown when a cell outside of a 4x4 grid is requested")
    @ParameterizedTest(name = "{displayName}.  input is {arguments}")
    @ValueSource(strings = {
            "e1", "E1", "e2", "E2", "e3", "E3", "e4", "E4",
            "a5", "A5", "b5", "B5", "c5", "C5", "d5", "D5", "e5", "E5"
    })
    void testOutsideOfBoardReference4X4(String inputString) {
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        String failedTestString = "Input of a cell outside of the board doesn't throw an OutsideCellRangeException";
        assertThrows(OutsideCellRangeException.class, ()-> sendCommandToController(inputString), failedTestString);
    }

    @DisplayName("Testing that OutsideCellRangeException is thrown when a cell outside of a 3x3 grid is requested")
    @ParameterizedTest(name = "{displayName}.  input is {arguments}")
    @ValueSource(strings = {
            "d1", "D1", "d2", "D2", "d3", "D3",
            "a4", "A4", "b4", "B4", "c4", "C4", "d4", "D4"
    })
    void testOutsideOfBoardReference3X3(String inputString) {
        controller.removeColumn();
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
        controller.removeRow();
        System.out.println("Board size if R:" + model.getNumberOfRows() + " C:" + model.getNumberOfColumns());
        String failedTestString = "Input of a cell outside of the board doesn't throw an OutsideCellRangeException";
        assertThrows(OutsideCellRangeException.class, ()-> sendCommandToController(inputString), failedTestString);
    }

    @DisplayName("Testing that OutsideCellRangeException is thrown when a cell outside of a 2x2 grid is requested")
    @ParameterizedTest(name = "{displayName}.  input is {arguments}")
    @ValueSource(strings = {
            "c1", "C1", "c2", "C2",
            "a3", "A3", "b3", "B3", "c3", "C3"
    })
    void testOutsideOfBoardReference2X2(String inputString) {
        controller.removeColumn();
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
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        String failedTestString = "Input of a cell outside of the board doesn't throw an OutsideCellRangeException";
        assertThrows(OutsideCellRangeException.class, ()-> sendCommandToController(inputString), failedTestString);
    }

    @DisplayName("Testing that OutsideCellRangeException is thrown when a cell outside of a 1x1 grid is requested")
    @ParameterizedTest(name = "{displayName}.  input is {arguments}")
    @ValueSource(strings = {
            "b1", "B1",
            "a2", "A2", "b2", "B2"
    })
    void testOutsideOfBoardReference1X1(String inputString) {
        controller.removeColumn();
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
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        String failedTestString = "Input of a cell outside of the board doesn't throw an OutsideCellRangeException";
        assertThrows(OutsideCellRangeException.class, ()-> sendCommandToController(inputString), failedTestString);
    }

}
