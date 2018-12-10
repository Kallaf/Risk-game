/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package risk.game;

import GUI.TerritoryButton;
import Player.Player;
import java.util.ArrayList;
import static risk.game.RiskGame.NEIGHBORS;
import static risk.game.RiskGame.gameTerritory;

/**
 *
 * @author Ahmed
 */
public class Territory {
    
    public Player Owner;
    public int noOfArmies;
    public TerritoryButton button;
    public final ArrayList<Territory> enemyNeighbors;
    public final ArrayList<Territory> friendNeighbors;
    public final int id;
    public int noOfFightingArmies;
    
    public Territory(TerritoryButton button,int id)
    {
        this.Owner = null;
        this.noOfArmies = 0;
        this.button = button;
        this.enemyNeighbors = new ArrayList<>();
        this.friendNeighbors = new ArrayList<>();
        this.id = id;
    }
    
    private Territory(int id)
    {
        this.enemyNeighbors = new ArrayList<>();
        this.friendNeighbors = new ArrayList<>();
        this.id = id;
    }
    
    public void setArmies(int no)
    {
        this.noOfArmies = no;
        this.button.setBackground(Owner.getColor());
        this.button.setText(String.valueOf(no));
    }
    
    public void setNeighbors()
    {
        
            for(int x:NEIGHBORS[this.id])
            {
                if(!(gameTerritory[x].Owner == null))
                {
                    if(gameTerritory[x].Owner.equals(this.Owner))
                    {
                       this.friendNeighbors.add(gameTerritory[x]);
                       gameTerritory[x].friendNeighbors.add(this);
                    }
                    else
                    {
                        this.enemyNeighbors.add(gameTerritory[x]);
                        gameTerritory[x].enemyNeighbors.add(this);
                    }    
                }
            }
    }
    
    public void exchangeNeighbors()
    {
        ArrayList<Territory> temp = new ArrayList<>();
        friendNeighbors.forEach((friend) -> {
            temp.add(friend);
        });
        friendNeighbors.clear();
        enemyNeighbors.forEach((enemy) -> {
            friendNeighbors.add(enemy);
            enemy.enemyNeighbors.remove(this);
            enemy.friendNeighbors.add(this);
        });
        enemyNeighbors.clear();
        temp.forEach((territory) -> {
            enemyNeighbors.add(territory);
            territory.friendNeighbors.remove(this);
            territory.enemyNeighbors.add(this);
        });
        temp.clear();
    }
    
    public boolean canAttack()
    {
        return (noOfArmies>1 && !enemyNeighbors.isEmpty());
    }
    
    
    
    public int compareTo(Territory t) {
       return this.noOfArmies - t.noOfArmies;
    }
    
    public Territory copy()
    {
        Territory copy = new Territory(this.id);
        copy.Owner = this.Owner;
        copy.noOfArmies = this.noOfArmies;
        return copy;
    }
    
}
