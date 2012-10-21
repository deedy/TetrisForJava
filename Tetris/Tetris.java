import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Tetris extends GraphicsProgram implements KeyListener {
    
    /** Number of bricks per row */
    public static  int TILE_COLUMNS= 16;
    /** Number of rows of bricks, in range 1..10. */
    public static  int TILE_ROWS= 32;
    /** Width of a brick */
    public static int TILE_SIZE=20;
    /** Width of the game display (al coordinates are in pixels) */
    public static final int GAME_WIDTH= 480;
    /** Height of the game display */
    public static final int GAME_HEIGHT= 700;
    /** random number generator */
    private RandomGenerator rgen= new RandomGenerator();
    private static Tetronimo ACTIVE;
    
    private static final int BOARD_WIDTH=TILE_SIZE*TILE_COLUMNS;
    private static final int BOARD_HEIGHT=TILE_SIZE*TILE_ROWS;
    private static GLine[] BOARD=new GLine[4];
    //private static GRect BOARD;
    /** Run the program as an application.
      */
    
    public static void main(String[] args) {

        String[] sizeArgs= {"width=" + GAME_WIDTH, "height=" + GAME_HEIGHT};
        new Tetris().start(sizeArgs);
    }
    
    /** Run the Minesweeper program. */
    public void run() {
        addKeyListeners ();
        setup();
        
        play();
    }
    
    /** Set Up all the components of Minesweeper */
    public void setup () {
        addBoard();
            
        setUpPiece();
    }
    private void addBoard() {
        BOARD[0]=new GLine(0,0, TILE_SIZE*TILE_COLUMNS, 0);
        BOARD[1]=new GLine(TILE_SIZE*TILE_COLUMNS,0, TILE_SIZE*TILE_COLUMNS, TILE_SIZE*TILE_ROWS);
        BOARD[2]=new GLine (0,0,0,TILE_SIZE*TILE_ROWS);
        BOARD[3]=new GLine (0,TILE_SIZE*TILE_ROWS, TILE_SIZE*TILE_COLUMNS, TILE_SIZE*TILE_ROWS);
        for (int i=0;i<4;i++) 
            add (BOARD[i]);
    }
    
    private void addPiece() {
        ACTIVE= new Tetronimo (rgen.nextInt(1,7), rgen.nextInt(0,3));
        move (ACTIVE, 5, 0);
        add(ACTIVE);
        
    }
    
    /**Adds a tetronimo to the board */
    private void setUpPiece() {
        ACTIVE= new Tetronimo (rgen.nextInt(1,7), rgen.nextInt(0,3));
        move (ACTIVE, 5, 0);
        pause(300);
        add(ACTIVE);
        
    }
    public void play () {
        while (true) {
        pause (100);
        boolean b=move (ACTIVE, 0,1);
        if (!b) {
            
            setUpPiece();
        }
        }
    }
    
    public void keyTyped(KeyEvent ke) {
    }
    public void keyReleased(KeyEvent ke) {
    }

    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_UP) {} 
        
        else if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
        move (ACTIVE, 0,2);
        }
        
        else if (ke.getKeyCode() == KeyEvent.VK_Z) {
            rotate (ACTIVE, false);
        }
        
        else if (ke.getKeyCode() == KeyEvent.VK_X) {
            rotate (ACTIVE, true);
        }
         else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
           move (ACTIVE, 1, 0);
        }
        else if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
           move (ACTIVE, -1, 0);
        }
        
        
    }

    public boolean move (Tetronimo t, int i, int j) {
        //System.out.println (t.getPos().getX()+" "+t.getPos().getY());
        if (!(t.getLeftSkirt().getX()+i>=0))
            i=(int)-t.getLeftSkirt().getX();
        if (!(t.getRightSkirt().getX()+i<TILE_COLUMNS))
            i=TILE_COLUMNS-(int)t.getRightSkirt().getX()-1;
        //System.out.println ("Is there a collision?"+isColliding(t,i,j));
        if (isColliding(t,i,j)==false && t.getBottomSkirt().getY()+j<TILE_ROWS) {
            for (int x=0;x<4;x++) {
                (t.getPiece()[x]).move (i*TILE_SIZE,j*TILE_SIZE);
            }
        return true;
        }
        return false;
    }
    
    public boolean isColliding (Tetronimo t, int x, int y) {
        GPoint [] p= t.getPositions();
        for (int i=0;i<4;i++) {
            GObject g=getElementAt ((p[i].getX()+x+0.5)*TILE_SIZE, (p[i].getY()+y+0.5)*TILE_SIZE);
            //System.out.println ((p[i].getY()+y+0.5));
            if ((g instanceof G3DRect && !t.contains((G3DRect)g)) || (p[i].getY()+y+0.5)>TILE_ROWS) {
                isSolved (ACTIVE);
                pause (100);

                return true;
            }
        }
        
        return false;
            
   
    }
    
    public void isSolved (Tetronimo t) {
        GPoint [] p= t.getPositions();
        for (int i=0;i<4;i++) {
           int temp=(int)p[i].getY();
           boolean done=true;
           //System.out.println (t);
            for (int j=0;j<TILE_COLUMNS;j++) {
                //System.out.println (j+" "+!(getElementAt((j+0.5)*TILE_SIZE, (temp+0.5)*TILE_SIZE) instanceof G3DRect));
                if (!(getElementAt((j+0.5)*TILE_SIZE, (temp+0.5)*TILE_SIZE) instanceof G3DRect))
                    done=false;
           // break;
            }
            if (done==true) {
                deleteRow (temp);
                movedown (temp);
                for (int z=0;z<4;z++) {
                    p[z].setLocation(p[z].getX(),p[z].getY()+1);
                    i=-1;
                }
            
            }
        }
    }
        
    public void deleteRow (int k) {
        for (int i=0;i<TILE_COLUMNS;i++) {
            remove(getElementAt((i+0.5)*TILE_SIZE, (k+0.5)*TILE_SIZE));
        }
    }
    public void movedown (int k) {
        for (int i=k-1;i>=0;i--) {
            for (int j=0;j<TILE_COLUMNS;j++) {
                GObject g=getElementAt((j+0.5)*TILE_SIZE, (i+0.5)*TILE_SIZE);
                if (g!=null && g instanceof G3DRect)
                    ((G3DRect)g).move(0,TILE_SIZE);
            }
        }
    }
       
    
    public void rotate (Tetronimo t, boolean right) {
        remove(t);
        GPoint g= t.getPos();
        t.rotate(right);
        move (t,(int)g.getX(),(int)g.getY());
        add(t);
    }
    public void add (Tetronimo t)
    {for (int i=0;i<4;i++)
            add (t.getPiece()[i]);
    }
    
    public void remove (Tetronimo t)
    {for (int i=0;i<4;i++)
            remove (t.getPiece()[i]);
    }
    /** Creates the playing Board for Tetris */
    private void createBoard () {
    }
    
}

