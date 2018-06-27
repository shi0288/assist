package com.xyauto.assist.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.mcp.fastcloud.util.exception.base.ErrorException;

import java.util.HashMap;
import java.util.Map;

import static com.xyauto.assist.util.constant.ErrorCons.EXECUTION_CONDITION_ERROR;


/**
 * Created by shiqm on 2018-01-18.
 */


public class JsonSchemaValidator {

    private final static JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
    private final static Map<String, JsonNode> mainCache = new HashMap();

    public static boolean validatorSchema(String schemaPath, String instance) {
        try {
            JsonNode mainNode;
            if (mainCache.containsKey(schemaPath)) {
                mainNode = mainCache.get(schemaPath);
            } else {
                mainNode = JsonLoader.fromResource(schemaPath);
                mainCache.put(schemaPath, mainNode);
            }
            JsonNode instanceNode = JsonLoader.fromString(instance);
            JsonSchema schema = factory.getJsonSchema(mainNode);
            ProcessingReport processingReport = schema.validateUnchecked(instanceNode);
            boolean ret = processingReport.isSuccess();
            System.out.println(processingReport);
            return ret;
        } catch (Exception e) {
            throw new ErrorException(EXECUTION_CONDITION_ERROR);
        }
    }
}
