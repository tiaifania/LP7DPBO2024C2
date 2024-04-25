import javax.swing.*;
import java.awt.*;


public class Player {
    private int posX;
    private int posY;
    private int width;
    private int height;
    private Image image;
    private int velocityY;

    public Player(int posX, int posY, int width, int height, Image image) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.image = image;

        this.velocityY = -0;
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

    public void setVelocityY(int velocityY) {this.velocityY = velocityY;}

    public int getVelocityY() {return this.velocityY;}
}
