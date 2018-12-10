import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class MainMenu 
{

	public static void main(String [] args)
	{
		JFrame f = new JFrame();
		f.setVisible(true);
		f.setSize(Commons.BOARD_WIDTH, Commons.BOARD_HEIGTH);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		
		JMenuBar mb = new JMenuBar();
		JMenu restart = new JMenu("Restart");
		mb.add(restart);
		JMenu edit = new JMenu("Edit");
		mb.add(edit);
		
		f.setJMenuBar(mb);
	}
}