package org.rij.minecraft.PineTreePlugin.PineTree.REST;

import org.rij.minecraft.PineTree.Annotations.GetMapping;
import org.rij.minecraft.PineTree.Annotations.PostMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class Spark {
    public static class Cached{
        public Cached(Method m, Object i) {
            this.m = m;
            this.i = i;
        }

        public Method m;
        public Object i;
    }

    private static boolean isInitialized = false;

    private static Map<String, Cached> Methods = new HashMap<>();
    private static final List<Cached> GetwaitingFonInitialization = new LinkedList<>();
    private static final List<Cached> PostwaitingFonInitialization = new LinkedList<>();


    public static void addGetEndpoint(Method m){
        try {
            String path = m.getAnnotation(GetMapping.class).value();

            Methods.put(path, new Cached(m, m.getDeclaringClass().getDeclaredConstructor().newInstance()));

            get(path, (req, res) -> {
                Cached c = Methods.get(req.uri());

                c.m.invoke(c.i, req, res);
                return res.body();
            });
        } catch (Exception any) {
            throw new RuntimeException(any);
        }
    }
    public static void addGetEndpoint(Object obj, Method m){
        if(!isInitialized){
            GetwaitingFonInitialization.add(new Cached(m, obj));
            return;
        }



        try {
            String path = m.getAnnotation(GetMapping.class).value();
            System.out.println("GET: " + path);

            get(path, (req, res) -> {
                m.invoke(obj, req, res);
                return res.body();
            });
        } catch (Exception any) {
            throw new RuntimeException(any);
        }
    }
    public static void addPostEndpoint(Method m){
        try {
            String path = m.getAnnotation(PostMapping.class).value();

            Methods.put(path, new Cached(m, m.getDeclaringClass().getDeclaredConstructor().newInstance()));

            post(path, (req, res) -> {
                Cached c = Methods.get(req.uri());
                c.m.invoke(c.i, req, res);
                return res.body();
            });
        } catch (Exception any) {
            throw new RuntimeException(any);
        }
    }
    public static void addPostEndpoint(Object obj, Method m){
        if(!isInitialized){
            PostwaitingFonInitialization.add(new Cached(m, obj));
            return;
        }

        try {
            String path = m.getAnnotation(PostMapping.class).value();
            System.out.println("POST: " + path);

            post(path, (req, res) -> {
                m.invoke(obj, req, res);
                return res.body();
            });
        } catch (Exception any) {
            throw new RuntimeException(any);
        }
    }


    public static void startandAwait(int port){
        init(port);
        awaitInitialization();
        isInitialized = true;

        GetwaitingFonInitialization.forEach(cached -> {
            addGetEndpoint(cached.i, cached.m);
        });
        PostwaitingFonInitialization.forEach(cached -> {
            addPostEndpoint(cached.i, cached.m);
        });
    }

    public static void init(int port){
        port(port);


        get("/test", (req, res) -> {
            return 200;
        });


    }

}
