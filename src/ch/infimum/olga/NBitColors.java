
package ch.infimum.olga;

import java.util.Iterator;
import java.lang.Math;
import java.util.*;


public class NBitColors implements Iterable<Integer>
{
    public static final int RED_MASK = 0x00FF0000;
    public static final int GREEN_MASK = 0x0000FF00;
    public static final int BLUE_MASK = 0x000000FF;
    private int bitsPerChannel;
    private int channelMask;
    private int additionalShift;
    private int maxVal;

    public NBitColors(int bitsPerChannel)
    {
        this.bitsPerChannel = bitsPerChannel;
        this.channelMask = (1 << bitsPerChannel) - 1;
        this.additionalShift = 8 - bitsPerChannel;
        this.maxVal = (1 << (3 * bitsPerChannel)) - 1;
    }

    @Override
    public Iterator<Integer> iterator(){

        return new Iterator<Integer>(){
            private int currentColor = 0;

            @Override
            public boolean hasNext()
            {
                return currentColor <= maxVal;
            }

            @Override
            public Integer next()
            {
                int color = currentColor;
                int blue = color & channelMask;
                color >>= bitsPerChannel;
                int green = color & channelMask;
                color >>= bitsPerChannel;
                int red = color & channelMask;

                currentColor += 1;

                return (red << (16 + additionalShift)) | (green << (8 + additionalShift)) | (blue << additionalShift);
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static int euclideanDistance(int color1, int color2)
    {
        int dRed = ((color1 & RED_MASK) >> 16) - ((color2 & RED_MASK) >> 16);
        int dGreen = ((color1 & GREEN_MASK) >> 8) - ((color2 & GREEN_MASK) >> 8);
        int dBlue = (color1 & BLUE_MASK) - (color2 & BLUE_MASK);

        int dist = dRed*dRed + dGreen*dGreen + dBlue*dBlue;

        return dist;
    }

    public static int brightness(int color)
    {
        int red = (color & RED_MASK) >> 16; 
        int green = (color & GREEN_MASK) >> 8;
        int blue = (color & BLUE_MASK);

        double brightness = red*red*0.241 + green*green*0.691 + blue*blue*0.068;

        return (int)brightness;
    }


    public static int warmth(int color)
    {
        int red = (color & RED_MASK) >> 16;
        int blue = (color & BLUE_MASK);

        return (red - blue);
    }

    public static int hammingDist(int color1, int color2)
    {
        return Integer.bitCount(color1 ^ color2);
    }

    // Relative Luminance
    // http://en.wikipedia.org/wiki/Luminance_(relative)
    public static int luminance(int color)
    {
        int red = (color & RED_MASK) >> 16; 
        int green = (color & GREEN_MASK) >> 8;
        int blue = (color & BLUE_MASK);

        double y = 0.2126*red + 0.7152*green + 0.0722*blue;

        return (int)y;
    }

    // Luma
    // http://www.georeference.org/doc/colors_as_hue_saturation_and_brightness.htm
    public static int luma(int color)
    {
        int red = (color & RED_MASK) >> 16; 
        int green = (color & GREEN_MASK) >> 8;
        int blue = (color & BLUE_MASK);

        double y = 0.3*red + 0.59*green + 0.11*blue;

        return (int)y;
    }

    // Chroma
    // http://en.wikipedia.org/wiki/HSL_and_HSV
    public static int chroma(int color)
    {
        int red = (color & RED_MASK) >> 16; 
        int green = (color & GREEN_MASK) >> 8;
        int blue = (color & BLUE_MASK);

        int max = red;
        int min = red;
        List<Integer> d = Arrays.asList(red,green,blue);
        for(int c : d) {
            if(c > max) max = c;
            if(c < min) min = c;
        }
        
        return max-min;
    }

    // Saturation
    // http://en.wikipedia.org/wiki/HSL_and_HSV
    // http://www.georeference.org/doc/colors_as_hue_saturation_and_brightness.htm
    public static int saturation(int color)
    {
        int red = (color & RED_MASK) >> 16; 
        int green = (color & GREEN_MASK) >> 8;
        int blue = (color & BLUE_MASK);

        int saturation = 0;
        if(!( red == green && green == blue)) {

            int max = red;
            int min = red;
            List<Integer> d = Arrays.asList(red,green,blue);
            for(int c : d) {
                if(c > max) max = c;
                if(c < min) min = c;
            }

            saturation = 255 * (max - min) / (max + min); //also try 255 * (M - m) / (511 - (M + m))
        }

        
        return saturation;
    }




    // Chebyshev distance
    // http://en.wikipedia.org/wiki/Chebyshev_distance
    public static int chebyshevDist(int color1, int color2)
    {

        int rd = Math.abs( ((color1 & RED_MASK) >> 16) - ((color2 & RED_MASK) >> 16) );
        int gd = Math.abs( ((color1 & GREEN_MASK) >> 8) - ((color2 & GREEN_MASK) >> 8) );
        int bd = Math.abs( (color1 & BLUE_MASK) - (color2 & BLUE_MASK) );

        List<Integer> d = Arrays.asList(rd,gd,bd);
        int cheby = rd;
        for(int distance : d) {
            if(distance > cheby) cheby = distance;
        }

        return cheby;
    }

    // TaxiCab distance
    // http://en.wikipedia.org/wiki/Manhattan_distance
    public static int taxiCab(int color1, int color2) {

        int dRed = Math.abs( ((color1 & RED_MASK) >> 16) - ((color2 & RED_MASK) >> 16));
        int dGreen = Math.abs( ((color1 & GREEN_MASK) >> 8) - ((color2 & GREEN_MASK) >> 8));
        int dBlue = Math.abs( (color1 & BLUE_MASK) - (color2 & BLUE_MASK));

        int dist = dRed + dGreen + dBlue;

        return dist;

    }

    // Minkowski distance
    // http://en.wikipedia.org/wiki/Minkowski_distance
    public static int minkowskiDist(int color1, int color2) {

        double p = 8;
        // use p >= 1 we have the Minkowski inequality
        // careful : the higher the power, the longer the wait

        double dRed = Math.abs( ((color1 & RED_MASK) >> 16) - ((color2 & RED_MASK) >> 16));
        double dGreen = Math.abs( ((color1 & GREEN_MASK) >> 8) - ((color2 & GREEN_MASK) >> 8));
        double dBlue = Math.abs( (color1 & BLUE_MASK) - (color2 & BLUE_MASK));

        int dist = (int)Math.pow(Math.pow(dRed,p) + Math.pow(dGreen,p) + Math.pow(dBlue,p), 1/p);

        return dist;

    }

    // Jaccard index
    // http://en.wikipedia.org/wiki/Jaccard_index
    public static int jaccardDist(int color1, int color2) {

        double index = ((double)(color1 & color2 )) / ((double)( color1 | color2 ));
        return (int)index * 100000;

    }

    // Damerau–Levenshtein distance
    // http://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance
    // 
    public static int damerauDist(int color1, int color2) {

        String compOne = color1+"";
        String compTwo = color2+"";

        int res = -1;
        int INF = compOne.length() + compTwo.length();
 
        int[][] matrix = new int[compOne.length()+1][compTwo.length()+1];
 
        for (int i = 0; i < compOne.length(); i++)
        {
            matrix[i+1][1] = i;
            matrix[i+1][0] = INF;
        }
 
        for (int i = 0; i < compTwo.length(); i++)
        {
            matrix[1][i+1] = i;
            matrix[0][i+1] = INF;
        }
 
        int[] DA = new int[24];
 
        for (int i = 0; i < 24; i++)
        {
            DA[i] = 0;
        }
 
        for (int i = 1; i < compOne.length(); i++)
        {
            int db = 0;
 
            for (int j = 1; j < compTwo.length(); j++)
            {
 
                int i1 = DA[compTwo.indexOf(compTwo.charAt(j-1))];
                int j1 = db;
                int d = ((compOne.charAt(i-1)==compTwo.charAt(j-1))?0:1);
                if (d == 0) db = j;
 
                matrix[i+1][j+1] = Math.min(Math.min(matrix[i][j]+d, matrix[i+1][j]+1),Math.min(matrix[i][j+1]+1,matrix[i1][j1]+(i - i1-1)+1+(j-j1-1)));
            }
            DA[compOne.indexOf(compOne.charAt(i-1))] = i;
        }
         
        return matrix[compOne.length()][compTwo.length()];
    }

    // Hellinger distance
    // http://en.wikipedia.org/wiki/Hellinger_distance
    public static int hellingerDist(int color1, int color2) {

        double dRed = Math.pow( Math.sqrt((color1 & RED_MASK) >> 16) - Math.sqrt((color2 & RED_MASK) >> 16),2);
        double dGreen = Math.pow( Math.sqrt((color1 & GREEN_MASK) >> 8) - Math.sqrt((color2 & GREEN_MASK) >> 8),2);
        double dBlue = Math.pow( Math.sqrt(color1 & BLUE_MASK) - Math.sqrt(color2 & BLUE_MASK),2);

        double dist = (1/Math.sqrt(2))*Math.sqrt(dRed + dGreen + dBlue);

        return (int)dist;

    }

    // Kullback-Leibler Divergence
    // http://en.wikipedia.org/wiki/Bregman_divergence
    public static int kullbackDist(int color1, int color2) {

        int red1 = (color1 & RED_MASK) >> 16; 
        int green1 = (color1 & GREEN_MASK) >> 8;
        int blue1 = (color1 & BLUE_MASK);
        int red2 = (color2 & RED_MASK) >> 16; 
        int green2 = (color2 & GREEN_MASK) >> 8;
        int blue2 = (color2 & BLUE_MASK);

        int sp = red1 + green1 + blue1;
        int sq = red2 + green2 + blue2;
        List<Integer> p = Arrays.asList(red1,green1,blue1);
        List<Integer> q = Arrays.asList(red2,green2,blue2);

        double ln = 0;
        for(int i =0; i < 3; i++) {
            ln = p.get(i)*Math.log((double)p.get(i)/(double)q.get(i));
        }

        return (int)(ln - sp + sq);

    }

    // Hue
    // http://en.wikipedia.org/wiki/Hue
    public static int hue(int color) {

        double red = (color & RED_MASK) >> 16;
        double green = (color & GREEN_MASK) >> 8;
        double blue = (color & BLUE_MASK);

        return (int)Math.atan2(Math.sqrt(3)*(green - blue), 2*red - green - blue);
    }

}
