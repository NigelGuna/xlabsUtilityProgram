import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordnik.swagger.util.Json;
import com.wordnik.swagger.util.Yaml;
import com.wordnik.swagger.models.Swagger;
import com.wordnik.swagger.models.Path;
import com.wordnik.swagger.models.Operation;
import com.wordnik.swagger.models.parameters.HeaderParameter;
import com.wordnik.swagger.models.parameters.Parameter;
import io.swagger.parser.SwaggerParser;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by dlakshman on 11/19/2018.
 */
public class authenticationFix {

    static ObjectMapper jsonMapper = Json.mapper();
    static ObjectMapper yamlMapper = Yaml.mapper();

    public Swagger readSwagger(JsonNode node) throws IllegalArgumentException {
        SwaggerParser parser = new SwaggerParser();
        return parser.read(node);
    }

    public JsonNode readNode(String text) {
        try {
            if (text.trim().startsWith("{")) {
                return jsonMapper.readTree(text);
            } else {
                return yamlMapper.readTree(text);
            }
        } catch (IOException e) {
            return null;
        }
    }

    private int injectHeader(Operation operation, Parameter parameter, int count){

        if(operation!=null){
            count++;
            if(operation.getParameters() ==null){
                operation.setParameters(new ArrayList<>());
            }
            AtomicBoolean headerExists = new AtomicBoolean(false);
            operation.getParameters().parallelStream().forEach(parameterE -> {
                if(parameterE.getName().equalsIgnoreCase("Authorization")){
                    headerExists.set(true);
                    return;
                }


            });

            if(!headerExists.get())
                operation.addParameter(parameter);
        }

        return count;
    }

    public String readFile(String pathname){

        StringBuffer urlParameters = new StringBuffer();
        File file = new File(pathname);

        if(!file.exists()){
            return "";
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null)
            {
                urlParameters.append(line);
            }

            //Return string from read file
            return urlParameters.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void writeFile(String content, String fileName){

        String fileNameC = "fixed-swagger/";

        try {
            FileWriter writer = new FileWriter(fileNameC + fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addParams(String inputDoc, String fileName){
        authenticationFix swaggerMain = new authenticationFix();

        JsonNode spec = swaggerMain.readNode(inputDoc);
        Swagger swagger = swaggerMain.readSwagger(spec);

        if(swagger ==null ){

        }
        else {

            int count=0;
            Parameter parameter = new HeaderParameter();

            parameter.setDescription("bearer <token>");
            parameter.setIn("header");
            parameter.setName("Authorization");
            parameter.setRequired(true);
            ((HeaderParameter) parameter).setType("string");

            for (Map.Entry<String, Path> entry : swagger.getPaths().entrySet()) {

                if(entry.getValue() !=null){

                    count = swaggerMain.injectHeader(entry.getValue().getGet(), parameter, count);
                    count = swaggerMain.injectHeader(entry.getValue().getPatch(), parameter, count);
                    count = swaggerMain.injectHeader(entry.getValue().getPost(), parameter, count);
                    count = swaggerMain.injectHeader(entry.getValue().getPut(), parameter, count);
                    count = swaggerMain.injectHeader(entry.getValue().getDelete(), parameter, count);
                    count = swaggerMain.injectHeader(entry.getValue().getOptions(), parameter, count);

                }

            }

            String prettySwagger = Json.pretty(swagger);
            swaggerMain.writeFile(prettySwagger,fileName);
        }
    }

    private void removeDefault(String inputDoc, String fileName){
        authenticationFix swaggerMain = new authenticationFix();

        JsonNode spec = swaggerMain.readNode(inputDoc);
        Swagger swagger = swaggerMain.readSwagger(spec);

        if(swagger ==null ){

        }
        else {

            int count=0;

            String[] swaggerPaths = swagger.getPaths().toString().substring(1).replace(" ","").split(",");
            List<String> swaggerPatharray = new ArrayList<>();

            for (String x: swaggerPaths) {
                swaggerPatharray.add(x.split("=")[0]);
            }
            for (String x: swaggerPatharray) {

                System.out.println(x);

                if(swagger.getPath(x).getPost()!=null){
                    System.out.println("Post found");
                    swagger.getPath(x).getPost().getResponses().remove("200");
                }else{
                    System.out.println("Post not found");
                }

                if(swagger.getPath(x).getDelete()!=null){
                    System.out.println("Delete found");
                    swagger.getPath(x).getDelete().getResponses().remove("200");
                }else{
                    System.out.println("Delete not found");
                }

                if(swagger.getPath(x).getGet()!=null){
                    System.out.println("Get found");
                    swagger.getPath(x).getGet().getResponses().remove("200");
                }else{
                    System.out.println("Get not found");
                }

                if(swagger.getPath(x).getPatch()!=null){
                    System.out.println("Patch found");
                    swagger.getPath(x).getPatch().getResponses().remove("200");
                }else{
                    System.out.println("Patch not found");
                }

                if(swagger.getPath(x).getPut()!=null){
                    System.out.println("Put found");
                    swagger.getPath(x).getPut().getResponses().remove("200");
                }else{
                    System.out.println("Put not found");
                }

                System.out.println("=======================================");


            }


            String prettySwagger = Json.pretty(swagger);
            swaggerMain.writeFile(prettySwagger,fileName);
        }
    }

    public static void main(String[] args) {


        File dir = new File("swaggers/");
        authenticationFix swaggerMain = new authenticationFix();



        for (File file : dir.listFiles()) {

            System.out.println("=================================================");
            System.out.println("Started Conversion for " +file.getName() );
            System.out.println();

            String document = swaggerMain.readFile(file.toString());
            swaggerMain.removeDefault(document, file.getName());


            System.out.println("Swagger "+file.getName()+" converted Successfully");

        }

        System.out.println("=================================================");
        System.out.println();
        System.out.println("Action Completed");

    }

}
