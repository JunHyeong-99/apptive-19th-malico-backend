package com.apptive.marico.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.apptive.marico.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.apptive.marico.exception.ErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageUploadService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    @Transactional
    public String upload(MultipartFile image) {
        if(image.isEmpty()){
            throw new CustomException(PREFERRED_STYLE_IMAGE_NOT_EXIST);
        }
        return getImageUrl(image);
    }

    @Transactional
    public List<String> upload(List<MultipartFile> images) {
        if(images.isEmpty()){
            throw new CustomException(PREFERRED_STYLE_IMAGE_NOT_EXIST);
        }

        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile image : images) {
            imageUrls.add(getImageUrl(image));
        }

        return imageUrls;
    }

    private String getImageUrl(MultipartFile image) {
        String mediaName = createFileName(image.getOriginalFilename()); // 각 파일의 이름을 저장

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(image.getSize());
        objectMetadata.setContentType(image.getContentType());

        System.out.println("for each 진입 : " + mediaName);

        try (InputStream inputStream = image.getInputStream()) {
            // S3에 업로드 및 저장
            amazonS3.putObject(new PutObjectRequest(bucket, mediaName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new CustomException(FILE_UPLOAD_ERROR);
        }
        // 접근가능한 URL 가져오기
        String imageUrl = amazonS3.getUrl(bucket, mediaName).toString();
        return imageUrl;
    }

    public String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단하였습니다.
    public String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }



}
