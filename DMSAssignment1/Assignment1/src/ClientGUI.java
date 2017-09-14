import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ClientGUI extends JPanel implements ActionListener 
{
    private static final long serialVersionUID = 1L;
    private JLabel usernameLabel;
    private JTextField usernameTField, serverTField, portTField;
    protected JButton login, logout, whoIsIn;
    protected JTextArea chatTextArea;
    private boolean isConnected;
    private Client client;
    private int defaultPort;
    private String defaultHost;
    protected JPanel chatPanel, topPanel, servAndPortPanel, buttonPanel, textPanel;
    protected JScrollPane strollPane;
    
    public ClientGUI(String host, int port) 
    {
        defaultPort = port;
        defaultHost = host;

        topPanel = new JPanel(new GridLayout(1,1));
        servAndPortPanel = new JPanel();
        buttonPanel = new JPanel();
        chatPanel = new JPanel(new GridLayout(1,2));
        textPanel = new JPanel(new GridLayout(3,0));
        
        serverTField = new JTextField("0.0.0.0", 7);
        portTField = new JTextField(" " + defaultPort, 4);

        servAndPortPanel.add(new JLabel("Server Address:"));
        servAndPortPanel.add(serverTField);
        servAndPortPanel.add(new JLabel("Port Number:"));
        servAndPortPanel.add(portTField);
     
        topPanel.add(servAndPortPanel, BorderLayout.NORTH);
        
        usernameLabel = new JLabel("Enter your username below", 10);
        usernameTField = new JTextField("USERNAME", 40);
        
        chatTextArea = new JTextArea("WELCOME TO THE CHATROOM! \n \n", 25, 40);
        chatTextArea.setLineWrap(true);
        chatTextArea.setWrapStyleWord(true);
        
        strollPane = new JScrollPane(chatTextArea);
        strollPane.setOpaque(true);
	strollPane.setViewportBorder(new EmptyBorder(5, 5, 5, 5));
        chatPanel.add(strollPane);
        chatTextArea.setEditable(false);
        
        textPanel.add(usernameLabel);
        textPanel.add(usernameTField);

        login = new JButton("Log In");
        logout = new JButton("Log Out");
        whoIsIn = new JButton("Users in?");
        
        logout.setEnabled(false);       
        whoIsIn.setEnabled(false);

        buttonPanel.add(login);
        buttonPanel.add(logout);
        buttonPanel.add(whoIsIn);
        
        textPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);
        add(chatPanel);
        add(textPanel, BorderLayout.SOUTH);
        
        logout.addActionListener(this);
        login.addActionListener(this);
        whoIsIn.addActionListener(this);
        usernameTField.addActionListener(this);
    }

    public void connectionFailed() 
    {
        login.setEnabled(true);
        logout.setEnabled(false);
        whoIsIn.setEnabled(false);
        isConnected = false;
    }

    public void actionPerformed(ActionEvent e) 
    {
        Object obj = e.getSource();
        
        if(obj == logout) 
        {
            try 
            {            
                client.outputSocketStream.writeObject(new Message(Message.LOGOUT, 
                        ""));
            } 
            catch (IOException ex) 
            {
                chatTextArea.append("Exception writing to server: \n " + e);
            }
            
            usernameTField.setText("");
            portTField.setText("1400");
            usernameLabel.setText("Enter your username below");
            
            serverTField.setEditable(true);
            portTField.setEditable(true);
            return;
        }

        if(obj == whoIsIn) 
        {
            try 
            {            
                client.outputSocketStream.writeObject(new Message(Message.WHOISIN, 
                        ""));
            } 
            catch (IOException ex) 
            {
                chatTextArea.append("Exception writing to server: \n " + e);
            }
        }

        if(obj == login) 
        {
            String username = usernameTField.getText().trim();

            if(username.length() == 0)
                return;
                
            String server = serverTField.getText().trim();

            if(server.length() == 0)
                return;

            String portNumber = portTField.getText().trim();

            if(portNumber.length() == 0)
                return;

            try 
            {
                defaultPort = Integer.parseInt(portNumber);
            }
            catch(Exception ee) 
            {
                return;
            }

            client = new Client(server, defaultPort, username, this);
            
            if(!client.start())
                return;
            
            try 
            {            
                client.outputSocketStream.writeObject(new Message(Message.LOGIN, 
                        ""));
            } 
            catch (IOException ex) 
            {
                chatTextArea.append("Exception writing to server: \n " + e);
            }
            
            usernameTField.setText("");
            usernameLabel.setText("Enter your message below:");
            serverTField.setEditable(false);
            portTField.setEditable(false);

            login.setEnabled(false);
            logout.setEnabled(true);
            whoIsIn.setEnabled(true);
            
            isConnected = true;
        }
        
        if(isConnected) 
        {
            try 
            {            
                client.outputSocketStream.writeObject(new Message(Message.MESSAGE, 
                        usernameTField.getText()));
            } 
            catch (IOException ex) 
            {
                chatTextArea.append("Exception writing to server: \n " + ex);
            }
            
            usernameTField.setText("");
            return;
        }
    }
    
    public static void main(String[] args) 
    {
        JFrame clientFrame = new JFrame("CLIENT GUI");
        clientFrame.setPreferredSize(new Dimension(500,600));
        clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientFrame.getContentPane().add(new ClientGUI("0.0.0.0", 1400));
        clientFrame.pack();
        clientFrame.setResizable(false);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenDimension = tk.getScreenSize();
        Dimension frameDimension = clientFrame.getSize();
        clientFrame.setLocation((screenDimension.width-frameDimension.width)/4,
           (screenDimension.height-frameDimension.height)/2);
        clientFrame.setVisible(true);
    }
        
}


