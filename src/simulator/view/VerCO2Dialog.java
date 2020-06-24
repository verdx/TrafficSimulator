package simulator.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;


public class VerCO2Dialog extends JDialog{

	private static final long serialVersionUID = 1L;
	protected SpinnerNumberModel CO2LimitSpinnerNM;

	public VerCO2Dialog(CO2TableModel co2table) {
		
		this.setTitle("Roads Contamination History");

		//Set Location and Size
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int width = 800;
		int height = 800;
		int x = (dim.width-width)/2;
		int y = (dim.height-height)/2;
		this.setLocation(x, y);
		this.setPreferredSize(new Dimension(width, height));

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		this.setContentPane(mainPanel);
		
		//Dialog options and info Panel
		JPanel dialogPanel = new JPanel();
		dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
		int width_d = 800;
		int height_d = 50;
		dialogPanel.setPreferredSize(new Dimension(width_d, height_d));
	
		//TOP PANEL
		JPanel topPanel = new JPanel(new GridLayout());
		JLabel explanationText = new JLabel("Select a contamination limit and press UPDATE to show the roads that exceeded this total CO2 in each tick");
		explanationText.setPreferredSize(new Dimension(width, height/3));
		topPanel.add(explanationText);
		topPanel.setBorder(new EmptyBorder(10,10,10,10));
		
		//MIDDLE PANEL
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
		middlePanel.setPreferredSize(new Dimension(width, height/3));
		middlePanel.setBorder(new EmptyBorder(10,10,10,10));

		//CO2 Limit
		JLabel Co2LimitText = new JLabel("Contamination Limit: ");
		middlePanel.add(Co2LimitText);

		CO2LimitSpinnerNM = new SpinnerNumberModel(1,1,Integer.MAX_VALUE,1);
		JSpinner CO2LimitSpinner = new JSpinner(CO2LimitSpinnerNM);
		CO2LimitSpinner.setMaximumSize(new Dimension(width, 30));
		middlePanel.add(CO2LimitSpinner);



		//LOWER PANEL
		JPanel lowerPanelContainer = new JPanel();
		lowerPanelContainer.setLayout(new GridLayout(1, 2, 10, 10));
		lowerPanelContainer.setMaximumSize(new Dimension(width/3, height/3));
		lowerPanelContainer.setBorder(new EmptyBorder(10,10,10,10));
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancel.setPreferredSize(new Dimension(height/3-20, width/6-10));
		lowerPanelContainer.add(cancel);

		JButton ok = new JButton("Update");
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				co2table.update(CO2LimitSpinnerNM.getNumber().intValue());
			}
		});
		ok.setPreferredSize(new Dimension(height/3-20, width/6-10));
		lowerPanelContainer.add(ok);
		
		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.X_AXIS));
		lowerPanel.add(lowerPanelContainer);
		
		//TablePanel
		JScrollPane tableView = new JScrollPane(new JTable(co2table));
		tableView.setPreferredSize(new Dimension(500, 200));
		

		dialogPanel.add(topPanel);
		dialogPanel.add(middlePanel);
		dialogPanel.add(lowerPanel);
		this.add(dialogPanel);
		this.add(tableView);

		this.pack();
		this.setVisible(true);
	}
	
	
	
	

}
