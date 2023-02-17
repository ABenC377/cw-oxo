package edu.uob;

public class OXOController {
    OXOModel gameModel;

    public OXOController(OXOModel model) {
        gameModel = model;
    }

    public void handleIncomingCommand(String command) throws OXOMoveException {
        if (gameModel.getWinner() == null) {
            int currentPlayer = gameModel.getCurrentPlayerNumber();
            int row = (int)command.trim().toLowerCase().charAt(0) - (int)'a';
            int col = Integer.parseInt(command.trim().toLowerCase().substring(1)) - 1;
            int nextPlayer = (currentPlayer + 1 >= gameModel.getNumberOfPlayers()) ? 0 : currentPlayer + 1;
            gameModel.setCellOwner(row, col, gameModel.getPlayerByNumber(currentPlayer));
            gameModel.setCurrentPlayerNumber(nextPlayer);
            if (this.moveIsAWinner(row, col)) {
                gameModel.setWinner(gameModel.getPlayerByNumber(currentPlayer));
            }
        }
    }
    public void addRow() {
        gameModel.addRow();
    }
    public void removeRow() {
        gameModel.removeRow();
    }
    public void addColumn() {
        gameModel.addColumn();
    }
    public void removeColumn() {
        gameModel.removeColumn();
    }
    public void increaseWinThreshold() {}
    public void decreaseWinThreshold() {}
    public void reset() {}

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
