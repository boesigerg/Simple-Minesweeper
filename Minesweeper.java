package minesweeper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static java.awt.Color.*;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.ImageIcon;

public class Minesweeper extends JFrame{
    
    private static int GRID_ROWS = 13;
    private static int GRID_COLS = 28;
    private static int NUM_MINES = 88;
    public static int MINES_LEFT = NUM_MINES;
    
    private static final int[][] PROPOSAL = new int[][]{
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,9,0,0,0,9,0,0,9,9,0,0,9,9,9,0,0,9,9,9,0,0,9,0,0,0,9,0},
        {0,9,9,0,9,9,0,9,0,0,9,0,9,0,0,9,0,9,0,0,9,0,0,9,0,9,0,0},
        {0,9,0,9,0,9,0,9,0,0,9,0,9,0,0,9,0,9,0,0,9,0,0,0,9,0,0,0},
        {0,9,0,0,0,9,0,9,9,9,9,0,9,9,9,0,0,9,9,9,0,0,0,0,9,0,0,0},
        {0,9,0,0,0,9,0,9,0,0,9,0,9,0,0,9,0,9,0,0,9,0,0,0,9,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,9,0,0,0,9,0,9,9,9,9,0,0,9,9,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,9,9,0,9,9,0,9,0,0,0,0,9,0,0,9,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,9,0,9,0,9,0,9,9,9,0,0,0,0,9,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,9,0,0,0,9,0,9,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,9,0,0,0,9,0,9,9,9,9,0,0,0,9,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
    };
    
    private static JFrame frame;
    private static Container pane, hud, grid;
    private static JTextField timer;
    private static JButton reset;
    
    public static Square[][] mineField;
    public static boolean gameOver = false;
    private static boolean continueGame = true;

    public static void main(String[] args) {
        while(continueGame){
            Minesweeper game = new Minesweeper();
            while(!gameOver & MINES_LEFT > 0){
                //Listen for button presses
                //Update timer
                try{
                    Thread.sleep(10);
                }catch(InterruptedException ex){
                    Thread.currentThread().interrupt();
                }
            }
            if(gameOver){
                for(Square[] sqrRow : mineField){
                    for(Square sqr : sqrRow){
                        sqr.setEnabled(false);
                        if(sqr.getValue() == 9){
                            sqr.setBackground(Color.RED);
                            sqr.setText("X");
                        }else{
                            sqr.setText(Integer.toString(sqr.getValue()));
                        }
                    }
                }
                try{
                    Thread.sleep(3600000);
                }catch(InterruptedException ex){
                    Thread.currentThread().interrupt();
                }
                int again = JOptionPane.showConfirmDialog(null, "Game Over. Try Again?", null, JOptionPane.YES_NO_OPTION);
                if(again != JOptionPane.YES_OPTION){
                    continueGame = false;
                }
            }else{
                int again = JOptionPane.showConfirmDialog(null, "You Win! Play Again?", null, JOptionPane.YES_NO_OPTION);
                if(again != JOptionPane.YES_OPTION){
                    continueGame = false;
                }
            }
            MINES_LEFT = NUM_MINES;
            game.dispose();
        }
        System.exit(0);
    }
    
    public Minesweeper(){
        //Set up base GUI window
        setTitle("Minesweeper");
        setSize(50*GRID_COLS,50*GRID_ROWS);
        setLocationRelativeTo(null);
        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());
        
        //TODO: Set up heads up display
//        hud = new Container();
//        hud.setLayout(new FlowLayout());
//        timer = new JTextField("Time: ");
//        reset = new JButton("Reset");
//        hud.add(timer);
//        hud.add(reset);
//        pane.add(hud, BorderLayout.PAGE_START);
        
        grid = buildMineField(GRID_ROWS, GRID_COLS, NUM_MINES);
        pane.add(grid, BorderLayout.CENTER);
        
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        gameOver = false;
    }

    private static JPanel buildMineField(int rows, int cols, int NUM_MINES) {
        JPanel grid = new JPanel();
        grid.setLayout(new GridLayout(rows, cols, 0, 0));
        mineField = new Square[rows][cols];
        
        for(int i = 0; i<rows; i++){
            for(int j = 0; j<cols; j++){
                mineField[i][j] = new Square();
                mineField[i][j].addMouseListener(new SquareListener());
                mineField[i][j].setCoords(i,j);
                grid.add(mineField[i][j]);
            }
        }
        
        //plantMines(mineField, rows, cols, NUM_MINES);
        plantProposal(mineField);
        setFieldValues(mineField, rows, cols);
        
        return grid;
    }
    
    private static void plantProposal(Square[][] mineField){
        for(int i = 0; i<GRID_ROWS; i++){
            for(int j = 0; j<GRID_COLS; j++){
                mineField[i][j].setValue(PROPOSAL[i][j]);
            }
        }
    }
    
    private static void plantMines(Square[][] mineField,int rows, int cols, int numMines){
        Random rand = new Random();
        int x,y;
        while(numMines > 0){
            x = rand.nextInt(rows);
            y = rand.nextInt(cols);
            
            if(mineField[x][y].getValue() != 9){
                mineField[x][y].setValue(9);
                numMines--;
            }
        }
    }
    
    private static void setFieldValues(Square[][] mineField, int rows, int cols){
        for(int i = 0; i<rows; i++){
            for(int j = 0; j<cols; j++){
                if(mineField[i][j].getValue() != 9){
                    int minesFound = 0;
                    for(int k = i-1; k<i+2; k++){
                        for(int m = j-1; m<j+2; m++){
                            try{
                                if (mineField[k][m].getValue() == 9){
                                    minesFound++;
                                }
                            }catch (IndexOutOfBoundsException e){
                                //do nothing
                            }
                        }
                    }
                    mineField[i][j].setValue(minesFound);
                }
            }
        }
    }
    
}