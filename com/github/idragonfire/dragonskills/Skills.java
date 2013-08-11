package com.github.idragonfire.dragonskills;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.idragonfire.dragonskills.api.ActiveSkill;
import com.github.idragonfire.dragonskills.api.DSystem;
import com.github.idragonfire.dragonskills.api.Skill;
import com.github.idragonfire.dragonskills.api.SkillResult;
import com.github.idragonfire.dragonskills.skills.DiaFinder;
import com.github.idragonfire.dragonskills.skills.Earthquake;
import com.github.idragonfire.dragonskills.skills.Fireman;
import com.github.idragonfire.dragonskills.skills.Firewall;
import com.github.idragonfire.dragonskills.skills.Hole;
import com.github.idragonfire.dragonskills.skills.LeaveWall;
import com.github.idragonfire.dragonskills.skills.Pillar;
import com.github.idragonfire.dragonskills.skills.Sponge;
import com.github.idragonfire.dragonskills.skills.Wallhack;
import com.github.idragonfire.dragonskills.skills.Waterfall;
import com.github.idragonfire.dragonskills.utils.SkillConfig;

public class Skills {
    private HashMap<String, Skill> skillList;
    private final File skillFolder;

    public Skills(DragonSkillsPlugin plugin) {
        skillList = new HashMap<String, Skill>();
        skillFolder = new File(plugin.getDataFolder(), "skills_config");
        skillFolder.mkdirs();

        List<Skill> skills = new ArrayList<Skill>();

        skills.add(new DiaFinder(plugin));
        skills.add(new Hole(plugin));
        skills.add(new LeaveWall(plugin));
        skills.add(new Firewall(plugin));
        skills.add(new Fireman(plugin));
        skills.add(new Sponge(plugin));
        skills.add(new Waterfall(plugin));
        skills.add(new Wallhack(plugin));
        skills.add(new Pillar(plugin));
        skills.add(new Earthquake(plugin));

        for (Skill skill : skills) {
            addSkill(skill);
            load(skill);
            save(skill);
        }
    }

    public List<Skill> getSkills() {
        return new ArrayList<Skill>(skillList.values());
    }

    public boolean hasSkill(String skillName) {
        return skillList.containsKey(skillName.toLowerCase());
    }

    public Skill getSkill(String skillName) {
        return skillList.get(skillName);
    }

    public void addSkill(Skill skill) {
        skillList.put(skill.getSkillName().toLowerCase(), skill);
    }

    public void useSkill(String skillName, DPlayer player) {
        skillName = skillName.toLowerCase();
        String perm = new StringBuilder("dragonskills.skill.")
                .append(skillName).toString();
        if (!player.getBukkitPlayer().hasPermission(perm)) {
            DSystem.log("missing perm $1", perm);
            return;
        }
        if (!hasSkill(skillName)) {
            DSystem.log("skill $1 not found", skillName);
            return;
        }
        if (!(getSkill(skillName) instanceof ActiveSkill)) {
            DSystem.log("no active skill");
            return;
        }
        if (player.hasCooldown(skillName)) {
            DSystem.log("on cooldown");
            return;
        }
        ActiveSkill skill = (ActiveSkill) getSkill(skillName);
        if (skill.use(player.getBukkitPlayer()) == SkillResult.SUCESSFULL) {
            player.addCooldown(skillName, skill.getCooldown());
        }
    }

    public List<String> materialList(Set<?> materialList) {
        List<String> list = new ArrayList<String>();
        for (Object mat : materialList) {
            if (mat instanceof Material) {
                list.add(mat.toString());
            }
        }
        return list;
    }

    // TODO: move to skills
    public void load(Skill skill) {
        File file = new File(skillFolder.getAbsolutePath() + File.separator
                + skill.getSkillName().toLowerCase() + ".yml");
        if (!file.exists()) {
            return;
        }
        FileConfiguration skillConfig = new YamlConfiguration();
        try {
            skillConfig.load(file);
            Map<String, Field> fieldMap = new HashMap<String, Field>();
            for (Field f : getSkillConfigFields(skill)) {
                fieldMap.put(f.getName(), f);
            }
            Field tmpField = null;
            for (String key : skillConfig.getKeys(false)) {
                if (fieldMap.containsKey(key)) {
                    tmpField = fieldMap.get(key);
                    tmpField.setAccessible(true);
                    if (tmpField.get(skill) instanceof Set<?>) {
                        List<String> tmpList = skillConfig.getStringList(key);
                        HashSet<Material> materials = new HashSet<Material>();
                        for (String materialString : tmpList) {
                            materials.add(Material.valueOf(materialString));
                        }
                        tmpField.set(skill, materials);
                    } else {
                        tmpField.set(skill, skillConfig.get(key));
                    }
                } else {
                    DSystem.log("cannot init: " + key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: move to skills
    public void save(Skill skill) {
        File file = new File(skillFolder.getAbsolutePath() + File.separator
                + skill.getSkillName().toLowerCase() + ".yml");
        FileConfiguration skillConfig = new YamlConfiguration();

        for (Field field : getSkillConfigFields(skill)) {
            try {
                field.setAccessible(true);
                Object toSave = field.get(skill);
                if (toSave instanceof Set<?>) {
                    skillConfig.set(field.getName(),
                            materialList((Set<?>) toSave));
                } else {
                    skillConfig.set(field.getName(), toSave);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            skillConfig.save(file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public List<Field> getSkillConfigFields(Skill skill) {
        List<Field> list = new ArrayList<Field>();
        for (Field field : getInheritedFields(skill.getClass())) {
            if (field.getAnnotation(SkillConfig.class) != null) {
                list.add(field);
            }
        }
        return list;
    }

    public static List<Field> getInheritedFields(Class<?> type) {
        List<Field> fields = new ArrayList<Field>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }
}
