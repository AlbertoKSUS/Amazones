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
import edu.upc.epsevg.prop.amazons.SearchType;
import edu.upc.epsevg.prop.amazons.heuristica;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author alpez
 */
public class NouGroup implements IPlayer, IAuto {

    private String name;
    CellType propi;
    CellType enemic;
    int count;

    public NouGroup(String name) {
        this.name = name;
    }

    @Override
    public void timeout() {
        // Nothing to do! I'm so fast, I never timeout 8-)
    }
    
    private void mostra(ArrayList<Point> n){
        for(int i = 0; i<n.size();++i){
            System.out.println("=>"+ i+ " pos " + n.get(i));
        }
    }
    /*private ArrayList<Point> MovimentsComplets(ArrayList<Point> mov){
        
    }*/
    public Move move(GameStatus s) {
        
        //Obtenemos el color que representa al jugador
        propi = s.getCurrentPlayer();
        //obtenemos el color que representa el enemigo
        if(propi == CellType.PLAYER1) enemic = CellType.PLAYER2;
        else enemic = CellType.PLAYER1;
        //iniciamos un valor a minimo para guardar el maximo de las heuristicas que se han obtenido
        int max = Integer.MIN_VALUE;
        //iniciamos un valor para contar el numero de nodos explorados
        count = 0;
        
        Move​ fi = null;
        
        //Obtenemos las casillas vacias del tablero.
        ArrayList<Point> emptyCells = getEmptyCells(s);
        //System.out.println("=> "+ emptyCells.size());
        
        int qn = s.getNumberOfAmazonsForEachColor();
        int contar = 0;
        //Por cada Amazona del jugador...
        //ArrayList<Point> pendingAmazons = new ArrayList<>();
        System.out.println("qn" + qn);
        for (int q = 0; q < qn; q++) {
            
            //Obtenemos una amazona
            Point amazona = s.getAmazon(propi, q);
            System.out.println("amazona =>" + amazona);
            
            //Obtenemos todos los movimientos possibles de es amazona...
            ArrayList<Point> possiblesMovimientos = s.getAmazonMoves(amazona, false);
            
            // Para cada possible movimiento de esa amazona...
            for(int i = 0; i < possiblesMovimientos.size(); i++){
                System.out.println("moviments =>" + possiblesMovimientos.size()+ " " + possiblesMovimientos.get(i));
                ++contar;
                //Creamos un tablero con el movimiento de la amazona realizado...
                GameStatus s2 = new GameStatus(s);
                s2.moveAmazon(amazona, possiblesMovimientos.get(i));
                boolean trobat = false;
                //lista para contemplas las emptycells de ese movimiento de amazona
                ArrayList<Point> emptyCells1 = new ArrayList<>(emptyCells);
                //Modificamos la lista emptyCells1
                for(int k = 0; k < emptyCells1.size() && !trobat; k++){
                    if (emptyCells1.get(k).x == possiblesMovimientos.get(i).x && emptyCells1.get(k).y == possiblesMovimientos.get(i).y ) {
                       emptyCells1.set(k,amazona);
                       trobat = true; 
                    }
                }
            
                //Por cada possible casilla en la que tirar la flecha...
                for(int r = 0; r < emptyCells1.size(); r++ ){
                    GameStatus s3 = new GameStatus(s2);
                    ++contar;
                   // System.out.println("peta aki");
                    s3.placeArrow(emptyCells1.get(r));
                   // System.out.println("esto no deberai salir");
                
                    //LLamamos a minimax
                    
                    ArrayList<Point> emptyCells2 = new ArrayList<>(emptyCells1);
                    emptyCells2.remove(r);
                    //sumamos uno porque en este punto hemos llegado a un nodo explorado;
                    count++;
                    int valor = min(s3,4 ,Integer.MIN_VALUE,Integer.MAX_VALUE,emptyCells2);
                    System.out.println("==>"+ valor);
                    
                    //genera el movimiento actual, con el numero de  nodos buscados, su profundidad(por ejemlpo 8  y el metodo de busqueda, en este caso MINIMAX)
                    Move actual = new Move(amazona,possiblesMovimientos.get(i),emptyCells.get(r),count,4,SearchType.MINIMAX);
                    
                    //comparación para quedarnos con el maximo de los hijos del primer nodo del MINIMAX
                    if(max < valor){
                        System.out.println("mama");
                        max = valor;
                        fi = actual;
                    }
                    
                    
                    
                }

            }
            
              
        }
        System.out.println("<<<=" +count + "poopp" + contar);
        return fi;
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
    private int min(GameStatus estat, int depth, int alpha, int beta, ArrayList<java.awt.Point> emptyCells){
        // si hemos llegado al nodo final  o ya no hay movimientos por hacer
        if (depth == 0 || estat.isGameOver()){
            if(estat.GetWinner() == enemic) return Integer.MIN_VALUE;
            else {
                heuristica actu = new heuristica(estat,1);
                return actu.getHeuristica();
                //return heuristica(estat);
            } //aqui va la heuristica
        }//n heuristica(estat, enemic);
        int qn = estat.getNumberOfAmazonsForEachColor();
        
        //Por cada Amazona del jugador...
        //ArrayList<Point> pendingAmazons = new ArrayList<>();
        //System.out.println("=> "+ emptyCells.size());
        for (int q = 0; q < qn; q++) {
            //Obtenemos una amazona
            Point amazona = estat.getAmazon(enemic, q);
            
            //Obtenemos todos los movimientos possibles de es amazona...
            ArrayList<Point> possiblesMovimientos = estat.getAmazonMoves(amazona, false);
            
            // Para cada possible movimiento de esa amazona...
            for(int i = 0; i < possiblesMovimientos.size(); i++){
           
                //Creamos un tablero con el movimiento de la amazona realizado...
                GameStatus s2 = new GameStatus(estat);
                s2.moveAmazon(amazona, possiblesMovimientos.get(i));
                boolean trobat = false;
                //lista para contemplas las emptycells de ese movimiento de amazona
                ArrayList<Point> emptyCells1 = new ArrayList<>(emptyCells);
                //Modificamos la lista emptyCells1
                for(int k = 0; k < emptyCells1.size() && !trobat; k++){
                    if (emptyCells1.get(k).x == possiblesMovimientos.get(i).x && emptyCells1.get(k).y == possiblesMovimientos.get(i).y ) {
                       emptyCells1.set(k,amazona);
                       trobat = true; 
                    }
                }
            
                //Por cada possible casilla en la que tirar la flecha...
                for(int r = 0; r < emptyCells1.size(); r++ ){
                    GameStatus s3 = new GameStatus(s2);
                    
                    s3.placeArrow(emptyCells1.get(r));
                    //LLamamos a minimax
                    
                    ArrayList<Point> emptyCells2 = new ArrayList<>(emptyCells1);
                    emptyCells2.remove(r);
                    //sumamos uno porque en este punto hemos llegado a un nodo explorado;
                    count++;
                    int valor = max(s3,depth-1,alpha,beta,emptyCells2);  
                    //poda alfa beta
                    beta = Math.min(beta, valor);
                    if (beta <= alpha){
                        return beta;
                    }
                }
            }
        }
        return beta;
    }
    private int max(GameStatus estat, int depth, int alpha, int beta, ArrayList<java.awt.Point> emptyCells){
        // si hemos llegado al nodo final  o ya no hay movimientos por hacer
        if (depth == 0 || estat.isGameOver()){
            if(estat.GetWinner() == propi) return Integer.MAX_VALUE;
            else {
                heuristica actu = new heuristica(estat,1);
                return actu.getHeuristica();
               // return heuristica(estat);
            } //aqui va la heuristica
        }//heuristica(estat, propi);
        int qn = estat.getNumberOfAmazonsForEachColor();
        //System.out.println("=> "+ emptyCells.size());
        
        //Por cada Amazona del jugador...
        //ArrayList<Point> pendingAmazons = new ArrayList<>();
        for (int q = 0; q < qn; q++) {
            //Obtenemos una amazona
            Point amazona = estat.getAmazon(propi, q);
            
            //Obtenemos todos los movimientos possibles de es amazona...
            ArrayList<Point> possiblesMovimientos = estat.getAmazonMoves(amazona, false);
            
            // Para cada possible movimiento de esa amazona...
            for(int i = 0; i < possiblesMovimientos.size(); i++){
           
                //Creamos un tablero con el movimiento de la amazona realizado...
                GameStatus s2 = new GameStatus(estat);
                s2.moveAmazon(amazona, possiblesMovimientos.get(i));
                boolean trobat = false;
                //lista para contemplas las emptycells de ese movimiento de amazona
                ArrayList<Point> emptyCells1 = new ArrayList<>(emptyCells);
                //Modificamos la lista emptyCells1
                for(int k = 0; k < emptyCells1.size() && !trobat; k++){
                    if (emptyCells1.get(k).x == possiblesMovimientos.get(i).x && emptyCells1.get(k).y == possiblesMovimientos.get(i).y ) {
                       emptyCells1.set(k,amazona);
                       trobat = true; 
                    }
                }
            
                //Por cada possible casilla en la que tirar la flecha...
                for(int r = 0; r < emptyCells1.size(); r++ ){
                    GameStatus s3 = new GameStatus(s2);
                    s3.placeArrow(emptyCells1.get(r));
                    //LLamamos a minimax
                    
                    ArrayList<Point> emptyCells2 = new ArrayList<>(emptyCells1);
                    emptyCells2.remove(r);
                    //sumamos uno porque en este punto hemos llegado a un nodo explorado;
                    count++;
                    int valor = min(s3,depth-1,alpha,beta,emptyCells2);  
                    //poda alfa beta
                    alpha = Math.max(alpha, valor);
                    if (beta <= alpha){
                        return alpha;
                    }
                }
            }
        }
        return alpha;
    }
    private int heuristica (GameStatus s){
        int valor = 0;
        for (int i = 0; i < s.getNumberOfAmazonsForEachColor(); i++) {
            
            //Obtenemos una amazona de nuestro jugador
            Point amazonaAmiga = s.getAmazon(propi, i);
            //Obtenemos una amazona de jugador enemigo
            Point amazonaEnemiga = s.getAmazon(enemic, i);
            
            //Obtenemos todos los movimientos possibles de la amazona amiga.
            /*ArrayList<Point> possiblesMovimientosAmigos = s.getAmazonMoves(amazonaAmiga, true);
            //Obtenemos todos los movimientos possibles de la amazona enemiga
            ArrayList<Point> possiblesMovimientosEnemigos = s.getAmazonMoves(amazonaEnemiga, true);*/
            
            /*valor += s.getAmazonMoves(amazonaAmiga, false).size();
            valor -= s.getAmazonMoves(amazonaEnemiga,false ).size();*/
            //---------------------propias--------------------------
            if (amazonaAmiga.x - 1 > -1 )
                if (s.getPos(amazonaAmiga.x - 1,amazonaAmiga.y) == CellType.EMPTY) ++valor;
            if (amazonaAmiga.x + 1 < 10 )
                if (s.getPos(amazonaAmiga.x + 1,amazonaAmiga.y) == CellType.EMPTY) ++valor;
            if (amazonaAmiga.y - 1 > -1 )
                if (s.getPos(amazonaAmiga.x ,amazonaAmiga.y - 1) == CellType.EMPTY) ++valor;
            if (amazonaAmiga.y + 1 < 10 )
                if (s.getPos(amazonaAmiga.x ,amazonaAmiga.y + 1) == CellType.EMPTY) ++valor;
            if (amazonaAmiga.x - 1 > -1 && amazonaAmiga.y - 1 > -1)
                if (s.getPos(amazonaAmiga.x - 1,amazonaAmiga.y - 1) == CellType.EMPTY) ++valor;
            if (amazonaAmiga.x - 1 > -1 && amazonaAmiga.y + 1 < 10 )
                if (s.getPos(amazonaAmiga.x - 1,amazonaAmiga.y +1) == CellType.EMPTY) ++valor;
            if (amazonaAmiga.x + 1 < 10 && amazonaAmiga.y - 1 > -1 )
                if (s.getPos(amazonaAmiga.x + 1,amazonaAmiga.y - 1) == CellType.EMPTY) ++valor;
            if (amazonaAmiga.x + 1 < 10  && amazonaAmiga.y + 1 < 10)
                if (s.getPos(amazonaAmiga.x + 1,amazonaAmiga.y +1) == CellType.EMPTY) ++valor;
            
            //-----------------------enemigo------------------------------
            if (amazonaEnemiga.x - 1 > -1 )
                if (s.getPos(amazonaEnemiga.x - 1,amazonaEnemiga.y) == CellType.EMPTY) --valor;
            if (amazonaEnemiga.x + 1 < 10 )
                if (s.getPos(amazonaEnemiga.x + 1,amazonaEnemiga.y) == CellType.EMPTY) --valor;
            if (amazonaEnemiga.y - 1 > -1 )
                if (s.getPos(amazonaEnemiga.x ,amazonaEnemiga.y - 1) == CellType.EMPTY) --valor;
            if (amazonaEnemiga.y + 1 < 10 )
                if (s.getPos(amazonaEnemiga.x ,amazonaEnemiga.y + 1) == CellType.EMPTY) --valor;
            if (amazonaEnemiga.x - 1 > -1 && amazonaEnemiga.y - 1 > -1)
                if (s.getPos(amazonaEnemiga.x - 1,amazonaEnemiga.y - 1) == CellType.EMPTY) --valor;
            if (amazonaEnemiga.x - 1 > -1 && amazonaEnemiga.y + 1 < 10 )
                if (s.getPos(amazonaEnemiga.x - 1,amazonaEnemiga.y +1) == CellType.EMPTY) --valor;
            if (amazonaEnemiga.x + 1 < 10 && amazonaEnemiga.y - 1 > -1 )
                if (s.getPos(amazonaEnemiga.x + 1,amazonaEnemiga.y - 1) == CellType.EMPTY) --valor;
            if (amazonaEnemiga.x + 1 < 10  && amazonaEnemiga.y + 1 < 10)
                if (s.getPos(amazonaEnemiga.x + 1,amazonaEnemiga.y +1) == CellType.EMPTY) --valor;
            
        }
        return valor;
    }
    @Override
    public String getName() {
        return "Player(" + name + ")";
    }
}