package br.com.luque.simplemvc.reflection;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;

public class FoundComponent {

    private final Annotation[] annotations;
    private final Class clazz;

    public FoundComponent(Class clazz, Annotation... annotations) {
        this.annotations = annotations;
        this.clazz = clazz;
    }

    public FoundComponent(Class clazz, Collection<Annotation> annotations) {
        this.annotations = annotations.toArray(new Annotation[annotations.size()]);
        this.clazz = clazz;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public Class getClazz() {
        return clazz;
    }

    @Override
    public String toString() {
        return "FoundComponent{" +
                "annotations=" + Arrays.toString(annotations) +
                ", clazz=" + clazz +
                '}';
    }
}
