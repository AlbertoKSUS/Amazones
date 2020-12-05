package edu.upc.epsevg.prop.amazons;

import edu.upc.epsevg.prop.amazons.players.HumanPlayer;
import edu.upc.epsevg.prop.amazons.players.CarlinhosPlayer;
import edu.upc.epsevg.prop.amazons.players.RandomPlayer;
import edu.upc.epsevg.prop.amazons.players.NouGroup;
import javax.swing.SwingUtilities;

/**
 *
 * @author bernat
 */
public class Amazons {
        /**
     * @param args
     */
    public static void main(String[] args) {
        
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                IPlayer custom = new NouGroup("Hola");
                IPlayer player1 = new HumanPlayer("Snail");
                IPlayer player2 = new CarlinhosPlayer();
              
                
                new AmazonsBoard(player1 , custom, 10, Level.HALF_BOARD);
                
            }
        });
    }
}
