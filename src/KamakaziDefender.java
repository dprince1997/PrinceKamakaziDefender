 
//Main class
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

@SuppressWarnings("serial")
public class KamakaziDefender extends JFrame implements Commons {


	public KamakaziDefender()
    {
        add(new Board());
        setTitle("Kamakazi Defender");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(BOARD_WIDTH, BOARD_HEIGTH);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        
		JMenuBar mb = new JMenuBar();
		setJMenuBar(mb);
		
		JButton restart = new JButton("Restart");
		mb.add(restart);
		restart.addActionListener(new ActionListener()	{
			public void actionPerformed(ActionEvent e){
				dispose();
				new KamakaziDefender();
			}
		});
		
		JButton exit = new JButton("Exit");
		mb.add(exit);
		exit.addActionListener(new ActionListener()	{
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
		
    }

    public static void main(String[] args) {
        new KamakaziDefender(); 
    }
    
}