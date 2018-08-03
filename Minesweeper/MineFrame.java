/**
 * 一个简单的扫雷游戏
 */


package com.waston.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * 主窗体类
 * @author thnk
 *
 */
public class MineFrame extends JFrame implements ActionListener,MouseListener,Runnable{

	private static final long serialVersionUID = 1L;
	
	private JPanel jp1;
	private JPanel jp2;
	private JMenuBar menuBar;
	private JButton[][] buttons;
	private JLabel showMine;//显示剩余地雷的个数
	private JLabel showTime;//显示已使用的时间
	private int time;//已使用的时间
	boolean isOver = true;//游戏是否还在继续
	private int[] vis;//是地雷按钮的角标
	private int[][] numbers;//按钮上显示的数字
	private boolean[][] isclicked;//该按钮是否被点击
	private int cols;//地雷的行和列
	private int rows;
	private int totalCounts;//地雷的总个数
	private int clickCounts;//已经点开的个数
	private int gaussCounts;//猜中的个数
	
	public MineFrame(){
		//基本的设置
		super("扫雷游戏");
		this.setLocation(300,200);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//创建菜单栏
		createMenus();
		
		//初始化标签信息
		showMine = new JLabel();
		showTime = new JLabel();
		jp1 = new JPanel();
		jp2 = new JPanel();
		jp1.add(showMine);
		jp1.add(new JLabel("  "));
		jp1.add(showTime);
		this.add(jp1,BorderLayout.NORTH);
		this.add(jp2,BorderLayout.CENTER);
		
		//初始化游戏
		initGame(9,9,10);
		
	}
	
	//创建菜单
	private void createMenus(){
		menuBar = new JMenuBar();
		JMenu options = new JMenu("选项");
		JMenuItem newGame = new JMenuItem("新游戏");//开始游戏
		JMenuItem exit = new JMenuItem("退出");
		options.add(newGame);
		options.add(exit);
		
		//游戏等级
		JMenu setting = new JMenu("设置");
		JMenuItem easy = new JMenuItem("容易");
		JMenuItem medium = new JMenuItem("中等");
		JMenuItem difficult = new JMenuItem("困难");
		setting.add(easy);
		setting.add(medium);
		setting.add(difficult);
		
		//游戏帮助
		JMenu help = new JMenu("帮助");
		JMenuItem about = new JMenuItem("关于");
		help.add(about);
		
		menuBar.add(options);
		menuBar.add(setting);
		menuBar.add(help);
		this.setJMenuBar(menuBar);
		
		//注册监听器
		newGame.setActionCommand("newGame");
		newGame.addActionListener(this);
		
		exit.setActionCommand("exit");
		exit.addActionListener(this);
		
		easy.setActionCommand("easy");
		easy.addActionListener(this);
		medium.setActionCommand("medium");
		medium.addActionListener(this);
		difficult.setActionCommand("difficult");
		difficult.addActionListener(this);
		
		about.setActionCommand("help");
		about.addActionListener(this);
	}
	
	//初始化游戏
		public void initGame(int rows,int cols,int totalCounts){
			this.rows = rows;
			this.cols = cols;
			this.totalCounts = totalCounts;
			isclicked = new boolean[rows][cols];
			time = 0;
			isOver = true;
			clickCounts = 0;
			gaussCounts = 0;
			
			showMine.setText("你以标记地雷个数:0");
			showTime.setText("您已使用时间:0秒");
			jp2.removeAll();//移除掉原来的按钮
			createMines();
			//设置大小
			this.setSize(rows*35,cols*35);
			//设置出现的位置,居中
			int x = (int) this.getToolkit().getScreenSize().getWidth();
			int y = (int) this.getToolkit().getScreenSize().getHeight();
			this.setLocation((int)(x-this.getSize().getWidth())/2,
					(int)(y-this.getSize().getHeight())/2);
			
			//开启线程计时
			Thread t = new Thread(this);
			t.start();
		}
	
