package model;

import model.Exceptions.GameOver;
import model.Exceptions.InvalidMoveException;
import model.Pieces.*;

import java.awt.*;
import java.util.List;
import java.util.*;

public class Chessboard extends Observable implements Iterable<ChessPiece> {
    private ChessPiece[][] board;
    private Map<String,Boolean> inCheck;
    private Map<String,ChessPiece> kings;
    private List<List<Point>> pathsToKing;
    private String turnColour;

    public Chessboard() {
        ChessPiece whiteKing = new King("white");
        ChessPiece blackKing = new King("black");
        ChessPiece[][] defaultBoard = {
                {new Rook("white"), new Knight("white"), new Bishop("white"),
                        whiteKing, new Queen("white"), new Bishop("white"), new Knight("white"),
                        new Rook("white")},

                {new Pawn("white"), new Pawn("white"), new Pawn("white"),  new Pawn("white"),
                        new Pawn("white"), new Pawn("white"), new Pawn("white"), new Pawn("white")},

                {new EmptySquare(), new EmptySquare(), new EmptySquare(), new EmptySquare(), new EmptySquare(),
                        new EmptySquare(), new EmptySquare(), new EmptySquare()},

                {new EmptySquare(), new EmptySquare(), new EmptySquare(), new EmptySquare(), new EmptySquare(),
                        new EmptySquare(), new EmptySquare(), new EmptySquare()},

                {new EmptySquare(), new EmptySquare(), new EmptySquare(), new EmptySquare(), new EmptySquare(),
                        new EmptySquare(), new EmptySquare(), new EmptySquare()},

                {new EmptySquare(), new EmptySquare(), new EmptySquare(), new EmptySquare(), new EmptySquare(),
                        new EmptySquare(), new EmptySquare(), new EmptySquare()},

                {new Pawn("black"), new Pawn("black"), new Pawn("black"),  new Pawn("black"),
                        new Pawn("black"), new Pawn("black"), new Pawn("black"), new Pawn("black")},

                {new Rook("black"), new Knight("black"), new Bishop("black"),
                        blackKing, new Queen("black"), new Bishop("black"), new Knight("black"),
                        new Rook("black")},
        };

        board = defaultBoard;
        turnColour = "white";
        pathsToKing = new ArrayList<>();

        kings = new HashMap<>();
        kings.put("white", whiteKing);
        kings.put("black", blackKing);

        inCheck = new HashMap<>();
        inCheck.put("white", false);
        inCheck.put("black", false);

        setPiecePositions();

    }


    public void move(Point start, Point end) throws InvalidMoveException {
        ChessPiece piece = getPiece(start);

        if (!piece.getColour().equals(turnColour)) {
            throw new InvalidMoveException();
        } else if (inCheck.get(turnColour) && !piece.equals(kings.get(turnColour))) {
            isCheckMate(turnColour);
            for (List l : pathsToKing) {
                if (!l.contains(end)) {
                    throw new InvalidMoveException();
                }
            }
        } else {
            piece.move(end);
            movePiece(start, end);
        }

        resetInCheck();
        notifyObservers();
    }

    private void isCheckMate(String colour) throws InvalidMoveException {
        if (kings.get(colour).noMoves()) {
            throw new GameOver(getOppositeColour(colour));
        }
    }

    private void movePiece(Point start, Point end) {
        ChessPiece temp = getPiece(start);
        removePiece(temp);
        board[end.x][end.y] = temp;
        hasChanged();
    }

    public void setInCheckOpponent(String colour, List<Point> path) {
        inCheck.put(colour, true);
        pathsToKing.add(path);
    }

    public void resetInCheck() {
        inCheck = new HashMap<>();
        pathsToKing = new ArrayList<>();
    }

    /**
     *
     * @param startPiece the piece from where the vertical
     *                   and horizontal rows are calculated
     * @return A <code>List</code> containing lists for the top, bottom, left and right rows
     */

