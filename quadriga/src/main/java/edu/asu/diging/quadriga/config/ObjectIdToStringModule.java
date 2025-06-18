package edu.asu.diging.quadriga.config;


import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;

public class ObjectIdToStringModule extends SimpleModule {

    public ObjectIdToStringModule() {
        addSerializer(ObjectId.class, new ToStringSerializer());
    }
}
