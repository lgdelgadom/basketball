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

import javax.swing.JFrame;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.Iterator;
import java.util.Random;

public class Basketball extends JFrame implements Runnable, KeyListener, MouseListener {

    private Image dbImage;
    private Graphics dbg;
    private int gravedad;
    private int velocidadVertical;
    private int velocidadHorizontal;
    private Bola bola;
    private Canasta canasta;
    private int score;
    private boolean left;
    private boolean right;
    private int vidas;
    private int contador;
    private boolean activo;
    
    public Basketball() {
        
        setSize(700, 700);
        setBackground(Color.orange);
        addKeyListener(this);
        addMouseListener(this);
        
        gravedad = 1;
        velocidadVertical = (int)(-1 * (Math.random() * 20) - 5);
        velocidadHorizontal = (int)((Math.random() * 15) + 5);
        URL bURL = this.getClass().getResource("bola.png");
        URL cURL = this.getClass().getResource("canasta.png");
        right = false;
        left = false;
        vidas = 5;
        contador = 3;
        activo = false;
        
        bola = new Bola(100, 400, Toolkit.getDefaultToolkit().getImage(bURL));
        canasta = new Canasta(0, 0, Toolkit.getDefaultToolkit().getImage(cURL));
        canasta.setPosX(getWidth()/2 - canasta.getAncho()/2);
        canasta.setPosY(getHeight() - canasta.getAlto() - 10);
        start();
        
    }
    
    public void start() {
        
        Thread th = new Thread(this);
        th.start();
        
    }
    
    public void run() {
        
        while(true){
        
            actualiza();
            checaColision();
            repaint();
            try{
                Thread.sleep(20);
            } 
            catch(InterruptedException ex){
                System.out.println("Error en " + ex.toString());
            }
        }
    }
    
    void actualiza() {
        
        if(activo) {
            bola.setPosX(bola.getPosX() + velocidadHorizontal);
            bola.setPosY(bola.getPosY() + velocidadVertical);
            velocidadVertical += gravedad;
        }
        
        if(left){
            canasta.setPosX(canasta.getPosX()-(vidas * 3));
        }
        if(right){
            canasta.setPosX(canasta.getPosX()+(vidas * 3));
        }
        
    }
    
    void checaColision() {
        
        if(bola.getPosY() > getHeight()){
            
            bola.setPosX(100);
            bola.setPosY(400);
            velocidadVertical = (int)(-1 * (Math.random() * 20) - 5);
            velocidadHorizontal = (int)((Math.random() * 15) + 5);
            activo = false;
            
            if(contador > 1) {
                contador--;
            } else {
                contador = 3;
                vidas--;
            }
            
        }
        
        if(bola.getPosX() + bola.getAncho() > getWidth()){
            
            velocidadHorizontal *= -1;
            
        }
        
        if(bola.intersecta(canasta)){
            
            bola.setPosX(100);
            bola.setPosY(400);
            velocidadVertical = (int)(-1 * (Math.random() * 20) - 5);
            velocidadHorizontal = (int)((Math.random() * 15) + 5);
            score +=2;
            activo = false;
            
        }
        
    }
    
    public void keyPressed(KeyEvent e) {
        
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = true;
        }
        
    }
    
    public void keyReleased(KeyEvent e) {
        
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = false;
        }
        
    }
    
    public void mousePressed(MouseEvent e) {
        
        if(bola.getPerimetro().contains(e.getX(), e.getY())) {
            
            activo = true;
            
        }
        
    }
    
    public void paint(Graphics g) {
        
        if(dbImage == null) {
            dbImage = createImage(this.getSize().width, this.getSize().height);
            dbg = dbImage.getGraphics ();
        }

        dbg.setColor(getBackground ());
	dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);

	dbg.setColor(getForeground());
	paint1(dbg);

	g.drawImage (dbImage, 0, 0, this);
        
    }
    
    public void paint1 (Graphics g){
        
        if(bola != null){
            
            g.drawImage(bola.getImagenI(), bola.getPosX(), bola.getPosY(), this);
            g.drawImage(canasta.getImagenI(), canasta.getPosX(), canasta.getPosY(), this);
            g.setFont(new Font("arial", Font.BOLD, 20));
            g.setColor(Color.white);
            g.drawString("Score: " + score, 20, 60);
            g.drawString("Lives: " + vidas, 20, 80);
            
        } else {
            
            g.setColor(Color.blue);
            g.drawString("No se cargo la imagen...", 20, 20);
            
        }
        
    }
    
    public static void main(String[] args) {
        
        Basketball basket = new Basketball();
        basket.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        basket.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent me) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}