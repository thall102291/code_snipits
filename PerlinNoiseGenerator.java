/**
 * FILE PerlinNoiseGenerator.java
 * AUTH Timothy
 * DATE 5/22/2017
 * DESC Class that generates 1D perlin noise. (gradient noise to increase realism of appearance)
 *      Based off of Algorithm of Michael Bromleys javascrip implementation.
 *      http://www.michaelbromley.co.uk/blog/90/simple-1d-noise-in-javascript
 */

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;


public class PerlinNoiseGenerator {
    private float min, max;
    private float amplitude;
    private float scale;
    private Random r;
    private ArrayList<Float> history;

    public PerlinNoiseGenerator(float min, float max, float amplitude, float scale){
        this.min = min;
        this.max = max;
        this.amplitude = amplitude;
        this.scale = scale;
        r = new Random();
        history = new ArrayList<>();
        history.add(6f);
    }

    private void generateFirstPoint(){
        float x = randInRange();
        float scaledx = x * scale;
        float floorx = (float)Math.floor(scaledx);

        float t = scaledx - floorx;
        float tSmoothStep = t * t * (1.0f * t);

        history.add(lerp(x, x, tSmoothStep) * amplitude);
    }

    public void generateNoise(){
        float x = randInRange();
        float scaledx = x * scale;
        float floorx = (float)Math.floor(scaledx);

        float t = scaledx - floorx;
        float tSmoothStep = t * t * (1.0f * t);

        history.add(0, lerp(t, history.get(history.size() - 1), tSmoothStep) * amplitude);
    }

    public ArrayList<Float> getHistory(){ return history; }

    /**Linear Interpolate*/
    private float lerp(float a, float b, float t){ return a * (1 - t) + b * t; }

    private float randInRange(){
        return r.nextFloat() * ((max - min) + min);
    }

//**********************************************************************************************************************
//                                                    [Test Case]
//**********************************************************************************************************************
    public static void main(String[] args){
        //generate the random noises
        int numOfPoints = 25;
        int min = -50, max = 500;
        float a = .8f;
        float s = .9f;
        PerlinNoiseGenerator png = new PerlinNoiseGenerator(min, max, a, s);
        PerlinNoiseGenerator png2 = new PerlinNoiseGenerator(min, max, a, s); //noise list twice as long as png
        PerlinNoiseGenerator png3 = new PerlinNoiseGenerator(min, max, a, s); //noise list triple lengh of png

        //generate lists of noise continuing where the previous list left off
        for(int i = 0; i < numOfPoints; i++) {
            png.generateNoise();
        }

        png2.getHistory().addAll(png.getHistory());
        for(int i = 0; i < numOfPoints; i++){
            png2.generateNoise();
        }

        png3.getHistory().addAll(png2.getHistory());
        for(int i = 0; i < numOfPoints; i++){
            png3.generateNoise();
        }


        JFrame f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setTitle("Perlin Noise Example");
        f.setResizable(false);

        JPanel jp = new JPanel(){
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                for(int i = 0; i < png.getHistory().size()-1; i++){
                    g.setColor(Color.RED);
                    g.drawLine(i * (getWidth() / png.getHistory().size()),
                            (int)(60 - (png.getHistory().get(i) * 2.35f)),
                            (i + 1) * (getWidth() / png.getHistory().size()),
                            (int)(60 - (png.getHistory().get(i + 1) * 2.35f)));
                }

                for(int i = 0; i < png2.getHistory().size()-1; i++){
                    g.setColor(i < png.getHistory().size() ? Color.RED : Color.BLUE);
                    g.drawLine(i * (getWidth() / png2.getHistory().size()),
                            (int)(120 - (png2.getHistory().get(i) * 2.1)),
                            (i + 1) * (getWidth() / png2.getHistory().size()),
                            (int)(120 - (png2.getHistory().get(i + 1) * 2.1)));
                }

                for(int i = 0; i < png3.getHistory().size()-1; i++){
                    if(i > png2.getHistory().size()) g.setColor(Color.GREEN);
                    else if(i > png.getHistory().size()) g.setColor(Color.BLUE);
                    else g.setColor(Color.RED);

                    g.drawLine(i * (getWidth() / png3.getHistory().size()),
                            (int)(180 - (png3.getHistory().get(i) * 1.7f)),
                            (i + 1) * (getWidth() / png3.getHistory().size()),
                            (int)(180 - (png3.getHistory().get(i + 1) * 1.7f)));
                }
            }
        };

        jp.setPreferredSize(new Dimension(400, 200));
        jp.setBackground(Color.GRAY);

        f.setContentPane(jp);
        f.pack();
        f.setVisible(true);
        f.setLocationRelativeTo(null);
    }

}
