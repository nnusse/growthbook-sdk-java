package growthbook.sdk.java.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import growthbook.sdk.java.services.GrowthBookJsonUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Defines a single Experiment
 */
@Data
@Builder
@AllArgsConstructor
public class Experiment<ValueType> {
    /**
     * The globally unique identifier for the experiment
     */
    String key;


    /**
     * The different variations to choose between
     */
    @Builder.Default
    ArrayList<ValueType> variations = new ArrayList<>();

    /**
     * How to weight traffic between variations. Must add to 1.
     */
    ArrayList<Float> weights;

    /**
     * If set to false, always return the control (first variation)
     */
    @Builder.Default
    Boolean isActive = true;

    /**
     * What percent of users should be included in the experiment (between 0 and 1, inclusive)
     */
    Float coverage;

    // TODO: Condition
    /**
     * Optional targeting condition
     */
    String conditionJson;

    @Nullable
    Namespace namespace;

    /**
     * All users included in the experiment will be forced into the specific variation index
     */
    Integer force;

    /**
     * What user attribute should be used to assign variations (defaults to `id`)
     */
    @Builder.Default
    String hashAttribute = "id";

    public String toJson() {
        return Experiment.getJson(this).toString();
    }

    @Override
    public String toString() {
        return this.toJson();
    }


    // region Serialization

    public static <ValueType> JsonElement getJson(Experiment<ValueType> object) {
        JsonObject json = new JsonObject();

        json.addProperty("key", object.getKey());

        JsonElement variationsElement = GrowthBookJsonUtils.getJsonElementForObject(object.getVariations());
        json.add("variations", variationsElement);

        JsonElement weightsElement = GrowthBookJsonUtils.getJsonElementForObject(object.getWeights());
        json.add("weights", weightsElement);

        json.addProperty("isActive", object.getIsActive());
        json.addProperty("coverage", object.getCoverage());

        JsonElement namespaceElement = GrowthBookJsonUtils.getJsonElementForObject(object.getNamespace());
        json.add("namespace", namespaceElement);

        json.addProperty("force", object.getForce());
        json.addProperty("hashAttribute", object.getHashAttribute());

        return json;
    }

    public static <ValueType> JsonSerializer<Experiment<ValueType>> getSerializer() {
        return new JsonSerializer<Experiment<ValueType>>() {
            @Override
            public JsonElement serialize(Experiment<ValueType> src, Type typeOfSrc, JsonSerializationContext context) {
                return Experiment.getJson(src);
            }
        };
    }

    // endregion Serialization
}
