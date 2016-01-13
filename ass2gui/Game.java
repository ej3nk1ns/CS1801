/** CS1801 **
 *********************************************************************
 * Assignment 2: Conway's game of life: the GUI version
 * With material taken from Click.java in 'Java, concisely', and 
 * http://blog.netopyr.com/2012/06/14/using-the-javafx-animationtimer/
 *
 * Liz Jenkins, December 2015 - January 2016
 */

package game;

// The entry point for JavaFX applications is the Application class.
// https://docs.oracle.com/javase/8/javafx/api/javafx/application/Application.html
import javafx.application.Application;
// The class AnimationTimer allows to create a timer, that is called in each frame. 
// https://docs.oracle.com/javase/8/javafx/api/javafx/animation/AnimationTimer.html
import javafx.animation.AnimationTimer;
// The JavaFX Stage class is the top level JavaFX container. 
// https://docs.oracle.com/javase/8/javafx/api/javafx/stage/Stage.html
import javafx.stage.Stage;
// The JavaFX Scene class is the container for all content in a scene graph.
// https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Scene.html
import javafx.scene.Scene;
// VBox lays out its children in a single vertical column. 
// https://docs.oracle.com/javase/8/javafx/api/javafx/scene/layout/VBox.html
import javafx.scene.layout.VBox;
// Canvas is an image that can be drawn on using a set of graphics commands 
// provided by a GraphicsContext. 
// https://docs.oracle.com/javase/8/javafx/api/javafx/scene/canvas/Canvas.html
import javafx.scene.canvas.Canvas;
// This class is used to issue draw calls to a Canvas using a buffer. 
// https://docs.oracle.com/javase/8/javafx/api/javafx/scene/canvas/GraphicsContext.html
import javafx.scene.canvas.GraphicsContext;
// Base class for a color or gradients used to fill shapes and backgrounds when 
// rendering the scene graph.
// https://docs.oracle.com/javase/8/javafx/api/javafx/scene/paint/Paint.html
import javafx.scene.paint.Color;

// random number generation
import java.util.Random;
// A simple text scanner which can parse primitive types & strings using reg exps 
// https://docs.oracle.com/javase/7/docs/api/java/util/Scanner.html
import java.util.Scanner;
// An abstract representation of file and directory pathnames. 
// https://docs.oracle.com/javase/7/docs/api/java/io/File.html
import java.io.File;
import java.io.FileNotFoundException;

// a javafx class must extend javafx.application.Application (A)
public class Game extends Application 
{
    // define the grid dimensions 
    int x = 5, y = 5;           // <<<<<<<<<<<<<<< replace hardcoded values
    // define grid objects to hold the data of the game
    GridData aGrid;
    GridData bGrid;
    // manage swapping grids A <-> B
    boolean swap = true;
    // define the size of a cell in pixels
    int squareSize;
    // define a timer to manage the display rate
    long lastTime;  
 
    // define a graphics context to make draw calls to the canvas on
    GraphicsContext gc;
    // define an array of colours to make it look pretty
    Color colours[] = {Color.AQUAMARINE, Color.CADETBLUE, Color.STEELBLUE,
            Color.CORNFLOWERBLUE, Color.BLUEVIOLET, Color.DODGERBLUE, 
            Color.DARKCYAN, Color.DARKBLUE, Color.HOTPINK};
    
    // main method (A1)
    public static void main(String[] args) 
    {
        // Launch the immediately enclosing class as a standalone application
        // (An application may get these parameters using the getParameters() method.)
        launch(args);
    }
 
