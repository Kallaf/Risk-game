/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.Timer;
import static risk.game.RiskGame.Step.CHOOSING_ATTACKER;
import static risk.game.RiskGame.Step.WINNER;
import static risk.game.RiskGame.blueDice;
import static risk.game.RiskGame.blueDiceIcons;
import static risk.game.RiskGame.currentPlayer;
import static risk.game.RiskGame.currentStep;
import static risk.game.RiskGame.players;
import static risk.game.RiskGame.redDice;
import static risk.game.RiskGame.redDiceIcons;

/**
 *
 * @author Ahmed
 */
public class DiceFrame extends javax.swing.JFrame {

    public boolean opened;
    private boolean twoDice = true;
    private boolean threeDice = true;
    private final Timer timer;
    private boolean rolling = false;
    public DiceFrame() {
        opened = false;
        initComponents();
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    players[currentPlayer].setAttacking();
                    players[currentPlayer].endAttack();
                    opened = false;
                }
        });
        timer = new Timer(140, (ActionEvent ae) -> {
            roll();
        });
}

    public void star()
    {
        opened = true;
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setVisible(true);
        this.attackerIconLabel.setIcon(players[currentPlayer].Icon());
        this.defenderIconLabel.setIcon(players[(currentPlayer+1)%2].Icon());
        this.attackerNameLabel.setText(players[currentPlayer].getName());
        this.defenderNameLabel.setText(players[(currentPlayer+1)%2].getName());
        this.attackerNameLabel.setForeground(players[currentPlayer].getColor());
        this.defenderNameLabel.setForeground(players[(currentPlayer+1)%2].getColor());
        this.redDiceButton1.setIcon(null);
        this.redDiceButton2.setIcon(null);
        this.redDiceButton3.setIcon(null);
        this.blueDiceButton1.setIcon(null);
        this.blueDiceButton2.setIcon(null);
        this.attackerArmiesLabel.setText(String.valueOf(players[currentPlayer].getAttacker().noOfArmies));
        this.defenderArmiesLabel.setText(String.valueOf(players[currentPlayer].getDefender().noOfArmies));
        this.rollButton.setEnabled(true);
        this.rolling = false;
        switch(players[currentPlayer].getAttacker().noOfArmies)
        {
            case 2:
                players[currentPlayer].getAttacker().noOfFightingArmies = 1;
                twoDice = false;
                threeDice = false;
                this.redDiceButton2.setEnabled(false);
                this.redDiceButton3.setEnabled(false);
                this.redDiceButton2.setBackground(Color.white);
                this.redDiceButton3.setBackground(Color.white);
                break;
            case 3:
                players[currentPlayer].getAttacker().noOfFightingArmies = 2;
                twoDice = true;
                threeDice = false;
                this.redDiceButton2.setEnabled(true);
                this.redDiceButton3.setEnabled(false);
                this.redDiceButton2.setBackground(new Color(153,0,0));
                this.redDiceButton3.setBackground(Color.white);
                break;
            default:
                players[currentPlayer].getAttacker().noOfFightingArmies = 3;
                twoDice = true;
                threeDice = true;
                this.redDiceButton2.setEnabled(true);
                this.redDiceButton3.setEnabled(true);
                this.redDiceButton2.setBackground(new Color(153,0,0));
                this.redDiceButton3.setBackground(new Color(153,0,0));
        }
        
        switch(players[currentPlayer].getDefender().noOfArmies)
        {
            case 1:
                players[currentPlayer].getDefender().noOfFightingArmies = 1;
                this.blueDiceButton2.setBackground(Color.white);
                break;
            default:
                players[currentPlayer].getDefender().noOfFightingArmies = 2;
                this.blueDiceButton2.setBackground(new Color(0,0,153));
        }
    }
    
    private void close()
    {
        if(currentStep == CHOOSING_ATTACKER)
                {
                 players[currentPlayer].setAttacking();
                 players[currentPlayer].endAttack();   
                }
                this.setVisible(false);
                if(!players[currentPlayer].canAttack())
                {
                    players[currentPlayer].setAttacker(null);
                }
                opened = false;
    }
    
    
    private int counter = 0;
    private void roll()
    {
        counter++;
        if(counter == 8)
        {
        players[currentPlayer].attack();
        this.redDiceButton1.setIcon(redDiceIcons[redDice[0]]);
        if(players[currentPlayer].getAttacker().noOfFightingArmies > 1)
        {
            this.redDiceButton2.setIcon(redDiceIcons[redDice[1]]);
            if(players[currentPlayer].getAttacker().noOfFightingArmies > 2)
                this.redDiceButton3.setIcon(redDiceIcons[redDice[2]]);
        }
        this.blueDiceButton1.setIcon(blueDiceIcons[blueDice[0]]);
        if(players[currentPlayer].getDefender().noOfFightingArmies > 1)
        {
            this.blueDiceButton2.setIcon(blueDiceIcons[blueDice[1]]);
        }
        }
        else if(counter == 16)
        {
            timer.stop();
            counter = 0;
            if(currentStep == CHOOSING_ATTACKER || currentStep == WINNER)
            {
                close();
            }
            else
                this.star();
        }else if(counter < 8)
        {
            this.redDiceButton1.setIcon(redDiceIcons[(int) (Math.random() * 6) + 1]);
            if(players[currentPlayer].getAttacker().noOfFightingArmies > 1)
            {
                this.redDiceButton2.setIcon(redDiceIcons[(int) (Math.random() * 6) + 1]);
                if(players[currentPlayer].getAttacker().noOfFightingArmies > 2)
                    this.redDiceButton3.setIcon(redDiceIcons[(int) (Math.random() * 6) + 1]);
            }
            this.blueDiceButton1.setIcon(blueDiceIcons[(int) (Math.random() * 6) + 1]);
            if(players[currentPlayer].getDefender().noOfFightingArmies > 1)
            {
                this.blueDiceButton2.setIcon(blueDiceIcons[(int) (Math.random() * 6) + 1]);
            }
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        attackerNameLabel = new javax.swing.JLabel();
        attackerIconLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        defenderNameLabel = new javax.swing.JLabel();
        defenderIconLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        redDiceButton3 = new javax.swing.JButton();
        redDiceButton1 = new javax.swing.JButton();
        blueDiceButton1 = new javax.swing.JButton();
        redDiceButton2 = new javax.swing.JButton();
        blueDiceButton2 = new javax.swing.JButton();
        rollButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        attackerArmiesLabel = new javax.swing.JLabel();
        defenderArmiesLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(0, 103, 103));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        attackerNameLabel.setFont(new java.awt.Font("Constantia", 1, 24)); // NOI18N
        attackerNameLabel.setText("Aggresive");
        attackerNameLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        attackerNameLabel.setPreferredSize(new java.awt.Dimension(196, 50));
        jPanel2.add(attackerNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 13, 122, 40));
        jPanel2.add(attackerIconLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 0, 66, 65));

        jPanel4.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 210, 66));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        defenderNameLabel.setFont(new java.awt.Font("Constantia", 1, 24)); // NOI18N
        defenderNameLabel.setText("Aggresive");
        defenderNameLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        defenderNameLabel.setPreferredSize(new java.awt.Dimension(196, 50));
        jPanel3.add(defenderNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 13, 122, 40));
        jPanel3.add(defenderIconLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 0, 66, 65));

        jPanel4.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(259, 90, 210, 66));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 102, 102));
        jLabel1.setText("Attacker");
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, -1, -1));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 255));
        jLabel2.setText("Defender");
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 50, -1, -1));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        redDiceButton3.setBackground(new java.awt.Color(153, 0, 0));
        redDiceButton3.setForeground(new java.awt.Color(255, 255, 255));
        redDiceButton3.setBorder(null);
        redDiceButton3.setFocusable(false);
        redDiceButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redDiceButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(redDiceButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(87, 120, 30, 30));

        redDiceButton1.setBackground(new java.awt.Color(153, 0, 0));
        redDiceButton1.setForeground(new java.awt.Color(255, 255, 255));
        redDiceButton1.setBorder(null);
        redDiceButton1.setFocusable(false);
        redDiceButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redDiceButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(redDiceButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(87, 30, 30, 30));

        blueDiceButton1.setBackground(new java.awt.Color(0, 0, 153));
        blueDiceButton1.setForeground(new java.awt.Color(255, 255, 255));
        blueDiceButton1.setBorder(null);
        blueDiceButton1.setFocusable(false);
        jPanel1.add(blueDiceButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(292, 30, 30, 30));

        redDiceButton2.setBackground(new java.awt.Color(153, 0, 0));
        redDiceButton2.setForeground(new java.awt.Color(255, 255, 255));
        redDiceButton2.setBorder(null);
        redDiceButton2.setFocusable(false);
        redDiceButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redDiceButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(redDiceButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(87, 75, 30, 30));

        blueDiceButton2.setBackground(new java.awt.Color(0, 0, 153));
        blueDiceButton2.setForeground(new java.awt.Color(255, 255, 255));
        blueDiceButton2.setBorder(null);
        blueDiceButton2.setFocusable(false);
        jPanel1.add(blueDiceButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(292, 75, 30, 30));

        jPanel4.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 200, 409, 190));

        rollButton.setBackground(new java.awt.Color(0, 153, 153));
        rollButton.setFont(new java.awt.Font("Comic Sans MS", 1, 24)); // NOI18N
        rollButton.setText("Roll");
        rollButton.setFocusable(false);
        rollButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rollButtonActionPerformed(evt);
            }
        });
        jPanel4.add(rollButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 400, 90, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("click on your dice to select number of dice to roll");
        jPanel4.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 460, 300, -1));

        attackerArmiesLabel.setFont(new java.awt.Font("Trebuchet MS", 0, 24)); // NOI18N
        attackerArmiesLabel.setText("3");
        jPanel4.add(attackerArmiesLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 160, 50, -1));

        defenderArmiesLabel.setFont(new java.awt.Font("Trebuchet MS", 0, 24)); // NOI18N
        defenderArmiesLabel.setText("2");
        jPanel4.add(defenderArmiesLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 160, 50, -1));

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 500));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void redDiceButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redDiceButton1ActionPerformed
        if(!rolling)
        {
           players[currentPlayer].getAttacker().noOfFightingArmies = 1;
           if(twoDice)
           {
               this.redDiceButton2.setBackground(Color.gray);
               if(threeDice)
                   this.redDiceButton3.setBackground(Color.gray);
           }   
        }
               
    }//GEN-LAST:event_redDiceButton1ActionPerformed

    private void redDiceButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redDiceButton2ActionPerformed
        if(!rolling)
        {
            players[currentPlayer].getAttacker().noOfFightingArmies = 2;
            this.redDiceButton2.setBackground(new Color(153,0,0));
            if(threeDice)
                this.redDiceButton3.setBackground(Color.gray);
        }
    }//GEN-LAST:event_redDiceButton2ActionPerformed

    private void redDiceButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redDiceButton3ActionPerformed
        if(!rolling)
        {
            players[currentPlayer].getAttacker().noOfFightingArmies = 3;
            this.redDiceButton2.setBackground(new Color(153,0,0));
            this.redDiceButton3.setBackground(new Color(153,0,0));
        }
    }//GEN-LAST:event_redDiceButton3ActionPerformed

    private void rollButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rollButtonActionPerformed
        this.rollButton.setEnabled(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.rolling = true;
        if(players[currentPlayer].getAttacker().noOfFightingArmies < 3)
        {
            this.redDiceButton3.setBackground(Color.white);
            this.redDiceButton3.setEnabled(false);
           if(players[currentPlayer].getAttacker().noOfFightingArmies < 2)
           {
               this.redDiceButton2.setBackground(Color.white);
               this.redDiceButton2.setEnabled(false);
           }
        }
        timer.start();
    }//GEN-LAST:event_rollButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel attackerArmiesLabel;
    public javax.swing.JLabel attackerIconLabel;
    public javax.swing.JLabel attackerNameLabel;
    private javax.swing.JButton blueDiceButton1;
    private javax.swing.JButton blueDiceButton2;
    private javax.swing.JLabel defenderArmiesLabel;
    public javax.swing.JLabel defenderIconLabel;
    public javax.swing.JLabel defenderNameLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JButton redDiceButton1;
    private javax.swing.JButton redDiceButton2;
    private javax.swing.JButton redDiceButton3;
    private javax.swing.JButton rollButton;
    // End of variables declaration//GEN-END:variables
}
