package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.yaml.snakeyaml.Yaml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class Utils {

    public static Function<String, String> readFileAsString = path -> {
        StringBuilder sb = new StringBuilder();
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {

                String sCurrentLine = "";
                while ((sCurrentLine = br.readLine()) != null) {
                    sb.append(sCurrentLine);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    };


    public static Function<InputStream, String> readInputStreamAsString = is -> {
        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(is, writer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();
    };


    public static Function<String, Document> readFile = path -> convertStringToDocument(readFileAsString.apply(path));
    public static Function<InputStream, Document> readInputStream = path -> convertStringToDocument(readInputStreamAsString.apply(path));


    public static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Function<String, JsonNode> loadJsonNodeFromString = x -> {
        try {
            return JsonLoader.fromString(x);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    };


    public static Function<String, String> convertYamlToJson = yamlString -> {

        Yaml yaml = new Yaml();
        Map<String, Object> map = (Map<String, Object>) yaml.load(yamlString);
        JSONObject jsonObject = new JSONObject(map);
        return jsonObject.toString();
    };

    public static Predicate<String> isJSONValid = x -> {
        try {
            new Gson().fromJson(x, Object.class);
            return true;
        } catch (JsonSyntaxException ex) {
            return false;
        }
    };

    public static Function<Object, String> getBeautifiedJson = (x) -> new GsonBuilder().setPrettyPrinting().create().toJson(x);


    public static Function<String, String> convertJsonToYaml = jsonString -> {
        // parse JSON
        JsonNode jsonNodeTree = null;
        try {
            jsonNodeTree = new ObjectMapper().readTree(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // save it as YAML
        String jsonAsYaml = null;
        try {
            jsonAsYaml = new YAMLMapper().writeValueAsString(jsonNodeTree);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonAsYaml;
    };

    public static Function<String, String> getResourceFileAsString = path -> readInputStreamAsString.apply(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));

    public static BiPredicate<String, String> validateJsonToJsonSchema = (data, schema) -> {

        try {
            final JsonNode fstabSchema = Utils.loadJsonNodeFromString.apply(schema);
            final JsonNode good = Utils.loadJsonNodeFromString.apply(data);

            final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
            final JsonSchema jsonSchema = factory.getJsonSchema(fstabSchema);

            return jsonSchema.validate(good).isSuccess();
        } catch (ProcessingException pe) {

        }

        return false;
    };


    public static void writeStringToFile(String content, String fileName) throws IOException {
        try (PrintWriter out = new PrintWriter(fileName)) {
            out.println(content);
        }
    }
}
