package com.openedit.modules.update;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.openedit.PlugIn;

import com.openedit.ModuleManager;
import com.openedit.OpenEditException;
import com.openedit.WebServer;
import com.openedit.page.manage.PageManager;
import com.openedit.util.XmlUtil;

public class PlugInFinder
{
	protected ModuleManager fieldModuleManager;
	protected WebServer fieldWebServer;
	protected PageManager fieldPageManager;
	//protected List fieldInstalledPlugIns;
	protected List fieldNotInstalledPlugIns;
	protected String fieldAppServerPath;
	protected String fieldId;
	
	public String getId()
	{
		return fieldId;
	}

	public void setId(String inId)
	{
		fieldId = inId;
	}

	public String getAppServerPath()
	{
		return fieldAppServerPath;
	}

	public void setAppServerPath(String inAppServerPath)
	{
		fieldAppServerPath = inAppServerPath;
	}

	public List getNotInstalledPlugIns()
	{
		if( fieldNotInstalledPlugIns == null)
		{
			loadPlugIns();
		}
		return fieldNotInstalledPlugIns;
	}

//	public List getInstalledPlugIns()
//	{
//		if( fieldInstalledPlugIns == null)
//		{
//			loadPlugIns();
//		}
//		return fieldInstalledPlugIns;
//	}
	public void loadPlugIns()
	{
		//List installed = getWebServer().getAllPlugIns();

		Map installedmap = new HashMap();
//		for (Iterator iterator = installed.iterator(); iterator.hasNext();)
//		{
//			PlugIn plugin = (PlugIn) iterator.next();
//			installedmap.put( plugin.getId(), plugin);
//		}
		
		List available = listAvailablePlugIns();
		List notinstalled = new ArrayList();
		for (Iterator iterator = available.iterator(); iterator.hasNext();)
		{
			Element found = (Element) iterator.next();
			String id = found.attributeValue("id");
			PlugIn plugin = (PlugIn) installedmap.get(id);
			if (plugin == null)
			{
				plugin = new PlugIn();
				plugin.setId(id);
				plugin.setInstalled(false);
				notinstalled.add(plugin);
			}
			populatePluginDetails(found, plugin);
		}
		
		//fieldInstalledPlugIns = installed;
		fieldNotInstalledPlugIns = notinstalled;
		checkDepends(available);

	}
	public List listAll()
	{
		List sorted = new ArrayList();
		//sorted.addAll(getInstalledPlugIns());
		sorted.addAll(getNotInstalledPlugIns());
		return sorted;
	}
	
	protected void checkDepends(List inAvailable)
	{
		for (Iterator iterator = inAvailable.iterator(); iterator.hasNext();)
		{
			Element found = (Element) iterator.next();
			String depends = found.attributeValue("depends");
			if( depends != null && depends.length() > 0)
			{
				String id = found.attributeValue("id");
				PlugIn thisone = getPlugIn(id);
				if( thisone != null)
				{
					String[] ids = depends.split(",");
					for (int i = 0; i < ids.length; i++)
					{
						PlugIn dep = getPlugIn(ids[i]);
						thisone.addDependsOn(dep);
					}
				}
			}
		}	
	}

	protected void populatePluginDetails(Element found, PlugIn plugin)
	{
		plugin.setVendorLink(found.attributeValue("vendorlink"));
		plugin.setAvailableVersion(found.attributeValue("currentversion"));
		plugin.setAvailableVersionNotes(found.elementTextTrim("currentversionnotes"));
		plugin.setLongDescription(found.elementTextTrim("description"));
		plugin.setInstallScript(found.attributeValue("installscript"));
		plugin.setTitle(found.attributeValue("title"));
	}
	
	public ModuleManager getModuleManager()
	{
		return fieldModuleManager;
	}

	public void setModuleManager(ModuleManager inModuleManager)
	{
		fieldModuleManager = inModuleManager;
	}

	protected List listAvailablePlugIns() 
	{
		XmlUtil util = new XmlUtil();
		try
		{
			URL link = new URL(getAppServerPath());
			Element root = util.getXml(link.openStream(), "UTF-8");
			return root.elements();
		}
		catch( Exception ex)
		{
			throw new OpenEditException(ex);
		}
	}

	public WebServer getWebServer()
	{
		return fieldWebServer;
	}

	public void setWebServer(WebServer inWebServer)
	{
		fieldWebServer = inWebServer;
	}

	public PageManager getPageManager()
	{
		return fieldPageManager;
	}

	public void setPageManager(PageManager inPageManager)
	{
		fieldPageManager = inPageManager;
	}

	public PlugIn getPlugIn(String inPluginid)
	{
//		for (Iterator iterator = getInstalledPlugIns().iterator(); iterator.hasNext();)
//		{
//			PlugIn plugin = (PlugIn) iterator.next();
//			if( inPluginid.equals(plugin.getId()) )
//			{
//				return plugin;
//			}
//		}
		for (Iterator iterator = getNotInstalledPlugIns().iterator(); iterator.hasNext();)
		{
			PlugIn plugin = (PlugIn) iterator.next();
			if( inPluginid.equals(plugin.getId()) )
			{
				return plugin;
			}
		}
		return null;
	}

}