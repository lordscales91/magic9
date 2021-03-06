package io.github.lordscales91.magic9.panels;

import io.github.lordscales91.magic9.HackingPath;
import io.github.lordscales91.magic9.constants.MagicActions;
import io.github.lordscales91.magic9.constants.MagicPropKeys;
import io.github.lordscales91.magic9.core.CallbackReceiver;
import io.github.lordscales91.magic9.core.HackingResource;
import io.github.lordscales91.magic9.domain.FirmwareVersion;
import io.github.lordscales91.magic9.domain.HackingStep;
import io.github.lordscales91.magic9.widget.LinkLabel;
import io.github.lordscales91.magic9.widget.ProgressCellRender;
import io.github.lordscales91.magic9.widget.UpdatableModel;
import io.github.lordscales91.magic9.workers.MagicWorker;
import io.github.lordscales91.magic9.workers.MagicWorkerHandler;
import io.github.lordscales91.magic9.workers.TorrentDownloadWorker;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

@SuppressWarnings("serial")
public class DownloadPanel extends JPanel implements CallbackReceiver {

	private static final Font ORIG_FONT = new Font("Arial", Font.PLAIN, 12);
	private static final String ORIG_MESSAGE = "Please select a download directory below:";
	private CallbackReceiver caller;
	private JLabel lblDownloadRequiredResources;
	private JLabel lblThisToolsNeeds;
	private JLabel lblMessage;
	private JPanel panel;
	private JLabel lblStagingDirectory;
	private JTextField txtHackingDir;
	private JButton btnSelectStagingDir;
	private JButton btnStartDownloads;
	private JScrollPane scrollPane;
	private JTable tblDownloads;
	private String hackingDir;
	private Map<String, MagicWorkerHandler> handlers;
	private JPanel panel_1;
	private JButton btnPrepare;
	private int totalDownloadsCount;
	private int downloadsCompleted;
	private Thread statusTracker;
	private boolean stopTracking;
	private LinkLabel lblUpdateLink;
	private JPanel panel_2;
	private JLabel lblLatestHackableFirmware;

	public DownloadPanel(CallbackReceiver caller) {
		this();
		this.caller = caller;
	}

