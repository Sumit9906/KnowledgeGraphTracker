package com.kg.extractor;

import com.kg.graph.GraphStore;
import com.kg.graph.NodeType;

import java.io.File;

public class FileExtractor {

    public static void extract(File file, GraphStore graph) throws Exception {

        // Use relative path as File node ID
        String fileId = file.getCanonicalPath();

        graph.addNode(NodeType.FILE, fileId,fileId);
    }
}
