/*
   Filename: ThingReactions.java
   @author: Matthew Wojtechko
   Created: 10.13.17
   Edited:  11.23.17
            5/18
   Deleted: 12.8.17 (oops)
   Recovered: 12.8.17
   Editied:  Summer 2018
   
   This program provides the messages that describe what happens in the Game World 
   when the player does a given action to a given Thing. 
   It also provides changes to any Thing which could result from an interaction with 
   it.
   
   It contains static functions corresponding to each interaction, which, when called 
   with a given Thing passed as an argument, makes any needed changes to the Thing and 
   returns a String depicting the interaction.
   
   The proper function must be called with corresponding Thing sent as an argument.
   For example: ThingReactions.open(doorObj); would execute the code that "opens" 
   the door object called doorObj, and would return the message describing that 
   interaction.
   
   Most of the possible interactions with Things would not have much of a bearing on 
   the Game World. Because of this, most of the functions merely return a description  
   of the player performing (or attempting) the interaction - no other code is run.
   For those interactions in which something happens, however, a function beloning to 
   that specific object is run. This function will change the onject in some way, and 
   also return a message describing the interaction.
   
      Below are how the static methods should be called:
      push(Thing T);
      pickUp(Thing T);
      open(Thing T);
      close(Thing T);
      unlock(Thing T, boolean hasKey);
      unlock(Thing T);    // assumes no key, or key irreleveant
*/
package Game.Physical;
import Game.Physical.Things.*;
import Game.Spatial.Maps.*;
import Game.Linguistics.Action;  /* Action is a type, of all possible flavors of action:
    GOTHRU, GO, OPEN, CLOSE, UNLOCK, LOOK, CHECK, PUSH, PICKUP, DROP, INVENTORY, ERROR */
import Game.Linguistics.PositionTerms;

   
public class ThingReactions
{

   // Determines proper method to call based off Action, and passes Thing
   public static String getReaction(Action Act, Thing T, boolean needPronoun)
   {
      switch (Act)
      {
         case LOOK:
            return look(T, needPronoun);
         case OPEN:
            return open(T, needPronoun);
         case UNLOCK:
            return unlock(T, needPronoun);
         case PUSH:
            return push(T, needPronoun);
         case GOTHRU:
            return goThru(T, needPronoun);
         case CLOSE:
            return close(T, needPronoun);
         case CHECK:
            return check(T, needPronoun);
         case PICKUP:
            return pickUp(T, needPronoun);
         case DROP:
            return drop(T, needPronoun);
         default:
            return "You could not complete the action.";
      }
   }
   
   // PUSH - modifies Thing T in terms of interaction, and returns message of interaction.
   public static String push(Thing T, boolean needPronoun)
   {
      String name = T.getName().toUpperCase();
      if (T instanceof Lever)
         return "You give it all you might, but the " + name + " does not budge.";    
      else if (T instanceof Rug)
         return "You get on the ground and scoot " + ((needPronoun) ? "it" : "the " + name) + " a little ways across the floor.";
      else if (T instanceof Door)
         return "You slam into " + ((needPronoun) ? "its unyielding surface." : "the unyielding surface of the " + name + ".");
      else if (T instanceof Wall)
         return ((Wall)T).reactPush(needPronoun);
      else if (T instanceof Barrier)
         return ((Barrier)T).reactPush(needPronoun);
      else if (T instanceof Key)
         return "You don't see a way to push " + ((needPronoun) ? "it" : "the " + name) + " because it is too small.";
      else if (T instanceof Skeleton)
         return "You find you're unable to push " + ((needPronoun) ? "it" : "the " + name) + " as it is stuck in its position.";
      else if (T instanceof Skull)
         return "You don't see a way to push " + ((needPronoun) ? "it" : "the " + name) + " because it is too small.";
      else if (T instanceof Design)
         return "You are unable to push " + ((needPronoun) ? "it" : "the " + name) + ", as it is merely drawn on a wall.";
      else if (T instanceof Box)
         return "You crouch and push, but " + ((needPronoun) ? "it" : "the " + name) + " will not budge.";
      else if (T instanceof Chest)
         return ((needPronoun) ? "It" : "The " + name) + " will not budge as hard as you push.";
      else
         return "You cannot push " + ((needPronoun) ? "it" : "the " + name) + ".";
   }
   
