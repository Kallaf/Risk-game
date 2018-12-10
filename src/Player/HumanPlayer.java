/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Player;
import java.awt.Color;
import javax.swing.ImageIcon;
import static risk.game.RiskGame.Step.ADDING_ARMIES;
import static risk.game.RiskGame.Step.ATTACK;
import static risk.game.RiskGame.Step.CHOOSING_ATTACKER;
import static risk.game.RiskGame.Step.CHOOSING_DEFENDER;

import static risk.game.RiskGame.getIcon;
import risk.game.Territory;
import static risk.game.RiskGame.Step.INITIALIZING1;
import static risk.game.RiskGame.Step.INITIALIZING2;
import static risk.game.RiskGame.diceFrame;
import static risk.game.RiskGame.gameFrame;
import static risk.game.RiskGame.unSelectedTerritories;
import static risk.game.RiskGame.currentStep;

/**
 *
 * @author Ahmed
 */
public class HumanPlayer extends AbstractPlayer {

    public static ImageIcon humanIcon = getIcon("human.png");

    public HumanPlayer(Color color) {
        super(color);
    }
    
    private void cancelAttack(Territory territory)
    {
        if(this.attacker == territory)
        {
            attacker.button.setBackground(this.getColor());
            attacker.enemyNeighbors.forEach((enemy) -> {
                enemy.button.setBackground(enemy.Owner.getColor());
            });
            gameFrame.getEndAttackButton().setEnabled(true);
            gameFrame.getEndAttackButton().setBackground(new Color(150,0,0));
            gameFrame.getAdditionalArmiesLabel().setText("select attacker");
            this.attacker = null;
            currentStep = CHOOSING_ATTACKER;
        }
    }
    
    @Override
    public void clickedTerritory(Territory territory) {
       if(territory.Owner == this)
       {
       switch(currentStep)
       {
           case INITIALIZING2:
               addArmy(territory);
               break;    
           case ADDING_ARMIES:
               addArmies(territory);
               break;
           case CHOOSING_ATTACKER:
               setAttacker(territory);
               break;
           case CHOOSING_DEFENDER:
               cancelAttack(territory);
               break;
           case ATTACK:
               
               break;
           default:
               //do nothing;
       }
     }
     else if(currentStep == CHOOSING_DEFENDER)
     {
         if(attacker.enemyNeighbors.contains(territory))
         {
             setDefender(territory);
         }
     }
    }

    @Override
    public ImageIcon Icon() {
        return humanIcon;
    }
    
    @Override
    public String getName() {
        return "Human";
    }
    
    @Override
    public void play()
    {
        if(currentStep == INITIALIZING1)
        {
            intializeTerritory(unSelectedTerritories.get((int) (Math.random() * unSelectedTerritories.size())));
        }
        else if(currentStep == INITIALIZING2)
        {
            this.noOfAdditionalArmies++;
            setAdditionalArmies();
        }
        else if(currentStep == ADDING_ARMIES && startAdding)
        {
            noOfAdditionalArmies = this.territories.size()/3;
            
            if(noOfAdditionalArmies<3)
                noOfAdditionalArmies = 3;
            gameFrame.getAdditionalArmiesLabel().setText("bonus armies " + String.valueOf(noOfAdditionalArmies));
            startAdding = false;
        }
        else if(currentStep == CHOOSING_ATTACKER)
        {
               if(canAttack())
               {
                   gameFrame.getEndAttackButton().setEnabled(true);
                   gameFrame.getEndAttackButton().setBackground(new Color(150,0,0));
               }else if(!diceFrame.opened)
               {
                   setAttacker(null);
               }
               
        }else if(currentStep == ATTACK && !attacking)
        {
            this.attacking = true;
            diceFrame.star();
        }
        
    }

    @Override
    protected Territory getTerritoryToAdd() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void clickedEndAttack()
    {
        if(currentStep == CHOOSING_ATTACKER)
        {
            setAttacker(null);
        }
    }
    
}
