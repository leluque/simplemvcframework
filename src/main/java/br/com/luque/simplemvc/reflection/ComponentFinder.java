package br.com.luque.simplemvc.reflection;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

public class ComponentFinder {

    private static final Logger logger = Logger.getLogger(ComponentFinder.class.getName());

    private final Class<? extends Annotation>[] annotations;

    public ComponentFinder(Class<? extends Annotation>... annotations) {
        this.annotations = annotations;
    }

    public Collection<FoundComponent> find(String basePackage) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        logger.info("Trying to find the package %s in the class loader...".formatted(basePackage));
        logger.info("Found %d occurrences.".formatted(Collections.list(classLoader.getResources(PackageHelper.packageToPath(basePackage))).size()));
        Enumeration<URL> urls = classLoader.getResources(PackageHelper.packageToPath(basePackage));
        List<FoundComponent> foundComponents = new LinkedList<>();
        while (urls.hasMoreElements()) {
            URL packagePath = urls.nextElement();
            logger.info("Handling path %s.".formatted(packagePath.getFile()));
            File packageFolder = new File(packagePath.getFile());
            foundComponents.addAll(findRecursively(basePackage, packageFolder));
        }
        return foundComponents;
    }

    private Collection<FoundComponent> findRecursively(String basePackage, File packageFolder) {
        if (!packageFolder.isDirectory()) {
            logger.info("%s is not a directory.".formatted(packageFolder));
            return Collections.emptyList();
        }

        List<FoundComponent> foundComponents = new LinkedList<>();
        logger.info("Looking for children of %s...".formatted(packageFolder));
        File[] children = packageFolder.listFiles();
        logger.info("Found %d child(ren).".formatted(children.length));
        for (File child : children) {
            if (child.isDirectory()) {
                logger.info("The child %s is a directory.".formatted(child.getName()));
                foundComponents.addAll(findRecursively(basePackage + "." + child.getName(), child));
            } else if (child.getName().endsWith(".class")) {
                logger.info("The child %s is a class file.".formatted(child.getName()));
                try {
                    Class clazz = Class.forName(basePackage + "." + FilenameUtils.removeExtension(child.getName()));
                    Collection<Annotation> foundAnnotations = findAnnotations(clazz);
                    if (!foundAnnotations.isEmpty()) {
                        logger.info("A component has been found: %s.".formatted(clazz.getName()));
                        foundComponents.add(new FoundComponent(clazz, foundAnnotations));
                    }
                } catch (ClassNotFoundException e) {

                }
            }
        }
        return foundComponents;
    }

    private Collection<Annotation> findAnnotations(Class clazz) {
        logger.info("Looking for annotations of %s...".formatted(clazz.getName()));
        List<Annotation> foundAnnotations = new LinkedList<>();
        Arrays.stream(annotations).forEach(annotationClass -> {
            Annotation[] annotation = clazz.getDeclaredAnnotationsByType(annotationClass);
            logger.info("Looking for annotation %s...".formatted(annotationClass.getName()));
            if (annotation.length > 0) {
                logger.info("Found %d occurrence(s).".formatted(annotation.length));
                foundAnnotations.addAll(Arrays.asList(annotation));
            }
        });
        return foundAnnotations;
    }


}
