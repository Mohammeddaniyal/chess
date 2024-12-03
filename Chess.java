import com.thinking.machines.nframework.client.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class Chess extends JFrame implements ActionListener
{
private Map<String,Object> chessGameState;
class UNDOMove
{
public JButton tile1,tile2;
public String name1,name2;
public Color tileColor1;
public Color tileColor2;
public int row1,row2,column1,column2;
public boolean castling;
public boolean pawnPromotion;
}
private boolean madeAMove=false;
private javax.swing.Timer timer;
private char playerPieceColor;
private boolean whiteKingMoved=false;
private boolean rightWhiteRookMoved=false;
private boolean leftWhiteRookMoved=false;
private boolean blackKingMoved=false;
private boolean rightBlackRookMoved=false;
private boolean leftBlackRookMoved=false;
private boolean white=true;
private boolean black=false;
private KingCastling whiteKingCastling;
private KingCastling blackKingCastling;
private NFrameworkClient client;
private ButtonPanel buttonPanel;
private JPanel boardPanel;
private JButton[][] tiles;
private boolean[][] possibleMoves;
private ImageIcon blackTile;
private ImageIcon whiteTile;
private Container container;
private JButton sourceTile=null;
private JButton targetTile=null;
private boolean click=false;
private Color darkTileColor;
private Color lightTileColor;
private Border darkTileBorder;
private Border lightTileBorder;
private boolean undo=false;
private ImageIcon blackRookIcon;
private ImageIcon blackKnightIcon;
private ImageIcon blackBishopIcon;
private ImageIcon blackQueenIcon;
private ImageIcon blackKingIcon;
private ImageIcon blackPawnIcon;
private ImageIcon whiteRookIcon;
private ImageIcon whiteKnightIcon;
private ImageIcon whiteBishopIcon;
private ImageIcon whiteQueenIcon;
private ImageIcon whiteKingIcon;
private ImageIcon whitePawnIcon;
private UNDOMove undoMove;
private boolean undoMoveValid=true;
private int startRowIndex,startColumnIndex,destinationRowIndex,destinationColumnIndex;
private String playerName;
private int playerNumber;
private String uuid;
public Chess(String playerName)
{
this.playerName=playerName;
//client connected to server
client=new NFrameworkClient();
//now update the information of player to the connected server

try
{
uuid=(String)client.execute("/serverChessUpdater/initialize",this.playerName);
//System.out.println(uuid);
//Map<String,Object> m=new HashMap<>();
playerNumber=((Double)client.execute("/serverChessUpdater/getPlayerNumber",new Object[0])).intValue();
//playerNumber=((Double)m.get("playerNumber")).intValue();
//uuid=(String)m.get("uuid");
System.out.println("Player number : "+playerNumber);
System.out.println("UUID : "+uuid);
}catch(Throwable exception)
{
System.out.println("Exception : "+exception);
}
//we got player number in our hand



undoMove=new UNDOMove();
whiteKingCastling=new KingCastling();
whiteKingCastling.kingMoved=whiteKingMoved;
whiteKingCastling.leftRookMoved=leftWhiteRookMoved;
whiteKingCastling.rightRookMoved=rightWhiteRookMoved;
blackKingCastling=new KingCastling();
blackKingCastling.kingMoved=blackKingMoved;
blackKingCastling.leftRookMoved=leftBlackRookMoved;
blackKingCastling.rightRookMoved=rightBlackRookMoved;
tiles=new JButton[8][8];
boardPanel=new JPanel();
boardPanel.setLayout(new GridLayout(8,8));
container=getContentPane();
container.setLayout(new BorderLayout());
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
tile=null;
if(playerNumber==1)tile=setupBoard(e,f,tileColor,tileBorder);
else if(playerNumber==2)
{
tile=new JButton();
tile.setBackground(tileColor);
tile.setActionCommand("");
tile.setBorder(tileBorder);
}
tile.addActionListener(this);
tiles[e][f]=tile;
boardPanel.add(tile);
}
}
Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
int width=650;
int height=600;
setSize(width,height);
int x=(d.width/2)-(width/2);
int y=(d.height/2)-(height/2);
buttonPanel=new ButtonPanel();
container.add(boardPanel,BorderLayout.CENTER);
container.add(buttonPanel,BorderLayout.EAST);
setLocation(x,y);
setVisible(true);
setTitle(playerName+" - Chess");
timer=new javax.swing.Timer(1000,new ActionListener()
{
public void actionPerformed(ActionEvent ev)
{
System.out.println("Timer started, player : "+playerNumber);
try
{
/*
boolean done=(Boolean)(client.execute("/serverChessUpdater/getDone",new Object[0]));

if(done==false)
{
System.out.println("Done is false,opponent haven't moved yet");
return;
}
*/
Map<String,Object> chessGameState=(Map<String,Object>)client.execute("/serverChessUpdater/getChessGameState",uuid,playerName);
if(chessGameState==null)
{
System.out.println("cannot update because chessgamestate is null");
return;
}
updateChessGameState(chessGameState);
}catch(Throwable exception)
{
System.out.println("Exception : "+exception);
return;
}
System.out.println("Switching off timer acc. to condition");
System.out.println("Enabling board");
Chess.this.setEnabled(true);
timer.stop();
}
});
//now passing the board state to server to get board state
if(playerNumber==1)
{
playerPieceColor='w';
//pass the ds to server for updation
System.out.println("PLAYER 1");
java.util.List<String> pieces=new ArrayList<>();
for(int e=0;e<8;e++)
{
for(int f=0;f<8;f++)
{
pieces.add(tiles[e][f].getActionCommand());
}
}
try
{
client.execute("/serverChessUpdater/populatePieces",pieces);
setEnabled(false);
while(true)
{
int count=((Double)client.execute("/serverChessUpdater/getPlayerNumber",new Object[0])).intValue();

//(client.execute("/serverChessUpdater/getPlayerNumber",new Object[0])).intValue();
//System.out.print(count);
if(count==2)
{
//System.out.println("Count : "+count);
setEnabled(true);
break;
}
}
}catch(Throwable exception)
{
System.out.println("Exception : "+exception);
}

}
else
{
this.playerPieceColor='b';
//if it's 2nd player it will get the board state DS from server
System.out.println("PLAYER 2");
try
{
java.util.List<String> pieces=(java.util.List<String>)client.execute("/serverChessUpdater/getPiecesList",new Object[0]);
System.out.println("List Size : "+pieces.size());
setupClientBoard(pieces);
}catch(Throwable exception)
{
System.out.println("Exception : "+exception);
}
//disable board of player 2 because it's player 1 turn
setEnabled(false);
//switch on the timer
timer.start();

}

}

