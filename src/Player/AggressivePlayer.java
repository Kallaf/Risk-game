/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Player;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import static risk.game.RiskGame.getIcon;
import risk.game.Territory;

/**
 *
 * @author Ahmed
 */
public class AggressivePlayer extends AbstractPlayer {
    
    public static ImageIcon aggresiveIcon = getIcon("aggressive.jpg");

    public AggressivePlayer(Color color) {
        super(color);
    }
    
    @Override
    public ImageIcon Icon() {
        return aggresiveIcon;
    }
    
    @Override
    public String getName() {
        return "Aggressive";
    }

    @Override
    protected Territory getTerritoryToAdd() {
         return getMax();
    }
    
    @Override
    public Territory getAttacker() {
        if(this.canAttack())
        {
            ArrayList<Territory> canAttackTerritories = new ArrayList<>();
            territories.stream().filter((ter) -> (ter.canAttack())).forEachOrdered((ter) -> {
                canAttackTerritories.add(ter);
            });
            Territory max = canAttackTerritories.get(0);
            for(int i=1;i<canAttackTerritories.size();i++)
            {
                if(canAttackTerritories.get(i).compareTo(max) > 0)
                {
                    max = canAttackTerritories.get(i);
                }else if(canAttackTerritories.get(i).compareTo(max) == 0)
                {
                    if(canAttackTerritories.get(i).enemyNeighbors.size() > max.enemyNeighbors.size())
                        max = canAttackTerritories.get(i);
                }
            }
            return max;
        }
        return null;
    }

    @Override
    public Territory getDefender() {
        Territory min = this.attacker.enemyNeighbors.get(0);
        for(int i=1;i<this.attacker.enemyNeighbors.size();i++)
        {
            if(this.attacker.enemyNeighbors.get(i).compareTo(min) < 0)
            {
                min = this.attacker.enemyNeighbors.get(i);
            }
        }
        return min;
    }
    
    @Override
    public boolean continueAttacking()
    {
        return (this.attacker == getMax() && this.canAttack());
    }
    
    
    @Override
    protected void setNOofAttackingArmies() {
        attacker.noOfFightingArmies = attacker.noOfArmies - 1;
        if(attacker.noOfFightingArmies > 3)
            attacker.noOfFightingArmies = 3;
    }
}
