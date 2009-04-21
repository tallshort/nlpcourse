import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


public class Reader {
	String path;
	HashMap<String,String> hm;
	Document d;
	public Reader(String path) {
		super();
		hm=new HashMap<String,String>();
		this.path = path;
		try
		{
			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
			DocumentBuilder builder=factory.newDocumentBuilder();
			d=builder.parse(path);
			d.normalize();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public HashMap<String,String> read()
	{
		NodeList nl=d.getElementsByTagName("lexelt");
		for(int i=0;i<nl.getLength();i++)
		{
			String lexelt=nl.item(i).getAttributes().item(0).getTextContent();
			//System.out.println("µÚ"+i+"¸ö´Ê£º"+lexelt);
			hm.put(String.valueOf(i),lexelt );
		}
		return hm;
	}
}

