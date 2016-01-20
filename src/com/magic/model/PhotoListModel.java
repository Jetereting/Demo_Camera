package com.magic.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.magic.upload.util.ImageItem;

/**
 * ���ڴ洢  ---ĳ�����
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
