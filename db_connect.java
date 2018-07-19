
import java.sql.*;
import java.sql.DriverManager;

public class db_connect
{
    public static Connection con = null;
    static
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/url","root","");
            //System.out.println(con+"Ho rha hai");
        }
        catch(Exception e){ System.out.println(e);}
    }
    public static Connection getcon(){
        return con;
    }
}
