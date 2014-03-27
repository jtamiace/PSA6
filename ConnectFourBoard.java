import java.util.Scanner;

/** A representation of the game of Connect 4.  
  * Author: Christine Alvarado
  * Date: April 1, 2013
  * */

public class ConnectFourBoard {
  /** The state of the board */
  private char[][] board;
  /** The height of the board (number of rows) */
  private int height;
  /** The width of the board (number of columns) */
  private int width;
  
  /**
   * Constructor which sets the board size (columns, 
   * and rows) to the values received as arguments 
   * and initializes each location in the board to an space
   * @param width Number of columns in the board
   * @param height Number of rows in the board
   */
  public ConnectFourBoard( int width, int height )
  {
    this.height = height;
    this.width = width;
    board = new char[height][width];
    // WARNING: This next step is important and likely to be missed.
    for ( int row = 0; row < height; row++ ) {
      for ( int col = 0; col < width; col++ ) {
        board[row][col] = ' ';
      }
    }
  }
  /**
   * Default constructor which sets the board size 
   * to 7 columns and 6 rows and initializes each 
   * location in the board to an space
   */
  public ConnectFourBoard() {
    this( 7, 6 );
  }
  
  // Note that because of the way we have chosen to print the board,
  // the bottom row is actually the LARGEST.
  /**
   * Generate and returns a formatted string representation of 
   * the current status of the board. It includes 
   * columns, rows, checkers and labels in the bottom.
   * @return The string representation of the board
   */
  public String toString() {
    String toReturn = new String();
    for ( char[] row : board ) {
      for ( char value : row ) {
        toReturn += "|" + value;
      }
      toReturn += "|\n";
    }
    for ( int rnum = 0; rnum < this.width; rnum++ ) {
      toReturn += "--"; 
    }
    toReturn += "-\n";
    for ( int rnum = 0; rnum < this.width; rnum++ ) {
      toReturn += " " + rnum % 10; 
    }
    toReturn += "\n";
    
    return toReturn;
  }
  
  /** Get the contents of the cell at the specified row and column.
    * @param row The row in question
    * @param column The column in question
    * @return The contents of the cell at row, column
    * */
  public char getContents( int row, int column ) {
    return board[row][column];
  }
  
  /**
   * This method adds checkers to the next 
   * available position in column.
   * @param column The column where the checker will be added
   * @param checker The char representing the checker to be added
   */
  public void addMove( int column, char checker ) {
    // We go in reverse order because of the way we print the board 
    // (Largest row at the bottom).  If the students print it the other way
    // then they can go from smallest to largest.
    int row = this.height - 1;
    while ( row >= 0 ) {
      if ( this.board[row][column] == ' ' ) {
        this.board[row][column] = checker;
        // This break is TRICKY.  They can either break or return, but they have to do
        // one of the other, otherwise they get a column full of the checker.
        break;
      }
      row--;
    }
  }
  
  /**
   * Clears the board by assigning spaces 
   * to each location in the board.
   */
  public void clear() {
    for ( int row = 0; row < height; row++ ) {
      for ( int col = 0; col < width; col++ ) {
        board[row][col] = ' ';
      }
    }
  }
  
  /** Takes in a string of columns and places
    * alternating checkers in those columns,
    * starting with 'X'
    * 
    * For example, call b.setBoard("012345")
    * to see 'X's and 'O's alternate on the
    * bottom row, or b.setBoard("000000") to
    * see them alternate in the left column.
    * 
    * @param moveString A string of integers. 
    *    Note that this method will only play 
    *    in the first 10 columns of a board.
    */   
  public void setBoard( String moveString ) {
    char nextCh = 'X';   // start by playing 'X'
    for ( int i = 0; i < moveString.length(); i++ ) {
      int col = Character.getNumericValue( moveString.charAt( i ) );
      if ( 0 <= col && col < width )
        addMove(col, nextCh);
      if ( nextCh == 'X' )
        nextCh = 'O';
      else
        nextCh = 'X';
    }
  }
  
