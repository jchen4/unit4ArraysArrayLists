import java.util.ArrayList;
/**
 * The model for radar scan and accumulator
 * 
 * @author @gcschmit
 * @version 19 July 2014
 */
public class Radar
{

    // stores whether each cell triggered detection for the current scan of the radar
    private boolean[][] currentScan;

    // value of each cell is incremented for each scan in which that cell triggers detection 
    private int[][] accumulator;

    // location of the monster
    private int monsterLocationRow;
    private int monsterLocationCol;

    // probability that a cell will trigger a false detection (must be >= 0 and < 1)
    private double noiseFraction;

    // number of scans of the radar since construction
    private int numScans;

    // velocities of monster, must be less than or equal to 5
    private int dx;

    private int dy;

    private ArrayList<VPos> positions;
    
    private boolean [][] prevState;
    /**
     * Constructor for objects of class Radar
     * 
     * @param   rows    the number of rows in the radar grid
     * @param   cols    the number of columns in the radar grid
     */
    public Radar(int rows, int cols, int vx, int vy)
    {
        // initialize instance variables
        currentScan = new boolean[rows][cols]; // elements will be set to false
        prevState = new boolean[rows][cols];
        accumulator = new int[rows][cols]; // elements will be set to 0

        // randomly set the location of the monster (can be explicity set through the
        //  setMonsterLocation method
        monsterLocationRow = 0;
        monsterLocationCol = 0;

        //sets monster velocity
        dx = vx;
        dy = vy;

        positions = new ArrayList<VPos>();
        
        for (int x = -5; x <= 5; x++)
        {
            for (int y = -5; y <= 5; y++)
            {
                positions.add(new VPos(x, y));
            }
        }
        for (VPos vpos : positions)
        {
            vpos.setCoords(0, 0);
        }
        
        noiseFraction = 0.005;
        numScans= 0;
    }

    /**
     * Performs a scan of the radar. Noise is injected into the grid and the accumulator is updated.
     * 
     */
    public void scan()
    {
        // zero the current scan grid
        for(int row = 0; row < currentScan.length; row++)
        {
            for(int col = 0; col < currentScan[0].length; col++)
            {
                currentScan[row][col] = false;
            }
        }

        // detect the monster
        currentScan[monsterLocationRow][monsterLocationCol] = true;
        
        
        // inject noise into the grid
        injectNoise();

        
        //checks if any points in the previous frame are 5 or less away from a point in the current frame then stores its
        //position in a VPos class.
        for(int row = 0; row < currentScan.length; row++)
        {
            for(int col = 0; col < currentScan[0].length; col++)
            {
                if(currentScan[row][col] == true)
                {
                    for(int row1 = 0; row1 < currentScan.length; row1++)
                    {
                        for(int col1 = 0; col1 < currentScan[0].length; col1++)
                        {
                            int xch = row - row1;
                            int ych = col - col1;
                            if(prevState[row1][col1] == true && Math.abs(xch) <= 5 && Math.abs(ych) <= 5)
                            {
                                for (VPos vpo : positions)
                                {
                                    if (vpo.getDX() == xch && vpo.getDY() == ych && (ych !=0 && xch !=0))
                                    {
                                        vpo.checkConsecutive(row, col);
                                        vpo.setCoords(row, col);
                                        vpo.updateNext(row, col);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        //saves the state of the current frame 
        saveState();
        
        //updates monster location as long as it is in the frames bounds
        if(monsterLocationRow < 95 && monsterLocationCol < 95)
        {
            monsterLocationCol = monsterLocationCol + dy;
            monsterLocationRow = monsterLocationRow + dx;
        }
        
        
        // keep track of the total number of scans
        numScans++;
    }

    /**
     * returns the x and y velocities in an array
     */
    public int[] getMost()
    {
        int sentinel = 0;
        VPos most = new VPos(0,0);
        int[] xy = new int[2];
        
        for (VPos vpo : positions)
        {
            if (vpo.getConsecutive() > sentinel)
            {
                most = vpo;
                sentinel = vpo.getConsecutive();
            }
        }
        xy[0] = most.getDX();
        xy[1] = most.getDY();
        
        return xy;
    }
    
    /**
     * Saves the previous frame
     * 
     */
    public void saveState()
    {
        for(int row = 0; row < currentScan.length; row++)
        {
            for(int col = 0; col < currentScan[0].length; col++)
            {
                prevState[row][col] = currentScan[row][col];
            }
        }
    }

    /**
     * Sets the location of the monster
     * 
     * @param   row     the row in which the monster is located
     * @param   col     the column in which the monster is located
     * @pre row and col must be within the bounds of the radar grid
     */
    public void setMonsterLocation(int row, int col)
    {
        // remember the row and col of the monster's location
        monsterLocationRow = row;
        monsterLocationCol = col;

        // update the radar grid to show that something was detected at the specified location
        currentScan[row][col] = true;
    }

    /**
     * Sets the probability that a given cell will generate a false detection
     * 
     * @param   fraction    the probability that a given cell will generate a flase detection expressed
     *                      as a fraction (must be >= 0 and < 1)
     */
    public void setNoiseFraction(double fraction)
    {
        noiseFraction = fraction;
    }

    /**
     * Returns true if the specified location in the radar grid triggered a detection.
     * 
     * @param   row     the row of the location to query for detection
     * @param   col     the column of the location to query for detection
     * @return true if the specified location in the radar grid triggered a detection
     */
    public boolean isDetected(int row, int col)
    {
        return currentScan[row][col];
    }

    /**
     * Returns the number of times that the specified location in the radar grid has triggered a
     *  detection since the constructor of the radar object.
     * 
     * @param   row     the row of the location to query for accumulated detections
     * @param   col     the column of the location to query for accumulated detections
     * @return the number of times that the specified location in the radar grid has
     *          triggered a detection since the constructor of the radar object
     */
    public int getAccumulatedDetection(int row, int col)
    {
        return accumulator[row][col];
    }

    /**
     * Returns the number of rows in the radar grid
     * 
     * @return the number of rows in the radar grid
     */
    public int getNumRows()
    {
        return currentScan.length;
    }

    /**
     * Returns the number of columns in the radar grid
     * 
     * @return the number of columns in the radar grid
     */
    public int getNumCols()
    {
        return currentScan[0].length;
    }

    /**
     * Returns the number of scans that have been performed since the radar object was constructed
     * 
     * @return the number of scans that have been performed since the radar object was constructed
     */
    public int getNumScans()
    {
        return numScans;
    }

    /**
     * Sets cells as falsely triggering detection based on the specified probability
     * 
     */
    private void injectNoise()
    {
        for(int row = 0; row < currentScan.length; row++)
        {
            for(int col = 0; col < currentScan[0].length; col++)
            {
                // each cell has the specified probablily of being a false positive
                if(Math.random() < noiseFraction)
                {
                    currentScan[row][col] = true;
                }
            }
        }
    }

}
