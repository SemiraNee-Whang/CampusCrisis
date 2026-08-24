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
	import Admin.AdminLogin;
	import Admin.AdminMenu;
	import Admin.RequestManager;
	import Admin.UserManager;
	
	
	// TODO: Auto-generated Javadoc
/**
	 * The Class GamePanel.
	 */
	public class GamePanel extends JPanel implements Runnable {
	    
	    /** The original tile size. */
    	//Screen Settings
	    private final int originalTileSize = 16;
	    
    	/** The scale. */
    	private final int scale = 4;
	    
    	/** The tile size. */
    	public final int tileSize = originalTileSize * scale; // 64x64
	    
    	/** The max screen col. */
    	public final int maxScreenCol = 15;
	    
    	/** The max screen row. */
    	public final int maxScreenRow = 10;
	    
    	/** The screen width. */
    	public final int screenWidth = tileSize * maxScreenCol; // 960px
	    
    	/** The screen height. */
    	public final int screenHeight = tileSize * maxScreenRow; // 640px
	    
	    /** The Constant FPS. */
    	//FPS
	    private static final int FPS = 60;
	    
	    /** The user storage. */
    	//System (Variables)
	    public UserStorage userStorage = new UserStorage();
	    
    	/** The request storage. */
    	public RequestStorage requestStorage = new RequestStorage();
	    
    	/** The admin storage. */
    	public AdminStorage adminStorage = new AdminStorage();
	    
    	/** The tile M. */
    	public TileManager tileM = new TileManager(this);
	    
    	/** The key H. */
    	public KeyHandler keyH = new KeyHandler(this); 
	    
    	/** The ui. */
    	public UI ui = new UI(this); 
	    
    	/** The login M. */
    	public LoginManager loginM = new LoginManager(this);
	    
    	/** The mouse H. */
    	public MouseHandler mouseH = new MouseHandler(this);
	    
    	/** The p setup. */
    	public PresidentSetup pSetup = new PresidentSetup(this);
	    
    	/** The report M. */
    	public ReportManager reportM = new ReportManager(this);
	    
    	/** The dashboard. */
    	public Dashboard dashboard = new Dashboard(this);
	    
    	/** The req list. */
    	public RequestList reqList = new RequestList(this); 
	    
    	/** The instructions. */
    	public Instructions instructions = new Instructions(this);
	    
    	/** The report view. */
    	public GUI.ReportView reportView = new GUI.ReportView(this);
	    
    	/** The history view. */
    	public GUI.DecisionHistory historyView = new GUI.DecisionHistory(this);
	    
    	/** The admin login. */
    	public AdminLogin adminLogin = new AdminLogin(this);
	    
    	/** The admin menu. */
    	public AdminMenu adminMenu = new AdminMenu(this);
	    
    	/** The request manager. */
    	public RequestManager requestManager = new RequestManager(this);
	    
    	/** The user manager. */
    	public UserManager userManager = new UserManager(this);
	    
    	/** The decision manager. */
    	public DecisionManager decisionManager = new DecisionManager(this);
	    
	  
	    /** The history. */
    	public java.util.ArrayList<main.Request> history = new java.util.ArrayList<>();
	    
    	/** The requests handled. */
    	public int requestsHandled = 0;
	    
	    /** The game thread. */
    	private Thread gameThread;
	    
	    /** The player. */
    	//Entitiy
	    public Player player = new Player(this);
	    
	    /** The game state. */
    	//Game State
	    public int gameState;
	    
    	/** The title state. */
    	public final int titleState = 0;
	    
    	/** The play state. */
    	public final int playState = 1;    
	    
    	/** The login state. */
    	public final int loginState = 2;
	    
    	/** The setup state. */
    	public final int setupState = 3;
	    
    	/** The request state. */
    	public final int requestState = 4; 
	    
    	/** The history state. */
    	public final int historyState = 5;
	    
    	/** The instruction state. */
    	public final int instructionState = 6;
	    
    	/** The report state. */
    	public final int reportState = 7;
	    
    	/** The admin login state. */
    	public final int adminLoginState = 8;	
	    
    	/** The admin state. */
    	public final int adminState = 9;
	    
    	/** The admin request state. */
    	public final int adminRequestState = 10;
	    
    	/** The admin user state. */
    	public final int adminUserState = 11;
	    
	    /**
    	 * Instantiates a new game panel.
    	 */
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
	    
	    /**
    	 * Start game thread.
    	 */
    	public void startGameThread() {

	        if (gameThread == null) {

	            gameThread = new Thread(this);
	            gameThread.start();
	        }
	    }
	    
	    /**
    	 * Run.
    	 */
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
	    
	    /**
    	 * Update.
    	 */
    	public void update() {
	    	if (gameState == playState || gameState == requestState || gameState == historyState) {
	    		dashboard.updateTimer();
	            checkGameOver();
	        }
	    }
	    
	    /**
    	 * Check game over.
    	 */
    	public void checkGameOver() {
	
	        boolean budgetOut =
	                dashboard.budget <= 0;
	
	        boolean approvalOut =
	                dashboard.approval <= 0;
	
	        boolean approvalComplete =
	                dashboard.approval >= 100;
	
	        boolean requestsFinished =
	                reqList.allRequests.isEmpty();
	
	        boolean timeOut =
	                dashboard.minutes == 0
	                && dashboard.seconds == 0;
	
	        if (budgetOut
	                || approvalOut
	                || approvalComplete
	                || requestsFinished
	                || timeOut) {
	
	            triggerEndScreen();
	        }
	    }
	
	    /**
	     * Ends the current term, saves the results
	     * and displays the report screen.
	     */
	    public void triggerEndScreen() {

	        String presidentName = pSetup.presidentName;

	        if (presidentName == null
	                || presidentName.trim().isEmpty()) {

	            presidentName = "President";
	        }

	        //Attempts to save the end-of-term summary
	        boolean historySaved =
	                reportM.saveGameToHistory(
	                        presidentName,
	                        dashboard.budget,
	                        dashboard.approval);

	        //Generates the detailed report
	        reportM.generateFinalReport(
	                dashboard.approval,
	                dashboard.budget,
	                reqList.history);

	        //Checks whether either storage operation failed
	        if (!historySaved
	                || !reportM.getLastError().isEmpty()) {

	            System.out.println(
	                    "Report error: "
	                    + reportM.getLastError());
	        }

	        //Reloads previous-term records
	        reportView.loadGameHistory();

	        //Displays report screen
	        gameState = reportState;
	    }
	    
	    
	    /**
    	 * Paint component.
    	 *
    	 * @param g the g
    	 */
    	//Using Painting Component
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        Graphics2D g2 = (Graphics2D) g;
	        
	        //Menu State
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
	        else if (gameState == adminLoginState) {
	            adminLogin.draw(g2);
	        }
	        else if (gameState == adminState) {
	            adminMenu.draw(g2);
	        }
	        else if (gameState == adminRequestState) {
	            requestManager.draw(g2);
	        }
	        else if (gameState == adminUserState) {
	            userManager.draw(g2);
	        }
	        
	        
	        //PlayState
	        else if (gameState == playState || gameState == requestState || gameState == historyState) {            
	            
	            //Classroom Layer
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
	            
	            //UI Dashboard (Top & Bottom bars)
	            dashboard.draw(g2); 
	            
	            ui.draw(g2);
	        }
	        g2.dispose();
	    }
	    
	    /**
	     * Increases the number of completed requests.
	     */
	    public void incrementRequestsHandled() {
	        requestsHandled++;
	    }
	    
	    /**
	     * Resets all game values for a new term.
	     */
	    public void resetGame() {

	        dashboard.budget = pSetup.STARTING_BUDGET;
	        dashboard.approval = pSetup.STARTING_APPROVAL;

	        dashboard.minutes = 5;
	        dashboard.seconds = 0;
	        dashboard.secondCounter = 0;

	        requestsHandled = 0;

	        history.clear();
	        reqList.history.clear();

	        reqList.reloadRequests();

	        pSetup.presidentName = "";
	        pSetup.nameBoxSelected = false;
	        pSetup.subState = 0;

	        player.setDefaultValues();
	    }
	}