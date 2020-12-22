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
import java.util.Collections;

/**
 *
 * @author alpez kilroig
 */
public class amazonxes_iter implements IPlayer, IAuto {

    //Atributos
    private String name;
    int countNodes;
    int countFulles;
    int profunditatMaxima;
    Move res;
    boolean first;
    boolean timeTOstop;

    // Metodos
    
    /**
     * Constructor
     * @param name 
     */
    public amazonxes_iter(String name) {
        this.name = name;
        this.timeTOstop = false;
    }

    @Override
    public void timeout() {
        this.timeTOstop = true;
    }
    
    @Override
    public String getName() {
        return "Player(" + name + ")";
    }
    
    
    /**
     * Utiliza minimax para calcular el mejor movimiento possible para esa jugada.
     * @param s Estado del juego
     * @return Devuelve el mejor movimiento possible en esa jugada.
     */
    public Move move(GameStatus s) {
        this.profunditatMaxima = 1;
        res = null;
        res = inici(s);
        ++profunditatMaxima;
        while(!timeTOstop){
            res = inici(s);
            ++profunditatMaxima;
        }
        this.timeTOstop = false;
       return res;
        
    }
     /**
     * Inicia el minimax
     * @param s Estado del juego
     * @return Devuelve el mejor movimiento possible en esa jugada.
     */
    private Move inici(GameStatus s){
        // Inicializaciones
        
        //Obtenemos el color que representa al jugador
        CellType actual = s.getCurrentPlayer();
        
        //Variables para optimizar la poda.
        double max = Double.NEGATIVE_INFINITY;
        double alfa = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        
        //Variables de informacion del minimax      
        countNodes = 0;
        countFulles = 0;    
     
        //Variables para guardar el mejor movimiento.
        Point mejorAmazona = new Point(0,0);
        Point destinoAmazona = new Point(0,0);
        Point mejorFlecha = new Point(0,0);
        
        // Calculos
             
        //Por cada amazona de la partida....
        for (int q = 0; q < s.getNumberOfAmazonsForEachColor(); q++) {
            
            //Obtenemos una amazona
            Point amazona = s.getAmazon(actual, q);
            
            //Obtenemos todos los movimientos possibles de es amazona...
            ArrayList<Point> possiblesMovimientos = s.getAmazonMoves(amazona, false); // -> True para simplificar y tardar menos.
            //mejorar la poda alfa beta empezando por el movimiento de amazona mas prometedor de la iteracion anterior
            if (res != null) 
                if(amazona == res.getAmazonFrom())
                    for(int k = 0; k < possiblesMovimientos.size(); k++){
                        if(possiblesMovimientos.get(k) == res.getAmazonTo()){
                            Collections.swap(possiblesMovimientos, k,0);
                        }
                    }
            // Para cada possible movimiento de esa amazona...
            for(int i = 0; i < possiblesMovimientos.size(); i++){
            
                //Realizamos el movimiento de la amazona en el tablero...
                GameStatus s2 = new GameStatus(s);
                s2.moveAmazon(amazona, possiblesMovimientos.get(i));
                
                //Obtenemos una lista con todas las casillas libres del tablero...
                ArrayList<Point> casillasLibres = getEmptyCells(s2);
                //mejorar la poda alfa beta empezando por el movimiento de amazona mas prometedor de la iteracion anterior
                if (i==0 && res != null) 
                    for(int k =0; k <casillasLibres.size(); k++){
                        if(casillasLibres.get(k) == res.getArrowTo()){
                            Collections.swap(casillasLibres, k,0);
                        }
                    }
                
                //Por cada possible casilla libre en la que tirar la flecha...
                for(int r = 0; r < casillasLibres.size(); r++ ){
                     
                    //Realizamos el movimiento de tirar la flecha en el tablero...
                    GameStatus s3 = new GameStatus(s);
                    s3.moveAmazon(amazona,possiblesMovimientos.get(i));
                    s3.placeArrow(casillasLibres.get(r));
                    
                    //Llamamos a MINIMAX                        
                    double valor = min(s3,profunditatMaxima-1,alfa,beta);
                    
                    //CComprobamos que el nodo actual es el mejor candidato, en caso afirmativo, guardamos la informacion del movimiento
                    if(max < valor){
                        max = valor;
                        mejorAmazona = amazona;
                        destinoAmazona = possiblesMovimientos.get(i);
                        mejorFlecha = casillasLibres.get(r);
                    }                 
                }
            }         
        }
        //Devolvemos el mejor movimiento
        return new Move(mejorAmazona,destinoAmazona,mejorFlecha,countNodes,profunditatMaxima,SearchType.MINIMAX);
    } 
    
