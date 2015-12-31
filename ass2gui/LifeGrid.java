/** CS1801 
 * Assignment 2: Conway's game of life: the GUI version
 * Liz Jenkins, December 2015
 */
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class LifeGrid
{
  // define the grid outside the constructor ;-)
  private int [][] grid;
  
  private static int x, y;  	// dimensions of 2d array
  private static int gen = 0; 	// count the generations
  
  // possible input filenames as Strings:

  private static String ipFile = "blinker.txt"; 	// 5 x 5
//  private static String ipFile = "toad.txt";		// 5 x 5
//  private static String ipFile = "glider.txt";	// 5 x 5
//  private static String ipFile = "glider2.txt";	// 5 x 5
//  private static String ipFile = "block.txt";		// 5 x 5
//  private static String ipFile = "testCode.txt"; 	// 5 x 10

  // we know the grid dimensions
  private static int xdim = 5;
  private static int ydim = 5; // change to 10 for test code
  
  // constructors: 
  // x denotes the no. of rows (or the no. of arrays in our index array), from 1 to x
  // y denotes the no. of cols (or the no. of elements in each 1d array), from 1 to y
  // filename (if provided)is where to find the starting pattern
  LifeGrid(int x, int y)      // declare a grid but don't fill it
  {
    // define the 2d grid
    grid = new int[x][y];
    
    // set dimensions on the object instance
    this.x = x;
    this.y = y;
  }
  // main constructor
  LifeGrid(int x, int y, String filename)throws 
        FileNotFoundException, StringIndexOutOfBoundsException
  {
    // define the 2d grid
    grid = new int[x][y];
    
    // set dimensions on the object instance
    this.x = x;
    this.y = y;
    
    // try opening the input file for reading
    File ipFile = new File(filename);
    try
    {
      // read from filename into grid, show selection
      Scanner s = new Scanner(ipFile);
      System.out.println("");
      System.out.println("Input file selected is "+filename);
    
      for (int i = 0; i < x; i++)
      {
        String str = s.nextLine();
        for (int j = 0; j < y; j++)
        {
          try
          {
//            System.out.println("Value read from cell at row "+i+
//                " and col "+j+" is '"+str.charAt(j)+"'.");
            // test for a live cell
            if (str.charAt(j) == '*')
            {
              grid [i][j] = 1;
            }
            else
            {
              grid [i][j] = 0;
            }
          }
          // input file missing some chars?
          catch(StringIndexOutOfBoundsException stringIOOBE)
          {
            System.err.println(stringIOOBE + ": error reading file "+ipFile);
          }
        } // end for
      } // end for
    } // end try
    // check for errors finding file
    catch (FileNotFoundException fileNFE)
    {
      System.err.println(fileNFE + ": error finding file "+ipFile);
    }
  } // end constructor

  // display the curent grid, with borders 
  public void show()
  {
    System.out.println("");
    System.out.println("Displaying grid at generation "+gen+":");
    // top border
    hBorder();
    // grid
    for (int i = 0; i < x; i++)
    {
      // left border
      System.out.print("|");
      
      for (int j = 0; j < y; j++)
      {
        System.out.print(grid [i][j]);
      }
      // right border and new line
      System.out.print("|");
      System.out.println("");
    }
    // lower border
    hBorder();
  }
  
  // draw the horizontal border to enclose the grid (+2)
  public void hBorder()
  {
    for (int j = 0; j < y + 2; j++)
    {
      System.out.print("=");
    }
    System.out.println("");
  }
  
  // getters
  public int getWidth()
  {
    return x;  
  }
  public int getHeight()
  {
    return y;
  }
  public int getGen()
  {
    return gen;
  }
  public int getCell(int x, int y)
  {
    // note these are not the same x and y!
    return grid [x][y];
  }
  
  // work out no. of neighbours
  public int neighbours(int x, int y)
  {
    int count = 0;
    // Assume Moore's definition of what is a neighbour
    // Assume our grid is surrounded by empty cells, rather than toroidal (for now);
    // (I think generation 10 of the glider shows edge effects?).
    // Assume we can't use what Wikipedia calls the scientific observer's viewpoint (more efficient?)
    for (int i = x - 1; i <= x + 1; i++)
    {
      // don't count off the left or the right
      if (i >= 0 && i < getWidth())
      {
//        System.out.println("i = "+i+", x = "+x+" and array rows = "+this.x);
        for (int j = y - 1; j <= y + 1; j++)
        {
          // don't count off the top or the bottom
          if (j >= 0 && j < getHeight())
          {
//            System.out.println("j = "+j+", y = "+y+" and array cols = "+this.y);
            // don't count cell at (x,y)
            if (i != x || j != y)
            {
              // ok to count this one!
                count += getCell(i, j);
//              System.out.println("Counted! "+count);
            }
          }  	// end top/bottom check
        } 	// end for y
      } 	// end left/right check
    } 		// end for x
    
    return count; 
  } // end method
  
  public static void run()
  {
    // run the Game of Life!
    try
    {
      // create a grid object and fill it from the input file
      LifeGrid aGrid = new LifeGrid(xdim, ydim, ipFile); 
      aGrid.show();  // generation 0
      
      // create an empty grid object for next generation
      LifeGrid bGrid = new LifeGrid(xdim, ydim);
//      bGrid.show();

      // manage swapping A <-> B
      boolean swap = true;
      
      // arbitrary limit to number of generations
      int genCount = 0;
      while (genCount < 10)
      {
        // work out the next generation and put it in the other grid
        for (int i = 0; i < xdim; i++)
        {
          for (int j = 0; j < ydim; j++)
          {
            // test which way we are going
            if (swap)
            {
              bGrid.grid[i][j] = aGrid.nextCell( i, j);
            }
            else
            {
              aGrid.grid[i][j] = bGrid.nextCell( i, j);
            }
          } // end for
        } // end for
        
        // increment the generation and count, and show grid that was updated
        gen += 1;
        genCount += 1;
        System.out.println("");	// new line
        
        if (swap)   bGrid.show();
        if (!swap)  aGrid.show();

        // other way next time
        swap = !swap;
      } // end while
    } // end try

    // check for errors reading file
    catch (FileNotFoundException fileNFE)
    {
      System.err.println(fileNFE + ": error reading file: "+ipFile);
    }
  } // end method
  
  public int nextCell(int x, int y)
  {
    // return the next generation for this cell
    int total = 0;
    // using observer's algorithm after all
    total = getCell(x, y) + neighbours(x, y);
    
    switch(total)
    {
      // either 3 neighbours give birth, or 2 neighbours keep cell alive
      case 3: return 1; 
      // either 3 neighbours keep cell alive, or 4 prevent it being born
      case 4: return getCell(x, y);
      // all other cases cell dies or remains empty
      default: return 0;
    }
  } // end method
  
  public static void main(String[] args)
  {
    // not much happening here...
    run();
 
  } // end main
} // end class