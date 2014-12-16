

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class RadarTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class RadarTest
{
    @Test
    public void positivePositive()
    {
        Radar radar = new Radar(100, 100, 1, 1);
        radar.setMonsterLocation(0, 0);
        for(int i = 0; i < 20; i++)
        {
            radar.scan();
        }
        
        assertEquals(1, radar.getMost()[0]);
        assertEquals(1, radar.getMost()[1]);
    }
    
    @Test
    public void positiveNegative()
    {
        Radar radar = new Radar(100, 100, 1, -1);
        radar.setMonsterLocation(0, 80);
        for(int i = 0; i < 20; i++)
        {
            radar.scan();
        }
        
        assertEquals(1, radar.getMost()[0]);
        assertEquals(-1, radar.getMost()[1]);
    }
    
    @Test
    public void negativePositive()
    {
        Radar radar = new Radar(100, 100, -1, 1);
        radar.setMonsterLocation(80, 0);
        for(int i = 0; i < 20; i++)
        {
            radar.scan();
        }
        
        assertEquals(-1, radar.getMost()[0]);
        assertEquals(1, radar.getMost()[1]);
    }
    
    @Test
    public void negativeNegative()
    {
        Radar radar = new Radar(100, 100, -1, -1);
        radar.setMonsterLocation(80, 80);
        for(int i = 0; i < 20; i++)
        {
            radar.scan();
        }
        
        assertEquals(-1, radar.getMost()[0]);
        assertEquals(-1, radar.getMost()[1]);
    }
}
