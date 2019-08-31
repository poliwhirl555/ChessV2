package model.Pieces;

import model.Chessboard;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.util.Observable;

public class King extends ChessPiece {

    public King(String colour) {
        super(colour);
    }

    @Override
    public void update(Observable o, Object arg) {
        resetLists();

        Chessboard board = (Chessboard)o;
        List<Point> possibleMoves = new ArrayList<>();
        List<Point> opposingCoveredSquares = board.getOpposingCoveredSquares(this.getColour());

        possibleMoves.add(new Point(getLocation().x + 1, getLocation().y + 1));
        possibleMoves.add(new Point(getLocation().x - 1, getLocation().y - 1));
        possibleMoves.add(new Point(getLocation().x + 1, getLocation().y - 1));
        possibleMoves.add(new Point(getLocation().x - 1, getLocation().y + 1));
        possibleMoves.add(new Point(getLocation().x, getLocation().y - 1));
        possibleMoves.add(new Point(getLocation().x, getLocation().y + 1));
        possibleMoves.add(new Point(getLocation().x + 1, getLocation().y));
        possibleMoves.add(new Point(getLocation().x - 1, getLocation().y));

        possibleMoves.removeIf(point -> !Chessboard.onBoard(point));

        for (Point p : possibleMoves) {
            if (!opposingCoveredSquares.contains(p)) {
                if (board.getPiece(p).getColour().equals(this.getColour())) {
                    coveredSquares.add(p);
                } else {
                    validMoves.add(p);
                    coveredSquares.add(p);
                }
            }
        }
    }
}
