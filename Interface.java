import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class Interface extends Agent {
    
    @Override
    protected void setup(){
        
        JFrame window = new JFrame("TRABALHO 3º - INTELIGENCIA COMPUTACIONAL ");
        AgInterface game = new AgInterface();
        
        window.add(game);
        window.pack();
        window.setVisible(true);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        
        game.start();
            
        addBehaviour(new CyclicBehaviour(this) {

            public void action(){
                             
                ACLMessage msg = myAgent.receive();
            
                if(msg != null){
                    String content = msg.getContent();
                
                    if(content.equalsIgnoreCase("verde") && "sinalVertical@127.0.0.1:1099/JADE".equals(msg.getSender().getName())){
                       game.imgVert = game.verdeVert;
                        System.out.println("sV VERDE");
                    } else if(content.equalsIgnoreCase("amarelo") && "sinalVertical@127.0.0.1:1099/JADE".equals(msg.getSender().getName())){
                       game.imgVert = game.amareloVert;
                        System.out.println("sV AMARELO");
                    } else if(content.equalsIgnoreCase("red") && "sinalVertical@127.0.0.1:1099/JADE".equals(msg.getSender().getName())){
                        game.imgVert = game.redVert;
                        System.out.println("sV VERMELHO");
                    }
                    
                    if(content.equalsIgnoreCase("verde") && "sinalHorizontal@127.0.0.1:1099/JADE".equals(msg.getSender().getName())){
                       game.imgHori = game.verdeHori;
                       System.out.println("sH VERDE");

                    } else if(content.equalsIgnoreCase("amarelo") && "sinalHorizontal@127.0.0.1:1099/JADE".equals(msg.getSender().getName())){
                       game.imgHori = game.amareloHori;
                       System.out.println("sH AMARELO");
                    } else if(content.equalsIgnoreCase("red") && "sinalHorizontal@127.0.0.1:1099/JADE".equals(msg.getSender().getName())){
                       game.imgHori = game.redHori;
                      System.out.println("sH VERMELHO");
                    }

                    
                } else {
                    block();
                }
            }
            
        });
    }
    
    public class AgInterface extends Canvas implements Runnable{
        private static final int larg = 800;
        private static final int alt = 600;
    
        private boolean gameOn = false;
    
        private Thread thread;
    
        private int imglarg = larg;
        private int imgalt = alt;

        private Image img = new ImageIcon(getClass().getResource("transito.png")).getImage();
        public Image verdeVert = new ImageIcon(getClass().getResource("verdev.png")).getImage();
        public Image amareloVert = new ImageIcon(getClass().getResource("amarelov.png")).getImage();
        public Image redVert = new ImageIcon(getClass().getResource("redv.png")).getImage();
        public Image verdeHori = new ImageIcon(getClass().getResource("verdeh.png")).getImage();
        public Image amareloHori = new ImageIcon(getClass().getResource("amareloh.png")).getImage();
        public Image redHori = new ImageIcon(getClass().getResource("redh.png")).getImage();
        public Image imgVert = verdeVert;
        public Image imgHori = redHori;

    
        public AgInterface(){
            setPreferredSize(new Dimension(larg,alt));
        }
    
        public void start(){
            gameOn = true;
            thread = new Thread(this);
            thread.start();
        }
    
        @Override
        public void run(){
            while(gameOn){
                update();
                draw();
            }
        }   
    
        public void draw(){
            BufferStrategy buffer = getBufferStrategy();
            if(buffer == null){
                createBufferStrategy(3);
                return;
            }
       
            Graphics g = buffer.getDrawGraphics();
       
            g.drawImage(img,0,0,imglarg,imgalt,0,0,768,575,null);
            g.drawImage(imgVert,30,180,28,74,null);
            g.drawImage(imgHori,720,280,74,28,null);

            g.dispose();
            buffer.show();
        }
    
        public void update(){
        }
   } 
}   
