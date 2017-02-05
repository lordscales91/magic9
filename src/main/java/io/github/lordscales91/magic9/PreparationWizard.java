package io.github.lordscales91.magic9;

import io.github.lordscales91.magic9.core.CallbackReceiver;
import io.github.lordscales91.magic9.domain.ConsoleModel;
import io.github.lordscales91.magic9.domain.ConsoleRegion;
import io.github.lordscales91.magic9.domain.FirmwareVersion;
import io.github.lordscales91.magic9.panels.DownloadPanel;
import io.github.lordscales91.magic9.panels.SelectFirmwarePanel;
import io.github.lordscales91.magic9.panels.SelectModelPanel;
import io.github.lordscales91.magic9.panels.SelectRegionPanel;
import io.github.lordscales91.magic9.panels.SelectSDPanel;

import java.awt.EventQueue;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagConstraints;
import java.awt.CardLayout;
import java.awt.Insets;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class PreparationWizard extends JFrame implements CallbackReceiver {

	private static final String SELECT_MODEL = "SelectModelPane";
	private static final String SELECT_REGION = "SelectRegionPane";	
	private static final String SELECT_FIRMWARE = "SelectFirmwarePane";
	private static final String DOWNLOAD_PANEL = "DownloadPane";
	private static final String SELECT_SD = "SelectSDPane";
	private static final String[] PANELS = new String[]{SELECT_MODEL, SELECT_REGION,
								SELECT_FIRMWARE, SELECT_SD, DOWNLOAD_PANEL};
	
	private JPanel contentPane;
	private JPanel cards;
	private JPanel navigation;
	private JButton btnPrevious;
	private JButton btnNext;
	private JButton btnCancel;
	private String model;
	private char region;
	private int currentPanel;

	/**
	 * Launch the wizard.
	 */
	public static void showFrame() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PreparationWizard frame = new PreparationWizard();
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
	public PreparationWizard() {
		setTitle("Magic9 Tool - Hacking Wizard");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		// contentPane.add(new SelectModelPanel());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		cards = new JPanel();		
		GridBagConstraints gbc_cards = new GridBagConstraints();
		gbc_cards.insets = new Insets(0, 0, 5, 0);
		gbc_cards.fill = GridBagConstraints.BOTH;
		gbc_cards.gridx = 0;
		gbc_cards.gridy = 0;
		contentPane.add(cards, gbc_cards);
		cards.setLayout(new CardLayout(0, 0));
		SelectModelPanel selectModelPanel = new SelectModelPanel(this);
		cards.add(selectModelPanel, SELECT_MODEL);
		SelectRegionPanel selectRegion = new SelectRegionPanel(this);
		cards.add(selectRegion, SELECT_REGION);
		SelectFirmwarePanel selectFw = new SelectFirmwarePanel(this);
		cards.add(selectFw, SELECT_FIRMWARE);
		DownloadPanel dlpanel = new DownloadPanel(this);
		cards.add(dlpanel, DOWNLOAD_PANEL);
		cards.add(new SelectSDPanel(this), SELECT_SD);
		
		navigation = new JPanel();
		GridBagConstraints gbc_navigation = new GridBagConstraints();
		gbc_navigation.fill = GridBagConstraints.HORIZONTAL;
		gbc_navigation.gridx = 0;
		gbc_navigation.gridy = 1;
		contentPane.add(navigation, gbc_navigation);
		
		btnPrevious = new JButton("Previous");
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPrevious_actionPerformed(e);
			}
		});
		btnPrevious.setEnabled(false);
		navigation.add(btnPrevious);
		
		btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNext_actionPerformed(e);
			}
		});
		btnNext.setEnabled(false);
		navigation.add(btnNext);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCancel_actionPerformed(e);
			}
		});
		navigation.add(btnCancel);
	}

	protected void btnNext_actionPerformed(ActionEvent e) {
		currentPanel++;
		if(currentPanel >= PANELS.length) {
			currentPanel = PANELS.length - 1;
			// TODO: Move to next wizard
			JOptionPane.showMessageDialog(this, "Sorry! Not yet implemented");
		} else {			
			showPanel();
		}
		
	}

	protected void btnPrevious_actionPerformed(ActionEvent e) {
		currentPanel--;
		showPanel();
	}
	
	protected void btnCancel_actionPerformed(ActionEvent e) {
		int op = JOptionPane.showConfirmDialog(this, "Are you sure?", "Cancel confirmation", JOptionPane.YES_NO_OPTION);
		if(op == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	private void showPanel() {
		showPanel(PANELS[currentPanel]);
	}

	private void showPanel(String panel) {
		CardLayout cl = (CardLayout)cards.getLayout();
		cl.show(cards, panel);
		updateButtonsState();
		btnNext.setEnabled(false); // Always disable next button until getting a response
	}

	private void updateButtonsState() {
		btnPrevious.setEnabled(currentPanel > 0);
		btnNext.setEnabled(currentPanel < PANELS.length);
	}

	private boolean wasUpdated() {
		int op = JOptionPane.showConfirmDialog(this, "Have you updated the console with a game cartridge?");
		return op == JOptionPane.YES_OPTION;
	}

	@Override
	public void receiveData(Object data, String tag) {
		if(data instanceof Exception) {
			JOptionPane.showMessageDialog(this, ((Exception)data).getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			updateButtonsState();
			if(MagicActions.MODEL_SELECTED.equals(tag)) {
				this.model = data.toString();
			} else if(MagicActions.REGION_SELECTED.equals(tag)) {
				this.region = (char) data;
			} else if(MagicActions.FIRMWARE_SELECTED.equals(tag)) {
				if(MagicActions.FIRMWARE_CHANGED.equals(data)) {
					// User touched the combo boxes
					btnNext.setEnabled(false);
				} else {
					// Receive the firmware
					FirmwareVersion fw = (FirmwareVersion) data;
					// Fill the missing data
					fw.setModel(ConsoleModel.fromModelType(model));
					fw.setRegion(ConsoleRegion.fromFirmware(region));
					// Init the hacking path already
					boolean cartUpdated = wasUpdated();
					HackingPath.resolve(fw, cartUpdated);
				}
			} else if(MagicActions.DOWNLOADS_STATUS_CHANGED.equals(tag)) {
				if(MagicActions.DOWNLOADS_STARTED.equals(data)) {
					btnNext.setEnabled(false);
					btnPrevious.setEnabled(false);
				} else if(MagicActions.DOWNLOADS_FINISHED.equals(data)) {
					btnNext.setEnabled(true);
				}
			} else if(MagicActions.SD_CARD_SELECTED.equals(tag)) {
				btnNext.setEnabled(true);
			}
		}
	}
}
