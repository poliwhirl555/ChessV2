package model.Pieces;

import model.Chessboard;
import model.Exceptions.InvalidMoveException;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

public abstract class ChessPiece implements Observer {
    private String colour;
    private Point boardLocation;
    protected List<Point> validMoves;
    protected List<Point> coveredSquares;

    public ChessPiece() {
        this.colour = null;
    }

    public ChessPiece(String colour) {
        this.colour = colour;
    }

    public Point getLocation() {
        return boardLocation;
    }

    public void setLocation(Point location) {
        boardLocation = location;
    }

    public String getColour() {
        return colour;
    }

    public List<Point> getCoveredSquares() {
        return coveredSquares;
    }

    public void move(Point endLocation) throws InvalidMoveException {
        if (this.validMoves.contains(endLocation)) {
            movePiece(endLocation);
        } else {
            throw new InvalidMoveException();
        }
    }

    public boolean noMoves() {
        return validMoves.size() == 0;
    }

    protected void traversePaths(List<List<Point>> paths, Chessboard board) {
        resetLists();

        for(List l : paths) {
            for (Object p : l) {
                Point location = (Point) p;
                ChessPiece piece = board.getPiece(location);
                List<Point> path = new ArrayList<>();

                if (piece instanceof King && !piece.getColour().equals(this.getColour())) {
                    path.add(this.getLocation());
                    board.setInCheckOpponent(this.getColour(), path);
                    break;
                } else if (piece.getColour().equals(this.getColour())) {
                    coveredSquares.add(location);
                } else {
                    validMoves.add(location);
                    coveredSquares.add(location);
                    path.add(location);
                    if (!(piece instanceof EmptySquare)) {
                        break;
                    }
                }

            }
        }
    }

    protected void resetLists() {
        coveredSquares = new ArrayList<>();
        validMoves = new ArrayList<>();
    }

    private void movePiece(Point endLocation) {
        boardLocation = endLocation;
    }

}
