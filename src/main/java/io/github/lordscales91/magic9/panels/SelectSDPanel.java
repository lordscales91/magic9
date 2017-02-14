package io.github.lordscales91.magic9.panels;

import io.github.lordscales91.magic9.HackingPath;
import io.github.lordscales91.magic9.constants.MagicActions;
import io.github.lordscales91.magic9.constants.MagicConstants;
import io.github.lordscales91.magic9.core.CallbackReceiver;
import io.github.lordscales91.magic9.core.MagicUtils;
import io.github.lordscales91.magic9.widget.LinkLabel;
import io.github.lordscales91.magic9.workers.BackupWorker;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JProgressBar;

@SuppressWarnings("serial")
public class SelectSDPanel extends JPanel implements CallbackReceiver {
	private static final String BACKUP_SD = "Backup SD";
	private JLabel lblBeforeContinuing;
	private JLabel lblItShouldBe;
	private LinkLabel lblSDCheckerLink;
	private JLabel lblItsRecommendedTo;
	private JPanel panel;
	private JLabel lblSelectThePath;
	private JTextField txtSelectSD;
	private JButton btnSelectSD;
	private CallbackReceiver caller;
	private JPanel sdBackupPanel;
	private JLabel lblSelectDirectoryFor;
	private JTextField txtSdBackupDir;
	private JButton btnSelectSDBackupDir;
	private JButton btnBackup;
	private JProgressBar pbBackupSD;
	
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
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
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
		gbc_panel.insets = new Insets(0, 0, 5, 0);
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
		
		sdBackupPanel = new JPanel();
		sdBackupPanel.setVisible(false);
		GridBagConstraints gbc_sdBackupPanel = new GridBagConstraints();
		gbc_sdBackupPanel.insets = new Insets(0, 0, 5, 0);
		gbc_sdBackupPanel.fill = GridBagConstraints.BOTH;
		gbc_sdBackupPanel.gridx = 0;
		gbc_sdBackupPanel.gridy = 5;
		add(sdBackupPanel, gbc_sdBackupPanel);
		
		lblSelectDirectoryFor = new JLabel("Select directory for SD contents backup");
		lblSelectDirectoryFor.setFont(new Font("Arial", Font.PLAIN, 12));
		sdBackupPanel.add(lblSelectDirectoryFor);
		
		txtSdBackupDir = new JTextField();
		txtSdBackupDir.setEditable(false);
		sdBackupPanel.add(txtSdBackupDir);
		txtSdBackupDir.setColumns(15);
		
		btnSelectSDBackupDir = new JButton("...");
		btnSelectSDBackupDir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSelectSDBackupDir_actionPerformed(e);
			}
		});
		sdBackupPanel.add(btnSelectSDBackupDir);
		
		btnBackup = new JButton("Backup");
		btnBackup.setEnabled(false);
		btnBackup.setVisible(false);
		btnBackup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnBackup_actionPerformed(e);
			}
		});
		
		pbBackupSD = new JProgressBar();
		pbBackupSD.setVisible(false);
		GridBagConstraints gbc_pbBackupSD = new GridBagConstraints();
		gbc_pbBackupSD.insets = new Insets(0, 0, 5, 0);
		gbc_pbBackupSD.gridx = 0;
		gbc_pbBackupSD.gridy = 6;
		add(pbBackupSD, gbc_pbBackupSD);
		GridBagConstraints gbc_btnBackup = new GridBagConstraints();
		gbc_btnBackup.gridx = 0;
		gbc_btnBackup.gridy = 7;
		add(btnBackup, gbc_btnBackup);

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

	protected void btnSelectSDBackupDir_actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int op = chooser.showOpenDialog(getParent());
		if(op == JFileChooser.APPROVE_OPTION) {
			txtSdBackupDir.setText(chooser.getSelectedFile().getAbsolutePath());
			btnBackup.setEnabled(true);
		}
	}

	protected void btnBackup_actionPerformed(ActionEvent e) {
		File sd = new File(txtSelectSD.getText());
		File backupDir = new File(txtSdBackupDir.getText());
		pbBackupSD.setVisible(true);
		pbBackupSD.setStringPainted(true);
		pbBackupSD.setString(String.format("%.02f%%", 0.0f));
		btnBackup.setEnabled(false);
		BackupWorker worker = new BackupWorker(BACKUP_SD, this, backupDir, sd);
		worker.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getPropertyName().equals("real_progress")) {
					updateProgress(BACKUP_SD, (float)evt.getNewValue());
				}
			}
		});
		worker.execute();
	}

	protected void updateProgress(String tag, float progress) {
		int iProgress = (int) progress;
		pbBackupSD.setValue((iProgress>100)?100:iProgress);
		pbBackupSD.setString(String.format("%.02f%%", progress));
	}

	private void checkSD(File sd) {		
		if(sd.getTotalSpace() < MagicConstants.SD_MIN_SIZE) {
			JOptionPane.showMessageDialog(getParent(), "Sorry, your SD is too small.\n"
					+ "You should go and purchase an SD of 4GB or more", 
					"Error",JOptionPane.ERROR_MESSAGE);
		} else if(sd.list().length > 0) {
			JOptionPane.showMessageDialog(getParent(), "It seems the SD Card is not empty.\n"
					+ "Please select a directory to backup its contents.");
			sdBackupPanel.setVisible(true);
			btnBackup.setVisible(true);
		} else {
			HackingPath.getInstance().setSdCardDir(sd.getAbsolutePath());
			if(caller != null) {
				caller.receiveData(MagicActions.FILE_SELECTED, MagicActions.SD_CARD_SELECTED);
			}
		}
	}

	@Override
	public void receiveData(Object data, String tag) {
		if(data instanceof Exception) {
			JOptionPane.showMessageDialog(getParent(), ((Exception)data).getMessage(), 
					"Error", JOptionPane.ERROR_MESSAGE);
		} else {
			
			URI target = new File(txtSdBackupDir.getText()).toURI();
			try {
				Desktop.getDesktop().browse(target);
			} catch (IOException e) {}
			int op = JOptionPane.showConfirmDialog(getParent(), "Please, "
					+ "confirm that the contents have been safely backed up.");
			if(op == JOptionPane.YES_OPTION) {
				File sd = new File(txtSelectSD.getText());
				try {					
					MagicUtils.clearDirectory(sd);
				} catch (IOException e) {}
				HackingPath.getInstance().setSdCardDir(sd.getAbsolutePath());
				if(caller != null) {
					caller.receiveData(MagicActions.FILE_SELECTED, MagicActions.SD_CARD_SELECTED);
				}
			}
			
		}
	}
}
