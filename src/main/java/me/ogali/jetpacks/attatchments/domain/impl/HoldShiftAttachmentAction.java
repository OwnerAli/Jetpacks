package me.ogali.jetpacks.attatchments.domain.impl;

import me.ogali.jetpacks.attatchments.domain.AttachmentAction;
import me.ogali.jetpacks.players.JetpackPlayer;

public interface HoldShiftAttachmentAction extends AttachmentAction {

    @Override
    void trigger(JetpackPlayer jetpackPlayer);

}
