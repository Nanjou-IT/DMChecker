package fr.upem.dmchecker.graphic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;




import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import fr.upem.dmchecker.Criteria;
import fr.upem.dmchecker.Data;


public class Window extends JFrame implements ActionListener {

	private JMenuBar menuBar;
	private JMenu menu;
	private JButton leftB;
	private JButton rightB;
	private JPanel topPanel;
	private JLabel nameL;
	private JButton runB;
	private JButton stopB;
	private GridBagLayout gridBagLayout;
	private ArrayList<Data> dataList;
	private int indexArray;
	
	
	public void initComponent(List<Criteria> listOfCriteria)
	{
		indexArray=0;
		
		// Menu
		menu = new JMenu();
		menu.setText("Menu");
		menuBar = new JMenuBar();		
		menuBar.add(menu);
		this.add(menuBar);


		//TOP PANEL
		
		Border border = BorderFactory.createLineBorder(Color.black);
		
		topPanel = new JPanel();
		gridBagLayout = new GridBagLayout();
		topPanel.setLayout(gridBagLayout);
		//topPanel.se
		
		
		leftB = new JButton(new ImageIcon("./Image/precedent.png"));
		leftB.setPreferredSize(new Dimension(30, 30));
		leftB.addActionListener(this);
		rightB = new JButton(new ImageIcon("./Image/suivant.png"));
		rightB.setPreferredSize(new Dimension(30, 30));
		rightB.addActionListener(this);
		
		runB = new JButton("RUN");
		runB.addActionListener(this);
	//	runB.setPreferredSize(new Dimension(60, 30));
		stopB = new JButton("STOP");
		stopB.addActionListener(this);
	//	stopB.setPreferredSize(new Dimension(60, 30));
		
		
		GridBagConstraints gbc= new GridBagConstraints();
		gbc.fill = gbc.BOTH;

		
		
		gbc.gridx =0;
		gbc.gridx =0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;		
		topPanel.add(leftB,gbc);
		
		gbc.gridx =1;
		gbc.gridheight = 2;
		gbc.gridwidth = 2;
		topPanel.add(runB);
		
		gbc.gridx =3;
		topPanel.add(stopB);
		gbc.gridx =5;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		topPanel.add(rightB);
		

		nameL = new JLabel("NOM PRENOMS DU BINOME");
		nameL.setPreferredSize(nameL.getMaximumSize());
		nameL.setBorder(border);
		
		
		gbc.gridx = 0;
		gbc.gridy= 1 ;
		gbc.gridwidth = 6;
		gbc.gridheight = 4;
		
		
		//gbc.anchor = 
		topPanel.add(nameL,gbc);
		
		JRadioButton jr = new JRadioButton(new ImageIcon("1"));//new ImageIcon("./Image/precedent.png")
		topPanel.add(jr);
		
		// Criteria
		
		gbc.gridx = 0;
		gbc.gridy= 	5 ;
		
		for(Criteria criteria : listOfCriteria){
			gbc.gridx = 0;
			gbc.gridwidth = 6;
			gbc.gridheight = 2;
			//title
			JLabel label= new JLabel(criteria.getTitle());
			label.setBorder(border);
			topPanel.add(label,gbc);			
			
			
			//JCombobox
			gbc.gridy+=2;			
						
			gbc.gridwidth = 6;
			gbc.gridheight = 2;
			JComboBox<String> markCB = new JComboBox<>();

			for(int i=0; i<= criteria.getBarem();i++){
				markCB.addItem(""+i);
			}
			
			topPanel.add(markCB,gbc);
			gbc.gridy+=2;	
			
			gbc.gridwidth = 6;
			gbc.gridheight = 30;
			JTextArea descriptionTA = new JTextArea(2,1);
			descriptionTA.setText(criteria.getDescription());
			descriptionTA.setBorder(border);
			JScrollPane descriptionSP = new JScrollPane(descriptionTA,
                     JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                     JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			
			JTextArea commentTA = new JTextArea(2,1);
			commentTA.setBorder(border);
			JScrollPane commentSP = new JScrollPane(commentTA,
                     JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                     JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			
			topPanel.add(descriptionSP,gbc);
			gbc.gridy+=30;	
			topPanel.add(commentSP,gbc);
			gbc.gridy+=30;	
			
			
		}
		
		
		this.add(topPanel);
		
		
	}

	public Window(ArrayList<Data> dataList) {
		if(dataList == null || dataList.size() == 0){
			// TODO error
			System.out.println("Error with the list passed in entry");
			System.exit(1);
		}
		this.dataList = dataList;
		LinkedList<Criteria> listOfCriteria = new LinkedList<Criteria>();
		for(int i =0; i<5 ; i++){
			listOfCriteria.add(new Criteria(i,"Title","Description",i*2));
			
		}
		initComponent(listOfCriteria);
		this.setTitle("Little test application");
		this.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(),600); 
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);	// Permet de maximiser la fenetre 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	


	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("datalistnull :"+ (dataList==null)+" Size :" +dataList.size());
		// TODO Auto-generated method stub
		if(e.getSource()== runB){
			
			Runtime runtime = Runtime.getRuntime();
			try {
				
				runtime.exec(dataList.get(indexArray).getExecutablePath().toString());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == stopB){
			
		}
		else if(e.getSource() == leftB){
			if(indexArray>0){
				indexArray--;
			}
		}
		else if(e.getSource() == rightB){
			if(dataList.size()>(indexArray+1)){
				indexArray++;
			}
		}

	}

	public static void main(String[] args) {

		Window mw = new Window(null);
		mw.setVisible(true);
		
	}

}

