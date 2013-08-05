package com.github.idragonfire.dragonskills;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import api.ActiveSkill;
import api.DSystem;
import api.Skill;

import com.github.idragonfire.dragonskills.skills.DiaFinder;
import com.github.idragonfire.dragonskills.skills.Hole;
import com.github.idragonfire.dragonskills.skills.LeaveWall;

public class Skills {
    private HashMap<String, Skill> skillList;

    public Skills(DragonSkillsPlugin plugin) {
        skillList = new HashMap<String, Skill>();
        List<Skill> skills = new ArrayList<Skill>();

        skills.add(new DiaFinder(plugin));
        skills.add(new Hole(plugin));
        skills.add(new LeaveWall(plugin));

        for (Skill skill : skills) {
            addSkill(skill);
        }
    }

    public List<Skill> getSkills() {
        return new ArrayList<Skill>(skillList.values());
    }

    public boolean hasSkill(String skillName) {
        return skillList.containsKey(skillName.toLowerCase());
    }

    public Skill getSkill(String skillName) {
        return skillList.get(skillName.toLowerCase());
    }

    public void addSkill(Skill skill) {
        skillList.put(skill.getSkillName().toLowerCase(), skill);
    }

    public void useSkill(String skillName, Player player) {
        if (!hasSkill(skillName)) {
            DSystem.log("found no skill: " + skillName);
            return;
        }
        if (!(getSkill(skillName) instanceof ActiveSkill)) {
            DSystem.log("no active skill");
            return;
        }
        ActiveSkill skill = (ActiveSkill) getSkill(skillName);
        skill.use(player);
    }
}
