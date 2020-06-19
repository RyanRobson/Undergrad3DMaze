
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


public class Maze3D extends JFrame
{
  public Maze3D(String args[]) throws IOException 
  {
    super("3D Maze");
    //DungeonGenerator dg = new DungeonGenerator();
    //dg.main(args);
    String fnm = null;
    if (args.length == 1)
      fnm = args[0];
    else if (args.length == 0) 
      fnm = "curMaze.txt";  // default maze file
    else {
      System.out.println( "Usage: java Maze3D <fileName>");
      System.exit(0);
    }
    
    MazeManager mm = new MazeManager(fnm);
    BirdsEye map = new BirdsEye(mm);      // bird's eye view over the maze
   
    WrapMaze3D w3d = new WrapMaze3D(mm, map);

 
    Container c = getContentPane();
    c.setLayout( new BoxLayout(c, BoxLayout.X_AXIS) );
    c.add(w3d);   // main camera pane
    c.add( Box.createRigidArea( new Dimension(4,0)) ); // some space

    Box vertBox = Box.createVerticalBox();
    
    
    vertBox.add( Box.createRigidArea( new Dimension(0,4)) );  // space
    vertBox.add(map);           // bird's eyeview pane
    JButton optionsButton = new JButton("Options");
    JButton exitButton = new JButton("Exit");
    c.add(vertBox);
    vertBox.add(optionsButton);
    vertBox.add(exitButton);
    setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    pack();
    setResizable(true);   
    setVisible(true);
    
    exitButton.addActionListener(new ActionListener(){
    	public void actionPerformed(ActionEvent arg0){
    		int confirmed = JOptionPane.showConfirmDialog(null, 
    		        "Are you sure you want to exit?", "Exit",
    		        JOptionPane.YES_NO_OPTION);

    		    if (confirmed == JOptionPane.YES_OPTION) {
    		      dispose();
    		    }
    		  }

    });
    
  } // end of Maze3D()


// -----------------------------------------

  public static void main(String[] args) throws IOException 
  {  new Maze3D(args); }

} // end of Maze3D class

