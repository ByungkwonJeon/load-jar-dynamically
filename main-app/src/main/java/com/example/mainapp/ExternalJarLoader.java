
package com.example.mainapp;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.Constructor;
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

                // ✅ Register repositories
                Set<Class<?>> repositories = componentsReflections.getTypesAnnotatedWith(org.springframework.stereotype.Repository.class);
                repositories.forEach(repoClass -> {
                    context.registerBean(repoClass);
                    System.out.println("Registered repository: " + repoClass.getName());
                });

                // ✅ Register services
                Set<Class<?>> services = componentsReflections.getTypesAnnotatedWith(org.springframework.stereotype.Service.class);
                for (Class<?> serviceClass : services) {
                    Constructor<?>[] constructors = serviceClass.getConstructors();
                    for (Constructor<?> constructor : constructors) {
                        Object[] dependencies = new Object[constructor.getParameterCount()];
                        for (int i = 0; i < constructor.getParameterCount(); i++) {
                            Class<?> dependencyType = constructor.getParameterTypes()[i];
                            // ✅ Check for @Qualifier annotation
                            Qualifier qualifier = constructor.getParameters()[i].getAnnotation(Qualifier.class);
                            if (qualifier != null) {
                                // ✅ If @Qualifier is present, get the bean by name
                                String beanName = qualifier.value();
                                dependencies[i] = context.getBean(beanName);
                                System.out.println("Injecting @Qualifier '" + beanName + "' into " + serviceClass.getName());
                            } else {
                                // ✅ If no @Qualifier, get by type
                                dependencies[i] = context.getBean(dependencyType);
                            }
                        }

                        Object serviceInstance = constructor.newInstance(dependencies);
                        context.registerBean(serviceClass.getSimpleName(), (Class<Object>) serviceClass, () -> serviceInstance);
                        System.out.println("Registered service: " + serviceClass.getName());
                    }
                }

                // Find all annotated components
                Set<Class<?>> components = componentsReflections.getTypesAnnotatedWith(org.springframework.stereotype.Component.class);
                for (Class<?> componentClass : components) {
                    Constructor<?>[] constructors = componentClass.getConstructors();
                    for (Constructor<?> constructor : constructors) {
                        Object[] dependencies = new Object[constructor.getParameterCount()];
                        for (int i = 0; i < constructor.getParameterCount(); i++) {
                            Class<?> dependencyType = constructor.getParameterTypes()[i];
                            dependencies[i] = context.getBean(dependencyType);
                        }

                        Object serviceInstance = constructor.newInstance(dependencies);
                        context.registerBean(componentClass.getSimpleName(), (Class<Object>) componentClass, () -> serviceInstance);
                        System.out.println("Registered service: " + componentClass.getName());
                    }
                }

                Set<Class<?>> mappers = componentsReflections.getTypesAnnotatedWith(org.mapstruct.Mapper.class);
                for (Class<?> mapperClass : mappers) {
                    Object mapperInstance = Mappers.getMapper(mapperClass);
                    context.registerBean(mapperClass.getSimpleName(), (Class<Object>) mapperClass, () -> mapperInstance);
                    System.out.println("Registered mapper: " + mapperClass.getName());
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
