/**
 * The wrapper class for dealing with images in JES.
 * It creates pictures, draws them on a JPanel, and 
 * provides a way to gain pixel information.
 * Created for the Jython Environment for Students (JES)
 * @author Keith McDermottt, gte047w@cc.gatech.edu
 */

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;

public class DrawImageControlPanel extends JPanel implements Cloneable 
{
	public Image image;
	public Image doubleBufferedImage;
	public String fileName;
	public int[] imageRGB;
	private JFrame frame = new JFrame(); 



	//Stuff Adam Added:
	public Object clone()
	{
		DrawImageControlPanel newPic = new DrawImageControlPanel();
		newPic.imageRGB = imageRGB;
		newPic.fileName = fileName;
		newPic.image = image;
		newPic.reLoadImage();
		return newPic;
	}


	/**
     * Displays the picture in a JFrame
     */
	public void setUpNotify()
	{
		frame.setTitle(fileName);
		frame.setSize(this.getWidth(), (this.getHeight()));
		int minWidth = 100 + 6 * fileName.length();
	
		if (this.getWidth() < minWidth)
		{
	    	frame.setSize(minWidth, (this.getHeight()+30));
	    }
		frame.getContentPane().add(this);
		frame.setVisible(false);
		frame.addNotify();
		this.repaintImage();
	}

	//END Stuff Adam Added	
	

	
	/**
     * Public constructor
     */
	public DrawImageControlPanel() 
	{
	}
	
	/**
     * Public constructor for use by PictureViewer only
     *
     *@param Image image
     */
	public DrawImageControlPanel(Image image)
	{
		this.image = image;
		//Grab the pixels
		int width = image.getWidth(null);
   		int height = image.getHeight(null);
		
   		imageRGB = new int[ width * height ];

   		PixelGrabber pg = new PixelGrabber( image, 0, 0, width, height, imageRGB, 0, width );
   		try
   		{
     		pg.grabPixels();
   		}
   		catch (InterruptedException e)
   		{
   		}
	}
	
	/**
     * Overwrites JPanels paint function
     *@param Graphics graphics
     */
	public void paint(Graphics graphics) 
	{
		//makes sure current version is being used
		//this.reLoadImage();
		super.paint(graphics);
		graphics.setColor(Color.white);
		try
		{
			graphics.drawImage(image, 0, 0, null);
		}
		catch(NullPointerException e)
        {
            			
            //Creates an OK button
			Object[] options = { "OK" };
			//Creates the string to print
			String information="There is no picture to display";
			                  
			
			//Creates a JOptionPane with the instructions and Icon
            JOptionPane.showOptionDialog(null, information, "Picture Error", 
            			JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
            			null, options, options[0]);
            			
        }
		
	}
	
	/**
     * Overwrites JPanels method
     */
	public Dimension getPreferredSize()
	{
		return (new Dimension(image.getWidth(null), image.getHeight(null)));
	}

	/**
     * Loads an image on to the panel from a file or url
     * returns 1 for success and 0 for failure.  If failure 
     * object should be destoryed for by calling class as to
     * avoid problems
     *@param String filename
     *@return int
     */
	public int loadImage(String fileName)
	{
		this.fileName=fileName;
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		image = toolkit.createImage(fileName);
		MediaTracker mediaTracker = new MediaTracker(this);
		mediaTracker.addImage(image, 0);
		try
		{
			mediaTracker.waitForID(0);
		}
		catch (Exception e)
		{
			//failed to load?
		}
		
		if(image.getHeight(null)==-1)
		{	
			//image did not load
			return(0);
		}
		
		//Grab the pixels
		int width = image.getWidth(null);
   		int height = image.getHeight(null);
		
   		imageRGB = new int[ width * height ];

   		PixelGrabber pg = new PixelGrabber( image, 0, 0, width, height, imageRGB, 0, width );
   		try
   		{
     		pg.grabPixels();
   		}
   		catch (InterruptedException e)
   		{
   		}
		setUpNotify();
   		//successful load operation
   		return(1);
	}
	
	/**
     * Loads the image from an int array
     *
     */
	public void reLoadImage()
	{
		//loads the image from an int array
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		
		MemoryImageSource source = new MemoryImageSource(width, height,
                                        ColorModel.getRGBdefault(),imageRGB, 0, width);                               

		Image newImage = Toolkit.getDefaultToolkit().createImage(source);
		this.setImage(newImage);
	}
	
	/**
     * Reloads the image then repaints the panel
     */
	public void repaintImage()
	{
		this.reLoadImage();
		this.repaint();
	}
	/**
     * Height accessor
     *@return int height
     */
	public int getHeight()
	{
		return image.getHeight(null);
	}
	
	/**
     * Width accessor
     *@return int width
     */
	public int getWidth()
	{
		return image.getWidth(null);
	}
	
