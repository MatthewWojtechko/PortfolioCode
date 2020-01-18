package Game.Spatial.Maps;

import Game.Spatial.*;
import Game.Physical.Things.*;
import java.util.Random;
import Game.Physical.Player;

public class Map1
{
   public static Room[] rooms = new Room[8];
   public static Skeleton Skeleton6;
   public static Skull Skull0;
   public static Barrier barrier;
   public static boolean isRugLifted = false;

   // creates and fills all the rooms for rooms
   public static void setMap()
   {
      // Shared Objects
      Door door1 = new Door("door", 1, 0, 2),
            door2 = new Door("door2", 1, 2, 1),
            door3 = new Door("door3", 1, 2, 3), 
            door4 = new Door("door4", 2, 5, 7);
      Wall breakWall = new Wall("breakable", null, false, 1);
      breakWall.room1 = 2;
      breakWall.room2 = 5;
      Wall passage36 = new Wall("passage36", null, true, 0);
      passage36.room1 = 3;
      passage36.room2 = 6;
      Wall passage65 = new Wall("passage65", null, true, 0);
      passage65.room1 = 6;
      passage65.room1 = 5;      
      barrier = new Barrier("barrier", false, 1, 4);
      
      
      // Room 0
      rooms[0] = new Room(0, 3, 3, "You close the door behind you, welcomed by nothing but cold, still air.\n" + 
                          "You are in utter blackness.", 
                          2, -1, -1, -1);
      rooms[0].addTrait("PITCH"); 
      rooms[0].add(new Door("door0", 3, 1, 1), 'S');
      Skull0 = new Skull("skull0");
      addWalls(rooms[0]);
      rooms[0].add(door1, 'N');
      
      // Room 1
      rooms[1] = new Room(1, 4, 4, "You step into a medium sized room.\nThere's a BARRIER made of bars in the " + 
                          "NORTH WALL with a SKULL sitting in that corner. The ground is inscribed with a \"I,\"" +
                          " and... you could have sworn you just heard some utterance from above.", 
                          4, 2, -1, -1);
      rooms[1].addTrait("CHIRP");
      addWalls(rooms[1]);
      rooms[1].add(barrier, 2, 4);
      rooms[1].add(door2, 'E');
      rooms[1].add(Skull0, 1, 4);      
      
      // Room 2
      rooms[2] = new Room(2, 8, 8, "Your footsteps echo loudly as you enter the doorway to a huge chamber. You can make out some details now, " +
                          "as dim light fliters through the stony expanse around you. A Roman Numeral \"II\" etched in the ground stretches to the four walls.\n" +
                          "Otherwise, all you can see in the vast grayness is a DOOR to the WEST and a DOOR to the EAST.",
                          5, 3, 0, 1);
      rooms[2].addTrait("ECHO");
      rooms[2].addTrait("EMPTY");
      rooms[2].add(new Wall("2wallW", null, false, 0), 'W');
      rooms[2].add(new Wall("2wallE", null, false, 0), 'E');
      rooms[2].add(new Wall("2wallW", null, false, 0), 'W');
      rooms[2].add(door3, 'E');
      rooms[2].add(door1, 'S');
      rooms[2].add(door2, 'W');
      rooms[2].add(breakWall, 'N');
      
      // Room 3
      rooms[3] = new Room(3, 5, 5, "A chalky skull inscribed on the EAST WALL is smiling at you. " + 
                          "This wall DESIGN peers over a row of 5 metal BOXES which you notice are inscribed with letters. From North to South, there's the " +
                          "\"A\" BOX, \"B\" BOX, \"C\" BOX, \"D\" BOX, and \"E\" BOX." +
                          "\nSuddenly, a freezing chill brushes over your shoulder." + 
                          "\nYou see the ground is marked with a \"III.\"",
                          6, -1, -1, 2);
      rooms[3].addTrait("PRESENCE");
      rooms[3].addTrait("CHILL");
      rooms[3].add(passage36, 'N');
      rooms[3].add(new Wall("3wallE", "There's a skull drawn on it in white, black, and red.",
                            false, 0), 'E');
      rooms[3].add(new Design("design", "It's a skull drawn in white, black, " + 
                            "and red.", "The drawn dead face forbodingly stares into your soul."), 5, 3);
      rooms[3].add(new Wall("3wallS", null, false, 0), 'S');
      rooms[3].add(new Wall("3wallW", null, false, 0), 'W');
      rooms[3].add(door3, 'W');
      
      // Adds a row of boxes ([4,5]; 2[4,4]; 3[4,3]; 4[4,2]; 5[4,]) - a random one has a key!
      int keyBoxNum = (new Random()).nextInt(4) + 1;
      boolean hasKey;
      char letter;
      for (int i=1; i<=5; i++)
      {
         switch (i)
         {
            case 1:
               letter = 'A';
               break;
            case 2:
               letter = 'B';
               break;
            case 3:
               letter = 'C';
               break;
            case 4:
               letter = 'D';
               break;
            default:
               letter = 'E';
         }
         if (i != keyBoxNum)
            hasKey = false;
         else
            hasKey = true;
         rooms[3].add(new Box("BOX " + i, letter, false, hasKey), 4, -1*(i-6));
      }
            
      // Room 4
      rooms[4] = new Room(4, 4, 4, "You enter a simple stone room with the ground \"IV\" obscured by a little red RUG " + 
                          "in the middle.", 
                          -1, 5, 1, -1);
      addWalls(rooms[4]);
      rooms[4].add(new Rug("rug"), 2, 2);
      rooms[4].add(barrier, 2, 1);
      
      // Room 5
      rooms[5] = new Room(5, 8, 2, "There's an eerie echo here. The room is spacious, long across the EAST and WEST. " + 
                           "To the NORTH WALL is a door, and a \"V\" stretches across the floor.",
                           7, 6, 2, 4);
      rooms[5].addTrait("ECHO");
      rooms[5].add(new Wall("5wallN", null, false, 0), 'N');
      rooms[5].add(new Wall("5wallE", null, false, 0), 'E');
      rooms[5].add(breakWall, 'S');
      rooms[5].add(new Wall("5wallW", null, false, 0), 'W');
      rooms[5].add(door4, 'N');
      
      // Room 6
      rooms[6] = new Room(6, 4, 4, "A scream resounds as if on cue. But when you look around the little " + 
                         "gray room ahead of you, you notice nothing and no one...\n" + 
                         "You see some sort of LEVER " + 
                         "right over by the NORTH WALL, with what looks like a mangaled SKELETON attached to it. If it wasn't missing its head " +
                         " (or was quite so dead), this guy might have been able to scream like that.\nThis room " +
                         "has a \"VI\" on the ground.", 
                         -1, -1, 3, 5);
      rooms[6].addTrait("PRESENCE");
      rooms[6].add(new Wall("6wallN", null, false, 0), 'N');
      rooms[6].add(new Wall("6wallE", null, false, 0), 'E');
      rooms[6].add(new Wall("6wallS", null, false, 1), 'S');
      rooms[6].add(passage65, 'W');
      Skeleton6 = new Skeleton("skele6", "The boney body leans into the LEVER with a silent intensity.", "head");
      rooms[6].add(Skeleton6, 2, 4);
      rooms[6].add(new Lever("lever", false), 3, 4);
      
      // Room 7
      rooms[7] = new Room(7, 1, 8, "The door leads into a tunnel. The only direction to look is straight down to the NORTH, " + 
                           "but you cannot see the end. What you presume to be a long \"VII\" stretches down out of sight.", 
                          -1, -1, 5, -1);
      rooms[7].addTrait("CLAUST");
      rooms[7].add(door4, 'S');
      rooms[7].add(new Chest("chest", 2), 1, 8);
   }
   // Special Events -- Actions to make to the game world under special circumstances
   
