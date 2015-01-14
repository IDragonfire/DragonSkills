package com.github.idragonfire.dragonskills.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.Bukkit;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.Skill;

public class SkillLoader extends URLClassLoader {
	private final DragonSkillsPlugin plugin;

	public SkillLoader(DragonSkillsPlugin plugin) {
		super(((URLClassLoader) plugin.getClass().getClassLoader()).getURLs(),
				plugin.getClass().getClassLoader());
		this.plugin = plugin;
		File dir = new File(plugin.getDataFolder(), "skills");

		Bukkit.broadcastMessage(dir.toString());
		File file = null;
		for (String skillFile : dir.list()) {
			if (skillFile.contains(".jar")) {
				file = new File(dir, skillFile);
				try {
					addURL(file.toURI().toURL());
					loadSkill(file);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String getMainClass(File f) {
		String mainClass = null;
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(f);
			Enumeration<JarEntry> entries = jarFile.entries();

			while (entries.hasMoreElements()) {
				JarEntry element = (JarEntry) entries.nextElement();
				if (element.getName().equalsIgnoreCase("skill.info")) {
					BufferedReader reader;
					reader = new BufferedReader(new InputStreamReader(
							jarFile.getInputStream(element)));
					mainClass = reader.readLine().substring(12);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				jarFile.close();
			} catch (IOException e) {
				// nothing
			}
		}
		return mainClass;
	}

	public Skill loadSkill(File f) {
		String mainClass = getMainClass(f);
		if (mainClass == null) {
			return null;
		}
		try {
			Class<?> clazz = Class.forName(mainClass, true, this);
			Class<? extends Skill> skillClass = clazz.asSubclass(Skill.class);
			Constructor<? extends Skill> constructor = skillClass
					.getConstructor(new Class[] { this.plugin.getClass() });
			Skill skill = (Skill) constructor
					.newInstance(new Object[] { this.plugin });
			this.plugin.getSkills().addSkill(skill);
			return skill;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
