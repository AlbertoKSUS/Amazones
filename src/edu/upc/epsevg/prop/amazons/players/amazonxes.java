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
 * @author alpez kilroig
 */
public class amazonxes implements IPlayer, IAuto {

    //Atributos
    private String name;
    CellType propi;
    CellType enemic;
    int countNodes;
    int countFulles;
    int profunditatMaxima;

    // Metodos
    
    /**
     * Constructor
     * @param name 
     */
    public amazonxes(String name,int profunditatMaxima) {
        this.name = name;
        this.profunditatMaxima = profunditatMaxima;
    }

    @Override
    public void timeout() {
        // Nothing to do! I'm so fast, I never timeout 8-)
    }
    
    @Override
    public String getName() {
        return "Player(" + name + ")";
    }
    
    private void mostra(ArrayList<Point> n){
        for(int i = 0; i<n.size();++i){
            System.out.println("=>"+ i+ " pos " + n.get(i));
        }
    }
    /*private ArrayList<Point> MovimentsComplets(ArrayList<Point> mov){
        
    }*/
    
    /**
     * Utiliza minimax para calcular el mejor movimiento possible para esa jugada.
     * @param s Estado del juego
     * @return Devuelve el mejor movimiento possible en esa jugada.
     */
    public Move move(GameStatus s) {
        
        // Inicializaciones
        
        //Obtenemos el color que representa al jugador
        propi = s.getCurrentPlayer();
        //Obtenemos el color que representa el enemigo
        if(propi == CellType.PLAYER1) enemic = CellType.PLAYER2;
        else enemic = CellType.PLAYER1;
        
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
            Point amazona = s.getAmazon(propi, q);
            
            //Obtenemos todos los movimientos possibles de es amazona...
            ArrayList<Point> possiblesMovimientos = s.getAmazonMoves(amazona, false); // -> True para simplificar y tardar menos.
            
            // Para cada possible movimiento de esa amazona...
            for(int i = 0; i < possiblesMovimientos.size(); i++){
            
                //Realizamos el movimiento de la amazona en el tablero...
                //GameStatus s2 = new GameStatus(s);
                s.moveAmazon(amazona, possiblesMovimientos.get(i));
                
                //Obtenemos una lista con todas las casillas libres del tablero...
                ArrayList<Point> casillasLibres = getEmptyCells(s);
                
                //Por cada possible casilla libre en la que tirar la flecha...
                for(int r = 0; r < casillasLibres.size(); r++ ){
                    
                    //Realizamos el movimiento de tirar la flecha en el tablero...
                    GameStatus s3 = new GameStatus(s);
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
                s.moveAmazon(possiblesMovimientos.get(i),amazona);
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
    
    
    /**
     * Funcion Min del minimax ( ESTARIA BIEN JUNTAR MIN Y MAX EN UNA SOLA FUNCION)
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
        
        // Si la partida ha acabado, devolvemos infinito o -infinito dependiendo quien ha ganado
        if ( s.isGameOver()) {
            if (s.GetWinner() == propi) return Double.POSITIVE_INFINITY;
            else return Double.NEGATIVE_INFINITY;
        }
        else if ( profunditat == 0) {
            countFulles++;
            heuristica actu = new heuristica(s,1);
            return actu.getHeuristica();
        }
        
        // Seguimos explorando
        
        //Valor inicial del node en +infinit
        double valorNodeActual = Double.POSITIVE_INFINITY;

        //Por cada amazona de la partida....
        for (int q = 0; q < s.getNumberOfAmazonsForEachColor(); q++) {
            
            //Obtenemos una amazona
            Point amazona = s.getAmazon(enemic, q);

            //Obtenemos todos los movimientos possibles de es amazona...
            ArrayList<Point> possiblesMovimientos = s.getAmazonMoves(amazona, false); // -> True para simplificar y tardar menos.
            
            // Para cada possible movimiento de esa amazona...
            for(int i = 0; i < possiblesMovimientos.size(); i++){
            
                //Realizamos el movimiento de la amazona en el tablero...
               // GameStatus s2 = new GameStatus(s);
                s.moveAmazon(amazona, possiblesMovimientos.get(i));
                
                //Obtenemos una lista con todas las casillas libres del tablero...
                ArrayList<Point> casillasLibres = getEmptyCells(s);
                
                //Por cada possible casilla libre en la que tirar la flecha...
                for(int r = 0; r < casillasLibres.size(); r++ ){
                    
                    //Realizamos el movimiento de tirar la flecha en el tablero...
                    GameStatus s3 = new GameStatus(s);
                    s3.placeArrow(casillasLibres.get(r));                   
                
                    //Llamamos a MAX
                    valorNodeActual = Math.min(valorNodeActual, max(s3,profunditat-1,alfa,beta));
                        
                    // Ampliacio poda alfa-beta
                    alfa = Math.min(valorNodeActual,alfa);
                    if( beta <= alfa) return valorNodeActual;
                }
                s.moveAmazon(possiblesMovimientos.get(i),amazona);
            }
        }
        return valorNodeActual;
    }
            
    private double max(GameStatus s, int profunditat, double alfa, double beta){
        
        //Contador de nodos explorados
        countNodes++;
        
        // Si la partida ha acabado, devolvemos infinito o -infinito dependiendo quien ha ganado
        if ( s.isGameOver()) {
            if (s.GetWinner() == propi) return Double.POSITIVE_INFINITY;
            else return Double.NEGATIVE_INFINITY;
        }
        else if ( profunditat == 0) {
            countFulles++;
            heuristica actu = new heuristica(s,1);
            return actu.getHeuristica();
        }
        
        // Seguimos explorando
        
        //Valor inicial del node en -infinit
        double valorNodeActual = Double.NEGATIVE_INFINITY;

        //Por cada amazona de la partida....
        for (int q = 0; q < s.getNumberOfAmazonsForEachColor(); q++) {
            
            //Obtenemos una amazona
            Point amazona = s.getAmazon(propi, q);
            
            //Obtenemos todos los movimientos possibles de es amazona...
            ArrayList<Point> possiblesMovimientos = s.getAmazonMoves(amazona, false); // -> True para simplificar y tardar menos.
            
            // Para cada possible movimiento de esa amazona...
            for(int i = 0; i < possiblesMovimientos.size(); i++){
            
                //Realizamos el movimiento de la amazona en el tablero...
                //GameStatus s2 = new GameStatus(s);
                s.moveAmazon(amazona, possiblesMovimientos.get(i));
                
                //Obtenemos una lista con todas las casillas libres del tablero...
                ArrayList<Point> casillasLibres = getEmptyCells(s);
                
                //Por cada possible casilla libre en la que tirar la flecha...
                for(int r = 0; r < casillasLibres.size(); r++ ){
                    
                    //Realizamos el movimiento de tirar la flecha en el tablero...
                    GameStatus s3 = new GameStatus(s);
                    s3.placeArrow(casillasLibres.get(r));                                
                
                    //Llamamos a MIN                      
                    valorNodeActual = Math.max(valorNodeActual, min(s3,profunditat-1,alfa,beta));
                        
                    // Ampliacio poda alfa-beta
                    alfa = Math.max(valorNodeActual,alfa);
                    if( beta<= alfa) return valorNodeActual;
   
                }
                s.moveAmazon(possiblesMovimientos.get(i),amazona);
            }
        }
        return valorNodeActual;
    }
}