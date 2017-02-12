package io.github.lordscales91.magic9;

import io.github.lordscales91.magic9.constants.MagicPropKeys;
import io.github.lordscales91.magic9.core.CallbackReceiver;
import io.github.lordscales91.magic9.core.HackingProcess;
import io.github.lordscales91.magic9.core.MagicUtils;
import io.github.lordscales91.magic9.domain.HackingStep;
import io.github.lordscales91.magic9.widget.LinkLabel;
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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class HackingWizard extends JFrame implements CallbackReceiver {

	private static final String SD_PROCESS = "SD Process";
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
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
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
		GridBagConstraints gbc_btnProceed = new GridBagConstraints();
		gbc_btnProceed.insets = new Insets(0, 0, 5, 0);
		gbc_btnProceed.gridx = 0;
		gbc_btnProceed.gridy = 3;
		contentPane.add(btnProceed, gbc_btnProceed);
		
		pbProcess = new JProgressBar();
		GridBagConstraints gbc_pbProcess = new GridBagConstraints();
		gbc_pbProcess.insets = new Insets(0, 0, 5, 0);
		gbc_pbProcess.gridx = 0;
		gbc_pbProcess.gridy = 4;
		contentPane.add(pbProcess, gbc_pbProcess);
		
		lblStepInfo = new JLabel("Now continue on step X in guide. Once finished press confirm");
		lblStepInfo.setFont(new Font("Arial", Font.BOLD, 12));
		lblStepInfo.setVisible(false);
		GridBagConstraints gbc_lblStepInfo = new GridBagConstraints();
		gbc_lblStepInfo.insets = new Insets(0, 0, 5, 0);
		gbc_lblStepInfo.gridx = 0;
		gbc_lblStepInfo.gridy = 5;
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
		gbc_lblFinalMessage.gridy = 6;
		contentPane.add(lblFinalMessage, gbc_lblFinalMessage);
		btnConfirm.setEnabled(false);
		GridBagConstraints gbc_btnConfirm = new GridBagConstraints();
		gbc_btnConfirm.insets = new Insets(0, 0, 5, 0);
		gbc_btnConfirm.gridx = 0;
		gbc_btnConfirm.gridy = 7;
		contentPane.add(btnConfirm, gbc_btnConfirm);
		
		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.SOUTH;
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 8;
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
		btnCancel.setEnabled(hp.getProcess().isSafeToPause());
	}

	private void updateStep() {
		btnProceed.setEnabled(true);
		pbProcess.setValue(0);
		pbProcess.setStringPainted(false);		
		HackingStep step = HackingPath.getInstance().getCurrentStep();		
		lblStepInfo.setText(MagicUtils.getMessageForStep(step));
		lblStepInfo.setVisible(false);
		lblHackingStepName.setText(step.displayName());
		lblInstructionsLink.setText("Click here to access the guide");
		lblInstructionsLink.setTarget(HackingPath.URLS.getProperty(MagicPropKeys.$3DS_GUIDE)+step.getURLPart());
		lblInstructionsLink.init();
	}

	protected void btnProceed_actionPerformed(ActionEvent e) {
		btnProceed.setEnabled(false);
		pbProcess.setIndeterminate(true);
		HackingProcess proc = HackingPath.getInstance().getProcess();
		// TODO: Handle SD availability issues (maybe is not connected or the path has changed)
		HackingWorker worker = new HackingWorker(proc, SD_PROCESS, this);
		worker.execute();		
	}
	protected void this_windowOpened(WindowEvent e) {
		updateStep();
		btnProceed.requestFocusInWindow();
	}
	protected void btnConfirm_actionPerformed(ActionEvent e) {
		int op = JOptionPane.showConfirmDialog(this, "Are you sure you have finished all "+ lblHackingStepName.getText()+" steps?");
		if(op == JOptionPane.YES_OPTION) {
			updateButtonsState();
			btnConfirm.setEnabled(false);
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
			if(HackingStep.INSTALL_ARM9LOADERHAX.equals(HackingPath.getInstance().getCurrentStep())) {
				lblFinalMessage.setVisible(true);
			}
			lblStepInfo.setVisible(true);
		}
	}
}
