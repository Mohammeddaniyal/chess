import javax.swing.*;
public class PawnMoveValidator
{
public static boolean validateMove(int startRowIndex,int startColumnIndex,int destinationRowIndex,int destinationColumnIndex,JButton[][] tiles)
{
String sourceIconName=tiles[startRowIndex][startColumnIndex].getActionCommand();
if(sourceIconName.equals("blackPawn"))
{
if(destinationRowIndex<=startRowIndex || startColumnIndex!=destinationColumnIndex)
{
// for restricting backward movement of the pawn
//restricting diagonal movement of the pawn
return false;
}
if(startRowIndex==1)//at begining allowing 2 steps
{
if(destinationRowIndex-startRowIndex==2)//2 square tile forward validation
{
JButton piece=tiles[startRowIndex+1][startColumnIndex];
String pieceName=piece.getActionCommand();
if(pieceName.equals("")==false) return false;
}
if((destinationRowIndex-startRowIndex!=2 && destinationRowIndex-startRowIndex!=1))
{
//for 2 steps at the first move of the pawn
return false;
}
//at begining allowing 2 steps ends here
}else if(startRowIndex!=1 && destinationRowIndex-startRowIndex!=1)
{
// restrictring more than one tile movement of the pawn
return false;
}
//black pawn part ends here
}else if(sourceIconName.equals("whitePawn"))
{
if(startRowIndex<=destinationRowIndex || startColumnIndex!=destinationColumnIndex)
{
// for restricting backward movement of the pawn
//restricting diagonal movement of the pawn
return false;
}
if(startRowIndex==6 && (startRowIndex-destinationRowIndex!=2 && startRowIndex-destinationRowIndex!=1))
{
//for 2 steps at the first move of the pawn
return false;
}else if(startRowIndex!=6 && startRowIndex-destinationRowIndex!=1)
{
// restrictring more than one tile movement of the pawn
return false;
}
}//white pawn part ends here
return true;
}
}
