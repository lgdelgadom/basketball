/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package basketball;

/**
 * v.0.0.1
 * @author Luis y Ricardo
 * A01191118 y A01191463
 */

import javax.swing.JFrame;
//import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
//import java.net.URL;
//import java.util.Iterator;
//import java.util.Random;
import java.io.File;
//import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class Basketball extends JFrame implements Runnable, KeyListener, MouseListener {

    private Image dbImage;
    private Graphics dbg;
    private final int gravedad = 1;
    private int velocidadVertical;
    private int velocidadHorizontal;
    private Bola bola;
    private Canasta canasta;
    private int score;
    private boolean left;
    private boolean right;
    private int vidas;
    private int contador;
    private boolean activo, guardar, cargar, silencio;
    private int state;
    
    private long tiempoActual;
    private long tiempoInicial;
    //Sonidos
    private SoundClip bomb, punch;
     //Animaciones
    private Animacion pelotaAnim, canastaAnim, netLeft, netRight;
    
    public Basketball(){
        init();
        start();
    }
    
    public void init() {
        
        setSize(1000, 700);
        setBackground(Color.orange);
        addKeyListener(this);
        addMouseListener(this);
        
        //SoundClips
        bomb = new SoundClip("Explosion.wav");
        punch = new SoundClip("punch.wav");
        //Animaciones
	Image bola1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("basketball1.png"));
	Image bola2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("basketball2.png"));
        Image bola3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("basketball3.png"));
        Image bola4 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("basketball4.png"));
        Image bola5 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("basketball5.png"));
        Image bola6 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("basketball6.png"));
	Image canasta1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("net.png"));
        Image canastaR1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("netRight1.png"));
        Image canastaR2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("netRight2.png"));
        Image canastaL1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("netLeft1.png"));
        Image canastaL2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("netLeft2.png"));
	
	pelotaAnim = new Animacion();
        canastaAnim = new Animacion();
        netLeft = new Animacion();
        netRight = new Animacion();
        
        
	pelotaAnim.sumaCuadro(bola1, 100);
        pelotaAnim.sumaCuadro(bola2, 100);
        pelotaAnim.sumaCuadro(bola3, 100);
        pelotaAnim.sumaCuadro(bola4, 100);
        pelotaAnim.sumaCuadro(bola5, 100);
        pelotaAnim.sumaCuadro(bola6, 100);

        canastaAnim.sumaCuadro(canasta1, 100);
        netLeft.sumaCuadro(canastaL1, 100);
        netLeft.sumaCuadro(canastaL2, 100);
        netRight.sumaCuadro(canastaR1, 100);
        netRight.sumaCuadro(canastaR2, 100);

        

        velocidadVertical = (int)(-1 * (Math.random() * 15 + 10));
        velocidadHorizontal = (int)((Math.random() * 6) + 10);

        right = false;
        left = false;
        vidas = 5;
        contador = 3;
        activo = false;
        guardar = cargar = silencio = false;
        state = 0;
        
        bola = new Bola(100, 400, pelotaAnim);
        canasta = new Canasta(0, 0, canastaAnim);
        canasta.setPosX(getWidth()/2 - canasta.getAncho()/2);
        canasta.setPosY(getHeight() - canasta.getAlto() - 10);
        
        
    }
    
    public void start() {
        
        Thread th = new Thread(this);
        th.start();
        
    }
    
    public void run() {
        tiempoActual = System.currentTimeMillis();
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
    
    void actualiza(){
        //Determina el tiempo que ha transcurrido desde que el Applet inicio su ejecución
         if(guardar){
             guardar = false;
             try {
                 grabaArchivo();
             } catch(IOException e) {
                 System.out.println("Error en guardar");
             }
         }
         if(cargar){
             cargar=false;
             try {
                leeArchivo();
             } catch(IOException e) {
                 System.out.println("Error en cargar");
             }
         }
         long tiempoTranscurrido = System.currentTimeMillis() - tiempoActual;
            
         //Guarda el tiempo actual
       	 tiempoActual += tiempoTranscurrido;

         //Actualiza la animación en base al tiempo transcurrido
         
        if(state == 0) {
            if(activo) {
                bola.setPosX(bola.getPosX() + velocidadHorizontal);
                bola.setPosY(bola.getPosY() + velocidadVertical);
                velocidadVertical += gravedad;
                pelotaAnim.actualiza(tiempoTranscurrido);
            }

            if(left && canasta.getPosX() > getWidth()/2){
                canasta.setPosX(canasta.getPosX()-(vidas * 3));
                netLeft.actualiza(tiempoTranscurrido);
                canasta.setImageIcon(netLeft);
            }
            else if(right && canasta.getPosX()+canasta.getAncho() < getWidth()){
                canasta.setPosX(canasta.getPosX()+(vidas * 3));
                netRight.actualiza(tiempoTranscurrido);
                canasta.setImageIcon(netRight);
            }
            else{
                canastaAnim.actualiza(tiempoTranscurrido);
                canasta.setImageIcon(canastaAnim);
            }
        }
    }
    
           public void leeArchivo() throws IOException {
                                                          
                BufferedReader fileIn;
                try {
                        fileIn = new BufferedReader(new FileReader("guacamoleSave"));
                } catch (FileNotFoundException e){
                        File puntos = new File("guacamoleSave");
                        PrintWriter fileOut = new PrintWriter(puntos);
                        fileOut.println("100,demo");
                        fileOut.close();
                        fileIn = new BufferedReader(new FileReader("guacamoleSave"));
                }
                String dato = fileIn.readLine();
                      velocidadVertical = (Integer.parseInt(dato));
                      dato = fileIn.readLine();
                      velocidadHorizontal = (Integer.parseInt(dato));
                      dato = fileIn.readLine();
                      score = (Integer.parseInt(dato));
                      dato = fileIn.readLine();
                      vidas = (Integer.parseInt(dato));
                      dato = fileIn.readLine();
                      contador = (Integer.parseInt(dato));
                      dato = fileIn.readLine();
                      bola.setPosX(Integer.parseInt(dato));
                      dato = fileIn.readLine();
                      bola.setPosY(Integer.parseInt(dato));
                      dato = fileIn.readLine();
                      canasta.setPosX(Integer.parseInt(dato));
                      dato = fileIn.readLine();
                      state = (Integer.parseInt(dato));
                      dato = fileIn.readLine();
                      activo = Boolean.parseBoolean(dato);
                fileIn.close();
        }
    
    public void grabaArchivo() throws IOException {
                PrintWriter fileOut = new PrintWriter(new FileWriter("guacamoleSave"));
                fileOut.println(String.valueOf(velocidadVertical));
                fileOut.println(String.valueOf(velocidadHorizontal));
                fileOut.println(String.valueOf(score));
                fileOut.println(String.valueOf(vidas));
                fileOut.println(String.valueOf(contador));
                fileOut.println(String.valueOf(bola.getPosX()));
                fileOut.println(String.valueOf(bola.getPosY()));
                fileOut.println(String.valueOf(canasta.getPosX()));
                fileOut.println(String.valueOf(state));
                fileOut.println(String.valueOf(activo));

                
                fileOut.close();
        }
    
    void checaColision() {
        
        if(bola.getPosY() > getHeight()){
            
            bola.setPosX(100);
            bola.setPosY(400);
            velocidadVertical = (int)(-1 * (Math.random() * 15 + 10));
            velocidadHorizontal = (int)((Math.random() * 6) + 10);
            activo = false;
            if(!silencio)
                bomb.play();
            
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
            velocidadVertical = (int)(-1 * (Math.random() * 15 + 10));
            velocidadHorizontal = (int)((Math.random() * 6) + 10);
            score +=2;
            activo = false;
            if(!silencio)
                punch.play();
            
        }
        
    }
    
    public void keyPressed(KeyEvent e) {
        
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_P) {
            if(state == 1){
                state = 0;
            } else {
                state = 1;
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_I) {
            if(state == 2){
                state = 0;
            } else {
                state = 2;
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_G) {
            guardar=true;
        }
        if(e.getKeyCode() == KeyEvent.VK_C) {
            cargar=true;
        }
        if(e.getKeyCode() == KeyEvent.VK_S) {
            silencio=!silencio;
        }
        if(e.getKeyCode() == KeyEvent.VK_R) {            
            init();
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
            if(vidas==0){
                g.setColor(Color.red);
                g.setFont(new Font("arial", Font.BOLD, 60));
                g.drawString("PERDISTE", getWidth()/2 - 100, getHeight()/2);    
                state = 1;
            }
            else if(state == 1) {
                g.setFont(new Font("arial", Font.BOLD, 60));
                g.drawString("PAUSA", getWidth()/2 - 100, getHeight()/2);
            }
            if(state == 2) {
                g.setColor(Color.green);
                g.fillRect(100, 100, getWidth() - 200, getHeight() - 200);
                g.setColor(Color.black);
                g.setFont(new Font("arial", Font.BOLD, 50));
                g.drawString("INSTRUCCIONES", getWidth()/2 - 210, 200);
                g.setFont(new Font("arial", Font.BOLD, 20));
                g.drawString("Objetivo: atrapar la pelota de basketball para hacer puntos.", getWidth()/2 - 300, 220);
                g.drawString("Presionar flechas(izquierda y derecha) para moverse.", getWidth()/2 - 300, 240);
                g.drawString("Presionar 'i' para instruciones.", getWidth()/2 - 300, 260);
                g.drawString("Presionar 'p' para pausar.", getWidth()/2 - 300, 280);
                g.drawString("Presionar 'g' para guardar el juego.", getWidth()/2 - 300, 300);
                g.drawString("Presionar 'c' para cargar el juego", getWidth()/2 - 300, 320);
                g.drawString("Presionar 's; para silenciar los sonidos.", getWidth()/2 - 300, 340);
                g.drawString("Presionar 'r' para reinicar(experimental) el juego.", getWidth()/2 - 300, 360);
                g.drawString("Tienes 5 vidas y perderas 1 vida cada tercera vez que", getWidth()/2 - 300, 390);
                g.drawString("la pelota toque el piso. Suerte!", getWidth()/2 - 300, 410);
            }
            if(vidas==0){
                g.setColor(Color.red);
                g.setFont(new Font("arial", Font.BOLD, 60));
                g.drawString("PERDISTE", getWidth()/2 - 100, getHeight()/2);    
            }
            
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
