package mines;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * --- Mine-Sweeper Game ---
 * 
 * @author kobim
 *
 */
public class Mines {

	private int height, width; // Board's size
	private boolean showAll = false; // Open the board and show everything
	private Position[][] board; // The board (positions matrix)

	/**
	 * Constructor: creates the board by the size received, and put mines in random
	 * places.
	 * 
	 * @param height
	 * @param width
	 * @param numMines
	 */
	public Mines(int height, int width, int numMines) {
		this.height = height;
		this.width = width;

		// Initialize the board:
		board = new Position[height][width];
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				board[i][j] = new Position(i, j);

		// Puts mines according to the received number, in random places:
		int i, j;
		Random rnd = new Random();
		for (int k = 0; k < numMines; k++) {
			i = rnd.nextInt(height); // Ensure valid value for rows.
			j = rnd.nextInt(width); // Ensure valid value for columns.
			addMine(i, j); // Add the mines.
		}
	}

	/**
	 * Puts mine in received position and returns true.
	 * 
	 * @param i (Row)
	 * @param j (Col)
	 * @return
	 */
	public boolean addMine(int i, int j) {
		board[i][j].mine = true;
		return board[i][j].isMine();
	}

	/**
	 * Open position: Marks the received position as opened, and returns true if
	 * there is no mine in this position. If there are no mines in the position's
	 * neighbors, keep open the neighbors, and the neighbors of these neighbors
	 * (Recursive) until there is mine around.
	 * 
	 * @param i (Row)
	 * @param j (Col)
	 * @return
	 */
	public boolean open(int i, int j) {
		// Case of mine in this position:
		if (board[i][j].isMine())
			return false;
		// Case that the position is already opened.
		if (board[i][j].isOpen())
			return true;

		// Case that position is closed:
		board[i][j].opened = true; // Mark as open.
		// Open each neighbor if it's not a mine:
		List<Position> p = board[i][j].getNeighbors();
		if (board[i][j].minesAround() == 0) // If there are no mines neighbors.
			for (Position temp : p) {
				open(temp.i, temp.j); // Open the neighbors of this position (Recursive).
			}
		return true;
	}

	/**
	 * Puts flag in the place received, or removes it if there was already a flag
	 * there.
	 * 
	 * @param i (Row)
	 * @param j (Col)
	 */
	public void toggleFlag(int i, int j) {
		if (board[i][j].isFlag())// Case of already flagged.
			board[i][j].flag = false; // Remove flag.
		else // Case of not flagged yet.
			board[i][j].flag = true; // Toggle flag.
	}

	/**
	 * Checks if the user won the game - returns true if all of the positions that
	 * are not mines are opened.
	 * 
	 * @return
	 */
	public boolean isDone() {
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				if (board[i][j].isOpen() == false && board[i][j].isMine() == false) // Close and not mine.
					return false; // Means not finished yet.

		return true; // If only the mines position is not open.
	}

	/**
	 * Returns the position's value as a string. If showAll=true, return the whole
	 * board as it was opened (before open it).
	 * 
	 * @param i (Row)
	 * @param j (Col)
	 * @return
	 */
	public String getValueAsString(int i, int j) {
		if (!showAll)
			return "" + board[i][j]; // Uses Position's toString method.
		else { // Case that showAll=true:
			if (board[i][j].isMine()) // Case of mine.
				return "X";
			if (board[i][j].minesAround() > 0) // Case that there are mines around.
				return "" + board[i][j].minesAround();

			// Case of no mines around.
			return " ";
		}
	}

	/**
	 * Sets the 'ShowAll' value. If 'ShowAll' is true, at the next board refresh the
	 * whole board will be opened.
	 * 
	 * @param showAll
	 */
	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}

	/**
	 * Returns the whole board as a string, using the 'getValueAsString' method for
	 * each position on the board.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++)
				sb.append(getValueAsString(i, j)); // Add to 'sb' the 'getValueAsString' value of each position.
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Inner class that represents single "Position", Contains: the number of mines
	 * around it, a list of it's neighbors, if opened, if it's a mine, if it was
	 * flagged, and it's indexes.
	 * 
	 * @author kobim
	 */
	private class Position {
		private int i, j;
		private boolean opened = false;
		private boolean mine = false;
		private boolean flag = false;
		private int numMinesAround = -1;
		private List<Position> neighbors; // List of the neighbors.

		/**
		 * Constructor:
		 * 
		 * @param i (Row)
		 * @param j (Col)
		 */
		public Position(int i, int j) {
			this.i = i;
			this.j = j;
		}

		/**
		 * Returns "true" if this position is already opened, else "false".
		 * 
		 * @return
		 */
		public boolean isOpen() {
			return opened;
		}

		/**
		 * Returns "true" if this position is a mine, else "false".
		 * 
		 * @return
		 */
		public boolean isMine() {
			return mine;
		}

		/**
		 * Returns "true" if this position has been flagged, else "false".
		 * 
		 * @return
		 */
		public boolean isFlag() {
			return flag;
		}

		/**
		 * Returns ArrayList of the neighbors positions.
		 * 
		 * @return
		 */
		public List<Position> getNeighbors() {

			// If the list hasn't created yet, create and return it. Else, return the list.
			if (neighbors == null) {
				neighbors = new ArrayList<>();

				// Add neighbors properly, considering the width and length.
				if (i < height - 1) {
					neighbors.add(board[i + 1][j]);
					if (j < width - 1)
						neighbors.add(board[i + 1][j + 1]);
					if (j > 0)
						neighbors.add(board[i + 1][j - 1]);
				}
				if (i > 0) {
					neighbors.add(board[i - 1][j]);
					if (j > 0)
						neighbors.add(board[i - 1][j - 1]);
					if (j < width - 1)
						neighbors.add(board[i - 1][j + 1]);
				}
				if (j < width - 1)
					neighbors.add(board[i][j + 1]);
				if (j > 0)
					neighbors.add(board[i][j - 1]);
			}

			return neighbors;
		}

		/**
		 * Returns the number of mines around this position, by checking it's neighbors.
		 * 
		 * @return
		 */
		public int minesAround() {
			if (numMinesAround != -1) // Case it was already calculated:
				return numMinesAround;

			numMinesAround = 0; // Case not calculated yet:
			for (Position temp : getNeighbors()) {
				if (temp.isMine())
					numMinesAround++;
			}
			return numMinesAround;
		}

		/**
		 * toString method, returns the position's value as a String.
		 */
		@Override
		public String toString() {
			// Case that position is already open:
			if (isOpen()) {
				if (isMine()) // Case of mine.
					return "X";
				// Case not mine:
				if (minesAround() == 0) // Case of no mines around.
					return " ";
				return "" + minesAround(); // Case that there are mines around.
			}
			// Case that position is still closed:
			if (isFlag()) // Case of flag.
				return "F";
			return "."; // Case of close and not flagged.
		}
	}
}