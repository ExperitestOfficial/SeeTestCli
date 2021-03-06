package com.experitest.cloud.cli;

import com.experitest.cloud.v2.AccessKeyCloudAuthentication;
import com.experitest.cloud.v2.Cloud;
import com.experitest.cloud.v2.CloudClientDebug;
import com.experitest.cloud.v2.ProxyInformation;
import com.experitest.cloud.v2.pojo.DataProvider;
import com.experitest.cloud.v2.pojo.Project;
import com.inamik.text.tables.Cell;
import com.inamik.text.tables.GridTable;
import com.inamik.text.tables.grid.Border;
import com.inamik.text.tables.grid.Util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

public class CloudUtils {
	public static Cloud getCloud() throws IOException {
		File propFile = new File("cloud.properties");
		if(!propFile.exists()){
			throw new RuntimeException("cloud.properties file was not found in the current folder. You should first call set command");
		}
		Properties properties = new Properties();
		try(FileReader reader = new FileReader(propFile)){
			properties.load(reader);
		}
		String cloudName = properties.getProperty("cloud.name");
		if(cloudName == null){
			System.err.println("Cannot find cloud.name");
			return null;
		}
		System.out.println("Cloud: " + cloudName);
		String cloudUrl = properties.getProperty(cloudName + ".cloud.url");
		String accessKey = properties.getProperty(cloudName + ".cloud.accessKey");
		String proxyHost = properties.getProperty("proxy.host");
		String proxyPort = properties.getProperty("proxy.port");
		String proxyUser = properties.getProperty("proxy.user");
		String proxyPassword = properties.getProperty("proxy.password");
		ProxyInformation proxyInformation = null;
		if(proxyHost != null){
			proxyInformation = new ProxyInformation(proxyHost, Integer.parseInt(proxyPort), proxyUser, proxyPassword);
		}
		Cloud cloud = new Cloud(cloudUrl, new AccessKeyCloudAuthentication(accessKey), proxyInformation);
		return cloud;
	}

	public static int projectNameToId(Cloud cloud, String name){
		try {
			return Integer.parseInt(name);
		} catch (Exception ignore){}
		List<Project> projects = cloud.projects().get();
		for(Project p: projects){
			if(name.equalsIgnoreCase(p.getName())){
				return (int)p.getId();
			}
		}
		return -1;
	}
	public static void printAsTable(String filter, boolean csv, List<?> list, String[] more, String...fields){
		if(fields == null){
			return;
		}
		if(more == null){
			more = new String[0];
		}
		ArrayList<String[]> elements = new ArrayList<>();
		ArrayList<String[]> elementsFiltered = new ArrayList<>();
		if(list.size() > 0 && list.get(0) instanceof DataProvider){
			for(Object o: list){
				String[] values = new String[fields.length + more.length];
				String[] dp = ((DataProvider)o).getData(fields);
				System.arraycopy(dp, 0, values, 0, dp.length);
				for (int j = 0; j < more.length; j++) {
					Method[] methods = o.getClass().getMethods();
					String value = "N/A";
					for (Method m : methods) {
						if ((m.getName().equalsIgnoreCase("get" + more[j]) || m.getName().equalsIgnoreCase("is" + more[j])) &&
								m.getParameters().length == 0 && !m.getReturnType().equals(Void.TYPE)) {
							try {
								value = String.valueOf(m.invoke(o));
								break;
							} catch (Exception ignore) {
							}
						}
					}
					values[dp.length + j] = value;
				}
				elements.add(values);
			}
		} else {
			for (int i = 0; i < list.size(); i++) {
				String[] values = new String[fields.length + more.length];

				Object o = list.get(i);
				for (int j = 0; j < fields.length; j++) {
					Method[] methods = o.getClass().getMethods();
					String value = "N/A";
					for (Method m : methods) {
						if ((m.getName().equalsIgnoreCase("get" + fields[j]) || m.getName().equalsIgnoreCase("is" + fields[j])) &&
								m.getParameters().length == 0 && !m.getReturnType().equals(Void.TYPE)) {
							try {
								value = String.valueOf(m.invoke(o));
								break;
							} catch (Exception ignore) {
							}
						}
					}
					values[j] = value;
				}
				for (int j = 0; j < more.length; j++) {
					Method[] methods = o.getClass().getMethods();
					String value = "N/A";
					for (Method m : methods) {
						if ((m.getName().equalsIgnoreCase("get" + more[j]) || m.getName().equalsIgnoreCase("is" + more[j])) &&
								m.getParameters().length == 0 && !m.getReturnType().equals(Void.TYPE)) {
							try {
								value = String.valueOf(m.invoke(o));
								break;
							} catch (Exception ignore) {
							}
						}
					}
					values[fields.length + j] = value;
				}
				elements.add(values);
			}
		}
		fields = Stream.concat(Arrays.stream(fields), Arrays.stream(more))
				.toArray(String[]::new);
		if(filter != null){
			String[] filters = filter.toLowerCase().split(",");
			for(String[] line: elements){
				String lstring = Arrays.toString(line).toLowerCase();
				boolean allFound = true;
				for(String f: filters){
					if(!lstring.contains(f)){
						allFound = false;
						break;
					}
				}
				if(allFound){
					elementsFiltered.add(line);
				}
			}
		} else {
			elementsFiltered = elements;
		}

		if(elementsFiltered.size() > 1000 || csv){
			for (int i = 0; i < fields.length; i++) {
				System.out.print(fields[i]);
				System.out.print(",");
			}
			System.out.print("\n");
			for (int i = 0; i < elementsFiltered.size(); i++) {
				for (int j = 0; j < fields.length; j++) {
					System.out.print(elementsFiltered.get(i)[j]);
					System.out.print(",");

				}
				System.out.print("\n");
			}
		} else {
			GridTable g = new GridTable(elementsFiltered.size() + 1, fields.length);
			for (int i = 0; i < fields.length; i++) {
				g.put(0, i, Cell.of(fields[i].toUpperCase()));
			}
			for (int i = 0; i < elementsFiltered.size(); i++) {
				for (int j = 0; j < fields.length; j++) {
					try {
						g.put(i + 1, j, Cell.of(elementsFiltered.get(i)[j]));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
			g = Border.SINGLE_LINE.apply(g);
			Util.print(g);
		}

	}
	public static void saveToFile(String fileName, String content) throws IOException{
		if(fileName.contains("${data}")){
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyymmdd");
			String strDate= formatter.format(date);
			fileName = fileName.replace("${data}", strDate);
		}
		if(fileName.contains("${time}")){
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("HHmm");
			String strDate= formatter.format(date);
			fileName = fileName.replace("${time}", strDate);
		}
		File file = new File(fileName);
		try (FileWriter writer = new FileWriter(file)){
			writer.write(content);
		}
	}
}
