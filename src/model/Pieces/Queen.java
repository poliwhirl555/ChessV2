package model.Pieces;

import model.Chessboard;

import java.util.List;
import java.awt.*;
import java.util.Observable;

public class Queen extends ChessPiece {

    public Queen(String colour) {
        super(colour);
    }

    @Override
    public void update(Observable o, Object arg) {
        Chessboard board = (Chessboard)o;
        List<List<Point>> paths = board.getVerticalAndHorizontals(this);

        paths.addAll(board.getDiagonals(this));
        traversePaths(paths, board);

    }
}
