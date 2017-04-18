package io.github.lordscales91.magic9.panels;

import io.github.lordscales91.magic9.constants.MagicActions;
import io.github.lordscales91.magic9.core.CallbackReceiver;
import io.github.lordscales91.magic9.domain.FirmwareVersion;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

@SuppressWarnings("serial")
public class SelectFirmwarePanel extends JPanel {
	private JLabel lblSelectYourFirmware;
	private JLabel lblEnterTheFirst;
	private JPanel panel;
	private JLabel label;
	private JLabel label_1;
	private JLabel label_3;
	private JComboBox<Integer> cbMajor;
	private JComboBox<Integer> cbMinor;
	private JComboBox<Integer> cbPatch;
	private JComboBox<Integer> cbBrowser;
	private JButton btnConfirm;
	private CallbackReceiver caller;
	
	public SelectFirmwarePanel(CallbackReceiver caller) {
		this();
		this.caller = caller;
	}

	/**
	 * Create the panel.
	 */
	public SelectFirmwarePanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		lblSelectYourFirmware = new JLabel("Select your firmware.");
		lblSelectYourFirmware.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblSelectYourFirmware = new GridBagConstraints();
		gbc_lblSelectYourFirmware.insets = new Insets(0, 0, 5, 0);
		gbc_lblSelectYourFirmware.gridx = 0;
		gbc_lblSelectYourFirmware.gridy = 0;
		add(lblSelectYourFirmware, gbc_lblSelectYourFirmware);
		
		lblEnterTheFirst = new JLabel("Use the combo boxes below");
		lblEnterTheFirst.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblEnterTheFirst = new GridBagConstraints();
		gbc_lblEnterTheFirst.insets = new Insets(0, 0, 5, 0);
		gbc_lblEnterTheFirst.gridx = 0;
		gbc_lblEnterTheFirst.gridy = 1;
		add(lblEnterTheFirst, gbc_lblEnterTheFirst);
		
		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 2;
		add(panel, gbc_panel);
		
		cbMajor = new JComboBox<Integer>();
		cbMajor.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				cbMajor_itemStateChanged(e);
			}
		});
		panel.add(cbMajor);
		
		label = new JLabel(".");
		label.setFont(new Font("Arial", Font.BOLD, 14));
		panel.add(label);
		
		cbMinor = new JComboBox<Integer>();
		cbMinor.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				cbMinor_itemStateChanged(e);
			}
		});
		panel.add(cbMinor);
		
		label_1 = new JLabel(".");
		label_1.setFont(new Font("Arial", Font.BOLD, 14));
		panel.add(label_1);
		
		cbPatch = new JComboBox<Integer>();
		cbPatch.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				cbPatch_itemStateChanged(e);
			}
		});
		panel.add(cbPatch);
		
		label_3 = new JLabel("-");
		label_3.setFont(new Font("Arial", Font.BOLD, 14));
		panel.add(label_3);
		
		cbBrowser = new JComboBox<Integer>();
		cbBrowser.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				cbBrowser_itemStateChanged(e);
			}
		});
		panel.add(cbBrowser);
		
		btnConfirm = new JButton("Confirm");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnConfirm_actionPerformed(e);
			}
		});
		GridBagConstraints gbc_btnConfirm = new GridBagConstraints();
		gbc_btnConfirm.gridx = 0;
		gbc_btnConfirm.gridy = 3;
		add(btnConfirm, gbc_btnConfirm);
		
		fillCombos();

	}

	protected void btnConfirm_actionPerformed(ActionEvent e) {
		if(caller != null) {
			FirmwareVersion fw = new FirmwareVersion(extractItem(cbMajor), 
					extractItem(cbMinor), 
					extractItem(cbPatch),
					extractItem(cbBrowser));
			caller.receiveData(fw, MagicActions.FIRMWARE_SELECTED);
		}
	}
	protected void cbMajor_itemStateChanged(ItemEvent e) {
		notifyVersionChanged();
	}
	protected void cbMinor_itemStateChanged(ItemEvent e) {
		notifyVersionChanged();
	}
	protected void cbPatch_itemStateChanged(ItemEvent e) {
		notifyVersionChanged();
	}
	protected void cbBrowser_itemStateChanged(ItemEvent e) {
		notifyVersionChanged();
	}

	private void fillCombos() {
			// Add a hyphen by default
	//		cbMajor.addItem("--");
	//		cbMinor.addItem("--");
	//		cbPatch.addItem("--");
	//		cbBrowser.addItem("--");
			// Major version
			for(int i=1;i<=11;i++) {
				cbMajor.addItem(i);
			}
			for(int i=0;i<=39;i++) {
				cbMinor.addItem(i);
				cbPatch.addItem(i);
				cbBrowser.addItem(i);
			}
		}

	private <I> I extractItem(JComboBox<I> combo) {
		int index = combo.getSelectedIndex();
		return combo.getItemAt(index);
	}

	private void notifyVersionChanged() {
		if(caller != null) {
			caller.receiveData(MagicActions.FIRMWARE_CHANGED, MagicActions.FIRMWARE_SELECTED);
		}
	}
}
