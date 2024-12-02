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
private Map<String,Map<String,Object>> playersInfo;
private Map<String,Object> chessGameState;
private int playersCount=0;
private NFrameworkServer server;
private JButton[][] tiles;
private static ServerChessFrame serverChessFrame=null;
int startRowIndex,startColumnIndex,destinationRowIndex,destinationColumnIndex;
public static ServerChessFrame getServerChessFrame()
{
if(serverChessFrame==null) 
{
serverChessFrame=new ServerChessFrame();
}
return serverChessFrame;
}
public void setMatch()
{
}
public void setChessGameState(Map<String,Object> chessGameState)
{
this.chessGameState=new HashMap<String,Object>();
String playerName=(String)(chessGameState.get("playerName"));
int r1=((Double)chessGameState.get("sourceRow")).intValue();
int c1=((Double)chessGameState.get("sourceColumn")).intValue();
int r2=((Double)chessGameState.get("destinationRow")).intValue();
int c2=((Double)chessGameState.get("destinationColumn")).intValue();
char turn=((String)chessGameState.get("turn")).charAt(0);
boolean castling=(Boolean)chessGameState.get("castling");
System.out.printf("(%d,%d) -> (%d,%d) turn : %c\n",r1,c1,r2,c2,turn);
System.out.println("Castling : "+castling);

this.chessGameState.put("playerName",playerName);
this.chessGameState.put("sourceRow",r1);
this.chessGameState.put("sourceColumn",c1);
this.chessGameState.put("destinationRow",r2);
this.chessGameState.put("destinationColumn",c2);
this.chessGameState.put("turn",turn);
this.chessGameState.put("castling",castling);

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
System.out.println("Opponent : "+opponentPlayerName);
Map<String,Object> chessGameState=this.playersInfo.get(opponentPlayerName);
this.playersInfo.remove(opponentPlayerName);
return chessGameState;
}

