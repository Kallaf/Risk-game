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
public class PassivePlayer extends AbstractPlayer {
    public static ImageIcon passiveIcon = getIcon("passive.jpg");

    public PassivePlayer(Color color) {
        super(color);
    }
    
   @Override
    public ImageIcon Icon() {
        return passiveIcon;
    }
    
    @Override
    public String getName() {
        return "Passive";
    }
    
    @Override
    protected Territory getTerritoryToAdd() {
        return getMin();
    }
    
    @Override
    public Territory getAttacker() {
        return null;
    }

    @Override
    public Territory getDefender() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
