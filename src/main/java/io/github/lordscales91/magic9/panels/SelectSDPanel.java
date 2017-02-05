package io.github.lordscales91.magic9.panels;

import io.github.lordscales91.magic9.HackingPath;
import io.github.lordscales91.magic9.constants.MagicActions;
import io.github.lordscales91.magic9.constants.MagicConstants;
import io.github.lordscales91.magic9.core.CallbackReceiver;
import io.github.lordscales91.magic9.core.MagicUtils;
import io.github.lordscales91.magic9.widget.LinkLabel;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class SelectSDPanel extends JPanel {
	private JLabel lblBeforeContinuing;
	private JLabel lblItShouldBe;
	private LinkLabel lblSDCheckerLink;
	private JLabel lblItsRecommendedTo;
	private JPanel panel;
	private JLabel lblSelectThePath;
	private JTextField txtSelectSD;
	private JButton btnSelectSD;
	private CallbackReceiver caller;
	
	public SelectSDPanel(CallbackReceiver caller) {
		this();
		this.caller = caller;
	}

	/**
	 * Create the panel.
	 */
	public SelectSDPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		lblBeforeContinuing = new JLabel("Before continuing connect your SD card");
		lblBeforeContinuing.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblBeforeContinuing = new GridBagConstraints();
		gbc_lblBeforeContinuing.insets = new Insets(0, 0, 5, 0);
		gbc_lblBeforeContinuing.gridx = 0;
		gbc_lblBeforeContinuing.gridy = 0;
		add(lblBeforeContinuing, gbc_lblBeforeContinuing);
		
		lblItShouldBe = new JLabel("It should have a capacity of 4GB or more and in FAT32 format");
		lblItShouldBe.setFont(new Font("Arial", Font.BOLD, 12));
		GridBagConstraints gbc_lblItShouldBe = new GridBagConstraints();
		gbc_lblItShouldBe.insets = new Insets(0, 0, 5, 0);
		gbc_lblItShouldBe.gridx = 0;
		gbc_lblItShouldBe.gridy = 1;
		add(lblItShouldBe, gbc_lblItShouldBe);
		
		lblSDCheckerLink = new LinkLabel(MagicUtils.getSDCheckURL(), "Instructions for " + MagicUtils.getSDChecker());
		lblSDCheckerLink.setFont(new Font("Arial", Font.BOLD, 12));
		lblSDCheckerLink.init();
		
		lblItsRecommendedTo = new JLabel("it's recommended to perform a test for errors");
		lblItsRecommendedTo.setFont(new Font("Arial", Font.BOLD, 12));
		GridBagConstraints gbc_lblItsRecommendedTo = new GridBagConstraints();
		gbc_lblItsRecommendedTo.insets = new Insets(0, 0, 5, 0);
		gbc_lblItsRecommendedTo.gridx = 0;
		gbc_lblItsRecommendedTo.gridy = 2;
		add(lblItsRecommendedTo, gbc_lblItsRecommendedTo);
		GridBagConstraints gbc_lblSDCheckerLink = new GridBagConstraints();
		gbc_lblSDCheckerLink.insets = new Insets(0, 0, 5, 0);
		gbc_lblSDCheckerLink.gridx = 0;
		gbc_lblSDCheckerLink.gridy = 3;
		add(lblSDCheckerLink, gbc_lblSDCheckerLink);
		
		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 4;
		add(panel, gbc_panel);
		
		lblSelectThePath = new JLabel("Select the path to your SD root");
		lblSelectThePath.setFont(new Font("Arial", Font.PLAIN, 12));
		panel.add(lblSelectThePath);
		
		txtSelectSD = new JTextField();
		txtSelectSD.setEditable(false);
		panel.add(txtSelectSD);
		txtSelectSD.setColumns(15);
		
		btnSelectSD = new JButton("...");
		btnSelectSD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSelectSD_actionPerformed(e);
			}
		});
		panel.add(btnSelectSD);

	}

	protected void btnSelectSD_actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int op = chooser.showOpenDialog(getParent());
		if(op == JFileChooser.APPROVE_OPTION) {
			txtSelectSD.setText(chooser.getSelectedFile().getAbsolutePath());
			checkSD(chooser.getSelectedFile());
		}
	}

	private void checkSD(File sd) {		
		if(sd.getTotalSpace() < MagicConstants.SD_MIN_SIZE) {
			JOptionPane.showMessageDialog(getParent(), "Sorry, your SD is too small.\n"
					+ "You should go and purchase an SD of 4GB or more", 
					"Error",JOptionPane.ERROR_MESSAGE);
		} else {
			HackingPath.getInstance().setSdCardDir(sd.getAbsolutePath());
			if(caller != null) {
				caller.receiveData(MagicActions.FILE_SELECTED, MagicActions.SD_CARD_SELECTED);
			}
		}
	}
}
