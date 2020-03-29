package com.lukeshay.restapi.utils;

import com.google.gson.GsonBuilder;

import java.util.Collection;

public class ModelUtils {

	public static <T> String toString(T model) {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(model);
	}

	public static <T, U> Boolean equals(T modelOne, U modelTwo) {
		return modelOne.getClass().equals(modelTwo.getClass()) && modelOne.toString().equals(modelTwo.toString());
	}

	public static <T, U> Boolean collectionsEqual(
			Collection<T> collectionOne, Collection<U> collectionTwo
	) {
		if (collectionOne == null && collectionTwo == null) {
			return true;
		}
		else if ((collectionOne == null && collectionTwo != null) || (collectionTwo == null && collectionOne != null)) {
			return false;
		}
		else {
			return collectionOne.containsAll(collectionTwo) && collectionTwo.containsAll(collectionOne);
		}
	}
}
