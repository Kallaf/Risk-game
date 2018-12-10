/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import java.util.ArrayList;
import static risk.game.RiskGame.noOfTerritories;
import risk.game.Territory;

/**
 *
 * @author Ahmed
 */
public class State implements Comparable<State>{
    public ArrayList<Territory> playerTerritories;
    public ArrayList<Territory> enemyTerritories;
    public Territory[] gameTerritory;
    public Territory attacker;
    public Territory defender;
    public Territory[] territoriesToAdd;
    public final State parent;
    public int heuristic;
    public final int depth;
    public int bouns = 0;
    public int[][] id;
    private ArrayList<Territory[]> bounsArmiesCombitations;
    private int bounsArmiesCounter = 0;
    public boolean endAttack = false;
    
    
    //for inital state
    public State(ArrayList<Territory> playerTerritories,ArrayList<Territory> enemyTerritories,Territory[] gameTerritory,int bouns)
    {
        this.bouns = bouns;
        this.playerTerritories = playerTerritories;
        this.enemyTerritories = enemyTerritories;
        this.gameTerritory = gameTerritory;
        this.parent = null;
        this.id = new int[noOfTerritories+2][2];
        this.id[0][0] = 1;
        this.id[0][1] = 1;
        for(int i=1;i<=noOfTerritories;i++)
        {
            this.id[i][0] = this.gameTerritory[i].noOfArmies;
            this.id[i][1] = this.gameTerritory[i].Owner == playerTerritories.get(0).Owner ? 1:2;
        } 
        setHeuristic();
        depth = 0;
    }
    
    
    //for adding armies state
    private State(State parent,Territory[] territoriesToAdd,int depth,int bouns)
    {
        this.parent = parent;
        this.bouns = bouns;
        copyTerritories();
        this.territoriesToAdd = new Territory[territoriesToAdd.length];
        for(int i=0;i<territoriesToAdd.length;i++)
        {
            this.territoriesToAdd[i] = gameTerritory[territoriesToAdd[i].id];
            this.territoriesToAdd[i].noOfArmies++;
        }
        setHeuristic();
        this.depth = depth;
        
    }
    
    //for END ATTACK state
    private State(State parent,int depth)
    {
        this.parent = parent;
        this.bouns = 0;
        copyTerritories();
        setHeuristic();
        this.depth = depth;
        this.endAttack = true;
    }
    
    //for attack state
    private State(State parent,Territory attacker,Territory defender,int noOfAttackingArmies,boolean isMax,int depth)
    {
        this.parent = parent;
        this.bouns = 0;
        copyTerritories();
        this.attacker = gameTerritory[attacker.id];
        this.defender = gameTerritory[defender.id];
        
        this.attacker.noOfFightingArmies = noOfAttackingArmies;
        if(this.attacker.noOfFightingArmies > 3)
            this.attacker.noOfFightingArmies = 3;
        
        this.defender.noOfFightingArmies = (this.defender.noOfArmies == 1)?1:2;
        
        if(probabilityOfLose() < 0.5)
        {
            this.defender.noOfArmies -= this.defender.noOfFightingArmies;
            if(this.defender.noOfArmies == 0)
            {
                this.attacker.noOfArmies -= this.attacker.noOfFightingArmies;
                if(isMax)
                {
                    playerTerritories.add(this.defender);
                    enemyTerritories.remove(this.defender);
                }else
                {
                    enemyTerritories.add(this.defender);
                    playerTerritories.remove(this.defender);
                }

                this.defender.Owner = this.attacker.Owner;
                this.defender.noOfArmies = this.attacker.noOfFightingArmies;
                this.defender.exchangeNeighbors();
            }
        }
        else
            this.attacker.noOfArmies -= this.attacker.noOfFightingArmies;
        
        
        
        setHeuristic();
        this.depth = depth;
    }
    
    
    private void copyTerritories()
    {
        gameTerritory = new Territory[noOfTerritories+2];
       
       for(int i=1;i<=noOfTerritories;i++)
          gameTerritory[i] = parent.gameTerritory[i].copy();
       
       playerTerritories = new ArrayList<>();
       parent.playerTerritories.forEach((territory) -> {
           Territory copyTerritory = gameTerritory[territory.id];
           playerTerritories.add(copyTerritory);
           territory.friendNeighbors.forEach((friend) -> {
               copyTerritory.friendNeighbors.add(gameTerritory[friend.id]);
           });
           territory.enemyNeighbors.forEach((enemy) -> {
               copyTerritory.enemyNeighbors.add(gameTerritory[enemy.id]);
           });
        });
       
       enemyTerritories = new ArrayList<>();
       parent.enemyTerritories.forEach((territory) -> {
           Territory copyTerritory = gameTerritory[territory.id];
           enemyTerritories.add(copyTerritory);
           territory.friendNeighbors.forEach((friend) -> {
               copyTerritory.friendNeighbors.add(gameTerritory[friend.id]);
           });
           territory.enemyNeighbors.forEach((enemy) -> {
               copyTerritory.enemyNeighbors.add(gameTerritory[enemy.id]);
           });
        });
       
       
        this.id = new int[noOfTerritories+2][2];
        this.id[0][0] = 2;
        this.id[0][1] = 2;
        for(int i=1;i<=noOfTerritories;i++)
        {
            this.id[i][0] = this.gameTerritory[i].noOfArmies;
            this.id[i][1] = this.gameTerritory[i].Owner == playerTerritories.get(0).Owner ? 1:2;
        } 
       
       
    }
    
