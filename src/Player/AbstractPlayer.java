/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Player;

import java.awt.Color;
import java.util.ArrayList;
import static risk.game.RiskGame.Step.ADDING_ARMIES;
import static risk.game.RiskGame.Step.ATTACK;
import static risk.game.RiskGame.Step.INITIALIZING2;
import static risk.game.RiskGame.Step.CHOOSING_ATTACKER;
import static risk.game.RiskGame.Step.CHOOSING_DEFENDER;
import static risk.game.RiskGame.Step.WINNER;
import static risk.game.RiskGame.TIMER;
import static risk.game.RiskGame.blueDice;
import static risk.game.RiskGame.changePlayer;
import static risk.game.RiskGame.currentPlayer;
import static risk.game.RiskGame.gameFrame;
import static risk.game.RiskGame.unSelectedTerritories;
import risk.game.Territory;
import static risk.game.RiskGame.currentStep;
import static risk.game.RiskGame.noOfTerritories;
import static risk.game.RiskGame.players;
import static risk.game.RiskGame.redDice;

/**
 *
 * @author Ahmed
 */
public abstract class AbstractPlayer implements Player,Cloneable{
    
    protected final Color color;
    protected final ArrayList<Territory> territories;
    protected int noOfAdditionalArmies;
    protected boolean startAdding;
    protected Territory attacker;
    protected Territory defender;
    protected boolean attacking = false;
    
    //P = f ∗ L + T. (performance measure)
    protected long L = 0;
    protected long T = 0;
    
    public AbstractPlayer(Color color)
    {
        this.color = color;
        this.territories = new ArrayList<>();
        this.noOfAdditionalArmies = 20;
        this.startAdding = true;
    }
   
   @Override
   public boolean continueAttacking()
   {
       return true;
   }
    
   @Override
   public Territory getAttacker()
   {
       return this.attacker;
   }
   
   @Override
   public Territory getDefender()
   {
       return this.defender;
   }
    
    @Override
    public void setAttacking()
    {
        this.attacking = false;
    }
    
    @Override
    public Color getColor() {
        return color;
    }
    
    @Override
    public ArrayList<Territory> getTerritories()
    {
        return this.territories;
    }
    
    @Override
    public void clickedTerritory(Territory territory) {
        
    }
    
    
    @Override
    public void clickedEndAttack()
    {
        
    }
    
    @Override
    public void play()
    {
        L++;
        switch(currentStep)
       {
           case INITIALIZING1:
               intializeTerritory(unSelectedTerritories.get((int) (Math.random() * unSelectedTerritories.size())));
               break;
           case INITIALIZING2:
               addArmy(getTerritoryToAdd());
               break;
           case ADDING_ARMIES:
               setBounsArmies();
               addArmies(getTerritoryToAdd());
               break;
           case CHOOSING_ATTACKER:
               if(canAttack())
               {
                   setAttacker(getAttacker());
                   if(attacker != null)
                    setNOofAttackingArmies();
               }
               else
                   setAttacker(null);
               break;
           case CHOOSING_DEFENDER:
               setDefender(getDefender());
               setNOofDefendingArmies();
               break;
           case ATTACK:
               attack();
               if(currentStep != WINNER)
               {
                  if(!continueAttacking() || currentStep == CHOOSING_ATTACKER )
                      endAttack();
                  else
                  {
                      setNOofAttackingArmies();
                      setNOofDefendingArmies();
                  } 
               }
               
               break;
           default:
               //do nothing;
       }
    }

    
    protected abstract Territory getTerritoryToAdd();
    
    protected void intializeTerritory(Territory territory) {
         if(territory.Owner == null)
         {
            this.territories.add(territory);
            unSelectedTerritories.remove(territory);
            this.noOfAdditionalArmies --;
            territory.Owner = players[currentPlayer];
            territory.setArmies(1);
            territory.setNeighbors();
            if(unSelectedTerritories.isEmpty())
            {
                TIMER.setDelay(500);
                if(this.noOfAdditionalArmies > 0)
                    currentStep = INITIALIZING2;
                else
                    currentStep = ADDING_ARMIES;
            }

            changePlayer();
         }
    }
    
    protected void addArmy(Territory territory) {
        setAdditionalArmies();
        territory.setArmies(territory.noOfArmies+1);
        if(currentPlayer == 1 && this.noOfAdditionalArmies <= 0)
        {
            currentStep =  ADDING_ARMIES;
        }
        changePlayer();
        
    }

    protected void setBounsArmies()
    {
        if(startAdding)
        {
          noOfAdditionalArmies = this.territories.size()/3;
            
          if(noOfAdditionalArmies<3)
            noOfAdditionalArmies = 3;  
          startAdding = false;
          gameFrame.getAdditionalArmiesLabel().setText("bonus armies " + String.valueOf(noOfAdditionalArmies));
        }
    }
    
    protected void addArmies(Territory territory) {
           territory.setArmies(territory.noOfArmies+1);
           setAdditionalArmies();
           if(this.noOfAdditionalArmies == 0)
           {
             startAdding = true;
             currentStep = CHOOSING_ATTACKER;
             gameFrame.getAdditionalArmiesLabel().setText("select attacker");
             TIMER.setDelay(750);
          } 
        
            
    }
    
