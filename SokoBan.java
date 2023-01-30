package game;

import graphics.G;
import graphics.G.V;
import graphics.Window;
import java.awt.*;
import java.awt.event.KeyEvent;

public class SokoBan extends Window {

  public static Board board = new Board();
  public static G.V LEFT = new G.V(-1, 0), RIGHT = new G.V(1, 0);
  public static G.V UP = new G.V(0, -1),DOWN = new G.V(0, 1);
  public SokoBan(){
    super("SokoBan", 1000, 700);
    board.loadStringArray(puzz2);
  }

  public static void main(String[] args){
    (PANEL = new SokoBan()).launch();
  }

  public void paintComponent(Graphics g){
    G.Clear(g); // clear the bg
    board.show(g);
    if(board.done()){
      g.setColor(Color.black);
      g.drawString("U win!!", 20, 30);
    }
  }

  public void keyPressed(KeyEvent ke){
    int vk = ke.getKeyCode(); // keycode is a number working on keyboard
    if (vk == KeyEvent.VK_LEFT){ // if it's left arrow
      board.go(LEFT);}
    if (vk == KeyEvent.VK_UP){ board.go(UP);}
    if (vk == KeyEvent.VK_DOWN){ board.go(DOWN);}
    if (vk == KeyEvent.VK_RIGHT){ board.go(RIGHT);}
    if (vk == KeyEvent.VK_SPACE){
      board.clear();
      board.loadStringArray(puzz2);
    }
    repaint();
  }

  //-------Board --------//
  public static class Board{
    public static final int N = 25;
    public static final int xM = 50, yM = 50, W= 40;

    public static String boardStates = " WPCGgE"; // Space, wall, player, container, Goal, , E error
    public static Color[] colors = {Color.white, Color.darkGray, Color.green, Color.orange,
                                    Color.cyan, Color.blue, Color.red}; // initialize an array in java

    public char[][] b = new char[N][N]; // create the board
    public G.V player = new G.V(0,0);
    public boolean onGoal = false; // if it's moving on to goal
    public G.V dest = new G.V(0, 0); // set destination
    public Board(){ clear();}

    // make the board white
    private void clear() {
      for (int r = 0; r < N; r++){
        for (int c =0; c < N; c++){
          b[r][c] = ' '; // first time creating, it's all white board
        }
      }
      player.set(0, 0); // set initial coords
      onGoal = false;
    }
    public void show(Graphics g) {
      //fill the color
      for (int r = 0; r < N; r++) {
        for (int c = 0; c < N; c++) {
          int index = boardStates.indexOf(b[r][c]);
          g.setColor(colors[index]);
          g.fillRect(xM + c*W, yM + r*W, W, W);
        }
      }
    }
    public char ch (G.V v){return b[v.y][v.x];} //getter
    public void set(G.V v,  char c){b[v.y][v.x] = c;} //setter
    public void movePerson(){
      boolean res = ch(dest) == 'G'; // whether the person is in Gold state
      set(player, onGoal ? 'G' : ' '); //if it's not onGoal, set value for space player left
      set(dest, 'P'); // where player moves
      player.set(dest);
      onGoal = res;
    }

    public void go(G.V v) {
      System.out.println("player " + player.x + " " + player.y);
      dest.set(player); //
      dest.add(v); //direction player is going, actual destination
      System.out.println("player " + dest.x + " " + dest.y);
      if (ch(dest) == 'W' || ch(dest) == 'E'){ // can't walk onto
        return;}
      if (ch(dest) == ' ' || ch(dest) == 'G'){ movePerson(); return;}
      if (ch(dest) == 'C' || ch(dest) == 'g'){
        dest.add(v);
        if (ch(dest) != ' ' && ch(dest) != 'G'){return;} // person can move the box, alter the person and box locs
        set(dest, (ch(dest) == 'G') ? 'g' : 'C'); // put box at final spot
        dest.set(player);
        dest.add(v);
        set(dest, (ch(dest) == 'g' ? 'G' : ' ')); // reset the space box left
        movePerson();
      }
    }

    public void loadStringArray(String[] a){
      for (int r = 0; r < a.length; r++){ //length is a member of the array class
        String s = a[r];
        for (int c =0; c < s.length(); c++){ // in string, have to call length()
          char ch = s.charAt(c);
          b[r][c] = (boardStates.indexOf(ch) > -1) ? ch : 'E';
          if (ch == 'P'){ // if it's player
            player.x = c;
            player.y = r;
          }
        }
      }
    }
    public boolean done(){
      for(int r = 0; r < N; r++){
        for(int c = 0; c < N; c++){
          if(b[r][c] == 'C'){ return false;}
        }
      }
      return true;
    }

  }

  public static String[] puzz1 = {
      "  WWWWW",
      "WWW   W",
      "WGPC  W",
      "WWW CGW",
      "WGWWC W",
      "W W G WW",
      "WC gCCGW",
      "W   G  W",
      "WWWWWWWW"
  };

  public static String[] puzz2 = {
      "WWWWWWWWWWWW",
      "WGG  W     WWW",
      "WGG  W C  C  W",
      "WGG  WCWWWW  W",
      "WGG    P WW  W",
      "WGG  W W  C WW",
      "WWWWWW WWC C W",
      "  W C  C C C W",
      "  W    W     W",
      "  WWWWWWWWWWWW"
  };


}