public void setDone(boolean done)
{
this.done=done;
}
public boolean getDone()
{
return this.done;
}
public String initializeClientInfo(String playerName)
{
String uuid=null;
playersCount++;
System.out.println("Player Name : "+playerName);
this.playersInfo.put(playerName,null);
if(playersCount==1)
{
System.out.println("First player");
Map<String,String> map=new HashMap<>();
map.put("player1",playerName);
uuid=UUID.randomUUID().toString();
matchInfo.put(uuid,map);
}
else if(playersCount==2)
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
private ServerChessFrame()
{
matchInfo=new HashMap<>();
this.piecesList=new ArrayList<>();
this.playersInfo=new HashMap<>();
tiles=new JButton[8][8];
/*
container=getContentPane();
container.setLayout(new GridLayout(8,8));
JButton tile;
blackTile=new ImageIcon("images/lightBlack_tile.png");
whiteTile=new ImageIcon("images/grey_tile.png");
darkTileColor=new Color(70,70,70);
lightTileColor=new Color(240,240,240);
darkTileBorder=BorderFactory.createLineBorder(new Color(20,20,20),2);
lightTileBorder=BorderFactory.createLineBorder(new Color(255,255,255),2);
Color tileColor;
Border tileBorder;
blackRookIcon=new ImageIcon("images/black_rook.png");
blackKnightIcon=new ImageIcon("images/black_knight.png");
blackBishopIcon=new ImageIcon("images/black_bishop.png");
blackQueenIcon=new ImageIcon("images/black_queen.png");
blackKingIcon=new ImageIcon("images/black_king.png");
blackPawnIcon=new ImageIcon("images/black_pawn.png");


whiteRookIcon=new ImageIcon("images/white_rook.png");
whiteKnightIcon=new ImageIcon("images/white_knight.png");
whiteBishopIcon=new ImageIcon("images/white_bishop.png");
whiteQueenIcon=new ImageIcon("images/white_queen.png");
whiteKingIcon=new ImageIcon("images/white_king.png");
whitePawnIcon=new ImageIcon("images/white_pawn.png");



for(int e=0;e<8;e++)
{
for(int f=0;f<8;f++)
{
if(e%2==0)
{
if(f%2==0)
{
 tileColor=lightTileColor;
 tileBorder=darkTileBorder;
}
else
{
 tileColor=darkTileColor;
 tileBorder=lightTileBorder;
}
}
else
{
if(f%2==0) 
{
tileColor=darkTileColor;
tileBorder=lightTileBorder;
}
else 
{
tileColor=lightTileColor;
tileBorder=darkTileBorder;
}
}
tile=new JButton();
tile.setBackground(tileColor);
tile.setBorder(tileBorder);

tile.addActionListener(this);
tiles[e][f]=tile;
container.add(tile);
}
}
*/
}
public ArrayList<String> getPiecesName()
{
ArrayList<String> piecesName=new ArrayList<>();
for(int e=0;e<8;e++)
{
for(int f=0;f<8;f++)
{
piecesName.add(tiles[e][f].getActionCommand());
}
}
return piecesName;
}

public void movePiece(int startRowIndex,int startColumnIndex,int destinationRowIndex,int destinationColumnIndex)
{
JButton sourceTile=tiles[startRowIndex][startColumnIndex];
JButton targetTile=tiles[destinationRowIndex][destinationColumnIndex];
String sourceIconName=sourceTile.getActionCommand();
sourceTile.setActionCommand("");
targetTile.setActionCommand("");
targetTile.setActionCommand(sourceIconName);
/*
Color sourceTileColor=sourceTile.getBackground();
Color targetTileColor=targetTile.getBackground();
sourceTile.removeAll();
sourceTile.revalidate();
sourceTile.repaint();
sourceTile.setBackground(sourceTileColor);
targetTile.removeAll();
targetTile.revalidate();
targetTile.repaint();
targetTile.setBackground(targetTileColor);
ImageIcon pieceIcon=null;
String pieceName="";
if(sourceIconName.equals("blackPawn"))
{
pieceIcon=this.blackPawnIcon;
pieceName="blackPawn";
}else if(sourceIconName.equals("whitePawn"))
{
pieceIcon=this.whitePawnIcon;
pieceName="whitePawn";
}else if(sourceIconName.equals("blackRook"))
{
pieceIcon=this.blackRookIcon;
pieceName="blackRook";
}else if(sourceIconName.equals("whiteRook"))
{
pieceIcon=this.whiteRookIcon;
pieceName="whiteRook";
}else if(sourceIconName.equals("blackBishop"))
{
pieceIcon=this.blackBishopIcon;
pieceName="blackBishop";
}else if(sourceIconName.equals("whiteBishop"))
{
pieceIcon=this.whiteBishopIcon;
pieceName="whiteBishop";
}else if(sourceIconName.equals("blackKnight"))
{
pieceIcon=this.blackKnightIcon;
pieceName="blackKnight";
}else if(sourceIconName.equals("whiteKnight"))
{
pieceIcon=this.whiteKnightIcon;
pieceName="whiteKnight";
}else if(sourceIconName.equals("blackQueen"))
{
pieceIcon=this.blackQueenIcon;
pieceName="blackQueen";
}else if(sourceIconName.equals("whiteQueen"))
{
pieceIcon=this.whiteQueenIcon;
pieceName="whiteQueen";
}else if(sourceIconName.equals("blackKing"))
{
pieceIcon=this.blackKingIcon;
pieceName="blackKing";
}else if(sourceIconName.equals("whiteKing"))
{
pieceIcon=this.whiteKingIcon;
pieceName="whiteKing";
}

targetTile.setLayout(new BorderLayout());
targetTile.setActionCommand(pieceName);
targetTile.add(new JLabel(pieceIcon));
*/
}
public void setupBoard(String pieceName,int e,int f)
{
JButton tile=new JButton();
tile.setActionCommand(pieceName);
tiles[e][f]=tile;
}//populate pieces function ends here


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