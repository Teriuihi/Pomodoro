package com.teri.alttd.Utilities;

import net.dv8tion.jda.api.Permission;

public class NeededPermissions {
    public enum guildPerms {
        POM_START(Permission.MANAGE_ROLES);

        private final Permission[] permissions;
        guildPerms(Permission... permissions){
            this.permissions = permissions;
        }

        public Permission[] getPermissions() {
            return permissions;
        }
    }

    public enum channelPerms {
        POM_START(Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION);

        private final Permission[] permissions;
        channelPerms(Permission... permissions){
            this.permissions = permissions;
        }

        public Permission[] getPermissions() {
            return permissions;
        }
    }
}
