package MusikSchieberPackage;

import java.io.File;

public interface MusicFile {
	public void setDuplicate(boolean duplicate);
	public void setDuplicateFilepath(String filepath);
	public void setTitle(String title);
	public void setArtist(String artist);
	public void setYear(String year);
	public void setTrackNumber(String number);
	public void setGenre(String genre);
	public void setAlbumTitle(String album);
	public void setFilename();	
	public String getFilePath();
	public boolean damaged();
	public boolean duplicate();
	public String getDuplicateFilepath();
	public String getTitle();
	public String getArtist();
	public String getYear();
	public String getTrackNumber();
	public String getGenre();
	public String getAlbumTitle();
	public boolean missingNonCriticalValues();
	public boolean missingCriticalValues();
	public String getFileName();
	public String getUpdatedFileName();
	public String getUpdatedFilePath();
	public void delete();
	public void saveTag(String filePath);
	public void replace(boolean keep);
	public String cleanString(String name);
	public String makeDir(String path);
	public String[] tagToArray();
	public void copyMP3(File original, File copy);
}
