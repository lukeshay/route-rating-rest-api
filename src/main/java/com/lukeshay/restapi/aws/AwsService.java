package com.lukeshay.restapi.aws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface AwsService {
	Logger LOG = LoggerFactory.getLogger(AwsService.class.getName());

	File convertFile(MultipartFile file);

	String uploadFileToS3(String fileName, MultipartFile file);
}
