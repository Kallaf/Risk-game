/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import static risk.game.RiskGame.currentPlayer;
import static risk.game.RiskGame.gameTerritory;
import static risk.game.RiskGame.players;
import static risk.game.RiskGame.unSelectedTerritories;
import risk.game.Territory;

/**
 *
 * @author Ahmed
 */
public class TerritoryButton extends JButton {
    
    private final Territory territory;
    public TerritoryButton(int no)
    {
        this.territory = new Territory(this,no);
        gameTerritory[no] = this.territory;
        unSelectedTerritories.add(this.territory);
        this.setFocusPainted(false);
        this.setFont(new Font("Impact",0,14));
        this.setMargin(new Insets(0,0,0,0));
        this.setBackground(Color.white);
        this.setForeground(Color.white);
        this.addActionListener((ActionEvent evt) -> {
            buttonActionPerformed(evt);
        });

    }
    
    private void buttonActionPerformed(ActionEvent evt) {
        players[currentPlayer].clickedTerritory(this.territory);
    }
    

    
}
