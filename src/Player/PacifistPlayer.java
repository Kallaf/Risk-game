/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Player;

import java.awt.Color;
import javax.swing.ImageIcon;
import static risk.game.RiskGame.getIcon;
import risk.game.Territory;

/**
 *
 * @author Ahmed
 */
public class PacifistPlayer extends AbstractPlayer {
    public static ImageIcon pacifistIcon = getIcon("Pacifist.jpg");

    public PacifistPlayer(Color color) {
        super(color);
    }
    
    @Override
    public ImageIcon Icon() {
        return pacifistIcon;
    }
    
    @Override
    public String getName() {
        return "Pacifist";
    }
    
    @Override
    protected Territory getTerritoryToAdd() {
        return getMin();
    }
    
    @Override
    public Territory getAttacker() {
        for(Territory territory:territories)
        {
            if(territory.noOfArmies>3)
            {
                for(Territory enemy:territory.enemyNeighbors)
                {
                    if(enemy.noOfArmies == 1)
                        return territory;
                }
            }
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
        return (this.attacker.noOfArmies > 3 && this.canAttack());
    }
    
    
    @Override
    protected void setNOofAttackingArmies() {
        attacker.noOfFightingArmies = attacker.noOfArmies - 1;
        if(attacker.noOfFightingArmies > 3)
            attacker.noOfFightingArmies = 3;
    }
}
