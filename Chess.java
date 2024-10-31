import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
public class Chess extends JFrame implements ActionListener
{
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
int startRowIndex,startColumnIndex,destinationRowIndex,destinationColumnIndex;
public Chess()
{
tiles=new JButton[8][8];
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
tile=setupBoard(e,f,tileColor,tileBorder);
tile.addActionListener(this);
tiles[e][f]=tile;
container.add(tile);
}
}
Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
int width=600;
int height=600;
setSize(width,height);
int x=(d.width/2)-(width/2);
int y=(d.height/2)-(height/2);
setLocation(x,y);
setVisible(true);
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
this.sourceTile.setBorder(UIManager.getBorder("Button.border"));
sourceTile.setEnabled(false);
sourceTile.setEnabled(true);
reset();
return;
}


if(click==false) //clicking on the source tile
{
this.sourceTile=tile;
startRowIndex=e;
startColumnIndex=f;
click=true;
this.sourceTile.setBorder(BorderFactory.createLineBorder(Color.GREEN,3));
this.sourceTile.setBackground(new Color(144,238,144));

possibleMoves=PossibleMoves.getPossibleMoves(tiles,startRowIndex,startColumnIndex);

JButton validTile;
for(e=0;e<8;e++)
{
for(f=0;f<8;f++)
{
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

if(targetIconName.equals("")==false)
{
this.sourceTile.setBorder(UIManager.getBorder("Button.border"));
targetTile.setEnabled(false);
targetTile.setEnabled(true);
reset();
 return;
}
boolean validMove=validMovement(sourceIconName);
if(validMove==false) 
{
this.sourceTile.setBorder(UIManager.getBorder("Button.border"));
targetTile.setEnabled(false);
targetTile.setEnabled(true);
reset();
return;
}
this.sourceTile.setBorder(BorderFactory.createLineBorder(Color.GREEN,3));
movePiece(sourceIconName);
this.sourceTile.setBorder(UIManager.getBorder("Button.border"));
reset();
}
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
if(sourceIconName.equals("blackKing") || sourceIconName.equals("whiteKing"))
{
return KingMoveValidator.validateMove(startRowIndex,startColumnIndex,destinationRowIndex,destinationColumnIndex);
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

return true;
}
public static void main(String gg[])
{
Chess chess=new Chess();
}
}