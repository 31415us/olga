
package ch.infimum.olga;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class ImageGenerator
{
    private int bitsPerChannel;
    private String fileName;
    private Evaluator evaluator;

    private int width;
    private int height;


    public ImageGenerator(int bitsPerChannel, String fileName, Evaluator evaluator)
    {
        this.bitsPerChannel = bitsPerChannel;
        this.fileName = fileName;
        this.evaluator = evaluator;

        int length = 3 * bitsPerChannel;
        int shorter = length / 2;
        int longer = length - shorter;

        this.width = (1 << longer);
        this.height = (1 << shorter);
    }

    public void generate()
    {
        NBitColors colorStream = new NBitColors(bitsPerChannel);

        ArrayList<Integer> colorPermutation = new ArrayList<Integer>(width * height);

        Random rnd = new Random();

        for(int color : colorStream)
        {
            colorPermutation.add(color);
        }

        Collections.shuffle(colorPermutation, rnd);

        BufferedImage img = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        HashSet<Pixel> openSet = new HashSet<Pixel>();
        HashSet<Pixel> closedSet = new HashSet<Pixel>();

        for(int color : colorPermutation)
        {

            Pixel best = new Pixel(rnd.nextInt(width),rnd.nextInt(height));
            int bestValue = Integer.MAX_VALUE;
            for(Pixel p : openSet)
            {
                List<Integer> neighbourColors = new ArrayList<Integer>(8);
                for(Pixel n : p.neighbours(width,height))
                {
                    int nColor = img.getRGB(n.x(),n.y()) & 0x00FFFFFF;
                    if(nColor != 0)
                    {
                        neighbourColors.add(nColor);
                    }
                }

                int val = evaluator.evaluate(color,neighbourColors);
                if(val < bestValue)
                {
                    bestValue = val;
                    best = p;
                }
            }

            img.setRGB(best.x(),best.y(),color);

            openSet.remove(best);
            closedSet.add(best);

            for(Pixel n : best.neighbours(width,height))
            {
                if(!closedSet.contains(n))
                {
                    openSet.add(n);
                }
            }
        }

        try
        {
            ImageIO.write(img,"png",new File(fileName));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}

