
package ch.infimum.olga;

import java.util.Iterator;

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
}