private void updateChessGameState(Map<String,Object> chessGameState)
{
String playerName=(String)(chessGameState.get("playerName"));
int r1=((Double)chessGameState.get("sourceRow")).intValue();
int c1=((Double)chessGameState.get("sourceColumn")).intValue();
int r2=((Double)chessGameState.get("destinationRow")).intValue();
int c2=((Double)chessGameState.get("destinationColumn")).intValue();
char turn=((String)chessGameState.get("turn")).charAt(0);
boolean castling=(Boolean)chessGameState.get("castling");
boolean pawnPromotion=(Boolean)chessGameState.get("pawnPromotion");
boolean gameEnd=(Boolean)chessGameState.get("gameEnd");
System.out.printf("(%d,%d) -> (%d,%d) turn : %c\n",r1,c1,r2,c2,turn);
System.out.println("Castling : "+castling);
System.out.println("PAWN : "+pawnPromotion);

this.sourceTile=tiles[r1][c1];
this.targetTile=tiles[r2][c2];
movePiece(this.sourceTile.getActionCommand());

System.out.println("Piece moved");
if(pawnPromotion)
{
String pawnPromotedTo=(String)chessGameState.get("pawnPromotedTo");
System.out.println("pawnPromotion "+pawnPromotedTo);
String color=pawnPromotedTo.substring(0,5);
System.out.println("Color : "+color);
//updating for pawn tile where to promote pawn
this.destinationRowIndex=r2;
this.destinationColumnIndex=c2;
promotePawn(color,pawnPromotedTo,getPieceIconByName(pawnPromotedTo));
}

if(castling)
{
System.out.println("1");
int cr1=((Double)chessGameState.get("castlingRow1")).intValue();
System.out.println("2");
int cc1=((Double)chessGameState.get("castlingColumn1")).intValue();
System.out.println("3");
int cr2=((Double)chessGameState.get("castlingRow2")).intValue();
System.out.println("4");
int cc2=((Double)chessGameState.get("castlingColumn2")).intValue();
System.out.println("4");

System.out.printf("(%d,%d)->(%d,%d) : ",cr1,cc1,cr2,cc2);
System.out.println("Castling case");
this.sourceTile=tiles[cr1][cc1];
this.targetTile=tiles[cr2][cc2];
movePiece(this.sourceTile.getActionCommand());
System.out.println("castling done");
}

reset();
if(turn=='b')
{ 
if(gameEnd)
{
System.out.println("BLACK PLAYER, WHITE WINS");
reset();
JOptionPane.showMessageDialog(this,"White wins!","Game over",JOptionPane.INFORMATION_MESSAGE);
setEnabled(false);
return;
}
this.black=true;
this.white=false;
}
else {
if(gameEnd)
{
System.out.println("WHITE PLAYER, BLACK WINS");
reset();
JOptionPane.showMessageDialog(this,"Black wins!","Game over",JOptionPane.INFORMATION_MESSAGE);
setEnabled(false);
return;
}

this.white=true;
this.black=false;
}
}

