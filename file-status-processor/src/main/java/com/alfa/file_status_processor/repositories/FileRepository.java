package com.alfa.file_status_processor.repositories;

import com.alfa.file_status_processor.models.File;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends MongoRepository<File, String> {

}