  /**
   * Checks if the column received as argument allows
   * a move in it by checking that the column number 
   * is legal to this board (from 0 to the last column)
   * and checking if the column is not full. It returns 
   * true if the move is allowed and false otherwise.
   * @param col The column to be checked
   */
  public boolean allowsMove( int col ) {
    return col >= 0 && col < width && board[0][col] == ' ';
  }
  
  /**
   * Checks if the board is full of checkers. 
   * Returns true if it is full and false if 
   * there is at least one available space.
   * @return Whether if the board if full or not.
   */
  public boolean isFull( ) {
    for ( int col = 0; col < width; col++ ) {
      if (allowsMove(col))
        return false;
    }
    return true;
  }
  
  /**
   * Helper method to check if the received checker
   * have 4 contiguous moves horizontally. 
   * Returns true if there are four contiguous chechers
   * or false otherwise.
   * @return The result of the horizontal check.
   */
  private boolean winsHorizontal( char checker ) {
   // check for horizontal wins
    for ( int row = 0; row < height; row++ ) {
      for (int col = 0; col < width-3; col++ ) {
        if ( board[row][col] == checker &&
            board[row][col+1] == checker && 
            board[row][col+2] == checker && 
            board[row][col+3] == checker )
          return true;
      }
    }
    return false;
  }

  /**
   * Helper method to check if the received checker
   * have 4 contiguous moves vertically. 
   * Returns true if there are four contiguous chechers
   * or false otherwise.
   * @return The result of the horizontal check.
   */
  private boolean winsVertical( char checker ) {
    for ( int row = 0; row < height-3; row++ ) {
      for (int col = 0; col < width; col++ ) {
        if ( board[row][col] == checker &&
            board[row+1][col] == checker && 
            board[row+2][col] == checker && 
            board[row+3][col] == checker )
          return true;
      }
    }
    return false;
  }
  
  // This is challenging.  Draw concrete examples.
  /**
   * Helper method to check if the received checker
   * have 4 contiguous moves diagonally. There are two
   * posibilities: from bottom left to upper right and
   * from upper rigth to bottom left.
   * Returns true if there are four contiguous chechers
   * or false otherwise.
   * @return The result of the diagonal check.
   */
  private boolean winsDiagonal( char checker ) {
    // Check for diagonal in one direction
    for ( int row = 0; row < height-3; row++ ) {
      for (int col = 0; col < width-3; col++ ) {
        // check one direction
        if ( board[row][col] == checker &&
            board[row+1][col+1] == checker && 
            board[row+2][col+2] == checker && 
            board[row+3][col+3] == checker )
          return true;
        // And the other direction
        if ( board[row][col+3] == checker &&
            board[row+1][col+2] == checker &&
            board[row+2][col+1] == checker &&
            board[row+3][col] == checker )
          return true;
      }
    }
    
    return false;
  }

  /**
   * Checks if the received checker have 4 in a row
   * horizontally, vertically and diagonally. 
   * Returns true if there are four contiguous chechers
   * or false otherwise.
   * @return The result of the check.
   */
  public boolean winsFor( char checker ) {
    return ( winsVertical( checker ) || winsHorizontal( checker ) || 
            winsDiagonal( checker ) );
  }
  
  /**
   * Launches connect four game for two players.
   * It prints the board and ask the players to 
   * input the column number where they want to 
   * place their checkers. It iterate until a 
   * player wins or the board is full and the 
   * player tied.
   */
  public void hostGame() {
    this.clear();
    System.out.println( "Welcome to Connect Four!" );
    char player = 'X';
    Scanner input = new Scanner( System.in );
    while (!winsFor('X') && !winsFor('O') && !isFull()) {
      System.out.println( this );
      System.out.println( player + "'s turn." );
      int nextMove = -1;
      do {
        System.out.print( "Please enter your move: " );
        nextMove = input.nextInt();
      } while (!allowsMove(nextMove));
      
      this.addMove( nextMove, player );
      if ( player == 'X' )
        player = 'O';
      else
        player = 'X';
    }
    
    System.out.println( this );
    if (winsFor( 'X' )) {
      System.out.println( "X wins!" );
    }
    else if (winsFor('O')) {
      System.out.println( "O wins!" );
    }
    else {
      System.out.println( "Tie game!" );
    }
  }
}
