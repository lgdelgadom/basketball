/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package basketball;

/**
 *
 * @author Luis
 */

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Rectangle;


public class Objeto {
    
    private int posX;
    private int posY;
    private Animacion animacion;  
    
    public Objeto(int posX, int posY, Animacion animacion) {
        
        this.posX = posX;
        this.posY = posY;
        this.animacion = animacion;
        
    }
    
    public void setPosX(int posX) {
        
        this.posX = posX;
        
    }
    
    public int getPosX() {
        
        return posX;
        
    }
    
    public void setPosY(int posY) {
        
        this.posY = posY;
        
    }
    
    public int getPosY() {
        
        return posY;
        
    }
    
    public void setImageIcon(Animacion animacion) {
        
        this.animacion = animacion;
        
    }
    
    public ImageIcon getImageIcon() {
        
        return animacion.getImagen();
        
    }
    
    public int getAncho() {
        
        return animacion.getImagen().getIconWidth();
    
    }
    
    public int getAlto() {
        
        return animacion.getImagen().getIconHeight();
        
    }
    
    public Image getImagenI() {
        
        return animacion.getImagen().getImage();
        
    }
    
    public Rectangle getPerimetro() {
        
        return new Rectangle(getPosX(), getPosY(), getAncho(), getAlto());
        
    }
    
    public boolean intersecta(Objeto obj) {
        
        return getPerimetro().intersects(obj.getPerimetro());
        
    }
        
}
