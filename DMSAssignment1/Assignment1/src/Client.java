
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client  
{
    protected ObjectInputStream inputSocketStream;
    protected ObjectOutputStream outputSocketStream;
    protected Socket socket;
    private final ClientGUI clientGUI;
    private String serverName, username;
    private int port;
    protected String msg;
    
    public Client(String server, int port, String username, ClientGUI clientGUI)
    {
        this.serverName = server;
        this.port = port;
        this.username = username;
        this.clientGUI = clientGUI;
    }

    public boolean start() 
    {
        try 
        {
            socket = new Socket(serverName, port);
        }

        catch(IOException ec) 
        {
            clientGUI.chatTextArea.append("Error: Cannot connect to a server: \n" 
                    + ec + "\n");
            return false;
        }

        msg = "Connection accepted " + socket.getInetAddress() + ": " 
                + socket.getPort() + "\n";
        
        clientGUI.chatTextArea.append(msg);

        try
        {
            inputSocketStream  = new ObjectInputStream(socket.getInputStream());
            outputSocketStream = new ObjectOutputStream(socket.getOutputStream());
        }
        
        catch (IOException eIO) 
        {
            clientGUI.chatTextArea.append("Exception creating new Input/output "
                    + "Streams: " + eIO);
            return false;
        }
        
        new ServerListener().start();

        try
        {
            outputSocketStream.writeObject(username);
            
        }
        catch (IOException eIO) 
        {
            clientGUI.chatTextArea.append("Exception doing login : " + eIO);
            disconnect();
            return false;
        }
        return true;
    }

    private void disconnect() 
    {
        try 
        {
            if(inputSocketStream != null) 
                inputSocketStream.close();
        }

        catch(IOException e) {}

        try 
        {
            if(outputSocketStream != null) 
                outputSocketStream.close();
        }
        
        catch(IOException e) {}
        
        try
        {
            if(socket != null) socket.close();
        }
        
        catch(IOException e) {} 

        if(clientGUI != null)
        {
            clientGUI.connectionFailed();
        }
    }
    
    public class ServerListener extends Thread 
    {
        public void run() 
        {
            while(true)
            {
                try 
                {
                    msg = (String) inputSocketStream.readObject();
                    clientGUI.chatTextArea.append(msg);
                    
                }
                
                catch(IOException e) 
                {                    
                    if(clientGUI != null)
                        clientGUI.connectionFailed();
                    break;
                }

                catch(ClassNotFoundException e2) {}
            }
        }
    }
}

