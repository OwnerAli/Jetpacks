package me.ogali.jetpacks.attatchments.domain;

import me.ogali.jetpacks.players.JetpackPlayer;

@FunctionalInterface
public interface AttachmentAction {

    void trigger(JetpackPlayer jetpackPlayer);

}
