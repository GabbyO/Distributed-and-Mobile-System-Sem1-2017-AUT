import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Server 
{
    private static int UNIQUE_ID;
    private ArrayList<ClientThread> clientsTreadList;
    protected ClientThread clientThreadSocket;
    private ServerGUI serverGUI;
    private int port;
    private boolean checkStart;
    protected Socket socket;
    private SimpleDateFormat dateTime;
    
    public Server(int port, ServerGUI serverGUI) 
    {
        this.serverGUI = serverGUI;
        this.port = port;
        dateTime = new SimpleDateFormat("HH:mm:ss");
        clientsTreadList = new ArrayList<ClientThread>();
    }
    
    public void start() 
    {
        checkStart = true;
        
        try
        {
            ServerSocket serverSocket = new ServerSocket(port);
            
            while(checkStart)
            {
                serverGUI.logTArea.append("Server is waiting for Clients on port " 
                    +  port + "...\n");

                socket = serverSocket.accept(); 

                if(!checkStart)
                    break;
                
                clientThreadSocket = new ClientThread(socket);
                clientsTreadList.add(clientThreadSocket);
                
                clientThreadSocket.start();            
            }
            
            try 
            {
                serverSocket.close();
                for(int i = 0; i < clientsTreadList.size(); ++i) 
                {
                    ClientThread clientThread = clientsTreadList.get(i);
                    try 
                    {
                        clientThread.socketInput.close();
                        clientThread.socketOutput.close();
                        clientThread.socket.close();
                    }
                    catch(IOException e) {}
                }
            }
            catch(IOException e) 
            {
                serverGUI.logTArea.append("Exception closing the server and clients: "
                        + "\n" + e);
            }
            
        }
        catch (IOException e) 
        {
            String msg = dateTime.format(new Date()) + " Exception on new ServerSocket: " 
                    + e + "\n";
            serverGUI.logTArea.append(msg + "\n");
        }
    }      

    protected void stop() 
    {
        checkStart = false;
        
        try 
        {
            socket = new Socket("", port);
        }
        
        catch(IOException e) 
        {
            serverGUI.logTArea.append("Error: \n" + e);
        }
    }

    private synchronized void BroadcastMessage(String message) 
    {
        String msgTime = message + "\n";

        for(int i = clientsTreadList.size(); --i >= 0;) 
        {
            ClientThread clientThread = clientsTreadList.get(i);
            
            if(!clientThread.writeMsg(msgTime)) 
            {
                clientsTreadList.remove(i);
                serverGUI.logTArea.append("Client Disconnected " + 
                        clientThread.username + " removed from list.");
            }
        }
    }

    synchronized void remove(int id) 
    {
        for(int i = 0; i < clientsTreadList.size(); ++i) 
        {
            ClientThread clientThread = clientsTreadList.get(i);

            if(clientThread.id == id) 
            {
                clientsTreadList.remove(i);
                return;
            }
        }
    }

    public class ClientThread extends Thread 
    {
        protected Socket socket;
        protected ObjectInputStream socketInput;
        protected ObjectOutputStream socketOutput;
        protected int id;
        protected String username, date;
        protected Message msg;
        
        public ClientThread(Socket socket) 
        {
            id = ++UNIQUE_ID;
            this.socket = socket;
            
            try
            {
                socketOutput = new ObjectOutputStream(socket.getOutputStream());
                socketInput  = new ObjectInputStream(socket.getInputStream());
                username = (String) socketInput.readObject();
                serverGUI.logTArea.append(username + " just connected. \n");
            }
            catch (IOException e) 
            {
                serverGUI.logTArea.append("Exception creating new Input "
                        + "and output Streams: " + e + "\n");
                return;
            }
            catch (ClassNotFoundException e) {}
            
            date = new Date().toString() + "\n";
            
        }

        public void run() 
        {
            boolean ifStart = true;

            BroadcastMessage("SERVER IS NOW CREATED IP-ADDRESS: " + 
                    socket.getLocalSocketAddress() + "\n");
            
            while(ifStart) 
            {
                try 
                {
                    msg = (Message) socketInput.readObject();
                }
                catch (IOException e) 
                {
                    serverGUI.logTArea.append(username + 
                            " has DISCONNECTED! \nException reading Streams: "
                                    + "\n" + e);
                    break;             
                }
                
                catch(ClassNotFoundException e) 
                {
                    break;
                }
                
                String message = msg.getMessage();

                String time = dateTime.format(new Date());

                switch(msg.getType()) 
                {
                    case Message.MESSAGE:
                        BroadcastMessage(time + ": " + username + ">> " + message);
                        break;

                    case Message.LOGOUT:
                        serverGUI.logTArea.append(username + " has DISCONNECTED. \n");
                        BroadcastMessage(username + " decided to LOG OUT. \n");
                        ifStart = false;
                        break;

                    case Message.WHOISIN:
                        BroadcastMessage("List of the users connected at " + 
                                dateTime.format(new Date()));

                        for(int i = 0; i < clientsTreadList.size(); ++i) 
                        {
                            ClientThread clientThread = clientsTreadList.get(i);
                            BroadcastMessage(" " + (i+1) + ") " + clientThread.username 
                                    + " since " + clientThread.date);
                        }
                        break;
                        
                    case Message.LOGIN:
                        BroadcastMessage(username + " just connected. \n");
                        break;
                }
            }
            
            remove(id);
            close();
        }
        
        private void close() 
        {
            try 
            {
                if(socketOutput != null) socketOutput.close();
            }
            
            catch(IOException e) {}
            
            try 
            {
                if(socketInput != null) socketInput.close();
            }
            
            catch(IOException e) {}

            try 
            {
                if(socket != null) socket.close();
            }
            
            catch (IOException e) {}
        }
        
        private boolean writeMsg(String msg) 
        {
            if(!socket.isConnected()) 
            {
                close();
                return false;
            }
            
            try 
            {
                socketOutput.writeObject(msg);
            }

            catch(IOException e) 
            {
                serverGUI.logTArea.append("Error: Cannot send message to " + username 
                    + "\n" + e.toString());
            }
            return true;
        }
    }
}
