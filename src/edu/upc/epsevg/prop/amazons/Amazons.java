package edu.upc.epsevg.prop.amazons;

import edu.upc.epsevg.prop.amazons.players.HumanPlayer;
import edu.upc.epsevg.prop.amazons.players.CarlinhosPlayer;
import edu.upc.epsevg.prop.amazons.players.RandomPlayer;
import edu.upc.epsevg.prop.amazons.players.amazonxes_iter;
import edu.upc.epsevg.prop.amazons.players.amazonxes;
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
                
                //IPlayer player1 = new amazonxes("papa", 2);
                IPlayer player1 = new amazonxes_iter("papa");
                        
                IPlayer player2 = new CarlinhosPlayer();
                
                new AmazonsBoard(player1 , player2, 10, Level.QUARTERBOARD);
                
            }
        });
    }
}
