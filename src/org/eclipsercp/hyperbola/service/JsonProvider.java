package org.eclipsercp.hyperbola.service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import org.eclipsercp.hyperbola.controller.NodeController;
import org.eclipsercp.hyperbola.handler.InterfaceAdapter;
import org.eclipsercp.hyperbola.model.GroupNode;
import org.eclipsercp.hyperbola.model.INode;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * A service for serializing / deserializing tree of nodes to / from file.
 */
public class JsonProvider {

	private static final String JSON_PROVIDER = "JSON_PROVIDER";

	/**
	 * Serializes tree of nodes to the file.
	 * 
	 * @param filename
	 *            the name of the file
	 */
	public static void saveTreeOfNodesToFile(String filename) {
		try (FileWriter file = new FileWriter(filename)) {

			switch (PropertyService.getInstance().getPropertyValue(JSON_PROVIDER)) {
			case "gson": {
				Gson gson = new GsonBuilder().registerTypeAdapter(INode.class, new InterfaceAdapter<INode>()).create();
				gson.toJson(NodeController.getInstance().getNodeList(), file);
				break;
			}
			case "jackson": {
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
				mapper.writeValue(file, NodeController.getInstance().getNodeList());
				break;
			}
			default:
				break;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deserializes tree of nodes from the file.
	 * 
	 * @param filename
	 *            the name of the file
	 */
	@SuppressWarnings("unchecked")
	public static void loadTreeOfNodesFromFile(String filename) {
		try (FileReader file = new FileReader(filename)) {

			switch (PropertyService.getInstance().getPropertyValue(JSON_PROVIDER)) {
			case "gson": {
				Gson gson = new GsonBuilder().registerTypeAdapter(INode.class, new InterfaceAdapter<INode>()).create();
				Type listOfTestObject = new TypeToken<List<GroupNode>>() {
				}.getType();

				List<GroupNode> itemList = (List<GroupNode>) gson.fromJson(file, listOfTestObject);
				for (GroupNode group : itemList) {
					setParentToChildren(group);
				}
				//NodeController.getInstance().setNodeList(itemList);
				break;
			}
			case "jackson": {
				ObjectMapper mapper = new ObjectMapper();
				mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

				TypeReference<List<INode>> type = new TypeReference<List<INode>>() {
				};
				List<INode> itemList = mapper.readValue(file, type);

				for (INode group : itemList) {
					setParentToChildren(group);
				}
				NodeController.getInstance().setNodeList(itemList);
				break;
			}
			default:
				break;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void setParentToChildren(INode node) {
		for (INode child : node.getChildren()) {
			child.setParent(node);
			setParentToChildren(child);
		}
	}

}