/** An instance is a Brick */
class Tetronimo extends Tetris {
    private int PieceType;

    private  G3DRect block[]=new G3DRect[4];
    private  int rot=0;
    private static int [][][][] Piece = {{{}},
        {   {{0,0},{0,1},{1,0},{1,1}}  ,  {{0,0},{0,1},{1,0},{1,1}}  ,  {{0,0},{0,1},{1,0},{1,1}}  ,  {{0,0},{0,1},{1,0},{1,1}}  },  //Square
        {   {{0,0},{1,0},{2,0},{3,0}}  ,  {{2,0},{2,1},{2,2},{2,3}}  ,  {{0,1},{1,1},{2,1},{3,1}}  ,  {{1,0},{1,1},{1,2},{1,3}}  }, //Skinny
        {   {{0,1},{1,1},{1,0},{2,1}}  ,  {{1,2},{1,1},{1,0},{2,1}}  ,  {{0,1},{1,1},{1,2},{2,1}}  ,  {{0,1},{1,0},{1,1},{1,2}}  }, //T
        {   {{0,1},{1,1},{1,0},{2,0}}  ,  {{2,1},{1,1},{1,0},{2,2}}  ,  {{0,2},{1,1},{1,2},{2,1}}  ,  {{0,0},{0,1},{1,1},{1,2}}  }, //Dog1
        {   {{0,0},{1,1},{1,0},{2,1}}  ,  {{0,1},{1,1},{1,0},{0,2}}  ,  {{0,1},{1,1},{1,2},{2,2}}  ,  {{0,1},{0,2},{1,0},{1,1}}  }, //Dog2
        {   {{0,0},{0,1},{1,1},{2,1}}  ,  {{0,0},{0,1},{0,2},{1,0}}  ,  {{0,0},{1,0},{2,0},{2,1}}  ,  {{0,2},{1,0},{1,1},{1,2}}  }, //Peri1
        {   {{0,0},{0,1},{1,0},{2,0}}  ,  {{0,0},{1,0},{1,1},{1,2}}  ,  {{0,1},{1,1},{2,0},{2,1}}  ,  {{0,0},{0,1},{0,2},{1,2}}  },//Peri2
    };
    private static Color PieceCol []= {Color.white,Color.yellow,Color.cyan,Color.magenta,Color.green,Color.red,Color.blue,Color.orange};
    /** Constructor: a tetronimo piece from type 1 to 7 */
    public Tetronimo(int i) {
        PieceType=i;
        makePiece(PieceType);
    }
    
