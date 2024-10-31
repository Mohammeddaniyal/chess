import javax.swing.*;
public class PossibleMoves
{
public static boolean[][] getPossibleMoves(JButton[][] tiles,int startRowIndex,int startColumnIndex)
{
boolean [][]possibleMoves=new boolean[8][8];
String pieceName;
pieceName=tiles[startRowIndex][startColumnIndex].getActionCommand();
int destinationRowIndex,destinationColumnIndex;
for(int e=0;e<8;e++)
{
destinationRowIndex=e;
for(int f=0;f<8;f++)
{
destinationColumnIndex=f;
if(tiles[destinationRowIndex][destinationColumnIndex].getActionCommand().equals("")==false)
{
possibleMoves[destinationRowIndex][destinationColumnIndex]=false;
continue;
}
if(pieceName.equals("blackKing") || pieceName.equals("whiteKing"))
{
possibleMoves[destinationRowIndex][destinationColumnIndex]=KingMoveValidator.validateMove(startRowIndex,startColumnIndex,destinationRowIndex,destinationColumnIndex);
//king validation part ends here
}else
if(pieceName.equals("whiteQueen") || pieceName.equals("blackQueen"))
{
possibleMoves[destinationRowIndex][destinationColumnIndex]=QueenMoveValidator.validateMove(startRowIndex,startColumnIndex,destinationRowIndex,destinationColumnIndex,tiles);
}else
if(pieceName.equals("whiteKnight") || pieceName.equals("blackKnight"))
{
possibleMoves[destinationRowIndex][destinationColumnIndex]=KnightMoveValidator.validateMove(startRowIndex,startColumnIndex,destinationRowIndex,destinationColumnIndex);
}else
if(pieceName.equals("whiteBishop") || pieceName.equals("blackBishop"))
{
possibleMoves[destinationRowIndex][destinationColumnIndex]=BishopMoveValidator.validateMove(startRowIndex,startColumnIndex,destinationRowIndex,destinationColumnIndex,tiles);
//Bishop validation ends here
}else
if(pieceName.equals("whiteRook") || pieceName.equals("blackRook"))
{
possibleMoves[destinationRowIndex][destinationColumnIndex]=RookMoveValidator.validateMove(startRowIndex,startColumnIndex,destinationRowIndex,destinationColumnIndex,tiles);
}else 
if(pieceName.equals("blackPawn") || pieceName.equals("whitePawn"))
{
possibleMoves[destinationRowIndex][destinationColumnIndex]=PawnMoveValidator.validateMove(startRowIndex,startColumnIndex,destinationRowIndex,destinationColumnIndex,tiles);
}
}//inner loop ends
}//outer loop ends
return possibleMoves;
}
}