private void setupClientBoard(java.util.List<String> piecesList)
{
System.out.println("setupClientBoard called");
int e=0;
int f=0;
for(String pieceName:piecesList)
{
tiles[e][f].setActionCommand(pieceName);
tiles[e][f].add(new JLabel(getPieceIconByName(pieceName)));
tiles[e][f].repaint();
tiles[e][f].revalidate();
f++;
if(f==8)
{
f=0;
e++;
}
if(e==8) break;
}//for loop ends
}//method ends

private void updateServerBoard()
{
ArrayList<String> piecesName=new ArrayList<>();
for(int e=0;e<8;e++)
{
for(int f=0;f<8;f++)
{
piecesName.add(tiles[e][f].getActionCommand());
}
}
try
{
client.execute("/serverChessUpdater/populatePieces",piecesName);
}catch(Throwable t)
{
System.out.println(t.getMessage());
}
System.out.println("Pieces sent");
}

private void updateChange()
{
try
{
/*
ArrayList<Integer> indexes=new ArrayList<>();
indexes.add(new Integer(this.startRowIndex));
indexes.add(new Integer(this.startColumnIndex));
indexes.add(new Integer(this.destinationRowIndex));
indexes.add(new Integer(this.destinationColumnIndex));
*/
//Integer indexes[]=new Integer[]{this.startRowIndex,this.startColumnIndex,this.destinationRowIndex,this.destinationColumnIndex};
//client.execute("/serverChessUpdater/updateBoard",indexes);
//System.out.println(this.startRowIndex+"/"+this.startColumnIndex+"/"+this.destinationRowIndex+"/"+this.destinationColumnIndex);
//client.execute("/serverChessUpdater/updateBoard",new Integer(startRowIndex),new Integer(startColumnIndex),new Integer(destinationRowIndex),new Integer(destinationColumnIndex));
client.execute("/serverChessUpdater/updateBoard",startRowIndex,startColumnIndex,destinationRowIndex,destinationColumnIndex);

}catch(Throwable t)
{
System.out.println(t.getMessage());
}
System.out.println("Piece move");
}
private void reset()
{
this.click=false;
this.targetTile=null;
this.sourceTile=null;
startRowIndex=-1;
destinationRowIndex=-1;
startColumnIndex=-1;
destinationColumnIndex=-1;
if(possibleMoves!=null)
{
Color tileColor=null;
Border tileBorder=null;
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
tiles[e][f].setBackground(tileColor);
tiles[e][f].setBorder(tileBorder);
//tiles[e][f].setBorder(UIManager.getBorder("Button.border"));
possibleMoves[e][f]=false;
}
}
}
/*
//resetting undomove
undoMove.name1="";
undoMove.name2="";
undoMove.tileColor1=null;
undoMove.tileColor2=null;
undoMove.row1=-1;
undoMove.row2=-1;
undoMove.column1=-1;
undoMove.column2=-1;
undoMove.castling=false;
undoMove.castling=false;
*/	
}
private JButton setupBoard(int e,int f,Color tileColor,Border tileBorder)
{	
JButton tile=new JButton();
tile.setBackground(tileColor);
tile.setBorder(tileBorder);
//generating black piece
if(e==0 || e==1)//for black pieces
{
tile.setLayout(new BorderLayout());
if(e==0)
{
if((f==0 ||f==7) )  
{
JLabel blackRookLabel=new JLabel(blackRookIcon);
tile.setActionCommand("blackRook");
tile.add(blackRookLabel,BorderLayout.CENTER);
}else
if((f==1 || f==6 ))
{
tile.setActionCommand("blackKnight");
tile.add(new JLabel(blackKnightIcon));
}else
if((f==2 || f==5 ))
{
tile.setActionCommand("blackBishop");
tile.add(new JLabel(blackBishopIcon));
}else
if(f==3 )
{
tile.setActionCommand("blackQueen");
tile.add(new JLabel(blackQueenIcon));
}else
if(f==4 )
{
tile.setActionCommand("blackKing");
tile.add(new JLabel(blackKingIcon));
}
}else
if(e==1)
{
tile.add(new JLabel(blackPawnIcon));
tile.setActionCommand("blackPawn");
}
}
//generating black piece ends here
//generating white piece starts here
else 
if(e==6 || e==7)
{
JLabel whiteRookLabel=new JLabel(whiteRookIcon);
if(e==7)
{
if((f==0 ||f==7) )  
{
tile.setActionCommand("whiteRook");
tile.add(whiteRookLabel,BorderLayout.CENTER);
}else
if((f==1 || f==6 ))
{
tile.setActionCommand("whiteKnight");
tile.add(new JLabel(whiteKnightIcon));
}else
if((f==2 || f==5 ))
{
tile.setActionCommand("whiteBishop");
tile.add(new JLabel(whiteBishopIcon));
}else
if(f==3 )
{
tile.setActionCommand("whiteQueen");
tile.add(new JLabel(whiteQueenIcon));
}else
if(f==4 )
{
tile.setActionCommand("whiteKing");
tile.add(new JLabel(whiteKingIcon));
}
}else
if(e==6)
{
tile.setActionCommand("whitePawn");
tile.add(new JLabel(whitePawnIcon));
}
}
//generating white piece ends here
return tile;
}