   // PICK UP - modifies Thing T in terms of interaction, and returns message of interaction.
   public static String pickUp(Thing T, boolean needPronoun)
   {
      String name = T.getName().toUpperCase();
      if (T instanceof Key || T instanceof Skull)
      {
         if (T.getID().equalsIgnoreCase("BoxKey")) // If the appearing key in Room 3, get the proper Box, and update that it's empty.
         {
            Box tempBox;
            for (int i = 1; i < 6; i++)
            {
               tempBox = (Box)Map1.rooms[Player.roomNum].getThingByID("Box " + i);
               if (tempBox != null && tempBox.hasKey)
               {
                  tempBox.hasKey = false;
                  tempBox.setDescription();
                  tempBox.setDetails();
               }
            }
         }
         Map1.rooms[Player.roomNum].removeThing(T);
         Player.Inventory.add(T);
         return "You take " + ((needPronoun) ? "it" : "the " + name) + ".";
      }
      else if (T instanceof Skeleton)
         return "You try, but " + ((needPronoun) ? "it" : "the " + name) + " is too tall to carry.";
      else if (T instanceof Box)
         return "You grab and pull, but " + ((needPronoun) ? "it" : "the " + name) + " seems rooted to the floor";
      else if (T instanceof Rug)
      {
         String msg = "You lift a corner of " + ((needPronoun) ? "it. The " + name : "the " + name + ". It") + "is too large to carry, so you drop it, " + 
                " coughing from the swirl of dust.";
         if (!Map1.isRugLifted)
            return msg + "\n\n" + Map1.rugKeyRevealed();    // if rug key still there, reveal it
         else
            return msg;
      }
      if (T instanceof Passageway || T instanceof Wall)
         return "You realize that not even you can lift " + ((needPronoun) ? "it" : "the " + name) + ".";
      else if (T instanceof Lever)
         return "You pull, but you can't yank " + ((needPronoun) ? "it" : "the " + name) + " out of the floor.";
      else if (T instanceof Door)
         return "You don't see any way to lift " + ((needPronoun) ? "it" : "the " + name) + ".";
      else if (T instanceof Barrier)
      {
         if (needPronoun)
            return "Its bars and its frame are rooted to the floor, so you're unable to pick it up.";
         else
            return "The bars and the frame of the " + name + " are rooted to the floor, so you're unable to pick it up.";
      }
      else if (T instanceof Design)
         return "You are unable to pick " + ((needPronoun) ? "it up" : "up the " + name) + ", as it is merely drawn on a wall.";
      else if (T instanceof Chest)
         return "You pull up on " + ((needPronoun) ? "it" : "the " + name) + ", but you just can't lift it.";
      else
         return "You cannot pick that up.";
   }
   
   // OPEN - modifies Thing T in terms of interaction, and returns message of interaction.
   public static String open(Thing T, boolean needPronoun)
   {
      String name = T.getName().toUpperCase();
      if (T instanceof Door)
         return ((Door)T).reactOpen(needPronoun);
      else if (T instanceof Passageway || T instanceof Wall || T instanceof Barrier)
         return "You cannot find an opening mechanism on " + ((needPronoun) ? "it" : "the " + name) + ".";
      else if (T instanceof Box)
         return ((Box)T).reactOpen(needPronoun);
      else if (T instanceof Key || T instanceof Skull || T instanceof Rug)
         return "You do not see any way of opening " + ((needPronoun) ? "it" : "the " + name) + ".";
      else if (T instanceof Design)
         return "You try to open " + ((needPronoun) ? "it" : "the " + name) + ", but it is merely drawn on a wall.";
      else if (T instanceof Chest)
      {
         if (((Chest)T).getStatus() == 1) // if the chest is closed not locked
            Map1.chestOpened();
         return ((Chest)T).reactOpen(needPronoun);
      }
      else
         return "You cannot open " + ((needPronoun) ? "it" : "the " + name) + ".";
   }
   
   // CLOSE - modifies Thing T in terms of interaction, and returns message of interaction.
   public static String close(Thing T, boolean needPronoun)
   {
      String name = T.getName().toUpperCase();
      if (T instanceof Door)
         return ((Door)T).reactClose(needPronoun);
      else if (T instanceof Passageway || T instanceof Wall || T instanceof Barrier)
         return "You cannot find a closing mechanism on " + ((needPronoun) ? "it" : "the " + name) + ".";
      else if (T instanceof Box)
         return ((Box)T).reactClose(needPronoun);
      else if (T instanceof Key || T instanceof Skull || T instanceof Rug)
         return "You do not see any way of closing " + ((needPronoun) ? "it" : "the " + name) + ".";
      else if (T instanceof Design)
         return "You try to close " + ((needPronoun) ? "it" : "the " + name) + ", but it is merely drawn on a wall.";
      else if (T instanceof Chest)
         return ((Chest)T).reactClose(needPronoun);
      else
         return "You cannot close " + ((needPronoun) ? "it" : "the " + name) + ".";
   }
   
   // UNLOCK (hasKey?) - modifies Thing T in terms of interaction, and returns message of interaction.
   public static String unlock(Thing T, boolean needPronoun)
   {
      String name = T.getName().toUpperCase();
      if (T instanceof Door)
         return ((Door)T).reactUnlock(needPronoun);
      else if (T instanceof Chest)
         return ((Chest)T).reactUnlock(needPronoun);
      else if (T instanceof Passageway || T instanceof Wall || T instanceof Barrier ||
               T instanceof Key || T instanceof Skull || T instanceof Rug)
         return "You do not see a way to unlock " + ((needPronoun) ? "it" : "the " + name) + ".";
      else if (T instanceof Box)
         return ((needPronoun) ? "It" : "The " + name) + " does not seem to have a locking mechanism.";
      else if (T instanceof Design)
         return "You try to unlock " + ((needPronoun) ? "it" : "the " + name) + ", but it is merely drawn on a wall.";
      else
         return "You cannot unlock " + ((needPronoun) ? "It" : "The " + name) + ".";
   }
   
