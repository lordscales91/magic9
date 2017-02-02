package io.github.lordscales91.magic9;

import io.github.lordscales91.magic9.core.CallbackReceiver;
import io.github.lordscales91.magic9.core.HackingResource;

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

@SuppressWarnings("serial")
public class DownloadPanel extends JPanel implements CallbackReceiver {
	
	private CallbackReceiver caller;
	private JLabel lblDownloadRequiredResources;
	private JLabel lblThisToolsNeeds;
	private JLabel lblPleaseSelectA;
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

	public DownloadPanel(CallbackReceiver caller) {
		this();
		this.caller = caller;
	}

	/**
	 * Create the panel.
	 */
	public DownloadPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		lblDownloadRequiredResources = new JLabel("Download Required Resources");
		lblDownloadRequiredResources.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblDownloadRequiredResources = new GridBagConstraints();
		gbc_lblDownloadRequiredResources.insets = new Insets(0, 0, 5, 0);
		gbc_lblDownloadRequiredResources.gridx = 0;
		gbc_lblDownloadRequiredResources.gridy = 0;
		add(lblDownloadRequiredResources, gbc_lblDownloadRequiredResources);
		
		lblThisToolsNeeds = new JLabel("This tool needs to download some files.");
		lblThisToolsNeeds.setFont(new Font("Arial", Font.PLAIN, 12));
		GridBagConstraints gbc_lblThisToolsNeeds = new GridBagConstraints();
		gbc_lblThisToolsNeeds.insets = new Insets(0, 0, 5, 0);
		gbc_lblThisToolsNeeds.gridx = 0;
		gbc_lblThisToolsNeeds.gridy = 1;
		add(lblThisToolsNeeds, gbc_lblThisToolsNeeds);
		
		lblPleaseSelectA = new JLabel("Please select a download directory below:");
		lblPleaseSelectA.setFont(new Font("Arial", Font.PLAIN, 12));
		GridBagConstraints gbc_lblPleaseSelectA = new GridBagConstraints();
		gbc_lblPleaseSelectA.insets = new Insets(0, 0, 5, 0);
		gbc_lblPleaseSelectA.gridx = 0;
		gbc_lblPleaseSelectA.gridy = 2;
		add(lblPleaseSelectA, gbc_lblPleaseSelectA);
		
		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 3;
		add(panel, gbc_panel);
		
		lblStagingDirectory = new JLabel("Staging directory:");
		lblStagingDirectory.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(lblStagingDirectory);
		
		txtHackingDir = new JTextField();
		txtHackingDir.setEditable(false);
		txtHackingDir.setFont(new Font("Arial", Font.PLAIN, 12));
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
		gbc_panel_1.gridy = 4;
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
		gbc_scrollPane.gridy = 5;
		add(scrollPane, gbc_scrollPane);
		
		tblDownloads = new JTable();
		tblDownloads.setModel(new UpdatableModel(new Object[][] {
		}, new String[] {
			"File", "Source", "Status", "Progress"
		}));
		tblDownloads.getColumn("Progress").setCellRenderer(new ProgressCellRender());
		scrollPane.setViewportView(tblDownloads);

	}

	private void fillTable() {
		UpdatableModel model = (UpdatableModel) tblDownloads.getModel();
		List<HackingResource> res = HackingPath.getInstance().resolveResources(hackingDir);
		// System.out.println(res);
		totalDownloadsCount = res.size();
		handlers = new HashMap<>(totalDownloadsCount);
		// for now just instantiate the handlers with the corresponding workers
		for(HackingResource r:res) {
			MagicWorker worker = r.getWorker(this);
			final String tag = worker.getTag();
			worker.addPropertyChangeListener(new PropertyChangeListener() {				
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if("real_progress".equals(evt.getPropertyName())) {
						updateProgress(tag, (float)evt.getNewValue());
					}
					
				}
			});
			model.addRow(new Object[]{tag, r.getSource(), "PREPARED", 0.0f});
			handlers.put(tag, new MagicWorkerHandler(worker));
		}
		tblDownloads.repaint();
		
	}
	
	private void startDownloads() {
		// Notify the caller before starting to download
		if(caller != null) {
			caller.receiveData(MagicActions.DOWNLOADS_STARTED, MagicActions.DOWNLOADS_STATUS_CHANGED);
		}
		UpdatableModel model = (UpdatableModel) tblDownloads.getModel();
		for (MagicWorkerHandler handler : handlers.values()) {
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
			for(MagicWorkerHandler h:handlers.values()) {
				if(h.isStuck()) {
					MagicWorker worker = h.getWorker();
					if(worker instanceof TorrentDownloadWorker) {
						MagicWorker copy = new TorrentDownloadWorker((TorrentDownloadWorker) worker);
						final String tag = h.getTag();
						copy.addPropertyChangeListener(new PropertyChangeListener() {
							@Override
							public void propertyChange(PropertyChangeEvent evt) {
								if("real_progress".equals(evt.getPropertyName())) {
									updateProgress(tag, (float) evt.getNewValue());
								}								
							}
						});
						h.restart(copy, true);
					}
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {}
		}
	}

	protected void updateProgress(String tag, float progress) {
		UpdatableModel model = (UpdatableModel) tblDownloads.getModel();
		model.updateProgress(tag, progress);
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
			boolean doit = true;
			if(dir.list().length > 0) {
				op = JOptionPane.showConfirmDialog(getParent(),
						"The directory is not empty!\n Do you want to continue anyway?", "Warning", 
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				doit = op == JOptionPane.YES_OPTION;
			} 
			if(doit) {
				hackingDir = dir.getAbsolutePath();
				txtHackingDir.setText(hackingDir);
				btnPrepare.setEnabled(true);
			}
			
		}
	}

	protected void btnPrepare_actionPerformed(ActionEvent e) {
		fillTable();
		btnStartDownloads.setEnabled(true);
	}

	@Override
	public void receiveData(Object data, String tag) {
		UpdatableModel model = (UpdatableModel) tblDownloads.getModel();
		if(data instanceof Exception) {
			model.updateStatus(tag, "ERROR");
			JOptionPane.showMessageDialog(getParent(), ((Exception)data).getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			// currently we only use this to track downloads completion
			model.updateStatus(tag, "FINISHED");
			model.updateProgress(tag, 100.0f);
			downloadsCompleted++;
			if(totalDownloadsCount > 0 && downloadsCompleted == totalDownloadsCount) {
				stopTracking = true;
				btnSelectStagingDir.setEnabled(true);
				btnPrepare.setEnabled(true);
				if(caller != null) {
					caller.receiveData(MagicActions.DOWNLOADS_FINISHED, MagicActions.DOWNLOADS_STATUS_CHANGED);
				}
			}
		}
	}
}