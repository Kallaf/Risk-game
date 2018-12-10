/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author Ahmed
 */
public interface GameFrame {
 public JLabel getAdditionalArmiesLabel();
 public JButton getEndAttackButton();
 public JLabel getIconLabel();
 public JLabel getPlayerNameLabel();
 public void start();
}
