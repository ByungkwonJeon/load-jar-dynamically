
package com.example.mainapp;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

@Component
//@Import(DynamicComponentLoader.class)
public class ExternalJarLoader {

    private final GenericApplicationContext context;

    public ExternalJarLoader(GenericApplicationContext context) {
        this.context = context;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadExternalJar() {
        try {
            File jarFile = new File("main-app/external-lib/external-lib-0.0.1-SNAPSHOT.jar");
            if (jarFile.exists()) {
//                URL jarUrl = jarFile.toURI().toURL();
//                URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{jarUrl}, getClass().getClassLoader());

                // Register the external service dynamically
//                Class<?> externalServiceClass = Class.forName("com.example.external.ExternalService", true, urlClassLoader);
//                Object externalServiceInstance = externalServiceClass.getDeclaredConstructor().newInstance();
                // ✅ Register bean with explicit cast
//                context.registerBean(
//                        "externalService",
//                        (Class<Object>) externalServiceClass,
//                        () -> externalServiceInstance
//                );

                // ✅ Load the JAR dynamically
                URL jarUrl = jarFile.toURI().toURL();
                URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{jarUrl}, getClass().getClassLoader());

                // ✅ Scan for @Configuration files using Reflections
                Reflections configReflections = new Reflections(new ConfigurationBuilder()
                        .setUrls(jarUrl)
                        .setScanners(Scanners.TypesAnnotated)
                        .addClassLoaders(urlClassLoader));

                // ✅ Find all @Configuration classes
                Set<Class<?>> configurations = configReflections.getTypesAnnotatedWith(org.springframework.context.annotation.Configuration.class);

                for (Class<?> configClass : configurations) {
                    System.out.println("Registering external config: " + configClass.getName());

                    // ✅ Find and print all @Bean names
                    for (Method method : configClass.getDeclaredMethods()) {
                        if (method.isAnnotationPresent(Bean.class)) {
                            Bean beanAnnotation = method.getAnnotation(Bean.class);

                            // ✅ Get the bean name (if specified) or use the method name
                            String beanName = beanAnnotation.name().length > 0 ? beanAnnotation.name()[0] : method.getName();
                            System.out.println("➕ Found Bean: " + beanName);

                            // ✅ Register the bean dynamically
                            context.registerBean(beanName, (Class<Object>) method.getReturnType(), () -> {
                                try {
                                    return method.invoke(configClass.getDeclaredConstructor().newInstance());
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        }
                    }
                }

                System.out.println("External configurations loaded and registered successfully!");

                // ✅ Provide the URL directly to Reflections
                Reflections componentsReflections = new Reflections(new ConfigurationBuilder()
                        .setUrls(jarUrl)
                        .setScanners(Scanners.TypesAnnotated)
                        .addClassLoaders(urlClassLoader));

                // Find all annotated components
                Set<Class<?>> components = componentsReflections.getTypesAnnotatedWith(org.springframework.stereotype.Component.class);
                components.addAll(componentsReflections.getTypesAnnotatedWith(org.springframework.stereotype.Service.class));
                components.addAll(componentsReflections.getTypesAnnotatedWith(org.springframework.stereotype.Repository.class));
                components.addAll(componentsReflections.getTypesAnnotatedWith(org.springframework.web.bind.annotation.RestController.class));

                // ✅ Register each component dynamically
                for (Class<?> clazz : components) {
                    Object instance = clazz.getDeclaredConstructor().newInstance();
                    context.registerBean(clazz.getSimpleName(), (Class<Object>) clazz, () -> instance);
                    System.out.println("Registered external component: " + clazz.getSimpleName());
                }

                System.out.println("External JAR loaded and registered: " + jarFile.getAbsolutePath());
            } else {
                System.out.println("External JAR not found: " + jarFile.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
