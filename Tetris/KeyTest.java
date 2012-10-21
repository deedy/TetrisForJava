import java.awt.*; 
import java.awt.event.*;
 
public class KeyTest extends Frame implements KeyListener{  
  TextField t1;
  Label l1;
  public KeyTest(String s ) {  
      super(s); 
      Panel p =new Panel();
      l1 = new Label ("Key Listener!" ) ;
      p.add(l1);  
      add(p);
      addKeyListener ( this ) ; 
      setSize ( 200,100 );
      setVisible(true);
      
  }  
  public void keyTyped ( KeyEvent e ){  
  l1.setText("Key Typed");
 }  
  public void keyPressed ( KeyEvent e){  
  l1.setText ( "Key Pressed" ) ; 
  }  
  public void keyReleased ( KeyEvent e ){  
  l1.setText( "Key Released" ) ; 
  }  
  public static void main (String[]args ){  
  new KeyTest( "Key Listener Tester" ) ; 
 }  
  }  
