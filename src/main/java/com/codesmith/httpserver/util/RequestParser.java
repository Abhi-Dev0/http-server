package com.codesmith.httpserver.util;

import com.codesmith.httpserver.exception.HttpRequestParsingException;
import com.codesmith.httpserver.model.HttpMethod;
import com.codesmith.httpserver.model.HttpRequest;
import com.codesmith.httpserver.model.HttpVersion;

import javax.xml.stream.events.Characters;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestParser {

    private static RequestParser requestParser;

    private static final int SP = 0x1A; //32 Octet
    private static final int CR = 0x0B; //13 Octet
    private static final int LF = 0x08; //10 Octet

    private static final Pattern httpVersionPattern = Pattern.compile("HTTP/(?<major>\\d+)\\.(?<minor>\\d+)");

    private RequestParser(){}

    public static RequestParser getInstance(){
        if(requestParser == null){
            requestParser = new RequestParser();
        }
        return requestParser;
    }

    public HttpRequest parseHttpRequest(InputStream in){
        HttpRequest request = new HttpRequest();
        InputStreamReader reader = new InputStreamReader(in, StandardCharsets.US_ASCII);
        BufferedReader bufferedReader = new BufferedReader(reader);

        // Parse Request Line
        parseHttpRequestLine(bufferedReader, request);

        // Parse Headers
        parseHeaders(bufferedReader, request);

        //Parse Body
        parseBody(bufferedReader, request);

        return request;
    }

    private void parseHttpRequestLine(BufferedReader reader, HttpRequest request){
        try{
            String line = reader.readLine();
            if(line == null || line.isBlank())
                return;

            String[] httpParts = line.split(" ");
            if(httpParts.length != 3){
                throw new HttpRequestParsingException("Malformed request line");
            }

            // Parse HTTP Method
            request.setMethod(HttpMethod.getHttpMethod(httpParts[0]));

            // Parse Target URI
            request.setTargetUri(httpParts[1]);

            // Parse HTTP Version
            Matcher matcher = httpVersionPattern.matcher(httpParts[2]);
            if(matcher.matches()){
                int major = Integer.parseInt(matcher.group("major"));
                int minor = Integer.parseInt(matcher.group("minor"));
                request.setHttpVersion(HttpVersion.getCompatibleHttpVersion(httpParts[2], major, minor));
            }else{
                throw new HttpRequestParsingException("Invalid HTTP Version Format "+httpParts[2]);
            }
        } catch (IOException e) {
            throw new HttpRequestParsingException("Unable to read request line", e);
        }
    }

    private void parseHeaders(BufferedReader reader, HttpRequest request){
        try{
            StringBuilder line;
            while(!(line = new StringBuilder(reader.readLine())).isEmpty()){
                int colonIndex = line.indexOf(":");
                if(colonIndex == -1)
                    throw new HttpRequestParsingException("Malformed HTTP Header Line "+line);

                String header = line.substring(0, colonIndex).trim();
                String value = line.substring(colonIndex + 1).trim();

                request.addHeader(header, value);
            }
        }catch(IOException e){
            throw new HttpRequestParsingException("Unable to read headers", e);
        }
    }

    private void parseBody(BufferedReader reader, HttpRequest request){
        String lengthStr = request.getHeaders().get("Content-Length");
        try{
            // Empty body check
            int length;
            if(lengthStr == null || (length = Integer.parseInt(lengthStr)) <= 0)
                return;

            char[] bodyChars = new char[length];
            int read = reader.read(bodyChars, 0, length);
            if(read != length){
                throw new HttpRequestParsingException("Unable to read full body");
            }

            //Set Body to request Object
            request.setBody(new String(bodyChars));
        }catch (NumberFormatException e){
            throw new HttpRequestParsingException("Invalid Content-Length header value: " + lengthStr, e);
        }
        catch (IOException e) {
            throw new HttpRequestParsingException("Unable to parse body", e);
        }
    }
}