    private void setHeuristic()
    {
       this.heuristic = playerTerritories.size() * 1000;
       playerTerritories.stream().map((territory) -> {
            this.heuristic += territory.noOfArmies * (territory.enemyNeighbors.size()+1);
            return territory;
        }).forEachOrdered((_item) -> {
            this.heuristic++;
        });
        enemyTerritories.stream().map((territory) -> {
            this.heuristic -= territory.noOfArmies * (territory.enemyNeighbors.size()+1);
            return territory;
        }).forEachOrdered((_item) -> {
            this.heuristic--;
        });
    }

    private double probabilityOfLose() {
        if(defender.noOfFightingArmies == 1)
        {
            switch(attacker.noOfFightingArmies)
            {
                case 1:
                    return 0.587;
                case 2:
                    return 0.421;
                default:
                    return 0.34;
            }
        }
        else
        {
           switch(attacker.noOfFightingArmies)
            {
                case 1:
                    return 0.745;
                case 2:
                    return 0.448;
                default:
                    return 0.292;
            } 
        }
    }
    
    public ArrayList<State> neighbors(boolean attacking,boolean isMax,int depth)
    {
        ArrayList<State> neighbors = new ArrayList<>();
        if(attacking)
        {
            neighbors.add(new State(this,depth));
            if(isMax)
            {
                playerTerritories.sort((Territory ter1, Territory ter2) -> ter1.enemyNeighbors.size() - ter2.enemyNeighbors.size());
                for(Territory Attacker:playerTerritories)
                {
                  
                  if(Attacker.canAttack())
                  {
                        int no = Attacker.noOfArmies - 1;
                        if(no > 3)
                            no = 3;
                        for(Territory Defender:Attacker.enemyNeighbors)
                        {
                            for(int i=no;i>1;i--)
                              neighbors.add(new State(this, Attacker, Defender,i,true,depth));
                        }
                  }
                  
                  if(neighbors.size() > 11)
                      break;
                  
                } 
            }else
            {
                enemyTerritories.sort((Territory ter1, Territory ter2) -> ter1.enemyNeighbors.size() - ter2.enemyNeighbors.size());
                for(Territory Attacker:enemyTerritories)
                {
                  
                  if(Attacker.canAttack())
                  {
                        int no = Attacker.noOfArmies - 1;
                        if(no > 3)
                            no = 3;
                        for(Territory Defender:Attacker.enemyNeighbors)
                        {
                            for(int i=no;i>1;i--)
                              neighbors.add(new State(this, Attacker, Defender,i,true,depth));
                        }
                  }
                  
                  if(neighbors.size() > 11)
                      break;
                  
                } 
            }
        }else
        {
            ArrayList<Territory> availableTerritories = new ArrayList<>();
            int newBouns;
            if(isMax)
            {
                playerTerritories.sort((Territory ter1, Territory ter2) -> ter1.enemyNeighbors.size() - ter2.enemyNeighbors.size());
                for(int i=playerTerritories.size()-1,j=0;i>0 && j<3;i--,j++)
                {
                    if(playerTerritories.get(i).enemyNeighbors.isEmpty())
                        break;
                    availableTerritories.add(playerTerritories.get(i));
                }
                
                
                if(this.bouns == 0)
                {
                    newBouns = playerTerritories.size();
                    newBouns = newBouns > 3 ? 3 : newBouns;
                }else
                {
                    newBouns = this.bouns;
                }
                
            }else
            {
                
                enemyTerritories.sort((Territory ter1, Territory ter2) -> ter1.enemyNeighbors.size() - ter2.enemyNeighbors.size());
                for(int i=enemyTerritories.size()-1,j=0;i>0 && j<3;i--,j++)
                {
                    if(enemyTerritories.get(i).enemyNeighbors.isEmpty())
                        break;
                    availableTerritories.add(enemyTerritories.get(i));
                }
                
                if(this.bouns == 0)
                {
                    newBouns = enemyTerritories.size();
                    newBouns = newBouns > 3 ? 3 : newBouns;   
                }else
                {
                    newBouns = this.bouns;
                }
                
            }
            this.setBounsCombination(availableTerritories,availableTerritories.size(),newBouns);
            for(Territory[] addedTerritories:bounsArmiesCombitations)
            {
                neighbors.add(new State(this,addedTerritories,depth,newBouns));
            }
            
        }
        
        return neighbors;
    }
    
    private void CombinationRepetitionUtil(int chosen[],ArrayList<Territory> arr,int index, int r, int start, int end) {
        if (index == r) { 
            Territory[] tempArray = new Territory[r];
            for (int i = 0; i < r; i++) { 
                tempArray[i] = arr.get(chosen[i]);
            }
            bounsArmiesCombitations.add(tempArray);
            return; 
        } 
   
        for (int i = start; i <= end; i++) { 
            chosen[index] = i; 
            CombinationRepetitionUtil(chosen, arr, index + 1, 
                    r, i, end); 
        } 
    } 
    
    private void setBounsCombination(ArrayList<Territory> arr, int n, int r) { 
        int chosen[] = new int[r + 1]; 
        this.bounsArmiesCombitations = new ArrayList<>();
        CombinationRepetitionUtil(chosen, arr, 0, r, 0, n - 1); 
    }
    
    public boolean noBounsArmies()
    {
        return this.bounsArmiesCounter == this.bouns;
    }
    
    public Territory getNextTerritoryToAdd()
    {
        return this.territoriesToAdd[bounsArmiesCounter++];
    }

    @Override
    public int compareTo(State other) {
        return this.heuristic - other.heuristic;
    }
    
}
