package com.jema.cinema.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserProfilePictureService {
    @Value("${upload.path}")
    private String uploadPath;

    @PostConstruct
    public void init(){
        try{
            Files.createDirectories(Paths.get(uploadPath));
        }catch (IOException e){
            throw new RuntimeException("Could not create upload Folder!");
        }
    }

    public void saveProfilePicture(MultipartFile profilePicture){
        try{
            Path root = Paths.get(uploadPath);
            if(!Files.exists(root)){
                init();
            }
            String fileExtension = Objects.requireNonNull(profilePicture.getOriginalFilename()).split("\\.")[1];
            if(fileExtension.equals("jpg") ||
                fileExtension.equals("jpeg") ||
                fileExtension.equals("png")){
                File tmp = new File(uploadPath + "\\" +profilePicture.getOriginalFilename());
                if(tmp.exists()){
                    String fileName = UUID.randomUUID().toString() + "." + fileExtension;
                    byte[] bytes = profilePicture.getBytes();
                    String path = uploadPath + "\\" + fileName;
                    Files.write(Paths.get(path), bytes);
                }else{
                    Files.copy(profilePicture.getInputStream(),
                            root.resolve(profilePicture.getOriginalFilename()));
                }
            }else{
                throw new RuntimeException("only jpg, jpeg and png pictures allowed");
            }
        }catch(Exception e){
            throw new RuntimeException("Could not store the file. Error");
        }
    }
}
