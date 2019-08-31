package model.Pieces;

import model.Chessboard;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Knight extends ChessPiece {

    public Knight(String colour) {
        super(colour);
    }

    @Override
    public void update(Observable o, Object arg) {
        resetLists();

        Chessboard board = (Chessboard)o;

        List<Point> possibleMoves = new ArrayList<>(Arrays.asList(
                new Point(getLocation().x + 2, getLocation().y + 1),
                new Point(getLocation().x + 1, getLocation().y + 2),
                new Point(getLocation().x - 1,getLocation().y - 2),
                new Point(getLocation().x - 1, getLocation().y + 2),
                new Point(getLocation().x + 1, getLocation().y - 2),
                new Point(getLocation().x + 2, getLocation().y - 1),
                new Point(getLocation().x - 2, getLocation().y -1),
                new Point(getLocation().x - 2, getLocation().y + 1)));

        possibleMoves.removeIf(point -> !Chessboard.onBoard(point));

        for (Point p : possibleMoves) {
            ChessPiece piece = board.getPiece(p);
            List<Point> positionAsList = Collections.singletonList(this.getLocation());
            if (piece instanceof King && !piece.getColour().equals(this.getColour())) {
                board.setInCheckOpponent(this.getColour(), positionAsList);
            } else if (piece.getColour().equals(this.getColour())) {
                coveredSquares.add(p);
            } else {
                validMoves.add(p);
                coveredSquares.add(p);
            }
        }
    }
}