    protected void setAdditionalArmies()
    {
        this.noOfAdditionalArmies --;
        if(noOfAdditionalArmies > 0)
            gameFrame.getAdditionalArmiesLabel().setText("bonus armies " + String.valueOf(noOfAdditionalArmies));
        else
            gameFrame.getAdditionalArmiesLabel().setText("");
    }

    
    @Override
    public boolean canAttack()
    {
        return territories.stream().anyMatch((territory) -> (territory.canAttack()));
    }
    
    @Override
    public void setAttacker(Territory attacker) {
        if(attacker == null)
        {
            this.attacker = null;
            currentStep = ADDING_ARMIES;
            gameFrame.getEndAttackButton().setEnabled(false);
            gameFrame.getEndAttackButton().setBackground(Color.white);
            TIMER.setDelay(500);
            changePlayer();
        }
        else if(attacker.canAttack())
        {
            this.attacker = attacker;
            attacker.button.setBackground(Color.red);
            attacker.enemyNeighbors.forEach((enemy) -> {
                enemy.button.setBackground(Color.gray);
            });
            gameFrame.getEndAttackButton().setEnabled(false);
            currentStep = CHOOSING_DEFENDER;
            gameFrame.getEndAttackButton().setEnabled(false);
            gameFrame.getEndAttackButton().setBackground(Color.white);
            gameFrame.getAdditionalArmiesLabel().setText("select defender");
        }
    }
    
    protected void setDefender(Territory defender) {
       this.defender = defender;
       attacker.enemyNeighbors.forEach((enemy) -> {
                enemy.button.setBackground(enemy.Owner.getColor());
            });
       this.defender.button.setBackground(Color.blue);
       gameFrame.getAdditionalArmiesLabel().setText("");
       currentStep = ATTACK;
    }
    
    
    protected Territory getMax()
    {
        
        Territory max = this.territories.get(0);
        for(int i=1;i<this.territories.size();i++)
        {
            if(territories.get(i).compareTo(max) > 0)
            {
                max = territories.get(i);
            }
        }
        return max;
    }
    
    protected Territory getMin()
    {
        Territory min = this.territories.get(0);
        for(int i=1;i<this.territories.size();i++)
        {
            if(territories.get(i).compareTo(min) < 0)
            {
                min = territories.get(i);
            }
        }
        return min;
    }
    
    @Override
    public void attack()
    {
        redDice[0] = (int) (Math.random() * 6) + 1;
        for(int i=1;i<attacker.noOfFightingArmies;i++)
        {
            redDice[i] = (int) (Math.random() * 6) + 1;
            for(int j=i;j>0;j--)
            {
                if(redDice[j] < redDice[j-1])
                    break;
                int temp = redDice[j];
                redDice[j] = redDice[j-1];
                redDice[j-1] = temp;
            }
        }
        blueDice[0] = (int) (Math.random() * 6) + 1; 
        if(defender.noOfFightingArmies == 2)
        {
            blueDice[1] = (int) (Math.random() * 6) + 1;
            if(blueDice[1] > blueDice[0])
            {
                int temp = blueDice[0];
                blueDice[0] = blueDice[1];
                blueDice[1] = temp;
            }
        }
        if(blueDice[0] >= redDice[0])
        {
            attacker.setArmies(attacker.noOfArmies-attacker.noOfFightingArmies);
            attacker.button.setBackground(Color.red);
            if(attacker.noOfArmies == 1)
            {
                currentStep = CHOOSING_ATTACKER;
            }
        }
        else
        {
            defender.setArmies(defender.noOfArmies-defender.noOfFightingArmies);
            defender.button.setBackground(Color.blue);
            if(defender.noOfArmies == 0)
            {
                attacker.setArmies(attacker.noOfArmies-attacker.noOfFightingArmies);
                attacker.button.setBackground(Color.red);
                currentStep = CHOOSING_ATTACKER;
                this.territories.add(defender);
                defender.Owner.getTerritories().remove(defender);
                defender.Owner = attacker.Owner;
                defender.setArmies(attacker.noOfFightingArmies);
                defender.exchangeNeighbors();
                if(this.territories.size() == noOfTerritories)
                {
                    System.out.println("p= f*L+T = 1*"+L+'+'+T+" = "+(1*L+T));
                    currentStep = WINNER;
                    this.attacker.button.setBackground(attacker.Owner.getColor());
                    gameFrame.getAdditionalArmiesLabel().setText("    WINNER");
                    TIMER.stop();
                }
            }
        }
        
    }
    
    @Override
    public void endAttack()
    {
        this.attacker.button.setBackground(attacker.Owner.getColor());
        this.defender.button.setBackground(defender.Owner.getColor());
        currentStep = CHOOSING_ATTACKER;
        gameFrame.getAdditionalArmiesLabel().setText("select attacker");
    }

    protected void setNOofAttackingArmies() {
        
    }
    
    protected void setNOofDefendingArmies() {
        defender.noOfFightingArmies = (defender.noOfArmies == 1)?1:2;
    }
    
    @Override
    public Player clone() throws CloneNotSupportedException {
                return (Player) super.clone();
        }
    
    
}