     // flag the start method as overridden
    @Override
    // the start method sets up the scene graph (a tree of nodes representing the
    // relationship of data objects, or nodes, to each other). (A2)
    public void start(final Stage primaryStage) 
    {
        // initial grid data set-up
        initialise();
        // next follows the JavaFX component set-up:
        
        // define the root node of the scene graph as a VBox object
        VBox root = new VBox(8);
        // define a canvas object with the specified dimensions
        Canvas canvas = new Canvas(x * squareSize, y * squareSize);
        // define the graphics context for the canvas
        gc = canvas.getGraphicsContext2D();
        
        // set the stage's title property
        primaryStage.setTitle("Conway's Game of Life (CS1801)");
        // add the canvas object as a node below root in the scene graph
        root.getChildren().addAll(canvas);
        // instantiate a scene with our root node on this stage 
        primaryStage.setScene(new Scene(root));
        
        // attempt to show the window by setting visibility to true
        primaryStage.show();
        
        // moved temporarily
        GridData aGrid = new GridData(x, y);  // <<<<<<<<<<<<< for now!
            
         // call fillGrid() if a randomly-generated grid is required
         aGrid.fillGrid();

         // create an empty grid object for next generation
         GridData bGrid = new GridData(x, y);
        // show the first grid (generation 0)
        display(aGrid);
        
         // this bit gets done 30 - 40 times per second? (C)
        new AnimationTimer() 
        {
            // flag handle as an overridden method
            @Override
            // overriding this method lets us programmatically rewrite the screen (C1)
            public void handle(long now) 
            {
                // the argument 'now' is system time in nano seconds - ie. uptime?
                long secs = now / 1000000000;
                // check if it is at least a second since last refreshed display
                int diff = Long.compare(secs, lastTime);
                // avoid running the simulation too fast by refreshing approx once per sec
                if (diff == 1)
                {
 //                   System.out.println("Different! Seconds: " + secs + ", last time " + lastTime);

                    // reset the check for time lapsed  
                    lastTime = secs;
                                  
                    // calculate and display the next generation
                    // >>>>>>>>>>> logic problem here, set random values for now
                    if (swap)
                    {
                        // calculate from the 'a' grid, and store values in 'b' 
           //             nextGen(x, y, aGrid, bGrid);  
                       bGrid.fillGrid();
                    }
                    else
                    {
                        // calculate from the 'b' grid and store values in 'a'
             //           nextGen(x, y, bGrid, aGrid);
                       aGrid.fillGrid();
                    }
                    // show the new grid        
                    if (swap)   display(bGrid); 
                    if (!swap)  display(aGrid);         
          
                    // other way next time
                    swap = !swap;
                }
 
            }  // end handle
            // what is this bit?
        }.start(); // end AnimationTimer
    } // end start
    
    // instantiate the starting grid, either from file or randomly (A3)
    public void initialise()
    {
         // set size of square in pixels
         squareSize = 20;
         
         // create a grid object and fill it from the input file
//        GridData aGrid = new GridData(x, y, filename); 
 //        GridData aGrid = new GridData(x, y);  // <<<<<<<<<<<<< for now!
            
         // call fillGrid() if a randomly-generated grid is required
   //      aGrid.fillGrid();

         // create an empty grid object for next generation
     //    GridData bGrid = new GridData(x, y);

         // retrieve any parameters that were passed in - how to test for type?
         
         // call readGrid() if an input file needs to be read
//         gridData.readGrid()  <<<<<<<<<<<<<<< for now!
         // set dimensions x and y
    } // end initialise

            // map the grid values onto the coloured squares in the graphics context 
        public void display(GridData gridDisp)
        {
  //          System.out.println("display");
 //           System.out.println("x dimension = "+gridDisp.getX()+", y dim = "+gridDisp.getY());    

            for (int y = 0; y < gridDisp.getY(); y++)
            {
                for (int x = 0; x < gridDisp.getX(); x++)
                {
 //           System.out.println("x = "+x+", "+gridDisp.getX()+", y = "+y+", "+gridDisp.getY());    
                    // clear an empty square and colour a filled square 
                    if (gridDisp.getCell(x, y) == 0)
                    {
                        // set the paint fill attribute to white
 //           System.out.println("white");
                        gc.setFill(Color.WHITE);
 //           System.out.println("done");

                    }
                    else
                    {
 //           System.out.println("colour");
                        // set the paint fill attribute to a grid cell colour based on each fifth
                        // generation, modulus the length of the colour array (so colours cycle)
 //           System.out.println("setFill "+gridDisp.getGen()+" "+colours.length+" "+
  //                      colours[(gridDisp.getGen() / 5) % colours.length]);
                        gc.setFill(colours[(gridDisp.getGen() / 5) % colours.length]);
              
 //           System.out.println("colour set");
                    }
                  
                    // fill a square of this size at this location x, y with colour or white
 //           System.out.println("fillRect "+x+" "+y+" "+squareSize);
                    gc.fillRect(x * squareSize, y * squareSize, squareSize, squareSize);
                   
                } // end for
            } // end for
           
        } // end display
    
