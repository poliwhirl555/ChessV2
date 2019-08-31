package model.Pieces;

import model.Chessboard;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Observable;

public class Pawn extends ChessPiece {
    private int movementModifier;
    private Point startPosition;
    private boolean firstUpdate;

    public Pawn(String colour) {
        super(colour);

        firstUpdate = true;
        if (this.getColour().equals("white")) {
            movementModifier = 1;
        } else {
            movementModifier = -1;
        }

    }

    @Override
    public void update(Observable o, Object arg) {
        resetLists();

        Chessboard board = (Chessboard)o;
        if (firstUpdate) {
            this.startPosition = this.getLocation();
        }

        List<Point> possibleMoves = new ArrayList<>();

        if (this.getLocation().equals(startPosition)) {
            possibleMoves.add(new Point(this.getLocation().x + (2 * movementModifier), this.getLocation().y));
        }

        Point frontSpace = new Point(this.getLocation().x + movementModifier, this.getLocation().y);

        if (board.getPiece(frontSpace) instanceof EmptySquare) {
            possibleMoves.add(frontSpace);
        }

        for (int i = -1; i < 2; i += 2) {
            Point attackingSquare = new Point(this.getLocation().x + movementModifier, this.getLocation().y + i);
            ChessPiece piece = board.getPiece(attackingSquare);

            if (piece instanceof King && !piece.getColour().equals(this.getColour())) {
                board.setInCheckOpponent(this.getColour(), Collections.singletonList(attackingSquare));
            } else {
                if (!piece.getColour().equals(this.getColour())) {
                    validMoves.add(attackingSquare);
                }

                coveredSquares.add(attackingSquare);
            }

        }
    }
}