	/**
     * Pixel accessor
     *@param int x
     *@param int y
     *@return JavaPixel
     */
	public Pixel getPixel(int x, int y)
	{
		return new Pixel(this, x, y);
	}
	
	/**
     * Sets the image, mainly used from reLoadImage()
     *@param Image image
     */
	public void setImage(Image image)
	{
		this.image=image;
	}
	
	/**
     * Returns the file name
     *@return String fileName
     */
	public String getFileName()
	{
		return(fileName);
	}
	
	/**
     * Returns the image
     *@return Image image
     */
	public Image getImage()
	{
		return image;
	}
	
	/**
     * Returns the int array used to represent the image
     *@return int[] imageRGB
     */
	public int[] getImageRGB()
	{
		return imageRGB;
	}
	
	/**
     * Returns the image for the PictureViewer.  
     * It ensures an updated image is set when zoom is used
     *@return Image image
     */
	public Image getZoomImage()
	{
		this.reLoadImage();
		return (image);
	}
	
	/**
     * Creates a blank canvas as an image
     *@param int x
     *@param int y
     */
	public void createNewImage(int width, int height)
	{
		int type = BufferedImage.TYPE_INT_RGB;
   
  		BufferedImage bufferedImage = new BufferedImage(
      			width, height, type);
      			
      	this.image = Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource());
      	
      			
      	imageRGB = new int[ width * height ];

   		PixelGrabber pg = new PixelGrabber( image, 0, 0, width, height, imageRGB, 0, width );
   		try
   		{
     		pg.grabPixels();
   		}
   		catch (InterruptedException e)
   		{
   		}
   		
   		this.fileName="Blank Canvas";
		setUpNotify();
	}
	
	/**
     * Saves the image
     *@param String newFileName
     */
	public void saveImage(String newFileName) throws java.io.IOException
    {
    	this.reLoadImage();
		ImageIO.write(this.createBufferedImage(), "jpg", new File(newFileName));
		fileName=newFileName;
		frame.setTitle(newFileName);
    }
	
	/**
     * Displays the picture in a JFrame
     */
	public void showPictureWithTitle(String title)
	{
		if(frame!=null)
		{
			if (frame.isVisible())
			{
				this.repaintImage();
				frame.repaint();
			}
			else
			{
				frame = new ShowImage(this, title);
				
			}
		}
		else
		{
			frame = new ImageDisplay(this, title);
		}
	}
	/**
     * Quick little tool to convert images to bufferedImages
     */
	private BufferedImage createBufferedImage()
	{           
  
  		int type = BufferedImage.TYPE_INT_RGB;
   
  		BufferedImage bufferedImage = new BufferedImage(
      			image.getWidth(null), image.getHeight(null), type);
      			
  		Graphics2D graphics2d = bufferedImage.createGraphics();
  		graphics2d.drawImage(image, null, null); 
  
  		return bufferedImage; 
	}
	
	/**
     * Creates a buffer so graphics can be added to the image
     *@return Image doubleBufferedImage
     */
	public Image getDoubleBufferedImage()
	{
		this.repaint();
		doubleBufferedImage = this.createImage(this.getWidth(), this.getHeight());
		this.paint(doubleBufferedImage.getGraphics());
		return doubleBufferedImage;
	}
	
	/**
     * Takes the buffered image and sets the changes into the actual image
     */
	public void setBufferedChanges()
	{
		image = doubleBufferedImage;
		
		//reGrab the pixels
		int width = image.getWidth(null);
   		int height = image.getHeight(null);
		
   		imageRGB = new int[ width * height ];

   		PixelGrabber pg = new PixelGrabber( image, 0, 0, width, height, imageRGB, 0, width );
   		try
   		{
     		pg.grabPixels();
   		}
   		catch (InterruptedException e)
   		{
   		}
   		
   		this.repaintImage();
   	}
		
	/**
     * Test Main
     *@param String[] argvs
     */
	public static void main(String[] args) 
	{
		
		DrawImageControlPanel p = new DrawImageControlPanel();
		//p.createNewImage(100,100);
		//p.showPicture();
		
		p.createNewImage(250,250);
		//p.showPictureWithTitle("Testing");
		//p.loadImage("c:\\School Work\\Fall_Research\\testBig.jpg");
		//p.showPictureWithTitle("Testing");

		
		for(int x=0;x<p.getWidth();x++)
		{
			for(int y=0;y<p.getHeight();y++)
			{
				p.getPixel(x,y).setRed(255);
			}
		}
		p.showPictureWithTitle("testing");
		System.out.println(p.getPixel(1,1).getRed());
		
		ImageDisplay test = new ImageDisplay(p);
		for(int x=0;x<p.getWidth()/2;x++)
		{
			for(int y=0;y<p.getHeight()/2;y++)
			{
				p.getPixel(x,y).setBlue(200);
			}
		}
		p.showPictureWithTitle("testing");
		
		//p.getDoubleBufferedImage().getGraphics();
		
	}
}

