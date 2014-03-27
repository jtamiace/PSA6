// import all the necessary built-in java stuff
import javax.swing.*;  // For swing classes (the "J" classes)
import java.awt.*;     // For awt classes (e.g., Dimension)
import java.awt.event.*; // For events (which you will implement)

/** PSA 6
  * Author: Jessica Aceret
  * Date: May 16, 2013
  * A class that implements a GUI version of connect 4.  
  * 
  * PRE-PSA QUESTIONS:
  * 1. I would add an instance of a PlayListener object to a JPanel object,
  *    specifically a BoardCell object.
  * 2. The number of BoardCell objects that would need to be created is the 
  *    product of w and h, and I would store them to a JPanel
  * 3. JLabel status will be accessed in multiple methods and classes, not just 
  *    in the constructor, so the scope needs to be widened to the whole class
  *    of ConnectFour.
  * 4. The information about the contents of the board will be located in the
  *    instance variable theBoard. BoardCell will call getContents to access the
  *    values stored in theBoard.
  * 5. makeMove calls allowsMove, which will determine both when the game is over
  *    and detect illegal moves.
  * 6. ActionListener will be used to connect the act of clicking and 
  *    resetting/repainting board. So yes, it is a separate listener.
  * 7. I would create a main method in the ConnectFour class and declare a new 
  *    ConnectFour constructor.
  ******************************************************/
public class ConnectFour extends JFrame {
  
  /** The underlying board that will hold the state of the game */
  private ConnectFourBoard theBoard;  
  
  /** Whose turn it is.  We use 'X' and 'O', but we will translate 'X's and 
    * 'O's into colors to display them (I use blue and red in my example,
    * but you can use any two colors you like).  */
  private char turn;
  /** The status message at the top of the window */
  private JLabel status;
  /** The variable used for telling whose turn it is in the JLabel variable 
    * status in the makeMove method*/
  private String color;
//****************************************************
  /** Create a new Connect 4 game that is 7x6.  */
  public ConnectFour() {
    this( 7, 6 );
  }
//*****************************************************
  /** Create a new ConnectFour game with the specified height and width */
  public ConnectFour( int width, int height ) {
    // X starts
    this.turn = 'X';
    // Make a new underlying board.
    this.theBoard = new ConnectFourBoard(width, height);
    // The reset button.  It doesn't do anything yet.
    // You'll need to define and add the appropriate listener to it.
    JButton jbtReset = new JButton( "New Game" );
    jbtReset.addActionListener( new ActionListener() {
      public void actionPerformed( ActionEvent e ) {
        theBoard.clear();
        turn = 'X';
        status.setText("I hope you're prepared for ANOTHER totally awesome "+
                       "Connect Four experience. It's Pokeball's turn.");
        repaint();
      }
    });
    
    // This message will always display the current status
    // of the game (e.g., whose turn it is, whether the game is 
    // over, who won, etc).  Feel free to change the initial message.
    this.status = new JLabel( "I hope you're prepared for a totally awesome "+
                             "Connect Four experience. It's Pokeball's turn." );
    // This is the panel that will hold the BoardCells. 
    // You will need to populate it.  I suggest writing a helper
    // method to create the BoardCell objects and add them to
    // the JPanel.  You will want to use a GridLayout on the displayBoard
    // to lay out the BoardCell objects.
    JPanel displayBoard = new JPanel();
    displayBoard.setLayout( new GridLayout(height,width) );
    for ( int row = 0; row < height; row++ ) {
      for ( int col = 0; col < width; col++ ) {
        displayBoard.add( new BoardCell(row,col) );
      }
    }
    
    // Use a BorderLayout to lay out the game board
    setLayout( new BorderLayout() );
    add( status, BorderLayout.NORTH );
    add( displayBoard, BorderLayout.CENTER );
    add( jbtReset, BorderLayout.SOUTH );
    
    // Size and show the board
    pack();
    setVisible( true ); 
  }
//****************************************************
  /** makeMove is the method that determines whose turn it is, whether the game
    * is over, whether a move is illegal, and who the winner is*/
  private void makeMove( int col ) {
    if (!theBoard.winsFor('X') && !theBoard.winsFor('O') && !theBoard.isFull()
          && theBoard.allowsMove(col) ) {
      theBoard.addMove( col, turn );
      if ( this.turn == 'X' ) {
        this.turn = 'O';
        color = "Green";
        status.setText(this.color+"'s turn!");
      }
      else {
        this.turn = 'X';
        color = "Pokeball";
        status.setText(this.color+"'s turn!");
      }
    }
    if (theBoard.isFull() || !theBoard.allowsMove(col)) 
      status.setText("Next move in that column is illegal... it's "+this.color+ 
                     "'s turn!");
    
    if (theBoard.winsFor('X'))
      status.setText("Pokeball is the winner!! :D :D Click New Game to play again :)");
    else if (theBoard.winsFor('O'))
      status.setText("Green is the winner!! :D :D Click New Game to play again :)");
    else if (!theBoard.winsFor('X') && !theBoard.winsFor('O') && theBoard.isFull() )
      status.setText("It's a tie! Click New Game to play again :)");
    
    repaint();
  }
  
//****************************************************
  /** An inner class that represents one graphical cell in the connect 4 board.
    * Each cell keeps track of what row and column it is in.
    * These are the objects that will listen for mouse clicks.
    * Because they are an inner class, they have access to all of
    * the methods in the ConnectFour outer class.  
    * 
    * Notice that a BoardCell object IS A JPanel, so it can be added directly
    * to a ConnectFour object (which IS A JFrame).  You can also add listeners
    * to JPanels (and BoardCells, since they are JPanels), which will be useful
    * to detect where the user wants to play.  */
  class BoardCell extends JPanel {
    /** The row in which this BoardCell appears in the board. */
    private int row;
    /** The column in which this BoardCell appears in the board. */
    private int column;    
    