    public Tetronimo(int i, int rot) {
        PieceType=i;
        this.rot=rot;
        makePiece(PieceType);
    }
    
    public String toString () {
        return ""+PieceType;
    }
    /** Square */
    public void makePiece(int i) {
        for (int x=0;x<4;x++) {
            block[x]=new G3DRect(Piece[i][rot][x][0]*TILE_SIZE,Piece[i][rot][x][1]*TILE_SIZE,TILE_SIZE,TILE_SIZE);
            block[x].setFillColor(PieceCol[i]);
            block[x].setFilled(true);
        }
    }
     public GPoint getPos() {
        GPoint p=new GPoint (((int)getPiece()[0].getX()/TILE_SIZE)-Piece[PieceType][rot][0][0], ((int)getPiece()[0].getY()/TILE_SIZE)-Piece[PieceType][rot][0][1]);
        
        return p;
    }
     public GPoint getLeftSkirt () {
         GPoint p=getPos();
         int x=Math.min (Math.min (Piece[PieceType][rot][0][0],Piece[PieceType][rot][1][0]),Math.min (Piece[PieceType][rot][2][0],Piece[PieceType][rot][3][0]));
         p.setLocation(p.getX()+x,p.getY());
         return p;
     }
      public GPoint getRightSkirt () {
         GPoint p=getPos();
         int x=Math.max (Math.max (Piece[PieceType][rot][0][0],Piece[PieceType][rot][1][0]),Math.max (Piece[PieceType][rot][2][0],Piece[PieceType][rot][3][0]));
         p.setLocation(p.getX()+x,p.getY());
         return p;
     }
      public GPoint getBottomSkirt () {
         GPoint p=getPos();
         int y=Math.max (Math.max (Piece[PieceType][rot][0][1],Piece[PieceType][rot][1][1]),Math.max (Piece[PieceType][rot][2][1],Piece[PieceType][rot][3][1]));
         p.setLocation(p.getX(),p.getY()+y);
         return p;
     }
    public void move(int i,int j) {
        for (int x=0;x<4;x++) {
            block[x].move(i*TILE_SIZE,j*TILE_SIZE);
        }
    }
    
    public void rotate (boolean right){
        for (int i=0;i<4;i++)
            remove (getPiece()[i]);
        if (right) {
        rot++;
        if (rot >= 4)
            rot=0;
        }
        else {
            rot--;
            if (rot<0)
                rot=3;
        }
        makePiece(PieceType);
    }
    
   
    public G3DRect[] getPiece() {
        return block;
    }
    public boolean contains (G3DRect gr) {
        for (int i=0;i<4;i++) {
            if (block[i]==gr)
                return true;
        }
        return false;
        
    }
    public GPoint[] getPositions() {
        GPoint []p=new GPoint[4];
        for (int i=0;i<4;i++) {
            p[i]=new GPoint((int) block[i].getX()/TILE_SIZE, (int) block[i].getY()/TILE_SIZE);
        }
        return p;
    }
         
    }
