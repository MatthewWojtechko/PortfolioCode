package Game.Linguistics;
import java.util.Scanner;

public class PositionTerms
{
   private static enum Region {ALONG_W, NEAR_W, SLIGHT_W, SLIGHT_E, NEAR_E, ALONG_E,
                              ALONG_N, NEAR_N, SLIGHT_N, SLIGHT_S, NEAR_S, ALONG_S,
                              CENTER}; 
   
   // grammar check
   // south should = 1
   // handle cooridors (one dimension being 1) as well as 1x1
   public static String getPhrase(int roomX, int roomY, int positionX, int positionY)
   {
      Region XRegion = getRegion(roomX, positionX, true),
             YRegion = getRegion(roomY, positionY, false);
      
      if (XRegion == Region.CENTER)    // x center
      {
         if (YRegion == Region.CENTER)
            return "in the center of the room";
         else if (YRegion == Region.SLIGHT_N)
            return "slightly North";
         else if (YRegion == Region.SLIGHT_S)
            return "slightly South";
         else if (YRegion == Region.NEAR_N)
            return "near the middle of the NORTH WALL";
         else if (YRegion == Region.NEAR_S)
            return "near the middle of the SOUTH WALL";
         else if (YRegion == Region.ALONG_N)
            return "by the middle of the NORTH WALL";
         else
            return "by the middle of the SOUTH WALL";
      }
      else if (XRegion == Region.SLIGHT_W || XRegion == Region.SLIGHT_E)   // slightly E or W
      {
         String westOrEast = "West";
         if (XRegion == Region.SLIGHT_E)
            westOrEast = "East";
            
         if (YRegion == Region.ALONG_N)
            return "along the NORTH WALL, slightly " + westOrEast;
         else if (YRegion == Region.NEAR_N)
            return "near the NORTH WALL, slightly " + westOrEast;
         else if (YRegion == Region.SLIGHT_N)
            return "slightly North" + westOrEast.toLowerCase();
         else if (YRegion == Region.CENTER)
            return "slightly " + westOrEast;
         else if (YRegion == Region.SLIGHT_S)
            return "slightly South" + westOrEast.toLowerCase();
         else if (YRegion == Region.NEAR_S)
            return "near the SOUTH WALL, slightly " + westOrEast;
         else
            return "along the SOUTH WALL, slightly " + westOrEast;
      }
      else if (XRegion == Region.NEAR_W || XRegion == Region.NEAR_E)   // near E or W
      {
         String westOrEast = "West";
         if (XRegion == Region.NEAR_E)
            westOrEast = "East";
         
         if (YRegion == Region.ALONG_N)
            return "near the North" + westOrEast.toLowerCase() + ", along the NORTH WALL";
         else if (YRegion == Region.NEAR_N)
            return "near the North" + westOrEast.toLowerCase() + " corner";
         else if (YRegion == Region.SLIGHT_N)
            return "near the " + westOrEast.toUpperCase() + " WALL, slightly North";
         else if (YRegion == Region.CENTER)
            return "near the middle of the " + westOrEast.toUpperCase() + "WALL";
         else if (YRegion == Region.SLIGHT_S)
            return "near the " + westOrEast.toUpperCase() + " WALL, slightly South";
         else if (YRegion == Region.NEAR_S)
            return "near the South" + westOrEast.toLowerCase() + " corner";
         else
            return "near the South" + westOrEast.toLowerCase() + ", along the SOUTH WALL";
      }
      else  // along E or W walls
      {
         String westOrEast = "West";
         if (XRegion == Region.ALONG_E)
            westOrEast = "East";
            
         if (YRegion == Region.ALONG_N)
            return "in the North" + westOrEast.toLowerCase() + " corner";
         else if (YRegion == Region.NEAR_N)
            return "near the North" + westOrEast.toLowerCase() + " corner, along the " + westOrEast.toUpperCase() + " WALL";
         else if (YRegion == Region.SLIGHT_N)
            return "along the " + westOrEast.toUpperCase() + " WALL, slightly North";
         else if (YRegion == Region.CENTER)
            return "by the middle of the " + westOrEast.toUpperCase() + " WALL";
         else if (YRegion == Region.SLIGHT_S)
            return "along the " + westOrEast.toUpperCase() + " WALL, slightly South";
         else if (YRegion == Region.NEAR_S)
            return "near the South" + westOrEast.toLowerCase() + " corner, along the " + westOrEast.toUpperCase() + " WALL";
         else
            return "in the South" + westOrEast.toLowerCase() + " corner";
      }
   }
   
   
   // roomX cannot be  <= 1!!!
   private static Region getRegion(int dimLength, int dimPos, boolean isXRegion)
   {
      // find the score if it is in the West dimension
      double westScore = getWestOrNorthScore(dimLength, dimPos);
      Region region;
      if (westScore <= 7.0/12 && westScore >= 5.0/12)
         region = Region.CENTER;
      else if (westScore <= 9.0/12 && westScore > 7.0/12)
         region = Region.SLIGHT_W;
      else if (westScore < 1 && westScore > 9.0/12)
         region = Region.NEAR_W;
      else if (westScore == 1)
         region = Region.ALONG_W;
      else if (westScore >= 3.0/12)
         region = Region.SLIGHT_E;
      else if (westScore > 0)
         region = Region.NEAR_E;
      else
         region = Region.ALONG_E;
      
      if (isXRegion)    // if wanting X region, return it as is
         return region;
      else     // otherwise, return the corresponding Y region
      {
         if (region == Region.SLIGHT_W)
            return Region.SLIGHT_S;
         else if (region == Region.NEAR_W)
            return Region.NEAR_S;
         else if (region == Region.ALONG_W)
            return Region.ALONG_S;
         else if (region == Region.SLIGHT_E)
            return Region.SLIGHT_N;
         else if (region == Region.NEAR_E)
            return Region.NEAR_N;
         else if (region == Region.ALONG_E)
            return Region.ALONG_N;
         else
            return Region.CENTER;
      }     
   }
   
    
       
