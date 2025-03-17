//package com.example.mainapp;
//
//import org.springframework.beans.factory.config.BeanDefinition;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
//import org.springframework.core.type.filter.AnnotationTypeFilter;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Set;
//
//@Configuration
//public class DynamicComponentLoader {
//
//    @Bean
//    public void registerExternalComponents() {
//        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
//        scanner.addIncludeFilter(new AnnotationTypeFilter(Component.class));
//        scanner.addIncludeFilter(new AnnotationTypeFilter(Service.class));
//        scanner.addIncludeFilter(new AnnotationTypeFilter(RestController.class));
//
//        // Scan the package of the external JAR
//        Set<BeanDefinition> components = scanner.findCandidateComponents("com.example.external");
//
//        for (BeanDefinition component : components) {
//            try {
//                Class<?> clazz = Class.forName(component.getClass().getName());
//                System.out.println("Registering external component: " + clazz.getName());
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}