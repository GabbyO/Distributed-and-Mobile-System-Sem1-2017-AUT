
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ServerGUI extends JPanel implements ActionListener 
{
    private static final long serialVersionUID = 1L;
    private final JButton STOP_START;
    protected JTextArea logTArea, chatTArea;
    private final JTextField PORT_NO_TFIELD;
    private Server server;
    public int port;
    protected Client client;
    protected JPanel topPanel,centerPanel;
    protected JScrollPane strollPane;
            
    public ServerGUI(int port) 
    {
        server = null;
        
        topPanel = new JPanel();
        centerPanel = new JPanel();
        
        STOP_START = new JButton("START SERVER");
        PORT_NO_TFIELD = new JTextField("  " + port);

        logTArea = new JTextArea(30,35);
        logTArea.setEditable(false);
        logTArea.append("...SERVER LOGS... \n");
        logTArea.setLineWrap(true);
        logTArea.setWrapStyleWord(true);
        
        strollPane = new JScrollPane(logTArea);
        strollPane.setOpaque(true);
	strollPane.setViewportBorder(new EmptyBorder(5, 5, 5, 5));
        centerPanel.add(strollPane);
        
        topPanel.add(new JLabel("Port number: "));
        topPanel.add(PORT_NO_TFIELD);
        topPanel.add(STOP_START);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.SOUTH);
        
        STOP_START.addActionListener(this);
    }      
    
    public void actionPerformed(ActionEvent e) 
    {   
        if(server != null) 
        {
            server.stop();
            server = null;
            PORT_NO_TFIELD.setEditable(true);
            STOP_START.setText("START SERVER");
             
            return;
        } 
        else 
        {
            STOP_START.setText("STOP SERVER");
            PORT_NO_TFIELD.setEditable(false);
        }
        
        try 
        {
            port = Integer.parseInt(PORT_NO_TFIELD.getText().trim());
        }
        catch(NumberFormatException er) 
        {
            logTArea.append("Invalid port number: " + er);
            return;
        }
        
        server = new Server(port, this);
        new ServerRunning().start();
    }

    public class ServerRunning extends Thread 
    {
        public void run() 
        {
            server.start();
            PORT_NO_TFIELD.setEditable(true);
            logTArea.append("\nServer has DISCONNECTED\n");
            server = null;
        }
    }
    
    public static void main(String[] args) 
    {
        JFrame serverFrame = new JFrame("SERVER GUI");;
        serverFrame.setPreferredSize(new Dimension(500,600));
        serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverFrame.getContentPane().add(new ServerGUI(1400));
        serverFrame.pack();
        serverFrame.setResizable(false);
        
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenDimension = tk.getScreenSize();
        Dimension frameDimension = serverFrame.getSize();
        serverFrame.setLocation((screenDimension.width+frameDimension.width)/3,
           (screenDimension.height-frameDimension.height)/2);
        serverFrame.setVisible(true);
    }
}