public void actionPerformed(ActionEvent ev)
{
boolean found=false;
JButton tile=null;
int e=0;
int f=0;
for(e=0;e<8;e++)
{
for(f=0;f<8;f++)
{
tile=tiles[e][f];
if(tile==ev.getSource())
{
found=true;
break;
}
}
if(found) break;
}

//clicking again on the same piece
//clicking the empty tile
if(this.sourceTile==null && tile.getActionCommand().equals("")) 
{
tile.setEnabled(false);
tile.setEnabled(true);
return;
}
if(tile==this.sourceTile)
{
this.sourceTile.setBorder(UIManager.getBorder("Button.border"));//for making the button border as the default as system
sourceTile.setEnabled(false);//doing this trick to remove foucs from button
sourceTile.setEnabled(true);
reset();
return;
}


if(click==false) //clicking on the source tile
{
String pieceName=tile.getActionCommand();
if(pieceName.equals("")) 
{
reset();
return;
}
this.sourceTile=tile;
String pieceColor=this.sourceTile.getActionCommand().substring(0,5);
if(this.white && pieceColor.equals("black")) 
{
System.out.println("white turn,Not black");
return;
}
else if(this.black && pieceColor.equals("white")) 
{
System.out.println("black turn,Not white");
return;
}
startRowIndex=e;
startColumnIndex=f;
click=true;
this.sourceTile.setBorder(BorderFactory.createLineBorder(Color.GREEN,3));
this.sourceTile.setBackground(new Color(144,238,144));
KingCastling kingCastling=null;
if(pieceName.equals("whiteKing"))
{
kingCastling=whiteKingCastling;
}else if(pieceName.equals("blackKing"))
{
kingCastling=blackKingCastling;
}
possibleMoves=CheckmateDetector.getPossibleMoves(tiles,startRowIndex,startColumnIndex,kingCastling);

JButton validTile;
for(e=0;e<8;e++)
{
for(f=0;f<8;f++)
{
//System.out.print(possibleMoves[e][f]+"  ");
if(possibleMoves[e][f]==false) continue;
validTile=tiles[e][f];
//validTile.setBorder(BorderFactory.createDashedBorder(Color.RED,30,10));
Color c;
//c=new Color(32,178,170);
c=Color.GREEN;
//c=new Color(144,238,144);
//validTile.setBackground(c);
validTile.setBorder(BorderFactory.createLineBorder(c,3));
}
//System.out.println();
}

}
else //clicking on the target tile
{
destinationRowIndex=e;
destinationColumnIndex=f;
click=false;
this.targetTile=tile;


String sourceIconName=this.sourceTile.getActionCommand();
String targetIconName=this.targetTile.getActionCommand();

String sourceIconPieceColor=sourceIconName.substring(0,5);
String targetIconPieceColor="";
boolean capture=false;
int rowIndex=0;
int columnIndex=0;
if(targetIconName.equals("")==false)
{
capture=true;
rowIndex=destinationRowIndex;
columnIndex=destinationColumnIndex;
targetIconPieceColor=targetIconName.substring(0,5);
}
if(capture && targetIconPieceColor.equals(sourceIconPieceColor))
{
System.out.println("bye bye same color");
this.sourceTile.setBorder(UIManager.getBorder("Button.border"));
targetTile.setEnabled(false);
targetTile.setEnabled(true);
reset();
return;
}
//boolean validMove=validMovement(sourceIconName);
boolean validMove=possibleMoves[this.destinationRowIndex][this.destinationColumnIndex];
if(validMove==false) 
{
this.sourceTile.setBorder(UIManager.getBorder("Button.border"));
targetTile.setEnabled(false);
targetTile.setEnabled(true);
reset();
return;
}
this.sourceTile.setBorder(BorderFactory.createLineBorder(Color.GREEN,3));
undoMove.name1=this.sourceTile.getActionCommand();
undoMove.name2=this.targetTile.getActionCommand();
undoMove.tileColor1=this.sourceTile.getBackground();
undoMove.tileColor2=this.targetTile.getBackground();
undoMove.row1=this.startRowIndex;
undoMove.row2=this.destinationRowIndex;
undoMove.column1=this.startColumnIndex;
undoMove.column2=this.destinationColumnIndex;
undoMove.castling=false;
undoMove.pawnPromotion=false;
chessGameState=new HashMap<>();
chessGameState.put("playerName",playerName);
chessGameState.put("gameEnd",false);
chessGameState.put("sourceRow",this.startRowIndex);
chessGameState.put("sourceColumn",this.startColumnIndex);
chessGameState.put("destinationRow",this.destinationRowIndex);
chessGameState.put("destinationColumn",this.destinationColumnIndex);
chessGameState.put("turn",(playerPieceColor=='b'?'w':'b'));

movePiece(sourceIconName);
buttonPanel.setDoneEnable(true);
buttonPanel.setUNDOEnable(undoMoveValid);
//if(white && undoMoveValid==false)buttonPanel.setUNDOEnable(true);
//else if(black && undoMoveValid==true) buttonPanel.setUNDOEnable(true);
this.sourceTile.setBorder(UIManager.getBorder("Button.border"));
//pawn promotion case
if(sourceIconName.equals("whitePawn") && this.destinationRowIndex==0)
{
undoMove.pawnPromotion=true;
promotePawn("white",null,null);
}
else if(sourceIconName.equals("blackPawn") && this.destinationRowIndex==7)
{
undoMove.pawnPromotion=true;
promotePawn("black",null,null);
}

chessGameState.put("pawnPromotion",undoMove.pawnPromotion);


//king castling
if(sourceIconName.equals("whiteKing"))
{
if(whiteKingMoved==false && destinationColumnIndex==6)
{
//king's side castling
undoMove.castling=true;
System.out.println("King's side castling case (white)");
this.startRowIndex=7;
this.startColumnIndex=7;
this.destinationRowIndex=7;
this.destinationColumnIndex=5;
this.sourceTile=tiles[startRowIndex][startColumnIndex];
this.targetTile=tiles[destinationRowIndex][destinationColumnIndex];
movePiece("whiteRook");



}
else if(whiteKingMoved==false && destinationColumnIndex==2)
{
//queen's side castling
undoMove.castling=true;
System.out.println("queen's side castling case (white)");
this.startRowIndex=7;
this.startColumnIndex=0;
this.destinationRowIndex=7;
this.destinationColumnIndex=3;
this.sourceTile=tiles[startRowIndex][startColumnIndex];
this.targetTile=tiles[destinationRowIndex][destinationColumnIndex];
movePiece("whiteRook");
}
}
else if(sourceIconName.equals("blackKing"))
{
if(blackKingMoved==false && destinationColumnIndex==6)
{
//king's side castling
undoMove.castling=true;
System.out.println("King's side castling case (black)");
this.startRowIndex=0;
this.startColumnIndex=7;
this.destinationRowIndex=0;
this.destinationColumnIndex=5;
this.sourceTile=tiles[startRowIndex][startColumnIndex];
this.targetTile=tiles[destinationRowIndex][destinationColumnIndex];
movePiece("blackRook");
}
else if(blackKingMoved==false && destinationColumnIndex==2)
{
//queen's side castling
undoMove.castling=true;
System.out.println("queen's side castling case (black)");
this.startRowIndex=0;
this.startColumnIndex=0;
this.destinationRowIndex=0;
this.destinationColumnIndex=3;
this.sourceTile=tiles[startRowIndex][startColumnIndex];
this.targetTile=tiles[destinationRowIndex][destinationColumnIndex];
movePiece("blackRook");
}
}

chessGameState.put("castling",undoMove.castling);
if(undoMove.castling)
{
//updating chess game state for castling case
System.out.println("Castling :");
System.out.printf("(%d,%d)->(%d,%d) : ",this.startRowIndex,this.startColumnIndex,this.destinationRowIndex,this.destinationColumnIndex);
chessGameState.put("castlingRow1",this.startRowIndex);
chessGameState.put("castlingColumn1",this.startColumnIndex);
chessGameState.put("castlingRow2",this.destinationRowIndex);
chessGameState.put("castlingColumn2",this.destinationColumnIndex);
}

//switching black/white turn
if(white) 
{

if(whiteKingMoved==false)
{
if(tiles[7][4].getActionCommand().equals("whiteKing")==false)
{
System.out.println("White King moved : "+whiteKingMoved);
whiteKingMoved=true;
whiteKingCastling.kingMoved=true;
}
}
if(leftWhiteRookMoved==false)
{
if(tiles[7][0].getActionCommand().equals("whiteRook")==false)
{
System.out.println("Left White Rook moved : "+rightWhiteRookMoved);
leftWhiteRookMoved=true;
whiteKingCastling.leftRookMoved=true;
}
}
if(rightWhiteRookMoved==false)
{
if(tiles[7][7].getActionCommand().equals("whiteRook")==false)
{
System.out.println("Right White Rook moved : "+leftWhiteRookMoved);
rightWhiteRookMoved=true;
whiteKingCastling.rightRookMoved=true;
}
}
if(targetIconName.equals("blackKing"))
{
reset();
JOptionPane.showMessageDialog(this,"White wins!","Game over",JOptionPane.INFORMATION_MESSAGE);
setEnabled(false);
return;
}
//checking is king in danger or not
if(CheckmateDetector.detectCheckmate(tiles,"black"))
{
reset();

try{
//chessGameState=new HashMap<>();
chessGameState.remove("gameEnd");
//chessGameState.put("playerName",playerName);
chessGameState.put("gameEnd",true);
client.execute("/serverChessUpdater/setMap",chessGameState);
}catch(Throwable exception)
{
}

JOptionPane.showMessageDialog(this,"White wins!","Game over",JOptionPane.INFORMATION_MESSAGE);
setEnabled(false);
return;
}
white=false;
black=true;
}
else
{
if(blackKingMoved==false)
{
if(tiles[0][4].getActionCommand().equals("blackKing")==false)
{
blackKingMoved=true;
}
}
if(leftBlackRookMoved==false)
{
if(tiles[0][0].getActionCommand().equals("blackRook")==false)
{
leftBlackRookMoved=true;
}
}
if(rightBlackRookMoved==false)
{
if(tiles[0][7].getActionCommand().equals("blackRook")==false)
{
leftBlackRookMoved=true;
}
}
if(targetIconName.equals("whiteKing"))
{
reset();
JOptionPane.showMessageDialog(this,"Black wins!","Game over",JOptionPane.INFORMATION_MESSAGE);
setEnabled(false);
return;
}
if(CheckmateDetector.detectCheckmate(tiles,"white"))
{
reset();
try{
//chessGameState=new HashMap<>();
chessGameState.remove("gameEnd");
//chessGameState.put("playerName",playerName);
chessGameState.put("gameEnd",true);
client.execute("/serverChessUpdater/setMap",chessGameState);
}catch(Throwable exception)
{
}

JOptionPane.showMessageDialog(this,"Black wins!","Game over",JOptionPane.INFORMATION_MESSAGE);
setEnabled(false);
return;
}
white=true;
black=false;
}
reset();
}
}
private void undoMove()
{
undoMoveValid=false;
buttonPanel.setDoneEnable(false);
if(white==false)
{
System.out.println("last turn was white's");
white=true;
black=false;
}
else{
System.out.println("last turn was black's");
white=false;
black=true;
}
JButton tile1=tiles[undoMove.row1][undoMove.column1];
JButton tile2=tiles[undoMove.row2][undoMove.column2];

undoMoveUpdateBoard(tile1,tile2);

if(undoMove.castling)
{
if(undoMove.row1==7 && undoMove.column1==4)
{
undoMove.name1="";
undoMove.name2="whiteRook";
//white king castling
if(undoMove.row2==7 && undoMove.column2==6)
{
System.out.println("white king undo king's side castling");
tile1=tiles[7][7];
tile2=tiles[7][5];
}
else
{
System.out.println("white king undo king's side castling");
tile1=tiles[7][0];
tile2=tiles[7][3];
}
}
else if(undoMove.row1==0 && undoMove.column1==4)
{
undoMove.name1="";
undoMove.name2="blackRook";
//black king castling
if(undoMove.row2==0 && undoMove.column2==6)
{
System.out.println("black king undo king's side castling");
tile1=tiles[0][7];
tile2=tiles[0][5];
}
else
{
System.out.println("black king undo king's side castling");
tile1=tiles[0][0];
tile2=tiles[0][3];
}
}
undoMoveUpdateBoard(tile2,tile1);
}//undo castling part ends here
buttonPanel.setUNDOEnable(false);
}

