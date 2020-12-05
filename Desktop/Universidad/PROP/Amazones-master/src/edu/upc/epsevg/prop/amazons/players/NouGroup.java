/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.epsevg.prop.amazons.players;

import edu.upc.epsevg.prop.amazons.CellType;
import edu.upc.epsevg.prop.amazons.GameStatus;
import edu.upc.epsevg.prop.amazons.IAuto;
import edu.upc.epsevg.prop.amazons.IPlayer;
import edu.upc.epsevg.prop.amazons.Move;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author alpez
 */
public class NouGroup implements IPlayer, IAuto {

    private String name;

    public NouGroup(String name) {
        this.name = name;
    }

    @Override
    public void timeout() {
        // Nothing to do! I'm so fast, I never timeout 8-)
    }
    
    public Move move(GameStatus s) {
        
        //Obtenemos el color que representa al jugador
        CellType color = s.getCurrentPlayer();
        
        //Obtenemos las casillas vacias del tablero.
        ArrayList<Point> emptyCells = getEmptyCells(s);
        
        int qn = s.getNumberOfAmazonsForEachColor();
        
        //Por cada Amazona del jugador...
        ArrayList<Point> pendingAmazons = new ArrayList<>();
        for (int q = 0; q < qn; q++) {
            //Obtenemos una amazona
            Point amazona = s.getAmazon(color, q);
            
            //Obtenemos todos los movimientos possibles de es amazona...
            ArrayList<Point> possiblesMovimientos = s.getAmazonMoves(amazona, true);
            
            // Para cada possible movimiento de esa amazona...
            for(int i = 0; i < possiblesMovimientos.size(); i++){
           
                //Creamos un tablero con el movimiento de la amazona realizado...
                GameStatus s2 = new GameStatus(s);
                s2.moveAmazon(amazona, possiblesMovimientos.get(i));
                boolean trobat = false;
                
                //Modificamos la lista emptyCells
                for(int k = 0; k < emptyCells.size() && !trobat; k++){
                    if (emptyCells.get(k) == possiblesMovimientos.get(i)){
                       emptyCells.add(k,amazona);
                        trobat = true; 
                    }
                }
            
                //Por cada possible casilla en la que tirar la flecha...
                for(int r = 0; r < emptyCells.size(); r++ ){
                    GameStatus s3 = new GameStatus(s2);
                    //Tiramos la flecha
                    System.out.println("peta aki");
                    s3.placeArrow(emptyCells.get(r));
                    System.out.println("esto no deberai salir");
                
                    //LLamamos a minimax
                    
                    ArrayList<Point> emptyCells2 = new ArrayList<>(emptyCells);
                    
                    emptyCells2.remove(r);
                    
                    //min(s3,alfa,beta,"PLAYER2"emptyCells2,)
                    
                    
                    
                }

            }
            
            
        }
        return null;
    }
    
    public ArrayList<java.awt.Point> getEmptyCells(GameStatus s) {
        
        ArrayList<Point> emptyCells = new ArrayList<>();
        
        //Recorremos el tablero buscando casillas libres.
        for(int x = 0; x < s.getSize(); x ++) {
            for ( int y = 0; y < s.getSize(); y++) {
                if ( s.getPos(x,y) == CellType.EMPTY) {
                    //Guardamos el punto con casilla libre en el array.
                    Point p = new Point(x,y);
                    emptyCells.add(p);
                }              
            }
        }
        
         return emptyCells;    
    }

    @Override
    public String getName() {
        return "Player(" + name + ")";
    }
}
