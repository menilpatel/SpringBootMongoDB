package com.springmongo.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadServices {
	@Value("")
	private Path fileStorageLocation;

	@Value("${upload.path}")
	private String uploadPath;

	@PostConstruct
	public void init() {
		this.fileStorageLocation = Paths.get(uploadPath).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			System.out.println(
					"Could not create the directory where the uploaded files will be stored." + ex.getMessage());
		}
	}

	public Boolean createDir(Path location) {
		try {
			Files.createDirectories(location);
			return true;
		} catch (Exception ex) {
			System.out.println(
					"Could not create the directory where the uploaded files will be stored." + ex.getMessage());
			return false;
		}
	}

	public String uploadFile(MultipartFile file, String filename) {

		Date date = new Date();

		String fileName = StringUtils.cleanPath(
				filename + "_" + date.getTime() + '.' + FilenameUtils.getExtension(file.getOriginalFilename()));
		try {
			if (fileName.contains("..")) {
				System.out.println("Sorry! Filename contains invalid path sequence " + fileName);
			}
			String location = uploadPath + File.separator + filename;

			this.fileStorageLocation = Paths.get(location).toAbsolutePath().normalize();

			try {
				this.createDir(this.fileStorageLocation);
			} catch (Exception ex) {
				return null;
			}
			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			if (Files.exists(targetLocation)) {
				// Delete the existing file
				Files.delete(targetLocation);
			}
			Files.createFile(targetLocation);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return fileName;
		} catch (IOException ex) {
			System.out.println("Could not store file " + fileName + ". Please try again!" + ex.getMessage());
			return null;
		}
	}

	public Resource loadFileAsResource(String fileName, String shopid, String listingid) {
		try {

			this.fileStorageLocation = Paths.get(uploadPath + File.separator + shopid + File.separator + listingid)
					.toAbsolutePath().normalize();

			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				System.out.println("File not found " + fileName);
				return null;
			}
		} catch (MalformedURLException ex) {
			System.out.println("File not found " + fileName + ex.getMessage());
			return null;
		}
	}
}
