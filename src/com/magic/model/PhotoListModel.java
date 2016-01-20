package com.magic.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.magic.upload.util.ImageItem;

/**
 * 用于存储  ---某个相册
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class PhotoListModel implements Serializable{
	
	ArrayList<ImageItem> photoList;

	public PhotoListModel(ArrayList<ImageItem> photoList) {
		super();
		this.photoList = photoList;
	}

	public ArrayList<ImageItem> getPhotoList() {
		return photoList;
	}

	public void setPhotoList(ArrayList<ImageItem> photoList) {
		this.photoList = photoList;
	}
	
}
