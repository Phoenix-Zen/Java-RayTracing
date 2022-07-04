package fr.phoenix.engine.object.render;

public class Color {

    private float red;
    private float green;
    private float blue;

    public Color(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
    public Color(int argb) {
        red = (float) (((argb >> 16) & 0xff) / 255.0);
        green = (float) (((argb >> 8) & 0xff)/255.0);
        blue = (float) (((argb) & 0xff)/255.0);
    }

    public Color multiply(float v){
        return new Color(red*v,green*v,blue*v);
    }

    public java.awt.Color getColor(){
        return new java.awt.Color(red, green, blue);
    }

    public static final Color BLACK = new Color(0F,0F,0F);
    public static final Color WHITE = new Color(1F, 1F, 1F);
    public static final Color RED = new Color(1F, 0F, 0F);
    public static final Color GREEN = new Color(0F, 1F, 0F);
    public static final Color BLUE = new Color(0F, 0F, 1F);
    public static final Color MAGENTA = new Color(1.0F, 0.0F, 1.0F);
    public static final Color GRAY = new Color(0.5F, 0.5F, 0.5F);
    public static final Color DARK_GRAY = new Color(0.2F, 0.2F, 0.2F);
    public static final Color BROWN = new Color(.3254F, .1921F, 0.0941F);

    //0 < ratio < 1
    public Color mix(Color color, float ratio) {
        return multiply(1-ratio).add(color.multiply(ratio));
    }

    public Color add(float r,float g,float b){
        return new Color(red+r, green+g, blue+b);
    }
    public Color add(Color c){
        return add(c.red, c.green, c.blue);
    }

    @Override
    public String toString() {
        return "R:"+red+" G:"+green+" B:"+blue;
    }
}
