
package ch.infimum.olga;

import java.util.List;
import java.util.Random;

public class Main
{
    public static void main(String[] args)
    {

        if(args.length == 0)
        {
            System.out.println("please provide some string as argument.");
            return;
        }

        String name = args[0];

        StringBuilder pngName = new StringBuilder(name);
        pngName.append(".png");

        Random rng = new Random(name.hashCode());

        Evaluator[] evaluators = {new MinDistanceEvaluator(), new MinBrightnessDifferenceEvaluator(),
                                  new MinWarmthDiffEvaluator(), new MinHammingDistEvaluator(), new ChebyshevDistEvaluator(),
                                  new TaxiCabEvaluator(), new MinDistanceEvaluator(), new DamerauDistEvaluator(),
                                  new JaccardDistEvaluator(), new ChromaDifEvaluator(), new LuminanceDifEvaluator(),
                                  new SaturationEvaluator(), new LuminanceDifEvaluator(), new HellingerDistEvaluator(),
                                  new KullbackDistEvaluator(), new HueEvaluator()};

        Evaluator eval = evaluators[rng.nextInt(evaluators.length)];
        boolean strictFrame = rng.nextBoolean();

        ImageGenerator generator = new ImageGenerator(6,pngName.toString(),rng,eval,strictFrame);

        // generators gonna generate .-P
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

class MinHammingDistEvaluator implements Evaluator
{
    public int evaluate(int color, List<Integer> nColors)
    {
        int min = Integer.MAX_VALUE;

        for(int n : nColors)
        {

            int dist = NBitColors.hammingDist(color,n);

            if(dist < min)
            {
                min = dist;
            }
        }

        return min;
    }
}

class AvgHammingDistEvaluator implements Evaluator
{
    public int evaluate(int color, List<Integer> nColors)
    {
        int acc = 0;

        for(int n : nColors)
        {
            acc += NBitColors.hammingDist(color,n);
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

class ChebyshevDistEvaluator implements Evaluator
{

    public int evaluate(int color, List<Integer> nColors) {

        int min = Integer.MAX_VALUE;
        for(int neigh : nColors) {
            int dist = NBitColors.chebyshevDist(color,neigh);
            if(dist < min) min = dist;
        }

        return min;
    }

}

class TaxiCabEvaluator implements Evaluator
{

    public int evaluate(int color, List<Integer> nColors) {

        int min = Integer.MAX_VALUE;
        for(int neigh : nColors) {
            int dist = NBitColors.taxiCab(color,neigh);
            if(dist < min) min = dist;
        }

        return min;
    }

}

class MinkowskiDistEvaluator implements Evaluator
{

    public int evaluate(int color, List<Integer> nColors) {

        int min = Integer.MAX_VALUE;
        for(int neigh : nColors) {
            int dist = NBitColors.minkowskiDist(color,neigh);
            if(dist < min) min = dist;
        }

        return min;
    }

}

class DamerauDistEvaluator implements Evaluator
{

    public int evaluate(int color, List<Integer> nColors) {

        int min = Integer.MAX_VALUE;
        for(int neigh : nColors) {
            int dist = NBitColors.damerauDist(color,neigh);
            if(dist < min) min = dist;
        }

        return min;
    }

}

class JaccardDistEvaluator implements Evaluator
{

    public int evaluate(int color, List<Integer> nColors) {

        int min = Integer.MAX_VALUE;
        for(int neigh : nColors) {
            int dist = NBitColors.jaccardDist(color,neigh);
            if(dist < min) min = dist;
        }

        return min;
    }

}

class ChromaDifEvaluator implements Evaluator
{
    public int evaluate(int color, List<Integer> nColors)
    {
        int min = Integer.MAX_VALUE;

        for(int n : nColors)
        {
            int diff = NBitColors.chroma(color) - NBitColors.chroma(n);
            diff = Math.abs(diff);

            if(diff < min)
            {
                min = diff;
            }
        }

        return min;
    }
}

class LuminanceDifEvaluator implements Evaluator
{
    public int evaluate(int color, List<Integer> nColors)
    {
        int min = Integer.MAX_VALUE;

        for(int n : nColors)
        {
            int diff = NBitColors.luminance(color) - NBitColors.luminance(n);
            diff = Math.abs(diff);

            if(diff < min)
            {
                min = diff;
            }
        }

        return min;
    }
}

class SaturationEvaluator implements Evaluator
{
    public int evaluate(int color, List<Integer> nColors)
    {
        int min = Integer.MAX_VALUE;

        for(int n : nColors)
        {
            int diff = NBitColors.saturation(color) - NBitColors.saturation(n);
            diff = Math.abs(diff);

            if(diff < min)
            {
                min = diff;
            }
        }

        return min;
    }
}

class LumaEvaluator implements Evaluator
{
    public int evaluate(int color, List<Integer> nColors)
    {
        int min = Integer.MAX_VALUE;

        for(int n : nColors)
        {
            int diff = NBitColors.luma(color) - NBitColors.luma(n);
            diff = Math.abs(diff);

            if(diff < min)
            {
                min = diff;
            }
        }

        return min;
    }
}

class HellingerDistEvaluator implements Evaluator
{

    public int evaluate(int color, List<Integer> nColors) {

        int min = Integer.MAX_VALUE;
        for(int neigh : nColors) {
            int dist = NBitColors.hellingerDist(color,neigh);
            if(dist < min) min = dist;
        }

        return min;
    }

}

class KullbackDistEvaluator implements Evaluator
{

    public int evaluate(int color, List<Integer> nColors) {

        int min = Integer.MAX_VALUE;
        for(int neigh : nColors) {
            int dist = NBitColors.kullbackDist(color,neigh);
            if(dist < min) min = dist;
        }

        return min;
    }

}

class HueEvaluator implements Evaluator
{
    public int evaluate(int color, List<Integer> nColors)
    {
        int min = Integer.MAX_VALUE;

        for(int n : nColors)
        {
            int diff = NBitColors.hue(color) - NBitColors.hue(n);
            diff = Math.abs(diff);

            if(diff < min)
            {
                min = diff;
            }
        }

        return min;
    }
}
