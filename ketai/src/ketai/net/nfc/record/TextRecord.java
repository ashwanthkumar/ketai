/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ketai.net.nfc.record;

import android.nfc.NdefRecord;

import java.io.UnsupportedEncodingException;

import processing.core.PApplet;

/**
 * An NFC Text Record
 */
public class TextRecord implements ParsedNdefRecord {

	/** ISO/IANA language code */
	private final String mLanguageCode;

	private final String mText;

	private TextRecord(String languageCode, String text) {
		mLanguageCode = languageCode;
		mText = text;
	}

	public String getText() {
		return mText;
	}

	/**
	 * Returns the ISO/IANA language code associated with this text element.
	 */
	public String getLanguageCode() {
		return mLanguageCode;
	}

	// TODO: deal with text fields which span multiple NdefRecords
	public static TextRecord parse(NdefRecord record) {
		try {
			byte[] payload = record.getPayload();

			PApplet.println("TextRecord parsed and NdefRecord with a payload of "
					+ payload.length + " bytes.");
			
//			if (payload.length < 2)
//			if(true)
//				throw new IllegalArgumentException(
//						"Not enough Payload to parse TextRecord");
			/*
			 * payload[0] contains the "Status Byte Encodings" field, per the
			 * NFC Forum "Text Record Type Definition" section 3.2.1.
			 * 
			 * bit7 is the Text Encoding Field.
			 * 
			 * if (Bit_7 == 0): The text is encoded in UTF-8 if (Bit_7 == 1):
			 * The text is encoded in UTF16
			 * 
			 * Bit_6 is reserved for future use and must be set to zero.
			 * 
			 * Bits 5 to 0 are the length of the IANA language code.
			 */
			String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8"
					: "UTF-16";
			int languageCodeLength = payload[0] & 0077;
			String languageCode = new String(payload, 1, languageCodeLength,
					"US-ASCII");
			String text = new String(payload, languageCodeLength + 1,
					payload.length - languageCodeLength - 1, textEncoding);

			PApplet.println("TextRecord parsing: " + payload);
			PApplet.println("\t parsed text:" + text);
			return new TextRecord(languageCode, text);
		} catch (UnsupportedEncodingException e) {
			// should never happen unless we get a malformed tag.
			throw new IllegalArgumentException(e);
		}
		catch(Exception x){
			throw new IllegalArgumentException("Error parsing as a TextRecord: "+x.getMessage());
		}
	}

	public static boolean isText(NdefRecord record) {
		try {
			parse(record);
			PApplet.println("TextRecord.isText is true!");
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public String getTag() {
		return getText();
	}
}
