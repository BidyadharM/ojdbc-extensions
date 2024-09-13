package oracle.jdbc.provider.oson;

import com.fasterxml.jackson.core.*;
import oracle.sql.json.OracleJsonFactory;
import oracle.sql.json.OracleJsonGenerator;
import oracle.sql.json.OracleJsonParser;
import oracle.sql.json.OracleJsonParser.Event;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class can be used to convert json bytes to oson bytes and vice versa
 * 
 * @author sreekar
 */
public class OsonConverter {

	OracleJsonFactory oFac = null;
	JsonFactory jsonFac = null;
	
	public OsonConverter() {
		oFac = new OracleJsonFactory();
		jsonFac = new JsonFactory();
	}

	/**
	 * @param input Byte Array Input stream that contains json bytes to be converted to oson
	 * @return a byte array containing oson representation of the json document
	 * @throws JsonParseException
	 * @throws IOException
	 */	
	public byte[] toOson(InputStream input) throws JsonParseException, IOException {
		
		JsonParser jsonParser = jsonFac.createParser(input);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OracleJsonGenerator oJen = oFac.createJsonBinaryGenerator(out);
		
		convertJsonToOson(jsonParser, oJen);
		oJen.close();
		
		return out.toByteArray();
	}
	
	/**
	 * @param input Byte array input stream containing oson bytes to be converted to json
	 * @return byte array containing json representation of the oson object
	 * @throws IOException
	 */
	public byte[] fromOson(InputStream input) throws IOException {
		OracleJsonParser osonParser = oFac.createJsonBinaryParser(input);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		JsonGenerator jGen = jsonFac.createGenerator(out);
		
		convertOsontoJson(osonParser, jGen);
		jGen.flush();
		jGen.close();
		return out.toByteArray();
	}
	
	/**
	 * @param jsonParser parser of the json document
	 * @param oJen oson events are written to this generator for the corresponding json
	 * @throws IOException
	 */
	private void convertJsonToOson(JsonParser jsonParser, OracleJsonGenerator oJen) throws IOException {
		
		JsonToken token;
		while(!jsonParser.isClosed()) {
			token = jsonParser.nextToken();
			if(token == null) {
				jsonParser.close();
				break;
			}
			switch(token) {
			case START_OBJECT:
				oJen.writeStartObject();
				break;
			case END_OBJECT:
				oJen.writeEnd();
				break;
			case FIELD_NAME:
				oJen.writeKey(jsonParser.getCurrentName());
				//System.out.println(jsonParser.currentName());
				break;
			case VALUE_NUMBER_INT:
				oJen.write(jsonParser.getIntValue());
				break;
			case VALUE_NUMBER_FLOAT:
				oJen.write(jsonParser.getFloatValue());
				break;
			case VALUE_STRING:
				oJen.write(jsonParser.getValueAsString());
				break;
			case VALUE_NULL:
				oJen.writeNull();
				break;
			case VALUE_TRUE:
				oJen.write(Boolean.TRUE);
				break;
			case VALUE_FALSE:
				oJen.write(Boolean.FALSE);
				break;
			case START_ARRAY:
				oJen.writeStartArray();
				this.convertJsonToOson(jsonParser, oJen);
				jsonParser.skipChildren();
				break;
			case END_ARRAY:
				oJen.writeEnd();
				return;
			case NOT_AVAILABLE:
				break;
			case VALUE_EMBEDDED_OBJECT:
				oJen.write(jsonParser.getBinaryValue());
			default:
				oJen.write(jsonParser.getBinaryValue());
			}
		}
	}
	
	/**
	 * @param osonParser parser of the oson document
	 * @param jGen json tokens are written to this generator for the corresponding json
	 * @throws IOException
	 */
	private void convertOsontoJson(OracleJsonParser osonParser, JsonGenerator jGen) throws IOException {
		Event event;
		while(osonParser.hasNext()) {
			event = osonParser.next();
			switch(event) {
			case START_OBJECT:
				jGen.writeStartObject();
				break;
			case END_OBJECT:
				jGen.writeEndObject();
				break;
			case KEY_NAME:
				jGen.writeFieldName(osonParser.getString());
				//System.out.println(osonParser.getString());
				break;
			case VALUE_BINARY:
				jGen.writeBinary(osonParser.getBytes());
				break;
			case VALUE_DATE:
				jGen.writeString(osonParser.getLocalDateTime().toString());
				break;
			case VALUE_DECIMAL:
				jGen.writeNumber(osonParser.getBigDecimal());
				break;
			case VALUE_DOUBLE:
				jGen.writeNumber(osonParser.getDouble());
				break;
			case VALUE_FLOAT:
				jGen.writeNumber(osonParser.getFloat());
				break;
			case VALUE_FALSE:
				jGen.writeBoolean(Boolean.FALSE);
				break;
			case VALUE_TRUE:
				jGen.writeBoolean(Boolean.TRUE);
				break;
			case VALUE_NULL:
				jGen.writeNull();
				break;
			case VALUE_STRING:
				jGen.writeString(osonParser.getString());
				break;
			case VALUE_TIMESTAMP:
				jGen.writeString(osonParser.getLocalDateTime().toString());
				break;
			case VALUE_TIMESTAMPTZ:
				jGen.writeString(osonParser.getOffsetDateTime().toString());
				break;
			case VALUE_INTERVALYM:
				jGen.writeString(osonParser.getPeriod().toString());
				break;
			case VALUE_INTERVALDS:
				jGen.writeString(osonParser.getDuration().toString());
				break;
			case START_ARRAY:
				jGen.writeStartArray();
				this.convertOsontoJson(osonParser, jGen);
				osonParser.skipArray();
				break;
			case END_ARRAY:
				jGen.writeEndArray();
				return;
			}
		}
		
	}
}