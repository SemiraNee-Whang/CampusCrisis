package main;

import java.awt.*;
import javax.swing.*;
import entity.Player;
import tile.TileManager;
import GUI.UI;
import GUI.LoginManager;
import GUI.PresidentSetup;
import GUI.Dashboard;
import GUI.RequestList; 
import GUI.Instructions;
import GUI.ReportView;


public class GamePanel extends JPanel implements Runnable {
    
    // SCREEN SETTINGS
    final int originalTileSize = 16;
    final int scale = 4;
    public final int tileSize = originalTileSize * scale; // 64x64
    public final int maxScreenCol = 15;
    public final int maxScreenRow = 10;
    public final int screenWidth = tileSize * maxScreenCol; // 960px
    public final int screenHeight = tileSize * maxScreenRow; // 640px
    
    // FPS
    int FPS = 60;
    
    // SYSTEM (Varibles)
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this); 
    public CollisionChecker cChecker = new CollisionChecker(this);
    public UI ui = new UI(this); 
    public LoginManager loginM = new LoginManager(this);
    public MouseHandler mouseH = new MouseHandler(this);
    public PresidentSetup pSetup = new PresidentSetup(this);
    public EventManager eventM = new EventManager(this);
    public Dashboard dashboard = new Dashboard(this);
    public RequestList reqList = new RequestList(this); 
    public Instructions instructions = new Instructions(this);
    public GUI.ReportView reportView = new GUI.ReportView(this);
    public GUI.DecisionHistory historyView = new GUI.DecisionHistory(this);
    public java.util.ArrayList<main.Request> history = new java.util.ArrayList<>();
    
    Thread gameThread;
    
    // ENTITY
    public Player player = new Player(this, keyH);
    
    // GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;    
    public final int loginState = 2;
    public final int setupState = 3;
    public final int requestState = 4; 
    public final int historyState = 5;
    public final int instructionState = 6;
    public final int reportState = 7;
    
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(new Color(30, 30, 40)); 
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH); 
        this.addMouseListener(mouseH);
        this.addMouseMotionListener(mouseH);
        this.addMouseWheelListener(mouseH);
        this.setFocusable(true);
        
        gameState = titleState; 
    }
    
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    public void run() {
        double drawInterval = 1000000000 / FPS; 
        double time = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        
        while (gameThread != null) {
            currentTime = System.nanoTime();
            time += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            
            if (time >= 1) {
                update();
                repaint();
                time--;
            }
        }
    }
    
    public void update() {
        if (gameState == playState) {
            player.update();
            dashboard.updateTimer();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        //MENU STATES
        if (gameState == titleState) { 
            ui.draw(g2); 
        }
        else if (gameState == loginState) { 
            loginM.draw(g2);
        }
        else if (gameState == setupState) { 
            pSetup.draw(g2);
        }
        
        else if (gameState == instructionState) { 
            instructions.draw(g2);
        }
        
        else if (gameState == reportState) {
            reportView.draw(g2);
        }
        
        
        //PlayState
        else if (gameState == playState || gameState == requestState || gameState == historyState) {            
            
            // 1. THE CLASSROOM LAYERS
            tileM.drawBackground(g2);   
            tileM.drawChalkboard(g2);   
            tileM.drawDesks(g2);        
            player.draw(g2);            
            tileM.drawMainDesks(g2);    
            tileM.drawLayer2(g2);       
            
            if (gameState == requestState) {
                reqList.draw(g2);       
            }
            if (gameState == historyState) {
                historyView.draw(g2); 
            }
            
            // 3. UI Dashboard (Top & Bottom bars)
            dashboard.draw(g2); 
            
            ui.draw(g2);
        }
        g2.dispose();
    }
}