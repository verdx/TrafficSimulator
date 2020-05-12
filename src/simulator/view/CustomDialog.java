package simulator.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import simulator.control.Controller;

public abstract class CustomDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	protected Controller _ctrl;
	protected int _time;
	protected JComboBox<Object> list1DropList;
	protected JComboBox<Object> list2DropList;
	protected SpinnerNumberModel ticksSpinnerNM;

	public CustomDialog(MainWindow mw, Controller ctrl, List<?> list1, List<?> list2, int time, String nombreLista1, String nombreLista2, String descripcion) {
		super(mw, true);
		
		_ctrl = ctrl;
		_time = time;
		
		this.setTitle("Change Road Weather");

		//Set Location and Size
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int width = 800;
		int height = 150;
		int x = (dim.width-width)/2;
		int y = (dim.height-height)/2;
		this.setLocation(x, y);
		this.setPreferredSize(new Dimension(width, height));

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		this.setContentPane(mainPanel);
	
		//TOP PANEL
		JPanel topPanel = new JPanel(new GridLayout());
		JLabel explanationText = new JLabel(descripcion);
		explanationText.setPreferredSize(new Dimension(width, height/3));
		topPanel.add(explanationText);
		topPanel.setBorder(new EmptyBorder(10,10,10,10));
		
		//MIDDLE PANEL
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
		middlePanel.setPreferredSize(new Dimension(width, height/3));
		middlePanel.setBorder(new EmptyBorder(10,10,10,10));

		//List 1
		JLabel lista1Text = new JLabel(nombreLista1 + ": ");
		middlePanel.add(lista1Text);

		list1DropList = new JComboBox<Object>();
		for(Object o: list1) {
			list1DropList.addItem(o);
		}
		list1DropList.setMaximumSize(new Dimension(width, 30));
		middlePanel.add(list1DropList);

		//List 2
		JLabel lista2Text = new JLabel(nombreLista2 + ": ");
		middlePanel.add(lista2Text);

		list2DropList = new JComboBox<Object>();
		for(Object o: list2) {
			list2DropList.addItem(o);
		}
		list2DropList.setMaximumSize(new Dimension(width, 30));
		middlePanel.add(list2DropList);


		//Ticks
		JLabel TicksText = new JLabel("Ticks: ");
		middlePanel.add(TicksText);

		ticksSpinnerNM = new SpinnerNumberModel(1,1,100,1);
		JSpinner ticksSpinner = new JSpinner(ticksSpinnerNM);
		ticksSpinner.setMaximumSize(new Dimension(width, 30));
		middlePanel.add(ticksSpinner);

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

		JButton ok = new JButton("Ok");
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				okActionPerformed(e);
			}
		});
		ok.setPreferredSize(new Dimension(height/3-20, width/6-10));
		lowerPanelContainer.add(ok);
		
		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.X_AXIS));
		lowerPanel.add(lowerPanelContainer);

		this.add(topPanel);
		this.add(middlePanel);
		this.add(lowerPanel);

		this.pack();
		this.setVisible(true);
	}
	
	public abstract void okActionPerformed(ActionEvent e);
}

