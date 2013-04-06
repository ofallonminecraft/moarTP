package com.ofallonminecraft.moarTP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.command.CommandSender;

public class View {

	public static boolean view(CommandSender sender, String[] args) {

		// load necessary files
		Map<String, MTLocation>   locations = null;
		Map<String, List<String>> creators  = null;
		try {
			locations = SLAPI.load("plugins/moarTP/moarTP_locs.bin");
			creators  = SLAPI.load("plugins/moarTP/moarTP_creators.bin");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// check number of arguments
		if (args.length > 1) {
			sender.sendMessage("Please choose one player name.");
			return false;
		}

		// ----- VIEW ----- //

		if (args.length == 1) {
			String creator = args[0].toLowerCase();
			// TODO: display all the creator's locs here (and error checking)
			// NOTE: this entire section can be refactored
		} else {
			Set<String>  viewLocs   = locations.keySet();  // set of locations
			List<String> sortedLocs = new ArrayList<String>(viewLocs);  // list of locs
			if (sortedLocs.size()>0) {
				// display all locations to player			
				Collections.sort(sortedLocs);
				Iterator<String> i = sortedLocs.iterator();
				String toView = i.next();
				while (i.hasNext()) {
					toView += ", " + i.next();
				}
				sender.sendMessage(toView);
			} else {
				sender.sendMessage("No locations claimed yet. Claim one with /claim <location>");
			}
		}

		// close file stream
		try {
			SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
		// ----- END VIEW ----- //

	}
}
