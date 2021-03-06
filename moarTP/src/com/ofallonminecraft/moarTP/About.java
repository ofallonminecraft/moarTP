package com.ofallonminecraft.moarTP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import com.ofallonminecraft.SpellChecker.SpellChecker;

public class About {

  public static boolean about(CommandSender sender, String[] args, Connection c) {

    // check permissions and number of args
    if (sender.hasPermission("moarTP.about")) {
      if (args.length > 1) {
        sender.sendMessage("Too many arguments!");
        return false;
      }
      if (args.length < 1) {
        sender.sendMessage("Not enough arguments!");
        return false;
      }

      // ----- ABOUT ----- //
      try {
        PreparedStatement s = c.prepareStatement("select info,creator,creationTime from moarTP where location=?;");
        s.setString(1, args[0].toLowerCase());
        ResultSet rs = s.executeQuery();
        if (!rs.isBeforeFirst()) {
          sender.sendMessage(args[0] + " is not in the library!");
          HashSet<String> dict_sub = new HashSet<String>();
          dict_sub.add(SpellChecker.LOCATIONS);
          String sug = new SpellChecker(c, dict_sub).getSuggestion(args[0].toLowerCase());
          if (sug != null) {
            sender.sendMessage("Did you mean \"/about " + sug + "\"?");
          }
          return true;
        } else {
          String locInfo = "\n"+args[0].toLowerCase()+":\n";
          rs.next();
          boolean descriptionNull = false;
          boolean timeNull        = false;
          boolean creatorNull     = false;
          if (rs.getString(1)!=null && !rs.getString(1).equals("null")) {
            locInfo+=rs.getString(1)+"\n";
          } else descriptionNull = true;
          if (rs.getString(2)!=null && !rs.getString(2).equals("null")) {
            locInfo+="Created by "+ Bukkit.getServer().getOfflinePlayer(UUID.fromString(rs.getString(2))).getName();
            if (rs.getString(3)!=null && !rs.getString(3).equals("null")) {
              locInfo+=" on "+rs.getString(3)+".";
            } else timeNull = true;
          } else creatorNull = true;
          if (descriptionNull && (timeNull || creatorNull)) {
            sender.sendMessage("Information for "+args[0].toLowerCase()
                + " is unavailable. It appears that this location was"
                + " created with an earlier version of the moarTP"
                + " plugin.");
          } else sender.sendMessage(locInfo);
        }
        rs.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
      return true;
      // ----- END ABOUT ----- //
    }
    sender.sendMessage("You don't have permission to do this!");
    return false;
  }
}
