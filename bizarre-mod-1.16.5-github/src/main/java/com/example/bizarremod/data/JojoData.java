package com.example.bizarremod.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

/** Dati JoJo di un giocatore (razza, stile, stand, energia). Solo lato server. */
public class JojoData {
    private static final String KEY = "bizarremod";
    private static final Map<UUID, JojoData> CACHE = new HashMap<UUID, JojoData>();

    public Race race = Race.HUMAN;
    public FightStyle style = FightStyle.NONE;
    public StandType stand = StandType.NONE;
    public boolean standActive = false;           // non salvato: al login e' sempre false
    public float energy = 100f;
    public int selectedMove = 0;                  // 0 = raffica, 1-3 = abilita' dello stand
    public final long[] cooldowns = new long[6];  // 0-3 mosse stand, 4 stile, 5 razza (non salvati)

    public static JojoData get(PlayerEntity p) {
        JojoData d = CACHE.get(p.getUniqueID());
        if (d == null) {
            d = new JojoData();
            d.read(p.getPersistentData().getCompound(KEY));
            CACHE.put(p.getUniqueID(), d);
        }
        return d;
    }

    /** Scrive i dati nel salvataggio del giocatore. */
    public static void save(PlayerEntity p) {
        JojoData d = CACHE.get(p.getUniqueID());
        if (d != null) p.getPersistentData().put(KEY, d.write());
    }

    /** Salva e toglie dalla cache (al logout). */
    public static void saveAndForget(PlayerEntity p) {
        save(p);
        CACHE.remove(p.getUniqueID());
    }

    private CompoundNBT write() {
        CompoundNBT t = new CompoundNBT();
        t.putString("race", race.id);
        t.putString("style", style.id);
        t.putString("stand", stand.id);
        t.putFloat("energy", energy);
        t.putInt("move", selectedMove);
        return t;
    }

    private void read(CompoundNBT t) {
        race = Race.byId(t.getString("race"));
        style = FightStyle.byId(t.getString("style"));
        stand = StandType.byId(t.getString("stand"));
        energy = t.contains("energy") ? t.getFloat("energy") : 100f;
        selectedMove = Math.floorMod(t.getInt("move"), 4);
    }
}
