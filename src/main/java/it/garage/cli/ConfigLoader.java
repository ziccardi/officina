package it.garage.cli;
import it.garage.Garage;
import it.garage.Platform;
import it.garage.operations.OperationFactory;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple class that loads the garage config from an XML file
 */
public class ConfigLoader {

    private static List<String> nodeListToStringArray(NodeList nl, String attName) {
        List<String> res = new ArrayList<>(nl.getLength());
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            res.add(node.getAttributes().getNamedItem(attName).getNodeValue());
        }
        return res;
    }

    public static Garage parseConfig(File f) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(f);
        Element el = document.getDocumentElement();

        // Getting all the operations - configuring the operation factory
        Node operations = el.getElementsByTagName("Operations").item(0);
        NodeList ops = operations.getChildNodes();
        for (int i = 0; i < ops.getLength(); i++) {
            Node op = ops.item(i);
            if (op.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            OperationFactory.configureOp(op.getAttributes().getNamedItem("name").getNodeValue(), Long.parseLong(op.getAttributes().getNamedItem("duration").getNodeValue()));
        }
        // Configuring the it.garage.Garage platforms...
        Garage garage = new Garage();

        Node platformsNode = el.getElementsByTagName("Platforms").item(0);
        NodeList platforms = platformsNode.getChildNodes();
        for (int i = 0; i < platforms.getLength(); i++) {
            Node platform = platforms.item(i);
            if (platform.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            String platformName = platform.getAttributes().getNamedItem("name").getNodeValue();
            // Get all the Vehicles
            List<String> vehicles = ConfigLoader.nodeListToStringArray(((Element) platform).getElementsByTagName("Vehicles").item(0).getChildNodes(), "name");
            List<String> platformOperations = ConfigLoader.nodeListToStringArray(((Element) platform).getElementsByTagName("Operations").item(0).getChildNodes(), "name");
            garage.addPlatform(new Platform(platformName, vehicles, platformOperations));
        }

        return garage;
    }
}
