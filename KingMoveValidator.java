public class KingMoveValidator
{
private KingMoveValidator(){};
public static boolean validateMove(int startRowIndex,int startColumnIndex,int destinationRowIndex,int destinationColumnIndex)
{
int d=0;
if(startColumnIndex==destinationColumnIndex)//moving one step veritcally
{
d=(startRowIndex<destinationRowIndex)?destinationRowIndex-startRowIndex:startRowIndex-destinationRowIndex;
if(d!=1) return false;
}else if(startRowIndex==destinationRowIndex)//moving one step horizontally
{
d=(startColumnIndex<destinationColumnIndex)?destinationColumnIndex-startColumnIndex:startColumnIndex-destinationColumnIndex;
if(d!=1) return false;
}
else//moving one step diagonally
{
int d1=(startRowIndex<destinationRowIndex)?destinationRowIndex-startRowIndex:startRowIndex-destinationRowIndex;
int d2=(startColumnIndex<destinationColumnIndex)?destinationColumnIndex-startColumnIndex:startColumnIndex-destinationColumnIndex;
if(d1!=1 || d2!=1) 
{
return false;
}
}
return true;
}
}//class ends here (KingValidator)
