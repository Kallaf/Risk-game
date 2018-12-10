/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package risk.game;

import GUI.DiceFrame;
import GUI.GameFrame;
import GUI.MapFrame;

import Player.Player;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import static risk.game.RiskGame.Step.INITIALIZING1;


/**
 *
 * @author Ahmed
 */
public class RiskGame {
    public static int noOfTerritories;
    public static GameFrame gameFrame;
    public static DiceFrame diceFrame;
    
    public static ImageIcon[] redDiceIcons;
    public static ImageIcon[] blueDiceIcons;
    public static int[] redDice = new int[4];
    public static int[] blueDice = new int[3];
    
    public static Color[] playersColor = {new Color(0,153,153),new Color(245,194,92)};
    public static Player[] players = new Player[2];
    public static int currentPlayer = 0;
    public static void changePlayer()
    {
       currentPlayer = (++currentPlayer) % 2;
       
    }
    
    public static Territory[] gameTerritory = new Territory[52];
    public static ArrayList<Territory> unSelectedTerritories = new ArrayList<>();
    public static int[][] NEIGHBORS;
    
    
    
    public static final Timer TIMER = new Timer(175,new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
         gameFrame.getPlayerNameLabel().setText(players[currentPlayer].getName());
         gameFrame.getPlayerNameLabel().setForeground(players[currentPlayer].getColor());
         gameFrame.getIconLabel().setIcon(players[currentPlayer].Icon());
         players[currentPlayer].play();
        }
    });
    
    public static enum Step{
      INITIALIZING1,INITIALIZING2,ADDING_ARMIES,CHOOSING_ATTACKER,CHOOSING_DEFENDER,ATTACK,WINNER;  
    }
    public static Step currentStep = INITIALIZING1;
    
    public static ImageIcon getIcon(String imageName)
    {
        ImageIcon imageIcon = new ImageIcon("icons/"+imageName);
        Image img = imageIcon.getImage();
        return new ImageIcon(img.getScaledInstance(60,60,Image.SCALE_SMOOTH ));
    }
    
    
    
    public static void main(String[] args) {
        diceFrame = new DiceFrame();
        redDiceIcons = new ImageIcon[7];
        blueDiceIcons = new ImageIcon[7];
        for(int i=1;i<7;i++)
        {
            ImageIcon imageIcon = new ImageIcon("icons/"+"red dice "+String.valueOf(i)+".png");
            Image img = imageIcon.getImage();
            redDiceIcons[i] = new ImageIcon(img.getScaledInstance(30,30,Image.SCALE_SMOOTH ));
        }
        for(int i=1;i<7;i++)
        {
            ImageIcon imageIcon = new ImageIcon("icons/"+"blue dice "+String.valueOf(i)+".png");
            Image img = imageIcon.getImage();
            blueDiceIcons[i] = new ImageIcon(img.getScaledInstance(30,30,Image.SCALE_SMOOTH ));
        }
        MapFrame mapFrame = new MapFrame();
        mapFrame.start();
    }
    
}
