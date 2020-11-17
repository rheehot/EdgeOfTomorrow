package com.oracle.eot.storage;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

	void init();

	String store(MultipartFile file);

	String store(String prefix, MultipartFile file);

	String store(String prefix, String filename, MultipartFile file);
	
	String store(String prefix, String filename);

	Stream<Path> loadAll();

	Path load(String filename);

	Resource loadAsResource(String filename);

	void deleteAll();

}
