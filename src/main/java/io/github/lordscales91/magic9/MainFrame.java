package io.github.lordscales91.magic9;

import io.github.lordscales91.magic9.core.CallbackReceiver;
import io.github.lordscales91.magic9.core.MagicUtils;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.turn.ttorrent.client.Client;
import java.awt.Insets;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements CallbackReceiver {

	private JPanel contentPane;
	private File basedir;
	private TorrentDownloadWorker worker;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
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
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JButton btnTestTorrent = new JButton("Test Torrent");
		GridBagConstraints gbc_btnTestTorrent = new GridBagConstraints();
		gbc_btnTestTorrent.insets = new Insets(0, 0, 0, 5);
		gbc_btnTestTorrent.gridx = 0;
		gbc_btnTestTorrent.gridy = 0;
		contentPane.add(btnTestTorrent, gbc_btnTestTorrent);
		
		JButton btnRestart = new JButton("Stop");
		GridBagConstraints gbc_btnRestart = new GridBagConstraints();
		gbc_btnRestart.gridx = 1;
		gbc_btnRestart.gridy = 0;
		contentPane.add(btnRestart, gbc_btnRestart);
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
				btnRestart_actioonPerformed();
			}
			
		});
	}

	protected void btnRestart_actioonPerformed() {
		if(worker != null && !worker.isCancelled() && !worker.isDone()) {
			worker.stop();
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
			worker = new TorrentDownloadWorker(chooser.getSelectedFile(),
					basedir, "test_torrent", this);
			worker.addPropertyChangeListener(new PropertyChangeListener() {
				
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if(TorrentDownloadWorker.REAL_PROGRESS.equals(evt.getPropertyName())) {
						progress_update((float) evt.getNewValue(),"track_torrent");
					}
				}
			});
			worker.execute();
		}
	}
	
	protected void progress_update(float f, String tag) {
		System.out.printf(Locale.ROOT, "%.02f%% Completed\n", f);
	}

	@Override
	public void receiveData(Object data, String tag) {
		if(data instanceof Exception) {
			((Exception)data).printStackTrace();
			JOptionPane.showMessageDialog(this, ((Exception)data).getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			if(tag.equals("test_torrent")) {
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
			}
		}
	}

}
