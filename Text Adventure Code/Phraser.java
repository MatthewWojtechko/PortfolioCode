// This class contains static methods, which return English prases based on given data.
package Game.Linguistics;

import java.util.ArrayList;

public class Phraser
{
   // Converts an ArrayList of Strings into a linguistic, English list phrase.
   public static String getListPhrase(ArrayList<String> thingsList)
   {
      String contentsString = "";
      if (thingsList.size() > 2)   // format String if there's 3+ matches
      {
         int index = 1;
         for (String X : thingsList) // step thru each thing
         {
            // Add the article: a, the, another
            if (!contentsString.contains(X) && !X.contains("wall"))  // if no repeat and no wall: a
               contentsString += "a ";
            else if (contentsString.contains(X)) // if it is a repeat: another
               contentsString += "another ";
            
            // Add the noun aka thing name 
            contentsString += X;
            
            // List item ending:   ,   and   nothing
            if (index > 1)  // 2+ remaining items: ,
               contentsString += ", ";
            else if (index == index-1)  // if 1: and
               contentsString += ", and ";
            // if 0: nothing
            index--;
         }
      
      }
      else if (thingsList.size() > 1) // if there's 2 matches
      {
         contentsString += "a " + thingsList.get(0) + " and ";
         if (!thingsList.get(1).equalsIgnoreCase(thingsList.get(0)))
            contentsString += "a " + thingsList.get(1);
         else
            contentsString += "another " + thingsList.get(1);
      }
      else if (thingsList.size() == 1) // if there's 1 match
         contentsString += "a " + thingsList.get(0);
      // else, there's no matches: leave String empty
      
      return contentsString;
   }
}