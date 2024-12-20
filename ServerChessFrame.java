import com.thinking.machines.nframework.server.*;
import com.thinking.machines.nframework.server.annotations.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
public class ServerChessFrame extends JFrame implements ActionListener
{
private boolean done=false;
private java.util.List<String> piecesList;
private Map<String,Map<String,String>> matchInfo;
private Map<String,Boolean> matchQuit;
private Map<String,Map<String,Object>> playersInfo;
private Map<String,Object> chessGameState;
private Map<String,String> users;
private Map<String,String> loginPlayers;
private  Map<String,String> availablePlayers;
private Map<String,java.util.List<String>> invitations;
private Map<String,java.util.List<String>> invitationsAccepted;
private int playersCount=0;
private NFrameworkServer server;
private static ServerChessFrame serverChessFrame=null;
int startRowIndex,startColumnIndex,destinationRowIndex,destinationColumnIndex;
 private ServerChessFrame()
{
users=new MemberDAO().getAll();
matchQuit=new HashMap<>();
loginPlayers=new HashMap<>();
invitationsAccepted=new HashMap<>();
invitations=new HashMap<>();
matchInfo=new HashMap<>();
availablePlayers=new HashMap<>();
this.piecesList=new ArrayList<>();
this.playersInfo=new HashMap<>();
}
public static ServerChessFrame getServerChessFrame()
{
if(serverChessFrame==null) 
{
serverChessFrame=new ServerChessFrame();
}
return serverChessFrame;
}
public boolean login(String username,String password)
{
//check if  already loggedin or not
//if(loginPlayers.get(username)!=null) return false;
/*
MemberDAO memberDAO=new MemberDAO();
MemberDTO memberDTO=memberDAO.getByUsername(username);
if(memberDTO==null) return false;

if(password.equals(memberDTO.getPassword())==false) return false;
*/
String _password=users.get(username);
if(_password==null) return false;
if(_password.equals(password)==false) return false;
availablePlayers.put(username,password);
loginPlayers.put(username,password);

return true;
}
public void logout(String username)
{
availablePlayers.remove(username);
loginPlayers.remove(username);
}
public void setAvailable(String username)
{
playersCount--;
availablePlayers.put(username,loginPlayers.get(username));
}
public void setBusy(String username)
{
availablePlayers.remove(username);
}

public void sendInvitationTo(String from,String to)
{
java.util.List<String> list=invitations.get(to);
if(list==null)
{
list=new ArrayList<>();
invitations.put(to,list);
}
list.add(from);
}
public void invitationAccepted(String from,String of)
{
java.util.List<String> list=invitationsAccepted.get(of);
if(list==null) 
{
list=new ArrayList<>();
invitationsAccepted.put(of,list);
}
list.add(from);
}
public java.util.List<String> invitationReply(String player)
{
java.util.List<String> list=invitationsAccepted.get(player);
if(list==null) list=new ArrayList<>();
else invitationsAccepted.remove(player);
return list;
}
public java.util.List<String> areThereAnyInvitation(String forPlayer)
{
java.util.List<String> list=invitations.get(forPlayer);
if(list==null) list=new ArrayList<>();
else invitations.remove(forPlayer);
//System.out.println(list.size());
return list;
}
public boolean isPlayerLeft(String uuid)
{
boolean left=matchQuit.get(uuid);
if(left)
{
matchQuit.remove(uuid);
}
return left;
}
public void quitGame(String uuid)
{
matchQuit.remove(uuid);
matchQuit.put(uuid,true);
}

public ArrayList<String> getAvailablePlayers()
{
ArrayList<String> list=new ArrayList<>();
availablePlayers.forEach((k,v)->{
list.add(k);
});
return list;
}

public void setChessGameState(Map<String,Object> chessGameState)
{
this.chessGameState=new HashMap<String,Object>();
String playerName=(String)(chessGameState.get("playerName"));
boolean gameEnd=(Boolean)(chessGameState.get("gameEnd"));
System.out.println("Game ends : "+gameEnd);
this.chessGameState.put("playerName",playerName);
this.chessGameState.put("gameEnd",gameEnd);
int r1=((Double)chessGameState.get("sourceRow")).intValue();
int c1=((Double)chessGameState.get("sourceColumn")).intValue();
int r2=((Double)chessGameState.get("destinationRow")).intValue();
int c2=((Double)chessGameState.get("destinationColumn")).intValue();
char turn=((String)chessGameState.get("turn")).charAt(0);
boolean castling=(Boolean)chessGameState.get("castling");
boolean pawnPromotion=(Boolean)chessGameState.get("pawnPromotion");
boolean h=(Boolean)chessGameState.get("pawnPromotion");
System.out.println("PAWN : "+h);
String pawnPromotedTo="";
System.out.printf("(%d,%d) -> (%d,%d) turn : %c\n",r1,c1,r2,c2,turn);
System.out.println("Castling : "+castling);
System.out.println("pawn promotion : "+pawnPromotion+" : pawnPromotedTo :  "+pawnPromotedTo);
this.chessGameState.put("sourceRow",r1);
this.chessGameState.put("sourceColumn",c1);
this.chessGameState.put("destinationRow",r2);
this.chessGameState.put("destinationColumn",c2);
this.chessGameState.put("turn",turn);
this.chessGameState.put("castling",castling);
this.chessGameState.put("pawnPromotion",pawnPromotion);
if(pawnPromotion)
{
pawnPromotedTo=(String)chessGameState.get("pawnPromotedTo");
this.chessGameState.put("pawnPromotedTo",pawnPromotedTo);
}
if(castling)
{
int cr1=((Double)chessGameState.get("castlingRow1")).intValue();
int cc1=((Double)chessGameState.get("castlingColumn1")).intValue();
int cr2=((Double)chessGameState.get("castlingRow2")).intValue();
int cc2=((Double)chessGameState.get("castlingColumn2")).intValue();
System.out.printf("(%d,%d)->(%d,%d) : ",cr1,cc1,cr2,cc2);
this.chessGameState.put("castlingRow1",cr1);
this.chessGameState.put("castlingColumn1",cc1);
this.chessGameState.put("castlingRow2",cr2);
this.chessGameState.put("castlingColumn2",cc2);
}

this.playersInfo.remove(playerName);
this.playersInfo.put(playerName,this.chessGameState);
}
public Map<String,Object> getChessGameState(String uuid,String playerName)
{
Map<String,String> match=matchInfo.get(uuid);
String player1=match.get("player1");
String player2=match.get("player2");
String opponentPlayerName;
if(playerName.equals(player1))
{
opponentPlayerName=player2;
}
else
{
opponentPlayerName=player1;
}
//System.out.println("Opponent : "+opponentPlayerName);
Map<String,Object> chessGameState=this.playersInfo.get(opponentPlayerName);
this.playersInfo.remove(opponentPlayerName);
return chessGameState;
}

public String initializeClientInfo(String playerName)
{
String uuid=null;
playersCount++;
System.out.println("Player Name : "+playerName);
this.playersInfo.put(playerName,null);
int playerNumber=playersCount%2;
if(playerNumber==0) playerNumber+=2;
System.out.println("PLAYER NUMBER INTIALIZE : "+playerNumber);
if(playerNumber==1)
{
System.out.println("First player");
Map<String,String> map=new HashMap<>();
map.put("player1",playerName);
uuid=UUID.randomUUID().toString();
matchInfo.put(uuid,map);
matchQuit.put(uuid,false);
}
else if(playerNumber==2)
{

Set<Map.Entry<String,Map<String,String>>> s;
s=matchInfo.entrySet();
Iterator<Map.Entry<String,Map<String,String>>> it;
it=s.iterator();
Map.Entry<String,Map<String,String>> entry;
Map<String,String> match;
while(it.hasNext())
{
entry=it.next();
match=entry.getValue();
if(match.get("player2")==null)
{
uuid=entry.getKey();
System.out.println("Player 2 putting in same uuid of map key");
match.put("player2",playerName);
break;
}
}
}
System.out.println("Player : "+playerName+" UUID  : "+uuid);
return uuid;
}
public int getPlayerCount()
{
return this.playersCount;
}
public void setPiecesList(java.util.List<String> piecesList)
{
System.out.println("List "+piecesList.size());
this.piecesList=piecesList;
}
public java.util.List<String> getPiecesList()
{
java.util.List<String> piecesList=new ArrayList<>();
for(String pieceName:this.piecesList)
{
piecesList.add(pieceName);
}
return piecesList;
}

public void actionPerformed(ActionEvent ev)
{

}
public static void main(String gg[])
{
ServerChessFrame scf=getServerChessFrame();
NFrameworkServer server=new NFrameworkServer();
server.registerClass(ServerChessUpdater.class);
server.start();
}
}