private void undoMoveUpdateBoard(JButton tile1,JButton tile2)
{
tile1.removeAll();
tile1.setActionCommand("");
tile1.repaint();
tile1.revalidate();
tile2.removeAll();
tile2.setActionCommand("");
tile2.repaint();
tile2.revalidate();

ImageIcon pieceIcon1=getPieceIconByName(undoMove.name1);
ImageIcon pieceIcon2=null;
if(undoMove.name2.equals("")==false)
{
pieceIcon2=getPieceIconByName(undoMove.name2);
}
//tile1.setBackground(undoMove.tileColor1);
//tile2.setBackground(undoMove.tileColor2);
tile1.setLayout(new BorderLayout());
tile1.add(new JLabel(pieceIcon1));
tile1.setActionCommand(undoMove.name1);
if(pieceIcon2!=null)
{
tile2.setLayout(new BorderLayout());
tile2.setActionCommand(undoMove.name2);
tile2.add(new JLabel(pieceIcon2));
}
tile1.repaint();
tile1.revalidate();
tile2.repaint();
tile2.revalidate();
}

private ImageIcon getPieceIconByName(String iconName)
{
ImageIcon pieceIcon=null;
if(iconName.equals("blackPawn"))
{
pieceIcon=this.blackPawnIcon;
}else if(iconName.equals("whitePawn"))
{
pieceIcon=this.whitePawnIcon;
}else if(iconName.equals("blackRook"))
{
pieceIcon=this.blackRookIcon;
}else if(iconName.equals("whiteRook"))
{
pieceIcon=this.whiteRookIcon;
}else if(iconName.equals("blackBishop"))
{
pieceIcon=this.blackBishopIcon;
}else if(iconName.equals("whiteBishop"))
{
pieceIcon=this.whiteBishopIcon;
}else if(iconName.equals("blackKnight"))
{
pieceIcon=this.blackKnightIcon;
}else if(iconName.equals("whiteKnight"))
{
pieceIcon=this.whiteKnightIcon;
}else if(iconName.equals("blackQueen"))
{
pieceIcon=this.blackQueenIcon;
}else if(iconName.equals("whiteQueen"))
{
pieceIcon=this.whiteQueenIcon;
}else if(iconName.equals("blackKing"))
{
pieceIcon=this.blackKingIcon;
}else if(iconName.equals("whiteKing"))
{
pieceIcon=this.whiteKingIcon;
}
return pieceIcon;
}
private void movePiece(String sourceIconName)
{
Color sourceTileColor=this.sourceTile.getBackground();
Color targetTileColor=this.targetTile.getBackground();
this.sourceTile.removeAll();
this.sourceTile.setActionCommand("");
this.sourceTile.revalidate();
this.sourceTile.repaint();
this.sourceTile.setBackground(sourceTileColor);
this.targetTile.removeAll();
this.targetTile.setActionCommand("");
this.targetTile.revalidate();
this.targetTile.repaint();
this.targetTile.setBackground(targetTileColor);
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
targetTile.setEnabled(false);
targetTile.setEnabled(true);
}
private boolean validMovement(String sourceIconName)
{

/*
if(sourceIconName.equals("blackKing") || sourceIconName.equals("whiteKing"))
{
KingCastling kingCastling=null;
if(sourceIconName.equals("whiteKing"))
{
kingCastling=whiteKingCastling;
}else if(sourceIconName.equals("blackKing"))
{
kingCastling=blackKingCastling;
}
return KingMoveValidator.validateMove(tiles,startRowIndex,startColumnIndex,destinationRowIndex,destinationColumnIndex,kingCastling);
//king validation part ends here
}else
if(sourceIconName.equals("whiteQueen") || sourceIconName.equals("blackQueen"))
{
return QueenMoveValidator.validateMove(startRowIndex,startColumnIndex,destinationRowIndex,destinationColumnIndex,tiles);
}else
if(sourceIconName.equals("whiteKnight") || sourceIconName.equals("blackKnight"))
{
return KnightMoveValidator.validateMove(startRowIndex,startColumnIndex,destinationRowIndex,destinationColumnIndex);
}else
if(sourceIconName.equals("whiteBishop") || sourceIconName.equals("blackBishop"))
{
return BishopMoveValidator.validateMove(startRowIndex,startColumnIndex,destinationRowIndex,destinationColumnIndex,tiles);
//Bishop validation ends here
}else
if(sourceIconName.equals("whiteRook") || sourceIconName.equals("blackRook"))
{
return RookMoveValidator.validateMove(startRowIndex,startColumnIndex,destinationRowIndex,destinationColumnIndex,tiles);
}else 
if(sourceIconName.equals("blackPawn") || sourceIconName.equals("whitePawn"))
{
return PawnMoveValidator.validateMove(startRowIndex,startColumnIndex,destinationRowIndex,destinationColumnIndex,tiles);
}
*/
return true;
}
private void promotePawn(String color,String promotePawnTo,ImageIcon promoteIcon)
{
String promoteTo[]={""};
ImageIcon image[]={null};
if(promotePawnTo==null && promoteIcon==null)
{
JPanel panel=new JPanel();
panel.setLayout(new GridLayout(4,1,5,0));
panel.setBackground(new Color(240,240,240));
JButton whiteQueen=new JButton(whiteQueenIcon);
whiteQueen.setBackground(Color.WHITE);
whiteQueen.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,2));
whiteQueen.setPreferredSize(new Dimension(80,50));

