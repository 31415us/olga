
package ch.infimum.olga;

import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        ImageGenerator generator = new ImageGenerator(6,"test.png", new AvgWarmthDiffEvaluator());

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

class MinBrightnessDifferenceEvaluator implements Evaluator
{
    public int evaluate(int color, List<Integer> nColors)
    {
        int min = Integer.MAX_VALUE;

        for(int n : nColors)
        {
            int diff = NBitColors.brightness(color) - NBitColors.brightness(n);
            diff = Math.abs(diff);

            if(diff < min)
            {
                min = diff;
            }
        }

        return min;
    }
}

class AvgBrightnessDifferenceEvaluator implements Evaluator
{
    public int evaluate(int color, List<Integer> nColors)
    {
        int acc = 0;

        for(int n : nColors)
        {
            acc += Math.abs(NBitColors.brightness(color) - NBitColors.brightness(n));
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

class MinWarmthDiffEvaluator implements Evaluator
{
    public int evaluate(int color, List<Integer> nColors)
    {
        int min = Integer.MAX_VALUE;

        for(int n : nColors)
        {
            int diff = NBitColors.warmth(color) - NBitColors.warmth(n);
            diff = Math.abs(diff);

            if(diff < min)
            {
                min = diff;
            }
        }

        return min;
    }
}

class AvgWarmthDiffEvaluator implements Evaluator
{
    public int evaluate(int color, List<Integer> nColors)
    {
        int acc = 0;

        for(int n : nColors)
        {
            acc += Math.abs(NBitColors.warmth(color) - NBitColors.warmth(n));
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

