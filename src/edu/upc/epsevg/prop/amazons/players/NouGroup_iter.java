/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.epsevg.prop.amazons.players;

import edu.upc.epsevg.prop.amazons.GameStatus;
import edu.upc.epsevg.prop.amazons.IAuto;
import edu.upc.epsevg.prop.amazons.IPlayer;
import edu.upc.epsevg.prop.amazons.Move;

/**
 *
 * @author alpez
 */
public class NouGroup_iter implements IPlayer, IAuto {

    private String name;
    private GameStatus s;

    public NouGroup_iter(String name) {
        this.name = name;
    }

    @Override
    public void timeout() {
        // Nothing to do! I'm so fast, I never timeout 8-)
    }
    
    public Move move(GameStatus s) {
        return null;
    }

    @Override
    public String getName() {
        return "Player(" + name + ")";
    }
    private int max(GameStatus s, int depth, int player, int alpha, int beta){
      // Max
      if (!estat.espotmoure() || depth == 0) return heuristica(estat, player);
      for (int i=0;i<estat.getMida();i++){    
          if (estat.movpossible(i)){
              Tauler estat2 = new Tauler(estat);
              cont++;
              estat2.afegeix(i, player);
              if (estat2.solucio(i, player)) return Integer.MAX_VALUE;
              int valor = min(estat2, depth-1, -player, alpha, beta);    
              alpha = Math.max(alpha, valor);
              if (beta <= alpha){
                  return alpha;
              }
          }
      }
      return alpha;
  }
    
  private int min(GameStatus estat, int depth, int player, int alpha, int beta){
      // Min
      if (depth == 0 || !estat.espotmoure()) return heuristica(estat, player);
      for (int i=0;i<estat.getMida();i++){
          if (estat.movpossible(i)){
              Tauler estat2 = new Tauler(estat);
              estat2.afegeix(i, player); 
              cont++;
              if (estat2.solucio(i, player)) return Integer.MIN_VALUE;
              int valor = max(estat2, depth-1, -player, alpha, beta);    
              beta = Math.min(beta, valor);
              if (beta <= alpha){
                  return beta;
              }
          }
      }
      return beta;
  }
}