JButton whiteBishop=new JButton(whiteBishopIcon);
whiteBishop.setBackground(Color.WHITE);
whiteBishop.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,2));
whiteBishop.setPreferredSize(new Dimension(80,50));

JButton whiteRook=new JButton(whiteRookIcon);
whiteRook.setBackground(Color.WHITE);
whiteRook.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,2));
whiteRook.setPreferredSize(new Dimension(80,50));

JButton whiteKnight=new JButton(whiteKnightIcon);
whiteKnight.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,2));
whiteKnight.setBackground(Color.WHITE);
whiteKnight.setPreferredSize(new Dimension(80,50));

whiteQueen.addActionListener((ev)->{
promoteTo[0]="whiteQueen";
image[0]=whiteQueenIcon;
});

whiteBishop.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
promoteTo[0]="whiteBishop";
image[0]=whiteBishopIcon;
}
});

whiteRook.addActionListener((ev)->{
promoteTo[0]="whiteRook";
image[0]=whiteRookIcon;
});
whiteKnight.addActionListener((ev)->{
promoteTo[0]="whiteKnight";
image[0]=whiteKnightIcon;
});

JButton blackQueen=new JButton(blackQueenIcon);
blackQueen.setBackground(Color.WHITE);
blackQueen.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,2));
blackQueen.setPreferredSize(new Dimension(80,50));

