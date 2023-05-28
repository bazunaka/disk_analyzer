/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bazunaka.disk_analyze;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author bazunaka
 */
public class Analyzer {
    private HashMap<String, Long> sizes;
    
    public Map<String, Long> calculateDirectorySize(Path path) { //можно сделать дерево 
        try {
            sizes = new HashMap<>();
            //обход дерева - путь, визитор(доступ к файлам и директориям, к которым может не быть доступа у пользователя)
            Files.walkFileTree(path, 
                    new SimpleFileVisitor<>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            long size = Files.size(file);
                            updateDirSize(file, size);
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                            return FileVisitResult.SKIP_SUBTREE;
                        }
                    }
            );
            return sizes;
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    //рекурсия
    private void updateDirSize(Path path, Long size) {
        String key = path.toString();
        sizes.put(key, size + sizes.getOrDefault(key, 0L));

        //обновление вышестоящих директорий
        Path parent = path.getParent();

        if (parent != null) {
            updateDirSize(parent, size);
        }
    }
}
