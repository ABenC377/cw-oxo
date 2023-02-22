package edu.uob;
import edu.uob.OXOMoveException.*;
import org.jetbrains.annotations.NotNull;

import static java.lang.Character.*;

public class OXOController {
    public OXOModel gameModel;

    public OXOController(OXOModel model) {
        gameModel = model;
    }

    public void handleIncomingCommand(String command) throws OXOMoveException {
        if (this.checkAlreadyWon()) {
            return;
        }
        // DO A TONNE OF TESTS TO MAKE SURE THAT EXCEPTIONS AREN'T THROWN WHEN GAME WON
        if (command.length() != 2) {
            throw new InvalidIdentifierLengthException(command.length());
        }
        int currentPlayer = gameModel.getCurrentPlayerNumber();
        int row = getRowIndex(command);
        int col = getColIndex(command);
        if (gameModel.getCellOwner(row, col) != null) {
            throw new CellAlreadyTakenException(row, col);
        }
        gameModel.setCellOwner(row, col, gameModel.getPlayerByNumber(currentPlayer));
        int nextPlayer = (currentPlayer + 1 >= gameModel.getNumberOfPlayers()) ? 0 : currentPlayer + 1;
        gameModel.setCurrentPlayerNumber(nextPlayer);
        if (this.moveIsAWinner(row, col)) {
            gameModel.setWinner(gameModel.getPlayerByNumber(currentPlayer));
        } else if (this.boardIsFull()) {
            gameModel.setGameDrawn();
        }
    }
    private boolean boardIsFull() {
        for (int row = 0; row < gameModel.getNumberOfRows(); row++) {
            for (int col = 0; col < gameModel.getNumberOfColumns(); col++) {
                if (gameModel.getCellOwner(row, col) == null) {
                    return false;
                }
            }
        }
        return true;
    }
    private boolean checkAlreadyWon() {
        return (gameModel.getWinner() != null);
    }

    private int getRowIndex(String command) throws OXOMoveException {
        char rowChar = command.trim().charAt(0);
        if (!(rowChar >= 'A' && rowChar <= 'Z') && !(rowChar >= 'a' && rowChar <= 'z')) {
            throw new InvalidIdentifierCharacterException(RowOrColumn.ROW, rowChar);
        }
        int row = (int)toLowerCase(rowChar) - (int)'a';
        if (row < 0 || row >= gameModel.getNumberOfRows()) {
            throw new OutsideCellRangeException(RowOrColumn.ROW, row);
        }
        return row;
    }

    private int getColIndex(String command) throws OXOMoveException {
        char colChar = command.trim().charAt(1);
        if (!(colChar >= '0' && colChar <= '9')) {
            throw new InvalidIdentifierCharacterException(RowOrColumn.COLUMN, colChar);
        }
        int col = (int)colChar - (int)'1';
        if (col < 0 || col >= gameModel.getNumberOfColumns()) {
            throw new OutsideCellRangeException(RowOrColumn.COLUMN, col);
        }
        return col;
    }

    public void addRow() {
        if (gameModel.getNumberOfRows() < 9) {
            gameModel.addRow();
            gameModel.setGameNotDrawn();
        }
    }
    public void removeRow() {
        boolean lastRowEmpty = this.lastRowIsEmpty();
        boolean otherRowsNotFull = !this.otherRowsFull();
        boolean safeToReduce = lastRowEmpty && otherRowsNotFull;
        if (gameModel.getNumberOfRows() > 1 && safeToReduce ) {
            gameModel.removeRow();
        }
    }
    private boolean lastRowIsEmpty() {
        int r = gameModel.getNumberOfRows();
        int c = gameModel.getNumberOfColumns();
        for (int col = 0; col < c; col++) {
            if (gameModel.getCellOwner(r - 1, col) != null) {
                return false;
            }
        }
        return true;
    }
    private boolean otherRowsFull() {
        for (int row = 0; row < (gameModel.getNumberOfRows() - 1); row++) {
            for (int col = 0; col < gameModel.getNumberOfColumns(); col++) {
                if (gameModel.getCellOwner(row, col) == null) {
                    return false;
                }
            }
        }
        return true;
    }
    public void addColumn() {
        if (gameModel.getNumberOfColumns() < 9) {
            gameModel.addColumn();
            gameModel.setGameNotDrawn();
        }
    }
    public void removeColumn() {
        boolean lastColEmpty = this.lastColumnIsEmpty();
        boolean otherColsNotFull = !this.otherColumnsFull();
        boolean safeToReduce = lastColEmpty && otherColsNotFull;
        if (gameModel.getNumberOfColumns() > 1 && safeToReduce) {
            gameModel.removeColumn();
        }
    }
    private boolean lastColumnIsEmpty() {
        int r = gameModel.getNumberOfRows();
        int c = gameModel.getNumberOfColumns();
        for (int row = 0; row < r; row++) {
            if (gameModel.getCellOwner(row, c - 1) != null) {
                return false;
            }
        }
        return true;
    }
    private boolean otherColumnsFull() {
        for (int row = 0; row < gameModel.getNumberOfRows(); row++) {
            for (int col = 0; col < (gameModel.getNumberOfColumns() - 1); col++) {
                if (gameModel.getCellOwner(row, col) == null) {
                    return false;
                }
            }
        }
        return true;
    }
    public void increaseWinThreshold() {
        int currentThreshold = gameModel.getWinThreshold();
        gameModel.setWinThreshold(currentThreshold + 1);
    }

    public void decreaseWinThreshold() {
        if ((isEmpty() || gameModel.getWinner() != null) && (gameModel.getWinThreshold() > 3)) {
            int currentThreshold = gameModel.getWinThreshold();
            gameModel.setWinThreshold(currentThreshold - 1);
        }
    }

    private boolean isEmpty() {
        for (int row = 0; row < gameModel.getNumberOfRows(); row++) {
            for (int col = 0; col < gameModel.getNumberOfColumns(); col++) {
                if (gameModel.getCellOwner(row, col) != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public void reset() {
        for (int row = 0; row < gameModel.getNumberOfRows(); row++) {
            for (int col = 0; col < gameModel.getNumberOfColumns(); col++) {
                gameModel.setCellOwner(row, col, null);
            }
        }
        gameModel.setCurrentPlayerNumber(0);
        gameModel.setWinner(null);
        gameModel.setGameNotDrawn();
    }

    private boolean moveIsAWinner(int row, int col) {
        for (int rowDirection = -1; rowDirection <= 1; rowDirection++) {
            for (int colDirection = -1; colDirection <= 1; colDirection++) {
                if (this.checkDirection(row, col, rowDirection, colDirection)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDirection(int row, int col, int rowDir, int colDir) {
        if (rowDir == 0 && colDir == 0) {
            return false;
        }
        OXOPlayer player = gameModel.getCellOwner(row, col);
        for (int offset = 1; offset < gameModel.getWinThreshold(); offset++) {
            int rowToCheck = row + (offset * rowDir);
            int colToCheck = col + (offset * colDir);
            boolean checkRow = (rowToCheck >= 0 && rowToCheck < gameModel.getNumberOfRows());
            boolean checkCol = (colToCheck >= 0 && colToCheck < gameModel.getNumberOfColumns());
            boolean shouldCheck = checkRow && checkCol;
            if (!shouldCheck || gameModel.getCellOwner(rowToCheck, colToCheck) != player) {
                return false;
            }
        }
        return true;
    }
}
