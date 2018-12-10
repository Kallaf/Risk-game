/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Player;

import State.State;
import java.awt.Color;
import java.util.Collections;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
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
public class AStarPlayer extends AbstractPlayer {
    public static ImageIcon AStarIcon = getIcon("AStar.jpg");
    private State nextState;
    private boolean attackerSelected = false;
    private boolean startAdding1 = true;
    public AStarPlayer(Color color) {
        super(color);
    }
    

    private void AStarSearch(State initalState,boolean attacking)
    {
        PriorityQueue<State> frontier = new PriorityQueue(Collections.reverseOrder());
        frontier.add(initalState);
        Set<int[][]> explored = new HashSet<>();
        explored.add(initalState.id);
        
        State state = frontier.remove();
        
        for(State neighbor:state.neighbors(attacking,true,1))
        {
            frontier.add(neighbor);
            explored.add(neighbor.id);
        }
        
        while(!frontier.isEmpty())
        {
         
           this.T++;
           state = frontier.remove();
           if(state.enemyTerritories.isEmpty())
           {
               setPath(state);
               break;
           }
           
           for(State neighbor:state.neighbors(!state.endAttack,true,state.depth+1))
           {
               if(!explored.contains(neighbor.id))
               {
                   frontier.add(neighbor);
                   explored.add(neighbor.id);
               }
           }
        }
        
    }
    
    @Override
    public ImageIcon Icon() {
        return AStarIcon;
    }

    @Override
    public String getName() {
        return "A*";
    }

    @Override
    protected Territory getTerritoryToAdd() {
        if(nextState != null &&nextState.noBounsArmies())
            startAdding1 = true;
        if(this.startAdding1)
        {
            AStarSearch(new State(territories,players[(currentPlayer+1)%2].getTerritories(),gameTerritory,noOfAdditionalArmies),false);
            startAdding1 = false;
        }
        return gameTerritory[nextState.getNextTerritoryToAdd().id];
    }

    @Override
    public Territory getAttacker() {
        if(!attackerSelected)
        {
           AStarSearch(new State(territories,players[(currentPlayer+1)%2].getTerritories(),gameTerritory,noOfAdditionalArmies),true);
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
    
    private void setPath(State finalState)
    {
        State[] path = new State[finalState.depth+1];
        State state= finalState;
        while(state != null)
        {
            path[state.depth] = state;
            state = state.parent;
        }
        nextState = path[1];
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
            AStarSearch(new State(territories,players[(currentPlayer+1)%2].getTerritories(),gameTerritory,noOfAdditionalArmies),true);
            if(nextState.attacker != null && gameTerritory[nextState.attacker.id] == attacker && gameTerritory[nextState.defender.id] == defender)
                return true;
            attackerSelected = true;  
        }
        return false;
    }
    
}
