package app.common;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ValueNode;

/**
 * @author: landy
 * @date: 2017-12-24 23:36
 */
public class CustomJsonNodeFactory extends JsonNodeFactory {
    @Override
    public NumericNode numberNode(long v) {
        return CustomLongNode.valueOf(v);
    }

    @Override
    public ValueNode numberNode(Long value) {
        if (value == null) {
            return nullNode();
        }
        return CustomLongNode.valueOf(value.longValue());
    }
}
