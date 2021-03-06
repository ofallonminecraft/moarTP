package com.ofallonminecraft.moarTP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ofallonminecraft.SpellChecker.SpellChecker;

public class SetHome {

  // TODO: fix result set closure issues

  @SuppressWarnings("deprecation")
  public static boolean sethome(CommandSender sender, String[] args, Player player, Connection c) {
    if (sender.hasPermission("moarTP.sethome")) {

      // check number of arguments
      if (args.length < 1) {
        sender.sendMessage("Must enter a location name!");
        return false;
      }
      if (args.length > 1) {
        sender.sendMessage("You can only choose one home!");
        return false;
      }

      // ----- SETHOME ----- //
      try {
        PreparedStatement s = c.prepareStatement("select home,secret from moarTP where location=?;");
        s.setString(1, args[0].toLowerCase());
        ResultSet rs = s.executeQuery();
        if (!rs.next()) {
          sender.sendMessage(args[0].toLowerCase()+" could not be found in the library!"
              + "  Choose an existing location to be your home.");
          HashSet<String> dict_subs = new HashSet<String>();
          dict_subs.add(SpellChecker.LOCATIONS);
          String sug = new SpellChecker(c, dict_subs).getSuggestion(args[0].toLowerCase());
          if (sug != null) {
            sender.sendMessage("Did you mean \"/sethome " + sug + "\"?");
          }
        } else {
          if (rs.getString(2).equals("Y")) {
            player.sendMessage("Sorry, you cannot set a secret location as your home.");
            return true;
          }
          String oldHomeList = rs.getString(1);
          PreparedStatement s2 = c.prepareStatement("select home,location,secret from moarTP where home LIKE ?;");
          s2.setString(1, "%"+player.getUniqueId().toString()+"%");
          ResultSet rs2 = s2.executeQuery();
          if (rs2.next()) {
            boolean playerVerified = false;
            boolean hasNext = true;
            while (!playerVerified && hasNext) {
              String[] playerList = rs2.getString(1).split(",");
              for (String person : playerList) {
                if (person.equals(player.getUniqueId().toString())) {
                  playerVerified = true;
                  if (rs2.getString(2).equals(args[0].toLowerCase())) {
                    sender.sendMessage(args[0].toLowerCase()+" is already your home!");
                  } else {
                    // clear player's old home
                    String newPlayerList = "";
                    for (String person2 : playerList) {
                      if (!person2.equals(player.getUniqueId().toString())) {
                        // TODO: find another way (getOfflinePlayer is deprecated)
                        if (!newPlayerList.equals("")) newPlayerList += ","
                            + Bukkit.getServer().getOfflinePlayer(person2).getUniqueId().toString();
                        else newPlayerList = person2;
                      }
                    }
                    String template = "update moarTP set home = ? where location = ?;";
                    PreparedStatement update = c.prepareStatement(template);
                    if (newPlayerList.equals("")) update.setNull(1, 12);
                    else update.setString(1, newPlayerList);
                    update.setString(2, rs2.getString(2));
                    update.executeUpdate();

                    // add player home
                    addPlayerHome(player, oldHomeList, c, args[0].toLowerCase());

                    sender.sendMessage("Old home ("+rs2.getString(2)+") overwritten; new home"
                        + " set to "+args[0].toLowerCase()+".");
                  }
                }
              }
              hasNext = rs.next();
            }
            if (!playerVerified) {
              // add player home
              addPlayerHome(player, oldHomeList, c, args[0].toLowerCase());
            }
          } else {
            // add player home
            addPlayerHome(player, oldHomeList, c, args[0].toLowerCase());
          }
        }
        rs.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
      return true;
      // ----- END SETHOME ----- //

    } else {
      sender.sendMessage("You don't have permission to do this!");
      return false;
    }
  }

  public static void addPlayerHome(Player player, String oldPlayerList, Connection c, String newHome) {
    String newPlayerList = "";
    if (oldPlayerList==null || oldPlayerList.equals("null")) newPlayerList = player.getUniqueId().toString();
    else newPlayerList = oldPlayerList+","+player.getUniqueId().toString();
    String template = "update moarTP set home = ? where location = ?;";
    PreparedStatement update;
    try {
      update = c.prepareStatement(template);
      update.setString(1, newPlayerList);
      update.setString(2, newHome);
      update.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
    player.sendMessage("New home set to "+newHome+".");
  }

}
