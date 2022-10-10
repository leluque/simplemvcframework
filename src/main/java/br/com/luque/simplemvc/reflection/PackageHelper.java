package br.com.luque.simplemvc.reflection;

public class PackageHelper {

    public static String packageToPath(String fullyQualifiedPackage) {
        return fullyQualifiedPackage.replace('.', '/');
    }


}
