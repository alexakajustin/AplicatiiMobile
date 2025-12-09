
package com.example.diaconescu_andrei_alexandru_1088;

import android.os.AsyncTask;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.example.diaconescu_andrei_alexandru_1088.Locatie;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class LocatieXML extends AsyncTask<URL, Void, InputStream> {

    InputStream ist = null;

    public List<Locatie> locatieList = new ArrayList<>();

    @Override
    protected InputStream doInBackground(URL... urls) {

        try {
            HttpURLConnection connection = (HttpURLConnection) urls[0].openConnection();
            connection.setRequestMethod("GET");
            ist = connection.getInputStream();

            // parse XML
            locatieList = parseXML(ist);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ist;
    }

    public static Node getNodeByName(String nodeName, Node parentNode) {
        if (parentNode.getNodeName().equals(nodeName))
            return parentNode;
        NodeList list = parentNode.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = getNodeByName(nodeName, list.item(i));
            if (node != null)
                return node;
        }
        return null;
    }

    public static String getAttributeValue(Node node, String attrName) {
        try {
            return ((Element) node).getAttribute(attrName);
        } catch (Exception ex) {
            return "";
        }
    }

    public List<Locatie> parseXML(InputStream ist) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document domDoc = db.parse(ist);
            domDoc.getDocumentElement().normalize();

            Node parinte = getNodeByName("Locatii", domDoc.getDocumentElement());
            if (parinte != null) {
                NodeList listaCopii = parinte.getChildNodes();
                for (int i = 0; i < listaCopii.getLength(); i++) {
                    Node copil = listaCopii.item(i);
                    if (copil != null && copil.getNodeName().equals("Locatie")) {
                        Locatie locatie = new Locatie("", "urban", 0, 0, "");

                        NodeList taguri = copil.getChildNodes();
                        for (int j = 0; j < taguri.getLength(); j++) {
                            Node tag = taguri.item(j);
                            if (tag != null) {
                                switch (tag.getNodeName()) {
                                    case "Nume":
                                        locatie.setNume(tag.getTextContent());
                                        break;
                                    case "Rating":
                                        locatie.setRating(Float.parseFloat(tag.getTextContent()));
                                        break;
                                    case "Puncte":
                                        locatie.setPuncte(Integer.parseInt(tag.getTextContent()));
                                        break;
                                    case "Tip":
                                        locatie.setTip(tag.getTextContent());
                                        break;
                                    case "Adresa":
                                        locatie.setAdresa(tag.getTextContent());
                                        break;
                                }
                            }
                        }
                        locatieList.add(locatie);
                    }
                }
            }

        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
        return locatieList;
    }
}