   public static String skeletonHeaded()
   {
      Skeleton6.setMissing("");
      Skeleton6.setDescription("It's a dirty old human skeleton. At least it has its head now.");
      Player.Inventory.remove(Skull0);
      barrier.open();
      return "You place the SKULL onto the formerly headless SKELETON. Slowly, the LEVER it grips begins to budge. After a struggle, it " +
             " the handle slams to the other end, as the SKELETON beams its toothy grin.\nThe unliving personality jitters soon after. It shakes fiercly, " +
             "just like the LEVER it clutches. You fall to the ground that likewise quakes with explosive violence. The tremors subside, and a deep echo rolls in from the WEST."; 
   }
   
   public static String rugKeyRevealed()
   {
      isRugLifted = true;
      rooms[4].add(new Key(), 2, 2);
      return "There was a KEY underneath the RUG.";
   }
   
   public static void chestOpened()
   {
      rooms[7].add(new Thing("It's a yellow MAP full of tears.", "It looks like if you touch it to much, this thing will fall apart.", "map7", "MAP"), 1, 8);
   }
   
   // adds the 4 generic walls to a Room1 object passed
   private static void addWalls(Room r)
   {
      r.add(new Wall(r.getID() + "wallN", null, false, 0), 'N');
      r.add(new Wall(r.getID() + "wallE", null, false, 0), 'E');
      r.add(new Wall(r.getID() + "wallS", null, false, 0), 'S');
      r.add(new Wall(r.getID() + "wallW", null, false, 0), 'W');
   }
}