/*
   Filename: ThingReactions.java
   @author: Matthew Wojtechko
   Created: 12.30.17
   
   This contains data about the player.
*/
package Game.Physical;
import Game.Physical.Things.*;
import Game.Spatial.Maps.*;
import Game.Spatial.*;
import Mediator.Control;
import java.util.ArrayList;

public class Player
{
   public static Inventory Inventory = new Inventory();
   public static int mapNum = -1,
                     roomNum = 0,
                     xLoc = -1,
                     yLoc = -1;
   public static boolean justEntered = false; // whether the Player just entered a room
   
   // Moves the player forward
   public static String go2()
   {
      // set player x and y to coordinates
      return "You try to go forward. However, this feature is not implemented in the program yet, unfortunately, so you do not do so. "  +
             "Pathetic, I know. This should tell you that you went forward, tell you where you are now, and what's around you. " +
             "For example, right here there's " + RoomMessages.getContentsAt(xLoc, yLoc, Map1.rooms[roomNum]) + ". " + 
             "Nifty, right? For the time being, consider this an Easter egg. Congratulations, a winner is you.";
     //           NEED TO IMPLEMENT MOVE FORWARD, AND THE ROOM METHOD THAT TELLS YOU WHERE YOU ARE ------------------------------------------------------!!!
   }
   
   // Move to coordinates of given Thing, returning message.
   // WARNING: Thing MUST be in room, otherwise, the player will be given an improper location.
   public static String go(Thing T)
   {
      int index = Map1.rooms[roomNum].getThingIndex(T);
      int[] xy = Map1.rooms[roomNum].ThingsLoc.get(index);
      if (xLoc == xy[0] && yLoc == xy[1])
         return "You are already at the " + T.getName() + ".";
      else
      {
         xLoc = xy[0];
         yLoc = xy[1];
         return "You go over to the " + T.getName() + ".";
      }       
   } 
   
   
   // moves player "through" Transport by moving them to the Transport's adjacent side in the adjacent room, sets walls in room to proper name via Room method
   public static void goThru(Transport Trans)
   {
      roomNum = ((Player.roomNum == Trans.room1) ? Trans.room2 : Trans.room1);     // set to new Room Number, whichever of the two options not currently in
      Room newRoom = Map1.rooms[Player.roomNum];                        
      int[] newXY = newRoom.getThingLoc(Trans);
      xLoc = newXY[0];
      yLoc = newXY[1];
      Map1.rooms[roomNum].setDoorWallNames();
      justEntered = true;
      Control.lastObj = null;
   }
   
   // returns true if location of given Thing and player are same
   public static boolean at(Thing T)
   {
      int index = Map1.rooms[roomNum].getThingIndex(T);
      if (index > -1)
      {
         int[] xy = Map1.rooms[roomNum].ThingsLoc.get(index);
         if (xLoc == xy[0] && yLoc == xy[1])
            return true;         
      }
      return false;
   }
   
   // toString for debugging
   public static String getString()
   {
      return "Player Coordinates: (" + xLoc + ", " + yLoc + ")\n" +
             "Inventory:          " + Inventory;
   }
      
      
}