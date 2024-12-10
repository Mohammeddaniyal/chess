import java.sql.*;
public class MemberDAO
{
public java.util.Map<String,String> getAll()
{
Map<String,String> members=new HashMap<>();
members.put("anis","Anis");
members.put("daniyal","Daniyal");
members.put("azka","Azka");
members.put("hasnain","Hasnain");
members.put("shinan","Shinan");
members.put("halima","Halima");
return members;
}
}