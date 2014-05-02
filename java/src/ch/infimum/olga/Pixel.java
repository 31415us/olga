
package ch.infimum.olga;

import java.util.List;
import java.util.ArrayList;

public class Pixel
{

    private int x;
    private int y;

    public Pixel(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int x()
    {
        return x;
    }

    public int y()
    {
        return y;
    }

    public List<Pixel> neighbours(int width, int height)
    {
        List<Pixel> result = new ArrayList<Pixel>(8);
        for(int i = -1; i <= 1; i++)
        {
            for(int j = -1; j <= 1; j++)
            {
                if(i != 0 || j != 0)
                {
                    int newX = x + i;
                    int newY = y + j;

                    if(newX >= 0 && newX < width && newY >= 0 && newY < height)
                    {
                        result.add(new Pixel(newX,newY));
                    }
                }
            }
        }

        return result;
    }

    @Override
    public boolean equals(Object o)
    {
        if(o == null)
        {
            return false;
        }

        if(o == this)
        {
            return true;
        }

        if(!(o instanceof Pixel))
        {
            return false;
        }

        Pixel other = (Pixel) o;

        return (this.x == other.x) && (this.y == other.y);
    }

    @Override
    public int hashCode()
    {
        return x ^ y;
    }

}

