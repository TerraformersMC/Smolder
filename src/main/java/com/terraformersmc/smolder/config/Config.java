package com.terraformersmc.smolder.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.terraformersmc.smolder.Smolder;

public class Config {
	private static JsonObject config;
	private static boolean rewrite = false;

	private static void load() {
		if (config == null) {
			File file = getFolder();
			if (!file.exists()) {
				file.mkdirs();
			}
			file = getFile();
			if (file.exists()) {
				Gson gson = new Gson();
				try {
					Reader reader = new FileReader(file);
					config = gson.fromJson(reader, JsonObject.class);
					if (config == null) {
						config = new JsonObject();
						rewrite = true;
					}
				}
				catch (FileNotFoundException e) {
					e.printStackTrace();
					config = new JsonObject();
					rewrite = true;
				}
			}
			else {
				config = new JsonObject();
				rewrite = true;
			}
		}
	}

	public static void save() {
		if (rewrite) {
			File file = getFolder();
			if (!file.exists()) {
				file.mkdirs();
			}
			file = getFile();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			try {
				FileWriter writer = new FileWriter(file);
				String gstring = gson.toJson(config);
				writer.write(gstring);
				writer.flush();
		        writer.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static File getFile() {
		return new File(String.format("./config/%s.json", Smolder.MOD_ID));
	}
	
	private static File getFolder() {
		return new File("./config/");
	}

	public static boolean getBoolean(String groups, String name, boolean def) {
		load();
		name += "[def: " + def + "]";
		
		JsonObject group = getGroup(groups);
		JsonElement element = group.get(name);
		
		if (element != null) {
			return element.getAsBoolean();
		} else {
			group.addProperty(name, def);
			rewrite = true;
			return def;
		}
	}
	
	public static void setBoolean(String groups, String name, boolean def, boolean value) {
		name += "[def: " + def + "]";
		
		JsonObject group = getGroup(groups);
		group.addProperty(name, value);
		
		rewrite = true;
	}
	
	public static float getFloat(String groups, String name, float def) {
		load();
		name += "[def: " + def + "]";
		
		JsonObject group = getGroup(groups);
		JsonElement element = group.get(name);
		
		if (element != null) {
			return element.getAsFloat();
		} else {
			group.addProperty(name, def);
			rewrite = true;
			return def;
		}
	}
	
	public static void setFloat(String groups, String name, float def, float value) {
		name += "[def: " + def + "]";
		
		JsonObject group = getGroup(groups);
		group.addProperty(name, value);
		
		rewrite = true;
	}
	
	public static int getInt(String groups, String name, int def) {
		load();
		name += "[def: " + def + "]";
		
		JsonObject group = getGroup(groups);
		JsonElement element = group.get(name);
		
		if (element != null) {
			return element.getAsInt();
		} else {
			group.addProperty(name, def);
			rewrite = true;
			return def;
		}
	}
	
	public static void setInt(String groups, String name, int def, int value) {
		name += "[def: " + def + "]";
		
		JsonObject group = getGroup(groups);
		group.addProperty(name, value);
		
		rewrite = true;
	}
	
	public static JsonObject getGroup(String groups) {
		JsonObject obj = config;
		String[] groupsArr = groups.split("\\.");
		for (String group: groupsArr) {
			JsonObject jGroup = obj.getAsJsonObject(group);
			if (jGroup == null) {
				jGroup = new JsonObject();
				obj.add(group, jGroup);
			}
			obj = jGroup;
		}
		return obj;
	}
}
