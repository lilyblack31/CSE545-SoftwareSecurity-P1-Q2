/**
 *
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class WriteFile {

    /**
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        try {
            File folder = new File("selectedAPKs");
            String[] listOfFiles = folder.list();
            System.out.println(listOfFiles.length);
            List<String> list=new ArrayList<>();
            List<String> appName=new ArrayList<>();
            List<String> appPermission=new ArrayList<>();
            for (String name : listOfFiles) {
                list.add(name);
                //System.out.println(name);
            }

            for (int i = 0; i < listOfFiles.length; i++) {
                String s;
                Process p;
                Runtime r;
                try{
                    System.out.println(listOfFiles[i]);
                    r= Runtime.getRuntime();
                    p = r.exec("apktool d selectedAPKs/"+listOfFiles[i] +" -o decode/"+list.get(i));
                    BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    while ((s = br.readLine()) != null)
                        System.out.println("line: " + s);
                    p.waitFor();
                    //System.out.println ("exit: " + p.exitValue());
                    p.destroy();
                    //String json = data.toJSONString();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
            try {
                for (int i = 0; i < list.size(); i++) {
                    if(list.get(i).equals(".DS_Store")){
                        continue;
                    }
                    System.out.println("^^^^^^^^^^^^^");
                    System.out.println(list.get(i));
                    System.out.println("Size of list "+list.size());
                    System.out.println("###############");
                    File inputFile = new File("decode/"+list.get(i)+"/AndroidManifest.xml");
                    System.out.println(inputFile.getName());
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(inputFile);
                    doc.getDocumentElement().normalize();
                    System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
                    NodeList nList = doc.getElementsByTagName("uses-permission");
                    System.out.println("----------------------------");

                    //JSONObject jsonObject = new JSONObject();
                    for (int temp = 0; temp < nList.getLength(); temp++) {
                        Node nNode = nList.item(temp);
                        System.out.println("\nCurrent Element :" + nNode.getNodeName());
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            System.out.println(eElement.getAttribute("android:name"));
                            //hMap.put(list.get(0),eElement.getAttribute("android:name"));
                            appName.add(list.get(i));
                            appPermission.add(eElement.getAttribute("android:name"));
                            //jsonObject.put(listOfFiles[0], eElement.getAttribute("android:name"));
                        }
                    }

                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                FileWriter myWriter = new FileWriter("listoffiles.txt");
                for (int j = 0; j < appName.size(); j++) {
                    myWriter.write(appName.get(j));
                    myWriter.write(':');
                    myWriter.write(appPermission.get(j));
                    myWriter.write('\n');
                }
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        } catch( Exception e) {
            e.printStackTrace();
        }
    }
}

