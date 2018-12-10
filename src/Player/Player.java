/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Player;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import risk.game.Territory;


/**
 *
 * @author Ahmed
 */
public interface Player {
   public Color getColor();
   public ImageIcon Icon();
   public String getName();
   public void clickedTerritory(Territory territory);
   public void clickedEndAttack();
   public void play();
   public void endAttack();
   public void attack();
   public void setAttacking();
   public Territory getAttacker();
   public Territory getDefender();
   public ArrayList<Territory> getTerritories();
   public boolean canAttack();
   public void setAttacker(Territory attacker);
   public boolean continueAttacking();
}