   // returns your closeness to absolute West or absolute North, as a decimal percent.
   // In other words, how much distance you have to travel to get to the farthest West or North cell.
   // If it is a West score:
   // 0 -> total East
   // .5 -> total middle
   // 1 -> total West
   // If it is a norht score:
   // 1 -> total South
   // .5 -> total middle
   // 0 -> total North
   private static double getWestOrNorthScore(int dimensionLength, int posDimension)
   {
      return ((double)(dimensionLength - posDimension)) / (dimensionLength - 1);
      //return (roomX - posX + 1) / ((double)roomX);
   }
   
      
   public static void main(String[] args)
   {
      boolean keepGoing = true;
      Scanner Keyboard = new Scanner(System.in);
      int roomX, roomY;
      
      while (keepGoing)
      {
         System.out.print("Enter how many cells there are in the East-West direction: ");
         roomX = Keyboard.nextInt();
         System.out.print("Now, enter how many cells there are in the North-South direction: ");
         roomY = Keyboard.nextInt();
         //System.out.print("Now enter which cell is the position: ");
         //posX = Keyboard.nextInt();
         //System.out.println("X Region Term: " + getXRegion(roomX, posX) + "\n\n");
         
         System.out.println();
         for (int y = 1; y <= roomY; y++)
         {
            System.out.print("\n\n " + (roomY-y+1));
            for (int x = 1; x <= roomX; x++)
               System.out.print("[ ] ");
         }
         System.out.print("\n ");
         for (int x = 1; x <= roomX; x++)
            System.out.print("  " + x + " ");
         System.out.println("\n");
         for (int x = 1; x <= roomX; x++)
         {
            for (int y = 1; y <= roomY; y++)
               System.out.println("(" + x + ", " + y + ") = \"" + getPhrase(roomX, roomY, x, y) + "\"");
         }
         
         
         
         Keyboard.nextLine();
         System.out.print("ENTER to try again (enter any key to quit).");
         if (!Keyboard.nextLine().equals(""))
            keepGoing = false;
         System.out.println();
      }
   }
}