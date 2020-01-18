// Determines whether a given character string abides by the grammar rules for user input.
// The method to use is called parse. When given a string, it returns a string of an error 
// mesage if the input is bad. Otherwise, it returns null.
// The grammar rules are written in Backus-Naur Form in Grammar_March.txt
package Mediator;
import Game.Physical.Things.Thing;
import Game.Physical.Player;
import Game.Spatial.Room;
import Game.Spatial.Maps.Map1;
import Game.Linguistics.Action;
import java.util.ArrayList;

public class Parser
{
   /*
      Action is a type, of all possible flavors of action (imported):
         GOTHRU, GO, OPEN, CLOSE, UNLOCK, LOOK, CHECK, PUSH, PICKUP, DROP, INVENTORY, ERROR
      ActionPhrases has all the phrases that correspond to each value of Action. They are terminals.
   */
   private static String[][] ActionPhrases = { {"go through", "exit through", "exit into", "exit", "enter through", "enter into", "enter", "leave into", "leave through", "leave",},
                                                {"go to", "go over to", "go", "walk to", "walk over to", "walk", "run to", "run over to", "run", "locomote to", "locomote"},                                             
                                                {"open up", "open"}, 
                                                {"close up", "close"},
                                                {"unlock"},
                                                {"check", "examine", "study", "look closely at"},
                                                {"look at", "look", "view", "observe"},
                                                {"push into", "push up against", "push against", "push", "slam into", "slam against", "slam up against", "slam up against", "shove", "slam",},
                                                {"pick up", "lift", "take up", "take", "grab"}, 
                                                {"drop", "set down", "set", "let go of", "let go"} };
                                                
   /* Words that may come before a thing in the input String. More terminals. */
   private static String[] ObjectExtras = {"the", "a"};

   /*
      If a proper action is discovered, playerAct is given the value of the proper Action type.
      If a proper thing is discovered, playerThing is given a reference to the Thing object.
      actPhraseUsed is the action phrase last used by player.
   */
   public static Action playerAct = Action.ERROR;
   public static Thing playerThing = null;  
   public static String actPhraseUsed = "";
   
   private static boolean validObject = true;                                           
   
   
   /* This is the method that should be called by an outside file to parse a String. */ 
   public static String parse(String inString)
   {
      validObject = true;
      playerAct = Action.ERROR;
      playerThing = null;
      
      inString = inString.replaceAll("\\s+", " ").toLowerCase().trim(); // formats - removes extra spaces and makes all lowercase
      String parsed = command(inString);
      System.out.println(parsed);
      if (parsed == null)  // no proper action
      {
         return "No valid action at beginning.";
      }
      else if (parsed.equals(""))   // PARSE SUCCESSFUL!
      {
         return "";
      }
      else     // EXTRA WORDS AFTER action OR object
      {
         if (validObject)
            return "Improper words following object.";
         else
            return "Improper words following action.";
      }
   }
    
   // ////////////////////////////////////////////////// All derivations
   
   /*
      This is the start rule.
      Checks if String is a valid Action Phrase, or is an Action Phrase followed by an Object Phrase.
      If this is the case, it returns true. 
      The global variables playerAction and playerObject would then be given the values corresponding to what 
      action and object corresponding to the String.
      It returns false if this is not the case. If this is happens, it is because there there was no proper 
      Action Phrase at the beginning, there was text following the Action Phrase that was not a valid Object Phrase, 
      or there was any text following the Object Phrase.

   */
   private static String command(String inString)
   {
      String tempString = actionPhrase(inString);
      if (tempString != null)
      {
         inString = tempString;
         tempString = objectPhrase(inString);
         if (tempString != null)
            return tempString;
         else
            return inString;
      }
      else
         return null;
   }

   
   /*
      Checks if beginning of String match to any String in ActionPhrases
      If so, returns the substring after portion that was parsed
      Otherwise returns null
   */
   private static String actionPhrase(String inString)
   {
      for (int i = 0; i < ActionPhrases.length; i++) // Go through each index for each synonym phrases array in the ActionPhrases array
      {
         for (String singlePhrase : ActionPhrases[i]) // Go through each phrase in the synonym array
         {
            // If contains at beginning.
            if (singlePhrase.length() <= inString.length())
            {
               if (inString.substring(0, singlePhrase.length()).equals(singlePhrase))
               {
                  actPhraseUsed = singlePhrase;
                  playerAct = Action.values()[i];  
                  return inString.replace(singlePhrase, "").trim();
               }
            }
         }
      }
      return null;
   }
   
   
   /*
      Checks if beginning of String contains any object's name - which are the names of things in the current 
      room, or the words "room", "inventory", or "item".
      If one of these are found, sets the global variable playerThing to the corresponding Thing (null for nothing/room, 
      the inventory object for the inventory, and a Thing in the room for something in the room.
      Otherwise, simply returns null.
   */
   private static String object(String inString)
   {
      // Check if it's room/nothing
      if (inString.equals(""))
      {
         return "";
      }
      else if (inString.equals("room"))
      {
         return inString.replace("inventory", "").trim();
      }
      // Check if it's inventory
      if (inString.length() >= "inventory".length() && inString.substring(0, "inventory".length()).equals("inventory"))
      {
         playerAct = Action.INVENTORY;
         return inString.replace("inventory", "").trim();
      }
      else if (inString.length() > "inventory".length() && inString.substring(0, "items".length()).equals("items"))
      {
         playerAct = Action.INVENTORY;
         return inString.replace("items", "").trim();
      } 
      // Check it it's a Thing in the room
      else
      {
         ArrayList<Thing> ThingList;
         if (playerAct != Action.DROP)    // ThingList is either all the Things in the room or in Inventory items, depending on whether dropping
            ThingList = Map1.rooms[Player.roomNum].Things;
         else
            ThingList = Player.Inventory.items;
         
         for (Thing ThingElement : ThingList)
         {
            if (inString.length() >= ThingElement.name.length())
            {
               if (ThingElement.name.equalsIgnoreCase(inString.substring(0, ThingElement.name.length())))
               {
                  playerThing = ThingElement;
                  return inString.replace(ThingElement.name.toLowerCase(), "").trim();
               }
            }
         } 
      }
      validObject = false;
      return null;      
   }
   
   /*
      Checks if given String begins with an object phrase.
      This means it checks if it begins with an object only or if it begins with an object-extra-word followed 
      by an object.
      If this is the case, returns the String, but without the beginning part that was parsed.
      Also, if this is the case, the global variable playerThing will be given a reference to the object.
      Otherwise, it returns null.
   */
   private static String objectPhrase(String inString)
   {
      String tempString = objectExtra(inString);
      if (tempString == null)
         tempString = inString;
      return object(tempString);
   }
   
   /*
      Checks if given String begins with object extra term.
      If so, it returns the String, with the beginning object extra removed.
      Else, it returns null.
   */
   private static String objectExtra(String inString)
   {
      for (String extra : ObjectExtras)
      {
         if (extra.length() <= inString.length())
         {
            if (extra.equals(inString.substring(0, extra.length())))
               return inString.replace(extra, "").trim();
         }
      }
      return null;
   }
   
      

   public static void main(String[] args)
   {
      Map1.setMap();
      Map1.rooms[0].setDoorWallNames();
      Player.roomNum = 0;

      System.out.println(parse("look at north door"));
      System.out.println(playerAct +"\n"+ playerThing);
   }

}
