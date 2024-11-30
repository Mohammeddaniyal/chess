import javax.swing.*;
import java.util.*;
public class CheckmateDetector
{
public static boolean[][] getPossibleMoves(JButton[][] tiles,int startRowIndex,int startColumnIndex,KingCastling kingCastling)
{
boolean [][]possibleMoves=PossibleMoves.getPossibleMoves(tiles,startRowIndex,startColumnIndex,kingCastling);
PossibleMovesIndex pieceValidIndex;
ArrayList<PossibleMovesIndex> piecesValidIndexes=new ArrayList<>();
for(int e=0;e<8;e++)
{
for(int f=0;f<8;f++)
{
if(possibleMoves[e][f]==true)
{
pieceValidIndex=new PossibleMovesIndex();
pieceValidIndex.row=e;
pieceValidIndex.column=f;
pieceValidIndex.safe=true;
piecesValidIndexes.add(pieceValidIndex);
}
}
}//piece valid indexes loop ends here

if(piecesValidIndexes.size()==0) return possibleMoves;
boolean [][]validPossibleMoves=possibleMoves;
//find index of king
String pieceName=tiles[startRowIndex][startColumnIndex].getActionCommand();
String pieceColor=pieceName.substring(0,5);
String kingName=pieceColor+"King";
int kingRowIndex=0;
int kingColumnIndex=0;
boolean pieceIsKing=false;
if(pieceName.equals("whiteKing") || pieceName.equals("blackKing"))
{
kingRowIndex=startRowIndex;
kingColumnIndex=startColumnIndex;
pieceIsKing=true;
}
else
{
for(int e=0;e<8;e++)
{
for(int f=0;f<8;f++)
{
if(tiles[e][f].getActionCommand().equals(kingName)) 
{
kingRowIndex=e;
kingColumnIndex=f;
break;
}
}
}
}


JButton[][] dummyTiles=new JButton[8][8];
JButton dummyTile;
JButton tile;
String tilePieceName;
for(int e=0;e<8;e++)
{
for(int f=0;f<8;f++)
{
tile=tiles[e][f];
tilePieceName=tile.getActionCommand();
dummyTile=new JButton();
dummyTiles[e][f]=dummyTile;
if(e==startRowIndex && f==startColumnIndex)
{
dummyTile.setActionCommand("");
}
else
{
dummyTile.setActionCommand(tilePieceName);
}
}
}//creating dummy tiles(D.S) ends here

int row;
int column;
ArrayList<PieceMoves> capturingPiecesMovesList;
for(PossibleMovesIndex pmi:piecesValidIndexes)
{
row=pmi.row;
column=pmi.column;
dummyTile=dummyTiles[row][column];
String s=dummyTile.getActionCommand();
dummyTile.setActionCommand(pieceName);
if(pieceIsKing==false)capturingPiecesMovesList=isPieceInDanger(dummyTiles,pieceColor,kingRowIndex,kingColumnIndex,false);
else capturingPiecesMovesList=isPieceInDanger(dummyTiles,pieceColor,row,column,false);
for(PieceMoves pieceMoves:capturingPiecesMovesList)
{
possibleMoves=pieceMoves.possibleMoves;
if(pieceIsKing==true)
{
if(possibleMoves[row][column]==true)
{
validPossibleMoves[row][column]=false;
break;
}
}
else
{
if(possibleMoves[kingRowIndex][kingColumnIndex]==true)
{
validPossibleMoves[row][column]=false;
break;
}
}//else ends 
}//for loop ends(possibleMovesCapture)
dummyTile.setActionCommand(s);
s="";
}
return validPossibleMoves;
}


public static ArrayList<PieceMoves> isPieceInDanger(JButton[][] tiles,String pieceColor,int rowIndex,int columnIndex,boolean includeAllValidPieces)
{
if(pieceColor==null)
{
JButton piece=tiles[rowIndex][columnIndex];
String pieceName=piece.getActionCommand();
pieceColor=pieceName.substring(0,5);
}
JButton opponentPiece;
String opponentPieceName;
String opponentPieceColor;
PieceMoves pieceMoves;
ArrayList<PieceMoves> piecesMoves;
piecesMoves=new ArrayList<>();
boolean[][] possibleMoves;
KingCastling kingCastling=new KingCastling();
kingCastling.checkCastling=false;
for(int e=0;e<8;e++)
{
for(int f=0;f<8;f++)
{
opponentPiece=tiles[e][f];
opponentPieceName=opponentPiece.getActionCommand();
if(opponentPieceName.equals("")) 
{
continue;
}
opponentPieceColor=opponentPieceName.substring(0,5);
if(opponentPieceColor.equals(pieceColor))
{
 continue;
}
possibleMoves=PossibleMoves.getPossibleMoves(tiles,e,f,kingCastling);
if(possibleMoves[rowIndex][columnIndex]==true)
{
pieceMoves=new PieceMoves();
pieceMoves.possibleMoves=possibleMoves;
pieceMoves.rowIndex=e;
pieceMoves.columnIndex=f;
piecesMoves.add(pieceMoves);
if(includeAllValidPieces==false)
{
return piecesMoves;
}
}
}
}
return piecesMoves;
}
public static boolean detectCheckmate(JButton[][] tiles,String color)
{
KingCastling kingCastling=new KingCastling();
kingCastling.checkCastling=false;
String k=color+"King";
int kingRowIndex=0;
int kingColumnIndex=0;
for(int e=0;e<8;e++)
{
for(int f=0;f<8;f++)
{
if(tiles[e][f].getActionCommand().equals(k))
{
kingRowIndex=e;
kingColumnIndex=f;
break;
}
}
}

JButton king=tiles[kingRowIndex][kingColumnIndex];
String kingName=king.getActionCommand();
String kingColor=kingName.substring(0,5);
ArrayList<PieceMoves> piecesMoves=isPieceInDanger(tiles,null,kingRowIndex,kingColumnIndex,true);
if(piecesMoves.size()==0) 
{
//System.out.println(kingName+" not in danger");
return false;
}

boolean [][]kingPossibleMoves=PossibleMoves.getPossibleMoves(tiles,kingRowIndex,kingColumnIndex,kingCastling);
PossibleMovesIndex kingValidIndex;
ArrayList<PossibleMovesIndex> kingValidIndexes=new ArrayList<>();
for(int e=0;e<8;e++)
{
for(int f=0;f<8;f++)
{
if(kingPossibleMoves[e][f]==true)
{
kingValidIndex=new PossibleMovesIndex();
//System.out.println("King can move to tile : "+e+"/"+f);
kingValidIndex.row=e;
kingValidIndex.column=f;
kingValidIndex.safe=true;
kingValidIndexes.add(kingValidIndex);
}
}
}//king valid indexes loop ends here



if(kingValidIndexes.size()==0 && piecesMoves.size()>1)
{
//if king dont have any valid move and king is in threat by more than 1 opponent piece
//System.out.println("Checkmate detected no safe tile and being attacked by more than 1 pieces");
return true;
}


PieceMoves attackingPieceMoves=piecesMoves.get(0);
boolean[][] possibleMoves;
//creating a dummy tiles
//without the king which is in danger
JButton[][] dummyTiles=new JButton[8][8];
JButton dummyTile;
JButton tile;
String tilePieceName;
for(int e=0;e<8;e++)
{
for(int f=0;f<8;f++)
{
tile=tiles[e][f];
tilePieceName=tile.getActionCommand();
dummyTile=new JButton();
dummyTiles[e][f]=dummyTile;
if(e==kingRowIndex && f==kingColumnIndex)
{
dummyTile.setActionCommand("");
}
else
{
dummyTile.setActionCommand(tilePieceName);
}
}
}//creating dummy tiles(D.S) ends here
int kingValidIndexesCount=kingValidIndexes.size();
int row,column;
ArrayList<PieceMoves> capturingPiecesMovesList;
ArrayList<PossibleMovesIndex> kingSafeIndexes=new ArrayList<>();
for(PossibleMovesIndex kvi:kingValidIndexes)
{
row=kvi.row;
column=kvi.column;
dummyTile=dummyTiles[row][column];
String s=dummyTile.getActionCommand();
dummyTile.setActionCommand(kingName);
capturingPiecesMovesList=isPieceInDanger(dummyTiles,null,row,column,true);
//System.out.println("Simulating king at : "+row+"/"+column+" size of capturing pieces : "+capturingPiecesMovesList.size());
for(PieceMoves pieceMoves:capturingPiecesMovesList)
{
possibleMoves=pieceMoves.possibleMoves;
//System.out.println("Attacking piece name : "+tiles[row][column].getActionCommand()+row+"/"+column);
//System.out.println("value at 2/6 "+possibleMoves[2][6]);
if(possibleMoves[row][column]==true)
{
//System.out.println("This tile is not safe for king : "+row+"/"+column);
kvi.safe=false;
break;
}
}
if(kvi.safe==true)
{
kingSafeIndexes.add(kvi);
}
dummyTile.setActionCommand(s);
s="";
}
boolean safeTile=true;
if(kingSafeIndexes.size()==0)
{
safeTile=false;
for(PossibleMovesIndex kvi:kingValidIndexes)
{
//System.out.println(kvi.row+"/"+kvi.column);
}
//System.out.println("Checkmate detected(no safe tile) to move");
}
else
{
//System.out.println("Safe tile available");
//System.out.println("King safe indexes");
for(PossibleMovesIndex kvi:kingSafeIndexes)
{
//System.out.println(kvi.row+"/"+kvi.column);
}
return false;
}
row=attackingPieceMoves.rowIndex;
column=attackingPieceMoves.columnIndex;
possibleMoves=attackingPieceMoves.possibleMoves;
String opponentPieceName=tiles[row][column].getActionCommand();
//System.out.println("Attacking piece name : "+opponentPieceName);
int attackingPieceRowIndex=row;
int attackingPieceColumnIndex=column;
boolean captureOpponentPiece=false;
boolean blockOpponentPiece=false;
boolean knightPiece=false;
PossibleMovesIndex attackingPiecePossibleMovesIndex;
ArrayList<PossibleMovesIndex> attackingPiecePossibleMovesIndexes=new ArrayList<>();
if(opponentPieceName.equals("blackKnight") || opponentPieceName.equals("whiteKinght"))
{
//System.out.println("Attacking piece is knight");
knightPiece=true;
}

else
{
for(int e=0;e<8;e++)
{
for(int f=0;f<8;f++)
{
if(possibleMoves[e][f]==true)
{
attackingPiecePossibleMovesIndex=new PossibleMovesIndex();
attackingPiecePossibleMovesIndex.row=e;
attackingPiecePossibleMovesIndex.column=f;
attackingPiecePossibleMovesIndexes.add(attackingPiecePossibleMovesIndex);
}
}//inner loop
}//outer loop
}//attacking piece (possible moves indexes)
int row1=0;
int column1=0;
for(int e=0;e<8;e++)
{
for(int f=0;f<8;f++)
{
tile=tiles[e][f];
tilePieceName=tile.getActionCommand();
if(tilePieceName.equals("")) continue;
if(tilePieceName.substring(0,5).equals(kingColor)==false) continue;//in case of oppoent piece
if(tilePieceName.equals(k)) continue;//k contains name of king like(blackKing / whiteKing)
possibleMoves=PossibleMoves.getPossibleMoves(tiles,e,f,kingCastling);
if(possibleMoves[attackingPieceRowIndex][attackingPieceColumnIndex]==true)
{
// the which is threating the king can be captured
//System.out.println("Threating piece can be captured by : "+tilePieceName);
captureOpponentPiece=true;
break;
}
//for blocking
if(knightPiece==false)
{
//creating dummyTiles
dummyTiles=generateDummyTiles(tiles,tilePieceName);
ArrayList<PossibleMovesIndex> friendlyPiecePossibleMovesIndexes=getPossibleMovesIndexesList(possibleMoves);
for(PossibleMovesIndex pmi:friendlyPiecePossibleMovesIndexes)
{
row1=pmi.row;
column1=pmi.column;
dummyTiles[row1][column1].setActionCommand(tilePieceName);

boolean[][] opponentPiecePossibleMoves=PossibleMoves.getPossibleMoves(dummyTiles,attackingPieceRowIndex,attackingPieceColumnIndex,kingCastling);
if(opponentPiecePossibleMoves[kingRowIndex][kingColumnIndex]==false)
{
//the piece is blocked
//System.out.println("the piece is blocked by : "+tilePieceName);
blockOpponentPiece=true;
break; 
}	
/*
ArrayList<PieceMoves> blockingPiece=isPieceInDanger(dummyTiles,kingRowIndex,kingColumnIndex);
if(blockingPiece.size()==0)
{
//the piece is blocked
blockOpponentPiece=true;
break; 
}
*/
dummyTiles[row1][column1].setActionCommand("");
}
if(blockOpponentPiece==true) break;//done
}
}
}// part of blocking or capturing end's here	
if(captureOpponentPiece==false && blockOpponentPiece==false ) 
{
//System.out.println("cannot be captured nor be blocked");
return true;
}
/*
if(blockOpponentPiece==false)
{
//System.out.println("cannot be blocked");
return true;
}
*/
//System.out.println("NO THREAT");
return false;
}
private static JButton[][] generateDummyTiles(JButton[][] tiles,String pieceNameNotToInclude)
{
JButton[][] dummyTiles=new JButton[8][8];
JButton dummyTile;
JButton tile;
String tilePieceName;
for(int e=0;e<8;e++)
{
for(int f=0;f<8;f++)
{
tile=tiles[e][f];
tilePieceName=tile.getActionCommand();
dummyTile=new JButton();
dummyTiles[e][f]=dummyTile;
if(tilePieceName.equals(pieceNameNotToInclude))
{
dummyTile.setActionCommand("");
}
else
{
dummyTile.setActionCommand(tilePieceName);
}
}
}//creating dummy tiles(D.S) ends here
return dummyTiles;
}
private static ArrayList<PossibleMovesIndex> getPossibleMovesIndexesList(boolean [][]possibleMoves)
{
ArrayList<PossibleMovesIndex> possibleMovesIndexes=new ArrayList<>();
PossibleMovesIndex possibleMovesIndex;
for(int e=0;e<8;e++)
{
for(int f=0;f<8;f++)
{
if(possibleMoves[e][f]==true)
{
possibleMovesIndex=new PossibleMovesIndex();
possibleMovesIndex.row=e;
possibleMovesIndex.column=f;
possibleMovesIndexes.add(possibleMovesIndex);
}
}//inner loop
}//outer loop
return possibleMovesIndexes;
}
}