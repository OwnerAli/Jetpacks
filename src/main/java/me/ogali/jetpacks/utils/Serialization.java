package me.ogali.jetpacks.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class Serialization {

    public static String serialize(ItemStack item) {

        String encodedObject;

        try {
            ByteArrayOutputStream io = new ByteArrayOutputStream();
            BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);
            os.writeObject(item);
            os.flush();
            os.close();

            byte[] serializedObject = io.toByteArray();
            encodedObject = Base64.getEncoder().encodeToString(serializedObject);

            return encodedObject;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ItemStack deserialize(String s) {
        byte[] b = Base64.getDecoder().decode(s);
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(b);
            BukkitObjectInputStream is = new BukkitObjectInputStream(in);

            return (ItemStack) is.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
