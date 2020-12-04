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
    private GameStatus s;

    public NouGroup(String name) {
        this.name = name;
    }

    @Override
    public void timeout() {
        // Nothing to do! I'm so fast, I never timeout 8-)
    }
    
    public Move move(GameStatus s) {
        CellType color = s.getCurrentPlayer();
        this.s = s;
        int qn = s.getNumberOfAmazonsForEachColor();
        ArrayList<Point> pendingAmazons = new ArrayList<>();
        for (int q = 0; q < qn; q++) {
            pendingAmazons.add(s.getAmazon(color, q));
        }
        return null;
    }

    @Override
    public String getName() {
        return "Player(" + name + ")";
    }
}
