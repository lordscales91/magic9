package io.github.lordscales91.magic9;

import io.github.lordscales91.magic9.constants.MagicPropKeys;
import io.github.lordscales91.magic9.widget.LinkLabel;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JButton btnStart;
	private LinkLabel lbl3DSLink;

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
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				frame_windowOpened();
			}
		});
		setTitle("Magic9 GUI Tool");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblWelcomeToThis = new JLabel("Welcome to this Useful Tool! (^_^)");
		lblWelcomeToThis.setFont(new Font("Arial", Font.BOLD, 18));
		GridBagConstraints gbc_lblWelcomeToThis = new GridBagConstraints();
		gbc_lblWelcomeToThis.insets = new Insets(0, 0, 5, 0);
		gbc_lblWelcomeToThis.gridx = 0;
		gbc_lblWelcomeToThis.gridy = 0;
		contentPane.add(lblWelcomeToThis, gbc_lblWelcomeToThis);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		contentPane.add(panel, gbc_panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		
		JLabel lblBeforeStarting = new JLabel("Before starting this journey to the 3DS Scene you should read about the risks ");
		panel.add(lblBeforeStarting);
		lblBeforeStarting.setFont(new Font("Arial", Font.BOLD, 14));
		lbl3DSLink = new LinkLabel(HackingPath.URLS.getProperty(MagicPropKeys.$3DS_GUIDE), "here");
		panel.add(lbl3DSLink);
		lbl3DSLink.setFont(new Font("Arial", Font.BOLD, 14));
		
		JLabel lblImNotLiable = new JLabel("I'm not liable for any damage caused to your device. Use this tool at your own risk");
		lblImNotLiable.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblImNotLiable = new GridBagConstraints();
		gbc_lblImNotLiable.insets = new Insets(0, 0, 5, 0);
		gbc_lblImNotLiable.gridx = 0;
		gbc_lblImNotLiable.gridy = 2;
		contentPane.add(lblImNotLiable, gbc_lblImNotLiable);
		
		JLabel lblOnceYouFeel = new JLabel("Once you feel ready press the button below");
		lblOnceYouFeel.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblOnceYouFeel = new GridBagConstraints();
		gbc_lblOnceYouFeel.insets = new Insets(0, 0, 5, 0);
		gbc_lblOnceYouFeel.gridx = 0;
		gbc_lblOnceYouFeel.gridy = 3;
		contentPane.add(lblOnceYouFeel, gbc_lblOnceYouFeel);
		
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnStart_actionPerformed(e);
			}
		});
		
		GridBagConstraints gbc_btnStart = new GridBagConstraints();
		gbc_btnStart.gridx = 0;
		gbc_btnStart.gridy = 4;
		contentPane.add(btnStart, gbc_btnStart);
	}

	protected void frame_windowOpened() {
		btnStart.requestFocusInWindow();
		lbl3DSLink.init();
	}

	protected void btnStart_actionPerformed(ActionEvent e) {
		this.dispose();
		PreparationWizard.showFrame();
	}
}
