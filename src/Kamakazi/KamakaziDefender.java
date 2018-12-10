package Kamakazi; 

/*David Prince - Everything
 * Kamakazi Defender: This program is a spinoff of SpaceInvaders with guns.
 * Purpose: To play a game
 * Serialization: Counts number of plays in a .dat file, and keeps track of high score in a textfile
 * Model Classes are Board.java, Commons.java, and KamakaziDefender.java
 * Controller Classes are Characters.java, Bomb.java, Kamakazi.java, Boards.java, and Player.java.
 * I would like to add lives, more levels, and things to unlock based on scores earned.
 * 
 * Controls: Move with the arrow keys, shoot with ctrl, click for buttons.
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

//Main class
@SuppressWarnings("serial")
public class KamakaziDefender extends JFrame implements Commons {


	public KamakaziDefender() throws IOException
    {
        add(new Board()); //creates interface
        setTitle("Kamakazi Defender");  //sets title
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(BOARD_WIDTH, BOARD_HEIGTH); //size of game interface
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        
		JMenuBar mb = new JMenuBar(); //menu bar
		setJMenuBar(mb);
		
		JButton restart = new JButton("Restart"); //restart button
		mb.add(restart);
		restart.addActionListener(new ActionListener()	{
			public void actionPerformed(ActionEvent e){
				dispose();
				try {
					new KamakaziDefender();
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		JButton exit = new JButton("Exit"); //exit button
		mb.add(exit);
		exit.addActionListener(new ActionListener()	{
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
		
    }

    public static void main(String[] args) throws IOException{
        new KamakaziDefender();                       
    }
    
}