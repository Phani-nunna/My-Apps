package com.svaad.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {

	public static List<String> getHashTags(String commentText) {
		List<String> resultedTags = new ArrayList<String>();
		Pattern MY_PATTERN = Pattern.compile("#(\\w+)");
		Matcher mat = MY_PATTERN.matcher(commentText);

		while (mat.find()) {

			resultedTags.add(mat.group(1));
		}

		return resultedTags;
	}

}