	/**
	 * Create the panel.
	 */
	public DownloadPanel() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				this_componentShown(e);
			}
		});
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		lblDownloadRequiredResources = new JLabel("Download Required Resources");
		lblDownloadRequiredResources.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblDownloadRequiredResources = new GridBagConstraints();
		gbc_lblDownloadRequiredResources.insets = new Insets(0, 0, 5, 0);
		gbc_lblDownloadRequiredResources.gridx = 0;
		gbc_lblDownloadRequiredResources.gridy = 0;
		add(lblDownloadRequiredResources, gbc_lblDownloadRequiredResources);

		lblThisToolsNeeds = new JLabel(
				"This tool needs to download some files.");
		lblThisToolsNeeds.setFont(ORIG_FONT);
		GridBagConstraints gbc_lblThisToolsNeeds = new GridBagConstraints();
		gbc_lblThisToolsNeeds.insets = new Insets(0, 0, 5, 0);
		gbc_lblThisToolsNeeds.gridx = 0;
		gbc_lblThisToolsNeeds.gridy = 1;
		add(lblThisToolsNeeds, gbc_lblThisToolsNeeds);

		lblMessage = new JLabel(ORIG_MESSAGE);
		lblMessage.setFont(ORIG_FONT);
		GridBagConstraints gbc_lblMessage = new GridBagConstraints();
		gbc_lblMessage.insets = new Insets(0, 0, 5, 0);
		gbc_lblMessage.gridx = 0;
		gbc_lblMessage.gridy = 2;
		add(lblMessage, gbc_lblMessage);

		panel_2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setHgap(0);
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 3;
		add(panel_2, gbc_panel_2);

		lblLatestHackableFirmware = new JLabel(
				"Latest hackable firmware is "+FirmwareVersion.LATEST_HACKABLE.toShortVersion()
				+". You can check the updates ");
		lblLatestHackableFirmware.setFont(new Font("Arial", Font.BOLD, 12));
		lblLatestHackableFirmware.setVisible(false);
		panel_2.add(lblLatestHackableFirmware);
		lblUpdateLink = new LinkLabel(
				HackingPath.URLS.getProperty(MagicPropKeys.TDS_UPDATES_USA),
				"here");
		lblUpdateLink.setFont(new Font("Arial", Font.BOLD, 12));
		lblUpdateLink.setVisible(false);
		panel_2.add(lblUpdateLink);

		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 4;
		add(panel, gbc_panel);

		lblStagingDirectory = new JLabel("Staging directory:");
		lblStagingDirectory.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(lblStagingDirectory);

		txtHackingDir = new JTextField();
		txtHackingDir.setEditable(false);
		txtHackingDir.setFont(ORIG_FONT);
		panel.add(txtHackingDir);
		txtHackingDir.setColumns(15);

		btnSelectStagingDir = new JButton("...");
		btnSelectStagingDir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSelectStagingDir_actionPerformed(e);
			}
		});
		panel.add(btnSelectStagingDir);

		panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 5;
		add(panel_1, gbc_panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		btnPrepare = new JButton("Prepare");
		btnPrepare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPrepare_actionPerformed(e);
			}
		});
		btnPrepare.setEnabled(false);
		panel_1.add(btnPrepare);

		btnStartDownloads = new JButton("Start Downloads");
		panel_1.add(btnStartDownloads);
		btnStartDownloads.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnStartDownloads_actionPerformed(e);
			}
		});
		btnStartDownloads.setEnabled(false);

		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 6;
		add(scrollPane, gbc_scrollPane);

		tblDownloads = new JTable();
		tblDownloads.setModel(new UpdatableModel(new Object[][] {},
				new String[] { "File", "Source", "Status", "Progress" }));
		tblDownloads.getColumn("Progress").setCellRenderer(
				new ProgressCellRender());
		scrollPane.setViewportView(tblDownloads);

	}

	private void fillTable() {
		UpdatableModel model = (UpdatableModel) tblDownloads.getModel();
		List<HackingResource> res = HackingPath.getInstance().resolveResources(
				hackingDir);
		// System.out.println(res);
		totalDownloadsCount = res.size();
		handlers = new HashMap<>(totalDownloadsCount);
		// for now just instantiate the handlers with the corresponding workers
		for(HackingResource r : res) {
			MagicWorker worker = r.getWorker(this);
			final String tag = worker.getTag();
			worker.addPropertyChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if("real_progress".equals(evt.getPropertyName())) {
						updateProgress(tag, (float) evt.getNewValue());
					}

				}
			});
			model.addRow(new Object[] { tag, r.getSource(), "PREPARED", 0.0f });
			handlers.put(tag, new MagicWorkerHandler(worker));
		}
		tblDownloads.repaint();

	}

	private void startDownloads() {
		// Notify the caller before starting to download
		if(caller != null) {
			caller.receiveData(MagicActions.DOWNLOADS_STARTED,
					MagicActions.DOWNLOADS_STATUS_CHANGED);
		}
		UpdatableModel model = (UpdatableModel) tblDownloads.getModel();
		for(MagicWorkerHandler handler : handlers.values()) {
			handler.startWorker();
			model.updateStatus(handler.getTag(), "STARTED");
		}
		stopTracking = false;
		this.statusTracker = new Thread(new Runnable() {
			@Override
			public void run() {
				trackStatus();
			}
		});
		this.statusTracker.start();
	}

	protected void trackStatus() {
		while(!stopTracking) {
			for(MagicWorkerHandler h : handlers.values()) {
				if(h.isStuck()) {
					MagicWorker worker = h.getWorker();
					if(worker instanceof TorrentDownloadWorker) {
						MagicWorker copy = new TorrentDownloadWorker(
								(TorrentDownloadWorker) worker);
						final String tag = h.getTag();
						copy.addPropertyChangeListener(new PropertyChangeListener() {
							@Override
							public void propertyChange(PropertyChangeEvent evt) {
								if("real_progress".equals(evt.getPropertyName())) {
									updateProgress(tag,
											(float) evt.getNewValue());
								}
							}
						});
						h.restart(copy, true);
					}
				} else if(h.getWorker().isDone()) {
					// Deal with race condition
					updateProgress(h.getTag(), 100.0f);
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		}
	}

	protected synchronized void updateProgress(String tag, float progress) {
		UpdatableModel model = (UpdatableModel) tblDownloads.getModel();
		model.updateProgress(tag, progress);
	}
	
	protected synchronized void updateStatus(String tag, String status) {
		UpdatableModel model = (UpdatableModel) tblDownloads.getModel();
		model.updateStatus(tag, status);
	}

	protected void btnStartDownloads_actionPerformed(ActionEvent e) {
		btnStartDownloads.setEnabled(false);
		btnPrepare.setEnabled(false);
		btnSelectStagingDir.setEnabled(false);
		startDownloads();

	}

	protected void btnSelectStagingDir_actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setDialogTitle("Choose a staging directory");
		int op = chooser.showOpenDialog(getParent());
		if(op == JFileChooser.APPROVE_OPTION) {
			File dir = chooser.getSelectedFile();
			hackingDir = dir.getAbsolutePath();
			HackingPath.getInstance().setHackingDir(hackingDir);
			txtHackingDir.setText(hackingDir);
			boolean doit = true;
			if(dir.list().length > 0) {
				op = JOptionPane
						.showConfirmDialog(
								getParent(),
								"Do you want to skip the downloads?",
								"Warning", JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE);
				if (op == JOptionPane.YES_OPTION) {
					doit = false;
					if(caller != null) {
						caller.receiveData(MagicActions.DOWNLOADS_SKIPPED, MagicActions.DOWNLOADS_STATUS_CHANGED);
					}
				} else {
					op = JOptionPane
							.showConfirmDialog(
									getParent(),
									"The directory is not empty!\n Do you want to download the files anyway?",
									"Warning", JOptionPane.YES_NO_OPTION,
									JOptionPane.WARNING_MESSAGE);
					doit = op == JOptionPane.YES_OPTION;
				}
			}
			if(doit) {
				btnPrepare.setEnabled(true);
			}

		}
	}

	protected void btnPrepare_actionPerformed(ActionEvent e) {
		if(!HackingPath.getInstance().requiresUpdate()) {
			fillTable();
			btnStartDownloads.setEnabled(true);
		}

	}

	protected void this_componentShown(ComponentEvent e) {
		HackingPath hp = HackingPath.getInstance();
		boolean showMessage = hp.requiresUpdate();
		// If it requires an update don't bother to ask
		if(!showMessage
				&& HackingStep.DECRYPT9_MSET.equals(hp.getCurrentStep())) {
			int op = JOptionPane
					.showConfirmDialog(
							getParent(),
							"You will need an NDS flashcart to continue.\nDo you have one?",
							"Warning", JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE);
			if(op == JOptionPane.NO_OPTION) {
				/*
				 * No flashcard? Then update required (or flashcard purchase but
				 * I prefer to do not consider that option).
				 */
				showMessage = true;
			}
		}
		showMessage = showMessage || !hp.isHackable();
		if(showMessage) {
			String message = "Oops! It seems that you need to update your system";
			if(!hp.isHackable()) {
				message = "Sorry! It seems that your system is not hackable yet";
			}
			lblMessage.setText(message);
			lblMessage.setFont(new Font("Arial", Font.BOLD, 12));
			lblLatestHackableFirmware.setVisible(true);
			lblUpdateLink.init();
			lblUpdateLink.setVisible(true);
			btnSelectStagingDir.setEnabled(false);
		} else {
			lblMessage.setText(ORIG_MESSAGE);
			lblMessage.setFont(ORIG_FONT);
			lblLatestHackableFirmware.setVisible(false);
			lblUpdateLink.setVisible(false);
			btnSelectStagingDir.setEnabled(true);
		}
	}

	@Override
	public void receiveData(Object data, String tag) {
		if(data instanceof Exception) {
			updateStatus(tag, "ERROR");
			JOptionPane.showMessageDialog(getParent(),
					((Exception) data).getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} else {
			// currently we only use this to track downloads completion
			updateStatus(tag, "FINISHED");
			updateProgress(tag, 100.0f);
			downloadsCompleted++;
			if(totalDownloadsCount > 0
					&& downloadsCompleted == totalDownloadsCount) {
				stopTracking = true;
				btnSelectStagingDir.setEnabled(true);
				btnPrepare.setEnabled(true);
				if(caller != null) {
					caller.receiveData(MagicActions.DOWNLOADS_FINISHED,
							MagicActions.DOWNLOADS_STATUS_CHANGED);
				}
			}
		}
	}
}