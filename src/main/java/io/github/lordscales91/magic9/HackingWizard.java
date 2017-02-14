package io.github.lordscales91.magic9;

import io.github.lordscales91.magic9.constants.MagicPropKeys;
import io.github.lordscales91.magic9.core.CallbackReceiver;
import io.github.lordscales91.magic9.core.HackingProcess;
import io.github.lordscales91.magic9.core.MagicUtils;
import io.github.lordscales91.magic9.domain.HackingStep;
import io.github.lordscales91.magic9.widget.LinkLabel;
import io.github.lordscales91.magic9.workers.BackupWorker;
import io.github.lordscales91.magic9.workers.ChecksumWorker;
import io.github.lordscales91.magic9.workers.HackingWorker;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class HackingWizard extends JFrame implements CallbackReceiver {

	private static final String SD_PROCESS = "SD Process";
	private static final String NAND_BACKUP = "NAND Backup";
	private static final String COMPUTE_NAND_CHECKSUM = "Compute Checksum";
	private JPanel contentPane;
	private JLabel lblHackingStepName;
	private LinkLabel lblInstructionsLink;
	private JButton btnProceed;
	private JProgressBar pbProcess;
	private JPanel panel;
	private JButton btnNext;
	private JButton btnCancel;
	private JLabel lblEnsureThatYou;
	private JLabel lblStepInfo;
	private JButton btnConfirm;
	private JLabel lblFinalMessage;
	private JLabel lblPreInfoStep;
	private File NANDBackupSha;
	private File NANDbackup;

	/**
	 * Launch the application.
	 */
	public static void showFrame() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HackingWizard frame = new HackingWizard();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public HackingWizard() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				this_windowOpened(e);
			}
		});
		setTitle("Magic9 Tool - Hacking Phase");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		lblHackingStepName = new JLabel("Hacking Step Name");
		lblHackingStepName.setFont(new Font("Arial", Font.BOLD, 20));
		GridBagConstraints gbc_lblHackingStepName = new GridBagConstraints();
		gbc_lblHackingStepName.insets = new Insets(0, 0, 5, 0);
		gbc_lblHackingStepName.gridx = 0;
		gbc_lblHackingStepName.gridy = 0;
		contentPane.add(lblHackingStepName, gbc_lblHackingStepName);
		
		lblInstructionsLink = new LinkLabel();
		lblInstructionsLink.setFont(new Font("Arial", Font.BOLD, 12));
		GridBagConstraints gbc_lblInstructionsLink = new GridBagConstraints();
		gbc_lblInstructionsLink.insets = new Insets(0, 0, 5, 0);
		gbc_lblInstructionsLink.gridx = 0;
		gbc_lblInstructionsLink.gridy = 1;
		contentPane.add(lblInstructionsLink, gbc_lblInstructionsLink);
		
		btnProceed = new JButton("Proceed");
		btnProceed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnProceed_actionPerformed(e);
			}
		});
		
		lblEnsureThatYou = new JLabel("Ensure that you have the SD card connected to the PC. Then, click on Proceed");
		lblEnsureThatYou.setFont(new Font("Arial", Font.BOLD, 12));
		GridBagConstraints gbc_lblEnsureThatYou = new GridBagConstraints();
		gbc_lblEnsureThatYou.insets = new Insets(0, 0, 5, 0);
		gbc_lblEnsureThatYou.gridx = 0;
		gbc_lblEnsureThatYou.gridy = 2;
		contentPane.add(lblEnsureThatYou, gbc_lblEnsureThatYou);
		
		lblPreInfoStep = new JLabel("For this step, first do sth");
		lblPreInfoStep.setFont(new Font("Arial", Font.BOLD, 12));
		GridBagConstraints gbc_lblPreInfoStep = new GridBagConstraints();
		gbc_lblPreInfoStep.insets = new Insets(0, 0, 5, 0);
		gbc_lblPreInfoStep.gridx = 0;
		gbc_lblPreInfoStep.gridy = 3;
		contentPane.add(lblPreInfoStep, gbc_lblPreInfoStep);
		GridBagConstraints gbc_btnProceed = new GridBagConstraints();
		gbc_btnProceed.insets = new Insets(0, 0, 5, 0);
		gbc_btnProceed.gridx = 0;
		gbc_btnProceed.gridy = 4;
		contentPane.add(btnProceed, gbc_btnProceed);
		
		pbProcess = new JProgressBar();
		GridBagConstraints gbc_pbProcess = new GridBagConstraints();
		gbc_pbProcess.insets = new Insets(0, 0, 5, 0);
		gbc_pbProcess.gridx = 0;
		gbc_pbProcess.gridy = 5;
		contentPane.add(pbProcess, gbc_pbProcess);
		
		lblStepInfo = new JLabel("Now continue on step X in guide. Once finished press confirm");
		lblStepInfo.setFont(new Font("Arial", Font.BOLD, 12));
		lblStepInfo.setVisible(false);
		GridBagConstraints gbc_lblStepInfo = new GridBagConstraints();
		gbc_lblStepInfo.insets = new Insets(0, 0, 5, 0);
		gbc_lblStepInfo.gridx = 0;
		gbc_lblStepInfo.gridy = 6;
		contentPane.add(lblStepInfo, gbc_lblStepInfo);
		
		btnConfirm = new JButton("Confirm");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnConfirm_actionPerformed(e);
			}
		});
		
		lblFinalMessage = new JLabel("Just follow the instructions and everything should be fine");
		lblFinalMessage.setFont(new Font("Arial", Font.BOLD, 12));
		lblFinalMessage.setVisible(false);
		GridBagConstraints gbc_lblFinalMessage = new GridBagConstraints();
		gbc_lblFinalMessage.insets = new Insets(0, 0, 5, 0);
		gbc_lblFinalMessage.gridx = 0;
		gbc_lblFinalMessage.gridy = 7;
		contentPane.add(lblFinalMessage, gbc_lblFinalMessage);
		btnConfirm.setEnabled(false);
		GridBagConstraints gbc_btnConfirm = new GridBagConstraints();
		gbc_btnConfirm.insets = new Insets(0, 0, 5, 0);
		gbc_btnConfirm.gridx = 0;
		gbc_btnConfirm.gridy = 8;
		contentPane.add(btnConfirm, gbc_btnConfirm);
		
		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.SOUTH;
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 9;
		contentPane.add(panel, gbc_panel);
		
		btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNext_actionPerformed(e);
			}
		});
		btnNext.setEnabled(false);
		panel.add(btnNext);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setEnabled(false);
		panel.add(btnCancel);
	}

	private void updateButtonsState() {
		HackingPath hp = HackingPath.getInstance();
		btnNext.setEnabled(hp.hasNext());
		boolean enabled = HackingStep.NAND_BACKUP.equals(hp.getCurrentStep())
				|| hp.getProcess().isSafeToPause();
		btnCancel.setEnabled(enabled);
	}

	private void updateStep() {
		HackingStep step = HackingPath.getInstance().getCurrentStep();
		btnProceed.setEnabled(true);
		pbProcess.setValue(0);
		boolean isNANDBackup = HackingStep.NAND_BACKUP.equals(step);
		pbProcess.setStringPainted(isNANDBackup);
		String message = MagicUtils.getMessageForStep(step);
		String preMessage = MagicUtils.getPreMessageForStep(step);
		if(preMessage != null) {
			lblPreInfoStep.setText(preMessage);
			lblPreInfoStep.setVisible(true);
		} else  {
			lblPreInfoStep.setVisible(false);
		}
		if(message != null) {
			lblStepInfo.setText(message);
		}		
		lblStepInfo.setVisible(false);
		lblHackingStepName.setText(step.displayName());
		lblInstructionsLink.setText("Click here to access the guide");
		if(isNANDBackup) {
			lblInstructionsLink.setTarget(HackingPath.URLS.getProperty(MagicPropKeys.NAND_BACKUP_GUIDE));
		} else {
			lblInstructionsLink.setTarget(HackingPath.URLS.getProperty(MagicPropKeys.$3DS_GUIDE)+step.getURLPart());
		}
		
		lblInstructionsLink.init();
	}

	protected void updateProgress(String tag, float progress) {
		int iProgress = (int) progress;
		pbProcess.setValue((iProgress>100)?100:iProgress);
		pbProcess.setString(String.format("%.02f%%", progress));
	}

	protected void btnProceed_actionPerformed(ActionEvent e) {
		if(HackingStep.NAND_BACKUP.equals(HackingPath.getInstance().getCurrentStep())) {
			JFileChooser chooser = new JFileChooser(HackingPath.getInstance().getSdCardDir());
			chooser.setDialogTitle("Please, select the NAND Backup");
			chooser.setFileFilter(new FileNameExtensionFilter("NAND Backup (*.bin)", "bin"));
			int op = chooser.showOpenDialog(this);
			if(op == JFileChooser.APPROVE_OPTION) {
				NANDbackup = chooser.getSelectedFile();
				NANDBackupSha = new File(NANDbackup.getParentFile(), NANDbackup.getName()+".sha");
				File backupDir = new File(HackingPath.getInstance().getHackingDir(), "backups");
				if(!backupDir.exists()) {
					backupDir.mkdirs();
				}
				BackupWorker worker = new BackupWorker(NAND_BACKUP, this, backupDir, NANDbackup, NANDBackupSha);
				worker.addPropertyChangeListener(new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if(evt.getPropertyName().equals("real_progress")) {
							updateProgress(NAND_BACKUP, (float)evt.getNewValue());
						}
					}
				});
				worker.execute();
				btnProceed.setEnabled(false);
				pbProcess.setStringPainted(true);
			} else {
				JOptionPane.showMessageDialog(this, "No file choosed. Aborting", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		} else {
			HackingProcess proc = HackingPath.getInstance().getProcess();
			// TODO: Handle SD availability issues (maybe is not connected or the path has changed)
			HackingWorker worker = new HackingWorker(proc, SD_PROCESS, this);
			worker.execute();
			btnProceed.setEnabled(false);
			pbProcess.setIndeterminate(true);
		}
				
	}
	protected void this_windowOpened(WindowEvent e) {
		updateStep();
		btnProceed.requestFocusInWindow();
	}
	protected void btnConfirm_actionPerformed(ActionEvent e) {
		if(HackingStep.NAND_BACKUP.equals(HackingPath.getInstance().getCurrentStep())) {
			File backupDir = new File(HackingPath.getInstance().getHackingDir(), "backups");
			pbProcess.setValue(0);
			ChecksumWorker worker = new ChecksumWorker(new File(backupDir, NANDbackup.getName()), 
					COMPUTE_NAND_CHECKSUM, this);
			worker.addPropertyChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if(evt.getPropertyName().equals("real_progress")) {
						updateProgress(COMPUTE_NAND_CHECKSUM, (float) evt.getNewValue());
					}
				}
			});
			btnConfirm.setEnabled(false);
			worker.execute();
		} else  {
			String message = "Are you sure that you have finished all "+ lblHackingStepName.getText()+" steps?";
			if(HackingStep.INSTALL_ARM9LOADERHAX.equals(HackingPath.getInstance().getCurrentStep())) {
				message = "Does it really worked?\n"
						+ "Wonderful! Thanks for using this tool (^_^)";
			}
			int op = JOptionPane.showConfirmDialog(this, message);
			if(op == JOptionPane.YES_OPTION) {
				updateButtonsState();
				btnConfirm.setEnabled(false);
			}
		}		
	}

	protected void btnNext_actionPerformed(ActionEvent e) {
		HackingPath.getInstance().proceedToNext();
		updateStep();
		btnNext.setEnabled(false);
	}

	@Override
	public void receiveData(Object data, String tag) {
		if(data instanceof Exception) {
			JOptionPane.showMessageDialog(this, ((Exception)data).getMessage(), 
					"Error", JOptionPane.ERROR_MESSAGE);
			btnProceed.setEnabled(true); // Allow to retry
		} else {
			
			pbProcess.setIndeterminate(false);
			pbProcess.setStringPainted(true);
			pbProcess.setValue(100);		
			btnConfirm.setEnabled(true);
			HackingStep step = HackingPath.getInstance().getCurrentStep();
			if(HackingStep.INSTALL_ARM9LOADERHAX.equals(step)) {
				lblFinalMessage.setVisible(true);
			} else if(HackingStep.NAND_BACKUP.equals(step)) {
				lblStepInfo.setVisible(true);
				pbProcess.setString(null);
			}
			if(COMPUTE_NAND_CHECKSUM.equals(tag)) {				
				try {
					String digest = (String) data;
					String checksum = MagicUtils.extractChecksum(NANDBackupSha, 32);
					if(!checksum.isEmpty() && checksum.equals(digest)) {
						updateButtonsState();
					} else {
						JOptionPane.showMessageDialog(this, "The Backup seems invalid.\n"
								+ "You should delete it and create a new one", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (IOException e) {
					JOptionPane.showMessageDialog(this, "Problem verifying the NAND Backup", "Error", JOptionPane.ERROR_MESSAGE);
					btnConfirm.setEnabled(true);
				}
			}
			lblStepInfo.setVisible(true);
		}
	}
}