    public List<List<Point>> getVerticalAndHorizontals(ChessPiece startPiece) {
        Point start = startPiece.getLocation();
        int rowPosition = start.x;
        int columnPosition = start.y;
        List<List<Point>> crossLists = new ArrayList<>();

        List<Point> left = new ArrayList<>();
        for (int c = columnPosition; c < 8 && c > -1; c--) {
            left.add(new Point(rowPosition, c));
        }

        List<Point> right = new ArrayList<>();
        for (int c = columnPosition; c < 8 && c > -1; c++) {
            right.add(new Point(rowPosition, c));
        }

        List<Point> top = new ArrayList<>();
        for (int r = rowPosition; r < 8 && r > -1; r--) {
            top.add(new Point(r, columnPosition));
        }

        List<Point> bottom = new ArrayList<>();
        for (int r = rowPosition; r < 8 && r > -1; r++) {
            bottom.add(new Point(r, columnPosition));
        }

        crossLists.add(top);
        crossLists.add(bottom);
        crossLists.add(left);
        crossLists.add(right);

        return crossLists;

    }

    /**
     *
     * @param startPiece the piece from where the diagonals are calculated
     * @return A <code>List</code> containing lists for the all the diagonals
     */

    public List<List<Point>> getDiagonals(ChessPiece startPiece) {
        Point start = startPiece.getLocation();
        int rowPosition = start.x;
        int columnPosition = start.y;
        List<List<Point>> diagonalLists = new ArrayList<>();

        List<Point> topLeft = new ArrayList<>();
        for (int r = rowPosition, c = columnPosition; r < 8 && c < 8 && r > -1 && c > -1; r--,c--) {
            topLeft.add(new Point(r, c));
        }

        List<Point> topRight = new ArrayList<>();
        for (int r = rowPosition, c = columnPosition; r < 8 && c < 8 && r > -1 && c > -1; r--,c++) {
            topRight.add(new Point(r, c));
        }

        List<Point> bottomLeft = new ArrayList<>();
        for (int r = rowPosition, c = columnPosition; r < 8 && c < 8 && r > -1 && c > -1; r++,c--) {
            bottomLeft.add(new Point(r, c));
        }

        List<Point> bottomRight = new ArrayList<>();
        for (int r = rowPosition, c = columnPosition; r < 8 && c < 8 && r > -1 && c > -1; r++,c++) {
            bottomRight.add(new Point(r, c));
        }

        diagonalLists.add(topLeft);
        diagonalLists.add(topRight);
        diagonalLists.add(bottomLeft);
        diagonalLists.add(bottomRight);

        return diagonalLists;

    }

    public List<Point> getOpposingCoveredSquares(String colour) {
        List<Point> coveredSquares = new ArrayList<>();
        for (ChessPiece p : this) {
            if (!p.getColour().equals(colour)) {
                coveredSquares.addAll(p.getCoveredSquares());
            }
        }

        return coveredSquares;
    }

    /**
     *
     * @param location the location of the desired piece
     * @return Piece at the <code>location</code>
     */

    public ChessPiece getPiece(Point location) {
        return board[location.x][location.y];
    }


    /**
     *
     * @param piece the <code>ChessPiece</code> to be removed
     *
     * Removes every occurrence of this specific piece from the internal array
     */
    // MODIFIES: this
    // EFFECTS: Removes every occurrence of this specific piece from the internal array
    public void removePiece(ChessPiece piece) {
        for (int r = 0; r <8; r++) {
            for (int c = 0; c <8; c++) {
                if (board[r][c] == piece) {
                    board[r][c] = new EmptySquare();
                }
            }
        }


    }

    public static boolean onBoard(Point p) {
        return (p.x > -1 && p.x < 8 && p.y > -1 && p.y < 8);
    }

    private String getOppositeColour(String colour) {
        if (colour.equals("white")) {
            return "black";
        } else  {
            return "white";
        }
    }

    private void setPiecePositions() {
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                board[row][column].setLocation(new Point(row, column));
            }
        }
    }


    @Override
    public Iterator<ChessPiece> iterator() {
        return new BoardIterator();
    }

    // BoardIterator that iterates through the board starting on the white side
    // A1 to the black side in the order A1, B1, C1,...
    private class BoardIterator implements Iterator<ChessPiece> {
        private int boardRow;
        private int boardColumn;


        BoardIterator() {
            boardRow = 0;
            boardColumn = 0;
        }


        @Override
        public boolean hasNext() {
             return boardRow > 7;
        }

        @Override
        public ChessPiece next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            if (boardColumn == 8) {
                boardColumn = 1;
            } else {
                boardColumn++;
            }

            return board[boardRow][boardColumn - 1];

        }
    }


}
