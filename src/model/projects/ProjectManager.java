package model.projects;

import java.util.Collection;

import org.openedit.Data;
import org.openedit.entermedia.MediaArchive;

import com.openedit.WebPageRequest;
import com.openedit.users.User;

public interface ProjectManager
{

	public abstract String getCatalogId();

	public abstract void setCatalogId(String inCatId);

	public Collection<UserCollection> loadCollections(WebPageRequest inReq);
	
	public void addAssetToLibrary(WebPageRequest inReq, MediaArchive archive, String libraryid, String assetid);

	public void addAssetToCollection(WebPageRequest inReq, MediaArchive archive, String libraryid, String assetid);
	
	public Collection<String> loadAssetsInCollection(WebPageRequest inReq, MediaArchive archive, String inCollectionId);
	
	public boolean addUserToLibrary(MediaArchive archive, Data inLibrary, User inUser);
}