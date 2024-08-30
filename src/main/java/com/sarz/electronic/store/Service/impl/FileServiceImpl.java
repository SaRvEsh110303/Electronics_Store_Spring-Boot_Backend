package com.sarz.electronic.store.Service.impl;

import com.sarz.electronic.store.Service.FileService;
import com.sarz.electronic.store.entities.Category;
import com.sarz.electronic.store.entities.Product;
import com.sarz.electronic.store.entities.User;
import com.sarz.electronic.store.exceptions.BadApiRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    User user=new User();
    Category category = new Category();

    Product product = new Product();
    Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String uploadImage(MultipartFile file, String path) throws IOException {
        String originalFilename = file.getOriginalFilename();
        logger.info("Original File Name : {}",originalFilename);

        // to avoid randomness of the file
        String fileName= UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        logger.info("File Name Extension: {} ",extension);
        String fileNameWithExtension = fileName+extension;
        logger.info("File Name : {}",fileNameWithExtension);
        String fullPathWithFileName=path+fileNameWithExtension;
        logger.info("Full Path With file Name : {}",fullPathWithFileName);

        if(extension.equalsIgnoreCase(".png")|| extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase(".webp")){
                //Create File Folder
                File folder=new File(path);
                if(!folder.exists()){
                    folder.mkdirs();
                }

                //UPLOAD
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
                return fileNameWithExtension;

        }else {
            throw new BadApiRequest("File With this "+extension+" not allowed!!");

        }
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath= path +File.separator+name;
        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }
}
