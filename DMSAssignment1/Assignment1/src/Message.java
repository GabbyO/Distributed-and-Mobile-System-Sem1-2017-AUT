
import java.io.Serializable;

public class Message implements Serializable 
{
    protected static final long serialVersionUID = 1112122200L;
    static final int WHOISIN = 0, MESSAGE = 1, LOGOUT = 2, LOGIN = 3;
    private int type;
    private String message;

    public Message(int type, String message) 
    {
        this.type = type;
        this.message = message;
    }
    
    public int getType() 
    {
        return type;
    }
    
    public String getMessage() 
    {
        return message;
    }
}
    