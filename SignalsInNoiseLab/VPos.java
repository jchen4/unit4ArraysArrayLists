import java.util.ArrayList;
/**
 * Write a description of class VPos here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class VPos
{
    private int dx;
    private int dy;
    private int count;
    
    private int xCoord;
    
    private int yCoord;
    
    private int consec;
    
    private ArrayList<Integer> nextx;
    private ArrayList<Integer> nexty;
    /**
     * Constructor for objects of class VPos
     */
    public VPos(int x, int y)
    {
        dx = x;
        dy = y;
        
        nextx = new ArrayList<Integer>();
        nexty = new ArrayList<Integer>();
        
        count = 0;
    }
    
    public void updateNext(int x1, int y1)
    {
        nextx.add(x1 + dx);
        nexty.add(y1 + dy);
    }
    
    public int getCount()
    {
        return count;
    }

    public int getDX()
    {
        return dx;
    }
    
    public int getDY()
    {
        return dy;
    }
    
    public void incrementCount()
    {
        count = count + 1;
    }
    
    public void setCoords(int nx, int ny)
    {
        xCoord = nx;
        yCoord = ny;
    }
    
    public int getConsecutive()
    {
        return consec;
    }
    
    public void checkConsecutive(int newx, int newy)
    {
        for (int item = 0; item < nextx.size(); item++)
        {
            if(newx  - nextx.get(item) == dx && newy - nexty.get(item) == dy)
            {
                consec++;
            }
        }
        this.updateNext(newx, newy);
    }
}