	//创建按钮,初始化界面,由createMines()调用
	private void createButtons(){
		jp2.setLayout(new GridLayout(rows, cols));
		buttons = new JButton[rows][cols];
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				buttons[i][j] = new JButton();
				buttons[i][j].setMargin(new Insets(0,0,0,0));
				//buttons[i][j].setText(numbers[i][j]+"");
				buttons[i][j].addMouseListener(this);
				buttons[i][j].setName(i+" "+j);//设置按钮的名字,方便知道是哪个按钮触发,并且传递角标
				jp2.add(buttons[i][j]);
			}
		}
		
		
	}
	
	//随机创建地雷,并算出每一个点应显示的周围地雷的个数
	private void createMines(){
		//通过该数组计算出地雷周围八个格子应该显示的数字
		int[][] dir = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};
		numbers = new int[rows][cols];
		vis = new int[totalCounts];
		for(int i=0;i<vis.length;i++){
			boolean flag = true;
			int index = new Random().nextInt(rows*cols);
			for(int j=0;j<i;j++){
				if(vis[j]==index){
					flag = false;
					i--;
					break;
				}
			}
			if(flag){
				vis[i] = index;
				int x = index/cols;
				int y = index%cols;
				//本身是地雷,让数字等于地雷的总个数加1
				numbers[x][y] = totalCounts+1;
				for(int j=0;j<dir.length;j++){
					int realX = x+dir[j][0];
					int realY = y+dir[j][1];
					//如果这个点是有效的(没有出界)并且本身不是地雷
					if(realX>=0&&realX<rows&&realY>=0&&realY<cols&&numbers[realX][realY]<totalCounts+1)
						numbers[realX][realY]++;
				}
			}
		}
		
		createButtons();
	}
	
	
	
	//踩到地雷后,显示所有地雷
	private void showAllMine(){
		for(int i=0;i<vis.length;i++){
			int x = vis[i]/cols;
			int y = vis[i]%cols;
			ImageIcon icon = new ImageIcon("2.jpg");
			buttons[x][y].setIcon(icon);
		}
	}
	
	//当点击一个空白区域时,显示这一块不是地雷按钮
	private void showEmpty(int x,int y){
		buttons[x][y].setEnabled(false);
		buttons[x][y].setBackground(Color.GREEN);
		isclicked[x][y] = true;
		int[][] dir = {{0,-1},{-1,0},{0,1},{1,0}};
		for(int i=0;i<4;i++){
			int nextX = x+dir[i][0];
			int nextY = y+dir[i][1];
			//还没有被点过
			if(nextX>=0&&nextX<rows&&nextY>=0&&nextY<cols&&!isclicked[nextX][nextY]){
				
				if(numbers[nextX][nextY]==0){
					showEmpty(nextX,nextY);
				}
				else if(numbers[nextX][nextY]<=8){
					buttons[nextX][nextY].setText(numbers[nextX][nextY]+"");
					buttons[nextX][nextY].setBackground(Color.GREEN);
				}
			}
		}
	}
	
	//该线程用来计算使用时间
	@Override
	public void run() {
		time = 0;
		while(isOver){
			try {
				Thread.sleep(1000);//睡眠一秒钟
				time++;
				//System.out.println(time);
				showTime.setText("您已使用时间:"+time+"秒!");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//退出游戏
		if(e.getActionCommand().equals("exit")){
			System.exit(0);
		}
		//新游戏
		else if("newGame".equals(e.getActionCommand())){
			isOver = false;
			initGame(rows,cols,10);
		}
		else if("easy".equals(e.getActionCommand())){
			isOver = false;
			initGame(9,9,10);
		}
		else if("medium".equals(e.getActionCommand())){
			isOver = false;
			initGame(15,15,50);
		}
		else if("difficult".equals(e.getActionCommand())){
			isOver = false;
			initGame(22,22,100);
		}
		//游戏帮助
		else if("help".equals(e.getActionCommand())){
			String information = "尽快的找到游戏中所有布置的雷,这样你才能获取胜利!\n"
					+"记住,千万不要踩中地雷,否则您就输了!";
			JOptionPane.showMessageDialog(null,information);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//System.out.println(e.getModifiers());
		//如果已经猜中地雷了,再次点击按钮将不再触发事件
		if(!isOver)
			return;
		
		JButton source = (JButton) e.getSource();
		String[] infos = source.getName().split(" ");
		int x = Integer.parseInt(infos[0]);
		int y = Integer.parseInt(infos[1]);
		if(e.getModifiers()==MouseEvent.BUTTON1_MASK){
			isclicked[x][y] = true;//该按钮已被点击过
			if(numbers[x][y]==totalCounts+1){
				showAllMine();
				isOver = false;//游戏结束
				JOptionPane.showMessageDialog(null, "你踩中地雷了,请重新开始！");
				return;
			}
			if(numbers[x][y]==0){
				showEmpty(x, y);
				return;
			}
			source.setBackground(Color.GREEN);
			source.setText(numbers[x][y]+"");
			//source.setEnabled(false);
			
		}else if(e.getModifiers()==MouseEvent.BUTTON3_MASK){
			//奇数次右键标记地雷
			if(!isclicked[x][y]){
				ImageIcon icon = new ImageIcon("1.png");
				source.setIcon(icon);
				clickCounts++;
				showMine.setText("你以标记地雷个数: "+clickCounts);
				if(numbers[x][y]==totalCounts+1){
					gaussCounts++;
				}
				if(gaussCounts==totalCounts){
					JOptionPane.showMessageDialog(null, "恭喜您赢啦！");
				}
			}
			//偶数次右键取消标记
			else{
				clickCounts--;
				showMine.setText("你以标记地雷个数: "+clickCounts);
				if(numbers[x][y]==totalCounts+1){
					gaussCounts--;
				}
				//去掉图标
				ImageIcon icon = new ImageIcon();
				source.setIcon(icon);
			}
			isclicked[x][y] = !isclicked[x][y];
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	

}