    // End of one class (A); start of the other (B)
    
    // this is our 2d integer array object for mapping the screen (B)
    class GridData 
    {
        // declare a grid variable that will point at a 2D integer array
        int[][] grid;
        int gen = 0; 	// count the generations
      
        // constructor method takes dimensions as parameters <<<<<<< replace with 2x LifeGrid
        // constructors, B1, B2
 
        // There are 2 constructors with the following parameters: 
        // x denotes the no. of rows (or the no. of arrays in our index array), from 1 to x
        // y denotes the no. of cols (or the no. of elements in each 1d array), from 1 to y
        // filename (if provided)is where to find the starting pattern
  
        // sub-constructor - declare a grid but don't fill it
        GridData(int x, int y)
        {
            // define the 2d grid
              grid = new int[x][y];

        }
     
        // return the length of the first element of the 2D array (the width, x, L -> R)(B3)
        public int getX() 
        {
            return grid[0].length;
        }
        // return the length of the index array (the height, y, top to bottom)(B4)
        public int getY() 
        {
            return grid.length;
        }
        //  (B5)  <<<<<<<<<<<< check usage of gen is replaced with getGen()!
        public int getGen()
        {
            return gen; 
        }
        // return a cell value (zero is empty, one is filled) (B6)
        public int getCell(int x, int y) 
        {
            return grid[x][y];
        }
        // set a cell value to the value passed in (B7)
        public void setCell(int x, int y, int v) 
        {
            grid[x][y] = v;
        }

        // calculate the next generation for all cells (B8)
        public void nextGen(int xdim, int ydim, GridData grid1, GridData grid2)
        {
            for (int i = 0; i < xdim; i++)
            {
                for (int j = 0; j < ydim; j++)
                {
                    // test which way we are going
                    if (swap)
                    {
                         grid2.grid[i][j] = grid1.nextCell( i, j, xdim, ydim);
                    }
                    else
                    {
                         grid1.grid[i][j] = grid2.nextCell( i, j, xdim, ydim);
                    }
                } // end for
            } // end for
        
            // increment the generation 
            gen += 1;

        }
        
        // calculate the next generation for a particular cell (B9)
        public int nextCell(int x, int y, int xdim, int ydim)
        {
            // parameters (x,y) are the cell position; (xdim,ydim) are the grid dimensions
            int total = 0;
            // using what Wikipedia calls the scientific observer's viewpoint algorithm 
            total = getCell(x, y) + neighbours(x, y, xdim, ydim);
    
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
            
        // work out no. of neighbours for a cell (B10)
        public int neighbours(int x, int y, int xdim, int ydim)
        {
            // parameters (x,y) are the cell position; (xdim,ydim) are the grid dimensions

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
                } 
                else 
                {
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
                    } 
                    else 
                    {
                        // j is positive, the modulus alone is what we want
                        ymod = j % ybase;
                    }
     
                    // don't count cell at (x,y)
                    if (xmod != x || ymod != y)
                    {
                        // ok to count this one!
                        count += getCell(xmod, ymod);
                    }
                } // end for y
            } // end for x

            return count; 
        } // end method            
            
        // put some random initial values in the grid (B11)
        public void fillGrid()
        {
             for (int y = 0; y < getY(); y++)
             {
                 for (int x = 0; x < getX(); x++)
                 {
                     // set cell value to either '0' or '1' 
                     int value = (int)(Math.random() * 2);
                     setCell(x, y, value);
 //                    System.out.println("fill grid "+x+" "+y+" "+value);
       
                 } // end for
             } // end for
        } // end fillGrid
 
        // read the initial grid data in from a text file (B12)
        public void readGrid()
        {
    
        } // end readGrid
           
    
    } // end class GridData

} // end class Game
