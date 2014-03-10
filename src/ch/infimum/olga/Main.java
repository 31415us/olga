
package ch.infimum.olga;

import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        ImageGenerator generator = new ImageGenerator(5,"test.png", new AvgDistanceEvaluator());

        generator.generate();
    }
}

class MinDistanceEvaluator implements Evaluator
{
    public int evaluate(int color, List<Integer> nColors)
    {
        int min = Integer.MAX_VALUE;
        for(int neigh : nColors)
        {
            
            int dist = NBitColors.euclideanDistance(color,neigh);

            if(dist < min)
            {
                min = dist;
            }
        }

        return min;
    }
}

class AvgDistanceEvaluator implements Evaluator
{
    public int evaluate(int color, List<Integer> nColors)
    {
        int acc = 0;

        for(int n : nColors)
        {
            acc += NBitColors.euclideanDistance(color,n);
        }

        int size = nColors.size();
        if(size == 0)
        {
            return 0;
        }
        else
        {
            return acc / size;
        }
    }
}

