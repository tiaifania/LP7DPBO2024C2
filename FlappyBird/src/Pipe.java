import javax.swing.*;
import java.awt.*;
public class Pipe {
    private int posX;
    private int posY;
    private int width;
    private int height;
    private Image image;
    private int velocityX;
    boolean passed;

    public Pipe(int posX, int posY, int width, int height, Image image) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.image = image;

        this.velocityX = -3;
        this.passed = false;
    }

    public void setPosX(int posX) {this.posX = posX;}

    public int getPosX() {return this.posX;}

    public void setPosY(int posY) {this.posY = posY;}

    public int getPosY() {return this.posY;}

    public void setWidth(int width) {this.width = width;}

    public int getWidth() {return this.width;}

    public void setHeight(int height) {this.height = height;}

    public int getHeight() {return this.height;}

    public void setImage(Image image) {this.image = image;}

    public Image getImage() {return this.image;}

    public void setVelocityX(int velocityX) {this.velocityX = velocityX;}

    public int getVelocityX() {return this.velocityX;}

    public void setPassed(boolean passed) {this.passed = passed;}

    public boolean getPassed() {return this.passed;}
}
