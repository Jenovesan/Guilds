# Guilds
### Allows player to create and join guilds.

---

## How to:
### Create a guild
`/g create <guild name> <?guild description?>`  
### Join a guild
`/g join <guild name | player name>`  
### Leave a guild
`/g leave`

---

## Guilds' Features
- Invite/deinvite players to their guild
- Kick players from the guild
- Guild ranks
  - Recruit, Member, Mod, Coleader, Leader
- Promote/demote guild members
- Chat channels for each guild rank
  - Each rank has their own channel in the guild.
  - Only guild members of that rank or higher can view and send messages in that channel
- Claims
  - Guilds can claim/unclaim land
  - Players not in the guild cannot break blocks in the guild's claims
  - Can claim/unclaim single chunks, an area of chunks, or claim wherever they walk
- Set the guild's description
- Disband the guild
- Set a guild home
  - Guild members can teleport to the guild home
- List the players in the guild
  - States whether each player is online or offline
- See a map of nearby guild claims
  -  Shows the claims of all guilds (not just yours)
  -  Color codes the claims so each guild has their own color for claims
  -  Marks your location on the map
  -  Gives cardinal directions on the map
- Guild power
  - Used for claiming land. The more power your guild has the more land you can claim
  - You gain permanent power for each member that is in your guild
    - Bigger guilds have more power and can have more land
  - If you have more land than power, other guilds can overclaim your land
  - You temporarily lose power when a player in the guild dies
    - Makes it so you have to be careful to not die too much in a short period or another guild may overclaim your guild's land
- Guild relations
  - Can enemy and truce other guilds
- Can view info on other guilds
- Can raid other guilds where you can break blocks in their claims

---

## Some Images of the Plugin
### Guild Claim Map
![image](https://github.com/Jenovesan/GuildWars/assets/67431462/923aa9d9-6dff-4b5b-a6fc-409d1da02ac4)
### Guild Commands 
![image](https://github.com/Jenovesan/GuildWars/assets/67431462/e9e5e0fe-031b-4cf9-ba54-9bc68f023d73)

--- 

## Cool things  
### The event system is very good. Very easy to add new commands with aliases, arguments, and player requirements  
Example: Command that renames a player's guild's description  

    public class gDesc extends gCommand {

      public gDesc() {
          // Name
          super("desc");
  
          // Alliases
          addAlias("description");
  
          // Reqs
          addReq(new InGuildReq());
          addReq(new GuildPermissionReq(GuildPermission.SET_DESC));
  
          // Args
          addArg(new GuildDescArg(true));
      }
  
      @Override
      public void perform() throws CmdException {
          // Args
          String description = readNextArg();
  
          // Apply
          guild.setDescription(description);
  
          // Inform
  
          // Send guild announcement
          guild.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.description set", guild.describe(gPlayer), description));
  
          // Inform
          gPlayer.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.desc.success", description));
      }
    }

  
### Guild claims were mapped to a 3d array for a low-memory, pre-filled, instant-access, and asyncable claiming system   
Similar plugins utilize a hashmap system for mapping guild plugins, so this plugin using a 3d array makes this plugin much faster since
getting the guild at a location is called very frequently.  
    
    public class Board extends Coll<GuildChunk>{
        ...  
	    private final GuildChunk[][] board = new GuildChunk[worldClaimRadius * 2][worldClaimRadius * 2];  
        ...
    }
 
### Reverse Spiral claiming system
When claiming a square of chunks, claiming occurs in a reverse spiral incase the guild runs out of power mid-claiming and to keep claims connected

    private GuildChunk[] getSquareChunks(int radius) {
        // Uses a reverse-spiral matrix algorithm so claims will be connected while claiming in a radius

        // Get starting position
        int x = player.getLocation().getChunk().getX();
        int z = player.getLocation().getChunk().getZ();

        // Create variables
        int diameter = (radius * 2) + 1;
        int size = diameter * diameter;
        GuildChunk[] nearbyChunks = new GuildChunk[size];

        // Create variables for reverse-spiral
        int len = 0;
        int d = 0;
        int[] directions = new int[]{0, 1, 0, -1, 0};

        // Reverse-spiral
        for (int i = 0; i < size;) {
            if (d == 0 || d == 2) {
                len++;
            }
            for (int k = 0; k < len; k++) {
                nearbyChunks[i++] = Board.get().getGuildChunkAt(x, z);
                x += directions[d];
                z += directions[d + 1];
            }

            d = ++d % 4;
        }
        return nearbyChunks;
    }

---

## Notes
- The file system and some code-features of this plugin may seem odd. This is because this plugin was meant to be a part of a larger plugin
  - If I was making this plugin today I would have separated this plugin and make an API that other plugins could use
- This plugin (Guilds) was about 90% complete. Moved onto stock trading algorithms

## What I learned 
* I gained valuable insights into the significance of proper OOP and design patterns. Iteratively, I refactored substantial sections of this plugin multiple times as I identified flaws in the existing system that would pose challenges down the line
* Learned the importance of writing clean, simple, and readable code in large projects
