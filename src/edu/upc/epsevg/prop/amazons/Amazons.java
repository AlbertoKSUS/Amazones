package edu.upc.epsevg.prop.amazons;

import edu.upc.epsevg.prop.amazons.players.HumanPlayer;
import edu.upc.epsevg.prop.amazons.players.CarlinhosPlayer;
import edu.upc.epsevg.prop.amazons.players.RandomPlayer;
import edu.upc.epsevg.prop.amazons.players.NouGroup;
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
                
                IPlayer player1 = new amazonxes("hola", 2);
                IPlayer player2 = new CarlinhosPlayer();
                
                new AmazonsBoard(player1 , player2, 10, Level.QUARTERBOARD);
                
            }
        });
    }
}
