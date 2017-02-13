package io.github.lordscales91.magic9.panels;

import io.github.lordscales91.magic9.constants.MagicActions;
import io.github.lordscales91.magic9.core.CallbackReceiver;
import io.github.lordscales91.magic9.core.MagicUtils;
import io.github.lordscales91.magic9.domain.ConsoleRegion;

import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class SelectRegionPanel extends JPanel {
	private JLabel lblSelectRegion;
	private CallbackReceiver caller;
	private JPanel panel;
	private JButton btnUsa;
	private JButton btnEur;
	private JButton btnJap;
	private JButton btnKor;
	private JPanel panel_1;
	private JLabel lblYouHaveSelected;
	private JLabel lblRegion;
	
	public SelectRegionPanel(CallbackReceiver caller) {
		this();
		this.caller = caller;		
	}

	/**
	 * Create the panel.
	 */
	public SelectRegionPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{123, 0};
		gridBagLayout.rowHeights = new int[]{17, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		lblSelectRegion = new JLabel("Select your console's Region");
		lblSelectRegion.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblSelectRegion = new GridBagConstraints();
		gbc_lblSelectRegion.insets = new Insets(0, 0, 5, 0);
		gbc_lblSelectRegion.anchor = GridBagConstraints.NORTH;
		gbc_lblSelectRegion.gridx = 0;
		gbc_lblSelectRegion.gridy = 0;
		add(lblSelectRegion, gbc_lblSelectRegion);
		
		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		add(panel, gbc_panel);
		
		btnUsa = new JButton("USA");
		btnUsa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnUsa_actionPerformed(e);
			}
		});
		panel.add(btnUsa);
		
		btnEur = new JButton("EUR");
		btnEur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnEur_actionPerformed(e);
			}
		});
		panel.add(btnEur);
		
		btnJap = new JButton("JAP");
		btnJap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnJap_actionPerformed(e);
			}
		});
		panel.add(btnJap);
		
		btnKor = new JButton("KOR");
		btnKor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnKor_actionPerformed(e);
			}
		});
		panel.add(btnKor);
		
		MagicUtils.setImage(btnEur, "data/images/EUR_flag.png");
		MagicUtils.setImage(btnUsa, "data/images/USA_flag.png");
		MagicUtils.setImage(btnJap, "data/images/JAP_flag.png");
		MagicUtils.setImage(btnKor, "data/images/KOR_flag.png");
		
		panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 2;
		add(panel_1, gbc_panel_1);
		
		lblYouHaveSelected = new JLabel("You have selected:");
		lblYouHaveSelected.setFont(new Font("Arial", Font.BOLD, 14));
		lblYouHaveSelected.setVisible(false);
		panel_1.add(lblYouHaveSelected);
		
		lblRegion = new JLabel("region");
		lblRegion.setFont(new Font("Arial", Font.BOLD, 14));
		lblRegion.setVisible(false);
		panel_1.add(lblRegion);

	}

	protected void btnUsa_actionPerformed(ActionEvent e) {
		regionSelected(ConsoleRegion.USA.toLetter(), ConsoleRegion.USA.displayName());
	}
	protected void btnEur_actionPerformed(ActionEvent e) {
		regionSelected(ConsoleRegion.EUR.toLetter(), ConsoleRegion.EUR.displayName());
	}
	protected void btnJap_actionPerformed(ActionEvent e) {
		regionSelected(ConsoleRegion.JPN.toLetter(), ConsoleRegion.JPN.displayName());
	}
	protected void btnKor_actionPerformed(ActionEvent e) {
		regionSelected(ConsoleRegion.KOR.toLetter(), ConsoleRegion.KOR.displayName());
	}

	private void regionSelected(char region, String display) {
		lblYouHaveSelected.setVisible(true);
		lblRegion.setVisible(true);
		lblRegion.setText(display);
		if(caller != null) {
			caller.receiveData(region, MagicActions.REGION_SELECTED);
		}
	}
}
