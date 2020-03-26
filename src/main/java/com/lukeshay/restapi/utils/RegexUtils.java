package com.lukeshay.restapi.utils;

public class RegexUtils {

	public static String VALID_EMAIL_REGEX =
		"^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
	public static String VALID_PASSWORD_REGEX = ".";
	public static String VALID_US_ZIP_CODE = "^\\d{5}(-\\d{4})?$";
	public static String VALID_STATE =
		"^(AE|AL|AK|AP|AS|AZ|AR|CA|CO|CT|DE|DC|FM|FL|GA|GU|HI|ID|IL|IN|IA|KS|KY|LA|ME|MH|MD|MA|MI|MN|MS|MO|MP|MT|NE|NV|NH|NJ|NM|NY|NC|ND|OH|OK|OR|PW|PA|PR|RI|SC|SD|TN|TX|UT|VT|VI|VA|WA|WV|WI|WY)$";

	public static boolean isValidEmail(String email) {
		return email.matches(VALID_EMAIL_REGEX);
	}

	public static boolean isValidPassword(String password) {
		//    return password.matches(VALID_PASSWORD_REGEX);
		return true;
	}

	public static boolean isValidUSZipCode(String zipCode) {
		return zipCode.matches(VALID_US_ZIP_CODE);
	}

	public static boolean isValidState(String state) {
		return state.matches(VALID_STATE);
	}
}
