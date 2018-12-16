package uk.co.infinityflame;

import java.io.File;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RM
  extends JavaPlugin
{
  public RM() {}
  
  public void onEnable()
  {
    cfg = getConfig();
    getLogger().info("ReviewMe Successfully Enabled!");
  }
  
  public void onDisable()
  {
    getLogger().info("ReviewMe Successfully Disabled!");
  }
  
  private File file = new File("plugins/ReviewMe", "config.yml");
  private FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
  
  public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
  {
    if (!(sender instanceof Player)) {
      sender.sendMessage("This command can only be run by a player.");
      return true;
    }
    if (commandLabel.equalsIgnoreCase("rm")) {
      Player player = (Player)sender;
      Location loc = player.getLocation();
      String name = player.getName();
      if (args.length == 0)
      {
        sender.sendMessage(ChatColor.BLUE + "Not Enough arguments, correct usage is");
        sender.sendMessage(ChatColor.GOLD + "/rm help");
        return true; }
      if (args[0].equalsIgnoreCase("help")) {
        sender.sendMessage(ChatColor.GOLD + "---------------------ReviewMe help-------------------");
        sender.sendMessage(ChatColor.GREEN + "/rm help" + ChatColor.BLUE + " = This page");
        sender.sendMessage(ChatColor.GREEN + "/rm gethelp" + ChatColor.BLUE + " = Sends a message to moderators that you require assistance");
        sender.sendMessage(ChatColor.GREEN + "/rm review" + ChatColor.BLUE + " = Submit your build to be reviewed");
        sender.sendMessage(ChatColor.GREEN + "/rm noreview" + ChatColor.BLUE + " = Cancel your build submission");
        sender.sendMessage(ChatColor.GREEN + "/rm list [P, A, D]" + ChatColor.BLUE + " = List builds that are to be reviewed, Pending or have been Denied");
        sender.sendMessage(ChatColor.GREEN + "/rm goto <username>" + ChatColor.BLUE + " = Teleport to a submitted build");
        sender.sendMessage(ChatColor.GREEN + "/rm complete <username>" + ChatColor.BLUE + " = Accept a build");
        sender.sendMessage(ChatColor.GREEN + "/rm deny <username>" + ChatColor.BLUE + " = Deny a build");
        sender.sendMessage(ChatColor.GREEN + "/rm check" + ChatColor.BLUE + " = Check to see if your build has been reviewed");
        
        return true; }
      if (args[0].equalsIgnoreCase("check")) {
        sender.sendMessage(ChatColor.GOLD + "---------------------ReviewMe Stats------------------");
        if (cfg.contains("rm." + name)) {
          double x = cfg.getDouble("rm." + name + ".complete");
          if (x == 1.0D) {
            sender.sendMessage(ChatColor.GREEN + " [A] " + ChatColor.BLUE + " Your Build has been reviewed, and Approved");
            return true; }
          if (x == 2.0D) {
            sender.sendMessage(ChatColor.RED + " [D] " + ChatColor.BLUE + " Your build was denied, to try again do" + ChatColor.GREEN + " /rm noreview");
            return true;
          }
          sender.sendMessage(ChatColor.GOLD + " [P] " + ChatColor.BLUE + " Your build is still pending");
          return true;
        }
        
        sender.sendMessage(ChatColor.GOLD + "Sorry, you have not submitted a build do " + ChatColor.GREEN + "/rm review" + ChatColor.GOLD + " to have your build reviewed.");
        return true;
      }
      if ((args[0].equalsIgnoreCase("gethelp")) && (player.hasPermission("reviewme.gethelp"))) {
        sender.sendMessage(ChatColor.GOLD + "Help is on its way! , a message has been sent to server staff");
        Bukkit.broadcast(ChatColor.BLUE + "[RM] " + ChatColor.RED + name + " has asked for assistance", "reviewme.gethelp");
        return true;
      }
      
      if ((args[0].equalsIgnoreCase("noreview")) && (player.hasPermission("reviewme.review"))) {
        cfg.set("rm." + name, null);
        saveConfig();
        sender.sendMessage(ChatColor.GOLD + "You have removed your build from the review list");
        if (player.hasPermission("reviewme.alert")) {
          player.sendMessage(ChatColor.BLUE + "[RM] " + ChatColor.GOLD + name + " has pulled thier build from review");
          return true;
        }
        return true;
      }
      if ((args[0].equalsIgnoreCase("review")) && (player.hasPermission("reviewme.review"))) {
        cfg.set("rm." + name + ".complete", null);
        cfg.set("rm." + name + ".world", loc.getWorld().getName().toString());
        cfg.set("rm." + name + ".x", Double.valueOf(loc.getX()));
        cfg.set("rm." + name + ".y", Double.valueOf(loc.getY()));
        cfg.set("rm." + name + ".z", Double.valueOf(loc.getZ()));
        cfg.set("rm." + name + ".yaw", Float.valueOf(loc.getYaw()));
        cfg.set("rm." + name + ".pitch", Float.valueOf(loc.getPitch()));
        saveConfig();
        sender.sendMessage(ChatColor.GOLD + "Your Set to be reviewed");
        Bukkit.broadcast(ChatColor.BLUE + "[RM] " + ChatColor.GOLD + name + " has submitted thier build for review", "reviewme.alert");
        return true;
      }
      if (args.length == 2) {
        if ((args[0].equalsIgnoreCase("goto")) && (player.hasPermission("reviewme.view"))) {
          if (cfg.contains("rm." + args[1] + ".x")) {
            World w = Bukkit.getWorld(cfg.getString("rm." + args[1] + ".world"));
            double x = cfg.getDouble("rm." + args[1] + ".x");
            double y = cfg.getDouble("rm." + args[1] + ".y");
            double z = cfg.getDouble("rm." + args[1] + ".z");
            float yaw = (float)cfg.getDouble("rm." + args[1] + ".yaw");
            float pitch = (float)cfg.getDouble("rm." + args[1] + ".pitch");
            Location loc1 = new Location(w, x, y, z, yaw, pitch);
            player.teleport(loc1);
            sender.sendMessage(ChatColor.GOLD + "Teleporting you to " + args[1] + "'s build.");
            sender.sendMessage(ChatColor.GOLD + "To confirm you have been do " + ChatColor.BLUE + "/rm <complete|deny> <username>");
            saveConfig();
            return true;
          }
          sender.sendMessage(ChatColor.GOLD + args[1] + " has not submitted a build.");
          return true;
        }
        if (args[0].equalsIgnoreCase("list")) {
          if (args[1].equalsIgnoreCase("P")) {
            sender.sendMessage(ChatColor.GOLD + "---------------------ReviewMe list-------------------");
            sender.sendMessage(ChatColor.GOLD + " [P] " + ChatColor.GOLD + "= Pending" + ChatColor.BLUE + " To list all, do " + ChatColor.GOLD + "/rm list");
            ConfigurationSection sec = getConfig().getConfigurationSection("rm");
            if (sec != null) {
              for (String key : sec.getKeys(false)) {
                double x = cfg.getDouble("rm." + key + ".complete");
                if ((x != 1.0D) && 
                  (x != 2.0D))
                {
                  sender.sendMessage(ChatColor.GOLD + " [P] " + ChatColor.BLUE + key);
                }
              }
              return true;
            }
          } else if (args[1].equalsIgnoreCase("A")) {
            sender.sendMessage(ChatColor.GOLD + "---------------------ReviewMe list-------------------");
            sender.sendMessage(ChatColor.GREEN + " [A] " + ChatColor.GOLD + "= Approved" + ChatColor.BLUE + " To list all, do " + ChatColor.GOLD + "/rm list");
            ConfigurationSection sec2 = getConfig().getConfigurationSection("rm");
            if (sec2 != null) {
              for (String key : sec2.getKeys(false)) {
                double x = cfg.getDouble("rm." + key + ".complete");
                if (x == 1.0D) {
                  sender.sendMessage(ChatColor.GREEN + " [A] " + ChatColor.BLUE + key);
                }
              }
              


              return true;
            }
          } else { if (args[1].equalsIgnoreCase("D")) {
              sender.sendMessage(ChatColor.GOLD + "---------------------ReviewMe list-------------------");
              sender.sendMessage(ChatColor.RED + " [D] " + ChatColor.GOLD + "= Denied" + ChatColor.BLUE + " To list all, do " + ChatColor.GOLD + "/rm list");
              ConfigurationSection sec3 = getConfig().getConfigurationSection("rm");
              if (sec3 != null) {
                for (String key : sec3.getKeys(false)) {
                  double x = cfg.getDouble("rm." + key + ".complete");
                  if ((x != 1.0D) && 
                    (x == 2.0D)) {
                    sender.sendMessage(ChatColor.RED + " [D] " + ChatColor.BLUE + key);
                  }
                }
              }
              



              return true;
            }
            sender.sendMessage(ChatColor.BLUE + "That is not a Valid command operator, try");
            sender.sendMessage(ChatColor.GOLD + "/rm list");
            return true;
          }
        } else { if ((args[0].equalsIgnoreCase("deny")) && (player.hasPermission("reviewme.deny"))) {
            Object bless = Integer.valueOf(2);
            cfg.set("rm." + args[1], null);
            cfg.set("rm." + args[1] + ".complete", bless);
            saveConfig();
            sender.sendMessage(ChatColor.GOLD + args[1] + "'s build has been set to denied!");
            Bukkit.getServer().getPlayer(args[1]).sendMessage(ChatColor.BLUE + "[RM] " + ChatColor.GOLD + "Your Build was assessed and" + ChatColor.RED + " Denied!");
            return true; }
          if ((args[0].equalsIgnoreCase("complete")) && (player.hasPermission("reviewme.complete"))) {
            Object yes = Integer.valueOf(1);
            cfg.set("rm." + args[1], null);
            cfg.set("rm." + args[1] + ".complete", yes);
            saveConfig();
            sender.sendMessage(ChatColor.GOLD + args[1] + "'s build has been set to completed!");
            Bukkit.getServer().getPlayer(args[1]).sendMessage(ChatColor.BLUE + "[RM] " + ChatColor.GOLD + "Your Build was assessed and" + ChatColor.GREEN + " Approved!");
            
            return true;
          }
        } } else if (args.length == 1) {
        if (args[0].equalsIgnoreCase("goto")) {
          sender.sendMessage(ChatColor.BLUE + "You did not specify a username, try");
          sender.sendMessage(ChatColor.GOLD + "/rm goto <username>");
          return true; }
        if ((args[0].equalsIgnoreCase("list")) && (player.hasPermission("reviewme.list"))) {
          sender.sendMessage(ChatColor.GOLD + "---------------------ReviewMe list-------------------");
          sender.sendMessage(ChatColor.GREEN + " [A] " + ChatColor.GOLD + "= Approved" + ChatColor.RED + " [D] " + ChatColor.GOLD + "= Denied" + ChatColor.GOLD + " [P] " + ChatColor.GOLD + "= Pending");
          ConfigurationSection sec = getConfig().getConfigurationSection("rm");
          if (sec != null) {
            for (String key : sec.getKeys(false)) {
              double x = cfg.getDouble("rm." + key + ".complete");
              if (x == 1.0D) {
                sender.sendMessage(ChatColor.GREEN + " [A] " + ChatColor.BLUE + key);
              } else if (x == 2.0D) {
                sender.sendMessage(ChatColor.RED + " [D] " + ChatColor.BLUE + key);
              } else {
                sender.sendMessage(ChatColor.GOLD + " [P] " + ChatColor.BLUE + key);
              }
            }
            
            return true;
          }
        } else { if (args[0].equalsIgnoreCase("complete")) {
            sender.sendMessage(ChatColor.BLUE + "You did not specify a username , try");
            sender.sendMessage(ChatColor.GOLD + "/rm complete <username>");
            return true;
          }
          return false;
        }
      }
    }
    else {
      sender.sendMessage(ChatColor.BLUE + "That is not a Valid command operator, try");
      sender.sendMessage(ChatColor.GOLD + "/rm help");
      return true;
    }
    return false;
  }
}
