/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.epsevg.prop.amazons;

//Imports
import edu.upc.epsevg.prop.amazons.CellType;
import edu.upc.epsevg.prop.amazons.GameStatus;
import java.awt.Point;
import java.util.ArrayList;

/**
 * Guarda implementacions de diverses heuristiques.
 * @author kilian alpez
 */
public class heuristica {
    
    // Atributos //
    CellType propi; // Identifica a nuestro jugador
    CellType enemic; // Identifica al jugador enemigo
    
    int valor; // Valor de la heuristica
    /**
     * Constructor de la classe
     * @param s Estado del juego
     * @param n Nivel de complejidad de la heuristica utilizada
     */
    public heuristica(GameStatus s, int n) {
        
        
        //Obtenemos el color que representa al jugador
        propi = s.getCurrentPlayer();
        
        //obtenemos el color que representa el enemigo
        if(propi == CellType.PLAYER1) enemic = CellType.PLAYER2;
        else enemic = CellType.PLAYER1;
        
        //Inicializamos el valor heuristico en 0
        valor = 0;
        
        if (n == 1) basica(s);
        else if ( n == 2) intermedia(s);
        else if ( n == 3) compleja(s);
        
    }
    
    /**
     * Una heuristica muy simple, unicamente tendremos en cuenta  el
     * numero de possibles movimientos (restringidos) que pueden realizar las damas de cada jugador.
     * @param s Estado del juego
     */
    public void basica(GameStatus s) {
        
        //Por cada amazona de la partida...
        for (int i = 0; i < s.getNumberOfAmazonsForEachColor(); i++) {
            
            //Obtenemos una amazona de nuestro jugador
            Point amazonaAmiga = s.getAmazon(propi, i);
            //Obtenemos una amazona de jugador enemigo
            Point amazonaEnemiga = s.getAmazon(enemic, i);
            
            //Obtenemos todos los movimientos possibles de la amazona amiga.
            /*ArrayList<Point> possiblesMovimientosAmigos = s.getAmazonMoves(amazonaAmiga, true);
            //Obtenemos todos los movimientos possibles de la amazona enemiga
            ArrayList<Point> possiblesMovimientosEnemigos = s.getAmazonMoves(amazonaEnemiga, true);*/
            
            valor += s.getAmazonMoves(amazonaAmiga, false).size();
            valor -= s.getAmazonMoves(amazonaEnemiga,false ).size();
            
        }

    }
    
    /**
     * Una heuristica intermedia, tiene en cuenta todos los possibles movimientos 
     * que puede realizar cada dama de la partida
     * @param s Estado del juego
     */
    public void intermedia(GameStatus s) {
       
        //Por cada amazona de la partida...
        for (int i = 0; i < s.getNumberOfAmazonsForEachColor(); i++) {
            
            //Obtenemos una amazona de nuestro jugador
            Point amazonaAmiga = s.getAmazon(propi, i);
            //Obtenemos una amazona de jugador enemigo
            Point amazonaEnemiga = s.getAmazon(enemic, i);
            
            //Obtenemos todos los movimientos possibles de la amazona amiga.
            ArrayList<Point> possiblesMovimientosAmigos = s.getAmazonMoves(amazonaAmiga, false);
            //Obtenemos todos los movimientos possibles de la amazona enemiga
            ArrayList<Point> possiblesMovimientosEnemigos = s.getAmazonMoves(amazonaAmiga, false);
            
            valor += possiblesMovimientosAmigos.size();
            valor -= possiblesMovimientosEnemigos.size();
        
        }
    }
    
    /**
     * Una heuristica avançada, tendremos en cuenta tanto el numero de possibles movimientos de cada dama de la partida,
     * como el control del territorio que tienen, es decir, las casillas que pueden ocupar antes que otras damas.
     * @param s 
     */
    public void compleja(GameStatus s) {
        
    }
    
    /**
     * Devuelve el valor heuristico.
     * @return 
     */
    public int getHeuristica() {
        return valor;
    }
    
}


