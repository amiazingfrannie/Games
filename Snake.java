package game;

import graphics.G;
import graphics.G.V;
import graphics.Window;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.Timer;

public class Snake extends Window implements ActionListener {

  public static Color cFood = Color.green, cSnake = Color.blue, cBad = Color.red;
  public static Cell.List snake = new Cell.List();
  public static Cell food = new Cell();
  public static Cell crash = null;
  public static Timer timer;
  public Snake(){
    super("Snake", 1000, 700);
    startGame();
    timer = new Timer(200, this);
    timer.start();
  }

  @Override
  public void actionPerformed(ActionEvent e) {moveSnake(); repaint();}

  public void startGame(){
    snake.clear(); //empty list
    snake.iHead = 0; // initialized
    snake.growList();
    food.rndLoc(); // set random loc for food
    crash = null; // clear the former crash
  }

  public void keyPressed(KeyEvent ke){
    int vk = ke.getKeyCode();
    if(vk == KeyEvent.VK_LEFT){snake.direction = G.LEFT;}
    if(vk == KeyEvent.VK_RIGHT){snake.direction = G.RIGHT;}
    if(vk == KeyEvent.VK_UP){snake.direction = G.UP;}
    if(vk == KeyEvent.VK_DOWN){snake.direction = G.DOWN;}
    if(vk == KeyEvent.VK_SPACE){startGame();}
  }

  public static void moveSnake(){
    if(crash != null){return;} //once crashed, game over
    snake.move();
    Cell head = snake.head();
    if(head.hits(food)){snake.growList();food.rndLoc();return;}
    if(!head.inBoundary()){crash = head; snake.stop(); return;}
    if(snake.hits(head)){crash = head; snake.stop(); return;}
  }

  public void paintComponent(Graphics g){
    G.Clear(g); //clear the bg
    g.setColor(cSnake); snake.show(g);
    g.setColor(cFood); food.show(g);
    if(crash!=null){g.setColor(cBad); crash.show(g);}
    Cell.drawBoundary(g);
  }

  public static void main(String [] args){
    (PANEL = new Snake()).launch();
  }

  // ------ cell -------//
  public static class Cell extends G.V{
    //G.V is the location where the cell is
    public static final int xM = 35, yM= 35, nX = 30, nY = 20, W = 30;
    public Cell() {
      super(G.rnd(nX), G.rnd(nY)); //generate random loc x and y
    }

    public Cell(Cell c){super(c.x, c.y);}

    public void rndLoc(){set(G.rnd(nX), G.rnd(nY));}

    public void show(Graphics g){
      g.fillRect(xM+x*W, yM+y*W, W, W);
    }

    public boolean hits(Cell c){
      return c.x == x && c.y == y; // whether this cell hits the other one
    }

    public boolean inBoundary(){
      return x >= 0 && x < nX && y >= 0 && y < nY;
    }

    public static void drawBoundary(Graphics g){
      int xMax = xM+nX*W, yMax = yM+nY*W;
      g.setColor(Color.black);
      g.drawLine(xM, yM, xM, yMax); //vertical line
      g.drawLine(xMax, yM, xMax, yMax);
      g.drawLine(xM, yM, xMax, yM); //horizontal line
      g.drawLine(xM, yMax, xMax, yMax);
    }

    //---------- list ------------// Snake.cell.list
    public static class List extends ArrayList<Cell>{

      public static G.V STOPPED = new G.V(0, 0); // 0, 0 not moving
      public G.V direction = STOPPED;
      public int iHead = 0; // head of snake when starts the game
      public void show(Graphics g){
        for(Cell c: this){c.show(g);}
      }
      public void growList(){
        Cell cell = (size() == 0) ? new Cell() : new Cell(get(0));
        add(cell); // has nothing in front of a function, it's located to the class
      } // empty list, create a new cell, otherwise, get the first cell on the list

      public void move(){
        if(direction == STOPPED){return;}
        int iTail = (iHead+1) % size();
        //if iTail > size(), illegal
        // if iHead+1 = size(), no reminder, iTail = 0;
        Cell tail = get(iTail); // set tail as the new head of the snake
        tail.set(get(iHead)); // get(iHead) is the coords
        tail.add(direction);
        iHead = iTail; // new head id former tail
      }

      public Cell head(){return get(iHead);}
      public void stop(){direction = STOPPED;}
      public boolean hits(Cell t){ //if c hits target cell t
        for(Cell c : this){ if(c != t && c.hits(t)) {return true;}}
        return false;
      }


    }
  }
}
