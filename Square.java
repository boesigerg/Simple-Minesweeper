package minesweeper;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

public class Square extends JButton{
    
    private int value;
    private boolean isFlagged;
    private int[] coords;
    
    public Square(){
        this.value = 0;
        this.isFlagged = false;
        this.coords = new int[2];
    }
    
    public void setValue(int x){
        this.value = x;
    }
    
    public int getValue(){
        return this.value;
    }
    
    public int[] getCoords(){
        return coords;
    }
    
    public void setCoords(int x, int y){
        this.coords[0] = x;
        this.coords[1] = y;
    }
    
    public boolean isFlagged(){
        return this.isFlagged;
    }

    public void flagSquare(){
        this.isFlagged = !this.isFlagged;
    }
}

class SquareListener extends MouseAdapter{

    @Override
    public void mouseClicked(MouseEvent me) {
        Square sqr = (Square) me.getSource();
        if(me.getButton() == MouseEvent.BUTTON1){
            sqr.setText(Integer.toString(sqr.getValue()));
            sqr.setEnabled(false);
            if(sqr.getValue() == 9){
                Minesweeper.gameOver = true;
                sqr.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
            }
            else if(sqr.getValue() == 0){
                int[] coords = sqr.getCoords();
                clearZero(coords[0], coords[1]);
            }
        }else if(me.getButton() == MouseEvent.BUTTON3){
            sqr.flagSquare();
            if(sqr.isFlagged()){
                sqr.setText("!");
                if(sqr.getValue() == 9){
                    Minesweeper.MINES_LEFT--;
                }else{
                    Minesweeper.MINES_LEFT++;
                }
            }else{
                sqr.setText("");
                if(sqr.getValue() == 9){
                    Minesweeper.MINES_LEFT++;
                }else{
                    Minesweeper.MINES_LEFT--;
                }
            }
        }
    }
    
    private void clearZero(int x, int y){
        for(int i = x-1; i<x+2; i++){
            for(int j = y-1; j<y+2; j++){
                try{
                   Square sqr = Minesweeper.mineField[i][j];
                   if (sqr.isEnabled()){
                        sqr.setText(Integer.toString(sqr.getValue()));
                        sqr.setEnabled(false);
                        if (sqr.getValue() == 0){
                            clearZero(i, j);
                        }
                    }
                }catch (ArrayIndexOutOfBoundsException e){
                    //do nothing
                }
            }
        }
    }
}