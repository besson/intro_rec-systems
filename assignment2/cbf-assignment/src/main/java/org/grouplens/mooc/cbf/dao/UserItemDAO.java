package org.grouplens.mooc.cbf.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongSet;

import javax.annotation.Nullable;

import org.grouplens.lenskit.cursors.Cursor;
import org.grouplens.lenskit.data.dao.DataAccessException;
import org.grouplens.lenskit.util.DelimitedTextCursor;

public class UserItemDAO implements ItemTitleDAO {
	
	private File titleFile;
	private Long2ObjectMap<List<String>> cache;
	
	public UserItemDAO(File file) {
        titleFile = file;
        loadTitleCache();
    }

	@Override
	public LongSet getItemIds() {
		return null;
	}

	@Override
	@Nullable
	public String getItemTitle(long item) {
		return cache.get(item).get(0);
	}
	
	public List<String> getUserTitles(long user) {
		return cache.get(user);
	}
	
	 private Long2ObjectMap<List<String>> loadTitleCache() {
	        cache = new Long2ObjectOpenHashMap<List<String>>();
	        Cursor<String[]> lines = null;
	        try {
	            lines = new DelimitedTextCursor(titleFile, ",");
	        } catch (FileNotFoundException e) {
	            throw new DataAccessException("cannot open file", e);
	        }
	        try {
	            for (String[] line: lines) {
	                long mid = Long.parseLong(line[0]);
	                if (cache.containsKey(mid)) {
	                	cache.get(mid).add(line[1]);	                	
	                } else {
	                	List<String> items = new ArrayList<String>();
	                	items.add(line[1]);
	                	cache.put(mid, items);	                	
	                }
	            }
	        } finally {
	            lines.close();
	        }
	        return cache;
	    }

}