   // DROP - modifies Thing T in terms of interaction, and returns message of interaction
   public static String drop(Thing T, boolean needPronoun)
   {
      String name = T.getName().toUpperCase();
      if (T instanceof Skull && Player.roomNum == 6)  // if player drops skull in room with headless skeleton
      {
         Skeleton Skele = (Skeleton) Map1.rooms[Player.roomNum].getThingByID("skele6");
         if (Skele.missing.equalsIgnoreCase("head"))  // if skeleton headless
         {
            int[] skeleXY = Map1.rooms[Player.roomNum].getThingLoc(Skele);
            if (skeleXY[0] == Player.xLoc && skeleXY[1] == Player.yLoc) // if Player is at skeleton
               return Map1.skeletonHeaded(); // Update skeleton, remove skull, return message
         }
         
      }
      if (Player.Inventory.contains(T))
      {
         Map1.rooms[Player.roomNum].add(T, Player.xLoc, Player.yLoc);
         Player.Inventory.remove(T);
         if (T instanceof Skull && Map1.rooms[Player.roomNum].sameLocation(T, (Thing)Map1.Skeleton6))   // if dropped skull onto skeleton, 
            return Map1.skeletonHeaded();
         return "You set " + ((needPronoun) ? "it" : "the " + name) + " down right by you.";
      }
      else
         return "You want to drop " + ((needPronoun) ? "it" : "the " + name) + ", but you cannot because you do not possess it.";
   }
   
   // GOTHRU - modifies Thing T in terms of interaction, and returns message of interaction
   public static String goThru(Thing T, boolean needPronoun)
   {
      String name = T.getName().toUpperCase();
      if (T instanceof Door)
         return ((Door)T).reactGoThru(needPronoun);
      else if (T instanceof Wall)
         return ((Wall)T).reactGoThru(needPronoun);
      else if (T instanceof Barrier)
         return ((Barrier)T).reactGoThru(needPronoun);
      else
         return "You try to go through " + ((needPronoun) ? "it" : "the " + name) + ", but you cannot find anything to travel through.";
   }
   
   // LOOK
   /*
      Returns a message depicting what happens when the player LOOKS at something, 
      given the Thing and whether a pronoun is needed in the message for said Thing.
      The location of the Thing is described in the message, unless the Thing is a wall.
   */
   public static String look(Thing T, boolean needPronoun)
   {
      String msg;
      int[] thingLoc = Map1.rooms[Player.roomNum].getThingLoc(T);
      String locMsg = "";
      if (!(T instanceof Wall))
         locMsg = " " + PositionTerms.getPhrase(Map1.rooms[Player.roomNum].xDim, Map1.rooms[Player.roomNum].yDim,
                                       thingLoc[0], thingLoc[1]);
      if (!needPronoun)
         msg = "You take a look at the " + T.name + locMsg + ". ";
      else
         msg = "You take a look at it. ";
      return msg + T.getDescription();
   }
   
   // CHECK
   /*
      Returns a message depicting what happens when the player CHECKS something, 
      given the Thing and whether a pronoun is needed in the message for said Thing.
      The location of the Thing is described in the message, unless the Thing is a wall.
   */
   public static String check(Thing T, boolean needPronoun)
   {
      String msg;
      int[] thingLoc = Map1.rooms[Player.roomNum].getThingLoc(T);
      String locMsg = "";
      if (!(T instanceof Wall))
         locMsg = " " + PositionTerms.getPhrase(Map1.rooms[Player.roomNum].xDim, Map1.rooms[Player.roomNum].yDim,
                                       thingLoc[0], thingLoc[1]);

      if (!needPronoun)
         msg = "You take a close look at the " + T.name + locMsg + ". ";
      else
         msg = "You take a close look at it. ";
      return msg + T.getDescription() + " " + T.getDetails();
   }
   
   public static String checkAnotherBox(Box B, boolean needPronoun)
   {
      String msg;
      if (!needPronoun)
         msg = "You take a close look at the " + B.name + ". ";
      else
         msg = "You take a close look at it. ";
      return msg + B.closedShortDetails;
   }  
   
   // TEST METHOD
  /* public static void main(String[] args)
   {
      Thing[] things = new Thing[9];
      things[0] = new Door();
      things[1] = new Passageway();
      things[2] = new Wall();
      things[3] = new Barrier();
      things[4] = new Key();
      things[5] = new Skull();
      things[6] = new Rug();
      things[7] = new Box();
      things[8] = new Design();
      
      for (Thing tempThing : things)
      {
         System.out.println(tempThing + "\n--------------------------\n\n" + 
                            ThingReactions.push(tempThing, false) + "\n\n" + 
                            ThingReactions.pickUp(tempThing, false) + "\n\n" + 
                            ThingReactions.open(tempThing, false) + "\n\n" + 
                            ThingReactions.close(tempThing, false) + "\n\n" + 
                            ThingReactions.unlock(tempThing, false) + "\n\n\n");
      }
   }*/
}