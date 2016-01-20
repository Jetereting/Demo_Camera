package com.magic.upload.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;

public class Res {

	private static String pkgName;
	private static Resources resources;
	public static void init(Context context) {
		pkgName = context.getPackageName();
		resources = context.getResources();
	}
	public static int getLayoutID(String layoutName) {
		return resources.getIdentifier(layoutName, "layout", pkgName);
	}

	public static int getWidgetID(String widgetName) {
		return resources.getIdentifier(widgetName, "id", pkgName);
	}

	public static int getAnimID(String animName) {
		return resources.getIdentifier(animName, "anim", pkgName);
	}
	public static int getXmlID(String xmlName) {
		return resources.getIdentifier(xmlName, "xml", pkgName);
	}

	public static XmlResourceParser getXml(String xmlName) {
		int xmlId = getXmlID(xmlName);
		return (XmlResourceParser) resources.getXml(xmlId);
	}

	public static int getRawID(String rawName) {
		return resources.getIdentifier(rawName, "raw", pkgName);
	}

	public static int getDrawableID(String drawName) {
		return resources.getIdentifier(drawName, "drawable", pkgName);
	}

	public static Drawable getDrawable(String drawName) {
		int drawId = getDrawableID(drawName);
		return resources.getDrawable(drawId);
	}

	public static int getAttrID(String attrName) {
		return resources.getIdentifier(attrName, "attr", pkgName);
	}

	public static int getDimenID(String dimenName) {
		return resources.getIdentifier(dimenName, "dimen", pkgName);
	}

	public static int getColorID(String colorName) {
		return resources.getIdentifier(colorName, "color", pkgName);
	}

	public static int getColor(String colorName) {
		return resources.getColor(getColorID(colorName));
	}

	public static int getStyleID(String styleName) {
		return resources.getIdentifier(styleName, "style", pkgName);
	}

	public static int getStringID(String strName) {
		return resources.getIdentifier(strName, "string", pkgName);
	}

	public static String getString(String strName) {
		int strId = getStringID(strName);
		return resources.getString(strId);
	}

	public static int[] getInteger(String strName) {
		return resources.getIntArray(resources.getIdentifier(strName, "array",
				pkgName));
	}

}