JButton blackBishop=new JButton(blackBishopIcon);
blackBishop.setBackground(Color.WHITE);
blackBishop.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,2));
blackBishop.setPreferredSize(new Dimension(80,50));

JButton blackRook=new JButton(blackRookIcon);
blackRook.setBackground(Color.WHITE);
blackRook.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,2));
blackRook.setPreferredSize(new Dimension(80,50));

JButton blackKnight=new JButton(blackKnightIcon);
blackKnight.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,2));
blackKnight.setBackground(Color.WHITE);
blackKnight.setPreferredSize(new Dimension(80,50));

blackQueen.addActionListener((ev)->{
promoteTo[0]="blackQueen";
image[0]=blackQueenIcon;
});

blackBishop.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
promoteTo[0]="blackBishop";
image[0]=blackBishopIcon;
}
});

blackRook.addActionListener((ev)->{
promoteTo[0]="blackRook";
image[0]=blackRookIcon;
});
blackKnight.addActionListener((ev)->{
promoteTo[0]="blackKnight";
image[0]=blackKnightIcon;
});

	
if(color.equals("white"))
{
panel.add(whiteQueen);
panel.add(whiteBishop);
panel.add(whiteRook);
panel.add(whiteKnight);
promoteTo[0]="whiteQueen";
image[0]=whiteQueenIcon;
}
else
{
panel.add(blackQueen);
panel.add(blackBishop);
panel.add(blackRook);
panel.add(blackKnight);
promoteTo[0]="blackQueen";
image[0]=blackQueenIcon;
}


