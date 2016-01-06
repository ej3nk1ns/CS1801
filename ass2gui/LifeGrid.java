/** CS1801 
 * Assignment 2: Conway's game of life: the GUI version
 * Liz Jenkins, December 2015 - January 2016
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
  
  // possible input filenames as Strings (comment out those not required):

//  private static String ipFile = "blinker.txt"; 	// 5 x 5
//  private static String ipFile = "toad.txt";		// 5 x 5
  private static String ipFile = "glider.txt";	// 5 x 5
//  private static String ipFile = "glider2.txt";	// 5 x 5
//  private static String ipFile = "block.txt";		// 5 x 5
//  private static String ipFile = "testCode.txt"; 	// 5 x 10

  // we know the grid dimensions (make this a parameter?)
  private static int xdim = 5;
  private static int ydim = 5; // change to 10 for test code
  
  // There are 2 constructors with the following parameters: 
  // x denotes the no. of rows (or the no. of arrays in our index array), from 1 to x
  // y denotes the no. of cols (or the no. of elements in each 1d array), from 1 to y
  // filename (if provided)is where to find the starting pattern
  
  // sub-constructor - declare a grid but don't fill it
  LifeGrid(int x, int y)
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
    // define the 2d grid (repetitive, but recursion not allowed in constructors)
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
    // Assume our grid is a torus (right joins to left, top joins to bottom)
    
    // define variables for modulus of cell position
    int xbase = xdim;
    int ybase = ydim;
    int xmod = 0;
    int ymod = 0;

    // loop over rows around cell
    for (int i = x - 1; i <= x + 1; i++)
    {
      // rewrite the loop variable using modulus to keep it in the grid
      if (i < 0)
      {
        // i is negative, add the modulus to the base
        xmod = xbase + i % xbase;
      } else {
        // i is positive, the modulus alone is what we want
        xmod = i % xbase;
      }
      
      // loop over columns within the row
      for (int j = y - 1; j <= y + 1; j++)
      {
        // rewrite the loop variable using modulus to keep it in the grid
        if (j < 0)
        {
          // j is negative, add the modulus to the base
          ymod = ybase + j % ybase;
        } else {
          // j is positive, the modulus alone is what we want
          ymod = j % ybase;
        }
     
        // don't count cell at (x,y)
        if (xmod != x || ymod != y)
        {
          // ok to count this one!
            count += getCell(xmod, ymod);
        }
      } 	// end for y
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

      // manage swapping A <-> B
      boolean swap = true;
      
      // arbitrary limit to number of generations >>>>>>> parameterise or wait for steady state?!
      int genCount = 0;
      while (genCount < 25)
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
    // using what Wikipedia calls the scientific observer's viewpoint algorithm 
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