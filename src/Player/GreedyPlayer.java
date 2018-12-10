/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Player;

import State.State;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import static risk.game.RiskGame.currentPlayer;
import static risk.game.RiskGame.gameTerritory;
import static risk.game.RiskGame.getIcon;
import static risk.game.RiskGame.players;
import risk.game.Territory;

/**
 *
 * @author Ahmed
 */
public class GreedyPlayer extends AbstractPlayer{
    public static ImageIcon greedyIcon = getIcon("Greedy.png");

    private State nextState;
    private boolean attackerSelected = false;
    private boolean startAdding1 = true;
    
    public GreedyPlayer(Color color) {
        super(color);
    }
    
    @Override
    public ImageIcon Icon() {
        return greedyIcon;
    }
    
    @Override
    public String getName() {
        return "Greedy";
    }
    
    private void greedySearch(boolean attacking)
    {
        State initalState = new State(territories,players[(currentPlayer+1)%2].getTerritories(),gameTerritory,this.noOfAdditionalArmies);
        ArrayList<State> neighbors = initalState.neighbors(attacking,true,1);
        nextState = neighbors.get(0);
        neighbors.stream().map((neighbor) -> {
            if(neighbor.heuristic > nextState.heuristic)
            {
                nextState = neighbor;
            }
            return neighbor;
        }).forEachOrdered((_item) -> {
            this.T++;
        });
    }
    
    @Override
    protected Territory getTerritoryToAdd() {
        if(nextState != null &&nextState.noBounsArmies())
            startAdding1 = true;
        if(this.startAdding1)
        {
            greedySearch(false);
            startAdding1 = false;
        }
        return gameTerritory[nextState.getNextTerritoryToAdd().id];
    }
    
    @Override
    public Territory getAttacker() {
        if(!attackerSelected)
        {
            greedySearch(true);
        }
        attackerSelected = false;
        if(nextState.attacker != null)
            return gameTerritory[nextState.attacker.id];
        else
            return null;
    }

    @Override
    public Territory getDefender() {
        return gameTerritory[nextState.defender.id];
    }
    
    @Override
    protected void setNOofAttackingArmies() {
        attacker.noOfFightingArmies = nextState.attacker.noOfFightingArmies;
    }
    
    @Override
    public boolean continueAttacking()
    {
        if(this.canAttack())
        {
            greedySearch(true);
            if(nextState.attacker != null && gameTerritory[nextState.attacker.id] == attacker && gameTerritory[nextState.defender.id] == defender)
                return true;
            attackerSelected = true;
        }
        
        return false;
    }
    
}
