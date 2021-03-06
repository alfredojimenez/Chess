package board;

import java.util.ArrayList;

import chess.chess;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import pieces.King;
import pieces.Piece;

public class Square extends Group {

	public static final double SIZE = 80;
	private Rectangle bg;
	private Color originalColor;
	private Piece piece;
	public static Square active;
	private static int turnCounter = 0;
	public static ArrayList<Square> marked = new ArrayList<Square>();

	public Square(Color c) {
		originalColor = c;
		bg = new Rectangle(SIZE, SIZE, c);
		this.getChildren().add(bg);

		this.setOnMouseClicked(event -> {

			if (marked.contains(this)) {
				Piece p = active.piece;
				if (this.piece instanceof King) {
					if (this.piece.getColor() == Color.WHITE) {
						chess.gameOver();
						System.out.println("GAME OVER");
					}
					if (this.piece.getColor() == Color.BLACK) {
						chess.win();
						System.out.println("Congratulations! You won in "+ Square.turnCounter + " turns!");
					}
					return;
				}
				active.piece = null;
				active.makeInactive();
				turnCounter++;
				System.out.println("Turn #"+ turnCounter);
				this.addPiece(p);
				p.move();
				return;
			}

			if (!hasPiece()) {
				return;
			}

			if (hasPiece()) {
				if (turnCounter % 2 == 0 && piece.getColor() == Color.BLACK) { // White
					return;
				}
				if (turnCounter % 2 == 1 && piece.getColor() == Color.WHITE) { // Black
					return;
				}
				makeActive();
				@SuppressWarnings("unused")
				int row = row();
				@SuppressWarnings("unused")
				int col = col();
			} else {
				if (active != null) {
					active.makeInactive();
				}
				mark(this.piece.getColor());
			}
		});
	}

	private int col() {
		int y = row();
		for (int i = 0; i < 8; i++) {
			if (ChessBoard.all_squares.get(y).get(i) == this) {
				return i;
			}
		}
		return -1;
	}

	public int row() {
		for (int i = 0; i < 8; i++) {
			if (ChessBoard.all_squares.get(i).contains(this)) {
				return i;
			}
		}
		return -1;
	}

	public void moveMark() {
		Circle cir = new Circle(SIZE / 2, SIZE / 2, SIZE / 10, Color.YELLOW);
		this.getChildren().add(cir);
		marked.add(this);
	}

	public void mark(Color c) {
		if (!this.hasPiece()) {
			moveMark();
		} else {
			if (this.piece.getColor() != c) {
				attackMark();
			}
		}
	}

	public void attackMark() {
		Circle cir = new Circle(SIZE / 2, SIZE / 2, SIZE / 10, Color.RED);
		this.getChildren().add(cir);
		marked.add(this);
	}

	private void removeMoveMark() {
		for (Square square : marked) {
			square.getChildren().remove(square.getChildren().size() - 1);
		}
		marked.clear();
	}

	private void makeActive() {
		if (active != null) {
			active.makeInactive();
		}
		active = this;
		this.piece.showMove(col(), row());
		this.getBackground().setFill(Color.GREEN);
	}

	private void makeInactive() {
		removeMoveMark();
		active = null;
		this.getBackground().setFill(originalColor);
	}

	public void addPiece(Piece p) {
		if (hasPiece()) {
			this.getChildren().remove(piece);
		}
		this.piece = p;
		this.getChildren().add(p);
	}

	public boolean hasPiece() {
		return this.piece != null;
	}

	public Rectangle getBackground() {
		return this.bg;
	}

	public Boolean turn(int i) {
		boolean whiteTurn = true;
		if (i % 2 == 0) {
			return whiteTurn = false;
		} else {
			return whiteTurn;
		}
	}
}