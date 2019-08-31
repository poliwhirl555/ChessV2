package model.Pieces;

import model.Chessboard;

import java.awt.*;
import java.util.List;
import java.util.Observable;

public class Rook extends ChessPiece {

    public Rook(String colour) {
        super(colour);
    }

    @Override
    public void update(Observable o, Object arg) {
        Chessboard board = (Chessboard) o;
        List<List<Point>> paths = board.getVerticalAndHorizontals(this);

        traversePaths(paths, board);
    }
}
