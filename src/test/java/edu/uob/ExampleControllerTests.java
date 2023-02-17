package edu.uob;

import edu.uob.OXOMoveException.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class ExampleControllerTests {
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

  // Test simple move taking and cell claiming functionality
  @Test
  void testBasicMoveTaking() throws OXOMoveException {
    // Find out which player is going to make the first move
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    // Make a move
    sendCommandToController("a1");
    // Check that A1 (cell [0,0] on the board) is now "owned" by the first player
    String failedTestComment = "Cell a1 wasn't claimed by the first player";
    assertEquals(firstMovingPlayer, controller.gameModel.getCellOwner(0, 0), failedTestComment);
  }

  // Test out basic win detection
  @Test
  void testBasicWin() throws OXOMoveException {
    // Find out which player is going to make the first move (they should be the eventual winner)
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    // Make a bunch of moves for the two players
    sendCommandToController("a1"); // First player
    sendCommandToController("b1"); // Second player
    sendCommandToController("a2"); // First player
    sendCommandToController("b2"); // Second player
    sendCommandToController("a3"); // First player

    // a1, a2, a3 should be a win for the first player (since players alternate between moves)
    // Let's check to see whether the first moving player is indeed the winner
    String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
  }

  // Example of how to test for the throwing of exceptions
  @Test
  void testInvalidIdentifierException() throws OXOMoveException {
    // Check that the controller throws a suitable exception when it gets an invalid command
    String failedTestComment = "Controller failed to throw an InvalidIdentifierLengthException for command `abc123`";
    // The next lins is a bit ugly, but it is the easiest way to test exceptions (soz)
    assertThrows(InvalidIdentifierLengthException.class, ()-> sendCommandToController("abc123"), failedTestComment);
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

  // Testing the addRow() method in the controller.  Should add a row until you reach 9.
  // Then it should do nothing.
  @Test
  void addRowTo4Works() throws OXOMoveException {
    controller.addRow();
    String failedTestComment = "Adding row to three-row board does not result in a four-row board";
    assertEquals(model.getNumberOfRows(), 4, failedTestComment);
  }
  @Test
  void addRowTo5Works() throws OXOMoveException {
    controller.addRow();
    controller.addRow();
    String failedTestComment = "Adding row to four-row board does not result in a five-row board";
    assertEquals(model.getNumberOfRows(), 5, failedTestComment);
  }
  @Test
  void addRowTo6Works() throws OXOMoveException {
    controller.addRow();
    controller.addRow();
    controller.addRow();
    String failedTestComment = "Adding row to five-row board does not result in a six-row board";
    assertEquals(model.getNumberOfRows(), 6, failedTestComment);
  }
  @Test
  void addRowTo7Works() throws OXOMoveException {
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addRow();
    String failedTestComment = "Adding row to six-row board does not result in a seven-row board";
    assertEquals(model.getNumberOfRows(), 7, failedTestComment);
  }

  @Test
  void addRowTo8Works() throws OXOMoveException {
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addRow();
    String failedTestComment = "Adding row to seven-row board does not result in a eight-row board";
    assertEquals(model.getNumberOfRows(), 8, failedTestComment);
  }

  @Test
  void addRowTo9Works() throws OXOMoveException {
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addRow();
    String failedTestComment = "Adding row to eight-row board does not result in a nine-row board";
    assertEquals(model.getNumberOfRows(), 9, failedTestComment);
  }

  @Test
  void addRowTo10Fails() throws OXOMoveException {
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addRow();
    String failedTestComment = "Adding row to nine-row board results in a ten-row board (which it shouldn't)";
    assertEquals(model.getNumberOfRows(), 9, failedTestComment);
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