    /** Create a new BoardCell object at row r and column c. 
      * The constructor is the right place to add the PlayListener too. */
    BoardCell( int r, int c ) {
      row = r; 
      column = c;
      this.addMouseListener( new PlayListener() );
    }
    
    /** Return the preferred size for this BoardCell */
    public Dimension getPreferredSize() {
      // Just a suggestion. Feel free to change it if you want.  
      return new Dimension( 80, 80 );
    }
    
    // Paint the BoardCell.  Note that this method should paint empty cells 
    // in one color, cells filled with and 'X' with another color, and cells 
    // filled with an 'O' in a third color.  
    //
    // IMPORTANT: You will need to refer to the 
    // theBoard object in the ConnectFour class to get the contents of the 
    // underlying cell that this visual cell represents.  If this does not make
    // sense to you, seek help now.
    //
    // My suggestion here is to paint the whole background of the cell with a 
    // solid rectangle, and then paint a circle (oval) on top to represent the space
    // or the checker.  You'll want to make your circle slightly smaller than
    // your rectangle.  Of course, feel free to get creative, add shadow, etc.
    protected void paintComponent( Graphics g ) { 
      super.paintComponent( g );
      int width = getWidth(); 
      int height = getHeight();
      // empty space
      if ( theBoard.getContents(row,column) == ' ' ) {
        g.setColor( new Color(100,0,175) );
        g.fillRect(0,0,width,height);
        g.setColor( new Color(220,220,255) );
        g.fillOval(width/20,height/20,(11*width)/12,(11*height)/12);
      }
      // X winner
      else if ( theBoard.winsFor('X') && theBoard.getContents(row,column) == 'X' ) {
        g.setColor( new Color(100,0,175) );
        g.fillRect(0,0,width,height);
        g.setColor( new Color(255,0,0) );
        g.fillOval(width/20,height/20,(11*width)/12,(11*height)/12);
        g.setColor( new Color(255,255,255) );
        g.fillArc(width/15,height/4,(9*width)/10,(3*height)/4,0,-180);
        g.setColor( new Color(0,0,0) );
        g.fillRect(width/15,height/2,width-width/10,height/8);
        g.fillOval(width/3+width/25,height/3+height/13,width/3,height/3);
        g.setColor( new Color(0,0,200) );
        g.fillOval(width/3,height/3,width/10,height/10);
        g.fillOval((2*width)/3,height/3,width/10,height/10);
        g.fillArc(width/3,height/4,(2*width)/5,height/2,0,-180);       
      }
      // O winner
      else if ( theBoard.winsFor('O') && theBoard.getContents(row,column) == 'O' ) {
        g.setColor( new Color(100,0,175) );
        g.fillRect(0,0,width,height);
        g.setColor( new Color(200,230,200) );
        g.fillOval(width/20,height/20,(11*width)/12,(11*height)/12);
        g.setColor( new Color(160,230,160));
        g.fillOval(width/10,height/10,(10*width)/12,(10*height)/12);
        g.setColor( new Color(130,230,130));
        g.fillOval(width/6,height/6,(8*width)/12,(8*height)/12);
        g.setColor( new Color(100,230,100));
        g.fillOval(width/4,height/4,width/2,height/2);
        g.setColor( new Color(60,230,60));
        g.fillOval(width/3,height/3,width/3,height/3);
        g.setColor( new Color(40,230,40));
        g.fillOval(width/3+width/20,height/3+height/20,width/5,height/5);
        g.setColor( new Color(0,0,0) );
        g.fillOval(width/3,height/3,width/18,height/18);
        g.fillOval((2*width)/3,height/3,width/18,height/18);
        g.fillArc(width/3,height/4,(2*width)/5,height/2,0,-180);
      }
      // X checker
      else if ( theBoard.getContents(row,column) == 'X' ) {
        g.setColor( new Color(100,0,175) );
        g.fillRect(0,0,width,height);
        g.setColor( new Color(255,0,0) );
        g.fillOval(width/20,height/20,(11*width)/12,(11*height)/12);
        g.setColor( new Color(255,255,255) );
        g.fillArc(width/15,height/4,(9*width)/10,(3*height)/4,0,-180);
        g.setColor( new Color(0,0,0) );
        g.fillRect(width/15,height/2,width-width/10,height/8);
        g.fillOval(width/3+width/25,height/3+height/13,width/3,height/3);
        g.setColor( new Color(255,255,255) );
        g.fillOval(width/3+width/9,height/3+height/7,width/5,height/5);
      }
      // O checker
      else if ( theBoard.getContents(row,column) == 'O' ) {
        g.setColor( new Color(100,0,175) );
        g.fillRect(0,0,width,height);
        g.setColor( new Color(200,230,200) );
        g.fillOval(width/20,height/20,(11*width)/12,(11*height)/12);
        g.setColor( new Color(160,230,160));
        g.fillOval(width/10,height/10,(10*width)/12,(10*height)/12);
        g.setColor( new Color(130,230,130));
        g.fillOval(width/6,height/6,(8*width)/12,(8*height)/12);
        g.setColor( new Color(100,230,100));
        g.fillOval(width/4,height/4,width/2,height/2);
        g.setColor( new Color(60,230,60));
        g.fillOval(width/3,height/3,width/3,height/3);
        g.setColor( new Color(40,230,40));
        g.fillOval(width/3+width/20,height/3+height/20,width/5,height/5);
        
      }
    }
    
    /** This is the listener that will handle clicks on the board cell.
      * You will not need to change this code at all, but you should understand 
      * what is going on.
      * */
//****************************************************
    class PlayListener implements MouseListener {
      /** Inform the ConnectFour object that the corresponding column has been
        * clicked */
      public void mouseClicked( MouseEvent e ) {
        // We just need to tell the board to play a checker in the appropriate
        // column.  column refers to the instance variable in the BoardCell
        // object.  This method calls the makeMove method in the ConnectFour class. 
        makeMove( column );
      }
      public void mousePressed( MouseEvent e ) {}
      public void mouseReleased( MouseEvent e ) {}
      public void mouseEntered( MouseEvent e ) {}
      public void mouseExited( MouseEvent e ) {}
    }
  }
  
  /** This is the main method that runs the ConnectFour game*/
  public static void main( String args[] ){
    new ConnectFour();
  }
}