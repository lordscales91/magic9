package io.github.lordscales91.magic9.panels;

import io.github.lordscales91.magic9.constants.MagicActions;
import io.github.lordscales91.magic9.core.CallbackReceiver;
import io.github.lordscales91.magic9.core.MagicUtils;
import io.github.lordscales91.magic9.domain.ConsoleModel;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SelectModelPanel extends JPanel {
	private JLabel lblSelectYourConsoles;
	private JButton btnNew3ds;
	private JButton btnNew3dsXl;
	private JButton btnOld3ds;
	private JButton btn2ds;
	private JPanel panel;
	private JButton btnOld3dsXl;
	private JLabel lblYouHaveSelected;
	private JLabel lblSelectedmodel;
	private JPanel panel_1;
	private CallbackReceiver caller;
	
	public SelectModelPanel(CallbackReceiver caller) {
		this();
		this.caller = caller;
	}

	/**
	 * Create the panel.
	 */
	public SelectModelPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{159, 0, 0};
		gridBagLayout.rowHeights = new int[]{14, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		lblSelectYourConsoles = new JLabel("Select your console's model");
		lblSelectYourConsoles.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblSelectYourConsoles = new GridBagConstraints();
		gbc_lblSelectYourConsoles.gridwidth = 2;
		gbc_lblSelectYourConsoles.insets = new Insets(0, 0, 5, 0);
		gbc_lblSelectYourConsoles.gridx = 0;
		gbc_lblSelectYourConsoles.gridy = 0;
		add(lblSelectYourConsoles, gbc_lblSelectYourConsoles);
		btnNew3ds = new JButton("New 3DS"); // fallback text
		btnNew3ds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNew3ds_actionPerformed(e);
			}
		});
		MagicUtils.setImage(btnNew3ds, "data/images/new3ds.png");
		GridBagConstraints gbc_btnNew3ds = new GridBagConstraints();
		gbc_btnNew3ds.insets = new Insets(0, 0, 5, 5);
		gbc_btnNew3ds.gridx = 0;
		gbc_btnNew3ds.gridy = 1;
		add(btnNew3ds, gbc_btnNew3ds);
		
		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 1;
		add(panel, gbc_panel);
		
		btnOld3ds = new JButton("Old 3DS");
		btnOld3ds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnOld3ds_actionPerformed(e);
			}
		});
		MagicUtils.setImage(btnOld3ds, "data/images/old3ds.png");
		panel.add(btnOld3ds);
		
		btnOld3dsXl = new JButton("Old 3DS XL");
		btnOld3dsXl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnOld3dsXl_actionPerformed(e);
			}
		});
		MagicUtils.setImage(btnOld3dsXl, "data/images/old3dsxl.png");
		panel.add(btnOld3dsXl);
		
		btnNew3dsXl = new JButton("New 3DS XL");
		btnNew3dsXl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNew3dsXl_actionPerformed(e);
			}
		});
		MagicUtils.setImage(btnNew3dsXl, "data/images/new3dsxl.png");
		GridBagConstraints gbc_btnNew3dsXl = new GridBagConstraints();
		gbc_btnNew3dsXl.insets = new Insets(0, 0, 5, 5);
		gbc_btnNew3dsXl.gridx = 0;
		gbc_btnNew3dsXl.gridy = 2;
		add(btnNew3dsXl, gbc_btnNew3dsXl);
		
		btn2ds = new JButton("2DS");
		btn2ds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btn2ds_actionPerformed(e);
			}
		});
		MagicUtils.setImage(btn2ds, "data/images/2ds.png");
		GridBagConstraints gbc_btn2ds = new GridBagConstraints();
		gbc_btn2ds.insets = new Insets(0, 0, 5, 0);
		gbc_btn2ds.gridx = 1;
		gbc_btn2ds.gridy = 2;
		add(btn2ds, gbc_btn2ds);
		
		panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.gridwidth = 2;
		gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 3;
		add(panel_1, gbc_panel_1);
		
		lblYouHaveSelected = new JLabel("You have selected: ");
		panel_1.add(lblYouHaveSelected);
		lblYouHaveSelected.setFont(new Font("Arial", Font.BOLD, 14));
		
		lblSelectedmodel = new JLabel("selectedModel");
		panel_1.add(lblSelectedmodel);
		lblSelectedmodel.setFont(new Font("Arial", Font.BOLD, 14));
		lblSelectedmodel.setVisible(false);
		lblYouHaveSelected.setVisible(false);
		
	}

	protected void btnNew3ds_actionPerformed(ActionEvent e) {
		modelSelected(ConsoleModel.N3DS.modelType(), ConsoleModel.N3DS.displayName());
	}
	protected void btnNew3dsXl_actionPerformed(ActionEvent e) {
		modelSelected(ConsoleModel.N3DSXL.modelType(), ConsoleModel.N3DSXL.displayName());
	}
	protected void btnOld3ds_actionPerformed(ActionEvent e) {
		modelSelected(ConsoleModel.O3DS.modelType(), ConsoleModel.O3DS.displayName());
	}
	protected void btnOld3dsXl_actionPerformed(ActionEvent e) {
		modelSelected(ConsoleModel.O3DSXL.modelType(), ConsoleModel.O3DSXL.displayName());
	}
	protected void btn2ds_actionPerformed(ActionEvent e) {
		modelSelected(ConsoleModel.O2DS.modelType(), ConsoleModel.O2DS.displayName());
	}

	private void modelSelected(String model, String display) {
		lblYouHaveSelected.setVisible(true);
		lblSelectedmodel.setVisible(true);
		lblSelectedmodel.setText(display);
		if(caller != null) {
			caller.receiveData(model, MagicActions.MODEL_SELECTED);
		}
	}
}