JOptionPane.showMessageDialog(this,panel,"Choose a piece",JOptionPane.PLAIN_MESSAGE);
//updating chessGameState for pawn promotion
chessGameState.put("pawnPromotedTo",promoteTo[0]);
}
else
{
promoteTo[0]=promotePawnTo;
image[0]=promoteIcon;
}
System.out.println("Promote pawn to : "+promotePawnTo);
JButton pawn=this.tiles[this.destinationRowIndex][this.destinationColumnIndex];
pawn.removeAll();
pawn.setActionCommand("");
pawn.repaint();
pawn.revalidate();
pawn.setActionCommand(promoteTo[0]);
pawn.setLayout(new BorderLayout());
pawn.add(new JLabel(image[0]));
pawn.repaint();
pawn.revalidate();


}

private class ButtonPanel extends JPanel implements ActionListener
{
private JButton undoButton;
private JButton doneButton;
ButtonPanel()
{
ImageIcon undoIcon=new ImageIcon("images/undo_32.png");
undoButton=new JButton(undoIcon);
undoButton.setForeground(Color.BLACK);
undoButton.setBackground(new Color(220,240,220));
ImageIcon doneIcon=new ImageIcon("images/done_32.png");
doneButton=new JButton(doneIcon);
undoButton.setBounds(100+200,20,50,50);
doneButton.setBackground(new Color(220,240,220));
doneButton.setForeground(Color.BLACK);
undoButton.setEnabled(false);
doneButton.setEnabled(false);

setBorder(BorderFactory.createEmptyBorder(20,10,20,10));
setBackground(new Color(240,240,240));
setLayout(new GridLayout(8,1));
undoButton.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
doneButton.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
add(new JLabel("       "));
add(new JLabel("       "));
add(new JLabel("       "));
add(new JLabel("       "));
add(new JLabel("       "));
add(new JLabel(" 	"));
add(undoButton);
add(doneButton);
undoButton.addActionListener(this);
doneButton.addActionListener(this);
}
public void setUNDOEnable(boolean enable)
{
undoButton.setEnabled(enable);
}
public void setDoneEnable(boolean enable)
{
doneButton.setEnabled(enable);
}

public void actionPerformed(ActionEvent ev)
{
if(ev.getSource()==undoButton)
{
Chess.this.undoMove();
undoButton.setEnabled(false);
}else if(ev.getSource()==doneButton)
{
//enabling undo move after a turn when a player used only one chance to undo
//at each turn
//resetting undo move button
Chess.this.undoMoveValid=true;
undoButton.setEnabled(false);
doneButton.setEnabled(false);
System.out.println("Done");
System.out.println("setting done at server");
try
{
client.execute("/serverChessUpdater/setMap",chessGameState);
}catch(Throwable exception)
{
System.out.println("Exception hello : "+exception.getMessage());
}
Chess.this.setEnabled(false);
timer.start();
}

}


}
public static void main(String gg[])
{
if(gg.length==0)
{
System.out.println("Give player name as cmd line argument");
}
Chess chess=new Chess(gg[0]);
}
}