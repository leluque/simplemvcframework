package br.com.luque.simplemvc.reflection;

import br.com.luque.simplemvc.reflection.component.TestComponentA;
import br.com.luque.simplemvc.reflection.component.annotation.TestAnnotation1;
import br.com.luque.simplemvc.reflection.component.annotation.TestAnnotation2;
import br.com.luque.simplemvc.reflection.component.subpackage.TestComponentC;
import br.com.luque.simplemvc.reflection.component.subpackage.subpackage.TestComponentD;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class ComponentFinderTests {

    @Test
    void shouldFindNoComponentsWhenNoAnnotationIsProvided() throws IOException {
        ComponentFinder componentFinder = new ComponentFinder();
        Collection<FoundComponent> foundComponents = componentFinder.find("br.com.luque.simplemvc.reflection.component");
        assertNotNull(foundComponents);
        assertTrue(foundComponents.isEmpty());
    }

    @Test
    void shouldFind3ComponentsWhenUsedInComponentPackage() throws IOException {
        ComponentFinder componentFinder = new ComponentFinder(TestAnnotation1.class, TestAnnotation2.class);
        Collection<FoundComponent> foundComponents = componentFinder.find("br.com.luque.simplemvc.reflection.component");
        assertNotNull(foundComponents);
        assertEquals(3, foundComponents.size());
    }

    @Test
    void classTestComponentAShouldHaveTestAnnotation1() throws IOException {
        ComponentFinder componentFinder = new ComponentFinder(TestAnnotation1.class, TestAnnotation2.class);
        Collection<FoundComponent> foundComponents = componentFinder.find("br.com.luque.simplemvc.reflection.component");
        Annotation[] annotations = foundComponents
                .stream()
                .filter(it -> it.getClazz().equals(TestComponentA.class))
                .findFirst()
                .map(FoundComponent::getAnnotations)
                .orElse(new Annotation[]{});
        assertEquals(1, annotations.length);
        assertEquals(TestAnnotation1.class, annotations[0].annotationType());
    }

    @Test
    void classTestComponentCShouldHaveTestAnnotation2() throws IOException {
        ComponentFinder componentFinder = new ComponentFinder(TestAnnotation1.class, TestAnnotation2.class);
        Collection<FoundComponent> foundComponents = componentFinder.find("br.com.luque.simplemvc.reflection.component");
        Annotation[] annotations = foundComponents
                .stream()
                .filter(it -> it.getClazz().equals(TestComponentC.class))
                .findFirst()
                .map(FoundComponent::getAnnotations)
                .orElse(new Annotation[]{});
        assertEquals(1, annotations.length);
        assertEquals(TestAnnotation2.class, annotations[0].annotationType());
    }

    @Test
    void classTestComponentDShouldHaveTestAnnotation1And2() throws IOException {
        ComponentFinder componentFinder = new ComponentFinder(TestAnnotation1.class, TestAnnotation2.class);
        Collection<FoundComponent> foundComponents = componentFinder.find("br.com.luque.simplemvc.reflection.component");
        Annotation[] annotations = foundComponents
                .stream()
                .filter(it -> it.getClazz().equals(TestComponentD.class))
                .findFirst()
                .map(FoundComponent::getAnnotations)
                .orElse(new Annotation[]{});
        assertEquals(2, annotations.length);
        assertEquals(TestAnnotation1.class, annotations[0].annotationType());
        assertEquals(TestAnnotation2.class, annotations[1].annotationType());
    }

    @Test
    void shouldNotFindUnspecifiedAnnotation() throws IOException {
        ComponentFinder componentFinder = new ComponentFinder(TestAnnotation1.class);
        Collection<FoundComponent> foundComponents = componentFinder.find("br.com.luque.simplemvc.reflection.component");
        Annotation[] annotations = foundComponents
                .stream()
                .filter(it -> it.getClazz().equals(TestComponentD.class))
                .findFirst()
                .map(FoundComponent::getAnnotations)
                .orElse(new Annotation[]{});
        assertEquals(1, annotations.length);
        assertEquals(TestAnnotation1.class, annotations[0].annotationType());
    }

}
