public class MemberDTO implements java.io.Serializable,Comparable<MemberDTO>
{
private String username;
private String password;
public MemberDTO()
{
this.username="";
this.password="";
}
public void setUsername(String username)
{
this.username=username;
}
public void setPassword(String password)
{
this.password=password;
}
public String getUsername()
{
return username;
}
public String getPassword()
{
return password;
}
public boolean equals(Object object)
{
if(! (object instanceof MemberDTO)) return false;
MemberDTO other=(MemberDTO)object;
return this.username.equals(other.username);
}
public int compareTo(MemberDTO other)
{
return this.username.compareToIgnoreCase(other.username);
}
public int hashCode()
{
return this.username.hashCode();
}
}