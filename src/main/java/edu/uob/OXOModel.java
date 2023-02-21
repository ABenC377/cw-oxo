package edu.uob;

import java.util.ArrayList;

public class OXOModel {
    private final ArrayList<ArrayList<OXOPlayer>> cells;
    private final ArrayList<OXOPlayer> players;
    private int currentPlayerNumber;
    private OXOPlayer winner;
    private boolean gameDrawn;
    private int winThreshold;

    public OXOModel(int numberOfRows, int numberOfColumns, int winThresh) {
        winThreshold = winThresh;
        cells = new ArrayList<>();
        for (int i = 0; i < numberOfRows; i++) {
            cells.add(new ArrayList<>());
            for (int j = 0; j < numberOfColumns; j++) {
                cells.get(i).add(null);
            }
        }
        players = new ArrayList<>();
        winner = null;
        gameDrawn = false;
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public void addPlayer(OXOPlayer player) {
        players.add(player);
    }

    public void removePlayer() {
        if (players.size() > 0) {
            players.remove(players.size() - 1);
        }
    }

    public OXOPlayer getPlayerByNumber(int number) {
        return players.get(number);
    }

    public OXOPlayer getWinner() {
        return winner;
    }

    public void setWinner(OXOPlayer player) {
        winner = player;
    }

    public int getCurrentPlayerNumber() {
        return currentPlayerNumber;
    }

    public void setCurrentPlayerNumber(int playerNumber) {
        currentPlayerNumber = playerNumber;
    }

    public int getNumberOfRows() {
        return cells.size();
    }

    public int getNumberOfColumns() {
        return cells.get(0).size();
    }

    public OXOPlayer getCellOwner(int rowNumber, int colNumber) {
        return cells.get(rowNumber).get(colNumber);
    }

    public void setCellOwner(int rowNumber, int colNumber, OXOPlayer player) {
        cells.get(rowNumber).set(colNumber, player);
    }

    public void setWinThreshold(int winThresh) {
        winThreshold = winThresh;
    }

    public int getWinThreshold() {
        return winThreshold;
    }

    public void setGameDrawn() {
        gameDrawn = true;
    }

    public void setGameNotDrawn() {
        gameDrawn = false;
    }

    public boolean isGameDrawn() {
        return gameDrawn;
    }

    public void addColumn() {
        for (ArrayList<OXOPlayer> row : cells) {
            row.add(null);
        }
    }
    public void addRow() {
        cells.add(new ArrayList<>());
        for (OXOPlayer ignored : cells.get(0)) {
            cells.get(cells.size() - 1).add(null);
        }
    }
    public void removeColumn() {
        for (ArrayList<OXOPlayer> row : cells) {
            row.remove(row.size() - 1);
        }
    }
    public void removeRow() {
        cells.remove(cells.size() - 1);
    }


}
