import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Stack;

/*
 * Class that demonstrates creating a graphical user interface to work with pictures
 */

public class DrawImageControlPanel extends JFrame{
	
	/*picture for the label*/
	Picture picture = new Picture(300,300);
	
	/*picture label*/
	JLabel pictureLabel = null;
	
	/*stack to hold old pictures for undo*/
	Stack<Picture> pictureStack = new Stack<Picture>();
	
	/*Constructor that sets up the GUI*/
	public DrawImageControlPanel(){		
	
	
	//set title on JFrame
	super("Picture Tool");
	
	//set up the menu
	setUpMenu();
	
	//set the picture in the label
	pictureLabel = new JLabel();
	setPicture(picture);
	this.getContentPane().add(pictureLabel,BorderLayout.CENTER);
	
	//set the frame size and show it
	this.pack();
	this.setVisible(true);	
}

 /*Method to set up the menu*/
private void setUpMenu(){
	//set up the menu bar
	JMenuBar menuBar = new JMenuBar();
	setJMenuBar(menuBar);
	
	//create the file menu
	JMenu fileMenu = new JMenu("Show Picture");
	menuBar.add(fileMenu);
	JMenuItem openItem = new JMenuItem("Open");
	JMenuItem saveItem = new JMenuItem("Save");
	JMenuItem saveAsItem = new JMenuItem("Save as");
	fileMenu.add(openItem);
	fileMenu.add(saveItem);
	fileMenu.add(saveAsItem);
	
	//handle the open
	openItem.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			addPictureToStack();
			String file = FileChooser.pickAFile();
			setPicture(new Picture(file));
		}
	});
	
	//handle the save
	saveItem.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			String file = picture.getFileName();
			picture.write(file);
		}
	});
	
	//handle the save as
	saveAsItem.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			String file = SimpleInput.getString("Enter filename");
			picture.write(FileChooser.getMediaPath(file));
		}
	});
	
	//create the tools menu
	JMenu toolsMenu = new JMenu("Tools");
	menuBar.add(toolsMenu);
	JMenuItem negateItem = new JMenuItem("Negate");
	JMenuItem flipItem = new JMenuItem("Flip");
	JMenuItem undoItem = new JMenuItem("Undo");
	toolsMenu.add(negateItem);
	toolsMenu.add(flipItem);
	toolsMenu.add(undoItem);
	
	//handle negate
	negateItem.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			addPictureToStack();
			picture.negate();
			setPicture(picture);
		}
	});
	
	//handle flip
	flipItem.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			addPictureToStack();
			Picture flippedPict = picture.flip();
			setPicture(flippedPict);
		}
	});
	
	//handle undo
	undoItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(!pictureStack.empty()) {
				Picture pict = pictureStack.pop();
			}
		}
	});
}

   //method to save the current picture on the stack
   private void addPictureToStack(){
	   pictureStack.push(picture.copy());
   }
   
	
   //Method to set the picture to a new picture
   //@param p the new picture to use
   public void setPicture(Picture p){
	   picture=p;
	   pictureLabel.setIcon(new ImageIcon(p.getImage()));
	   this.pack(); //resize for the new picture	
	}
   
   //Main method for testing
   public static void main(String args[]){
	   DrawImageControlPanel pictTool = new DrawImageControlPanel();
	   if(args.length>0) {
		   pictTool.setPicture(new Picture(args[0]));
	    }
     }
  // }
   
   
   //Base of sound that we're creating
   public Sound root;
   //Sound that we're creating to add in
   public Sound newSound;
   //Declare these her so we can reach them inside listeners
   private JTextField filename;
   private JTextField count;
   int num;

   public DrawImageControlPanel(int i){
	   super("Rhythm Tool");
	   FileChooser.setMediaPath("D:/cs1316/mediasources/");
       
	   root = new Sound(new Sound(1)); //nearly empty sound
	   newSound = new Sound(new Sound(1));
	   
	   //layout for the window overall
	   this.getContentPane().setLayout(new BorderLayout());

   
   /* First panel has new sound field */
   JPanel panel1 = new JPanel();
   // Put panel one at the top
   this.getContentPane().add(panel1,BorderLayout.NORTH);
   // Create a space for entering a new sound filename
   filename = new JTextField("soundfilename.wav");
   filename.addActionListener(
     new ActionListener() {
     public void actionPerformed(ActionEvent e) {
       /* When hit return in filename field,
        * create a new sound with that name.
        * Printing is for debugging purposes.
        **/
       newSound = new Sound(
                      new Sound(
                       FileChooser.getMediaPath(filename.getText())));
       System.out.println("New sound from "+
                          FileChooser.getMediaPath(filename.getText()));
     }
   }
   );
   panel1.add(filename);


   /* Put in another panel with number field 
    * and repeat & weave buttons */
   JPanel panel2 = new JPanel();
   // This layout is for the PANEL, not the WINDOW
   panel2.setLayout(new BorderLayout());
   // Add to MIDDLE of WINDOW
   this.getContentPane().add(panel2,BorderLayout.CENTER);
   // Add a field for arguments for Repeat and Weave
   count = new JTextField("10");
   num = 10; // Default value
   count.addActionListener(
     new ActionListener() {
     public void actionPerformed(ActionEvent e) {
       // Here's how we convert a string to a number
       num = Integer.parseInt(count.getText());
     }
   }
   );
   // Add to top of panel
   panel2.add(count,BorderLayout.NORTH);

   // Now do the Repeat button
   JButton button1 = new JButton("Repeat");
   button1.addActionListener(
     new ActionListener() { 
       public void actionPerformed(ActionEvent e) {
         // Repeat the number of times specified
          root.repeatNext(newSound,num);
       }
     }
   );
   // Add to RIGHT of PANEL
   panel2.add(button1,BorderLayout.EAST);


// Now do the Weave button
   JButton button2 = new JButton("Weave");
   button2.addActionListener(
     new ActionListener() { 
       public void actionPerformed(ActionEvent e) {
         // We'll weave 10 copies in
         // every num times
          root.weave(newSound,10,num);
       }
     }
   );
   // Add to LEFT of PANEL
   panel2.add(button2,BorderLayout.WEST);

   /* Put in another panel with the Play button */
   JPanel panel3 = new JPanel();
   // Put in bottom of WINDOW
   this.getContentPane().add(panel3,BorderLayout.SOUTH);
   JButton button3 = new JButton("Play");
   button3.addActionListener(
     new ActionListener() { 
       // If this gets triggered, play the composed sound
       public void actionPerformed(ActionEvent e) {
          root.playFromMeOn();
       }
     }
   );
    panel3.add(button3); // No layout manager here

     this.pack();
   this.setVisible(true);
 }
 
}
