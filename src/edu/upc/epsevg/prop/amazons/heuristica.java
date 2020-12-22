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
     */
    public heuristica(GameStatus s) {
        
        
        //Obtenemos el color que representa al jugador
        propi = s.getCurrentPlayer();
        
        //obtenemos el color que representa el enemigo
        if(propi == CellType.PLAYER1) enemic = CellType.PLAYER2;
        else enemic = CellType.PLAYER1;
        
        //Inicializamos el valor heuristico en 0
        valor = 0;
        
        basica(s);
        
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
            
            valor += s.getAmazonMoves(amazonaAmiga, false).size();
            valor -= s.getAmazonMoves(amazonaEnemiga,false ).size();
            
        }

    }
    
    /**
     * Devuelve el valor heuristico.
     * @return 
     */
    public int getHeuristica() {
        return valor;
    }
    
}