    /**
     * Recorremos el tablero en busca de las casillas libres
     * @param s Estado del juego
     * @return Devuelve una lista con todas las casillas libres del tablero.
     */
    private ArrayList<java.awt.Point> getEmptyCells(GameStatus s) {
        
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
    
    
    /**
     * Funcion Min del minimax 
     * @param estat
     * @param depth
     * @param alpha
     * @param beta
     * @param emptyCells
     * @return 
     */
    private double min(GameStatus s, int profunditat, double alfa, double beta){
        
        //Contador de nodos explorados
        countNodes++;
        CellType actual = s.getCurrentPlayer();
        // Si la partida ha acabado, devolvemos infinito o -infinito dependiendo quien ha ganado
        if ( s.isGameOver()) {
            if (s.GetWinner() == actual) return Double.NEGATIVE_INFINITY;
            else return Double.POSITIVE_INFINITY;
        }
        else if ( profunditat == 0 || timeTOstop) {
            countFulles++;
            heuristica actu = new heuristica(s);
            return -actu.getHeuristica();
        }
        
        // Seguimos explorando
        
        //Valor inicial del node en +infinit
        double valorNodeActual = Double.POSITIVE_INFINITY;

        //Por cada amazona de la partida....
        for (int q = 0; q < s.getNumberOfAmazonsForEachColor(); q++) {
            
            //Obtenemos una amazona
            Point amazona = s.getAmazon(actual, q);

            //Obtenemos todos los movimientos possibles de es amazona...
            ArrayList<Point> possiblesMovimientos = s.getAmazonMoves(amazona, false); // -> True para simplificar y tardar menos.
            
            // Para cada possible movimiento de esa amazona...
            for(int i = 0; i < possiblesMovimientos.size(); i++){
            
                //Realizamos el movimiento de la amazona en el tablero...
                GameStatus s2 = new GameStatus(s);
                s2.moveAmazon(amazona, possiblesMovimientos.get(i));
                
                //Obtenemos una lista con todas las casillas libres del tablero...
                ArrayList<Point> casillasLibres = getEmptyCells(s2);
                
                //Por cada possible casilla libre en la que tirar la flecha...
                for(int r = 0; r < casillasLibres.size(); r++ ){
                    
                    //Realizamos el movimiento de tirar la flecha en el tablero...
                    GameStatus s3 = new GameStatus(s);
                    s3.moveAmazon(amazona,possiblesMovimientos.get(i));
                    s3.placeArrow(casillasLibres.get(r));                   
                
                    //Llamamos a MAX
                    valorNodeActual = Math.min(valorNodeActual, max(s3,profunditat-1,alfa,beta));
                        
                    // Ampliacio poda alfa-beta
                    beta = Math.min(valorNodeActual,beta);
                    if( beta <= alfa) return beta;
                }
            }
        }
        return valorNodeActual;
    }
            
    private double max(GameStatus s, int profunditat, double alfa, double beta){
        
        //Contador de nodos explorados
        countNodes++;
        CellType actual = s.getCurrentPlayer();
        // Si la partida ha acabado, devolvemos infinito o -infinito dependiendo quien ha ganado
        if ( s.isGameOver()) {
            if (s.GetWinner() == actual) return Double.POSITIVE_INFINITY;
            else return Double.NEGATIVE_INFINITY;
        }
        else if ( profunditat == 0 || timeTOstop) {
            countFulles++;
            heuristica actu = new heuristica(s);
            return actu.getHeuristica();
        }
        
        // Seguimos explorando
        
        //Valor inicial del node en -infinit
        double valorNodeActual = Double.NEGATIVE_INFINITY;

        //Por cada amazona de la partida....
        for (int q = 0; q < s.getNumberOfAmazonsForEachColor(); q++) {
            
            //Obtenemos una amazona
            Point amazona = s.getAmazon(actual, q);
            
            //Obtenemos todos los movimientos possibles de es amazona...
            ArrayList<Point> possiblesMovimientos = s.getAmazonMoves(amazona, false); // -> True para simplificar y tardar menos.
            
            // Para cada possible movimiento de esa amazona...
            for(int i = 0; i < possiblesMovimientos.size(); i++){
            
                //Realizamos el movimiento de la amazona en el tablero...
                GameStatus s2 = new GameStatus(s);
                s2.moveAmazon(amazona, possiblesMovimientos.get(i));
                
                //Obtenemos una lista con todas las casillas libres del tablero...
                ArrayList<Point> casillasLibres = getEmptyCells(s2);
                
                //Por cada possible casilla libre en la que tirar la flecha...
                for(int r = 0; r < casillasLibres.size(); r++ ){
                    
                    //Realizamos el movimiento de tirar la flecha en el tablero...
                    GameStatus s3 = new GameStatus(s);
                    s3.moveAmazon(amazona, possiblesMovimientos.get(i));
                    s3.placeArrow(casillasLibres.get(r));                                
                
                    //Llamamos a MIN                      
                    valorNodeActual = Math.max(valorNodeActual, min(s3,profunditat-1,alfa,beta));
                        
                    // Ampliacio poda alfa-beta
                    alfa = Math.max(valorNodeActual,alfa);
                    if( beta <= alfa) return alfa;
   
                }
            }
        }
        return valorNodeActual;
    }
}