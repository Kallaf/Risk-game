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
public class MinimaxPlayer extends AbstractPlayer{
    public static ImageIcon MinMaxIcon = getIcon("Minimax.jpg");
    private int alpha;
    private int beta;
    private int MAX_DEPTH;
    private State nextState;
    private boolean attackerSelected = false;
    private boolean startAdding1 = true;
    
    public MinimaxPlayer(Color color) {
        super(color);
        
    }
    
    @Override
    public ImageIcon Icon() {
        return MinMaxIcon;
    }
    
    @Override
    public String getName() {
        return "Minimax";
    }
    
    @Override
    protected Territory getTerritoryToAdd() {
        
        if(nextState != null &&nextState.noBounsArmies())
            startAdding1 = true;
        if(this.startAdding1)
        {
            miniMaxSearch(new State(territories,players[(currentPlayer+1)%2].getTerritories(),gameTerritory,noOfAdditionalArmies),false);
            startAdding1 = false;
        }
        return gameTerritory[nextState.getNextTerritoryToAdd().id];
    }
    
    @Override
    public Territory getAttacker() {
        
        if(!attackerSelected)
        {
           miniMaxSearch(new State(territories,players[(currentPlayer+1)%2].getTerritories(),gameTerritory,noOfAdditionalArmies),true);
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
    
    private State minimax(int depth,State currentState, boolean  isMax) 
    {  
        if (depth == MAX_DEPTH) 
            return currentState; 
        if (isMax) 
        {
            if(!currentState.endAttack)
            {
                  ArrayList<State> neighbors = currentState.neighbors(true,true,depth+1);
                  State max = minimax(depth+1,neighbors.get(0),true);
                  for(State neighbor:neighbors)
                  {
                      State temp = minimax(depth+1,neighbor,true);
                      if(temp.heuristic > max.heuristic)
                          max = temp;
                  }
                  return max;
            }else
            {
                  ArrayList<State> neighbors = currentState.neighbors(false,false,depth+1);
                  if(neighbors.isEmpty())
                      return currentState;
                  State min = minimax(depth+1,neighbors.get(0),false);
                  for(State neighbor:neighbors)
                  {
                      State temp = minimax(depth+1,neighbor,false);
                      if(temp.heuristic < min.heuristic)
                          min = temp;

                      alpha = Math.max(alpha,min.heuristic);
                      if (beta <= alpha) 
                          break;
                  }
                  return min; 
            }
        }
        else
        {
            if(!currentState.endAttack)
            {
                  ArrayList<State> neighbors = currentState.neighbors(true,false,depth+1);
                  State min = minimax(depth+1,neighbors.get(0),false);
                  for(State neighbor:neighbors)
                  {
                      State temp = minimax(depth+1,neighbor,false);
                      if(temp.heuristic < min.heuristic)
                          min = temp;
                  }
                  return min;
            }else
            {
                  ArrayList<State> neighbors = currentState.neighbors(false,true,depth+1);
                  if(neighbors.isEmpty())
                      return currentState;
                  State max = minimax(depth+1,neighbors.get(0),true);
                  for(State neighbor:neighbors)
                  {
                      State temp = minimax(depth+1,neighbor,true);
                      if(temp.heuristic > max.heuristic)
                          max = temp;
                      beta = Math.min(beta,max.heuristic);
                      if (beta <= alpha) 
                          break;
                  }
                  return max; 
            }
            
        } 
    }

    private void miniMaxSearch(State initalState,boolean attacking) {
        alpha = -2147483648;
        beta = 2147483647;
        State finalState;
        if(!attacking)
        {
            this.MAX_DEPTH = 3;
            ArrayList<State> neighbors = initalState.neighbors(false,true,1);
            finalState = minimax(1,neighbors.get(0),true);
            for(State neighbor:neighbors)
            {
                State temp = minimax(1,neighbor,true);
                if(temp.heuristic > finalState.heuristic)
                    finalState = temp;
            }
        }else
        {
            this.MAX_DEPTH = 5;
            finalState = minimax(0,initalState,true);
        }
            
        
        setPath(finalState);
   }
    
    private void setPath(State finalState)
    {
        State[] path = new State[MAX_DEPTH+1];
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
            miniMaxSearch(new State(territories,players[(currentPlayer+1)%2].getTerritories(),gameTerritory,noOfAdditionalArmies),true);
            if(nextState.attacker != null && gameTerritory[nextState.attacker.id] == attacker && gameTerritory[nextState.defender.id] == defender)
                return true;
            attackerSelected = true;  
        }
        return false;
    }
    
}
