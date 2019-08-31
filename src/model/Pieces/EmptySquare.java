package model.Pieces;

import model.Exceptions.EmptySquareException;
import model.Exceptions.InvalidMoveException;

import java.awt.*;
import java.util.Observable;

public class EmptySquare extends ChessPiece {

    public EmptySquare() {}

    @Override
    public void update(Observable o, Object arg) {
        // Pass, since there's nothing to update
    }

    @Override
    public void move(Point endLocation) throws InvalidMoveException {
        throw new EmptySquareException();
    }

}
