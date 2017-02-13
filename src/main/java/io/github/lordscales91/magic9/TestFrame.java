package io.github.lordscales91.magic9;

import io.github.lordscales91.magic9.constants.MagicConstants;
import io.github.lordscales91.magic9.core.CallbackReceiver;
import io.github.lordscales91.magic9.core.HackingProcess;
import io.github.lordscales91.magic9.core.HackingResource;
import io.github.lordscales91.magic9.domain.FirmwareVersion;
import io.github.lordscales91.magic9.domain.HackingStep;
import io.github.lordscales91.magic9.workers.DownloadWorker;
import io.github.lordscales91.magic9.workers.GithubDownloadWorker;
import io.github.lordscales91.magic9.workers.MagicWorker;
import io.github.lordscales91.magic9.workers.MagicWorkerHandler;
import io.github.lordscales91.magic9.workers.TorrentDownloadWorker;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.SystemColor;
import java.awt.Font;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class TestFrame extends JFrame implements CallbackReceiver {

	private static final String TEST_TORRENT = "test_torrent";
	private static final String GH_DOWNLOAD = "gh_download";
	private static final String DIRECTDL = "directdl";
	private JPanel contentPane;
	private File basedir;
	private List<MagicWorkerHandler> handlers = Collections.synchronizedList(new ArrayList<MagicWorkerHandler>());
	private File torrent;
	private boolean stop;
	private Thread monitor;
	private JPanel testPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestFrame frame = new TestFrame();
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
	public TestFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JButton btnTestTorrent = new JButton("Test Torrent");
		GridBagConstraints gbc_btnTestTorrent = new GridBagConstraints();
		gbc_btnTestTorrent.insets = new Insets(0, 0, 5, 5);
		gbc_btnTestTorrent.gridx = 0;
		gbc_btnTestTorrent.gridy = 0;
		contentPane.add(btnTestTorrent, gbc_btnTestTorrent);
		
		JButton btnRestart = new JButton("Stop Torrent");
		GridBagConstraints gbc_btnRestart = new GridBagConstraints();
		gbc_btnRestart.insets = new Insets(0, 0, 5, 0);
		gbc_btnRestart.gridx = 1;
		gbc_btnRestart.gridy = 0;
		contentPane.add(btnRestart, gbc_btnRestart);
		
		JButton btnTestGithub = new JButton("Test Github");
		GridBagConstraints gbc_btnTestGithub = new GridBagConstraints();
		gbc_btnTestGithub.insets = new Insets(0, 0, 5, 5);
		gbc_btnTestGithub.gridx = 0;
		gbc_btnTestGithub.gridy = 1;
		contentPane.add(btnTestGithub, gbc_btnTestGithub);
		
		JButton btnTestHomebrewDl = new JButton("Test Homebrew DL");
		btnTestHomebrewDl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnTestHomebrewDL_actionPerformed();
			}
		});
		GridBagConstraints gbc_btnTestHomebrewDl = new GridBagConstraints();
		gbc_btnTestHomebrewDl.insets = new Insets(0, 0, 5, 0);
		gbc_btnTestHomebrewDl.gridx = 1;
		gbc_btnTestHomebrewDl.gridy = 1;
		contentPane.add(btnTestHomebrewDl, gbc_btnTestHomebrewDl);
		
		JButton btnTestDownload = new JButton("Test Download");		
		GridBagConstraints gbc_btnTestDownload = new GridBagConstraints();
		gbc_btnTestDownload.insets = new Insets(0, 0, 5, 5);
		gbc_btnTestDownload.gridx = 0;
		gbc_btnTestDownload.gridy = 2;
		contentPane.add(btnTestDownload, gbc_btnTestDownload);
		
		JButton btnTestDecryptBrowser = new JButton("Test Decrypt9 Browser");
		btnTestDecryptBrowser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				testDecrypt9Browser();
			}
		});
		GridBagConstraints gbc_btnTestDecryptBrowser = new GridBagConstraints();
		gbc_btnTestDecryptBrowser.insets = new Insets(0, 0, 5, 0);
		gbc_btnTestDecryptBrowser.gridx = 1;
		gbc_btnTestDecryptBrowser.gridy = 2;
		contentPane.add(btnTestDecryptBrowser, gbc_btnTestDecryptBrowser);
		
		JButton btnTestTorrentResource = new JButton("Test Torrent Resource");
		btnTestTorrentResource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				testTorrentResource();
			}
		});
		
		JButton btnToggleJpanelVisibility = new JButton("Toggle JPanel visibility");
		btnToggleJpanelVisibility.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnToggleJpanelVisibility_actionPerformed(e);
			}
		});
		GridBagConstraints gbc_btnToggleJpanelVisibility = new GridBagConstraints();
		gbc_btnToggleJpanelVisibility.insets = new Insets(0, 0, 5, 5);
		gbc_btnToggleJpanelVisibility.gridx = 0;
		gbc_btnToggleJpanelVisibility.gridy = 3;
		contentPane.add(btnToggleJpanelVisibility, gbc_btnToggleJpanelVisibility);
		GridBagConstraints gbc_btnTestTorrentResource = new GridBagConstraints();
		gbc_btnTestTorrentResource.insets = new Insets(0, 0, 5, 0);
		gbc_btnTestTorrentResource.gridx = 1;
		gbc_btnTestTorrentResource.gridy = 3;
		contentPane.add(btnTestTorrentResource, gbc_btnTestTorrentResource);
		
		testPanel = new JPanel();
		GridBagConstraints gbc_testPanel = new GridBagConstraints();
		gbc_testPanel.gridwidth = 2;
		gbc_testPanel.insets = new Insets(0, 0, 5, 0);
		gbc_testPanel.fill = GridBagConstraints.BOTH;
		gbc_testPanel.gridx = 0;
		gbc_testPanel.gridy = 4;
		contentPane.add(testPanel, gbc_testPanel);
		
		JButton btnNewButton = new JButton("New button");
		testPanel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("New button");
		testPanel.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("New button");
		testPanel.add(btnNewButton_2);
		
		JLabel lblTextBelow = new JLabel("Text Below");
		GridBagConstraints gbc_lblTextBelow = new GridBagConstraints();
		gbc_lblTextBelow.gridwidth = 2;
		gbc_lblTextBelow.insets = new Insets(0, 0, 0, 5);
		gbc_lblTextBelow.gridx = 0;
		gbc_lblTextBelow.gridy = 5;
		contentPane.add(lblTextBelow, gbc_lblTextBelow);
		// This is placed here just for test purposes.
		// The final GUI will be different
		btnTestTorrent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnTestTorrent_acionPerformed();
			}
		});
		btnRestart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				btnRestartTorrent_actionPerformed();
			}
			
		});
		btnTestGithub.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				btnGithub_actiionPerformed();
			}
		});
		btnTestDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnTestDownload_actionPerformed();
			}
		});
	}

	protected void testTorrentResource() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Choose a Fake SD path to test");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int op = chooser.showOpenDialog(contentPane);
		File sdCardDir = null;
		File hackingDir = null;
		if(op == JFileChooser.APPROVE_OPTION) {
			sdCardDir = chooser.getSelectedFile();
		}
		chooser.setDialogTitle("Choose a Fake staging directory (hacking dir) to test");
		op = chooser.showOpenDialog(contentPane);
		if(op == JFileChooser.APPROVE_OPTION) {
			hackingDir = chooser.getSelectedFile();
		}
		HackingPath.resolve(new FirmwareVersion("11.1.0-26J"), false);
		if(hackingDir!=null && sdCardDir != null) {
			HackingProcess proc = HackingProcess.getInstance(HackingStep.CTRTRANSFER_210, hackingDir, sdCardDir);
			List<HackingResource> res = proc.getRequiredResources();
			for (HackingResource r : res) {
				MagicWorker worker = r.getWorker(this);
				final String tag = worker.getTag();
				worker.addPropertyChangeListener(new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if(evt.getPropertyName().equals("real_progress")) {
							progress_update((float) evt.getNewValue(), tag);
						}
					}
				});
				worker.execute();
			}
		}
	}

	protected void testDecrypt9Browser() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Choose a Fake SD path to test");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int op = chooser.showOpenDialog(contentPane);
		File sdCardDir = null;
		File hackingDir = null;
		if(op == JFileChooser.APPROVE_OPTION) {
			sdCardDir = chooser.getSelectedFile();
		}
		chooser.setDialogTitle("Choose a Fake staging directory (hacking dir) to test");
		op = chooser.showOpenDialog(contentPane);
		if(op == JFileChooser.APPROVE_OPTION) {
			hackingDir = chooser.getSelectedFile();
		}
		HackingPath.resolve(new FirmwareVersion("11.1.0-35E"), false);
		if(hackingDir!=null && sdCardDir != null) {
			HackingProcess proc = HackingProcess.getInstance(HackingStep.DECRYPT9_HOMEBREW, hackingDir, sdCardDir);
			List<HackingResource> resources = proc.getRequiredResources();
			int i =0;
			for(HackingResource res:resources) {
				final String tag = "worker-"+i;
				MagicWorker worker = res.getWorker(tag, this);
				worker.addPropertyChangeListener(new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if(evt.getPropertyName().equals("real_progress")) {
							progress_update((float) evt.getNewValue(), tag);
						}
					}
				});								
				worker.execute();
				i++;
			}
		}
	}

	protected void btnTestHomebrewDL_actionPerformed() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Choose a Fake SD path to test");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int op = chooser.showOpenDialog(contentPane);
		File sdCardDir = null;
		File hackingDir = null;
		if(op == JFileChooser.APPROVE_OPTION) {
			sdCardDir = chooser.getSelectedFile();
		}
		chooser.setDialogTitle("Choose a Fake staging directory (hacking dir) to test");
		op = chooser.showOpenDialog(contentPane);
		if(op == JFileChooser.APPROVE_OPTION) {
			hackingDir = chooser.getSelectedFile();
		}
		HackingPath.resolve(new FirmwareVersion("11.1.0-35E"), false);
		if(hackingDir!=null && sdCardDir != null) {
			HackingProcess proc = HackingProcess.getInstance(HackingStep.HOMEBREW_SOUNDHAX, hackingDir, sdCardDir);
			List<HackingResource> resources = proc.getRequiredResources();
			int i =0;
			for(HackingResource res:resources) {
				final String tag = "worker-"+i;
				MagicWorker worker = res.getWorker(tag, this);
				worker.addPropertyChangeListener(new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if(evt.getPropertyName().equals("real_progress")) {
							progress_update((float) evt.getNewValue(), tag);
						}
					}
				});								
				worker.execute();
				i++;
			}
		}
		
		
	}

	protected void btnTestDownload_actionPerformed() {
		String url = "http://smealum.github.io/ninjhax2/starter.zip";
		File outdir = new File("target/directdl");
		if(!outdir.exists()) {
			outdir.mkdirs();
		}
		DownloadWorker dlworker = new DownloadWorker(url, new File(outdir, MagicConstants.STARTER_KIT_ZIP), DIRECTDL, this);
		dlworker.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if(DownloadWorker.REAL_PROGRESS.equals(evt.getPropertyName())) {
					progress_update((float) evt.getNewValue(), DIRECTDL);
				}
			}
		});
		// Remove previous handler if needed
		MagicWorkerHandler handler = findHandler(DIRECTDL);
		if(handler != null) {
			handlers.remove(handler);
		}
		handler = new MagicWorkerHandler(dlworker);
		handler.startWorker();
		handlers.add(handler);
	}

	protected void btnGithub_actiionPerformed() {
		String releaseUrl = "https://github.com/AuroraWright/Luma3DS/releases/latest";
		File outdir = new File("target/ghdownloads");
		if(!outdir.exists()) {
			outdir.mkdirs();
		}
		GithubDownloadWorker ghworker = new GithubDownloadWorker(releaseUrl, "7z", new File(outdir, MagicConstants.LUMA3DS_7Z), 
				GH_DOWNLOAD, this);
		ghworker.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if(GithubDownloadWorker.REAL_PROGRESS.equals(evt.getPropertyName())) {
					progress_update((float) evt.getNewValue(), GH_DOWNLOAD);
				}
			}
		});
		ghworker.execute();
		// Remove previous handler if needed
		MagicWorkerHandler handler = findHandler(DIRECTDL);
		if(handler != null) {
			handlers.remove(handler);
		}
		handler = new MagicWorkerHandler(ghworker);
		handler.startWorker();
		handlers.add(handler);
	}

	protected void btnRestartTorrent_actionPerformed() {
		MagicWorkerHandler handler = findHandler(TEST_TORRENT);
		if(handler == null) {
			JOptionPane.showMessageDialog(contentPane, "Torrent has not been started yet", "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			TorrentDownloadWorker worker = getTorrentWorker(torrent);
			if(!handler.restart(worker)) {
				JOptionPane.showMessageDialog(contentPane, "Max retries exceeded. Try again later", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	protected void btnTestTorrent_acionPerformed() {
		basedir = new File("target/torrent_test");
		if(!basedir.exists()) {
			basedir.mkdirs();
		}
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Choose a torrent to test");
		chooser.setFileFilter(new FileNameExtensionFilter("Torrent file (*.torrent)", "torrent"));
		int op = chooser.showOpenDialog(contentPane);
		if(op == JFileChooser.APPROVE_OPTION) {
			torrent = chooser.getSelectedFile();
			TorrentDownloadWorker worker = getTorrentWorker(torrent);
			// Remove previous handler if needed
			MagicWorkerHandler handler = findHandler(DIRECTDL);
			if(handler != null) {
				handlers.remove(handler);
			}
			handler = new MagicWorkerHandler(worker);
			handler.startWorker();
			handlers.add(handler);
			startMonitoring();
		}
	}

	private MagicWorkerHandler findHandler(String tag) {
		for(MagicWorkerHandler handler:handlers) {
			if(handler.getTag().equals(tag)) {
				return handler;
			}
		}
		return null;
	}

	private void startMonitoring() {
		monitor = new Thread(new Runnable() {
			@Override
			public void run() {
				checkHandlers();
			}
		});
		monitor.start();
	}

	protected void checkHandlers() {
		while(!stop) {
			synchronized (handlers) {
				for(MagicWorkerHandler handler:handlers) {
					if(handler.isStuck()) {
						System.out.println("Handler "+handler.getTag()+" is stuck");
						System.out.println("Restarting... ");
						restartHandler(handler);
					}
				}
			}
			try {
				Thread.sleep(500); // Check every 0.5s
			} catch (InterruptedException ignore) {}
		}
	}

	private void restartHandler(MagicWorkerHandler handler) {
		if(TEST_TORRENT.equals(handler.getTag())) {
			if(!handler.restart(getTorrentWorker(torrent), true)) {
				JOptionPane.showMessageDialog(contentPane, "Couldn't restart the torrent download", "Error", JOptionPane.ERROR_MESSAGE);
				// Remove this handler from the list to avoid future attempts to restart it.
				handlers.remove(handler);
			}
		}
	}

	private TorrentDownloadWorker getTorrentWorker(File torrent) {
		TorrentDownloadWorker worker = new TorrentDownloadWorker(torrent,
				basedir, TEST_TORRENT, this);
		worker.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if(TorrentDownloadWorker.REAL_PROGRESS.equals(evt.getPropertyName())) {
					progress_update((float) evt.getNewValue(), TEST_TORRENT);
				}
			}
		});
		return worker;
	}
	
	protected void progress_update(float f, String tag) {
		System.out.printf(Locale.ROOT, "%s: %.02f%% Completed\n", tag, f);
	}

	@Override
	public void receiveData(Object data, String tag) {
		if(data instanceof Exception) {
			((Exception)data).printStackTrace();
			JOptionPane.showMessageDialog(this, ((Exception)data).getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			if(tag.equals(TEST_TORRENT)) {
				System.out.println("Torrent download completed");
//				try {
//					File src = new File(basedir, ((Client)data).getTorrent().getName());
//					File dst = new File(basedir, "fbi_injectable.zip");
//					MagicUtils.moveFile(src, dst);
//					System.out.println("File renamed");					
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			} else if(tag.equals(GH_DOWNLOAD)) {
				System.out.println("Github download completed");
			} else if(tag.equals(DIRECTDL)) {
				System.out.println("Direct download completed");
			}
		}
	}

	protected void btnToggleJpanelVisibility_actionPerformed(ActionEvent e) {
		boolean visible = !testPanel.isVisible();
		testPanel.setVisible(visible);
	}
}
